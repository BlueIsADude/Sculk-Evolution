package net.bluethedude.sculkevolution.item.custom;

import net.bluethedude.sculkevolution.enchantment.SculkEnchantments;
import net.bluethedude.sculkevolution.sound.SculkSoundEvents;
import net.bluethedude.sculkevolution.util.SculkDataComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SickleItem extends Item {

    private boolean sculkLunging;
    private int sculkLungeTicks;

    public SickleItem(Settings settings) {
        super(settings);
    }

    public int getSculkCharge(ItemStack stack) {
        if (stack.contains(SculkDataComponents.SCULK_CHARGE)) {
            return Objects.requireNonNull(stack.get(SculkDataComponents.SCULK_CHARGE));
        } else {
            return 0;
        }
    }

    public void setSculkCharge(ItemStack stack, int sculkCharge) {
        stack.set(SculkDataComponents.SCULK_CHARGE, sculkCharge);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 0.0F, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -1.0F, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    public static ToolComponent createToolComponent() {
        return new ToolComponent(List.of(
                ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 15.0F),
                ToolComponent.Rule.of(BlockTags.SWORD_EFFICIENT, 5.0F),
                ToolComponent.Rule.ofAlwaysDropping(BlockTags.HOE_MINEABLE, 6.0F)), 1.0F, 1);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    public static boolean hasEnchantment(ItemStack stack, RegistryKey<Enchantment> enchantment) {
        return stack.getEnchantments().getEnchantments().toString().
                contains(enchantment.getValue().toString());
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        if (hasEnchantment(stack, SculkEnchantments.ECHO_CHAMBER)) {
            return 35;
        } else {
            return 0;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hasEnchantment(stack, SculkEnchantments.ECHO_CHAMBER)) {
            if (getSculkCharge(stack) >= 3) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SculkSoundEvents.SICKLE_SONIC_CHARGE,
                        SoundCategory.PLAYERS,
                        3.0F,
                        1.0F
                );
                user.setCurrentHand(hand);
                return TypedActionResult.consume(stack);
            }
        }
        if (hasEnchantment(stack, SculkEnchantments.SOUL_LUNGE)) {
            if (getSculkCharge(stack) >= 3 && !user.isFallFlying()) {
                Vec3d userRotation = user.getRotationVector();
                Vec3d userVelocity = user.getVelocity();
                user.setVelocity(
                        userVelocity.add(
                                userRotation.x * 0.1 + (userRotation.x * 2.5 - userVelocity.x) * 0.5,
                                0.1,
                                userRotation.z * 0.1 + (userRotation.z * 2.5 - userVelocity.z) * 0.5
                        )
                );
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SculkSoundEvents.SICKLE_SONIC_BOOM,
                        SoundCategory.PLAYERS,
                        3.0F,
                        3.0F
                );

                sculkLunging = true;
                user.getItemCooldownManager().set(this, 32);
                stack.damage(1, user, EquipmentSlot.MAINHAND);
                setSculkCharge(stack, getSculkCharge(stack) - 3);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (hasEnchantment(stack, SculkEnchantments.ECHO_CHAMBER)) {
            if (user instanceof PlayerEntity playerEntity && getSculkCharge(stack) >= 3) {
                if(!world.isClient) {
                    spawnSonicBoom(stack, world, user);
                }
                if (getSculkCharge(stack) >= 9) {
                    playerEntity.getItemCooldownManager().set(this, 128);
                    stack.damage(15, playerEntity, EquipmentSlot.MAINHAND);
                } else if (getSculkCharge(stack) >= 6) {
                    playerEntity.getItemCooldownManager().set(this, 112);
                    stack.damage(10, playerEntity, EquipmentSlot.MAINHAND);
                } else {
                    playerEntity.getItemCooldownManager().set(this, 96);
                    stack.damage(5, playerEntity, EquipmentSlot.MAINHAND);
                }
                setSculkCharge(stack, 0);
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof LivingEntity livingEntity) {
            if(sculkLunging) {
                if (!world.isClient) {
                    ((ServerWorld) world).spawnParticles(
                            ParticleTypes.SCULK_SOUL,
                            livingEntity.getX(),
                            (livingEntity.getY() + livingEntity.getEyeY()) / 2,
                            livingEntity.getZ(),
                            1, 0.3, 0.3, 0.3, 0
                    );
                    ((ServerWorld) world).spawnParticles(
                            ParticleTypes.SOUL_FIRE_FLAME,
                            livingEntity.getX(),
                            (livingEntity.getY() + livingEntity.getEyeY()) / 2,
                            livingEntity.getZ(),
                            2, 0.3, 0.3, 0.3, 0
                    );
                }
                sculkLungeTicks++;
                if (sculkLungeTicks % 36 == 0 || livingEntity.hurtTime != 0) {
                    sculkLunging = false;
                }
            }
            if(selected || slot == 99) {
                if (livingEntity.hurtTime != 0 && getSculkCharge(stack) != 0) {
                    world.playSound(
                            null,
                            livingEntity.getX(),
                            livingEntity.getY(),
                            livingEntity.getZ(),
                            SculkSoundEvents.SICKLE_RESET,
                            SoundCategory.PLAYERS,
                            1.5F,
                            1.25F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
                    );
                    if (livingEntity instanceof PlayerEntity user) {
                        if (getSculkCharge(stack) >= 3 && hasEnchantment(stack, SculkEnchantments.REPULSE)) {
                            if (!world.isClient) {
                                Set<Entity> hit = new HashSet<>();
                                hit.addAll(world.getEntitiesByClass(LivingEntity.class, new Box(new BlockPos(
                                                (int) user.getX(), (int) user.getY(), (int) user.getZ())).expand(1),
                                        it -> !(it instanceof TameableEntity tamed && tamed.isOwner(user)))
                                );
                                hit.remove(user);
                                for (Entity hitTarget : hit) {
                                    if(hitTarget instanceof LivingEntity mob) {
                                        Vec3d target = user.getPos().subtract(mob.getPos());
                                        Vec3d knockback = target.normalize().multiply((getSculkCharge(stack)) * -0.2);
                                        mob.damage(world.getDamageSources().sonicBoom(user), 2.0f);
                                        mob.addVelocity(knockback.x, 0.0F, knockback.z);
                                    }
                                }
                                ((ServerWorld) world).spawnParticles(
                                        ParticleTypes.SONIC_BOOM,
                                        user.getX(),
                                        user.getY(),
                                        user.getZ(),
                                        1, 0.0, 0.0, 0.0, 0.0
                                );
                            }
                            world.playSound(
                                    null,
                                    user.getX(),
                                    user.getY(),
                                    user.getZ(),
                                    SculkSoundEvents.SICKLE_SONIC_BOOM,
                                    SoundCategory.PLAYERS,
                                    3.0F,
                                    3.0F
                            );
                        }
                        if (getSculkCharge(stack) >= 9) {
                            user.getItemCooldownManager().set(this, 16);
                        } else if (getSculkCharge(stack) >= 6) {
                            user.getItemCooldownManager().set(this, 12);
                        } else if (getSculkCharge(stack) >= 3) {
                            user.getItemCooldownManager().set(this, 8);
                        }
                    }
                    setSculkCharge(stack, 0);
                }
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        if (attacker instanceof PlayerEntity user && !user.getItemCooldownManager().isCoolingDown(this)) {
            if (getSculkCharge(stack) < 9) {
                if (!world.isClient) {
                    setSculkCharge(stack, getSculkCharge(stack) + 1);
                }
                if (getSculkCharge(stack) == 3 || getSculkCharge(stack) == 6 || getSculkCharge(stack) == 9) {
                    world.playSound(
                            null,
                            attacker.getX(),
                            attacker.getY(),
                            attacker.getZ(),
                            SculkSoundEvents.SICKLE_GROW_LARGE,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.25F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
                    );
                } else {
                    world.playSound(
                            null,
                            attacker.getX(),
                            attacker.getY(),
                            attacker.getZ(),
                            SculkSoundEvents.SICKLE_GROW_SMALL,
                            SoundCategory.PLAYERS,
                            5.0F,
                            1.25F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
                    );
                }
            }
        }
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (damageSource.getSource() instanceof PlayerEntity player) {
            ItemStack stack = player.getMainHandStack();
            if (stack.isOf(this)) {
                return (float) getSculkCharge(stack);
            } else {
                return 0.0F;
            }
        } else {
            return 0.0F;
        }
    }

    private void spawnSonicBoom(ItemStack stack, World world, LivingEntity user) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SculkSoundEvents.SICKLE_SONIC_BOOM, SoundCategory.PLAYERS, 3.0f, 1.0f);

        float heightOffset = user.getStandingEyeHeight();
        int distance = 8;
        if (getSculkCharge(stack) >= 9) {
            distance = 12;
        } else if (getSculkCharge(stack) >= 6) {
            distance = 10;
        }

        Vec3d source = user.getPos().add(0.0, heightOffset, 0.0);
        Vec3d target = source.add(user.getRotationVector().multiply(distance));
        Vec3d offset = target.subtract(source);
        Vec3d normalized = offset.normalize();

        Set<Entity> hit = new HashSet<>();
        for (int particleIndex = 1; particleIndex < MathHelper.floor(offset.length()); ++particleIndex) {
            Vec3d particlePos = source.add(normalized.multiply(particleIndex));
            ((ServerWorld) world).spawnParticles(ParticleTypes.SONIC_BOOM, particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);

            hit.addAll(world.getEntitiesByClass(LivingEntity.class, new Box(new BlockPos(
                    (int) particlePos.getX(), (int) particlePos.getY(), (int) particlePos.getZ())).expand(1),
                    it -> !(it instanceof TameableEntity tamed && tamed.isOwner(user)))
            );
        }

        hit.remove(user);

        for (Entity hitTarget : hit) {
            if(hitTarget instanceof LivingEntity mob) {
                mob.damage(world.getDamageSources().sonicBoom(user), 1.0f + getSculkCharge(stack));
                double vertical = (0.5 - mob.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                double horizontal = 2.5 * (1.0 - mob.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                mob.addVelocity(normalized.getX() * horizontal, normalized.getY() * vertical, normalized.getZ() * horizontal);
            }
        }
    }

    @Override
    public UseAction getUseAction (ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.ECHO_SHARD);
    }
}