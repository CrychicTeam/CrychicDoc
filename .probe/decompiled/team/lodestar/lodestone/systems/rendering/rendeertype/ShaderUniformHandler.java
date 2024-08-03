package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.ShaderInstance;

public interface ShaderUniformHandler {

    ShaderUniformHandler LUMITRANSPARENT = instance -> instance.safeGetUniform("LumiTransparency").set(1.0F);

    ShaderUniformHandler DEPTH_FADE = instance -> instance.safeGetUniform("DepthFade").set(1.5F);

    ShaderUniformHandler LUMITRANSPARENT_DEPTH_FADE = instance -> {
        instance.safeGetUniform("LumiTransparency").set(1.0F);
        instance.safeGetUniform("DepthFade").set(1.5F);
    };

    void updateShaderData(ShaderInstance var1);
}