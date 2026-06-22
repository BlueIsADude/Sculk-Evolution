package net.bluethedude.sculkevolution.entity.custom;

import net.bluethedude.sculkevolution.entity.SculkEntityTypes;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.bluethedude.sculkevolution.sound.SculkSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class EchoArrowEntity extends PersistentProjectileEntity {

    public EchoArrowEntity(EntityType<? extends EchoArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public EchoArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(SculkEntityTypes.ECHO_ARROW, owner, world, stack, shotFrom);
    }

    public EchoArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(SculkEntityTypes.ECHO_ARROW, x, y, z, world, stack, shotFrom);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        if (this.getWorld() instanceof ServerWorld) {
            World world = this.getWorld();
            LivingEntity user = (LivingEntity) this.getOwner();

            Set<Entity> hit = new HashSet<>();
            hit.addAll(world.getEntitiesByClass(LivingEntity.class, new Box(new BlockPos(
                            (int) this.getX(), (int) this.getY(), (int) this.getZ())).expand(2),
                    it -> (it != target && it != this.getOwner() && it instanceof LivingEntity)));
            for (Entity hitTarget : hit) {
                if(hitTarget instanceof LivingEntity mob) {
                    mob.damage(world.getDamageSources().sonicBoom(user), (float) (getDamage() * 2.5F));
                }
            }
            this.playSound(SculkSoundEvents.ENTITY_ECHO_ARROW_SONIC_BOOM, 1.5F, 3.0F);
            ((ServerWorld) world).spawnParticles(
                    ParticleTypes.SONIC_BOOM,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    1, 0.0, 0.0, 0.0, 0.0
            );
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(SculkItems.ECHO_ARROW);
    }
}
