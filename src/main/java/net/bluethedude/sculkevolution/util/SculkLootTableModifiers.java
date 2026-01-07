package net.bluethedude.sculkevolution.util;

import net.bluethedude.sculkevolution.item.SculkItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class SculkLootTableModifiers {
    private static final Identifier ANCIENT_CITY_ID =
            Identifier.ofVanilla("chests/ancient_city");

    public static void modifyLootTables(){
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (ANCIENT_CITY_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(3))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .with(ItemEntry.builder(SculkItems.UMBRA_KEY))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1)).build());
                tableBuilder.pool(poolBuilder);
                poolBuilder.build();
            }
        });
    }
}
