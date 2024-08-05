package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ShaderInstance;

public record RenderSystemState(int texture0, int texture1, int texture2, ShaderInstance program, float[] shaderColor, BlendFuncDepthFuncState blendFuncDepthFunc) {

    public static RenderSystemState current() {
        return new RenderSystemState(RenderSystem.getShaderTexture(0), RenderSystem.getShaderTexture(1), RenderSystem.getShaderTexture(2), RenderSystem.getShader(), (float[]) RenderSystem.getShaderColor().clone(), BlendFuncDepthFuncState.current());
    }

    public void apply() {
        RenderSystem.setShaderTexture(0, this.texture0);
        RenderSystem.setShaderTexture(1, this.texture1);
        RenderSystem.setShaderTexture(2, this.texture2);
        RenderSystem.setShader(() -> this.program);
        RenderSystem.setShaderColor(this.shaderColor[0], this.shaderColor[1], this.shaderColor[2], this.shaderColor[3]);
        this.blendFuncDepthFunc.apply();
    }
}