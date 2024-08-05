package com.simibubi.create.content.redstone.link.controller;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.Couple;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class LinkedControllerItem extends Item implements MenuProvider {

    public LinkedControllerItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        } else {
            Level world = ctx.getLevel();
            BlockPos pos = ctx.getClickedPos();
            BlockState hitState = world.getBlockState(pos);
            if (player.mayBuild()) {
                if (player.m_6144_()) {
                    if (AllBlocks.LECTERN_CONTROLLER.has(hitState)) {
                        if (!world.isClientSide) {
                            ((LecternControllerBlock) AllBlocks.LECTERN_CONTROLLER.get()).withBlockEntityDo(world, pos, be -> be.swapControllers(stack, player, ctx.getHand(), hitState));
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    if (AllBlocks.REDSTONE_LINK.has(hitState)) {
                        if (world.isClientSide) {
                            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.toggleBindMode(ctx.getClickedPos()));
                        }
                        player.getCooldowns().addCooldown(this, 2);
                        return InteractionResult.SUCCESS;
                    }
                    if (hitState.m_60713_(Blocks.LECTERN) && !(Boolean) hitState.m_61143_(LecternBlock.HAS_BOOK)) {
                        if (!world.isClientSide) {
                            ItemStack lecternStack = player.isCreative() ? stack.copy() : stack.split(1);
                            ((LecternControllerBlock) AllBlocks.LECTERN_CONTROLLER.get()).replaceLectern(hitState, world, pos, lecternStack);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (AllBlocks.LECTERN_CONTROLLER.has(hitState)) {
                        return InteractionResult.PASS;
                    }
                }
            }
            return this.use(world, player, ctx.getHand()).getResult();
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.m_21120_(hand);
        if (player.m_6144_() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer && player.mayBuild()) {
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItem(heldItem));
            }
            return InteractionResultHolder.success(heldItem);
        } else {
            if (!player.m_6144_()) {
                if (world.isClientSide) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::toggleActive);
                }
                player.getCooldowns().addCooldown(this, 2);
            }
            return InteractionResultHolder.pass(heldItem);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void toggleBindMode(BlockPos pos) {
        LinkedControllerClientHandler.toggleBindMode(pos);
    }

    @OnlyIn(Dist.CLIENT)
    private void toggleActive() {
        LinkedControllerClientHandler.toggle();
    }

    public static ItemStackHandler getFrequencyItems(ItemStack stack) {
        ItemStackHandler newInv = new ItemStackHandler(12);
        if (AllItems.LINKED_CONTROLLER.get() != stack.getItem()) {
            throw new IllegalArgumentException("Cannot get frequency items from non-controller: " + stack);
        } else {
            CompoundTag invNBT = stack.getOrCreateTagElement("Items");
            if (!invNBT.isEmpty()) {
                newInv.deserializeNBT(invNBT);
            }
            return newInv;
        }
    }

    public static Couple<RedstoneLinkNetworkHandler.Frequency> toFrequency(ItemStack controller, int slot) {
        ItemStackHandler frequencyItems = getFrequencyItems(controller);
        return Couple.create(RedstoneLinkNetworkHandler.Frequency.of(frequencyItems.getStackInSlot(slot * 2)), RedstoneLinkNetworkHandler.Frequency.of(frequencyItems.getStackInSlot(slot * 2 + 1)));
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.m_21205_();
        return LinkedControllerMenu.create(id, inv, heldItem);
    }

    @Override
    public Component getDisplayName() {
        return this.m_41466_();
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new LinkedControllerItemRenderer()));
    }
}