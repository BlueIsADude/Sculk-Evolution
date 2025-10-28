package net.bluethedude.sculkevolution.util;

import com.mojang.serialization.Codec;
import net.bluethedude.sculkevolution.SculkEvolution;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class SculkDataComponents {

    public static final ComponentType<Integer> SCULK_CHARGE =
            register("sculk_charge", builder -> builder.codec(Codec.INT));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(SculkEvolution.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        SculkEvolution.LOGGER.info("Registering Data Component Types for " + SculkEvolution.MOD_ID);
    }
}
