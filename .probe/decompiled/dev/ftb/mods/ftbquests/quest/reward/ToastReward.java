package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ToastReward extends Reward {

    private String description = "";

    public ToastReward(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.TOAST;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("description", this.description);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.description = nbt.getString("description");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.description, 32767);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.description = buffer.readUtf(32767);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString("description", this.description, v -> this.description = v, "");
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
    }
}