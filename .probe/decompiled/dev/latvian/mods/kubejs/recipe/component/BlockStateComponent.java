package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.util.JsonIO;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Map;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record BlockStateComponent(ComponentRole crole, boolean preferObjectForm) implements RecipeComponent<BlockState> {

    public static final RecipeComponent<BlockState> INPUT = new BlockStateComponent(ComponentRole.INPUT, true);

    public static final RecipeComponent<BlockState> OUTPUT = new BlockStateComponent(ComponentRole.OUTPUT, true);

    public static final RecipeComponent<BlockState> BLOCK = new BlockStateComponent(ComponentRole.OTHER, true);

    public static final RecipeComponent<BlockState> INPUT_STRING = new BlockStateComponent(ComponentRole.INPUT, false);

    public static final RecipeComponent<BlockState> OUTPUT_STRING = new BlockStateComponent(ComponentRole.OUTPUT, false);

    public static final RecipeComponent<BlockState> BLOCK_STRING = new BlockStateComponent(ComponentRole.OTHER, false);

    @Override
    public ComponentRole role() {
        return this.crole;
    }

    @Override
    public String componentType() {
        return "block_state";
    }

    @Override
    public Class<?> componentClass() {
        return BlockState.class;
    }

    public JsonElement write(RecipeJS recipe, BlockState value) {
        return (JsonElement) (this.preferObjectForm ? (JsonElement) BlockState.CODEC.encode(value, JsonOps.INSTANCE, new JsonObject()).getOrThrow(true, message -> {
            throw new RecipeExceptionJS("Failed to write blockstate to object form: " + message);
        }) : new JsonPrimitive(BlockStateParser.serialize(value)));
    }

    public BlockState read(RecipeJS recipe, Object from) {
        if (from instanceof BlockState) {
            return (BlockState) from;
        } else if (from instanceof Block b) {
            return b.defaultBlockState();
        } else if (from instanceof JsonPrimitive json) {
            return UtilsJS.parseBlockState(json.getAsString());
        } else {
            Map<?, ?> map = MapJS.of(from);
            return map == null ? UtilsJS.parseBlockState(String.valueOf(from)) : (BlockState) BlockState.CODEC.parse(JsonOps.INSTANCE, JsonIO.GSON.toJsonTree(from)).getOrThrow(true, message -> {
                throw new RecipeExceptionJS("Failed to parse blockstate: " + message);
            });
        }
    }

    public boolean isInput(RecipeJS recipe, BlockState value, ReplacementMatch match) {
        if (this.crole.isInput() && match instanceof BlockStatePredicate m2 && m2.test(value)) {
            return true;
        }
        return false;
    }

    public boolean isOutput(RecipeJS recipe, BlockState value, ReplacementMatch match) {
        if (this.crole.isOutput() && match instanceof BlockStatePredicate m2 && m2.test(value)) {
            return true;
        }
        return false;
    }

    public String checkEmpty(RecipeKey<BlockState> key, BlockState value) {
        return value.m_60734_() == Blocks.AIR ? "Block '" + key.name + "' can't be empty!" : "";
    }

    public String toString() {
        return this.componentType();
    }
}