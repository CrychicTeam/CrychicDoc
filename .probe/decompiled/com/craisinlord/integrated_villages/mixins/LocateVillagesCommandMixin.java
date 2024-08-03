package com.craisinlord.integrated_villages.mixins;

import com.craisinlord.integrated_villages.IntegratedVillages;
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
public class LocateVillagesCommandMixin {

    private static final SimpleCommandExceptionType DISABLE_VILLAGES = new SimpleCommandExceptionType(Component.translatable("Integrated Villages disables vanilla villages, you can change this in the config. Use /locate structure integrated_villages:village_here!"));

    @Inject(method = { "locateStructure" }, at = { @At("HEAD") })
    private static void overrideLocateVanillaVillages(CommandSourceStack cmdSource, ResourceOrTagKeyArgument.Result<Structure> result, CallbackInfoReturnable<Integer> ci) throws CommandSyntaxException {
        Optional<ResourceKey<Structure>> optional = result.unwrap().left();
        if (IntegratedVillages.CONFIG.general.disableVanillaVillages && optional.isPresent() && (((ResourceKey) optional.get()).location().equals(new ResourceLocation("village_desert")) || ((ResourceKey) optional.get()).location().equals(new ResourceLocation("village_plains")) || ((ResourceKey) optional.get()).location().equals(new ResourceLocation("village_savanna")) || ((ResourceKey) optional.get()).location().equals(new ResourceLocation("village_snowy")) || ((ResourceKey) optional.get()).location().equals(new ResourceLocation("village_taiga")) || ((ResourceKey) optional.get()).location().equals(new ResourceLocation("terralith:fortified_desert_village")) || ((ResourceKey) optional.get()).location().equals(new ResourceLocation("terralith:fortified_village")))) {
            throw DISABLE_VILLAGES.create();
        }
    }
}