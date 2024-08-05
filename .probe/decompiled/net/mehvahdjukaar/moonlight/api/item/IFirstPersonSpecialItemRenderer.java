package net.mehvahdjukaar.moonlight.api.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.misc.IExtendedItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IFirstPersonSpecialItemRenderer {

    boolean renderFirstPersonItem(AbstractClientPlayer var1, ItemStack var2, InteractionHand var3, HumanoidArm var4, PoseStack var5, float var6, float var7, float var8, float var9, MultiBufferSource var10, int var11, ItemInHandRenderer var12);

    static void attachToItem(Item target, IFirstPersonSpecialItemRenderer object) {
        if (PlatHelper.getPhysicalSide().isClient()) {
            IExtendedItem extendedItem = (IExtendedItem) target;
            if (extendedItem.moonlight$getClientAnimationExtension() != null && PlatHelper.isDev()) {
                throw new AssertionError("A client animation extension was already registered for this item");
            }
            extendedItem.moonlight$setClientAnimationExtension(object);
        }
    }

    static IFirstPersonSpecialItemRenderer get(Item target) {
        if (target instanceof IFirstPersonSpecialItemRenderer) {
            return (IFirstPersonSpecialItemRenderer) target;
        } else {
            Object var2 = ((IExtendedItem) target).moonlight$getClientAnimationExtension();
            return var2 instanceof IFirstPersonSpecialItemRenderer ? (IFirstPersonSpecialItemRenderer) var2 : null;
        }
    }
}