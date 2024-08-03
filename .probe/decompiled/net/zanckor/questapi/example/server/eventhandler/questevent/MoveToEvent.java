package net.zanckor.questapi.example.server.eventhandler.questevent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Mathematic;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE)
public class MoveToEvent {

    @SubscribeEvent
    public static void moveToQuest(TickEvent.PlayerTickEvent e) throws IOException {
        if (e.player != null && !e.side.isClient() && e.player.m_20194_().getTickCount() % 20 == 0) {
            List<Path> moveToQuests = QuestDialogManager.getQuestTypePathLocation(EnumGoalType.MOVE_TO);
            if (moveToQuests != null) {
                for (Path path : moveToQuests) {
                    UserQuest playerQuest = (UserQuest) GsonManager.getJsonClass(path.toFile(), UserQuest.class);
                    if (playerQuest != null && !playerQuest.isCompleted()) {
                        moveTo(playerQuest, (ServerPlayer) e.player);
                    }
                }
            }
        }
    }

    public static void moveTo(UserQuest userQuest, ServerPlayer player) throws IOException {
        BlockPos targetPos = null;
        for (int indexGoals = 0; indexGoals < userQuest.getQuestGoals().size(); indexGoals++) {
            UserGoal questGoal = (UserGoal) userQuest.getQuestGoals().get(indexGoals);
            List<Double> additionalListData = (List<Double>) questGoal.getAdditionalListData();
            if (questGoal.getType().equals(EnumGoalType.MOVE_TO.toString()) && additionalListData != null) {
                Vec3i vec3Coord = new Vec3i(((Double) additionalListData.get(0)).intValue(), ((Double) additionalListData.get(1)).intValue(), ((Double) additionalListData.get(2)).intValue());
                targetPos = new BlockPos(vec3Coord);
            }
        }
        Vec3 playerCoord = new Vec3((double) player.m_146903_(), (double) player.m_146904_(), (double) player.m_146907_());
        if (targetPos != null && Mathematic.numberBetween(playerCoord.x, (double) (targetPos.m_123304_(Direction.Axis.X) - 10), (double) (targetPos.m_123304_(Direction.Axis.X) + 10)) && Mathematic.numberBetween(playerCoord.y, (double) (targetPos.m_123304_(Direction.Axis.Y) - 10), (double) (targetPos.m_123304_(Direction.Axis.Y) + 10)) && Mathematic.numberBetween(playerCoord.z, (double) (targetPos.m_123304_(Direction.Axis.Z) - 10), (double) (targetPos.m_123304_(Direction.Axis.Z) + 10))) {
            ServerHandler.questHandler(EnumGoalType.MOVE_TO, player, null);
        }
    }
}