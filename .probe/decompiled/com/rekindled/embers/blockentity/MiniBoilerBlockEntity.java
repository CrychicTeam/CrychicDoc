package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.projectile.EffectDamage;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.block.FluidDialBlock;
import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.entity.EmberProjectileEntity;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.recipe.FluidHandlerContext;
import com.rekindled.embers.recipe.IBoilingRecipe;
import com.rekindled.embers.upgrade.MiniBoilerUpgrade;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.joml.Vector3f;

public class MiniBoilerBlockEntity extends PipeBlockEntityBase implements ISoundController, IExtraDialInformation, IExtraCapabilityInformation {

    public static final int SOUND_SLOW = 1;

    public static final int SOUND_MEDIUM = 2;

    public static final int SOUND_FAST = 3;

    public static final int SOUND_PRESSURE_LOW = 4;

    public static final int SOUND_PRESSURE_MEDIUM = 5;

    public static final int SOUND_PRESSURE_HIGH = 6;

    public static final int[] SOUND_IDS = new int[] { 1, 2, 3, 4, 5, 6 };

    Random random = new Random();

    HashSet<Integer> soundsPlaying = new HashSet();

    protected FluidTank fluidTank = new FluidTank(ConfigManager.MINI_BOILER_CAPACITY.get()) {

        @Override
        public void onContentsChanged() {
            MiniBoilerBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IFluidHandler> fluidHolder = LazyOptional.of(() -> this.fluidTank);

    protected FluidTank gasTank = new FluidTank(ConfigManager.MINI_BOILER_CAPACITY.get()) {

        @Override
        public void onContentsChanged() {
            MiniBoilerBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IFluidHandler> gasHolder = LazyOptional.of(() -> this.gasTank);

    protected MiniBoilerUpgrade upgrade;

    int lastBoil;

    int boilTime;

    public IBoilingRecipe cachedRecipe = null;

    public MiniBoilerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MINI_BOILER_ENTITY.get(), pPos, pBlockState);
        this.upgrade = new MiniBoilerUpgrade(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        Direction facing = (Direction) this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == Direction.UP) {
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.gasHolder);
            }
            if (side == Direction.DOWN || side != facing) {
                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, this.fluidHolder);
            }
        }
        return cap == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && side == facing ? this.upgrade.getCapability(cap, side) : super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.fluidTank.readFromNBT(nbt.getCompound("fluidTank"));
        this.gasTank.readFromNBT(nbt.getCompound("gasTank"));
        this.lastBoil = nbt.getInt("lastBoil");
        this.boilTime = nbt.getInt("boilTime");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("fluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
        nbt.put("gasTank", this.gasTank.writeToNBT(new CompoundTag()));
        nbt.putInt("lastBoil", this.lastBoil);
        nbt.putInt("boilTime", this.boilTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("fluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
        nbt.put("gasTank", this.gasTank.writeToNBT(new CompoundTag()));
        nbt.putInt("lastBoil", this.lastBoil);
        nbt.putInt("boilTime", this.boilTime);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void initConnections() {
        Block block = this.f_58857_.getBlockState(this.f_58858_).m_60734_();
        for (Direction direction : Misc.horizontals) {
            BlockState facingState = this.f_58857_.getBlockState(this.f_58858_.relative(direction));
            BlockEntity facingBE = this.f_58857_.getBlockEntity(this.f_58858_.relative(direction));
            if (facingState.m_204336_(EmbersBlockTags.FLUID_PIPE_CONNECTION)) {
                if (facingBE instanceof PipeBlockEntityBase && !((PipeBlockEntityBase) facingBE).getConnection(direction.getOpposite()).transfer) {
                    this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.NONE;
                } else {
                    this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.PIPE;
                }
            } else {
                this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.NONE;
            }
        }
        this.loaded = true;
        this.setChanged();
        this.f_58857_.getChunkAt(this.f_58858_).m_8092_(true);
        this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, block);
    }

    public int getCapacity() {
        return ConfigManager.MINI_BOILER_CAPACITY.get();
    }

    public int getFluidAmount() {
        return this.fluidTank.getFluidAmount();
    }

    public int getGasAmount() {
        return this.gasTank.getFluidAmount();
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }

    public FluidTank getGasTank() {
        return this.gasTank;
    }

    public Fluid getFluid() {
        return this.fluidTank.getFluid() != null ? this.fluidTank.getFluid().getFluid() : null;
    }

    public Fluid getGas() {
        return this.gasTank.getFluid() != null ? this.gasTank.getFluid().getFluid() : null;
    }

    public FluidStack getFluidStack() {
        return this.fluidTank.getFluid();
    }

    public FluidStack getGasStack() {
        return this.gasTank.getFluid();
    }

    public void boil(double heat) {
        FluidStack fluid = this.getFluidStack();
        FluidHandlerContext context = new FluidHandlerContext(this.fluidTank);
        this.cachedRecipe = Misc.getRecipe(this.cachedRecipe, RegistryManager.BOILING.get(), context, this.f_58857_);
        if (this.cachedRecipe != null && fluid.getAmount() > 0 && heat > 0.0) {
            int fluidBoiled = Mth.clamp((int) (ConfigManager.MINI_BOILER_HEAT_MULTIPLIER.get() * heat), 1, fluid.getAmount());
            if (fluidBoiled > 0) {
                FluidStack gas = this.cachedRecipe.process(context, fluidBoiled);
                if (gas != null) {
                    int leftover = gas.getAmount() - this.gasTank.fill(gas, IFluidHandler.FluidAction.EXECUTE);
                    if (ConfigManager.MINI_BOILER_CAN_EXPLODE.get() && leftover > 0 && !this.f_58857_.isClientSide()) {
                        this.explode();
                    }
                }
            }
            this.lastBoil = fluidBoiled;
            this.boilTime = fluidBoiled / 200;
        }
    }

    public void explode() {
        double posX = (double) this.f_58858_.m_123341_() + 0.5;
        double posY = (double) this.f_58858_.m_123342_() + 0.5;
        double posZ = (double) this.f_58858_.m_123343_() + 0.5;
        this.f_58857_.playSound(null, this.f_58858_, EmbersSounds.MINI_BOILER_RUPTURE.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
        Explosion explosion = this.f_58857_.explode(null, posX, posY, posZ, 3.0F, Level.ExplosionInteraction.NONE);
        this.f_58857_.removeBlock(this.f_58858_, false);
        EffectDamage effect = new EffectDamage(4.0F, preset -> this.f_58857_.damageSources().explosion(explosion), 10, 0.0);
        for (int i = 0; i < 12; i++) {
            EmberProjectileEntity proj = RegistryManager.EMBER_PROJECTILE.get().create(this.f_58857_);
            proj.shoot(this.random.nextDouble() - 0.5, this.random.nextDouble() - 0.5, this.random.nextDouble() - 0.5, 0.5F, 0.0F, 10.0);
            proj.m_6034_(posX, posY, posZ);
            proj.setLifetime(20 + this.random.nextInt(40));
            proj.setEffect(effect);
            this.f_58857_.m_7967_(proj);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MiniBoilerBlockEntity blockEntity) {
        blockEntity.handleSound();
        blockEntity.spawnParticles();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MiniBoilerBlockEntity blockEntity) {
        if (!blockEntity.loaded) {
            blockEntity.initConnections();
        }
        if (blockEntity.boilTime > 0) {
            blockEntity.boilTime--;
            blockEntity.setChanged();
        }
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        if ("fluid".equals(dialType) && facing.getAxis() != Direction.Axis.Y) {
            ChatFormatting gasFormat = ChatFormatting.WHITE;
            if ((double) this.getGasAmount() > (double) this.getCapacity() * 0.8) {
                gasFormat = ChatFormatting.RED;
            } else if ((double) this.getGasAmount() > (double) this.getCapacity() * 0.5) {
                gasFormat = ChatFormatting.YELLOW;
            }
            information.add(0, FluidDialBlock.formatFluidStack(this.getGasStack(), this.getCapacity()).withStyle(gasFormat));
        }
    }

    @Override
    public int getComparatorData(Direction facing, int data, String dialType) {
        if ("fluid".equals(dialType) && facing.getAxis() != Direction.Axis.Y) {
            double fill = (double) this.getGasAmount() / (double) this.getCapacity();
            return fill > 0.0 ? (int) (1.0 + fill * 14.0) : 0;
        } else {
            return data;
        }
    }

    public void spawnParticles() {
        double gasRatio = (double) this.getGasAmount() / (double) this.getCapacity();
        int spouts = 0;
        if (gasRatio > 0.8) {
            spouts = 3;
        } else if (gasRatio > 0.5) {
            spouts = 2;
        } else if (gasRatio > 0.25) {
            spouts = 1;
        }
        Vector3f color = IClientFluidTypeExtensions.of(this.getGas().getFluidType()).modifyFogColor(Minecraft.getInstance().gameRenderer.getMainCamera(), 0.0F, (ClientLevel) this.f_58857_, 6, 0.0F, new Vector3f(1.0F, 1.0F, 1.0F));
        Random posRand = new Random(this.f_58858_.asLong());
        for (int i = 0; i < spouts; i++) {
            double angleA = posRand.nextDouble() * Math.PI * 2.0;
            double angleB = posRand.nextDouble() * Math.PI * 2.0;
            float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
            float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
            float zOffset = (float) Math.sin(angleB);
            float speed = 0.13875F;
            float vx = xOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vy = yOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vz = zOffset * speed + posRand.nextFloat() * speed * 0.3F;
            this.f_58857_.addParticle(new VaporParticleOptions(color, new Vec3((double) vx, (double) vy, (double) vz), 1.0F), (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void playSound(int id) {
        float soundX = (float) this.f_58858_.m_123341_() + 0.5F;
        float soundY = (float) this.f_58858_.m_123342_() + 0.5F;
        float soundZ = (float) this.f_58858_.m_123343_() + 0.5F;
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.MINI_BOILER_LOOP_SLOW.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 2:
                EmbersSounds.playMachineSound(this, 2, EmbersSounds.MINI_BOILER_LOOP_MID.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 3:
                EmbersSounds.playMachineSound(this, 3, EmbersSounds.MINI_BOILER_LOOP_FAST.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 4:
                EmbersSounds.playMachineSound(this, 4, EmbersSounds.MINI_BOILER_PRESSURE_LOW.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 5:
                EmbersSounds.playMachineSound(this, 5, EmbersSounds.MINI_BOILER_PRESSURE_MID.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 6:
                EmbersSounds.playMachineSound(this, 6, EmbersSounds.MINI_BOILER_PRESSURE_HIGH.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
        }
        this.soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        this.soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        int speedId = 0;
        int pressureId = 0;
        int gasAmount = this.getGasAmount();
        double gasRatio = (double) gasAmount / (double) this.getCapacity();
        if (gasRatio > 0.8) {
            pressureId = 6;
        } else if (gasRatio > 0.5) {
            pressureId = 5;
        } else if (gasRatio > 0.25) {
            pressureId = 4;
        }
        if (this.boilTime > 0 && this.lastBoil > 0) {
            if (this.lastBoil >= 2400) {
                speedId = 3;
            } else if (this.lastBoil >= 400) {
                speedId = 2;
            } else {
                speedId = 1;
            }
        }
        return speedId == id || pressureId == id;
    }

    @Override
    public float getCurrentPitch(int id, float pitch) {
        if (id == 5) {
            return 1.0F + ((float) this.getGasAmount() / (float) this.getCapacity() - 0.5F) * 1.7F;
        } else {
            return id == 6 ? 1.0F + ((float) this.getGasAmount() / (float) this.getCapacity() - 0.8F) * 1.0F : pitch;
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.fluidHolder.invalidate();
        this.gasHolder.invalidate();
        this.upgrade.invalidate();
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.FLUID_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (facing == Direction.UP) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.steam")));
        } else if (facing == Direction.DOWN || facing != this.f_58857_.getBlockState(this.f_58858_).m_61143_(BlockStateProperties.HORIZONTAL_FACING)) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.fluid", Component.translatable("embers.tooltip.goggles.fluid.water")));
        }
    }
}