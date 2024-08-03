package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class AnimalTameEvent extends LivingEvent {

    private final Animal animal;

    private final Player tamer;

    public AnimalTameEvent(Animal animal, Player tamer) {
        super(animal);
        this.animal = animal;
        this.tamer = tamer;
    }

    public Animal getAnimal() {
        return this.animal;
    }

    public Player getTamer() {
        return this.tamer;
    }
}