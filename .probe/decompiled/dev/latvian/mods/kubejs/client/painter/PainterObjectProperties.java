package dev.latvian.mods.kubejs.client.painter;

import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import dev.latvian.mods.unit.FixedColorUnit;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import dev.latvian.mods.unit.UnitContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PainterObjectProperties {

    public final CompoundTag tag;

    public PainterObjectProperties(CompoundTag t) {
        this.tag = t;
    }

    public boolean hasAny(String key) {
        return this.tag.contains(key);
    }

    public boolean has(String key, int type) {
        return this.tag.contains(key, type);
    }

    public boolean hasNumber(String key) {
        return this.tag.contains(key, 99);
    }

    public boolean hasString(String key) {
        return this.tag.contains(key, 8);
    }

    public String getString(String key, String def) {
        return this.has(key, 8) ? this.tag.getString(key) : def;
    }

    @Nullable
    public ResourceLocation getResourceLocation(String key, @Nullable ResourceLocation def) {
        String s = this.getString(key, "").trim();
        return s.isEmpty() ? def : new ResourceLocation(s);
    }

    public Unit getUnit(String key, Unit def) {
        if (this.hasString(key)) {
            return UnitContext.DEFAULT.parse(this.tag.getString(key));
        } else {
            return (Unit) (this.hasNumber(key) ? FixedNumberUnit.of((double) this.tag.getFloat(key)) : def);
        }
    }

    public Unit getColor(String key, Unit def) {
        if (this.hasString(key)) {
            Color col = (Color) ColorWrapper.MAP.get(this.getString(key, ""));
            if (col != null) {
                return FixedColorUnit.of(col.getArgbJS(), true);
            }
        }
        return this.getUnit(key, def);
    }
}