package dev.latvian.mods.kubejs.client.painter;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.SpecialEquality;
import dev.latvian.mods.unit.FixedBooleanUnit;
import dev.latvian.mods.unit.Unit;
import net.minecraft.nbt.CompoundTag;

public abstract class PainterObject implements SpecialEquality {

    public String id = "";

    public PainterObjectStorage parent;

    public Unit visible = FixedBooleanUnit.TRUE;

    public PainterObject id(String i) {
        this.id = i;
        return this;
    }

    protected void load(PainterObjectProperties properties) {
        this.visible = properties.getUnit("visible", this.visible);
    }

    public final void update(CompoundTag tag) {
        if (tag.getBoolean("remove")) {
            if (this.parent != null) {
                this.parent.remove(this.id);
            }
        } else {
            try {
                this.load(new PainterObjectProperties(tag));
            } catch (Exception var3) {
                ConsoleJS.CLIENT.error("Failed to update Painter object " + this.id + "/" + this.getClass().getSimpleName() + ": " + var3);
            }
        }
    }

    public boolean equals(Object o) {
        return o == this || o instanceof PainterObject && this.id.equals(((PainterObject) o).id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return this.id;
    }

    @Override
    public boolean specialEquals(Object o, boolean shallow) {
        if (this == o || this.id == o) {
            return true;
        } else if (o instanceof PainterObject po) {
            return this.id.equals(po.id);
        } else {
            return o instanceof String ? this.id.equals(this.toString()) : false;
        }
    }
}