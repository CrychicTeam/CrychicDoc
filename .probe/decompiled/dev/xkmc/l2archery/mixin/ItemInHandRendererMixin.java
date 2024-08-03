package dev.xkmc.l2archery.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ItemInHandRenderer.class })
public abstract class ItemInHandRendererMixin {

    @Shadow
    protected abstract void applyItemArmTransform(PoseStack var1, HumanoidArm var2, float var3);

    @Shadow
    public abstract void renderItem(LivingEntity var1, ItemStack var2, ItemDisplayContext var3, boolean var4, PoseStack var5, MultiBufferSource var6, int var7);

    @Shadow
    protected abstract void applyItemArmAttackTransform(PoseStack var1, HumanoidArm var2, float var3);

    @Inject(at = { @At("HEAD") }, method = { "renderArmWithItem" }, cancellable = true)
    public void renderArmWithItem(AbstractClientPlayer player, float partial_tick, float f9, InteractionHand hand, float f, ItemStack stack, float f2, PoseStack pose, MultiBufferSource buffer, int i, CallbackInfo ci) {
        if (stack.getItem() instanceof GenericBowItem bow) {
            boolean is_right_hand = hand == InteractionHand.MAIN_HAND;
            HumanoidArm arm = is_right_hand ? player.m_5737_() : player.m_5737_().getOpposite();
            pose.pushPose();
            int k = is_right_hand ? 1 : -1;
            if (player.m_6117_() && player.m_21212_() > 0 && player.m_7655_() == hand) {
                this.applyItemArmTransform(pose, arm, f2);
                pose.translate((float) k * -0.2785682F, 0.18344387F, 0.15731531F);
                pose.mulPose(Axis.XP.rotationDegrees(-13.935F));
                pose.mulPose(Axis.YP.rotationDegrees((float) k * 35.3F));
                pose.mulPose(Axis.ZP.rotationDegrees((float) k * -9.785F));
                float pull_time = (float) stack.getUseDuration() - ((float) player.m_21212_() - partial_tick + 1.0F);
                float pull = bow.getPowerForTime(player, pull_time);
                if (pull > 0.1F) {
                    float f15 = Mth.sin((pull_time - 0.1F) * 1.3F);
                    float f18 = pull - 0.1F;
                    float f20 = f15 * f18;
                    pose.translate(0.0F, f20 * 0.004F, 0.0F);
                }
                pose.translate(pull * 0.0F, 0.0F, pull * 0.04F);
                pose.scale(1.0F, 1.0F, 1.0F + pull * 0.2F);
                pose.mulPose(Axis.YN.rotationDegrees((float) k * 45.0F));
            } else {
                float f5 = -0.4F * Mth.sin(Mth.sqrt(f) * (float) Math.PI);
                float f6 = 0.2F * Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2));
                float f10 = -0.2F * Mth.sin(f * (float) Math.PI);
                pose.translate((float) k * f5, f6, f10);
                this.applyItemArmTransform(pose, arm, f2);
                this.applyItemArmAttackTransform(pose, arm, f);
            }
            ItemDisplayContext type = is_right_hand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
            this.renderItem(player, stack, type, !is_right_hand, pose, buffer, i);
            pose.popPose();
            ci.cancel();
        }
    }
}