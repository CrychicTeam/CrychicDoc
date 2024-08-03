package com.mna.gui.radial;

import com.google.common.collect.Lists;
import com.mna.KeybindInit;
import com.mna.events.ClientEventHandler;
import com.mna.gui.radial.components.IRadialMenuHost;
import com.mna.gui.radial.components.ItemStackRadialMenuItem;
import com.mna.gui.radial.components.RadialMenuItem;
import com.mna.items.base.IRadialInventorySelect;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandlerModifiable;

@EventBusSubscriber({ Dist.CLIENT })
public class ItemstackRadialSelect extends Screen {

    private ItemStack stackEquipped;

    private IItemHandlerModifiable inventory;

    private boolean needsRecheckStacks = true;

    private final List<ItemStackRadialMenuItem> cachedMenuItems = Lists.newArrayList();

    private final GenericRadialMenu menu;

    private Minecraft mc;

    private final boolean offhand;

    private int overrideStackCount = -1;

    public ItemstackRadialSelect(boolean offhand) {
        super(Component.literal("RADIAL MENU"));
        this.mc = Minecraft.getInstance();
        this.offhand = offhand;
        this.stackEquipped = this.getHandItem();
        if (this.stackEquipped.getItem() instanceof IRadialInventorySelect) {
            this.inventory = ((IRadialInventorySelect) this.stackEquipped.getItem()).getInventory(this.stackEquipped, this.mc.player);
        } else {
            this.m_7379_();
        }
        this.menu = new GenericRadialMenu(Minecraft.getInstance(), new IRadialMenuHost() {

            @Override
            public void renderTooltip(GuiGraphics pGuiGraphics, ItemStack stack, int mouseX, int mouseY) {
            }

            @Override
            public void renderTooltip(GuiGraphics pGuiGraphics, Component text, int mouseX, int mouseY) {
            }

            @Override
            public Screen getScreen() {
                return ItemstackRadialSelect.this;
            }

            @Override
            public Font getFontRenderer() {
                return ItemstackRadialSelect.this.f_96547_;
            }
        }) {

            @Override
            public void onClickOutside() {
                this.close();
            }
        };
        this.menu.radiusOffset = 35.0F;
    }

    public ItemstackRadialSelect setOverrideStackCount(int count) {
        this.overrideStackCount = count;
        return this;
    }

    private ItemStack getHandItem() {
        return this.offhand ? this.mc.player.m_21206_() : this.mc.player.m_21205_();
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof ItemstackRadialSelect) {
            event.setCanceled(true);
        }
    }

    @Override
    public void removed() {
        super.removed();
        ClientEventHandler.wipeOpen();
    }

    @Override
    public void tick() {
        super.tick();
        this.menu.tick();
        if (this.menu.isClosed()) {
            Minecraft.getInstance().setScreen(null);
            ClientEventHandler.wipeOpen();
        }
        if (this.menu.isReady() && this.inventory != null) {
            ItemStack inHand = this.getHandItem();
            if (!(inHand.getItem() instanceof IRadialInventorySelect)) {
                this.inventory = null;
            } else if (inHand.getCount() <= 0) {
                this.inventory = null;
                this.stackEquipped = null;
            } else if (this.stackEquipped != inHand) {
                this.menu.close();
            }
            if (this.inventory == null) {
                Minecraft.getInstance().setScreen(null);
            } else if (!ClientEventHandler.isKeyDown((KeyMapping) KeybindInit.RadialMenuOpen.get())) {
                this.processClick(false);
            }
        }
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        this.processClick(true);
        return super.m_6348_(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    protected void processClick(boolean triggeredByMouse) {
        this.menu.clickItem();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.inventory != null) {
            final ItemStack inHand = this.getHandItem();
            if (inHand.getItem() instanceof IRadialInventorySelect) {
                if (this.needsRecheckStacks) {
                    this.cachedMenuItems.clear();
                    int slotCount = this.overrideStackCount > 0 ? Math.min(this.inventory.getSlots(), this.overrideStackCount) : this.inventory.getSlots();
                    for (int i = 0; i < slotCount; i++) {
                        ItemStack inSlot = this.inventory.getStackInSlot(i);
                        final int index = i;
                        ItemStackRadialMenuItem item = new ItemStackRadialMenuItem(this.menu, inSlot, Component.translatable("gui.mna.spellbook.empty")) {

                            @Override
                            public boolean onClick() {
                                ((IRadialInventorySelect) inHand.getItem()).setSlot(ItemstackRadialSelect.this.mc.player, ItemstackRadialSelect.this.offhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, index, ItemstackRadialSelect.this.offhand, true);
                                ItemstackRadialSelect.this.menu.close();
                                return true;
                            }
                        };
                        item.setVisible(true);
                        this.cachedMenuItems.add(item);
                    }
                    this.menu.clear();
                    this.menu.addAll(this.cachedMenuItems);
                    this.needsRecheckStacks = false;
                }
                if (this.cachedMenuItems.stream().noneMatch(RadialMenuItem::isVisible)) {
                    this.menu.setCentralText(Component.translatable("gui.mna.spellbook.empty"));
                } else {
                    this.menu.setCentralText(null);
                }
                this.menu.draw(pGuiGraphics, partialTicks, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}