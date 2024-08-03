package dev.ftb.mods.ftbquests.quest.task;

import com.mojang.datafixers.util.Either;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftbquests.net.SyncStructuresRequestMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StructureTask extends AbstractBooleanTask {

    private static final ResourceLocation DEFAULT_STRUCTURE = new ResourceLocation("minecraft:mineshaft");

    private static final List<String> KNOWN_STRUCTURES = new ArrayList();

    private Either<ResourceKey<Structure>, TagKey<Structure>> structure = Either.left(ResourceKey.create(Registries.STRUCTURE, DEFAULT_STRUCTURE));

    public StructureTask(long id, Quest quest) {
        super(id, quest);
    }

    public static void syncKnownStructureList(List<String> data) {
        KNOWN_STRUCTURES.clear();
        KNOWN_STRUCTURES.addAll(data);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.STRUCTURE;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("structure", this.getStructure());
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.setStructure(nbt.getString("structure"));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.getStructure());
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.setStructure(buffer.readUtf(1024));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        if (KNOWN_STRUCTURES.isEmpty()) {
            config.addString("structure", this.getStructure(), this::setStructure, "minecraft:mineshaft");
        } else {
            config.addEnum("structure", this.getStructure(), this::setStructure, NameMap.of(DEFAULT_STRUCTURE.toString(), KNOWN_STRUCTURES).create());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.task.ftbquests.structure").append(": ").append(Component.literal(this.getStructure()).withStyle(ChatFormatting.DARK_GREEN));
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
            ServerLevel level = (ServerLevel) player.m_9236_();
            return (Boolean) this.structure.map(key -> level.structureManager().getStructureWithPieceAt(player.m_20183_(), key).isValid(), tag -> level.structureManager().getStructureWithPieceAt(player.m_20183_(), tag).isValid());
        }
    }

    private void setStructure(String resLoc) {
        this.structure = resLoc.startsWith("#") ? Either.right(TagKey.create(Registries.STRUCTURE, this.safeResourceLocation(resLoc.substring(1), DEFAULT_STRUCTURE))) : Either.left(ResourceKey.create(Registries.STRUCTURE, this.safeResourceLocation(resLoc, DEFAULT_STRUCTURE)));
    }

    private String getStructure() {
        return (String) this.structure.map(key -> key.location().toString(), tag -> "#" + tag.location());
    }

    public static void maybeRequestStructureSync() {
        if (KNOWN_STRUCTURES.isEmpty()) {
            new SyncStructuresRequestMessage().sendToServer();
        }
    }
}