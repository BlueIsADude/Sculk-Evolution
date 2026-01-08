package net.bluethedude.sculkevolution.block.entity.custom;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.bluethedude.sculkevolution.block.custom.UmbraVaultBlock;
import net.bluethedude.sculkevolution.block.entity.SculkBlockEntities;
import net.bluethedude.sculkevolution.block.util.*;
import net.bluethedude.sculkevolution.particle.SculkParticleTypes;
import net.bluethedude.sculkevolution.sound.SculkSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VaultBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UmbraVaultBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final UmbraVaultServerData serverData = new UmbraVaultServerData();
    private final UmbraVaultSharedData sharedData = new UmbraVaultSharedData();
    private final UmbraVaultClientData clientData = new UmbraVaultClientData();
    private UmbraVaultConfig config = UmbraVaultConfig.DEFAULT;

    public UmbraVaultBlockEntity(BlockPos pos, BlockState state) {
        super(SculkBlockEntities.UMBRA_VAULT_BE, pos, state);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return Util.make(new NbtCompound(), nbt -> nbt.put("shared_data", encodeValue(UmbraVaultSharedData.codec, this.sharedData, registryLookup)));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.put("config", encodeValue(UmbraVaultConfig.codec, this.config, registryLookup));
        nbt.put("shared_data", encodeValue(UmbraVaultSharedData.codec, this.sharedData, registryLookup));
        nbt.put("server_data", encodeValue(UmbraVaultServerData.codec, this.serverData, registryLookup));
    }

    private static <T> NbtElement encodeValue(Codec<T> codec, T value, RegistryWrapper.WrapperLookup registries) {
        return codec.encodeStart(registries.getOps(NbtOps.INSTANCE), value).getOrThrow();
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        DynamicOps<NbtElement> dynamicOps = registryLookup.getOps(NbtOps.INSTANCE);
        if (nbt.contains("server_data")) {
            UmbraVaultServerData.codec.parse(dynamicOps, nbt.get("server_data")).resultOrPartial(LOGGER::error).ifPresent(this.serverData::copyFrom);
        }

        if (nbt.contains("config")) {
            UmbraVaultConfig.codec.parse(dynamicOps, nbt.get("config")).resultOrPartial(LOGGER::error).ifPresent(config -> this.config = config);
        }

        if (nbt.contains("shared_data")) {
            UmbraVaultSharedData.codec.parse(dynamicOps, nbt.get("shared_data")).resultOrPartial(LOGGER::error).ifPresent(this.sharedData::copyFrom);
        }
    }

    @Nullable
    public UmbraVaultServerData getServerData() {
        return this.world != null && !this.world.isClient ? this.serverData : null;
    }

    public UmbraVaultSharedData getSharedData() {
        return this.sharedData;
    }

    public UmbraVaultClientData getClientData() {
        return this.clientData;
    }

    public UmbraVaultConfig getConfig() {
        return this.config;
    }

    public static final class Client {

        public static void tick(World world, BlockPos pos, BlockState state, UmbraVaultClientData clientData, UmbraVaultSharedData sharedData) {
            clientData.rotateDisplay();
            if (world.getTime() % 20L == 0L) {
                spawnConnectedParticles(world, pos, state, sharedData);
            }

            spawnAmbientParticles(world, pos, sharedData);
            playAmbientSound(world, pos, sharedData);
        }

        public static void spawnActivateParticles(World world, BlockPos pos, BlockState state, UmbraVaultSharedData sharedData, ParticleEffect particle) {
            spawnConnectedParticles(world, pos, state, sharedData);
            Random random = world.random;

            for (int i = 0; i < 20; i++) {
                Vec3d vec3d = getRegularParticlesPos(pos, random);
                world.addParticle(ParticleTypes.SMOKE, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
                world.addParticle(particle, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
            }
        }

        public static void spawnDeactivateParticles(World world, BlockPos pos, ParticleEffect particle) {
            Random random = world.random;

            for (int i = 0; i < 20; i++) {
                Vec3d vec3d = getDeactivateParticlesPos(pos, random);
                Vec3d vec3d2 = new Vec3d(random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02);
                world.addParticle(particle, vec3d.getX(), vec3d.getY(), vec3d.getZ(), vec3d2.getX(), vec3d2.getY(), vec3d2.getZ());
            }
        }

        private static void spawnAmbientParticles(World world, BlockPos pos, UmbraVaultSharedData sharedData) {
            Random random = world.getRandom();
            if (random.nextFloat() <= 0.5F) {
                Vec3d vec3d = getRegularParticlesPos(pos, random);
                world.addParticle(ParticleTypes.SMOKE, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
                if (hasDisplayItem(sharedData)) {
                    world.addParticle(ParticleTypes.SCULK_CHARGE_POP, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.0, 0.0, 0.0);
                }
            }
        }

        private static void spawnConnectedParticlesFor(World world, Vec3d pos, PlayerEntity player) {
            Random random = world.random;
            Vec3d vec3d = pos.relativize(player.getPos().add(0.0, player.getHeight() / 2.0F, 0.0));
            int i = MathHelper.nextInt(random, 2, 5);

            for (int j = 0; j < i; j++) {
                Vec3d vec3d2 = vec3d.addRandom(random, 1.0F);
                world.addParticle(SculkParticleTypes.UMBRA_VAULT_CONNECTION, pos.getX(), pos.getY(), pos.getZ(), vec3d2.getX(), vec3d2.getY(), vec3d2.getZ());
            }
        }

        private static void spawnConnectedParticles(World world, BlockPos pos, BlockState state, UmbraVaultSharedData sharedData) {
            Set<UUID> set = sharedData.getConnectedPlayers();
            if (!set.isEmpty()) {
                Vec3d vec3d = getConnectedParticlesOrigin(pos, state.get(VaultBlock.FACING));

                for (UUID uUID : set) {
                    PlayerEntity playerEntity = world.getPlayerByUuid(uUID);
                    if (playerEntity != null && isPlayerWithinConnectedParticlesRange(pos, sharedData, playerEntity)) {
                        spawnConnectedParticlesFor(world, vec3d, playerEntity);
                    }
                }
            }
        }

        private static boolean isPlayerWithinConnectedParticlesRange(BlockPos pos, UmbraVaultSharedData sharedData, PlayerEntity player) {
            return player.getBlockPos().getSquaredDistance(pos) <= MathHelper.square(sharedData.getConnectedParticlesRange());
        }

        private static void playAmbientSound(World world, BlockPos pos, UmbraVaultSharedData sharedData) {
            if (hasDisplayItem(sharedData)) {
                Random random = world.getRandom();
                if (random.nextFloat() <= 0.01F) {
                    world.playSoundAtBlockCenter(
                            pos, SculkSoundEvents.BLOCK_UMBRA_VAULT_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false
                    );
                }
            }
        }

        public static boolean hasDisplayItem(UmbraVaultSharedData sharedData) {
            return sharedData.hasDisplayItem();
        }

        private static Vec3d getDeactivateParticlesPos(BlockPos pos, Random random) {
            return Vec3d.of(pos).add(MathHelper.nextDouble(random, 0.4, 0.6), MathHelper.nextDouble(random, 0.4, 0.6), MathHelper.nextDouble(random, 0.4, 0.6));
        }

        private static Vec3d getRegularParticlesPos(BlockPos pos, Random random) {
            return Vec3d.of(pos).add(MathHelper.nextDouble(random, 0.1, 0.9), MathHelper.nextDouble(random, 0.25, 0.75), MathHelper.nextDouble(random, 0.1, 0.9));
        }

        private static Vec3d getConnectedParticlesOrigin(BlockPos pos, Direction direction) {
            return Vec3d.ofBottomCenter(pos).add((double)direction.getOffsetX() * 0.5, 2.0, (double)direction.getOffsetZ() * 0.5);
        }
    }

    public static final class Server {

        public static void tick(ServerWorld world, BlockPos pos, BlockState state, UmbraVaultConfig config, UmbraVaultServerData serverData, UmbraVaultSharedData sharedData) {
            UmbraVaultState vaultState = state.get(UmbraVaultBlock.UMBRA_VAULT_STATE);
            if (shouldUpdateDisplayItem(world.getTime(), vaultState)) {
                updateDisplayItem(world, vaultState, config, sharedData, pos);
            }

            BlockState blockState = state;
            if (world.getTime() >= serverData.getStateUpdatingResumeTime()) {
                blockState = state.with(UmbraVaultBlock.UMBRA_VAULT_STATE, vaultState.update(world, pos, config, serverData, sharedData));
                if (!state.equals(blockState)) {
                    changeVaultState(world, pos, state, blockState, config, sharedData);
                }
            }

            if (serverData.dirty || sharedData.dirty) {
                UmbraVaultBlockEntity.markDirty(world, pos, state);
                if (sharedData.dirty) {
                    world.updateListeners(pos, state, blockState, Block.NOTIFY_LISTENERS);
                }

                serverData.dirty = false;
                sharedData.dirty = false;
            }
        }

        public static void tryUnlock(
                ServerWorld world,
                BlockPos pos,
                BlockState state,
                UmbraVaultConfig config,
                UmbraVaultServerData serverData,
                UmbraVaultSharedData sharedData,
                PlayerEntity player,
                ItemStack stack
        ) {
            UmbraVaultState vaultState = state.get(UmbraVaultBlock.UMBRA_VAULT_STATE);
            if (canBeUnlocked(config, vaultState)) {
                if (!isValidKey(config, stack)) {
                    playFailedUnlockSound(world, serverData, pos);
                } else {
                    List<ItemStack> list = generateLoot(world, config, pos, player);
                    if (!list.isEmpty()) {
                        player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                        stack.decrementUnlessCreative(config.keyItem().getCount(), player);
                        unlock(world, state, pos, config, serverData, sharedData, list);
                        sharedData.updateConnectedPlayers(world, pos, config, config.deactivationRange());
                    }
                }
            }
        }

        static void changeVaultState(ServerWorld world, BlockPos pos, BlockState oldState, BlockState newState, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
            UmbraVaultState vaultState = oldState.get(UmbraVaultBlock.UMBRA_VAULT_STATE);
            UmbraVaultState vaultState2 = newState.get(UmbraVaultBlock.UMBRA_VAULT_STATE);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);
            vaultState.onStateChange(world, pos, vaultState2, config, sharedData);
        }

        public static void updateDisplayItem(ServerWorld world, UmbraVaultState state, UmbraVaultConfig config, UmbraVaultSharedData sharedData, BlockPos pos) {
            if (!canBeUnlocked(config, state)) {
                sharedData.setDisplayItem(ItemStack.EMPTY);
            } else {
                ItemStack itemStack = generateDisplayItem(world, pos, config.overrideLootTableToDisplay().orElse(config.lootTable()));
                sharedData.setDisplayItem(itemStack);
            }
        }

        private static ItemStack generateDisplayItem(ServerWorld world, BlockPos pos, RegistryKey<LootTable> lootTable) {
            LootTable lootTable2 = world.getServer().getReloadableRegistries().getLootTable(lootTable);
            LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(world).add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).build(LootContextTypes.VAULT);
            List<ItemStack> list = lootTable2.generateLoot(lootContextParameterSet, world.getRandom());
            return list.isEmpty() ? ItemStack.EMPTY : Util.getRandom(list, world.getRandom());
        }

        private static void unlock(
                ServerWorld world, BlockState state, BlockPos pos, UmbraVaultConfig config, UmbraVaultServerData serverData, UmbraVaultSharedData sharedData, List<ItemStack> itemsToEject
        ) {
            serverData.setItemsToEject(itemsToEject);
            sharedData.setDisplayItem(serverData.getItemToDisplay());
            serverData.setStateUpdatingResumeTime(world.getTime() + 14L);
            changeVaultState(world, pos, state, state.with(UmbraVaultBlock.UMBRA_VAULT_STATE, UmbraVaultState.UNLOCKING), config, sharedData);
        }

        private static List<ItemStack> generateLoot(ServerWorld world, UmbraVaultConfig config, BlockPos pos, PlayerEntity player) {
            LootTable lootTable = world.getServer().getReloadableRegistries().getLootTable(config.lootTable());
            LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(world)
                    .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                    .luck(player.getLuck())
                    .add(LootContextParameters.THIS_ENTITY, player)
                    .build(LootContextTypes.VAULT);
            return lootTable.generateLoot(lootContextParameterSet);
        }

        private static boolean canBeUnlocked(UmbraVaultConfig config, UmbraVaultState state) {
            return config.lootTable() != LootTables.EMPTY && !config.keyItem().isEmpty() && state != UmbraVaultState.INACTIVE;
        }

        private static boolean isValidKey(UmbraVaultConfig config, ItemStack stack) {
            return ItemStack.areItemsAndComponentsEqual(stack, config.keyItem()) && stack.getCount() >= config.keyItem().getCount();
        }

        private static boolean shouldUpdateDisplayItem(long time, UmbraVaultState state) {
            return time % 20L == 0L && state == UmbraVaultState.ACTIVE;
        }

        private static void playFailedUnlockSound(ServerWorld world, UmbraVaultServerData serverData, BlockPos pos) {
            if (world.getTime() >= serverData.getLastFailedUnlockTime() + 15L) {
                world.playSound(null, pos, SculkSoundEvents.BLOCK_UMBRA_VAULT_INSERT_ITEM_FAIL, SoundCategory.BLOCKS);
                serverData.setLastFailedUnlockTime(world.getTime());
            }
        }
    }
}

