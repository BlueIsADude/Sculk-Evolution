package net.bluethedude.sculkevolution;

import net.bluethedude.sculkevolution.block.SculkBlocks;
import net.bluethedude.sculkevolution.enchantment.SculkEnchantmentEffects;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.bluethedude.sculkevolution.sound.SculkSoundEvents;
import net.bluethedude.sculkevolution.util.SculkDataComponents;
import net.bluethedude.sculkevolution.util.SculkLootTableModifiers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkEvolution implements ModInitializer {

    public static final String MOD_ID = "sculkevolution";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        SculkBlocks.registerSculkBlocks();
        SculkItems.registerSculkItems();

        SculkEnchantmentEffects.registerEnchantmentEffects();
        SculkDataComponents.registerDataComponentTypes();

        SculkSoundEvents.registerSoundEvents();

        SculkLootTableModifiers.modifyLootTables();
    }
}
