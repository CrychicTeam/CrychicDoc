package net.minecraft.advancements.critereon;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MobEffectsPredicate {

    public static final MobEffectsPredicate ANY = new MobEffectsPredicate(Collections.emptyMap());

    private final Map<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> effects;

    public MobEffectsPredicate(Map<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> mapMobEffectMobEffectsPredicateMobEffectInstancePredicate0) {
        this.effects = mapMobEffectMobEffectsPredicateMobEffectInstancePredicate0;
    }

    public static MobEffectsPredicate effects() {
        return new MobEffectsPredicate(Maps.newLinkedHashMap());
    }

    public MobEffectsPredicate and(MobEffect mobEffect0) {
        this.effects.put(mobEffect0, new MobEffectsPredicate.MobEffectInstancePredicate());
        return this;
    }

    public MobEffectsPredicate and(MobEffect mobEffect0, MobEffectsPredicate.MobEffectInstancePredicate mobEffectsPredicateMobEffectInstancePredicate1) {
        this.effects.put(mobEffect0, mobEffectsPredicateMobEffectInstancePredicate1);
        return this;
    }

    public boolean matches(Entity entity0) {
        if (this == ANY) {
            return true;
        } else {
            return entity0 instanceof LivingEntity ? this.matches(((LivingEntity) entity0).getActiveEffectsMap()) : false;
        }
    }

    public boolean matches(LivingEntity livingEntity0) {
        return this == ANY ? true : this.matches(livingEntity0.getActiveEffectsMap());
    }

    public boolean matches(Map<MobEffect, MobEffectInstance> mapMobEffectMobEffectInstance0) {
        if (this == ANY) {
            return true;
        } else {
            for (Entry<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> $$1 : this.effects.entrySet()) {
                MobEffectInstance $$2 = (MobEffectInstance) mapMobEffectMobEffectInstance0.get($$1.getKey());
                if (!((MobEffectsPredicate.MobEffectInstancePredicate) $$1.getValue()).matches($$2)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static MobEffectsPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "effects");
            Map<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> $$2 = Maps.newLinkedHashMap();
            for (Entry<String, JsonElement> $$3 : $$1.entrySet()) {
                ResourceLocation $$4 = new ResourceLocation((String) $$3.getKey());
                MobEffect $$5 = (MobEffect) BuiltInRegistries.MOB_EFFECT.getOptional($$4).orElseThrow(() -> new JsonSyntaxException("Unknown effect '" + $$4 + "'"));
                MobEffectsPredicate.MobEffectInstancePredicate $$6 = MobEffectsPredicate.MobEffectInstancePredicate.fromJson(GsonHelper.convertToJsonObject((JsonElement) $$3.getValue(), (String) $$3.getKey()));
                $$2.put($$5, $$6);
            }
            return new MobEffectsPredicate($$2);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            for (Entry<MobEffect, MobEffectsPredicate.MobEffectInstancePredicate> $$1 : this.effects.entrySet()) {
                $$0.add(BuiltInRegistries.MOB_EFFECT.getKey((MobEffect) $$1.getKey()).toString(), ((MobEffectsPredicate.MobEffectInstancePredicate) $$1.getValue()).serializeToJson());
            }
            return $$0;
        }
    }

    public static class MobEffectInstancePredicate {

        private final MinMaxBounds.Ints amplifier;

        private final MinMaxBounds.Ints duration;

        @Nullable
        private final Boolean ambient;

        @Nullable
        private final Boolean visible;

        public MobEffectInstancePredicate(MinMaxBounds.Ints minMaxBoundsInts0, MinMaxBounds.Ints minMaxBoundsInts1, @Nullable Boolean boolean2, @Nullable Boolean boolean3) {
            this.amplifier = minMaxBoundsInts0;
            this.duration = minMaxBoundsInts1;
            this.ambient = boolean2;
            this.visible = boolean3;
        }

        public MobEffectInstancePredicate() {
            this(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, null, null);
        }

        public boolean matches(@Nullable MobEffectInstance mobEffectInstance0) {
            if (mobEffectInstance0 == null) {
                return false;
            } else if (!this.amplifier.matches(mobEffectInstance0.getAmplifier())) {
                return false;
            } else if (!this.duration.matches(mobEffectInstance0.getDuration())) {
                return false;
            } else {
                return this.ambient != null && this.ambient != mobEffectInstance0.isAmbient() ? false : this.visible == null || this.visible == mobEffectInstance0.isVisible();
            }
        }

        public JsonElement serializeToJson() {
            JsonObject $$0 = new JsonObject();
            $$0.add("amplifier", this.amplifier.m_55328_());
            $$0.add("duration", this.duration.m_55328_());
            $$0.addProperty("ambient", this.ambient);
            $$0.addProperty("visible", this.visible);
            return $$0;
        }

        public static MobEffectsPredicate.MobEffectInstancePredicate fromJson(JsonObject jsonObject0) {
            MinMaxBounds.Ints $$1 = MinMaxBounds.Ints.fromJson(jsonObject0.get("amplifier"));
            MinMaxBounds.Ints $$2 = MinMaxBounds.Ints.fromJson(jsonObject0.get("duration"));
            Boolean $$3 = jsonObject0.has("ambient") ? GsonHelper.getAsBoolean(jsonObject0, "ambient") : null;
            Boolean $$4 = jsonObject0.has("visible") ? GsonHelper.getAsBoolean(jsonObject0, "visible") : null;
            return new MobEffectsPredicate.MobEffectInstancePredicate($$1, $$2, $$3, $$4);
        }
    }
}