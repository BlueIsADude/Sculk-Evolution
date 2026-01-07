package net.bluethedude.sculkevolution.block;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.custom.BladedHookBlock;
import net.bluethedude.sculkevolution.block.custom.UmbraVaultBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
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

    public static final Block UMBRA_VAULT = registerBlock("umbra_vault",
            new UmbraVaultBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SCULK_CATALYST)
                    .luminance(state -> state.get(UmbraVaultBlock.UMBRA_VAULT_STATE).getLuminance())
                    .strength(50.0F)
                    .blockVision(Blocks::never)
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

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
                entries.addAfter(Blocks.VAULT, UMBRA_VAULT)
        );
    }
}
