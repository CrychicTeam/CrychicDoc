package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.util.KnownServerRegistries;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AdvancementTask extends AbstractBooleanTask {

    private ResourceLocation advancement = new ResourceLocation("minecraft:story/root");

    private String criterion = "";

    public AdvancementTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.ADVANCEMENT;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("advancement", this.advancement.toString());
        nbt.putString("criterion", this.criterion);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.advancement = new ResourceLocation(nbt.getString("advancement"));
        this.criterion = nbt.getString("criterion");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(this.advancement);
        buffer.writeUtf(this.criterion, 32767);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.advancement = buffer.readResourceLocation();
        this.criterion = buffer.readUtf(32767);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        if (KnownServerRegistries.client != null && !KnownServerRegistries.client.advancements.isEmpty()) {
            Map<ResourceLocation, KnownServerRegistries.AdvancementInfo> advancements = KnownServerRegistries.client.advancements;
            KnownServerRegistries.AdvancementInfo def = (KnownServerRegistries.AdvancementInfo) advancements.values().iterator().next();
            config.addEnum("advancement", this.advancement, v -> this.advancement = v, NameMap.of(def.id, (ResourceLocation[]) advancements.keySet().toArray(new ResourceLocation[0])).icon(id -> ItemIcon.getItemIcon(((KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.getOrDefault(id, def)).icon)).name(id -> ((KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.getOrDefault(id, def)).name).create()).setNameKey("ftbquests.task.ftbquests.advancement");
        } else {
            config.addString("advancement", this.advancement.toString(), v -> this.advancement = new ResourceLocation(v), "minecraft:story/root").setNameKey("ftbquests.task.ftbquests.advancement");
        }
        config.addString("criterion", this.criterion, v -> this.criterion = v, "");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        KnownServerRegistries.AdvancementInfo info = KnownServerRegistries.client == null ? null : (KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.get(this.advancement);
        return (Component) (info != null && info.name.getContents() != ComponentContents.EMPTY ? Component.translatable("ftbquests.task.ftbquests.advancement").append(": ").append(Component.literal("").append(info.name).withStyle(ChatFormatting.YELLOW)) : super.getAltTitle());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        KnownServerRegistries.AdvancementInfo info = KnownServerRegistries.client == null ? null : (KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.get(this.advancement);
        return info != null && !info.icon.isEmpty() ? ItemIcon.getItemIcon(info.icon) : super.getAltIcon();
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 5;
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        Advancement a = player.server.getAdvancements().getAdvancement(this.advancement);
        if (a == null) {
            return false;
        } else {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(a);
            if (this.criterion.isEmpty()) {
                return progress.isDone();
            } else {
                CriterionProgress criterionProgress = progress.getCriterion(this.criterion);
                return criterionProgress != null && criterionProgress.isDone();
            }
        }
    }
}