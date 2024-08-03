package com.mojang.realmsclient.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.realms.RealmsObjectSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import org.slf4j.Logger;

public class RealmsSelectFileToUploadScreen extends RealmsScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Component UNABLE_TO_LOAD_WORLD = Component.translatable("selectWorld.unable_to_load");

    static final Component WORLD_TEXT = Component.translatable("selectWorld.world");

    static final Component HARDCORE_TEXT = Component.translatable("mco.upload.hardcore").withStyle(p_264655_ -> p_264655_.withColor(-65536));

    static final Component CHEATS_TEXT = Component.translatable("selectWorld.cheats");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

    private final RealmsResetWorldScreen lastScreen;

    private final long worldId;

    private final int slotId;

    Button uploadButton;

    List<LevelSummary> levelList = Lists.newArrayList();

    int selectedWorld = -1;

    RealmsSelectFileToUploadScreen.WorldSelectionList worldSelectionList;

    private final Runnable callback;

    public RealmsSelectFileToUploadScreen(long long0, int int1, RealmsResetWorldScreen realmsResetWorldScreen2, Runnable runnable3) {
        super(Component.translatable("mco.upload.select.world.title"));
        this.lastScreen = realmsResetWorldScreen2;
        this.worldId = long0;
        this.slotId = int1;
        this.callback = runnable3;
    }

    private void loadLevelList() throws Exception {
        LevelStorageSource.LevelCandidates $$0 = this.f_96541_.getLevelSource().findLevelCandidates();
        this.levelList = (List<LevelSummary>) ((List) this.f_96541_.getLevelSource().loadLevelSummaries($$0).join()).stream().filter(p_193517_ -> !p_193517_.requiresManualConversion() && !p_193517_.isLocked()).collect(Collectors.toList());
        for (LevelSummary $$1 : this.levelList) {
            this.worldSelectionList.addEntry($$1);
        }
    }

    @Override
    public void init() {
        this.worldSelectionList = new RealmsSelectFileToUploadScreen.WorldSelectionList();
        try {
            this.loadLevelList();
        } catch (Exception var2) {
            LOGGER.error("Couldn't load level list", var2);
            this.f_96541_.setScreen(new RealmsGenericErrorScreen(UNABLE_TO_LOAD_WORLD, Component.nullToEmpty(var2.getMessage()), this.lastScreen));
            return;
        }
        this.m_7787_(this.worldSelectionList);
        this.uploadButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.upload.button.name"), p_231307_ -> this.upload()).bounds(this.f_96543_ / 2 - 154, this.f_96544_ - 32, 153, 20).build());
        this.uploadButton.f_93623_ = this.selectedWorld >= 0 && this.selectedWorld < this.levelList.size();
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_280747_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 + 6, this.f_96544_ - 32, 153, 20).build());
        this.m_175073_(new RealmsLabel(Component.translatable("mco.upload.select.world.subtitle"), this.f_96543_ / 2, m_120774_(-1), 10526880));
        if (this.levelList.isEmpty()) {
            this.m_175073_(new RealmsLabel(Component.translatable("mco.upload.select.world.none"), this.f_96543_ / 2, this.f_96544_ / 2 - 20, 16777215));
        }
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(this.m_96636_(), this.m_175075_());
    }

    private void upload() {
        if (this.selectedWorld != -1 && !((LevelSummary) this.levelList.get(this.selectedWorld)).isHardcore()) {
            LevelSummary $$0 = (LevelSummary) this.levelList.get(this.selectedWorld);
            this.f_96541_.setScreen(new RealmsUploadScreen(this.worldId, this.slotId, this.lastScreen, $$0, this.callback));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.worldSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 13, 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
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

    static Component gameModeName(LevelSummary levelSummary0) {
        return levelSummary0.getGameMode().getLongDisplayName();
    }

    static String formatLastPlayed(LevelSummary levelSummary0) {
        return DATE_FORMAT.format(new Date(levelSummary0.getLastPlayed()));
    }

    class Entry extends ObjectSelectionList.Entry<RealmsSelectFileToUploadScreen.Entry> {

        private final LevelSummary levelSummary;

        private final String name;

        private final Component id;

        private final Component info;

        public Entry(LevelSummary levelSummary0) {
            this.levelSummary = levelSummary0;
            this.name = levelSummary0.getLevelName();
            this.id = Component.translatable("mco.upload.entry.id", levelSummary0.getLevelId(), RealmsSelectFileToUploadScreen.formatLastPlayed(levelSummary0));
            Component $$1;
            if (levelSummary0.isHardcore()) {
                $$1 = RealmsSelectFileToUploadScreen.HARDCORE_TEXT;
            } else {
                $$1 = RealmsSelectFileToUploadScreen.gameModeName(levelSummary0);
            }
            if (levelSummary0.hasCheats()) {
                $$1 = Component.translatable("mco.upload.entry.cheats", $$1.getString(), RealmsSelectFileToUploadScreen.CHEATS_TEXT);
            }
            this.info = $$1;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.renderItem(guiGraphics0, int1, int3, int2);
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            RealmsSelectFileToUploadScreen.this.worldSelectionList.m_7109_(RealmsSelectFileToUploadScreen.this.levelList.indexOf(this.levelSummary));
            return true;
        }

        protected void renderItem(GuiGraphics guiGraphics0, int int1, int int2, int int3) {
            String $$4;
            if (this.name.isEmpty()) {
                $$4 = RealmsSelectFileToUploadScreen.WORLD_TEXT + " " + (int1 + 1);
            } else {
                $$4 = this.name;
            }
            guiGraphics0.drawString(RealmsSelectFileToUploadScreen.this.f_96547_, $$4, int2 + 2, int3 + 1, 16777215, false);
            guiGraphics0.drawString(RealmsSelectFileToUploadScreen.this.f_96547_, this.id, int2 + 2, int3 + 12, 8421504, false);
            guiGraphics0.drawString(RealmsSelectFileToUploadScreen.this.f_96547_, this.info, int2 + 2, int3 + 12 + 10, 8421504, false);
        }

        @Override
        public Component getNarration() {
            Component $$0 = CommonComponents.joinLines(Component.literal(this.levelSummary.getLevelName()), Component.literal(RealmsSelectFileToUploadScreen.formatLastPlayed(this.levelSummary)), RealmsSelectFileToUploadScreen.gameModeName(this.levelSummary));
            return Component.translatable("narrator.select", $$0);
        }
    }

    class WorldSelectionList extends RealmsObjectSelectionList<RealmsSelectFileToUploadScreen.Entry> {

        public WorldSelectionList() {
            super(RealmsSelectFileToUploadScreen.this.f_96543_, RealmsSelectFileToUploadScreen.this.f_96544_, RealmsSelectFileToUploadScreen.m_120774_(0), RealmsSelectFileToUploadScreen.this.f_96544_ - 40, 36);
        }

        public void addEntry(LevelSummary levelSummary0) {
            this.m_7085_(RealmsSelectFileToUploadScreen.this.new Entry(levelSummary0));
        }

        @Override
        public int getMaxPosition() {
            return RealmsSelectFileToUploadScreen.this.levelList.size() * 36;
        }

        @Override
        public void renderBackground(GuiGraphics guiGraphics0) {
            RealmsSelectFileToUploadScreen.this.m_280273_(guiGraphics0);
        }

        public void setSelected(@Nullable RealmsSelectFileToUploadScreen.Entry realmsSelectFileToUploadScreenEntry0) {
            super.m_6987_(realmsSelectFileToUploadScreenEntry0);
            RealmsSelectFileToUploadScreen.this.selectedWorld = this.m_6702_().indexOf(realmsSelectFileToUploadScreenEntry0);
            RealmsSelectFileToUploadScreen.this.uploadButton.f_93623_ = RealmsSelectFileToUploadScreen.this.selectedWorld >= 0 && RealmsSelectFileToUploadScreen.this.selectedWorld < this.m_5773_() && !((LevelSummary) RealmsSelectFileToUploadScreen.this.levelList.get(RealmsSelectFileToUploadScreen.this.selectedWorld)).isHardcore();
        }
    }
}