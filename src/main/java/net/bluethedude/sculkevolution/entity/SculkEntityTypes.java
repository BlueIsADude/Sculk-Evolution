package net.bluethedude.sculkevolution.entity;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.entity.custom.EchoArrowEntity;
import net.bluethedude.sculkevolution.entity.custom.EchomiteEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class SculkEntityTypes {

    private static final RegistryKey<EntityType<?>> ECHO_ARROW_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SculkEvolution.MOD_ID, "echo_arrow"));

    private static final RegistryKey<EntityType<?>> ECHOMITE_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SculkEvolution.MOD_ID, "echomite"));


    public static final EntityType<EchoArrowEntity> ECHO_ARROW = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(SculkEvolution.MOD_ID, "echo_arrow"),
            EntityType.Builder.<EchoArrowEntity>create(EchoArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
                    .build(String.valueOf(ECHO_ARROW_KEY))
    );

    public static final EntityType<EchomiteEntity> ECHOMITE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(SculkEvolution.MOD_ID, "echomite"),
            EntityType.Builder.create(EchomiteEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.4F, 0.3F)
                    .eyeHeight(0.13F)
                    .passengerAttachments(0.2375F)
                    .maxTrackingRange(8)
                    .build(String.valueOf(ECHOMITE_KEY))
    );

    public static void registerSculkEntities() {
        SculkEvolution.LOGGER.info("Registering Mod Entities for " + SculkEvolution.MOD_ID);
    }
}
