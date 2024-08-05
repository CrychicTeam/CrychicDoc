package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Stack;
import org.joml.Vector3f;

public record LightingState(Vector3f shaderLightDirection0, Vector3f shaderLightDirection1) {

    private static final Stack<LightingState> STACK = new Stack();

    public static LightingState current() {
        return new LightingState(RenderSystem.shaderLightDirections[0], RenderSystem.shaderLightDirections[1]);
    }

    public void saveAndApply() {
        STACK.push(current());
        this.apply();
    }

    public void revert() {
        ((LightingState) STACK.pop()).apply();
    }

    public void apply() {
        RenderSystem.setShaderLights(this.shaderLightDirection0, this.shaderLightDirection1);
    }
}