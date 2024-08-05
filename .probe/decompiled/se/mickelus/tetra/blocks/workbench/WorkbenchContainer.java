package se.mickelus.tetra.blocks.workbench;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.gui.ToggleableSlot;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class WorkbenchContainer extends AbstractContainerMenu {

    public static RegistryObject<MenuType<WorkbenchContainer>> containerType;

    private final WorkbenchTile workbench;

    private ToggleableSlot[] materialSlots = new ToggleableSlot[0];

    public WorkbenchContainer(int windowId, WorkbenchTile workbench, Container playerInventory, Player player) {
        super(containerType.get(), windowId);
        this.workbench = workbench;
        workbench.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.m_38897_(new SlotItemHandler(handler, 0, 152, 58));
            this.materialSlots = new ToggleableSlot[3];
            for (int ix = 0; ix < this.materialSlots.length; ix++) {
                this.materialSlots[ix] = new ToggleableSlot(handler, ix + 1, 167 + 28 * ix, 108);
                this.m_38897_(this.materialSlots[ix]);
            }
        });
        IItemHandler playerInventoryHandler = new InvWrapper(playerInventory);
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                this.m_38897_(new SlotItemHandler(playerInventoryHandler, y * 9 + x + 9, x * 17 + 84, y * 17 + 166));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.m_38897_(new SlotItemHandler(playerInventoryHandler, i, i * 17 + 84, 221));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static WorkbenchContainer create(int windowId, BlockPos pos, Inventory inv) {
        WorkbenchTile te = (WorkbenchTile) Minecraft.getInstance().level.m_7702_(pos);
        return new WorkbenchContainer(windowId, te, inv, Minecraft.getInstance().player);
    }

    private int getSlots() {
        return (Integer) this.workbench.getCapability(ForgeCapabilities.ITEM_HANDLER).map(IItemHandler::getSlots).orElse(0);
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos pos = this.workbench.m_58899_();
        return this.workbench.m_58904_().getBlockState(this.workbench.m_58899_()).m_60734_() instanceof AbstractWorkbenchBlock ? player.m_20275_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5) <= 64.0 : false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack resultStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            resultStack = slotStack.copy();
            if (index < this.getSlots()) {
                if (!this.m_38903_(slotStack, this.getSlots(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(slotStack, 0, this.getSlots(), false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        this.workbench.setChanged();
        return resultStack;
    }

    public void updateSlots() {
        int numMaterialSlots = (Integer) Optional.ofNullable(this.workbench.getCurrentSchematic()).map(UpgradeSchematic::getNumMaterialSlots).orElse(0);
        for (int i = 0; i < this.materialSlots.length; i++) {
            this.materialSlots[i].toggle(i < numMaterialSlots);
            this.materialSlots[i].f_40220_ = 194 + getSlotOffsetY(i, numMaterialSlots);
        }
    }

    public static int getSlotOffsetY(int index, int numMaterialSlots) {
        return numMaterialSlots == 2 ? 11 + 32 * index : 28 * index;
    }

    public WorkbenchTile getTileEntity() {
        return this.workbench;
    }
}