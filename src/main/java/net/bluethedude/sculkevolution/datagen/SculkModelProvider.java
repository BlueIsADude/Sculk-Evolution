package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.block.custom.UmbraVaultBlock;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SculkBlock;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SculkModelProvider  extends FabricModelProvider {
    public SculkModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        this.registerUmbraVault(blockStateModelGenerator);
        this.registerCalibratedSculkCatalyst(blockStateModelGenerator);
    }

    private void registerUmbraVault(BlockStateModelGenerator modelGenerator) {
        Block block = SculkBlocks.UMBRA_VAULT;
        Identifier sculk = TextureMap.getSubId(Blocks.SCULK_CATALYST, "_top");
        TextureMap textureMap = TextureMap.vault(block, "_front_off", "_side_off", "_top_off", "_bottom");
        TextureMap textureMap2 = TextureMap.vault(block, "_front_on", "_side_on", "_top_on", "_bottom");
        TextureMap textureMap3 = TextureMap.vault(block, "_front_ejecting", "_side_on", "_top_on", "_bottom");
        TextureMap textureMap4 = TextureMap.vault(block, "_front_ejecting", "_side_on", "_top_ejecting", "_bottom");
        Identifier identifier = Models.TEMPLATE_VAULT.upload(block, textureMap, modelGenerator.modelCollector);
        Identifier identifier2 = Models.TEMPLATE_VAULT.upload(block, "_active", textureMap2, modelGenerator.modelCollector);
        Identifier identifier3 = Models.TEMPLATE_VAULT.upload(block, "_unlocking", textureMap3, modelGenerator.modelCollector);
        Identifier identifier4 = Models.TEMPLATE_VAULT.upload(block, "_ejecting_reward", textureMap4, modelGenerator.modelCollector);
        modelGenerator.registerParentedItemModel(block, identifier);
        modelGenerator.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(block)
                                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
                                .coordinate(BlockStateVariantMap.create(UmbraVaultBlock.UMBRA_VAULT_STATE).register((vaultState) -> switch (vaultState) {
                                    case INACTIVE -> BlockStateVariant.create().put(VariantSettings.MODEL, identifier);
                                    case ACTIVE -> BlockStateVariant.create().put(VariantSettings.MODEL, identifier2);
                                    case UNLOCKING -> BlockStateVariant.create().put(VariantSettings.MODEL, identifier3);
                                    case EJECTING -> BlockStateVariant.create().put(VariantSettings.MODEL, identifier4);
                                }))
                );
    }

    private void registerCalibratedSculkCatalyst(BlockStateModelGenerator modelGenerator) {
        Identifier identifier = TextureMap.getSubId(SculkBlocks.CALIBRATED_SCULK_CATALYST, "_bottom");
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.BOTTOM, identifier)
                .put(TextureKey.TOP, TextureMap.getSubId(SculkBlocks.CALIBRATED_SCULK_CATALYST, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(SculkBlocks.CALIBRATED_SCULK_CATALYST, "_side"));
        TextureMap textureMap2 = new TextureMap()
                .put(TextureKey.BOTTOM, identifier)
                .put(TextureKey.TOP, TextureMap.getSubId(SculkBlocks.CALIBRATED_SCULK_CATALYST, "_top_bloom"))
                .put(TextureKey.SIDE, TextureMap.getSubId(SculkBlocks.CALIBRATED_SCULK_CATALYST, "_side_bloom"));
        Identifier identifier2 = Models.CUBE_BOTTOM_TOP.upload(SculkBlocks.CALIBRATED_SCULK_CATALYST, "", textureMap, modelGenerator.modelCollector);
        Identifier identifier3 = Models.CUBE_BOTTOM_TOP.upload(SculkBlocks.CALIBRATED_SCULK_CATALYST, "_bloom", textureMap2, modelGenerator.modelCollector);
        modelGenerator.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(SculkBlocks.CALIBRATED_SCULK_CATALYST)
                                .coordinate(
                                        BlockStateVariantMap.create(Properties.BLOOM).register(bloom -> BlockStateVariant.create().put(VariantSettings.MODEL, bloom ? identifier3 : identifier2))
                                )
                );
        modelGenerator.registerParentedItemModel(SculkBlocks.CALIBRATED_SCULK_CATALYST, identifier2);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(SculkItems.ECHO_ARROW, Models.GENERATED);
        itemModelGenerator.register(SculkItems.WARDEN_KEY, Models.GENERATED);
        itemModelGenerator.register(SculkItems.BLADED_HOOK, Models.GENERATED);

        itemModelGenerator.register(SculkItems.ECHOMITE_SPAWN_EGG,
                new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty())
        );
    }
}
