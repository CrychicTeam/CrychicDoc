package net.mehvahdjukaar.moonlight.api.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.misc.IExtendedItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IFirstPersonAnimationProvider {

    @Deprecated(forRemoval = true)
    default void animateItemFirstPerson(LivingEntity entity, ItemStack stack, InteractionHand hand, PoseStack matrixStack, float partialTicks, float pitch, float attackAnim, float handHeight) {
    }

    default void animateItemFirstPerson(Player entity, ItemStack stack, InteractionHand hand, HumanoidArm arm, PoseStack poseStack, float partialTicks, float pitch, float attackAnim, float handHeight) {
        this.animateItemFirstPerson(entity, stack, hand, poseStack, partialTicks, pitch, attackAnim, handHeight);
    }

    static void attachToItem(Item target, IFirstPersonAnimationProvider object) {
        if (PlatHelper.getPhysicalSide().isClient()) {
            IExtendedItem extendedItem = (IExtendedItem) target;
            if (extendedItem.moonlight$getClientAnimationExtension() != null && PlatHelper.isDev()) {
                throw new AssertionError("A client animation extension was already registered for this item");
            }
            extendedItem.moonlight$setClientAnimationExtension(object);
        }
    }

    static IFirstPersonAnimationProvider get(Item target) {
        if (target instanceof IFirstPersonAnimationProvider) {
            return (IFirstPersonAnimationProvider) target;
        } else {
            Object var2 = ((IExtendedItem) target).moonlight$getClientAnimationExtension();
            return var2 instanceof IFirstPersonAnimationProvider ? (IFirstPersonAnimationProvider) var2 : null;
        }
    }
}