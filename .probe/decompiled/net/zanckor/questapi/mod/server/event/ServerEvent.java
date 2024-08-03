package net.zanckor.questapi.mod.server.event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.npc.entity_type_tag.codec.EntityTypeTagDialog;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.npcmarker.ValidNPCMarker;
import net.zanckor.questapi.mod.common.network.packet.quest.ActiveQuestList;
import net.zanckor.questapi.mod.common.util.MCUtil;
import net.zanckor.questapi.mod.server.startdialog.StartDialog;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Timer;
import net.zanckor.questapi.util.Util;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class ServerEvent {

    @SubscribeEvent
    public static void questWithTimer(TickEvent.PlayerTickEvent e) throws IOException {
        if (e.player.m_20194_() != null && e.player.m_20194_().getTickCount() % 20 == 0 && !e.player.m_9236_().isClientSide) {
            Path activeQuest = CommonMain.getActiveQuest(CommonMain.getUserFolder(e.player.m_20148_()));
            Path uncompletedQuest = CommonMain.getFailedQuest(CommonMain.getUserFolder(e.player.m_20148_()));
            for (File file : activeQuest.toFile().listFiles()) {
                UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
                if (userQuest != null) {
                    timer(userQuest, e.player, file, uncompletedQuest);
                }
            }
        }
    }

    public static void timer(UserQuest userQuest, Player player, File file, Path uncompletedQuest) throws IOException {
        if (userQuest == null) {
            CommonMain.Constants.LOG.error(player.getScoreboardName() + " has corrupted quest: " + file.getName());
        } else {
            if (!userQuest.isCompleted() && userQuest.hasTimeLimit() && Timer.canUseWithCooldown(player.m_20148_(), userQuest.getId(), (float) userQuest.getTimeLimitInSeconds())) {
                userQuest.setCompleted(true);
                GsonManager.writeJson(file, userQuest);
                for (int indexGoals = 0; indexGoals < userQuest.getQuestGoals().size(); indexGoals++) {
                    UserGoal questGoal = (UserGoal) userQuest.getQuestGoals().get(indexGoals);
                    Enum<?> goalEnum = EnumRegistry.getEnum(questGoal.getType(), EnumRegistry.getQuestGoal());
                    MCUtil.moveFileToUncompletedFolder(uncompletedQuest, file, userQuest, goalEnum);
                }
                SendQuestPacket.TO_CLIENT(player, new ActiveQuestList(player.m_20148_()));
            }
        }
    }

    @SubscribeEvent
    public static void uncompletedQuestOnLogOut(PlayerEvent.PlayerLoggedOutEvent e) throws IOException {
        Path activeQuest = CommonMain.getActiveQuest(CommonMain.getUserFolder(e.getEntity().m_20148_()));
        Path uncompletedQuest = CommonMain.getFailedQuest(CommonMain.getUserFolder(e.getEntity().m_20148_()));
        for (File file : activeQuest.toFile().listFiles()) {
            UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
            if (userQuest != null && userQuest.hasTimeLimit()) {
                for (int indexGoals = 0; indexGoals < userQuest.getQuestGoals().size(); indexGoals++) {
                    UserGoal questGoal = (UserGoal) userQuest.getQuestGoals().get(indexGoals);
                    Enum<?> goalEnum = EnumRegistry.getEnum(questGoal.getType(), EnumRegistry.getQuestGoal());
                    userQuest.setCompleted(true);
                    GsonManager.writeJson(file, userQuest);
                    MCUtil.moveFileToUncompletedFolder(uncompletedQuest, file, userQuest, goalEnum);
                }
            }
        }
    }

    @SubscribeEvent
    public static void loadHashMaps(PlayerEvent.PlayerLoggedInEvent e) throws IOException {
        CommonMain.Constants.LOG.info("Loading hash maps with quests and dialogs for player " + e.getEntity().getName());
        Path userFolder = CommonMain.getUserFolder(e.getEntity().m_20148_());
        Path activeQuest = CommonMain.getActiveQuest(userFolder);
        Path completedQuest = CommonMain.getCompletedQuest(userFolder);
        Path uncompletedQuest = CommonMain.getFailedQuest(userFolder);
        Path[] questPaths = new Path[] { activeQuest, completedQuest, uncompletedQuest };
        for (Path path : questPaths) {
            if (path.toFile().listFiles() != null) {
                for (File file : path.toFile().listFiles()) {
                    UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
                    if (userQuest != null) {
                        for (int indexGoals = 0; indexGoals < userQuest.getQuestGoals().size(); indexGoals++) {
                            UserGoal questGoal = (UserGoal) userQuest.getQuestGoals().get(indexGoals);
                            Enum<?> goalEnum = EnumRegistry.getEnum(questGoal.getType(), EnumRegistry.getQuestGoal());
                            QuestDialogManager.registerQuestTypeLocation(goalEnum, file.toPath().toAbsolutePath());
                        }
                        QuestDialogManager.registerQuestByID(file.getName().substring(0, file.getName().length() - 5), file.toPath().toAbsolutePath());
                    }
                }
            }
        }
        if (CommonMain.serverDialogs.toFile().listFiles() != null) {
            for (File filex : CommonMain.serverDialogs.toFile().listFiles()) {
                QuestDialogManager.registerConversationLocation(filex.getName(), filex.toPath().toAbsolutePath());
            }
        }
        SendQuestPacket.TO_CLIENT(e.getEntity(), new ValidNPCMarker());
        SendQuestPacket.TO_CLIENT(e.getEntity(), new ActiveQuestList(e.getEntity().m_20148_()));
    }

    @SubscribeEvent
    public static void loadDialogOrAddQuestViaItem(PlayerInteractEvent e) throws IOException {
        ItemStack ITEM_STACK = e.getItemStack();
        Player PLAYER = e.getEntity();
        CompoundTag TAG = ITEM_STACK.getTag();
        if (!e.getSide().isClient() && ITEM_STACK != null && TAG != null) {
            if (TAG.contains("display_dialog")) {
                String dialogID = TAG.getString("display_dialog");
                StartDialog.loadDialog(PLAYER, dialogID, e.getItemStack().getItem());
            }
            if (TAG.contains("give_quest")) {
                String questID = TAG.getString("give_quest");
                MCUtil.addQuest(PLAYER, questID);
            }
        }
    }

    @SubscribeEvent
    public static void loadDialogPerEntityType(PlayerInteractEvent.EntityInteract e) throws IOException {
        Player player = e.getEntity();
        Entity target = e.getTarget();
        String targetEntityType = EntityType.getKey(target.getType()).toString();
        List<String> dialogPerEntityType = QuestDialogManager.getConversationByEntityType(targetEntityType);
        if (!player.m_9236_().isClientSide && dialogPerEntityType != null && e.getHand().equals(InteractionHand.MAIN_HAND) && openVanillaMenu(player)) {
            String selectedDialog = target.getPersistentData().getString("dialog");
            if (target.getPersistentData().get("dialog") == null || Util.isConversationCompleted(selectedDialog, player)) {
                for (String conversationID : dialogPerEntityType) {
                    if (!Util.isConversationCompleted(conversationID, player)) {
                        selectedDialog = conversationID;
                        break;
                    }
                }
                target.getPersistentData().putString("dialog", selectedDialog);
            }
            StartDialog.loadDialog(player, selectedDialog, target);
        }
    }

    @SubscribeEvent
    public static void loadDialogPerCompoundTag(PlayerInteractEvent.EntityInteract e) throws IOException {
        Player player = e.getEntity();
        Entity target = e.getTarget();
        List<String> dialogList = new ArrayList();
        if (!player.m_9236_().isClientSide && e.getHand().equals(InteractionHand.MAIN_HAND) && openVanillaMenu(player)) {
            for (Entry<String, File> entry : QuestDialogManager.conversationByCompoundTag.entrySet()) {
                CompoundTag entityNBT = NbtPredicate.getEntityTagToCompare(target);
                File value = (File) entry.getValue();
                String targetEntityType = EntityType.getKey(target.getType()).toString();
                EntityTypeTagDialog entityTypeConversation = (EntityTypeTagDialog) GsonManager.getJsonClass(value, EntityTypeTagDialog.class);
                boolean isEntityType = entityTypeConversation.getEntity_type().contains(targetEntityType);
                if (isEntityType) {
                    for (EntityTypeTagDialog.EntityTypeTagDialogCondition conditions : entityTypeConversation.getConditions()) {
                        switch(conditions.getLogic_gate()) {
                            case OR:
                                for (EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT nbt : conditions.getNbt()) {
                                    if (entityNBT.get(nbt.getTag()) == null) {
                                        boolean tagCompare = false;
                                    } else {
                                        boolean tagCompare = entityNBT.get(nbt.getTag()).getAsString().contains(nbt.getValue());
                                        if (tagCompare) {
                                            dialogList.addAll(conditions.getDialog_list());
                                            break;
                                        }
                                    }
                                }
                                break;
                            case AND:
                                boolean shouldAddDialogList = false;
                                for (EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT nbtx : conditions.getNbt()) {
                                    boolean tagCompare;
                                    if (entityNBT.get(nbtx.getTag()) != null) {
                                        tagCompare = entityNBT.get(nbtx.getTag()).getAsString().contains(nbtx.getValue());
                                    } else {
                                        tagCompare = false;
                                    }
                                    shouldAddDialogList = tagCompare;
                                    if (!tagCompare) {
                                        break;
                                    }
                                }
                                if (shouldAddDialogList) {
                                    dialogList.addAll(conditions.getDialog_list());
                                }
                        }
                    }
                }
            }
            if (!dialogList.isEmpty()) {
                e.setCanceled(true);
                String selectedDialog = target.getPersistentData().getString("dialog");
                if (!dialogList.isEmpty() && (target.getPersistentData().get("dialog") == null || Util.isConversationCompleted(selectedDialog, player))) {
                    for (String conversationID : dialogList) {
                        if (!Util.isConversationCompleted(conversationID, player)) {
                            selectedDialog = conversationID;
                            break;
                        }
                    }
                    target.getPersistentData().putString("dialog", selectedDialog);
                }
                StartDialog.loadDialog(player, selectedDialog, target);
            }
        }
    }

    public static boolean openVanillaMenu(Player player) {
        if (player.m_6144_()) {
            player.m_20260_(false);
            return false;
        } else {
            return true;
        }
    }
}