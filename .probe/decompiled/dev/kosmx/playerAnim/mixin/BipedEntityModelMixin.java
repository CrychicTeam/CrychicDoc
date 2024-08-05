package dev.kosmx.playerAnim.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.SetableSupplier;
import dev.kosmx.playerAnim.impl.Helper;
import dev.kosmx.playerAnim.impl.IMutableModel;
import dev.kosmx.playerAnim.impl.IUpperPartHelper;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import java.util.function.Function;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HumanoidModel.class })
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements IMutableModel {

    @Final
    @Shadow
    public ModelPart rightArm;

    @Final
    @Shadow
    public ModelPart leftArm;

    @Unique
    private SetableSupplier<AnimationProcessor> animation = new SetableSupplier<>();

    @Final
    @Shadow
    public ModelPart body;

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart hat;

    @Inject(method = { "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V" }, at = { @At("RETURN") })
    private void initBend(ModelPart modelPart, Function<ResourceLocation, RenderType> function, CallbackInfo ci) {
        IBendHelper.INSTANCE.initBend(modelPart.getChild("body"), Direction.DOWN);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("right_arm"), Direction.UP);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("left_arm"), Direction.UP);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("right_leg"), Direction.UP);
        IBendHelper.INSTANCE.initBend(modelPart.getChild("left_leg"), Direction.UP);
        ((IUpperPartHelper) this.rightArm).setUpperPart(true);
        ((IUpperPartHelper) this.leftArm).setUpperPart(true);
        ((IUpperPartHelper) this.head).setUpperPart(true);
        ((IUpperPartHelper) this.hat).setUpperPart(true);
    }

    @Override
    public void setEmoteSupplier(SetableSupplier<AnimationProcessor> emoteSupplier) {
        this.animation = emoteSupplier;
    }

    @Inject(method = { "copyPropertiesTo" }, at = { @At("RETURN") })
    private void copyMutatedAttributes(HumanoidModel<T> bipedEntityModel, CallbackInfo ci) {
        if (this.animation != null) {
            ((IMutableModel) bipedEntityModel).setEmoteSupplier(this.animation);
        }
    }

    @Intrinsic(displace = true)
    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if (Helper.isBendEnabled() && this.animation.get() != null && this.animation.get().isActive()) {
            this.m_5607_().forEach(part -> {
                if (!((IUpperPartHelper) part).isUpperPart()) {
                    part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
                }
            });
            this.m_5608_().forEach(part -> {
                if (!((IUpperPartHelper) part).isUpperPart()) {
                    part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
                }
            });
            SetableSupplier<AnimationProcessor> emoteSupplier = this.animation;
            matrices.pushPose();
            IBendHelper.rotateMatrixStack(matrices, emoteSupplier.get().getBend("body"));
            this.m_5607_().forEach(part -> {
                if (((IUpperPartHelper) part).isUpperPart()) {
                    part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
                }
            });
            this.m_5608_().forEach(part -> {
                if (((IUpperPartHelper) part).isUpperPart()) {
                    part.render(matrices, vertices, light, overlay, red, green, blue, alpha);
                }
            });
            matrices.popPose();
        } else {
            super.renderToBuffer(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public SetableSupplier<AnimationProcessor> getEmoteSupplier() {
        return this.animation;
    }
}