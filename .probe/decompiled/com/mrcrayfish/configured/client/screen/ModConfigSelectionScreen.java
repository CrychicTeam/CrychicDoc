package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.Environment;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.SessionData;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import com.mrcrayfish.configured.platform.Services;
import com.mrcrayfish.configured.util.ConfigHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.io.FilenameUtils;

public class ModConfigSelectionScreen extends ListMenuScreen {

    private final Map<ConfigType, Set<IModConfig>> configMap;

    public ModConfigSelectionScreen(Screen parent, Component title, ResourceLocation background, Map<ConfigType, Set<IModConfig>> configMap) {
        super(parent, title, background, 30);
        this.configMap = configMap;
    }

    @Override
    protected void constructEntries(List<ListMenuScreen.Item> entries) {
        Set<IModConfig> localConfigs = this.getLocalConfigs();
        if (!localConfigs.isEmpty()) {
            entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.title.client_configuration").getString()));
            List<ListMenuScreen.Item> localEntries = new ArrayList();
            localConfigs.forEach(config -> localEntries.add(new ModConfigSelectionScreen.FileItem(config)));
            Collections.sort(localEntries);
            entries.addAll(localEntries);
        }
        Set<IModConfig> remoteConfigs = this.getRemoteConfigs();
        if (!remoteConfigs.isEmpty() && (!ConfigHelper.isPlayingGame() || ConfigHelper.isConfiguredInstalledOnServer())) {
            Player player = Minecraft.getInstance().player;
            if (ConfigHelper.isPlayingGame() && isPlayingRemotely()) {
                if (SessionData.isLan()) {
                    entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.title.server_configuration").getString()));
                    entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.lan_server")));
                    return;
                }
                if (!ConfigHelper.isOperator(player)) {
                    return;
                }
                if (!SessionData.isDeveloper(player)) {
                    entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.title.server_configuration").getString()));
                    entries.add(new ListMenuScreen.MultiTextItem(Component.translatable("configured.gui.no_developer_status"), Component.translatable("configured.gui.developer_details", Component.literal("configured.developer.toml").withStyle(ChatFormatting.GOLD).withStyle(Style.EMPTY.withUnderlined(true))).withStyle(ChatFormatting.GRAY).withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("configured.gui.developer_file"))))));
                    return;
                }
            }
            entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.title.server_configuration").getString()));
            List<ListMenuScreen.Item> remoteEntries = new ArrayList();
            remoteConfigs.forEach(config -> remoteEntries.add(new ModConfigSelectionScreen.FileItem(config)));
            Collections.sort(remoteEntries);
            entries.addAll(remoteEntries);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 - 75, this.f_96544_ - 29, 150, 20, CommonComponents.GUI_BACK, button -> this.f_96541_.setScreen(this.parent)));
    }

    private Set<IModConfig> getLocalConfigs() {
        return (Set<IModConfig>) this.configMap.entrySet().stream().filter(entry -> !((ConfigType) entry.getKey()).isServer()).flatMap(entry -> ((Set) entry.getValue()).stream()).collect(Collectors.toSet());
    }

    private Set<IModConfig> getRemoteConfigs() {
        return (Set<IModConfig>) this.configMap.entrySet().stream().filter(entry -> {
            ConfigType type = (ConfigType) entry.getKey();
            return type.isServer() && type.getEnv().orElse(null) != Environment.DEDICATED_SERVER;
        }).flatMap(entry -> ((Set) entry.getValue()).stream()).collect(Collectors.toSet());
    }

    public static String createLabelFromModConfig(IModConfig config) {
        if (config.getTranslationKey() != null) {
            return I18n.get(config.getTranslationKey());
        } else {
            String fileName = config.getFileName();
            fileName = fileName.replace(config.getModId() + "-", "");
            if (fileName.endsWith(".toml")) {
                fileName = fileName.substring(0, fileName.length() - ".toml".length());
            }
            fileName = FilenameUtils.getName(fileName);
            return ConfigScreen.createLabel(fileName);
        }
    }

    public static boolean canEditConfig(@Nullable Player player, IModConfig config) {
        return switch(config.getType()) {
            case CLIENT ->
                Services.PLATFORM.getEnvironment() == Environment.CLIENT;
            case UNIVERSAL, MEMORY ->
                true;
            case SERVER, WORLD, SERVER_SYNC, WORLD_SYNC ->
                !ConfigHelper.isPlayingGame() || isRunningLocalServer() || ConfigHelper.isOperator(player) && SessionData.isDeveloper(player);
            case DEDICATED_SERVER ->
                false;
        };
    }

    public static boolean canRestoreConfig(Player player, IModConfig config) {
        return switch(config.getType()) {
            case CLIENT, UNIVERSAL, MEMORY ->
                true;
            case SERVER, SERVER_SYNC ->
                !ConfigHelper.isPlayingGame() || isRunningLocalServer();
            case WORLD, WORLD_SYNC ->
                isRunningLocalServer();
            case DEDICATED_SERVER ->
                false;
        };
    }

    public static boolean isRunningLocalServer() {
        return Minecraft.getInstance().hasSingleplayerServer();
    }

    public static boolean isPlayingRemotely() {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener != null && !listener.getConnection().isMemoryConnection();
    }

    public class FileItem extends ListMenuScreen.Item {

        protected final IModConfig config;

        protected final Component title;

        protected final Component fileName;

        protected final Button modifyButton;

        @Nullable
        protected final Button restoreButton;

        public FileItem(IModConfig config) {
            super(ModConfigSelectionScreen.createLabelFromModConfig(config));
            this.config = config;
            this.title = this.createTrimmedFileName(ModConfigSelectionScreen.createLabelFromModConfig(config));
            this.fileName = this.createTrimmedFileName(config.getFileName()).withStyle(ChatFormatting.DARK_GRAY);
            this.modifyButton = this.createModifyButton(config);
            this.modifyButton.f_93623_ = ModConfigSelectionScreen.canEditConfig(Minecraft.getInstance().player, config);
            this.restoreButton = this.createRestoreButton(config);
            this.updateRestoreDefaultButton();
        }

        private void showRestoreScreen() {
            ConfirmationScreen confirmScreen = new ConfirmationScreen(ModConfigSelectionScreen.this, Component.translatable("configured.gui.restore_message"), ConfirmationScreen.Icon.WARNING, result -> {
                if (!result) {
                    return true;
                } else {
                    this.config.restoreDefaults();
                    this.updateRestoreDefaultButton();
                    return true;
                }
            });
            confirmScreen.setBackground(ModConfigSelectionScreen.this.background);
            confirmScreen.setPositiveText(Component.translatable("configured.gui.restore").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            confirmScreen.setNegativeText(CommonComponents.GUI_CANCEL);
            Minecraft.getInstance().setScreen(confirmScreen);
        }

        private MutableComponent createTrimmedFileName(String fileName) {
            MutableComponent trimmedFileName = Component.literal(fileName);
            if (Minecraft.getInstance().font.width(fileName) > 150) {
                trimmedFileName = Component.literal(Minecraft.getInstance().font.plainSubstrByWidth(fileName, 140) + "...");
            }
            return trimmedFileName;
        }

        private Button createModifyButton(IModConfig config) {
            int width = ModConfigSelectionScreen.canRestoreConfig(Minecraft.getInstance().player, config) ? 60 : 82;
            return new IconButton(0, 0, this.getModifyIconU(config), this.getModifyIconV(config), width, this.getModifyLabel(config), button -> {
                if (button.m_142518_() && button.f_93624_) {
                    if (!ConfigHelper.isPlayingGame()) {
                        if (ConfigHelper.isWorldConfig(config)) {
                            Minecraft.getInstance().setScreen(new WorldSelectionScreen(ModConfigSelectionScreen.this, ModConfigSelectionScreen.this.background, config, this.title));
                        } else if (config.getType() != ConfigType.DEDICATED_SERVER) {
                            Component newTitle = ModConfigSelectionScreen.this.f_96539_.copy().append(Component.literal(" > ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(this.title);
                            Minecraft.getInstance().setScreen(new ConfigScreen(ModConfigSelectionScreen.this, newTitle, config, ModConfigSelectionScreen.this.background));
                        }
                    } else if (ModConfigSelectionScreen.isPlayingRemotely() && config.getType().isServer() && !config.getType().isSync()) {
                        if (Services.PLATFORM.isModLoaded(config.getModId())) {
                            Component newTitle = ModConfigSelectionScreen.this.f_96539_.copy().append(Component.literal(" > ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(this.title);
                            Minecraft.getInstance().setScreen(new RequestScreen(ModConfigSelectionScreen.this, newTitle, ModConfigSelectionScreen.this.background, config));
                        }
                    } else {
                        if (Services.PLATFORM.isModLoaded(config.getModId())) {
                            Component newTitle = ModConfigSelectionScreen.this.f_96539_.copy().append(Component.literal(" > ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(this.title);
                            Minecraft.getInstance().setScreen(new ConfigScreen(ModConfigSelectionScreen.this, newTitle, config, ModConfigSelectionScreen.this.background));
                        }
                    }
                }
            });
        }

        private int getModifyIconU(IModConfig config) {
            return !ConfigHelper.isPlayingGame() && ConfigHelper.isWorldConfig(config) ? 11 : 0;
        }

        private int getModifyIconV(IModConfig config) {
            if (ConfigHelper.isPlayingGame()) {
                if (config.isReadOnly()) {
                    return 33;
                }
            } else if (config.isReadOnly() && !ConfigHelper.isWorldConfig(config)) {
                return 33;
            }
            return 22;
        }

        private Component getModifyLabel(IModConfig config) {
            if (!ConfigHelper.isPlayingGame() && ConfigHelper.isWorldConfig(config)) {
                return Component.translatable("configured.gui.select_world");
            } else {
                return config.isReadOnly() ? Component.translatable("configured.gui.view") : Component.translatable("configured.gui.modify");
            }
        }

        private Button createRestoreButton(IModConfig config) {
            if (!ModConfigSelectionScreen.canRestoreConfig(Minecraft.getInstance().player, config)) {
                return null;
            } else {
                IconButton restoreButton = new IconButton(0, 0, 0, 0, onPress -> this.showRestoreScreen());
                restoreButton.f_93623_ = !config.isReadOnly() && ConfigHelper.hasPermissionToEdit(Minecraft.getInstance().player, config);
                return restoreButton;
            }
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
            graphics.drawString(Minecraft.getInstance().font, this.title, left + 28, top + 2, 16777215);
            graphics.drawString(Minecraft.getInstance().font, this.fileName, left + 28, top + 12, 16777215);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(IconButton.ICONS, left + 4, top, 18, 22, (float) this.getIconU(), (float) this.getIconV(), 9, 11, 64, 64);
            if (this.config.isReadOnly()) {
                graphics.blit(IconButton.ICONS, left + 1, top + 15, 11, 11, 0.0F, 33.0F, 11, 11, 64, 64);
            }
            this.modifyButton.m_252865_(left + width - 83);
            this.modifyButton.m_253211_(top);
            this.modifyButton.m_88315_(graphics, mouseX, mouseY, partialTicks);
            if (this.restoreButton != null) {
                this.restoreButton.m_252865_(left + width - 21);
                this.restoreButton.m_253211_(top);
                this.restoreButton.m_88315_(graphics, mouseX, mouseY, partialTicks);
            }
            if (this.config.isReadOnly() && ScreenUtil.isMouseWithin(left - 1, top + 15, 11, 11, mouseX, mouseY)) {
                ModConfigSelectionScreen.this.setActiveTooltip(Component.translatable("configured.gui.read_only_config"), -14785178);
            }
        }

        private int getIconU() {
            return this.config.getType().ordinal() % 3 * 9 + 33;
        }

        private int getIconV() {
            return this.config.getType().ordinal() / 3 * 11;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.restoreButton != null ? ImmutableList.of(this.modifyButton, this.restoreButton) : ImmutableList.of(this.modifyButton);
        }

        private void updateRestoreDefaultButton() {
            if (this.config != null && this.restoreButton != null && ModConfigSelectionScreen.canRestoreConfig(Minecraft.getInstance().player, this.config)) {
                this.restoreButton.f_93623_ = !this.config.isReadOnly() && this.config.isChanged();
            }
        }
    }
}