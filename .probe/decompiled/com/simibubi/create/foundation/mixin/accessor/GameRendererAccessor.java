package com.simibubi.create.foundation.mixin.accessor;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ GameRenderer.class })
public interface GameRendererAccessor {

    @Invoker("getFov")
    double create$callGetFov(Camera var1, float var2, boolean var3);
}