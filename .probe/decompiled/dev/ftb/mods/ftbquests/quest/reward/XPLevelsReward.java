package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbquests.net.DisplayRewardToastMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class XPLevelsReward extends Reward {

    private int xpLevels;

    public XPLevelsReward(long id, Quest quest, int x) {
        super(id, quest);
        this.xpLevels = x;
    }

    public XPLevelsReward(long id, Quest quest) {
        this(id, quest, 5);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.XP_LEVELS;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putInt("xp_levels", this.xpLevels);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.xpLevels = nbt.getInt("xp_levels");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarInt(this.xpLevels);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.xpLevels = buffer.readVarInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addInt("xp_levels", this.xpLevels, v -> this.xpLevels = v, 1, 1, Integer.MAX_VALUE).setNameKey("ftbquests.reward.ftbquests.xp_levels");
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        player.giveExperienceLevels(this.xpLevels);
        if (notify) {
            new DisplayRewardToastMessage(this.id, Component.translatable("ftbquests.reward.ftbquests.xp_levels").append(": ").append(Component.literal("+" + this.xpLevels).withStyle(ChatFormatting.GREEN)), Color4I.empty()).sendTo(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.reward.ftbquests.xp_levels").append(": ").append(Component.literal("+" + this.xpLevels).withStyle(ChatFormatting.GREEN));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getButtonText() {
        return "+" + this.xpLevels;
    }
}