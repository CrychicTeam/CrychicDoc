package dev.ftb.mods.ftbquests.quest.task;

import dev.architectury.fluid.FluidStack;
import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.FluidConfig;
import dev.ftb.mods.ftblibrary.config.NBTConfig;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class FluidTask extends Task {

    public static final ResourceLocation TANK_TEXTURE = new ResourceLocation("ftbquests", "textures/tasks/tank.png");

    private Fluid fluid = Fluids.WATER;

    private CompoundTag fluidNBT = null;

    private long amount = FluidStack.bucketAmount();

    private FluidStack cachedFluidStack = null;

    public FluidTask(long id, Quest quest) {
        super(id, quest);
    }

    public Fluid getFluid() {
        return this.fluid;
    }

    public FluidTask setFluid(Fluid fluid) {
        this.fluid = fluid;
        return this;
    }

    public CompoundTag getFluidNBT() {
        return this.fluidNBT;
    }

    @Override
    public TaskType getType() {
        return TaskTypes.FLUID;
    }

    @Override
    public long getMaxProgress() {
        return this.amount;
    }

    @Override
    public String formatMaxProgress() {
        return getVolumeString(this.amount);
    }

    @Override
    public String formatProgress(TeamData teamData, long progress) {
        return getVolumeString((long) ((int) Math.min(2147483647L, progress)));
    }

    @Override
    public boolean consumesResources() {
        return true;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("fluid", RegistrarManager.getId(this.fluid, Registries.FLUID).toString());
        nbt.putLong("amount", this.amount);
        if (this.fluidNBT != null) {
            nbt.put("nbt", this.fluidNBT);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(nbt.getString("fluid")));
        if (this.fluid == null || this.fluid == Fluids.EMPTY) {
            this.fluid = Fluids.WATER;
        }
        this.amount = Math.max(1L, nbt.getLong("amount"));
        this.fluidNBT = (CompoundTag) nbt.get("nbt");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(RegistrarManager.getId(this.fluid, Registries.FLUID));
        buffer.writeNbt(this.fluidNBT);
        buffer.writeVarLong(this.amount);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.fluid = BuiltInRegistries.FLUID.get(buffer.readResourceLocation());
        if (this.fluid == null || this.fluid == Fluids.EMPTY) {
            this.fluid = Fluids.WATER;
        }
        this.fluidNBT = buffer.readNbt();
        this.amount = buffer.readVarLong();
    }

    @Override
    public void clearCachedData() {
        super.clearCachedData();
        this.cachedFluidStack = null;
    }

    public FluidStack createFluidStack() {
        if (this.cachedFluidStack == null) {
            this.cachedFluidStack = FluidStack.create(this.fluid, FluidStack.bucketAmount(), this.fluidNBT);
        }
        return this.cachedFluidStack;
    }

    public static String getVolumeString(long a) {
        StringBuilder builder = new StringBuilder();
        if (a >= FluidStack.bucketAmount()) {
            if (a % FluidStack.bucketAmount() != 0L) {
                builder.append(StringUtils.formatDouble((double) a / (double) FluidStack.bucketAmount()));
            } else {
                builder.append(a / FluidStack.bucketAmount());
            }
            builder.append(" B");
        } else {
            builder.append(a).append(" mB");
        }
        return builder.toString();
    }

    public MutableComponent getAltTitle() {
        return Component.literal(getVolumeString(this.amount) + " of ").append(this.createFluidStack().getName());
    }

    @Override
    public Icon getAltIcon() {
        FluidStack stack = this.createFluidStack();
        return Icon.getIcon(ClientUtils.getStillTexture(stack)).withTint(Color4I.rgb(ClientUtils.getFluidColor(stack)));
    }

    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("fluid", new FluidConfig(false), FluidStack.create(this.fluid, 1000L), v -> this.fluid = v.getFluid(), FluidStack.create(Fluids.WATER, 1000L));
        config.add("fluid_nbt", new NBTConfig(), this.fluidNBT, v -> this.fluidNBT = v, null);
        config.addLong("amount", this.amount, v -> this.amount = v, FluidStack.bucketAmount(), 1L, Long.MAX_VALUE);
    }

    @Override
    public boolean canInsertItem() {
        return true;
    }

    @Nullable
    @Override
    public Optional<PositionedIngredient> getIngredient(Widget widget) {
        return PositionedIngredient.of(this.createFluidStack(), widget);
    }
}