package net.bluethedude.sculkevolution.sound;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SculkSoundEvents {

    public static final SoundEvent ITEM_SICKLE_GROW_SMALL = registerSoundEvent("item.sickle.grow_small");
    public static final SoundEvent ITEM_SICKLE_GROW_LARGE = registerSoundEvent("item.sickle.grow_large");
    public static final SoundEvent ITEM_SICKLE_RESET = registerSoundEvent("item.sickle.reset");

    public static final SoundEvent ITEM_SICKLE_SONIC_CHARGE = registerSoundEvent("item.sickle.sonic_charge");
    public static final SoundEvent ITEM_SICKLE_SONIC_BOOM = registerSoundEvent("item.sickle.sonic_boom");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(SculkEvolution.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSoundEvents() {
        SculkEvolution.LOGGER.info("Registering Mod Sounds for " + SculkEvolution.MOD_ID);
    }
}
