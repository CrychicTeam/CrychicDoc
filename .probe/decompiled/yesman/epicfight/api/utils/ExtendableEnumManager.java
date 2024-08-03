package yesman.epicfight.api.utils;

import com.google.common.collect.Maps;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.network.chat.Component;
import yesman.epicfight.main.EpicFightMod;

public class ExtendableEnumManager<T extends ExtendableEnum> {

    private final Map<Integer, T> enumMapByOrdinal = Maps.newLinkedHashMap();

    private final Map<String, T> enumMapByName = Maps.newLinkedHashMap();

    private final Map<String, Class<?>> enums = Maps.newConcurrentMap();

    private final String enumName;

    private int lastOrdinal = 0;

    public ExtendableEnumManager(String enumName) {
        this.enumName = enumName;
    }

    public void registerEnumCls(String modid, Class<?> cls) {
        if (this.enums.containsKey(modid)) {
            EpicFightMod.LOGGER.error(modid + " is already registered in " + this.enumName);
        }
        EpicFightMod.LOGGER.debug("Registered Extendable Enum " + cls + " in " + this.enumName);
        this.enums.put(modid, cls);
    }

    public void loadEnum() {
        List<String> orderByModid = new ArrayList(this.enums.keySet());
        Collections.sort(orderByModid);
        Class<?> cls = null;
        try {
            for (String modid : orderByModid) {
                cls = (Class<?>) this.enums.get(modid);
                Method m = cls.getMethod("values");
                m.invoke(null);
                EpicFightMod.LOGGER.debug("Loaded enums in " + cls);
            }
        } catch (ClassCastException var6) {
            EpicFightMod.LOGGER.error(cls.getCanonicalName() + " is not an ExtendableEnum!");
            var6.printStackTrace();
        } catch (NoSuchMethodException var7) {
            EpicFightMod.LOGGER.error(cls.getCanonicalName() + " is not an Enum class!");
            var7.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException var8) {
            EpicFightMod.LOGGER.warn("Error while loading extendable enum " + cls.getCanonicalName());
            var8.printStackTrace();
        }
    }

    public int assign(T value) {
        int lastOrdinal = this.lastOrdinal;
        String enumName = value.toString().toLowerCase(Locale.ROOT);
        if (this.enumMapByName.containsKey(enumName)) {
            throw new IllegalArgumentException("Enum name " + enumName + " already exists in " + this.enumName);
        } else {
            this.enumMapByOrdinal.put(lastOrdinal, value);
            this.enumMapByName.put(enumName, value);
            this.lastOrdinal++;
            return lastOrdinal;
        }
    }

    public T get(int id) {
        if (!this.enumMapByOrdinal.containsKey(id)) {
            throw new IllegalArgumentException("Enum id " + id + " does not exist in " + this.enumName);
        } else {
            return (T) this.enumMapByOrdinal.get(id);
        }
    }

    public T get(String name) {
        String key = name.toLowerCase(Locale.ROOT);
        if (!this.enumMapByName.containsKey(key)) {
            throw new IllegalArgumentException("Enum name " + key + " does not exist in " + this.enumName);
        } else {
            return (T) this.enumMapByName.get(key);
        }
    }

    public Collection<T> universalValues() {
        return this.enumMapByOrdinal.values();
    }

    public String toTranslated(ExtendableEnum e) {
        return Component.translatable("epicfight." + this.enumName + "." + e.toString().toLowerCase(Locale.ROOT)).getString();
    }
}