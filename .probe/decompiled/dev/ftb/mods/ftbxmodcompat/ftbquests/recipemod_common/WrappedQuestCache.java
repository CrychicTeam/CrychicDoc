package dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardAutoClaim;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public class WrappedQuestCache {

    private final List<WrappedQuest> wrappedQuestsCache = new ArrayList();

    private final ItemStackToListCache<WrappedQuest> inputCache = new ItemStackToListCache<>();

    private final ItemStackToListCache<WrappedQuest> outputCache = new ItemStackToListCache<>();

    private boolean needsRefresh = true;

    public List<WrappedQuest> getCachedItems() {
        if (this.needsRefresh) {
            this.rebuildWrappedQuestCache();
            this.needsRefresh = false;
        }
        return this.wrappedQuestsCache;
    }

    public void clear() {
        this.needsRefresh = true;
        this.inputCache.clear();
        this.outputCache.clear();
    }

    private void rebuildWrappedQuestCache() {
        this.wrappedQuestsCache.clear();
        if (ClientQuestFile.exists()) {
            ClientQuestFile.INSTANCE.forAllQuests(quest -> {
                if (ClientQuestFile.INSTANCE.selfTeamData.canStartTasks(quest) && !quest.getRewards().isEmpty() && quest.showInRecipeMod()) {
                    List<Reward> rewards = quest.getRewards().stream().filter(reward -> reward.getAutoClaimType() != RewardAutoClaim.INVISIBLE && reward.getIcon().getIngredient() != null).toList();
                    if (!rewards.isEmpty()) {
                        this.wrappedQuestsCache.add(new WrappedQuest(quest, rewards));
                    }
                }
            });
        }
    }

    public List<WrappedQuest> findQuestsWithInput(ItemStack stack) {
        return this.inputCache.getList(stack, k -> this.getCachedItems().stream().filter(q -> q.hasInput(stack)).toList());
    }

    public List<WrappedQuest> findQuestsWithOutput(ItemStack stack) {
        return this.outputCache.getList(stack, k -> this.getCachedItems().stream().filter(q -> q.hasOutput(stack)).toList());
    }
}