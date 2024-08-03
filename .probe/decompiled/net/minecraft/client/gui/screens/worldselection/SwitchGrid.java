package net.minecraft.client.gui.screens.worldselection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

class SwitchGrid {

    private static final int DEFAULT_SWITCH_BUTTON_WIDTH = 44;

    private final List<SwitchGrid.LabeledSwitch> switches;

    SwitchGrid(List<SwitchGrid.LabeledSwitch> listSwitchGridLabeledSwitch0) {
        this.switches = listSwitchGridLabeledSwitch0;
    }

    public void refreshStates() {
        this.switches.forEach(SwitchGrid.LabeledSwitch::m_267626_);
    }

    public static SwitchGrid.Builder builder(int int0) {
        return new SwitchGrid.Builder(int0);
    }

    public static class Builder {

        final int width;

        private final List<SwitchGrid.SwitchBuilder> switchBuilders = new ArrayList();

        int paddingLeft;

        int rowSpacing = 4;

        int rowCount;

        Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = Optional.empty();

        public Builder(int int0) {
            this.width = int0;
        }

        void increaseRow() {
            this.rowCount++;
        }

        public SwitchGrid.SwitchBuilder addSwitch(Component component0, BooleanSupplier booleanSupplier1, Consumer<Boolean> consumerBoolean2) {
            SwitchGrid.SwitchBuilder $$3 = new SwitchGrid.SwitchBuilder(component0, booleanSupplier1, consumerBoolean2, 44);
            this.switchBuilders.add($$3);
            return $$3;
        }

        public SwitchGrid.Builder withPaddingLeft(int int0) {
            this.paddingLeft = int0;
            return this;
        }

        public SwitchGrid.Builder withRowSpacing(int int0) {
            this.rowSpacing = int0;
            return this;
        }

        public SwitchGrid build(Consumer<LayoutElement> consumerLayoutElement0) {
            GridLayout $$1 = new GridLayout().rowSpacing(this.rowSpacing);
            $$1.addChild(SpacerElement.width(this.width - 44), 0, 0);
            $$1.addChild(SpacerElement.width(44), 0, 1);
            List<SwitchGrid.LabeledSwitch> $$2 = new ArrayList();
            this.rowCount = 0;
            for (SwitchGrid.SwitchBuilder $$3 : this.switchBuilders) {
                $$2.add($$3.build(this, $$1, 0));
            }
            $$1.arrangeElements();
            consumerLayoutElement0.accept($$1);
            SwitchGrid $$4 = new SwitchGrid($$2);
            $$4.refreshStates();
            return $$4;
        }

        public SwitchGrid.Builder withInfoUnderneath(int int0, boolean boolean1) {
            this.infoUnderneath = Optional.of(new SwitchGrid.InfoUnderneathSettings(int0, boolean1));
            return this;
        }
    }

    static record InfoUnderneathSettings(int f_268439_, boolean f_268690_) {

        private final int maxInfoRows;

        private final boolean alwaysMaxHeight;

        InfoUnderneathSettings(int f_268439_, boolean f_268690_) {
            this.maxInfoRows = f_268439_;
            this.alwaysMaxHeight = f_268690_;
        }
    }

    static record LabeledSwitch(CycleButton<Boolean> f_267423_, BooleanSupplier f_267403_, @Nullable BooleanSupplier f_267483_) {

        private final CycleButton<Boolean> button;

        private final BooleanSupplier stateSupplier;

        @Nullable
        private final BooleanSupplier isActiveCondition;

        LabeledSwitch(CycleButton<Boolean> f_267423_, BooleanSupplier f_267403_, @Nullable BooleanSupplier f_267483_) {
            this.button = f_267423_;
            this.stateSupplier = f_267403_;
            this.isActiveCondition = f_267483_;
        }

        public void refreshState() {
            this.button.setValue(this.stateSupplier.getAsBoolean());
            if (this.isActiveCondition != null) {
                this.button.f_93623_ = this.isActiveCondition.getAsBoolean();
            }
        }
    }

