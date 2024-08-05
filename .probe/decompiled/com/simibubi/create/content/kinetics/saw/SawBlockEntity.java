package com.simibubi.create.content.kinetics.saw;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.RecipeConditions;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.TreeCutter;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.KelpPlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SawBlockEntity extends BlockBreakingKineticBlockEntity {

    private static final Object cuttingRecipesKey = new Object();

    public static final Supplier<RecipeType<?>> woodcuttingRecipeType = Suppliers.memoize(() -> ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation("druidcraft", "woodcutting")));

    public ProcessingInventory inventory = new ProcessingInventory(this::start).withSlotLimit(!AllConfigs.server().recipes.bulkCutting.get());

    private int recipeIndex;

    private final LazyOptional<IItemHandler> invProvider;

    private FilteringBehaviour filtering;

    private ItemStack playEvent;

    public SawBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory.remainingTime = -1.0F;
        this.recipeIndex = 0;
        this.invProvider = LazyOptional.of(() -> this.inventory);
        this.playEvent = ItemStack.EMPTY;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.filtering = new FilteringBehaviour(this, new SawFilterSlot()).forRecipes();
        behaviours.add(this.filtering);
        behaviours.add(new DirectBeltInputBehaviour(this).allowingBeltFunnelsWhen(this::canProcess));
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.SAW_PROCESSING });
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putInt("RecipeIndex", this.recipeIndex);
        super.write(compound, clientPacket);
        if (clientPacket && !this.playEvent.isEmpty()) {
            compound.put("PlayEvent", this.playEvent.serializeNBT());
            this.playEvent = ItemStack.EMPTY;
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.recipeIndex = compound.getInt("RecipeIndex");
        if (compound.contains("PlayEvent")) {
            this.playEvent = ItemStack.of(compound.getCompound("PlayEvent"));
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).inflate(0.125);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickAudio() {
        super.tickAudio();
        if (this.getSpeed() != 0.0F) {
            if (!this.playEvent.isEmpty()) {
                boolean isWood = false;
                Item item = this.playEvent.getItem();
                if (item instanceof BlockItem) {
                    Block block = ((BlockItem) item).getBlock();
                    isWood = block.getSoundType(block.defaultBlockState(), this.f_58857_, this.f_58858_, null) == SoundType.WOOD;
                }
                this.spawnEventParticles(this.playEvent);
                this.playEvent = ItemStack.EMPTY;
                if (!isWood) {
                    AllSoundEvents.SAW_ACTIVATE_STONE.playAt(this.f_58857_, this.f_58858_, 3.0F, 1.0F, true);
                } else {
                    AllSoundEvents.SAW_ACTIVATE_WOOD.playAt(this.f_58857_, this.f_58858_, 3.0F, 1.0F, true);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (this.shouldRun() && this.ticksUntilNextProgress < 0) {
            this.destroyNextTick();
        }
        super.tick();
        if (this.canProcess()) {
            if (this.getSpeed() != 0.0F) {
                if (this.inventory.remainingTime == -1.0F) {
                    if (!this.inventory.isEmpty() && !this.inventory.appliedRecipe) {
                        this.start(this.inventory.getStackInSlot(0));
                    }
                } else {
                    float processingSpeed = Mth.clamp(Math.abs(this.getSpeed()) / 24.0F, 1.0F, 128.0F);
                    this.inventory.remainingTime -= processingSpeed;
                    if (this.inventory.remainingTime > 0.0F) {
                        this.spawnParticles(this.inventory.getStackInSlot(0));
                    }
                    if (!(this.inventory.remainingTime < 5.0F) || this.inventory.appliedRecipe) {
                        Vec3 itemMovement = this.getItemMovementVec();
                        Direction itemMovementFacing = Direction.getNearest(itemMovement.x, itemMovement.y, itemMovement.z);
                        if (!(this.inventory.remainingTime > 0.0F)) {
                            this.inventory.remainingTime = 0.0F;
                            for (int slot = 0; slot < this.inventory.getSlots(); slot++) {
                                ItemStack stack = this.inventory.getStackInSlot(slot);
                                if (!stack.isEmpty()) {
                                    ItemStack tryExportingToBeltFunnel = this.getBehaviour(DirectBeltInputBehaviour.TYPE).tryExportingToBeltFunnel(stack, itemMovementFacing.getOpposite(), false);
                                    if (tryExportingToBeltFunnel != null) {
                                        if (tryExportingToBeltFunnel.getCount() != stack.getCount()) {
                                            this.inventory.setStackInSlot(slot, tryExportingToBeltFunnel);
                                            this.notifyUpdate();
                                            return;
                                        }
                                        if (!tryExportingToBeltFunnel.isEmpty()) {
                                            return;
                                        }
                                    }
                                }
                            }
                            BlockPos nextPos = this.f_58858_.offset(BlockPos.containing(itemMovement));
                            DirectBeltInputBehaviour behaviour = BlockEntityBehaviour.get(this.f_58857_, nextPos, DirectBeltInputBehaviour.TYPE);
                            if (behaviour != null) {
                                boolean changed = false;
                                if (behaviour.canInsertFromSide(itemMovementFacing)) {
                                    if (!this.f_58857_.isClientSide || this.isVirtual()) {
                                        for (int slotx = 0; slotx < this.inventory.getSlots(); slotx++) {
                                            ItemStack stack = this.inventory.getStackInSlot(slotx);
                                            if (!stack.isEmpty()) {
                                                ItemStack remainder = behaviour.handleInsertion(stack, itemMovementFacing, false);
                                                if (!remainder.equals(stack, false)) {
                                                    this.inventory.setStackInSlot(slotx, remainder);
                                                    changed = true;
                                                }
                                            }
                                        }
                                        if (changed) {
                                            this.m_6596_();
                                            this.sendData();
                                        }
                                    }
                                }
                            } else {
                                Vec3 outPos = VecHelper.getCenterOf(this.f_58858_).add(itemMovement.scale(0.5).add(0.0, 0.5, 0.0));
                                Vec3 outMotion = itemMovement.scale(0.0625).add(0.0, 0.125, 0.0);
                                for (int slotxx = 0; slotxx < this.inventory.getSlots(); slotxx++) {
                                    ItemStack stack = this.inventory.getStackInSlot(slotxx);
                                    if (!stack.isEmpty()) {
                                        ItemEntity entityIn = new ItemEntity(this.f_58857_, outPos.x, outPos.y, outPos.z, stack);
                                        entityIn.m_20256_(outMotion);
                                        this.f_58857_.m_7967_(entityIn);
                                    }
                                }
                                this.inventory.clear();
                                this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, this.m_58900_().m_60734_());
                                this.inventory.remainingTime = -1.0F;
                                this.sendData();
                            }
                        }
                    } else if (!this.f_58857_.isClientSide || this.isVirtual()) {
                        this.playEvent = this.inventory.getStackInSlot(0);
                        this.applyRecipe();
                        this.inventory.appliedRecipe = true;
                        this.inventory.recipeDuration = 20.0F;
                        this.inventory.remainingTime = 20.0F;
                        this.sendData();
                    }
                }
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.invProvider.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(this.f_58857_, this.f_58858_, this.inventory);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER && side != Direction.DOWN ? this.invProvider.cast() : super.getCapability(cap, side);
    }

    protected void spawnEventParticles(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            ParticleOptions particleData = null;
            if (stack.getItem() instanceof BlockItem) {
                particleData = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) stack.getItem()).getBlock().defaultBlockState());
            } else {
                particleData = new ItemParticleOption(ParticleTypes.ITEM, stack);
            }
            RandomSource r = this.f_58857_.random;
            Vec3 v = VecHelper.getCenterOf(this.f_58858_).add(0.0, 0.3125, 0.0);
            for (int i = 0; i < 10; i++) {
                Vec3 m = VecHelper.offsetRandomly(new Vec3(0.0, 0.25, 0.0), r, 0.125F);
                this.f_58857_.addParticle(particleData, v.x, v.y, v.z, m.x, m.y, m.y);
            }
        }
    }

    protected void spawnParticles(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            ParticleOptions particleData = null;
            float speed = 1.0F;
            if (stack.getItem() instanceof BlockItem) {
                particleData = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) stack.getItem()).getBlock().defaultBlockState());
            } else {
                particleData = new ItemParticleOption(ParticleTypes.ITEM, stack);
                speed = 0.125F;
            }
            RandomSource r = this.f_58857_.random;
            Vec3 vec = this.getItemMovementVec();
            Vec3 pos = VecHelper.getCenterOf(this.f_58858_);
            float offset = this.inventory.recipeDuration != 0.0F ? this.inventory.remainingTime / this.inventory.recipeDuration : 0.0F;
            offset /= 2.0F;
            if (this.inventory.appliedRecipe) {
                offset -= 0.5F;
            }
            this.f_58857_.addParticle(particleData, pos.x() + -vec.x * (double) offset, pos.y() + 0.45F, pos.z() + -vec.z * (double) offset, -vec.x * (double) speed, (double) (r.nextFloat() * speed), -vec.z * (double) speed);
        }
    }

    public Vec3 getItemMovementVec() {
        boolean alongX = !(Boolean) this.m_58900_().m_61143_(SawBlock.AXIS_ALONG_FIRST_COORDINATE);
        int offset = this.getSpeed() < 0.0F ? -1 : 1;
        return new Vec3((double) (offset * (alongX ? 1 : 0)), 0.0, (double) (offset * (alongX ? 0 : -1)));
    }

    private void applyRecipe() {
        List<? extends Recipe<?>> recipes = this.getRecipes();
        if (!recipes.isEmpty()) {
            if (this.recipeIndex >= recipes.size()) {
                this.recipeIndex = 0;
            }
            Recipe<?> recipe = (Recipe<?>) recipes.get(this.recipeIndex);
            int rolls = this.inventory.getStackInSlot(0).getCount();
            this.inventory.clear();
            List<ItemStack> list = new ArrayList();
            for (int roll = 0; roll < rolls; roll++) {
                List<ItemStack> results = new LinkedList();
                if (recipe instanceof CuttingRecipe) {
                    results = ((CuttingRecipe) recipe).rollResults();
                } else if (recipe instanceof StonecutterRecipe || recipe.getType() == woodcuttingRecipeType.get()) {
                    results.add(recipe.getResultItem(this.f_58857_.registryAccess()).copy());
                }
                for (int i = 0; i < results.size(); i++) {
                    ItemStack stack = (ItemStack) results.get(i);
                    ItemHelper.addToList(stack, list);
                }
            }
            for (int slot = 0; slot < list.size() && slot + 1 < this.inventory.getSlots(); slot++) {
                this.inventory.setStackInSlot(slot + 1, (ItemStack) list.get(slot));
            }
            this.award(AllAdvancements.SAW_PROCESSING);
        }
    }

    private List<? extends Recipe<?>> getRecipes() {
        Optional<CuttingRecipe> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.f_58857_, this.inventory.getStackInSlot(0), AllRecipeTypes.CUTTING.getType(), CuttingRecipe.class);
        if (assemblyRecipe.isPresent() && this.filtering.test(((CuttingRecipe) assemblyRecipe.get()).m_8043_(this.f_58857_.registryAccess()))) {
            return ImmutableList.of((CuttingRecipe) assemblyRecipe.get());
        } else {
            Predicate<Recipe<?>> types = RecipeConditions.isOfType(AllRecipeTypes.CUTTING.getType(), AllConfigs.server().recipes.allowStonecuttingOnSaw.get() ? RecipeType.STONECUTTING : null, AllConfigs.server().recipes.allowWoodcuttingOnSaw.get() ? (RecipeType) woodcuttingRecipeType.get() : null);
            List<Recipe<?>> startedSearch = RecipeFinder.get(cuttingRecipesKey, this.f_58857_, types);
            return (List<? extends Recipe<?>>) startedSearch.stream().filter(RecipeConditions.outputMatchesFilter(this.filtering)).filter(RecipeConditions.firstIngredientMatches(this.inventory.getStackInSlot(0))).filter(r -> !AllRecipeTypes.shouldIgnoreInAutomation(r)).collect(Collectors.toList());
        }
    }

    public void insertItem(ItemEntity entity) {
        if (this.canProcess()) {
            if (this.inventory.isEmpty()) {
                if (entity.m_6084_()) {
                    if (!this.f_58857_.isClientSide) {
                        this.inventory.clear();
                        ItemStack remainder = this.inventory.insertItem(0, entity.getItem().copy(), false);
                        if (remainder.isEmpty()) {
                            entity.m_146870_();
                        } else {
                            entity.setItem(remainder);
                        }
                    }
                }
            }
        }
    }

    public void start(ItemStack inserted) {
        if (this.canProcess()) {
            if (!this.inventory.isEmpty()) {
                if (!this.f_58857_.isClientSide || this.isVirtual()) {
                    List<? extends Recipe<?>> recipes = this.getRecipes();
                    boolean valid = !recipes.isEmpty();
                    int time = 50;
                    if (recipes.isEmpty()) {
                        this.inventory.remainingTime = this.inventory.recipeDuration = 10.0F;
                        this.inventory.appliedRecipe = false;
                        this.sendData();
                    } else {
                        if (valid) {
                            this.recipeIndex++;
                            if (this.recipeIndex >= recipes.size()) {
                                this.recipeIndex = 0;
                            }
                        }
                        Recipe<?> recipe = (Recipe<?>) recipes.get(this.recipeIndex);
                        if (recipe instanceof CuttingRecipe) {
                            time = ((CuttingRecipe) recipe).getProcessingDuration();
                        }
                        this.inventory.remainingTime = (float) (time * Math.max(1, inserted.getCount() / 5));
                        this.inventory.recipeDuration = this.inventory.remainingTime;
                        this.inventory.appliedRecipe = false;
                        this.sendData();
                    }
                }
            }
        }
    }

    protected boolean canProcess() {
        return this.m_58900_().m_61143_(SawBlock.FACING) == Direction.UP;
    }

    @Override
    protected boolean shouldRun() {
        return ((Direction) this.m_58900_().m_61143_(SawBlock.FACING)).getAxis().isHorizontal();
    }

    @Override
    protected BlockPos getBreakingPos() {
        return this.m_58899_().relative((Direction) this.m_58900_().m_61143_(SawBlock.FACING));
    }

    @Override
    public void onBlockBroken(BlockState stateToBreak) {
        Optional<AbstractBlockBreakQueue> dynamicTree = TreeCutter.findDynamicTree(stateToBreak.m_60734_(), this.breakingPos);
        if (dynamicTree.isPresent()) {
            ((AbstractBlockBreakQueue) dynamicTree.get()).destroyBlocks(this.f_58857_, null, this::dropItemFromCutTree);
        } else {
            super.onBlockBroken(stateToBreak);
            TreeCutter.findTree(this.f_58857_, this.breakingPos).destroyBlocks(this.f_58857_, null, this::dropItemFromCutTree);
        }
    }

    public void dropItemFromCutTree(BlockPos pos, ItemStack stack) {
        float distance = (float) Math.sqrt(pos.m_123331_(this.breakingPos));
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(this.f_58857_, dropPos.x, dropPos.y, dropPos.z, stack);
        entity.m_20256_(Vec3.atLowerCornerOf(this.breakingPos.subtract(this.f_58858_)).scale((double) (distance / 20.0F)));
        this.f_58857_.m_7967_(entity);
    }

    @Override
    public boolean canBreak(BlockState stateToBreak, float blockHardness) {
        boolean sawable = isSawable(stateToBreak);
        return super.canBreak(stateToBreak, blockHardness) && sawable;
    }

    public static boolean isSawable(BlockState stateToBreak) {
        if (stateToBreak.m_204336_(BlockTags.SAPLINGS)) {
            return false;
        } else if (TreeCutter.isLog(stateToBreak) || stateToBreak.m_204336_(BlockTags.LEAVES)) {
            return true;
        } else if (TreeCutter.isRoot(stateToBreak)) {
            return true;
        } else {
            Block block = stateToBreak.m_60734_();
            if (block instanceof BambooStalkBlock) {
                return true;
            } else if (block instanceof StemGrownBlock) {
                return true;
            } else if (block instanceof CactusBlock) {
                return true;
            } else if (block instanceof SugarCaneBlock) {
                return true;
            } else if (block instanceof KelpPlantBlock) {
                return true;
            } else if (block instanceof KelpBlock) {
                return true;
            } else {
                return block instanceof ChorusPlantBlock ? true : TreeCutter.canDynamicTreeCutFrom(block);
            }
        }
    }
}