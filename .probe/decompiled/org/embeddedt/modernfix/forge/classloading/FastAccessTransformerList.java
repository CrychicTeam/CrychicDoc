package org.embeddedt.modernfix.forge.classloading;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraftforge.accesstransformer.AccessTransformer;
import net.minecraftforge.accesstransformer.AccessTransformerEngine;
import net.minecraftforge.accesstransformer.INameHandler;
import net.minecraftforge.accesstransformer.Target;
import net.minecraftforge.accesstransformer.parser.AccessTransformerList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

public class FastAccessTransformerList extends AccessTransformerList {

    private FastAccessTransformerList.FastATMap accessTransformerMap;

    public static void attemptReplace() {
        FastAccessTransformerList myList = new FastAccessTransformerList();
        try {
            Field master = AccessTransformerEngine.class.getDeclaredField("masterList");
            master.setAccessible(true);
            AccessTransformerList masterList = (AccessTransformerList) master.get(AccessTransformerEngine.INSTANCE);
            Field transfomersMap = AccessTransformerList.class.getDeclaredField("accessTransformers");
            transfomersMap.setAccessible(true);
            Map<Target<?>, AccessTransformer> map = (Map<Target<?>, AccessTransformer>) transfomersMap.get(masterList);
            INameHandler nameHandler = (INameHandler) ObfuscationReflectionHelper.getPrivateValue(AccessTransformerList.class, masterList, "nameHandler");
            myList.setNameHandler(nameHandler);
            myList.accessTransformerMap = new FastAccessTransformerList.FastATMap(map);
            ObfuscationReflectionHelper.setPrivateValue(AccessTransformerList.class, myList, myList.accessTransformerMap, "accessTransformers");
            master.set(AccessTransformerEngine.INSTANCE, myList);
        } catch (ReflectiveOperationException var6) {
            var6.printStackTrace();
        }
    }

    public boolean containsClassTarget(Type type) {
        return this.accessTransformerMap.containsType(type);
    }

    private static class FastATMap implements Map<Target<?>, AccessTransformer> {

        private final Map<Target<?>, AccessTransformer> delegate = new HashMap();

        private final Set<Type> allContainedTypes = new ObjectOpenHashSet();

        public FastATMap(Map<Target<?>, AccessTransformer> delegate) {
            this.putAll(delegate);
        }

        public int size() {
            return this.delegate.size();
        }

        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        public boolean containsKey(Object o) {
            return this.delegate.containsKey(o);
        }

        public boolean containsValue(Object o) {
            return this.delegate.containsValue(o);
        }

        public AccessTransformer get(Object o) {
            return (AccessTransformer) this.delegate.get(o);
        }

        @Nullable
        public AccessTransformer put(Target<?> target, AccessTransformer accessTransformer) {
            this.allContainedTypes.add(target.getASMType());
            return (AccessTransformer) this.delegate.put(target, accessTransformer);
        }

        public AccessTransformer remove(Object o) {
            if (o instanceof Target) {
                this.allContainedTypes.remove(((Target) o).getASMType());
            }
            return (AccessTransformer) this.delegate.remove(o);
        }

        public void putAll(@NotNull Map<? extends Target<?>, ? extends AccessTransformer> map) {
            for (Target<?> key : map.keySet()) {
                this.allContainedTypes.add(key.getASMType());
            }
            this.delegate.putAll(map);
        }

        public void clear() {
            this.allContainedTypes.clear();
            this.delegate.clear();
        }

        @NotNull
        public Set<Target<?>> keySet() {
            return this.delegate.keySet();
        }

        @NotNull
        public Collection<AccessTransformer> values() {
            return this.delegate.values();
        }

        @NotNull
        public Set<Entry<Target<?>, AccessTransformer>> entrySet() {
            return this.delegate.entrySet();
        }

        public boolean containsType(Type type) {
            return this.allContainedTypes.contains(type);
        }
    }
}