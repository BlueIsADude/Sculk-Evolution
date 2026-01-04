package net.bluethedude.sculkevolution.block.util;

import net.minecraft.util.math.MathHelper;

public class UmbraVaultClientData {
	public static final float DISPLAY_ROTATION_SPEED = 10.0F;
	private float displayRotation;
	private float prevDisplayRotation;

	public UmbraVaultClientData() {
	}

	public float getDisplayRotation() {
		return this.displayRotation;
	}

	public float getPreviousDisplayRotation() {
		return this.prevDisplayRotation;
	}

	public void rotateDisplay() {
		this.prevDisplayRotation = this.displayRotation;
		this.displayRotation = MathHelper.wrapDegrees(this.displayRotation + 10.0F);
	}
}
