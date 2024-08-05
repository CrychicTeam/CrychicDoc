package dev.xkmc.l2library.serial.advancements;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import java.util.function.BiFunction;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;

public class BaseCriterion<T extends BaseCriterionInstance<T, R>, R extends BaseCriterion<T, R>> extends SimpleCriterionTrigger<T> {

    private final ResourceLocation id;

    private final BiFunction<ResourceLocation, ContextAwarePredicate, T> factory;

    private final Class<T> cls;

    public BaseCriterion(ResourceLocation id, BiFunction<ResourceLocation, ContextAwarePredicate, T> factory, Class<T> cls) {
        this.id = id;
        this.factory = factory;
        this.cls = cls;
        CriteriaTriggers.register(this);
    }

    protected T createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
        T ans = (T) this.factory.apply(this.id, player);
        JsonCodec.from(json, this.cls, ans);
        return ans;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
}