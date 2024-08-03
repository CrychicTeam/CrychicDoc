package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructEntityAreaTask;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructBreed extends ConstructEntityAreaTask<Animal, ConstructBreed> {

    private static final int MIN_ANIMALS = 2;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private int interactTimer = this.getInteractTime(ConstructCapability.CARRY);

    private ArrayList<Animal> currentBreedTargets;

    private int currentBreedIndex = 0;

    private boolean foundOneAnimalInLove = false;

    public ConstructBreed(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, Animal.class);
        this.currentBreedTargets = new ArrayList();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.getValidHeldBreedItems().size() == 0) {
            this.exitCode = 1;
        } else if (this.currentBreedTargets.size() < (this.foundOneAnimalInLove ? 1 : 2) && !this.locateTarget()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.breed_no_target", new Object[0]), false);
            this.exitCode = 1;
        } else {
            Animal candidate;
            for (candidate = (Animal) this.currentBreedTargets.get(this.currentBreedIndex); !this.claimEntityMutex(candidate); candidate = (Animal) this.currentBreedTargets.get(this.currentBreedIndex)) {
                this.currentBreedIndex++;
                if (this.currentBreedIndex >= this.currentBreedTargets.size()) {
                    this.forceFail();
                    return;
                }
            }
            this.setSelectedTarget(candidate);
            this.setMoveTarget(candidate);
            if (!this.hasMoveTarget()) {
                this.forceFail();
            } else {
                if (this.doMove() && this.breedTarget()) {
                    this.releaseEntityMutex(this.getSelectedTarget());
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.breed_success", new Object[] { this.translate((Entity) this.currentBreedTargets.get(this.currentBreedIndex)) }), false);
                    this.currentBreedIndex++;
                    if (this.currentBreedIndex >= this.currentBreedTargets.size()) {
                        this.setSuccessCode();
                    }
                }
            }
        }
    }

    protected boolean entityPredicate(Animal candidate) {
        return candidate.m_6084_() && !candidate.m_6162_() && candidate.m_146764_() == 0 && this.getHeldItems().stream().anyMatch(i -> candidate.isFood(i));
    }

    protected Animal selectTarget(Collection<Animal> entities) {
        this.currentBreedIndex = 0;
        HashMap<EntityType<?>, List<Animal>> locatedEntities = new HashMap();
        entities.forEach(e -> {
            if (!locatedEntities.containsKey(e.m_6095_())) {
                locatedEntities.put(e.m_6095_(), new ArrayList(Arrays.asList(e)));
            } else {
                ((List) locatedEntities.get(e.m_6095_())).add(e);
            }
        });
        Optional<List<Animal>> target = locatedEntities.entrySet().stream().filter(e -> ((List) e.getValue()).size() >= 2).filter(e -> !this.getHeldBreedItem((Animal) ((List) e.getValue()).get(0)).isEmpty()).map(e -> (List) e.getValue()).findAny();
        if (!target.isPresent()) {
            return null;
        } else {
            List<Animal> potentials = (List<Animal>) target.get();
            this.foundOneAnimalInLove = potentials.stream().anyMatch(e -> e.isInLove());
            potentials = (List<Animal>) potentials.stream().filter(e -> !e.isInLove()).collect(Collectors.toList());
            if (potentials.size() < (this.foundOneAnimalInLove ? 1 : 2)) {
                return null;
            } else {
                this.currentBreedTargets.clear();
                this.currentBreedTargets.addAll(potentials);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.breed_target", new Object[] { this.currentBreedTargets.size(), this.translate((Entity) this.currentBreedTargets.get(0)) }), false);
                return (Animal) potentials.get(0);
            }
        }
    }

    private boolean breedTarget() {
        Animal animal = (Animal) this.currentBreedTargets.get(this.currentBreedIndex);
        if (animal.isInLove()) {
            this.currentBreedTargets.remove(this.currentBreedIndex);
            this.foundOneAnimalInLove = false;
            return false;
        } else {
            ItemStack breedStack = this.getHeldBreedItem(animal);
            if (breedStack.isEmpty()) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.breed_wrong_item", new Object[0]), false);
                this.exitCode = 1;
                return false;
            } else if (this.interactTimer > 0) {
                if (this.interactTimer == 5) {
                    this.construct.getHandWithCapability(ConstructCapability.CARRY).ifPresent(h -> this.construct.asEntity().m_6674_(h));
                }
                this.interactTimer--;
                return false;
            } else {
                animal.setInLoveTime(600);
                breedStack.shrink(1);
                this.interactTimer = this.getInteractTime(ConstructCapability.CARRY);
                return true;
            }
        }
    }

    private List<Item> getValidHeldBreedItems() {
        HashMap<Item, Integer> breedItems = new HashMap();
        for (ItemStack stack : this.getHeldItems()) {
            if (breedItems.containsKey(stack.getItem())) {
                breedItems.put(stack.getItem(), (Integer) breedItems.get(stack.getItem()) + stack.getCount());
            } else {
                breedItems.put(stack.getItem(), stack.getCount());
            }
        }
        return (List<Item>) breedItems.entrySet().stream().filter(e -> (Integer) e.getValue() >= 1).map(e -> (Item) e.getKey()).collect(Collectors.toList());
    }

    private ItemStack getHeldBreedItem(Animal animal) {
        AbstractGolem c = this.getConstructAsEntity();
        if (animal.isFood(c.m_21205_())) {
            return c.m_21205_();
        } else {
            return animal.isFood(c.m_21206_()) ? c.m_21206_() : ItemStack.EMPTY;
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.clearMoveTarget();
        this.currentBreedTargets.clear();
        this.currentBreedIndex = 0;
        this.foundOneAnimalInLove = false;
    }

    @Override
    public void stop() {
        super.m_8041_();
    }

    @Override
    protected String getAreaIdentifier() {
        return "breed.area";
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.BREED);
    }

    public ConstructBreed duplicate() {
        return new ConstructBreed(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructBreed copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructBreed) {
            this.currentBreedTargets.clear();
            this.currentBreedTargets.addAll(((ConstructBreed) other).currentBreedTargets);
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
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}