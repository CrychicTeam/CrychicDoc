package fr.frinn.custommachinery.common.crafting.craft;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.ComponentNotFoundException;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IProcessor;
import fr.frinn.custommachinery.api.crafting.IProcessorTemplate;
import fr.frinn.custommachinery.api.crafting.ProcessorType;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.api.requirement.IChanceableRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.common.component.variant.item.ResultItemComponentVariant;
import fr.frinn.custommachinery.common.crafting.CraftingContext;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CraftProcessor implements IProcessor {

    private final MachineTile tile;

    private final Random rand = Utils.RAND;

    private final CraftingContext.Mutable mutableCraftingContext;

    private final CraftRecipeFinder recipeFinder;

    private boolean shouldCheck = true;

    private int recipeCheckCooldown = this.rand.nextInt(20);

    @Nullable
    private CraftingContext currentContext;

    @Nullable
    private CustomCraftRecipe currentRecipe;

    private boolean initialized = false;

    public CraftProcessor(MachineTile tile) {
        this.tile = tile;
        this.mutableCraftingContext = new CraftingContext.Mutable(this, tile.getUpgradeManager());
        this.recipeFinder = new CraftRecipeFinder(tile, 20);
    }

    @Nullable
    @Override
    public ICraftingContext getCurrentContext() {
        return this.currentContext;
    }

    @Override
    public ProcessorType<CraftProcessor> getType() {
        return (ProcessorType<CraftProcessor>) Registration.CRAFT_PROCESSOR.get();
    }

    @Override
    public MachineTile getTile() {
        return this.tile;
    }

    @Override
    public double getRecipeProgressTime() {
        return 0.0;
    }

    @Override
    public void tick() {
        if (!this.initialized) {
            this.recipeFinder.init();
            this.initialized = true;
        }
        if (this.currentRecipe == null) {
            this.recipeFinder.findRecipe(this.mutableCraftingContext, this.shouldCheck).ifPresent(this::setCurrentRecipe);
        } else if (this.mutableCraftingContext != null && (this.shouldCheck || this.recipeCheckCooldown-- == 0)) {
            this.recipeCheckCooldown = 20;
            if (!this.checkRecipe(this.currentRecipe, this.currentContext)) {
                this.reset();
            }
        }
        this.shouldCheck = false;
    }

    @Override
    public void setMachineInventoryChanged() {
        this.shouldCheck = true;
        this.recipeFinder.setInventoryChanged(true);
    }

    public void craft() {
        if (this.currentRecipe != null && this.currentContext != null) {
            this.processRecipe(this.currentRecipe, this.currentContext);
            this.reset();
        }
    }

    public boolean bulkCraft() {
        if (this.currentRecipe != null && this.currentContext != null) {
            this.processRecipe(this.currentRecipe, this.currentContext);
            if (this.checkRecipe(this.currentRecipe, this.currentContext)) {
                this.setCurrentRecipe(this.currentRecipe);
                return true;
            } else {
                this.reset();
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkRecipe(CustomCraftRecipe recipe, CraftingContext context) {
        return recipe.getRequirements().stream().allMatch(requirement -> {
            IMachineComponent component = (IMachineComponent) this.tile.getComponentManager().getComponent(requirement.getComponentType()).orElseThrow(() -> new ComponentNotFoundException(recipe, this.tile.getMachine(), requirement.getType()));
            return requirement.test(component, context);
        });
    }

    private void setCurrentRecipe(CustomCraftRecipe recipe) {
        this.currentRecipe = recipe;
        this.currentContext = new CraftingContext(this, this.tile.getUpgradeManager(), recipe);
        this.tile.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponents().stream().filter(component -> component.getVariant() == ResultItemComponentVariant.INSTANCE).findFirst()).ifPresent(component -> component.setItemStack(recipe.getOutput().copy()));
    }

    private void processRecipe(CustomCraftRecipe recipe, CraftingContext context) {
        for (IRequirement<?> requirement : recipe.getRequirements()) {
            IMachineComponent component = (IMachineComponent) this.tile.getComponentManager().getComponent(requirement.getComponentType()).orElseThrow(() -> new ComponentNotFoundException(recipe, this.tile.getMachine(), requirement.getType()));
            if (!(requirement instanceof IChanceableRequirement chanceable) || !chanceable.shouldSkip(component, this.rand, context)) {
                ((IRequirement<IMachineComponent>) requirement).processStart(component, context);
                ((IRequirement<IMachineComponent>) requirement).processEnd(component, context);
            }
        }
    }

    @Override
    public void reset() {
        this.currentRecipe = null;
        this.currentContext = null;
        this.tile.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponents().stream().filter(component -> component.getVariant() == ResultItemComponentVariant.INSTANCE).findFirst()).ifPresent(component -> component.setItemStack(ItemStack.EMPTY));
    }

    @Override
    public CompoundTag serialize() {
        return new CompoundTag();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
    }

    public static class Template implements IProcessorTemplate<CraftProcessor> {

        public static final NamedCodec<CraftProcessor.Template> CODEC = NamedCodec.unit(CraftProcessor.Template::new, "Craft processor");

        public static final CraftProcessor.Template DEFAULT = new CraftProcessor.Template();

        @Override
        public ProcessorType<CraftProcessor> getType() {
            return (ProcessorType<CraftProcessor>) Registration.CRAFT_PROCESSOR.get();
        }

        public CraftProcessor build(MachineTile tile) {
            return new CraftProcessor(tile);
        }
    }
}