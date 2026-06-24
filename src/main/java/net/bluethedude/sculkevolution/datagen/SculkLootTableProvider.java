package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class SculkLootTableProvider extends FabricBlockLootTableProvider {
    public SculkLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(SculkBlocks.CALIBRATED_SCULK_CATALYST, dropsWithSilkTouch(SculkBlocks.CALIBRATED_SCULK_CATALYST));

        addDrop(SculkBlocks.SMALL_ECHO_BUD, dropsWithSilkTouch(SculkBlocks.SMALL_ECHO_BUD));
        addDrop(SculkBlocks.MEDIUM_ECHO_BUD, dropsWithSilkTouch(SculkBlocks.MEDIUM_ECHO_BUD));
        addDrop(SculkBlocks.LARGE_ECHO_BUD, dropsWithSilkTouch(SculkBlocks.LARGE_ECHO_BUD));
        addDrop(SculkBlocks.ECHO_CLUSTER,
                block -> this.dropsWithSilkTouch(block, ItemEntry.builder(Items.ECHO_SHARD)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                        .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                        .alternatively(
                                this.applyExplosionDecay(
                                        block, ItemEntry.builder(Items.ECHO_SHARD).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                )
                        )
                )
        );
    }
}
