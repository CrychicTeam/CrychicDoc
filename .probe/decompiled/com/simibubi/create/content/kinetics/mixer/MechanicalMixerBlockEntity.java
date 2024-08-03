package com.simibubi.create.content.kinetics.mixer;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.items.IItemHandler;

public class MechanicalMixerBlockEntity extends BasinOperatingBlockEntity {

    private static final Object shapelessOrMixingRecipesKey = new Object();

    public int runningTicks;

    public int processingTicks;

    public boolean running;

    public MechanicalMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public float getRenderedHeadOffset(float partialTicks) {
        float offset = 0.0F;
        if (this.running) {
            if (this.runningTicks < 20) {
                int localTick = this.runningTicks;
                float num = ((float) localTick + partialTicks) / 20.0F;
                num = (2.0F - Mth.cos((float) ((double) num * Math.PI))) / 2.0F;
                offset = num - 0.5F;
            } else if (this.runningTicks <= 20) {
                offset = 1.0F;
            } else {
                int localTick = 40 - this.runningTicks;
                float num = ((float) localTick - partialTicks) / 20.0F;
                num = (2.0F - Mth.cos((float) ((double) num * Math.PI))) / 2.0F;
                offset = num - 0.5F;
            }
        }
        return offset + 0.4375F;
    }

