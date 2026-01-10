package net.bluethedude.sculkevolution.enchantment;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class SculkEnchantmentTags {
    public static final TagKey<Enchantment> SICKLE_EXCLUSIVE_SET = createTag("exclusive_set/sickle");
    public static final TagKey<Enchantment> REPULSE_EXCLUSIVE_SET = createTag("exclusive_set/repulse");

    private static TagKey<Enchantment> createTag(String name) {
        return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SculkEvolution.MOD_ID, name));
    }
}
