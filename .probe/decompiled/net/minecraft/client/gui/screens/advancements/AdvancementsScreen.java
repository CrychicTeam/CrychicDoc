package net.minecraft.client.gui.screens.advancements;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;

public class AdvancementsScreen extends Screen implements ClientAdvancements.Listener {

    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");

    public static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");

    public static final int WINDOW_WIDTH = 252;

    public static final int WINDOW_HEIGHT = 140;

    private static final int WINDOW_INSIDE_X = 9;

    private static final int WINDOW_INSIDE_Y = 18;

    public static final int WINDOW_INSIDE_WIDTH = 234;

    public static final int WINDOW_INSIDE_HEIGHT = 113;

    private static final int WINDOW_TITLE_X = 8;

    private static final int WINDOW_TITLE_Y = 6;

    public static final int BACKGROUND_TILE_WIDTH = 16;

    public static final int BACKGROUND_TILE_HEIGHT = 16;

    public static final int BACKGROUND_TILE_COUNT_X = 14;

    public static final int BACKGROUND_TILE_COUNT_Y = 7;

    private static final Component VERY_SAD_LABEL = Component.translatable("advancements.sad_label");

    private static final Component NO_ADVANCEMENTS_LABEL = Component.translatable("advancements.empty");

    private static final Component TITLE = Component.translatable("gui.advancements");

    private final ClientAdvancements advancements;

    private final Map<Advancement, AdvancementTab> tabs = Maps.newLinkedHashMap();

    @Nullable
    private AdvancementTab selectedTab;

    private boolean isScrolling;

    public AdvancementsScreen(ClientAdvancements clientAdvancements0) {
        super(GameNarrator.NO_TITLE);
        this.advancements = clientAdvancements0;
    }

    @Override
    protected void init() {
        this.tabs.clear();
        this.selectedTab = null;
        this.advancements.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.advancements.setSelectedTab(((AdvancementTab) this.tabs.values().iterator().next()).getAdvancement(), true);
        } else {
            this.advancements.setSelectedTab(this.selectedTab == null ? null : this.selectedTab.getAdvancement(), true);
        }
    }

    @Override
    public void removed() {
        this.advancements.setListener(null);
        ClientPacketListener $$0 = this.f_96541_.getConnection();
        if ($$0 != null) {
            $$0.send(ServerboundSeenAdvancementsPacket.closedScreen());
        }
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (int2 == 0) {
            int $$3 = (this.f_96543_ - 252) / 2;
            int $$4 = (this.f_96544_ - 140) / 2;
            for (AdvancementTab $$5 : this.tabs.values()) {
                if ($$5.isMouseOver($$3, $$4, double0, double1)) {
                    this.advancements.setSelectedTab($$5.getAdvancement(), true);
                    break;
                }
            }
        }
        return super.m_6375_(double0, double1, int2);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (this.f_96541_.options.keyAdvancements.matches(int0, int1)) {
            this.f_96541_.setScreen(null);
            this.f_96541_.mouseHandler.grabMouse();
            return true;
        } else {
            return super.keyPressed(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = (this.f_96543_ - 252) / 2;
        int $$5 = (this.f_96544_ - 140) / 2;
        this.m_280273_(guiGraphics0);
        this.renderInside(guiGraphics0, int1, int2, $$4, $$5);
        this.renderWindow(guiGraphics0, $$4, $$5);
        this.renderTooltips(guiGraphics0, int1, int2, $$4, $$5);
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        if (int2 != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(double3, double4);
            }
            return true;
        }
    }

    private void renderInside(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        AdvancementTab $$5 = this.selectedTab;
        if ($$5 == null) {
            guiGraphics0.fill(int3 + 9, int4 + 18, int3 + 9 + 234, int4 + 18 + 113, -16777216);
            int $$6 = int3 + 9 + 117;
            guiGraphics0.drawCenteredString(this.f_96547_, NO_ADVANCEMENTS_LABEL, $$6, int4 + 18 + 56 - 9 / 2, -1);
            guiGraphics0.drawCenteredString(this.f_96547_, VERY_SAD_LABEL, $$6, int4 + 18 + 113 - 9, -1);
        } else {
            $$5.drawContents(guiGraphics0, int3 + 9, int4 + 18);
        }
    }

    public void renderWindow(GuiGraphics guiGraphics0, int int1, int int2) {
        RenderSystem.enableBlend();
        guiGraphics0.blit(WINDOW_LOCATION, int1, int2, 0, 0, 252, 140);
        if (this.tabs.size() > 1) {
            for (AdvancementTab $$3 : this.tabs.values()) {
                $$3.drawTab(guiGraphics0, int1, int2, $$3 == this.selectedTab);
            }
            for (AdvancementTab $$4 : this.tabs.values()) {
                $$4.drawIcon(guiGraphics0, int1, int2);
            }
        }
        guiGraphics0.drawString(this.f_96547_, TITLE, int1 + 8, int2 + 6, 4210752, false);
    }

    private void renderTooltips(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        if (this.selectedTab != null) {
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate((float) (int3 + 9), (float) (int4 + 18), 400.0F);
            RenderSystem.enableDepthTest();
            this.selectedTab.drawTooltips(guiGraphics0, int1 - int3 - 9, int2 - int4 - 18, int3, int4);
            RenderSystem.disableDepthTest();
            guiGraphics0.pose().popPose();
        }
        if (this.tabs.size() > 1) {
            for (AdvancementTab $$5 : this.tabs.values()) {
                if ($$5.isMouseOver(int3, int4, (double) int1, (double) int2)) {
                    guiGraphics0.renderTooltip(this.f_96547_, $$5.getTitle(), int1, int2);
                }
            }
        }
    }

    @Override
    public void onAddAdvancementRoot(Advancement advancement0) {
        AdvancementTab $$1 = AdvancementTab.create(this.f_96541_, this, this.tabs.size(), advancement0);
        if ($$1 != null) {
            this.tabs.put(advancement0, $$1);
        }
    }

    @Override
    public void onRemoveAdvancementRoot(Advancement advancement0) {
    }

    @Override
    public void onAddAdvancementTask(Advancement advancement0) {
        AdvancementTab $$1 = this.getTab(advancement0);
        if ($$1 != null) {
            $$1.addAdvancement(advancement0);
        }
    }

    @Override
    public void onRemoveAdvancementTask(Advancement advancement0) {
    }

    @Override
    public void onUpdateAdvancementProgress(Advancement advancement0, AdvancementProgress advancementProgress1) {
        AdvancementWidget $$2 = this.getAdvancementWidget(advancement0);
        if ($$2 != null) {
            $$2.setProgress(advancementProgress1);
        }
    }

    @Override
    public void onSelectedTabChanged(@Nullable Advancement advancement0) {
        this.selectedTab = (AdvancementTab) this.tabs.get(advancement0);
    }

    @Override
    public void onAdvancementsCleared() {
        this.tabs.clear();
        this.selectedTab = null;
    }

    @Nullable
    public AdvancementWidget getAdvancementWidget(Advancement advancement0) {
        AdvancementTab $$1 = this.getTab(advancement0);
        return $$1 == null ? null : $$1.getWidget(advancement0);
    }

    @Nullable
    private AdvancementTab getTab(Advancement advancement0) {
        while (advancement0.getParent() != null) {
            advancement0 = advancement0.getParent();
        }
        return (AdvancementTab) this.tabs.get(advancement0);
    }
}