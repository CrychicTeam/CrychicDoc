package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructDropItem extends ConstructAITask<ConstructDropItem> {

    private static final int INTERACT_TIME = 20;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private int interactTimer = 20;

    protected BlockPos blockPos;

    public ConstructDropItem(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.blockPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        AbstractGolem c = this.getConstructAsEntity();
        if (this.construct.getCarryingHands().length == 0) {
            if (!this.isSuccess()) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.drop_item_hands_empty", new Object[0]));
            }
        } else {
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.blockPos));
            this.setMoveTarget(this.blockPos);
            if (this.doMove()) {
                InteractionHand[] carrying = this.construct.getCarryingHands();
                if (this.interactTimer > 0) {
                    if (this.interactTimer == 5 && carrying.length > 0) {
                        c.m_6674_(carrying[0]);
                    }
                    this.interactTimer--;
                } else if (this.interactTimer == 0) {
                    if (carrying.length == 0) {
                        this.exitCode = 1;
                        return;
                    }
                    String itemTranslated = "";
                    InteractionHand hand = carrying[0];
                    ItemStack stack = c.m_21120_(hand);
                    ItemEntity item = new ItemEntity(c.m_9236_(), (double) this.blockPos.m_123341_() + 0.5, (double) this.blockPos.m_123342_() + 0.5, (double) this.blockPos.m_123343_() + 0.5, stack.copy());
                    item.m_20334_(0.0, 0.25, 0.0);
                    item.setDefaultPickUpDelay();
                    c.m_9236_().m_7967_(item);
                    c.m_21008_(hand, ItemStack.EMPTY);
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.drop_item_success", new Object[] { itemTranslated, this.blockPos.m_123341_(), this.blockPos.m_123342_(), this.blockPos.m_123343_() }));
                    this.exitCode = 0;
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.DROP_ITEM);
    }

    public ConstructDropItem duplicate() {
        return new ConstructDropItem(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructDropItem copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructDropItem) {
            this.blockPos = ((ConstructDropItem) other).blockPos;
        }
        return this;
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        if (this.blockPos != null) {
            nbt.put("blockPos", NbtUtils.writeBlockPos(this.blockPos));
        }
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("blockPos")) {
            this.blockPos = NbtUtils.readBlockPos(nbt.getCompound("blockPos"));
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("drop.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("drop.point"));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null;
    }
}