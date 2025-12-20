package net.bluethedude.sculkevolution.mixin;

import net.bluethedude.sculkevolution.item.custom.PreHitItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements PreHitItem {
    @Override
    public void sculk_Evolution$preHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }
}
