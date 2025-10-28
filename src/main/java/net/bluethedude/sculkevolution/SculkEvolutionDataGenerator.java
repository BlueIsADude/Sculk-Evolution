package net.bluethedude.sculkevolution;

import net.bluethedude.sculkevolution.datagen.*;
import net.bluethedude.sculkevolution.enchantment.SculkEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class SculkEvolutionDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(SculkBlockTagProvider::new);
        pack.addProvider(SculkItemTagProvider::new);
        pack.addProvider(SculkEnchantmentTagProvider::new);
        pack.addProvider(SculkLootTableProvider::new);
        pack.addProvider(SculkModelProvider::new);
        pack.addProvider(SculkRecipeProvider::new);
        pack.addProvider(SculkRegistryDataGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, SculkEnchantments::bootstrap);
    }
}
