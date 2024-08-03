package team.lodestar.lodestone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;

@Mixin({ GuiGraphics.class })
public abstract class GuiGraphicsMixin {

    @Shadow
    @Final
    private PoseStack pose;

    @Inject(at = { @At("HEAD") }, method = { "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V" })
    private void lodestone$renderGuiItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int int0, int int1, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStackEarly(this.pose, stack, x, y);
    }

    @Inject(at = { @At("TAIL") }, method = { "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V" })
    private void lodestone$renderGuiItemLate(LivingEntity pEntity, Level pLevel, ItemStack pStack, int pX, int pY, int pSeed, int pGuiOffset, CallbackInfo ci) {
        ScreenParticleHandler.renderItemStackLate();
    }
}