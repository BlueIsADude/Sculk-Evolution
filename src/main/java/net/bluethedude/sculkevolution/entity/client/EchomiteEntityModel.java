package net.bluethedude.sculkevolution.entity.client;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class EchomiteEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer ECHOMITE = new EntityModelLayer(Identifier.of(SculkEvolution.MOD_ID, "echomite"), "main");
    private static final int[][] SEGMENT_DIMENSIONS = new int[][]{{6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};
    private static final int[][] SEGMENT_UVS = new int[][]{{0, 0}, {14, 7}, {14, 13}, {18, 0}, {18, 3}};
    private final ModelPart root;
    private final ModelPart[] bodySegments = new ModelPart[5];
    private final ModelPart[] jaws = new ModelPart[2];

    public EchomiteEntityModel(ModelPart root) {
        this.root = root;
        Arrays.setAll(this.bodySegments, index -> root.getChild(getSegmentName(index)));
        Arrays.setAll(this.jaws, index -> root.getChild(getLayerName(index)));
    }

    private static String getLayerName(int index) {
        return "layer" + index;
    }

    private static String getSegmentName(int index) {
        return "segment" + index;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float[] fs = new float[5];
        float f = -3.5F;

        for (int i = 0; i < 5; i++) {
            modelPartData.addChild(
                    getSegmentName(i),
                    ModelPartBuilder.create()
                            .uv(SEGMENT_UVS[i][0], SEGMENT_UVS[i][1])
                            .cuboid(
                                    SEGMENT_DIMENSIONS[i][0] * -0.5F, 0.0F, SEGMENT_DIMENSIONS[i][2] * -0.5F, SEGMENT_DIMENSIONS[i][0], SEGMENT_DIMENSIONS[i][1], SEGMENT_DIMENSIONS[i][2]
                            ),
                    ModelTransform.pivot(0.0F, 24 - SEGMENT_DIMENSIONS[i][1], f)
            );
            fs[i] = f;
            if (i < 4) {
                f += (SEGMENT_DIMENSIONS[i][2] + SEGMENT_DIMENSIONS[i + 1][2]) * 0.5F;
            }
        }
        modelPartData.addChild(
                getLayerName(0),
                ModelPartBuilder.create().uv(0, 7).cuboid(-3.0F, 0.5F, SEGMENT_DIMENSIONS[0][0] * -0.5F - 2.0F, 3.0F, 3.0F, 4.0F),
                ModelTransform.pivot(0.0F, 20.0F, fs[0])
        );
        modelPartData.addChild(
                getLayerName(1),
                ModelPartBuilder.create().uv(0, 14).cuboid(0.0F, 0.5F, SEGMENT_DIMENSIONS[0][0] * -0.5F - 2.0F, 3.0F, 3.0F, 4.0F),
                ModelTransform.pivot(0.0F, 20.0F, fs[0])
        );
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        for (int i = 0; i < this.bodySegments.length; i++) {
            this.bodySegments[i].yaw = MathHelper.cos(animationProgress * 0.9F + i * 0.15F * (float) Math.PI) * (float) Math.PI * 0.01F * (1 + Math.abs(i - 2));
            this.bodySegments[i].pivotX = MathHelper.sin(animationProgress * 0.9F + i * 0.15F * (float) Math.PI) * (float) Math.PI * 0.1F * Math.abs(i - 2);
        }

        this.jaws[0].yaw = this.bodySegments[1].yaw + 0.15F;
        this.jaws[1].yaw = this.bodySegments[1].yaw - 0.15F;
        this.jaws[0].pivotX = this.bodySegments[1].pivotX;
        this.jaws[1].pivotX = this.bodySegments[1].pivotX;
    }
}
