package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.item.SculkItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SculkRecipeProvider extends FabricRecipeProvider {
    public SculkRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, SculkItems.SICKLE)
                .input('#', SculkItems.BLADED_HOOK)
                .input('X', Items.BONE)
                .input('O', Items.ECHO_SHARD)
                .pattern("#")
                .pattern("X")
                .pattern("O")
                .criterion("has_bladed_hook", conditionsFromItem(SculkItems.BLADED_HOOK))
                .offerTo(exporter);
    }
}
