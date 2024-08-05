package com.nameless.indestructible.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class AHPatchPlayAnimationCommand implements Command<CommandSourceStack> {

    private static final AHPatchPlayAnimationCommand COMMAND = new AHPatchPlayAnimationCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("play").then(Commands.argument("animation", StringArgumentType.string()).then(Commands.argument("convert_time", FloatArgumentType.floatArg()).then(Commands.argument("speed", FloatArgumentType.floatArg()).executes(COMMAND))));
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity living = EntityArgument.getEntity(context, "living_entity");
        StaticAnimation animation = EpicFightMod.getInstance().animationManager.findAnimationByPath(StringArgumentType.getString(context, "animation"));
        float convert_time = FloatArgumentType.getFloat(context, "convert_time");
        float speed = FloatArgumentType.getFloat(context, "speed");
        LivingEntityPatch<?> mobPatch = EpicFightCapabilities.getEntityPatch(living, LivingEntityPatch.class);
        if (mobPatch != null && animation != null) {
            if (mobPatch instanceof AdvancedCustomHumanoidMobPatch<?> AHPatch) {
                AHPatch.setAttackSpeed(speed);
            }
            mobPatch.playAnimationSynchronized(animation, convert_time);
        }
        return 1;
    }
}