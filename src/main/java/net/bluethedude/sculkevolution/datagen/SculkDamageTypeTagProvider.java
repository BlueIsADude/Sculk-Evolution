package net.bluethedude.sculkevolution.datagen;

import net.bluethedude.sculkevolution.entity.damage.SculkDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class SculkDamageTypeTagProvider extends FabricTagProvider<DamageType> {

    public SculkDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(SculkDamageTypeTags.WONT_RESET_SICKLE)
                .addOptionalTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .addOptional(DamageTypes.STARVE)
                .addOptional(DamageTypes.OUTSIDE_BORDER);
    }
}
