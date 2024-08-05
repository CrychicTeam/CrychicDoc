package net.minecraft.advancements.critereon;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PlayerPredicate implements EntitySubPredicate {

    public static final int LOOKING_AT_RANGE = 100;

    private final MinMaxBounds.Ints level;

    @Nullable
    private final GameType gameType;

    private final Map<Stat<?>, MinMaxBounds.Ints> stats;

    private final Object2BooleanMap<ResourceLocation> recipes;

    private final Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> advancements;

    private final EntityPredicate lookingAt;

    private static PlayerPredicate.AdvancementPredicate advancementPredicateFromJson(JsonElement jsonElement0) {
        if (jsonElement0.isJsonPrimitive()) {
            boolean $$1 = jsonElement0.getAsBoolean();
            return new PlayerPredicate.AdvancementDonePredicate($$1);
        } else {
            Object2BooleanMap<String> $$2 = new Object2BooleanOpenHashMap();
            JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement0, "criterion data");
            $$3.entrySet().forEach(p_62288_ -> {
                boolean $$2x = GsonHelper.convertToBoolean((JsonElement) p_62288_.getValue(), "criterion test");
                $$2.put((String) p_62288_.getKey(), $$2x);
            });
            return new PlayerPredicate.AdvancementCriterionsPredicate($$2);
        }
    }

    PlayerPredicate(MinMaxBounds.Ints minMaxBoundsInts0, @Nullable GameType gameType1, Map<Stat<?>, MinMaxBounds.Ints> mapStatMinMaxBoundsInts2, Object2BooleanMap<ResourceLocation> objectBooleanMapResourceLocation3, Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> mapResourceLocationPlayerPredicateAdvancementPredicate4, EntityPredicate entityPredicate5) {
        this.level = minMaxBoundsInts0;
        this.gameType = gameType1;
        this.stats = mapStatMinMaxBoundsInts2;
        this.recipes = objectBooleanMapResourceLocation3;
        this.advancements = mapResourceLocationPlayerPredicateAdvancementPredicate4;
        this.lookingAt = entityPredicate5;
    }

    @Override
    public boolean matches(Entity entity0, ServerLevel serverLevel1, @Nullable Vec3 vec2) {
        if (!(entity0 instanceof ServerPlayer $$3)) {
            return false;
        } else if (!this.level.matches($$3.f_36078_)) {
            return false;
        } else if (this.gameType != null && this.gameType != $$3.gameMode.getGameModeForPlayer()) {
            return false;
        } else {
            StatsCounter $$4 = $$3.getStats();
            for (Entry<Stat<?>, MinMaxBounds.Ints> $$5 : this.stats.entrySet()) {
                int $$6 = $$4.getValue((Stat<?>) $$5.getKey());
                if (!((MinMaxBounds.Ints) $$5.getValue()).matches($$6)) {
                    return false;
                }
            }
            RecipeBook $$7 = $$3.getRecipeBook();
            ObjectIterator var13 = this.recipes.object2BooleanEntrySet().iterator();
            while (var13.hasNext()) {
                it.unimi.dsi.fastutil.objects.Object2BooleanMap.Entry<ResourceLocation> $$8 = (it.unimi.dsi.fastutil.objects.Object2BooleanMap.Entry<ResourceLocation>) var13.next();
                if ($$7.contains((ResourceLocation) $$8.getKey()) != $$8.getBooleanValue()) {
                    return false;
                }
            }
            if (!this.advancements.isEmpty()) {
                PlayerAdvancements $$9 = $$3.getAdvancements();
                ServerAdvancementManager $$10 = $$3.m_20194_().getAdvancements();
                for (Entry<ResourceLocation, PlayerPredicate.AdvancementPredicate> $$11 : this.advancements.entrySet()) {
                    Advancement $$12 = $$10.getAdvancement((ResourceLocation) $$11.getKey());
                    if ($$12 == null || !((PlayerPredicate.AdvancementPredicate) $$11.getValue()).test($$9.getOrStartProgress($$12))) {
                        return false;
                    }
                }
            }
            if (this.lookingAt != EntityPredicate.ANY) {
                Vec3 $$13 = $$3.m_146892_();
                Vec3 $$14 = $$3.m_20252_(1.0F);
                Vec3 $$15 = $$13.add($$14.x * 100.0, $$14.y * 100.0, $$14.z * 100.0);
                EntityHitResult $$16 = ProjectileUtil.getEntityHitResult($$3.m_9236_(), $$3, $$13, $$15, new AABB($$13, $$15).inflate(1.0), p_156765_ -> !p_156765_.isSpectator(), 0.0F);
                if ($$16 == null || $$16.getType() != HitResult.Type.ENTITY) {
                    return false;
                }
                Entity $$17 = $$16.getEntity();
                if (!this.lookingAt.matches($$3, $$17) || !$$3.m_142582_($$17)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static PlayerPredicate fromJson(JsonObject jsonObject0) {
        MinMaxBounds.Ints $$1 = MinMaxBounds.Ints.fromJson(jsonObject0.get("level"));
        String $$2 = GsonHelper.getAsString(jsonObject0, "gamemode", "");
        GameType $$3 = GameType.byName($$2, null);
        Map<Stat<?>, MinMaxBounds.Ints> $$4 = Maps.newHashMap();
        JsonArray $$5 = GsonHelper.getAsJsonArray(jsonObject0, "stats", null);
        if ($$5 != null) {
            for (JsonElement $$6 : $$5) {
                JsonObject $$7 = GsonHelper.convertToJsonObject($$6, "stats entry");
                ResourceLocation $$8 = new ResourceLocation(GsonHelper.getAsString($$7, "type"));
                StatType<?> $$9 = BuiltInRegistries.STAT_TYPE.get($$8);
                if ($$9 == null) {
                    throw new JsonParseException("Invalid stat type: " + $$8);
                }
                ResourceLocation $$10 = new ResourceLocation(GsonHelper.getAsString($$7, "stat"));
                Stat<?> $$11 = getStat($$9, $$10);
                MinMaxBounds.Ints $$12 = MinMaxBounds.Ints.fromJson($$7.get("value"));
                $$4.put($$11, $$12);
            }
        }
        Object2BooleanMap<ResourceLocation> $$13 = new Object2BooleanOpenHashMap();
        JsonObject $$14 = GsonHelper.getAsJsonObject(jsonObject0, "recipes", new JsonObject());
        for (Entry<String, JsonElement> $$15 : $$14.entrySet()) {
            ResourceLocation $$16 = new ResourceLocation((String) $$15.getKey());
            boolean $$17 = GsonHelper.convertToBoolean((JsonElement) $$15.getValue(), "recipe present");
            $$13.put($$16, $$17);
        }
        Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> $$18 = Maps.newHashMap();
        JsonObject $$19 = GsonHelper.getAsJsonObject(jsonObject0, "advancements", new JsonObject());
        for (Entry<String, JsonElement> $$20 : $$19.entrySet()) {
            ResourceLocation $$21 = new ResourceLocation((String) $$20.getKey());
            PlayerPredicate.AdvancementPredicate $$22 = advancementPredicateFromJson((JsonElement) $$20.getValue());
            $$18.put($$21, $$22);
        }
        EntityPredicate $$23 = EntityPredicate.fromJson(jsonObject0.get("looking_at"));
        return new PlayerPredicate($$1, $$3, $$4, $$13, $$18, $$23);
    }

    private static <T> Stat<T> getStat(StatType<T> statTypeT0, ResourceLocation resourceLocation1) {
        Registry<T> $$2 = statTypeT0.getRegistry();
        T $$3 = $$2.get(resourceLocation1);
        if ($$3 == null) {
            throw new JsonParseException("Unknown object " + resourceLocation1 + " for stat type " + BuiltInRegistries.STAT_TYPE.getKey(statTypeT0));
        } else {
            return statTypeT0.get($$3);
        }
    }

    private static <T> ResourceLocation getStatValueId(Stat<T> statT0) {
        return statT0.getType().getRegistry().getKey(statT0.getValue());
    }

    @Override
    public JsonObject serializeCustomData() {
        JsonObject $$0 = new JsonObject();
        $$0.add("level", this.level.m_55328_());
        if (this.gameType != null) {
            $$0.addProperty("gamemode", this.gameType.getName());
        }
        if (!this.stats.isEmpty()) {
            JsonArray $$1 = new JsonArray();
            this.stats.forEach((p_222489_, p_222490_) -> {
                JsonObject $$3x = new JsonObject();
                $$3x.addProperty("type", BuiltInRegistries.STAT_TYPE.getKey(p_222489_.getType()).toString());
                $$3x.addProperty("stat", getStatValueId(p_222489_).toString());
                $$3x.add("value", p_222490_.m_55328_());
                $$1.add($$3x);
            });
            $$0.add("stats", $$1);
        }
        if (!this.recipes.isEmpty()) {
            JsonObject $$2 = new JsonObject();
            this.recipes.forEach((p_222499_, p_222500_) -> $$2.addProperty(p_222499_.toString(), p_222500_));
            $$0.add("recipes", $$2);
        }
        if (!this.advancements.isEmpty()) {
            JsonObject $$3 = new JsonObject();
            this.advancements.forEach((p_222495_, p_222496_) -> $$3.add(p_222495_.toString(), p_222496_.toJson()));
            $$0.add("advancements", $$3);
        }
        $$0.add("looking_at", this.lookingAt.serializeToJson());
        return $$0;
    }

    @Override
    public EntitySubPredicate.Type type() {
        return EntitySubPredicate.Types.PLAYER;
    }

    static class AdvancementCriterionsPredicate implements PlayerPredicate.AdvancementPredicate {

        private final Object2BooleanMap<String> criterions;

        public AdvancementCriterionsPredicate(Object2BooleanMap<String> objectBooleanMapString0) {
            this.criterions = objectBooleanMapString0;
        }

        @Override
        public JsonElement toJson() {
            JsonObject $$0 = new JsonObject();
            this.criterions.forEach($$0::addProperty);
            return $$0;
        }

        public boolean test(AdvancementProgress advancementProgress0) {
            ObjectIterator var2 = this.criterions.object2BooleanEntrySet().iterator();
            while (var2.hasNext()) {
                it.unimi.dsi.fastutil.objects.Object2BooleanMap.Entry<String> $$1 = (it.unimi.dsi.fastutil.objects.Object2BooleanMap.Entry<String>) var2.next();
                CriterionProgress $$2 = advancementProgress0.getCriterion((String) $$1.getKey());
                if ($$2 == null || $$2.isDone() != $$1.getBooleanValue()) {
                    return false;
                }
            }
            return true;
        }
    }

    static class AdvancementDonePredicate implements PlayerPredicate.AdvancementPredicate {

        private final boolean state;

        public AdvancementDonePredicate(boolean boolean0) {
            this.state = boolean0;
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.state);
        }

        public boolean test(AdvancementProgress advancementProgress0) {
            return advancementProgress0.isDone() == this.state;
        }
    }

    interface AdvancementPredicate extends Predicate<AdvancementProgress> {

        JsonElement toJson();
    }

    public static class Builder {

        private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;

        @Nullable
        private GameType gameType;

        private final Map<Stat<?>, MinMaxBounds.Ints> stats = Maps.newHashMap();

        private final Object2BooleanMap<ResourceLocation> recipes = new Object2BooleanOpenHashMap();

        private final Map<ResourceLocation, PlayerPredicate.AdvancementPredicate> advancements = Maps.newHashMap();

        private EntityPredicate lookingAt = EntityPredicate.ANY;

        public static PlayerPredicate.Builder player() {
            return new PlayerPredicate.Builder();
        }

        public PlayerPredicate.Builder setLevel(MinMaxBounds.Ints minMaxBoundsInts0) {
            this.level = minMaxBoundsInts0;
            return this;
        }

        public PlayerPredicate.Builder addStat(Stat<?> stat0, MinMaxBounds.Ints minMaxBoundsInts1) {
            this.stats.put(stat0, minMaxBoundsInts1);
            return this;
        }

        public PlayerPredicate.Builder addRecipe(ResourceLocation resourceLocation0, boolean boolean1) {
            this.recipes.put(resourceLocation0, boolean1);
            return this;
        }

        public PlayerPredicate.Builder setGameType(GameType gameType0) {
            this.gameType = gameType0;
            return this;
        }

        public PlayerPredicate.Builder setLookingAt(EntityPredicate entityPredicate0) {
            this.lookingAt = entityPredicate0;
            return this;
        }

        public PlayerPredicate.Builder checkAdvancementDone(ResourceLocation resourceLocation0, boolean boolean1) {
            this.advancements.put(resourceLocation0, new PlayerPredicate.AdvancementDonePredicate(boolean1));
            return this;
        }

        public PlayerPredicate.Builder checkAdvancementCriterions(ResourceLocation resourceLocation0, Map<String, Boolean> mapStringBoolean1) {
            this.advancements.put(resourceLocation0, new PlayerPredicate.AdvancementCriterionsPredicate(new Object2BooleanOpenHashMap(mapStringBoolean1)));
            return this;
        }

        public PlayerPredicate build() {
            return new PlayerPredicate(this.level, this.gameType, this.stats, this.recipes, this.advancements, this.lookingAt);
        }
    }
}