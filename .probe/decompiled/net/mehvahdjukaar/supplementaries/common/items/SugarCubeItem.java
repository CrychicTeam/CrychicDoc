package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SugarCubeItem extends BlockItem {

    public SugarCubeItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.m_20202_() instanceof Horse horse && (Integer) CommonConfigs.Building.SUGAR_BLOCK_HORSE_SPEED_DURATION.get() != 0) {
            ItemStack stack = player.m_21120_(usedHand);
            horse.m_30580_(player, stack);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        return super.m_7203_(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Entity v = context.getPlayer().m_20202_();
        return v instanceof Horse && CommonConfigs.Building.SUGAR_BLOCK_HORSE_SPEED_DURATION.get() != 0 ? InteractionResult.PASS : super.useOn(context);
    }
}