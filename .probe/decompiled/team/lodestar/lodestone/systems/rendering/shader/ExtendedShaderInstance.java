package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;

public abstract class ExtendedShaderInstance extends ShaderInstance {

    protected Map<String, Consumer<Uniform>> defaultUniformData;

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ResourceLocation location, VertexFormat pVertexFormat) throws IOException {
        super(pResourceProvider, location, pVertexFormat);
    }

    public void setUniformDefaults() {
        for (Entry<String, Consumer<Uniform>> defaultDataEntry : this.getDefaultUniformData().entrySet()) {
            Uniform t = (Uniform) this.f_173333_.get(defaultDataEntry.getKey());
            ((Consumer) defaultDataEntry.getValue()).accept(t);
            float var4 = 0.0F;
        }
    }

    public abstract ShaderHolder getShaderHolder();

    public Map<String, Consumer<Uniform>> getDefaultUniformData() {
        if (this.defaultUniformData == null) {
            this.defaultUniformData = new HashMap();
        }
        return this.defaultUniformData;
    }

    @Override
    public void parseUniformNode(JsonElement pJson) throws ChainedJsonException {
        super.parseUniformNode(pJson);
        JsonObject jsonobject = GsonHelper.convertToJsonObject(pJson, "uniform");
        String uniformName = GsonHelper.getAsString(jsonobject, "name");
        if (this.getShaderHolder().uniformsToCache.contains(uniformName)) {
            Uniform uniform = (Uniform) this.f_173331_.get(this.f_173331_.size() - 1);
            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                IntBuffer buffer = uniform.getIntBuffer();
                buffer.position(0);
                int[] array = new int[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            } else {
                FloatBuffer buffer = uniform.getFloatBuffer();
                buffer.position(0);
                float[] array = new float[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            }
            this.getDefaultUniformData().put(uniformName, consumer);
        }
    }
}