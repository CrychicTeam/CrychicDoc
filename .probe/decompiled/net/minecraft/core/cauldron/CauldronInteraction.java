package net.minecraft.core.cauldron;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public interface CauldronInteraction {

    Map<Item, CauldronInteraction> EMPTY = newInteractionMap();

    Map<Item, CauldronInteraction> WATER = newInteractionMap();

    Map<Item, CauldronInteraction> LAVA = newInteractionMap();

    Map<Item, CauldronInteraction> POWDER_SNOW = newInteractionMap();

    CauldronInteraction FILL_WATER = (p_175683_, p_175684_, p_175685_, p_175686_, p_175687_, p_175688_) -> emptyBucket(p_175684_, p_175685_, p_175686_, p_175687_, p_175688_, (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);

    CauldronInteraction FILL_LAVA = (p_175676_, p_175677_, p_175678_, p_175679_, p_175680_, p_175681_) -> emptyBucket(p_175677_, p_175678_, p_175679_, p_175680_, p_175681_, Blocks.LAVA_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);

    CauldronInteraction FILL_POWDER_SNOW = (p_175669_, p_175670_, p_175671_, p_175672_, p_175673_, p_175674_) -> emptyBucket(p_175670_, p_175671_, p_175672_, p_175673_, p_175674_, (BlockState) Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);

    CauldronInteraction SHULKER_BOX = (p_175662_, p_175663_, p_175664_, p_175665_, p_175666_, p_175667_) -> {
        Block $$6 = Block.byItem(p_175667_.getItem());
        if (!($$6 instanceof ShulkerBoxBlock)) {
            return InteractionResult.PASS;
        } else {
            if (!p_175663_.isClientSide) {
                ItemStack $$7 = new ItemStack(Blocks.SHULKER_BOX);
                if (p_175667_.hasTag()) {
                    $$7.setTag(p_175667_.getTag().copy());
                }
                p_175665_.m_21008_(p_175666_, $$7);
                p_175665_.awardStat(Stats.CLEAN_SHULKER_BOX);
                LayeredCauldronBlock.lowerFillLevel(p_175662_, p_175663_, p_175664_);
            }
            return InteractionResult.sidedSuccess(p_175663_.isClientSide);
        }
    };

    CauldronInteraction BANNER = (p_278890_, p_278891_, p_278892_, p_278893_, p_278894_, p_278895_) -> {
        if (BannerBlockEntity.getPatternCount(p_278895_) <= 0) {
            return InteractionResult.PASS;
        } else {
            if (!p_278891_.isClientSide) {
                ItemStack $$6 = p_278895_.copyWithCount(1);
                BannerBlockEntity.removeLastPattern($$6);
                if (!p_278893_.getAbilities().instabuild) {
                    p_278895_.shrink(1);
                }
                if (p_278895_.isEmpty()) {
                    p_278893_.m_21008_(p_278894_, $$6);
                } else if (p_278893_.getInventory().add($$6)) {
                    p_278893_.inventoryMenu.m_150429_();
                } else {
                    p_278893_.drop($$6, false);
                }
                p_278893_.awardStat(Stats.CLEAN_BANNER);
                LayeredCauldronBlock.lowerFillLevel(p_278890_, p_278891_, p_278892_);
            }
            return InteractionResult.sidedSuccess(p_278891_.isClientSide);
        }
    };

    CauldronInteraction DYED_ITEM = (p_175629_, p_175630_, p_175631_, p_175632_, p_175633_, p_175634_) -> {
        if (!(p_175634_.getItem() instanceof DyeableLeatherItem $$7)) {
            return InteractionResult.PASS;
        } else if (!$$7.hasCustomColor(p_175634_)) {
            return InteractionResult.PASS;
        } else {
            if (!p_175630_.isClientSide) {
                $$7.clearColor(p_175634_);
                p_175632_.awardStat(Stats.CLEAN_ARMOR);
                LayeredCauldronBlock.lowerFillLevel(p_175629_, p_175630_, p_175631_);
            }
            return InteractionResult.sidedSuccess(p_175630_.isClientSide);
        }
    };

    static Object2ObjectOpenHashMap<Item, CauldronInteraction> newInteractionMap() {
        return Util.make(new Object2ObjectOpenHashMap(), p_175646_ -> p_175646_.defaultReturnValue((CauldronInteraction) (p_175739_, p_175740_, p_175741_, p_175742_, p_175743_, p_175744_) -> InteractionResult.PASS));
    }

    InteractionResult interact(BlockState var1, Level var2, BlockPos var3, Player var4, InteractionHand var5, ItemStack var6);

    static void bootStrap() {
        addDefaultInteractions(EMPTY);
        EMPTY.put(Items.POTION, (CauldronInteraction) (p_175732_, p_175733_, p_175734_, p_175735_, p_175736_, p_175737_) -> {
            if (PotionUtils.getPotion(p_175737_) != Potions.WATER) {
                return InteractionResult.PASS;
            } else {
                if (!p_175733_.isClientSide) {
                    Item $$6 = p_175737_.getItem();
                    p_175735_.m_21008_(p_175736_, ItemUtils.createFilledResult(p_175737_, p_175735_, new ItemStack(Items.GLASS_BOTTLE)));
                    p_175735_.awardStat(Stats.USE_CAULDRON);
                    p_175735_.awardStat(Stats.ITEM_USED.get($$6));
                    p_175733_.setBlockAndUpdate(p_175734_, Blocks.WATER_CAULDRON.defaultBlockState());
                    p_175733_.playSound(null, p_175734_, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    p_175733_.m_142346_(null, GameEvent.FLUID_PLACE, p_175734_);
                }
                return InteractionResult.sidedSuccess(p_175733_.isClientSide);
            }
        });
        addDefaultInteractions(WATER);
        WATER.put(Items.BUCKET, (CauldronInteraction) (p_175725_, p_175726_, p_175727_, p_175728_, p_175729_, p_175730_) -> fillBucket(p_175725_, p_175726_, p_175727_, p_175728_, p_175729_, p_175730_, new ItemStack(Items.WATER_BUCKET), p_175660_ -> (Integer) p_175660_.m_61143_(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
        WATER.put(Items.GLASS_BOTTLE, (CauldronInteraction) (p_175718_, p_175719_, p_175720_, p_175721_, p_175722_, p_175723_) -> {
            if (!p_175719_.isClientSide) {
                Item $$6 = p_175723_.getItem();
                p_175721_.m_21008_(p_175722_, ItemUtils.createFilledResult(p_175723_, p_175721_, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                p_175721_.awardStat(Stats.USE_CAULDRON);
                p_175721_.awardStat(Stats.ITEM_USED.get($$6));
                LayeredCauldronBlock.lowerFillLevel(p_175718_, p_175719_, p_175720_);
                p_175719_.playSound(null, p_175720_, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                p_175719_.m_142346_(null, GameEvent.FLUID_PICKUP, p_175720_);
            }
            return InteractionResult.sidedSuccess(p_175719_.isClientSide);
        });
        WATER.put(Items.POTION, (CauldronInteraction) (p_175704_, p_175705_, p_175706_, p_175707_, p_175708_, p_175709_) -> {
            if ((Integer) p_175704_.m_61143_(LayeredCauldronBlock.LEVEL) != 3 && PotionUtils.getPotion(p_175709_) == Potions.WATER) {
                if (!p_175705_.isClientSide) {
                    p_175707_.m_21008_(p_175708_, ItemUtils.createFilledResult(p_175709_, p_175707_, new ItemStack(Items.GLASS_BOTTLE)));
                    p_175707_.awardStat(Stats.USE_CAULDRON);
                    p_175707_.awardStat(Stats.ITEM_USED.get(p_175709_.getItem()));
                    p_175705_.setBlockAndUpdate(p_175706_, (BlockState) p_175704_.m_61122_(LayeredCauldronBlock.LEVEL));
                    p_175705_.playSound(null, p_175706_, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    p_175705_.m_142346_(null, GameEvent.FLUID_PLACE, p_175706_);
                }
                return InteractionResult.sidedSuccess(p_175705_.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        });
        WATER.put(Items.LEATHER_BOOTS, DYED_ITEM);
        WATER.put(Items.LEATHER_LEGGINGS, DYED_ITEM);
        WATER.put(Items.LEATHER_CHESTPLATE, DYED_ITEM);
        WATER.put(Items.LEATHER_HELMET, DYED_ITEM);
        WATER.put(Items.LEATHER_HORSE_ARMOR, DYED_ITEM);
        WATER.put(Items.WHITE_BANNER, BANNER);
        WATER.put(Items.GRAY_BANNER, BANNER);
        WATER.put(Items.BLACK_BANNER, BANNER);
        WATER.put(Items.BLUE_BANNER, BANNER);
        WATER.put(Items.BROWN_BANNER, BANNER);
        WATER.put(Items.CYAN_BANNER, BANNER);
        WATER.put(Items.GREEN_BANNER, BANNER);
        WATER.put(Items.LIGHT_BLUE_BANNER, BANNER);
        WATER.put(Items.LIGHT_GRAY_BANNER, BANNER);
        WATER.put(Items.LIME_BANNER, BANNER);
        WATER.put(Items.MAGENTA_BANNER, BANNER);
        WATER.put(Items.ORANGE_BANNER, BANNER);
        WATER.put(Items.PINK_BANNER, BANNER);
        WATER.put(Items.PURPLE_BANNER, BANNER);
        WATER.put(Items.RED_BANNER, BANNER);
        WATER.put(Items.YELLOW_BANNER, BANNER);
        WATER.put(Items.WHITE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.GRAY_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.BLACK_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.BLUE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.BROWN_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.CYAN_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.GREEN_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.LIGHT_BLUE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.LIGHT_GRAY_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.LIME_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.MAGENTA_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.ORANGE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.PINK_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.PURPLE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.RED_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.YELLOW_SHULKER_BOX, SHULKER_BOX);
        LAVA.put(Items.BUCKET, (CauldronInteraction) (p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_) -> fillBucket(p_175697_, p_175698_, p_175699_, p_175700_, p_175701_, p_175702_, new ItemStack(Items.LAVA_BUCKET), p_175651_ -> true, SoundEvents.BUCKET_FILL_LAVA));
        addDefaultInteractions(LAVA);
        POWDER_SNOW.put(Items.BUCKET, (CauldronInteraction) (p_175690_, p_175691_, p_175692_, p_175693_, p_175694_, p_175695_) -> fillBucket(p_175690_, p_175691_, p_175692_, p_175693_, p_175694_, p_175695_, new ItemStack(Items.POWDER_SNOW_BUCKET), p_175627_ -> (Integer) p_175627_.m_61143_(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL_POWDER_SNOW));
        addDefaultInteractions(POWDER_SNOW);
    }

    static void addDefaultInteractions(Map<Item, CauldronInteraction> mapItemCauldronInteraction0) {
        mapItemCauldronInteraction0.put(Items.LAVA_BUCKET, FILL_LAVA);
        mapItemCauldronInteraction0.put(Items.WATER_BUCKET, FILL_WATER);
        mapItemCauldronInteraction0.put(Items.POWDER_SNOW_BUCKET, FILL_POWDER_SNOW);
    }

    static InteractionResult fillBucket(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, ItemStack itemStack5, ItemStack itemStack6, Predicate<BlockState> predicateBlockState7, SoundEvent soundEvent8) {
        if (!predicateBlockState7.test(blockState0)) {
            return InteractionResult.PASS;
        } else {
            if (!level1.isClientSide) {
                Item $$9 = itemStack5.getItem();
                player3.m_21008_(interactionHand4, ItemUtils.createFilledResult(itemStack5, player3, itemStack6));
                player3.awardStat(Stats.USE_CAULDRON);
                player3.awardStat(Stats.ITEM_USED.get($$9));
                level1.setBlockAndUpdate(blockPos2, Blocks.CAULDRON.defaultBlockState());
                level1.playSound(null, blockPos2, soundEvent8, SoundSource.BLOCKS, 1.0F, 1.0F);
                level1.m_142346_(null, GameEvent.FLUID_PICKUP, blockPos2);
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    static InteractionResult emptyBucket(Level level0, BlockPos blockPos1, Player player2, InteractionHand interactionHand3, ItemStack itemStack4, BlockState blockState5, SoundEvent soundEvent6) {
        if (!level0.isClientSide) {
            Item $$7 = itemStack4.getItem();
            player2.m_21008_(interactionHand3, ItemUtils.createFilledResult(itemStack4, player2, new ItemStack(Items.BUCKET)));
            player2.awardStat(Stats.FILL_CAULDRON);
            player2.awardStat(Stats.ITEM_USED.get($$7));
            level0.setBlockAndUpdate(blockPos1, blockState5);
            level0.playSound(null, blockPos1, soundEvent6, SoundSource.BLOCKS, 1.0F, 1.0F);
            level0.m_142346_(null, GameEvent.FLUID_PLACE, blockPos1);
        }
        return InteractionResult.sidedSuccess(level0.isClientSide);
    }
}