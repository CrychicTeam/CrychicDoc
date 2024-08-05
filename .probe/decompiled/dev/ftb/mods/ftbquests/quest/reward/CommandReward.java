package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CommandReward extends Reward {

    private String command = "/say Hi, @p!";

    private boolean elevatePerms;

    private boolean silent;

    public CommandReward(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.COMMAND;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("command", this.command);
        if (this.elevatePerms) {
            nbt.putBoolean("elevate_perms", true);
        }
        if (this.silent) {
            nbt.putBoolean("silent", true);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.command = nbt.getString("command");
        this.elevatePerms = nbt.getBoolean("elevate_perms");
        this.silent = nbt.getBoolean("silent");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.command, 32767);
        buffer.writeBoolean(this.elevatePerms);
        buffer.writeBoolean(this.silent);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.command = buffer.readUtf(32767);
        this.elevatePerms = buffer.readBoolean();
        this.silent = buffer.readBoolean();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString("command", this.command, v -> this.command = v, "/say Hi, @team!").setNameKey("ftbquests.reward.ftbquests.command");
        config.addBool("elevate", this.elevatePerms, v -> this.elevatePerms = v, false);
        config.addBool("silent", this.silent, v -> this.silent = v, false);
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        Map<String, Object> overrides = new HashMap();
        overrides.put("p", player.m_36316_().getName());
        BlockPos pos = player.m_20183_();
        overrides.put("x", pos.m_123341_());
        overrides.put("y", pos.m_123342_());
        overrides.put("z", pos.m_123343_());
        if (this.getQuestChapter() != null) {
            overrides.put("chapter", this.getQuestChapter());
        }
        overrides.put("quest", this.quest);
        String cmd = this.command;
        for (Entry<String, Object> entry : overrides.entrySet()) {
            if (entry.getValue() != null) {
                cmd = cmd.replace("{" + (String) entry.getKey() + "}", entry.getValue().toString());
            }
        }
        CommandSourceStack source = player.m_20203_();
        if (this.elevatePerms) {
            source = source.withPermission(2);
        }
        if (this.silent) {
            source = source.withSuppressedOutput();
        }
        player.server.getCommands().performPrefixedCommand(source, cmd);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.reward.ftbquests.command").append(": ").append(Component.literal(this.command).withStyle(ChatFormatting.RED));
    }
}