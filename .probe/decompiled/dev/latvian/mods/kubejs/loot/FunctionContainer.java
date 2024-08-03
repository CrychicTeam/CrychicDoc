package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

public interface FunctionContainer {

    FunctionContainer addFunction(JsonObject var1);

    default FunctionContainer addConditionalFunction(Consumer<ConditionalFunction> func) {
        ConditionalFunction conditionalFunction = new ConditionalFunction();
        func.accept(conditionalFunction);
        if (conditionalFunction.function != null) {
            conditionalFunction.function.add("conditions", conditionalFunction.conditions);
            return this.addFunction(conditionalFunction.function);
        } else {
            return this;
        }
    }

    default FunctionContainer count(NumberProvider count) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:set_count");
        o.add("count", UtilsJS.numberProviderJson(count));
        return this.addFunction(o);
    }

    default FunctionContainer enchantWithLevels(NumberProvider levels, boolean treasure) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:enchant_with_levels");
        o.add("levels", UtilsJS.numberProviderJson(levels));
        o.addProperty("treasure", treasure);
        return this.addFunction(o);
    }

    default FunctionContainer enchantRandomly(ResourceLocation[] enchantments) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:enchant_randomly");
        JsonArray a = new JsonArray();
        for (ResourceLocation r : enchantments) {
            a.add(r.toString());
        }
        o.add("enchantments", a);
        return this.addFunction(o);
    }

    default FunctionContainer nbt(CompoundTag tag) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:set_nbt");
        o.addProperty("tag", tag.toString());
        return this.addFunction(o);
    }

    default FunctionContainer furnaceSmelt() {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:furnace_smelt");
        return this.addFunction(o);
    }

    default FunctionContainer lootingEnchant(NumberProvider count, int limit) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:looting_enchant");
        o.add("count", UtilsJS.numberProviderJson(count));
        o.addProperty("limit", limit);
        return this.addFunction(o);
    }

    default FunctionContainer damage(NumberProvider damage) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:set_damage");
        o.add("damage", UtilsJS.numberProviderJson(damage));
        return this.addFunction(o);
    }

    default FunctionContainer name(Component name, @Nullable LootContext.EntityTarget entity) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:set_name");
        o.add("name", Component.Serializer.toJsonTree(name));
        if (entity != null) {
            o.addProperty("entity", entity.name);
        }
        return this.addFunction(o);
    }

    default FunctionContainer name(Component name) {
        return this.name(name, null);
    }

    default FunctionContainer copyName(CopyNameFunction.NameSource source) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:copy_name");
        o.addProperty("source", source.name);
        return this.addFunction(o);
    }

    default FunctionContainer lootTable(ResourceLocation table, long seed) {
        JsonObject o = new JsonObject();
        o.addProperty("function", "minecraft:set_loot_table");
        o.addProperty("name", table.toString());
        if (seed != 0L) {
            o.addProperty("seed", seed);
        }
        return this.addFunction(o);
    }
}