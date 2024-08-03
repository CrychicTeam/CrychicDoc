package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

public class LodestoneRenderType extends RenderType {

    public final RenderType.CompositeState state;

    private final Optional<RenderType> outline;

    private final boolean isOutline;

    public static LodestoneRenderType createRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, RenderType.CompositeState pState) {
        return new LodestoneRenderType(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pState);
    }

    public LodestoneRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, RenderType.CompositeState pState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, () -> pState.states.forEach(RenderStateShard::m_110185_), () -> pState.states.forEach(RenderStateShard::m_110188_));
        this.state = pState;
        this.outline = pState.outlineProperty == RenderType.OutlineProperty.AFFECTS_OUTLINE ? pState.textureState.cutoutTexture().map(p_173270_ -> (RenderType) RenderType.CompositeRenderType.OUTLINE.apply(p_173270_, pState.cullState)) : Optional.empty();
        this.isOutline = pState.outlineProperty == RenderType.OutlineProperty.IS_OUTLINE;
    }

    @Override
    public void end(BufferBuilder builder, VertexSorting sorting) {
        if (builder.building()) {
            if (this.f_110393_) {
                builder.setQuadSorting(sorting);
            }
            BufferBuilder.RenderedBuffer buffer = builder.end();
            if (buffer.isEmpty()) {
                buffer.release();
                return;
            }
            this.m_110185_();
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.depthMask(false);
            BufferUploader.drawWithShader(buffer);
            if (Minecraft.getInstance().levelRenderer.transparencyChain != null) {
                RenderSystem.colorMask(false, false, false, false);
                RenderSystem.depthMask(true);
                ShaderInstance shader = RenderSystem.getShader();
                shader.apply();
                BufferUploader.lastImmediateBuffer.draw();
                shader.clear();
            }
            this.m_110188_();
            RenderSystem.colorMask(true, true, true, true);
        }
    }

    @Override
    public Optional<RenderType> outline() {
        return this.outline;
    }

    @Override
    public boolean isOutline() {
        return this.isOutline;
    }

    protected final RenderType.CompositeState state() {
        return this.state;
    }

    @Override
    public String toString() {
        return "RenderType[" + this.f_110133_ + ":" + this.state + "]";
    }
}