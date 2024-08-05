package net.minecraft.world.level.block.entity;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class CommandBlockEntity extends BlockEntity {

    private boolean powered;

    private boolean auto;

    private boolean conditionMet;

    private final BaseCommandBlock commandBlock = new BaseCommandBlock() {

        @Override
        public void setCommand(String p_59157_) {
            super.setCommand(p_59157_);
            CommandBlockEntity.this.m_6596_();
        }

        @Override
        public ServerLevel getLevel() {
            return (ServerLevel) CommandBlockEntity.this.f_58857_;
        }

        @Override
        public void onUpdated() {
            BlockState $$0 = CommandBlockEntity.this.f_58857_.getBlockState(CommandBlockEntity.this.f_58858_);
            this.getLevel().sendBlockUpdated(CommandBlockEntity.this.f_58858_, $$0, $$0, 3);
        }

        @Override
        public Vec3 getPosition() {
            return Vec3.atCenterOf(CommandBlockEntity.this.f_58858_);
        }

        @Override
        public CommandSourceStack createCommandSourceStack() {
            Direction $$0 = (Direction) CommandBlockEntity.this.m_58900_().m_61143_(CommandBlock.FACING);
            return new CommandSourceStack(this, Vec3.atCenterOf(CommandBlockEntity.this.f_58858_), new Vec2(0.0F, $$0.toYRot()), this.getLevel(), 2, this.m_45439_().getString(), this.m_45439_(), this.getLevel().getServer(), null);
        }

        @Override
        public boolean isValid() {
            return !CommandBlockEntity.this.m_58901_();
        }
    };

    public CommandBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.COMMAND_BLOCK, blockPos0, blockState1);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        this.commandBlock.save(compoundTag0);
        compoundTag0.putBoolean("powered", this.isPowered());
        compoundTag0.putBoolean("conditionMet", this.wasConditionMet());
        compoundTag0.putBoolean("auto", this.isAutomatic());
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.commandBlock.load(compoundTag0);
        this.powered = compoundTag0.getBoolean("powered");
        this.conditionMet = compoundTag0.getBoolean("conditionMet");
        this.setAutomatic(compoundTag0.getBoolean("auto"));
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }

    public void setPowered(boolean boolean0) {
        this.powered = boolean0;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public boolean isAutomatic() {
        return this.auto;
    }

    public void setAutomatic(boolean boolean0) {
        boolean $$1 = this.auto;
        this.auto = boolean0;
        if (!$$1 && boolean0 && !this.powered && this.f_58857_ != null && this.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
            this.scheduleTick();
        }
    }

    public void onModeSwitch() {
        CommandBlockEntity.Mode $$0 = this.getMode();
        if ($$0 == CommandBlockEntity.Mode.AUTO && (this.powered || this.auto) && this.f_58857_ != null) {
            this.scheduleTick();
        }
    }

    private void scheduleTick() {
        Block $$0 = this.m_58900_().m_60734_();
        if ($$0 instanceof CommandBlock) {
            this.markConditionMet();
            this.f_58857_.m_186460_(this.f_58858_, $$0, 1);
        }
    }

    public boolean wasConditionMet() {
        return this.conditionMet;
    }

    public boolean markConditionMet() {
        this.conditionMet = true;
        if (this.isConditional()) {
            BlockPos $$0 = this.f_58858_.relative(((Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(CommandBlock.FACING)).getOpposite());
            if (this.f_58857_.getBlockState($$0).m_60734_() instanceof CommandBlock) {
                BlockEntity $$1 = this.f_58857_.getBlockEntity($$0);
                this.conditionMet = $$1 instanceof CommandBlockEntity && ((CommandBlockEntity) $$1).getCommandBlock().getSuccessCount() > 0;
            } else {
                this.conditionMet = false;
            }
        }
        return this.conditionMet;
    }

    public CommandBlockEntity.Mode getMode() {
        BlockState $$0 = this.m_58900_();
        if ($$0.m_60713_(Blocks.COMMAND_BLOCK)) {
            return CommandBlockEntity.Mode.REDSTONE;
        } else if ($$0.m_60713_(Blocks.REPEATING_COMMAND_BLOCK)) {
            return CommandBlockEntity.Mode.AUTO;
        } else {
            return $$0.m_60713_(Blocks.CHAIN_COMMAND_BLOCK) ? CommandBlockEntity.Mode.SEQUENCE : CommandBlockEntity.Mode.REDSTONE;
        }
    }

    public boolean isConditional() {
        BlockState $$0 = this.f_58857_.getBlockState(this.m_58899_());
        return $$0.m_60734_() instanceof CommandBlock ? (Boolean) $$0.m_61143_(CommandBlock.CONDITIONAL) : false;
    }

    public static enum Mode {

        SEQUENCE, AUTO, REDSTONE
    }
}