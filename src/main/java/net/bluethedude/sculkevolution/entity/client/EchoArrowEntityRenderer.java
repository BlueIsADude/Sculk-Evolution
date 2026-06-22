package net.bluethedude.sculkevolution.entity.client;

import net.bluethedude.sculkevolution.SculkEvolution;
import net.bluethedude.sculkevolution.entity.custom.EchoArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EchoArrowEntityRenderer extends ProjectileEntityRenderer<EchoArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of( SculkEvolution.MOD_ID, "textures/entity/projectiles/echo_arrow.png");

    public EchoArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(EchoArrowEntity echoArrowEntity) {
        return TEXTURE;
    }
}
