package net.bluethedude.sculkevolution.enchantment;

import com.mojang.serialization.MapCodec;
import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.enchantment.custom.BlankEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SculkEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> BLANK =
            registerEntityEffect("blank", BlankEnchantmentEffect.CODEC);


    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
                                                                                    MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(SculkEvolution.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        SculkEvolution.LOGGER.info("Registering Mod Enchantment Effects for " + SculkEvolution.MOD_ID);
    }
}
