package net.bluethedude.sculkevolution.entity.damage;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class SculkDamageTypeTags {
    public static final TagKey<DamageType> WONT_RESET_SICKLE = createTag("wont_reset_sickle");

    private static TagKey<DamageType> createTag(String name) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(SculkEvolution.MOD_ID, name));
    }
}
