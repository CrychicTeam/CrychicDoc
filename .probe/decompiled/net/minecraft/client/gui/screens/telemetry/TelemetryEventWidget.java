package net.minecraft.client.gui.screens.telemetry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleConsumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TelemetryEventWidget extends AbstractScrollWidget {

    private static final int HEADER_HORIZONTAL_PADDING = 32;

    private static final String TELEMETRY_REQUIRED_TRANSLATION_KEY = "telemetry.event.required";

    private static final String TELEMETRY_OPTIONAL_TRANSLATION_KEY = "telemetry.event.optional";

    private static final Component PROPERTY_TITLE = Component.translatable("telemetry_info.property_title").withStyle(ChatFormatting.UNDERLINE);

    private final Font font;

    private TelemetryEventWidget.Content content;

    @Nullable
    private DoubleConsumer onScrolledListener;

    public TelemetryEventWidget(int int0, int int1, int int2, int int3, Font font4) {
        super(int0, int1, int2, int3, Component.empty());
        this.font = font4;
        this.content = this.buildContent(Minecraft.getInstance().telemetryOptInExtra());
    }

    public void onOptInChanged(boolean boolean0) {
        this.content = this.buildContent(boolean0);
        this.setScrollAmount(this.m_239030_());
    }

    private TelemetryEventWidget.Content buildContent(boolean boolean0) {
        TelemetryEventWidget.ContentBuilder $$1 = new TelemetryEventWidget.ContentBuilder(this.containerWidth());
        List<TelemetryEventType> $$2 = new ArrayList(TelemetryEventType.values());
        $$2.sort(Comparator.comparing(TelemetryEventType::m_260839_));
        if (!boolean0) {
            $$2.removeIf(TelemetryEventType::m_260839_);
        }
        for (int $$3 = 0; $$3 < $$2.size(); $$3++) {
            TelemetryEventType $$4 = (TelemetryEventType) $$2.get($$3);
            this.addEventType($$1, $$4);
            if ($$3 < $$2.size() - 1) {
                $$1.addSpacer(9);
            }
        }
        return $$1.build();
    }

    public void setOnScrolledListener(@Nullable DoubleConsumer doubleConsumer0) {
        this.onScrolledListener = doubleConsumer0;
    }

    @Override
    protected void setScrollAmount(double double0) {
        super.setScrollAmount(double0);
        if (this.onScrolledListener != null) {
            this.onScrolledListener.accept(this.m_239030_());
        }
    }

    @Override
    protected int getInnerHeight() {
        return this.content.container().m_93694_();
    }

    @Override
    protected double scrollRate() {
        return 9.0;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.m_252907_() + this.m_239244_();
        int $$5 = this.m_252754_() + this.m_239244_();
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((double) $$5, (double) $$4, 0.0);
        this.content.container().m_264134_(p_280896_ -> p_280896_.render(guiGraphics0, int1, int2, float3));
        guiGraphics0.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.content.narration());
    }

    private void addEventType(TelemetryEventWidget.ContentBuilder telemetryEventWidgetContentBuilder0, TelemetryEventType telemetryEventType1) {
        String $$2 = telemetryEventType1.isOptIn() ? "telemetry.event.optional" : "telemetry.event.required";
        telemetryEventWidgetContentBuilder0.addHeader(this.font, Component.translatable($$2, telemetryEventType1.title()));
        telemetryEventWidgetContentBuilder0.addHeader(this.font, telemetryEventType1.description().withStyle(ChatFormatting.GRAY));
        telemetryEventWidgetContentBuilder0.addSpacer(9 / 2);
        telemetryEventWidgetContentBuilder0.addLine(this.font, PROPERTY_TITLE, 2);
        this.addEventTypeProperties(telemetryEventType1, telemetryEventWidgetContentBuilder0);
    }

    private void addEventTypeProperties(TelemetryEventType telemetryEventType0, TelemetryEventWidget.ContentBuilder telemetryEventWidgetContentBuilder1) {
        for (TelemetryProperty<?> $$2 : telemetryEventType0.properties()) {
            telemetryEventWidgetContentBuilder1.addLine(this.font, $$2.title());
        }
    }

    private int containerWidth() {
        return this.f_93618_ - this.m_240012_();
    }

    static record Content(GridLayout f_260717_, Component f_260488_) {

        private final GridLayout container;

        private final Component narration;

        Content(GridLayout f_260717_, Component f_260488_) {
            this.container = f_260717_;
            this.narration = f_260488_;
        }
    }

    static class ContentBuilder {

        private final int width;

        private final GridLayout grid;

        private final GridLayout.RowHelper helper;

        private final LayoutSettings alignHeader;

        private final MutableComponent narration = Component.empty();

        public ContentBuilder(int int0) {
            this.width = int0;
            this.grid = new GridLayout();
            this.grid.defaultCellSetting().alignHorizontallyLeft();
            this.helper = this.grid.createRowHelper(1);
            this.helper.addChild(SpacerElement.width(int0));
            this.alignHeader = this.helper.newCellSettings().alignHorizontallyCenter().paddingHorizontal(32);
        }

        public void addLine(Font font0, Component component1) {
            this.addLine(font0, component1, 0);
        }

        public void addLine(Font font0, Component component1, int int2) {
            this.helper.addChild(new MultiLineTextWidget(component1, font0).setMaxWidth(this.width), this.helper.newCellSettings().paddingBottom(int2));
            this.narration.append(component1).append("\n");
        }

        public void addHeader(Font font0, Component component1) {
            this.helper.addChild(new MultiLineTextWidget(component1, font0).setMaxWidth(this.width - 64).setCentered(true), this.alignHeader);
            this.narration.append(component1).append("\n");
        }

        public void addSpacer(int int0) {
            this.helper.addChild(SpacerElement.height(int0));
        }

        public TelemetryEventWidget.Content build() {
            this.grid.arrangeElements();
            return new TelemetryEventWidget.Content(this.grid, this.narration);
        }
    }
}