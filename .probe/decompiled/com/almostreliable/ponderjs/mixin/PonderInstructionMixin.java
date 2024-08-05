package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.util.PonderErrorHelper;
import com.simibubi.create.foundation.ponder.PonderScene;
import dev.latvian.mods.rhino.RhinoException;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "com.simibubi.create.foundation.ponder.instruction.PonderInstruction$Simple" })
public class PonderInstructionMixin {

    @Shadow(remap = false)
    private Consumer<PonderScene> callback;

    @Inject(method = { "<init>" }, at = { @At("RETURN") }, remap = false)
    private void init(Consumer<PonderScene> argCallback, CallbackInfo ci) {
        this.callback = ponderScene -> {
            try {
                argCallback.accept(ponderScene);
            } catch (RhinoException var3) {
                PonderErrorHelper.yeet(var3);
                if (Minecraft.getInstance() != null) {
                    Minecraft.getInstance().setScreen(null);
                }
            }
        };
    }
}