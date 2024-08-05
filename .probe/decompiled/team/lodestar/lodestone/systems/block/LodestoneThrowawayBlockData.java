package team.lodestar.lodestone.systems.block;

import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;

public class LodestoneThrowawayBlockData {

    public static final LodestoneThrowawayBlockData EMPTY = new LodestoneThrowawayBlockData();

    private Supplier<Supplier<RenderType>> renderType;

    private boolean hasCustomRenderType;

    public LodestoneThrowawayBlockData setRenderType(Supplier<Supplier<RenderType>> renderType) {
        this.renderType = renderType;
        this.hasCustomRenderType = true;
        return this;
    }

    public Supplier<Supplier<RenderType>> getRenderType() {
        return this.renderType;
    }

    public boolean hasCustomRenderType() {
        return this.hasCustomRenderType;
    }
}