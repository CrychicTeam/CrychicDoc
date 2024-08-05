package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructEntityAreaTask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructButcher extends ConstructEntityAreaTask<Animal, ConstructButcher> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.MELEE_ATTACK };

    private int attackTimer = this.getInteractTime(ConstructCapability.MELEE_ATTACK);

    private int pairs = 1;

    public ConstructButcher(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, Animal.class);
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.getSelectedTarget() == null && !this.locateTarget()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.butcher_no_target", new Object[0]), false);
            this.exitCode = 1;
        } else if (!this.getSelectedTarget().m_6084_()) {
            this.setSelectedTarget(null);
            this.exitCode = 1;
        } else {
            this.setMoveTarget(this.getSelectedTarget());
            if (this.doMove() && this.attackButcherTarget()) {
                this.exitCode = 0;
            }
        }
    }

    protected boolean entityPredicate(Animal candidate) {
        return candidate.m_6084_() && !candidate.m_6162_() && !candidate.isInLove();
    }

    protected Animal selectTarget(Collection<Animal> entities) {
        HashMap<EntityType<?>, List<Animal>> locatedEntities = new HashMap();
        entities.forEach(e -> {
            if (!locatedEntities.containsKey(e.m_6095_())) {
                locatedEntities.put(e.m_6095_(), new ArrayList(Arrays.asList(e)));
            } else {
                ((List) locatedEntities.get(e.m_6095_())).add(e);
            }
        });
        Optional<List<Animal>> target = locatedEntities.entrySet().stream().filter(e -> ((List) e.getValue()).size() > this.pairs * 2).map(e -> (List) e.getValue()).findAny();
        if (!target.isPresent()) {
            return null;
        } else {
            List<Animal> potentials = (List<Animal>) target.get();
            Optional<Animal> potential = potentials.stream().filter(a -> a.m_146764_() != 0).findAny();
            if (!potential.isPresent()) {
                potential = potentials.stream().findAny();
                if (!potential.isPresent()) {
                    return null;
                }
            }
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.butcher_target", new Object[] { this.translate(this.getSelectedTarget()) }), false);
            return (Animal) potential.get();
        }
    }

    private boolean attackButcherTarget() {
        if (this.attackTimer > 0) {
            if (this.attackTimer == 5) {
                this.construct.getHandWithCapability(ConstructCapability.MELEE_ATTACK).ifPresent(h -> this.construct.asEntity().m_6674_(h));
            }
            this.attackTimer--;
            return false;
        } else {
            this.construct.asEntity().m_7327_(this.getSelectedTarget());
            this.attackTimer = this.getInteractTime(ConstructCapability.MELEE_ATTACK);
            if (!this.getSelectedTarget().m_6084_()) {
                this.clearMoveTarget();
                this.setSelectedTarget(null);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.butcher_attack_success", new Object[0]), false);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.clearMoveTarget();
        this.setSelectedTarget(null);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.BUTCHER);
    }

    public ConstructButcher duplicate() {
        return new ConstructButcher(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructButcher copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructButcher) {
            this.setSelectedTarget(((ConstructButcher) other).getSelectedTarget());
            this.pairs = ((ConstructButcher) other).pairs;
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
        this.getParameter("butcher.pairs").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.pairs = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    protected String getAreaIdentifier() {
        return "butcher.area";
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskIntegerParameter("butcher.pairs", 1, 10));
        return parameters;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}