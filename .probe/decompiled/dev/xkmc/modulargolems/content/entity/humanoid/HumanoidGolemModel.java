package dev.xkmc.modulargolems.content.entity.humanoid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import dev.xkmc.modulargolems.content.entity.common.IHeadedModel;
import dev.xkmc.modulargolems.content.entity.ranged.GolemShooterHelper;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;

public class HumanoidGolemModel extends PlayerModel<HumanoidGolemEntity> implements IGolemModel<HumanoidGolemEntity, HumaniodGolemPartType, HumanoidGolemModel>, IHeadedModel {

    public HumanoidGolemModel(EntityModelSet set) {
        this(set.bakeLayer(ModelLayers.PLAYER), false);
    }

    public HumanoidGolemModel(ModelPart modelPart, boolean slim) {
        super(modelPart, slim);
    }

    public void renderToBufferInternal(HumaniodGolemPartType type, PoseStack stack, VertexConsumer consumer, int i, int j, float f1, float f2, float f3, float f4) {
        if (type == HumaniodGolemPartType.BODY) {
            this.f_102810_.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.f_102808_.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.f_102809_.render(stack, consumer, i, j, f1, f2, f3, f4);
        } else if (type == HumaniodGolemPartType.ARMS) {
            this.f_102812_.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.f_102811_.render(stack, consumer, i, j, f1, f2, f3, f4);
        } else if (type == HumaniodGolemPartType.LEGS) {
            this.f_102814_.render(stack, consumer, i, j, f1, f2, f3, f4);
            this.f_102813_.render(stack, consumer, i, j, f1, f2, f3, f4);
        }
    }

    @Override
    public ResourceLocation getTextureLocationInternal(ResourceLocation rl) {
        String id = rl.getNamespace();
        String mat = rl.getPath();
        return new ResourceLocation(id, "textures/entity/humanoid_golem/" + mat + ".png");
    }

    public void setupAnim(HumanoidGolemEntity entity, float f1, float f2, float f3, float f4, float f5) {
        super.setupAnim(entity, f1, f2, f3, f4, f5);
        if (entity.m_5912_() && this.f_102608_ == 0.0F) {
            if (this.f_102815_ == HumanoidModel.ArmPose.ITEM) {
                this.f_102812_.xRot = -1.8F;
            } else if (this.f_102816_ == HumanoidModel.ArmPose.ITEM) {
                this.f_102811_.xRot = -1.8F;
            }
        }
        this.f_103374_.copyFrom(this.f_102812_);
        this.f_103375_.copyFrom(this.f_102811_);
    }

    protected void setupAttackAnimation(HumanoidGolemEntity entity, float time) {
        if ((this.f_102815_ == HumanoidModel.ArmPose.ITEM || this.f_102816_ == HumanoidModel.ArmPose.ITEM) && this.f_102608_ > 0.0F) {
            AnimationUtils.swingWeaponDown(this.f_102811_, this.f_102812_, entity, this.f_102608_, time);
        } else {
            super.m_7884_(entity, time);
        }
    }

    public void prepareMobModel(HumanoidGolemEntity entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        this.f_102816_ = HumanoidModel.ArmPose.EMPTY;
        this.f_102815_ = HumanoidModel.ArmPose.EMPTY;
        InteractionHand hand = entity.getWeaponHand();
        ItemStack itemstack = entity.m_21120_(hand);
        HumanoidModel.ArmPose pos = HumanoidModel.ArmPose.EMPTY;
        if (entity.m_5912_() && GolemShooterHelper.isValidThrowableWeapon(entity, itemstack, hand).isThrowable() && entity.m_6117_()) {
            pos = HumanoidModel.ArmPose.THROW_SPEAR;
        } else if (entity.m_5912_() && itemstack.getItem() instanceof BowItem) {
            pos = HumanoidModel.ArmPose.BOW_AND_ARROW;
        } else if (itemstack.getItem() instanceof CrossbowItem) {
            if (entity.isChargingCrossbow()) {
                pos = HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            } else if (entity.m_5912_()) {
                pos = HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }
        } else if (entity.m_5912_()) {
            pos = HumanoidModel.ArmPose.ITEM;
        }
        HumanoidModel.ArmPose anti_pos = HumanoidModel.ArmPose.EMPTY;
        if (hand == InteractionHand.OFF_HAND) {
            anti_pos = pos;
            pos = HumanoidModel.ArmPose.EMPTY;
        }
        if (entity.isBlocking()) {
            if (entity.m_21205_().canPerformAction(ToolActions.SHIELD_BLOCK)) {
                pos = HumanoidModel.ArmPose.BLOCK;
            } else {
                anti_pos = HumanoidModel.ArmPose.BLOCK;
            }
        }
        if (entity.m_5737_() == HumanoidArm.RIGHT) {
            this.f_102816_ = pos;
            this.f_102815_ = anti_pos;
        } else {
            this.f_102815_ = pos;
            this.f_102816_ = anti_pos;
        }
        super.m_6839_(entity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    }

    @Override
    public void translateToHead(PoseStack pose) {
        pose.translate(0.0F, -0.25F, 0.0F);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        pose.scale(0.625F, -0.625F, -0.625F);
    }
}