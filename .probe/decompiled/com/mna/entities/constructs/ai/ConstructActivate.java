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
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructActivate extends ConstructAITask<ConstructActivate> {

    private static final int INTERACT_TIME = 20;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[0];

    private int interactTimer = 20;

    private BlockPos interactPos = null;

    public ConstructActivate(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else if (this.interactPos != null && this.construct.asEntity().m_9236_().isLoaded(this.interactPos)) {
            return true;
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.activate_missing", new Object[0]), false);
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.interactPos));
        this.setMoveTarget(this.interactPos);
        if (this.doMove()) {
            if (this.interactTimer > 0) {
                this.interactTimer--;
            } else {
                AbstractGolem c = this.getConstructAsEntity();
                BlockState state = c.m_9236_().getBlockState(this.interactPos);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.activate_success", new Object[] { this.translate(state), this.interactPos.m_123341_(), this.interactPos.m_123342_(), this.interactPos.m_123343_() }), true);
                Player player = FakePlayerFactory.getMinecraft((ServerLevel) this.construct.asEntity().m_9236_());
                BlockHitResult brtr = new BlockHitResult(new Vec3((double) this.interactPos.m_123341_() + 0.5, (double) this.interactPos.m_123342_() + 0.5, (double) this.interactPos.m_123343_() + 0.5), Direction.DOWN, this.interactPos, true);
                state.m_60664_(c.m_9236_(), player, InteractionHand.MAIN_HAND, brtr);
                this.interactTimer = 20;
                c.m_6674_(Math.random() < 0.5 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
                this.exitCode = 0;
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
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.ACTIVATE);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructActivate duplicate() {
        return new ConstructActivate(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructActivate copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructActivate) {
            this.interactPos = ((ConstructActivate) other).interactPos;
        }
        return this;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void inflateParameters() {
        this.getParameter("activate.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.interactPos = ((ConstructTaskPointParameter) param).getPosition();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("activate.point"));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.interactPos != null;
    }
}