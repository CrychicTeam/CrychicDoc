package mezz.jei.gui.input.handlers;

import java.util.List;
import java.util.Optional;
import mezz.jei.common.config.GiveMode;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IClientToggleState;
import mezz.jei.common.gui.TooltipRenderer;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.network.packets.PacketDeletePlayerItem;
import mezz.jei.common.network.packets.PacketJei;
import mezz.jei.common.util.ServerCommandUtil;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import mezz.jei.gui.overlay.IIngredientGrid;
import mezz.jei.gui.util.CheatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DeleteItemInputHandler implements IUserInputHandler {

    private final IIngredientGrid ingredientGrid;

    private final IClientToggleState toggleState;

    private final IClientConfig clientConfig;

    private final IConnectionToServer serverConnection;

    private final CheatUtil cheatUtil;

    public DeleteItemInputHandler(IIngredientGrid ingredientGrid, IClientToggleState toggleState, IClientConfig clientConfig, IConnectionToServer serverConnection, CheatUtil cheatUtil) {
        this.ingredientGrid = ingredientGrid;
        this.toggleState = toggleState;
        this.clientConfig = clientConfig;
        this.serverConnection = serverConnection;
        this.cheatUtil = cheatUtil;
    }

    @Override
    public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput userInput, IInternalKeyMappings keyBindings) {
        if (!userInput.is(keyBindings.getLeftClick())) {
            return Optional.empty();
        } else {
            double mouseX = userInput.getMouseX();
            double mouseY = userInput.getMouseY();
            if (!this.ingredientGrid.isMouseOver(mouseX, mouseY)) {
                return Optional.empty();
            } else {
                Minecraft minecraft = Minecraft.getInstance();
                if (!this.shouldDeleteItemOnClick(minecraft, mouseX, mouseY)) {
                    return Optional.empty();
                } else {
                    LocalPlayer player = minecraft.player;
                    if (player == null) {
                        return Optional.empty();
                    } else {
                        ItemStack itemStack = player.f_36096_.getCarried();
                        if (itemStack.isEmpty()) {
                            return Optional.empty();
                        } else {
                            if (!userInput.isSimulate()) {
                                player.f_36096_.setCarried(ItemStack.EMPTY);
                                PacketJei packet = new PacketDeletePlayerItem(itemStack);
                                this.serverConnection.sendPacketToServer(packet);
                            }
                            return Optional.of(this);
                        }
                    }
                }
            }
        }
    }

    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component deleteItem = Component.translatable("jei.tooltip.delete.item");
        TooltipRenderer.drawHoveringText(guiGraphics, List.of(deleteItem), mouseX, mouseY);
    }

    public boolean shouldDeleteItemOnClick(Minecraft minecraft, double mouseX, double mouseY) {
        if (this.toggleState.isCheatItemsEnabled() && this.serverConnection.isJeiOnServer()) {
            Player player = minecraft.player;
            if (player == null) {
                return false;
            } else {
                ItemStack itemStack = player.containerMenu.getCarried();
                if (itemStack.isEmpty()) {
                    return false;
                } else {
                    GiveMode giveMode = this.clientConfig.getGiveMode();
                    return giveMode == GiveMode.MOUSE_PICKUP ? (Boolean) this.ingredientGrid.getIngredientUnderMouse(mouseX, mouseY).findFirst().map(this.cheatUtil::getCheatItemStack).map(i -> !ServerCommandUtil.canStack(itemStack, i)).orElse(true) : true;
                }
            }
        } else {
            return false;
        }
    }
}