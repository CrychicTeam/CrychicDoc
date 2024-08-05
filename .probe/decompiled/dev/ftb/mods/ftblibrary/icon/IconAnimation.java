package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class IconAnimation extends Icon {

    public final List<Icon> list;

    public static Icon fromList(List<Icon> icons, boolean includeEmpty) {
        List<Icon> list = new ArrayList(icons.size());
        for (Icon icon : icons) {
            if (icon instanceof IconAnimation) {
                for (Icon icon1 : ((IconAnimation) icon).list) {
                    if (includeEmpty || !icon1.isEmpty()) {
                        list.add(icon1);
                    }
                }
            } else if (includeEmpty || !icon.isEmpty()) {
                list.add(icon);
            }
        }
        if (list.isEmpty()) {
            return empty();
        } else {
            return (Icon) (list.size() == 1 ? (Icon) list.get(0) : new IconAnimation(list));
        }
    }

    private IconAnimation(List<Icon> l) {
        this.list = l;
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        if (!this.list.isEmpty()) {
            ((Icon) this.list.get((int) (System.currentTimeMillis() / 1000L % (long) this.list.size()))).draw(graphics, x, y, w, h);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawStatic(GuiGraphics graphics, int x, int y, int w, int h) {
        if (!this.list.isEmpty()) {
            ((Icon) this.list.get(0)).drawStatic(graphics, x, y, w, h);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw3D(GuiGraphics graphics) {
        if (!this.list.isEmpty()) {
            ((Icon) this.list.get((int) (System.currentTimeMillis() / 1000L % (long) this.list.size()))).draw3D(graphics);
        }
    }

    @Override
    public JsonElement getJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", "animation");
        JsonArray array = new JsonArray();
        for (Icon icon : this.list) {
            array.add(icon.getJson());
        }
        json.add("icons", array);
        return json;
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof IconAnimation && this.list.equals(((IconAnimation) o).list);
    }

    @Nullable
    @Override
    public Object getIngredient() {
        return !this.list.isEmpty() ? ((Icon) this.list.get((int) (System.currentTimeMillis() / 1000L % (long) this.list.size()))).getIngredient() : null;
    }
}