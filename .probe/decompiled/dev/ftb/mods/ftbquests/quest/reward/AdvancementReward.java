package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.util.KnownServerRegistries;
import dev.ftb.mods.ftbquests.quest.Quest;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AdvancementReward extends Reward {

    private ResourceLocation advancement = new ResourceLocation("minecraft:story/root");

    private String criterion = "";

    public AdvancementReward(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public RewardType getType() {
        return RewardTypes.ADVANCEMENT;
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
            config.addEnum("advancement", this.advancement, v -> this.advancement = v, NameMap.of((ResourceLocation) KnownServerRegistries.client.advancements.keySet().iterator().next(), (ResourceLocation[]) KnownServerRegistries.client.advancements.keySet().toArray(new ResourceLocation[0])).icon(resourceLocation -> ItemIcon.getItemIcon(((KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.get(resourceLocation)).icon)).name(resourceLocation -> ((KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.get(resourceLocation)).name).create()).setNameKey("ftbquests.reward.ftbquests.advancement");
        } else {
            config.addString("advancement", this.advancement.toString(), v -> this.advancement = new ResourceLocation(v), "minecraft:story/root").setNameKey("ftbquests.reward.ftbquests.advancement");
        }
        config.addString("criterion", this.criterion, v -> this.criterion = v, "");
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        Advancement a = player.server.getAdvancements().getAdvancement(this.advancement);
        if (a != null) {
            if (this.criterion.isEmpty()) {
                for (String s : a.getCriteria().keySet()) {
                    player.getAdvancements().award(a, s);
                }
            } else {
                player.getAdvancements().award(a, this.criterion);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        KnownServerRegistries.AdvancementInfo info = KnownServerRegistries.client == null ? null : (KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.get(this.advancement);
        return (Component) (info != null && info.name.getContents() != ComponentContents.EMPTY ? Component.translatable("ftbquests.reward.ftbquests.advancement").append(": ").append(info.name.copy().withStyle(ChatFormatting.YELLOW)) : super.getAltTitle());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        KnownServerRegistries.AdvancementInfo info = KnownServerRegistries.client == null ? null : (KnownServerRegistries.AdvancementInfo) KnownServerRegistries.client.advancements.get(this.advancement);
        return info != null && !info.icon.isEmpty() ? ItemIcon.getItemIcon(info.icon) : super.getAltIcon();
    }
}