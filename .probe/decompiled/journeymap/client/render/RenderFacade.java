package journeymap.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RenderFacade extends EntityRenderer {

    public RenderFacade(EntityRendererProvider.Context unused) {
        super(unused);
    }

    public static ResourceLocation getEntityTexture(EntityRenderer render, Entity entity) {
        return render.getTextureLocation(entity);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return null;
    }

    @Override
    public void render(Entity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
    }
}