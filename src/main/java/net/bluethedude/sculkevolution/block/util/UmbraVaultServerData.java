package net.bluethedude.sculkevolution.block.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class UmbraVaultServerData {
	public static Codec<UmbraVaultServerData> codec = RecordCodecBuilder.create(
		instance -> instance.group(
					Codec.LONG.lenientOptionalFieldOf("state_updating_resumes_at", 0L).forGetter(data -> data.stateUpdatingResumesAt),
					ItemStack.CODEC.listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter(data -> data.itemsToEject),
					Codec.INT.lenientOptionalFieldOf("total_ejections_needed", 0).forGetter(data -> data.totalEjectionsNeeded)
				)
				.apply(instance, UmbraVaultServerData::new)
	);
	private long stateUpdatingResumesAt;
	private final List<ItemStack> itemsToEject = new ObjectArrayList<>();
	private long lastFailedUnlockTime;
	private int totalEjectionsNeeded;
	public boolean dirty;

	UmbraVaultServerData(long stateUpdatingResumesAt, List<ItemStack> itemsToEject, int totalEjectionsNeeded) {
		this.stateUpdatingResumesAt = stateUpdatingResumesAt;
		this.itemsToEject.addAll(itemsToEject);
		this.totalEjectionsNeeded = totalEjectionsNeeded;
	}

	public UmbraVaultServerData() {
	}

	public void setLastFailedUnlockTime(long lastFailedUnlockTime) {
		this.lastFailedUnlockTime = lastFailedUnlockTime;
	}

	public long getLastFailedUnlockTime() {
		return this.lastFailedUnlockTime;
	}

	public long getStateUpdatingResumeTime() {
		return this.stateUpdatingResumesAt;
	}

	public void setStateUpdatingResumeTime(long stateUpdatingResumesAt) {
		this.stateUpdatingResumesAt = stateUpdatingResumesAt;
		this.markDirty();
	}

	List<ItemStack> getItemsToEject() {
		return this.itemsToEject;
	}

	void finishEjecting() {
		this.totalEjectionsNeeded = 0;
		this.markDirty();
	}

	public void setItemsToEject(List<ItemStack> itemsToEject) {
		this.itemsToEject.clear();
		this.itemsToEject.addAll(itemsToEject);
		this.totalEjectionsNeeded = this.itemsToEject.size();
		this.markDirty();
	}

	public ItemStack getItemToDisplay() {
		return this.itemsToEject.isEmpty()
			? ItemStack.EMPTY
			: Objects.requireNonNullElse(this.itemsToEject.getLast(), ItemStack.EMPTY);
	}

	ItemStack getItemToEject() {
		if (this.itemsToEject.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.markDirty();
			return Objects.requireNonNullElse(this.itemsToEject.removeLast(), ItemStack.EMPTY);
		}
	}

	public void copyFrom(UmbraVaultServerData data) {
		this.stateUpdatingResumesAt = data.getStateUpdatingResumeTime();
		this.itemsToEject.clear();
		this.itemsToEject.addAll(data.itemsToEject);
	}

	private void markDirty() {
		this.dirty = true;
	}

	public float getEjectSoundPitchModifier() {
		return this.totalEjectionsNeeded == 1
			? 1.0F
			: 1.0F - MathHelper.getLerpProgress((float)this.getItemsToEject().size(), 1.0F, (float)this.totalEjectionsNeeded);
	}
}
