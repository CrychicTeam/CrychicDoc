package noppes.npcs.api.entity;

import net.minecraft.world.entity.TamableAnimal;

public interface IPixelmon<T extends TamableAnimal> extends IAnimal<T> {

    Object getPokemonData();
}