package fuzs.puzzleslib.impl.client.core.context;

import com.google.common.base.Preconditions;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import fuzs.puzzleslib.api.client.core.v1.context.RenderTypesContext;
import java.util.Objects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;

public final class FluidRenderTypesContextImpl implements RenderTypesContext<Fluid> {

    public void registerRenderType(RenderType renderType, Fluid... fluids) {
        Objects.requireNonNull(renderType, "render type is null");
        Objects.requireNonNull(fluids, "fluids is null");
        Preconditions.checkPositionIndex(1, fluids.length, "fluids is empty");
        for (Fluid fluid : fluids) {
            Objects.requireNonNull(fluid, "fluid is null");
            ClientAbstractions.INSTANCE.registerRenderType(fluid, renderType);
        }
    }

    public RenderType getRenderType(Fluid object) {
        return ClientAbstractions.INSTANCE.getRenderType(object);
    }
}