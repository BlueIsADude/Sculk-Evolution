package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.entity.SculkEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class SculkEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public SculkEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(EntityTypeTags.ARROWS)
                .add(SculkEntityTypes.ECHO_ARROW);
    }
}
