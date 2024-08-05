package dev.ftb.mods.ftbquests.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.utils.Env;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.client.gui.CustomToast;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.net.DeleteObjectMessage;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.StructureTask;
import dev.ftb.mods.ftbquests.quest.theme.QuestTheme;
import dev.ftb.mods.ftbquests.util.TextUtils;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ClientQuestFile extends BaseQuestFile {

    private static final List<String> MISSING_DATA_ERR = List.of("Unable to open Quest GUI: no quest book data received from server!", "- Check that FTB Quests and FTB Teams are installed on the server", "  and that no server-side errors were logged when you connected.");

    public static ClientQuestFile INSTANCE;

    public TeamData selfTeamData;

    private QuestScreen questScreen;

    private QuestScreen.PersistedData persistedData;

    private boolean editorPermission;

    public static boolean exists() {
        return INSTANCE != null && !INSTANCE.invalid;
    }

    public static void syncFromServer(BaseQuestFile newInstance) {
        if (newInstance instanceof ClientQuestFile clientInstance) {
            if (INSTANCE != null) {
                INSTANCE.deleteChildren();
                INSTANCE.deleteSelf();
            }
            INSTANCE = clientInstance;
            INSTANCE.onReplaced();
        } else {
            throw new IllegalArgumentException("need a client quest file instance!");
        }
    }

    private void onReplaced() {
        this.selfTeamData = new TeamData(Util.NIL_UUID, INSTANCE, "Loading...");
        this.selfTeamData.setLocked(true);
        this.refreshGui();
        FTBQuests.getRecipeModHelper().refreshRecipes(INSTANCE);
    }

    @Override
    public boolean canEdit() {
        return Minecraft.getInstance().player != null && this.hasEditorPermission() && this.selfTeamData.getCanEdit(Minecraft.getInstance().player);
    }

    @Override
    public void refreshGui() {
        this.clearCachedData();
        if (this.questScreen != null) {
            this.persistedData = this.questScreen.getPersistedScreenData();
            if (ClientUtils.getCurrentGuiAs(QuestScreen.class) != null) {
                double mx = Minecraft.getInstance().mouseHandler.xpos();
                double my = Minecraft.getInstance().mouseHandler.ypos();
                Minecraft.getInstance().setScreen(null);
                this.questScreen = new QuestScreen(this, this.persistedData);
                this.questScreen.openGui();
                InputConstants.grabOrReleaseMouse(Minecraft.getInstance().getWindow().getWindow(), 212993, mx, my);
            }
        }
    }

    public Optional<QuestScreen> getQuestScreen() {
        return Optional.ofNullable(this.questScreen);
    }

    public static QuestScreen openGui() {
        if (INSTANCE != null) {
            return INSTANCE.openQuestGui();
        } else {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                MISSING_DATA_ERR.forEach(s -> player.displayClientMessage(Component.literal(s).withStyle(ChatFormatting.RED), false));
            }
            return null;
        }
    }

    public static QuestScreen openGui(Quest quest, boolean focused) {
        QuestScreen screen = openGui();
        if (screen != null) {
            screen.open(quest, focused);
        }
        return screen;
    }

    private QuestScreen openQuestGui() {
        if (exists()) {
            if (this.isDisableGui() && !this.canEdit()) {
                Minecraft.getInstance().getToasts().addToast(new CustomToast(Component.translatable("item.ftbquests.book.disabled"), Icons.BARRIER, Component.empty()));
            } else {
                if (!this.selfTeamData.isLocked()) {
                    if (this.canEdit()) {
                        StructureTask.maybeRequestStructureSync();
                    }
                    this.questScreen = new QuestScreen(this, this.persistedData);
                    this.questScreen.openGui();
                    this.questScreen.refreshWidgets();
                    return this.questScreen;
                }
                Minecraft.getInstance().getToasts().addToast(new CustomToast((Component) (this.lockMessage.isEmpty() ? Component.literal("Quests locked!") : TextUtils.parseRawText(this.lockMessage)), Icons.BARRIER, Component.empty()));
            }
        }
        return null;
    }

    @Override
    public Env getSide() {
        return Env.CLIENT;
    }

    @Override
    public void deleteObject(long id) {
        new DeleteObjectMessage(id).sendToServer();
    }

    @Override
    public void clearCachedData() {
        super.clearCachedData();
        QuestTheme.instance.clearCache();
    }

    @Override
    public TeamData getOrCreateTeamData(Entity player) {
        KnownClientPlayer kcp = (KnownClientPlayer) FTBTeamsAPI.api().getClientManager().getKnownPlayer(player.getUUID()).orElseThrow(() -> new RuntimeException("Unknown client player " + player.getUUID()));
        return kcp.id().equals(Minecraft.getInstance().player.m_20148_()) ? this.selfTeamData : this.getOrCreateTeamData(kcp.teamId());
    }

    public void setPersistedScreenInfo(QuestScreen.PersistedData persistedData) {
        this.persistedData = persistedData;
    }

    public static boolean canClientPlayerEdit() {
        return exists() && INSTANCE.selfTeamData.getCanEdit(FTBQuestsClient.getClientPlayer());
    }

    public static boolean isQuestPinned(long id) {
        return exists() && INSTANCE.selfTeamData.isQuestPinned(FTBQuestsClient.getClientPlayer(), id);
    }

    @Override
    public boolean isPlayerOnTeam(Player player, TeamData teamData) {
        return (Boolean) FTBTeamsAPI.api().getClientManager().getKnownPlayer(player.m_20148_()).map(kcp -> kcp.teamId().equals(teamData.getTeamId())).orElse(false);
    }

    @Override
    public boolean moveChapterGroup(long id, boolean movingUp) {
        if (super.moveChapterGroup(id, movingUp)) {
            this.clearCachedData();
            QuestScreen gui = ClientUtils.getCurrentGuiAs(QuestScreen.class);
            if (gui != null) {
                gui.refreshChapterPanel();
            }
            return true;
        } else {
            return false;
        }
    }

    public void setEditorPermission(boolean hasPermission) {
        this.editorPermission = hasPermission;
    }

    public boolean hasEditorPermission() {
        return this.editorPermission;
    }

    public static void openBookToQuestObject(long id) {
        if (exists()) {
            ClientQuestFile file = INSTANCE;
            if (file.questScreen == null) {
                INSTANCE.openQuestGui();
            }
            if (file.questScreen != null) {
                if (id != 0L) {
                    QuestObject qo = file.get(id);
                    if (qo != null) {
                        file.questScreen.open(qo, true);
                    }
                } else {
                    file.questScreen.openGui();
                }
            }
        }
    }
}