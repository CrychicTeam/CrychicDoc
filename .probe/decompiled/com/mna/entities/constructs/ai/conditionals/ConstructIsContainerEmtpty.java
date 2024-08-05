package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructIsContainerEmtpty extends ConstructConditional<ConstructIsContainerEmtpty> {

    private DirectionalPoint pos;

    public ConstructIsContainerEmtpty(IConstruct<?> construct, ResourceLocation guiIcon) {
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
                BlockEntity be = world.getBlockEntity(this.pos.getPosition());
                if (be == null) {
                    this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                    return false;
                } else {
                    LazyOptional<IItemHandler> handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, this.pos.getDirection());
                    if (!handler.isPresent()) {
                        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                        return false;
                    } else {
                        IItemHandler inv = (IItemHandler) handler.resolve().get();
                        int slots = inv.getSlots();
                        for (int i = 0; i < slots; i++) {
                            if (!inv.getStackInSlot(i).isEmpty()) {
                                this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                                return false;
                            }
                        }
                        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                        return true;
                    }
                }
            }
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("container_empty.pos").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.pos = ((ConstructTaskPointParameter) param).getPoint();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskPointParameter("container_empty.pos"));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.CONTAINER_EMPTY);
    }

    public ConstructIsContainerEmtpty copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructIsContainerEmtpty) {
            this.pos = ((ConstructIsContainerEmtpty) other).pos;
        }
        return this;
    }

    public ConstructIsContainerEmtpty duplicate() {
        ConstructIsContainerEmtpty output = new ConstructIsContainerEmtpty(this.construct, this.guiIcon);
        output.copyFrom(this);
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.pos != null && this.pos.isValid();
    }
}