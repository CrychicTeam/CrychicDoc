package se.mickelus.tetra.blocks.scroll;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayerLocation;
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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.blocks.rack.RackBlock;

@ParametersAreNonnullByDefault
public class ScrollRenderer implements BlockEntityRenderer<ScrollTile> {

    public static final Material material = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("tetra", "block/scroll"));

    private static final int availableGlyphs = 16;

    private static final int availableMaterials = 3;

    public static ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation("tetra", "block/scroll"), "main");

    private final ModelPart[] rolledModel;

    private final ModelPart ribbonModel;

    private final ModelPart[] wallModel;

    private final QuadRenderer[][] wallGlyphs;

    private final ModelPart[] openModel;

    private final QuadRenderer[][] openGlyphs;

    private final BlockEntityRendererProvider.Context context;

    public ScrollRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        ModelPart model = context.bakeLayer(layer);
        this.rolledModel = new ModelPart[3];
        this.wallModel = new ModelPart[3];
        this.openModel = new ModelPart[3];
        for (int i = 0; i < 3; i++) {
            this.rolledModel[i] = model.getChild("rolled" + i);
            this.wallModel[i] = model.getChild("wall" + i);
            this.openModel[i] = model.getChild("open" + i);
        }
        this.ribbonModel = model.getChild("ribbon");
        this.wallGlyphs = new QuadRenderer[2][];
        for (int i = 0; i < this.wallGlyphs.length; i++) {
            this.wallGlyphs[i] = new QuadRenderer[16];
        }
        for (int i = 0; i < 16; i++) {
            this.wallGlyphs[0][i] = new QuadRenderer(8.0F, 1.0F, 0.075F, 7.0F, 13.0F, (float) (i * 7), 51.0F, 128.0F, 64.0F, true, Direction.SOUTH);
            this.wallGlyphs[1][i] = new QuadRenderer(1.0F, 1.0F, 0.075F, 7.0F, 13.0F, (float) (i * 7), 51.0F, 128.0F, 64.0F, false, Direction.SOUTH);
        }
        this.openGlyphs = new QuadRenderer[4][];
        for (int i = 0; i < this.openGlyphs.length; i++) {
            this.openGlyphs[i] = new QuadRenderer[16];
        }
        for (int i = 0; i < 16; i++) {
            this.openGlyphs[0][i] = new QuadRenderer(1.0F, 0.075F, 2.0F, 7.0F, 6.0F, (float) (i * 7), 58.0F, 128.0F, 64.0F, true, Direction.UP);
            this.openGlyphs[1][i] = new QuadRenderer(8.0F, 0.075F, 2.0F, 7.0F, 6.0F, (float) (i * 7), 58.0F, 128.0F, 64.0F, false, Direction.UP);
            this.openGlyphs[2][i] = new QuadRenderer(1.0F, 0.075F, 2.0F, 7.0F, 6.0F, (float) (i * 7), 58.0F, 128.0F, 64.0F, true, Direction.UP);
            this.openGlyphs[3][i] = new QuadRenderer(8.0F, 0.075F, 2.0F, 7.0F, 6.0F, (float) (i * 7), 58.0F, 128.0F, 64.0F, false, Direction.UP);
        }
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();
        for (int i = 0; i < 3; i++) {
            parts.addOrReplaceChild("rolled" + i, CubeListBuilder.create().texOffs(34 * i, 4).addBox(1.0F, 0.0F, 7.0F, 14.0F, 3.0F, 3.0F), PartPose.ZERO);
            parts.addOrReplaceChild("wall" + i, CubeListBuilder.create().texOffs(34 * i, 0).addBox(1.0F, 14.0F, 0.0F, 14.0F, 2.0F, 2.0F).texOffs(34 * i, 10).addBox(1.0F, 1.0F, 0.05F, 14.0F, 13.0F, 0.0F), PartPose.ZERO);
            parts.addOrReplaceChild("open" + i, CubeListBuilder.create().texOffs(34 * i, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 2.0F, 2.0F).addBox(1.0F, 0.0F, 14.0F, 14.0F, 2.0F, 2.0F).texOffs(34 * i - 12, 10).addBox(1.0F, 0.05F, 2.0F, 14.0F, 0.0F, 12.0F), PartPose.ZERO);
        }
        parts.addOrReplaceChild("ribbon", CubeListBuilder.create().texOffs(0, 23).addBox(7.0F, 0.0F, 7.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }

    public void render(ScrollTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        VertexConsumer vertexBuilder = material.buffer(buffer, rl -> RenderType.entityCutout(rl));
        ScrollData[] scrolls = tile.getScrolls();
        ScrollBlock.Arrangement arrangement = ((ScrollBlock) tile.m_58900_().m_60734_()).getArrangement();
        Direction direction = (Direction) tile.m_58900_().m_61143_(RackBlock.facingProp);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.0, 0.5);
        matrixStack.mulPose(direction.getRotation());
        matrixStack.mulPose(Axis.XN.rotationDegrees(90.0F));
        matrixStack.translate(-0.5, 0.0, -0.5);
        switch(arrangement) {
            case rolled:
                this.renderRolled(scrolls, matrixStack, combinedLight, combinedOverlay, vertexBuilder);
                break;
            case wall:
                this.renderWall(scrolls, matrixStack, combinedLight, combinedOverlay, vertexBuilder);
                break;
            case open:
                this.renderOpen(scrolls, matrixStack, combinedLight, combinedOverlay, vertexBuilder);
        }
        matrixStack.popPose();
        if (this.shouldDrawLabel(scrolls, tile.m_58899_())) {
            matrixStack.pushPose();
            matrixStack.translate(0.5, 0.0, 0.5);
            if (arrangement == ScrollBlock.Arrangement.wall) {
                matrixStack.mulPose(direction.getOpposite().getRotation());
                matrixStack.mulPose(Axis.XN.rotationDegrees(90.0F));
                matrixStack.translate(0.0, 0.55, 0.4);
                this.drawLabel(scrolls[0], matrixStack, buffer, combinedLight);
            } else if (arrangement == ScrollBlock.Arrangement.open) {
                double angle = RotationHelper.getHorizontalAngle(Minecraft.getInstance().getCameraEntity().getEyePosition(partialTicks), Vec3.atCenterOf(tile.m_58899_()));
                Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
                rotation.mul(Axis.YP.rotationDegrees((float) (angle / Math.PI * 180.0 + 180.0)));
                matrixStack.mulPose(rotation);
                matrixStack.translate(0.0, 0.4F, 0.4);
                this.drawLabel(scrolls[0], matrixStack, buffer, combinedLight);
            }
            matrixStack.popPose();
        }
    }

    private void renderRolled(ScrollData[] scrolls, PoseStack matrixStack, int combinedLight, int combinedOverlay, VertexConsumer vertexBuilder) {
        matrixStack.translate(0.5, 0.0, 0.5);
        matrixStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        matrixStack.translate(-0.5, 0.0, -0.5);
        int offset = Math.min(scrolls.length, 3) - 1;
        if (offset > 0) {
            matrixStack.translate(0.0, 0.0, (double) offset * -0.125);
        }
        for (int i = 0; i < scrolls.length; i++) {
            int mat = this.getMaterial(scrolls, i);
            float red = (float) FastColor.ARGB32.red(scrolls[i].ribbon) / 255.0F;
            float green = (float) FastColor.ARGB32.green(scrolls[i].ribbon) / 255.0F;
            float blue = (float) FastColor.ARGB32.blue(scrolls[i].ribbon) / 255.0F;
            this.rolledModel[mat].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
            this.ribbonModel.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, red, green, blue, 1.0F);
            matrixStack.translate(0.0F, 0.0F, 0.25F);
            if (i == 2) {
                matrixStack.translate(0.0, 0.1875, -0.625);
            } else if (i == 4) {
                matrixStack.translate(0.0, 0.1875, -0.375);
            }
        }
    }

    private void renderWall(ScrollData[] scrolls, PoseStack matrixStack, int combinedLight, int combinedOverlay, VertexConsumer vertexBuilder) {
        int mat = this.getMaterial(scrolls, 0);
        int color = this.getGlyphColor(mat);
        float red = (float) FastColor.ARGB32.red(color) / 255.0F;
        float green = (float) FastColor.ARGB32.green(color) / 255.0F;
        float blue = (float) FastColor.ARGB32.blue(color) / 255.0F;
        this.wallModel[mat].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        for (int i = 0; i < this.wallGlyphs.length; i++) {
            int glyph = this.getGlyph(scrolls, i);
            this.wallGlyphs[i][glyph].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, red, green, blue, 1.0F);
        }
    }

    private void renderOpen(ScrollData[] scrolls, PoseStack matrixStack, int combinedLight, int combinedOverlay, VertexConsumer vertexBuilder) {
        int mat = this.getMaterial(scrolls, 0);
        int color = this.getGlyphColor(mat);
        float red = (float) FastColor.ARGB32.red(color) / 255.0F;
        float green = (float) FastColor.ARGB32.green(color) / 255.0F;
        float blue = (float) FastColor.ARGB32.blue(color) / 255.0F;
        matrixStack.translate(0.5, 0.0, 0.5);
        matrixStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        matrixStack.translate(-0.5, 0.0, -0.5);
        this.openModel[mat].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        for (int i = 0; i < this.openGlyphs.length; i++) {
            if (i == 2) {
                matrixStack.translate(0.5, 0.0, 0.5);
                matrixStack.mulPose(Axis.YN.rotationDegrees(180.0F));
                matrixStack.translate(-0.5, 0.0, -0.5);
            }
            int glyph = this.getGlyph(scrolls, i);
            this.openGlyphs[i][glyph].render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, red, green, blue, 1.0F);
        }
    }

    private int getGlyphColor(int material) {
        switch(material) {
            case 0:
            default:
                return 6709063;
            case 1:
                return 9411532;
            case 2:
                return 12558634;
        }
    }

    private int getGlyph(ScrollData[] data, int index) {
        if (data.length > 0) {
            if (data[0].glyphs.size() > index) {
                return Mth.clamp((Integer) data[0].glyphs.get(index), 0, 16);
            }
            if (data[0].glyphs.size() > 0) {
                return Mth.clamp((Integer) data[0].glyphs.get(0), 0, 16);
            }
        }
        return 0;
    }

    private int getMaterial(ScrollData[] data, int index) {
        return data.length > index ? Mth.clamp(data[index].material, 0, 2) : 0;
    }

    private boolean shouldDrawLabel(ScrollData[] scrolls, BlockPos pos) {
        HitResult mouseover = Minecraft.getInstance().hitResult;
        return scrolls != null && scrolls.length > 0 && mouseover != null && mouseover.getType() == HitResult.Type.BLOCK && pos.equals(((BlockHitResult) mouseover).getBlockPos());
    }

    private void drawLabel(ScrollData scroll, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        String label = I18n.get("item.tetra.scroll." + scroll.key + ".name");
        matrixStack.scale(-0.0125F, -0.0125F, 0.0125F);
        Matrix4f matrix4f = matrixStack.last().pose();
        Font fontrenderer = this.context.getFont();
        float x = (float) (-fontrenderer.width(label)) / 2.0F;
        fontrenderer.drawInBatch(label, x + 1.0F, 0.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        fontrenderer.drawInBatch(label, x - 1.0F, 0.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        fontrenderer.drawInBatch(label, x, -1.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        fontrenderer.drawInBatch(label, x, 1.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        matrixStack.translate(0.0F, 0.0F, -0.0125F);
        fontrenderer.drawInBatch(label, x, 0.0F, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
    }
}