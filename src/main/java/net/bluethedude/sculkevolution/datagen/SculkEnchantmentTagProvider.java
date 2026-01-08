package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.enchantment.SculkEnchantmentTags;
import net.bluethedude.sculkevolution.enchantment.SculkEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class SculkEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {

    public SculkEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(SculkEnchantmentTags.SICKLE_EXCLUSIVE_SET)
                .add(SculkEnchantments.ECHO_CHAMBER)
                .add(SculkEnchantments.REPULSE)
                .add(SculkEnchantments.SOUL_LUNGE);

        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(SculkEnchantments.ECHO_CHAMBER)
                .add(SculkEnchantments.REPULSE)
                .add(SculkEnchantments.SOUL_LUNGE);
    }
}
