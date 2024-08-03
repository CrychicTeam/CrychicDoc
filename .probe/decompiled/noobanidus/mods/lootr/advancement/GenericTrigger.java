package noobanidus.mods.lootr.advancement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import noobanidus.mods.lootr.api.advancement.IGenericPredicate;
import org.jetbrains.annotations.NotNull;

public class GenericTrigger<T> implements CriterionTrigger<GenericTrigger.Instance<T>> {

    private final ResourceLocation id;

    private final Map<PlayerAdvancements, GenericTrigger.Listeners<T>> listeners = Maps.newHashMap();

    private final IGenericPredicate<T> predicate;

    public GenericTrigger(String id, IGenericPredicate<T> predicate) {
        this(new ResourceLocation(id), predicate);
    }

    public GenericTrigger(ResourceLocation id, IGenericPredicate<T> predicate) {
        this.id = id;
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void addPlayerListener(@NotNull PlayerAdvancements advancementsIn, @NotNull CriterionTrigger.Listener<GenericTrigger.Instance<T>> listener) {
        GenericTrigger.Listeners<T> list = (GenericTrigger.Listeners<T>) this.listeners.get(advancementsIn);
        if (list == null) {
            list = new GenericTrigger.Listeners<>(advancementsIn);
            this.listeners.put(advancementsIn, list);
        }
        list.add(listener);
    }

    @Override
    public void removePlayerListener(@NotNull PlayerAdvancements advancementsIn, @NotNull CriterionTrigger.Listener<GenericTrigger.Instance<T>> listener) {
        GenericTrigger.Listeners<T> list = (GenericTrigger.Listeners<T>) this.listeners.get(advancementsIn);
        if (list != null) {
            list.remove(listener);
            if (list.isEmpty()) {
                this.listeners.remove(advancementsIn);
            }
        }
    }

    @Override
    public void removePlayerListeners(@NotNull PlayerAdvancements advancementsIn) {
        this.listeners.remove(advancementsIn);
    }

    public GenericTrigger.Instance<T> createInstance(JsonObject jsonObject, DeserializationContext conditionArrayParser) {
        ContextAwarePredicate contextawarepredicate = EntityPredicate.fromJson(jsonObject, "player", conditionArrayParser);
        return this.createInstance(jsonObject, contextawarepredicate, conditionArrayParser);
    }

    public GenericTrigger.Instance<T> createInstance(JsonObject jsonObject, ContextAwarePredicate context, DeserializationContext conditionArrayParser) {
        return new GenericTrigger.Instance<>(this.getId(), context, this.predicate.deserialize(jsonObject));
    }

    public void trigger(ServerPlayer player, T condition) {
        GenericTrigger.Listeners<T> list = (GenericTrigger.Listeners<T>) this.listeners.get(player.getAdvancements());
        if (list != null) {
            list.trigger(player, condition);
        }
    }

    public static class Instance<T> extends AbstractCriterionTriggerInstance {

        IGenericPredicate<T> predicate;

        Instance(ResourceLocation location, ContextAwarePredicate contextPredicate, IGenericPredicate<T> predicate) {
            super(location, contextPredicate);
            this.predicate = predicate;
        }

        public boolean test(ServerPlayer player, T event) {
            return this.predicate.test(player, event);
        }
    }

    public static class Listeners<T> {

        PlayerAdvancements advancements;

        Set<CriterionTrigger.Listener<GenericTrigger.Instance<T>>> listeners = Sets.newHashSet();

        Listeners(PlayerAdvancements advancementsIn) {
            this.advancements = advancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(CriterionTrigger.Listener<GenericTrigger.Instance<T>> listener) {
            this.listeners.add(listener);
        }

        public void remove(CriterionTrigger.Listener<GenericTrigger.Instance<T>> listener) {
            this.listeners.remove(listener);
        }

        void trigger(ServerPlayer player, T condition) {
            List<CriterionTrigger.Listener<GenericTrigger.Instance<T>>> list = Lists.newArrayList();
            for (CriterionTrigger.Listener<GenericTrigger.Instance<T>> listener : this.listeners) {
                if (listener.getTriggerInstance().test(player, condition)) {
                    list.add(listener);
                }
            }
            if (list.size() != 0) {
                for (CriterionTrigger.Listener<GenericTrigger.Instance<T>> listenerx : list) {
                    listenerx.run(this.advancements);
                }
            }
        }
    }
}