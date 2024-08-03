package mezz.jei.gui.util;

import mezz.jei.common.config.GiveMode;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.network.packets.PacketGiveItemStack;
import mezz.jei.common.network.packets.PacketSetHotbarItemStack;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.common.util.ServerCommandUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CommandUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private final IClientConfig clientConfig;

    private final IConnectionToServer serverConnection;

    public CommandUtil(IClientConfig clientConfig, IConnectionToServer serverConnection) {
        this.clientConfig = clientConfig;
        this.serverConnection = serverConnection;
    }

    public void giveStack(ItemStack itemStack, GiveAmount giveAmount) {
        GiveMode giveMode = this.clientConfig.getGiveMode();
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            LOGGER.error("Can't give stack, there is no player");
        } else {
            int amount = giveAmount.getAmountForStack(itemStack);
            if (minecraft.screen instanceof CreativeModeInventoryScreen && giveMode == GiveMode.MOUSE_PICKUP) {
                ItemStack sendStack = copyWithSize(itemStack, amount);
                ServerCommandUtil.mousePickupItemStack(player, sendStack);
            } else if (this.serverConnection.isJeiOnServer()) {
                ItemStack sendStack = copyWithSize(itemStack, amount);
                PacketGiveItemStack packet = new PacketGiveItemStack(sendStack, giveMode);
                this.serverConnection.sendPacketToServer(packet);
            } else {
                giveStackVanilla(itemStack, amount);
            }
        }
    }

    public void setHotbarStack(ItemStack itemStack, int hotbarSlot) {
        if (this.serverConnection.isJeiOnServer()) {
            ItemStack sendStack = copyWithSize(itemStack, itemStack.getMaxStackSize());
            PacketSetHotbarItemStack packet = new PacketSetHotbarItemStack(sendStack, hotbarSlot);
            this.serverConnection.sendPacketToServer(packet);
        }
    }

    private static void giveStackVanilla(ItemStack itemStack, int amount) {
        if (itemStack.isEmpty()) {
            String stackInfo = ErrorUtil.getItemStackInfo(itemStack);
            LOGGER.error("Empty itemStack: {}", stackInfo, new IllegalArgumentException());
        } else {
            LocalPlayer sender = Minecraft.getInstance().player;
            if (sender != null && sender.m_7500_()) {
                sendCreativeInventoryActions(sender, itemStack, amount);
            }
        }
    }

    private static void sendCreativeInventoryActions(LocalPlayer sender, ItemStack stack, int amount) {
        for (int i = 0; i < sender.m_150109_().items.size() && amount > 0; i++) {
            ItemStack currentStack = sender.m_150109_().items.get(i);
            if (currentStack.isEmpty()) {
                ItemStack sendAllRemaining = copyWithSize(stack, amount);
                sendSlotPacket(sendAllRemaining, i);
                amount = 0;
            } else if (ItemStack.isSameItem(currentStack, stack) && currentStack.getMaxStackSize() > currentStack.getCount()) {
                int canAdd = Math.min(currentStack.getMaxStackSize() - currentStack.getCount(), amount);
                ItemStack fillRemainingSpace = copyWithSize(stack, canAdd + currentStack.getCount());
                sendSlotPacket(fillRemainingSpace, i);
                amount -= canAdd;
            }
        }
        if (amount > 0) {
            ItemStack toDrop = copyWithSize(stack, amount);
            sendSlotPacket(toDrop, -1);
        }
    }

    private static void sendSlotPacket(ItemStack stack, int mainInventorySlot) {
        if (mainInventorySlot < 9 && mainInventorySlot != -1) {
            mainInventorySlot += 36;
        }
        Minecraft minecraft = Minecraft.getInstance();
        MultiPlayerGameMode playerController = minecraft.gameMode;
        if (playerController != null) {
            playerController.handleCreativeModeItemAdd(stack, mainInventorySlot);
        } else {
            LOGGER.error("Cannot send slot packet, minecraft.playerController is null");
        }
    }

    private static ItemStack copyWithSize(ItemStack itemStack, int size) {
        if (size == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack copy = itemStack.copy();
            copy.setCount(size);
            return copy;
        }
    }
}