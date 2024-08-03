package me.jellysquid.mods.lithium.mixin.collections.entity_filtering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.util.ClassInstanceMultiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ClassInstanceMultiMap.class })
public class TypeFilterableListMixin<T> {

    @Shadow
    @Final
    private Map<Class<?>, List<T>> byClass;

    @Shadow
    @Final
    private List<T> allInstances;

    @Overwrite
    public <S> Collection<S> find(Class<S> type) {
        Collection<T> collection = (Collection<T>) this.byClass.get(type);
        if (collection == null) {
            collection = this.createAllOfType(type);
        }
        return Collections.unmodifiableCollection(collection);
    }

    private <S> Collection<T> createAllOfType(Class<S> type) {
        List<T> list = new ArrayList();
        for (T allElement : this.allInstances) {
            if (type.isInstance(allElement)) {
                list.add(allElement);
            }
        }
        this.byClass.put(type, list);
        return list;
    }
}