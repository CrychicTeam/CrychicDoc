package dev.ftb.mods.ftbquests.util;

import dev.architectury.hooks.level.entity.PlayerHooks;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class FTBQuestsInventoryListener implements ContainerListener {

    public final ServerPlayer player;

    public FTBQuestsInventoryListener(ServerPlayer p) {
        this.player = p;
    }

    public static void detect(ServerPlayer player, ItemStack craftedItem, long sourceTask) {
        ServerQuestFile file = ServerQuestFile.INSTANCE;
        if (file != null && !PlayerHooks.isFake(player)) {
            List<Task> tasksToCheck = craftedItem.isEmpty() ? file.getSubmitTasks() : file.getCraftingTasks();
            if (!tasksToCheck.isEmpty()) {
                FTBTeamsAPI.api().getManager().getTeamForPlayer(player).ifPresent(team -> {
                    TeamData data = file.getNullableTeamData(team.getId());
                    if (data != null && !data.isLocked()) {
                        file.withPlayerContext(player, () -> {
                            for (Task task : tasksToCheck) {
                                if (task.id != sourceTask && data.canStartTasks(task.getQuest())) {
                                    task.submitTask(data, player, craftedItem);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int j) {
    }

    @Override
    public void slotChanged(AbstractContainerMenu menu, int index, ItemStack stack) {
        if (!stack.isEmpty() && menu.getSlot(index).container == this.player.m_150109_()) {
            int slotNum = menu.getSlot(index).getContainerSlot();
            if (slotNum >= 0 && slotNum < this.player.m_150109_().items.size()) {
                int delay = Mth.clamp(ServerQuestFile.INSTANCE.getDetectionDelay(), 0, 200);
                if (delay == 0) {
                    detect(this.player, ItemStack.EMPTY, 0L);
                } else {
                    DeferredInventoryDetection.scheduleInventoryCheck(this.player, delay);
                }
            }
        }
    }
}