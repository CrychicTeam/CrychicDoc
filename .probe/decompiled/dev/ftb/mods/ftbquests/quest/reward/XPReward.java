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

public class XPReward extends Reward {

    private int xp;

    public XPReward(long id, Quest quest, int xp) {
        super(id, quest);
        this.xp = xp;
    }

    public XPReward(long id, Quest quest) {
        this(id, quest, 100);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.XP;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putInt("xp", this.xp);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.xp = nbt.getInt("xp");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarInt(this.xp);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.xp = buffer.readVarInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addInt("xp", this.xp, v -> this.xp = v, 100, 1, Integer.MAX_VALUE).setNameKey("ftbquests.reward.ftbquests.xp");
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        player.giveExperiencePoints(this.xp);
        if (notify) {
            new DisplayRewardToastMessage(this.id, Component.translatable("ftbquests.reward.ftbquests.xp").append(": ").append(Component.literal("+" + this.xp).withStyle(ChatFormatting.GREEN)), Color4I.empty()).sendTo(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.reward.ftbquests.xp").append(": ").append(Component.literal("+" + this.xp).withStyle(ChatFormatting.GREEN));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getButtonText() {
        return "+" + this.xp;
    }
}