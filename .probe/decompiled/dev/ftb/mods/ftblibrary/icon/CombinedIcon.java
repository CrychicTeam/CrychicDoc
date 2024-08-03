package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CombinedIcon extends Icon {

    public final List<Icon> list;

    public static Icon getCombined(Collection<Icon> icons) {
        List<Icon> list = new ArrayList(icons.size());
        for (Icon icon : icons) {
            if (!icon.isEmpty()) {
                list.add(icon);
            }
        }
        if (list.isEmpty()) {
            return empty();
        } else {
            return (Icon) (list.size() == 1 ? (Icon) list.get(0) : new CombinedIcon(list));
        }
    }

    CombinedIcon(Collection<Icon> icons) {
        this.list = new ArrayList(icons.size());
        for (Icon icon : icons) {
            if (!icon.isEmpty()) {
                this.list.add(icon);
            }
        }
    }

    CombinedIcon(Icon o1, Icon o2) {
        this.list = new ArrayList(2);
        this.list.add(o1);
        this.list.add(o2);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        for (Icon icon : this.list) {
            icon.draw(graphics, x, y, w, h);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawStatic(GuiGraphics graphics, int x, int y, int w, int h) {
        for (Icon icon : this.list) {
            icon.drawStatic(graphics, x, y, w, h);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw3D(GuiGraphics graphics) {
        for (Icon icon : this.list) {
            icon.draw3D(graphics);
        }
    }

    @Override
    public JsonElement getJson() {
        JsonArray json = new JsonArray();
        for (Icon o : this.list) {
            json.add(o.getJson());
        }
        return json;
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof CombinedIcon && this.list.equals(((CombinedIcon) o).list);
    }
}