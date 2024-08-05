package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.LoadingDotsText;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SymlinkWarningScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class WorldSelectionList extends ObjectSelectionList<WorldSelectionList.Entry> {

    static final Logger LOGGER = LogUtils.getLogger();

    static final DateFormat DATE_FORMAT = new SimpleDateFormat();

    private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");

    static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");

    static final Component FROM_NEWER_TOOLTIP_1 = Component.translatable("selectWorld.tooltip.fromNewerVersion1").withStyle(ChatFormatting.RED);

    static final Component FROM_NEWER_TOOLTIP_2 = Component.translatable("selectWorld.tooltip.fromNewerVersion2").withStyle(ChatFormatting.RED);

    static final Component SNAPSHOT_TOOLTIP_1 = Component.translatable("selectWorld.tooltip.snapshot1").withStyle(ChatFormatting.GOLD);

    static final Component SNAPSHOT_TOOLTIP_2 = Component.translatable("selectWorld.tooltip.snapshot2").withStyle(ChatFormatting.GOLD);

    static final Component WORLD_LOCKED_TOOLTIP = Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);

    static final Component WORLD_REQUIRES_CONVERSION = Component.translatable("selectWorld.conversion.tooltip").withStyle(ChatFormatting.RED);

    private final SelectWorldScreen screen;

    private CompletableFuture<List<LevelSummary>> pendingLevels;

    @Nullable
    private List<LevelSummary> currentlyDisplayedLevels;

    private String filter;

    private final WorldSelectionList.LoadingHeader loadingHeader;

    public WorldSelectionList(SelectWorldScreen selectWorldScreen0, Minecraft minecraft1, int int2, int int3, int int4, int int5, int int6, String string7, @Nullable WorldSelectionList worldSelectionList8) {
        super(minecraft1, int2, int3, int4, int5, int6);
        this.screen = selectWorldScreen0;
        this.loadingHeader = new WorldSelectionList.LoadingHeader(minecraft1);
        this.filter = string7;
        if (worldSelectionList8 != null) {
            this.pendingLevels = worldSelectionList8.pendingLevels;
        } else {
            this.pendingLevels = this.loadLevels();
        }
        this.handleNewLevels(this.pollLevelsIgnoreErrors());
    }

    @Override
    protected void clearEntries() {
        this.m_6702_().forEach(WorldSelectionList.Entry::close);
        super.m_93516_();
    }

    @Nullable
    private List<LevelSummary> pollLevelsIgnoreErrors() {
        try {
            return (List<LevelSummary>) this.pendingLevels.getNow(null);
        } catch (CancellationException | CompletionException var2) {
            return null;
        }
    }

    void reloadWorldList() {
        this.pendingLevels = this.loadLevels();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (CommonInputs.selected(int0)) {
            Optional<WorldSelectionList.WorldListEntry> $$3 = this.getSelectedOpt();
            if ($$3.isPresent()) {
                ((WorldSelectionList.WorldListEntry) $$3.get()).joinWorld();
                return true;
            }
        }
        return super.m_7933_(int0, int1, int2);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        List<LevelSummary> $$4 = this.pollLevelsIgnoreErrors();
        if ($$4 != this.currentlyDisplayedLevels) {
            this.handleNewLevels($$4);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    private void handleNewLevels(@Nullable List<LevelSummary> listLevelSummary0) {
        if (listLevelSummary0 == null) {
            this.fillLoadingLevels();
        } else {
            this.fillLevels(this.filter, listLevelSummary0);
        }
        this.currentlyDisplayedLevels = listLevelSummary0;
    }

    public void updateFilter(String string0) {
        if (this.currentlyDisplayedLevels != null && !string0.equals(this.filter)) {
            this.fillLevels(string0, this.currentlyDisplayedLevels);
        }
        this.filter = string0;
    }

    private CompletableFuture<List<LevelSummary>> loadLevels() {
        LevelStorageSource.LevelCandidates $$0;
        try {
            $$0 = this.f_93386_.getLevelSource().findLevelCandidates();
        } catch (LevelStorageException var3) {
            LOGGER.error("Couldn't load level list", var3);
            this.handleLevelLoadFailure(var3.getMessageComponent());
            return CompletableFuture.completedFuture(List.of());
        }
        if ($$0.isEmpty()) {
            CreateWorldScreen.openFresh(this.f_93386_, null);
            return CompletableFuture.completedFuture(List.of());
        } else {
            return this.f_93386_.getLevelSource().loadLevelSummaries($$0).exceptionally(p_233202_ -> {
                this.f_93386_.delayCrash(CrashReport.forThrowable(p_233202_, "Couldn't load level list"));
                return List.of();
            });
        }
    }

    private void fillLevels(String string0, List<LevelSummary> listLevelSummary1) {
        this.clearEntries();
        string0 = string0.toLowerCase(Locale.ROOT);
        for (LevelSummary $$2 : listLevelSummary1) {
            if (this.filterAccepts(string0, $$2)) {
                this.m_7085_(new WorldSelectionList.WorldListEntry(this, $$2));
            }
        }
        this.notifyListUpdated();
    }

    private boolean filterAccepts(String string0, LevelSummary levelSummary1) {
        return levelSummary1.getLevelName().toLowerCase(Locale.ROOT).contains(string0) || levelSummary1.getLevelId().toLowerCase(Locale.ROOT).contains(string0);
    }

    private void fillLoadingLevels() {
        this.clearEntries();
        this.m_7085_(this.loadingHeader);
        this.notifyListUpdated();
    }

    private void notifyListUpdated() {
        this.screen.m_169407_(true);
    }

    private void handleLevelLoadFailure(Component component0) {
        this.f_93386_.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), component0));
    }

    @Override
    protected int getScrollbarPosition() {
        return super.m_5756_() + 20;
    }

    @Override
    public int getRowWidth() {
        return super.m_5759_() + 50;
    }

    public void setSelected(@Nullable WorldSelectionList.Entry worldSelectionListEntry0) {
        super.m_6987_(worldSelectionListEntry0);
        this.screen.updateButtonStatus(worldSelectionListEntry0 != null && worldSelectionListEntry0.isSelectable(), worldSelectionListEntry0 != null);
    }

    public Optional<WorldSelectionList.WorldListEntry> getSelectedOpt() {
        WorldSelectionList.Entry $$0 = (WorldSelectionList.Entry) this.m_93511_();
        return $$0 instanceof WorldSelectionList.WorldListEntry $$1 ? Optional.of($$1) : Optional.empty();
    }

    public SelectWorldScreen getScreen() {
        return this.screen;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        if (this.m_6702_().contains(this.loadingHeader)) {
            this.loadingHeader.m_142291_(narrationElementOutput0);
        } else {
            super.updateNarration(narrationElementOutput0);
        }
    }

    public abstract static class Entry extends ObjectSelectionList.Entry<WorldSelectionList.Entry> implements AutoCloseable {

        public abstract boolean isSelectable();

        public void close() {
        }
    }

    public static class LoadingHeader extends WorldSelectionList.Entry {

        private static final Component LOADING_LABEL = Component.translatable("selectWorld.loading_list");

        private final Minecraft minecraft;

        public LoadingHeader(Minecraft minecraft0) {
            this.minecraft = minecraft0;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            int $$10 = (this.minecraft.screen.width - this.minecraft.font.width(LOADING_LABEL)) / 2;
            int $$11 = int2 + (int5 - 9) / 2;
            guiGraphics0.drawString(this.minecraft.font, LOADING_LABEL, $$10, $$11, 16777215, false);
            String $$12 = LoadingDotsText.get(Util.getMillis());
            int $$13 = (this.minecraft.screen.width - this.minecraft.font.width($$12)) / 2;
            int $$14 = $$11 + 9;
            guiGraphics0.drawString(this.minecraft.font, $$12, $$13, $$14, 8421504, false);
        }

        @Override
        public Component getNarration() {
            return LOADING_LABEL;
        }

        @Override
        public boolean isSelectable() {
            return false;
        }
    }

    public final class WorldListEntry extends WorldSelectionList.Entry implements AutoCloseable {

        private static final int ICON_WIDTH = 32;

        private static final int ICON_HEIGHT = 32;

        private static final int ICON_OVERLAY_X_JOIN = 0;

        private static final int ICON_OVERLAY_X_JOIN_WITH_NOTIFY = 32;

        private static final int ICON_OVERLAY_X_WARNING = 64;

        private static final int ICON_OVERLAY_X_ERROR = 96;

        private static final int ICON_OVERLAY_Y_UNSELECTED = 0;

        private static final int ICON_OVERLAY_Y_SELECTED = 32;

        private final Minecraft minecraft;

        private final SelectWorldScreen screen;

        private final LevelSummary summary;

        private final FaviconTexture icon;

        @Nullable
        private Path iconFile;

        private long lastClickTime;

        public WorldListEntry(WorldSelectionList worldSelectionList0, LevelSummary levelSummary1) {
            this.minecraft = worldSelectionList0.f_93386_;
            this.screen = worldSelectionList0.getScreen();
            this.summary = levelSummary1;
            this.icon = FaviconTexture.forWorld(this.minecraft.getTextureManager(), levelSummary1.getLevelId());
            this.iconFile = levelSummary1.getIcon();
            this.validateIconFile();
            this.loadIcon();
        }

        private void validateIconFile() {
            if (this.iconFile != null) {
                try {
                    BasicFileAttributes $$0 = Files.readAttributes(this.iconFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                    if ($$0.isSymbolicLink()) {
                        List<ForbiddenSymlinkInfo> $$1 = new ArrayList();
                        this.minecraft.getLevelSource().getWorldDirValidator().validateSymlink(this.iconFile, $$1);
                        if (!$$1.isEmpty()) {
                            ???;
                            this.iconFile = null;
                        } else {
                            $$0 = Files.readAttributes(this.iconFile, BasicFileAttributes.class);
                        }
                    }
                    if (!$$0.isRegularFile()) {
                        this.iconFile = null;
                    }
                } catch (NoSuchFileException var3) {
                    this.iconFile = null;
                } catch (IOException var4) {
                    WorldSelectionList.LOGGER.error("could not validate symlink", var4);
                    this.iconFile = null;
                }
            }
        }

        @Override
        public Component getNarration() {
            Component $$0 = Component.translatable("narrator.select.world_info", this.summary.getLevelName(), new Date(this.summary.getLastPlayed()), this.summary.getInfo());
            Component $$1;
            if (this.summary.isLocked()) {
                $$1 = CommonComponents.joinForNarration($$0, WorldSelectionList.WORLD_LOCKED_TOOLTIP);
            } else {
                $$1 = $$0;
            }
            return Component.translatable("narrator.select", $$1);
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            String $$10 = this.summary.getLevelName();
            String $$11 = this.summary.getLevelId();
            long $$12 = this.summary.getLastPlayed();
            if ($$12 != -1L) {
                $$11 = $$11 + " (" + WorldSelectionList.DATE_FORMAT.format(new Date($$12)) + ")";
            }
            if (StringUtils.isEmpty($$10)) {
                $$10 = I18n.get("selectWorld.world") + " " + (int1 + 1);
            }
            Component $$13 = this.summary.getInfo();
            guiGraphics0.drawString(this.minecraft.font, $$10, int3 + 32 + 3, int2 + 1, 16777215, false);
            guiGraphics0.drawString(this.minecraft.font, $$11, int3 + 32 + 3, int2 + 9 + 3, 8421504, false);
            guiGraphics0.drawString(this.minecraft.font, $$13, int3 + 32 + 3, int2 + 9 + 9 + 3, 8421504, false);
            RenderSystem.enableBlend();
            guiGraphics0.blit(this.icon.textureLocation(), int3, int2, 0.0F, 0.0F, 32, 32, 32, 32);
            RenderSystem.disableBlend();
            if (this.minecraft.options.touchscreen().get() || boolean8) {
                guiGraphics0.fill(int3, int2, int3 + 32, int2 + 32, -1601138544);
                int $$14 = int6 - int3;
                boolean $$15 = $$14 < 32;
                int $$16 = $$15 ? 32 : 0;
                if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
                    guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 96.0F, (float) $$16, 32, 32, 256, 256);
                    guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 32.0F, (float) $$16, 32, 32, 256, 256);
                    return;
                }
                if (this.summary.isLocked()) {
                    guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 96.0F, (float) $$16, 32, 32, 256, 256);
                    if ($$15) {
                        this.screen.m_257959_(this.minecraft.font.split(WorldSelectionList.WORLD_LOCKED_TOOLTIP, 175));
                    }
                } else if (this.summary.requiresManualConversion()) {
                    guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 96.0F, (float) $$16, 32, 32, 256, 256);
                    if ($$15) {
                        this.screen.m_257959_(this.minecraft.font.split(WorldSelectionList.WORLD_REQUIRES_CONVERSION, 175));
                    }
                } else if (this.summary.markVersionInList()) {
                    guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 32.0F, (float) $$16, 32, 32, 256, 256);
                    if (this.summary.askToOpenWorld()) {
                        guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 96.0F, (float) $$16, 32, 32, 256, 256);
                        if ($$15) {
                            this.screen.m_257959_(ImmutableList.of(WorldSelectionList.FROM_NEWER_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.FROM_NEWER_TOOLTIP_2.getVisualOrderText()));
                        }
                    } else if (!SharedConstants.getCurrentVersion().isStable()) {
                        guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 64.0F, (float) $$16, 32, 32, 256, 256);
                        if ($$15) {
                            this.screen.m_257959_(ImmutableList.of(WorldSelectionList.SNAPSHOT_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.SNAPSHOT_TOOLTIP_2.getVisualOrderText()));
                        }
                    }
                } else {
                    guiGraphics0.blit(WorldSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 0.0F, (float) $$16, 32, 32, 256, 256);
                }
            }
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            if (this.summary.isDisabled()) {
                return true;
            } else {
                WorldSelectionList.this.setSelected((WorldSelectionList.Entry) this);
                if (double0 - (double) WorldSelectionList.this.m_5747_() <= 32.0) {
                    this.joinWorld();
                    return true;
                } else if (Util.getMillis() - this.lastClickTime < 250L) {
                    this.joinWorld();
                    return true;
                } else {
                    this.lastClickTime = Util.getMillis();
                    return true;
                }
            }
        }

        public void joinWorld() {
            if (!this.summary.isDisabled()) {
                if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
                    this.minecraft.setScreen(new SymlinkWarningScreen(this.screen));
                } else {
                    LevelSummary.BackupStatus $$0 = this.summary.backupStatus();
                    if ($$0.shouldBackup()) {
                        String $$1 = "selectWorld.backupQuestion." + $$0.getTranslationKey();
                        String $$2 = "selectWorld.backupWarning." + $$0.getTranslationKey();
                        MutableComponent $$3 = Component.translatable($$1);
                        if ($$0.isSevere()) {
                            $$3.withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
                        }
                        Component $$4 = Component.translatable($$2, this.summary.getWorldVersionName(), SharedConstants.getCurrentVersion().getName());
                        this.minecraft.setScreen(new BackupConfirmScreen(this.screen, (p_289912_, p_289913_) -> {
                            if (p_289912_) {
                                String $$2x = this.summary.getLevelId();
                                try (LevelStorageSource.LevelStorageAccess $$3x = this.minecraft.getLevelSource().validateAndCreateAccess($$2x)) {
                                    EditWorldScreen.makeBackupAndShowToast($$3x);
                                } catch (IOException var9) {
                                    SystemToast.onWorldAccessFailure(this.minecraft, $$2x);
                                    WorldSelectionList.LOGGER.error("Failed to backup level {}", $$2x, var9);
                                } catch (ContentValidationException var10) {
                                    WorldSelectionList.LOGGER.warn("{}", var10.getMessage());
                                    this.minecraft.setScreen(new SymlinkWarningScreen(this.screen));
                                }
                            }
                            this.loadWorld();
                        }, $$3, $$4, false));
                    } else if (this.summary.askToOpenWorld()) {
                        this.minecraft.setScreen(new ConfirmScreen(p_101741_ -> {
                            if (p_101741_) {
                                try {
                                    this.loadWorld();
                                } catch (Exception var3x) {
                                    WorldSelectionList.LOGGER.error("Failure to open 'future world'", var3x);
                                    this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(this.screen), Component.translatable("selectWorld.futureworld.error.title"), Component.translatable("selectWorld.futureworld.error.text")));
                                }
                            } else {
                                this.minecraft.setScreen(this.screen);
                            }
                        }, Component.translatable("selectWorld.versionQuestion"), Component.translatable("selectWorld.versionWarning", this.summary.getWorldVersionName()), Component.translatable("selectWorld.versionJoinButton"), CommonComponents.GUI_CANCEL));
                    } else {
                        this.loadWorld();
                    }
                }
            }
        }

        public void deleteWorld() {
            this.minecraft.setScreen(new ConfirmScreen(p_170322_ -> {
                if (p_170322_) {
                    this.minecraft.setScreen(new ProgressScreen(true));
                    this.doDeleteWorld();
                }
                this.minecraft.setScreen(this.screen);
            }, Component.translatable("selectWorld.deleteQuestion"), Component.translatable("selectWorld.deleteWarning", this.summary.getLevelName()), Component.translatable("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
        }

        public void doDeleteWorld() {
            LevelStorageSource $$0 = this.minecraft.getLevelSource();
            String $$1 = this.summary.getLevelId();
            try (LevelStorageSource.LevelStorageAccess $$2 = $$0.createAccess($$1)) {
                $$2.deleteLevel();
            } catch (IOException var8) {
                SystemToast.onWorldDeleteFailure(this.minecraft, $$1);
                WorldSelectionList.LOGGER.error("Failed to delete world {}", $$1, var8);
            }
            WorldSelectionList.this.reloadWorldList();
        }

        public void editWorld() {
            if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
                this.minecraft.setScreen(new SymlinkWarningScreen(this.screen));
            } else {
                this.queueLoadScreen();
                String $$0 = this.summary.getLevelId();
                try {
                    LevelStorageSource.LevelStorageAccess $$1 = this.minecraft.getLevelSource().validateAndCreateAccess($$0);
                    this.minecraft.setScreen(new EditWorldScreen(p_233244_ -> {
                        try {
                            $$1.close();
                        } catch (IOException var5) {
                            WorldSelectionList.LOGGER.error("Failed to unlock level {}", $$0, var5);
                        }
                        if (p_233244_) {
                            WorldSelectionList.this.reloadWorldList();
                        }
                        this.minecraft.setScreen(this.screen);
                    }, $$1));
                } catch (IOException var3) {
                    SystemToast.onWorldAccessFailure(this.minecraft, $$0);
                    WorldSelectionList.LOGGER.error("Failed to access level {}", $$0, var3);
                    WorldSelectionList.this.reloadWorldList();
                } catch (ContentValidationException var4) {
                    WorldSelectionList.LOGGER.warn("{}", var4.getMessage());
                    this.minecraft.setScreen(new SymlinkWarningScreen(this.screen));
                }
            }
        }

        public void recreateWorld() {
            if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
                this.minecraft.setScreen(new SymlinkWarningScreen(this.screen));
            } else {
                this.queueLoadScreen();
                try (LevelStorageSource.LevelStorageAccess $$0 = this.minecraft.getLevelSource().validateAndCreateAccess(this.summary.getLevelId())) {
                    Pair<LevelSettings, WorldCreationContext> $$1 = this.minecraft.createWorldOpenFlows().recreateWorldData($$0);
                    LevelSettings $$2 = (LevelSettings) $$1.getFirst();
                    WorldCreationContext $$3 = (WorldCreationContext) $$1.getSecond();
                    Path $$4 = CreateWorldScreen.createTempDataPackDirFromExistingWorld($$0.getLevelPath(LevelResource.DATAPACK_DIR), this.minecraft);
                    if ($$3.options().isOldCustomizedWorld()) {
                        this.minecraft.setScreen(new ConfirmScreen(p_275882_ -> this.minecraft.setScreen((Screen) (p_275882_ ? CreateWorldScreen.createFromExisting(this.minecraft, this.screen, $$2, $$3, $$4) : this.screen)), Component.translatable("selectWorld.recreate.customized.title"), Component.translatable("selectWorld.recreate.customized.text"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
                    } else {
                        this.minecraft.setScreen(CreateWorldScreen.createFromExisting(this.minecraft, this.screen, $$2, $$3, $$4));
                    }
                } catch (ContentValidationException var8) {
                    WorldSelectionList.LOGGER.warn("{}", var8.getMessage());
                    this.minecraft.setScreen(new SymlinkWarningScreen(this.screen));
                } catch (Exception var9) {
                    WorldSelectionList.LOGGER.error("Unable to recreate world", var9);
                    this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(this.screen), Component.translatable("selectWorld.recreate.error.title"), Component.translatable("selectWorld.recreate.error.text")));
                }
            }
        }

        private void loadWorld() {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (this.minecraft.getLevelSource().levelExists(this.summary.getLevelId())) {
                this.queueLoadScreen();
                this.minecraft.createWorldOpenFlows().loadLevel(this.screen, this.summary.getLevelId());
            }
        }

        private void queueLoadScreen() {
            this.minecraft.forceSetScreen(new GenericDirtMessageScreen(Component.translatable("selectWorld.data_read")));
        }

        private void loadIcon() {
            boolean $$0 = this.iconFile != null && Files.isRegularFile(this.iconFile, new LinkOption[0]);
            if ($$0) {
                try {
                    InputStream $$1 = Files.newInputStream(this.iconFile);
                    try {
                        this.icon.upload(NativeImage.read($$1));
                    } catch (Throwable var6) {
                        if ($$1 != null) {
                            try {
                                $$1.close();
                            } catch (Throwable var5) {
                                var6.addSuppressed(var5);
                            }
                        }
                        throw var6;
                    }
                    if ($$1 != null) {
                        $$1.close();
                    }
                } catch (Throwable var7) {
                    WorldSelectionList.LOGGER.error("Invalid icon for world {}", this.summary.getLevelId(), var7);
                    this.iconFile = null;
                }
            } else {
                this.icon.clear();
            }
        }

        @Override
        public void close() {
            this.icon.close();
        }

        public String getLevelName() {
            return this.summary.getLevelName();
        }

        @Override
        public boolean isSelectable() {
            return !this.summary.isDisabled();
        }
    }
}