package dev.ftb.mods.ftbquests.quest.task;

import com.mojang.brigadier.StringReader;
import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ObservationTask extends AbstractBooleanTask {

    private long timer = 0L;

    private ObservationTask.ObserveType observeType = ObservationTask.ObserveType.BLOCK;

    private String toObserve = "minecraft:dirt";

    public ObservationTask(long id, Quest quest) {
        super(id, quest);
    }

    public void setToObserve(String toObserve) {
        this.toObserve = toObserve;
    }

    public long getTimer() {
        return this.timer;
    }

    @Override
    public TaskType getType() {
        return TaskTypes.OBSERVATION;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putLong("timer", this.timer);
        nbt.putInt("observe_type", this.observeType.ordinal());
        nbt.putString("to_observe", this.toObserve);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.timer = nbt.getLong("timer");
        this.observeType = ObservationTask.ObserveType.values()[nbt.getInt("observe_type")];
        this.toObserve = nbt.getString("to_observe");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarLong(this.timer);
        buffer.writeEnum(this.observeType);
        buffer.writeUtf(this.toObserve);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.timer = buffer.readVarLong();
        this.observeType = buffer.readEnum(ObservationTask.ObserveType.class);
        this.toObserve = buffer.readUtf(32767);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addLong("timer", this.timer, v -> this.timer = v, 0L, 0L, 1200L);
        config.addEnum("observe_type", this.observeType, v -> this.observeType = v, ObservationTask.ObserveType.NAME_MAP);
        config.addString("to_observe", this.toObserve, v -> this.toObserve = v, "minecraft:dirt");
    }

    @Override
    public Component getAltTitle() {
        return Component.translatable("ftbquests.task.ftbquests.observation").append(": ").append(Component.literal(this.toObserve).withStyle(ChatFormatting.DARK_GREEN));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onButtonClicked(Button button, boolean canClick) {
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        return true;
    }

    @Override
    public boolean checkOnLogin() {
        return false;
    }

    public boolean observe(Player player, HitResult result) {
        if (this.toObserve.isEmpty()) {
            return false;
        } else if (result instanceof BlockHitResult blockResult) {
            BlockInWorld blockInWorld = new BlockInWorld(player.m_9236_(), blockResult.getBlockPos(), false);
            BlockState state = blockInWorld.getState();
            if (state == null) {
                return false;
            } else {
                Block block = state.m_60734_();
                BlockEntity blockEntity = blockInWorld.getEntity();
                switch(this.observeType) {
                    case BLOCK:
                        return String.valueOf(RegistrarManager.getId(block, Registries.BLOCK)).equals(this.toObserve);
                    case BLOCK_TAG:
                        return (Boolean) this.asTagRL(this.toObserve).map(rl -> state.m_204336_(TagKey.create(Registries.BLOCK, rl))).orElse(false);
                    case BLOCK_STATE:
                        BlockInput stateMatch = this.tryMatchBlock(this.toObserve, false);
                        return stateMatch != null && stateMatch.test(blockInWorld);
                    case BLOCK_ENTITY:
                        BlockInput stateNbtMatch = this.tryMatchBlock(this.toObserve, true);
                        return stateNbtMatch != null && stateNbtMatch.test(blockInWorld);
                    case BLOCK_ENTITY_TYPE:
                        return blockEntity != null && this.toObserve.equals(String.valueOf(RegistrarManager.getId(blockEntity.getType(), Registries.BLOCK_ENTITY_TYPE)));
                    default:
                        return false;
                }
            }
        } else {
            if (result instanceof EntityHitResult entityResult) {
                if (this.observeType == ObservationTask.ObserveType.ENTITY_TYPE) {
                    return this.toObserve.equals(String.valueOf(RegistrarManager.getId(entityResult.getEntity().getType(), Registries.ENTITY_TYPE)));
                }
                if (this.observeType == ObservationTask.ObserveType.ENTITY_TYPE_TAG) {
                    return (Boolean) this.asTagRL(this.toObserve).map(rl -> entityResult.getEntity().getType().is(TagKey.create(Registries.ENTITY_TYPE, rl))).orElse(false);
                }
            }
            return false;
        }
    }

    private Optional<ResourceLocation> asTagRL(String str) {
        try {
            return Optional.of(new ResourceLocation(str.startsWith("#") ? str.substring(1) : str));
        } catch (ResourceLocationException var3) {
            return Optional.empty();
        }
    }

    private BlockInput tryMatchBlock(String string, boolean parseNbt) {
        try {
            BlockStateParser.BlockResult blockStateParser = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.m_255303_(), new StringReader(string), false);
            return new BlockInput(blockStateParser.blockState(), blockStateParser.properties().keySet(), parseNbt ? blockStateParser.nbt() : null);
        } catch (Exception var4) {
            return null;
        }
    }

    static enum ObserveType {

        BLOCK,
        BLOCK_TAG,
        BLOCK_STATE,
        BLOCK_ENTITY,
        BLOCK_ENTITY_TYPE,
        ENTITY_TYPE,
        ENTITY_TYPE_TAG;

        public static final NameMap<ObservationTask.ObserveType> NAME_MAP = NameMap.of(BLOCK, values()).id(v -> v.name().toLowerCase()).create();
    }
}