    public float getRenderedHeadRotationSpeed(float partialTicks) {
        float speed = this.getSpeed();
        if (this.running) {
            if (this.runningTicks < 15) {
                return speed;
            } else {
                return this.runningTicks <= 20 ? speed * 2.0F : speed;
            }
        } else {
            return speed / 2.0F;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.MIXER });
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).expandTowards(0.0, -1.5, 0.0);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.running = compound.getBoolean("Running");
        this.runningTicks = compound.getInt("Ticks");
        super.read(compound, clientPacket);
        if (clientPacket && this.m_58898_()) {
            this.getBasin().ifPresent(bte -> bte.setAreFluidsMoving(this.running && this.runningTicks <= 20));
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putInt("Ticks", this.runningTicks);
        super.write(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.runningTicks >= 40) {
            this.running = false;
            this.runningTicks = 0;
            this.basinChecker.scheduleUpdate();
        } else {
            float speed = Math.abs(this.getSpeed());
            if (this.running && this.f_58857_ != null) {
                if (this.f_58857_.isClientSide && this.runningTicks == 20) {
                    this.renderParticles();
                }
                if ((!this.f_58857_.isClientSide || this.isVirtual()) && this.runningTicks == 20) {
                    if (this.processingTicks < 0) {
                        float recipeSpeed = 1.0F;
                        if (this.currentRecipe instanceof ProcessingRecipe) {
                            int t = ((ProcessingRecipe) this.currentRecipe).getProcessingDuration();
                            if (t != 0) {
                                recipeSpeed = (float) t / 100.0F;
                            }
                        }
                        this.processingTicks = Mth.clamp(Mth.log2((int) (512.0F / speed)) * Mth.ceil(recipeSpeed * 15.0F) + 1, 1, 512);
                        Optional<BasinBlockEntity> basin = this.getBasin();
                        if (basin.isPresent()) {
                            Couple<SmartFluidTankBehaviour> tanks = ((BasinBlockEntity) basin.get()).getTanks();
                            if (!tanks.getFirst().isEmpty() || !tanks.getSecond().isEmpty()) {
                                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.75F, speed < 65.0F ? 0.75F : 1.5F);
                            }
                        }
                    } else {
                        this.processingTicks--;
                        if (this.processingTicks == 0) {
                            this.runningTicks++;
                            this.processingTicks = -1;
                            this.applyBasinRecipe();
                            this.sendData();
                        }
                    }
                }
                if (this.runningTicks != 20) {
                    this.runningTicks++;
                }
            }
        }
    }

    public void renderParticles() {
        Optional<BasinBlockEntity> basin = this.getBasin();
        if (basin.isPresent() && this.f_58857_ != null) {
            for (SmartInventory inv : ((BasinBlockEntity) basin.get()).getInvs()) {
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    ItemStack stackInSlot = inv.m_8020_(slot);
                    if (!stackInSlot.isEmpty()) {
                        ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                        this.spillParticle(data);
                    }
                }
            }
            for (SmartFluidTankBehaviour behaviour : ((BasinBlockEntity) basin.get()).getTanks()) {
                if (behaviour != null) {
                    for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                        if (!tankSegment.isEmpty(0.0F)) {
                            this.spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
                        }
                    }
                }
            }
        }
    }

    protected void spillParticle(ParticleOptions data) {
        float angle = this.f_58857_.random.nextFloat() * 360.0F;
        Vec3 offset = new Vec3(0.0, 0.0, 0.25);
        offset = VecHelper.rotate(offset, (double) angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, this.getSpeed() > 0.0F ? 25.0 : -25.0, Direction.Axis.Y).add(0.0, 0.25, 0.0);
        Vec3 center = offset.add(VecHelper.getCenterOf(this.f_58858_));
        target = VecHelper.offsetRandomly(target.subtract(offset), this.f_58857_.random, 0.0078125F);
        this.f_58857_.addParticle(data, center.x, center.y - 1.75, center.z, target.x, target.y, target.z);
    }

    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> matchingRecipes = super.getMatchingRecipes();
        if (!AllConfigs.server().recipes.allowBrewingInMixer.get()) {
            return matchingRecipes;
        } else {
            Optional<BasinBlockEntity> basin = this.getBasin();
            if (!basin.isPresent()) {
                return matchingRecipes;
            } else {
                BasinBlockEntity basinBlockEntity = (BasinBlockEntity) basin.get();
                if (basin.isEmpty()) {
                    return matchingRecipes;
                } else {
                    IItemHandler availableItems = (IItemHandler) basinBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
                    if (availableItems == null) {
                        return matchingRecipes;
                    } else {
                        for (int i = 0; i < availableItems.getSlots(); i++) {
                            ItemStack stack = availableItems.getStackInSlot(i);
                            if (!stack.isEmpty()) {
                                List<MixingRecipe> list = (List<MixingRecipe>) PotionMixingRecipes.BY_ITEM.get(stack.getItem());
                                if (list != null) {
                                    for (MixingRecipe mixingRecipe : list) {
                                        if (this.matchBasinRecipe(mixingRecipe)) {
                                            matchingRecipes.add(mixingRecipe);
                                        }
                                    }
                                }
                            }
                        }
                        return matchingRecipes;
                    }
                }
            }
        }
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof CraftingRecipe && !(r instanceof IShapedRecipe) && AllConfigs.server().recipes.allowShapelessInMixer.get() && r.getIngredients().size() > 1 && !MechanicalPressBlockEntity.canCompress(r) && !AllRecipeTypes.shouldIgnoreInAutomation(r) || r.getType() == AllRecipeTypes.MIXING.getType();
    }

    @Override
    public void startProcessingBasin() {
        if (!this.running || this.runningTicks > 20) {
            super.startProcessingBasin();
            this.running = true;
            this.runningTicks = 0;
        }
    }

    @Override
    public boolean continueWithPreviousRecipe() {
        this.runningTicks = 20;
        return true;
    }

    @Override
    protected void onBasinRemoved() {
        if (this.running) {
            this.runningTicks = 40;
            this.running = false;
        }
    }

    @Override
    protected Object getRecipeCacheKey() {
        return shapelessOrMixingRecipesKey;
    }

    @Override
    protected boolean isRunning() {
        return this.running;
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.of(AllAdvancements.MIXER);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void tickAudio() {
        super.tickAudio();
        boolean slow = Math.abs(this.getSpeed()) < 65.0F;
        if (!slow || AnimationTickHolder.getTicks() % 2 != 0) {
            if (this.runningTicks == 20) {
                AllSoundEvents.MIXING.playAt(this.f_58857_, this.f_58858_, 0.75F, 1.0F, true);
            }
        }
    }
}