package net.bluethedude.sculkevolution.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class BladedHookBlock extends Block implements Waterloggable {

    public static final MapCodec<BladedHookBlock> CODEC = createCodec(BladedHookBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty HANGING = Properties.HANGING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape BASE_STANDING_SHAPE = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 4.0, 10.0);
    private static final VoxelShape BASE_HANGING_SHAPE = Block.createCuboidShape(6.0, 12.0, 6.0, 10.0, 16.0, 10.0);

    private static final VoxelShape NORTH_SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(7.0, 4.0, 3.0, 9.0, 16.0, 13.0),
            BASE_STANDING_SHAPE
    );
    private static final VoxelShape EAST_WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3.0, 4.0, 7.0, 13.0, 16.0, 9.0),
            BASE_STANDING_SHAPE
    );
    private static final VoxelShape NORTH_SOUTH_HANGING_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(7.0, 0.0, 3.0, 9.0, 12.0, 13.0),
            BASE_HANGING_SHAPE
    );
    private static final VoxelShape EAST_WEST_HANGING_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3.0, 0.0, 7.0, 13.0, 12.0, 9.0),
            BASE_HANGING_SHAPE
    );

    @Override
    public MapCodec<BladedHookBlock> getCodec() {
        return CODEC;
    }

    public BladedHookBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HANGING, false)
                .with(WATERLOGGED, false)
        );
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockState = this.getDefaultState()
                        .with(HANGING, direction == Direction.UP);
                if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                    return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(FACING, ctx.getHorizontalPlayerFacing());
                }
            }
        }

        return null;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HANGING, WATERLOGGED);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = attachedDirection(state).getOpposite();
        return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
    }

    protected static Direction attachedDirection(BlockState state) {
        return state.get(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return attachedDirection(state).getOpposite() == direction && !state.canPlaceAt(world, pos)
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> state.get(HANGING) ? NORTH_SOUTH_HANGING_SHAPE : NORTH_SOUTH_SHAPE;
            case EAST, WEST -> state.get(HANGING) ? EAST_WEST_HANGING_SHAPE : EAST_WEST_SHAPE;
            default -> state.get(HANGING) ? BASE_HANGING_SHAPE : BASE_STANDING_SHAPE;
        };
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
