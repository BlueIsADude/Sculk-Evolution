package net.bluethedude.sculkevolution.block.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UmbraVaultSharedData {
	static final String SHARED_DATA_KEY = "shared_data";
	public static Codec<UmbraVaultSharedData> codec = RecordCodecBuilder.create(
		instance -> instance.group(
					ItemStack.createOptionalCodec("display_item").forGetter(data -> data.displayItem),
					Uuids.LINKED_SET_CODEC.lenientOptionalFieldOf("connected_players", Set.of()).forGetter(data -> data.connectedPlayers),
					Codec.DOUBLE
						.lenientOptionalFieldOf("connected_particles_range", UmbraVaultConfig.DEFAULT.deactivationRange())
						.forGetter(data -> data.connectedParticlesRange)
				)
				.apply(instance, UmbraVaultSharedData::new)
	);
	private ItemStack displayItem = ItemStack.EMPTY;
	private Set<UUID> connectedPlayers = new ObjectLinkedOpenHashSet<>();
	private double connectedParticlesRange = UmbraVaultConfig.DEFAULT.deactivationRange();
	public boolean dirty;

	UmbraVaultSharedData(ItemStack displayItem, Set<UUID> connectedPlayers, double connectedParticlesRange) {
		this.displayItem = displayItem;
		this.connectedPlayers.addAll(connectedPlayers);
		this.connectedParticlesRange = connectedParticlesRange;
	}

	public UmbraVaultSharedData() {
	}

	public ItemStack getDisplayItem() {
		return this.displayItem;
	}

	public boolean hasDisplayItem() {
		return !this.displayItem.isEmpty();
	}

	public void setDisplayItem(ItemStack stack) {
		if (!ItemStack.areEqual(this.displayItem, stack)) {
			this.displayItem = stack.copy();
			this.markDirty();
		}
	}

	boolean hasConnectedPlayers() {
		return !this.connectedPlayers.isEmpty();
	}

	public Set<UUID> getConnectedPlayers() {
		return this.connectedPlayers;
	}

	public double getConnectedParticlesRange() {
		return this.connectedParticlesRange;
	}

	public void updateConnectedPlayers(ServerWorld world, BlockPos pos, UmbraVaultConfig config, double radius) {
		Set<UUID> set = new HashSet<>(config.playerDetector()
                .detect(world, config.entitySelector(), pos, radius, false));
		if (!this.connectedPlayers.equals(set)) {
			this.connectedPlayers = set;
			this.markDirty();
		}
	}

	private void markDirty() {
		this.dirty = true;
	}

	public void copyFrom(UmbraVaultSharedData data) {
		this.displayItem = data.displayItem;
		this.connectedPlayers = data.connectedPlayers;
		this.connectedParticlesRange = data.connectedParticlesRange;
	}
}
