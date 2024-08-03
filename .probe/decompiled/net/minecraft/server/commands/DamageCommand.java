package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class DamageCommand {

    private static final SimpleCommandExceptionType ERROR_INVULNERABLE = new SimpleCommandExceptionType(Component.translatable("commands.damage.invulnerable"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("damage").requires(p_270872_ -> p_270872_.hasPermission(2))).then(Commands.argument("target", EntityArgument.entity()).then(((RequiredArgumentBuilder) Commands.argument("amount", FloatArgumentType.floatArg(0.0F)).executes(p_288351_ -> damage((CommandSourceStack) p_288351_.getSource(), EntityArgument.getEntity(p_288351_, "target"), FloatArgumentType.getFloat(p_288351_, "amount"), ((CommandSourceStack) p_288351_.getSource()).getLevel().m_269111_().generic()))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("damageType", ResourceArgument.resource(commandBuildContext1, Registries.DAMAGE_TYPE)).executes(p_270840_ -> damage((CommandSourceStack) p_270840_.getSource(), EntityArgument.getEntity(p_270840_, "target"), FloatArgumentType.getFloat(p_270840_, "amount"), new DamageSource(ResourceArgument.getResource(p_270840_, "damageType", Registries.DAMAGE_TYPE))))).then(Commands.literal("at").then(Commands.argument("location", Vec3Argument.vec3()).executes(p_270444_ -> damage((CommandSourceStack) p_270444_.getSource(), EntityArgument.getEntity(p_270444_, "target"), FloatArgumentType.getFloat(p_270444_, "amount"), new DamageSource(ResourceArgument.getResource(p_270444_, "damageType", Registries.DAMAGE_TYPE), Vec3Argument.getVec3(p_270444_, "location"))))))).then(Commands.literal("by").then(((RequiredArgumentBuilder) Commands.argument("entity", EntityArgument.entity()).executes(p_270329_ -> damage((CommandSourceStack) p_270329_.getSource(), EntityArgument.getEntity(p_270329_, "target"), FloatArgumentType.getFloat(p_270329_, "amount"), new DamageSource(ResourceArgument.getResource(p_270329_, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(p_270329_, "entity"))))).then(Commands.literal("from").then(Commands.argument("cause", EntityArgument.entity()).executes(p_270848_ -> damage((CommandSourceStack) p_270848_.getSource(), EntityArgument.getEntity(p_270848_, "target"), FloatArgumentType.getFloat(p_270848_, "amount"), new DamageSource(ResourceArgument.getResource(p_270848_, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(p_270848_, "entity"), EntityArgument.getEntity(p_270848_, "cause"))))))))))));
    }

    private static int damage(CommandSourceStack commandSourceStack0, Entity entity1, float float2, DamageSource damageSource3) throws CommandSyntaxException {
        if (entity1.hurt(damageSource3, float2)) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.damage.success", float2, entity1.getDisplayName()), true);
            return 1;
        } else {
            throw ERROR_INVULNERABLE.create();
        }
    }
}