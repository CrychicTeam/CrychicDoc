package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructIsBlockAtPosition extends ConstructConditional<ConstructIsBlockAtPosition> {

    private ItemStack block = ItemStack.EMPTY;

    private DirectionalPoint pos;

    public ConstructIsBlockAtPosition(IConstruct<?> construct, ResourceLocation guiIcon) {
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
            } else {
                BlockState state = world.getBlockState(this.pos.getPosition());
                if (this.block.isEmpty()) {
                    if (!state.m_60795_()) {
                        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                        return true;
                    } else {
                        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                        return false;
                    }
                } else {
                    if (this.block.getItem() instanceof BlockItem blockItem && state.m_60734_() == blockItem.getBlock()) {
                        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                        return true;
                    }
                    this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                    return false;
                }
            }
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("find_block.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.pos = ((ConstructTaskPointParameter) param).getPoint();
            }
        });
        this.getParameter("find_block.block").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.block = ((ConstructTaskItemStackParameter) param).getStack();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskItemStackParameter("find_block.block"));
        output.add(new ConstructTaskPointParameter("find_block.point"));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.FIND_BLOCK);
    }

    public ConstructIsBlockAtPosition copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructIsBlockAtPosition) {
            this.block = ((ConstructIsBlockAtPosition) other).block.copy();
            this.pos = ((ConstructIsBlockAtPosition) other).pos;
        }
        return this;
    }

    public ConstructIsBlockAtPosition duplicate() {
        ConstructIsBlockAtPosition output = new ConstructIsBlockAtPosition(this.construct, this.guiIcon);
        output.copyFrom(this);
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.pos != null && this.pos.isValid();
    }
}