package net.mehvahdjukaar.moonlight.api.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.misc.IExtendedItem;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IThirdPersonSpecialItemRenderer {

    <T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> void renderThirdPersonItem(M var1, LivingEntity var2, ItemStack var3, HumanoidArm var4, PoseStack var5, MultiBufferSource var6, int var7);

    static void attachToItem(Item target, IThirdPersonSpecialItemRenderer object) {
        if (PlatHelper.getPhysicalSide().isClient()) {
            IExtendedItem extendedItem = (IExtendedItem) target;
            if (extendedItem.moonlight$getClientAnimationExtension() != null && PlatHelper.isDev()) {
                throw new AssertionError("A client animation extension was already registered for this item");
            }
            extendedItem.moonlight$setClientAnimationExtension(object);
        }
    }

    static IThirdPersonSpecialItemRenderer get(Item target) {
        if (target instanceof IThirdPersonSpecialItemRenderer) {
            return (IThirdPersonSpecialItemRenderer) target;
        } else {
            Object var2 = ((IExtendedItem) target).moonlight$getClientAnimationExtension();
            return var2 instanceof IThirdPersonSpecialItemRenderer ? (IThirdPersonSpecialItemRenderer) var2 : null;
        }
    }
}