package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.network.syncable.IntegerSyncable;
import fr.frinn.custommachinery.common.util.ExperienceUtils;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ExperienceMachineComponent extends AbstractMachineComponent implements ITickableComponent, ISerializableComponent, ISyncableStuff {

    private final int capacity;

    private final int capacityLevels;

    private final boolean retrieveFromSlots;

    private final List<String> slotIds;

    private int xp = 0;

    private int xpLevels = 0;

    public ExperienceMachineComponent(IMachineComponentManager manager, int capacity, boolean retrieveFromSlots, List<String> slotIds) {
        super(manager, ComponentIOMode.BOTH);
        this.capacity = capacity;
        this.capacityLevels = ExperienceUtils.getLevelFromXp((long) capacity);
        this.retrieveFromSlots = retrieveFromSlots;
        this.slotIds = slotIds;
    }

    public int getXp() {
        return this.xp;
    }

    public int getLevels() {
        return this.xpLevels;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getCapacityLevels() {
        return this.capacityLevels;
    }

    public void setXp(int xp) {
        this.xp = Mth.clamp(xp, 0, this.capacity);
        this.xpLevels = ExperienceUtils.getLevelFromXp((long) xp);
        this.getManager().markDirty();
    }

    public int receiveXp(int maxReceive, boolean simulate) {
        int xpReceived = Math.min(this.getCapacity() - this.getXp(), maxReceive);
        if (!simulate && xpReceived > 0) {
            this.setXp(this.getXp() + xpReceived);
        }
        return xpReceived;
    }

    public int receiveLevel(int levels, boolean simulate) {
        int toReceive = 0;
        for (int i = this.xpLevels; i < this.xpLevels + levels; i++) {
            toReceive += ExperienceUtils.getXpNeededForNextLevel(i);
        }
        int prevLevels = this.xpLevels;
        int received = this.receiveXp(toReceive, simulate);
        return received == toReceive ? levels : prevLevels + ExperienceUtils.getLevelFromXp((long) received);
    }

    public int extractXp(int maxExtract, boolean simulate) {
        int xpExtracted = Math.min(this.getXp(), maxExtract);
        if (!simulate && xpExtracted > 0) {
            this.setXp(this.getXp() - xpExtracted);
        }
        return xpExtracted;
    }

    public int extractLevel(int levels, boolean simulate) {
        int toExtract = 0;
        for (int i = this.xpLevels; i > this.xpLevels - levels; i--) {
            toExtract += ExperienceUtils.getXpNeededForNextLevel(i);
        }
        int prevLevels = this.xpLevels;
        int extracted = this.extractXp(toExtract, simulate);
        return extracted == toExtract ? levels : prevLevels - ExperienceUtils.getLevelFromXp((long) extracted);
    }

    @Override
    public MachineComponentType<?> getType() {
        return (MachineComponentType<?>) Registration.EXPERIENCE_MACHINE_COMPONENT.get();
    }

    @Override
    public void serialize(CompoundTag nbt) {
        nbt.putInt("xp", this.xp);
        nbt.putInt("levels", this.xpLevels);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("xp", 3)) {
            this.xp = nbt.getInt("xp");
        }
        if (nbt.contains("levels", 3)) {
            this.xpLevels = Math.min(nbt.getInt("levels"), this.capacity);
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(IntegerSyncable.create(() -> this.xp, xp -> this.xp = xp));
        container.accept(IntegerSyncable.create(() -> this.xpLevels, xpLevels -> this.xpLevels = xpLevels));
    }

    public boolean canRetrieveFromSlots() {
        return this.retrieveFromSlots;
    }

    public List<String> slotsFromCanRetrieve() {
        return this.slotIds;
    }

    public void addLevelToPlayer(int levelDiff, Player player) {
        int requestedLevel = player.experienceLevel + levelDiff;
        requestedLevel = Math.max(requestedLevel, 0);
        int playerXP = ExperienceUtils.getPlayerTotalXp(player);
        int requestedXP = ExperienceUtils.getXpFromLevel(requestedLevel) - playerXP;
        int awardXP = levelDiff > 0 ? Math.min(this.xp, requestedXP) : -Math.min(Math.abs(requestedXP), this.capacity - this.xp);
        this.awardXP(awardXP, player);
    }

    public void addAllLevelToPlayer(boolean give, Player player) {
        int awardXP;
        if (give) {
            awardXP = this.xp;
        } else {
            awardXP = -Math.min(ExperienceUtils.getPlayerTotalXp(player), this.capacity - this.xp);
        }
        this.awardXP(awardXP, player);
    }

    public void awardXP(int xp, Player player) {
        this.setXp(this.xp - xp);
        player.giveExperiencePoints(xp);
    }

    public static record Template(int capacity, boolean retrieve, List<String> slots) implements IMachineComponentTemplate<ExperienceMachineComponent> {

        public static final NamedCodec<ExperienceMachineComponent.Template> CODEC = NamedCodec.record(templateInstance -> templateInstance.group(NamedCodec.intRange(1, Integer.MAX_VALUE).fieldOf("capacity").forGetter(template -> template.capacity), NamedCodec.BOOL.optionalFieldOf("retrieve", false).forGetter(template -> template.retrieve), NamedCodec.STRING.listOf().optionalFieldOf("slots", Collections.emptyList()).aliases("slot").forGetter(template -> template.slots)).apply(templateInstance, ExperienceMachineComponent.Template::new), "Experience machine component");

        @Override
        public MachineComponentType<ExperienceMachineComponent> getType() {
            return (MachineComponentType<ExperienceMachineComponent>) Registration.EXPERIENCE_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return "";
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            return ingredient instanceof Float;
        }

        public ExperienceMachineComponent build(IMachineComponentManager manager) {
            return new ExperienceMachineComponent(manager, this.capacity, this.retrieve, this.slots);
        }
    }
}