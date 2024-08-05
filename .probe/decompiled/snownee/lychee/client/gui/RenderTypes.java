package snownee.lychee.client.gui;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class RenderTypes extends RenderStateShard {

    private static final RenderType FLUID = RenderType.create(createLayerName("fluid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(f_110145_).setTransparencyState(f_110139_).setLightmapState(f_110152_).setOverlayState(f_110154_).createCompositeState(true));

    public static RenderType getFluid() {
        return FLUID;
    }

    private static String createLayerName(String name) {
        return "lychee:" + name;
    }

    private RenderTypes() {
        super(null, null, null);
    }
}