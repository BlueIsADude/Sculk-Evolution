package net.bluethedude.sculkevolution.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface PreHitItem {
    void sculk_Evolution$preHit(ItemStack stack, LivingEntity target, LivingEntity attacker);
}
