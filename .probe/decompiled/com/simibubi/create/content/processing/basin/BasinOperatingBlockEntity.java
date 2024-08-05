package com.simibubi.create.content.processing.basin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.simple.DeferralBehaviour;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BasinOperatingBlockEntity extends KineticBlockEntity {

    public DeferralBehaviour basinChecker;

    public boolean basinRemoved;

    protected Recipe<?> currentRecipe;

    public BasinOperatingBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.basinChecker = new DeferralBehaviour(this, this::updateBasin);
        behaviours.add(this.basinChecker);
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        if (this.getSpeed() == 0.0F) {
            this.basinRemoved = true;
        }
        this.basinRemoved = false;
        this.basinChecker.scheduleUpdate();
    }

    @Override
    public void tick() {
        if (this.basinRemoved) {
            this.basinRemoved = false;
            this.onBasinRemoved();
            this.sendData();
        } else {
            super.tick();
        }
    }

    protected boolean updateBasin() {
        if (!this.isSpeedRequirementFulfilled()) {
            return true;
        } else if (this.getSpeed() == 0.0F) {
            return true;
        } else if (this.isRunning()) {
            return true;
        } else if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            Optional<BasinBlockEntity> basin = this.getBasin();
            if (!basin.filter(BasinBlockEntity::canContinueProcessing).isPresent()) {
                return true;
            } else {
                List<Recipe<?>> recipes = this.getMatchingRecipes();
                if (recipes.isEmpty()) {
                    return true;
                } else {
                    this.currentRecipe = (Recipe<?>) recipes.get(0);
                    this.startProcessingBasin();
                    this.sendData();
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    protected abstract boolean isRunning();

    public void startProcessingBasin() {
    }

    public boolean continueWithPreviousRecipe() {
        return true;
    }

    protected <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe) {
        if (recipe == null) {
            return false;
        } else {
            Optional<BasinBlockEntity> basin = this.getBasin();
            return !basin.isPresent() ? false : BasinRecipe.match((BasinBlockEntity) basin.get(), recipe);
        }
    }

    protected void applyBasinRecipe() {
        if (this.currentRecipe != null) {
            Optional<BasinBlockEntity> optionalBasin = this.getBasin();
            if (optionalBasin.isPresent()) {
                BasinBlockEntity basin = (BasinBlockEntity) optionalBasin.get();
                boolean wasEmpty = basin.canContinueProcessing();
                if (BasinRecipe.apply(basin, this.currentRecipe)) {
                    this.getProcessedRecipeTrigger().ifPresent(this::award);
                    basin.inputTank.sendDataImmediately();
                    if (wasEmpty && this.matchBasinRecipe(this.currentRecipe)) {
                        this.continueWithPreviousRecipe();
                        this.sendData();
                    }
                    basin.notifyChangeOfContents();
                }
            }
        }
    }

    protected List<Recipe<?>> getMatchingRecipes() {
        if ((Boolean) this.getBasin().map(BasinBlockEntity::isEmpty).orElse(true)) {
            return new ArrayList();
        } else {
            List<Recipe<?>> list = RecipeFinder.get(this.getRecipeCacheKey(), this.f_58857_, this::matchStaticFilters);
            return (List<Recipe<?>>) list.stream().filter(this::matchBasinRecipe).sorted((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size()).collect(Collectors.toList());
        }
    }

    protected abstract void onBasinRemoved();

    protected Optional<BasinBlockEntity> getBasin() {
        if (this.f_58857_ == null) {
            return Optional.empty();
        } else {
            BlockEntity basinBE = this.f_58857_.getBlockEntity(this.f_58858_.below(2));
            return !(basinBE instanceof BasinBlockEntity) ? Optional.empty() : Optional.of((BasinBlockEntity) basinBE);
        }
    }

    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.empty();
    }

    protected abstract <C extends Container> boolean matchStaticFilters(Recipe<C> var1);

    protected abstract Object getRecipeCacheKey();
}