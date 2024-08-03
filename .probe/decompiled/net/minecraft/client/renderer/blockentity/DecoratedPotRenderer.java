package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.EnumSet;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;

public class DecoratedPotRenderer implements BlockEntityRenderer<DecoratedPotBlockEntity> {

    private static final String NECK = "neck";

    private static final String FRONT = "front";

    private static final String BACK = "back";

    private static final String LEFT = "left";

    private static final String RIGHT = "right";

    private static final String TOP = "top";

    private static final String BOTTOM = "bottom";

    private final ModelPart neck;

    private final ModelPart frontSide;

    private final ModelPart backSide;

    private final ModelPart leftSide;

    private final ModelPart rightSide;

    private final ModelPart top;

    private final ModelPart bottom;

    private final Material baseMaterial = (Material) Objects.requireNonNull(Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.BASE));

    public DecoratedPotRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        ModelPart $$1 = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.DECORATED_POT_BASE);
        this.neck = $$1.getChild("neck");
        this.top = $$1.getChild("top");
        this.bottom = $$1.getChild("bottom");
        ModelPart $$2 = blockEntityRendererProviderContext0.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
        this.frontSide = $$2.getChild("front");
        this.backSide = $$2.getChild("back");
        this.leftSide = $$2.getChild("left");
        this.rightSide = $$2.getChild("right");
    }

    public static LayerDefinition createBaseLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        CubeDeformation $$2 = new CubeDeformation(0.2F);
        CubeDeformation $$3 = new CubeDeformation(-0.1F);
        $$1.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 0).addBox(4.0F, 17.0F, 4.0F, 8.0F, 3.0F, 8.0F, $$3).texOffs(0, 5).addBox(5.0F, 20.0F, 5.0F, 6.0F, 1.0F, 6.0F, $$2), PartPose.offsetAndRotation(0.0F, 37.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F));
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(-14, 13).addBox(0.0F, 0.0F, 0.0F, 14.0F, 0.0F, 14.0F);
        $$1.addOrReplaceChild("top", $$4, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        $$1.addOrReplaceChild("bottom", $$4, PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create($$0, 32, 32);
    }

    public static LayerDefinition createSidesLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        CubeListBuilder $$2 = CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH));
        $$1.addOrReplaceChild("back", $$2, PartPose.offsetAndRotation(15.0F, 16.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        $$1.addOrReplaceChild("left", $$2, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, (float) (-Math.PI / 2), (float) Math.PI));
        $$1.addOrReplaceChild("right", $$2, PartPose.offsetAndRotation(15.0F, 16.0F, 15.0F, 0.0F, (float) (Math.PI / 2), (float) Math.PI));
        $$1.addOrReplaceChild("front", $$2, PartPose.offsetAndRotation(1.0F, 16.0F, 15.0F, (float) Math.PI, 0.0F, 0.0F));
        return LayerDefinition.create($$0, 16, 16);
    }

    @Nullable
    private static Material getMaterial(Item item0) {
        Material $$1 = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(item0));
        if ($$1 == null) {
            $$1 = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(Items.BRICK));
        }
        return $$1;
    }

    public void render(DecoratedPotBlockEntity decoratedPotBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        poseStack2.pushPose();
        Direction $$6 = decoratedPotBlockEntity0.getDirection();
        poseStack2.translate(0.5, 0.0, 0.5);
        poseStack2.mulPose(Axis.YP.rotationDegrees(180.0F - $$6.toYRot()));
        poseStack2.translate(-0.5, 0.0, -0.5);
        VertexConsumer $$7 = this.baseMaterial.buffer(multiBufferSource3, RenderType::m_110446_);
        this.neck.render(poseStack2, $$7, int4, int5);
        this.top.render(poseStack2, $$7, int4, int5);
        this.bottom.render(poseStack2, $$7, int4, int5);
        DecoratedPotBlockEntity.Decorations $$8 = decoratedPotBlockEntity0.getDecorations();
        this.renderSide(this.frontSide, poseStack2, multiBufferSource3, int4, int5, getMaterial($$8.front()));
        this.renderSide(this.backSide, poseStack2, multiBufferSource3, int4, int5, getMaterial($$8.back()));
        this.renderSide(this.leftSide, poseStack2, multiBufferSource3, int4, int5, getMaterial($$8.left()));
        this.renderSide(this.rightSide, poseStack2, multiBufferSource3, int4, int5, getMaterial($$8.right()));
        poseStack2.popPose();
    }

    private void renderSide(ModelPart modelPart0, PoseStack poseStack1, MultiBufferSource multiBufferSource2, int int3, int int4, @Nullable Material material5) {
        if (material5 == null) {
            material5 = getMaterial(Items.BRICK);
        }
        if (material5 != null) {
            modelPart0.render(poseStack1, material5.buffer(multiBufferSource2, RenderType::m_110446_), int3, int4);
        }
    }
}