package noppes.npcs.client.model.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import noppes.npcs.shared.client.model.NopModelPart;

public class ModelMailboxWow extends Model {

    NopModelPart Shape4 = new NopModelPart(128, 64, 59, 0);

    NopModelPart Shape1;

    NopModelPart Shape2;

    NopModelPart Shape3;

    public ModelMailboxWow() {
        super(RenderType::m_110452_);
        this.Shape4.addBox(0.0F, 0.0F, 0.0F, 8.0F, 6.0F, 0.0F);
        this.Shape4.setPos(-4.0F, -4.0F, 0.0F);
        this.Shape1 = new NopModelPart(128, 64, 0, 39);
        this.Shape1.addBox(0.0F, 0.0F, 0.0F, 8.0F, 5.0F, 8.0F);
        this.Shape1.setPos(-4.0F, 19.0F, -4.0F);
        this.Shape2 = new NopModelPart(128, 64, 0, 21);
        this.Shape2.addBox(0.0F, 0.0F, 0.0F, 6.0F, 9.0F, 6.0F);
        this.Shape2.setPos(-3.0F, 10.0F, -3.0F);
        this.Shape3 = new NopModelPart(128, 64, 0, 0);
        this.Shape3.addBox(0.0F, 0.0F, 0.0F, 12.0F, 8.0F, 12.0F);
        this.Shape3.setPos(-6.0F, 2.0F, -6.0F);
    }

    @Override
    public void renderToBuffer(PoseStack mStack, VertexConsumer iVertex, int lightmapUV, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.Shape4.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Shape1.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Shape2.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        this.Shape3.render(mStack, iVertex, lightmapUV, packedOverlayIn);
    }
}