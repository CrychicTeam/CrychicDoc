package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.ftb.mods.ftblibrary.config.ImageResourceConfig;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class Icon implements Drawable {

    public static Color4I empty() {
        return Color4I.EMPTY_ICON;
    }

    public static Icon getIcon(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                JsonObject o = json.getAsJsonObject();
                if (o.has("id")) {
                    String icon = o.get("id").getAsString();
                    switch(icon) {
                        case "color":
                            Color4I color = Color4I.fromJson(o.get("color"));
                            return (Icon) (o.has("mutable") && o.get("mutable").getAsBoolean() ? color.mutable() : color);
                        case "padding":
                            return getIcon(o.get("parent")).withPadding(o.has("padding") ? o.get("padding").getAsInt() : 0);
                        case "tint":
                            return getIcon(o.get("parent")).withTint(Color4I.fromJson(o.get("color")));
                        case "animation":
                            List<Icon> icons = new ArrayList();
                            for (JsonElement e : o.get("icons").getAsJsonArray()) {
                                icons.add(getIcon(e));
                            }
                            return IconAnimation.fromList(icons, true);
                        case "border":
                            Icon iconx = empty();
                            Color4I outline = empty();
                            boolean roundEdges = false;
                            if (o.has("icon")) {
                                iconx = getIcon(o.get("icon"));
                            }
                            if (o.has("color")) {
                                outline = Color4I.fromJson(o.get("color"));
                            }
                            if (o.has("round_edges")) {
                                roundEdges = o.get("round_edges").getAsBoolean();
                            }
                            return iconx.withBorder(outline, roundEdges);
                        case "bullet":
                            return new BulletIcon().withColor(o.has("color") ? Color4I.fromJson(o.get("color")) : empty());
                        case "part":
                            PartIcon partIcon = new PartIcon(getIcon(o.get("parent")));
                            partIcon.posX = o.get("x").getAsInt();
                            partIcon.posY = o.get("y").getAsInt();
                            partIcon.width = o.get("width").getAsInt();
                            partIcon.height = o.get("height").getAsInt();
                            partIcon.corner = o.get("corner").getAsInt();
                            partIcon.textureWidth = o.get("texture_width").getAsInt();
                            partIcon.textureHeight = o.get("texture_height").getAsInt();
                            return partIcon;
                    }
                }
            } else if (json.isJsonArray()) {
                List<Icon> list = new ArrayList();
                for (JsonElement e : json.getAsJsonArray()) {
                    list.add(getIcon(e));
                }
                return CombinedIcon.getCombined(list);
            }
            String s = json.getAsString();
            if (isNone(s)) {
                return empty();
            } else {
                Icon iconx = (Icon) IconPresets.MAP.get(s);
                return iconx == null ? getIcon(s) : iconx;
            }
        } else {
            return empty();
        }
    }

    public static Icon getIcon(ResourceLocation id) {
        return (Icon) (id == null ? empty() : getIcon(id.toString()));
    }

    public static Icon getIcon(String id) {
        if (isNone(id)) {
            return empty();
        } else {
            String[] comb = id.split(" \\+ ");
            if (comb.length > 1) {
                ArrayList<Icon> list = new ArrayList(comb.length);
                for (String s : comb) {
                    list.add(getIcon(s));
                }
                return CombinedIcon.getCombined(list);
            } else {
                String[] ids = id.split("; ");
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = ids[i].trim();
                }
                Icon icon = getIcon0(ids[0]);
                if (ids.length > 1 && !icon.isEmpty()) {
                    IconProperties properties = new IconProperties();
                    for (int i = 1; i < ids.length; i++) {
                        String[] p = ids[i].split("=", 2);
                        properties.set(p[0], p.length == 1 ? "1" : p[1]);
                    }
                    icon.setProperties(properties);
                    int padding = properties.getInt("padding", 0);
                    if (padding != 0) {
                        icon = icon.withPadding(padding);
                    }
                    Color4I border = properties.getColor("border");
                    if (border != null) {
                        icon = icon.withBorder(border, properties.getBoolean("border_round_edges", false));
                    }
                    Color4I color = properties.getColor("color");
                    if (color != null) {
                        icon = icon.withColor(color);
                    }
                    Color4I tint = properties.getColor("tint");
                    if (tint != null) {
                        icon = icon.withTint(tint);
                    }
                }
                return icon;
            }
        }
    }

    private static Icon getIcon0(String id) {
        if (isNone(id)) {
            return empty();
        } else {
            Color4I col = Color4I.fromString(id);
            if (!col.isEmpty()) {
                return col;
            } else {
                String[] ida = id.split(":", 2);
                if (ida.length == 2) {
                    String var3 = ida[0];
                    switch(var3) {
                        case "color":
                            return Color4I.fromString(ida[1]);
                        case "item":
                            return ItemIcon.getItemIcon(ida[1]);
                        case "bullet":
                            return new BulletIcon().withColor(Color4I.fromString(ida[1]));
                        case "http":
                        case "https":
                        case "file":
                            try {
                                return new URLImageIcon(new URI(id));
                            } catch (Exception var6) {
                                return new ImageIcon(ImageIcon.MISSING_IMAGE);
                            }
                        case "hollow_rectangle":
                            return new HollowRectangleIcon(Color4I.fromString(ida[1]), false);
                        case "part":
                            return new PartIcon(getIcon(ida[1]));
                    }
                }
                return (Icon) (!id.endsWith(".png") && !id.endsWith(".jpg") ? new AtlasSpriteIcon(new ResourceLocation(id)) : new ImageIcon(new ResourceLocation(id)));
            }
        }
    }

    private static boolean isNone(String id) {
        return id.isEmpty() || id.equals("none") || id.equals(ImageResourceConfig.NONE.toString());
    }

    public boolean isEmpty() {
        return false;
    }

    public Icon copy() {
        return this;
    }

    public JsonElement getJson() {
        return new JsonPrimitive(this.toString());
    }

    public final Icon combineWith(Icon icon) {
        if (icon.isEmpty()) {
            return this;
        } else {
            return (Icon) (this.isEmpty() ? icon : new CombinedIcon(this, icon));
        }
    }

    public final Icon combineWith(Icon... icons) {
        if (icons.length == 0) {
            return this;
        } else if (icons.length == 1) {
            return this.combineWith(icons[0]);
        } else {
            List<Icon> list = new ArrayList(icons.length + 1);
            list.add(this);
            list.addAll(Arrays.asList(icons));
            return CombinedIcon.getCombined(list);
        }
    }

    public Icon withColor(Color4I color) {
        return this.copy();
    }

    public final Icon withBorder(Color4I color, boolean roundEdges) {
        return (Icon) (color.isEmpty() ? this.withPadding(1) : new IconWithBorder(this, color, roundEdges));
    }

    public final Icon withPadding(int padding) {
        return (Icon) (padding == 0 ? this : new IconWithPadding(this, padding));
    }

    public Icon withTint(Color4I color) {
        return this;
    }

    public Icon withUV(float u0, float v0, float u1, float v1) {
        return this;
    }

    public Icon withUV(float x, float y, float w, float h, float tw, float th) {
        return this.withUV(x / tw, y / th, (x + w) / tw, (y + h) / th);
    }

    public int hashCode() {
        return this.getJson().hashCode();
    }

    public boolean equals(Object o) {
        return o == this || o instanceof Icon && this.getJson().equals(((Icon) o).getJson());
    }

    public boolean hasPixelBuffer() {
        return false;
    }

    @Nullable
    public PixelBuffer createPixelBuffer() {
        return null;
    }

    @Nullable
    public Object getIngredient() {
        return null;
    }

    protected void setProperties(IconProperties properties) {
    }
}