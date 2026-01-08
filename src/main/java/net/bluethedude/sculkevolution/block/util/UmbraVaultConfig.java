package net.bluethedude.sculkevolution.block.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.item.SculkItems;
import net.minecraft.block.spawner.EntityDetector;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record UmbraVaultConfig(
	RegistryKey<LootTable> lootTable,
	double activationRange,
	double deactivationRange,
	ItemStack keyItem,
	Optional<RegistryKey<LootTable>> overrideLootTableToDisplay,
	EntityDetector playerDetector,
	EntityDetector.Selector entitySelector
) {
	public static UmbraVaultConfig DEFAULT = new UmbraVaultConfig();
	public static Codec<UmbraVaultConfig> codec = RecordCodecBuilder.<UmbraVaultConfig>create(
			instance -> instance.group(
						RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).lenientOptionalFieldOf("loot_table", DEFAULT.lootTable()).forGetter(UmbraVaultConfig::lootTable),
						Codec.DOUBLE.lenientOptionalFieldOf("activation_range", DEFAULT.activationRange()).forGetter(UmbraVaultConfig::activationRange),
						Codec.DOUBLE.lenientOptionalFieldOf("deactivation_range", DEFAULT.deactivationRange()).forGetter(UmbraVaultConfig::deactivationRange),
						ItemStack.createOptionalCodec("key_item").forGetter(UmbraVaultConfig::keyItem),
						RegistryKey.createCodec(RegistryKeys.LOOT_TABLE)
							.lenientOptionalFieldOf("override_loot_table_to_display")
							.forGetter(UmbraVaultConfig::overrideLootTableToDisplay)
					)
					.apply(instance, UmbraVaultConfig::new)
		)
		.validate(UmbraVaultConfig::validate);

	private UmbraVaultConfig() {
		this(
			RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(SculkEvolution.MOD_ID, "chests/ancient_altar/reward_umbra")),
			4.0,
			4.5,
			new ItemStack(SculkItems.WARDEN_KEY),
			Optional.empty(),
			EntityDetector.NON_SPECTATOR_PLAYERS,
			EntityDetector.Selector.IN_WORLD
		);
	}

	public UmbraVaultConfig(
		RegistryKey<LootTable> lootTable,
		double activationRange,
		double deactivationRange,
		ItemStack keyItem,
		Optional<RegistryKey<LootTable>> overrideLootTableToDisplay
	) {
		this(lootTable, activationRange, deactivationRange, keyItem, overrideLootTableToDisplay, DEFAULT.playerDetector(), DEFAULT.entitySelector());
	}

	private DataResult<UmbraVaultConfig> validate() {
		return this.activationRange > this.deactivationRange
			? DataResult.error(() -> "Activation range must (" + this.activationRange + ") be less or equal to deactivation range (" + this.deactivationRange + ")")
			: DataResult.success(this);
	}
}
