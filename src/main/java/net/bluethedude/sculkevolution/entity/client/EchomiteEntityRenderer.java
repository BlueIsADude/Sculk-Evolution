package net.bluethedude.sculkevolution.entity.client;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.entity.custom.EchomiteEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EchomiteEntityRenderer extends MobEntityRenderer<EchomiteEntity, EchomiteEntityModel<EchomiteEntity>> {
    private static final Identifier TEXTURE = Identifier.of(SculkEvolution.MOD_ID, "textures/entity/echomite.png");

    public EchomiteEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new EchomiteEntityModel<>(context.getPart(EchomiteEntityModel.ECHOMITE)), 0.3F);
    }

    protected float getLyingAngle(EchomiteEntity echomiteEntity) {
        return 180.0F;
    }

    public Identifier getTexture(EchomiteEntity echomiteEntity) {
        return TEXTURE;
    }
}
