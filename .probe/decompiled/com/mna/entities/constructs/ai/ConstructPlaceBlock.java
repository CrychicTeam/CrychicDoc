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
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructPlaceBlock extends ConstructAITask<ConstructPlaceBlock> {

    private static final int INTERACT_TIME = 20;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private int interactTimer = 20;

    protected BlockPos blockPos;

    protected Direction side;

    public ConstructPlaceBlock(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.blockPos != null && this.side != null;
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
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_block_hands_empty", new Object[0]));
            }
            this.exitCode = 1;
            c.f_21345_.removeGoal(this);
        } else if (c.m_9236_().isLoaded(this.blockPos) && c.m_9236_().m_46859_(this.blockPos)) {
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.blockPos));
            this.setMoveTarget(this.blockPos);
            if (this.doMove(8.0F)) {
                if (this.interactTimer > 0) {
                    this.interactTimer--;
                } else if (this.interactTimer == 0) {
                    InteractionHand[] carrying = this.construct.getCarryingHands(ix -> ix.getItem() instanceof BlockItem);
                    if (carrying.length == 0) {
                        this.exitCode = 1;
                        return;
                    }
                    boolean placed = false;
                    String blockTranslated = "";
                    for (int i = 0; i < carrying.length; i++) {
                        ItemStack stack = c.m_21120_(carrying[i]);
                        InteractionResult result = ((BlockItem) stack.getItem()).place(new BlockPlaceContext(this.createFakePlayer(), carrying[i], stack, new BlockHitResult(new Vec3((double) this.blockPos.m_123341_(), (double) this.blockPos.m_123342_(), (double) this.blockPos.m_123343_()), this.side, this.blockPos, true)));
                        if (result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) {
                            blockTranslated = this.translate(stack);
                            stack.shrink(1);
                            c.m_6674_(carrying[i]);
                            break;
                        }
                    }
                    if (!placed) {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_block_failed", new Object[0]));
                        this.exitCode = 1;
                    } else {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_block_success", new Object[] { blockTranslated, this.blockPos.m_123341_(), this.blockPos.m_123342_(), this.blockPos.m_123343_() }));
                        this.exitCode = 0;
                    }
                    return;
                }
            }
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_block_not_clear", new Object[0]));
            this.exitCode = 1;
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.PLACE_BLOCK);
    }

    public ConstructPlaceBlock duplicate() {
        return new ConstructPlaceBlock(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public void inflateParameters() {
        this.getParameter("place_block.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
                this.side = ((ConstructTaskPointParameter) param).getDirection();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("place_block.point"));
        return parameters;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructPlaceBlock copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructPlaceBlock) {
            this.side = ((ConstructPlaceBlock) other).side;
            this.blockPos = ((ConstructPlaceBlock) other).blockPos;
        }
        return this;
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        if (this.blockPos != null) {
            nbt.put("blockPos", NbtUtils.writeBlockPos(this.blockPos));
        }
        if (this.side != null) {
            nbt.putInt("direction", this.side.get3DDataValue());
        }
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("blockPos")) {
            this.blockPos = NbtUtils.readBlockPos(nbt.getCompound("blockPos"));
        }
        if (nbt.contains("direction")) {
            this.side = Direction.from3DDataValue(nbt.getInt("direction"));
        }
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null && this.side != null;
    }
}