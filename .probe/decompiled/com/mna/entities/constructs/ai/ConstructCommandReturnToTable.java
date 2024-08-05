package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.ConstructWorkbenchTile;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructCommandReturnToTable extends ConstructAITask<ConstructCommandReturnToTable> {

    public ConstructCommandReturnToTable(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        AbstractGolem c = this.getConstructAsEntity();
        return this.hasMoveTarget() && this.construct != null && c.m_9236_().isLoaded(this.getMoveBlockTarget()) && c.m_9236_().getBlockState(this.getMoveBlockTarget()).m_60734_() == BlockInit.CONSTRUCT_WORKBENCH.get();
    }

    @Override
    public void tick() {
        super.tick();
        AbstractGolem c = this.getConstructAsEntity();
        if (c.m_20183_().m_123314_(this.getMoveBlockTarget(), 4.0)) {
            if (!c.m_9236_().isClientSide() && !this.construct.asEntity().m_213877_()) {
                ConstructWorkbenchTile table = (ConstructWorkbenchTile) c.m_9236_().getBlockEntity(this.getMoveBlockTarget());
                if (table != null) {
                    if (!table.getIsCrafting() && table.isEmpty()) {
                        this.construct.dropAllItems();
                        table.setConstructData(this.construct.getConstructData(), c.m_8077_() ? c.m_7770_().getString() : null);
                        c.m_142687_(Entity.RemovalReason.DISCARDED);
                    } else {
                        this.construct.setCurrentCommand(this.owner, ConstructTasks.STAY.instantiateTask(this.construct));
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.return_to_workbench_not_empty", new Object[0]));
                    }
                } else {
                    this.construct.setCurrentCommand(this.owner, ConstructTasks.STAY.instantiateTask(this.construct));
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.return_to_workbench_invalid", new Object[0]));
                }
            }
        } else {
            this.doMove();
        }
    }

    @Override
    public void start() {
        super.start();
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.return_to_workbench", new Object[0]));
    }

    public void setTablePos(BlockPos pos) {
        this.setMoveTarget(pos);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.MODIFY);
    }

    public ConstructCommandReturnToTable copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructCommandReturnToTable) {
            this.setMoveTarget(((ConstructCommandReturnToTable) other).getMoveBlockTarget());
        }
        return this;
    }

    public ConstructCommandReturnToTable duplicate() {
        return new ConstructCommandReturnToTable(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("tablePos")) {
            this.setMoveTarget(NbtUtils.readBlockPos(nbt.getCompound("tablePos")));
        }
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        if (this.getMoveBlockTarget() != null) {
            nbt.put("tablePos", NbtUtils.writeBlockPos(this.getMoveBlockTarget()));
        }
        return nbt;
    }

    @Override
    public void inflateParameters() {
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}