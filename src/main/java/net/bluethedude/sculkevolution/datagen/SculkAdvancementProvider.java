package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SculkAdvancementProvider extends FabricAdvancementProvider {

    public SculkAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        Advancement.Builder.create()
                .parent(AdvancementTabGenerator.reference("adventure/root"))
                .display(
                        SculkItems.WARDEN_KEY,
                        Text.translatable("advancements.sculkevolution.pandoras_box.title"),
                        Text.translatable("advancements.sculkevolution.pandoras_box.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(
                        "pandoras_box",
                        ItemCriterion.Conditions.createItemUsedOnBlock(
                                LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create().blocks(SculkBlocks.UMBRA_VAULT).state(StatePredicate.Builder.create())),
                                ItemPredicate.Builder.create().items(SculkItems.WARDEN_KEY)
                        )
                )
                .build(consumer, SculkEvolution.MOD_ID + ":adventure/pandoras_box");
    }
}
