package snownee.kiwi.customization.item.loader;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;

public final class BlockItemTemplate extends KItemTemplate {

    private final Optional<ResourceLocation> block;

    private final String clazz;

    private BiFunction<Block, Item.Properties, Item> constructor;

    public BlockItemTemplate(Optional<ItemDefinitionProperties> properties, Optional<ResourceLocation> block, String clazz) {
        super(properties);
        this.block = block;
        this.clazz = clazz;
    }

    public static Codec<BlockItemTemplate> directCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(ItemDefinitionProperties.mapCodecField().forGetter(KItemTemplate::properties), ResourceLocation.CODEC.optionalFieldOf("block").forGetter(BlockItemTemplate::block), Codec.STRING.optionalFieldOf("class", "").forGetter(BlockItemTemplate::clazz)).apply(instance, BlockItemTemplate::new));
    }

    @Override
    public KItemTemplate.Type<?> type() {
        return KItemTemplates.BLOCK.getOrCreate();
    }

    @Override
    public void resolve(ResourceLocation key) {
        if (this.clazz.isEmpty()) {
            this.constructor = (block, properties) -> {
                if (block instanceof DoorBlock || block instanceof DoublePlantBlock) {
                    return new DoubleHighBlockItem(block, properties);
                } else if (block instanceof BedBlock) {
                    return new BedItem(block, properties);
                } else {
                    return (Item) (block instanceof ScaffoldingBlock ? new ScaffoldingBlockItem(block, properties) : new BlockItem(block, properties));
                }
            };
        } else {
            try {
                Class<?> clazz = Class.forName(this.clazz);
                this.constructor = (block, properties) -> {
                    try {
                        return (Item) clazz.getConstructor(Block.class, Item.Properties.class).newInstance(block, properties);
                    } catch (Throwable var4) {
                        throw new RuntimeException(var4);
                    }
                };
            } catch (Throwable var3) {
                throw new IllegalStateException(var3);
            }
        }
    }

    @Override
    public Item createItem(ResourceLocation id, Item.Properties properties, JsonObject json) {
        Block block = BuiltInRegistries.BLOCK.get((ResourceLocation) this.block.orElse(id));
        Preconditions.checkState(block != Blocks.AIR, "Block %s not found", this.block);
        return (Item) this.constructor.apply(block, properties);
    }

    public Optional<ResourceLocation> block() {
        return this.block;
    }

    public String clazz() {
        return this.clazz;
    }

    public String toString() {
        return "BlockItemTemplate[properties=" + this.properties + ", block=" + this.block + "]";
    }
}