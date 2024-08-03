package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class XPTask extends Task implements ISingleLongValueTask {

    private long value = 1L;

    private boolean points = false;

    public XPTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.XP;
    }

    @Override
    public long getMaxProgress() {
        return this.value;
    }

    @Override
    public String formatMaxProgress() {
        return Long.toUnsignedString(this.points && this.value <= 2147483647L ? (long) getLevelForExperience((int) this.value) : this.value);
    }

    @Override
    public String formatProgress(TeamData teamData, long progress) {
        return Long.toUnsignedString(this.points && this.value <= 2147483647L ? (long) getLevelForExperience((int) progress) : progress);
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putLong("value", this.value);
        nbt.putBoolean("points", this.points);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.value = nbt.getLong("value");
        this.points = nbt.getBoolean("points");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarLong(this.value);
        buffer.writeBoolean(this.points);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.value = buffer.readVarLong();
        this.points = buffer.readBoolean();
    }

    @Override
    public void setValue(long v) {
        this.value = v;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addLong("value", this.value, v -> this.value = v, 1L, 1L, Long.MAX_VALUE);
        config.addBool("points", this.points, v -> this.points = v, false);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.reward.ftbquests.xp_levels").append(": ").append(Component.literal(this.formatMaxProgress()).withStyle(ChatFormatting.RED));
    }

    @Override
    public boolean consumesResources() {
        return true;
    }

    public static int getPlayerXP(Player player) {
        return (int) ((float) getExperienceForLevel(player.experienceLevel) + player.experienceProgress * (float) player.getXpNeededForNextLevel());
    }

    public static void addPlayerXP(Player player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.totalExperience = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
    }

    public static int xpBarCap(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    private static int sum(int n, int a0, int d) {
        return n * (2 * a0 + (n - 1) * d) / 2;
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        } else if (level <= 15) {
            return sum(level, 7, 2);
        } else {
            return level <= 30 ? 315 + sum(level - 15, 37, 5) : 1395 + sum(level - 30, 112, 9);
        }
    }

    public static int getLevelForExperience(int targetXp) {
        int level = 0;
        while (true) {
            int xpToNextLevel = xpBarCap(level);
            if (targetXp < xpToNextLevel) {
                return level;
            }
            level++;
            targetXp -= xpToNextLevel;
        }
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (this.checkTaskSequence(teamData)) {
            int add = (int) Math.min(this.points ? (long) getPlayerXP(player) : (long) player.f_36078_, Math.min(this.value - teamData.getProgress(this), 2147483647L));
            if (add > 0) {
                if (this.points) {
                    addPlayerXP(player, -add);
                    player.giveExperienceLevels(0);
                } else {
                    player.giveExperienceLevels(-add);
                }
                teamData.addProgress(this, (long) add);
            }
        }
    }
}