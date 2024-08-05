package net.minecraft.client.gui.screens.social;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.reporting.ChatReportScreen;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class PlayerEntry extends ContainerObjectSelectionList.Entry<PlayerEntry> {

    private static final ResourceLocation REPORT_BUTTON_LOCATION = new ResourceLocation("textures/gui/report_button.png");

    private static final int TOOLTIP_DELAY = 10;

    private final Minecraft minecraft;

    private final List<AbstractWidget> children;

    private final UUID id;

    private final String playerName;

    private final Supplier<ResourceLocation> skinGetter;

    private boolean isRemoved;

    private boolean hasRecentMessages;

    private final boolean reportingEnabled;

    private final boolean playerReportable;

    private final boolean hasDraftReport;

    @Nullable
    private Button hideButton;

    @Nullable
    private Button showButton;

    @Nullable
    private Button reportButton;

    private float tooltipHoverTime;

    private static final Component HIDDEN = Component.translatable("gui.socialInteractions.status_hidden").withStyle(ChatFormatting.ITALIC);

    private static final Component BLOCKED = Component.translatable("gui.socialInteractions.status_blocked").withStyle(ChatFormatting.ITALIC);

    private static final Component OFFLINE = Component.translatable("gui.socialInteractions.status_offline").withStyle(ChatFormatting.ITALIC);

    private static final Component HIDDEN_OFFLINE = Component.translatable("gui.socialInteractions.status_hidden_offline").withStyle(ChatFormatting.ITALIC);

    private static final Component BLOCKED_OFFLINE = Component.translatable("gui.socialInteractions.status_blocked_offline").withStyle(ChatFormatting.ITALIC);

    private static final Component REPORT_DISABLED_TOOLTIP = Component.translatable("gui.socialInteractions.tooltip.report.disabled");

    private static final Component NOT_REPORTABLE_TOOLTIP = Component.translatable("gui.socialInteractions.tooltip.report.not_reportable");

    private static final Component HIDE_TEXT_TOOLTIP = Component.translatable("gui.socialInteractions.tooltip.hide");

    private static final Component SHOW_TEXT_TOOLTIP = Component.translatable("gui.socialInteractions.tooltip.show");

    private static final Component REPORT_PLAYER_TOOLTIP = Component.translatable("gui.socialInteractions.tooltip.report");

    private static final int SKIN_SIZE = 24;

    private static final int PADDING = 4;

    private static final int CHAT_TOGGLE_ICON_SIZE = 20;

    private static final int CHAT_TOGGLE_ICON_X = 0;

    private static final int CHAT_TOGGLE_ICON_Y = 38;

    public static final int SKIN_SHADE = FastColor.ARGB32.color(190, 0, 0, 0);

    public static final int BG_FILL = FastColor.ARGB32.color(255, 74, 74, 74);

    public static final int BG_FILL_REMOVED = FastColor.ARGB32.color(255, 48, 48, 48);

    public static final int PLAYERNAME_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);

    public static final int PLAYER_STATUS_COLOR = FastColor.ARGB32.color(140, 255, 255, 255);

    public PlayerEntry(Minecraft minecraft0, SocialInteractionsScreen socialInteractionsScreen1, UUID uUID2, String string3, Supplier<ResourceLocation> supplierResourceLocation4, boolean boolean5) {
        this.minecraft = minecraft0;
        this.id = uUID2;
        this.playerName = string3;
        this.skinGetter = supplierResourceLocation4;
        ReportingContext $$6 = minecraft0.getReportingContext();
        this.reportingEnabled = $$6.sender().isEnabled();
        this.playerReportable = boolean5;
        this.hasDraftReport = $$6.hasDraftReportFor(uUID2);
        Component $$7 = Component.translatable("gui.socialInteractions.narration.hide", string3);
        Component $$8 = Component.translatable("gui.socialInteractions.narration.show", string3);
        PlayerSocialManager $$9 = minecraft0.getPlayerSocialManager();
        boolean $$10 = minecraft0.getChatStatus().isChatAllowed(minecraft0.isLocalServer());
        boolean $$11 = !minecraft0.player.m_20148_().equals(uUID2);
        if ($$11 && $$10 && !$$9.isBlocked(uUID2)) {
            this.reportButton = new ImageButton(0, 0, 20, 20, 0, 0, 20, REPORT_BUTTON_LOCATION, 64, 64, p_238875_ -> $$6.draftReportHandled(minecraft0, socialInteractionsScreen1, () -> minecraft0.setScreen(new ChatReportScreen(socialInteractionsScreen1, $$6, uUID2)), false), Component.translatable("gui.socialInteractions.report")) {

                @Override
                protected MutableComponent createNarrationMessage() {
                    return PlayerEntry.this.getEntryNarationMessage(super.m_5646_());
                }
            };
            this.reportButton.m_257544_(this.createReportButtonTooltip());
            this.reportButton.m_257427_(10);
            this.hideButton = new ImageButton(0, 0, 20, 20, 0, 38, 20, SocialInteractionsScreen.SOCIAL_INTERACTIONS_LOCATION, 256, 256, p_100612_ -> {
                $$9.hidePlayer(uUID2);
                this.onHiddenOrShown(true, Component.translatable("gui.socialInteractions.hidden_in_chat", string3));
            }, Component.translatable("gui.socialInteractions.hide")) {

                @Override
                protected MutableComponent createNarrationMessage() {
                    return PlayerEntry.this.getEntryNarationMessage(super.m_5646_());
                }
            };
            this.hideButton.m_257544_(Tooltip.create(HIDE_TEXT_TOOLTIP, $$7));
            this.hideButton.m_257427_(10);
            this.showButton = new ImageButton(0, 0, 20, 20, 20, 38, 20, SocialInteractionsScreen.SOCIAL_INTERACTIONS_LOCATION, 256, 256, p_170074_ -> {
                $$9.showPlayer(uUID2);
                this.onHiddenOrShown(false, Component.translatable("gui.socialInteractions.shown_in_chat", string3));
            }, Component.translatable("gui.socialInteractions.show")) {

                @Override
                protected MutableComponent createNarrationMessage() {
                    return PlayerEntry.this.getEntryNarationMessage(super.m_5646_());
                }
            };
            this.showButton.m_257544_(Tooltip.create(SHOW_TEXT_TOOLTIP, $$8));
            this.showButton.m_257427_(10);
            this.reportButton.f_93623_ = false;
            this.children = new ArrayList();
            this.children.add(this.hideButton);
            this.children.add(this.reportButton);
            this.updateHideAndShowButton($$9.isHidden(this.id));
        } else {
            this.children = ImmutableList.of();
        }
    }

    private Tooltip createReportButtonTooltip() {
        if (!this.playerReportable) {
            return Tooltip.create(NOT_REPORTABLE_TOOLTIP);
        } else if (!this.reportingEnabled) {
            return Tooltip.create(REPORT_DISABLED_TOOLTIP);
        } else {
            return !this.hasRecentMessages ? Tooltip.create(Component.translatable("gui.socialInteractions.tooltip.report.no_messages", this.playerName)) : Tooltip.create(REPORT_PLAYER_TOOLTIP, Component.translatable("gui.socialInteractions.narration.report", this.playerName));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
        int $$10 = int3 + 4;
        int $$11 = int2 + (int5 - 24) / 2;
        int $$12 = $$10 + 24 + 4;
        Component $$13 = this.getStatusComponent();
        int $$14;
        if ($$13 == CommonComponents.EMPTY) {
            guiGraphics0.fill(int3, int2, int3 + int4, int2 + int5, BG_FILL);
            $$14 = int2 + (int5 - 9) / 2;
        } else {
            guiGraphics0.fill(int3, int2, int3 + int4, int2 + int5, BG_FILL_REMOVED);
            $$14 = int2 + (int5 - (9 + 9)) / 2;
            guiGraphics0.drawString(this.minecraft.font, $$13, $$12, $$14 + 12, PLAYER_STATUS_COLOR, false);
        }
        PlayerFaceRenderer.draw(guiGraphics0, (ResourceLocation) this.skinGetter.get(), $$10, $$11, 24);
        guiGraphics0.drawString(this.minecraft.font, this.playerName, $$12, $$14, PLAYERNAME_COLOR, false);
        if (this.isRemoved) {
            guiGraphics0.fill($$10, $$11, $$10 + 24, $$11 + 24, SKIN_SHADE);
        }
        if (this.hideButton != null && this.showButton != null && this.reportButton != null) {
            float $$16 = this.tooltipHoverTime;
            this.hideButton.m_252865_(int3 + (int4 - this.hideButton.m_5711_() - 4) - 20 - 4);
            this.hideButton.m_253211_(int2 + (int5 - this.hideButton.m_93694_()) / 2);
            this.hideButton.m_88315_(guiGraphics0, int6, int7, float9);
            this.showButton.m_252865_(int3 + (int4 - this.showButton.m_5711_() - 4) - 20 - 4);
            this.showButton.m_253211_(int2 + (int5 - this.showButton.m_93694_()) / 2);
            this.showButton.m_88315_(guiGraphics0, int6, int7, float9);
            this.reportButton.m_252865_(int3 + (int4 - this.showButton.m_5711_() - 4));
            this.reportButton.m_253211_(int2 + (int5 - this.showButton.m_93694_()) / 2);
            this.reportButton.m_88315_(guiGraphics0, int6, int7, float9);
            if ($$16 == this.tooltipHoverTime) {
                this.tooltipHoverTime = 0.0F;
            }
        }
        if (this.hasDraftReport && this.reportButton != null) {
            guiGraphics0.blit(AbstractWidget.WIDGETS_LOCATION, this.reportButton.m_252754_() + 5, this.reportButton.m_252907_() + 1, 182.0F, 24.0F, 15, 15, 256, 256);
        }
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return this.children;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public UUID getPlayerId() {
        return this.id;
    }

    public void setRemoved(boolean boolean0) {
        this.isRemoved = boolean0;
    }

    public boolean isRemoved() {
        return this.isRemoved;
    }

    public void setHasRecentMessages(boolean boolean0) {
        this.hasRecentMessages = boolean0;
        if (this.reportButton != null) {
            this.reportButton.f_93623_ = this.reportingEnabled && this.playerReportable && boolean0;
            this.reportButton.m_257544_(this.createReportButtonTooltip());
        }
    }

    public boolean hasRecentMessages() {
        return this.hasRecentMessages;
    }

    private void onHiddenOrShown(boolean boolean0, Component component1) {
        this.updateHideAndShowButton(boolean0);
        this.minecraft.gui.getChat().addMessage(component1);
        this.minecraft.getNarrator().sayNow(component1);
    }

    private void updateHideAndShowButton(boolean boolean0) {
        this.showButton.f_93624_ = boolean0;
        this.hideButton.f_93624_ = !boolean0;
        this.children.set(0, boolean0 ? this.showButton : this.hideButton);
    }

    MutableComponent getEntryNarationMessage(MutableComponent mutableComponent0) {
        Component $$1 = this.getStatusComponent();
        return $$1 == CommonComponents.EMPTY ? Component.literal(this.playerName).append(", ").append(mutableComponent0) : Component.literal(this.playerName).append(", ").append($$1).append(", ").append(mutableComponent0);
    }

    private Component getStatusComponent() {
        boolean $$0 = this.minecraft.getPlayerSocialManager().isHidden(this.id);
        boolean $$1 = this.minecraft.getPlayerSocialManager().isBlocked(this.id);
        if ($$1 && this.isRemoved) {
            return BLOCKED_OFFLINE;
        } else if ($$0 && this.isRemoved) {
            return HIDDEN_OFFLINE;
        } else if ($$1) {
            return BLOCKED;
        } else if ($$0) {
            return HIDDEN;
        } else {
            return this.isRemoved ? OFFLINE : CommonComponents.EMPTY;
        }
    }
}