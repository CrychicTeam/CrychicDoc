package mezz.jei.common.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.Optional;
import mezz.jei.common.config.GiveMode;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.network.IConnectionToClient;
import mezz.jei.common.network.ServerPacketContext;
import mezz.jei.common.network.packets.PacketCheatPermission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ServerCommandUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private ServerCommandUtil() {
    }

    public static boolean hasPermissionForCheatMode(ServerPlayer sender, IServerConfig serverConfig) {
        if (serverConfig.isCheatModeEnabledForCreative() && sender.isCreative()) {
            return true;
        } else {
            CommandSourceStack commandSource = sender.m_20203_();
            if (serverConfig.isCheatModeEnabledForOp()) {
                MinecraftServer minecraftServer = sender.m_20194_();
                if (minecraftServer != null) {
                    int opPermissionLevel = minecraftServer.getOperatorUserPermissionLevel();
                    return commandSource.hasPermission(opPermissionLevel);
                }
            }
            return serverConfig.isCheatModeEnabledForGive() ? (Boolean) getGiveCommand(sender).map(giveCommand -> giveCommand.canUse(commandSource)).orElse(false) : false;
        }
    }

    public static void executeGive(ServerPacketContext context, ItemStack itemStack, GiveMode giveMode) {
        ServerPlayer sender = context.player();
        IServerConfig serverConfig = context.serverConfig();
        if (hasPermissionForCheatMode(sender, serverConfig)) {
            if (itemStack.isEmpty()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Player '{} ({})' tried to give an empty ItemStack.", sender.m_7755_(), sender.m_20148_());
                }
                return;
            }
            if (giveMode == GiveMode.INVENTORY) {
                giveToInventory(sender, itemStack);
            } else if (giveMode == GiveMode.MOUSE_PICKUP) {
                mousePickupItemStack(sender, itemStack);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Player '{} ({})' tried to cheat an ItemStack '{}' but does not have permission.", sender.m_7755_(), sender.m_20148_(), itemStack.getDisplayName());
            }
            IConnectionToClient connection = context.connection();
            connection.sendPacketToClient(new PacketCheatPermission(false), sender);
        }
    }

    public static void setHotbarSlot(ServerPacketContext context, ItemStack itemStack, int hotbarSlot) {
        ServerPlayer sender = context.player();
        IServerConfig serverConfig = context.serverConfig();
        if (hasPermissionForCheatMode(sender, serverConfig)) {
            if (itemStack.isEmpty()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Player '{} ({})' tried to set an empty ItemStack to the hotbar slot: {}", sender.m_7755_(), sender.m_20148_(), hotbarSlot);
                }
                return;
            }
            if (!Inventory.isHotbarSlot(hotbarSlot)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Player '{} ({})' tried to set slot that is not in the hotbar: {}", sender.m_7755_(), sender.m_20148_(), hotbarSlot);
                }
                return;
            }
            ItemStack stackInSlot = sender.m_150109_().getItem(hotbarSlot);
            if (ItemStack.matches(stackInSlot, itemStack)) {
                return;
            }
            ItemStack itemStackCopy = itemStack.copy();
            sender.m_150109_().setItem(hotbarSlot, itemStack);
            sender.m_9236_().playSound(null, sender.m_20185_(), sender.m_20186_(), sender.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((sender.m_217043_().nextFloat() - sender.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            sender.f_36095_.m_38946_();
            notifyGive(sender, itemStackCopy);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Player '{} ({})' tried to cheat an item '{}' to their hotbar but does not have permission.", sender.m_7755_(), sender.m_20148_(), itemStack.getDisplayName());
            }
            IConnectionToClient connection = context.connection();
            connection.sendPacketToClient(new PacketCheatPermission(false), sender);
        }
    }

    public static void mousePickupItemStack(Player sender, ItemStack itemStack) {
        AbstractContainerMenu containerMenu = sender.containerMenu;
        ItemStack itemStackCopy = itemStack.copy();
        ItemStack existingStack = containerMenu.getCarried();
        int giveCount;
        if (canStack(existingStack, itemStack)) {
            int newCount = Math.min(existingStack.getMaxStackSize(), existingStack.getCount() + itemStack.getCount());
            giveCount = newCount - existingStack.getCount();
            if (giveCount > 0) {
                existingStack.setCount(newCount);
            }
        } else {
            containerMenu.setCarried(itemStack);
            giveCount = itemStack.getCount();
        }
        if (giveCount > 0) {
            itemStackCopy.setCount(giveCount);
            notifyGive(sender, itemStackCopy);
            containerMenu.broadcastChanges();
        }
    }

    public static boolean canStack(ItemStack a, ItemStack b) {
        ItemStack singleA = a.copyWithCount(1);
        ItemStack singleB = b.copyWithCount(1);
        return ItemEntity.areMergable(singleA, singleB);
    }

    private static void giveToInventory(Player entityplayermp, ItemStack itemStack) {
        ItemStack itemStackCopy = itemStack.copy();
        boolean flag = entityplayermp.getInventory().add(itemStack);
        if (flag && itemStack.isEmpty()) {
            itemStack.setCount(1);
            ItemEntity entityitem = entityplayermp.drop(itemStack, false);
            if (entityitem != null) {
                entityitem.makeFakeItem();
            }
            entityplayermp.m_9236_().playSound(null, entityplayermp.m_20185_(), entityplayermp.m_20186_(), entityplayermp.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((entityplayermp.m_217043_().nextFloat() - entityplayermp.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayermp.inventoryMenu.m_38946_();
        } else {
            ItemEntity entityitem = entityplayermp.drop(itemStack, false);
            if (entityitem != null) {
                entityitem.setNoPickUpDelay();
                entityitem.setTarget(entityplayermp.m_20148_());
            }
        }
        notifyGive(entityplayermp, itemStackCopy);
    }

    private static void notifyGive(Player player, ItemStack stack) {
        if (player.m_20194_() != null) {
            CommandSourceStack commandSource = player.m_20203_();
            int count = stack.getCount();
            Component stackTextComponent = stack.getDisplayName();
            Component displayName = player.getDisplayName();
            Component message = Component.translatable("commands.give.success.single", count, stackTextComponent, displayName);
            commandSource.sendSuccess(() -> message, true);
        }
    }

    private static Optional<CommandNode<CommandSourceStack>> getGiveCommand(Player sender) {
        return Optional.ofNullable(sender.m_20194_()).map(minecraftServer -> {
            Commands commandManager = minecraftServer.getCommands();
            CommandDispatcher<CommandSourceStack> dispatcher = commandManager.getDispatcher();
            RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();
            return root.getChild("give");
        });
    }
}