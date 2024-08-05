package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.util.KnownServerRegistries;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DimensionTask extends AbstractBooleanTask {

    private ResourceKey<Level> dimension = Level.NETHER;

    public DimensionTask(long id, Quest quest) {
        super(id, quest);
    }

    public DimensionTask withDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        return this;
    }

    @Override
    public TaskType getType() {
        return TaskTypes.DIMENSION;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("dimension", this.dimension.location().toString());
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("dimension")));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(this.dimension.location());
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.dimension = ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        if (KnownServerRegistries.client != null && !KnownServerRegistries.client.dimensions.isEmpty()) {
            config.addEnum("dim", this.dimension.location(), v -> this.dimension = ResourceKey.create(Registries.DIMENSION, v), NameMap.of((ResourceLocation) KnownServerRegistries.client.dimensions.iterator().next(), (ResourceLocation[]) KnownServerRegistries.client.dimensions.toArray(new ResourceLocation[0])).create());
        } else {
            config.addString("dim", this.dimension.location().toString(), v -> this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(v)), "minecraft:the_nether");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.task.ftbquests.dimension").append(": ").append(Component.literal(this.dimension.location().toString()).withStyle(ChatFormatting.DARK_GREEN));
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 100;
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        return !player.isSpectator() && player.m_9236_().dimension() == this.dimension;
    }
}