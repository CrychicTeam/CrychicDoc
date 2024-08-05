package fuzs.puzzleslib.impl.client.core;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForwardingClientItemExtensions implements IClientItemExtensions {

    private final IClientItemExtensions wrapped;

    public ForwardingClientItemExtensions(IClientItemExtensions wrapped) {
        this.wrapped = wrapped;
    }

    @Nullable
    @Override
    public Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
        return this.wrapped.getFont(stack, context);
    }

    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return this.wrapped.getArmPose(entityLiving, hand, itemStack);
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        return this.wrapped.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
    }

    @NotNull
    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return this.wrapped.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
    }

    @NotNull
    @Override
    public Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return this.wrapped.getGenericArmorModel(livingEntity, itemStack, equipmentSlot, original);
    }

    @Override
    public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTick) {
        this.wrapped.renderHelmetOverlay(stack, player, width, height, partialTick);
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return this.wrapped.getCustomRenderer();
    }
}