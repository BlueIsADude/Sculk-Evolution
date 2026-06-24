package net.bluethedude.sculkevolution.block.entity.custom;

import net.bluethedude.sculkevolution.block.entity.SculkBlockEntities;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkCatalystBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Nullables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

public class CalibratedSculkCatalystBlockEntity extends BlockEntity implements GameEventListener.Holder<CalibratedSculkCatalystBlockEntity.Listener> {
    private final CalibratedSculkCatalystBlockEntity.Listener eventListener;

    public CalibratedSculkCatalystBlockEntity(BlockPos pos, BlockState state) {
        super(SculkBlockEntities.CALIBRATED_SCULK_CATALYST, pos, state);
        this.eventListener = new CalibratedSculkCatalystBlockEntity.Listener(state, new BlockPositionSource(pos));
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
    }

    public CalibratedSculkCatalystBlockEntity.Listener getEventListener() {
        return this.eventListener;
    }

    public static class Listener implements GameEventListener {
        public static final int RANGE = 8;
        private final BlockState state;
        private final PositionSource positionSource;

        public Listener(BlockState state, PositionSource positionSource) {
            this.state = state;
            this.positionSource = positionSource;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public int getRange() {
            return RANGE;
        }

        @Override
        public GameEventListener.TriggerOrder getTriggerOrder() {
            return GameEventListener.TriggerOrder.BY_DISTANCE;
        }

        @Override
        public boolean listen(ServerWorld world, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event.matches(GameEvent.ENTITY_DIE) && emitter.sourceEntity() instanceof LivingEntity livingEntity) {
                if (!livingEntity.isExperienceDroppingDisabled()) {
                    DamageSource damageSource = livingEntity.getRecentDamageSource();
                    int i = livingEntity.getXpToDrop(world, Nullables.map(damageSource, DamageSource::getAttacker));
                    if (livingEntity.shouldDropXp() && i > 0) {
                        this.triggerCriteria(world, livingEntity);
                    }

                    livingEntity.disableExperienceDropping();
                    this.positionSource.getPos(world).ifPresent(pos -> this.bloom(world, BlockPos.ofFloored(pos), this.state, world.getRandom()));
                }
                return true;
            } else {
                return false;
            }
        }

        private void bloom(ServerWorld world, BlockPos pos, BlockState state, Random random) {
            world.setBlockState(pos, state.with(SculkCatalystBlock.BLOOM, true), Block.NOTIFY_ALL);
            world.scheduleBlockTick(pos, state.getBlock(), 8);
            world.spawnParticles(ParticleTypes.SCULK_SOUL, pos.getX() + 0.5, pos.getY() + 1.15, pos.getZ() + 0.5, 2, 0.2, 0.0, 0.2, 0.0);
            world.playSound(null, pos, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 2.0F, 0.6F + random.nextFloat() * 0.4F);
        }

        private void triggerCriteria(World world, LivingEntity deadEntity) {
            if (deadEntity.getAttacker() instanceof ServerPlayerEntity serverPlayerEntity) {
                DamageSource damageSource = deadEntity.getRecentDamageSource() == null
                        ? world.getDamageSources().playerAttack(serverPlayerEntity)
                        : deadEntity.getRecentDamageSource();
                Criteria.KILL_MOB_NEAR_SCULK_CATALYST.trigger(serverPlayerEntity, deadEntity, damageSource);
            }
        }
    }
}
