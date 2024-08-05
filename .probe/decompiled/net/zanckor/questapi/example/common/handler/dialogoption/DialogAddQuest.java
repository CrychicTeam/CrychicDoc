package net.zanckor.questapi.example.common.handler.dialogoption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogOption;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.NPCDialog;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerRequirement;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.mod.common.network.packet.dialogoption.CloseDialog;
import net.zanckor.questapi.mod.common.network.packet.quest.ActiveQuestList;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog.EnumDialogOption;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Timer;
import net.zanckor.questapi.util.Util;

public class DialogAddQuest extends AbstractDialogOption {

    @Override
    public void handler(Player player, Conversation dialog, int option_id, Entity entity) throws IOException {
        int currentDialog = (Integer) QuestDialogManager.currentDialog.get(player);
        NPCDialog.DialogOption option = (NPCDialog.DialogOption) ((NPCDialog.QuestDialog) dialog.getDialog().get(currentDialog)).getOptions().get(option_id);
        String quest = option.getQuest_id() + ".json";
        Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
        if (option.getType().equals(EnumDialogOption.ADD_QUEST.toString())) {
            if (Util.hasQuest(quest, userFolder)) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHandler::closeDialog);
            } else {
                for (File file : CommonMain.serverQuests.toFile().listFiles()) {
                    if (file.getName().equals(quest)) {
                        Path path = Paths.get(CommonMain.getActiveQuest(userFolder).toString(), File.separator, file.getName());
                        ServerQuest serverQuest = (ServerQuest) GsonManager.getJsonClass(file, ServerQuest.class);
                        for (int requirementIndex = 0; requirementIndex < serverQuest.getRequirements().size(); requirementIndex++) {
                            ServerRequirement serverRequirement = (ServerRequirement) serverQuest.getRequirements().get(requirementIndex);
                            String requirementType = serverRequirement.getType() != null ? serverRequirement.getType() : "NONE";
                            Enum<?> questRequirementEnum = EnumRegistry.getEnum(requirementType, EnumRegistry.getDialogRequirement());
                            AbstractQuestRequirement requirement = QuestTemplateRegistry.getQuestRequirement(questRequirementEnum);
                            if (!requirement.handler(player, serverQuest, requirementIndex)) {
                                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHandler::closeDialog);
                                return;
                            }
                        }
                        UserQuest userQuest = UserQuest.createQuest(serverQuest, path);
                        GsonManager.writeJson(path.toFile(), userQuest);
                        if (userQuest.hasTimeLimit()) {
                            Timer.updateCooldown(player.m_20148_(), option.getQuest_id(), (float) userQuest.getTimeLimitInSeconds());
                        }
                        QuestDialogManager.registerQuestByID(option.getQuest_id(), path);
                        break;
                    }
                }
                SendQuestPacket.TO_CLIENT(player, new CloseDialog());
                SendQuestPacket.TO_CLIENT(player, new ActiveQuestList(player.m_20148_()));
            }
        }
    }

    @Override
    public void handler(Player player, Conversation dialog, int option_id, String resourceLocation) throws IOException {
        this.handler(player, dialog, option_id, (Entity) null);
    }

    @Override
    public void handler(Player player, Conversation dialog, int option_id, Item item) throws IOException {
        this.handler(player, dialog, option_id, (Entity) null);
    }
}