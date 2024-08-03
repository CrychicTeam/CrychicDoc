package org.embeddedt.modernfix.registry;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

public class LifecycleMap<T> extends Reference2ReferenceOpenHashMap<T, Lifecycle> {

    public LifecycleMap() {
        this.defaultReturnValue(Lifecycle.stable());
    }

    public Lifecycle put(T t, Lifecycle lifecycle) {
        if (lifecycle != this.defRetValue) {
            return (Lifecycle) super.put(t, lifecycle);
        } else {
            return super.containsKey(t) ? (Lifecycle) super.get(t) : null;
        }
    }
}