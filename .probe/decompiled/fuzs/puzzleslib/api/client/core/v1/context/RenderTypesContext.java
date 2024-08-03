package fuzs.puzzleslib.api.client.core.v1.context;

import net.minecraft.client.renderer.RenderType;

public interface RenderTypesContext<T> {

    void registerRenderType(RenderType var1, T... var2);

    RenderType getRenderType(T var1);
}