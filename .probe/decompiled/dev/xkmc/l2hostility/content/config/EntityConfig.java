package dev.xkmc.l2hostility.content.config;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class EntityConfig extends BaseConfig {

    @SerialField
    @ConfigCollect(CollectType.COLLECT)
    public final ArrayList<EntityConfig.Config> list = new ArrayList();

    private final Map<EntityType<?>, EntityConfig.Config> cache = new HashMap();

    private final Map<ResourceLocation, ArrayList<Pair<SpecialConfigCondition<?>, EntityConfig.Config>>> conditions = new HashMap();

    public static boolean allow(EntityType<?> type, MobTrait trait) {
        EntityConfig.Config config = L2Hostility.ENTITY.getMerged().get(type);
        return config == null ? true : !config.blacklist.contains(trait);
    }

    @Override
    protected void postMerge() {
        for (EntityConfig.Config e : this.list) {
            if (e.specialConditions.isEmpty()) {
                for (EntityType<?> type : e.entities) {
                    this.cache.put(type, e);
                }
            } else {
                for (SpecialConfigCondition<?> str : e.specialConditions) {
                    ((ArrayList) this.conditions.computeIfAbsent(str.id, k -> new ArrayList())).add(Pair.of(str, e));
                }
            }
        }
    }

    @Nullable
    public EntityConfig.Config get(EntityType<?> type) {
        return !LHConfig.COMMON.enableEntitySpecificDatapack.get() ? null : (EntityConfig.Config) this.cache.get(type);
    }

    @Nullable
    public <T> EntityConfig.Config get(EntityType<?> type, ResourceLocation id, Class<T> cls, T obj) {
        if (!LHConfig.COMMON.enableEntitySpecificDatapack.get()) {
            return null;
        } else {
            ArrayList<Pair<SpecialConfigCondition<?>, EntityConfig.Config>> list = (ArrayList<Pair<SpecialConfigCondition<?>, EntityConfig.Config>>) this.conditions.get(id);
            if (list == null) {
                return null;
            } else {
                for (Pair<SpecialConfigCondition<?>, EntityConfig.Config> pair : list) {
                    SpecialConfigCondition<?> cond = (SpecialConfigCondition<?>) pair.getFirst();
                    if (((EntityConfig.Config) pair.getSecond()).entities.contains(type) && cond.cls() == cls && ((SpecialConfigCondition<Object>) cond).test(Wrappers.cast(obj))) {
                        return (EntityConfig.Config) pair.getSecond();
                    }
                }
                return null;
            }
        }
    }

    public final EntityConfig putEntity(int min, int base, double var, double scale, List<EntityType<?>> keys, List<EntityConfig.TraitBase> traits) {
        return this.putEntityAndItem(min, base, var, scale, keys, traits, List.of());
    }

    public final EntityConfig putEntityAndItem(int min, int base, double var, double scale, List<EntityType<?>> keys, List<EntityConfig.TraitBase> traits, List<EntityConfig.ItemPool> items) {
        return this.put(entity(min, base, var, scale, keys).trait(traits).item(items));
    }

    public final EntityConfig put(EntityConfig.Config config) {
        this.list.add(config);
        return this;
    }

    public static EntityConfig.Config entity(int min, int base, double var, double scale, List<EntityType<?>> keys) {
        return new EntityConfig.Config(new ArrayList(keys), new WorldDifficultyConfig.DifficultyConfig(min, base, var, scale, 1.0, 1.0));
    }

    public static EntityConfig.ItemPool simplePool(int level, String slot, ItemStack stack) {
        return new EntityConfig.ItemPool(level, 1.0F, slot, new ArrayList(List.of(new EntityConfig.ItemEntry(100, stack))));
    }

    public static EntityConfig.TraitBase trait(MobTrait trait, int free, int min) {
        return new EntityConfig.TraitBase(trait, free, min, null);
    }

    public static EntityConfig.TraitBase trait(MobTrait trait, int free, int min, int lv, float chance) {
        return new EntityConfig.TraitBase(trait, free, min, new EntityConfig.TraitCondition(lv, chance, null));
    }

    @SerialClass
    public static class Config {

        @SerialField
        private final ArrayList<EntityType<?>> entities = new ArrayList();

        @SerialField
        private final ArrayList<SpecialConfigCondition<?>> specialConditions = new ArrayList();

        @SerialField
        private final ArrayList<EntityConfig.TraitBase> traits = new ArrayList();

        @SerialField
        private final LinkedHashSet<MobTrait> blacklist = new LinkedHashSet();

        @SerialField
        private WorldDifficultyConfig.DifficultyConfig difficulty = new WorldDifficultyConfig.DifficultyConfig(0, 0, 0.0, 0.0, 1.0, 1.0);

        @SerialField
        public final ArrayList<EntityConfig.ItemPool> items = new ArrayList();

        @SerialField
        public int minSpawnLevel = 0;

        @SerialField
        public int maxLevel = 0;

        @SerialField
        public EntityConfig.MasterConfig asMaster = null;

        @Deprecated
        public Config() {
        }

        public Config(List<EntityType<?>> entities, WorldDifficultyConfig.DifficultyConfig difficulty) {
            this.entities.addAll(entities);
            this.difficulty = difficulty;
        }

        public Set<MobTrait> blacklist() {
            return this.blacklist;
        }

        public List<EntityConfig.TraitBase> traits() {
            return this.traits;
        }

        public WorldDifficultyConfig.DifficultyConfig difficulty() {
            return this.difficulty;
        }

        public EntityConfig.Config minLevel(int level) {
            this.minSpawnLevel = level;
            return this;
        }

        public EntityConfig.Config trait(List<EntityConfig.TraitBase> list) {
            this.traits.addAll(list);
            return this;
        }

        public EntityConfig.Config item(List<EntityConfig.ItemPool> list) {
            this.items.addAll(list);
            return this;
        }

        public EntityConfig.Config conditions(SpecialConfigCondition<?> list) {
            this.specialConditions.add(list);
            return this;
        }

        public EntityConfig.Config blacklist(MobTrait... list) {
            Collections.addAll(this.blacklist, list);
            return this;
        }

        public EntityConfig.Config master(int maxTotal, int interval, EntityConfig.Minion... minions) {
            this.asMaster = new EntityConfig.MasterConfig(maxTotal, interval, new ArrayList(List.of(minions)));
            return this;
        }
    }

    public static record ItemEntry(int weight, ItemStack stack) {
    }

    public static record ItemPool(int level, float chance, String slot, ArrayList<EntityConfig.ItemEntry> entries) {
    }

    public static record MasterConfig(int maxTotalCount, int spawnInterval, ArrayList<EntityConfig.Minion> minions) {
    }

    public static record Minion(EntityType<?> type, int maxCount, int minLevel, double maxHealthPercentage, int spawnRange, int cooldown, boolean copyLevel, boolean copyTrait, double linkDistance, boolean protectMaster, boolean discardOnUnlink) {
    }

    public static record TraitBase(MobTrait trait, int free, int min, @Nullable EntityConfig.TraitCondition condition) {
    }

    public static record TraitCondition(int lv, float chance, @Nullable ResourceLocation id) {

        public boolean match(LivingEntity entity, int mobLevel, MobDifficultyCollector ins) {
            if (entity.getRandom().nextDouble() > (double) this.chance) {
                return false;
            } else {
                return mobLevel < this.lv ? false : this.id == null || ins.hasAdvancement(this.id);
            }
        }
    }
}