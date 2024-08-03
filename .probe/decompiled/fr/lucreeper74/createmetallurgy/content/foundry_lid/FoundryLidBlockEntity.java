package fr.lucreeper74.createmetallurgy.content.foundry_lid;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import fr.lucreeper74.createmetallurgy.content.foundry_basin.FoundryBasinBlockEntity;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FoundryLidBlockEntity extends BasinOperatingBlockEntity {

    public int processingTime;

    public boolean running;

    public LerpedFloat gauge = LerpedFloat.linear();

    private final HashMap<BlazeBurnerBlock.HeatLevel, Integer> temp = new HashMap();

    private static final Object MeltingRecipesKey = new Object();

    public FoundryLidBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.temp.put(BlazeBurnerBlock.HeatLevel.NONE, 0);
        this.temp.put(BlazeBurnerBlock.HeatLevel.SMOULDERING, 500);
        this.temp.put(BlazeBurnerBlock.HeatLevel.FADING, 750);
        this.temp.put(BlazeBurnerBlock.HeatLevel.KINDLED, 1000);
        this.temp.put(BlazeBurnerBlock.HeatLevel.SEETHING, 2000);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("MeltingTime", this.processingTime);
        compound.putBoolean("Running", this.running);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.processingTime = compound.getInt("MeltingTime");
        this.running = compound.getBoolean("Running");
    }

    @Override
    protected void onBasinRemoved() {
        if (this.running) {
            this.processingTime = 0;
            this.currentRecipe = null;
            this.running = false;
        }
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe.getType() == CMRecipeTypes.MELTING.getType();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            this.gauge.tickChaser();
            this.gauge.chase((double) ((Integer) this.temp.getOrDefault(BasinBlockEntity.getHeatLevelOf(this.m_58904_().getBlockState(this.m_58899_().below(2))), 0)).intValue() / 2000.0, 0.2F, LerpedFloat.Chaser.EXP);
        }
        if (!this.f_58857_.isClientSide && (this.currentRecipe == null || this.processingTime == -1)) {
            this.running = false;
            this.processingTime = -1;
            this.basinChecker.scheduleUpdate();
        }
        if (this.running && this.f_58857_ != null) {
            if (!this.f_58857_.isClientSide && this.processingTime <= 0) {
                this.processingTime = -1;
                this.applyBasinRecipe();
                this.sendData();
            }
            if (!this.f_58857_.isClientSide && this.processingTime % 40 == 0) {
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.5F, 0.75F);
            }
            if (this.f_58857_.isClientSide && this.processingTime % 2 == 0) {
                this.spawnParticles();
            }
            if (this.processingTime > 0) {
                this.processingTime--;
            }
        }
    }

    protected void spawnParticles() {
        RandomSource r = this.f_58857_.getRandom();
        Vec3 c = VecHelper.getCenterOf(this.f_58858_);
        Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, 0.125F).multiply(1.0, 0.0, 1.0));
        if (r.nextInt(8) == 0) {
            this.f_58857_.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y + 0.45, v.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected Optional<BasinBlockEntity> getBasin() {
        if (this.f_58857_ == null) {
            return Optional.empty();
        } else {
            BlockEntity basinBE = this.f_58857_.getBlockEntity(this.f_58858_.below());
            if (!(basinBE instanceof FoundryBasinBlockEntity)) {
                return Optional.empty();
            } else {
                return this.m_58900_().m_61143_(FoundryLidBlock.OPEN) ? Optional.empty() : Optional.of((FoundryBasinBlockEntity) basinBE);
            }
        }
    }

    @Override
    protected boolean updateBasin() {
        if (this.running) {
            return true;
        } else if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            if (this.getBasin().filter(BasinBlockEntity::canContinueProcessing).isEmpty()) {
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

    @Override
    protected boolean isRunning() {
        return this.running;
    }

    @Override
    public void startProcessingBasin() {
        if (!this.running || this.processingTime <= 0) {
            super.startProcessingBasin();
            this.running = true;
            this.processingTime = this.currentRecipe instanceof ProcessingRecipe<?> processed ? processed.getProcessingDuration() : 20;
        }
    }

    @Override
    protected Object getRecipeCacheKey() {
        return MeltingRecipesKey;
    }
}