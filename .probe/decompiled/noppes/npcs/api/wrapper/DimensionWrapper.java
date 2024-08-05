package noppes.npcs.api.wrapper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import noppes.npcs.api.IDimension;

public class DimensionWrapper implements IDimension {

    private ResourceLocation id;

    private DimensionType type;

    public DimensionWrapper(ResourceLocation id, DimensionType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getId() {
        return this.id.toString();
    }
}