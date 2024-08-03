package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import com.mrcrayfish.configured.platform.Services;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import org.apache.commons.io.file.PathUtils;

public class WorldSelectionScreen extends ListMenuScreen {

    private static final LevelResource SERVER_CONFIG_FOLDER = Services.CONFIG.getServerConfigResource();

    private static final ResourceLocation MISSING_ICON = new ResourceLocation("textures/misc/unknown_server.png");

    private final IModConfig config;

    public WorldSelectionScreen(Screen parent, ResourceLocation background, IModConfig config, Component title) {
        super(parent, Component.translatable("configured.gui.edit_world_config", title.plainCopy().withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD)), background, 30);
        this.config = config;
    }

    @Override
    protected void constructEntries(List<ListMenuScreen.Item> entries) {
        try {
            LevelStorageSource source = Minecraft.getInstance().getLevelSource();
            List<LevelSummary> levels = new ArrayList((Collection) source.loadLevelSummaries(source.findLevelCandidates()).join());
            if (levels.size() > 6) {
                entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.title.recently_played").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)));
                List<LevelSummary> recent = levels.stream().sorted(Comparator.comparing(s -> -s.getLastPlayed())).limit(3L).toList();
                recent.forEach(summary -> entries.add(new WorldSelectionScreen.WorldItem(summary)));
                levels.removeAll(recent);
                entries.add(new ListMenuScreen.TitleItem(Component.translatable("configured.gui.title.other_worlds").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)));
            }
            levels.stream().sorted(Comparator.comparing(LevelSummary::m_78361_)).forEach(summary -> entries.add(new WorldSelectionScreen.WorldItem(summary)));
        } catch (LevelStorageException var5) {
            var5.printStackTrace();
        }
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 - 75, this.f_96544_ - 29, 150, 20, CommonComponents.GUI_BACK, button -> this.f_96541_.setScreen(this.parent)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.pose().pushPose();
        graphics.pose().translate((float) (this.f_96543_ - 30), 15.0F, 0.0F);
        graphics.pose().scale(2.5F, 2.5F, 2.5F);
        graphics.drawString(this.f_96547_, Component.literal("?").withStyle(ChatFormatting.BOLD), 0, 0, 16777215);
        graphics.pose().popPose();
    }

    @Override
    protected void updateTooltip(int mouseX, int mouseY) {
        super.updateTooltip(mouseX, mouseY);
        if (ScreenUtil.isMouseWithin(this.f_96543_ - 30, 15, 23, 23, mouseX, mouseY)) {
            this.setActiveTooltip(Component.translatable("configured.gui.server_config_info"));
        }
    }

    @Override
    public void onClose() {
        super.m_7379_();
        this.entries.forEach(item -> {
            if (item instanceof WorldSelectionScreen.WorldItem) {
                ((WorldSelectionScreen.WorldItem) item).disposeIcon();
            }
        });
    }

    public class WorldItem extends ListMenuScreen.Item {

        private final Component worldName;

        private final Component folderName;

        private final ResourceLocation iconId;

        private Path iconFile;

        private final DynamicTexture texture;

        private final Button modifyButton;

        public WorldItem(LevelSummary summary) {
            super(summary.getLevelName());
            this.worldName = Component.literal(summary.getLevelName());
            this.folderName = Component.literal(summary.getLevelId()).withStyle(ChatFormatting.DARK_GRAY);
            this.iconId = new ResourceLocation("minecraft", "worlds/" + Util.sanitizeName(summary.getLevelId(), ResourceLocation::m_135828_) + "/" + Hashing.sha1().hashUnencodedChars(summary.getLevelId()) + "/icon");
            this.iconFile = summary.getIcon();
            if (!Files.isRegularFile(this.iconFile, new LinkOption[0])) {
                this.iconFile = null;
            }
            this.texture = this.loadWorldIcon();
            this.modifyButton = new IconButton(0, 0, 0, this.getIconV(), 60, this.getButtonLabel(), onPress -> this.loadWorldConfig(summary.getLevelId(), summary.getLevelName()));
        }

        private Component getButtonLabel() {
            return WorldSelectionScreen.this.config.isReadOnly() ? Component.translatable("configured.gui.view") : Component.translatable("configured.gui.select");
        }

        private int getIconV() {
            return WorldSelectionScreen.this.config.isReadOnly() ? 33 : 22;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.modifyButton);
        }

        @Override
        public void render(GuiGraphics graphics, int x, int top, int left, int width, int p_230432_6_, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
            if (x % 2 != 0) {
                graphics.fill(left, top, left + width, top + 24, 1426063360);
            }
            if (this.modifyButton.m_5953_((double) mouseX, (double) mouseY)) {
                graphics.fill(left - 1, top - 1, left + 25, top + 25, -1);
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(this.texture != null ? this.iconId : WorldSelectionScreen.MISSING_ICON, left, top, 24, 24, 0.0F, 0.0F, 64, 64, 64, 64);
            graphics.drawString(WorldSelectionScreen.this.f_96541_.font, this.worldName, left + 30, top + 3, 16777215);
            graphics.drawString(WorldSelectionScreen.this.f_96541_.font, this.folderName, left + 30, top + 13, 16777215);
            this.modifyButton.m_252865_(left + width - 61);
            this.modifyButton.m_253211_(top + 2);
            this.modifyButton.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }

        private DynamicTexture loadWorldIcon() {
            if (this.iconFile == null) {
                return null;
            } else {
                try {
                    InputStream is = Files.newInputStream(this.iconFile);
                    DynamicTexture var4;
                    label84: {
                        DynamicTexture texture;
                        try (NativeImage image = NativeImage.read(is)) {
                            if (image.getWidth() == 64 && image.getHeight() == 64) {
                                texture = new DynamicTexture(image);
                                WorldSelectionScreen.this.f_96541_.getTextureManager().register(this.iconId, texture);
                                var4 = texture;
                                break label84;
                            }
                            texture = null;
                        } catch (Throwable var8) {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (Throwable var5) {
                                    var8.addSuppressed(var5);
                                }
                            }
                            throw var8;
                        }
                        if (is != null) {
                            is.close();
                        }
                        return texture;
                    }
                    if (is != null) {
                        is.close();
                    }
                    return var4;
                } catch (IOException var9) {
                    return null;
                }
            }
        }

        public void disposeIcon() {
            if (this.texture != null) {
                this.texture.close();
            }
        }

        private void loadWorldConfig(String worldFileName, String worldName) {
            try (LevelStorageSource.LevelStorageAccess storageAccess = Minecraft.getInstance().getLevelSource().createAccess(worldFileName)) {
                Path worldConfigPath = storageAccess.getLevelPath(WorldSelectionScreen.SERVER_CONFIG_FOLDER);
                PathUtils.createParentDirectories(worldConfigPath, new FileAttribute[0]);
                if (!Files.isDirectory(worldConfigPath, new LinkOption[0])) {
                    Files.createDirectory(worldConfigPath);
                }
                WorldSelectionScreen.this.config.loadWorldConfig(worldConfigPath, T -> {
                    if (Services.PLATFORM.isModLoaded(T.getModId())) {
                        Component configName = Component.literal(ModConfigSelectionScreen.createLabelFromModConfig(WorldSelectionScreen.this.config));
                        Component newTitle = Component.literal(worldName).m_6881_().append(Component.literal(" > ").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)).append(configName);
                        WorldSelectionScreen.this.f_96541_.setScreen(new ConfigScreen(WorldSelectionScreen.this.parent, newTitle, T, WorldSelectionScreen.this.background));
                    }
                });
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }
    }
}