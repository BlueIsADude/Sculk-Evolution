package net.bluethedude.sculkevolution.item.custom;

import net.bluethedude.sculkevolution.entity.custom.EchoArrowEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EchoArrowItem extends ArrowItem {
    public EchoArrowItem(Item.Settings settings) {
        super(settings);
        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new EchoArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        EchoArrowEntity echoArrowEntity = new EchoArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        echoArrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return echoArrowEntity;
    }
}
