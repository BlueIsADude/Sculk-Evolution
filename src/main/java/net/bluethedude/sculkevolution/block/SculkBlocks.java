package net.bluethedude.sculkevolution.block;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.block.custom.BladedHookBlock;
import net.bluethedude.sculkevolution.block.custom.CalibratedSculkCatalystBlock;
import net.bluethedude.sculkevolution.block.custom.UmbraVaultBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
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
                    .mapColor(MapColor.CYAN)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SCULK_CATALYST)
                    .luminance(state -> state.get(UmbraVaultBlock.UMBRA_VAULT_STATE).getLuminance())
                    .strength(50.0F)
                    .blockVision(Blocks::never)
            )
    );

    public static final Block CALIBRATED_SCULK_CATALYST = registerBlock("calibrated_sculk_catalyst",
            new CalibratedSculkCatalystBlock(AbstractBlock.Settings.copy(Blocks.SCULK_CATALYST))
    );

    public static final Block ECHO_CLUSTER = registerBlock("echo_cluster",
            new AmethystClusterBlock(
                    7.0F,
                    3.0F,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.BLACK)
                            .solid()
                            .nonOpaque()
                            .requiresTool()
                            .strength(1.5F)
                            .luminance(state -> 5)
                            .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                            .pistonBehavior(PistonBehavior.DESTROY)
            )
    );
    public static final Block SMALL_ECHO_BUD = registerBlock("small_echo_bud",
            new AmethystClusterBlock(
                    3.0F,
                    4.0F,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.BLACK)
                            .solid()
                            .nonOpaque()
                            .requiresTool()
                            .strength(1.5F)
                            .luminance(state -> 1)
                            .sounds(BlockSoundGroup.SMALL_AMETHYST_BUD)
                            .pistonBehavior(PistonBehavior.DESTROY)
            )
    );
    public static final Block MEDIUM_ECHO_BUD = registerBlock("medium_echo_bud",
            new AmethystClusterBlock(
                    4.0F,
                    3.0F,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.BLACK)
                            .solid()
                            .nonOpaque()
                            .requiresTool()
                            .strength(1.5F)
                            .luminance(state -> 2)
                            .sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD)
                            .pistonBehavior(PistonBehavior.DESTROY)
            )
    );
    public static final Block LARGE_ECHO_BUD = registerBlock("large_echo_bud",
            new AmethystClusterBlock(
                    5.0F,
                    3.0F,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.BLACK)
                            .solid()
                            .nonOpaque()
                            .requiresTool()
                            .strength(1.5F)
                            .luminance(state -> 4)
                            .sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)
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

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Blocks.VAULT, UMBRA_VAULT);
            entries.addAfter(Blocks.INFESTED_DEEPSLATE, CALIBRATED_SCULK_CATALYST,
                    SMALL_ECHO_BUD, MEDIUM_ECHO_BUD, LARGE_ECHO_BUD, ECHO_CLUSTER);
        });
    }
}
