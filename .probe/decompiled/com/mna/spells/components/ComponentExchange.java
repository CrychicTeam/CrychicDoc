package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.tools.BlockUtils;
import com.mna.tools.EnchantmentUtils;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ComponentExchange extends SpellEffect {

    public ComponentExchange(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 5.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster() && !target.isEntity()) {
            if (context.getServerLevel().m_46859_(target.getBlock())) {
                return ComponentApplicationResult.FAIL;
            } else {
                int harvestLevel = (int) modificationData.getValue(Attribute.MAGNITUDE);
                Tier harvestTier = BlockUtils.tierFromHarvestLevel(harvestLevel - 1);
                if (!BlockUtils.canDestroyBlock(source.getCaster(), context.getServerLevel(), target.getBlock(), harvestTier)) {
                    return ComponentApplicationResult.FAIL;
                } else {
                    CompoundTag nbt = context.getMeta();
                    Block searchBlock = null;
                    Item searchItem = null;
                    if (!nbt.contains("blockType")) {
                        ItemStack offhandStack = source.getPlayer().m_21120_(source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                        if (!(offhandStack.getItem() instanceof BlockItem)) {
                            return ComponentApplicationResult.FAIL;
                        }
                        if (!(offhandStack.getItem() instanceof BlockItem)) {
                            return ComponentApplicationResult.FAIL;
                        }
                        searchBlock = ((BlockItem) offhandStack.getItem()).getBlock();
                        searchItem = offhandStack.getItem();
                        nbt.putString("blockType", ForgeRegistries.ITEMS.getKey(offhandStack.getItem()).toString());
                    } else {
                        ResourceLocation rLoc = new ResourceLocation(nbt.getString("blockType"));
                        searchItem = ForgeRegistries.ITEMS.getValue(rLoc);
                        if (searchItem != null && searchItem instanceof BlockItem) {
                            searchBlock = ((BlockItem) searchItem).getBlock();
                        }
                    }
                    if (searchBlock != null && searchItem != null) {
                        if (!searchBlock.defaultBlockState().m_60710_(context.getServerLevel(), target.getBlock())) {
                            return ComponentApplicationResult.FAIL;
                        } else if (!source.getPlayer().isCreative() && InventoryUtilities.removeSingleItemFromInventory(ForgeRegistries.ITEMS.getKey(searchItem), source.getPlayer().getInventory()).isEmpty()) {
                            return ComponentApplicationResult.FAIL;
                        } else {
                            boolean silkTouch = EnchantmentUtils.getSilkTouch(source.getPlayer());
                            int fortune_level = 0;
                            if (!silkTouch) {
                                fortune_level = EnchantmentUtils.getFortuneLevel(source.getPlayer());
                            }
                            List<ItemStack> drops = new ArrayList();
                            Pair<Boolean, Boolean> captureRedirect = InventoryUtilities.getCaptureAndRedirect(source.getPlayer());
                            drops.addAll(BlockUtils.destroyBlockCaptureDrops(source.getCaster(), context.getServerLevel(), target.getBlock(), silkTouch, fortune_level, harvestTier));
                            if (source.isPlayerCaster()) {
                                InventoryUtilities.redirectCaptureOrDrop(source.getPlayer(), context.getServerLevel(), drops, (Boolean) captureRedirect.getSecond());
                            }
                            context.getServerLevel().m_7731_(target.getBlock(), searchBlock.defaultBlockState(), 3);
                            return ComponentApplicationResult.SUCCESS;
                        }
                    } else {
                        return ComponentApplicationResult.FAIL;
                    }
                }
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public float initialComplexity() {
        return 2.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    public class PlaceBlockInfo {

        public final Block block;

        public final BlockPos position;

        public final Level world;

        public PlaceBlockInfo(Block block, BlockPos position, Level world) {
            this.block = block;
            this.position = position;
            this.world = world;
        }
    }
}