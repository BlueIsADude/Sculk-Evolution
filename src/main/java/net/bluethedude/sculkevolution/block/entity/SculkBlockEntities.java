package net.bluethedude.sculkevolution.block.entity;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.block.entity.custom.CalibratedSculkCatalystBlockEntity;
import net.bluethedude.sculkevolution.block.entity.custom.UmbraVaultBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SculkBlockEntities {
    public static final BlockEntityType<UmbraVaultBlockEntity> UMBRA_VAULT =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(SculkEvolution.MOD_ID, "umbra_vault"),
                    BlockEntityType.Builder.create(UmbraVaultBlockEntity::new, SculkBlocks.UMBRA_VAULT).build(null)
            );

    public static final BlockEntityType<CalibratedSculkCatalystBlockEntity> CALIBRATED_SCULK_CATALYST =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(SculkEvolution.MOD_ID, "calibrated_sculk_catalyst"),
                    BlockEntityType.Builder.create(CalibratedSculkCatalystBlockEntity::new, SculkBlocks.CALIBRATED_SCULK_CATALYST).build(null)
            );


    public static void registerBlockEntities() {
        SculkEvolution.LOGGER.info("Registering Block Entities for " + SculkEvolution.MOD_ID);
    }
}
