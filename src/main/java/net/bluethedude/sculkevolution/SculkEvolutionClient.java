package net.bluethedude.sculkevolution;

import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.util.SculkModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class SculkEvolutionClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SculkModelPredicates.registerModelPredicates();

        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.BLADED_HOOK, RenderLayer.getCutout());
    }
}
