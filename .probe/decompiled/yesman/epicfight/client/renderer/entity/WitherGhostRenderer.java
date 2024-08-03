package yesman.epicfight.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.world.entity.WitherGhostClone;

@OnlyIn(Dist.CLIENT)
public class WitherGhostRenderer extends NoopLivingEntityRenderer<WitherGhostClone> {

    public WitherGhostRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F);
    }

    protected int getBlockLightLevel(WitherGhostClone witherBoss, BlockPos blockpos) {
        return 15;
    }
}