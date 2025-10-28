package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.item.SculkItemTags;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class SculkItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public SculkItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(SculkItemTags.SICKLE_ENCHANTABLE)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ConventionalItemTags.MELEE_WEAPON_TOOLS)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ConventionalItemTags.TOOLS)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ItemTags.WEAPON_ENCHANTABLE)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ItemTags.MINING_LOOT_ENCHANTABLE)
                .add(SculkItems.SICKLE);

        getOrCreateTagBuilder(ItemTags.BREAKS_DECORATED_POTS)
                .add(SculkItems.SICKLE);
    }
}
