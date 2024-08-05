package com.simibubi.create.foundation.config.ui;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.TextStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class BaseConfigScreen extends ConfigScreen {

    public static final DelegatedStencilElement.ElementRenderer DISABLED_RENDERER = (ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.BUTTON_DISABLE));

    private static final Map<String, UnaryOperator<BaseConfigScreen>> DEFAULTS = new HashMap();

    BoxWidget clientConfigWidget;

    BoxWidget commonConfigWidget;

    BoxWidget serverConfigWidget;

    BoxWidget goBack;

    BoxWidget others;

    BoxWidget title;

    ForgeConfigSpec clientSpec;

    ForgeConfigSpec commonSpec;

    ForgeConfigSpec serverSpec;

    String clientTitle = "Client Config";

    String commonTitle = "Common Config";

    String serverTitle = "Server Config";

    String modID;

    protected boolean returnOnClose;

    public static void setDefaultActionFor(String modID, UnaryOperator<BaseConfigScreen> transform) {
        if (!modID.equals("create")) {
            DEFAULTS.put(modID, transform);
        }
    }

    public static BaseConfigScreen forCreate(Screen parent) {
        return new BaseConfigScreen(parent);
    }

    public BaseConfigScreen(Screen parent, @Nonnull String modID) {
        super(parent);
        this.modID = modID;
        if (DEFAULTS.containsKey(modID)) {
            ((UnaryOperator) DEFAULTS.get(modID)).apply(this);
        } else {
            this.searchForSpecsInModContainer();
        }
    }

    private BaseConfigScreen(Screen parent) {
        this(parent, "create");
    }

    public BaseConfigScreen searchForSpecsInModContainer() {
        if (!ConfigHelper.hasAnyForgeConfig(this.modID)) {
            return this;
        } else {
            try {
                this.clientSpec = ConfigHelper.findForgeConfigSpecFor(Type.CLIENT, this.modID);
            } catch (Exception var4) {
                Create.LOGGER.debug("Unable to find ClientConfigSpec for mod: " + this.modID);
            }
            try {
                this.commonSpec = ConfigHelper.findForgeConfigSpecFor(Type.COMMON, this.modID);
            } catch (Exception var3) {
                Create.LOGGER.debug("Unable to find CommonConfigSpec for mod: " + this.modID);
            }
            try {
                this.serverSpec = ConfigHelper.findForgeConfigSpecFor(Type.SERVER, this.modID);
            } catch (Exception var2) {
                Create.LOGGER.debug("Unable to find ServerConfigSpec for mod: " + this.modID);
            }
            return this;
        }
    }

    public BaseConfigScreen withSpecs(@Nullable ForgeConfigSpec client, @Nullable ForgeConfigSpec common, @Nullable ForgeConfigSpec server) {
        this.clientSpec = client;
        this.commonSpec = common;
        this.serverSpec = server;
        return this;
    }

    public BaseConfigScreen withTitles(@Nullable String client, @Nullable String common, @Nullable String server) {
        if (client != null) {
            this.clientTitle = client;
        }
        if (common != null) {
            this.commonTitle = common;
        }
        if (server != null) {
            this.serverTitle = server;
        }
        return this;
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.returnOnClose = true;
        TextStencilElement clientText = new TextStencilElement(this.f_96547_, Components.literal(this.clientTitle)).centered(true, true);
        this.m_142416_(this.clientConfigWidget = new BoxWidget(this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 15 - 30, 200, 16).showingElement(clientText));
        if (this.clientSpec != null) {
            this.clientConfigWidget.withCallback(() -> this.linkTo(new SubMenuConfigScreen(this, Type.CLIENT, this.clientSpec)));
            clientText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.clientConfigWidget));
        } else {
            this.clientConfigWidget.f_93623_ = false;
            this.clientConfigWidget.updateColorsFromState();
            clientText.withElementRenderer(DISABLED_RENDERER);
        }
        TextStencilElement commonText = new TextStencilElement(this.f_96547_, Components.literal(this.commonTitle)).centered(true, true);
        this.m_142416_(this.commonConfigWidget = new BoxWidget(this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 15, 200, 16).showingElement(commonText));
        if (this.commonSpec != null) {
            this.commonConfigWidget.withCallback(() -> this.linkTo(new SubMenuConfigScreen(this, Type.COMMON, this.commonSpec)));
            commonText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.commonConfigWidget));
        } else {
            this.commonConfigWidget.f_93623_ = false;
            this.commonConfigWidget.updateColorsFromState();
            commonText.withElementRenderer(DISABLED_RENDERER);
        }
        TextStencilElement serverText = new TextStencilElement(this.f_96547_, Components.literal(this.serverTitle)).centered(true, true);
        this.m_142416_(this.serverConfigWidget = new BoxWidget(this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 15 + 30, 200, 16).showingElement(serverText));
        if (this.serverSpec == null) {
            this.serverConfigWidget.f_93623_ = false;
            this.serverConfigWidget.updateColorsFromState();
            serverText.withElementRenderer(DISABLED_RENDERER);
        } else if (this.f_96541_.level == null) {
            serverText.withElementRenderer(DISABLED_RENDERER);
            this.serverConfigWidget.getToolTip().add(Components.literal("Stored individually per World"));
            this.serverConfigWidget.getToolTip().addAll(TooltipHelper.cutTextComponent(Components.literal("Gameplay settings can only be accessed from the in-game menu after joining a World or Server."), TooltipHelper.Palette.ALL_GRAY));
        } else {
            this.serverConfigWidget.withCallback(() -> this.linkTo(new SubMenuConfigScreen(this, Type.SERVER, this.serverSpec)));
            serverText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.serverConfigWidget));
        }
        TextStencilElement titleText = new TextStencilElement(this.f_96547_, this.modID.toUpperCase(Locale.ROOT)).centered(true, true).withElementRenderer((ms, w, h, alpha) -> {
            UIRenderHelper.angledGradient(ms, 0.0F, 0, h / 2, h, w / 2, Theme.p(Theme.Key.CONFIG_TITLE_A));
            UIRenderHelper.angledGradient(ms, 0.0F, w / 2, h / 2, h, w / 2, Theme.p(Theme.Key.CONFIG_TITLE_B));
        });
        int boxWidth = this.f_96543_ + 10;
        int boxHeight = 39;
        int boxPadding = 4;
        this.title = new BoxWidget(-5, this.f_96544_ / 2 - 110, boxWidth, boxHeight).<BoxWidget>withBorderColors(Theme.p(Theme.Key.BUTTON_IDLE)).<ElementWidget>withPadding(0.0F, (float) boxPadding).<ElementWidget>rescaleElement((float) boxWidth / 2.0F, (float) (boxHeight - 2 * boxPadding) / 2.0F).showingElement(titleText.at(0.0F, 7.0F));
        this.title.f_93623_ = false;
        this.m_142416_(this.title);
        ConfigScreen.modID = this.modID;
        this.goBack = new BoxWidget(this.f_96543_ / 2 - 134, this.f_96544_ / 2, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).withCallback(() -> this.linkTo(this.parent));
        this.goBack.showingElement(AllIcons.I_CONFIG_BACK.asStencil().withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.goBack)));
        this.goBack.getToolTip().add(Components.literal("Go Back"));
        this.m_142416_(this.goBack);
        TextStencilElement othersText = new TextStencilElement(this.f_96547_, Components.literal("Access Configs of other Mods")).centered(true, true);
        this.others = new BoxWidget(this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 15 + 90, 200, 16).showingElement(othersText);
        othersText.withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.others));
        this.others.withCallback(() -> this.linkTo(new ConfigModListScreen(this)));
        this.m_142416_(this.others);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.drawCenteredString(this.f_96547_, "Access Configs for Mod:", this.f_96543_ / 2, this.f_96544_ / 2 - 105, Theme.i(Theme.Key.TEXT_ACCENT_STRONG));
    }

    private void linkTo(Screen screen) {
        this.returnOnClose = false;
        ScreenOpener.open(screen);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.m_7933_(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            if (keyCode == 259) {
                this.linkTo(this.parent);
            }
            return false;
        }
    }

    static {
        DEFAULTS.put("create", (UnaryOperator) base -> base.withTitles("Client Settings", "World Generation Settings", "Gameplay Settings").withSpecs(AllConfigs.client().specification, AllConfigs.common().specification, AllConfigs.server().specification));
    }
}