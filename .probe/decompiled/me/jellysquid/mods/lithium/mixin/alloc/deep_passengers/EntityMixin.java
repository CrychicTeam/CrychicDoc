package me.jellysquid.mods.lithium.mixin.alloc.deep_passengers;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @Shadow
    private ImmutableList<Entity> passengers;

    @Overwrite
    public Iterable<Entity> getIndirectPassengers() {
        if (this.passengers.isEmpty()) {
            return Collections.emptyList();
        } else {
            ArrayList<Entity> passengers = new ArrayList();
            this.addPassengersDeep(passengers);
            return passengers;
        }
    }

    @Overwrite
    private Stream<Entity> getIndirectPassengersStream() {
        if (this.passengers.isEmpty()) {
            return Stream.empty();
        } else {
            ArrayList<Entity> passengers = new ArrayList();
            this.addPassengersDeep(passengers);
            return passengers.stream();
        }
    }

    @Overwrite
    public Stream<Entity> getSelfAndPassengers() {
        if (this.passengers.isEmpty()) {
            return Stream.of((Entity) this);
        } else {
            ArrayList<Entity> passengers = new ArrayList();
            passengers.add((Entity) this);
            this.addPassengersDeep(passengers);
            return passengers.stream();
        }
    }

    @Overwrite
    public Stream<Entity> getPassengersAndSelf() {
        if (this.passengers.isEmpty()) {
            return Stream.of((Entity) this);
        } else {
            ArrayList<Entity> passengers = new ArrayList();
            this.addPassengersDeepFirst(passengers);
            passengers.add((Entity) this);
            return passengers.stream();
        }
    }

    private void addPassengersDeep(ArrayList<Entity> passengers) {
        ImmutableList<Entity> list = this.passengers;
        int i = 0;
        for (int listSize = list.size(); i < listSize; i++) {
            Entity passenger = (Entity) list.get(i);
            passengers.add(passenger);
            ((EntityMixin) passenger).addPassengersDeep(passengers);
        }
    }

    private void addPassengersDeepFirst(ArrayList<Entity> passengers) {
        ImmutableList<Entity> list = this.passengers;
        int i = 0;
        for (int listSize = list.size(); i < listSize; i++) {
            Entity passenger = (Entity) list.get(i);
            ((EntityMixin) passenger).addPassengersDeep(passengers);
            passengers.add(passenger);
        }
    }
}