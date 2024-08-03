package me.jellysquid.mods.sodium.mixin.features.render.immediate.buffer_builder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import java.util.stream.IntStream;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({ VertexBuffer.class })
public class VertexBufferMixin {

    private static final int NUM_SAMPLERS = 12;

    private static final String[] SAMPLER_IDS = (String[]) IntStream.range(0, 12).mapToObj(i -> "Sampler" + i).toArray(String[]::new);

    @ModifyConstant(method = { "_drawWithShader" }, constant = { @Constant(intValue = 12, ordinal = 0) })
    private int setSamplersManually(int constant, Matrix4f mat1, Matrix4f mat2, ShaderInstance shader) {
        for (int i = 0; i < constant; i++) {
            shader.setSampler(SAMPLER_IDS[i], RenderSystem.getShaderTexture(i));
        }
        return 0;
    }
}