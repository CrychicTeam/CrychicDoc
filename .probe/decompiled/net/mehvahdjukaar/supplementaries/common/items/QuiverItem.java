package net.mehvahdjukaar.supplementaries.common.items;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.client.QuiverArrowSelectGui;
import net.mehvahdjukaar.supplementaries.common.items.forge.QuiverItemImpl;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.QuiverTooltip;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuiverItem extends Item implements DyeableLeatherItem {

    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public QuiverItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack quiver, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = pSlot.getItem();
            AtomicBoolean didStuff = new AtomicBoolean(false);
            if (itemstack.isEmpty()) {
                QuiverItem.Data data = getQuiverData(quiver);
                if (data != null) {
                    data.removeOneStack().ifPresent(stack -> {
                        this.playRemoveOneSound(pPlayer);
                        data.tryAdding(pSlot.safeInsert(stack));
                        didStuff.set(true);
                    });
                }
            } else if (itemstack.getItem().canFitInsideContainerItems()) {
                QuiverItem.Data data = getQuiverData(quiver);
                if (data != null) {
                    ItemStack taken = pSlot.safeTake(itemstack.getCount(), itemstack.getMaxStackSize(), pPlayer);
                    ItemStack remaining = data.tryAdding(taken);
                    if (!remaining.equals(taken)) {
                        this.playInsertSound(pPlayer);
                        didStuff.set(true);
                    }
                    pSlot.set(remaining);
                }
            }
            return didStuff.get();
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack quiver, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            QuiverItem.Data data = getQuiverData(quiver);
            if (data != null) {
                AtomicBoolean didStuff = new AtomicBoolean(false);
                if (pOther.isEmpty()) {
                    data.removeOneStack().ifPresent(removed -> {
                        this.playRemoveOneSound(pPlayer);
                        pAccess.set(removed);
                        didStuff.set(true);
                    });
                } else {
                    ItemStack i = data.tryAdding(pOther);
                    if (!i.equals(pOther)) {
                        this.playInsertSound(pPlayer);
                        pAccess.set(i);
                        didStuff.set(true);
                    }
                }
                return didStuff.get();
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        ItemStack stack = player.m_21120_(pUsedHand);
        InteractionHand otherHand = pUsedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack possibleArrowStack = player.m_21120_(otherHand);
        QuiverItem.Data data = getQuiverData(stack);
        if (data.canAcceptItem(possibleArrowStack)) {
            ItemStack remaining = data.tryAdding(possibleArrowStack);
            if (!remaining.equals(possibleArrowStack)) {
                this.playInsertSound(player);
                player.m_21008_(otherHand, remaining);
                return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
            }
        }
        if (player.isSecondaryUseActive()) {
            if (data != null && data.cycle()) {
                this.playInsertSound(player);
            }
            return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
        } else {
            if (pLevel.isClientSide) {
                QuiverArrowSelectGui.setUsingItem(true);
            }
            this.playRemoveOneSound(player);
            player.m_6672_(pUsedHand);
            return InteractionResultHolder.consume(stack);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (level.isClientSide) {
            QuiverArrowSelectGui.setUsingItem(false);
        }
        this.playInsertSound(livingEntity);
        livingEntity.swing(livingEntity.getUsedItemHand());
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        QuiverItem.Data data = getQuiverData(pStack);
        return data != null ? data.getSelected().getCount() > 0 : false;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        QuiverItem.Data data = getQuiverData(pStack);
        return data != null ? Math.min(1 + 12 * data.getSelectedArrowCount() / (data.getSelected().getMaxStackSize() * data.getContentView().size()), 13) : 0;
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return BAR_COLOR;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        QuiverItem.Data data = getQuiverData(pStack);
        if (data != null) {
            NonNullList<ItemStack> list = NonNullList.create();
            boolean isEmpty = true;
            for (ItemStack v : data.getContentView()) {
                if (!v.isEmpty()) {
                    isEmpty = false;
                }
                list.add(v);
            }
            if (!isEmpty) {
                return Optional.of(new QuiverTooltip(new ArrayList(data.getContentView()), data.getSelectedSlot()));
            }
        }
        return Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        QuiverItem.Data data = getQuiverData(pStack);
        if (data != null) {
            int c = data.getSelectedArrowCount();
            if (c != 0) {
                pTooltipComponents.add(Component.translatable("message.supplementaries.quiver.tooltip", data.getSelected(null).getItem().getDescription(), c).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void onDestroyed(ItemEntity pItemEntity) {
        QuiverItem.Data data = getQuiverData(pItemEntity.getItem());
        if (data != null) {
            ItemUtils.onContainerDestroyed(pItemEntity, data.getContentView().stream());
        }
    }

    private void playRemoveOneSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static QuiverItem.Data getQuiverData(ItemStack stack) {
        return QuiverItemImpl.getQuiverData(stack);
    }

    @NotNull
    @ExpectPlatform
    @Transformed
    public static ItemStack getQuiver(LivingEntity entity) {
        return QuiverItemImpl.getQuiver(entity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        QuiverItem.Data data = getQuiverData(stack);
        if (data != null) {
            data.updateSelectedIfNeeded();
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public static boolean canAcceptItem(ItemStack toInsert) {
        return toInsert.getItem() instanceof ArrowItem && !toInsert.is(ModTags.QUIVER_BLACKLIST);
    }

    public interface Data {

        int getSelectedSlot();

        void setSelectedSlot(int var1);

        List<ItemStack> getContentView();

        default boolean canAcceptItem(ItemStack toInsert) {
            return QuiverItem.canAcceptItem(toInsert);
        }

        default ItemStack getSelected() {
            return this.getSelected(null);
        }

        default ItemStack getSelected(@Nullable Predicate<ItemStack> supporterArrows) {
            List<ItemStack> content = this.getContentView();
            int selected = this.getSelectedSlot();
            if (supporterArrows == null) {
                return (ItemStack) content.get(selected);
            } else {
                int size = content.size();
                for (int i = 0; i < size; i++) {
                    ItemStack s = (ItemStack) content.get((i + selected) % size);
                    if (supporterArrows.test(s)) {
                        return s;
                    }
                }
                return ItemStack.EMPTY;
            }
        }

        default boolean cycle() {
            return this.cycle(1);
        }

        default boolean cycle(boolean clockWise) {
            return this.cycle(clockWise ? 1 : -1);
        }

        default boolean cycle(int slotsMoved) {
            int originalSlot = this.getSelectedSlot();
            List<ItemStack> content = this.getContentView();
            if (slotsMoved == 0) {
                ItemStack selected = (ItemStack) content.get(this.getSelectedSlot());
                if (!selected.isEmpty()) {
                    return false;
                }
            }
            int maxSlots = content.size();
            slotsMoved %= maxSlots;
            this.setSelectedSlot((maxSlots + this.getSelectedSlot() + slotsMoved) % maxSlots);
            for (int i = 0; i < maxSlots; i++) {
                ItemStack selected = (ItemStack) content.get(this.getSelectedSlot());
                if (!selected.isEmpty()) {
                    break;
                }
                this.setSelectedSlot((maxSlots + this.getSelectedSlot() + (slotsMoved >= 0 ? 1 : -1)) % maxSlots);
            }
            return originalSlot != this.getSelectedSlot();
        }

        ItemStack tryAdding(ItemStack var1, boolean var2);

        default ItemStack tryAdding(ItemStack pInsertedStack) {
            return this.tryAdding(pInsertedStack, false);
        }

        Optional<ItemStack> removeOneStack();

        default int getSelectedArrowCount() {
            ItemStack selected = this.getSelected(null);
            int amount = 0;
            for (ItemStack item : this.getContentView()) {
                if (ForgeHelper.canItemStack(selected, item)) {
                    amount += item.getCount();
                }
            }
            return amount;
        }

        default void updateSelectedIfNeeded() {
            this.cycle(0);
        }

        void consumeArrow();
    }
}