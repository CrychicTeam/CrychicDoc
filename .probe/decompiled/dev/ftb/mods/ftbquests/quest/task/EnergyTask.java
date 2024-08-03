package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftbquests.client.EnergyTaskClientData;
import dev.ftb.mods.ftbquests.quest.Quest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EnergyTask extends Task implements ISingleLongValueTask {

    private long value = 1000L;

    private long maxInput = 1000L;

    public EnergyTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public long getMaxProgress() {
        return this.value;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putLong("value", this.value);
        if (this.maxInput > 0L) {
            nbt.putLong("max_input", this.maxInput);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.value = nbt.getLong("value");
        if (this.value < 1L) {
            this.value = 1L;
        }
        this.maxInput = nbt.getLong("max_input");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarLong(this.value);
        buffer.writeVarLong(this.maxInput);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.value = buffer.readVarLong();
        this.maxInput = buffer.readVarLong();
    }

    public long getValue() {
        return this.value;
    }

    @Override
    public void setValue(long v) {
        this.value = v;
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.literal(StringUtils.formatDouble((double) this.value, true));
    }

    @Override
    public boolean consumesResources() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addLong("value", this.value, v -> this.value = v, 1000L, 1L, Long.MAX_VALUE);
        config.addLong("max_input", this.maxInput, v -> this.maxInput = v, 1000L, 0L, 2147483647L).setNameKey("ftbquests.task.max_input");
    }

    public abstract EnergyTaskClientData getClientData();

    public long getMaxInput() {
        return this.maxInput;
    }
}