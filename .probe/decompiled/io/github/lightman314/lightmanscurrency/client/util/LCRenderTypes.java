package io.github.lightman314.lightmanscurrency.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class LCRenderTypes extends RenderStateShard {

    public static final ResourceLocation BLANK_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/blank.png");

    private static final RenderType OUTLINE_TRANSLUCENT = RenderType.create("lightmanscurrency:outline_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(f_173066_).setTextureState(new RenderStateShard.TextureStateShard(BLANK_TEXTURE, false, false)).setTransparencyState(f_110139_).setCullState(f_110110_).setLightmapState(f_110152_).setOverlayState(f_110154_).setWriteMaskState(f_110115_).createCompositeState(false));

    private LCRenderTypes() {
        super(null, null, null);
    }

    public static RenderType getOutlineTranslucent() {
        return OUTLINE_TRANSLUCENT;
    }
}