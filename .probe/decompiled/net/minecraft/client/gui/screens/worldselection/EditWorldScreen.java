package net.minecraft.client.gui.screens.worldselection;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.minecraft.FileUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.validation.ContentValidationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class EditWorldScreen extends Screen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Component NAME_LABEL = Component.translatable("selectWorld.enterName");

    private Button renameButton;

    private final BooleanConsumer callback;

    private EditBox nameEdit;

    private final LevelStorageSource.LevelStorageAccess levelAccess;

    public EditWorldScreen(BooleanConsumer booleanConsumer0, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess1) {
        super(Component.translatable("selectWorld.edit.title"));
        this.callback = booleanConsumer0;
        this.levelAccess = levelStorageSourceLevelStorageAccess1;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
    }

    @Override
    protected void init() {
        this.renameButton = Button.builder(Component.translatable("selectWorld.edit.save"), p_101280_ -> this.onRename()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 144 + 5, 98, 20).build();
        this.nameEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, 38, 200, 20, Component.translatable("selectWorld.enterName"));
        LevelSummary $$0 = this.levelAccess.getSummary();
        String $$1 = $$0 == null ? "" : $$0.getLevelName();
        this.nameEdit.setValue($$1);
        this.nameEdit.setResponder(p_280914_ -> this.renameButton.f_93623_ = !p_280914_.trim().isEmpty());
        this.m_7787_(this.nameEdit);
        Button $$2 = (Button) this.m_142416_(Button.builder(Component.translatable("selectWorld.edit.resetIcon"), p_280916_ -> {
            this.levelAccess.getIconFile().ifPresent(p_182594_ -> FileUtils.deleteQuietly(p_182594_.toFile()));
            p_280916_.f_93623_ = false;
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 0 + 5, 200, 20).build());
        this.m_142416_(Button.builder(Component.translatable("selectWorld.edit.openFolder"), p_101294_ -> Util.getPlatform().openFile(this.levelAccess.getLevelPath(LevelResource.ROOT).toFile())).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 24 + 5, 200, 20).build());
        this.m_142416_(Button.builder(Component.translatable("selectWorld.edit.backup"), p_101292_ -> {
            boolean $$1x = makeBackupAndShowToast(this.levelAccess);
            this.callback.accept(!$$1x);
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 48 + 5, 200, 20).build());
        this.m_142416_(Button.builder(Component.translatable("selectWorld.edit.backupFolder"), p_280915_ -> {
            LevelStorageSource $$1x = this.f_96541_.getLevelSource();
            Path $$2x = $$1x.getBackupPath();
            try {
                FileUtil.createDirectoriesSafe($$2x);
            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }
            Util.getPlatform().openFile($$2x.toFile());
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 72 + 5, 200, 20).build());
        this.m_142416_(Button.builder(Component.translatable("selectWorld.edit.optimize"), p_280913_ -> this.f_96541_.setScreen(new BackupConfirmScreen(this, (p_280911_, p_280912_) -> {
            if (p_280911_) {
                makeBackupAndShowToast(this.levelAccess);
            }
            this.f_96541_.setScreen(OptimizeWorldScreen.create(this.f_96541_, this.callback, this.f_96541_.getFixerUpper(), this.levelAccess, p_280912_));
        }, Component.translatable("optimizeWorld.confirm.title"), Component.translatable("optimizeWorld.confirm.description"), true))).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 96 + 5, 200, 20).build());
        this.m_142416_(this.renameButton);
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_101273_ -> this.callback.accept(false)).bounds(this.f_96543_ / 2 + 2, this.f_96544_ / 4 + 144 + 5, 98, 20).build());
        $$2.f_93623_ = this.levelAccess.getIconFile().filter(p_182587_ -> Files.isRegularFile(p_182587_, new LinkOption[0])).isPresent();
        this.m_264313_(this.nameEdit);
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        String $$3 = this.nameEdit.getValue();
        this.m_6575_(minecraft0, int1, int2);
        this.nameEdit.setValue($$3);
    }

    @Override
    public void onClose() {
        this.callback.accept(false);
    }

    private void onRename() {
        try {
            this.levelAccess.renameLevel(this.nameEdit.getValue().trim());
            this.callback.accept(true);
        } catch (IOException var2) {
            LOGGER.error("Failed to access world '{}'", this.levelAccess.getLevelId(), var2);
            SystemToast.onWorldAccessFailure(this.f_96541_, this.levelAccess.getLevelId());
            this.callback.accept(true);
        }
    }

    public static void makeBackupAndShowToast(LevelStorageSource levelStorageSource0, String string1) {
        boolean $$2 = false;
        try (LevelStorageSource.LevelStorageAccess $$3 = levelStorageSource0.validateAndCreateAccess(string1)) {
            $$2 = true;
            makeBackupAndShowToast($$3);
        } catch (IOException var8) {
            if (!$$2) {
                SystemToast.onWorldAccessFailure(Minecraft.getInstance(), string1);
            }
            LOGGER.warn("Failed to create backup of level {}", string1, var8);
        } catch (ContentValidationException var9) {
            LOGGER.warn("{}", var9.getMessage());
            SystemToast.onWorldAccessFailure(Minecraft.getInstance(), string1);
        }
    }

    public static boolean makeBackupAndShowToast(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0) {
        long $$1 = 0L;
        IOException $$2 = null;
        try {
            $$1 = levelStorageSourceLevelStorageAccess0.makeWorldBackup();
        } catch (IOException var6) {
            $$2 = var6;
        }
        if ($$2 != null) {
            Component $$4 = Component.translatable("selectWorld.edit.backupFailed");
            Component $$5 = Component.literal($$2.getMessage());
            Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.WORLD_BACKUP, $$4, $$5));
            return false;
        } else {
            Component $$6 = Component.translatable("selectWorld.edit.backupCreated", levelStorageSourceLevelStorageAccess0.getLevelId());
            Component $$7 = Component.translatable("selectWorld.edit.backupSize", Mth.ceil((double) $$1 / 1048576.0));
            Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.WORLD_BACKUP, $$6, $$7));
            return true;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 15, 16777215);
        guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 100, 24, 10526880);
        this.nameEdit.m_88315_(guiGraphics0, int1, int2, float3);
        super.render(guiGraphics0, int1, int2, float3);
    }
}