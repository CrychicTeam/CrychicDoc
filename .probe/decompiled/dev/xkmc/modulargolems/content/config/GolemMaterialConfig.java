package dev.xkmc.modulargolems.content.config;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.http.util.Asserts;

@SerialClass
public class GolemMaterialConfig extends BaseConfig {

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<ResourceLocation, HashMap<GolemStatType, Double>> stats = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<ResourceLocation, HashMap<GolemModifier, Integer>> modifiers = new HashMap();

    @ConfigCollect(CollectType.MAP_OVERWRITE)
    @SerialField
    public HashMap<ResourceLocation, Ingredient> ingredients = new HashMap();

    public static GolemMaterialConfig get() {
        return ModularGolems.MATERIALS.getMerged();
    }

    public Set<ResourceLocation> getAllMaterials() {
        TreeSet<ResourceLocation> set = new TreeSet(this.stats.keySet());
        set.retainAll(this.modifiers.keySet());
        set.retainAll(this.ingredients.keySet());
        return set;
    }

    public GolemMaterialConfig.Builder addMaterial(ResourceLocation id, Ingredient ingredient) {
        return new GolemMaterialConfig.Builder(this, id, ingredient);
    }

    public static class Builder {

        private final GolemMaterialConfig parent;

        private final ResourceLocation id;

        private final Ingredient ingredient;

        private final HashMap<GolemStatType, Double> stats = new HashMap();

        private final HashMap<GolemModifier, Integer> modifiers = new HashMap();

        private Builder(GolemMaterialConfig parent, ResourceLocation id, Ingredient ingredient) {
            this.parent = parent;
            this.id = id;
            this.ingredient = ingredient;
        }

        public GolemMaterialConfig.Builder addStat(GolemStatType type, double val) {
            this.stats.put(type, val);
            return this;
        }

        public GolemMaterialConfig.Builder addModifier(GolemModifier modifier, int lv) {
            Asserts.check(!(modifier instanceof AttributeGolemModifier), "Material cannot use attribute modifier");
            this.modifiers.put(modifier, lv);
            return this;
        }

        public GolemMaterialConfig end() {
            this.parent.stats.put(this.id, this.stats);
            this.parent.modifiers.put(this.id, this.modifiers);
            this.parent.ingredients.put(this.id, this.ingredient);
            return this.parent;
        }
    }
}