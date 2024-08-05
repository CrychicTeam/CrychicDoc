package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

public abstract class SimpleCriterionTrigger<T extends AbstractCriterionTriggerInstance> implements CriterionTrigger<T> {

    private final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<T>>> players = Maps.newIdentityHashMap();

    @Override
    public final void addPlayerListener(PlayerAdvancements playerAdvancements0, CriterionTrigger.Listener<T> criterionTriggerListenerT1) {
        ((Set) this.players.computeIfAbsent(playerAdvancements0, p_66252_ -> Sets.newHashSet())).add(criterionTriggerListenerT1);
    }

    @Override
    public final void removePlayerListener(PlayerAdvancements playerAdvancements0, CriterionTrigger.Listener<T> criterionTriggerListenerT1) {
        Set<CriterionTrigger.Listener<T>> $$2 = (Set<CriterionTrigger.Listener<T>>) this.players.get(playerAdvancements0);
        if ($$2 != null) {
            $$2.remove(criterionTriggerListenerT1);
            if ($$2.isEmpty()) {
                this.players.remove(playerAdvancements0);
            }
        }
    }

    @Override
    public final void removePlayerListeners(PlayerAdvancements playerAdvancements0) {
        this.players.remove(playerAdvancements0);
    }

    protected abstract T createInstance(JsonObject var1, ContextAwarePredicate var2, DeserializationContext var3);

    public final T createInstance(JsonObject jsonObject0, DeserializationContext deserializationContext1) {
        ContextAwarePredicate $$2 = EntityPredicate.fromJson(jsonObject0, "player", deserializationContext1);
        return this.createInstance(jsonObject0, $$2, deserializationContext1);
    }

    protected void trigger(ServerPlayer serverPlayer0, Predicate<T> predicateT1) {
        PlayerAdvancements $$2 = serverPlayer0.getAdvancements();
        Set<CriterionTrigger.Listener<T>> $$3 = (Set<CriterionTrigger.Listener<T>>) this.players.get($$2);
        if ($$3 != null && !$$3.isEmpty()) {
            LootContext $$4 = EntityPredicate.createContext(serverPlayer0, serverPlayer0);
            List<CriterionTrigger.Listener<T>> $$5 = null;
            for (CriterionTrigger.Listener<T> $$6 : $$3) {
                T $$7 = $$6.getTriggerInstance();
                if (predicateT1.test($$7) && $$7.getPlayerPredicate().matches($$4)) {
                    if ($$5 == null) {
                        $$5 = Lists.newArrayList();
                    }
                    $$5.add($$6);
                }
            }
            if ($$5 != null) {
                for (CriterionTrigger.Listener<T> $$8 : $$5) {
                    $$8.run($$2);
                }
            }
        }
    }
}