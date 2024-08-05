package com.simibubi.create.content.kinetics.press;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class MechanicalPressBlockEntity extends BasinOperatingBlockEntity implements PressingBehaviour.PressingBehaviourSpecifics {

    private static final Object compressingRecipesKey = new Object();

    public PressingBehaviour pressingBehaviour;

    private int tracksCreated;

    private static final RecipeWrapper pressingInv = new RecipeWrapper(new ItemStackHandler(1));

    public MechanicalPressBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).expandTowards(0.0, -1.5, 0.0).expandTowards(0.0, 1.0, 0.0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.pressingBehaviour = new PressingBehaviour(this);
        behaviours.add(this.pressingBehaviour);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.PRESS, AllAdvancements.COMPACTING, AllAdvancements.TRACK_CRAFTING });
    }

    public void onItemPressed(ItemStack result) {
        this.award(AllAdvancements.PRESS);
        if (AllTags.AllBlockTags.TRACKS.matches(result)) {
            this.tracksCreated = this.tracksCreated + result.getCount();
        }
        if (this.tracksCreated >= 1000) {
            this.award(AllAdvancements.TRACK_CRAFTING);
            this.tracksCreated = 0;
        }
    }

    public PressingBehaviour getPressingBehaviour() {
        return this.pressingBehaviour;
    }

    @Override
    public boolean tryProcessInBasin(boolean simulate) {
        this.applyBasinRecipe();
        Optional<BasinBlockEntity> basin = this.getBasin();
        if (basin.isPresent()) {
            SmartInventory inputs = ((BasinBlockEntity) basin.get()).getInputInventory();
            for (int slot = 0; slot < inputs.getSlots(); slot++) {
                ItemStack stackInSlot = inputs.m_8020_(slot);
                if (!stackInSlot.isEmpty()) {
                    this.pressingBehaviour.particleItems.add(stackInSlot);
                }
            }
        }
        return true;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (this.getBehaviour(AdvancementBehaviour.TYPE).isOwnerPresent()) {
            compound.putInt("TracksCreated", this.tracksCreated);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.tracksCreated = compound.getInt("TracksCreated");
    }

    @Override
    public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate) {
        ItemStack item = itemEntity.getItem();
        Optional<PressingRecipe> recipe = this.getRecipe(item);
        if (!recipe.isPresent()) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            ItemStack itemCreated = ItemStack.EMPTY;
            this.pressingBehaviour.particleItems.add(item);
            if (!this.canProcessInBulk() && item.getCount() != 1) {
                for (ItemStack result : RecipeApplier.applyRecipeOn(this.f_58857_, ItemHandlerHelper.copyStackWithSize(item, 1), (Recipe<?>) recipe.get())) {
                    if (itemCreated.isEmpty()) {
                        itemCreated = result.copy();
                    }
                    ItemEntity created = new ItemEntity(this.f_58857_, itemEntity.m_20185_(), itemEntity.m_20186_(), itemEntity.m_20189_(), result);
                    created.setDefaultPickUpDelay();
                    created.m_20256_(VecHelper.offsetRandomly(Vec3.ZERO, this.f_58857_.random, 0.05F));
                    this.f_58857_.m_7967_(created);
                }
                item.shrink(1);
            } else {
                RecipeApplier.applyRecipeOn(itemEntity, (Recipe<?>) recipe.get());
                itemCreated = itemEntity.getItem().copy();
            }
            if (!itemCreated.isEmpty()) {
                this.onItemPressed(itemCreated);
            }
            return true;
        }
    }

    @Override
    public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate) {
        Optional<PressingRecipe> recipe = this.getRecipe(input.stack);
        if (!recipe.isPresent()) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            this.pressingBehaviour.particleItems.add(input.stack);
            List<ItemStack> outputs = RecipeApplier.applyRecipeOn(this.f_58857_, this.canProcessInBulk() ? input.stack : ItemHandlerHelper.copyStackWithSize(input.stack, 1), (Recipe<?>) recipe.get());
            for (ItemStack created : outputs) {
                if (!created.isEmpty()) {
                    this.onItemPressed(created);
                    break;
                }
            }
            outputList.addAll(outputs);
            return true;
        }
    }

    @Override
    public void onPressingCompleted() {
        if (this.pressingBehaviour.onBasin() && this.matchBasinRecipe(this.currentRecipe) && this.getBasin().filter(BasinBlockEntity::canContinueProcessing).isPresent()) {
            this.startProcessingBasin();
        } else {
            this.basinChecker.scheduleUpdate();
        }
    }

    public Optional<PressingRecipe> getRecipe(ItemStack item) {
        Optional<PressingRecipe> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.f_58857_, item, AllRecipeTypes.PRESSING.getType(), PressingRecipe.class);
        if (assemblyRecipe.isPresent()) {
            return assemblyRecipe;
        } else {
            pressingInv.setItem(0, item);
            return AllRecipeTypes.PRESSING.find(pressingInv, this.f_58857_);
        }
    }

    public static <C extends Container> boolean canCompress(Recipe<C> recipe) {
        if (recipe instanceof CraftingRecipe && AllConfigs.server().recipes.allowShapedSquareInPress.get()) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            return (ingredients.size() == 4 || ingredients.size() == 9) && ItemHelper.matchAllIngredients(ingredients);
        } else {
            return false;
        }
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe instanceof CraftingRecipe && !(recipe instanceof MechanicalCraftingRecipe) && canCompress(recipe) && !AllRecipeTypes.shouldIgnoreInAutomation(recipe) || recipe.getType() == AllRecipeTypes.COMPACTING.getType();
    }

    @Override
    public float getKineticSpeed() {
        return this.getSpeed();
    }

    @Override
    public boolean canProcessInBulk() {
        return AllConfigs.server().recipes.bulkPressing.get();
    }

    @Override
    protected Object getRecipeCacheKey() {
        return compressingRecipesKey;
    }

    @Override
    public int getParticleAmount() {
        return 15;
    }

    @Override
    public void startProcessingBasin() {
        if (!this.pressingBehaviour.running || this.pressingBehaviour.runningTicks > 120) {
            super.startProcessingBasin();
            this.pressingBehaviour.start(PressingBehaviour.Mode.BASIN);
        }
    }

    @Override
    protected void onBasinRemoved() {
        this.pressingBehaviour.particleItems.clear();
        this.pressingBehaviour.running = false;
        this.pressingBehaviour.runningTicks = 0;
        this.sendData();
    }

    @Override
    protected boolean isRunning() {
        return this.pressingBehaviour.running;
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.of(AllAdvancements.COMPACTING);
    }
}