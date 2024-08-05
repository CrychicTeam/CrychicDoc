package snownee.jade.gui;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import java.io.File;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import snownee.jade.Jade;
import snownee.jade.JadeClient;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.theme.Theme;
import snownee.jade.gui.config.OptionButton;
import snownee.jade.gui.config.OptionsList;
import snownee.jade.gui.config.value.OptionValue;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.impl.config.WailaConfig;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.CommonProxy;

public class WailaConfigScreen extends BaseOptionsScreen {

    private OptionValue<Boolean> squareEntry;

    private OptionValue<Float> opacityEntry;

    public WailaConfigScreen(Screen parent) {
        super(parent, Component.translatable("gui.jade.jade_settings"));
        this.saver = Jade.CONFIG::save;
        Builder<KeyMapping, InputConstants.Key> keyMapBuilder = ImmutableMap.builder();
        for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
            if (JadeClient.openConfig.getCategory().equals(keyMapping.getCategory())) {
                keyMapBuilder.put(keyMapping, ClientProxy.getBoundKeyOf(keyMapping));
            }
        }
        ImmutableMap<KeyMapping, InputConstants.Key> keyMap = keyMapBuilder.build();
        this.canceller = () -> {
            Jade.CONFIG.invalidate();
            keyMap.forEach(KeyMapping::m_90848_);
            Minecraft.getInstance().options.save();
        };
    }

    public static OptionsList.Entry editBlocklist(OptionsList.Entry entry, String fileName, Runnable defaultCreator) {
        entry.getFirstWidget().setWidth(79);
        MutableComponent tooltip = Component.translatable("config.jade.edit_blocklist");
        entry.addWidget(Button.builder(Component.literal("☰"), b -> {
            File file = new File(CommonProxy.getConfigDirectory(), "jade/%s.json".formatted(fileName));
            if (!file.exists()) {
                defaultCreator.run();
            }
            Util.getPlatform().openFile(file);
        }).size(20, 20).tooltip(Tooltip.create(tooltip)).createNarration($ -> tooltip).build(), 80);
        return entry;
    }

    @Override
    public OptionsList createOptions() {
        Objects.requireNonNull(this.f_96541_);
        OptionsList options = new OptionsList(this, this.f_96541_, this.f_96543_ - 120, this.f_96544_, 0, this.f_96544_ - 32, 26, Jade.CONFIG::save);
        WailaConfig.ConfigGeneral general = Jade.CONFIG.get().getGeneral();
        options.title("general");
        if (CommonProxy.isDevEnv()) {
            options.choices("debug_mode", general.isDebug(), general::setDebug);
        }
        options.choices("display_tooltip", general.shouldDisplayTooltip(), general::setDisplayTooltip);
        OptionsList.Entry entry = options.choices("display_entities", general.getDisplayEntities(), general::setDisplayEntities);
        editBlocklist(entry, "hide-entities", () -> WailaClientRegistration.createEntityBlocklist().get());
        options.choices("display_bosses", general.getDisplayBosses(), general::setDisplayBosses).parent(entry);
        entry = options.choices("display_blocks", general.getDisplayBlocks(), general::setDisplayBlocks);
        editBlocklist(entry, "hide-blocks", () -> WailaClientRegistration.createBlockBlocklist().get());
        options.choices("display_fluids", general.getDisplayFluids(), general::setDisplayFluids).parent(entry);
        options.choices("display_mode", general.getDisplayMode(), general::setDisplayMode, builder -> builder.withTooltip(mode -> {
            String key = "display_mode_" + mode.name().toLowerCase(Locale.ENGLISH) + "_desc";
            if (mode == IWailaConfig.DisplayMode.LITE && "fabric".equals(CommonProxy.getPlatformIdentifier())) {
                key = key + ".fabric";
            }
            return Tooltip.create(OptionsList.Entry.makeTitle(key));
        }));
        OptionValue<?> value = options.choices("item_mod_name", general.showItemModNameTooltip(), general::setItemModNameTooltip);
        if (!WailaConfig.ConfigGeneral.itemModNameTooltipDisabledByMods.isEmpty()) {
            value.setDisabled(true);
            value.appendDescription(I18n.get("gui.jade.disabled_by_mods"));
            WailaConfig.ConfigGeneral.itemModNameTooltipDisabledByMods.forEach(value::appendDescription);
            if (value.getFirstWidget() != null && value.getDescription() != null) {
                value.getFirstWidget().setTooltip(Tooltip.create(Component.literal(value.getDescription())));
            }
        }
        options.choices("hide_from_debug", general.shouldHideFromDebug(), general::setHideFromDebug);
        options.choices("hide_from_tab_list", general.shouldHideFromTabList(), general::setHideFromTabList);
        options.choices("boss_bar_overlap", general.getBossBarOverlapMode(), general::setBossBarOverlapMode);
        options.slider("reach_distance", general.getReachDistance(), general::setReachDistance, 0.0F, 20.0F, f -> (float) Mth.floor(f * 2.0F) / 2.0F);
        WailaConfig.ConfigOverlay overlay = Jade.CONFIG.get().getOverlay();
        options.title("overlay");
        options.choices("overlay_theme", overlay.getTheme().id, IThemeHelper.get().getThemes().stream().filter($ -> !$.hidden).map($ -> $.id).toList(), id -> {
            if (!Objects.equals(id, overlay.getTheme().id)) {
                overlay.applyTheme(id);
                Theme theme = overlay.getTheme();
                if (theme.squareBorder != null) {
                    this.squareEntry.setValue(theme.squareBorder);
                }
                if (theme.opacity != 0.0F) {
                    this.opacityEntry.setValue(theme.opacity);
                }
            }
        }, id -> Component.translatable(Util.makeDescriptionId("jade.theme", id)));
        this.squareEntry = options.choices("overlay_square", overlay.getSquare(), overlay::setSquare);
        this.opacityEntry = options.slider("overlay_alpha", overlay.getAlpha(), overlay::setAlpha);
        options.forcePreview.add(options.slider("overlay_scale", overlay.getOverlayScale(), overlay::setOverlayScale, 0.2F, 2.0F, FloatUnaryOperator.identity()));
        options.forcePreview.add(entry = options.slider("overlay_pos_x", overlay.getOverlayPosX(), overlay::setOverlayPosX));
        options.forcePreview.add(options.slider("overlay_anchor_x", overlay.getAnchorX(), overlay::setAnchorX).parent(entry));
        options.forcePreview.add(entry = options.slider("overlay_pos_y", overlay.getOverlayPosY(), overlay::setOverlayPosY));
        options.forcePreview.add(options.slider("overlay_anchor_y", overlay.getAnchorY(), overlay::setAnchorY).parent(entry));
        options.choices("display_item", overlay.getIconMode(), overlay::setIconMode);
        options.choices("animation", overlay.getAnimation(), overlay::setAnimation);
        options.title("key_binds");
        options.keybind(JadeClient.openConfig);
        options.keybind(JadeClient.showOverlay);
        options.keybind(JadeClient.toggleLiquid);
        if (ClientProxy.shouldRegisterRecipeViewerKeys()) {
            options.keybind(JadeClient.showRecipes);
            options.keybind(JadeClient.showUses);
        }
        options.keybind(JadeClient.narrate);
        options.keybind(JadeClient.showDetails);
        options.title("accessibility");
        options.choices("flip_main_hand", overlay.getFlipMainHand(), overlay::setFlipMainHand);
        options.choices("tts_mode", general.getTTSMode(), general::setTTSMode);
        options.title("danger_zone").withStyle(ChatFormatting.RED);
        Component reset = Component.translatable("controls.reset").withStyle(ChatFormatting.RED);
        Component title = Component.translatable(OptionsList.Entry.makeKey("reset_settings")).withStyle(ChatFormatting.RED);
        options.add(new OptionButton(title, Button.builder(reset, w -> this.f_96541_.setScreen(new ConfirmScreen(bl -> {
            if (bl) {
                for (KeyMapping keyMapping : this.f_96541_.options.keyMappings) {
                    if (JadeClient.openConfig.getCategory().equals(keyMapping.getCategory())) {
                        keyMapping.setKey(keyMapping.getDefaultKey());
                    }
                }
                this.f_96541_.options.save();
                try {
                    int themesHash = Jade.CONFIG.get().getOverlay().themesHash;
                    Preconditions.checkState(Jade.CONFIG.getFile().delete());
                    Preconditions.checkState(PluginConfig.INSTANCE.getFile().delete());
                    Jade.CONFIG.invalidate();
                    Jade.CONFIG.get().getOverlay().themesHash = themesHash;
                    Jade.CONFIG.save();
                    PluginConfig.INSTANCE.reload();
                    this.m_232761_();
                } catch (Throwable var6x) {
                    Jade.LOGGER.catching(var6x);
                }
            }
            this.f_96541_.setScreen(this);
            this.options.setScrollAmount((double) this.options.m_93518_());
        }, title, Component.translatable(OptionsList.Entry.makeKey("reset_settings.confirm")), reset, Component.translatable("gui.cancel")))).size(100, 20).build()));
        return options;
    }
}