package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.MachineList;
import fr.frinn.custommachinery.common.util.TaskDelayer;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkloadMachineComponent extends AbstractMachineComponent implements ISerializableComponent, ITickableComponent {

    private boolean active;

    private int radius;

    private int tempo = -1;

    private static final TicketType<BlockPos> MACHINE_CHUNKLOADER = TicketType.create("custom_machine", Vec3i::compareTo, 0);

    public ChunkloadMachineComponent(IMachineComponentManager manager, boolean active, int radius) {
        super(manager, ComponentIOMode.NONE);
        this.active = active;
        this.radius = radius;
    }

    public ChunkloadMachineComponent(IMachineComponentManager manager) {
        this(manager, false, 1);
    }

    @Override
    public MachineComponentType<ChunkloadMachineComponent> getType() {
        return (MachineComponentType<ChunkloadMachineComponent>) Registration.CHUNKLOAD_MACHINE_COMPONENT.get();
    }

    @Override
    public void onRemoved() {
        if (this.getManager().getLevel() instanceof ServerLevel level && !this.getManager().getTile().isUnloaded()) {
            this.setInactive(level);
        }
    }

    @Override
    public void init() {
        if (this.active && this.getManager().getLevel() instanceof ServerLevel level) {
            ChunkPos pos = new ChunkPos(this.getManager().getTile().m_58899_());
            if (level.m_6522_(pos.x, pos.z, ChunkStatus.EMPTY, false) instanceof LevelChunk) {
                this.setActive(level, this.radius);
            } else {
                TaskDelayer.enqueue(1, () -> this.setActive(level, this.radius));
            }
        }
    }

    @Override
    public void serverTick() {
        if (this.tempo >= 0 && this.tempo-- == 0) {
            this.setInactive((ServerLevel) this.getManager().getLevel());
        }
    }

    @Override
    public void serialize(CompoundTag nbt) {
        nbt.putBoolean("active", this.active);
        nbt.putInt("radius", this.radius);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("active", 1)) {
            this.active = nbt.getBoolean("active");
        }
        if (nbt.contains("radius", 3)) {
            this.radius = nbt.getInt("radius");
        }
    }

    public void setActive(ServerLevel level, int radius) {
        if (this.active) {
            this.setInactive(level);
        }
        this.active = true;
        this.radius = radius;
        BlockPos machinePos = this.getManager().getTile().m_58899_();
        ChunkPos chunk = new ChunkPos(machinePos);
        level.setChunkForced(chunk.x, chunk.z, true);
        level.getChunkSource().addRegionTicket(MACHINE_CHUNKLOADER, chunk, radius + 1, machinePos);
    }

    public void setActiveWithTempo(ServerLevel level, int radius, int tempo) {
        this.tempo = Math.max(this.tempo, tempo);
        if (!this.active || this.radius < radius) {
            this.setActive(level, radius);
        }
    }

    public void setInactive(ServerLevel level) {
        this.active = false;
        BlockPos machinePos = this.getManager().getTile().m_58899_();
        ChunkPos chunk = new ChunkPos(machinePos);
        if (MachineList.findInSameChunk(this.getManager().getTile()).isEmpty()) {
            level.setChunkForced(chunk.x, chunk.z, false);
        }
        level.getChunkSource().removeRegionTicket(MACHINE_CHUNKLOADER, chunk, this.radius + 1, machinePos);
    }

    public boolean isActive() {
        return this.active;
    }

    public int getRadius() {
        return this.radius;
    }

    public static record Template(int radius) implements IMachineComponentTemplate<ChunkloadMachineComponent> {

        public static final NamedCodec<ChunkloadMachineComponent.Template> CODEC = NamedCodec.record(templateInstance -> templateInstance.group(NamedCodec.intRange(1, 32).optionalFieldOf("radius", 1).forGetter(template -> template.radius)).apply(templateInstance, ChunkloadMachineComponent.Template::new), "Chunkload machine component template");

        @Override
        public MachineComponentType<ChunkloadMachineComponent> getType() {
            return (MachineComponentType<ChunkloadMachineComponent>) Registration.CHUNKLOAD_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return "chunkload";
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            return false;
        }

        public ChunkloadMachineComponent build(IMachineComponentManager manager) {
            return new ChunkloadMachineComponent(manager, true, this.radius);
        }
    }
}