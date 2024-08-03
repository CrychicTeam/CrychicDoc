package net.blay09.mods.craftingtweaks.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.ItemCraftedEvent;
import net.blay09.mods.balm.api.event.client.ConnectedToServerEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenKeyEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.craftingtweaks.CompressType;
import net.blay09.mods.craftingtweaks.CraftingGuideButtonFixer;
import net.blay09.mods.craftingtweaks.CraftingTweaks;
import net.blay09.mods.craftingtweaks.CraftingTweaksProviderManager;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksClientAPI;
import net.blay09.mods.craftingtweaks.api.GridGuiHandler;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridGuiHandler;
import net.blay09.mods.craftingtweaks.api.impl.InternalClientMethodsImpl;
import net.blay09.mods.craftingtweaks.config.CraftingTweaksConfig;
import net.blay09.mods.craftingtweaks.config.CraftingTweaksMode;
import net.blay09.mods.craftingtweaks.network.BalanceMessage;
import net.blay09.mods.craftingtweaks.network.ClearMessage;
import net.blay09.mods.craftingtweaks.network.CompressMessage;
import net.blay09.mods.craftingtweaks.network.CraftStackMessage;
import net.blay09.mods.craftingtweaks.network.RotateMessage;
import net.blay09.mods.craftingtweaks.network.TransferStackMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class CraftingTweaksClient {

    private static final ClientProvider clientProvider = new ClientProvider();

    private static boolean ignoreMouseUp;

    private static int rightClickCraftingSlot = -1;

    private static Button unpleasantButton;

    private static int fixedUnpleasantButtonX;

    public static void initialize() {
        CraftingTweaksClientAPI.setupAPI(new InternalClientMethodsImpl());
        CraftingTweaksClientAPI.registerCraftingGridGuiHandler(AbstractContainerScreen.class, new DefaultGridGuiHandler());
        ModKeyMappings.initialize();
        Balm.getEvents().onEvent(ItemCraftedEvent.class, CraftingTweaksClient::onItemCrafted);
        Balm.getEvents().onEvent(ConnectedToServerEvent.class, it -> CraftingTweaks.isServerSideInstalled = false);
        Balm.getEvents().onEvent(ScreenInitEvent.Post.class, CraftingTweaksClient::screenInitialized);
        Balm.getEvents().onEvent(ScreenKeyEvent.Press.Post.class, CraftingTweaksClient::screenKeyPressed);
        Balm.getEvents().onEvent(ScreenMouseEvent.Click.Pre.class, CraftingTweaksClient::screenMouseClick);
        Balm.getEvents().onEvent(ScreenMouseEvent.Release.Pre.class, CraftingTweaksClient::screenMouseRelease);
        Balm.getEvents().onEvent(ScreenDrawEvent.Pre.class, CraftingTweaksClient::screenAboutToDraw);
        Balm.getEvents().onEvent(ScreenDrawEvent.Post.class, CraftingTweaksClient::screenDrawn);
        CraftingTweaksDebugger.initialize();
    }

    public static boolean screenKeyPressed(ScreenKeyEvent event) {
        Screen screen = event.getScreen();
        int key = event.getKey();
        int scanCode = event.getScanCode();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        } else {
            AbstractContainerMenu menu = player.f_36096_;
            if (menu == null) {
                return false;
            } else if (!(screen instanceof AbstractContainerScreen)) {
                return false;
            } else {
                Window window = Minecraft.getInstance().getWindow();
                if (CraftingTweaks.isServerSideInstalled && GLFW.glfwGetKey(window.getWindow(), 66) == 1 && GLFW.glfwGetKey(window.getWindow(), 76) == 1 && GLFW.glfwGetKey(window.getWindow(), 65) == 1 && (GLFW.glfwGetKey(window.getWindow(), 89) == 1 || GLFW.glfwGetKey(window.getWindow(), 90) == 1)) {
                    CraftingTweaks.isServerSideInstalled = false;
                    player.displayClientMessage(Component.literal("[CraftingTweaks] Enabled client-side testing mode"), false);
                }
                CraftingGrid grid = (CraftingGrid) CraftingTweaksProviderManager.getDefaultCraftingGrid(menu).orElse(null);
                CompressType compressType = ModKeyMappings.getCompressTypeForKey(key, scanCode, event.getModifiers());
                if (grid == null) {
                    if (CraftingTweaks.isServerSideInstalled) {
                        AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) screen;
                        if (compressType != null) {
                            Slot mouseSlot = ((AbstractContainerScreenAccessor) containerScreen).getHoveredSlot();
                            if (mouseSlot != null) {
                                Balm.getNetworking().sendToServer(new CompressMessage(mouseSlot.index, compressType));
                            }
                            return true;
                        }
                    }
                } else {
                    String modId = grid.getId().getNamespace();
                    CraftingTweaksMode config = CraftingTweaksConfig.getActive().getCraftingTweaksMode(modId);
                    if (config == CraftingTweaksMode.DEFAULT || config == CraftingTweaksMode.HOTKEYS) {
                        boolean isRotate = ModKeyMappings.keyRotate.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isRotateCCW = ModKeyMappings.keyRotateCounterClockwise.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isBalance = ModKeyMappings.keyBalance.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isSpread = ModKeyMappings.keySpread.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isClear = ModKeyMappings.keyClear.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isForceClear = ModKeyMappings.keyForceClear.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isRefill = ModKeyMappings.keyRefillLast.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        boolean isRefillStack = ModKeyMappings.keyRefillLastStack.isActiveAndMatchesKey(key, scanCode, event.getModifiers());
                        if (isRotate || isRotateCCW) {
                            if (CraftingTweaks.isServerSideInstalled) {
                                Balm.getNetworking().sendToServer(new RotateMessage(grid.getId(), isRotateCCW));
                            } else {
                                clientProvider.rotateGrid(player, menu, grid, isRotateCCW);
                            }
                            return true;
                        }
                        if (isClear || isForceClear) {
                            if (CraftingTweaks.isServerSideInstalled) {
                                Balm.getNetworking().sendToServer(new ClearMessage(grid.getId(), isForceClear));
                            } else {
                                clientProvider.clearGrid(player, menu, grid, isForceClear);
                            }
                            return true;
                        }
                        if (isBalance || isSpread) {
                            if (CraftingTweaks.isServerSideInstalled) {
                                Balm.getNetworking().sendToServer(new BalanceMessage(grid.getId(), isSpread));
                            } else if (isSpread) {
                                clientProvider.spreadGrid(player, menu, grid);
                            } else {
                                clientProvider.balanceGrid(player, menu, grid);
                            }
                            return true;
                        }
                        if (isRefill || isRefillStack) {
                            if (CraftingTweaks.isServerSideInstalled) {
                                clientProvider.refillLastCrafted(player, menu, grid, isRefillStack);
                            } else {
                                clientProvider.refillLastCrafted(player, menu, grid, isRefillStack);
                            }
                            return true;
                        }
                    }
                    AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) screen;
                    if (compressType != null) {
                        Slot mouseSlot = ((AbstractContainerScreenAccessor) containerScreen).getHoveredSlot();
                        if (mouseSlot != null) {
                            if (CraftingTweaks.isServerSideInstalled) {
                                Balm.getNetworking().sendToServer(new CompressMessage(mouseSlot.index, compressType));
                            } else {
                                clientProvider.compress(player, menu, grid, mouseSlot, compressType);
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    public static boolean screenMouseRelease(ScreenMouseEvent event) {
        if (ignoreMouseUp) {
            ignoreMouseUp = false;
            return true;
        } else {
            return false;
        }
    }

    public static boolean screenMouseClick(ScreenMouseEvent event) {
        Screen screen = event.getScreen();
        int button = event.getButton();
        rightClickCraftingSlot = -1;
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        } else {
            AbstractContainerMenu menu = player.containerMenu;
            if (menu == null) {
                return false;
            } else {
                Slot mouseSlot = screen instanceof AbstractContainerScreen ? ((AbstractContainerScreenAccessor) screen).getHoveredSlot() : null;
                CraftingGrid grid = (CraftingGrid) CraftingTweaksProviderManager.getDefaultCraftingGrid(menu).orElse(null);
                if (grid != null) {
                    if (ModKeyMappings.keyTransferStack.isActiveAndDown()) {
                        if (mouseSlot != null && mouseSlot.hasItem()) {
                            List<Slot> transferSlots = Lists.newArrayList();
                            transferSlots.add(mouseSlot);
                            if (Screen.hasShiftDown()) {
                                ItemStack mouseSlotStack = mouseSlot.getItem();
                                for (Slot slot : menu.slots) {
                                    if (slot.hasItem() && mouseSlot != slot) {
                                        ItemStack slotStack = slot.getItem();
                                        if (ItemStack.isSameItemSameTags(slotStack, mouseSlotStack)) {
                                            transferSlots.add(slot);
                                        }
                                    }
                                }
                            }
                            if (CraftingTweaks.isServerSideInstalled) {
                                for (Slot slotx : transferSlots) {
                                    Balm.getNetworking().sendToServer(new TransferStackMessage(grid.getId(), slotx.index));
                                }
                            } else {
                                for (Slot slotx : transferSlots) {
                                    clientProvider.transferIntoGrid(player, menu, grid, slotx);
                                }
                                ignoreMouseUp = true;
                            }
                            return true;
                        }
                    } else if (CraftingTweaksConfig.getActive().client.rightClickCraftsStack && button == 1 && mouseSlot instanceof ResultSlot) {
                        if (CraftingTweaks.isServerSideInstalled) {
                            Balm.getNetworking().sendToServer(new CraftStackMessage(mouseSlot.index));
                        } else {
                            rightClickCraftingSlot = mouseSlot.index;
                        }
                        ignoreMouseUp = true;
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public static void screenInitialized(ScreenInitEvent event) {
        Screen screen = event.getScreen();
        if (screen instanceof AbstractContainerScreen) {
            unpleasantButton = CraftingGuideButtonFixer.fixMistakes((AbstractContainerScreen<?>) screen);
            if (unpleasantButton != null) {
                fixedUnpleasantButtonX = unpleasantButton.m_252754_();
            }
        } else {
            unpleasantButton = null;
        }
        if (screen instanceof AbstractContainerScreen<?> containerScreen) {
            GridGuiHandler guiHandler = CraftingTweaksClientProviderManager.getGridGuiHandler(containerScreen);
            for (CraftingGrid grid : CraftingTweaksProviderManager.getCraftingGrids(((AbstractContainerScreen) screen).getMenu())) {
                String modId = grid.getId().getNamespace();
                CraftingTweaksMode config = CraftingTweaksConfig.getActive().getCraftingTweaksMode(modId);
                if (config == CraftingTweaksMode.DEFAULT || config == CraftingTweaksMode.BUTTONS) {
                    guiHandler.createButtons(containerScreen, grid, widget -> BalmClient.getScreens().addRenderableWidget(screen, widget));
                }
            }
        }
    }

    private static void handleRightClickCrafting() {
        if (rightClickCraftingSlot != -1) {
            int craftingSlot = rightClickCraftingSlot;
            rightClickCraftingSlot = -1;
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                MultiPlayerGameMode playerController = Minecraft.getInstance().gameMode;
                if (playerController != null) {
                    AbstractContainerMenu menu = player.containerMenu;
                    if (menu != null) {
                        if (craftingSlot < menu.slots.size()) {
                            Slot mouseSlot = menu.slots.get(craftingSlot);
                            if (!mouseSlot.hasItem()) {
                                rightClickCraftingSlot = mouseSlot.index;
                            } else {
                                ItemStack mouseStack = menu.getCarried();
                                if (mouseStack.isEmpty() || mouseStack.getCount() + mouseSlot.getItem().getCount() <= mouseStack.getMaxStackSize()) {
                                    playerController.handleInventoryMouseClick(menu.containerId, mouseSlot.index, 0, ClickType.PICKUP, player);
                                    rightClickCraftingSlot = mouseSlot.index;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void screenAboutToDraw(ScreenDrawEvent event) {
        Screen screen = event.getScreen();
        if (screen != null) {
            if (screen instanceof AbstractContainerScreen<?> containerScreen && unpleasantButton != null) {
                int unpleasantX = unpleasantButton.m_252754_();
                if (unpleasantX != fixedUnpleasantButtonX) {
                    unpleasantButton = CraftingGuideButtonFixer.fixMistakes(containerScreen);
                    if (unpleasantButton != null) {
                        fixedUnpleasantButtonX = unpleasantButton.m_252754_();
                    }
                }
            }
        }
    }

    public static void screenDrawn(ScreenDrawEvent event) {
        Screen screen = event.getScreen();
        if (screen != null) {
            handleRightClickCrafting();
        }
    }

    private static void onItemCrafted(ItemCraftedEvent event) {
        clientProvider.onItemCrafted(event.getCraftMatrix());
    }

    public static ClientProvider getClientProvider() {
        return clientProvider;
    }
}