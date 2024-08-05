package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

public class LodestoneBufferWrapper implements MultiBufferSource {

    public final RenderTypeProvider provider;

    public final MultiBufferSource buffer;

    public LodestoneBufferWrapper(RenderTypeProvider provider, MultiBufferSource buffer) {
        this.provider = provider;
        this.buffer = buffer;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return renderType instanceof RenderType.CompositeRenderType composite ? this.buffer.getBuffer(this.provider.applyAndCache(RenderTypeToken.createCachedToken(composite.state.textureState))) : this.buffer.getBuffer(renderType);
    }
}