package net.bluethedude.sculkevolution.enchantment;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.enchantment.custom.BlankEnchantmentEffect;
import net.bluethedude.sculkevolution.item.SculkItemTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class SculkEnchantments {
    public static final RegistryKey<Enchantment> ECHO_CHAMBER =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SculkEvolution.MOD_ID, "echo_chamber"));
    public static final RegistryKey<Enchantment> REPULSE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SculkEvolution.MOD_ID, "repulse"));
    public static final RegistryKey<Enchantment> SOUL_LUNGE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SculkEvolution.MOD_ID, "soul_lunge"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, ECHO_CHAMBER, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(SculkItemTags.SICKLE_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(25, 25),
                        Enchantment.leveledCost(75, 25),
                        4,
                        AttributeModifierSlot.HAND))
                .exclusiveSet(enchantments.getOrThrow(SculkEnchantmentTags.SICKLE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new BlankEnchantmentEffect()
                )
        );
        register(registerable, REPULSE, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(SculkItemTags.SICKLE_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(25, 25),
                        Enchantment.leveledCost(75, 25),
                        4,
                        AttributeModifierSlot.HAND))
                .exclusiveSet(enchantments.getOrThrow(SculkEnchantmentTags.SICKLE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new BlankEnchantmentEffect()
                )
        );
        register(registerable, SOUL_LUNGE, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(SculkItemTags.SICKLE_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(25, 25),
                        Enchantment.leveledCost(75, 25),
                        4,
                        AttributeModifierSlot.HAND))
                .exclusiveSet(enchantments.getOrThrow(SculkEnchantmentTags.SICKLE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM, new BlankEnchantmentEffect()
                )
        );
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
