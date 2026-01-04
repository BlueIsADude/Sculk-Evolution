package net.bluethedude.sculkevolution.block.util;

import net.bluethedude.sculkevolution.block.entity.custom.UmbraVaultBlockEntity;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

public enum UmbraVaultState implements StringIdentifiable {
	INACTIVE("inactive", UmbraVaultState.Light.HALF_LIT) {
		@Override
		protected void onChangedTo(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
			sharedData.setDisplayItem(ItemStack.EMPTY);

			UmbraVaultBlockEntity.Client.spawnDeactivateParticles(world, pos, ParticleTypes.SOUL_FIRE_FLAME);
			world.playSound(null, pos, SoundEvents.BLOCK_VAULT_DEACTIVATE, SoundCategory.BLOCKS);
		}
	},
	ACTIVE("active", UmbraVaultState.Light.LIT) {
		@Override
		public void onChangedTo(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
			if (!sharedData.hasDisplayItem()) {
				UmbraVaultBlockEntity.Server.updateDisplayItem(world, this, config, sharedData, pos);
			}

            if (world.getBlockEntity(pos) instanceof UmbraVaultBlockEntity umbraVaultBlockEntity) {
				UmbraVaultBlockEntity.Client.spawnActivateParticles(
						world,
						umbraVaultBlockEntity.getPos(),
						umbraVaultBlockEntity.getCachedState(),
						umbraVaultBlockEntity.getSharedData(),
                        ParticleTypes.SOUL_FIRE_FLAME
				);
            }
			world.playSound(null, pos, SoundEvents.BLOCK_VAULT_ACTIVATE, SoundCategory.BLOCKS);
		}
	},
	UNLOCKING("unlocking", UmbraVaultState.Light.LIT) {
		@Override
		protected void onChangedTo(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
			world.playSound(null, pos, SoundEvents.BLOCK_VAULT_INSERT_ITEM, SoundCategory.BLOCKS);
		}
	},
	EJECTING("ejecting", UmbraVaultState.Light.LIT) {
		@Override
		protected void onChangedTo(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
			world.playSound(null, pos, SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, SoundCategory.BLOCKS);
		}

		@Override
		protected void onChangedFrom(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
			world.playSound(null, pos, SoundEvents.BLOCK_VAULT_CLOSE_SHUTTER, SoundCategory.BLOCKS);
		}
	};

    private final String id;
	private final UmbraVaultState.Light light;

	UmbraVaultState(final String id, final UmbraVaultState.Light light) {
		this.id = id;
		this.light = light;
	}

	@Override
	public String asString() {
		return this.id;
	}

	public int getLuminance() {
		return this.light.luminance;
	}

	public UmbraVaultState update(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultServerData serverData, UmbraVaultSharedData sharedData) {
		return switch (this) {
			case INACTIVE -> updateActiveState(world, pos, config, serverData, sharedData, config.activationRange());
			case ACTIVE -> updateActiveState(world, pos, config, serverData, sharedData, config.deactivationRange());
			case UNLOCKING -> {
				serverData.setStateUpdatingResumeTime(world.getTime() + 20L);
				yield EJECTING;
			}
			case EJECTING -> {
				if (serverData.getItemsToEject().isEmpty()) {
					serverData.finishEjecting();
					yield updateActiveState(world, pos, config, serverData, sharedData, config.deactivationRange());
				} else {
					float f = serverData.getEjectSoundPitchModifier();
					this.ejectItem(world, pos, serverData.getItemToEject(), f);
					sharedData.setDisplayItem(serverData.getItemToDisplay());
                    int i = 20;
					serverData.setStateUpdatingResumeTime(world.getTime() + (long)i);
					yield EJECTING;
				}
			}
		};
	}

	private static UmbraVaultState updateActiveState(
			ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultServerData serverData, UmbraVaultSharedData sharedData, double radius
	) {
		sharedData.updateConnectedPlayers(world, pos, config, radius);
		serverData.setStateUpdatingResumeTime(world.getTime() + 20L);
		return sharedData.hasConnectedPlayers() ? ACTIVE : INACTIVE;
	}

	public void onStateChange(ServerWorld world, BlockPos pos, UmbraVaultState newState, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
		this.onChangedFrom(world, pos, config, sharedData);
		newState.onChangedTo(world, pos, config, sharedData);
	}

	protected void onChangedTo(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
	}

	protected void onChangedFrom(ServerWorld world, BlockPos pos, UmbraVaultConfig config, UmbraVaultSharedData sharedData) {
	}

	private void ejectItem(ServerWorld world, BlockPos pos, ItemStack stack, float pitchModifier) {
		ItemDispenserBehavior.spawnItem(world, stack, 2, Direction.UP, Vec3d.ofBottomCenter(pos).offset(Direction.UP, 1.2));
		world.syncWorldEvent(WorldEvents.VAULT_EJECTS_ITEM, pos, 0);
		world.playSound(null, pos, SoundEvents.BLOCK_VAULT_EJECT_ITEM, SoundCategory.BLOCKS, 1.0F, 0.8F + 0.4F * pitchModifier);
	}

	enum Light {
		HALF_LIT(6),
		LIT(12);

		final int luminance;

		Light(final int luminance) {
			this.luminance = luminance;
		}
	}
}
