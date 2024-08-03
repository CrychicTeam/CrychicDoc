package dev.latvian.mods.kubejs.block;

import com.google.gson.JsonElement;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Undefined;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.wrap.TypeWrapperFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.level.block.SoundType;

public class SoundTypeWrapper implements TypeWrapperFactory<SoundType> {

    public static final SoundTypeWrapper INSTANCE = new SoundTypeWrapper();

    private Map<String, SoundType> map;

    public Map<String, SoundType> getMap() {
        if (this.map == null) {
            this.map = new LinkedHashMap();
            this.map.put("empty", SoundType.EMPTY);
            try {
                for (Field field : SoundType.class.getFields()) {
                    if (field.getType() == SoundType.class && Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                        try {
                            String r = RemappingHelper.getMinecraftRemapper().getMappedField(SoundType.class, field);
                            this.map.put((r.isBlank() ? field.getName() : r).toLowerCase(), (SoundType) field.get(null));
                        } catch (Exception var6) {
                            var6.printStackTrace();
                        }
                    }
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
        return this.map;
    }

    public SoundType wrap(Context cx, Object o) {
        if (o instanceof SoundType) {
            return (SoundType) o;
        } else {
            return o != null && !Undefined.isUndefined(o) ? (SoundType) this.getMap().getOrDefault((o instanceof JsonElement j ? j.getAsString() : o.toString()).toLowerCase(), SoundType.EMPTY) : SoundType.EMPTY;
        }
    }
}