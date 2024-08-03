package fr.lucreeper74.createmetallurgy.content.casting;

import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.VecHelper;
import fr.lucreeper74.createmetallurgy.content.casting.recipe.CastingRecipe;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public abstract class CastingBlockEntity extends SmartBlockEntity {

    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartFluidTankBehaviour inputTank;

    public SmartInventory inv = new SmartInventory(1, this, 1, true).forbidInsertion();

    public SmartInventory moldInv = new SmartInventory(1, this, 1, true);

    protected CastingRecipe currentRecipe;

    public boolean running;

    public int processingTick;

    public CastingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(this.inv, this.moldInv));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("moldInv", this.moldInv.serializeNBT());
        compound.put("inv", this.inv.serializeNBT());
        compound.putInt("castingTime", this.processingTick);
        compound.putBoolean("running", this.running);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.moldInv.deserializeNBT(compound.getCompound("moldInv"));
        this.inv.deserializeNBT(compound.getCompound("inv"));
        this.processingTick = compound.getInt("castingTime");
        this.running = compound.getBoolean("running");
        super.read(compound, clientPacket);
    }

    public void readOnlyItems(CompoundTag compound) {
        this.inv.deserializeNBT(compound.getCompound("inv"));
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemCapability.cast();
        } else {
            return cap == ForgeCapabilities.FLUID_HANDLER ? this.inputTank.getCapability().cast() : super.getCapability(cap, side);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(this.f_58857_, this.f_58858_, this.inv);
        ItemHelper.dropContents(this.f_58857_, this.f_58858_, this.moldInv);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_ != null) {
            if (!this.f_58857_.isClientSide && (this.currentRecipe == null || this.processingTick == -1)) {
                this.running = false;
                this.processingTick = -1;
                this.startProcess();
            }
            if (this.running) {
                if (!this.f_58857_.isClientSide) {
                    if (this.matchCastingRecipe(this.currentRecipe)) {
                        if (this.processingTick <= 0) {
                            this.process();
                            this.f_58857_.playSound(null, this.f_58858_, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.2F, 0.5F);
                        }
                    } else {
                        this.processFailed();
                    }
                }
                if (this.f_58857_.isClientSide) {
                    this.spawnParticles();
                }
                if (this.processingTick >= 0) {
                    this.processingTick--;
                }
            }
        }
    }

    public void startProcess() {
        if (!this.running || this.processingTick <= 0) {
            List<Recipe<?>> recipes = this.getMatchingRecipes();
            if (!recipes.isEmpty()) {
                this.currentRecipe = (CastingRecipe) recipes.get(0);
                if (this.matchCastingRecipe(this.currentRecipe)) {
                    this.processingTick = isInAirCurrent(this.m_58904_(), this.m_58899_(), this) ? this.currentRecipe.getProcessingDuration() / 2 : this.currentRecipe.getProcessingDuration();
                    this.running = true;
                    this.sendData();
                }
            }
        }
    }

    public void process() {
        FluidStack fluidInTank = this.getFluidTank().getFluidInTank(0);
        this.inv.setStackInSlot(0, this.currentRecipe.getResultItem(this.f_58857_.registryAccess()).copy());
        fluidInTank.shrink(this.currentRecipe.getFluidIngredient().getRequiredAmount());
        this.getBehaviour(SmartFluidTankBehaviour.INPUT).forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
        if (this.currentRecipe.isMoldConsumed()) {
            this.moldInv.setStackInSlot(0, ItemStack.EMPTY);
        }
        this.processingTick = -1;
        this.currentRecipe = null;
        this.running = false;
        this.sendData();
    }

    public void processFailed() {
        this.processingTick = -1;
        this.currentRecipe = null;
        this.running = false;
        this.sendData();
    }

    protected void spawnParticles() {
        RandomSource r = this.f_58857_.getRandom();
        Vec3 c = VecHelper.getCenterOf(this.f_58858_);
        Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, 0.25F).multiply(1.0, 0.0, 1.0));
        if (r.nextInt(8) == 0) {
            this.f_58857_.addParticle(ParticleTypes.SMOKE, v.x, v.y + 0.45, v.z, 0.0, 0.0, 0.0);
        }
    }

    public IFluidHandler getFluidTank() {
        return this.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(new FluidTank(1));
    }

    public static boolean isInAirCurrent(Level level, BlockPos pos, BlockEntity be) {
        int range = 3;
        for (Direction direction : Direction.values()) {
            for (int i = 0; i <= range; i++) {
                BlockPos nearbyPos = pos.relative(direction, i);
                BlockState nearbyState = level.getBlockState(nearbyPos);
                if (nearbyState.m_60734_() instanceof EncasedFanBlock) {
                    EncasedFanBlockEntity fanBe = (EncasedFanBlockEntity) level.getBlockEntity(nearbyPos);
                    Direction facing = (Direction) nearbyState.m_61143_(EncasedFanBlock.FACING);
                    BlockEntity facingBe = level.getBlockEntity(nearbyPos.relative(facing, i));
                    float flowDist = fanBe.airCurrent.maxDistance;
                    if (be == facingBe && flowDist != 0.0F && flowDist >= (float) (i - 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected <C extends Container> boolean matchCastingRecipe(Recipe<C> recipe) {
        return recipe != null && this.inv.getStackInSlot(0).isEmpty() ? CastingRecipe.match(this, recipe) : false;
    }

    public List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> list = RecipeFinder.get(this.getRecipeCacheKey(), this.f_58857_, this::matchStaticFilters);
        return (List<Recipe<?>>) list.stream().filter(this::matchCastingRecipe).sorted((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size()).collect(Collectors.toList());
    }

    protected abstract <C extends Container> boolean matchStaticFilters(Recipe<C> var1);

    protected abstract Object getRecipeCacheKey();
}