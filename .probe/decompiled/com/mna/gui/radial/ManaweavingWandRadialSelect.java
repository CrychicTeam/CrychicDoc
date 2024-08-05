package com.mna.gui.radial;

import com.google.common.collect.Lists;
import com.mna.KeybindInit;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.events.ClientEventHandler;
import com.mna.gui.radial.components.IRadialMenuHost;
import com.mna.gui.radial.components.ItemStackRadialMenuItem;
import com.mna.gui.radial.components.ManaweaveRadialMenuItem;
import com.mna.gui.radial.components.RadialMenuItem;
import com.mna.items.manaweaving.ItemManaweaverWand;
import com.mna.network.ClientMessageDispatcher;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class ManaweavingWandRadialSelect extends Screen {

    private ItemStack stackEquipped;

    private boolean needsRecheckStacks = true;

    private final List<RadialMenuItem> cachedMenuItems = Lists.newArrayList();

    private final GenericRadialMenu menu;

    private Minecraft mc;

    private final boolean offhand;

    private final List<ManaweavingPattern> playerKnownPatterns = Lists.newArrayList();

    public ManaweavingWandRadialSelect(boolean offhand) {
        super(Component.literal("RADIAL MENU"));
        this.offhand = offhand;
        this.mc = Minecraft.getInstance();
        this.stackEquipped = this.getHandItem();
        if (!(this.stackEquipped.getItem() instanceof ItemManaweaverWand)) {
            this.m_7379_();
        }
        this.mc.player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> ManaweavingPatternHelper.getAllPatterns(this.mc.level).forEach(mwp -> {
            if (mwp.getTier() <= p.getTier()) {
                this.playerKnownPatterns.add(mwp);
            }
        }));
        if (this.playerKnownPatterns.size() == 0) {
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
                return ManaweavingWandRadialSelect.this;
            }

            @Override
            public Font getFontRenderer() {
                return ManaweavingWandRadialSelect.this.f_96547_;
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
        if (mc.screen instanceof ManaweavingWandRadialSelect) {
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
            if (!(inHand.getItem() instanceof ItemManaweaverWand)) {
                Minecraft.getInstance().setScreen(null);
            }
            if (!ClientEventHandler.isKeyDown((KeyMapping) KeybindInit.RadialMenuOpen.get())) {
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
        ItemStack inHand = this.getHandItem();
        if (inHand.getItem() instanceof ItemManaweaverWand) {
            if (this.needsRecheckStacks) {
                this.cachedMenuItems.clear();
                ItemStackRadialMenuItem defaultItem = new ItemStackRadialMenuItem(this.menu, inHand, Component.literal("wand")) {

                    @Override
                    public boolean onClick() {
                        ClientMessageDispatcher.sendManaweaveWandSlotChange(null, ManaweavingWandRadialSelect.this.offhand);
                        ManaweavingWandRadialSelect.this.menu.close();
                        return true;
                    }
                };
                defaultItem.setVisible(true);
                this.cachedMenuItems.add(defaultItem);
                for (int i = 0; i < this.playerKnownPatterns.size(); i++) {
                    final IManaweavePattern pattern = (IManaweavePattern) this.playerKnownPatterns.get(i);
                    ManaweaveRadialMenuItem item = new ManaweaveRadialMenuItem(this.menu, i, 0.07F, pattern) {

                        @Override
                        public boolean onClick() {
                            ClientMessageDispatcher.sendManaweaveWandSlotChange(pattern.getRegistryId(), ManaweavingWandRadialSelect.this.offhand);
                            ManaweavingWandRadialSelect.this.menu.close();
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}