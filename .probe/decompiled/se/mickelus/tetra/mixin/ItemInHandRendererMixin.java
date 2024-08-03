package se.mickelus.tetra.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@OnlyIn(Dist.CLIENT)
@Mixin({ ItemInHandRenderer.class })
public abstract class ItemInHandRendererMixin {

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 1) }, method = { "renderArmWithItem" })
    private void renderArmWithItem(AbstractClientPlayer player, float partialTicks, float interpolatedPitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (ModularCrossbowItem.instance.equals(itemStack.getItem())) {
            this.tetraTransformCrossbow(player, partialTicks, interpolatedPitch, hand, swingProgress, itemStack, equipProgress, poseStack, buffer, light, ci);
        }
    }

    @Shadow
    protected abstract void applyItemArmAttackTransform(PoseStack var1, HumanoidArm var2, float var3);

    @Shadow
    private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float float0) {
        throw new IllegalStateException("Mixin failed to shadow getItem()");
    }

    private void tetraTransformCrossbow(AbstractClientPlayer player, float partialTicks, float interpolatedPitch, InteractionHand hand, float float0, ItemStack itemStack, float float1, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        boolean isMainhand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = isMainhand ? player.m_5737_() : player.m_5737_().getOpposite();
        boolean isCharged = CrossbowItem.isCharged(itemStack);
        boolean flag2 = arm == HumanoidArm.RIGHT;
        int i = flag2 ? 1 : -1;
        if (player.m_6117_() && player.m_21212_() > 0 && player.m_7655_() == hand) {
            this.applyItemArmTransform(poseStack, arm, float1);
            poseStack.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-11.935F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 65.3F));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) i * -9.785F));
            float f9 = (float) itemStack.getUseDuration() - ((float) player.m_21212_() - partialTicks + 1.0F);
            float f13 = f9 / (float) ((ModularCrossbowItem) itemStack.getItem()).getReloadDuration(itemStack);
            if (f13 > 1.0F) {
                f13 = 1.0F;
            }
            if (f13 > 0.1F) {
                float f16 = Mth.sin((f9 - 0.1F) * 1.3F);
                float f3 = f13 - 0.1F;
                float f4 = f16 * f3;
                poseStack.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
            }
            poseStack.translate(0.0, 0.0, (double) f13 * 0.04);
            poseStack.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
            poseStack.mulPose(Axis.YN.rotationDegrees((float) i * 45.0F));
        } else if (isCharged && float0 < 0.001F && isMainhand) {
            poseStack.translate((double) ((float) i * -0.641864F), 0.0, 0.0);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 10.0F));
        }
    }
}