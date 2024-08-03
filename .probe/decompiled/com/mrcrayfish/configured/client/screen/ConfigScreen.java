package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.configured.Config;
import com.mrcrayfish.configured.Constants;
import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IConfigEntry;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.screen.widget.CheckBoxButton;
import com.mrcrayfish.configured.client.screen.widget.ConfiguredButton;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import com.mrcrayfish.configured.util.ConfigHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigScreen extends ListMenuScreen implements IEditing {

    public static final int TOOLTIP_WIDTH = 200;

    public static final Comparator<ListMenuScreen.Item> SORT_ALPHABETICALLY = (o1, o2) -> {
        if (o1 instanceof ConfigScreen.FolderItem && o2 instanceof ConfigScreen.FolderItem) {
            return o1.getLabel().compareTo(o2.getLabel());
        } else if (!(o1 instanceof ConfigScreen.FolderItem) && o2 instanceof ConfigScreen.FolderItem) {
            return 1;
        } else {
            return o1 instanceof ConfigScreen.FolderItem ? -1 : o1.getLabel().compareTo(o2.getLabel());
        }
    };

    protected final IConfigEntry folderEntry;

    protected final IModConfig config;

    protected final Map<String, String> cachedTextMap = new HashMap();

    protected Button saveButton;

    protected Button restoreButton;

    protected CheckBoxButton deepSearchCheckBox;

    private ConfigScreen(Screen parent, Component title, IModConfig config, ResourceLocation background, IConfigEntry folderEntry) {
        super(parent, title, background, 24);
        this.config = config;
        this.folderEntry = folderEntry;
    }

    public ConfigScreen(Screen parent, Component title, IModConfig config, ResourceLocation background) {
        super(parent, title, background, 24);
        this.config = config;
        this.folderEntry = config.getRoot();
    }

    @Override
    public IModConfig getActiveConfig() {
        return this.config;
    }

    @Override
    public void removed() {
        this.cachedTextMap.clear();
    }

    @Override
    protected void constructEntries(List<ListMenuScreen.Item> entries) {
        List<ListMenuScreen.Item> configEntries = new ArrayList();
        this.folderEntry.getChildren().forEach(entry -> {
            ListMenuScreen.Item item = this.createItemFromEntry(entry);
            if (item != null) {
                configEntries.add(item);
            }
        });
        configEntries.sort(SORT_ALPHABETICALLY);
        entries.addAll(configEntries);
    }

    @Nullable
    private ListMenuScreen.Item createItemFromEntry(IConfigEntry entry) {
        if (entry.isLeaf()) {
            IConfigValue<?> value = entry.getValue();
            if (value != null) {
                Object object = value.get();
                if (object instanceof Boolean) {
                    return new ConfigScreen.BooleanItem((IConfigValue<Boolean>) value);
                }
                if (object instanceof Integer) {
                    return new ConfigScreen.IntegerItem((IConfigValue<Integer>) value);
                }
                if (object instanceof Double) {
                    return new ConfigScreen.DoubleItem((IConfigValue<Double>) value);
                }
                if (object instanceof Long) {
                    return new ConfigScreen.LongItem((IConfigValue<Long>) value);
                }
                if (object instanceof Enum) {
                    return new ConfigScreen.EnumItem((IConfigValue<Enum<?>>) value);
                }
                if (object instanceof String) {
                    return new ConfigScreen.StringItem((IConfigValue<String>) value);
                }
                if (object instanceof List) {
                    return new ConfigScreen.ListItem((IConfigValue<List<?>>) value);
                }
                Constants.LOG.info("Unsupported config value: " + value.getName());
            }
            return null;
        } else {
            return new ConfigScreen.FolderItem(entry);
        }
    }

    @Override
    protected void init() {
        super.init();
        if (this.folderEntry.isRoot()) {
            this.saveButton = (Button) this.m_142416_(new IconButton(this.f_96543_ / 2 - 140, this.f_96544_ - 29, 22, 0, 90, Component.translatable("configured.gui.save"), button -> {
                this.saveConfig();
                if (ConfigHelper.getChangedValues(this.folderEntry).stream().anyMatch(IConfigValue::requiresGameRestart)) {
                    ConfirmationScreen confirm = new ConfirmationScreen(this.parent, Component.translatable("configured.gui.game_restart_needed"), ConfirmationScreen.Icon.INFO, result -> true);
                    confirm.setPositiveText(Component.translatable("configured.gui.close"));
                    confirm.setNegativeText(null);
                    this.f_96541_.setScreen(confirm);
                } else if (this.f_96541_.level != null && ConfigHelper.getChangedValues(this.folderEntry).stream().anyMatch(IConfigValue::requiresWorldRestart)) {
                    ConfirmationScreen confirm = new ConfirmationScreen(this.parent, Component.translatable("configured.gui.world_restart_needed"), ConfirmationScreen.Icon.INFO, result -> true);
                    confirm.setPositiveText(Component.translatable("configured.gui.close"));
                    confirm.setNegativeText(null);
                    this.f_96541_.setScreen(confirm);
                } else {
                    this.f_96541_.setScreen(this.parent);
                }
            }));
            this.restoreButton = (Button) this.m_142416_(new IconButton(this.f_96543_ / 2 - 45, this.f_96544_ - 29, 0, 0, 90, Component.translatable("configured.gui.reset_all"), button -> {
                if (this.folderEntry.isRoot()) {
                    this.showRestoreScreen();
                }
            }));
            this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 + 50, this.f_96544_ - 29, 90, 20, CommonComponents.GUI_CANCEL, button -> {
                if (this.isChanged(this.folderEntry)) {
                    this.f_96541_.setScreen(new ActiveConfirmationScreen(this, this.config, Component.translatable("configured.gui.unsaved_changes"), ConfirmationScreen.Icon.WARNING, result -> {
                        if (!result) {
                            return true;
                        } else {
                            this.f_96541_.setScreen(this.parent);
                            return false;
                        }
                    }).setBackground(this.background));
                } else {
                    this.f_96541_.setScreen(this.parent);
                }
            }));
            this.updateButtons();
        } else {
            this.m_142416_(new IconButton(this.f_96543_ / 2 - 130, this.f_96544_ - 29, 22, 44, 128, Component.translatable("configured.gui.home"), button -> {
                ConfigScreen target = this;
                while (target.parent instanceof ConfigScreen) {
                    target = (ConfigScreen) target.parent;
                }
                this.f_96541_.setScreen(target);
            }));
            this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 + 2, this.f_96544_ - 29, 128, 20, CommonComponents.GUI_BACK, button -> this.f_96541_.setScreen(this.parent)));
        }
        this.deepSearchCheckBox = new CheckBoxButton(this.f_96543_ / 2 + 115, 25, button -> this.updateSearchResults());
        this.m_142416_(this.deepSearchCheckBox);
    }

    private void saveConfig() {
        if (this.isChanged(this.folderEntry) && this.config != null) {
            this.config.update(this.folderEntry);
        }
    }

    private void showRestoreScreen() {
        ConfirmationScreen confirmScreen = new ActiveConfirmationScreen(this, this.config, Component.translatable("configured.gui.restore_message"), ConfirmationScreen.Icon.WARNING, result -> {
            if (!result) {
                return true;
            } else {
                this.restoreDefaults(this.folderEntry);
                this.updateButtons();
                return true;
            }
        });
        confirmScreen.setBackground(this.background);
        confirmScreen.setPositiveText(Component.translatable("configured.gui.reset_all").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        confirmScreen.setNegativeText(CommonComponents.GUI_CANCEL);
        Minecraft.getInstance().setScreen(confirmScreen);
    }

    private void restoreDefaults(IConfigEntry entry) {
        for (IConfigEntry child : entry.getChildren()) {
            if (child.isLeaf()) {
                IConfigValue<?> value = child.getValue();
                if (value != null) {
                    value.restore();
                }
            } else {
                this.restoreDefaults(child);
            }
        }
    }

    private void updateButtons() {
        if (this.folderEntry.isRoot()) {
            if (this.saveButton != null) {
                this.saveButton.f_93623_ = !this.config.isReadOnly() && this.isChanged(this.folderEntry);
            }
            if (this.restoreButton != null) {
                this.restoreButton.f_93623_ = !this.config.isReadOnly() && this.isModified(this.folderEntry);
            }
        }
    }

    @Override
    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.config.isReadOnly()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(IconButton.ICONS, this.f_96543_ - 30, 14, 20, 20, 0.0F, 33.0F, 10, 10, 64, 64);
            if (ScreenUtil.isMouseWithin(this.f_96543_ - 30, 14, 20, 20, mouseX, mouseY)) {
                this.setActiveTooltip(Component.translatable("configured.gui.read_only_config"), -14785178);
            }
        }
        if (this.deepSearchCheckBox.m_5953_((double) mouseX, (double) mouseY)) {
            this.setActiveTooltip(Component.translatable("configured.gui.deep_search"));
        }
    }

    @Override
    protected Collection<ListMenuScreen.Item> getSearchResults(String s) {
        List<ListMenuScreen.Item> entries = this.entries;
        if (this.deepSearchCheckBox.m_93840_()) {
            List<ListMenuScreen.Item> allEntries = new ArrayList();
            ConfigHelper.gatherAllConfigEntries(this.folderEntry).forEach(entry -> {
                ListMenuScreen.Item item = this.createItemFromEntry(entry);
                if (item instanceof ConfigScreen.ConfigItem) {
                    allEntries.add(item);
                }
            });
            allEntries.sort(SORT_ALPHABETICALLY);
            entries = allEntries;
        }
        return (Collection<ListMenuScreen.Item>) entries.stream().filter(item -> {
            if (item instanceof ListMenuScreen.IIgnoreSearch) {
                return false;
            } else {
                return item instanceof ConfigScreen.FolderItem && !Config.isIncludeFoldersInSearch() ? false : item.getLabel().toLowerCase(Locale.ENGLISH).contains(s.toLowerCase(Locale.ENGLISH));
            }
        }).collect(Collectors.toList());
    }

    private static String createLabelFromHolder(IConfigValue<?> holder) {
        return holder.getTranslationKey() != null && I18n.exists(holder.getTranslationKey()) ? Component.translatable(holder.getTranslationKey()).getString() : createLabel(holder.getName());
    }

    public static String createLabel(String input) {
        String[] words = input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        for (int i = 0; i < words.length; i++) {
            words[i] = StringUtils.capitalize(words[i]);
        }
        String valueName = Strings.join(words, " ");
        words = valueName.split("[_\\-.]");
        for (int i = 0; i < words.length; i++) {
            words[i] = StringUtils.capitalize(words[i]);
        }
        return Strings.join(words, " ").replaceAll("\\s++", " ");
    }

    private static List<FormattedText> splitTooltip(FormattedText text) {
        return Minecraft.getInstance().font.getSplitter().splitLines(text, 200, Style.EMPTY);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.config == null || this.config.getType() != ConfigType.WORLD;
    }

    public boolean isModified(IConfigEntry entry) {
        if (!entry.isLeaf()) {
            for (IConfigEntry child : entry.getChildren()) {
                if (this.isModified(child)) {
                    return true;
                }
            }
            return false;
        } else {
            IConfigValue<?> value = entry.getValue();
            return value != null && !value.isDefault();
        }
    }

    public boolean isChanged(IConfigEntry entry) {
        if (!entry.isLeaf()) {
            for (IConfigEntry child : entry.getChildren()) {
                if (this.isChanged(child)) {
                    return true;
                }
            }
            return false;
        } else {
            IConfigValue<?> value = entry.getValue();
            return value != null && value.isChanged();
        }
    }

    public class BooleanItem extends ConfigScreen.ConfigItem<Boolean> {

        private final Button button;

        public BooleanItem(IConfigValue<Boolean> holder) {
            super(holder);
            this.button = ScreenUtil.button(10, 5, 46, 20, CommonComponents.optionStatus(holder.get()), button -> {
                holder.set(!holder.get());
                button.m_93666_(CommonComponents.optionStatus(holder.get()));
                ConfigScreen.this.updateButtons();
            });
            this.button.f_93623_ = !ConfigScreen.this.config.isReadOnly();
            this.eventListeners.add(this.button);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            super.render(graphics, index, top, left, width, p_230432_6_, mouseX, mouseY, hovered, partialTicks);
            this.button.m_252865_(left + width - 69);
            this.button.m_253211_(top);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public void onResetValue() {
            this.button.m_93666_(CommonComponents.optionStatus(this.holder.get()));
        }
    }

    public abstract class ConfigItem<T> extends ListMenuScreen.Item {

        protected final IConfigValue<T> holder;

        protected final List<GuiEventListener> eventListeners = new ArrayList();

        protected final ConfiguredButton resetButton;

        protected Component validationHint;

        public ConfigItem(IConfigValue<T> holder) {
            super(ConfigScreen.createLabelFromHolder(holder));
            this.holder = holder;
            this.tooltip = this.createToolTip(holder);
            int maxTooltipWidth = Math.max(ConfigScreen.this.f_96543_ / 2 - 43, 170);
            this.resetButton = new IconButton(0, 0, 0, 0, onPress -> {
                this.holder.restore();
                this.onResetValue();
                ConfigScreen.this.updateButtons();
            });
            this.resetButton.setTooltip(Tooltip.create(Component.translatable("configured.gui.reset")), btn -> btn.m_142518_() && btn.m_198029_());
            this.resetButton.f_93623_ = !ConfigScreen.this.config.isReadOnly();
            this.eventListeners.add(this.resetButton);
        }

        protected void onResetValue() {
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.eventListeners;
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            boolean showValidationHint = this.validationHint != null;
            int trimLength = showValidationHint ? 100 : 80;
            ChatFormatting labelStyle = this.holder.isChanged() ? Config.getChangedFormatting() : ChatFormatting.RESET;
            graphics.drawString(Minecraft.getInstance().font, this.getTrimmedLabel(width - trimLength).withStyle(labelStyle), left, top + 6, 16777215);
            if (showValidationHint) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(IconButton.ICONS, left + width - 88, top + 3, 16, 16, 11.0F, 11.0F, 11, 11, 64, 64);
            }
            if (!ConfigScreen.this.config.isReadOnly() && (this.holder.requiresGameRestart() || this.holder.requiresWorldRestart())) {
                boolean gameRestart = this.holder.requiresGameRestart();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(IconButton.ICONS, left - 18, top + 5, 11, 11, gameRestart ? 51.0F : 11.0F, 22.0F, 11, 11, 64, 64);
                if (ScreenUtil.isMouseWithin(left - 18, top + 5, 11, 11, mouseX, mouseY)) {
                    String translationKey = gameRestart ? "configured.gui.requires_game_restart" : "configured.gui.requires_world_restart";
                    int outline = gameRestart ? -1438090048 : -1437158830;
                    ConfigScreen.this.setActiveTooltip(Component.translatable(translationKey), outline);
                }
            }
            if (this.m_5953_((double) mouseX, (double) mouseY)) {
                if (showValidationHint && ScreenUtil.isMouseWithin(left + width - 92, top, 23, 20, mouseX, mouseY)) {
                    ConfigScreen.this.setActiveTooltip(this.validationHint, -1428357120);
                } else if (mouseX < ConfigScreen.this.list.m_5747_() + ConfigScreen.this.list.getRowWidth() - 69) {
                    ConfigScreen.this.setActiveTooltip(this.tooltip);
                }
            }
            this.resetButton.f_93623_ = !this.holder.isDefault() && !ConfigScreen.this.config.isReadOnly();
            this.resetButton.m_252865_(left + width - 21);
            this.resetButton.m_253211_(top);
            this.resetButton.render(graphics, mouseX, mouseY, partialTicks);
        }

        private MutableComponent getTrimmedLabel(int maxWidth) {
            return ConfigScreen.this.f_96541_.font.width(this.label) > maxWidth ? Component.literal(ConfigScreen.this.f_96541_.font.substrByWidth(this.label, maxWidth).getString() + "...") : this.label.copy();
        }

        @Nullable
        private List<FormattedCharSequence> createToolTip(IConfigValue<T> holder) {
            Component comment = holder.getComment();
            if (comment == null) {
                return null;
            } else {
                Font font = Minecraft.getInstance().font;
                List<FormattedText> lines = font.getSplitter().splitLines(comment, 200, Style.EMPTY);
                String name = holder.getName();
                lines.add(0, Component.literal(name).withStyle(ChatFormatting.YELLOW));
                int rangeIndex = -1;
                for (int i = 0; i < lines.size(); i++) {
                    String text = ((FormattedText) lines.get(i)).getString();
                    if (text.startsWith("Range: ") || text.startsWith("Allowed Values: ")) {
                        rangeIndex = i;
                        break;
                    }
                }
                if (rangeIndex != -1) {
                    for (int ix = rangeIndex; ix < lines.size(); ix++) {
                        lines.set(ix, Component.literal(((FormattedText) lines.get(ix)).getString()).withStyle(ChatFormatting.GRAY));
                    }
                }
                return Language.getInstance().getVisualOrder(lines);
            }
        }

        public void setValidationHint(Component text) {
            this.validationHint = text;
        }
    }

    public class DoubleItem extends ConfigScreen.NumberItem<Double> {

        public DoubleItem(IConfigValue<Double> holder) {
            super(holder, Double::parseDouble);
        }
    }

    public class EnumItem extends ConfigScreen.ConfigItem<Enum<?>> {

        private final Button button;

        public EnumItem(IConfigValue<Enum<?>> holder) {
            super(holder);
            Component buttonText = ConfigScreen.this.config.isReadOnly() ? Component.translatable("configured.gui.view") : Component.translatable("configured.gui.change");
            this.button = ScreenUtil.button(10, 5, 46, 20, buttonText, button -> Minecraft.getInstance().setScreen(new ChangeEnumScreen(ConfigScreen.this, ConfigScreen.this.config, this.label, ConfigScreen.this.background, holder.get(), holder, e -> {
                holder.set(e);
                ConfigScreen.this.updateButtons();
            })));
            this.eventListeners.add(this.button);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            super.render(graphics, index, top, left, width, p_230432_6_, mouseX, mouseY, hovered, partialTicks);
            this.button.m_252865_(left + width - 69);
            this.button.m_253211_(top);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }
    }

    public class FolderItem extends ListMenuScreen.Item {

        private final IconButton button;

        public FolderItem(IConfigEntry entry) {
            super(createLabelForFolderEntry(entry));
            this.button = new IconButton(10, 5, 11, 33, 0, Component.literal(this.getLabel()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.WHITE), onPress -> {
                Component newTitle = ConfigScreen.this.f_96539_.copy().append(Component.literal(" > ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(this.getLabel());
                ConfigScreen.this.f_96541_.setScreen(new ConfigScreen(ConfigScreen.this, newTitle, ConfigScreen.this.config, ConfigScreen.this.background, entry));
            });
            if (entry.getTooltip() != null) {
                this.tooltip = Language.getInstance().getVisualOrder(ConfigScreen.splitTooltip(entry.getTooltip()));
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.button);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean selected, float partialTicks) {
            super.render(graphics, index, top, left, width, height, mouseX, mouseY, selected, partialTicks);
            this.button.m_252865_(left - 1);
            this.button.m_253211_(top);
            this.button.m_93674_(width);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }

        private static Component createLabelForFolderEntry(IConfigEntry entry) {
            String key = entry.getTranslationKey();
            return key != null && I18n.exists(key) ? Component.translatable(key) : Component.literal(ConfigScreen.createLabel(entry.getEntryName()));
        }
    }

    public class IntegerItem extends ConfigScreen.NumberItem<Integer> {

        public IntegerItem(IConfigValue<Integer> holder) {
            super(holder, Integer::parseInt);
        }
    }

    public class ListItem extends ConfigScreen.ConfigItem<List<?>> {

        private final Button button;

        public ListItem(IConfigValue<List<?>> holder) {
            super(holder);
            Component buttonText = ConfigScreen.this.config.isReadOnly() ? Component.translatable("configured.gui.view") : Component.translatable("configured.gui.edit");
            this.button = ScreenUtil.button(10, 5, 46, 20, buttonText, button -> Minecraft.getInstance().setScreen(new EditListScreen<>(ConfigScreen.this, ConfigScreen.this.config, this.label, holder, ConfigScreen.this.background)));
            this.eventListeners.add(this.button);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            super.render(graphics, index, top, left, width, p_230432_6_, mouseX, mouseY, hovered, partialTicks);
            this.button.m_252865_(left + width - 69);
            this.button.m_253211_(top);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }
    }

    public class LongItem extends ConfigScreen.NumberItem<Long> {

        public LongItem(IConfigValue<Long> holder) {
            super(holder, Long::parseLong);
        }
    }

    public abstract class NumberItem<T extends Number> extends ConfigScreen.ConfigItem<T> {

        private final ListMenuScreen.FocusedEditBox textField;

        private long lastTick;

        public NumberItem(IConfigValue<T> holder, Function<String, Number> parser) {
            super(holder);
            String text = (String) ConfigScreen.this.cachedTextMap.getOrDefault(holder.getName(), holder.get().toString());
            this.textField = ConfigScreen.this.new FocusedEditBox(ConfigScreen.this.f_96547_, 0, 0, 44, 18, this.label);
            this.textField.m_94151_(s -> {
                ConfigScreen.this.cachedTextMap.put(holder.getName(), s);
                try {
                    Number n = (Number) parser.apply(s);
                    if (holder.isValid((T) n)) {
                        this.textField.m_94202_(14737632);
                        holder.set((T) n);
                        ConfigScreen.this.updateButtons();
                        this.setValidationHint(null);
                    } else {
                        this.textField.m_94202_(16711680);
                        this.setValidationHint(holder.getValidationHint());
                    }
                } catch (Exception var5) {
                    this.textField.m_94202_(16711680);
                    this.setValidationHint(Component.translatable("configured.validator.not_a_number"));
                }
            });
            this.textField.m_94144_(text);
            this.textField.m_94186_(!ConfigScreen.this.config.isReadOnly());
            this.eventListeners.add(this.textField);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            super.render(graphics, index, top, left, width, p_230432_6_, mouseX, mouseY, hovered, partialTicks);
            long time = Util.getMillis();
            if (time - this.lastTick >= 50L) {
                this.textField.m_94120_();
                this.lastTick = time;
            }
            this.textField.m_252865_(left + width - 68);
            this.textField.m_253211_(top + 1);
            this.textField.render(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public void onResetValue() {
            this.textField.m_94144_(this.holder.get().toString());
        }
    }

    public class StringItem extends ConfigScreen.ConfigItem<String> {

        private final Button button;

        public StringItem(IConfigValue<String> holder) {
            super(holder);
            Component buttonText = ConfigScreen.this.config.isReadOnly() ? Component.translatable("configured.gui.view") : Component.translatable("configured.gui.edit");
            this.button = ScreenUtil.button(10, 5, 46, 20, buttonText, button -> Minecraft.getInstance().setScreen(new EditStringScreen(ConfigScreen.this, ConfigScreen.this.config, ConfigScreen.this.background, this.label, holder.get(), s -> holder.isValid(s) ? Pair.of(true, CommonComponents.EMPTY) : Pair.of(false, holder.getValidationHint()), s -> {
                holder.set(s);
                ConfigScreen.this.updateButtons();
            })));
            this.eventListeners.add(this.button);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            super.render(graphics, index, top, left, width, p_230432_6_, mouseX, mouseY, hovered, partialTicks);
            this.button.m_252865_(left + width - 69);
            this.button.m_253211_(top);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }
    }
}