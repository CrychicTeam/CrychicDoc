package net.minecraft.data.models.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class TextureMapping {

    private final Map<TextureSlot, ResourceLocation> slots = Maps.newHashMap();

    private final Set<TextureSlot> forcedSlots = Sets.newHashSet();

    public TextureMapping put(TextureSlot textureSlot0, ResourceLocation resourceLocation1) {
        this.slots.put(textureSlot0, resourceLocation1);
        return this;
    }

    public TextureMapping putForced(TextureSlot textureSlot0, ResourceLocation resourceLocation1) {
        this.slots.put(textureSlot0, resourceLocation1);
        this.forcedSlots.add(textureSlot0);
        return this;
    }

    public Stream<TextureSlot> getForced() {
        return this.forcedSlots.stream();
    }

    public TextureMapping copySlot(TextureSlot textureSlot0, TextureSlot textureSlot1) {
        this.slots.put(textureSlot1, (ResourceLocation) this.slots.get(textureSlot0));
        return this;
    }

    public TextureMapping copyForced(TextureSlot textureSlot0, TextureSlot textureSlot1) {
        this.slots.put(textureSlot1, (ResourceLocation) this.slots.get(textureSlot0));
        this.forcedSlots.add(textureSlot1);
        return this;
    }

    public ResourceLocation get(TextureSlot textureSlot0) {
        for (TextureSlot $$1 = textureSlot0; $$1 != null; $$1 = $$1.getParent()) {
            ResourceLocation $$2 = (ResourceLocation) this.slots.get($$1);
            if ($$2 != null) {
                return $$2;
            }
        }
        throw new IllegalStateException("Can't find texture for slot " + textureSlot0);
    }

    public TextureMapping copyAndUpdate(TextureSlot textureSlot0, ResourceLocation resourceLocation1) {
        TextureMapping $$2 = new TextureMapping();
        $$2.slots.putAll(this.slots);
        $$2.forcedSlots.addAll(this.forcedSlots);
        $$2.put(textureSlot0, resourceLocation1);
        return $$2;
    }

    public static TextureMapping cube(Block block0) {
        ResourceLocation $$1 = getBlockTexture(block0);
        return cube($$1);
    }

    public static TextureMapping defaultTexture(Block block0) {
        ResourceLocation $$1 = getBlockTexture(block0);
        return defaultTexture($$1);
    }

    public static TextureMapping defaultTexture(ResourceLocation resourceLocation0) {
        return new TextureMapping().put(TextureSlot.TEXTURE, resourceLocation0);
    }

    public static TextureMapping cube(ResourceLocation resourceLocation0) {
        return new TextureMapping().put(TextureSlot.ALL, resourceLocation0);
    }

    public static TextureMapping cross(Block block0) {
        return singleSlot(TextureSlot.CROSS, getBlockTexture(block0));
    }

    public static TextureMapping cross(ResourceLocation resourceLocation0) {
        return singleSlot(TextureSlot.CROSS, resourceLocation0);
    }

    public static TextureMapping plant(Block block0) {
        return singleSlot(TextureSlot.PLANT, getBlockTexture(block0));
    }

    public static TextureMapping plant(ResourceLocation resourceLocation0) {
        return singleSlot(TextureSlot.PLANT, resourceLocation0);
    }

    public static TextureMapping rail(Block block0) {
        return singleSlot(TextureSlot.RAIL, getBlockTexture(block0));
    }

    public static TextureMapping rail(ResourceLocation resourceLocation0) {
        return singleSlot(TextureSlot.RAIL, resourceLocation0);
    }

    public static TextureMapping wool(Block block0) {
        return singleSlot(TextureSlot.WOOL, getBlockTexture(block0));
    }

    public static TextureMapping flowerbed(Block block0) {
        return new TextureMapping().put(TextureSlot.FLOWERBED, getBlockTexture(block0)).put(TextureSlot.STEM, getBlockTexture(block0, "_stem"));
    }

    public static TextureMapping wool(ResourceLocation resourceLocation0) {
        return singleSlot(TextureSlot.WOOL, resourceLocation0);
    }

    public static TextureMapping stem(Block block0) {
        return singleSlot(TextureSlot.STEM, getBlockTexture(block0));
    }

    public static TextureMapping attachedStem(Block block0, Block block1) {
        return new TextureMapping().put(TextureSlot.STEM, getBlockTexture(block0)).put(TextureSlot.UPPER_STEM, getBlockTexture(block1));
    }

    public static TextureMapping pattern(Block block0) {
        return singleSlot(TextureSlot.PATTERN, getBlockTexture(block0));
    }

    public static TextureMapping fan(Block block0) {
        return singleSlot(TextureSlot.FAN, getBlockTexture(block0));
    }

    public static TextureMapping crop(ResourceLocation resourceLocation0) {
        return singleSlot(TextureSlot.CROP, resourceLocation0);
    }

    public static TextureMapping pane(Block block0, Block block1) {
        return new TextureMapping().put(TextureSlot.PANE, getBlockTexture(block0)).put(TextureSlot.EDGE, getBlockTexture(block1, "_top"));
    }

    public static TextureMapping singleSlot(TextureSlot textureSlot0, ResourceLocation resourceLocation1) {
        return new TextureMapping().put(textureSlot0, resourceLocation1);
    }

    public static TextureMapping column(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.END, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping cubeTop(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.TOP, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping pottedAzalea(Block block0) {
        return new TextureMapping().put(TextureSlot.PLANT, getBlockTexture(block0, "_plant")).put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.TOP, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping logColumn(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0)).put(TextureSlot.END, getBlockTexture(block0, "_top")).put(TextureSlot.PARTICLE, getBlockTexture(block0));
    }

    public static TextureMapping column(ResourceLocation resourceLocation0, ResourceLocation resourceLocation1) {
        return new TextureMapping().put(TextureSlot.SIDE, resourceLocation0).put(TextureSlot.END, resourceLocation1);
    }

    public static TextureMapping fence(Block block0) {
        return new TextureMapping().put(TextureSlot.TEXTURE, getBlockTexture(block0)).put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.TOP, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping customParticle(Block block0) {
        return new TextureMapping().put(TextureSlot.TEXTURE, getBlockTexture(block0)).put(TextureSlot.PARTICLE, getBlockTexture(block0, "_particle"));
    }

    public static TextureMapping cubeBottomTop(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.TOP, getBlockTexture(block0, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(block0, "_bottom"));
    }

    public static TextureMapping cubeBottomTopWithWall(Block block0) {
        ResourceLocation $$1 = getBlockTexture(block0);
        return new TextureMapping().put(TextureSlot.WALL, $$1).put(TextureSlot.SIDE, $$1).put(TextureSlot.TOP, getBlockTexture(block0, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(block0, "_bottom"));
    }

    public static TextureMapping columnWithWall(Block block0) {
        ResourceLocation $$1 = getBlockTexture(block0);
        return new TextureMapping().put(TextureSlot.TEXTURE, $$1).put(TextureSlot.WALL, $$1).put(TextureSlot.SIDE, $$1).put(TextureSlot.END, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping door(ResourceLocation resourceLocation0, ResourceLocation resourceLocation1) {
        return new TextureMapping().put(TextureSlot.TOP, resourceLocation0).put(TextureSlot.BOTTOM, resourceLocation1);
    }

    public static TextureMapping door(Block block0) {
        return new TextureMapping().put(TextureSlot.TOP, getBlockTexture(block0, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(block0, "_bottom"));
    }

    public static TextureMapping particle(Block block0) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(block0));
    }

    public static TextureMapping particle(ResourceLocation resourceLocation0) {
        return new TextureMapping().put(TextureSlot.PARTICLE, resourceLocation0);
    }

    public static TextureMapping fire0(Block block0) {
        return new TextureMapping().put(TextureSlot.FIRE, getBlockTexture(block0, "_0"));
    }

    public static TextureMapping fire1(Block block0) {
        return new TextureMapping().put(TextureSlot.FIRE, getBlockTexture(block0, "_1"));
    }

    public static TextureMapping lantern(Block block0) {
        return new TextureMapping().put(TextureSlot.LANTERN, getBlockTexture(block0));
    }

    public static TextureMapping torch(Block block0) {
        return new TextureMapping().put(TextureSlot.TORCH, getBlockTexture(block0));
    }

    public static TextureMapping torch(ResourceLocation resourceLocation0) {
        return new TextureMapping().put(TextureSlot.TORCH, resourceLocation0);
    }

    public static TextureMapping particleFromItem(Item item0) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getItemTexture(item0));
    }

    public static TextureMapping commandBlock(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.FRONT, getBlockTexture(block0, "_front")).put(TextureSlot.BACK, getBlockTexture(block0, "_back"));
    }

    public static TextureMapping orientableCube(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.FRONT, getBlockTexture(block0, "_front")).put(TextureSlot.TOP, getBlockTexture(block0, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(block0, "_bottom"));
    }

    public static TextureMapping orientableCubeOnlyTop(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.FRONT, getBlockTexture(block0, "_front")).put(TextureSlot.TOP, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping orientableCubeSameEnds(Block block0) {
        return new TextureMapping().put(TextureSlot.SIDE, getBlockTexture(block0, "_side")).put(TextureSlot.FRONT, getBlockTexture(block0, "_front")).put(TextureSlot.END, getBlockTexture(block0, "_end"));
    }

    public static TextureMapping top(Block block0) {
        return new TextureMapping().put(TextureSlot.TOP, getBlockTexture(block0, "_top"));
    }

    public static TextureMapping craftingTable(Block block0, Block block1) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(block0, "_front")).put(TextureSlot.DOWN, getBlockTexture(block1)).put(TextureSlot.UP, getBlockTexture(block0, "_top")).put(TextureSlot.NORTH, getBlockTexture(block0, "_front")).put(TextureSlot.EAST, getBlockTexture(block0, "_side")).put(TextureSlot.SOUTH, getBlockTexture(block0, "_side")).put(TextureSlot.WEST, getBlockTexture(block0, "_front"));
    }

    public static TextureMapping fletchingTable(Block block0, Block block1) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(block0, "_front")).put(TextureSlot.DOWN, getBlockTexture(block1)).put(TextureSlot.UP, getBlockTexture(block0, "_top")).put(TextureSlot.NORTH, getBlockTexture(block0, "_front")).put(TextureSlot.SOUTH, getBlockTexture(block0, "_front")).put(TextureSlot.EAST, getBlockTexture(block0, "_side")).put(TextureSlot.WEST, getBlockTexture(block0, "_side"));
    }

    public static TextureMapping snifferEgg(String string0) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_north")).put(TextureSlot.BOTTOM, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_bottom")).put(TextureSlot.TOP, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_top")).put(TextureSlot.NORTH, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_north")).put(TextureSlot.SOUTH, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_south")).put(TextureSlot.EAST, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_east")).put(TextureSlot.WEST, getBlockTexture(Blocks.SNIFFER_EGG, string0 + "_west"));
    }

    public static TextureMapping campfire(Block block0) {
        return new TextureMapping().put(TextureSlot.LIT_LOG, getBlockTexture(block0, "_log_lit")).put(TextureSlot.FIRE, getBlockTexture(block0, "_fire"));
    }

    public static TextureMapping candleCake(Block block0, boolean boolean1) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(Blocks.CAKE, "_side")).put(TextureSlot.BOTTOM, getBlockTexture(Blocks.CAKE, "_bottom")).put(TextureSlot.TOP, getBlockTexture(Blocks.CAKE, "_top")).put(TextureSlot.SIDE, getBlockTexture(Blocks.CAKE, "_side")).put(TextureSlot.CANDLE, getBlockTexture(block0, boolean1 ? "_lit" : ""));
    }

    public static TextureMapping cauldron(ResourceLocation resourceLocation0) {
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(Blocks.CAULDRON, "_side")).put(TextureSlot.SIDE, getBlockTexture(Blocks.CAULDRON, "_side")).put(TextureSlot.TOP, getBlockTexture(Blocks.CAULDRON, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(Blocks.CAULDRON, "_bottom")).put(TextureSlot.INSIDE, getBlockTexture(Blocks.CAULDRON, "_inner")).put(TextureSlot.CONTENT, resourceLocation0);
    }

    public static TextureMapping sculkShrieker(boolean boolean0) {
        String $$1 = boolean0 ? "_can_summon" : "";
        return new TextureMapping().put(TextureSlot.PARTICLE, getBlockTexture(Blocks.SCULK_SHRIEKER, "_bottom")).put(TextureSlot.SIDE, getBlockTexture(Blocks.SCULK_SHRIEKER, "_side")).put(TextureSlot.TOP, getBlockTexture(Blocks.SCULK_SHRIEKER, "_top")).put(TextureSlot.INNER_TOP, getBlockTexture(Blocks.SCULK_SHRIEKER, $$1 + "_inner_top")).put(TextureSlot.BOTTOM, getBlockTexture(Blocks.SCULK_SHRIEKER, "_bottom"));
    }

    public static TextureMapping layer0(Item item0) {
        return new TextureMapping().put(TextureSlot.LAYER0, getItemTexture(item0));
    }

    public static TextureMapping layer0(Block block0) {
        return new TextureMapping().put(TextureSlot.LAYER0, getBlockTexture(block0));
    }

    public static TextureMapping layer0(ResourceLocation resourceLocation0) {
        return new TextureMapping().put(TextureSlot.LAYER0, resourceLocation0);
    }

    public static TextureMapping layered(ResourceLocation resourceLocation0, ResourceLocation resourceLocation1) {
        return new TextureMapping().put(TextureSlot.LAYER0, resourceLocation0).put(TextureSlot.LAYER1, resourceLocation1);
    }

    public static TextureMapping layered(ResourceLocation resourceLocation0, ResourceLocation resourceLocation1, ResourceLocation resourceLocation2) {
        return new TextureMapping().put(TextureSlot.LAYER0, resourceLocation0).put(TextureSlot.LAYER1, resourceLocation1).put(TextureSlot.LAYER2, resourceLocation2);
    }

    public static ResourceLocation getBlockTexture(Block block0) {
        ResourceLocation $$1 = BuiltInRegistries.BLOCK.getKey(block0);
        return $$1.withPrefix("block/");
    }

    public static ResourceLocation getBlockTexture(Block block0, String string1) {
        ResourceLocation $$2 = BuiltInRegistries.BLOCK.getKey(block0);
        return $$2.withPath((UnaryOperator<String>) (p_248521_ -> "block/" + p_248521_ + string1));
    }

    public static ResourceLocation getItemTexture(Item item0) {
        ResourceLocation $$1 = BuiltInRegistries.ITEM.getKey(item0);
        return $$1.withPrefix("item/");
    }

    public static ResourceLocation getItemTexture(Item item0, String string1) {
        ResourceLocation $$2 = BuiltInRegistries.ITEM.getKey(item0);
        return $$2.withPath((UnaryOperator<String>) (p_252192_ -> "item/" + p_252192_ + string1));
    }
}