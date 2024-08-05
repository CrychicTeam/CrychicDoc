package noppes.npcs.packets.server;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.EntityIMixin;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketQuestCompletionCheck extends PacketServerBasic {

    private final int questId;

    public SPacketQuestCompletionCheck(int questId) {
        this.questId = questId;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketQuestCompletionCheck msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.questId);
    }

    public static SPacketQuestCompletionCheck decode(FriendlyByteBuf buf) {
        return new SPacketQuestCompletionCheck(buf.readInt());
    }

    @Override
    protected void handle() {
        PlayerData data = PlayerData.get(this.player);
        PlayerQuestData playerdata = data.questData;
        QuestData questdata = (QuestData) playerdata.activeQuests.get(this.questId);
        if (questdata != null) {
            Quest quest = questdata.quest;
            if (quest.questInterface.isCompleted(this.player)) {
                QuestEvent.QuestTurnedInEvent event = new QuestEvent.QuestTurnedInEvent(data.scriptData.getPlayer(), quest);
                event.expReward = quest.rewardExp;
                List<IItemStack> list = new ArrayList();
                for (ItemStack item : quest.rewardItems.items) {
                    if (!item.isEmpty()) {
                        list.add(NpcAPI.Instance().getIItemStack(item));
                    }
                }
                if (!quest.randomReward) {
                    event.itemRewards = (IItemStack[]) list.toArray(new IItemStack[list.size()]);
                } else if (!list.isEmpty()) {
                    event.itemRewards = new IItemStack[] { (IItemStack) list.get(this.player.m_217043_().nextInt(list.size())) };
                }
                EventHooks.onQuestTurnedIn(data.scriptData, event);
                for (IItemStack itemx : event.itemRewards) {
                    if (itemx != null) {
                        NoppesUtilServer.GivePlayerItem(this.player, this.player, itemx.getMCItemStack());
                    }
                }
                quest.questInterface.handleComplete(this.player);
                if (event.expReward > 0) {
                    NoppesUtilServer.playSound(this.player, SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, 0.5F * ((this.player.m_9236_().random.nextFloat() - this.player.m_9236_().random.nextFloat()) * 0.7F + 1.8F));
                    this.player.giveExperiencePoints(event.expReward);
                }
                quest.factionOptions.addPoints(this.player);
                if (quest.mail.isValid()) {
                    PlayerDataController.instance.addPlayerMessage(this.player.m_20194_(), this.player.m_7755_().getString(), quest.mail);
                }
                if (!quest.command.isEmpty()) {
                    FakePlayer cplayer = EntityNPCInterface.CommandPlayer;
                    ((EntityIMixin) cplayer).setLevel((ServerLevel) this.player.m_9236_());
                    cplayer.m_6034_(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_());
                    NoppesUtilServer.runCommand(cplayer, "QuestCompletion", quest.command, this.player);
                }
                PlayerQuestController.setQuestFinished(quest, this.player);
                if (quest.hasNewQuest()) {
                    PlayerQuestController.addActiveQuest(quest.getNextQuest(), this.player);
                }
            }
        }
    }
}