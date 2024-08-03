package fr.frinn.custommachinery.api.component;

import fr.frinn.custommachinery.api.component.handler.IComponentHandler;
import fr.frinn.custommachinery.api.machine.MachineTile;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public interface IMachineComponentManager {

    Map<MachineComponentType<?>, IMachineComponent> getComponents();

    List<ISerializableComponent> getSerializableComponents();

    List<ITickableComponent> getTickableComponents();

    List<ISyncableStuff> getSyncableComponents();

    List<IComparatorInputComponent> getComparatorInputComponents();

    List<IDumpComponent> getDumpComponents();

    <T extends IMachineComponent> Optional<T> getComponent(MachineComponentType<T> var1);

    <T extends IMachineComponent> Optional<IComponentHandler<T>> getComponentHandler(MachineComponentType<T> var1);

    boolean hasComponent(MachineComponentType<?> var1);

    MachineTile getTile();

    Level getLevel();

    MinecraftServer getServer();

    void markDirty();
}