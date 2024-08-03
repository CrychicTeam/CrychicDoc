package com.mna.items.runes;

import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.items.IPositionalItem;
import com.mna.gui.containers.providers.NamedMarkBook;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IRadialInventorySelect;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.items.renderers.books.MarkBookRenderer;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class MarkBookItem extends ItemBagBase implements IRadialInventorySelect, IPositionalItem<MarkBookItem> {

    public static final int INVENTORY_SIZE = 16;

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new MarkBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public ItemFilterGroup filterGroup() {
        return ItemFilterGroup.ANY_MARKING_RUNE;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedMarkBook(stack);
    }

    @Override
    public int getIndex(ItemStack stack) {
        return stack.getItem() == ItemInit.BOOK_MARKS.get() && stack.hasTag() && stack.getTag().contains("index") ? stack.getTag().getInt("index") : 0;
    }

    @Override
    public void setIndex(ItemStack stack, int index) {
        if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
            stack.getOrCreateTag().putInt("index", index);
        }
    }

    @Override
    public void setLocation(ItemStack stack, DirectionalPoint point) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().setLocation(stack, point);
        } else if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
            int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack invStack = inv.getStackInSlot(index);
            if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                ItemInit.RUNE_MARKING.get().setLocation(invStack, point);
            }
        }
    }

    @Override
    public void setLocation(ItemStack stack, BlockPos pos, Direction face) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().setLocation(stack, pos, face);
        } else if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
            int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack invStack = inv.getStackInSlot(index);
            if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                ItemInit.RUNE_MARKING.get().setLocation(invStack, pos, face);
            }
        }
    }

    @Override
    public void setLocation(ItemStack stack, BlockPos pos, Direction face, Level world) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().setLocation(stack, pos, face, world);
        } else if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
            int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack invStack = inv.getStackInSlot(index);
            if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                ItemInit.RUNE_MARKING.get().setLocation(invStack, pos, face, world);
            }
        }
    }

    @Override
    public void copyPositionFrom(ItemStack source, ItemStack destination) {
        if (source.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().copyPositionFrom(source, destination);
        } else if (source.getItem() == ItemInit.BOOK_MARKS.get()) {
            int index = ItemInit.BOOK_MARKS.get().getIndex(source);
            ItemInventoryBase inv = new ItemInventoryBase(source);
            ItemStack invStack = inv.getStackInSlot(index);
            if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                ItemInit.RUNE_MARKING.get().copyPositionFrom(invStack, destination);
            }
        }
    }

    @Override
    public BlockPos getLocation(ItemStack stack) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            return ItemInit.RUNE_MARKING.get().getLocation(stack);
        } else {
            if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
                int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
                ItemInventoryBase inv = new ItemInventoryBase(stack);
                ItemStack invStack = inv.getStackInSlot(index);
                if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                    return ItemInit.RUNE_MARKING.get().getLocation(invStack);
                }
            }
            return null;
        }
    }

    @Override
    public Direction getFace(ItemStack stack) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            return ItemInit.RUNE_MARKING.get().getFace(stack);
        } else {
            if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
                int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
                ItemInventoryBase inv = new ItemInventoryBase(stack);
                ItemStack invStack = inv.getStackInSlot(index);
                if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                    return ItemInit.RUNE_MARKING.get().getFace(invStack);
                }
            }
            return Direction.DOWN;
        }
    }

    @Override
    public Component getBlockName(ItemStack stack) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            return ItemInit.RUNE_MARKING.get().getBlockName(stack);
        } else {
            if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
                int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
                ItemInventoryBase inv = new ItemInventoryBase(stack);
                ItemStack invStack = inv.getStackInSlot(index);
                if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                    return ItemInit.RUNE_MARKING.get().getBlockName(invStack);
                }
            }
            return null;
        }
    }

    @Override
    public DirectionalPoint getDirectionalPoint(ItemStack stack) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            return ItemInit.RUNE_MARKING.get().getDirectionalPoint(stack);
        } else {
            if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
                int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
                ItemInventoryBase inv = new ItemInventoryBase(stack);
                ItemStack invStack = inv.getStackInSlot(index);
                if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                    return ItemInit.RUNE_MARKING.get().getDirectionalPoint(invStack);
                }
            }
            return null;
        }
    }

    @Override
    public String getBlockTranslateKey(ItemStack stack) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            return ItemInit.RUNE_MARKING.get().getBlockTranslateKey(stack);
        } else {
            if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
                int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
                ItemInventoryBase inv = new ItemInventoryBase(stack);
                ItemStack invStack = inv.getStackInSlot(index);
                if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                    return ItemInit.RUNE_MARKING.get().getBlockTranslateKey(invStack);
                }
            }
            return null;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
        ItemInventoryBase inv = new ItemInventoryBase(stack);
        ItemStack invStack = inv.getStackInSlot(index);
        if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().appendHoverText(invStack, worldIn, tooltip, flagIn);
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        IRadialInventorySelect.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int capacity() {
        return 16;
    }
}