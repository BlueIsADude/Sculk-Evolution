package net.bluethedude.sculkevolution.util;

import net.bluethedude.sculkevolution.item.SculkItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class SculkLootTableModifiers {
    private static final Identifier ANCIENT_CITY_ID =
            Identifier.ofVanilla("chests/ancient_city");

    public static void modifyLootTables(){
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (ANCIENT_CITY_ID.equals(key.getValue())) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.348f))
                        .with(ItemEntry.builder(SculkItems.WARDEN_KEY))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3)).build());
                tableBuilder.pool(poolBuilder);
                poolBuilder.build();
            }
        });
    }
}
