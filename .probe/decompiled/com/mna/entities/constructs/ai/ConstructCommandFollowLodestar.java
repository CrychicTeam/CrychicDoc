package com.mna.entities.constructs.ai;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructCommandTileEntityInteract;
import com.mna.blocks.tileentities.LodestarTile;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ConstructCommandFollowLodestar extends ConstructCommandTileEntityInteract<LodestarTile, ConstructCommandFollowLodestar> {

    private String curTaskId;

    private int taskCount = 0;

    private ConstructAITask<?> _current;

    private int waitTime = 0;

    public ConstructCommandFollowLodestar(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, LodestarTile.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (--this.waitTime <= 0) {
            try {
                this.currentCommand().ifPresent(task -> {
                    if (!task.isFinished()) {
                        task.tick();
                    }
                });
            } catch (Exception var2) {
                ManaAndArtifice.LOGGER.error("Error executing lodestar sub task, trying to recover.");
                ManaAndArtifice.LOGGER.catching(var2);
            }
        }
    }

    @Override
    public boolean canUse() {
        int power = this.construct.asEntity().m_9236_().m_277086_(this.blockPos);
        if (super.canUse() && power < 15) {
            MutableBoolean subCanUse = new MutableBoolean(false);
            if (this._current == null) {
                this.advanceTask(false);
            }
            this.currentCommand().ifPresent(task -> {
                if (!task.areCapabilitiesMet()) {
                    task.confuseConstructCapsMissing();
                    task.forceFail();
                    this.advanceTask(true);
                    subCanUse.setFalse();
                } else if (this.construct.getIntelligence() < task.getRequiredIntelligence()) {
                    task.confuseConstructLowIntelligence();
                    task.forceFail();
                    this.advanceTask(true);
                    subCanUse.setFalse();
                } else if (!task.isFullyConfigured()) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.task_not_configured", new Object[] { this.translate(task.getType().toString(), new Object[0]) }));
                    task.forceFail();
                    this.advanceTask(true);
                    subCanUse.setFalse();
                } else if (task.isFinished()) {
                    this.advanceTask(false);
                    subCanUse.setFalse();
                } else {
                    if (!task.canUse()) {
                        subCanUse.setFalse();
                        task.forceFail();
                        this.advanceTask(true);
                    } else {
                        subCanUse.setTrue();
                    }
                }
            });
            return subCanUse.booleanValue();
        } else {
            return false;
        }
    }

    private boolean advanceTask(boolean force) {
        MutableBoolean advanced = new MutableBoolean(false);
        this.currentCommand().ifPresent(task -> {
            if (task.isFinished() || force) {
                task.stop();
                this.curTaskId = task.getNextTask();
                if (this.curTaskId == null) {
                    this.reset();
                }
            } else if (!task.hasStarted()) {
                this.construct.setCurrentCommand(this.owner, task);
                task.start();
            }
        });
        Optional<ConstructAITask<?>> resolved = this.getTileEntity().getCommand(this.curTaskId);
        if (resolved.isPresent()) {
            this.taskCount++;
            if (this.taskCount > this.construct.getIntelligence()) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.confused", new Object[0]));
                this.construct.setConfused(100);
                this.reset();
            } else {
                this._current = ((ConstructAITask) resolved.get()).duplicate();
                this._current.setConstruct(this.construct);
                this._current.setMutexManager(this.getTileEntity());
                this._current.copyConnections((ConstructAITask<?>) resolved.get());
                this._current.onTaskSet();
                this._current.start();
                this.curTaskId = ((ConstructAITask) resolved.get()).getId();
                advanced.setTrue();
            }
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.task_not_found", new Object[0]));
            this.reset();
        }
        int power = this.construct.asEntity().m_9236_().m_277086_(this.blockPos);
        if (power > 0 && power < 15) {
            this.waitTime = 2 * power;
        }
        return advanced.booleanValue();
    }

    private void reset() {
        this.taskCount = 0;
        this.curTaskId = null;
        this._current = null;
    }

    @Override
    public void start() {
        super.start();
        if (this.getTileEntity() != null) {
            ;
        }
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.currentCommand().ifPresent(task -> task.stop());
        this.releaseMutexes();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.getTileEntity() != null && !this.construct.asEntity().m_9236_().m_276867_(this.blockPos)) {
            MutableBoolean subCanContinue = new MutableBoolean(false);
            try {
                this.currentCommand().ifPresent(task -> subCanContinue.setValue(task.canContinueToUse()));
            } catch (Exception var3) {
                ManaAndArtifice.LOGGER.error("Error executing lodestar sub task, trying to recover.");
                ManaAndArtifice.LOGGER.error(var3);
            }
            return subCanContinue.booleanValue();
        } else {
            return false;
        }
    }

    public Optional<ConstructAITask<?>> currentCommand() {
        return Optional.ofNullable(this._current);
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.curTaskId = null;
    }

    public ConstructCommandFollowLodestar duplicate() {
        return new ConstructCommandFollowLodestar(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructCommandFollowLodestar copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructCommandFollowLodestar) {
            this.curTaskId = ((ConstructCommandFollowLodestar) other).curTaskId;
            this.taskCount = ((ConstructCommandFollowLodestar) other).taskCount;
        }
        super.copyFrom(other);
        return this;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.LODESTAR);
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        super.readNBT(nbt);
        if (nbt.contains("curTaskId")) {
            this.curTaskId = nbt.getString("curTaskId");
        }
        if (nbt.contains("taskCount")) {
            this.taskCount = nbt.getInt("taskCount");
        }
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        nbt = super.writeInternal(nbt);
        if (this.curTaskId != null) {
            nbt.putString("curTaskId", this.curTaskId);
        }
        nbt.putInt("taskCount", this.taskCount);
        return nbt;
    }

    @Override
    public void releaseMutexes() {
        if (this._current != null) {
            this._current.releaseMutexes();
        }
    }
}