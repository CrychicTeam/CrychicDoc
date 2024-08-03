package net.minecraft.client.color.item;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.IdMapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemColors {

    private static final int DEFAULT = -1;

    private final IdMapper<ItemColor> itemColors = new IdMapper<>(32);

    public static ItemColors createDefault(BlockColors blockColors0) {
        ItemColors $$1 = new ItemColors();
        $$1.register((p_92708_, p_92709_) -> p_92709_ > 0 ? -1 : ((DyeableLeatherItem) p_92708_.getItem()).getColor(p_92708_), Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS, Items.LEATHER_HORSE_ARMOR);
        $$1.register((p_92705_, p_92706_) -> GrassColor.get(0.5, 1.0), Blocks.TALL_GRASS, Blocks.LARGE_FERN);
        $$1.register((p_92702_, p_92703_) -> {
            if (p_92703_ != 1) {
                return -1;
            } else {
                CompoundTag $$2x = p_92702_.getTagElement("Explosion");
                int[] $$3 = $$2x != null && $$2x.contains("Colors", 11) ? $$2x.getIntArray("Colors") : null;
                if ($$3 != null && $$3.length != 0) {
                    if ($$3.length == 1) {
                        return $$3[0];
                    } else {
                        int $$4 = 0;
                        int $$5 = 0;
                        int $$6 = 0;
                        for (int $$7 : $$3) {
                            $$4 += ($$7 & 0xFF0000) >> 16;
                            $$5 += ($$7 & 0xFF00) >> 8;
                            $$6 += ($$7 & 0xFF) >> 0;
                        }
                        $$4 /= $$3.length;
                        $$5 /= $$3.length;
                        $$6 /= $$3.length;
                        return $$4 << 16 | $$5 << 8 | $$6;
                    }
                } else {
                    return 9079434;
                }
            }
        }, Items.FIREWORK_STAR);
        $$1.register((p_92699_, p_92700_) -> p_92700_ > 0 ? -1 : PotionUtils.getColor(p_92699_), Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);
        for (SpawnEggItem $$2 : SpawnEggItem.eggs()) {
            $$1.register((p_92681_, p_92682_) -> $$2.getColor(p_92682_), $$2);
        }
        $$1.register((p_92687_, p_92688_) -> {
            BlockState $$3 = ((BlockItem) p_92687_.getItem()).getBlock().defaultBlockState();
            return blockColors0.getColor($$3, null, null, p_92688_);
        }, Blocks.GRASS_BLOCK, Blocks.GRASS, Blocks.FERN, Blocks.VINE, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.LILY_PAD);
        $$1.register((p_92696_, p_92697_) -> FoliageColor.getMangroveColor(), Blocks.MANGROVE_LEAVES);
        $$1.register((p_92693_, p_92694_) -> p_92694_ == 0 ? PotionUtils.getColor(p_92693_) : -1, Items.TIPPED_ARROW);
        $$1.register((p_232352_, p_232353_) -> p_232353_ == 0 ? -1 : MapItem.getColor(p_232352_), Items.FILLED_MAP);
        return $$1;
    }

    public int getColor(ItemStack itemStack0, int int1) {
        ItemColor $$2 = this.itemColors.byId(BuiltInRegistries.ITEM.m_7447_(itemStack0.getItem()));
        return $$2 == null ? -1 : $$2.getColor(itemStack0, int1);
    }

    public void register(ItemColor itemColor0, ItemLike... itemLike1) {
        for (ItemLike $$2 : itemLike1) {
            this.itemColors.addMapping(itemColor0, Item.getId($$2.asItem()));
        }
    }
}