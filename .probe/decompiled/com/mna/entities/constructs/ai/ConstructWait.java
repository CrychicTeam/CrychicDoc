package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructWait extends ConstructAITask<ConstructWait> {

    private int total_time = 100;

    private long target_time;

    public ConstructWait(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.construct.asEntity().m_9236_().getGameTime() >= this.target_time) {
            this.exitCode = 0;
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.construct.asEntity().m_21573_().stop();
        this.target_time = this.construct.asEntity().m_9236_().getGameTime() + (long) (this.total_time * 20);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.WAIT);
    }

    public ConstructWait duplicate() {
        return new ConstructWait(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructWait copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructWait) {
            this.target_time = ((ConstructWait) other).target_time;
            this.total_time = ((ConstructWait) other).total_time;
        }
        return this;
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        nbt.putLong("delta", this.target_time - this.construct.asEntity().m_9236_().getGameTime());
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("delta")) {
            this.target_time = this.construct.asEntity().m_9236_().getGameTime() + nbt.getLong("delta");
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("wait.seconds").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.total_time = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    public List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = new ArrayList();
        parameters.add(new ConstructTaskIntegerParameter("wait.seconds", 1, 60));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }
}