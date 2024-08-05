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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructFish extends ConstructAITask<ConstructFish> {

    protected Direction side;

    protected BlockPos blockPos;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.FISH };

    private int fish_counter = -1;

    private int end_wait_time = -1;

    public ConstructFish(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.construct.asEntity().m_9236_();
        if (!world.isLoaded(this.blockPos)) {
            this.exitCode = 1;
        } else if (this.end_wait_time > 0) {
            if (--this.end_wait_time <= 0) {
                this.construct.resetActions();
                this.exitCode = 0;
            }
        } else {
            if (!this.construct.isFishing()) {
                this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.blockPos));
                this.setMoveTarget(this.blockPos);
                if (this.doMove(8.0F)) {
                    if (world.getBlockState(this.blockPos).m_60734_() != Blocks.WATER) {
                        this.blockPos = this.blockPos.offset(this.side.getNormal());
                        if (world.getBlockState(this.blockPos).m_60734_() != Blocks.WATER) {
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.fish_not_water", new Object[0]));
                            this.forceFail();
                            return;
                        }
                    }
                    this.construct.setFishing(this.blockPos);
                }
            } else {
                this.setMoveTarget(this.blockPos);
                this.faceBlockPos(this.blockPos);
                this.doMove(8.0F);
                if (--this.fish_counter <= 0) {
                    AbstractGolem c = this.construct.asEntity();
                    this.construct.stopFishing();
                    for (ItemStack itemstack : this.getLootRoll(BuiltInLootTables.FISHING, new ItemStack(Items.FISHING_ROD), LootContextParamSets.FISHING)) {
                        Vec3 center = Vec3.atCenterOf(this.blockPos).add(0.0, 0.5, 0.0);
                        ItemEntity itementity = new ItemEntity(c.m_9236_(), center.x, center.y, center.z, itemstack);
                        double d0 = c.m_20185_() - center.x;
                        double d1 = c.m_20186_() - center.y;
                        double d2 = c.m_20189_() - center.z;
                        itementity.m_20334_(d0 * 0.1, d1 * 0.1 + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1);
                        c.m_9236_().m_7967_(itementity);
                    }
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.fish_success", new Object[0]));
                    this.end_wait_time = 20;
                }
            }
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.end_wait_time = -1;
        this.fish_counter = this.getInteractTime(ConstructCapability.FISH, 400);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.FISH);
    }

    public ConstructFish duplicate() {
        return new ConstructFish(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructFish copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructFish) {
            this.blockPos = ((ConstructFish) other).blockPos;
            this.side = ((ConstructFish) other).side;
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
    public void inflateParameters() {
        this.getParameter("fish.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
                this.side = ((ConstructTaskPointParameter) param).getDirection();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("fish.point"));
        return parameters;
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null;
    }

    public static ConstructCapability[] getRequiredcaps() {
        return requiredCaps;
    }
}