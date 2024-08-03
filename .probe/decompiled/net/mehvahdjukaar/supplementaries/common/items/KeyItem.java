package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SafeBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class KeyItem extends Item {

    public KeyItem(Item.Properties properties) {
        super(properties);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.VANISHING_CURSE;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        ListTag l = EnchantedBookItem.getEnchantments(book);
        return l.size() == 1 && l.get(0) == Enchantments.VANISHING_CURSE;
    }

    public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (PlatHelper.getPlatform().isFabric() && context.getPlayer().isSecondaryUseActive()) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof KeyLockableTile t) {
                if (t.tryClearingKey(context.getPlayer(), context.getItemInHand())) {
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (tile instanceof SafeBlockTile) {
                return level.getBlockState(pos).m_60664_(level, context.getPlayer(), context.getHand(), new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
            }
        }
        return super.useOn(context);
    }

    public String getPassword(ItemStack stack) {
        return stack.getHoverName().getString();
    }
}