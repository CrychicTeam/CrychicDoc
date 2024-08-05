package fuzs.puzzleslib.api.client.core.v1.context;

import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Consumer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface CoreShadersContext {

    void registerCoreShader(ResourceLocation var1, VertexFormat var2, Consumer<ShaderInstance> var3);
}