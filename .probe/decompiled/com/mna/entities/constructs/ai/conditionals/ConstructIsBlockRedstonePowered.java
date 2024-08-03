package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructIsBlockRedstonePowered extends ConstructConditional<ConstructIsBlockRedstonePowered> {

    private int minimum;

    private DirectionalPoint pos;

    public ConstructIsBlockRedstonePowered(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
    }

    @Override
    protected boolean evaluate() {
        if (this.pos == null) {
            return false;
        } else {
            Level world = this.construct.asEntity().m_9236_();
            if (!world.isLoaded(this.pos.getPosition())) {
                return false;
            } else if (world.m_277086_(this.pos.getPosition()) >= this.minimum) {
                this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                return true;
            } else {
                this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                return false;
            }
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("is_redstone_powered.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.pos = ((ConstructTaskPointParameter) param).getPoint();
            }
        });
        this.getParameter("is_redstone_powered.minimum").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.minimum = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskPointParameter("is_redstone_powered.point"));
        output.add(new ConstructTaskIntegerParameter("is_redstone_powered.minimum", 1, 15));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.REDSTONE_POWERED);
    }

    public ConstructIsBlockRedstonePowered copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructIsBlockRedstonePowered) {
            this.minimum = ((ConstructIsBlockRedstonePowered) other).minimum;
            this.pos = ((ConstructIsBlockRedstonePowered) other).pos;
        }
        return this;
    }

    public ConstructIsBlockRedstonePowered duplicate() {
        ConstructIsBlockRedstonePowered output = new ConstructIsBlockRedstonePowered(this.construct, this.guiIcon);
        output.copyFrom(this);
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.pos != null && this.pos.isValid();
    }
}