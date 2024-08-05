package com.simibubi.create.foundation.config.ui;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.common.collect.Lists;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.config.ui.entries.BooleanEntry;
import com.simibubi.create.foundation.config.ui.entries.EnumEntry;
import com.simibubi.create.foundation.config.ui.entries.NumberEntry;
import com.simibubi.create.foundation.config.ui.entries.SubMenuEntry;
import com.simibubi.create.foundation.config.ui.entries.ValueEntry;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ConfirmationScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class SubMenuConfigScreen extends ConfigScreen {

    public final Type type;

    protected ForgeConfigSpec spec;

    protected UnmodifiableConfig configGroup;

    protected ConfigScreenList list;

    protected BoxWidget resetAll;

    protected BoxWidget saveChanges;

    protected BoxWidget discardChanges;

    protected BoxWidget goBack;

    protected BoxWidget serverLocked;

    protected HintableTextFieldWidget search;

    protected int listWidth;

    protected String title;

    protected Set<String> highlights = new HashSet();

    public static SubMenuConfigScreen find(ConfigHelper.ConfigPath path) {
        ForgeConfigSpec spec = ConfigHelper.findForgeConfigSpecFor(path.getType(), path.getModID());
        UnmodifiableConfig values = spec.getValues();
        BaseConfigScreen base = new BaseConfigScreen(null, path.getModID());
        SubMenuConfigScreen screen = new SubMenuConfigScreen(base, "root", path.getType(), spec, values);
        List<String> remainingPath = Lists.newArrayList(path.getPath());
        label25: while (!remainingPath.isEmpty()) {
            String next = (String) remainingPath.remove(0);
            for (Entry<String, Object> entry : values.valueMap().entrySet()) {
                String key = (String) entry.getKey();
                Object obj = entry.getValue();
                if (key.equalsIgnoreCase(next)) {
                    if (obj instanceof AbstractConfig) {
                        values = (UnmodifiableConfig) obj;
                        screen = new SubMenuConfigScreen(screen, toHumanReadable(key), path.getType(), spec, values);
                        continue label25;
                    }
                    screen.highlights.add(path.getPath()[path.getPath().length - 1]);
                }
            }
            break;
        }
        ConfigScreen.modID = path.getModID();
        return screen;
    }

    public SubMenuConfigScreen(Screen parent, String title, Type type, ForgeConfigSpec configSpec, UnmodifiableConfig configGroup) {
        super(parent);
        this.type = type;
        this.spec = configSpec;
        this.title = title;
        this.configGroup = configGroup;
    }

    public SubMenuConfigScreen(Screen parent, Type type, ForgeConfigSpec configSpec) {
        super(parent);
        this.type = type;
        this.spec = configSpec;
        this.title = "root";
        this.configGroup = configSpec.getValues();
    }

    protected void clearChanges() {
        ConfigHelper.changes.clear();
        this.list.m_6702_().stream().filter(e -> e instanceof ValueEntry).forEach(e -> ((ValueEntry) e).onValueChange());
    }

    protected void saveChanges() {
        UnmodifiableConfig values = this.spec.getValues();
        ConfigHelper.changes.forEach((path, change) -> {
            ForgeConfigSpec.ConfigValue<Object> configValue = (ForgeConfigSpec.ConfigValue<Object>) values.get(path);
            configValue.set(change.value);
            if (this.type == Type.SERVER) {
                AllPackets.getChannel().sendToServer(new CConfigureConfigPacket<>(ConfigScreen.modID, path, change.value));
            }
            String command = (String) change.annotations.get("Execute");
            if (this.f_96541_.player != null && command != null && command.startsWith("/")) {
                this.f_96541_.player.connection.sendCommand(command.substring(1));
            }
        });
        this.clearChanges();
    }

    protected void resetConfig(UnmodifiableConfig values) {
        values.valueMap().forEach((key, obj) -> {
            if (obj instanceof AbstractConfig) {
                this.resetConfig((UnmodifiableConfig) obj);
            } else if (obj instanceof ForgeConfigSpec.ConfigValue<Object> configValue) {
                ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) this.spec.getRaw(configValue.getPath());
                List<String> comments = new ArrayList();
                if (valueSpec.getComment() != null) {
                    comments.addAll(Arrays.asList(valueSpec.getComment().split("\n")));
                }
                Pair<String, Map<String, String>> metadata = ConfigHelper.readMetadataFromComment(comments);
                ConfigHelper.setValue(String.join(".", configValue.getPath()), configValue, valueSpec.getDefault(), metadata.getSecond());
            }
        });
        this.list.m_6702_().stream().filter(e -> e instanceof ValueEntry).forEach(e -> ((ValueEntry) e).onValueChange());
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.listWidth = Math.min(this.f_96543_ - 80, 300);
        int yCenter = this.f_96544_ / 2;
        int listL = this.f_96543_ / 2 - this.listWidth / 2;
        int listR = this.f_96543_ / 2 + this.listWidth / 2;
        this.resetAll = new BoxWidget(listR + 10, yCenter - 25, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).withCallback((x, y) -> new ConfirmationScreen().centered().withText(FormattedText.of("Resetting all settings of the " + this.type.toString() + " config. Are you sure?")).withAction(success -> {
            if (success) {
                this.resetConfig(this.spec.getValues());
            }
        }).open(this));
        this.resetAll.showingElement(AllIcons.I_CONFIG_RESET.asStencil().withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.resetAll)));
        this.resetAll.getToolTip().add(Components.literal("Reset All"));
        this.resetAll.getToolTip().addAll(TooltipHelper.cutStringTextComponent("Click here to reset all settings to their default value.", TooltipHelper.Palette.ALL_GRAY));
        this.saveChanges = new BoxWidget(listL - 30, yCenter - 25, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).withCallback((x, y) -> {
            if (!ConfigHelper.changes.isEmpty()) {
                ConfirmationScreen confirm = new ConfirmationScreen().centered().withText(FormattedText.of("Saving " + ConfigHelper.changes.size() + " changed value" + (ConfigHelper.changes.size() != 1 ? "s" : ""))).withAction(success -> {
                    if (success) {
                        this.saveChanges();
                    }
                });
                this.addAnnotationsToConfirm(confirm).open(this);
            }
        });
        this.saveChanges.showingElement(AllIcons.I_CONFIG_SAVE.asStencil().withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.saveChanges)));
        this.saveChanges.getToolTip().add(Components.literal("Save Changes"));
        this.saveChanges.getToolTip().addAll(TooltipHelper.cutStringTextComponent("Click here to save your current changes.", TooltipHelper.Palette.ALL_GRAY));
        this.discardChanges = new BoxWidget(listL - 30, yCenter + 5, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).withCallback((x, y) -> {
            if (!ConfigHelper.changes.isEmpty()) {
                new ConfirmationScreen().centered().withText(FormattedText.of("Discarding " + ConfigHelper.changes.size() + " unsaved change" + (ConfigHelper.changes.size() != 1 ? "s" : ""))).withAction(success -> {
                    if (success) {
                        this.clearChanges();
                    }
                }).open(this);
            }
        });
        this.discardChanges.showingElement(AllIcons.I_CONFIG_DISCARD.asStencil().withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.discardChanges)));
        this.discardChanges.getToolTip().add(Components.literal("Discard Changes"));
        this.discardChanges.getToolTip().addAll(TooltipHelper.cutStringTextComponent("Click here to discard all the changes you made.", TooltipHelper.Palette.ALL_GRAY));
        this.goBack = new BoxWidget(listL - 30, yCenter + 65, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).withCallback(this::attemptBackstep);
        this.goBack.showingElement(AllIcons.I_CONFIG_BACK.asStencil().withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.goBack)));
        this.goBack.getToolTip().add(Components.literal("Go Back"));
        this.m_142416_(this.resetAll);
        this.m_142416_(this.saveChanges);
        this.m_142416_(this.discardChanges);
        this.m_142416_(this.goBack);
        this.list = new ConfigScreenList(this.f_96541_, this.listWidth, this.f_96544_ - 80, 35, this.f_96544_ - 45, 40);
        this.list.m_93507_(this.f_96543_ / 2 - this.list.getWidth() / 2);
        this.m_142416_(this.list);
        this.search = new ConfigTextField(this.f_96547_, this.f_96543_ / 2 - this.listWidth / 2, this.f_96544_ - 35, this.listWidth, 20);
        this.search.m_94151_(this::updateFilter);
        this.search.setHint("Search...");
        this.search.m_94198_();
        this.m_142416_(this.search);
        this.configGroup.valueMap().forEach((key, obj) -> {
            String humanKey = toHumanReadable(key);
            if (obj instanceof AbstractConfig) {
                SubMenuEntry entry = new SubMenuEntry(this, humanKey, this.spec, (UnmodifiableConfig) obj);
                entry.path = key;
                this.list.m_6702_().add(entry);
                if (this.configGroup.valueMap().size() == 1) {
                    ScreenOpener.open(new SubMenuConfigScreen(this.parent, humanKey, this.type, this.spec, (UnmodifiableConfig) obj));
                }
            } else if (obj instanceof ForgeConfigSpec.ConfigValue<?> configValue) {
                ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) this.spec.getRaw(configValue.getPath());
                Object value = configValue.get();
                ConfigScreenList.Entry entry = null;
                if (value instanceof Boolean) {
                    entry = new BooleanEntry(humanKey, (ForgeConfigSpec.ConfigValue<Boolean>) configValue, valueSpec);
                } else if (value instanceof Enum) {
                    entry = new EnumEntry(humanKey, (ForgeConfigSpec.ConfigValue<Enum<?>>) configValue, valueSpec);
                } else if (value instanceof Number) {
                    entry = NumberEntry.create(value, humanKey, configValue, valueSpec);
                }
                if (entry == null) {
                    entry = new ConfigScreenList.LabeledEntry("Impl missing - " + configValue.get().getClass().getSimpleName() + "  " + humanKey + " : " + value);
                }
                if (this.highlights.contains(key)) {
                    entry.annotations.put("highlight", ":)");
                }
                this.list.m_6702_().add(entry);
            }
        });
        Collections.sort(this.list.m_6702_(), (e, e2) -> {
            int group = (e2 instanceof SubMenuEntry ? 1 : 0) - (e instanceof SubMenuEntry ? 1 : 0);
            if (group == 0 && e instanceof ConfigScreenList.LabeledEntry && e2 instanceof ConfigScreenList.LabeledEntry) {
                ConfigScreenList.LabeledEntry le = (ConfigScreenList.LabeledEntry) e;
                ConfigScreenList.LabeledEntry le2 = (ConfigScreenList.LabeledEntry) e2;
                return le.label.getComponent().getString().compareTo(le2.label.getComponent().getString());
            } else {
                return group;
            }
        });
        this.list.search((String) this.highlights.stream().findFirst().orElse(""));
        if (this.type == Type.SERVER) {
            if (!this.f_96541_.hasSingleplayerServer()) {
                boolean canEdit = this.f_96541_ != null && this.f_96541_.player != null && this.f_96541_.player.m_20310_(2);
                Couple<Color> red = Theme.p(Theme.Key.BUTTON_FAIL);
                Couple<Color> green = Theme.p(Theme.Key.BUTTON_SUCCESS);
                DelegatedStencilElement stencil = new DelegatedStencilElement();
                this.serverLocked = new BoxWidget(listR + 10, yCenter + 5, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).showingElement(stencil);
                if (!canEdit) {
                    this.list.m_6702_().forEach(e -> e.setEditable(false));
                    this.resetAll.f_93623_ = false;
                    stencil.withStencilRenderer((ms, w, h, alpha) -> AllIcons.I_CONFIG_LOCKED.render(ms, 0, 0));
                    stencil.withElementRenderer((ms, w, h, alpha) -> UIRenderHelper.angledGradient(ms, 90.0F, 8, 0, 16, 16, red));
                    this.serverLocked.withBorderColors(red);
                    this.serverLocked.getToolTip().add(Components.literal("Locked").withStyle(ChatFormatting.BOLD));
                    this.serverLocked.getToolTip().addAll(TooltipHelper.cutStringTextComponent("You do not have enough permissions to edit the server config. You can still look at the current values here though.", TooltipHelper.Palette.ALL_GRAY));
                } else {
                    stencil.withStencilRenderer((ms, w, h, alpha) -> AllIcons.I_CONFIG_UNLOCKED.render(ms, 0, 0));
                    stencil.withElementRenderer((ms, w, h, alpha) -> UIRenderHelper.angledGradient(ms, 90.0F, 8, 0, 16, 16, green));
                    this.serverLocked.withBorderColors(green);
                    this.serverLocked.getToolTip().add(Components.literal("Unlocked").withStyle(ChatFormatting.BOLD));
                    this.serverLocked.getToolTip().addAll(TooltipHelper.cutStringTextComponent("You have enough permissions to edit the server config. Changes you make here will be synced with the server when you save them.", TooltipHelper.Palette.ALL_GRAY));
                }
                this.m_142416_(this.serverLocked);
            }
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWindow(graphics, mouseX, mouseY, partialTicks);
        int x = this.f_96543_ / 2;
        graphics.drawCenteredString(this.f_96541_.font, ConfigScreen.modID + " > " + this.type.toString().toLowerCase(Locale.ROOT) + " > " + this.title, x, 15, Theme.i(Theme.Key.TEXT));
    }

    @Override
    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWindowForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(@Nonnull Minecraft client, int width, int height) {
        double scroll = this.list.m_93517_();
        this.m_6575_(client, width, height);
        this.list.m_93410_(scroll);
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return (GuiEventListener) (ConfigScreenList.currentText != null ? ConfigScreenList.currentText : super.m_7222_());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.m_7933_(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            if (Screen.hasControlDown() && keyCode == 70) {
                this.search.m_93692_(true);
            }
            if (keyCode == 259) {
                this.attemptBackstep();
            }
            return false;
        }
    }

    private void updateFilter(String search) {
        if (this.list.search(search)) {
            this.search.m_94202_(Theme.i(Theme.Key.TEXT));
        } else {
            this.search.m_94202_(Theme.i(Theme.Key.BUTTON_FAIL));
        }
    }

    private void attemptBackstep() {
        if (!ConfigHelper.changes.isEmpty() && this.parent instanceof BaseConfigScreen) {
            this.showLeavingPrompt(success -> {
                if (success != ConfirmationScreen.Response.Cancel) {
                    if (success == ConfirmationScreen.Response.Confirm) {
                        this.saveChanges();
                    }
                    ConfigHelper.changes.clear();
                    ScreenOpener.open(this.parent);
                }
            });
        } else {
            ScreenOpener.open(this.parent);
        }
    }

    @Override
    public void onClose() {
        if (ConfigHelper.changes.isEmpty()) {
            super.m_7379_();
        } else {
            this.showLeavingPrompt(success -> {
                if (success != ConfirmationScreen.Response.Cancel) {
                    if (success == ConfirmationScreen.Response.Confirm) {
                        this.saveChanges();
                    }
                    ConfigHelper.changes.clear();
                    super.m_7379_();
                }
            });
        }
    }

    public void showLeavingPrompt(Consumer<ConfirmationScreen.Response> action) {
        ConfirmationScreen screen = new ConfirmationScreen().centered().withThreeActions(action).addText(FormattedText.of("Leaving with " + ConfigHelper.changes.size() + " unsaved change" + (ConfigHelper.changes.size() != 1 ? "s" : "") + " for this config"));
        this.addAnnotationsToConfirm(screen).open(this);
    }

    private ConfirmationScreen addAnnotationsToConfirm(ConfirmationScreen screen) {
        AtomicBoolean relog = new AtomicBoolean(false);
        AtomicBoolean restart = new AtomicBoolean(false);
        ConfigHelper.changes.values().forEach(change -> {
            if (change.annotations.containsKey(ConfigAnnotations.RequiresRelog.TRUE.getName())) {
                relog.set(true);
            }
            if (change.annotations.containsKey(ConfigAnnotations.RequiresRestart.CLIENT.getName())) {
                restart.set(true);
            }
        });
        if (relog.get()) {
            screen.addText(FormattedText.of(" "));
            screen.addText(FormattedText.of("At least one changed value will require you to relog to take full effect"));
        }
        if (restart.get()) {
            screen.addText(FormattedText.of(" "));
            screen.addText(FormattedText.of("At least one changed value will require you to restart your game to take full effect"));
        }
        return screen;
    }
}