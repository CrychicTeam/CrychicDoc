package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed"));

    private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed.uuid"));

    private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.summon.invalidPosition"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("summon").requires(p_138819_ -> p_138819_.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("entity", ResourceArgument.resource(commandBuildContext1, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(p_248175_ -> spawnEntity((CommandSourceStack) p_248175_.getSource(), ResourceArgument.getSummonableEntityType(p_248175_, "entity"), ((CommandSourceStack) p_248175_.getSource()).getPosition(), new CompoundTag(), true))).then(((RequiredArgumentBuilder) Commands.argument("pos", Vec3Argument.vec3()).executes(p_248173_ -> spawnEntity((CommandSourceStack) p_248173_.getSource(), ResourceArgument.getSummonableEntityType(p_248173_, "entity"), Vec3Argument.getVec3(p_248173_, "pos"), new CompoundTag(), true))).then(Commands.argument("nbt", CompoundTagArgument.compoundTag()).executes(p_248174_ -> spawnEntity((CommandSourceStack) p_248174_.getSource(), ResourceArgument.getSummonableEntityType(p_248174_, "entity"), Vec3Argument.getVec3(p_248174_, "pos"), CompoundTagArgument.getCompoundTag(p_248174_, "nbt"), false))))));
    }

    public static Entity createEntity(CommandSourceStack commandSourceStack0, Holder.Reference<EntityType<?>> holderReferenceEntityType1, Vec3 vec2, CompoundTag compoundTag3, boolean boolean4) throws CommandSyntaxException {
        BlockPos $$5 = BlockPos.containing(vec2);
        if (!Level.isInSpawnableBounds($$5)) {
            throw INVALID_POSITION.create();
        } else {
            CompoundTag $$6 = compoundTag3.copy();
            $$6.putString("id", holderReferenceEntityType1.key().location().toString());
            ServerLevel $$7 = commandSourceStack0.getLevel();
            Entity $$8 = EntityType.loadEntityRecursive($$6, $$7, p_138828_ -> {
                p_138828_.moveTo(vec2.x, vec2.y, vec2.z, p_138828_.getYRot(), p_138828_.getXRot());
                return p_138828_;
            });
            if ($$8 == null) {
                throw ERROR_FAILED.create();
            } else {
                if (boolean4 && $$8 instanceof Mob) {
                    ((Mob) $$8).finalizeSpawn(commandSourceStack0.getLevel(), commandSourceStack0.getLevel().m_6436_($$8.blockPosition()), MobSpawnType.COMMAND, null, null);
                }
                if (!$$7.tryAddFreshEntityWithPassengers($$8)) {
                    throw ERROR_DUPLICATE_UUID.create();
                } else {
                    return $$8;
                }
            }
        }
    }

    private static int spawnEntity(CommandSourceStack commandSourceStack0, Holder.Reference<EntityType<?>> holderReferenceEntityType1, Vec3 vec2, CompoundTag compoundTag3, boolean boolean4) throws CommandSyntaxException {
        Entity $$5 = createEntity(commandSourceStack0, holderReferenceEntityType1, vec2, compoundTag3, boolean4);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.summon.success", $$5.getDisplayName()), true);
        return 1;
    }
}