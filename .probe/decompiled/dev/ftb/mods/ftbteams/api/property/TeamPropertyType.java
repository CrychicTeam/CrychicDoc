package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TeamPropertyType<T> {

    private static final Map<String, TeamPropertyType<?>> MAP = new HashMap();

    public static final TeamPropertyType<Boolean> BOOLEAN = register("boolean", BooleanProperty::fromNetwork);

    public static final TeamPropertyType<String> STRING = register("string", StringProperty::fromNetwork);

    public static final TeamPropertyType<List<String>> STRING_LIST = register("string_list", StringListProperty::fromNetwork);

    public static final TeamPropertyType<Integer> INT = register("int", IntProperty::fromNetwork);

    public static final TeamPropertyType<Double> DOUBLE = register("double", DoubleProperty::fromNetwork);

    public static final TeamPropertyType<Color4I> COLOR = register("color", ColorProperty::fromNetwork);

    public static final TeamPropertyType<String> ENUM = register("enum", EnumProperty::fromNetwork);

    public static final TeamPropertyType<PrivacyMode> PRIVACY_MODE = register("privacy_mode", PrivacyProperty::fromNetwork);

    private final String id;

    private final TeamPropertyType.FromNet<T> deserializer;

    private TeamPropertyType(String id, TeamPropertyType.FromNet<T> deserializer) {
        this.id = id;
        this.deserializer = deserializer;
    }

    public static TeamProperty<?> read(FriendlyByteBuf buf) {
        return ((TeamPropertyType) MAP.get(buf.readUtf(32767))).deserializer.apply(buf.readResourceLocation(), buf);
    }

    public static void write(FriendlyByteBuf buf, TeamProperty<?> p) {
        buf.writeUtf(p.getType().id, 32767);
        buf.writeResourceLocation(p.id);
        p.write(buf);
    }

    private static <Y> TeamPropertyType<Y> register(String id, TeamPropertyType.FromNet<Y> p) {
        TeamPropertyType<Y> t = new TeamPropertyType<>(id, p);
        MAP.put(id, t);
        return t;
    }

    public interface FromNet<Y> {

        TeamProperty<Y> apply(ResourceLocation var1, FriendlyByteBuf var2);
    }
}