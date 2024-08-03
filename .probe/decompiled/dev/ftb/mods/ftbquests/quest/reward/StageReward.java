package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.integration.stages.StageHelper;
import dev.ftb.mods.ftbquests.quest.Quest;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StageReward extends Reward {

    private String stage = "";

    private boolean remove = false;

    public StageReward(long id, Quest quest) {
        super(id, quest);
        this.autoclaim = RewardAutoClaim.INVISIBLE;
    }

    @Override
    public RewardType getType() {
        return RewardTypes.STAGE;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("stage", this.stage);
        if (this.remove) {
            nbt.putBoolean("remove", true);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.stage = nbt.getString("stage");
        this.remove = nbt.getBoolean("remove");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.stage, 32767);
        buffer.writeBoolean(this.remove);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.stage = buffer.readUtf(32767);
        this.remove = buffer.readBoolean();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString("stage", this.stage, v -> this.stage = v, "").setNameKey("ftbquests.reward.ftbquests.gamestage");
        config.addBool("remove", this.remove, v -> this.remove = v, false);
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        if (this.remove) {
            StageHelper.INSTANCE.getProvider().remove(player, this.stage);
        } else {
            StageHelper.INSTANCE.getProvider().add(player, this.stage);
        }
        if (notify) {
            if (this.remove) {
                player.sendSystemMessage(Component.translatable("commands.gamestage.remove.target", this.stage), true);
            } else {
                player.sendSystemMessage(Component.translatable("commands.gamestage.add.target", this.stage), true);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.reward.ftbquests.gamestage").append(": ").append(Component.literal(this.stage).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean ignoreRewardBlocking() {
        return true;
    }

    @Override
    protected boolean isIgnoreRewardBlockingHardcoded() {
        return true;
    }
}