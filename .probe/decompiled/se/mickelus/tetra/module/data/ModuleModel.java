package se.mickelus.tetra.module.data;

import com.mojang.math.Transformation;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import se.mickelus.tetra.module.Priority;

@ParametersAreNonnullByDefault
public class ModuleModel {

    public String type = "item";

    public ResourceLocation location;

    public ResourceLocation renderType;

    public Transformation transform;

    public int emission = 0;

    public int tint = -1;

    public int overlayTint = -1;

    public Priority renderLayer = Priority.BASE;

    public boolean invertPerspectives = false;

    public ItemDisplayContext[] contexts;

    public ModuleModel() {
    }

    public ModuleModel(ResourceLocation location) {
        this.location = location;
    }

    public ModuleModel(String type, ResourceLocation location) {
        this.type = type;
        this.location = location;
    }

    public Priority getRenderLayer() {
        return this.renderLayer;
    }

    public ModuleModel copy() {
        ModuleModel copy = new ModuleModel();
        copy.type = this.type;
        copy.location = this.location;
        copy.renderType = this.renderType;
        copy.transform = this.transform;
        copy.emission = this.emission;
        copy.tint = this.tint;
        copy.overlayTint = this.overlayTint;
        copy.renderLayer = this.renderLayer;
        copy.invertPerspectives = this.invertPerspectives;
        copy.contexts = this.contexts;
        return copy;
    }
}