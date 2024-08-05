package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.PlatformHelper;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IComparatorInputComponent;
import fr.frinn.custommachinery.api.component.IDumpComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.network.syncable.LongSyncable;
import fr.frinn.custommachinery.common.network.syncable.SideConfigSyncable;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.transfer.ICommonEnergyHandler;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinery.impl.integration.jei.Energy;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;

public class EnergyMachineComponent extends AbstractMachineComponent implements ITickableComponent, ISerializableComponent, ISyncableStuff, IComparatorInputComponent, ISideConfigComponent, IDumpComponent {

    private long energy = 0L;

    private final long capacity;

    private final long maxInput;

    private final long maxOutput;

    private final ICommonEnergyHandler handler;

    private final SideConfig config;

    private long actualTick;

    public EnergyMachineComponent(IMachineComponentManager manager, long capacity, long maxInput, long maxOutput, SideConfig.Template configTemplate) {
        super(manager, ComponentIOMode.BOTH);
        this.capacity = capacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.handler = PlatformHelper.createEnergyHandler(this);
        this.config = configTemplate.build(this);
        this.config.setCallback(this.handler::configChanged);
    }

    public long getMaxInput() {
        return this.maxInput;
    }

    public long getMaxOutput() {
        return this.maxOutput;
    }

    public double getFillPercent() {
        return (double) this.energy / (double) this.capacity;
    }

    public long getEnergy() {
        return this.energy;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
        this.getManager().markDirty();
    }

    public long receiveEnergy(long maxReceive, boolean simulate) {
        if (this.getMaxInput() <= 0L) {
            return 0L;
        } else {
            long energyReceived = Math.min(this.getCapacity() - this.getEnergy(), Math.min(this.getMaxInput(), maxReceive));
            if (!simulate && energyReceived > 0L) {
                this.setEnergy(this.getEnergy() + energyReceived);
                this.getManager().markDirty();
            }
            return energyReceived;
        }
    }

    public long extractEnergy(long maxExtract, boolean simulate) {
        if (this.getMaxOutput() <= 0L) {
            return 0L;
        } else {
            long energyExtracted = Math.min(this.getEnergy(), Math.min(this.getMaxOutput(), maxExtract));
            if (!simulate && energyExtracted > 0L) {
                this.setEnergy(this.getEnergy() - energyExtracted);
                this.getManager().markDirty();
            }
            return energyExtracted;
        }
    }

    public ICommonEnergyHandler getEnergyHandler() {
        return this.handler;
    }

    @Override
    public SideConfig getConfig() {
        return this.config;
    }

    @Override
    public String getId() {
        return "energy";
    }

    @Override
    public MachineComponentType<EnergyMachineComponent> getType() {
        return (MachineComponentType<EnergyMachineComponent>) Registration.ENERGY_MACHINE_COMPONENT.get();
    }

    @Override
    public void serverTick() {
        this.handler.tick();
    }

    @Override
    public void onRemoved() {
        this.handler.invalidate();
    }

    @Override
    public void serialize(CompoundTag nbt) {
        nbt.putLong("energy", this.energy);
        nbt.put("config", this.config.serialize());
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("energy", 4)) {
            this.energy = Math.min(nbt.getLong("energy"), this.capacity);
        }
        if (nbt.contains("config")) {
            this.config.deserialize(nbt.get("config"));
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(LongSyncable.create(() -> this.energy, energy -> this.energy = energy));
        container.accept(SideConfigSyncable.create(this::getConfig, this.config::set));
    }

    @Override
    public int getComparatorInput() {
        return (int) (15.0 * ((double) this.energy / (double) this.capacity));
    }

    @Override
    public void dump(List<String> ids) {
        this.setEnergy(0L);
    }

    public int receiveRecipeEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(Utils.toInt(this.capacity - this.energy), maxReceive);
        if (!simulate) {
            this.energy += (long) energyReceived;
            this.getManager().markDirty();
        }
        return energyReceived;
    }

    public int extractRecipeEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(Utils.toInt(this.energy), maxExtract);
        if (!simulate) {
            this.energy -= (long) energyExtracted;
            this.getManager().markDirty();
        }
        return energyExtracted;
    }

    public static record Template(long capacity, long maxInput, long maxOutput, SideConfig.Template config) implements IMachineComponentTemplate<EnergyMachineComponent> {

        public static final NamedCodec<EnergyMachineComponent.Template> CODEC = NamedCodec.record(templateInstance -> templateInstance.group(NamedCodec.longRange(1L, Long.MAX_VALUE).fieldOf("capacity").forGetter(template -> template.capacity), NamedCodec.longRange(0L, Long.MAX_VALUE).optionalFieldOf("maxInput").forGetter(template -> template.maxInput == template.capacity ? Optional.empty() : Optional.of(template.maxInput)), NamedCodec.longRange(0L, Long.MAX_VALUE).optionalFieldOf("maxOutput").forGetter(template -> template.maxOutput == template.capacity ? Optional.empty() : Optional.of(template.maxOutput)), SideConfig.Template.CODEC.optionalFieldOf("config", SideConfig.Template.DEFAULT_ALL_INPUT).forGetter(template -> template.config)).apply(templateInstance, (capacity, maxInput, maxOutput, config) -> new EnergyMachineComponent.Template(capacity, (Long) maxInput.orElse(capacity), (Long) maxOutput.orElse(capacity), config)), "Energy machine component");

        @Override
        public MachineComponentType<EnergyMachineComponent> getType() {
            return (MachineComponentType<EnergyMachineComponent>) Registration.ENERGY_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return "";
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            return ingredient instanceof Energy;
        }

        public EnergyMachineComponent build(IMachineComponentManager manager) {
            return new EnergyMachineComponent(manager, this.capacity, this.maxInput, this.maxOutput, this.config);
        }
    }
}