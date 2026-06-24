package net.bluethedude.sculkevolution;

import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.block.entity.SculkBlockEntities;
import net.bluethedude.sculkevolution.block.entity.client.UmbraVaultBlockEntityRenderer;
import net.bluethedude.sculkevolution.entity.SculkEntityTypes;
import net.bluethedude.sculkevolution.entity.client.EchoArrowEntityRenderer;
import net.bluethedude.sculkevolution.entity.client.EchomiteEntityModel;
import net.bluethedude.sculkevolution.entity.client.EchomiteEntityRenderer;
import net.bluethedude.sculkevolution.particle.SculkParticleTypes;
import net.bluethedude.sculkevolution.particle.custom.ModConnectionParticle;
import net.bluethedude.sculkevolution.util.SculkModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SculkEvolutionClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SculkModelPredicates.registerModelPredicates();

        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.BLADED_HOOK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.UMBRA_VAULT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.ECHO_CLUSTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.SMALL_ECHO_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.MEDIUM_ECHO_BUD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SculkBlocks.LARGE_ECHO_BUD, RenderLayer.getCutout());

        BlockEntityRendererFactories.register(SculkBlockEntities.UMBRA_VAULT, UmbraVaultBlockEntityRenderer::new);

        EntityRendererRegistry.register(SculkEntityTypes.ECHO_ARROW, EchoArrowEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(EchomiteEntityModel.ECHOMITE, EchomiteEntityModel::getTexturedModelData);
        EntityRendererRegistry.register(SculkEntityTypes.ECHOMITE, EchomiteEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(SculkParticleTypes.UMBRA_VAULT_CONNECTION, ModConnectionParticle.UmbraVaultConnectionFactory::new);
    }
}
