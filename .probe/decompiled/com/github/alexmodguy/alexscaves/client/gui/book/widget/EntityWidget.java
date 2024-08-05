package com.github.alexmodguy.alexscaves.client.gui.book.widget;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.CustomBookEntityRenderer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityWidget extends BookWidget {

    @Expose
    @SerializedName("entity_id")
    private String entityId;

    @Expose
    private String nbt;

    @Expose
    private boolean sepia;

    @Expose
    @SerializedName("rot_x")
    private float rotX;

    @Expose
    @SerializedName("rot_y")
    private float rotY;

    @Expose
    @SerializedName("rot_z")
    private float rotZ;

    @Expose(serialize = false, deserialize = false)
    private Entity actualRenderEntity = null;

    public EntityWidget(int displayPage, String entityId, boolean sepia, String entityNBT, int x, int y, float scale) {
        this(displayPage, BookWidget.Type.ENTITY, entityId, sepia, entityNBT, x, y, scale);
    }

    public EntityWidget(int displayPage, BookWidget.Type type, String entityId, boolean sepia, String entityNBT, int x, int y, float scale) {
        super(displayPage, type, x, y, scale);
        this.entityId = entityId;
        this.sepia = sepia;
        this.nbt = entityNBT;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTicks, boolean onFlippingPage) {
        if (this.actualRenderEntity == null) {
            EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(this.entityId));
            if (type != null) {
                this.actualRenderEntity = type.create(Minecraft.getInstance().level);
                if (this.actualRenderEntity instanceof LivingEntity living && this.nbt != null && !this.nbt.isEmpty()) {
                    try {
                        living.readAdditionalSaveData(TagParser.parseTag(this.nbt));
                    } catch (CommandSyntaxException var8) {
                        throw new RuntimeException(var8);
                    }
                }
            }
        }
        float entityScale = 100.0F * this.getScale();
        float entityBBSize = Math.max(this.actualRenderEntity.getBbWidth(), this.actualRenderEntity.getBbHeight());
        if ((double) entityBBSize > 1.0) {
            entityScale /= entityBBSize * 1.5F;
        }
        poseStack.pushPose();
        poseStack.translate((float) this.getX(), (float) this.getY(), 120.0F);
        poseStack.scale(entityScale, entityScale, entityScale);
        poseStack.mulPose(Axis.XP.rotationDegrees(this.rotX));
        poseStack.mulPose(Axis.YP.rotationDegrees(this.rotY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(this.rotZ));
        Lighting.setupForEntityInInventory();
        this.renderEntityInSepia(this.actualRenderEntity, 0.0F, partialTicks, poseStack, bufferSource, 240);
        Lighting.setupFor3DItems();
        poseStack.popPose();
    }

    protected boolean isSepia() {
        return this.sepia;
    }

    private void renderEntityInSepia(Entity entityIn, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);
            if (entityIn instanceof LivingEntity living) {
                if (render instanceof CustomBookEntityRenderer customBookEntityRenderer) {
                    if (this.sepia) {
                        customBookEntityRenderer.setSepiaFlag(true);
                    }
                    matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                    RenderSystem.runAsFancy(() -> manager.render(entityIn, 0.0, 0.0, 0.0, 0.0F, 0.0F, matrixStack, bufferIn, packedLight));
                    if (this.sepia) {
                        customBookEntityRenderer.setSepiaFlag(false);
                    }
                } else if (render instanceof LivingEntityRenderer<?, ?> renderer) {
                    EntityModel model = renderer.getModel();
                    VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getBookWidget(render.getTextureLocation(entityIn), this.sepia));
                    matrixStack.pushPose();
                    boolean shouldSit = entityIn.isPassenger() && entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit();
                    model.young = living.isBaby();
                    model.riding = shouldSit;
                    model.setupAnim(living, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
                    matrixStack.scale(living.getScale(), living.getScale(), living.getScale());
                    model.m_7695_(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    matrixStack.popPose();
                }
            }
            Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        } catch (Throwable var15) {
            CrashReport crashreport = CrashReport.forThrowable(var15, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", yaw);
            crashreportcategory1.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }
}