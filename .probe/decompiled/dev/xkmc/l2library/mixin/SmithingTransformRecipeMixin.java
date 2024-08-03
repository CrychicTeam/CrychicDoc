package dev.xkmc.l2library.mixin;

import java.util.stream.Stream;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ SmithingTransformRecipe.class })
public class SmithingTransformRecipeMixin {

    @Shadow
    @Final
    public Ingredient base;

    @Shadow
    @Final
    public Ingredient addition;

    @Inject(at = { @At("HEAD") }, method = { "isIncomplete" }, cancellable = true)
    public void l2library$isIncomplete$fixJEI(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Stream.of(this.base, this.addition).anyMatch(ForgeHooks::hasNoElements));
    }
}