package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructUseItemOnBlock extends ConstructAITask<ConstructUseItemOnBlock> {

    private static final int INTERACT_TIME = 20;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private int interactTimer = 20;

    private BlockPos interactPos = null;

    private boolean leftHand = false;

    public ConstructUseItemOnBlock(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else if (this.interactPos != null && this.construct.asEntity().m_9236_().isLoaded(this.interactPos)) {
            boolean handCanUse = false;
            Optional<ItemConstructPart> arm = this.construct.getConstructData().getPart(this.leftHand ? ConstructSlot.LEFT_ARM : ConstructSlot.RIGHT_ARM);
            if (arm.isPresent()) {
                ConstructCapability[] lhCaps = ((ItemConstructPart) arm.get()).getEnabledCapabilities();
                for (int i = 0; i < lhCaps.length; i++) {
                    if (lhCaps[i] == ConstructCapability.CARRY) {
                        handCanUse = true;
                        break;
                    }
                }
            }
            return handCanUse;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setMoveTarget(this.interactPos);
        this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.interactPos));
        if (this.doMove()) {
            if (this.interactTimer > 0) {
                this.interactTimer--;
            } else {
                AbstractGolem c = this.getConstructAsEntity();
                InteractionHand hand = this.leftHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                InteractionHand playerHand = this.leftHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                ItemStack stack = c.m_21120_(hand);
                if (stack.isEmpty()) {
                    this.exitCode = 1;
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.use_item_on_block.empty_hand", new Object[0]), false);
                    return;
                }
                Player player = FakePlayerFactory.getMinecraft((ServerLevel) this.construct.asEntity().m_9236_());
                player.m_20359_(this.construct.asEntity());
                player.m_21008_(playerHand, stack.copy());
                BlockHitResult simResult = new BlockHitResult(Vec3.atCenterOf(this.interactPos), Direction.UP, this.interactPos, false);
                InteractionResult result = InteractionResult.FAIL;
                if (simResult.getType() == HitResult.Type.BLOCK) {
                    BlockState state = c.m_9236_().getBlockState(this.interactPos);
                    result = state.m_60664_(c.m_9236_(), player, playerHand, simResult);
                    c.m_21008_(hand, player.m_21120_(playerHand).copy());
                }
                c.m_6674_(hand);
                this.exitCode = result == InteractionResult.FAIL ? 1 : 0;
            }
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.USE_ITEM_ON_BLOCK);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructUseItemOnBlock duplicate() {
        return new ConstructUseItemOnBlock(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructUseItemOnBlock copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructUseItemOnBlock) {
            this.interactPos = ((ConstructUseItemOnBlock) other).interactPos;
            this.leftHand = ((ConstructUseItemOnBlock) other).leftHand;
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
        this.getParameter("use_item_on_block.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.interactPos = ((ConstructTaskPointParameter) param).getPosition();
            }
        });
        this.getParameter("use_item_on_block.left_hand").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter) {
                this.leftHand = ((ConstructTaskBooleanParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("use_item_on_block.point"));
        parameters.add(new ConstructTaskBooleanParameter("use_item_on_block.left_hand"));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.interactPos != null;
    }
}