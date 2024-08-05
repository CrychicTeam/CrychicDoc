package net.zanckor.questapi.mod.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.codec.ReadConversation;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerRequirement;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.quest.ActiveQuestList;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Util;

@EventBusSubscriber(modid = "questapi")
public class MCUtil {

    public static Entity getEntityLookinAt(Entity rayTraceEntity, double distance) {
        float playerRotX = rayTraceEntity.getXRot();
        float playerRotY = rayTraceEntity.getYRot();
        Vec3 startPos = rayTraceEntity.getEyePosition();
        float f2 = Mth.cos(-playerRotY * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f3 = Mth.sin(-playerRotY * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f4 = -Mth.cos(-playerRotX * (float) (Math.PI / 180.0));
        float additionY = Mth.sin(-playerRotX * (float) (Math.PI / 180.0));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = distance;
        Vec3 endVec = startPos.add((double) additionX * distance, (double) additionY * distance, (double) additionZ * distance);
        AABB startEndBox = new AABB(startPos, endVec);
        Entity entity = null;
        for (Entity entity1 : rayTraceEntity.level().getEntities(rayTraceEntity, startEndBox, val -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0) {
                    entity = entity1;
                    startPos = (Vec3) optional.orElse(startPos);
                    d0 = 0.0;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = (Vec3) optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0) {
                    if (entity1.getRootVehicle() != rayTraceEntity.getRootVehicle() || entity1.canRiderInteract()) {
                        entity = entity1;
                        startPos = vec31;
                        d0 = d1;
                    } else if (d0 == 0.0) {
                        entity = entity1;
                        startPos = vec31;
                    }
                }
            }
        }
        return entity;
    }

    public static void addQuest(Player player, String questID) throws IOException {
        String quest = questID + ".json";
        Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
        if (hasQuest(quest, userFolder)) {
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
                            return;
                        }
                    }
                    Util.createQuest(serverQuest, player, path);
                    QuestDialogManager.registerQuestByID(questID, path);
                    SendQuestPacket.TO_CLIENT(player, new ActiveQuestList(player.m_20148_()));
                    return;
                }
            }
        }
    }

    public static BlockHitResult getHitResult(Level level, Player player, float multiplier) {
        float xRot = player.m_146909_();
        float yRot = player.m_146908_();
        Vec3 eyePos = player.m_146892_();
        float yRotCos = Mth.cos(-yRot * (float) (Math.PI / 180.0) - (float) Math.PI);
        float yRotSin = Mth.sin(-yRot * (float) (Math.PI / 180.0) - (float) Math.PI);
        float xCosDegrees = -Mth.cos((float) (-Math.toDegrees((double) xRot)));
        float xSinDegrees = Mth.sin((float) (-Math.toDegrees((double) xRot)));
        float yCosRotation = yRotCos * xCosDegrees;
        float ySinRotation = yRotSin * xCosDegrees;
        double viewDistance = player.getBlockReach() * (double) multiplier;
        Vec3 lookingVector = eyePos.add((double) ySinRotation * viewDistance, (double) xSinDegrees * viewDistance, (double) yCosRotation * viewDistance);
        return level.m_45547_(new ClipContext(eyePos, lookingVector, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }

    public static void writeDialogRead(Player player, String conversationID, int dialogID) throws IOException {
        Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
        Path readDialogPath = Paths.get(CommonMain.getReadDialogs(userFolder).toString(), File.separator, "dialog_read.json");
        File readDialogFile = readDialogPath.toFile();
        ReadConversation readConversation = readDialogFile.exists() ? (ReadConversation) GsonManager.getJsonClass(readDialogFile, ReadConversation.class) : new ReadConversation();
        readConversation.getConversation(conversationID).add(dialogID);
        GsonManager.writeJson(readDialogFile, readConversation);
    }

    public static boolean isReadDialog(Player player, String conversationID, int dialogID) throws IOException {
        Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
        Path readDialogPath = Paths.get(CommonMain.getReadDialogs(userFolder).toString(), File.separator, "dialog_read.json");
        File readDialogFile = readDialogPath.toFile();
        ReadConversation readConversation = readDialogFile.exists() ? (ReadConversation) GsonManager.getJsonClass(readDialogFile, ReadConversation.class) : null;
        return readConversation.getConversation(conversationID).contains(dialogID);
    }

    public static boolean hasQuest(String quest, Path userFolder) {
        return Files.exists(Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), quest), new LinkOption[0]) || Files.exists(Paths.get(CommonMain.getActiveQuest(userFolder).toString(), quest), new LinkOption[0]) || Files.exists(Paths.get(CommonMain.getFailedQuest(userFolder).toString(), quest), new LinkOption[0]);
    }

    public static void moveFileToCompletedFolder(UserQuest userQuest, ServerPlayer player, File file) throws IOException {
        Path userFolder = Paths.get(CommonMain.playerData.toFile().toString(), player.m_20148_().toString());
        String questName = userQuest.getId() + ".json";
        Files.deleteIfExists(Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), file.getName()));
        Files.move(file.toPath(), Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), file.getName()));
        for (int indexGoals = 0; indexGoals < userQuest.getQuestGoals().size(); indexGoals++) {
            Enum goalEnum = EnumRegistry.getEnum(((UserGoal) userQuest.getQuestGoals().get(indexGoals)).getType(), EnumRegistry.getQuestGoal());
            QuestDialogManager.movePathQuest(userQuest.getId(), Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), questName), goalEnum);
        }
    }

    public static void moveFileToUncompletedFolder(Path uncompletedQuestFolder, File file, UserQuest userQuest, Enum goalEnum) throws IOException {
        Path uncompletedPath = Paths.get(uncompletedQuestFolder.toString(), file.getName());
        if (file.exists()) {
            Files.move(file.toPath(), uncompletedPath);
        }
        QuestDialogManager.movePathQuest(userQuest.getId(), uncompletedPath, goalEnum);
    }

    public static Entity getEntityByUUID(ServerLevel level, UUID uuid) {
        for (Entity entity : level.getAllEntities()) {
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    public static List<Integer> findSlotMatchingItemStack(ItemStack itemStack, Inventory inventory) {
        List<Integer> slots = new ArrayList();
        for (int itemSlot = 0; itemSlot < inventory.items.size(); itemSlot++) {
            if (!inventory.items.get(itemSlot).isEmpty() && ItemStack.isSameItem(itemStack, inventory.items.get(itemSlot))) {
                slots.add(itemSlot);
            }
        }
        return slots;
    }

    public static int randomBetween(double min, double max) {
        return (int) (Math.random() * (max - min) + min);
    }
}