package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;

public class TheEndGatewayRenderer extends TheEndPortalRenderer<TheEndGatewayBlockEntity> {

    private static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/end_gateway_beam.png");

    public TheEndGatewayRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        super(blockEntityRendererProviderContext0);
    }

    public void render(TheEndGatewayBlockEntity theEndGatewayBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        if (theEndGatewayBlockEntity0.isSpawning() || theEndGatewayBlockEntity0.isCoolingDown()) {
            float $$6 = theEndGatewayBlockEntity0.isSpawning() ? theEndGatewayBlockEntity0.getSpawnPercent(float1) : theEndGatewayBlockEntity0.getCooldownPercent(float1);
            double $$7 = theEndGatewayBlockEntity0.isSpawning() ? (double) theEndGatewayBlockEntity0.m_58904_().m_151558_() : 50.0;
            $$6 = Mth.sin($$6 * (float) Math.PI);
            int $$8 = Mth.floor((double) $$6 * $$7);
            float[] $$9 = theEndGatewayBlockEntity0.isSpawning() ? DyeColor.MAGENTA.getTextureDiffuseColors() : DyeColor.PURPLE.getTextureDiffuseColors();
            long $$10 = theEndGatewayBlockEntity0.m_58904_().getGameTime();
            BeaconRenderer.renderBeaconBeam(poseStack2, multiBufferSource3, BEAM_LOCATION, float1, $$6, $$10, -$$8, $$8 * 2, $$9, 0.15F, 0.175F);
        }
        super.render(theEndGatewayBlockEntity0, float1, poseStack2, multiBufferSource3, int4, int5);
    }

    @Override
    protected float getOffsetUp() {
        return 1.0F;
    }

    @Override
    protected float getOffsetDown() {
        return 0.0F;
    }

    @Override
    protected RenderType renderType() {
        return RenderType.endGateway();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}