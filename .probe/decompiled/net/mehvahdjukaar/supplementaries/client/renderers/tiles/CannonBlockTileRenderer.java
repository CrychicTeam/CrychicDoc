package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.client.cannon.CannonTrajectoryRenderer;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CannonBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CannonBlockTileRenderer implements BlockEntityRenderer<CannonBlockTile> {

    private final ModelPart head;

    private final ModelPart legs;

    private final ModelPart pivot;

    private final ModelPart model;

    public CannonBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart model = context.bakeLayer(ClientRegistry.CANNON_MODEL);
        this.legs = model.getChild("legs");
        this.pivot = this.legs.getChild("head_pivot");
        this.head = this.pivot.getChild("head");
        this.model = model;
    }

    public boolean shouldRenderOffScreen(CannonBlockTile blockEntity) {
        return true;
    }

    public void render(CannonBlockTile tile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Quaternionf rotation = ((Direction) tile.m_58900_().m_61143_(CannonBlock.f_52588_)).getRotation();
        poseStack.mulPose(rotation);
        VertexConsumer builder = ModMaterials.CANNON_MATERIAL.buffer(bufferSource, RenderType::m_110452_);
        long t = System.currentTimeMillis();
        float wave = (float) (t % 1000L) / 1000.0F;
        float yawRad = tile.getYaw(partialTick) * (float) (Math.PI / 180.0);
        float pitchRad = tile.getPitch(partialTick) * (float) (Math.PI / 180.0);
        Vector3f forward = new Vector3f(0.0F, 0.0F, 1.0F);
        forward.rotateX((float) Math.PI - pitchRad);
        forward.rotateY((float) Math.PI - yawRad);
        forward.rotate(rotation.invert());
        float var19 = (float) Mth.atan2((double) forward.x, (double) forward.z);
        pitchRad = (float) Mth.atan2((double) (-forward.y), (double) Mth.sqrt(forward.x * forward.x + forward.z * forward.z));
        float rollRad = (float) Math.atan2((double) forward.y, (double) forward.z);
        this.legs.yRot = var19;
        this.pivot.xRot = pitchRad;
        this.pivot.zRot = 0.0F;
        float scale = Mth.sin((float) (t % 200L) / 200.0F * (float) Math.PI) * 0.01F + 1.0F + wave * 0.06F;
        this.head.xScale = scale;
        this.head.yScale = scale;
        float c = wave * 0.1F;
        this.head.zScale = 1.0F - c;
        this.head.z = c * 5.675F;
        this.model.render(poseStack, builder, packedLight, packedOverlay);
        poseStack.popPose();
        CannonTrajectoryRenderer.render(tile, poseStack, bufferSource, packedLight, packedOverlay, partialTick, yawRad);
    }

    public static LayerDefinition createMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, 4.0F, -3.0F, 2.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(48, 0).addBox(-8.0F, 4.0F, -3.0F, 2.0F, 10.0F, 6.0F), PartPose.offset(0.0F, -8.0F, 0.0F));
        PartDefinition head = legs.addOrReplaceChild("head_pivot", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
        PartDefinition bone = head.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 46).addBox(-6.0F, -6.0F, -6.5F, 12.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-6.0F, -6.0F, -6.5F, 12.0F, 12.0F, 13.0F, new CubeDeformation(-0.3125F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -2.0F, 0.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 8.0F, -8.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}