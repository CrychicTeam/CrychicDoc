package net.minecraft.world.item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.Level;

public class BundleItem extends Item {

    private static final String TAG_ITEMS = "Items";

    public static final int MAX_WEIGHT = 64;

    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;

    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public BundleItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public static float getFullnessDisplay(ItemStack itemStack0) {
        return (float) getContentWeight(itemStack0) / 64.0F;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack0, Slot slot1, ClickAction clickAction2, Player player3) {
        if (clickAction2 != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack $$4 = slot1.getItem();
            if ($$4.isEmpty()) {
                this.playRemoveOneSound(player3);
                removeOne(itemStack0).ifPresent(p_150740_ -> add(itemStack0, slot1.safeInsert(p_150740_)));
            } else if ($$4.getItem().canFitInsideContainerItems()) {
                int $$5 = (64 - getContentWeight(itemStack0)) / getWeight($$4);
                int $$6 = add(itemStack0, slot1.safeTake($$4.getCount(), $$5, player3));
                if ($$6 > 0) {
                    this.playInsertSound(player3);
                }
            }
            return true;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack0, ItemStack itemStack1, Slot slot2, ClickAction clickAction3, Player player4, SlotAccess slotAccess5) {
        if (clickAction3 == ClickAction.SECONDARY && slot2.allowModification(player4)) {
            if (itemStack1.isEmpty()) {
                removeOne(itemStack0).ifPresent(p_186347_ -> {
                    this.playRemoveOneSound(player4);
                    slotAccess5.set(p_186347_);
                });
            } else {
                int $$6 = add(itemStack0, itemStack1);
                if ($$6 > 0) {
                    this.playInsertSound(player4);
                    itemStack1.shrink($$6);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if (dropContents($$3, player1)) {
            this.playDropContentsSound(player1);
            player1.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
        } else {
            return InteractionResultHolder.fail($$3);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack0) {
        return getContentWeight(itemStack0) > 0;
    }

    @Override
    public int getBarWidth(ItemStack itemStack0) {
        return Math.min(1 + 12 * getContentWeight(itemStack0) / 64, 13);
    }

    @Override
    public int getBarColor(ItemStack itemStack0) {
        return BAR_COLOR;
    }

    private static int add(ItemStack itemStack0, ItemStack itemStack1) {
        if (!itemStack1.isEmpty() && itemStack1.getItem().canFitInsideContainerItems()) {
            CompoundTag $$2 = itemStack0.getOrCreateTag();
            if (!$$2.contains("Items")) {
                $$2.put("Items", new ListTag());
            }
            int $$3 = getContentWeight(itemStack0);
            int $$4 = getWeight(itemStack1);
            int $$5 = Math.min(itemStack1.getCount(), (64 - $$3) / $$4);
            if ($$5 == 0) {
                return 0;
            } else {
                ListTag $$6 = $$2.getList("Items", 10);
                Optional<CompoundTag> $$7 = getMatchingItem(itemStack1, $$6);
                if ($$7.isPresent()) {
                    CompoundTag $$8 = (CompoundTag) $$7.get();
                    ItemStack $$9 = ItemStack.of($$8);
                    $$9.grow($$5);
                    $$9.save($$8);
                    $$6.remove($$8);
                    $$6.add(0, $$8);
                } else {
                    ItemStack $$10 = itemStack1.copyWithCount($$5);
                    CompoundTag $$11 = new CompoundTag();
                    $$10.save($$11);
                    $$6.add(0, $$11);
                }
                return $$5;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack itemStack0, ListTag listTag1) {
        return itemStack0.is(Items.BUNDLE) ? Optional.empty() : listTag1.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter(p_186350_ -> ItemStack.isSameItemSameTags(ItemStack.of(p_186350_), itemStack0)).findFirst();
    }

    private static int getWeight(ItemStack itemStack0) {
        if (itemStack0.is(Items.BUNDLE)) {
            return 4 + getContentWeight(itemStack0);
        } else {
            if ((itemStack0.is(Items.BEEHIVE) || itemStack0.is(Items.BEE_NEST)) && itemStack0.hasTag()) {
                CompoundTag $$1 = BlockItem.getBlockEntityData(itemStack0);
                if ($$1 != null && !$$1.getList("Bees", 10).isEmpty()) {
                    return 64;
                }
            }
            return 64 / itemStack0.getMaxStackSize();
        }
    }

    private static int getContentWeight(ItemStack itemStack0) {
        return getContents(itemStack0).mapToInt(p_186356_ -> getWeight(p_186356_) * p_186356_.getCount()).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getOrCreateTag();
        if (!$$1.contains("Items")) {
            return Optional.empty();
        } else {
            ListTag $$2 = $$1.getList("Items", 10);
            if ($$2.isEmpty()) {
                return Optional.empty();
            } else {
                int $$3 = 0;
                CompoundTag $$4 = $$2.getCompound(0);
                ItemStack $$5 = ItemStack.of($$4);
                $$2.remove(0);
                if ($$2.isEmpty()) {
                    itemStack0.removeTagKey("Items");
                }
                return Optional.of($$5);
            }
        }
    }

    private static boolean dropContents(ItemStack itemStack0, Player player1) {
        CompoundTag $$2 = itemStack0.getOrCreateTag();
        if (!$$2.contains("Items")) {
            return false;
        } else {
            if (player1 instanceof ServerPlayer) {
                ListTag $$3 = $$2.getList("Items", 10);
                for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                    CompoundTag $$5 = $$3.getCompound($$4);
                    ItemStack $$6 = ItemStack.of($$5);
                    player1.drop($$6, true);
                }
            }
            itemStack0.removeTagKey("Items");
            return true;
        }
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        if ($$1 == null) {
            return Stream.empty();
        } else {
            ListTag $$2 = $$1.getList("Items", 10);
            return $$2.stream().map(CompoundTag.class::cast).map(ItemStack::m_41712_);
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack0) {
        NonNullList<ItemStack> $$1 = NonNullList.create();
        getContents(itemStack0).forEach($$1::add);
        return Optional.of(new BundleTooltip($$1, getContentWeight(itemStack0)));
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        listComponent2.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(itemStack0), 64).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity0) {
        ItemUtils.onContainerDestroyed(itemEntity0, getContents(itemEntity0.getItem()));
    }

    private void playRemoveOneSound(Entity entity0) {
        entity0.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity0.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity0) {
        entity0.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity0.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity0) {
        entity0.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity0.level().getRandom().nextFloat() * 0.4F);
    }
}