package fr.frinn.custommachinery.common.crafting.machine;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.crafting.ComponentNotFoundException;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IProcessor;
import fr.frinn.custommachinery.api.crafting.IProcessorTemplate;
import fr.frinn.custommachinery.api.crafting.ProcessorType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.api.requirement.IChanceableRequirement;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.api.requirement.ITickableRequirement;
import fr.frinn.custommachinery.common.crafting.CraftingContext;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.machine.MachineAppearance;
import fr.frinn.custommachinery.common.network.syncable.DoubleSyncable;
import fr.frinn.custommachinery.common.network.syncable.IntegerSyncable;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class MachineProcessor implements IProcessor, ISyncableStuff {

    private final MachineTile tile;

    private final Random rand = Utils.RAND;

    private final List<IRequirement<?>> processedRequirements;

    private final CraftingContext.Mutable mutableCraftingContext;

    private final int amount;

    private CustomMachineRecipe currentRecipe;

    private ResourceLocation futureRecipeID;

    private double recipeProgressTime = 0.0;

    private int recipeTotalTime = 0;

    private CraftingContext context;

    private boolean initialized = false;

    private List<ITickableRequirement<IMachineComponent>> tickableRequirements;

    private List<IDelayedRequirement<IMachineComponent>> delayedRequirements;

    private MachineProcessor.PHASE phase = MachineProcessor.PHASE.STARTING;

    private final MachineRecipeFinder recipeFinder;

    public MachineProcessor(MachineTile tile, int amount, int recipeCheckCooldown) {
        this.tile = tile;
        this.amount = amount;
        this.mutableCraftingContext = new CraftingContext.Mutable(this, tile.getUpgradeManager());
        this.processedRequirements = new ArrayList();
        this.recipeFinder = new MachineRecipeFinder(tile, recipeCheckCooldown);
    }

    @Override
    public void tick() {
        if (!this.initialized) {
            this.init();
        }
        if (this.currentRecipe == null) {
            this.searchForRecipe(false);
        }
        if (this.currentRecipe != null) {
            if (this.phase == MachineProcessor.PHASE.STARTING) {
                this.startProcess();
            }
            if (this.phase == MachineProcessor.PHASE.CRAFTING_TICKABLE) {
                this.processTickable();
            }
            if (this.phase == MachineProcessor.PHASE.CRAFTING_DELAYED) {
                this.processDelayed();
            }
            if (this.phase == MachineProcessor.PHASE.ENDING) {
                this.endProcess();
            }
        } else {
            this.tile.setStatus(MachineStatus.IDLE);
            this.tile.setCustomAppearance(null);
            this.tile.setCustomGuiElements(null);
        }
    }

    private void init() {
        this.initialized = true;
        this.recipeFinder.init();
        if (this.futureRecipeID != null && this.tile.m_58904_() != null) {
            this.tile.m_58904_().getRecipeManager().byKey(this.futureRecipeID).filter(recipe -> recipe instanceof CustomMachineRecipe).map(recipe -> (CustomMachineRecipe) recipe).ifPresent(this::setOldRecipe);
            this.futureRecipeID = null;
        }
    }

    private void searchForRecipe(boolean immediately) {
        this.recipeFinder.findRecipe(this.mutableCraftingContext, immediately).ifPresent(this::setRecipe);
    }

    private void startProcess() {
        for (IRequirement<?> requirement : this.currentRecipe.getRequirements()) {
            if (!this.processedRequirements.contains(requirement)) {
                IMachineComponent component = (IMachineComponent) this.tile.getComponentManager().getComponent(requirement.getComponentType()).orElseThrow(() -> new ComponentNotFoundException(this.currentRecipe, this.tile.getMachine(), requirement.getType()));
                if (requirement instanceof IChanceableRequirement && ((IRequirement<IMachineComponent>) ((IChanceableRequirement) requirement)).shouldSkip(component, this.rand, this.context)) {
                    this.processedRequirements.add(requirement);
                } else {
                    CraftingResult result = ((IRequirement<IMachineComponent>) requirement).processStart(component, this.context);
                    if (!result.isSuccess()) {
                        if (this.currentRecipe.shouldResetOnError()) {
                            this.reset();
                            return;
                        }
                        this.setErrored(result.getMessage());
                        break;
                    }
                    this.processedRequirements.add(requirement);
                }
            }
        }
        if (this.processedRequirements.size() == this.currentRecipe.getRequirements().size()) {
            this.setRunning();
            this.phase = MachineProcessor.PHASE.CRAFTING_TICKABLE;
            this.processedRequirements.clear();
        }
    }

    private void processTickable() {
        for (ITickableRequirement<IMachineComponent> tickableRequirement : this.tickableRequirements) {
            if (!this.processedRequirements.contains(tickableRequirement)) {
                IMachineComponent component = (IMachineComponent) this.tile.getComponentManager().getComponent(tickableRequirement.getComponentType()).orElseThrow(() -> new ComponentNotFoundException(this.currentRecipe, this.tile.getMachine(), tickableRequirement.getType()));
                if (tickableRequirement instanceof IChanceableRequirement && ((IChanceableRequirement) tickableRequirement).shouldSkip(component, this.rand, this.context)) {
                    this.processedRequirements.add(tickableRequirement);
                } else {
                    CraftingResult result = tickableRequirement.processTick(component, this.context);
                    if (!result.isSuccess()) {
                        if (this.currentRecipe.shouldResetOnError()) {
                            this.reset();
                            return;
                        }
                        this.setErrored(result.getMessage());
                        break;
                    }
                    this.processedRequirements.add(tickableRequirement);
                }
            }
        }
        if (this.processedRequirements.size() == this.tickableRequirements.size()) {
            this.recipeProgressTime = this.recipeProgressTime + this.context.getModifiedSpeed();
            this.setRunning();
            this.processedRequirements.clear();
        }
        this.phase = MachineProcessor.PHASE.CRAFTING_DELAYED;
    }

    private void processDelayed() {
        Iterator<IDelayedRequirement<IMachineComponent>> iterator = this.delayedRequirements.iterator();
        while (iterator.hasNext()) {
            IDelayedRequirement<IMachineComponent> delayedRequirement = (IDelayedRequirement<IMachineComponent>) iterator.next();
            if (this.recipeProgressTime / (double) this.recipeTotalTime >= delayedRequirement.getDelay()) {
                IMachineComponent component = (IMachineComponent) this.tile.getComponentManager().getComponent(delayedRequirement.getComponentType()).orElseThrow(() -> new ComponentNotFoundException(this.currentRecipe, this.tile.getMachine(), delayedRequirement.getType()));
                CraftingResult result = delayedRequirement.execute(component, this.context);
                if (!result.isSuccess()) {
                    if (this.currentRecipe.shouldResetOnError()) {
                        this.reset();
                        return;
                    }
                    this.setErrored(result.getMessage());
                    break;
                }
                iterator.remove();
            }
        }
        if (this.delayedRequirements.stream().allMatch(delayedRequirementx -> this.recipeProgressTime / (double) this.recipeTotalTime < delayedRequirementx.getDelay())) {
            if (this.recipeProgressTime >= (double) this.recipeTotalTime) {
                this.phase = MachineProcessor.PHASE.ENDING;
            } else {
                this.phase = MachineProcessor.PHASE.CRAFTING_TICKABLE;
            }
        }
    }

    private void endProcess() {
        for (IRequirement<?> requirement : this.currentRecipe.getRequirements()) {
            if (!this.processedRequirements.contains(requirement)) {
                IMachineComponent component = (IMachineComponent) this.tile.getComponentManager().getComponent(requirement.getComponentType()).orElseThrow(() -> new ComponentNotFoundException(this.currentRecipe, this.tile.getMachine(), requirement.getType()));
                if (requirement instanceof IChanceableRequirement && ((IRequirement<IMachineComponent>) ((IChanceableRequirement) requirement)).shouldSkip(component, this.rand, this.context)) {
                    this.processedRequirements.add(requirement);
                } else {
                    CraftingResult result = ((IRequirement<IMachineComponent>) requirement).processEnd(component, this.context);
                    if (!result.isSuccess()) {
                        if (this.currentRecipe.shouldResetOnError()) {
                            this.reset();
                            return;
                        }
                        this.setErrored(result.getMessage());
                        break;
                    }
                    this.processedRequirements.add(requirement);
                }
            }
        }
        if (this.processedRequirements.size() == this.currentRecipe.getRequirements().size()) {
            this.currentRecipe = null;
            this.recipeProgressTime = 0.0;
            this.context = null;
            this.processedRequirements.clear();
            this.recipeFinder.setInventoryChanged(true);
            this.searchForRecipe(true);
        }
    }

    private void setRecipe(CustomMachineRecipe recipe) {
        this.currentRecipe = recipe;
        this.context = new CraftingContext(this, this.tile.getUpgradeManager(), recipe);
        this.tickableRequirements = this.currentRecipe.getRequirements().stream().filter(requirement -> requirement instanceof ITickableRequirement).map(requirement -> (ITickableRequirement) requirement).toList();
        this.delayedRequirements = (List<IDelayedRequirement<IMachineComponent>>) this.currentRecipe.getRequirements().stream().filter(requirement -> requirement instanceof IDelayedRequirement).map(requirement -> (IDelayedRequirement) requirement).filter(requirement -> requirement.getDelay() > 0.0 && requirement.getDelay() < 1.0).collect(Collectors.toCollection(ArrayList::new));
        this.recipeTotalTime = this.currentRecipe.getRecipeTime();
        this.phase = MachineProcessor.PHASE.STARTING;
        this.setRunning();
    }

    private void setOldRecipe(CustomMachineRecipe recipe) {
        this.currentRecipe = recipe;
        this.context = new CraftingContext(this, this.tile.getUpgradeManager(), recipe);
        this.tickableRequirements = this.currentRecipe.getRequirements().stream().filter(requirement -> requirement instanceof ITickableRequirement).map(requirement -> (ITickableRequirement) requirement).toList();
        this.delayedRequirements = (List<IDelayedRequirement<IMachineComponent>>) this.currentRecipe.getRequirements().stream().filter(requirement -> requirement instanceof IDelayedRequirement).map(requirement -> (IDelayedRequirement) requirement).filter(requirement -> requirement.getDelay() > 0.0 && requirement.getDelay() < 1.0).collect(Collectors.toCollection(ArrayList::new));
        this.recipeTotalTime = this.currentRecipe.getRecipeTime();
        this.setRunning();
    }

    public void setRunning() {
        this.tile.setStatus(MachineStatus.RUNNING);
        MachineAppearance customAppearance = this.currentRecipe.getCustomAppearance(this.tile.getMachine().getAppearance(this.getTile().getStatus()));
        if (customAppearance != null) {
            this.tile.setCustomAppearance(customAppearance);
        }
        List<IGuiElement> customGuiElements = this.currentRecipe.getCustomGuiElements(this.tile.getMachine().getGuiElements());
        if (customGuiElements != null && !customGuiElements.isEmpty()) {
            this.tile.setCustomGuiElements(customGuiElements);
        }
    }

    public void setErrored(Component message) {
        this.tile.setStatus(MachineStatus.ERRORED, message);
        this.tile.setCustomAppearance(null);
        this.tile.setCustomGuiElements(null);
    }

    @Override
    public void reset() {
        this.currentRecipe = null;
        this.futureRecipeID = null;
        this.tile.setStatus(MachineStatus.IDLE);
        this.recipeProgressTime = 0.0;
        this.recipeTotalTime = 0;
        this.processedRequirements.clear();
        this.context = null;
        this.tile.setCustomAppearance(null);
        this.tile.setCustomGuiElements(null);
    }

    @Override
    public MachineTile getTile() {
        return this.tile;
    }

    public CustomMachineRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    @Override
    public double getRecipeProgressTime() {
        return this.recipeProgressTime;
    }

    public int getRecipeTotalTime() {
        return this.recipeTotalTime;
    }

    @Nullable
    @Override
    public ICraftingContext getCurrentContext() {
        return this.context;
    }

    @Override
    public ProcessorType<MachineProcessor> getType() {
        return (ProcessorType<MachineProcessor>) Registration.MACHINE_PROCESSOR.get();
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", this.getType().getId().toString());
        if (this.currentRecipe != null) {
            nbt.putString("recipe", this.currentRecipe.getId().toString());
        }
        nbt.putString("phase", this.phase.toString());
        nbt.putDouble("recipeProgressTime", this.recipeProgressTime);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (!nbt.contains("type", 8) || nbt.getString("type").equals(this.getType().getId().toString())) {
            if (nbt.contains("recipe", 8)) {
                this.futureRecipeID = new ResourceLocation(nbt.getString("recipe"));
            }
            if (nbt.contains("phase", 8)) {
                this.phase = MachineProcessor.PHASE.value(nbt.getString("phase"));
            }
            if (nbt.contains("recipeProgressTime", 6)) {
                this.recipeProgressTime = nbt.getDouble("recipeProgressTime");
            }
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(DoubleSyncable.create(() -> this.recipeProgressTime, recipeProgressTime -> this.recipeProgressTime = recipeProgressTime));
        container.accept(IntegerSyncable.create(() -> this.recipeTotalTime, recipeTotalTime -> this.recipeTotalTime = recipeTotalTime));
    }

    @Override
    public void setMachineInventoryChanged() {
        this.recipeFinder.setInventoryChanged(true);
    }

    public static enum PHASE {

        STARTING, CRAFTING_TICKABLE, CRAFTING_DELAYED, ENDING;

        public static final NamedCodec<MachineProcessor.PHASE> CODEC = NamedCodec.enumCodec(MachineProcessor.PHASE.class);

        public static MachineProcessor.PHASE value(String string) {
            return valueOf(string.toUpperCase(Locale.ENGLISH));
        }
    }

    public static class Template implements IProcessorTemplate<MachineProcessor> {

        public static final NamedCodec<MachineProcessor.Template> CODEC = NamedCodec.record(templateInstance -> templateInstance.group(NamedCodec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("amount", 1).forGetter(template -> template.amount), NamedCodec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("cooldown", 20).forGetter(template -> template.recipeCheckCooldown)).apply(templateInstance, MachineProcessor.Template::new), "Machine processor");

        public static final MachineProcessor.Template DEFAULT = new MachineProcessor.Template(1, 20);

        private final int amount;

        private final int recipeCheckCooldown;

        private Template(int amount, int cooldown) {
            this.amount = amount;
            this.recipeCheckCooldown = cooldown;
        }

        @Override
        public ProcessorType<MachineProcessor> getType() {
            return (ProcessorType<MachineProcessor>) Registration.MACHINE_PROCESSOR.get();
        }

        public MachineProcessor build(MachineTile tile) {
            return new MachineProcessor(tile, this.amount, this.recipeCheckCooldown);
        }
    }
}