package net.bluethedude.sculkevolution.particle;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SculkParticleTypes {
    public static final SimpleParticleType UMBRA_VAULT_CONNECTION =
            registerParticle("umbra_vault_connection", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(SculkEvolution.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        SculkEvolution.LOGGER.info("Registering Particles for " + SculkEvolution.MOD_ID);
    }
}
