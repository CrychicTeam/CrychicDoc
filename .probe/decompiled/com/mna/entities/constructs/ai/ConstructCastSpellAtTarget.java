package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.math.MathUtils;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructCastSpellAtTarget extends ConstructAITask<ConstructCastSpellAtTarget> {

    protected BlockPos blockPos;

    protected int closeEnoughDistance = 8;

    protected int channelDuration = 8;

    protected boolean mainHandCast = true;

    private boolean casting = false;

    private int activeTicks = 0;

    private static final ConstructCapability[] required_caps = new ConstructCapability[] { ConstructCapability.STORE_MANA, ConstructCapability.CAST_SPELL };

    public ConstructCastSpellAtTarget(IConstruct<?> construct, ResourceLocation guiIcon) {
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
        if (this.closeEnoughDistance < 1) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.cast.cant_see", new Object[0]));
            this.forceFail();
        } else if (this.construct.getCastableSpells().length == 0) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.cast.no_spells", new Object[0]));
            this.forceFail();
        } else if (this.construct.getMana() == 0.0F) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.cast.no_mana", new Object[0]));
            this.forceFail();
        } else {
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, Vec3.atCenterOf(this.blockPos));
            this.setMoveTarget(this.blockPos);
            if (this.casting) {
                this.activeTicks++;
                boolean done = false;
                if (this.activeTicks == 1) {
                    if (!this.construct.setupSpellCast(this.mainHandCast)) {
                        this.forceFail();
                        return;
                    }
                } else if (this.activeTicks == 2) {
                    Vec3 constructEye = this.construct.asEntity().m_146892_();
                    Vec3 target = Vec3.atCenterOf(this.blockPos);
                    ClipContext ctx = new ClipContext(constructEye, target, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null);
                    BlockHitResult hitResult = this.construct.asEntity().m_9236_().m_45547_(ctx);
                    Direction face = Direction.UP;
                    if (hitResult.getType() != HitResult.Type.MISS) {
                        face = hitResult.getDirection();
                    }
                    this.construct.startSpellCast(new SpellTarget(this.blockPos, face));
                } else if (this.activeTicks < this.channelDuration) {
                    done = !this.construct.tickSpellCast();
                } else {
                    this.construct.resetSpellCast();
                    done = true;
                }
                if (done) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.cast.success", new Object[0]));
                    this.exitCode = 0;
                    return;
                }
            } else if (this.doMove((float) this.closeEnoughDistance)) {
                Vec3 target = Vec3.atCenterOf(this.blockPos);
                Vec3 constructEye = this.construct.asEntity().m_146892_();
                Vec3 delta = constructEye.subtract(target).normalize();
                target = target.add(delta);
                ClipContext ctx = new ClipContext(constructEye, target, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null);
                BlockHitResult hitResult = this.construct.asEntity().m_9236_().m_45547_(ctx);
                if (hitResult.getType() != HitResult.Type.MISS) {
                    this.closeEnoughDistance--;
                    return;
                }
                if (MathUtils.rotateEntityLookTowards(EntityAnchorArgument.Anchor.FEET, this.construct.asEntity(), target, 12.0F)) {
                    this.casting = true;
                    this.activeTicks = 0;
                }
            }
        }
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.CAST_SPELL_AT_TARGET);
    }

    public ConstructCastSpellAtTarget duplicate() {
        return new ConstructCastSpellAtTarget(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructCastSpellAtTarget copyFrom(ConstructAITask<?> other) {
        if (other instanceof ConstructCastSpellAtTarget) {
            this.blockPos = ((ConstructCastSpellAtTarget) other).blockPos;
            this.closeEnoughDistance = ((ConstructCastSpellAtTarget) other).closeEnoughDistance;
            this.channelDuration = ((ConstructCastSpellAtTarget) other).channelDuration;
            this.mainHandCast = ((ConstructCastSpellAtTarget) other).mainHandCast;
        }
        return this;
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        if (this.blockPos != null) {
            nbt.put("blockPos", NbtUtils.writeBlockPos(this.blockPos));
        }
        nbt.putInt("closeEnoughDistance", this.closeEnoughDistance);
        nbt.putInt("channelTime", this.channelDuration);
        nbt.putBoolean("mainHandCast", this.mainHandCast);
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("blockPos")) {
            this.blockPos = NbtUtils.readBlockPos(nbt.getCompound("blockPos"));
        }
        if (nbt.contains("closeEnoughDistance")) {
            this.closeEnoughDistance = nbt.getInt("closeEnoughDistance");
        }
        if (nbt.contains("channelTime")) {
            this.channelDuration = nbt.getInt("channelTime");
        }
        if (nbt.contains("mainHandCast")) {
            this.mainHandCast = nbt.getBoolean("mainHandCast");
        }
    }

    @Override
    public void inflateParameters() {
        this.getParameter("castAtTarget.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                this.blockPos = ((ConstructTaskPointParameter) param).getPosition();
            }
        });
        this.getParameter("castAtTarget.minDistance").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.closeEnoughDistance = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
        this.getParameter("castAtTarget.channelTime").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.channelDuration = ((ConstructTaskIntegerParameter) param).getValue() * 20;
            }
        });
        this.getParameter("castAtTarget.mainHand").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter) {
                this.mainHandCast = ((ConstructTaskBooleanParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskPointParameter("castAtTarget.point"));
        parameters.add(new ConstructTaskIntegerParameter("castAtTarget.minDistance", 1, 32));
        parameters.add(new ConstructTaskBooleanParameter("castAtTarget.mainHand", true));
        parameters.add(new ConstructTaskIntegerParameter("castAtTarget.channelTime", 1, 10));
        return parameters;
    }

    public void setDesiredLocation(BlockPos pos) {
        this.blockPos = pos;
        this.getParameter("castAtTarget.point").ifPresent(param -> {
            if (param instanceof ConstructTaskPointParameter) {
                ((ConstructTaskPointParameter) param).setPoint(new DirectionalPoint(pos, Direction.UP, ""));
            }
        });
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return required_caps;
    }
}