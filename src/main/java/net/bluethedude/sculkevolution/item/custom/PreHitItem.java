package net.bluethedude.sculkevolution.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface PreHitItem {
    void sculk_evolution$preHit(ItemStack stack, LivingEntity target, LivingEntity attacker);
}
