package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.RealmsUtil;
import com.mojang.realmsclient.util.task.DownloadTask;
import com.mojang.realmsclient.util.task.RestoreTask;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsObjectSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class RealmsBackupScreen extends RealmsScreen {

    static final Logger LOGGER = LogUtils.getLogger();

    static final ResourceLocation PLUS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/plus_icon.png");

    static final ResourceLocation RESTORE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/restore_icon.png");

    static final Component RESTORE_TOOLTIP = Component.translatable("mco.backup.button.restore");

    static final Component HAS_CHANGES_TOOLTIP = Component.translatable("mco.backup.changes.tooltip");

    private static final Component TITLE = Component.translatable("mco.configure.world.backup");

    private static final Component NO_BACKUPS_LABEL = Component.translatable("mco.backup.nobackups");

    private final RealmsConfigureWorldScreen lastScreen;

    List<Backup> backups = Collections.emptyList();

    RealmsBackupScreen.BackupObjectSelectionList backupObjectSelectionList;

    int selectedBackup = -1;

    private final int slotId;

    private Button downloadButton;

    private Button restoreButton;

    private Button changesButton;

    Boolean noBackups = false;

    final RealmsServer serverData;

    private static final String UPLOADED_KEY = "uploaded";

    public RealmsBackupScreen(RealmsConfigureWorldScreen realmsConfigureWorldScreen0, RealmsServer realmsServer1, int int2) {
        super(Component.translatable("mco.configure.world.backup"));
        this.lastScreen = realmsConfigureWorldScreen0;
        this.serverData = realmsServer1;
        this.slotId = int2;
    }

    @Override
    public void init() {
        this.backupObjectSelectionList = new RealmsBackupScreen.BackupObjectSelectionList();
        (new Thread("Realms-fetch-backups") {

            public void run() {
                RealmsClient $$0 = RealmsClient.create();
                try {
                    List<Backup> $$1 = $$0.backupsFor(RealmsBackupScreen.this.serverData.id).backups;
                    RealmsBackupScreen.this.f_96541_.execute(() -> {
                        RealmsBackupScreen.this.backups = $$1;
                        RealmsBackupScreen.this.noBackups = RealmsBackupScreen.this.backups.isEmpty();
                        RealmsBackupScreen.this.backupObjectSelectionList.m_7178_();
                        for (Backup $$1x : RealmsBackupScreen.this.backups) {
                            RealmsBackupScreen.this.backupObjectSelectionList.addEntry($$1x);
                        }
                    });
                } catch (RealmsServiceException var3) {
                    RealmsBackupScreen.LOGGER.error("Couldn't request backups", var3);
                }
            }
        }).start();
        this.downloadButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.backup.button.download"), p_88185_ -> this.downloadClicked()).bounds(this.f_96543_ - 135, m_120774_(1), 120, 20).build());
        this.restoreButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.backup.button.restore"), p_88179_ -> this.restoreClicked(this.selectedBackup)).bounds(this.f_96543_ - 135, m_120774_(3), 120, 20).build());
        this.changesButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.backup.changes.tooltip"), p_280692_ -> {
            this.f_96541_.setScreen(new RealmsBackupInfoScreen(this, (Backup) this.backups.get(this.selectedBackup)));
            this.selectedBackup = -1;
        }).bounds(this.f_96543_ - 135, m_120774_(5), 120, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_280691_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ - 100, this.f_96544_ - 35, 85, 20).build());
        this.m_7787_(this.backupObjectSelectionList);
        this.m_94725_(this.backupObjectSelectionList);
        this.updateButtonStates();
    }

    void updateButtonStates() {
        this.restoreButton.f_93624_ = this.shouldRestoreButtonBeVisible();
        this.changesButton.f_93624_ = this.shouldChangesButtonBeVisible();
    }

    private boolean shouldChangesButtonBeVisible() {
        return this.selectedBackup == -1 ? false : !((Backup) this.backups.get(this.selectedBackup)).changeList.isEmpty();
    }

    private boolean shouldRestoreButtonBeVisible() {
        return this.selectedBackup == -1 ? false : !this.serverData.expired;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(this.lastScreen);
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    void restoreClicked(int int0) {
        if (int0 >= 0 && int0 < this.backups.size() && !this.serverData.expired) {
            this.selectedBackup = int0;
            Date $$1 = ((Backup) this.backups.get(int0)).lastModifiedDate;
            String $$2 = DateFormat.getDateTimeInstance(3, 3).format($$1);
            Component $$3 = RealmsUtil.convertToAgePresentationFromInstant($$1);
            Component $$4 = Component.translatable("mco.configure.world.restore.question.line1", $$2, $$3);
            Component $$5 = Component.translatable("mco.configure.world.restore.question.line2");
            this.f_96541_.setScreen(new RealmsLongConfirmationScreen(p_280693_ -> {
                if (p_280693_) {
                    this.restore();
                } else {
                    this.selectedBackup = -1;
                    this.f_96541_.setScreen(this);
                }
            }, RealmsLongConfirmationScreen.Type.WARNING, $$4, $$5, true));
        }
    }

    private void downloadClicked() {
        Component $$0 = Component.translatable("mco.configure.world.restore.download.question.line1");
        Component $$1 = Component.translatable("mco.configure.world.restore.download.question.line2");
        this.f_96541_.setScreen(new RealmsLongConfirmationScreen(p_280690_ -> {
            if (p_280690_) {
                this.downloadWorldData();
            } else {
                this.f_96541_.setScreen(this);
            }
        }, RealmsLongConfirmationScreen.Type.INFO, $$0, $$1, true));
    }

    private void downloadWorldData() {
        this.f_96541_.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen.getNewScreen(), new DownloadTask(this.serverData.id, this.slotId, this.serverData.name + " (" + ((RealmsWorldOptions) this.serverData.slots.get(this.serverData.activeSlot)).getSlotName(this.serverData.activeSlot) + ")", this)));
    }

    private void restore() {
        Backup $$0 = (Backup) this.backups.get(this.selectedBackup);
        this.selectedBackup = -1;
        this.f_96541_.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen.getNewScreen(), new RestoreTask($$0, this.serverData.id, this.lastScreen)));
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.backupObjectSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 12, 16777215);
        guiGraphics0.drawString(this.f_96547_, TITLE, (this.f_96543_ - 150) / 2 - 90, 20, 10526880, false);
        if (this.noBackups) {
            guiGraphics0.drawString(this.f_96547_, NO_BACKUPS_LABEL, 20, this.f_96544_ / 2 - 10, 16777215, false);
        }
        this.downloadButton.f_93623_ = !this.noBackups;
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    class BackupObjectSelectionList extends RealmsObjectSelectionList<RealmsBackupScreen.Entry> {

        public BackupObjectSelectionList() {
            super(RealmsBackupScreen.this.f_96543_ - 150, RealmsBackupScreen.this.f_96544_, 32, RealmsBackupScreen.this.f_96544_ - 15, 36);
        }

        public void addEntry(Backup backup0) {
            this.m_7085_(RealmsBackupScreen.this.new Entry(backup0));
        }

        @Override
        public int getRowWidth() {
            return (int) ((double) this.f_93388_ * 0.93);
        }

        @Override
        public int getMaxPosition() {
            return this.m_5773_() * 36;
        }

        @Override
        public void renderBackground(GuiGraphics guiGraphics0) {
            RealmsBackupScreen.this.m_280273_(guiGraphics0);
        }

        @Override
        public int getScrollbarPosition() {
            return this.f_93388_ - 5;
        }

        @Override
        public void selectItem(int int0) {
            super.selectItem(int0);
            this.selectInviteListItem(int0);
        }

        public void selectInviteListItem(int int0) {
            RealmsBackupScreen.this.selectedBackup = int0;
            RealmsBackupScreen.this.updateButtonStates();
        }

        public void setSelected(@Nullable RealmsBackupScreen.Entry realmsBackupScreenEntry0) {
            super.m_6987_(realmsBackupScreenEntry0);
            RealmsBackupScreen.this.selectedBackup = this.m_6702_().indexOf(realmsBackupScreenEntry0);
            RealmsBackupScreen.this.updateButtonStates();
        }
    }

    class Entry extends ObjectSelectionList.Entry<RealmsBackupScreen.Entry> {

        private static final int Y_PADDING = 2;

        private static final int X_PADDING = 7;

        private final Backup backup;

        private final List<AbstractWidget> children = new ArrayList();

        @Nullable
        private ImageButton restoreButton;

        @Nullable
        private ImageButton changesButton;

        public Entry(Backup backup0) {
            this.backup = backup0;
            this.populateChangeList(backup0);
            if (!backup0.changeList.isEmpty()) {
                this.addChangesButton();
            }
            if (!RealmsBackupScreen.this.serverData.expired) {
                this.addRestoreButton();
            }
        }

        private void populateChangeList(Backup backup0) {
            int $$1 = RealmsBackupScreen.this.backups.indexOf(backup0);
            if ($$1 != RealmsBackupScreen.this.backups.size() - 1) {
                Backup $$2 = (Backup) RealmsBackupScreen.this.backups.get($$1 + 1);
                for (String $$3 : backup0.metadata.keySet()) {
                    if (!$$3.contains("uploaded") && $$2.metadata.containsKey($$3)) {
                        if (!((String) backup0.metadata.get($$3)).equals($$2.metadata.get($$3))) {
                            this.addToChangeList($$3);
                        }
                    } else {
                        this.addToChangeList($$3);
                    }
                }
            }
        }

        private void addToChangeList(String string0) {
            if (string0.contains("uploaded")) {
                String $$1 = DateFormat.getDateTimeInstance(3, 3).format(this.backup.lastModifiedDate);
                this.backup.changeList.put(string0, $$1);
                this.backup.setUploadedVersion(true);
            } else {
                this.backup.changeList.put(string0, (String) this.backup.metadata.get(string0));
            }
        }

        private void addChangesButton() {
            int $$0 = 9;
            int $$1 = 9;
            int $$2 = RealmsBackupScreen.this.backupObjectSelectionList.m_93520_() - 9 - 28;
            int $$3 = RealmsBackupScreen.this.backupObjectSelectionList.m_7610_(RealmsBackupScreen.this.backups.indexOf(this.backup)) + 2;
            this.changesButton = new ImageButton($$2, $$3, 9, 9, 0, 0, 9, RealmsBackupScreen.PLUS_ICON_LOCATION, 9, 18, p_279278_ -> RealmsBackupScreen.this.f_96541_.setScreen(new RealmsBackupInfoScreen(RealmsBackupScreen.this, this.backup)));
            this.changesButton.m_257544_(Tooltip.create(RealmsBackupScreen.HAS_CHANGES_TOOLTIP));
            this.children.add(this.changesButton);
        }

        private void addRestoreButton() {
            int $$0 = 17;
            int $$1 = 10;
            int $$2 = RealmsBackupScreen.this.backupObjectSelectionList.m_93520_() - 17 - 7;
            int $$3 = RealmsBackupScreen.this.backupObjectSelectionList.m_7610_(RealmsBackupScreen.this.backups.indexOf(this.backup)) + 2;
            this.restoreButton = new ImageButton($$2, $$3, 17, 10, 0, 0, 10, RealmsBackupScreen.RESTORE_ICON_LOCATION, 17, 20, p_279191_ -> RealmsBackupScreen.this.restoreClicked(RealmsBackupScreen.this.backups.indexOf(this.backup)));
            this.restoreButton.m_257544_(Tooltip.create(RealmsBackupScreen.RESTORE_TOOLTIP));
            this.children.add(this.restoreButton);
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            if (this.restoreButton != null) {
                this.restoreButton.m_6375_(double0, double1, int2);
            }
            if (this.changesButton != null) {
                this.changesButton.m_6375_(double0, double1, int2);
            }
            return true;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            int $$10 = this.backup.isUploadedVersion() ? -8388737 : 16777215;
            guiGraphics0.drawString(RealmsBackupScreen.this.f_96547_, Component.translatable("mco.backup.entry", RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModifiedDate)), int3, int2 + 1, $$10, false);
            guiGraphics0.drawString(RealmsBackupScreen.this.f_96547_, this.getMediumDatePresentation(this.backup.lastModifiedDate), int3, int2 + 12, 5000268, false);
            this.children.forEach(p_280700_ -> {
                p_280700_.setY(int2 + 2);
                p_280700_.render(guiGraphics0, int6, int7, float9);
            });
        }

        private String getMediumDatePresentation(Date date0) {
            return DateFormat.getDateTimeInstance(3, 3).format(date0);
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", this.backup.lastModifiedDate.toString());
        }
    }
}