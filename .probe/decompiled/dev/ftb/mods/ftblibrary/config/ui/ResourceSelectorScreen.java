package dev.ftb.mods.ftblibrary.config.ui;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftblibrary.FTBLibrary;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ResourceConfigValue;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.nbtedit.NBTEditorScreen;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.snbt.SNBTSyntaxException;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
import dev.ftb.mods.ftblibrary.ui.misc.SimpleToast;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ResourceSelectorScreen<T> extends AbstractThreePanelScreen<ResourceSelectorScreen<T>.StacksPanel> {

    private static final int ITEM_COLS = 9;

    private static final int ITEM_ROWS = 5;

    private static final ExecutorService SEARCH_EXECUTOR = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "FTBLibrary-ItemSearch");
        thread.setDaemon(true);
        return thread;
    });

    private final ResourceConfigValue<T> config;

    private final ConfigCallback callback;

    private SelectableResource<T> selectedStack;

    private int refreshTimer = 0;

    private final TextField selectedLabel;

    private final TextBox searchBox;

    private final ResourceSelectorScreen<T>.CountTextBox countBox;

    private final Button upBtn;

    private final Button downBtn;

    private final ResourceSelectorScreen<T>.SearchModeButton searchModeButton;

    private final ResourceSelectorScreen<T>.NBTButton nbtButton;

    private int nRows = 5;

    private int nCols = 9;

    public long update = Long.MAX_VALUE;

    public ResourceSelectorScreen(ResourceConfigValue<T> config, ConfigCallback callback) {
        this.config = config;
        this.callback = callback;
        this.searchModeButton = new ResourceSelectorScreen.SearchModeButton(this.topPanel);
        this.nbtButton = new ResourceSelectorScreen.NBTButton(this.topPanel);
        this.selectedLabel = new TextField(this.topPanel);
        this.searchBox = new TextBox(this.topPanel) {

            @Override
            public void onTextChanged() {
                ResourceSelectorScreen.this.refreshTimer = 5;
            }
        };
        this.searchBox.ghostText = I18n.get("gui.search_box");
        this.searchBox.setFocused(true);
        this.countBox = new ResourceSelectorScreen.CountTextBox();
        this.upBtn = new ResourceSelectorScreen.AdjusterButton(true);
        this.downBtn = new ResourceSelectorScreen.AdjusterButton(false);
        this.scrollBar.setCanAlwaysScroll(true);
        this.scrollBar.setScrollStep(19.0);
        this.setSelected(config.getResource());
    }

    public ResourceSelectorScreen<T> withGridSize(int rows, int cols) {
        Validate.isTrue(rows >= 1 && cols >= 1);
        this.nRows = rows;
        this.nCols = cols;
        return this;
    }

    @Override
    public boolean onInit() {
        this.setWidth(19 + 18 * this.nCols);
        this.setHeight(108 + 18 * this.nRows);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.refreshTimer > 0 && --this.refreshTimer == 0) {
            this.mainPanel.refreshWidgets();
        }
    }

    @Override
    protected int getTopPanelHeight() {
        return 78;
    }

    protected ResourceSelectorScreen<T>.StacksPanel createMainPanel() {
        return new ResourceSelectorScreen.StacksPanel();
    }

    @Override
    protected Panel createTopPanel() {
        return new ResourceSelectorScreen.CustomTopPanel();
    }

    @Override
    protected Pair<Integer, Integer> mainPanelInset() {
        return Pair.of(2, 2);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(graphics, theme, x, y, w, h);
        if (Util.getMillis() >= this.update) {
            this.update = Long.MAX_VALUE;
            CompletableFuture.supplyAsync(() -> this.makeResourceWidgets(this.searchBox.getText().toLowerCase()), SEARCH_EXECUTOR).thenAcceptAsync(this::updateItemWidgets, Minecraft.getInstance());
        }
    }

    @Override
    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if (!this.selectedStack.isEmpty()) {
            this.selectedStack.getIcon().drawStatic(graphics, this.getX() + 6, this.getY() + 17, 30, 30);
            GuiHelper.drawRectWithShade(graphics, this.getX() + 5, this.getY() + 16, 32, 32, Color4I.DARK_GRAY, -16);
            if (this.countBox.shouldDraw()) {
                theme.drawString(graphics, "x", this.getX() + 38, this.getY() + 28, 2);
            }
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        if (super.keyPressed(key)) {
            return true;
        } else if ((key.is(257) || key.is(335)) && key.modifiers.shift()) {
            this.doAccept();
            return true;
        } else {
            return false;
        }
    }

    protected void setSelected(SelectableResource<T> stack) {
        long count = this.selectedStack != null && !this.selectedStack.isEmpty() ? this.selectedStack.getCount() : Math.max(stack.getCount(), (long) this.countBox.getCount());
        this.selectedStack = stack.copyWithCount(count);
        Component name = (Component) (this.selectedStack.isEmpty() ? Component.translatable("ftblibrary.gui.no_selection").withStyle(ChatFormatting.ITALIC) : this.selectedStack.getName());
        this.selectedLabel.setText(name);
        this.countBox.setText(Long.toString(this.config.fixedResourceSize().orElse(this.selectedStack.getCount())), false);
    }

    @Override
    protected void doCancel() {
        this.callback.save(false);
    }

    @Override
    protected void doAccept() {
        boolean changed = this.config.setResource(this.selectedStack);
        this.callback.save(changed);
    }

    protected int defaultQuantity() {
        return 1;
    }

    protected abstract ResourceSelectorScreen<T>.ResourceButton makeResourceButton(Panel var1, @Nullable SelectableResource<T> var2);

    protected abstract SearchModeIndex<ResourceSearchMode<T>> getSearchModeIndex();

    private Optional<ResourceSearchMode<T>> getActiveSearchMode() {
        return this.getSearchModeIndex().getCurrentSearchMode();
    }

    public List<Widget> makeResourceWidgets(String search) {
        Stopwatch timer = Stopwatch.createStarted();
        if (this.getActiveSearchMode().isEmpty()) {
            return Collections.emptyList();
        } else {
            Collection<? extends SelectableResource<T>> items = ((ResourceSearchMode) this.getActiveSearchMode().get()).getAllResources();
            List<Widget> widgets = new ArrayList(search.isEmpty() ? items.size() + 1 : 64);
            ResourceSelectorScreen<T>.ResourceButton emptyButton = this.makeResourceButton(this.mainPanel, null);
            if (this.config.allowEmptyResource() && emptyButton.shouldAdd(search)) {
                emptyButton.setPos(1, 1);
                widgets.add(emptyButton);
            }
            for (SelectableResource<T> resource : items) {
                if (!resource.isEmpty()) {
                    ResourceSelectorScreen<T>.ResourceButton button = this.makeResourceButton(this.mainPanel, resource);
                    if (button.shouldAdd(search)) {
                        widgets.add(button);
                        int idx = widgets.size() - 1;
                        button.setPos(1 + idx % this.nCols * 18, 1 + idx / this.nCols * 18);
                    }
                }
            }
            FTBLibrary.LOGGER.info("Done updating item list in {}μs!", timer.stop().elapsed(TimeUnit.MICROSECONDS));
            return widgets;
        }
    }

    private void updateItemWidgets(List<Widget> items) {
        this.mainPanel.getWidgets().clear();
        this.mainPanel.addAll(items);
        this.scrollBar.setValue(0.0);
    }

    private class AdjusterButton extends SimpleTextButton {

        private final boolean up;

        public AdjusterButton(boolean up) {
            super(ResourceSelectorScreen.this.topPanel, Component.literal(up ? "+" : "-"), Icon.empty());
            this.up = up;
        }

        @Override
        public void onClicked(MouseButton button) {
            int amt = isShiftKeyDown() ? (this.up ? ResourceSelectorScreen.this.countBox.getCount() : ResourceSelectorScreen.this.countBox.getCount() / 2) : 1;
            if (amt != 0) {
                ResourceSelectorScreen.this.countBox.adjust(this.up ? amt : -amt);
            }
        }

        @Override
        public boolean shouldDraw() {
            return ResourceSelectorScreen.this.config.fixedResourceSize().isEmpty() && !ResourceSelectorScreen.this.selectedStack.isEmpty();
        }

        @Override
        public void addMouseOverText(TooltipList list) {
        }
    }

    private class CountTextBox extends TextBox {

        private static final Pattern INTEGER = Pattern.compile("^\\d+$");

        public CountTextBox() {
            super(ResourceSelectorScreen.this.topPanel);
        }

        @Override
        public boolean mouseScrolled(double scroll) {
            if (!this.isMouseOver) {
                return false;
            } else {
                if (isShiftKeyDown()) {
                    int adj = scroll > 0.0 ? this.getCount() : -this.getCount() / 2;
                    this.adjust(adj);
                } else {
                    this.adjust((int) Math.signum(scroll));
                }
                return true;
            }
        }

        @Override
        public void onTextChanged() {
            if (!ResourceSelectorScreen.this.selectedStack.isEmpty()) {
                ResourceSelectorScreen.this.selectedStack.setCount(Math.max(1, this.getCount()));
            }
        }

        @Override
        public boolean isValid(String txt) {
            return INTEGER.matcher(txt).matches();
        }

        private int getCount() {
            try {
                return Integer.parseInt(this.getText());
            } catch (NumberFormatException var2) {
                return 1;
            }
        }

        void adjust(int offset) {
            int count = Math.max(1, this.getCount() + offset);
            this.setText(Integer.toString(count));
        }

        @Override
        public boolean shouldDraw() {
            return ResourceSelectorScreen.this.config.fixedResourceSize().isEmpty() && !ResourceSelectorScreen.this.selectedStack.isEmpty();
        }
    }

    private class CustomTopPanel extends AbstractThreePanelScreen<ResourceSelectorScreen<T>.StacksPanel>.TopPanel {

        @Override
        public void addWidgets() {
            this.add(ResourceSelectorScreen.this.selectedLabel);
            this.add(ResourceSelectorScreen.this.searchModeButton);
            this.add(ResourceSelectorScreen.this.searchBox);
            if (ResourceSelectorScreen.this.config.canHaveNBT()) {
                this.add(ResourceSelectorScreen.this.nbtButton);
            }
            this.add(ResourceSelectorScreen.this.countBox);
            this.add(ResourceSelectorScreen.this.upBtn);
            this.add(ResourceSelectorScreen.this.downBtn);
        }

        @Override
        public void alignWidgets() {
            ResourceSelectorScreen.this.selectedLabel.setPosAndSize(5, 5, this.getGui().width - 10, ResourceSelectorScreen.this.getTheme().getFontHeight());
            ResourceSelectorScreen.this.selectedLabel.setMaxWidth(ResourceSelectorScreen.this.selectedLabel.width).setTrim().showTooltipForLongText();
            ResourceSelectorScreen.this.searchModeButton.setPos(5, 54);
            ResourceSelectorScreen.this.searchBox.setPosAndSize(ResourceSelectorScreen.this.searchModeButton.posX + 22, 56, 100, 16);
            ResourceSelectorScreen.this.countBox.setPosAndSize(46, 25, 45, 16);
            ResourceSelectorScreen.this.upBtn.setPosAndSize(ResourceSelectorScreen.this.countBox.posX + ResourceSelectorScreen.this.countBox.width + 1, 21, 12, 12);
            ResourceSelectorScreen.this.downBtn.setPosAndSize(ResourceSelectorScreen.this.countBox.posX + ResourceSelectorScreen.this.countBox.width + 1, 33, 12, 12);
            if (ResourceSelectorScreen.this.config.canHaveNBT()) {
                int x = ResourceSelectorScreen.this.config.fixedResourceSize().isEmpty() ? ResourceSelectorScreen.this.upBtn.getPosX() + 16 : 44;
                ResourceSelectorScreen.this.nbtButton.setPosAndSize(x, 23, 20, 20);
            }
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.drawBackground(graphics, theme, x, y, w, h);
            if (!ResourceSelectorScreen.this.selectedStack.isEmpty()) {
                theme.drawSlot(graphics, this.getX() + 5, this.getY() + 16, 32, 32, WidgetType.NORMAL);
            }
        }
    }

    private class NBTButton extends Button {

        public NBTButton(Panel panel) {
            super(panel, Component.translatable("ftblibrary.select_item.nbt"), ItemIcon.getItemIcon(Items.NAME_TAG));
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            CompoundTag toEdit = (CompoundTag) Objects.requireNonNullElse(ResourceSelectorScreen.this.selectedStack.getTag(), new CompoundTag());
            if (button.isLeft()) {
                StringConfig config = new StringConfig();
                SNBTCompoundTag snbt = SNBTCompoundTag.of(toEdit);
                snbt.singleLine();
                config.setCurrentValue(String.join(",", SNBT.writeLines(snbt)));
                this.getGui().pushModalPanel(this.makeMultilineEditPanel(config));
            } else if (button.isRight()) {
                CompoundTag info = Util.make(new CompoundTag(), tag -> tag.putString("type", "item"));
                new NBTEditorScreen(info, toEdit, (accepted, tag) -> {
                    if (accepted) {
                        ResourceSelectorScreen.this.selectedStack.setTag(tag.copy());
                        ResourceSelectorScreen.this.openGuiLater();
                    }
                }).openGui();
            }
        }

        @NotNull
        private EditMultilineStringConfigOverlay makeMultilineEditPanel(StringConfig config) {
            EditMultilineStringConfigOverlay panel = new EditMultilineStringConfigOverlay(ResourceSelectorScreen.this, config, accepted -> {
                if (accepted) {
                    try {
                        ResourceSelectorScreen.this.selectedStack.setTag(SNBT.readLines(List.of(config.getValue())));
                    } catch (SNBTSyntaxException var4) {
                        SimpleToast.error(Component.translatable("ftblibrary.gui.error"), Component.literal(var4.getMessage()));
                    }
                }
            });
            panel.setExtraZlevel(300);
            int w = this.getScreen().getGuiScaledWidth() - 10 - this.getX();
            panel.setPosAndSize(this.getPosX(), this.getPosY() + this.getHeight(), w, 50);
            return panel;
        }

        @Override
        public boolean shouldDraw() {
            return !ResourceSelectorScreen.this.selectedStack.isEmpty();
        }
    }

    protected abstract class ResourceButton extends Button {

        protected final SelectableResource<T> resource;

        protected ResourceButton(Panel panel, SelectableResource<T> is) {
            super(panel, Component.empty(), Icons.BARRIER);
            this.setSize(18, 18);
            this.resource = is;
            this.title = null;
            this.icon = this.resource.getIcon();
        }

        public T getStack() {
            return this.resource.stack();
        }

        public abstract boolean shouldAdd(String var1);

        @Override
        public Component getTitle() {
            if (this.title == null) {
                this.title = this.resource.getName();
            }
            return this.title;
        }

        @Override
        public void addMouseOverText(TooltipList list) {
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawSlot(graphics, x, y, w, h, this.getWidgetType());
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            ResourceSelectorScreen.this.setSelected(this.resource);
        }

        @Override
        public boolean mouseDoubleClicked(MouseButton button) {
            if (this.isMouseOver()) {
                ResourceSelectorScreen.this.setSelected(this.resource.copyWithCount((long) Math.max(1, ResourceSelectorScreen.this.countBox.getCount())));
                ResourceSelectorScreen.this.doAccept();
                return true;
            } else {
                return false;
            }
        }
    }

    private class SearchModeButton extends Button {

        public SearchModeButton(Panel panel) {
            super(panel);
            this.setSize(20, 20);
        }

        @Override
        public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            ResourceSelectorScreen.this.getActiveSearchMode().ifPresent(mode -> mode.getIcon().draw(graphics, x, y, w, h));
        }

        @Override
        public Component getTitle() {
            return Component.translatable("ftblibrary.select_item.list_mode");
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            super.addMouseOverText(list);
            ResourceSelectorScreen.this.getActiveSearchMode().ifPresent(mode -> list.add(mode.getDisplayName().withStyle(ChatFormatting.GRAY).append(Component.literal(" [" + ResourceSelectorScreen.this.mainPanel.getWidgets().size() + "]").withStyle(ChatFormatting.DARK_GRAY))));
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            ResourceSelectorScreen.this.getSearchModeIndex().nextMode();
            ResourceSelectorScreen.this.mainPanel.refreshWidgets();
        }
    }

    public class StacksPanel extends Panel {

        public StacksPanel() {
            super(ResourceSelectorScreen.this);
        }

        @Override
        public void addWidgets() {
            ResourceSelectorScreen.this.update = Util.getMillis() + 50L;
        }

        @Override
        public void alignWidgets() {
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            GuiHelper.drawHollowRect(graphics, x - 1, y - 1, w + 2, h + 2, Color4I.rgb(1052688), false);
        }
    }
}