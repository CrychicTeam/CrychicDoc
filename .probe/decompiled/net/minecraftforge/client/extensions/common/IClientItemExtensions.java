package net.minecraftforge.client.extensions.common;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IClientItemExtensions {

    IClientItemExtensions DEFAULT = new IClientItemExtensions() {
    };

    static IClientItemExtensions of(ItemStack stack) {
        return of(stack.getItem());
    }

    static IClientItemExtensions of(Item item) {
        return item.getRenderPropertiesInternal() instanceof IClientItemExtensions e ? e : DEFAULT;
    }

    @Nullable
    default Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
        return null;
    }

    @Nullable
    default HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return null;
    }

    default boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        return false;
    }

    @NotNull
    default HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return original;
    }

    @NotNull
    default Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        HumanoidModel<?> replacement = this.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
        if (replacement != original) {
            ForgeHooksClient.copyModelProperties(original, replacement);
            return replacement;
        } else {
            return original;
        }
    }

    default void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTick) {
    }

    default BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return Minecraft.getInstance().getItemRenderer().getBlockEntityRenderer();
    }

    public static enum FontContext {

        ITEM_COUNT, TOOLTIP, SELECTED_ITEM_NAME
    }
}