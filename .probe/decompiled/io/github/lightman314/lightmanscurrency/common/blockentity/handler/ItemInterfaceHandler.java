package io.github.lightman314.lightmanscurrency.common.blockentity.handler;

import io.github.lightman314.lightmanscurrency.common.blockentity.ItemTraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.traderinterface.handlers.ConfigurableSidedHandler;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemInterfaceHandler extends ConfigurableSidedHandler<IItemHandler> {

    public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "item_interface");

    protected final ItemTraderInterfaceBlockEntity blockEntity;

    private final Map<Direction, ItemInterfaceHandler.Handler> handlers = new HashMap();

    protected final TraderItemStorage getItemBuffer() {
        return this.blockEntity.getItemBuffer();
    }

    public ItemInterfaceHandler(ItemTraderInterfaceBlockEntity blockEntity, Supplier<TraderItemStorage> itemBufferSource) {
        this.blockEntity = blockEntity;
    }

    @Override
    public ResourceLocation getType() {
        return TYPE;
    }

    @Override
    public String getTag() {
        return "ItemData";
    }

    public IItemHandler getHandler(Direction side) {
        if (!this.inputSides.get(side) && !this.outputSides.get(side)) {
            return null;
        } else {
            if (!this.handlers.containsKey(side)) {
                this.handlers.put(side, new ItemInterfaceHandler.Handler(this, side));
            }
            return (IItemHandler) this.handlers.get(side);
        }
    }

    private static class Handler implements IItemHandler {

        final ItemInterfaceHandler handler;

        final Direction side;

        Handler(ItemInterfaceHandler handler, Direction side) {
            this.handler = handler;
            this.side = side;
        }

        protected final boolean allowInputs() {
            return this.handler.inputSides.get(this.side);
        }

        protected final boolean allowOutputs() {
            return this.handler.outputSides.get(this.side);
        }

        @Override
        public int getSlots() {
            return this.handler.getItemBuffer().getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return this.handler.getItemBuffer().getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (this.allowInputs() && this.handler.blockEntity.allowInput(stack)) {
                ItemStack result = this.handler.getItemBuffer().insertItem(slot, stack, simulate);
                if (!simulate) {
                    this.handler.blockEntity.setItemBufferDirty();
                }
                return result;
            } else {
                return stack.copy();
            }
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (this.allowOutputs()) {
                ItemStack stackInSlot = this.getStackInSlot(slot);
                if (this.handler.blockEntity.allowOutput(stackInSlot) && !stackInSlot.isEmpty()) {
                    ItemStack result = this.handler.getItemBuffer().extractItem(slot, amount, simulate);
                    if (!simulate) {
                        this.handler.blockEntity.setItemBufferDirty();
                    }
                    return result;
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return this.handler.blockEntity.getStorageStackLimit();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return this.allowInputs() && this.handler.blockEntity.allowInput(stack);
        }
    }
}