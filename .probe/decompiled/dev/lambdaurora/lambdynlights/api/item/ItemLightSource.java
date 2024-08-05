package dev.lambdaurora.lambdynlights.api.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.lambdaurora.lambdynlights.LambDynLights;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public abstract class ItemLightSource {

    private final ResourceLocation id;

    private final Item item;

    private final boolean waterSensitive;

    public ItemLightSource(ResourceLocation id, Item item, boolean waterSensitive) {
        this.id = id;
        this.item = item;
        this.waterSensitive = waterSensitive;
    }

    public ItemLightSource(ResourceLocation id, Item item) {
        this(id, item, false);
    }

    public ResourceLocation id() {
        return this.id;
    }

    public Item item() {
        return this.item;
    }

    public boolean waterSensitive() {
        return this.waterSensitive;
    }

    public int getLuminance(ItemStack stack, boolean submergedInWater) {
        return this.waterSensitive() && submergedInWater ? 0 : this.getLuminance(stack);
    }

    public abstract int getLuminance(ItemStack var1);

    public String toString() {
        return "ItemLightSource{id=" + this.id() + "item=" + this.item() + ", water_sensitive=" + this.waterSensitive() + "}";
    }

    @NotNull
    public static Optional<ItemLightSource> fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        if (json.has("item") && json.has("luminance")) {
            ResourceLocation affectId = new ResourceLocation(json.get("item").getAsString());
            Item item = ForgeRegistries.ITEMS.getValue(affectId);
            if (item == Items.AIR) {
                return Optional.empty();
            } else {
                boolean waterSensitive = false;
                if (json.has("water_sensitive")) {
                    waterSensitive = json.get("water_sensitive").getAsBoolean();
                }
                JsonPrimitive luminanceElement = json.get("luminance").getAsJsonPrimitive();
                if (luminanceElement.isNumber()) {
                    return Optional.of(new ItemLightSource.StaticItemLightSource(id, item, luminanceElement.getAsInt(), waterSensitive));
                } else {
                    if (luminanceElement.isString()) {
                        String luminanceStr = luminanceElement.getAsString();
                        if (luminanceStr.equals("block")) {
                            if (item instanceof BlockItem blockItem) {
                                return Optional.of(new ItemLightSource.BlockItemLightSource(id, item, blockItem.getBlock().defaultBlockState(), waterSensitive));
                            }
                        } else {
                            ResourceLocation blockId = ResourceLocation.tryParse(luminanceStr);
                            if (blockId != null) {
                                Block block = ForgeRegistries.BLOCKS.getValue(blockId);
                                if (block != Blocks.AIR) {
                                    return Optional.of(new ItemLightSource.BlockItemLightSource(id, item, block.defaultBlockState(), waterSensitive));
                                }
                            }
                        }
                    } else {
                        LambDynLights.get().warn("Failed to parse item light source \"" + id + "\", invalid format: \"luminance\" field value isn't string or integer.");
                    }
                    return Optional.empty();
                }
            }
        } else {
            LambDynLights.get().warn("Failed to parse item light source \"" + id + "\", invalid format: missing required fields.");
            return Optional.empty();
        }
    }

    public static class BlockItemLightSource extends ItemLightSource {

        private final BlockState mimic;

        public BlockItemLightSource(ResourceLocation id, Item item, BlockState block, boolean waterSensitive) {
            super(id, item, waterSensitive);
            this.mimic = block;
        }

        @Override
        public int getLuminance(ItemStack stack) {
            return getLuminance(stack, this.mimic);
        }

        static int getLuminance(ItemStack stack, BlockState state) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null) {
                CompoundTag blockStateTag = nbt.getCompound("BlockStateTag");
                StateDefinition<Block, BlockState> stateManager = state.m_60734_().getStateDefinition();
                for (String key : blockStateTag.getAllKeys()) {
                    Property<? extends Comparable<?>> property = (Property<? extends Comparable<?>>) stateManager.getProperty(key);
                    if (property != null) {
                        String value = blockStateTag.get(key).getAsString();
                        state = with(state, property, value);
                    }
                }
            }
            return state.m_60791_();
        }

        private static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
            return (BlockState) property.getValue(name).map(value -> (BlockState) state.m_61124_(property, value)).orElse(state);
        }
    }

    public static class StaticItemLightSource extends ItemLightSource {

        private final int luminance;

        public StaticItemLightSource(ResourceLocation id, Item item, int luminance, boolean waterSensitive) {
            super(id, item, waterSensitive);
            this.luminance = luminance;
        }

        public StaticItemLightSource(ResourceLocation id, Item item, int luminance) {
            super(id, item);
            this.luminance = luminance;
        }

        @Override
        public int getLuminance(ItemStack stack) {
            return this.luminance;
        }
    }
}