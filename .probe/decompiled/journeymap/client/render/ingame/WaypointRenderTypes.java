package journeymap.client.render.ingame;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import journeymap.client.render.RenderWrapper;
import journeymap.client.texture.Texture;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class WaypointRenderTypes extends RenderType {

    static final ResourceLocation WAYPOINT_DEFAULT_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    static final Map<Integer, RenderType> ICON_RENDER_TYPE_MAP = new HashMap();

    protected static final RenderStateShard.TransparencyStateShard ICON_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("icon_transparency", () -> {
        RenderWrapper.enableBlend();
        RenderWrapper.blendFuncSeparate(770, 771, 1, 0);
    }, () -> {
        RenderWrapper.disableBlend();
        RenderWrapper.defaultBlendFunc();
    });

    public static final RenderType BEAM_RENDER_TYPE = m_173215_("waypoint_beam", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(WAYPOINT_DEFAULT_BEAM, true, false)).setCullState(f_110158_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty())).setLayeringState(f_110117_).setTransparencyState(f_110139_).setLightmapState(f_110152_).setWriteMaskState(f_110115_).setShaderState(f_173103_).createCompositeState(false));

    public WaypointRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable pre, Runnable post) {
        super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, pre, post);
    }

    public static RenderType getIcon(Texture texture) {
        int id = texture.getTextureId();
        RenderType type = (RenderType) ICON_RENDER_TYPE_MAP.get(id);
        if (type == null) {
            type = m_173215_("icon" + id, DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setTextureState(new WaypointRenderTypes.IconStateShard(id)).setDepthTestState(f_110111_).setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty())).setLayeringState(f_110117_).setTransparencyState(ICON_TRANSPARENCY).setLightmapState(f_110153_).setCullState(f_110110_).setWriteMaskState(f_110114_).setShaderState(f_173101_).createCompositeState(false));
            ICON_RENDER_TYPE_MAP.put(id, type);
        }
        return type;
    }

    protected static class IconStateShard extends RenderStateShard.EmptyTextureStateShard {

        private final int textureId;

        public IconStateShard(int textureId) {
            super(() -> {
                RenderWrapper.texParameter(3553, 10241, 9729);
                RenderWrapper.texParameter(3553, 10240, 9729);
                RenderWrapper.texParameter(3553, 10242, 10497);
                RenderWrapper.texParameter(3553, 10243, 10497);
                RenderWrapper.setShaderTexture(0, textureId);
            }, () -> {
            });
            this.textureId = textureId;
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[" + this.textureId + ")]";
        }
    }
}