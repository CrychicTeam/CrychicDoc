package snownee.jade.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import snownee.jade.Jade;
import snownee.jade.gui.config.BelowOrAboveListEntryTooltipPositioner;
import snownee.jade.gui.config.NotUglyEditBox;
import snownee.jade.gui.config.OptionsList;
import snownee.jade.gui.config.OptionsNav;
import snownee.jade.gui.config.value.OptionValue;

public abstract class BaseOptionsScreen extends Screen {

    private final Screen parent;

    private final Set<GuiEventListener> entryWidgets = Sets.newIdentityHashSet();

    public Button saveButton;

    protected Runnable saver;

    protected Runnable canceller;

    protected OptionsList options;

    protected OptionsNav optionsNav;

    private NotUglyEditBox searchBox;

    public BaseOptionsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    public BaseOptionsScreen(Screen parent, String title) {
        this(parent, OptionsList.Entry.makeTitle(title));
    }

    @Override
    protected void init() {
        Objects.requireNonNull(this.f_96541_);
        double scroll = this.options == null ? 0.0 : this.options.m_93517_();
        super.init();
        this.entryWidgets.clear();
        if (this.options != null) {
            this.options.removed();
        }
        this.options = this.createOptions();
        this.options.m_93507_(120);
        this.optionsNav = new OptionsNav(this.options, 120, this.f_96544_, 18, this.f_96544_ - 32, 18);
        this.searchBox = new NotUglyEditBox(this.f_96547_, 0, 0, 120, 18, this.searchBox, Component.translatable("gui.jade.search"));
        this.searchBox.setHint(Component.translatable("gui.jade.search.hint"));
        this.searchBox.responder = s -> {
            this.options.updateSearch(s);
            this.optionsNav.refresh();
        };
        this.searchBox.paddingLeft = 12;
        this.searchBox.paddingTop = 6;
        this.searchBox.paddingRight = 18;
        this.m_142416_(this.optionsNav);
        this.m_142416_(this.searchBox);
        this.m_142416_(this.options);
        this.searchBox.responder.accept(this.searchBox.getValue());
        this.options.setScrollAmount(scroll);
        this.saveButton = (Button) this.m_142416_(Button.builder(Component.translatable("gui.jade.save_and_quit").withStyle(style -> style.withColor(-4589878)), w -> {
            this.options.save();
            this.saver.run();
            this.f_96541_.setScreen(this.parent);
        }).bounds(this.f_96543_ - 100, this.f_96544_ - 25, 90, 20).build());
        if (this.canceller != null) {
            this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, w -> this.onClose()).bounds(this.saveButton.m_252754_() - 95, this.f_96544_ - 25, 90, 20).build());
        }
        this.options.updateSaveState();
        if (this.f_96541_.level != null) {
            CycleButton<Boolean> previewButton = CycleButton.booleanBuilder(OptionsList.OPTION_ON, OptionsList.OPTION_OFF).create(10, this.saveButton.m_252907_(), 85, 20, Component.translatable("gui.jade.preview"), (button, value) -> {
                Jade.CONFIG.get().getGeneral().previewOverlay = value;
                this.saver.run();
            });
            previewButton.setValue(Jade.CONFIG.get().getGeneral().previewOverlay);
            this.m_142416_(previewButton);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        OptionsList.Entry entry = this.options.m_5953_((double) mouseX, (double) mouseY) ? this.options.getEntryAt((double) mouseX, (double) mouseY) : null;
        if (entry != null) {
            if (!Strings.isNullOrEmpty(entry.getDescription())) {
                int valueX = entry.getTextX(this.options.getRowWidth());
                if (mouseX >= valueX && mouseX < valueX + entry.getTextWidth()) {
                    this.m_262791_(Tooltip.create(Component.literal(entry.getDescription())), new BelowOrAboveListEntryTooltipPositioner(this.options, entry), false);
                }
            }
            if (entry instanceof OptionValue<?> optionValue && optionValue.serverFeature) {
                int x = entry.getTextX(this.options.getRowWidth()) + entry.getTextWidth() + 1;
                int y = this.options.getRowTop(this.options.m_6702_().indexOf(entry)) + 7;
                if (mouseX >= x && mouseX < x + 4 && mouseY >= y && mouseY < y + 4) {
                    this.m_262791_(Tooltip.create(Component.translatable("gui.jade.server_feature")), new BelowOrAboveListEntryTooltipPositioner(this.options, entry), false);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (this.searchBox != null) {
            this.searchBox.tick();
        }
    }

    public abstract OptionsList createOptions();

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.optionsNav.m_5953_(mouseX, mouseY) ? this.optionsNav.m_6050_(mouseX, mouseY, delta) : this.options.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.canceller != null) {
            this.canceller.run();
        }
        ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(this.parent);
    }

    @Override
    public void removed() {
        this.options.removed();
    }

    public <T extends GuiEventListener & NarratableEntry> T addEntryWidget(T widget) {
        this.entryWidgets.add(widget);
        return super.addWidget(widget);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int int0) {
        boolean onList = this.options.m_5953_(mouseX, mouseY);
        for (GuiEventListener guieventlistener : this.m_6702_()) {
            if ((onList || !this.entryWidgets.contains(guieventlistener)) && guieventlistener.mouseClicked(mouseX, mouseY, int0)) {
                this.m_7522_(guieventlistener);
                if (int0 == 0) {
                    this.m_7897_(true);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.options.selectedKey == null;
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        boolean onList = this.options != null && this.options.m_5953_(mouseX, mouseY);
        for (GuiEventListener guieventlistener : this.m_6702_()) {
            if ((onList || !this.entryWidgets.contains(guieventlistener)) && guieventlistener.isMouseOver(mouseX, mouseY)) {
                return Optional.of(guieventlistener);
            }
        }
        return Optional.empty();
    }

    public boolean forcePreviewOverlay() {
        Objects.requireNonNull(this.f_96541_);
        if (this.m_7282_() && this.options != null) {
            OptionsList.Entry entry = (OptionsList.Entry) this.options.m_93511_();
            return entry != null && entry.getFirstWidget() != null ? this.options.forcePreview.contains(entry) : false;
        } else {
            return false;
        }
    }
}