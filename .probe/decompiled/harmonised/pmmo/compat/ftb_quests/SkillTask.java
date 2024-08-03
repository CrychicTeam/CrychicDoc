package harmonised.pmmo.compat.ftb_quests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.IDataStorage;
import harmonised.pmmo.setup.datagen.LangProvider;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SkillTask extends Task {

    public static TaskType SKILL = FTBQHandler.SKILL;

    public String skill = "mining";

    public int requiredLevel = 1;

    public SkillTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return SKILL;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("skill", this.skill);
        nbt.putInt("requiredLevel", this.requiredLevel);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.skill = nbt.getString("skill");
        this.requiredLevel = nbt.getInt("requiredLevel");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.skill, 32767);
        buffer.writeInt(this.requiredLevel);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.skill = buffer.readUtf(32767);
        this.requiredLevel = buffer.readInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addEnum("skill", this.skill, input -> this.skill = (String) input, NameMap.of("mining", SkillsConfig.SKILLS.get().keySet().toArray()).create());
        config.addInt("requiredLevel", this.requiredLevel, input -> this.requiredLevel = input, this.requiredLevel, 1, Config.MAX_LEVEL.get());
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (!teamData.isCompleted(this)) {
            IDataStorage data = Core.get(player.m_9236_()).getData();
            long xp = (long) data.getPlayerSkillLevel(this.skill, player.m_20148_());
            SkillData config = (SkillData) SkillsConfig.SKILLS.get().getOrDefault(this.skill, SkillData.Builder.getDefault());
            if (config.isSkillGroup() && config.getUseTotalLevels()) {
                xp = (long) ((Map) config.groupedSkills().get()).entrySet().stream().map(entry -> (int) ((Double) entry.getValue() * (double) data.getPlayerSkillLevel((String) entry.getKey(), player.m_20148_()))).mapToInt(Integer::intValue).sum();
            }
            teamData.setProgress(this, xp);
        }
    }

    @Override
    public long getMaxProgress() {
        return (long) this.requiredLevel;
    }

    @Override
    public Component getAltTitle() {
        return LangProvider.FTBQ_SKILL_TITLE.asComponent(this.requiredLevel, LangProvider.skill(this.skill));
    }
}