package net.bluethedude.sculkevolution.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.bluethedude.sculkevolution.item.custom.SickleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyVariable(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", ordinal = 1), name = "bl4")
    private boolean canSweepAttack(boolean original, @Local(name = "itemStack") ItemStack itemStack) {
        if (itemStack.getItem() instanceof SickleItem) {
            return true;
        }
        return original;
    }
}
