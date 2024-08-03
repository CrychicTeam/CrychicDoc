package yesman.epicfight.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class WitherSkeletonMinionRenderer extends WitherSkeletonRenderer {

    private static final ResourceLocation WITHER_SKELETON_LOCATION = new ResourceLocation("epicfight", "textures/entity/wither_skeleton_minion.png");

    public WitherSkeletonMinionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        return WITHER_SKELETON_LOCATION;
    }
}