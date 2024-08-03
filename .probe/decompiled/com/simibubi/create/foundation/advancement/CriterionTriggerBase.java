package com.simibubi.create.foundation.advancement;

import com.google.common.collect.Maps;
import com.simibubi.create.Create;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CriterionTriggerBase<T extends CriterionTriggerBase.Instance> implements CriterionTrigger<T> {

    private final ResourceLocation id;

    protected final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<T>>> listeners = Maps.newHashMap();

    public CriterionTriggerBase(String id) {
        this.id = Create.asResource(id);
    }

    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<T> listener) {
        Set<CriterionTrigger.Listener<T>> playerListeners = (Set<CriterionTrigger.Listener<T>>) this.listeners.computeIfAbsent(playerAdvancementsIn, k -> new HashSet());
        playerListeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<T> listener) {
        Set<CriterionTrigger.Listener<T>> playerListeners = (Set<CriterionTrigger.Listener<T>>) this.listeners.get(playerAdvancementsIn);
        if (playerListeners != null) {
            playerListeners.remove(listener);
            if (playerListeners.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    protected void trigger(ServerPlayer player, @Nullable List<Supplier<Object>> suppliers) {
        PlayerAdvancements playerAdvancements = player.getAdvancements();
        Set<CriterionTrigger.Listener<T>> playerListeners = (Set<CriterionTrigger.Listener<T>>) this.listeners.get(playerAdvancements);
        if (playerListeners != null) {
            List<CriterionTrigger.Listener<T>> list = new LinkedList();
            for (CriterionTrigger.Listener<T> listener : playerListeners) {
                if (listener.getTriggerInstance().test(suppliers)) {
                    list.add(listener);
                }
            }
            list.forEach(listenerx -> listenerx.run(playerAdvancements));
        }
    }

    public abstract static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ResourceLocation idIn, ContextAwarePredicate predicate) {
            super(idIn, predicate);
        }

        protected abstract boolean test(@Nullable List<Supplier<Object>> var1);
    }
}