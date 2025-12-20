package net.bluethedude.sculkevolution.mixin;

import net.bluethedude.sculkevolution.item.custom.PreHitItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo info) {
        PlayerEntity targetThis = (PlayerEntity)(Object)this;
        if (target.isAttackable() && !target.handleAttack(targetThis)) {
            if (targetThis.getWorld() instanceof ServerWorld) {
                if (target instanceof LivingEntity livingEntity) {
                    ItemStack stack = targetThis.getWeaponStack();
                    ((PreHitItem)stack.getItem()).sculk_Evolution$preHit(stack, livingEntity, targetThis);
                }
            }
        }
    }
}
