package dev.kosmx.playerAnim.mixin;

import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.SetableSupplier;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.IMutableModel;
import dev.kosmx.playerAnim.impl.IPlayerModel;
import dev.kosmx.playerAnim.impl.IUpperPartHelper;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import java.util.function.Function;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { PlayerModel.class }, priority = 2000)
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> implements IPlayerModel {

    @Shadow
    @Final
    public ModelPart jacket;

    @Shadow
    @Final
    public ModelPart rightSleeve;

    @Shadow
    @Final
    public ModelPart leftSleeve;

    @Shadow
    @Final
    public ModelPart rightPants;

    @Shadow
    @Final
    public ModelPart leftPants;

    @Unique
    private final SetableSupplier<AnimationProcessor> emoteSupplier = new SetableSupplier<>();

    @Unique
    private boolean firstPersonNext = false;

    public PlayerModelMixin(ModelPart modelPart, Function<ResourceLocation, RenderType> function) {
        super(modelPart, function);
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void initBendableStuff(ModelPart modelPart, boolean bl, CallbackInfo ci) {
        IMutableModel thisWithMixin = (IMutableModel) this;
        this.emoteSupplier.set(null);
        thisWithMixin.setEmoteSupplier(this.emoteSupplier);
        this.addBendMutator(this.jacket, Direction.DOWN);
        this.addBendMutator(this.rightPants, Direction.UP);
        this.addBendMutator(this.rightSleeve, Direction.UP);
        this.addBendMutator(this.leftPants, Direction.UP);
        this.addBendMutator(this.leftSleeve, Direction.UP);
        ((IUpperPartHelper) this.rightSleeve).setUpperPart(true);
        ((IUpperPartHelper) this.leftSleeve).setUpperPart(true);
    }

    @Unique
    private void addBendMutator(ModelPart part, Direction d) {
        IBendHelper.INSTANCE.initBend(part, d);
    }

    @Unique
    private void setDefaultPivot() {
        this.f_102814_.setPos(1.9F, 12.0F, 0.0F);
        this.f_102813_.setPos(-1.9F, 12.0F, 0.0F);
        this.f_102808_.setPos(0.0F, 0.0F, 0.0F);
        this.f_102811_.z = 0.0F;
        this.f_102811_.x = -5.0F;
        this.f_102812_.z = 0.0F;
        this.f_102812_.x = 5.0F;
        this.f_102810_.xRot = 0.0F;
        this.f_102813_.z = 0.1F;
        this.f_102814_.z = 0.1F;
        this.f_102813_.y = 12.0F;
        this.f_102814_.y = 12.0F;
        this.f_102808_.y = 0.0F;
        this.f_102808_.zRot = 0.0F;
        this.f_102810_.y = 0.0F;
        this.f_102810_.x = 0.0F;
        this.f_102810_.z = 0.0F;
        this.f_102810_.yRot = 0.0F;
        this.f_102810_.zRot = 0.0F;
    }

    @Inject(method = { "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V" }, at = { @At("HEAD") })
    private void setDefaultBeforeRender(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        this.setDefaultPivot();
    }

    @Inject(method = { "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 0) })
    private void setEmote(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!this.firstPersonNext && livingEntity instanceof AbstractClientPlayer && ((IAnimatedPlayer) livingEntity).playerAnimator_getAnimation().isActive()) {
            AnimationApplier emote = ((IAnimatedPlayer) livingEntity).playerAnimator_getAnimation();
            this.emoteSupplier.set(emote);
            emote.updatePart("head", this.f_102808_);
            this.f_102809_.copyFrom(this.f_102808_);
            emote.updatePart("leftArm", this.f_102812_);
            emote.updatePart("rightArm", this.f_102811_);
            emote.updatePart("leftLeg", this.f_102814_);
            emote.updatePart("rightLeg", this.f_102813_);
            emote.updatePart("torso", this.f_102810_);
        } else {
            this.firstPersonNext = false;
            this.emoteSupplier.set(null);
            resetBend(this.f_102810_);
            resetBend(this.f_102812_);
            resetBend(this.f_102811_);
            resetBend(this.f_102814_);
            resetBend(this.f_102813_);
        }
    }

    @Unique
    private static void resetBend(ModelPart part) {
        IBendHelper.INSTANCE.bend(part, null);
    }

    @Override
    public void playerAnimator_prepForFirstPersonRender() {
        this.firstPersonNext = true;
    }
}