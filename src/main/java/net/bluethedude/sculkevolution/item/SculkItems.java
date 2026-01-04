package net.bluethedude.sculkevolution.item;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.item.custom.SickleItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class SculkItems {

    public static final Item UMBRA_KEY = registerItem("umbra_key", new Item(new Item.Settings()));

    public static final Item BLADED_HOOK = registerItem("bladed_hook",
            new AliasedBlockItem(SculkBlocks.BLADED_HOOK, new Item.Settings()
                    .rarity(Rarity.EPIC)
            )
    );

    public static final Item SICKLE = registerItem("sickle",
            new SickleItem(SculkToolMaterials.ECHO_SHARD, new Item.Settings()
                    .rarity(Rarity.EPIC)
                    .maxCount(1)
                    .component(DataComponentTypes.TOOL, SickleItem.createToolComponent())
                    .attributeModifiers(SickleItem.createAttributeModifiers(SculkToolMaterials.ECHO_SHARD, -2.0F, -2.0F))
            )
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SculkEvolution.MOD_ID, name), item);
    }

    public static void registerSculkItems() {
        SculkEvolution.LOGGER.info("Registering Mod Items for " + SculkEvolution.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.HEAVY_CORE, BLADED_HOOK);
            entries.addAfter(Items.OMINOUS_TRIAL_KEY, UMBRA_KEY);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries ->
                entries.addAfter(Items.MACE, SICKLE)
        );
    }
}
