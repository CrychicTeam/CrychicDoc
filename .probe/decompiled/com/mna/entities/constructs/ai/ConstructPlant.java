package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructBlockAreaTask;
import com.mna.api.tools.MATags;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructPlant extends ConstructBlockAreaTask<ConstructPlant> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.PLANT, ConstructCapability.CARRY };

    ResourceLocation plantBlockTag;

    public ConstructPlant(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public void tick() {
        super.m_8037_();
        AbstractGolem c = this.getConstructAsEntity();
        this.plantBlockTag = this.getHeldItemPlantBlockTag();
        if (this.plantBlockTag == null) {
            this.exitCode = 1;
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.plant_empty_hands", new Object[0]), false);
        } else if (this.currentTarget == null) {
            this.findBlockTarget();
        } else if (!MATags.isBlockIn(c.m_9236_().getBlockState(this.currentTarget).m_60734_(), this.plantBlockTag)) {
            this.currentTarget = null;
            this.findBlockTarget();
        } else {
            if (this.moveToCurrentTarget()) {
                if (c.m_9236_().m_46859_(this.currentTarget.above())) {
                    InteractionHand[] carrying = this.getHeldItem(this.plantBlockTag);
                    for (InteractionHand hand : carrying) {
                        ItemStack stack = c.m_21120_(hand);
                        if (!(stack.getItem() instanceof BlockItem)) {
                            this.exitCode = 1;
                            this.currentTarget = null;
                            return;
                        }
                        BlockState plant = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                        if (plant != null) {
                            String translatedStack = this.translate(stack);
                            stack.shrink(1);
                            c.m_9236_().setBlockAndUpdate(this.currentTarget.above(), plant);
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.planting", new Object[] { translatedStack, this.currentTarget.m_123341_(), this.currentTarget.m_123342_(), this.currentTarget.m_123343_() }));
                            this.exitCode = 0;
                            this.currentTarget = null;
                            return;
                        }
                    }
                } else {
                    this.removeBlockTarget(this.currentTarget);
                    this.currentTarget = null;
                }
            }
        }
    }

    private ResourceLocation getHeldItemPlantBlockTag() {
        if (this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.SOUL_SAND_PLANTABLE)).length != 0) {
            return MATags.Blocks.CONSTRUCT_PLANTABLE_SOUL_SAND;
        } else if (this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.SAND_PLANTABLE)).length != 0) {
            return MATags.Blocks.CONSTRUCT_PLANTABLE_SAND;
        } else if (this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.GRASS_PLANTABLE)).length != 0) {
            return MATags.Blocks.CONSTRUCT_PLANTABLE_GRASS;
        } else {
            return this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.FARMLAND_PLANTABLE)).length != 0 ? MATags.Blocks.CONSTRUCT_PLANTABLE_FARMLAND : null;
        }
    }

    private InteractionHand[] getHeldItem(ResourceLocation targetTag) {
        if (targetTag.equals(MATags.Blocks.CONSTRUCT_PLANTABLE_FARMLAND)) {
            return this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.FARMLAND_PLANTABLE));
        } else if (targetTag.equals(MATags.Blocks.CONSTRUCT_PLANTABLE_GRASS)) {
            return this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.GRASS_PLANTABLE));
        } else if (targetTag.equals(MATags.Blocks.CONSTRUCT_PLANTABLE_SAND)) {
            return this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.SAND_PLANTABLE));
        } else {
            return targetTag.equals(MATags.Blocks.CONSTRUCT_PLANTABLE_SOUL_SAND) ? this.construct.getCarryingHands(i -> MATags.isItemEqual(i, MATags.Items.Constructs.SOUL_SAND_PLANTABLE)) : new InteractionHand[0];
        }
    }

    @Override
    protected ConstructCapability getSwingArmCap() {
        return ConstructCapability.PLANT;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            this.forceFail();
            return false;
        } else if (this.getHeldItemPlantBlockTag() == null) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.plant_empty_hands", new Object[0]), false);
            this.exitCode = -1;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canUse();
    }

    @Override
    protected int getInteractTimer() {
        return this.getInteractTime(ConstructCapability.PLANT);
    }

    @Override
    protected boolean isValidBlock(BlockState state, BlockPos pos) {
        if (!this.construct.asEntity().m_9236_().m_46859_(pos.above())) {
            return false;
        } else if (this.plantBlockTag != null) {
            ResourceLocation blockTag = this.getHeldItemPlantBlockTag();
            return MATags.isBlockIn(state.m_60734_(), blockTag);
        } else {
            return MATags.isBlockIn(state.m_60734_(), MATags.Blocks.CONSTRUCT_PLANTABLE_FARMLAND) || MATags.isBlockIn(state.m_60734_(), MATags.Blocks.CONSTRUCT_PLANTABLE_SAND) || MATags.isBlockIn(state.m_60734_(), MATags.Blocks.CONSTRUCT_PLANTABLE_GRASS) || MATags.isBlockIn(state.m_60734_(), MATags.Blocks.CONSTRUCT_PLANTABLE_SOUL_SAND);
        }
    }

    @Override
    public void start() {
        super.m_8056_();
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.interactTimer = this.getInteractTime(ConstructCapability.CARRY);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.PLANT);
    }

    public ConstructPlant duplicate() {
        return new ConstructPlant(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructPlant copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
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
    protected String getAreaIdentifier() {
        return "plant.area";
    }
}