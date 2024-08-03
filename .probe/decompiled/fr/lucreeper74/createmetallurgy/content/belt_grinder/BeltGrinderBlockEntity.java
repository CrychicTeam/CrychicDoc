package fr.lucreeper74.createmetallurgy.content.belt_grinder;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.RecipeConditions;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BeltGrinderBlockEntity extends KineticBlockEntity {

    private static final Object grindingRecipesKey = new Object();

    protected LazyOptional<IItemHandlerModifiable> itemCapability;

    public ProcessingInventory inv = new ProcessingInventory(this::start);

    public int processingTick;

    private int recipeIndex;

    private FilteringBehaviour filtering;

    public BeltGrinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemCapability = LazyOptional.of(() -> this.inv);
        this.recipeIndex = 0;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.filtering = new FilteringBehaviour(this, new BeltGrinderFilterSlot()).forRecipes();
        behaviours.add(this.filtering);
        behaviours.add(new DirectBeltInputBehaviour(this));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("inv", this.inv.serializeNBT());
        compound.putInt("processTicks", this.processingTick);
        compound.putInt("RecipeIndex", this.recipeIndex);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.inv.deserializeNBT(compound.getCompound("inv"));
        this.processingTick = compound.getInt("processTicks");
        this.recipeIndex = compound.getInt("RecipeIndex");
        super.read(compound, clientPacket);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.itemCapability.cast() : super.getCapability(cap, side);
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(this.f_58857_, this.f_58858_, this.inv);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSpeed() != 0.0F) {
            if (this.inv.remainingTime == -1.0F) {
                if (!this.inv.isEmpty() && !this.inv.appliedRecipe) {
                    this.start(this.inv.getStackInSlot(0));
                }
            } else {
                float processingSpeed = Mth.clamp(Math.abs(this.getSpeed()) / 24.0F, 1.0F, 128.0F);
                this.inv.remainingTime -= processingSpeed;
                if (this.inv.remainingTime > 0.0F) {
                    this.spawnParticles(this.inv.getStackInSlot(0));
                }
                if (this.inv.remainingTime < 5.0F && !this.inv.appliedRecipe) {
                    if (this.f_58857_.isClientSide && !this.isVirtual()) {
                        return;
                    }
                    this.applyRecipe();
                    this.inv.appliedRecipe = true;
                    this.inv.recipeDuration = 20.0F;
                    this.inv.remainingTime = 20.0F;
                    this.sendData();
                }
                Vec3 itemMovement = this.getItemMovementVec();
                Direction itemMovementFacing = Direction.getNearest(itemMovement.x, itemMovement.y, itemMovement.z);
                if (!(this.inv.remainingTime > 0.0F)) {
                    this.inv.remainingTime = 0.0F;
                    for (int slot = 0; slot < this.inv.getSlots(); slot++) {
                        ItemStack stack = this.inv.getStackInSlot(slot);
                        if (!stack.isEmpty()) {
                            ItemStack tryExportingToBeltFunnel = this.getBehaviour(DirectBeltInputBehaviour.TYPE).tryExportingToBeltFunnel(stack, itemMovementFacing.getOpposite(), false);
                            if (tryExportingToBeltFunnel != null) {
                                if (tryExportingToBeltFunnel.getCount() != stack.getCount()) {
                                    this.inv.setStackInSlot(slot, tryExportingToBeltFunnel);
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
                        if (!behaviour.canInsertFromSide(itemMovementFacing)) {
                            return;
                        }
                        if (this.f_58857_.isClientSide && !this.isVirtual()) {
                            return;
                        }
                        for (int slotx = 0; slotx < this.inv.getSlots(); slotx++) {
                            ItemStack stack = this.inv.getStackInSlot(slotx);
                            if (!stack.isEmpty()) {
                                ItemStack remainder = behaviour.handleInsertion(stack, itemMovementFacing, false);
                                if (!remainder.equals(stack, false)) {
                                    this.inv.setStackInSlot(slotx, remainder);
                                    changed = true;
                                }
                            }
                        }
                        if (changed) {
                            this.m_6596_();
                            this.sendData();
                        }
                    }
                    Vec3 outPos = VecHelper.getCenterOf(this.f_58858_).add(itemMovement.scale(0.5).add(0.0, 0.5, 0.0));
                    Vec3 outMotion = itemMovement.scale(0.0625).add(0.0, 0.125, 0.0);
                    for (int slotxx = 0; slotxx < this.inv.getSlots(); slotxx++) {
                        ItemStack stack = this.inv.getStackInSlot(slotxx);
                        if (!stack.isEmpty()) {
                            ItemEntity entityIn = new ItemEntity(this.f_58857_, outPos.x, outPos.y, outPos.z, stack);
                            entityIn.m_20256_(outMotion);
                            this.f_58857_.m_7967_(entityIn);
                        }
                    }
                    this.inv.clear();
                    this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, this.m_58900_().m_60734_());
                    this.inv.remainingTime = -1.0F;
                    this.sendData();
                }
            }
        }
    }

    public Vec3 getItemMovementVec() {
        boolean alongX = ((Direction) this.m_58900_().m_61143_(BeltGrinderBlock.HORIZONTAL_FACING)).getAxis() != Direction.Axis.X;
        int offset = this.getSpeed() < 0.0F ? -1 : 1;
        return new Vec3((double) (offset * (alongX ? 1 : 0)), 0.0, (double) (offset * (alongX ? 0 : -1)));
    }

    private void start(ItemStack inserted) {
        if (!this.inv.isEmpty()) {
            if (!this.f_58857_.isClientSide || this.isVirtual()) {
                List<? extends Recipe<?>> recipes = this.getRecipes();
                boolean valid = !recipes.isEmpty();
                int time = 50;
                if (recipes.isEmpty()) {
                    this.inv.remainingTime = this.inv.recipeDuration = 10.0F;
                    this.inv.appliedRecipe = false;
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
                    this.inv.remainingTime = (float) (time * Math.max(1, inserted.getCount() / 5));
                    this.inv.recipeDuration = this.inv.remainingTime;
                    this.inv.appliedRecipe = false;
                    this.sendData();
                }
            }
        }
    }

    private List<? extends Recipe<?>> getRecipes() {
        Optional<GrindingRecipe> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.f_58857_, this.inv.getStackInSlot(0), CMRecipeTypes.GRINDING.getType(), GrindingRecipe.class);
        if (assemblyRecipe.isPresent() && this.filtering.test(((GrindingRecipe) assemblyRecipe.get()).m_8043_(this.f_58857_.registryAccess()))) {
            return ImmutableList.of((GrindingRecipe) assemblyRecipe.get());
        } else {
            Predicate<Recipe<?>> types = RecipeConditions.isOfType(CMRecipeTypes.GRINDING.getType(), AllRecipeTypes.SANDPAPER_POLISHING.getType());
            List<Recipe<?>> startedSearch = RecipeFinder.get(grindingRecipesKey, this.f_58857_, types);
            return (List<? extends Recipe<?>>) startedSearch.stream().filter(RecipeConditions.outputMatchesFilter(this.filtering)).filter(RecipeConditions.firstIngredientMatches(this.inv.getStackInSlot(0))).collect(Collectors.toList());
        }
    }

    private void applyRecipe() {
        List<? extends Recipe<?>> recipes = this.getRecipes();
        if (!recipes.isEmpty()) {
            if (this.recipeIndex >= recipes.size()) {
                this.recipeIndex = 0;
            }
            Recipe<?> recipe = (Recipe<?>) recipes.get(this.recipeIndex);
            int rolls = this.inv.getStackInSlot(0).getCount();
            this.inv.clear();
            List<ItemStack> list = new ArrayList();
            for (int roll = 0; roll < rolls; roll++) {
                List<ItemStack> results = new LinkedList();
                if (recipe instanceof GrindingRecipe) {
                    results = ((GrindingRecipe) recipe).rollResults();
                } else if (recipe instanceof SandPaperPolishingRecipe) {
                    results.add(recipe.getResultItem(this.f_58857_.registryAccess()).copy());
                }
                for (int i = 0; i < results.size(); i++) {
                    ItemStack stack = (ItemStack) results.get(i);
                    ItemHelper.addToList(stack, list);
                }
            }
            for (int slot = 0; slot < list.size() && slot + 1 < this.inv.getSlots(); slot++) {
                this.inv.setStackInSlot(slot + 1, (ItemStack) list.get(slot));
            }
        }
    }

    public void insertItem(ItemEntity entity) {
        if (this.inv.isEmpty()) {
            if (entity.m_6084_()) {
                if (!this.f_58857_.isClientSide) {
                    this.inv.clear();
                    ItemStack remainder = this.inv.insertItem(0, entity.getItem().copy(), false);
                    if (remainder.isEmpty()) {
                        entity.m_146870_();
                    } else {
                        entity.setItem(remainder);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickAudio() {
        super.tickAudio();
        float speed = Math.abs(this.getSpeed());
        if (speed != 0.0F) {
            if (!this.inv.isEmpty() && AnimationTickHolder.getTicks() % 4 == 0) {
                float pitch = Mth.clamp(speed / 256.0F * 2.0F, 0.5F, 1.6F);
                AllSoundEvents.SANDING_SHORT.playAt(this.f_58857_, this.f_58858_, 0.3F, this.f_58857_.random.nextFloat() * 0.5F + pitch, true);
            }
        }
    }

    protected void spawnParticles(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            float speed = 1.0F;
            ParticleOptions particleData;
            if (stack.getItem() instanceof BlockItem) {
                particleData = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) stack.getItem()).getBlock().defaultBlockState());
            } else {
                particleData = new ItemParticleOption(ParticleTypes.ITEM, stack);
                speed = 0.125F;
            }
            RandomSource r = this.f_58857_.random;
            Vec3 vec = this.getItemMovementVec();
            Vec3 pos = VecHelper.getCenterOf(this.f_58858_);
            float offset = this.inv.recipeDuration != 0.0F ? this.inv.remainingTime / this.inv.recipeDuration : 0.0F;
            offset /= 2.0F;
            if (this.inv.appliedRecipe) {
                offset -= 0.5F;
            }
            this.f_58857_.addParticle(particleData, pos.x() + -vec.x * (double) offset, pos.y() + 0.45F, pos.z() + -vec.z * (double) offset, -vec.x * (double) speed, (double) (r.nextFloat() * speed), -vec.z * (double) speed);
        }
    }
}