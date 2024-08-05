package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.GameRules;

public class EditGameRulesScreen extends Screen {

    private final Consumer<Optional<GameRules>> exitCallback;

    private EditGameRulesScreen.RuleList rules;

    private final Set<EditGameRulesScreen.RuleEntry> invalidEntries = Sets.newHashSet();

    private Button doneButton;

    @Nullable
    private List<FormattedCharSequence> tooltip;

    private final GameRules gameRules;

    public EditGameRulesScreen(GameRules gameRules0, Consumer<Optional<GameRules>> consumerOptionalGameRules1) {
        super(Component.translatable("editGamerule.title"));
        this.gameRules = gameRules0;
        this.exitCallback = consumerOptionalGameRules1;
    }

    @Override
    protected void init() {
        this.rules = new EditGameRulesScreen.RuleList(this.gameRules);
        this.m_7787_(this.rules);
        GridLayout.RowHelper $$0 = new GridLayout().columnSpacing(10).createRowHelper(2);
        this.doneButton = $$0.addChild(Button.builder(CommonComponents.GUI_DONE, p_101059_ -> this.exitCallback.accept(Optional.of(this.gameRules))).build());
        $$0.addChild(Button.builder(CommonComponents.GUI_CANCEL, p_101073_ -> this.exitCallback.accept(Optional.empty())).build());
        $$0.getGrid().m_264134_(p_267855_ -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(p_267855_);
        });
        $$0.getGrid().m_264152_(this.f_96543_ / 2 - 155, this.f_96544_ - 28);
        $$0.getGrid().arrangeElements();
    }

    @Override
    public void onClose() {
        this.exitCallback.accept(Optional.empty());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.tooltip = null;
        this.rules.render(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    private void updateDoneButton() {
        this.doneButton.f_93623_ = this.invalidEntries.isEmpty();
    }

    void markInvalid(EditGameRulesScreen.RuleEntry editGameRulesScreenRuleEntry0) {
        this.invalidEntries.add(editGameRulesScreenRuleEntry0);
        this.updateDoneButton();
    }

    void clearInvalid(EditGameRulesScreen.RuleEntry editGameRulesScreenRuleEntry0) {
        this.invalidEntries.remove(editGameRulesScreenRuleEntry0);
        this.updateDoneButton();
    }

    public class BooleanRuleEntry extends EditGameRulesScreen.GameRuleEntry {

        private final CycleButton<Boolean> checkbox;

        public BooleanRuleEntry(Component component0, List<FormattedCharSequence> listFormattedCharSequence1, String string2, GameRules.BooleanValue gameRulesBooleanValue3) {
            super(listFormattedCharSequence1, component0);
            this.checkbox = CycleButton.onOffBuilder(gameRulesBooleanValue3.get()).displayOnlyValue().withCustomNarration(p_170219_ -> p_170219_.createDefaultNarrationMessage().append("\n").append(string2)).create(10, 5, 44, 20, component0, (p_170215_, p_170216_) -> gameRulesBooleanValue3.set(p_170216_, null));
            this.f_101160_.add(this.checkbox);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.m_280223_(guiGraphics0, int2, int3);
            this.checkbox.m_252865_(int3 + int4 - 45);
            this.checkbox.m_253211_(int2);
            this.checkbox.m_88315_(guiGraphics0, int6, int7, float9);
        }
    }

    public class CategoryRuleEntry extends EditGameRulesScreen.RuleEntry {

        final Component label;

        public CategoryRuleEntry(Component component0) {
            super(null);
            this.label = component0;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            guiGraphics0.drawCenteredString(EditGameRulesScreen.this.f_96541_.font, this.label, int3 + int4 / 2, int2 + 5, 16777215);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {

                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput p_170225_) {
                    p_170225_.add(NarratedElementType.TITLE, CategoryRuleEntry.this.label);
                }
            });
        }
    }

    @FunctionalInterface
    interface EntryFactory<T extends GameRules.Value<T>> {

        EditGameRulesScreen.RuleEntry create(Component var1, List<FormattedCharSequence> var2, String var3, T var4);
    }

    public abstract class GameRuleEntry extends EditGameRulesScreen.RuleEntry {

        private final List<FormattedCharSequence> label;

        protected final List<AbstractWidget> children = Lists.newArrayList();

        public GameRuleEntry(List<FormattedCharSequence> listFormattedCharSequence0, Component component1) {
            super(listFormattedCharSequence0);
            this.label = EditGameRulesScreen.this.f_96541_.font.split(component1, 175);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }

        protected void renderLabel(GuiGraphics guiGraphics0, int int1, int int2) {
            if (this.label.size() == 1) {
                guiGraphics0.drawString(EditGameRulesScreen.this.f_96541_.font, (FormattedCharSequence) this.label.get(0), int2, int1 + 5, 16777215, false);
            } else if (this.label.size() >= 2) {
                guiGraphics0.drawString(EditGameRulesScreen.this.f_96541_.font, (FormattedCharSequence) this.label.get(0), int2, int1, 16777215, false);
                guiGraphics0.drawString(EditGameRulesScreen.this.f_96541_.font, (FormattedCharSequence) this.label.get(1), int2, int1 + 10, 16777215, false);
            }
        }
    }

    public class IntegerRuleEntry extends EditGameRulesScreen.GameRuleEntry {

        private final EditBox input;

        public IntegerRuleEntry(Component component0, List<FormattedCharSequence> listFormattedCharSequence1, String string2, GameRules.IntegerValue gameRulesIntegerValue3) {
            super(listFormattedCharSequence1, component0);
            this.input = new EditBox(EditGameRulesScreen.this.f_96541_.font, 10, 5, 42, 20, component0.copy().append("\n").append(string2).append("\n"));
            this.input.setValue(Integer.toString(gameRulesIntegerValue3.get()));
            this.input.setResponder(p_101181_ -> {
                if (gameRulesIntegerValue3.tryDeserialize(p_101181_)) {
                    this.input.setTextColor(14737632);
                    EditGameRulesScreen.this.clearInvalid(this);
                } else {
                    this.input.setTextColor(16711680);
                    EditGameRulesScreen.this.markInvalid(this);
                }
            });
            this.f_101160_.add(this.input);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.m_280223_(guiGraphics0, int2, int3);
            this.input.m_252865_(int3 + int4 - 44);
            this.input.m_253211_(int2);
            this.input.m_88315_(guiGraphics0, int6, int7, float9);
        }
    }

    public abstract static class RuleEntry extends ContainerObjectSelectionList.Entry<EditGameRulesScreen.RuleEntry> {

        @Nullable
        final List<FormattedCharSequence> tooltip;

        public RuleEntry(@Nullable List<FormattedCharSequence> listFormattedCharSequence0) {
            this.tooltip = listFormattedCharSequence0;
        }
    }

    public class RuleList extends ContainerObjectSelectionList<EditGameRulesScreen.RuleEntry> {

        public RuleList(final GameRules gameRules0) {
            super(EditGameRulesScreen.this.f_96541_, EditGameRulesScreen.this.f_96543_, EditGameRulesScreen.this.f_96544_, 43, EditGameRulesScreen.this.f_96544_ - 32, 24);
            final Map<GameRules.Category, Map<GameRules.Key<?>, EditGameRulesScreen.RuleEntry>> $$2 = Maps.newHashMap();
            GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {

                @Override
                public void visitBoolean(GameRules.Key<GameRules.BooleanValue> p_101238_, GameRules.Type<GameRules.BooleanValue> p_101239_) {
                    this.addEntry(p_101238_, (p_101228_, p_101229_, p_101230_, p_101231_) -> EditGameRulesScreen.this.new BooleanRuleEntry(p_101228_, p_101229_, p_101230_, p_101231_));
                }

                @Override
                public void visitInteger(GameRules.Key<GameRules.IntegerValue> p_101241_, GameRules.Type<GameRules.IntegerValue> p_101242_) {
                    this.addEntry(p_101241_, (p_101233_, p_101234_, p_101235_, p_101236_) -> EditGameRulesScreen.this.new IntegerRuleEntry(p_101233_, p_101234_, p_101235_, p_101236_));
                }

                private <T extends GameRules.Value<T>> void addEntry(GameRules.Key<T> p_101225_, EditGameRulesScreen.EntryFactory<T> p_101226_) {
                    Component $$2 = Component.translatable(p_101225_.getDescriptionId());
                    Component $$3 = Component.literal(p_101225_.getId()).withStyle(ChatFormatting.YELLOW);
                    T $$4 = gameRules0.getRule(p_101225_);
                    String $$5 = $$4.serialize();
                    Component $$6 = Component.translatable("editGamerule.default", Component.literal($$5)).withStyle(ChatFormatting.GRAY);
                    String $$7 = p_101225_.getDescriptionId() + ".description";
                    List<FormattedCharSequence> $$10;
                    String $$11;
                    if (I18n.exists($$7)) {
                        Builder<FormattedCharSequence> $$8 = ImmutableList.builder().add($$3.getVisualOrderText());
                        Component $$9 = Component.translatable($$7);
                        EditGameRulesScreen.this.f_96547_.split($$9, 150).forEach($$8::add);
                        $$10 = $$8.add($$6.getVisualOrderText()).build();
                        $$11 = $$9.getString() + "\n" + $$6.getString();
                    } else {
                        $$10 = ImmutableList.of($$3.getVisualOrderText(), $$6.getVisualOrderText());
                        $$11 = $$6.getString();
                    }
                    ((Map) $$2.computeIfAbsent(p_101225_.getCategory(), p_101223_ -> Maps.newHashMap())).put(p_101225_, p_101226_.create($$2, $$10, $$11, $$4));
                }
            });
            $$2.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(p_101210_ -> {
                this.m_7085_(EditGameRulesScreen.this.new CategoryRuleEntry(Component.translatable(((GameRules.Category) p_101210_.getKey()).getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)));
                ((Map) p_101210_.getValue()).entrySet().stream().sorted(java.util.Map.Entry.comparingByKey(Comparator.comparing(GameRules.Key::m_46328_))).forEach(p_170229_ -> this.m_7085_((EditGameRulesScreen.RuleEntry) p_170229_.getValue()));
            });
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
            super.m_88315_(guiGraphics0, int1, int2, float3);
            EditGameRulesScreen.RuleEntry $$4 = (EditGameRulesScreen.RuleEntry) this.m_168795_();
            if ($$4 != null && $$4.tooltip != null) {
                EditGameRulesScreen.this.m_257959_($$4.tooltip);
            }
        }
    }
}