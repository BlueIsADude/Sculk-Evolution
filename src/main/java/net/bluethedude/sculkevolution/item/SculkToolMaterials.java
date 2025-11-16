package net.bluethedude.sculkevolution.item;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class SculkToolMaterials {
    public static ToolMaterial ECHO_SHARD = new ToolMaterial() {
        @Override
        public int getDurability() {
            return 500;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 6.0F;
        }

        @Override
        public float getAttackDamage() {
            return 2.0F;
        }

        @Override
        public TagKey<Block> getInverseTag() {
            return BlockTags.INCORRECT_FOR_IRON_TOOL;
        }

        @Override
        public int getEnchantability() {
            return 22;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(Items.ECHO_SHARD);
        }
    };
}
