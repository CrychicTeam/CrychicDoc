package dev.ftb.mods.ftbquests.quest.task;

import com.mojang.datafixers.util.Either;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomeTask extends AbstractBooleanTask {

    private static final ResourceKey<Biome> DEFAULT_BIOME = Biomes.PLAINS;

    private static final List<String> KNOWN_BIOMES = new ArrayList();

    private Either<ResourceKey<Biome>, TagKey<Biome>> biome = Either.left(DEFAULT_BIOME);

    public BiomeTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.BIOME;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("biome", this.getBiome());
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.setBiome(nbt.getString("biome"));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.getBiome());
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.setBiome(buffer.readUtf(32767));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addEnum("biome", this.getBiome(), this::setBiome, NameMap.of(DEFAULT_BIOME.location().toString(), this.getKnownBiomes()).create());
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.task.ftbquests.biome").append(": ").append(Component.literal(this.getBiome())).withStyle(ChatFormatting.DARK_GREEN);
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public boolean checkOnLogin() {
        return false;
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        if (player.isSpectator()) {
            return false;
        } else {
            Holder<Biome> biomeHolder = player.m_9236_().m_204166_(player.m_20183_());
            return (Boolean) this.biome.map(key -> (Boolean) biomeHolder.unwrapKey().map(k -> k == key).orElse(false), tagKey -> {
                Registry<Biome> reg = (Registry<Biome>) player.m_9236_().registryAccess().registry(Registries.BIOME).orElseThrow();
                return (Boolean) reg.getTag(tagKey).map(holderSet -> holderSet.contains(biomeHolder)).orElse(false);
            });
        }
    }

    private String getBiome() {
        return (String) this.biome.map(key -> key.location().toString(), tagKey -> "#" + tagKey.location());
    }

    private void setBiome(String str) {
        this.biome = str.startsWith("#") ? Either.right(TagKey.create(Registries.BIOME, this.safeResourceLocation(str.substring(1), DEFAULT_BIOME.location()))) : Either.left(ResourceKey.create(Registries.BIOME, this.safeResourceLocation(str, DEFAULT_BIOME.location())));
    }

    private List<String> getKnownBiomes() {
        if (KNOWN_BIOMES.isEmpty()) {
            RegistryAccess registryAccess = FTBQuestsClient.getClientPlayer().m_9236_().registryAccess();
            KNOWN_BIOMES.addAll(registryAccess.registryOrThrow(Registries.BIOME).registryKeySet().stream().map(o -> o.location().toString()).sorted(String::compareTo).toList());
            KNOWN_BIOMES.addAll(registryAccess.registryOrThrow(Registries.BIOME).getTagNames().map(o -> "#" + o.location()).sorted(String::compareTo).toList());
        }
        return KNOWN_BIOMES;
    }
}