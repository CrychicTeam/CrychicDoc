package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.state.BlockStatePredicate;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BlockLootEventJS extends LootEventJS {

    public BlockLootEventJS(Map<ResourceLocation, JsonElement> c) {
        super(c);
    }

    public void build(BlockStatePredicate blocks, Consumer<LootBuilder> b) {
        this.addBlock(blocks, b);
        ConsoleJS.SERVER.warn("This method is no longer supported! Use event.addBlock(blockPredicate, loot => {...})");
    }

    @Override
    public String getType() {
        return "minecraft:block";
    }

    @Override
    public String getDirectory() {
        return "blocks";
    }

    public void addBlock(BlockStatePredicate blocks, Consumer<LootBuilder> b) {
        LootBuilder builder = this.createLootBuilder(null, b);
        JsonObject json = builder.toJson();
        for (ResourceLocation id : blocks.getBlockIds()) {
            ResourceLocation blockId = builder.customId == null ? id : builder.customId;
            if (blockId != null && !blockId.equals(BlockStatePredicate.AIR_ID)) {
                this.addJson(blockId, json);
            }
        }
    }

    public void addSimpleBlock(BlockStatePredicate blocks) {
        this.addSimpleBlock(blocks, ItemStack.EMPTY);
    }

    public void addSimpleBlock(BlockStatePredicate blocks, ItemStack item) {
        for (Block block : blocks.getBlocks()) {
            ItemStack item1 = item.isEmpty() ? new ItemStack(block.asItem()) : item;
            if (!item1.isEmpty()) {
                this.addBlock(new BlockStatePredicate.BlockMatch(block), loot -> loot.addPool(pool -> {
                    pool.addItem(item1);
                    pool.survivesExplosion();
                }));
            }
        }
    }

    public void modifyBlock(BlockStatePredicate blocks, Consumer<LootBuilder> b) {
        for (ResourceLocation blockId : blocks.getBlockIds()) {
            if (blockId != null && !blockId.equals(BlockStatePredicate.AIR_ID)) {
                this.modify(blockId, b);
            }
        }
    }
}