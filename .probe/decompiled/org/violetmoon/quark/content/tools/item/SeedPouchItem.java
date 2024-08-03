package org.violetmoon.quark.content.tools.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.api.ITrowelable;
import org.violetmoon.quark.api.IUsageTickerOverride;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tools.module.SeedPouchModule;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.IDisableable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.ItemNBTHelper;
import org.violetmoon.zeta.util.RegistryUtil;

public class SeedPouchItem extends ZetaItem implements IUsageTickerOverride, ITrowelable, CreativeTabManager.AppendsUniquely {

    private static final int SEED_BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    private static final int FERTILIZER_BAR_COLOR = Mth.color(0.85F, 0.85F, 0.85F);

    public SeedPouchItem(ZetaModule module) {
        super("seed_pouch", module, new Item.Properties().stacksTo(1));
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Items.LEAD, false);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack pouch, @NotNull ItemStack incoming, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess carriedSlotAccessor) {
        if (pouch.getCount() != 1 || action != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        } else {
            return incoming.isEmpty() ? this.dropOntoEmptyCursor(player, pouch, carriedSlotAccessor) : this.absorbFromCursor(player, pouch, carriedSlotAccessor);
        }
    }

    @Override
    public boolean overrideStackedOnOther(@NotNull ItemStack pouch, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        if (pouch.getCount() == 1 && action == ClickAction.SECONDARY && slot.allowModification(player)) {
            ItemStack droppedOnto = slot.getItem();
            return droppedOnto.isEmpty() ? this.dropIntoEmptySlot(player, pouch, slot) : this.absorbFromSlot(player, pouch, slot);
        } else {
            return false;
        }
    }

    private boolean absorbFromCursor(Player player, ItemStack pouch, SlotAccess cursorAccess) {
        return mutateContents(pouch, contents -> {
            ItemStack onCursor = cursorAccess.get();
            if (!contents.absorb(onCursor)) {
                return false;
            } else {
                cursorAccess.set(onCursor);
                playInsertSound(player);
                return true;
            }
        });
    }

    private boolean absorbFromSlot(Player player, ItemStack pouch, Slot pickupFrom) {
        return mutateContents(pouch, contents -> {
            if (!contents.absorb(pickupFrom.getItem())) {
                return false;
            } else {
                pickupFrom.setChanged();
                playInsertSound(player);
                return true;
            }
        });
    }

    private boolean dropOntoEmptyCursor(Player player, ItemStack pouch, SlotAccess cursorAccess) {
        return mutateContents(pouch, contents -> {
            if (contents.isEmpty()) {
                return false;
            } else {
                cursorAccess.set(contents.splitOneStack());
                playRemoveOneSound(player);
                return true;
            }
        });
    }

    private boolean dropIntoEmptySlot(Player player, ItemStack pouch, Slot depositInto) {
        return mutateContents(pouch, contents -> {
            if (contents.isEmpty()) {
                return false;
            } else {
                depositInto.set(contents.splitOneStack());
                playRemoveOneSound(player);
                return true;
            }
        });
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int count = getCount(stack);
        return Math.round((float) count * 13.0F / (float) SeedPouchModule.maxItems);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return getContents(stack).isFertilizer() ? FERTILIZER_BAR_COLOR : SEED_BAR_COLOR;
    }

    public static SeedPouchItem.PouchContents getContents(ItemStack stack) {
        return SeedPouchItem.PouchContents.readFromStack(stack);
    }

    public static int getCount(ItemStack stack) {
        return SeedPouchItem.PouchContents.readCountOnlyFromStack(stack);
    }

    public static <T> T mutateContents(ItemStack pouch, Function<SeedPouchItem.PouchContents, T> func) {
        return SeedPouchItem.PouchContents.mutate(pouch, func);
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        Component base = super.m_7626_(stack);
        SeedPouchItem.PouchContents contents = getContents(stack);
        if (contents.isEmpty()) {
            return base;
        } else {
            MutableComponent comp = base.copy();
            comp.append(Component.literal(" ("));
            comp.append(contents.getContents().getHoverName());
            comp.append(Component.literal(")"));
            return comp;
        }
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack pouch = context.getItemInHand();
        return mutateContents(pouch, contents -> {
            if (contents.isEmpty()) {
                return super.m_6225_(context);
            } else {
                ItemStack seed = contents.getContents().copy();
                int total = contents.count;
                seed.setCount(Math.min(seed.getMaxStackSize(), total));
                Player player = context.getPlayer();
                if (player != null && player.m_6144_() && !context.getLevel().isClientSide() && context.getLevel().getBlockEntity(BlockPos.containing(context.getClickLocation())) instanceof ChestBlockEntity chest) {
                    Optional<IItemHandler> optionalItemHandler = chest.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH).resolve();
                    if (optionalItemHandler.isPresent()) {
                        ItemStack toInsert = seed.copy();
                        toInsert.setCount(total);
                        ItemStack inserted = ItemHandlerHelper.insertItem((IItemHandler) optionalItemHandler.get(), toInsert, false);
                        int amountInserted = total - inserted.getCount();
                        if (amountInserted == 0) {
                            Component component = Component.literal("The inventory you are trying to insert into is full...").withStyle(ChatFormatting.RED);
                            player.displayClientMessage(component, true);
                        } else {
                            contents.shrink(amountInserted);
                        }
                    }
                }
                if (player != null && player.m_6144_()) {
                    InteractionResult bestRes = InteractionResult.FAIL;
                    int range = contents.isSeed() ? SeedPouchModule.shiftRange : SeedPouchModule.fertilizerShiftRange;
                    int blocks = range * range;
                    int shift = -((int) Math.floor((double) ((float) range / 2.0F)));
                    for (int i = 0; i < blocks; i++) {
                        int x = shift + i % range;
                        int z = shift + i / range;
                        InteractionResult res = this.placeSeed(contents, context, seed, context.getClickedPos().offset(x, 0, z));
                        if (contents.isEmpty()) {
                            break;
                        }
                        if (!bestRes.consumesAction()) {
                            bestRes = res;
                        }
                    }
                    return bestRes;
                } else {
                    return this.placeSeed(contents, context, seed, context.getClickedPos());
                }
            }
        });
    }

    private InteractionResult placeSeed(SeedPouchItem.PouchContents mutableContents, UseOnContext context, ItemStack seed, BlockPos pos) {
        Player player = context.getPlayer();
        InteractionResult res;
        if (player == null) {
            res = seed.getItem().useOn(new SeedPouchItem.PouchItemUseContext(context, seed, pos));
        } else {
            ItemStack restore = player.m_21120_(context.getHand());
            player.m_21008_(context.getHand(), seed);
            res = seed.getItem().useOn(new SeedPouchItem.PouchItemUseContext(context, seed, pos));
            player.m_21008_(context.getHand(), restore);
        }
        int itemsToTake = res == InteractionResult.CONSUME ? 1 : 0;
        if (itemsToTake != 0 && (player == null || !player.getAbilities().instabuild)) {
            mutableContents.shrink(itemsToTake);
        }
        return res;
    }

    @Override
    public List<ItemStack> appendItemsToCreativeTab() {
        if (!this.isEnabled()) {
            return List.of();
        } else {
            List<ItemStack> list = new ArrayList();
            list.add(new ItemStack(this));
            if (SeedPouchModule.showAllVariantsInCreative) {
                RegistryAccess access = Quark.proxy.hackilyGetCurrentClientLevelRegistryAccess();
                if (access != null) {
                    (SeedPouchModule.allowFertilizer ? Stream.of(SeedPouchModule.seedPouchHoldableTag, SeedPouchModule.seedPouchFertilizersTag) : Stream.of(SeedPouchModule.seedPouchHoldableTag)).flatMap(tag -> RegistryUtil.getTagValues(access, tag).stream()).filter(IDisableable::isEnabled).map(seed -> {
                        SeedPouchItem.PouchContents contents = new SeedPouchItem.PouchContents();
                        contents.setContents(new ItemStack(seed));
                        contents.setCount(SeedPouchModule.maxItems);
                        return contents.writeToStack(new ItemStack(this));
                    }).forEach(list::add);
                }
            }
            return list;
        }
    }

    @Override
    public ItemStack getUsageTickerItem(ItemStack stack) {
        SeedPouchItem.PouchContents contents = getContents(stack);
        return contents.isEmpty() ? stack : contents.getContents();
    }

    @Override
    public int getUsageTickerCountForItem(ItemStack stack, Predicate<ItemStack> target) {
        SeedPouchItem.PouchContents contents = getContents(stack);
        return !contents.isEmpty() && target.test(contents.getContents()) ? contents.getCount() : 0;
    }

    @NotNull
    @Override
    public Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return getCount(stack) == 0 ? Optional.empty() : Optional.of(new SeedPouchItem.Tooltip(stack));
    }

    public static class PouchContents {

        public static final String TAG_STORED_ITEM = "storedItem";

        public static final String TAG_COUNT = "itemCount";

        private ItemStack contents = ItemStack.EMPTY;

        private int count = 0;

        public ItemStack writeToStack(ItemStack target) {
            CompoundTag tag = target.getTag();
            if (this.isEmpty()) {
                if (tag != null) {
                    tag.remove("storedItem");
                    tag.remove("itemCount");
                    if (tag.isEmpty()) {
                        target.setTag(null);
                    }
                }
            } else {
                ItemNBTHelper.setCompound(target, "storedItem", this.contents.save(new CompoundTag()));
                ItemNBTHelper.setInt(target, "itemCount", this.count);
            }
            return target;
        }

        public static SeedPouchItem.PouchContents readFromStack(ItemStack target) {
            CompoundTag tag = target.getTag();
            SeedPouchItem.PouchContents contents = new SeedPouchItem.PouchContents();
            if (tag != null && tag.contains("storedItem") && tag.contains("itemCount")) {
                contents.contents = ItemStack.of(tag.getCompound("storedItem"));
                contents.count = tag.getInt("itemCount");
            }
            return contents;
        }

        public static int readCountOnlyFromStack(ItemStack target) {
            return ItemNBTHelper.getInt(target, "itemCount", 0);
        }

        public static <T> T mutate(ItemStack pouch, Function<SeedPouchItem.PouchContents, T> action) {
            SeedPouchItem.PouchContents contents = readFromStack(pouch);
            T result = (T) action.apply(contents);
            contents.writeToStack(pouch);
            return result;
        }

        public boolean isEmpty() {
            return this.contents.isEmpty() || this.count == 0;
        }

        public ItemStack getContents() {
            return this.contents;
        }

        public int getCount() {
            return this.count;
        }

        public boolean isSeed() {
            return !this.isEmpty() && this.contents.is(SeedPouchModule.seedPouchHoldableTag);
        }

        public boolean isFertilizer() {
            return !this.isEmpty() && this.contents.is(SeedPouchModule.seedPouchFertilizersTag);
        }

        public void setContents(ItemStack contents) {
            this.contents = contents.copy();
            this.contents.setCount(1);
        }

        public void setCount(int newCount) {
            this.count = newCount;
            if (this.count <= 0) {
                this.count = 0;
                this.contents = ItemStack.EMPTY;
            }
        }

        public void grow(int more) {
            this.setCount(this.count + more);
        }

        public void shrink(int less) {
            this.setCount(this.count - less);
        }

        public boolean absorb(ItemStack other) {
            if (!this.canFit(other)) {
                return false;
            } else {
                int toMove = Math.min(SeedPouchModule.maxItems - this.count, other.getCount());
                if (toMove == 0) {
                    return false;
                } else {
                    if (this.isEmpty()) {
                        this.setContents(other);
                        this.setCount(toMove);
                    } else {
                        this.grow(toMove);
                    }
                    other.shrink(toMove);
                    return true;
                }
            }
        }

        public ItemStack split(int request) {
            int howMany = Math.min(this.count, request);
            ItemStack result = this.contents.copy();
            result.setCount(howMany);
            this.shrink(howMany);
            return result;
        }

        public ItemStack splitOneStack() {
            return this.split(this.contents.getMaxStackSize());
        }

        public boolean canFit(ItemStack other) {
            return this.isEmpty() ? other.is(SeedPouchModule.seedPouchHoldableTag) || SeedPouchModule.allowFertilizer && other.is(SeedPouchModule.seedPouchFertilizersTag) : this.count < SeedPouchModule.maxItems && ItemStack.isSameItemSameTags(this.contents, other);
        }
    }

    public static class PouchItemUseContext extends UseOnContext {

        protected PouchItemUseContext(UseOnContext parent, ItemStack stack, BlockPos targetPos) {
            super(parent.getLevel(), parent.getPlayer(), parent.getHand(), stack, new BlockHitResult(parent.getClickLocation(), parent.getClickedFace(), targetPos, parent.isInside()));
        }
    }

    public static record Tooltip(ItemStack stack) implements TooltipComponent {
    }
}