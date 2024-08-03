package fr.lucreeper74.createmetallurgy.content.processing.casting.castingBasin;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.VecHelper;
import fr.lucreeper74.createmetallurgy.content.processing.casting.CastingUtils;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import fr.lucreeper74.createmetallurgy.utils.CMLang;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
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
import net.minecraftforge.items.ItemStackHandler;

public class CastingBasinBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected LazyOptional<IItemHandlerModifiable> itemCapability;

    public SmartFluidTankBehaviour inputTank;

    public SmartInventory inv = new SmartInventory(1, this, 1, true);

    public CastingBasinRecipe currentRecipe;

    public boolean running;

    public int processingTick;

    private static final Object CastingInBasinRecipesKey = new Object();

    public CastingBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemCapability = LazyOptional.of(() -> this.inv);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        this.inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 810, true);
        behaviours.add(this.inputTank);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("inv", this.inv.serializeNBT());
        compound.putInt("CastingTime", this.processingTick);
        compound.putBoolean("Running", this.running);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.inv.deserializeNBT(compound.getCompound("inv"));
        this.processingTick = compound.getInt("CastingTime");
        this.running = compound.getBoolean("Running");
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
                    if (this.canProcess()) {
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

    public boolean canProcess() {
        FluidStack fluidInTank = this.getFluidTank().getFluidInTank(0);
        return this.currentRecipe.getFluidIngredients().get(0).test(fluidInTank) && fluidInTank.getAmount() >= this.currentRecipe.getFluidIngredients().get(0).getRequiredAmount() && this.inv.m_7983_();
    }

    public void startProcess() {
        if (!this.running || this.processingTick <= 0) {
            List<Recipe<?>> recipes = this.getMatchingRecipes();
            if (!recipes.isEmpty()) {
                this.currentRecipe = (CastingBasinRecipe) recipes.get(0);
                if (this.canProcess()) {
                    this.processingTick = CastingUtils.isInAirCurrent(this.m_58904_(), this.m_58899_(), this) ? this.currentRecipe.getProcessingDuration() / 2 : this.currentRecipe.getProcessingDuration();
                    this.running = true;
                    this.sendData();
                }
            }
        }
    }

    public void process() {
        FluidStack fluidInTank = this.getFluidTank().getFluidInTank(0);
        this.inv.insertItem(0, this.currentRecipe.m_8043_(this.f_58857_.registryAccess()).copy(), false);
        fluidInTank.shrink(this.currentRecipe.getFluidIngredients().get(0).getRequiredAmount());
        this.getBehaviour(SmartFluidTankBehaviour.INPUT).forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
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
            this.f_58857_.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y + 0.45, v.z, 0.0, 0.0, 0.0);
        }
    }

    protected List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> list = RecipeFinder.get(this.getRecipeCacheKey(), this.f_58857_, this::matchStaticFilters);
        return (List<Recipe<?>>) list.stream().filter(recipe -> recipe instanceof CastingBasinRecipe castingRecipe ? castingRecipe.getFluidIngredients().get(0).test(this.getFluidTank().getFluidInTank(0)) : false).sorted((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size()).collect(Collectors.toList());
    }

    public IFluidHandler getFluidTank() {
        return this.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(new FluidTank(1));
    }

    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r.getType() == CMRecipeTypes.CASTING_IN_BASIN.getType();
    }

    protected Object getRecipeCacheKey() {
        return CastingInBasinRecipesKey;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CMLang.translate("gui.goggles.castingbasin_contents").forGoggles(tooltip);
        IItemHandlerModifiable items = this.itemCapability.orElse(new ItemStackHandler());
        IFluidHandler fluids = this.getFluidTank();
        boolean isEmpty = true;
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                CMLang.text("").add(Components.translatable(stackInSlot.getDescriptionId()).withStyle(ChatFormatting.GRAY)).add(CMLang.text(" x" + stackInSlot.getCount()).style(ChatFormatting.GREEN)).forGoggles(tooltip, 1);
                isEmpty = false;
            }
        }
        LangBuilder mb = CMLang.translate("generic.unit.millibuckets");
        for (int ix = 0; ix < fluids.getTanks(); ix++) {
            FluidStack fluidStack = fluids.getFluidInTank(ix);
            if (!fluidStack.isEmpty()) {
                CMLang.text("").add(CMLang.fluidName(fluidStack).add(CMLang.text(" ")).style(ChatFormatting.GRAY).add(CMLang.number((double) fluidStack.getAmount()).add(mb).style(ChatFormatting.BLUE))).forGoggles(tooltip, 1);
                isEmpty = false;
            }
        }
        if (isEmpty) {
            tooltip.remove(0);
        }
        return true;
    }
}