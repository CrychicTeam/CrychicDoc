package dev.latvian.mods.kubejs.client.painter;

import dev.latvian.mods.kubejs.client.painter.screen.ScreenPainterObject;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.unit.FixedNumberUnit;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.Nullable;

public class PainterObjectStorage {

    private static final ScreenPainterObject[] NO_SCREEN_OBJECTS = new ScreenPainterObject[0];

    public final Painter painter;

    private final Map<String, PainterObject> objects = new LinkedHashMap();

    public PainterObjectStorage(Painter p) {
        this.painter = p;
    }

    @Nullable
    public PainterObject getObject(String key) {
        return (PainterObject) this.objects.get(key);
    }

    public Collection<PainterObject> getObjects() {
        return (Collection<PainterObject>) (this.objects.isEmpty() ? List.of() : this.objects.values());
    }

    public void handle(CompoundTag root) {
        if (root.contains("bulk")) {
            ListTag bulk = root.getList("bulk", 10);
            for (int i = 0; i < bulk.size(); i++) {
                this.handle(bulk.getCompound(i));
            }
        } else {
            if (root.get("*") instanceof CompoundTag tag) {
                if (tag.getBoolean("remove")) {
                    this.objects.clear();
                } else {
                    for (PainterObject o : this.objects.values()) {
                        o.update(tag);
                    }
                }
            }
            if (root.get("$") instanceof CompoundTag tagx) {
                for (String k : tagx.getAllKeys()) {
                    if (tagx.contains(k, 99)) {
                        this.painter.setVariable(k, FixedNumberUnit.of((double) tagx.getFloat(k)));
                    } else {
                        this.painter.setVariable(k, this.painter.unitOf(ConsoleJS.CLIENT, tagx.get(k)));
                    }
                }
            }
            for (String key : root.getAllKeys()) {
                if (!key.equals("*") && !key.equals("$")) {
                    CompoundTag tagx = root.getCompound(key);
                    PainterObject o = (PainterObject) this.objects.get(key);
                    if (o != null) {
                        o.update(tagx);
                    } else if (key.indexOf(32) != -1) {
                        ConsoleJS.CLIENT.error("Painter id can't contain spaces!");
                    } else {
                        String type = tagx.getString("type");
                        PainterObject o1 = this.painter.make(type);
                        if (o1 != null) {
                            o1.id = key;
                            o1.parent = this;
                            o1.update(tagx);
                            this.objects.put(key, o1);
                        } else {
                            ConsoleJS.CLIENT.error("Unknown Painter type: " + type);
                        }
                    }
                }
            }
        }
    }

    public void clear() {
        this.objects.clear();
    }

    public ScreenPainterObject[] createScreenObjects() {
        return this.objects.isEmpty() ? NO_SCREEN_OBJECTS : (ScreenPainterObject[]) this.objects.values().stream().filter(o -> o instanceof ScreenPainterObject).map(o -> (ScreenPainterObject) o).toArray(ScreenPainterObject[]::new);
    }

    public void remove(String id) {
        this.objects.remove(id);
    }
}