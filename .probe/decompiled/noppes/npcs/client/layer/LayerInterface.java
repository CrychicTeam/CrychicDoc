package noppes.npcs.client.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.shared.client.model.NopModelPart;

public abstract class LayerInterface extends RenderLayer {

    protected LivingEntityRenderer render;

    protected EntityCustomNpc npc;

    protected ModelData playerdata;

    public HumanoidModel base;

    private int color;

    public LayerInterface(LivingEntityRenderer render) {
        super(render);
        this.render = render;
        this.base = (HumanoidModel) render.getModel();
    }

    public void setColor(ModelPartData data, LivingEntity entity) {
    }

    protected float red() {
        return this.npc.f_20916_ <= 0 && this.npc.f_20919_ <= 0 ? (float) (this.color >> 16 & 0xFF) / 255.0F : 1.0F;
    }

    protected float green() {
        return this.npc.f_20916_ <= 0 && this.npc.f_20919_ <= 0 ? (float) (this.color >> 8 & 0xFF) / 255.0F : 0.0F;
    }

    protected float blue() {
        return this.npc.f_20916_ <= 0 && this.npc.f_20919_ <= 0 ? (float) (this.color & 0xFF) / 255.0F : 0.0F;
    }

    protected float alpha() {
        if (this.npc.f_20916_ <= 0 && this.npc.f_20919_ <= 0) {
            boolean flag = !this.npc.m_20145_();
            boolean flag1 = !flag && !this.npc.m_20177_(Minecraft.getInstance().player);
            return flag1 ? 0.15F : 0.99F;
        } else {
            return 0.3F;
        }
    }

    public void preRender(ModelPartData data) {
        if (this.npc.f_20916_ <= 0 && this.npc.f_20919_ <= 0) {
            this.color = data.color;
            if (this.npc.display.getTint() != 16777215) {
                if (data.color != 16777215) {
                    this.color = this.blend(data.color, this.npc.display.getTint(), 0.5F);
                } else {
                    this.color = this.npc.display.getTint();
                }
            }
        }
    }

    public int blend(int color1, int color2, float ratio) {
        if (ratio >= 1.0F) {
            return color2;
        } else if (ratio <= 0.0F) {
            return color1;
        } else {
            int aR = (color1 & 0xFF0000) >> 16;
            int aG = (color1 & 0xFF00) >> 8;
            int aB = color1 & 0xFF;
            int bR = (color2 & 0xFF0000) >> 16;
            int bG = (color2 & 0xFF00) >> 8;
            int bB = color2 & 0xFF;
            int R = (int) ((float) aR + (float) (bR - aR) * ratio);
            int G = (int) ((float) aG + (float) (bG - aG) * ratio);
            int B = (int) ((float) aB + (float) (bB - aB) * ratio);
            return R << 16 | G << 8 | B;
        }
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.npc = (EntityCustomNpc) entity;
        if (!this.npc.m_20177_(Minecraft.getInstance().player)) {
            this.playerdata = this.npc.modelData;
            this.base = (HumanoidModel) this.render.getModel();
            this.rotate(matrixStackIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            matrixStackIn.pushPose();
            if (entity.isInvisible()) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.15F);
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(770, 771);
            }
            if (this.npc.f_20916_ <= 0 && this.npc.f_20919_ > 0) {
            }
            if (this.npc.m_6047_()) {
            }
            this.render(matrixStackIn, bufferIn, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            if (entity.isInvisible()) {
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
            }
            matrixStackIn.popPose();
        }
    }

    public RenderType getRenderType(ModelPartData data) {
        ResourceLocation resource = this.npc.textureLocation;
        if (!data.playerTexture) {
            resource = data.getResource();
        }
        return RenderType.entityTranslucent(resource);
    }

    public void setRotation(NopModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public abstract void render(PoseStack var1, MultiBufferSource var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9);

    public abstract void rotate(PoseStack var1, float var2, float var3, float var4, float var5, float var6, float var7);
}