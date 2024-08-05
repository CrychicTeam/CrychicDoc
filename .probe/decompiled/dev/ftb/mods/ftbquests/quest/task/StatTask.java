package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StatTask extends Task {

    private ResourceLocation stat;

    private int value = 1;

    public StatTask(long id, Quest quest) {
        super(id, quest);
        this.stat = Stats.MOB_KILLS;
    }

    @Override
    public TaskType getType() {
        return TaskTypes.STAT;
    }

    @Override
    public long getMaxProgress() {
        return (long) this.value;
    }

    @Override
    public String formatMaxProgress() {
        return Integer.toString(this.value);
    }

    @Override
    public String formatProgress(TeamData teamData, long progress) {
        return Long.toUnsignedString(progress);
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("stat", this.stat.toString());
        nbt.putInt("value", this.value);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.stat = new ResourceLocation(nbt.getString("stat"));
        this.value = nbt.getInt("value");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(this.stat);
        buffer.writeVarInt(this.value);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.stat = buffer.readResourceLocation();
        this.value = buffer.readVarInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        List<ResourceLocation> list = new ArrayList();
        Stats.CUSTOM.iterator().forEachRemaining(s -> list.add((ResourceLocation) s.getValue()));
        config.addEnum("stat", this.stat, v -> this.stat = v, NameMap.of(Stats.MOB_KILLS, list).name(v -> Component.translatable("stat." + v.getNamespace() + "." + v.getPath())).create());
        config.addInt("value", this.value, v -> this.value = v, 1, 1, Integer.MAX_VALUE);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("stat." + this.stat.getNamespace() + "." + this.stat.getPath());
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 3;
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (!teamData.isCompleted(this) && this.checkTaskSequence(teamData)) {
            ResourceLocation statId = BuiltInRegistries.CUSTOM_STAT.get(this.stat);
            if (statId == null) {
                statId = BuiltInRegistries.CUSTOM_STAT.get(new ResourceLocation(this.stat.getPath()));
            }
            if (statId != null) {
                int set = Math.min(this.value, player.getStats().m_13015_(Stats.CUSTOM.get(statId)));
                if ((long) set > teamData.getProgress(this)) {
                    teamData.setProgress(this, (long) set);
                }
            }
        }
    }
}