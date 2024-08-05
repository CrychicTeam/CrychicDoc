package org.violetmoon.quark.content.automation.block.be;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.content.automation.block.CrafterBlock;
import org.violetmoon.quark.content.automation.inventory.CrafterMenu;
import org.violetmoon.quark.content.automation.module.CrafterModule;

public class CrafterBlockEntity extends BaseContainerBlockEntity implements CraftingContainer, WorldlyContainer {

    private static final DispenseItemBehavior BEHAVIOR = new CrafterBlockEntity.CraftDispenseBehavior();

    public final NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);

    public final ResultContainer result = new ResultContainer();

    public final boolean[] blocked = new boolean[9];

    public final ContainerData delegate;

    private boolean didInitialScan = false;

    public CrafterBlockEntity(final BlockPos pos, BlockState state) {
        super(CrafterModule.blockEntityType, pos, state);
        this.delegate = new ContainerData() {

            @Override
            public int get(int index) {
                int res = CrafterBlockEntity.this.f_58857_.getBlockState(pos).m_61143_(CrafterBlock.POWER) == CrafterBlock.PowerState.TRIGGERED ? 1 : 0;
                for (int i = 0; i < 9; i++) {
                    if (CrafterBlockEntity.this.blocked[i]) {
                        res |= 1 << i + 1;
                    }
                }
                return res;
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ListTag list = new ListTag();
        for (boolean b : this.blocked) {
            list.add(ByteTag.valueOf(b));
        }
        nbt.put("Blocked", list);
        ContainerHelper.saveAllItems(nbt, this.stacks);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("Blocked")) {
            ListTag list = nbt.getList("Blocked", 1);
            for (int i = 0; i < list.size() && i < 9; i++) {
                this.blocked[i] = ((ByteTag) list.get(i)).getAsByte() != 0;
            }
        }
        ContainerHelper.loadAllItems(nbt, this.stacks);
    }

    public void craft() {
        if (this.f_58857_ instanceof ServerLevel sw) {
            this.update();
            BlockSource blockSource = new BlockSourceImpl(sw, this.f_58858_);
            ItemStack itemStack = this.result.getItem(0);
            if (!itemStack.isEmpty()) {
                Direction direction = (Direction) this.m_58900_().m_61143_(CrafterBlock.FACING);
                Container inventory = HopperBlockEntity.getContainerAt(this.f_58857_, this.f_58858_.relative(direction));
                if (inventory == null) {
                    BEHAVIOR.dispense(blockSource, itemStack);
                } else {
                    if (!this.hasSpace(inventory, direction, itemStack)) {
                        return;
                    }
                    if (inventory instanceof CrafterBlockEntity) {
                        int count = itemStack.getCount();
                        for (int i = 0; i < count; i++) {
                            ItemStack is = itemStack.copy();
                            is.setCount(1);
                            HopperBlockEntity.addItem(this.result, inventory, is, direction.getOpposite());
                        }
                    } else {
                        HopperBlockEntity.addItem(this.result, inventory, itemStack, direction.getOpposite());
                    }
                }
                this.takeItems();
                this.update();
            }
        }
    }

    private static IntStream getAvailableSlots(Container inventory, Direction side) {
        return inventory instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer) inventory).getSlotsForFace(side)) : IntStream.range(0, inventory.getContainerSize());
    }

    public boolean hasSpace(Container inv, Direction dir, ItemStack stack) {
        IntStream stream = getAvailableSlots(inv, dir);
        int inserted = 0;
        int slotMax = Math.min(stack.getMaxStackSize(), inv.getMaxStackSize());
        if (CrafterModule.useEmiLogic && inv instanceof CrafterBlockEntity) {
            slotMax = 1;
        }
        for (int i : stream.toArray()) {
            if (inv instanceof WorldlyContainer si && !si.canPlaceItemThroughFace(i, stack, dir)) {
                continue;
            }
            ItemStack is = inv.getItem(i);
            if (is.isEmpty()) {
                inserted += slotMax;
            } else if (ItemStack.isSameItemSameTags(is, stack) && is.getCount() < slotMax) {
                inserted += slotMax - is.getCount();
            }
            if (inserted >= stack.getCount()) {
                return true;
            }
        }
        return false;
    }

    public void takeItems() {
        NonNullList<ItemStack> defaultedList = this.f_58857_.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this, this.f_58857_);
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            BlockSource blockSource = new BlockSourceImpl(serverLevel, this.f_58858_);
            for (int i = 0; i < defaultedList.size(); i++) {
                ItemStack itemInCrafter = this.getItem(i);
                ItemStack remainingItem = defaultedList.get(i);
                if (remainingItem.isEmpty()) {
                    itemInCrafter.shrink(1);
                } else {
                    BEHAVIOR.dispense(blockSource, remainingItem);
                    itemInCrafter.shrink(itemInCrafter.getCount());
                }
            }
        }
        this.update();
    }

    public int getComparatorOutput() {
        int out = 0;
        for (int i = 0; i < 9; i++) {
            if (this.blocked[i] || !this.getItem(i).isEmpty()) {
                out++;
            }
        }
        return out;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, CrafterBlockEntity be) {
        if (!be.didInitialScan && !world.isClientSide) {
            be.update();
            be.didInitialScan = true;
        }
    }

    public static ItemStack getResult(Level world, CraftingContainer craftingInventory) {
        Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory, world);
        if (optional.isPresent()) {
            CraftingRecipe craftingRecipe = (CraftingRecipe) optional.get();
            ItemStack stack = craftingRecipe.m_5874_(craftingInventory, world.registryAccess());
            if (stack.isItemEnabled(world.m_246046_())) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void update() {
        ItemStack stack = getResult(this.f_58857_, this);
        this.result.setItem(0, stack);
        this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, CrafterModule.block);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.stacks.get(slot);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.stacks, slot, amount);
        if (!stack.isEmpty()) {
            this.m_6596_();
        }
        this.update();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = ContainerHelper.takeItem(this.stacks, slot);
        this.update();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        this.update();
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new CrafterMenu(syncId, playerInventory, it -> new TransientCraftingContainer(it, 3, 3, this.stacks), this.result, this.delegate, ContainerLevelAccess.create(this.f_58857_, this.m_58899_()));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.quark.crafter");
    }

    @Override
    public void fillStackedContents(StackedContents finder) {
        for (ItemStack itemstack : this.stacks) {
            finder.accountSimpleStack(itemstack);
        }
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public List<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        ItemStack stackInSlot = this.getItem(slot);
        boolean allowed = stackInSlot.isEmpty();
        if (!CrafterModule.useEmiLogic && !allowed) {
            int min = 999;
            for (int i = 0; i < 9; i++) {
                if (!this.blocked[i]) {
                    ItemStack testStack = this.getItem(i);
                    if (testStack.isEmpty() || ItemStack.isSameItemSameTags(stackInSlot, testStack)) {
                        min = Math.min(min, testStack.getCount());
                    }
                }
            }
            return stackInSlot.getCount() == min;
        } else {
            boolean blockedSlot = this.blocked[slot];
            boolean powered = ((CrafterBlock.PowerState) this.f_58857_.getBlockState(this.f_58858_).m_61143_(CrafterBlock.POWER)).powered();
            return allowed && !blockedSlot && (CrafterModule.allowItemsWhilePowered || !powered);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int ct = 0;
        for (boolean bl : this.blocked) {
            if (!bl) {
                ct++;
            }
        }
        int[] ret = new int[ct];
        int i = 0;
        for (int j = 0; j < this.blocked.length; j++) {
            if (!this.blocked[j]) {
                ret[i] = j;
                i++;
            }
        }
        return ret;
    }

    private static class CraftDispenseBehavior implements DispenseItemBehavior {

        @Override
        public final ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {
            ItemStack itemStack2 = this.dispenseSilently(blockSource, itemStack);
            this.playSound(blockSource);
            this.spawnParticles(blockSource, (Direction) blockSource.getBlockState().m_61143_(CrafterBlock.FACING));
            return itemStack2;
        }

        protected ItemStack dispenseSilently(BlockSource pointer, ItemStack stack) {
            Direction direction = (Direction) pointer.getBlockState().m_61143_(CrafterBlock.FACING);
            Position position = getOutputLocation(pointer);
            spawnItem(pointer.getLevel(), stack, 6, direction, position);
            return stack;
        }

        public static void spawnItem(Level world, ItemStack stack, int speed, Direction side, Position pos) {
            double d = pos.x();
            double e = pos.y();
            double f = pos.z();
            if (side.getAxis() == Direction.Axis.Y) {
                e -= 0.125;
            } else {
                e -= 0.15625;
            }
            ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
            double g = world.random.nextDouble() * 0.1 + 0.2;
            itemEntity.m_20334_(world.random.triangle((double) side.getStepX() * g, 0.0172275 * (double) speed), world.random.triangle(0.2, 0.0172275 * (double) speed), world.random.triangle((double) side.getStepZ() * g, 0.0172275 * (double) speed));
            world.m_7967_(itemEntity);
        }

        protected void playSound(BlockSource pointer) {
            pointer.getLevel().m_46796_(1000, pointer.getPos(), 0);
        }

        protected void spawnParticles(BlockSource pointer, Direction side) {
            pointer.getLevel().m_46796_(2000, pointer.getPos(), side.get3DDataValue());
        }

        private static Position getOutputLocation(BlockSource pointer) {
            Direction direction = (Direction) pointer.getBlockState().m_61143_(CrafterBlock.FACING);
            return pointer.getPos().getCenter().add(0.7 * (double) direction.getStepX(), 0.7 * (double) direction.getStepY(), 0.7 * (double) direction.getStepZ());
        }
    }
}