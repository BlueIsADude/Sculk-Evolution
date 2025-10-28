package net.bluethedude.sculkevolution.item;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class SculkItemTags {
    public static final TagKey<Item> SICKLE_ENCHANTABLE = createTag("enchantable/sickle");

    private static TagKey<Item> createTag(String name) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(SculkEvolution.MOD_ID, name));
    }
}
