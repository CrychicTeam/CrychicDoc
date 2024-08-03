package io.redspace.ironsspellbooks.render;

import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class StaffArmPose {

    @OnlyIn(Dist.CLIENT)
    public static HumanoidModel.ArmPose STAFF_ARM_POS = HumanoidModel.ArmPose.create("IRONS_SPELLBOOKS_STAFF", false, (model, entity, arm) -> (arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm).xRot = Mth.lerp(0.85F, (arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm).xRot, (float) (-Math.PI * 2.0 / 7.0) + model.head.xRot / 2.0F));

    public static void initializeClientHelper(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Nullable
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return StaffArmPose.STAFF_ARM_POS;
            }
        });
    }
}