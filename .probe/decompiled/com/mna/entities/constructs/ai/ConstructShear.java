package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructDiagnostics;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructBlockAreaTask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.ShearHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructShear extends ConstructBlockAreaTask<ConstructShear> {

    private static final String KEY_CHICKEN_SHEAR_TIME = "last_shear_time";

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.SHEAR };

    private boolean includeBlocks = false;

    private List<Animal> currentShearTargets;

    private int currentShearIndex = 0;

    private ItemStack shearsStack = new ItemStack(Items.SHEARS);

    public ConstructShear(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.currentShearTargets = new ArrayList();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.interactTimer > 0) {
            this.interactTimer--;
        } else if (!this.hasShearTarget() && !this.locateShearTarget()) {
            if (!this.searchingSurroundings && this.knownTargets.size() == 0) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_no_target", new Object[0]), false);
                this.forceFail();
            }
        } else {
            if (this.currentTarget == null) {
                this.locateShearTarget();
            }
            if (!this.includeBlocks && this.currentShearIndex >= this.currentShearTargets.size()) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_no_target", new Object[0]), false);
                this.forceFail();
            } else {
                this.setMoveTarget();
                if (!this.hasMoveTarget()) {
                    this.forceFail();
                } else {
                    if (this.doMove() && this.shearTarget()) {
                        if (this.currentShearIndex < this.currentShearTargets.size()) {
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_success", new Object[] { this.translate((Entity) this.currentShearTargets.get(this.currentShearIndex)) }), false);
                        }
                        if (this.includeBlocks) {
                            this.removeBlockTarget(this.currentTarget);
                            this.currentTarget = null;
                        }
                        this.currentShearIndex++;
                        if (this.currentShearIndex >= this.currentShearTargets.size()) {
                            this.setSuccessCode();
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean hasShearTarget() {
        return this.includeBlocks ? this.knownTargets.size() > 0 : this.currentShearTargets.size() > 0;
    }

    private boolean locateShearTarget() {
        return this.includeBlocks ? this.findBlockTarget() : this.locateShearEntity();
    }

    private void setMoveTarget() {
        if (this.includeBlocks) {
            this.setMoveTarget(this.currentTarget);
        } else {
            Animal candidate;
            for (candidate = (Animal) this.currentShearTargets.get(this.currentShearIndex); !this.claimEntityMutex(candidate); candidate = (Animal) this.currentShearTargets.get(this.currentShearIndex)) {
                this.currentShearIndex++;
                if (this.currentShearIndex >= this.currentShearTargets.size()) {
                    this.clearMoveTarget();
                    return;
                }
            }
            this.construct.getDiagnostics().pushTaskUpdate(this.getId(), this.guiIcon, IConstructDiagnostics.Status.RUNNING, candidate.m_19879_());
            this.setMoveTarget(candidate);
        }
    }

    private boolean shearTarget() {
        return this.includeBlocks ? this.shearBlock() : this.shearEntity();
    }

    private boolean locateShearEntity() {
        AbstractGolem c = this.getConstructAsEntity();
        this.currentShearIndex = 0;
        this.currentShearTargets = c.m_9236_().m_6443_(Animal.class, this.area, e -> e.m_6084_() && (e instanceof IForgeShearable && ((IForgeShearable) e).isShearable(this.shearsStack, c.m_9236_(), e.m_20183_()) || e instanceof Chicken && this.canChickenBeSheared((Chicken) e)));
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_target", new Object[] { this.currentShearTargets.size() }), false);
        return this.currentShearTargets.size() > 0;
    }

    private boolean shearBlock() {
        AbstractGolem c = this.getConstructAsEntity();
        List<ItemStack> loot = ShearHelper.shearBlock((ServerLevel) this.construct.asEntity().m_9236_(), this.getMoveBlockTarget(), Direction.UP, this.createFakePlayer());
        if (loot == null) {
            BlockState state = c.m_9236_().getBlockState(this.getMoveBlockTarget());
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_not_shearable", new Object[] { this.translate(state) }), false);
            return false;
        } else {
            Random rand = new Random();
            loot.forEach(d -> {
                ItemEntity ent = c.m_5552_(d, 1.0F);
                ent.m_20256_(ent.m_20184_().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
            });
            this.construct.getHandWithCapability(ConstructCapability.SHEAR).ifPresent(h -> c.m_6674_(h));
            c.m_9236_().playSound(null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0F, (float) ((int) (0.9 + Math.random() * 0.2)));
            this.interactTimer = this.getInteractTime(ConstructCapability.SHEAR);
            return true;
        }
    }

    private boolean shearEntity() {
        if (this.interactTimer > 0) {
            this.interactTimer--;
            return false;
        } else {
            Animal animal = (Animal) this.currentShearTargets.get(this.currentShearIndex);
            List<ItemStack> loot = null;
            AbstractGolem c = this.getConstructAsEntity();
            if (animal instanceof Chicken chicken) {
                if (!this.canChickenBeSheared(chicken)) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_not_shearable", new Object[] { this.translate(animal) }), false);
                    return false;
                }
                this.setNextShearTime(chicken);
                loot = new ArrayList();
                loot.add(new ItemStack(Items.FEATHER, (int) (Math.random() * 3.0 + 1.0)));
                if (this.construct.getIntelligence() < 16) {
                    chicken.m_6469_(c.m_269291_().mobAttack(c), 1.0F);
                }
            } else {
                loot = ShearHelper.shearEntity((ServerLevel) c.m_9236_(), this.createFakePlayer(), animal, InteractionHand.MAIN_HAND);
            }
            if (loot == null) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.shear_not_shearable", new Object[] { this.translate(animal) }), false);
                return false;
            } else {
                Random rand = new Random();
                loot.forEach(d -> {
                    ItemEntity ent = c.m_5552_(d, 1.0F);
                    ent.m_20256_(ent.m_20184_().add((double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double) (rand.nextFloat() * 0.05F), (double) ((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
                this.construct.getHandWithCapability(ConstructCapability.SHEAR).ifPresent(h -> c.m_6674_(h));
                c.m_9236_().playSound(null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0F, (float) ((int) (0.9 + Math.random() * 0.2)));
                this.interactTimer = this.getInteractTime(ConstructCapability.SHEAR);
                return true;
            }
        }
    }

    private boolean canChickenBeSheared(Chicken chicken) {
        return !chicken.getPersistentData().contains("last_shear_time") || this.construct.asEntity().m_9236_().getGameTime() >= chicken.getPersistentData().getLong("last_shear_time");
    }

    private void setNextShearTime(Chicken chicken) {
        chicken.getPersistentData().putLong("last_shear_time", this.construct.asEntity().m_9236_().getGameTime() + 12000L + (long) ((int) (Math.random() * 12000.0)));
    }

    @Override
    public void stop() {
        super.m_8041_();
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.clearMoveTarget();
        this.currentShearTargets.clear();
        this.currentShearIndex = 0;
    }

    @Override
    protected boolean isValidBlock(BlockState state, BlockPos pos) {
        return ShearHelper.canBlockBeSheared(this.construct.asEntity().m_9236_(), state, pos);
    }

    @Override
    protected int getInteractTimer() {
        return this.getInteractTime(ConstructCapability.SHEAR);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.SHEAR);
    }

    public ConstructShear duplicate() {
        return new ConstructShear(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructShear copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructShear) {
            this.currentShearTargets.clear();
            this.currentShearTargets.addAll(((ConstructShear) other).currentShearTargets);
            this.includeBlocks = ((ConstructShear) other).includeBlocks;
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
        super.inflateParameters();
        this.getParameter("shear.boolean").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter) {
                this.includeBlocks = ((ConstructTaskBooleanParameter) param).getValue();
            }
        });
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskBooleanParameter("shear.boolean"));
        return parameters;
    }

    @Override
    protected String getAreaIdentifier() {
        return "shear.area";
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}