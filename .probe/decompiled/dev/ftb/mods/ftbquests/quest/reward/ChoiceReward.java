package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.client.gui.SelectChoiceRewardScreen;
import dev.ftb.mods.ftbquests.quest.Quest;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class ChoiceReward extends RandomReward {

    public ChoiceReward(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.CHOICE;
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.getTable() != null) {
            this.getTable().addMouseOverText(list, false, false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onButtonClicked(Button button, boolean canClick) {
        if (canClick) {
            button.playClickSound();
            new SelectChoiceRewardScreen(this).openGui();
        }
    }

    @Override
    public boolean getExcludeFromClaimAll() {
        return true;
    }

    @Override
    public boolean automatedClaimPre(BlockEntity blockEntity, List<ItemStack> items, RandomSource random, UUID playerId, @Nullable ServerPlayer player) {
        return false;
    }
}