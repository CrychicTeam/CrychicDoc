package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructBlockAreaTask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.BlockUtils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructBreakBlocks extends ConstructBlockAreaTask<ConstructBreakBlocks> {

    private static final ConstructCapability[] _requiredCapabilities = new ConstructCapability[] { ConstructCapability.MINE };

    private Tier harvest_level = Tiers.WOOD;

    private DynamicItemFilter blockFilter;

    private int breakProgress = 0;

    private int lastBreakProgressSync = 0;

    private int _breakTime = 80;

    public ConstructBreakBlocks(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.blockFilter = new DynamicItemFilter();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.currentTarget == null) {
            this.findBlockTarget();
        } else {
            if (this.moveToCurrentTarget()) {
                this.harvest();
            }
        }
    }

    private void harvest() {
        this.breakProgress++;
        int breakProgressSync = (int) ((float) this.breakProgress / (float) this._breakTime * 10.0F);
        AbstractGolem c = this.getConstructAsEntity();
        if (this.breakProgress >= this._breakTime) {
            BlockState state = c.m_9236_().getBlockState(this.currentTarget);
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.breaking", new Object[] { this.translate(state), this.currentTarget.m_123341_(), this.currentTarget.m_123342_(), this.currentTarget.m_123343_() }));
            boolean success = BlockUtils.destroyBlock((LivingEntity) (this.construct.getOwner() != null ? this.construct.getOwner() : c), c.m_9236_(), this.currentTarget, true, this.harvest_level);
            this.knownTargets.remove(this.currentTarget);
            c.m_9236_().destroyBlockProgress(c.m_19879_(), this.currentTarget, -1);
            this.lastLocation = 0;
            this.currentTarget = null;
            this.exitCode = success ? 0 : 1;
            this.breakProgress = 0;
            this.lastBreakProgressSync = 0;
        } else {
            if (breakProgressSync != this.lastBreakProgressSync) {
                c.m_9236_().destroyBlockProgress(c.m_19879_(), this.currentTarget, breakProgressSync);
                this.lastBreakProgressSync = breakProgressSync;
            }
            if (this.breakProgress % 20 == 0) {
                this.construct.getHandWithCapability(ConstructCapability.MINE).ifPresent(h -> c.m_6674_(h));
            }
        }
    }

    @Override
    public void setConstruct(IConstruct<?> construct) {
        super.setConstruct(construct);
        this.harvest_level = this.construct.getBlockHarvestLevel(ConstructCapability.MINE);
        if (this.harvest_level == null) {
            this.harvest_level = Tiers.WOOD;
        }
        this._breakTime = this.getInteractTime(ConstructCapability.MINE, 60);
    }

    @Override
    protected boolean isValidBlock(BlockState state, BlockPos pos) {
        AbstractGolem c = this.getConstructAsEntity();
        return !state.m_60795_() && BlockUtils.canDestroyBlock(c, c.m_9236_(), pos, this.harvest_level) && this.blockFilter.matches(new ItemStack(state.m_60734_().asItem()));
    }

    @Override
    protected int getInteractTimer() {
        return this.getInteractTime(ConstructCapability.MINE, 20);
    }

    @Override
    protected String getAreaIdentifier() {
        return "break_blocks.area";
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.BREAK_BLOCKS);
    }

    public ConstructBlockAreaTask<ConstructBreakBlocks> duplicate() {
        ConstructBreakBlocks duplicated = new ConstructBreakBlocks(this.construct, this.guiIcon);
        duplicated.copyFrom(this);
        return duplicated;
    }

    @Override
    public void start() {
        super.m_8056_();
        this.interactTimer = this.getInteractTime(ConstructCapability.MINE);
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.interactTimer = this.getInteractTime(ConstructCapability.MINE);
        this.lastBreakProgressSync = 0;
        this.breakProgress = 0;
    }

    @Override
    public ConstructBlockAreaTask<ConstructBreakBlocks> copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructBreakBlocks) {
            this.blockFilter.copyFrom(((ConstructBreakBlocks) other).blockFilter);
        }
        return this;
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.getParameter("break_blocks.filter").ifPresent(param -> {
            if (param instanceof ConstructTaskFilterParameter) {
                this.blockFilter.copyFrom(((ConstructTaskFilterParameter) param).getValue());
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> params = super.instantiateParameters();
        params.add(new ConstructTaskFilterParameter("break_blocks.filter"));
        return params;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return _requiredCapabilities;
    }
}