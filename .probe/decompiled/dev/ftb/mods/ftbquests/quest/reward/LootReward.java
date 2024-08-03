package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.client.gui.RewardNotificationsScreen;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class LootReward extends RandomReward {

    public LootReward(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.LOOT;
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        RewardTable table = this.getTable();
        if (table != null) {
            for (WeightedReward wr : table.generateWeightedRandomRewards(player.m_217043_(), 1, true)) {
                wr.getReward().claim(player, notify);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.getTable() != null) {
            this.getTable().addMouseOverText(list, true, true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onButtonClicked(Button button, boolean canClick) {
        if (canClick) {
            new RewardNotificationsScreen().openGui();
        }
        super.onButtonClicked(button, canClick);
    }

    @Override
    public boolean getExcludeFromClaimAll() {
        return true;
    }

    @Override
    public boolean automatedClaimPre(BlockEntity blockEntity, List<ItemStack> items, RandomSource random, UUID playerId, @Nullable ServerPlayer player) {
        return false;
    }

    @Override
    public void automatedClaimPost(BlockEntity blockEntity, UUID playerId, @Nullable ServerPlayer player) {
    }
}