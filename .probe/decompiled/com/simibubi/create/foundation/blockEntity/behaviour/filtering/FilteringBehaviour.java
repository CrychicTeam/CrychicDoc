package com.simibubi.create.foundation.blockEntity.behaviour.filtering;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FilteringBehaviour extends BlockEntityBehaviour implements ValueSettingsBehaviour {

    public static final BehaviourType<FilteringBehaviour> TYPE = new BehaviourType<>();

    public MutableComponent customLabel;

    ValueBoxTransform slotPositioning;

    boolean showCount;

    private FilterItemStack filter = FilterItemStack.empty();

    public int count;

    public boolean upTo;

    private Predicate<ItemStack> predicate;

    private Consumer<ItemStack> callback;

    private Supplier<Boolean> isActive;

    private Supplier<Boolean> showCountPredicate;

    boolean recipeFilter;

    boolean fluidFilter;

    public FilteringBehaviour(SmartBlockEntity be, ValueBoxTransform slot) {
        super(be);
        this.slotPositioning = slot;
        this.showCount = false;
        this.callback = stack -> {
        };
        this.predicate = stack -> true;
        this.isActive = () -> true;
        this.count = 64;
        this.showCountPredicate = () -> this.showCount;
        this.recipeFilter = false;
        this.fluidFilter = false;
        this.upTo = true;
    }

    @Override
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.put("Filter", this.getFilter().serializeNBT());
        nbt.putInt("FilterAmount", this.count);
        nbt.putBoolean("UpTo", this.upTo);
        super.write(nbt, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        this.filter = FilterItemStack.of(nbt.getCompound("Filter"));
        this.count = nbt.getInt("FilterAmount");
        this.upTo = nbt.getBoolean("UpTo");
        if (this.count == 0) {
            this.upTo = true;
            this.count = this.filter.item().getMaxStackSize();
        }
        super.read(nbt, clientPacket);
    }

    public FilteringBehaviour withCallback(Consumer<ItemStack> filterCallback) {
        this.callback = filterCallback;
        return this;
    }

    public FilteringBehaviour withPredicate(Predicate<ItemStack> filterPredicate) {
        this.predicate = filterPredicate;
        return this;
    }

    public FilteringBehaviour forRecipes() {
        this.recipeFilter = true;
        return this;
    }

    public FilteringBehaviour forFluids() {
        this.fluidFilter = true;
        return this;
    }

    public FilteringBehaviour onlyActiveWhen(Supplier<Boolean> condition) {
        this.isActive = condition;
        return this;
    }

    public FilteringBehaviour showCountWhen(Supplier<Boolean> condition) {
        this.showCountPredicate = condition;
        return this;
    }

    public FilteringBehaviour showCount() {
        this.showCount = true;
        return this;
    }

    public boolean setFilter(Direction face, ItemStack stack) {
        return this.setFilter(stack);
    }

    public void setLabel(MutableComponent label) {
        this.customLabel = label;
    }

    public boolean setFilter(ItemStack stack) {
        ItemStack filter = stack.copy();
        if (!filter.isEmpty() && !this.predicate.test(filter)) {
            return false;
        } else {
            this.filter = FilterItemStack.of(filter);
            if (!this.upTo) {
                this.count = Math.min(this.count, stack.getMaxStackSize());
            }
            this.callback.accept(filter);
            this.blockEntity.m_6596_();
            this.blockEntity.sendData();
            return true;
        }
    }

    @Override
    public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings settings, boolean ctrlDown) {
        if (!this.getValueSettings().equals(settings)) {
            this.count = Mth.clamp(settings.value(), 1, this.filter.item().getMaxStackSize());
            this.upTo = settings.row() == 0;
            this.blockEntity.m_6596_();
            this.blockEntity.sendData();
            this.playFeedbackSound(this);
        }
    }

    @Override
    public ValueSettingsBehaviour.ValueSettings getValueSettings() {
        return new ValueSettingsBehaviour.ValueSettings(this.upTo ? 0 : 1, this.count == 0 ? this.filter.item().getMaxStackSize() : this.count);
    }

    @Override
    public void destroy() {
        if (this.filter.isFilterItem()) {
            Vec3 pos = VecHelper.getCenterOf(this.getPos());
            Level world = this.getWorld();
            world.m_7967_(new ItemEntity(world, pos.x, pos.y, pos.z, this.filter.item().copy()));
        }
        super.destroy();
    }

    @Override
    public ItemRequirement getRequiredItems() {
        return this.filter.isFilterItem() ? new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, this.filter.item()) : ItemRequirement.NONE;
    }

    public ItemStack getFilter(Direction side) {
        return this.getFilter();
    }

    public ItemStack getFilter() {
        return this.filter.item();
    }

    public boolean isCountVisible() {
        return (Boolean) this.showCountPredicate.get() && this.filter.item().getMaxStackSize() > 1;
    }

    public boolean test(ItemStack stack) {
        return !this.isActive() || this.filter.test(this.blockEntity.m_58904_(), stack);
    }

    public boolean test(FluidStack stack) {
        return !this.isActive() || this.filter.test(this.blockEntity.m_58904_(), stack);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean testHit(Vec3 hit) {
        BlockState state = this.blockEntity.m_58900_();
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(this.blockEntity.m_58899_()));
        return this.slotPositioning.testHit(state, localHit);
    }

    public int getAmount() {
        return this.count;
    }

    public boolean anyAmount() {
        return this.count == 0;
    }

    @Override
    public boolean acceptsValueSettings() {
        return this.isCountVisible();
    }

    @Override
    public boolean isActive() {
        return (Boolean) this.isActive.get();
    }

    @Override
    public ValueBoxTransform getSlotPositioning() {
        return this.slotPositioning;
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        ItemStack filter = this.getFilter(hitResult.getDirection());
        int maxAmount = filter.getItem() instanceof FilterItem ? 64 : filter.getMaxStackSize();
        return new ValueSettingsBoard(Lang.translateDirect("logistics.filter.extracted_amount"), maxAmount, 16, Lang.translatedOptions("logistics.filter", "up_to", "exactly"), new ValueSettingsFormatter(this::formatValue));
    }

    public MutableComponent formatValue(ValueSettingsBehaviour.ValueSettings value) {
        return value.row() == 0 && value.value() == this.filter.item().getMaxStackSize() ? Lang.translateDirect("logistics.filter.any_amount_short") : Components.literal((value.row() == 0 ? "≤" : "=") + Math.max(1, value.value()));
    }

    @Override
    public void onShortInteract(Player player, InteractionHand hand, Direction side) {
        Level level = this.getWorld();
        BlockPos pos = this.getPos();
        ItemStack itemInHand = player.m_21120_(hand);
        ItemStack toApply = itemInHand.copy();
        if (!AllItems.WRENCH.isIn(toApply)) {
            if (!AllBlocks.MECHANICAL_ARM.isIn(toApply)) {
                if (!level.isClientSide()) {
                    if (this.getFilter(side).getItem() instanceof FilterItem && (!player.isCreative() || ItemHelper.extract(new InvWrapper(player.getInventory()), stack -> ItemHandlerHelper.canItemStacksStack(stack, this.getFilter(side)), true).isEmpty())) {
                        player.getInventory().placeItemBackInInventory(this.getFilter(side).copy());
                    }
                    if (toApply.getItem() instanceof FilterItem) {
                        toApply.setCount(1);
                    }
                    if (!this.setFilter(side, toApply)) {
                        player.displayClientMessage(Lang.translateDirect("logistics.filter.invalid_item"), true);
                        AllSoundEvents.DENY.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                    } else {
                        if (!player.isCreative() && toApply.getItem() instanceof FilterItem) {
                            if (itemInHand.getCount() == 1) {
                                player.m_21008_(hand, ItemStack.EMPTY);
                            } else {
                                itemInHand.shrink(1);
                            }
                        }
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.25F, 0.1F);
                    }
                }
            }
        }
    }

    public MutableComponent getLabel() {
        return this.customLabel != null ? this.customLabel : Lang.translateDirect(this.recipeFilter ? "logistics.recipe_filter" : (this.fluidFilter ? "logistics.fluid_filter" : "logistics.filter"));
    }

    @Override
    public String getClipboardKey() {
        return "Filtering";
    }

    @Override
    public boolean writeToClipboard(CompoundTag tag, Direction side) {
        ValueSettingsBehaviour.super.writeToClipboard(tag, side);
        ItemStack filter = this.getFilter(side);
        tag.put("Filter", filter.serializeNBT());
        return true;
    }

    @Override
    public boolean readFromClipboard(CompoundTag tag, Player player, Direction side, boolean simulate) {
        boolean upstreamResult = ValueSettingsBehaviour.super.readFromClipboard(tag, player, side, simulate);
        if (!tag.contains("Filter")) {
            return upstreamResult;
        } else if (simulate) {
            return true;
        } else if (this.getWorld().isClientSide) {
            return true;
        } else {
            ItemStack refund = ItemStack.EMPTY;
            if (this.getFilter(side).getItem() instanceof FilterItem && !player.isCreative()) {
                refund = this.getFilter(side).copy();
            }
            ItemStack copied = ItemStack.of(tag.getCompound("Filter"));
            if (copied.getItem() instanceof FilterItem filterType && !player.isCreative()) {
                InvWrapper inv = new InvWrapper(player.getInventory());
                for (boolean preferStacksWithoutData : Iterate.trueAndFalse) {
                    if (refund.getItem() == filterType || !ItemHelper.extract(inv, stack -> stack.getItem() == filterType && preferStacksWithoutData != stack.hasTag(), 1, false).isEmpty()) {
                        if (!refund.isEmpty() && refund.getItem() != filterType) {
                            player.getInventory().placeItemBackInInventory(refund);
                        }
                        this.setFilter(side, copied);
                        return true;
                    }
                }
                player.displayClientMessage(Lang.translate("logistics.filter.requires_item_in_inventory", copied.getHoverName().copy().withStyle(ChatFormatting.WHITE)).style(ChatFormatting.RED).component(), true);
                AllSoundEvents.DENY.playOnServer(player.m_9236_(), player.m_20183_(), 1.0F, 1.0F);
                return false;
            }
            if (!refund.isEmpty()) {
                player.getInventory().placeItemBackInInventory(refund);
            }
            return this.setFilter(side, copied);
        }
    }
}