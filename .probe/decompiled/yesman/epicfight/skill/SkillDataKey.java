package yesman.epicfight.skill;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.IdMapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;
import yesman.epicfight.api.utils.ClearableIdMapper;
import yesman.epicfight.api.utils.math.Vec3f;

public class SkillDataKey<T> {

    private static final HashMultimap<Class<?>, SkillDataKey<?>> SKILL_DATA_KEYS = HashMultimap.create();

    private static final ResourceLocation CLASS_TO_DATA_KEYS = new ResourceLocation("epicfight", "classtodatakeys");

    private static final ResourceLocation DATA_KEY_TO_ID = new ResourceLocation("epicfight", "datakeytoid");

    private final BiConsumer<ByteBuf, T> encoder;

    private final Function<ByteBuf, T> decoder;

    private final T defaultValue;

    private final boolean syncronizeTrackingPlayers;

    public static SkillDataKey.SkillDataKeyCallbacks getCallBack() {
        return SkillDataKey.SkillDataKeyCallbacks.INSTANCE;
    }

    public static SkillDataKey<Integer> createIntKey(int defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return createSkillDataKey((buffer, val) -> buffer.writeInt(val), buffer -> buffer.readInt(), defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static SkillDataKey<Float> createFloatKey(float defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return createSkillDataKey((buffer, val) -> buffer.writeFloat(val), buffer -> buffer.readFloat(), defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static SkillDataKey<Double> createDoubleKey(double defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return createSkillDataKey((buffer, val) -> buffer.writeDouble(val), buffer -> buffer.readDouble(), defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static SkillDataKey<Boolean> createBooleanKey(boolean defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return createSkillDataKey((buffer, val) -> buffer.writeBoolean(val), buffer -> buffer.readBoolean(), defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static SkillDataKey<Vec3f> createVector3fKey(Vec3f defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return createSkillDataKey((buffer, val) -> {
            buffer.writeFloat(val.x);
            buffer.writeFloat(val.y);
            buffer.writeFloat(val.z);
        }, buffer -> new Vec3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()), defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static SkillDataKey<Vec3> createVector3dKey(Vec3 defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        return createSkillDataKey((buffer, val) -> {
            buffer.writeDouble(val.x);
            buffer.writeDouble(val.y);
            buffer.writeDouble(val.z);
        }, buffer -> new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), defaultValue, syncronizeTrackingPlayers, skillClass);
    }

    public static <T> SkillDataKey<T> createSkillDataKey(BiConsumer<ByteBuf, T> encoder, Function<ByteBuf, T> decoder, T defaultValue, boolean syncronizeTrackingPlayers, Class<?>... skillClass) {
        SkillDataKey<T> key = new SkillDataKey<>(encoder, decoder, defaultValue, syncronizeTrackingPlayers);
        for (Class<?> cls : skillClass) {
            SKILL_DATA_KEYS.put(cls, key);
        }
        return key;
    }

    public static IdMapper<SkillDataKey<?>> getIdMap() {
        return ((IForgeRegistry) SkillDataKeys.REGISTRY.get()).getSlaveMap(DATA_KEY_TO_ID, IdMapper.class);
    }

    public static Map<Class<?>, Set<SkillDataKey<?>>> getSkillDataKeyMap() {
        return ((IForgeRegistry) SkillDataKeys.REGISTRY.get()).getSlaveMap(CLASS_TO_DATA_KEYS, Map.class);
    }

    public static SkillDataKey<Object> byId(int id) {
        return (SkillDataKey<Object>) getIdMap().byId(id);
    }

    public SkillDataKey(BiConsumer<ByteBuf, T> encoder, Function<ByteBuf, T> decoder, T defaultValue, boolean syncronizeTrackingPlayers) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.defaultValue = defaultValue;
        this.syncronizeTrackingPlayers = syncronizeTrackingPlayers;
    }

    public T readFromBuffer(ByteBuf buffer) {
        return (T) this.decoder.apply(buffer);
    }

    public void writeToBuffer(ByteBuf buffer, T value) {
        this.encoder.accept(buffer, value);
    }

    public T defaultValue() {
        return this.defaultValue;
    }

    public int getId() {
        return getIdMap().getId(this);
    }

    public boolean syncronizeTrackingPlayers() {
        return this.syncronizeTrackingPlayers;
    }

    private static class SkillDataKeyCallbacks implements IForgeRegistry.BakeCallback<SkillDataKey<?>>, IForgeRegistry.CreateCallback<SkillDataKey<?>>, IForgeRegistry.ClearCallback<SkillDataKey<?>> {

        static final SkillDataKey.SkillDataKeyCallbacks INSTANCE = new SkillDataKey.SkillDataKeyCallbacks();

        @Override
        public void onBake(IForgeRegistryInternal<SkillDataKey<?>> owner, RegistryManager stage) {
            ClearableIdMapper<SkillDataKey<?>> skillDataKeyMap = owner.getSlaveMap(SkillDataKey.DATA_KEY_TO_ID, ClearableIdMapper.class);
            for (SkillDataKey<?> block : owner) {
                skillDataKeyMap.m_122667_(block);
            }
            Map<Class<?>, Set<SkillDataKey<?>>> skillDataKeys = owner.getSlaveMap(SkillDataKey.CLASS_TO_DATA_KEYS, Map.class);
            for (Class<?> key : SkillDataKey.SKILL_DATA_KEYS.keySet()) {
                Set<SkillDataKey<?>> dataKeySet = Sets.newHashSet();
                dataKeySet.addAll(SkillDataKey.SKILL_DATA_KEYS.get(key));
                skillDataKeys.put(key, dataKeySet);
                for (Class<?> superKey = key.getSuperclass(); superKey != null; superKey = superKey.getSuperclass()) {
                    if (SkillDataKey.SKILL_DATA_KEYS.containsKey(superKey)) {
                        Set<SkillDataKey<?>> dataKeySet$2 = (Set<SkillDataKey<?>>) skillDataKeys.get(key);
                        dataKeySet$2.addAll(SkillDataKey.SKILL_DATA_KEYS.get(superKey));
                    }
                }
            }
            SkillDataKey.SKILL_DATA_KEYS.clear();
        }

        @Override
        public void onCreate(IForgeRegistryInternal<SkillDataKey<?>> owner, RegistryManager stage) {
            owner.setSlaveMap(SkillDataKey.CLASS_TO_DATA_KEYS, Maps.newHashMap());
            owner.setSlaveMap(SkillDataKey.DATA_KEY_TO_ID, new ClearableIdMapper(owner.getKeys().size()));
        }

        @Override
        public void onClear(IForgeRegistryInternal<SkillDataKey<?>> owner, RegistryManager stage) {
            owner.<ClearableIdMapper>getSlaveMap(SkillDataKey.DATA_KEY_TO_ID, ClearableIdMapper.class).clear();
        }
    }
}