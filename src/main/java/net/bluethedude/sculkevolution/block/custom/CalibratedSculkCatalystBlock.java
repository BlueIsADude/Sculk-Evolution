package net.bluethedude.sculkevolution.block.custom;

import com.mojang.serialization.MapCodec;
import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.block.entity.custom.CalibratedSculkCatalystBlockEntity;
import net.bluethedude.sculkevolution.entity.SculkEntityTypes;
import net.bluethedude.sculkevolution.entity.custom.EchomiteEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class CalibratedSculkCatalystBlock extends BlockWithEntity {
    public static final MapCodec<CalibratedSculkCatalystBlock> CODEC = createCodec(CalibratedSculkCatalystBlock::new);
    public static final BooleanProperty BLOOM = Properties.BLOOM;
    private final IntProvider experience = ConstantIntProvider.create(5);

    @Override
    public MapCodec<CalibratedSculkCatalystBlock> getCodec() {
        return CODEC;
    }

    public CalibratedSculkCatalystBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(BLOOM, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BLOOM);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(BLOOM)) {
            BlockPos blockPos = pos.offset(Direction.UP);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = null;
            if (canGrowIn(blockState)) {
                block = SculkBlocks.SMALL_ECHO_BUD;
            } else if (blockState.isOf(SculkBlocks.SMALL_ECHO_BUD) && blockState.get(AmethystClusterBlock.FACING) == Direction.UP) {
                block = SculkBlocks.MEDIUM_ECHO_BUD;
            } else if (blockState.isOf(SculkBlocks.MEDIUM_ECHO_BUD) && blockState.get(AmethystClusterBlock.FACING) == Direction.UP) {
                block = SculkBlocks.LARGE_ECHO_BUD;
            } else if (blockState.isOf(SculkBlocks.LARGE_ECHO_BUD) && blockState.get(AmethystClusterBlock.FACING) == Direction.UP) {
                block = SculkBlocks.ECHO_CLUSTER;
            } else if (blockState.isOf(SculkBlocks.ECHO_CLUSTER) && blockState.get(AmethystClusterBlock.FACING) == Direction.UP) {
                EchomiteEntity echomiteEntity = SculkEntityTypes.ECHOMITE.create(world);
                if (echomiteEntity != null) {
                    echomiteEntity.refreshPositionAndAngles(blockPos.getX() + 0.5F, blockPos.getY() + 0.5F, blockPos.getZ() + 0.5F, 0.0F, 0.0F);
                    world.spawnEntity(echomiteEntity);
                }
            }

            if (block != null) {
                BlockState blockState2 = block.getDefaultState()
                        .with(AmethystClusterBlock.FACING, Direction.UP)
                        .with(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
                world.setBlockState(blockPos, blockState2);
            }

            world.setBlockState(pos, state.with(BLOOM, false), Block.NOTIFY_ALL);
        }
    }

    public static boolean canGrowIn(BlockState state) {
        return state.isAir() || state.isOf(Blocks.WATER) && state.getFluidState().getLevel() == 8;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CalibratedSculkCatalystBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            this.dropExperienceWhenMined(world, pos, tool, this.experience);
        }
    }
}
