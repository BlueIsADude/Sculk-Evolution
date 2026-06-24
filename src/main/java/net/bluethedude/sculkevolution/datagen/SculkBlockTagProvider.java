package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class SculkBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public SculkBlockTagProvider(FabricDataOutput output, CompletableFuture< RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(SculkBlocks.CALIBRATED_SCULK_CATALYST);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(SculkBlocks.ECHO_CLUSTER)
                .add(SculkBlocks.SMALL_ECHO_BUD)
                .add(SculkBlocks.MEDIUM_ECHO_BUD)
                .add(SculkBlocks.LARGE_ECHO_BUD);

        getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(SculkBlocks.SMALL_ECHO_BUD);

        getOrCreateTagBuilder(ConventionalBlockTags.BUDDING_BLOCKS)
                .add(SculkBlocks.CALIBRATED_SCULK_CATALYST);

        getOrCreateTagBuilder(ConventionalBlockTags.BUDS)
                .add(SculkBlocks.SMALL_ECHO_BUD)
                .add(SculkBlocks.MEDIUM_ECHO_BUD)
                .add(SculkBlocks.LARGE_ECHO_BUD);

        getOrCreateTagBuilder(ConventionalBlockTags.CLUSTERS)
                .add(SculkBlocks.ECHO_CLUSTER);
    }
}
