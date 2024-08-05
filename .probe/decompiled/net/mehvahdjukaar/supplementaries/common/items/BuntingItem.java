package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.supplementaries.common.block.blocks.BuntingBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BuntingItem extends Item {

    public BuntingItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.m_60713_((Block) ModRegistry.ROPE.get())) {
            BlockHitResult hit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, false);
            BlockState s = BuntingBlock.fromRope(state, hit);
            if (s != null) {
                level.setBlockAndUpdate(pos, s);
                InteractionResult ret = s.m_60664_(level, context.getPlayer(), context.getHand(), hit);
                if (!ret.consumesAction()) {
                    level.setBlockAndUpdate(pos, state);
                }
                return ret;
            }
        }
        return super.useOn(context);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String des = super.getDescriptionId(stack);
        CompoundTag t = stack.getTag();
        if (t != null) {
            des = des + "_" + t.getString("Color");
        }
        return des;
    }

    public static DyeColor getColor(ItemStack item) {
        if (item.getItem() instanceof BuntingItem) {
            CompoundTag tag = item.getTag();
            if (tag != null) {
                return DyeColor.byName(tag.getString("Color"), DyeColor.WHITE);
            }
        }
        return DyeColor.WHITE;
    }
}