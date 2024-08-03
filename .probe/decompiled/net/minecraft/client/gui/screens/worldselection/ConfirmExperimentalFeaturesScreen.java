package net.minecraft.client.gui.screens.worldselection;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlags;

public class ConfirmExperimentalFeaturesScreen extends Screen {

    private static final Component TITLE = Component.translatable("selectWorld.experimental.title");

    private static final Component MESSAGE = Component.translatable("selectWorld.experimental.message");

    private static final Component DETAILS_BUTTON = Component.translatable("selectWorld.experimental.details");

    private static final int COLUMN_SPACING = 10;

    private static final int DETAILS_BUTTON_WIDTH = 100;

    private final BooleanConsumer callback;

    final Collection<Pack> enabledPacks;

    private final GridLayout layout = new GridLayout().columnSpacing(10).rowSpacing(20);

    public ConfirmExperimentalFeaturesScreen(Collection<Pack> collectionPack0, BooleanConsumer booleanConsumer1) {
        super(TITLE);
        this.enabledPacks = collectionPack0;
        this.callback = booleanConsumer1;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), MESSAGE);
    }

    @Override
    protected void init() {
        super.init();
        GridLayout.RowHelper $$0 = this.layout.createRowHelper(2);
        LayoutSettings $$1 = $$0.newCellSettings().alignHorizontallyCenter();
        $$0.addChild(new StringWidget(this.f_96539_, this.f_96547_), 2, $$1);
        MultiLineTextWidget $$2 = $$0.addChild(new MultiLineTextWidget(MESSAGE, this.f_96547_).setCentered(true), 2, $$1);
        $$2.setMaxWidth(310);
        $$0.addChild(Button.builder(DETAILS_BUTTON, p_280898_ -> this.f_96541_.setScreen(new ConfirmExperimentalFeaturesScreen.DetailsScreen())).width(100).build(), 2, $$1);
        $$0.addChild(Button.builder(CommonComponents.GUI_PROCEED, p_252248_ -> this.callback.accept(true)).build());
        $$0.addChild(Button.builder(CommonComponents.GUI_BACK, p_250397_ -> this.callback.accept(false)).build());
        this.layout.m_264134_(p_269625_ -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(p_269625_);
        });
        this.layout.arrangeElements();
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        FrameLayout.alignInRectangle(this.layout, 0, 0, this.f_96543_, this.f_96544_, 0.5F, 0.5F);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public void onClose() {
        this.callback.accept(false);
    }

    class DetailsScreen extends Screen {

        private ConfirmExperimentalFeaturesScreen.DetailsScreen.PackList packList;

        DetailsScreen() {
            super(Component.translatable("selectWorld.experimental.details.title"));
        }

        @Override
        public void onClose() {
            this.f_96541_.setScreen(ConfirmExperimentalFeaturesScreen.this);
        }

        @Override
        protected void init() {
            super.init();
            this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_251286_ -> this.onClose()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120 + 24, 200, 20).build());
            this.packList = new ConfirmExperimentalFeaturesScreen.DetailsScreen.PackList(this.f_96541_, ConfirmExperimentalFeaturesScreen.this.enabledPacks);
            this.m_7787_(this.packList);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            this.m_280273_(guiGraphics0);
            this.packList.m_88315_(guiGraphics0, int1, int2, float3);
            guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 10, 16777215);
            super.render(guiGraphics0, int1, int2, float3);
        }

        class PackList extends ObjectSelectionList<ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry> {

            public PackList(Minecraft minecraft0, Collection<Pack> collectionPack1) {
                super(minecraft0, DetailsScreen.this.f_96543_, DetailsScreen.this.f_96544_, 32, DetailsScreen.this.f_96544_ - 64, (9 + 2) * 3);
                for (Pack $$2 : collectionPack1) {
                    String $$3 = FeatureFlags.printMissingFlags(FeatureFlags.VANILLA_SET, $$2.getRequestedFeatures());
                    if (!$$3.isEmpty()) {
                        Component $$4 = ComponentUtils.mergeStyles($$2.getTitle().copy(), Style.EMPTY.withBold(true));
                        Component $$5 = Component.translatable("selectWorld.experimental.details.entry", $$3);
                        this.m_7085_(DetailsScreen.this.new PackListEntry($$4, $$5, MultiLineLabel.create(DetailsScreen.this.f_96547_, $$5, this.getRowWidth())));
                    }
                }
            }

            @Override
            public int getRowWidth() {
                return this.f_93388_ * 3 / 4;
            }
        }

        class PackListEntry extends ObjectSelectionList.Entry<ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry> {

            private final Component packId;

            private final Component message;

            private final MultiLineLabel splitMessage;

            PackListEntry(Component component0, Component component1, MultiLineLabel multiLineLabel2) {
                this.packId = component0;
                this.message = component1;
                this.splitMessage = multiLineLabel2;
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                guiGraphics0.drawString(DetailsScreen.this.f_96541_.font, this.packId, int3, int2, 16777215);
                this.splitMessage.renderLeftAligned(guiGraphics0, int3, int2 + 12, 9, 16777215);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", CommonComponents.joinForNarration(this.packId, this.message));
            }
        }
    }
}