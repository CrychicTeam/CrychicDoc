package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WritableBookItem extends Item {

    public WritableBookItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if ($$3.m_60713_(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(useOnContext0.getPlayer(), $$1, $$2, $$3, useOnContext0.getItemInHand()) ? InteractionResult.sidedSuccess($$1.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        player1.openItemGui($$3, interactionHand2);
        player1.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
    }

    public static boolean makeSureTagIsValid(@Nullable CompoundTag compoundTag0) {
        if (compoundTag0 == null) {
            return false;
        } else if (!compoundTag0.contains("pages", 9)) {
            return false;
        } else {
            ListTag $$1 = compoundTag0.getList("pages", 8);
            for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
                String $$3 = $$1.getString($$2);
                if ($$3.length() > 32767) {
                    return false;
                }
            }
            return true;
        }
    }
}