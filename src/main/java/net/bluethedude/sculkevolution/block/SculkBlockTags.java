package net.bluethedude.sculkevolution.block;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class SculkBlockTags {


    private static TagKey<Block> createTag(String name) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(SculkEvolution.MOD_ID, name));
    }
}
