package dev.ftb.mods.ftbquests.quest.task;

import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class KillTask extends Task {

    private static final ResourceLocation ZOMBIE = new ResourceLocation("minecraft:zombie");

    private ResourceLocation entity = ZOMBIE;

    private long value = 100L;

    public KillTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.KILL;
    }

    @Override
    public long getMaxProgress() {
        return this.value;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("entity", this.entity.toString());
        nbt.putLong("value", this.value);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.entity = new ResourceLocation(nbt.getString("entity"));
        this.value = nbt.getLong("value");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.entity.toString(), 32767);
        buffer.writeVarLong(this.value);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.entity = new ResourceLocation(buffer.readUtf(32767));
        this.value = (long) buffer.readVarInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        List<ResourceLocation> ids = new ArrayList(BuiltInRegistries.ENTITY_TYPE.m_6566_());
        config.addEnum("entity", this.entity, v -> this.entity = v, NameMap.of(ZOMBIE, ids).nameKey(v -> "entity." + v.getNamespace() + "." + v.getPath()).icon(v -> {
            SpawnEggItem item = SpawnEggItem.byId(BuiltInRegistries.ENTITY_TYPE.get(v));
            return ItemIcon.getItemIcon((Item) (item != null ? item : Items.SPAWNER));
        }).create(), ZOMBIE);
        config.addLong("value", this.value, v -> this.value = v, 100L, 1L, Long.MAX_VALUE);
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.task.ftbquests.kill.title", this.formatMaxProgress(), Component.translatable("entity." + this.entity.getNamespace() + "." + this.entity.getPath()));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        SpawnEggItem item = SpawnEggItem.byId(BuiltInRegistries.ENTITY_TYPE.get(this.entity));
        return ItemIcon.getItemIcon((Item) (item != null ? item : Items.SPAWNER));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onButtonClicked(Button button, boolean canClick) {
    }

    public void kill(TeamData teamData, LivingEntity e) {
        if (!teamData.isCompleted(this) && this.entity.equals(RegistrarManager.getId(e.m_6095_(), Registries.ENTITY_TYPE))) {
            teamData.addProgress(this, 1L);
        }
    }
}