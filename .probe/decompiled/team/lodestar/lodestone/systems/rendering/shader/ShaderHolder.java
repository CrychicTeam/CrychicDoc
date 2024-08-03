package team.lodestar.lodestone.systems.rendering.shader;

import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

public class ShaderHolder {

    public final ResourceLocation shaderLocation;

    public final VertexFormat shaderFormat;

    protected ExtendedShaderInstance shaderInstance;

    public Collection<String> uniformsToCache;

    private final RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(this.getInstance());

    public ShaderHolder(ResourceLocation shaderLocation, VertexFormat shaderFormat, String... uniformsToCache) {
        this.shaderLocation = shaderLocation;
        this.shaderFormat = shaderFormat;
        this.uniformsToCache = new ArrayList(List.of(uniformsToCache));
    }

    public ExtendedShaderInstance createInstance(ResourceProvider provider) throws IOException {
        final ShaderHolder shaderHolder = this;
        ExtendedShaderInstance shaderInstance = new ExtendedShaderInstance(provider, this.shaderLocation, this.shaderFormat) {

            @Override
            public ShaderHolder getShaderHolder() {
                return shaderHolder;
            }
        };
        this.shaderInstance = shaderInstance;
        return shaderInstance;
    }

    public Supplier<ShaderInstance> getInstance() {
        return () -> this.shaderInstance;
    }

    public void setShaderInstance(ShaderInstance reloadedShaderInstance) {
        this.shaderInstance = (ExtendedShaderInstance) reloadedShaderInstance;
    }

    public RenderStateShard.ShaderStateShard getShard() {
        return this.shard;
    }
}