package com.mna.entities.constructs.ai.conditionals;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ConstructIsItemInContainer extends ConstructConditional<ConstructIsItemInContainer> {

    private DirectionalPoint pos;

    private DynamicItemFilter filter;

    private int minimum;

    private boolean individual_items = false;

    public ConstructIsItemInContainer(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.filter = new DynamicItemFilter();
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
                        HashMap<Integer, Integer> hashCount = new HashMap();
                        IItemHandler inv = (IItemHandler) handler.resolve().get();
                        int slots = inv.getSlots();
                        int total = 0;
                        for (int i = 0; i < slots; i++) {
                            ItemStack invStack = inv.getStackInSlot(i);
                            if (this.filter.matches(invStack)) {
                                int stackHash = this.filter.hashStack(invStack);
                                hashCount.compute(stackHash, (k, v) -> {
                                    int count = invStack.getCount();
                                    return v == null ? count : v + count;
                                });
                                total += invStack.getCount();
                                if (!this.individual_items && total >= this.minimum) {
                                    this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                                    return true;
                                }
                            }
                        }
                        MutableBoolean individualCount = new MutableBoolean(false);
                        if (this.individual_items) {
                            hashCount.forEach((k, v) -> {
                                if (v >= this.minimum) {
                                    individualCount.setTrue();
                                }
                            });
                        }
                        if (individualCount.booleanValue()) {
                            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.SUCCESS, Vec3.atCenterOf(this.pos.getPosition()));
                            return true;
                        } else {
                            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.FAILURE, Vec3.atCenterOf(this.pos.getPosition()));
                            return false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("item_in_container.amount").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.minimum = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
        Optional<ConstructAITaskParameter> filterParam = this.getParameter("item_in_container.filter");
        if (filterParam.isPresent() && filterParam.get() instanceof ConstructTaskFilterParameter) {
            this.filter = ((ConstructTaskFilterParameter) filterParam.get()).getValue();
        } else {
            this.filter = new DynamicItemFilter();
            InteractionHand[] carryingHands = this.construct.getCarryingHands();
            for (InteractionHand hand : carryingHands) {
                ItemStack stack = this.construct.asEntity().m_21120_(hand);
                ItemStack copied = stack.copy();
                copied.setCount(Math.max(1, this.minimum));
                this.filter.getWhiteList().add(copied);
            }
        }
        this.getParameter("item_in_container.pos").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.pos = ((ConstructTaskPointParameter) param).getPoint();
            }
        });
        this.getParameter("item_in_container.individual").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter) {
                this.individual_items = ((ConstructTaskBooleanParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        ArrayList<ConstructAITaskParameter> output = new ArrayList();
        output.add(new ConstructTaskFilterParameter("item_in_container.filter"));
        output.add(new ConstructTaskPointParameter("item_in_container.pos"));
        output.add(new ConstructTaskIntegerParameter("item_in_container.amount", 1, 128, 1, 1));
        output.add(new ConstructTaskBooleanParameter("item_in_container.individual"));
        return output;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.Conditions.CONTAINER_FIND);
    }

    public ConstructIsItemInContainer copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructIsItemInContainer) {
            this.pos = ((ConstructIsItemInContainer) other).pos;
            this.filter.copyFrom(((ConstructIsItemInContainer) other).filter);
            this.minimum = ((ConstructIsItemInContainer) other).minimum;
            this.individual_items = ((ConstructIsItemInContainer) other).individual_items;
        }
        return this;
    }

    public ConstructIsItemInContainer duplicate() {
        ConstructIsItemInContainer output = new ConstructIsItemInContainer(this.construct, this.guiIcon);
        output.copyFrom(this);
        return output;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.pos != null && this.pos.isValid();
    }
}