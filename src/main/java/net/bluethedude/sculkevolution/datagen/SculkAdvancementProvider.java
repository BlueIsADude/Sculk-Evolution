package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.bluethedude.sculkevolution.util.SculkDataComponents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.advancement.criterion.PlayerHurtEntityCriterion;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
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
                .parent(AdvancementTabGenerator.reference("adventure/avoid_vibration"))
                .display(
                        SculkItems.WARDEN_KEY,
                        Text.translatable("advancements.sculkevolution.pandoras_box.title"),
                        Text.translatable("advancements.sculkevolution.pandoras_box.description"),
                        null,
                        AdvancementFrame.GOAL,
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

        Advancement.Builder.create()
                .parent(AdvancementTabGenerator.reference("adventure/avoid_vibration"))
                .display(
                        SculkItems.SICKLE,
                        Text.translatable("advancements.sculkevolution.handheld_catalyst.title"),
                        Text.translatable("advancements.sculkevolution.handheld_catalyst.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .rewards(AdvancementRewards.Builder.experience(40))
                .criterion(
                        "handheld_catalyst",
                        PlayerHurtEntityCriterion.Conditions.create(
                                DamagePredicate.Builder.create()
                                        .dealt(NumberRange.DoubleRange.atLeast(10.0))
                                        .type(
                                                DamageSourcePredicate.Builder.create()
                                                        .tag(TagPredicate.expected(DamageTypeTags.IS_PLAYER_ATTACK))
                                                        .directEntity(
                                                                EntityPredicate.Builder.create()
                                                                        .type(EntityType.PLAYER)
                                                                        .equipment(EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().items(SculkItems.SICKLE)
                                                                                .component(ComponentPredicate.builder().add(SculkDataComponents.SCULK_CHARGE, 9).build())))
                                                        )
                                        )
                        )
                )
                .build(consumer, SculkEvolution.MOD_ID + ":adventure/pandoras_box");
    }
}