    public static class SwitchBuilder {

        private final Component label;

        private final BooleanSupplier stateSupplier;

        private final Consumer<Boolean> onClicked;

        @Nullable
        private Component info;

        @Nullable
        private BooleanSupplier isActiveCondition;

        private final int buttonWidth;

        SwitchBuilder(Component component0, BooleanSupplier booleanSupplier1, Consumer<Boolean> consumerBoolean2, int int3) {
            this.label = component0;
            this.stateSupplier = booleanSupplier1;
            this.onClicked = consumerBoolean2;
            this.buttonWidth = int3;
        }

        public SwitchGrid.SwitchBuilder withIsActiveCondition(BooleanSupplier booleanSupplier0) {
            this.isActiveCondition = booleanSupplier0;
            return this;
        }

        public SwitchGrid.SwitchBuilder withInfo(Component component0) {
            this.info = component0;
            return this;
        }

        SwitchGrid.LabeledSwitch build(SwitchGrid.Builder switchGridBuilder0, GridLayout gridLayout1, int int2) {
            switchGridBuilder0.increaseRow();
            StringWidget $$3 = new StringWidget(this.label, Minecraft.getInstance().font).alignLeft();
            gridLayout1.addChild($$3, switchGridBuilder0.rowCount, int2, gridLayout1.newCellSettings().align(0.0F, 0.5F).paddingLeft(switchGridBuilder0.paddingLeft));
            Optional<SwitchGrid.InfoUnderneathSettings> $$4 = switchGridBuilder0.infoUnderneath;
            CycleButton.Builder<Boolean> $$5 = CycleButton.onOffBuilder(this.stateSupplier.getAsBoolean());
            $$5.displayOnlyValue();
            boolean $$6 = this.info != null && !$$4.isPresent();
            if ($$6) {
                Tooltip $$7 = Tooltip.create(this.info);
                $$5.withTooltip(p_269644_ -> $$7);
            }
            if (this.info != null && !$$6) {
                $$5.withCustomNarration(p_269645_ -> CommonComponents.joinForNarration(this.label, p_269645_.createDefaultNarrationMessage(), this.info));
            } else {
                $$5.withCustomNarration(p_268230_ -> CommonComponents.joinForNarration(this.label, p_268230_.createDefaultNarrationMessage()));
            }
            CycleButton<Boolean> $$8 = $$5.create(0, 0, this.buttonWidth, 20, Component.empty(), (p_267942_, p_268251_) -> this.onClicked.accept(p_268251_));
            if (this.isActiveCondition != null) {
                $$8.f_93623_ = this.isActiveCondition.getAsBoolean();
            }
            gridLayout1.addChild($$8, switchGridBuilder0.rowCount, int2 + 1, gridLayout1.newCellSettings().alignHorizontallyRight());
            if (this.info != null) {
                $$4.ifPresent(p_269649_ -> {
                    Component $$4x = this.info.copy().withStyle(ChatFormatting.GRAY);
                    Font $$5x = Minecraft.getInstance().font;
                    MultiLineTextWidget $$6x = new MultiLineTextWidget($$4x, $$5x);
                    $$6x.setMaxWidth(switchGridBuilder0.width - switchGridBuilder0.paddingLeft - this.buttonWidth);
                    $$6x.setMaxRows(p_269649_.maxInfoRows());
                    switchGridBuilder0.increaseRow();
                    int $$7 = p_269649_.alwaysMaxHeight ? 9 * p_269649_.maxInfoRows - $$6x.getHeight() : 0;
                    gridLayout1.addChild($$6x, switchGridBuilder0.rowCount, int2, gridLayout1.newCellSettings().paddingTop(-switchGridBuilder0.rowSpacing).paddingBottom($$7));
                });
            }
            return new SwitchGrid.LabeledSwitch($$8, this.stateSupplier, this.isActiveCondition);
        }
    }
}