package dev.latvian.mods.kubejs.util;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.bindings.event.BlockEvents;
import dev.latvian.mods.kubejs.bindings.event.ItemEvents;
import dev.latvian.mods.kubejs.block.BlockModificationEventJS;
import dev.latvian.mods.kubejs.item.ItemModificationEventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.platform.MiscPlatformHelper;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.mod.util.Copyable;
import dev.latvian.mods.rhino.mod.util.MinecraftRemapper;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.util.color.SimpleColorWithAlpha;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ResourceLocationException;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class UtilsJS {

    public static final Random RANDOM = new Random();

    public static final Pattern REGEX_PATTERN = Pattern.compile("/(.*)/([a-z]*)");

    public static final ResourceLocation AIR_LOCATION = new ResourceLocation("minecraft:air");

    public static final Pattern SNAKE_CASE_SPLIT = Pattern.compile("[:_/]");

    public static final Set<String> ALWAYS_LOWER_CASE = new HashSet(Arrays.asList("a", "an", "the", "of", "on", "in", "and", "or", "but", "for"));

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static MinecraftServer staticServer = null;

    public static RegistryAccess staticRegistryAccess = RegistryAccess.EMPTY;

    public static final ResourceLocation UNKNOWN_ID = new ResourceLocation("unknown", "unknown");

    public static final Predicate<Object> ALWAYS_TRUE = o -> true;

    public static final Pattern TEMPORAL_AMOUNT_PATTERN = Pattern.compile("(\\d+)\\s*(y|M|d|w|h|m|s|ms|ns|t)\\b?");

    private static Collection<BlockState> ALL_STATE_CACHE = null;

    private static final Map<String, EntitySelector> ENTITY_SELECTOR_CACHE = new HashMap();

    private static final EntitySelector ALL_ENTITIES_SELECTOR = new EntitySelector(Integer.MAX_VALUE, true, false, e -> true, MinMaxBounds.Doubles.ANY, Function.identity(), null, EntitySelectorParser.ORDER_RANDOM, false, null, null, null, true);

    public static void tryIO(UtilsJS.TryIO tryIO) {
        try {
            tryIO.run();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public static <T> T cast(Object o) {
        return (T) o;
    }

    @Nullable
    public static Pattern parseRegex(Object o) {
        if (o instanceof CharSequence || o instanceof NativeRegExp) {
            return regex(o.toString());
        } else {
            return o instanceof Pattern ? (Pattern) o : null;
        }
    }

    @Nullable
    public static Pattern regex(String string) {
        if (string.length() < 3) {
            return null;
        } else {
            Matcher matcher = REGEX_PATTERN.matcher(string);
            if (matcher.matches()) {
                int flags = 0;
                String f = matcher.group(2);
                for (int i = 0; i < f.length(); i++) {
                    switch(f.charAt(i)) {
                        case 'U':
                            flags |= 256;
                            break;
                        case 'd':
                            flags |= 1;
                            break;
                        case 'i':
                            flags |= 2;
                            break;
                        case 'm':
                            flags |= 8;
                            break;
                        case 's':
                            flags |= 32;
                            break;
                        case 'u':
                            flags |= 64;
                            break;
                        case 'x':
                            flags |= 4;
                    }
                }
                return Pattern.compile(matcher.group(1), flags);
            } else {
                return null;
            }
        }
    }

    public static String toRegexString(Pattern pattern) {
        StringBuilder sb = new StringBuilder("/");
        sb.append(pattern.pattern());
        sb.append('/');
        int flags = pattern.flags();
        if ((flags & 1) != 0) {
            sb.append('d');
        }
        if ((flags & 2) != 0) {
            sb.append('i');
        }
        if ((flags & 4) != 0) {
            sb.append('x');
        }
        if ((flags & 8) != 0) {
            sb.append('m');
        }
        if ((flags & 32) != 0) {
            sb.append('s');
        }
        if ((flags & 64) != 0) {
            sb.append('u');
        }
        if ((flags & 256) != 0) {
            sb.append('U');
        }
        return sb.toString();
    }

    public static void queueIO(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    @Nullable
    public static Path getPath(Object o) {
        try {
            if (o instanceof Path) {
                return KubeJS.verifyFilePath((Path) o);
            } else {
                return o != null && !o.toString().isEmpty() ? KubeJS.verifyFilePath(KubeJS.getGameDirectory().resolve(o.toString())) : null;
            }
        } catch (Exception var2) {
            return null;
        }
    }

    @Nullable
    public static File getFileFromPath(Object o) {
        try {
            if (o instanceof File) {
                return KubeJS.verifyFilePath(((File) o).toPath()).toFile();
            } else {
                return o != null && !o.toString().isEmpty() ? KubeJS.verifyFilePath(KubeJS.getGameDirectory().resolve(o.toString())).toFile() : null;
            }
        } catch (Exception var2) {
            return null;
        }
    }

    @Nullable
    public static Object copy(@Nullable Object o) {
        if (o instanceof Copyable copyable) {
            return copyable.copy();
        } else if (o instanceof JsonElement json) {
            return JsonIO.copy(json);
        } else {
            return o instanceof Tag tag ? tag.copy() : o;
        }
    }

    @Nullable
    public static Object wrap(@Nullable Object o, JSObjectType type) {
        if (o != null && !(o instanceof WrappedJS) && !(o instanceof Tag) && !(o instanceof Number) && !(o instanceof Character) && !(o instanceof String) && !(o instanceof Enum) && (!o.getClass().isPrimitive() || o.getClass().isArray())) {
            if (o instanceof CharSequence || o instanceof ResourceLocation) {
                return o.toString();
            } else if (o instanceof Wrapper w) {
                return wrap(w.unwrap(), type);
            } else if (o instanceof Map) {
                return o;
            } else if (o instanceof Iterable<?> itr) {
                if (!type.checkList()) {
                    return null;
                } else {
                    ArrayList<Object> list = new ArrayList();
                    for (Object o1 : itr) {
                        list.add(o1);
                    }
                    return list;
                }
            } else if (o.getClass().isArray()) {
                return type.checkList() ? ListJS.ofArray(o) : null;
            } else if (o instanceof JsonPrimitive json) {
                return JsonIO.toPrimitive(json);
            } else if (o instanceof JsonObject json) {
                if (!type.checkMap()) {
                    return null;
                } else {
                    HashMap<String, Object> map = new HashMap(json.size());
                    for (Entry<String, JsonElement> entry : json.entrySet()) {
                        map.put((String) entry.getKey(), entry.getValue());
                    }
                    return map;
                }
            } else if (o instanceof JsonNull || o instanceof EndTag) {
                return null;
            } else if (o instanceof CompoundTag tag) {
                if (!type.checkMap()) {
                    return null;
                } else {
                    HashMap<String, Tag> map = new HashMap(tag.size());
                    for (String s : tag.getAllKeys()) {
                        map.put(s, tag.get(s));
                    }
                    return map;
                }
            } else if (o instanceof NumericTag tagx) {
                return tagx.getAsNumber();
            } else {
                return o instanceof StringTag tagx ? tagx.getAsString() : o;
            }
        } else {
            return o;
        }
    }

    public static int parseInt(@Nullable Object object, int def) {
        if (object == null) {
            return def;
        } else if (object instanceof Number num) {
            return num.intValue();
        } else {
            try {
                String s = object.toString();
                return s.isEmpty() ? def : Integer.parseInt(s);
            } catch (Exception var3) {
                return def;
            }
        }
    }

    public static long parseLong(@Nullable Object object, long def) {
        if (object == null) {
            return def;
        } else if (object instanceof Number num) {
            return num.longValue();
        } else {
            try {
                String s = object.toString();
                return s.isEmpty() ? def : Long.parseLong(s);
            } catch (Exception var4) {
                return def;
            }
        }
    }

    public static double parseDouble(@Nullable Object object, double def) {
        if (object == null) {
            return def;
        } else if (object instanceof Number num) {
            return num.doubleValue();
        } else {
            try {
                String s = object.toString();
                return s.isEmpty() ? def : Double.parseDouble(String.valueOf(object));
            } catch (Exception var4) {
                return def;
            }
        }
    }

    public static String getID(@Nullable String s) {
        if (s != null && !s.isEmpty()) {
            return s.indexOf(58) == -1 ? "minecraft:" + s : s;
        } else {
            return "minecraft:air";
        }
    }

    public static ResourceLocation getMCID(@Nullable Context cx, @Nullable Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof ResourceLocation) {
            return (ResourceLocation) o;
        } else if (o instanceof ResourceKey<?> key) {
            return key.location();
        } else {
            String s = o instanceof JsonPrimitive p ? p.getAsString() : o.toString();
            try {
                return new ResourceLocation(s);
            } catch (ResourceLocationException var4) {
                ConsoleJS.getCurrent(cx).error("Could not create ID from '%s'!".formatted(s), var4);
                return null;
            }
        }
    }

    public static String getNamespace(@Nullable String s) {
        if (s != null && !s.isEmpty()) {
            int i = s.indexOf(58);
            return i == -1 ? "minecraft" : s.substring(0, i);
        } else {
            return "minecraft";
        }
    }

    public static String getPath(@Nullable String s) {
        if (s != null && !s.isEmpty()) {
            int i = s.indexOf(58);
            return i == -1 ? s : s.substring(i + 1);
        } else {
            return "air";
        }
    }

    public static BlockState parseBlockState(String string) {
        if (string.isEmpty()) {
            return Blocks.AIR.defaultBlockState();
        } else {
            int i = string.indexOf(91);
            boolean hasProperties = i >= 0 && string.indexOf(93) == string.length() - 1;
            BlockState state = RegistryInfo.BLOCK.getValue(new ResourceLocation(hasProperties ? string.substring(0, i) : string)).defaultBlockState();
            if (hasProperties) {
                for (String s : string.substring(i + 1, string.length() - 1).split(",")) {
                    String[] s1 = s.split("=", 2);
                    if (s1.length == 2 && !s1[0].isEmpty() && !s1[1].isEmpty()) {
                        Property<? extends Comparable<?>> p = (Property<? extends Comparable<?>>) state.m_60734_().getStateDefinition().getProperty(s1[0]);
                        if (p != null) {
                            Optional<?> o = p.getValue(s1[1]);
                            if (o.isPresent()) {
                                state = (BlockState) state.m_61124_(p, cast(o.get()));
                            }
                        }
                    }
                }
            }
            return state;
        }
    }

    public static <T> Predicate<T> onMatchDo(Predicate<T> predicate, Consumer<T> onMatch) {
        return t -> {
            boolean match = predicate.test(t);
            if (match) {
                onMatch.accept(t);
            }
            return match;
        };
    }

    public static List<ItemStack> rollChestLoot(ResourceLocation id, @Nullable Entity entity) {
        ArrayList<ItemStack> list = new ArrayList();
        if (staticServer != null) {
            LootDataManager tables = staticServer.getLootData();
            LootTable table = tables.m_278676_(id);
            LootParams.Builder builder;
            if (entity != null) {
                builder = new LootParams.Builder((ServerLevel) entity.level()).withOptionalParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position());
            } else {
                builder = new LootParams.Builder(staticServer.overworld()).withOptionalParameter(LootContextParams.THIS_ENTITY, null).withParameter(LootContextParams.ORIGIN, Vec3.ZERO);
            }
            table.getRandomItems(builder.create(LootContextParamSets.CHEST), list::add);
        }
        return list;
    }

    public static void postModificationEvents() {
        BlockEvents.MODIFICATION.post(ScriptType.STARTUP, new BlockModificationEventJS());
        ItemEvents.MODIFICATION.post(ScriptType.STARTUP, new ItemModificationEventJS());
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            if (type instanceof ParameterizedType paramType) {
                Type rawType = paramType.getRawType();
                if (rawType instanceof Class) {
                    return (Class<?>) rawType;
                }
            } else {
                if (type instanceof GenericArrayType arrType) {
                    Type componentType = arrType.getGenericComponentType();
                    return Array.newInstance(getRawType(componentType), 0).getClass();
                }
                if (type instanceof TypeVariable) {
                    return Object.class;
                }
                if (type instanceof WildcardType wildcard) {
                    return getRawType(wildcard.getUpperBounds()[0]);
                }
            }
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, GenericArrayType, TypeVariable or WildcardType, but <" + type + "> is of type " + className);
        }
    }

    public static String toMappedTypeString(Type type) {
        MinecraftRemapper remapper = RemappingHelper.getMinecraftRemapper();
        if (type instanceof Class<?> clz) {
            String mapped = remapper.getMappedClass(clz);
            return Strings.isNullOrEmpty(mapped) ? clz.getSimpleName() : mapped.substring(mapped.lastIndexOf(46) + 1);
        } else if (type instanceof ParameterizedType paramType) {
            StringBuilder sb = new StringBuilder();
            Type owner = paramType.getOwnerType();
            if (owner != null) {
                sb.append(toMappedTypeString(owner));
                sb.append('.');
            }
            sb.append(toMappedTypeString(getRawType(paramType)));
            Type[] args = paramType.getActualTypeArguments();
            if (args.length > 0) {
                sb.append('<');
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(toMappedTypeString(args[i]));
                }
                sb.append('>');
            }
            return sb.toString();
        } else if (type instanceof GenericArrayType arrType) {
            return toMappedTypeString(arrType.getGenericComponentType()) + "[]";
        } else if (type instanceof TypeVariable<?> typeVar) {
            StringBuilder sbx = new StringBuilder(typeVar.getName());
            Type[] bounds = typeVar.getBounds();
            if (bounds.length > 0 && (bounds.length != 1 || !Object.class.equals(bounds[0]))) {
                sbx.append(" extends ");
                for (int i = 0; i < bounds.length; i++) {
                    if (i > 0) {
                        sbx.append(" & ");
                    }
                    sbx.append(toMappedTypeString(bounds[i]));
                }
            }
            return sbx.toString();
        } else if (!(type instanceof WildcardType wildcard)) {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, GenericArrayType, TypeVariable or WildcardType, but <" + type + "> is of type " + className);
        } else {
            StringBuilder sbx = new StringBuilder().append("?");
            Type[] lowerBounds = wildcard.getLowerBounds();
            Type[] upperBounds = wildcard.getUpperBounds();
            if (lowerBounds.length > 1 || lowerBounds.length == 1 && lowerBounds[0] != null) {
                sbx.append(" super ");
                for (int i = 0; i < lowerBounds.length; i++) {
                    if (i > 0) {
                        sbx.append(" & ");
                    }
                    sbx.append(toMappedTypeString(lowerBounds[i]));
                }
            } else if (upperBounds.length > 1 || upperBounds.length == 1 && !Object.class.equals(upperBounds[0])) {
                sbx.append(" extends ");
                for (int i = 0; i < upperBounds.length; i++) {
                    if (i > 0) {
                        sbx.append(" & ");
                    }
                    sbx.append(toMappedTypeString(upperBounds[i]));
                }
            }
            return sbx.toString();
        }
    }

    public static String snakeCaseToCamelCase(String string) {
        if (string != null && !string.isEmpty()) {
            String[] s = SNAKE_CASE_SPLIT.split(string, 0);
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String value : s) {
                if (!value.isEmpty()) {
                    if (first) {
                        first = false;
                        sb.append(value);
                    } else {
                        sb.append(Character.toUpperCase(value.charAt(0)));
                        sb.append(value, 1, value.length());
                    }
                }
            }
            return sb.toString();
        } else {
            return string;
        }
    }

    public static String snakeCaseToTitleCase(String string) {
        StringJoiner joiner = new StringJoiner(" ");
        String[] split = string.split("_");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            String titleCase = toTitleCase(s, i == 0);
            joiner.add(titleCase);
        }
        return joiner.toString();
    }

    public static IntProvider intProviderOf(Object o) {
        if (o instanceof Number n) {
            return ConstantInt.of(n.intValue());
        } else {
            if (o instanceof List l && !l.isEmpty()) {
                Number min = (Number) l.get(0);
                Number max = l.size() >= 2 ? (Number) l.get(1) : min;
                return UniformInt.of(min.intValue(), max.intValue());
            }
            if (o instanceof Map<String, Object> m) {
                UniformInt intBounds = parseIntBounds(m);
                if (intBounds != null) {
                    return intBounds;
                }
                if (m.containsKey("clamped")) {
                    IntProvider source = intProviderOf(m.get("clamped"));
                    UniformInt clampTo = parseIntBounds(m);
                    if (clampTo != null) {
                        return ClampedInt.of(source, clampTo.getMinValue(), clampTo.getMaxValue());
                    }
                } else if (m.containsKey("clamped_normal")) {
                    UniformInt clampTo = parseIntBounds(m);
                    int mean = ((Number) m.get("mean")).intValue();
                    int deviation = ((Number) m.get("deviation")).intValue();
                    if (clampTo != null) {
                        return ClampedNormalInt.of((float) mean, (float) deviation, clampTo.getMinValue(), clampTo.getMaxValue());
                    }
                }
                Optional<IntProvider> decoded = IntProvider.CODEC.parse(NbtOps.INSTANCE, NBTUtils.toTagCompound(m)).result();
                if (decoded.isPresent()) {
                    return (IntProvider) decoded.get();
                }
            }
            return ConstantInt.of(0);
        }
    }

    private static UniformInt parseIntBounds(Map<String, Object> m) {
        if (m.get("bounds") instanceof List bounds) {
            return UniformInt.of(parseInt(bounds.get(0), 0), parseInt(bounds.get(1), 0));
        } else if (m.containsKey("min") && m.containsKey("max")) {
            return UniformInt.of(((Number) m.get("min")).intValue(), ((Number) m.get("max")).intValue());
        } else if (m.containsKey("min_inclusive") && m.containsKey("max_inclusive")) {
            return UniformInt.of(((Number) m.get("min_inclusive")).intValue(), ((Number) m.get("max_inclusive")).intValue());
        } else if (m.containsKey("value")) {
            int f = ((Number) m.get("value")).intValue();
            return UniformInt.of(f, f);
        } else {
            return null;
        }
    }

    public static NumberProvider numberProviderOf(Object o) {
        if (o instanceof Number n) {
            float f = n.floatValue();
            return UniformGenerator.between(f, f);
        } else {
            if (o instanceof List l && !l.isEmpty()) {
                Number min = (Number) l.get(0);
                Number max = l.size() >= 2 ? (Number) l.get(1) : min;
                return UniformGenerator.between(min.floatValue(), max.floatValue());
            }
            if (o instanceof Map<String, Object> m) {
                if (m.containsKey("min") && m.containsKey("max")) {
                    return UniformGenerator.between((float) ((Number) m.get("min")).intValue(), ((Number) m.get("max")).floatValue());
                }
                if (m.containsKey("n") && m.containsKey("p")) {
                    return BinomialDistributionGenerator.binomial(((Number) m.get("n")).intValue(), ((Number) m.get("p")).floatValue());
                }
                if (m.containsKey("value")) {
                    float f = ((Number) m.get("value")).floatValue();
                    return UniformGenerator.between(f, f);
                }
            }
            return ConstantValue.exactly(0.0F);
        }
    }

    public static JsonElement numberProviderJson(NumberProvider gen) {
        return Deserializers.createConditionSerializer().create().toJsonTree(gen);
    }

    public static Vec3 vec3Of(@Nullable Object o) {
        if (o instanceof Vec3) {
            return (Vec3) o;
        } else if (o instanceof Entity entity) {
            return entity.position();
        } else {
            if (o instanceof List<?> list && list.size() >= 3) {
                return new Vec3(parseDouble(list.get(0), 0.0), parseDouble(list.get(1), 0.0), parseDouble(list.get(2), 0.0));
            }
            if (o instanceof BlockPos pos) {
                return new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
            } else {
                return o instanceof BlockContainerJS block ? new Vec3((double) block.getPos().m_123341_() + 0.5, (double) block.getPos().m_123342_() + 0.5, (double) block.getPos().m_123343_() + 0.5) : Vec3.ZERO;
            }
        }
    }

    public static BlockPos blockPosOf(@Nullable Object o) {
        if (o instanceof BlockPos) {
            return (BlockPos) o;
        } else {
            if (o instanceof List<?> list && list.size() >= 3) {
                return new BlockPos(parseInt(list.get(0), 0), parseInt(list.get(1), 0), parseInt(list.get(2), 0));
            }
            if (o instanceof BlockContainerJS block) {
                return block.getPos();
            } else {
                return o instanceof Vec3 vec ? BlockPos.containing(vec.x, vec.y, vec.z) : BlockPos.ZERO;
            }
        }
    }

    public static Collection<BlockState> getAllBlockStates() {
        if (ALL_STATE_CACHE != null) {
            return ALL_STATE_CACHE;
        } else {
            HashSet<BlockState> states = new HashSet();
            for (Block block : RegistryInfo.BLOCK.getArchitecturyRegistrar()) {
                states.addAll(block.getStateDefinition().getPossibleStates());
            }
            ALL_STATE_CACHE = Collections.unmodifiableCollection(states);
            return ALL_STATE_CACHE;
        }
    }

    public static String toTitleCase(String s) {
        return toTitleCase(s, false);
    }

    public static String toTitleCase(String s, boolean ignoreSpecial) {
        if (s.isEmpty()) {
            return "";
        } else if (!ignoreSpecial && ALWAYS_LOWER_CASE.contains(s)) {
            return s;
        } else if (s.length() == 1) {
            return s.toUpperCase();
        } else {
            char[] chars = s.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }
    }

    public static String getMobTypeId(MobType type) {
        if (type == MobType.UNDEAD) {
            return "undead";
        } else if (type == MobType.ARTHROPOD) {
            return "arthropod";
        } else if (type == MobType.ILLAGER) {
            return "illager";
        } else {
            return type == MobType.WATER ? "water" : "unknown";
        }
    }

    public static MobCategory mobCategoryByName(String s) {
        return MiscPlatformHelper.get().getMobCategory(s);
    }

    public static String stripIdForEvent(ResourceLocation id) {
        return stripEventName(id.toString());
    }

    public static String getUniqueId(JsonElement json) {
        return getUniqueId(json, Function.identity());
    }

    public static <T> String getUniqueId(T input, Codec<T> codec) {
        return getUniqueId(input, (Function<T, JsonElement>) (o -> (JsonElement) codec.encodeStart(JsonOps.COMPRESSED, o).getOrThrow(false, str -> {
            throw new RuntimeException("Could not encode element to JSON: " + str);
        })));
    }

    private static <T> String getUniqueId(T input, Function<T, JsonElement> toJson) {
        return JsonIO.getJsonHashString((JsonElement) toJson.apply(input));
    }

    public static String stripEventName(String s) {
        return s.replaceAll("[/:]", ".").replace('-', '_');
    }

    public static EntitySelector entitySelector(@Nullable Object o) {
        if (o == null) {
            return ALL_ENTITIES_SELECTOR;
        } else if (o instanceof EntitySelector) {
            return (EntitySelector) o;
        } else {
            String s = o.toString();
            if (s.isBlank()) {
                return ALL_ENTITIES_SELECTOR;
            } else {
                EntitySelector sel = (EntitySelector) ENTITY_SELECTOR_CACHE.get(s);
                if (sel == null) {
                    sel = ALL_ENTITIES_SELECTOR;
                    try {
                        sel = new EntitySelectorParser(new StringReader(s), true).parse();
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }
                ENTITY_SELECTOR_CACHE.put(s, sel);
                return sel;
            }
        }
    }

    @Nullable
    public static CreativeModeTab findCreativeTab(ResourceLocation id) {
        return RegistryInfo.CREATIVE_MODE_TAB.getValue(id);
    }

    public static <T> T makeFunctionProxy(ScriptType type, Class<T> targetClass, BaseFunction function) {
        return cast(NativeJavaObject.createInterfaceAdapter(((ScriptManager) type.manager.get()).context, targetClass, function));
    }

    public static TemporalAmount getTemporalAmount(Object o) {
        if (o instanceof TemporalAmount) {
            return (TemporalAmount) o;
        } else if (o instanceof Number n) {
            return Duration.ofMillis(n.longValue());
        } else if (o instanceof CharSequence) {
            Matcher matcher = TEMPORAL_AMOUNT_PATTERN.matcher(o.toString());
            long millis = 0L;
            long nanos = 0L;
            long ticks = -1L;
            while (matcher.find()) {
                double amount = Double.parseDouble(matcher.group(1));
                String var12 = matcher.group(2);
                switch(var12) {
                    case "t":
                        if (ticks == -1L) {
                            ticks = 0L;
                        }
                        ticks += (long) amount;
                        break;
                    case "ns":
                        nanos += (long) amount;
                        break;
                    case "ms":
                        millis += (long) amount;
                        break;
                    case "s":
                        millis += (long) (amount * 1000.0);
                        break;
                    case "m":
                        millis += (long) (amount * 60000.0);
                        break;
                    case "h":
                        millis += (long) (amount * 60000.0) * 60L;
                        break;
                    case "d":
                        millis += (long) (amount * 86400.0) * 1000L;
                        break;
                    case "w":
                        millis += (long) (amount * 86400.0) * 7000L;
                        break;
                    case "M":
                        millis += (long) (amount * 3.1556952E7 / 12.0) * 1000L;
                        break;
                    case "y":
                        millis += (long) (amount * 3.1556952E7) * 1000L;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid temporal unit: " + matcher.group(2));
                }
            }
            return (TemporalAmount) (ticks != -1L ? new TickDuration(ticks + millis / 50L) : Duration.ofMillis(millis).plusNanos(nanos));
        } else {
            throw new IllegalArgumentException("Invalid temporal amount: " + o);
        }
    }

    public static long getTickDuration(Object o) {
        if (o instanceof Number n) {
            return n.longValue();
        } else if (o instanceof JsonPrimitive json) {
            return json.getAsLong();
        } else {
            TemporalAmount t = getTemporalAmount(o);
            if (t instanceof TickDuration d) {
                return d.ticks();
            } else {
                return t instanceof Duration d ? d.toMillis() / 50L : 0L;
            }
        }
    }

    public static Duration getDuration(Object o) {
        TemporalAmount t = getTemporalAmount(o);
        if (t instanceof Duration) {
            return (Duration) t;
        } else if (t instanceof TickDuration d) {
            return Duration.ofMillis(d.ticks() * 50L);
        } else {
            Duration d = Duration.ZERO;
            for (TemporalUnit unit : t.getUnits()) {
                d = d.plus(t.get(unit), unit);
            }
            return d;
        }
    }

    public static void writeColor(FriendlyByteBuf buf, Color color) {
        buf.writeInt(color.getArgbJS());
    }

    public static Color readColor(FriendlyByteBuf buf) {
        return new SimpleColorWithAlpha(buf.readInt());
    }

    public static void appendTimestamp(StringBuilder builder, Calendar calendar) {
        int h = calendar.get(11);
        int m = calendar.get(12);
        int s = calendar.get(13);
        if (h < 10) {
            builder.append('0');
        }
        builder.append(h);
        builder.append(':');
        if (m < 10) {
            builder.append('0');
        }
        builder.append(m);
        builder.append(':');
        if (s < 10) {
            builder.append('0');
        }
        builder.append(s);
    }

    @FunctionalInterface
    public interface TryIO {

        void run() throws IOException;
    }
}