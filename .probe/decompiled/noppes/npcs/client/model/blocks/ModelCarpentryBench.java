package noppes.npcs.client.model.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import noppes.npcs.shared.client.model.NopModelPart;

public class ModelCarpentryBench extends Model {

    NopModelPart Leg1 = new NopModelPart(128, 64, 0, 0);

    NopModelPart Leg2;

    NopModelPart Leg3;

    NopModelPart Leg4;

    NopModelPart Bottom_plate;

    NopModelPart Desktop;

    NopModelPart Backboard;

    NopModelPart Vice_Jaw1;

    NopModelPart Vice_Jaw2;

    NopModelPart Vice_Base1;

    NopModelPart Vice_Base2;

    NopModelPart Vice_Crank;

    NopModelPart Vice_Screw;

    NopModelPart Blueprint;

    public ModelCarpentryBench() {
        super(RenderType::m_110458_);
        this.Leg1.addBox(0.0F, 0.0F, 0.0F, 2.0F, 14.0F, 2.0F);
        this.Leg1.setPos(6.0F, 10.0F, 5.0F);
        this.Leg2 = new NopModelPart(128, 64, 0, 0);
        this.Leg2.addBox(0.0F, 0.0F, 0.0F, 2.0F, 14.0F, 2.0F);
        this.Leg2.setPos(6.0F, 10.0F, -5.0F);
        this.Leg3 = new NopModelPart(128, 64, 0, 0);
        this.Leg3.addBox(0.0F, 0.0F, 0.0F, 2.0F, 14.0F, 2.0F);
        this.Leg3.setPos(-8.0F, 10.0F, 5.0F);
        this.Leg4 = new NopModelPart(128, 64, 0, 0);
        this.Leg4.addBox(0.0F, 0.0F, 0.0F, 2.0F, 14.0F, 2.0F);
        this.Leg4.setPos(-8.0F, 10.0F, -5.0F);
        this.Bottom_plate = new NopModelPart(128, 64, 0, 24);
        this.Bottom_plate.addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 10.0F);
        this.Bottom_plate.setPos(-7.0F, 21.0F, -4.0F);
        this.Bottom_plate.setTexSize(130, 64);
        this.Desktop = new NopModelPart(128, 64, 0, 3);
        this.Desktop.addBox(0.0F, 0.0F, 0.0F, 18.0F, 2.0F, 13.0F);
        this.Desktop.setPos(-9.0F, 9.0F, -6.0F);
        this.Backboard = new NopModelPart(128, 64, 0, 18);
        this.Backboard.addBox(-1.0F, 0.0F, 0.0F, 18.0F, 5.0F, 1.0F);
        this.Backboard.setPos(-8.0F, 7.0F, 7.0F);
        this.Vice_Jaw1 = new NopModelPart(128, 64, 54, 18);
        this.Vice_Jaw1.addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F);
        this.Vice_Jaw1.setPos(3.0F, 6.0F, -8.0F);
        this.Vice_Jaw2 = new NopModelPart(128, 64, 54, 21);
        this.Vice_Jaw2.addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 1.0F);
        this.Vice_Jaw2.setPos(3.0F, 6.0F, -6.0F);
        this.Vice_Base1 = new NopModelPart(128, 64, 38, 30);
        this.Vice_Base1.addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F);
        this.Vice_Base1.setPos(3.0F, 8.0F, -5.0F);
        this.Vice_Base2 = new NopModelPart(128, 64, 38, 25);
        this.Vice_Base2.addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F);
        this.Vice_Base2.setPos(4.0F, 7.0F, -5.0F);
        this.Vice_Crank = new NopModelPart(128, 64, 54, 24);
        this.Vice_Crank.addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F);
        this.Vice_Crank.setPos(6.0F, 6.0F, -9.0F);
        this.Vice_Screw = new NopModelPart(128, 64, 44, 25);
        this.Vice_Screw.addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F);
        this.Vice_Screw.setPos(4.0F, 8.0F, -8.0F);
        this.Blueprint = new NopModelPart(128, 64, 31, 18);
        this.Blueprint.addBox(0.0F, 0.0F, 0.0F, 8.0F, 0.0F, 7.0F);
        this.Blueprint.setPos(0.0F, 9.0F, 1.0F);
        this.setRotation(this.Blueprint, 0.3271718F, 0.1487144F, 0.0F);
    }

    @Override
    public void renderToBuffer(PoseStack mStack, VertexConsumer iVertex, int lightmapUV, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.Leg1.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Leg2.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Leg3.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Leg4.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Bottom_plate.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Desktop.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Backboard.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Vice_Jaw1.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Vice_Jaw2.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Vice_Base1.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Vice_Base2.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Vice_Crank.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Vice_Screw.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Blueprint.render(mStack, iVertex, lightmapUV, packedOverlayIn);
    }

    private void setRotation(NopModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}