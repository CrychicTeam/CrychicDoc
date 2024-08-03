package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemHandlerHelper;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.ItemNBTHelper;

@ZetaLoadModule(category = "tweaks")
public class ShulkerPackingModule extends ZetaModule {

    @Hint
    Item shulker_shell = Items.SHULKER_SHELL;

    @PlayEvent
    public void callFedEnd(ZRightClickBlock event) {
        BlockPos pos = event.getHitVec().getBlockPos();
        Player player = event.getPlayer();
        if (player.m_6144_()) {
            ItemStack mainHand = player.m_21205_();
            ItemStack offHand = player.m_21206_();
            if (mainHand.is(Items.SHULKER_SHELL) && offHand.is(Items.SHULKER_SHELL)) {
                Level level = player.m_9236_();
                BlockState state = level.getBlockState(pos);
                if (state.m_204336_(Tags.Blocks.CHESTS) && !state.m_60713_(Blocks.ENDER_CHEST)) {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
                    if (level.isClientSide()) {
                        return;
                    }
                    ItemStack newShulkerBox = new ItemStack(Blocks.SHULKER_BOX);
                    ShulkerBoxBlockEntity shulkerBoxData = new ShulkerBoxBlockEntity(pos, Blocks.SHULKER_BOX.defaultBlockState());
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof Container container && container.getContainerSize() == shulkerBoxData.getContainerSize()) {
                        for (int i = 0; i < container.getContainerSize(); i++) {
                            ItemStack inSlot = container.getItem(i);
                            if (shulkerBoxData.canPlaceItemThroughFace(i, inSlot, null)) {
                                shulkerBoxData.m_6836_(i, inSlot);
                                container.setItem(i, ItemStack.EMPTY);
                            }
                        }
                        if (be instanceof Nameable nameable && nameable.hasCustomName()) {
                            Component component = nameable.getCustomName();
                            if (component != null) {
                                shulkerBoxData.m_58638_(component);
                                newShulkerBox.setHoverName(component);
                            }
                        }
                        level.m_46953_(pos, false, player);
                        level.playSound(null, pos, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        player.awardStat(Stats.ITEM_USED.get(Items.SHULKER_SHELL), 2);
                        if (!player.getAbilities().instabuild) {
                            mainHand.shrink(1);
                            offHand.shrink(1);
                        }
                        ItemNBTHelper.setCompound(newShulkerBox, "BlockEntityTag", shulkerBoxData.m_187480_());
                        ItemHandlerHelper.giveItemToPlayer(player, newShulkerBox, player.getInventory().selected);
                    }
                }
            }
        }
    }
}