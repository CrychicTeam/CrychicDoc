package com.craisinlord.idas.mixin.iceandfire;

import com.craisinlord.idas.IDAS;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LocateCommand.class })
public class LocateIaFStructuresCommandMixin {

    private static final SimpleCommandExceptionType DISABLE_MAUSOLEUM = new SimpleCommandExceptionType(Component.translatable("IDAS overrides this Ice and Fire structure! Use /locate structure idas:iceandfire/dread_citadel instead!"));

    private static final SimpleCommandExceptionType DISABLE_GORGON_TEMPLE = new SimpleCommandExceptionType(Component.translatable("IDAS overrides this Ice and Fire structure! Use /locate structure idas:labyrinth instead!"));

    private static final SimpleCommandExceptionType DISABLE_GRAVEYARD = new SimpleCommandExceptionType(Component.translatable("IDAS overrides this Ice and Fire structure! Use /locate structure idas:haunted_manor instead!"));

    @Inject(method = { "locateStructure" }, at = { @At("HEAD") })
    private static void overrideLocateIafStructures(CommandSourceStack cmdSource, ResourceOrTagKeyArgument.Result<Structure> result, CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        Optional<ResourceKey<Structure>> optional = result.unwrap().left();
        if (IDAS.CONFIG.general.disableIaFStructures) {
            if (optional.isPresent() && ((ResourceKey) optional.get()).location().equals(new ResourceLocation("iceandfire:mausoleum"))) {
                throw DISABLE_MAUSOLEUM.create();
            }
            if (optional.isPresent() && ((ResourceKey) optional.get()).location().equals(new ResourceLocation("iceandfire:gorgon_temple"))) {
                throw DISABLE_GORGON_TEMPLE.create();
            }
            if (optional.isPresent() && ((ResourceKey) optional.get()).location().equals(new ResourceLocation("iceandfire:graveyard"))) {
                throw DISABLE_GRAVEYARD.create();
            }
        }
    }
}