package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record BlockComponent(ComponentRole crole) implements RecipeComponent<Block> {

    public static final RecipeComponent<Block> INPUT = new BlockComponent(ComponentRole.INPUT);

    public static final RecipeComponent<Block> OUTPUT = new BlockComponent(ComponentRole.OUTPUT);

    public static final RecipeComponent<Block> BLOCK = new BlockComponent(ComponentRole.OTHER);

    @Override
    public ComponentRole role() {
        return this.crole;
    }

    @Override
    public String componentType() {
        return "block";
    }

    @Override
    public Class<?> componentClass() {
        return Block.class;
    }

    public JsonPrimitive write(RecipeJS recipe, Block value) {
        return new JsonPrimitive(String.valueOf(RegistryInfo.BLOCK.getId(value)));
    }

    public Block read(RecipeJS recipe, Object from) {
        if (from instanceof Block) {
            return (Block) from;
        } else if (from instanceof BlockState s) {
            return s.m_60734_();
        } else {
            return from instanceof JsonPrimitive json ? UtilsJS.parseBlockState(json.getAsString()).m_60734_() : UtilsJS.parseBlockState(String.valueOf(from)).m_60734_();
        }
    }

    public boolean isInput(RecipeJS recipe, Block value, ReplacementMatch match) {
        if (this.crole.isInput() && match instanceof BlockStatePredicate m2 && m2.testBlock(value)) {
            return true;
        }
        return false;
    }

    public boolean isOutput(RecipeJS recipe, Block value, ReplacementMatch match) {
        if (this.crole.isOutput() && match instanceof BlockStatePredicate m2 && m2.testBlock(value)) {
            return true;
        }
        return false;
    }

    public String checkEmpty(RecipeKey<Block> key, Block value) {
        return value == Blocks.AIR ? "Block '" + key.name + "' can't be empty!" : "";
    }

    public String toString() {
        return this.componentType();
    }
}