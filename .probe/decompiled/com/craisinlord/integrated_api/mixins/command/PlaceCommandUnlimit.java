package com.craisinlord.integrated_api.mixins.command;

import net.minecraft.server.commands.PlaceCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = { PlaceCommand.class }, priority = 999)
public class PlaceCommandUnlimit {

    @ModifyConstant(method = { "register" }, constant = { @Constant(intValue = 7) }, require = 0)
    private static int changeJigsawDepth(int value) {
        return Integer.MAX_VALUE;
    }
}