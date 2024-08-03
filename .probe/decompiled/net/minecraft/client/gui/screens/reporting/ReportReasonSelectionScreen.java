package net.minecraft.client.gui.screens.reporting;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.Optionull;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.chat.report.ReportReason;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ReportReasonSelectionScreen extends Screen {

    private static final Component REASON_TITLE = Component.translatable("gui.abuseReport.reason.title");

    private static final Component REASON_DESCRIPTION = Component.translatable("gui.abuseReport.reason.description");

    private static final Component READ_INFO_LABEL = Component.translatable("gui.chatReport.read_info");

    private static final int FOOTER_HEIGHT = 95;

    private static final int BUTTON_WIDTH = 150;

    private static final int BUTTON_HEIGHT = 20;

    private static final int CONTENT_WIDTH = 320;

    private static final int PADDING = 4;

    @Nullable
    private final Screen lastScreen;

    @Nullable
    private ReportReasonSelectionScreen.ReasonSelectionList reasonSelectionList;

    @Nullable
    ReportReason currentlySelectedReason;

    private final Consumer<ReportReason> onSelectedReason;

    public ReportReasonSelectionScreen(@Nullable Screen screen0, @Nullable ReportReason reportReason1, Consumer<ReportReason> consumerReportReason2) {
        super(REASON_TITLE);
        this.lastScreen = screen0;
        this.currentlySelectedReason = reportReason1;
        this.onSelectedReason = consumerReportReason2;
    }

    @Override
    protected void init() {
        this.reasonSelectionList = new ReportReasonSelectionScreen.ReasonSelectionList(this.f_96541_);
        this.reasonSelectionList.m_93488_(false);
        this.m_7787_(this.reasonSelectionList);
        ReportReasonSelectionScreen.ReasonSelectionList.Entry $$0 = Optionull.map(this.currentlySelectedReason, this.reasonSelectionList::m_239167_);
        this.reasonSelectionList.setSelected($$0);
        int $$1 = this.f_96543_ / 2 - 150 - 5;
        this.m_142416_(Button.builder(READ_INFO_LABEL, p_280887_ -> this.f_96541_.setScreen(new ConfirmLinkScreen(p_280888_ -> {
            if (p_280888_) {
                Util.getPlatform().openUri("https://aka.ms/aboutjavareporting");
            }
            this.f_96541_.setScreen(this);
        }, "https://aka.ms/aboutjavareporting", true))).bounds($$1, this.buttonTop(), 150, 20).build());
        int $$2 = this.f_96543_ / 2 + 5;
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280889_ -> {
            ReportReasonSelectionScreen.ReasonSelectionList.Entry $$1x = (ReportReasonSelectionScreen.ReasonSelectionList.Entry) this.reasonSelectionList.m_93511_();
            if ($$1x != null) {
                this.onSelectedReason.accept($$1x.getReason());
            }
            this.f_96541_.setScreen(this.lastScreen);
        }).bounds($$2, this.buttonTop(), 150, 20).build());
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.reasonSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 16, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
        guiGraphics0.fill(this.contentLeft(), this.descriptionTop(), this.contentRight(), this.descriptionBottom(), 2130706432);
        guiGraphics0.drawString(this.f_96547_, REASON_DESCRIPTION, this.contentLeft() + 4, this.descriptionTop() + 4, -8421505);
        ReportReasonSelectionScreen.ReasonSelectionList.Entry $$4 = (ReportReasonSelectionScreen.ReasonSelectionList.Entry) this.reasonSelectionList.m_93511_();
        if ($$4 != null) {
            int $$5 = this.contentLeft() + 4 + 16;
            int $$6 = this.contentRight() - 4;
            int $$7 = this.descriptionTop() + 4 + 9 + 2;
            int $$8 = this.descriptionBottom() - 4;
            int $$9 = $$6 - $$5;
            int $$10 = $$8 - $$7;
            int $$11 = this.f_96547_.wordWrapHeight($$4.reason.description(), $$9);
            guiGraphics0.drawWordWrap(this.f_96547_, $$4.reason.description(), $$5, $$7 + ($$10 - $$11) / 2, $$9, -1);
        }
    }

    private int buttonTop() {
        return this.f_96544_ - 20 - 4;
    }

    private int contentLeft() {
        return (this.f_96543_ - 320) / 2;
    }

    private int contentRight() {
        return (this.f_96543_ + 320) / 2;
    }

    private int descriptionTop() {
        return this.f_96544_ - 95 + 4;
    }

    private int descriptionBottom() {
        return this.buttonTop() - 4;
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    public class ReasonSelectionList extends ObjectSelectionList<ReportReasonSelectionScreen.ReasonSelectionList.Entry> {

        public ReasonSelectionList(Minecraft minecraft0) {
            super(minecraft0, ReportReasonSelectionScreen.this.f_96543_, ReportReasonSelectionScreen.this.f_96544_, 40, ReportReasonSelectionScreen.this.f_96544_ - 95, 18);
            for (ReportReason $$2 : ReportReason.values()) {
                this.m_7085_(new ReportReasonSelectionScreen.ReasonSelectionList.Entry($$2));
            }
        }

        @Nullable
        public ReportReasonSelectionScreen.ReasonSelectionList.Entry findEntry(ReportReason reportReason0) {
            return (ReportReasonSelectionScreen.ReasonSelectionList.Entry) this.m_6702_().stream().filter(p_239293_ -> p_239293_.reason == reportReason0).findFirst().orElse(null);
        }

        @Override
        public int getRowWidth() {
            return 320;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.m_93520_() - 2;
        }

        public void setSelected(@Nullable ReportReasonSelectionScreen.ReasonSelectionList.Entry reportReasonSelectionScreenReasonSelectionListEntry0) {
            super.m_6987_(reportReasonSelectionScreenReasonSelectionListEntry0);
            ReportReasonSelectionScreen.this.currentlySelectedReason = reportReasonSelectionScreenReasonSelectionListEntry0 != null ? reportReasonSelectionScreenReasonSelectionListEntry0.getReason() : null;
        }

        public class Entry extends ObjectSelectionList.Entry<ReportReasonSelectionScreen.ReasonSelectionList.Entry> {

            final ReportReason reason;

            public Entry(ReportReason reportReason0) {
                this.reason = reportReason0;
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                int $$10 = int3 + 1;
                int $$11 = int2 + (int5 - 9) / 2 + 1;
                guiGraphics0.drawString(ReportReasonSelectionScreen.this.f_96547_, this.reason.title(), $$10, $$11, -1);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("gui.abuseReport.reason.narration", this.reason.title(), this.reason.description());
            }

            @Override
            public boolean mouseClicked(double double0, double double1, int int2) {
                if (int2 == 0) {
                    ReasonSelectionList.this.setSelected(this);
                    return true;
                } else {
                    return false;
                }
            }

            public ReportReason getReason() {
                return this.reason;
            }
        }
    }
}