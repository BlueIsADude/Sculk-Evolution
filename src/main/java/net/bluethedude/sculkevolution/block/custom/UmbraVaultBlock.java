package net.bluethedude.sculkevolution.block.custom;

import com.mojang.serialization.MapCodec;
import net.bluethedude.sculkevolution.block.entity.SculkBlockEntities;
import net.bluethedude.sculkevolution.block.entity.custom.UmbraVaultBlockEntity;
import net.bluethedude.sculkevolution.block.util.UmbraVaultState;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UmbraVaultBlock extends BlockWithEntity {
    public static final MapCodec<UmbraVaultBlock> CODEC = createCodec(UmbraVaultBlock::new);
    public static final Property<UmbraVaultState> UMBRA_VAULT_STATE = EnumProperty.of("umbra_vault_state", UmbraVaultState.class);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    @Override
    public MapCodec<UmbraVaultBlock> getCodec() {
        return CODEC;
    }

    public UmbraVaultBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(UMBRA_VAULT_STATE, UmbraVaultState.INACTIVE)
        );
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isEmpty() || state.get(UMBRA_VAULT_STATE) != UmbraVaultState.ACTIVE) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!(world instanceof ServerWorld serverWorld)) {
            return ItemActionResult.CONSUME;
        }
        if (!(serverWorld.getBlockEntity(pos) instanceof UmbraVaultBlockEntity vaultBlockEntity)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        UmbraVaultBlockEntity.Server.tryUnlock(
                serverWorld, pos, state, vaultBlockEntity.getConfig(), vaultBlockEntity.getServerData(), vaultBlockEntity.getSharedData(), player, stack
        );
        return ItemActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new UmbraVaultBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, UMBRA_VAULT_STATE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world instanceof ServerWorld serverWorld ?
                validateTicker(
                        type,
                        SculkBlockEntities.UMBRA_VAULT_BE,
                        (worldx, pos, statex, blockEntity) -> UmbraVaultBlockEntity.Server.tick(
                                serverWorld, pos, statex, blockEntity.getConfig(), blockEntity.getServerData(), blockEntity.getSharedData()
                        )
                ) :
                validateTicker(
                        type,
                        SculkBlockEntities.UMBRA_VAULT_BE,
                        (worldx, pos, statex, blockEntity) -> UmbraVaultBlockEntity.Client.tick(worldx, pos, statex, blockEntity.getClientData(), blockEntity.getSharedData())
                );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}