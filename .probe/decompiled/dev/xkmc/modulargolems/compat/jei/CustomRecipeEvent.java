package dev.xkmc.modulargolems.compat.jei;

import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraftforge.eventbus.api.Event;

public class CustomRecipeEvent extends Event {

    public final IRecipeRegistration registration;

    public CustomRecipeEvent(IRecipeRegistration registration) {
        this.registration = registration;
    }
}