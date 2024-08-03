package dev.ftb.mods.ftbquests.block.entity;

import dev.ftb.mods.ftblibrary.config.BooleanConfig;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import dev.ftb.mods.ftbquests.block.FTBQuestsBlocks;
import dev.ftb.mods.ftbquests.block.TaskScreenBlock;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.net.TaskScreenConfigResponse;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.util.ConfigQuestObject;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TaskScreenBlockEntity extends BlockEntity implements ITaskScreen {

    private long taskId = 0L;

    private Task task = null;

    private boolean indestructible = false;

    private boolean inputOnly = false;

    private boolean textShadow = false;

    private ItemStack inputModeIcon = ItemStack.EMPTY;

    private ItemStack skin = ItemStack.EMPTY;

    @NotNull
    private UUID teamId = Util.NIL_UUID;

    public float[] fakeTextureUV = null;

    private TeamData cachedTeamData = null;

    public TaskScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType<?>) FTBQuestsBlockEntities.CORE_TASK_SCREEN.get(), blockPos, blockState);
    }

    public Task getTask() {
        if (this.task == null && this.taskId != 0L || this.task != null && this.task.id != this.taskId) {
            this.task = FTBQuestsAPI.api().getQuestFile(this.f_58857_.isClientSide).getTask(this.taskId);
        }
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
        this.taskId = task == null ? 0L : task.id;
        this.m_6596_();
    }

    @Override
    public boolean isInputOnly() {
        return this.inputOnly;
    }

    public void setInputOnly(boolean inputOnly) {
        this.inputOnly = inputOnly;
        this.m_6596_();
    }

    public ItemStack getInputModeIcon() {
        return this.inputModeIcon;
    }

    public void setInputModeIcon(ItemStack inputModeIcon) {
        this.inputModeIcon = inputModeIcon;
        this.m_6596_();
    }

    @Override
    public boolean isIndestructible() {
        return this.indestructible;
    }

    public void setIndestructible(boolean indestructible) {
        this.indestructible = indestructible;
        this.m_6596_();
    }

    @Override
    public ItemStack getSkin() {
        return this.skin;
    }

    public void setSkin(ItemStack skin) {
        this.skin = skin;
        this.fakeTextureUV = null;
    }

    public boolean isTextShadow() {
        return this.textShadow;
    }

    public void setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
    }

    public void setTeamId(@NotNull UUID teamId) {
        this.teamId = teamId;
        this.cachedTeamData = null;
    }

    @NotNull
    @Override
    public UUID getTeamId() {
        return this.teamId;
    }

    public TeamData getCachedTeamData() {
        if (this.cachedTeamData == null) {
            BaseQuestFile f = FTBQuestsAPI.api().getQuestFile(this.f_58857_.isClientSide);
            this.cachedTeamData = f.getNullableTeamData(this.getTeamId());
        }
        return this.cachedTeamData;
    }

    @Override
    public Optional<TaskScreenBlockEntity> getCoreScreen() {
        return Optional.of(this);
    }

    public void removeAllAuxScreens() {
        if (this.f_58857_ != null && this.m_58900_().m_60734_() instanceof TaskScreenBlock tsb) {
            BlockPos.betweenClosedStream(TaskScreenBlock.getMultiblockBounds(this.m_58899_(), tsb.getSize(), (Direction) this.m_58900_().m_61143_(TaskScreenBlock.FACING))).forEach(pos -> {
                if (this.f_58857_.getBlockState(pos).m_60734_() == FTBQuestsBlocks.AUX_SCREEN.get()) {
                    this.f_58857_.removeBlock(pos, false);
                }
            });
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.teamId = compoundTag.hasUUID("TeamID") ? compoundTag.getUUID("TeamID") : Util.NIL_UUID;
        this.taskId = compoundTag.getLong("TaskID");
        this.skin = compoundTag.contains("Skin") ? ItemStack.of(compoundTag.getCompound("Skin")) : ItemStack.EMPTY;
        this.indestructible = compoundTag.getBoolean("Indestructible");
        this.inputOnly = compoundTag.getBoolean("InputOnly");
        this.inputModeIcon = compoundTag.contains("InputModeIcon") ? ItemStack.of(compoundTag.getCompound("InputModeIcon")) : ItemStack.EMPTY;
        this.textShadow = compoundTag.getBoolean("TextShadow");
        this.task = null;
        this.fakeTextureUV = null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.teamId != Util.NIL_UUID) {
            compoundTag.putUUID("TeamID", this.teamId);
        }
        if (this.taskId != 0L) {
            compoundTag.putLong("TaskID", this.taskId);
        }
        if (!this.skin.isEmpty()) {
            compoundTag.put("Skin", this.skin.save(new CompoundTag()));
        }
        if (this.indestructible) {
            compoundTag.putBoolean("Indestructible", true);
        }
        if (this.inputOnly) {
            compoundTag.putBoolean("InputOnly", true);
        }
        if (!this.inputModeIcon.isEmpty()) {
            compoundTag.put("InputModeIcon", this.inputModeIcon.save(new CompoundTag()));
        }
        if (this.textShadow) {
            compoundTag.putBoolean("TextShadow", true);
        }
    }

    public ConfigGroup fillConfigGroup(TeamData data) {
        ConfigGroup cg0 = new ConfigGroup("task_screen", accepted -> {
            if (accepted) {
                new TaskScreenConfigResponse(this).sendToServer();
            }
        });
        cg0.setNameKey(this.m_58900_().m_60734_().getDescriptionId());
        ConfigGroup cg = cg0.getOrCreateSubgroup("screen");
        cg.add("task", new ConfigQuestObject(o -> this.isSuitableTask(data, o)), this.getTask(), this::setTask, null).setNameKey("ftbquests.task");
        cg.add("skin", new ItemStackConfig(true, true), this.getSkin(), this::setSkin, ItemStack.EMPTY).setNameKey("block.ftbquests.screen.skin");
        cg.add("text_shadow", new BooleanConfig(), this.isTextShadow(), this::setTextShadow, false).setNameKey("block.ftbquests.screen.text_shadow");
        cg.add("indestructible", new BooleanConfig(), this.isIndestructible(), this::setIndestructible, false).setNameKey("block.ftbquests.screen.indestructible");
        cg.add("input_only", new BooleanConfig(), this.isInputOnly(), this::setInputOnly, false).setNameKey("block.ftbquests.screen.input_only");
        cg.add("input_icon", new ItemStackConfig(true, true), this.getInputModeIcon(), this::setInputModeIcon, ItemStack.EMPTY).setNameKey("block.ftbquests.screen.input_mode_icon");
        return cg0;
    }

    private boolean isSuitableTask(TeamData data, QuestObjectBase o) {
        if (o instanceof Task t && (data.getCanEdit(FTBQuestsClient.getClientPlayer()) || data.canStartTasks(t.getQuest())) && t.consumesResources()) {
            return true;
        }
        return false;
    }

    public float[] getFakeTextureUV() {
        if (this.fakeTextureUV == null) {
            if (!this.skin.isEmpty() && this.skin.getItem() instanceof BlockItem bi) {
                BlockState state = bi.getBlock().defaultBlockState();
                Direction facing = (Direction) this.m_58900_().m_61143_(TaskScreenBlock.FACING);
                if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                    state = (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, facing);
                } else if (state.m_61138_(BlockStateProperties.FACING)) {
                    state = (BlockState) state.m_61124_(BlockStateProperties.FACING, facing);
                }
                this.fakeTextureUV = FTBQuestsClient.getTextureUV(state, facing);
            } else {
                this.fakeTextureUV = new float[0];
            }
        }
        return this.fakeTextureUV;
    }
}