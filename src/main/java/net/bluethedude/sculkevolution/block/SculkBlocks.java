package net.bluethedude.sculkevolution.block;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.custom.BladedHookBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class SculkBlocks {

    public static final Block BLADED_HOOK = registerItemlessBlock("bladed_hook",
            new BladedHookBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.SNARE)
                    .sounds(BlockSoundGroup.COPPER)
                    .strength(10.0F, 1200.0F)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
            )
    );

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(SculkEvolution.MOD_ID, name), block);
    }

    private static Block registerItemlessBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(SculkEvolution.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(SculkEvolution.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerSculkBlocks() {
        SculkEvolution.LOGGER.info("Registering Mod Blocks for " + SculkEvolution.MOD_ID);
    }
}
