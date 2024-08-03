package se.mickelus.tetra.blocks.forged.container;

import java.util.Arrays;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
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
import se.mickelus.tetra.TetraMod;

@ParametersAreNonnullByDefault
public class ForgedContainerMenu extends AbstractContainerMenu {

    public static RegistryObject<MenuType<ForgedContainerMenu>> type;

    private final ForgedContainerBlockEntity tile;

    private ToggleableSlot[][] compartmentSlots;

    private int currentCompartment = 0;

    public ForgedContainerMenu(int windowId, ForgedContainerBlockEntity tile, Container playerInventory, @Nullable Player player) {
        super(type.get(), windowId);
        this.tile = tile;
        tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.compartmentSlots = new ToggleableSlot[ForgedContainerBlockEntity.compartmentCount][];
            for (int ix = 0; ix < this.compartmentSlots.length; ix++) {
                this.compartmentSlots[ix] = new ToggleableSlot[ForgedContainerBlockEntity.compartmentSize];
                int offset = ix * ForgedContainerBlockEntity.compartmentSize;
                for (int jx = 0; jx < 6; jx++) {
                    for (int k = 0; k < 9; k++) {
                        int index = jx * 9 + k;
                        this.compartmentSlots[ix][index] = new ToggleableSlot(handler, index + offset, k * 17 + 12, jx * 17);
                        this.compartmentSlots[ix][index].toggle(ix == 0);
                        this.m_38897_(this.compartmentSlots[ix][index]);
                    }
                }
            }
        });
        IItemHandler playerInventoryHandler = new InvWrapper(playerInventory);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new SlotItemHandler(playerInventoryHandler, i * 9 + j + 9, j * 17 + 12, i * 17 + 116));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.m_38897_(new SlotItemHandler(playerInventoryHandler, i, i * 17 + 12, 171));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static ForgedContainerMenu create(int windowId, BlockPos pos, Inventory inv) {
        ForgedContainerBlockEntity te = (ForgedContainerBlockEntity) Minecraft.getInstance().level.m_7702_(pos);
        return new ForgedContainerMenu(windowId, te, inv, Minecraft.getInstance().player);
    }

    public void changeCompartment(int compartmentIndex) {
        this.currentCompartment = compartmentIndex;
        for (int i = 0; i < this.compartmentSlots.length; i++) {
            boolean enabled = i == compartmentIndex;
            Arrays.stream(this.compartmentSlots[i]).forEach(slot -> slot.toggle(enabled));
        }
        if (this.tile.m_58904_().isClientSide) {
            TetraMod.packetHandler.sendToServer(new ChangeCompartmentPacket(compartmentIndex));
        }
    }

    private int getSlots() {
        return (Integer) this.tile.getCapability(ForgeCapabilities.ITEM_HANDLER).map(IItemHandler::getSlots).orElse(0);
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
            } else if (!this.m_38903_(slotStack, this.currentCompartment * ForgedContainerBlockEntity.compartmentSize, (this.currentCompartment + 1) * ForgedContainerBlockEntity.compartmentSize, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return resultStack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return m_38889_(ContainerLevelAccess.create(this.tile.m_58904_(), this.tile.m_58899_()), playerIn, ForgedContainerBlock.instance.get());
    }

    public ForgedContainerBlockEntity getTile() {
        return this.tile;
    }
}