package net.mehvahdjukaar.supplementaries.client.renderers.entities.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class QuiverLayer<T extends LivingEntity & IQuiverEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    private final Supplier<QuiverLayer.QuiverMode> quiverMode;

    private final boolean skeleton;

    public QuiverLayer(RenderLayerParent<T, M> parent, boolean isSkeleton) {
        super(parent);
        this.skeleton = isSkeleton;
        this.quiverMode = isSkeleton ? ClientConfigs.Items.QUIVER_SKELETON_RENDER_MODE : ClientConfigs.Items.QUIVER_RENDER_MODE;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity instanceof IQuiverEntity) {
            QuiverLayer.QuiverMode mode = (QuiverLayer.QuiverMode) this.quiverMode.get();
            if (mode != QuiverLayer.QuiverMode.HIDDEN) {
                ItemStack quiver;
                if (!this.skeleton) {
                    quiver = livingEntity.supplementaries$getQuiver();
                    if (livingEntity.getMainHandItem() == quiver || livingEntity.getOffhandItem() == quiver) {
                        return;
                    }
                } else {
                    if (!livingEntity.supplementaries$hasQuiver()) {
                        return;
                    }
                    quiver = ((QuiverItem) ModRegistry.QUIVER_ITEM.get()).m_7968_();
                }
                if (!quiver.isEmpty()) {
                    poseStack.pushPose();
                    ((HumanoidModel) this.m_117386_()).body.translateAndRotate(poseStack);
                    boolean flipped = livingEntity.getMainArm() == HumanoidArm.RIGHT;
                    double o = 0.001;
                    if (mode == QuiverLayer.QuiverMode.THIGH) {
                        boolean hasArmor = livingEntity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorItem;
                        double offset = hasArmor ? (Double) ClientConfigs.Items.QUIVER_ARMOR_OFFSET.get() : 0.0;
                        boolean sneaking = livingEntity.m_6047_();
                        if (sneaking) {
                            poseStack.translate(0.0, -0.125, -0.275);
                        }
                        o += offset == -1.0 ? 0.21875 : 0.1875 + offset;
                        if (flipped) {
                            float old = ((HumanoidModel) this.m_117386_()).leftLeg.xRot;
                            ((HumanoidModel) this.m_117386_()).leftLeg.xRot = old * 0.3F;
                            ((HumanoidModel) this.m_117386_()).leftLeg.translateAndRotate(poseStack);
                            ((HumanoidModel) this.m_117386_()).leftLeg.xRot = old;
                            poseStack.translate(0.0, -0.0625, -0.15625);
                            poseStack.translate(o, 0.0, 0.0);
                        } else {
                            float old = ((HumanoidModel) this.m_117386_()).rightLeg.xRot;
                            ((HumanoidModel) this.m_117386_()).rightLeg.xRot = old * 0.3F;
                            ((HumanoidModel) this.m_117386_()).rightLeg.translateAndRotate(poseStack);
                            ((HumanoidModel) this.m_117386_()).rightLeg.xRot = old;
                            poseStack.translate(0.0, -0.0625, -0.15625);
                            poseStack.translate(-o, 0.0, 0.0);
                        }
                    } else {
                        boolean hasArmorx = livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorItem;
                        double offsetx = hasArmorx ? (Double) ClientConfigs.Items.QUIVER_ARMOR_OFFSET.get() : 0.0;
                        if (mode == QuiverLayer.QuiverMode.HIP) {
                            o += offsetx == -1.0 ? 0.21875 : 0.1875 + offsetx;
                            poseStack.translate(0.0, 0.1, o);
                            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            if (flipped) {
                                poseStack.scale(-1.0F, 1.0F, -1.0F);
                            }
                            poseStack.translate(0.0, 0.4, -0.1875);
                            poseStack.mulPose(Axis.XN.rotationDegrees(-22.5F));
                        } else {
                            o += offsetx == -1.0 ? 0.25 : 0.1875 + offsetx;
                            poseStack.translate(0.0, 0.1, o);
                            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                            if (flipped) {
                                poseStack.scale(-1.0F, 1.0F, -1.0F);
                            }
                            poseStack.translate(0.0, 0.0, -0.125);
                        }
                    }
                    this.itemRenderer.renderStatic(livingEntity, quiver, ItemDisplayContext.HEAD, false, poseStack, buffer, livingEntity.m_9236_(), packedLight, OverlayTexture.NO_OVERLAY, 0);
                    poseStack.popPose();
                }
            }
        }
    }

    public static enum QuiverMode {

        HIDDEN, BACK, HIP, THIGH
    }
}