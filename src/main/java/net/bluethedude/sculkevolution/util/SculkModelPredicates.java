package net.bluethedude.sculkevolution.util;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class SculkModelPredicates {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(SculkItems.SICKLE, Identifier.of(SculkEvolution.MOD_ID, "charged"),
                (stack, world, entity, seed) ->
                        stack.get(SculkDataComponents.SCULK_CHARGE) == null ? 0F :
                                Objects.requireNonNull(stack.get(SculkDataComponents.SCULK_CHARGE)).floatValue() / 10.0F
        );
        ModelPredicateProviderRegistry.register(SculkItems.SICKLE, Identifier.of(SculkEvolution.MOD_ID, "reaping"),
                (stack, world, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );
    }
}
