package com.mna.gui.radial;

import com.google.common.collect.Lists;
import com.mna.KeybindInit;
import com.mna.events.ClientEventHandler;
import com.mna.gui.radial.components.IRadialMenuHost;
import com.mna.gui.radial.components.ItemStackRadialMenuItem;
import com.mna.gui.radial.components.RadialMenuItem;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.network.ClientMessageDispatcher;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class RitualKitRadialSelect extends Screen {

    private ItemStack stackEquipped;

    private boolean needsRecheckStacks = true;

    private final List<RadialMenuItem> cachedMenuItems = Lists.newArrayList();

    private final GenericRadialMenu menu;

    private boolean forceclose = false;

    private Minecraft mc = Minecraft.getInstance();

    private final boolean offhand;

    public RitualKitRadialSelect(boolean offhand) {
        super(Component.literal("RADIAL MENU"));
        this.offhand = offhand;
        this.stackEquipped = this.getHandItem();
        if (!(this.stackEquipped.getItem() instanceof ItemPractitionersPouch)) {
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
                return RitualKitRadialSelect.this;
            }

            @Override
            public Font getFontRenderer() {
                return RitualKitRadialSelect.this.f_96547_;
            }
        }) {

            @Override
            public void onClickOutside() {
                this.close();
            }
        };
    }

    private ItemStack getHandItem() {
        return this.offhand ? this.mc.player.m_21206_() : this.mc.player.m_21205_();
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof RitualKitRadialSelect) {
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
        if (this.menu.isReady()) {
            ItemStack inHand = this.getHandItem();
            if (!(inHand.getItem() instanceof ItemPractitionersPouch)) {
                this.forceclose = true;
            } else if (inHand.getCount() <= 0) {
                this.stackEquipped = null;
            } else if (this.stackEquipped != inHand) {
                this.menu.close();
            }
            if (this.forceclose) {
                Minecraft.getInstance().setScreen(null);
            } else if (!ClientEventHandler.isKeyDown((KeyMapping) KeybindInit.RadialMenuOpen.get())) {
                this.processClick(false);
                this.forceclose = true;
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
        if (this.needsRecheckStacks) {
            this.cachedMenuItems.clear();
            for (final int i = 0; i < 8; i++) {
                String translationKey = ItemPractitionersPouch.getRitualRLoc(this.stackEquipped, i);
                MutableComponent menuDisplayItem = null;
                ItemStack stack = ItemStack.EMPTY;
                if (translationKey == "") {
                    menuDisplayItem = Component.translatable("item.mna.ritual_kit.no_ritual_stored");
                    stack = new ItemStack(this.stackEquipped.getItem());
                    stack.getOrCreateTag().putBoolean("hideTier", true);
                } else {
                    stack = new ItemStack(ItemInit.RUNE_PATTERN_RITUAL_METAL.get());
                    stack.setHoverName(Component.translatable(translationKey));
                    stack.getOrCreateTag().putBoolean("hideTier", true);
                }
                ItemStackRadialMenuItem item = new ItemStackRadialMenuItem(this.menu, stack, menuDisplayItem) {

                    @Override
                    public boolean onClick() {
                        ClientMessageDispatcher.sendRitualKitIndexChange(i, RitualKitRadialSelect.this.offhand);
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
            this.menu.setCentralText(Component.translatable("item.mna.ritual_kit"));
        } else {
            this.menu.setCentralText(null);
        }
        this.menu.draw(pGuiGraphics, partialTicks, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}