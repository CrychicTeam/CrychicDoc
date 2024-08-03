package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.phys.Vec3;

public class RaidCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("raid").requires(p_180498_ -> p_180498_.hasPermission(3))).then(Commands.literal("start").then(Commands.argument("omenlvl", IntegerArgumentType.integer(0)).executes(p_180502_ -> start((CommandSourceStack) p_180502_.getSource(), IntegerArgumentType.getInteger(p_180502_, "omenlvl")))))).then(Commands.literal("stop").executes(p_180500_ -> stop((CommandSourceStack) p_180500_.getSource())))).then(Commands.literal("check").executes(p_180496_ -> check((CommandSourceStack) p_180496_.getSource())))).then(Commands.literal("sound").then(Commands.argument("type", ComponentArgument.textComponent()).executes(p_180492_ -> playSound((CommandSourceStack) p_180492_.getSource(), ComponentArgument.getComponent(p_180492_, "type")))))).then(Commands.literal("spawnleader").executes(p_180488_ -> spawnLeader((CommandSourceStack) p_180488_.getSource())))).then(Commands.literal("setomen").then(Commands.argument("level", IntegerArgumentType.integer(0)).executes(p_180481_ -> setBadOmenLevel((CommandSourceStack) p_180481_.getSource(), IntegerArgumentType.getInteger(p_180481_, "level")))))).then(Commands.literal("glow").executes(p_180471_ -> glow((CommandSourceStack) p_180471_.getSource()))));
    }

    private static int glow(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        Raid $$1 = getRaid(commandSourceStack0.getPlayerOrException());
        if ($$1 != null) {
            for (Raider $$3 : $$1.getAllRaiders()) {
                $$3.m_7292_(new MobEffectInstance(MobEffects.GLOWING, 1000, 1));
            }
        }
        return 1;
    }

    private static int setBadOmenLevel(CommandSourceStack commandSourceStack0, int int1) throws CommandSyntaxException {
        Raid $$2 = getRaid(commandSourceStack0.getPlayerOrException());
        if ($$2 != null) {
            int $$3 = $$2.getMaxBadOmenLevel();
            if (int1 > $$3) {
                commandSourceStack0.sendFailure(Component.literal("Sorry, the max bad omen level you can set is " + $$3));
            } else {
                int $$4 = $$2.getBadOmenLevel();
                $$2.setBadOmenLevel(int1);
                commandSourceStack0.sendSuccess(() -> Component.literal("Changed village's bad omen level from " + $$4 + " to " + int1), false);
            }
        } else {
            commandSourceStack0.sendFailure(Component.literal("No raid found here"));
        }
        return 1;
    }

    private static int spawnLeader(CommandSourceStack commandSourceStack0) {
        commandSourceStack0.sendSuccess(() -> Component.literal("Spawned a raid captain"), false);
        Raider $$1 = EntityType.PILLAGER.create(commandSourceStack0.getLevel());
        if ($$1 == null) {
            commandSourceStack0.sendFailure(Component.literal("Pillager failed to spawn"));
            return 0;
        } else {
            $$1.m_33075_(true);
            $$1.m_8061_(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());
            $$1.m_6034_(commandSourceStack0.getPosition().x, commandSourceStack0.getPosition().y, commandSourceStack0.getPosition().z);
            $$1.finalizeSpawn(commandSourceStack0.getLevel(), commandSourceStack0.getLevel().m_6436_(BlockPos.containing(commandSourceStack0.getPosition())), MobSpawnType.COMMAND, null, null);
            commandSourceStack0.getLevel().m_47205_($$1);
            return 1;
        }
    }

    private static int playSound(CommandSourceStack commandSourceStack0, @Nullable Component component1) {
        if (component1 != null && component1.getString().equals("local")) {
            ServerLevel $$2 = commandSourceStack0.getLevel();
            Vec3 $$3 = commandSourceStack0.getPosition().add(5.0, 0.0, 0.0);
            $$2.playSeededSound(null, $$3.x, $$3.y, $$3.z, SoundEvents.RAID_HORN, SoundSource.NEUTRAL, 2.0F, 1.0F, $$2.f_46441_.nextLong());
        }
        return 1;
    }

    private static int start(CommandSourceStack commandSourceStack0, int int1) throws CommandSyntaxException {
        ServerPlayer $$2 = commandSourceStack0.getPlayerOrException();
        BlockPos $$3 = $$2.m_20183_();
        if ($$2.serverLevel().isRaided($$3)) {
            commandSourceStack0.sendFailure(Component.literal("Raid already started close by"));
            return -1;
        } else {
            Raids $$4 = $$2.serverLevel().getRaids();
            Raid $$5 = $$4.createOrExtendRaid($$2);
            if ($$5 != null) {
                $$5.setBadOmenLevel(int1);
                $$4.m_77762_();
                commandSourceStack0.sendSuccess(() -> Component.literal("Created a raid in your local village"), false);
            } else {
                commandSourceStack0.sendFailure(Component.literal("Failed to create a raid in your local village"));
            }
            return 1;
        }
    }

    private static int stop(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        ServerPlayer $$1 = commandSourceStack0.getPlayerOrException();
        BlockPos $$2 = $$1.m_20183_();
        Raid $$3 = $$1.serverLevel().getRaidAt($$2);
        if ($$3 != null) {
            $$3.stop();
            commandSourceStack0.sendSuccess(() -> Component.literal("Stopped raid"), false);
            return 1;
        } else {
            commandSourceStack0.sendFailure(Component.literal("No raid here"));
            return -1;
        }
    }

    private static int check(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        Raid $$1 = getRaid(commandSourceStack0.getPlayerOrException());
        if ($$1 != null) {
            StringBuilder $$2 = new StringBuilder();
            $$2.append("Found a started raid! ");
            commandSourceStack0.sendSuccess(() -> Component.literal($$2.toString()), false);
            StringBuilder $$3 = new StringBuilder();
            $$3.append("Num groups spawned: ");
            $$3.append($$1.getGroupsSpawned());
            $$3.append(" Bad omen level: ");
            $$3.append($$1.getBadOmenLevel());
            $$3.append(" Num mobs: ");
            $$3.append($$1.getTotalRaidersAlive());
            $$3.append(" Raid health: ");
            $$3.append($$1.getHealthOfLivingRaiders());
            $$3.append(" / ");
            $$3.append($$1.getTotalHealth());
            commandSourceStack0.sendSuccess(() -> Component.literal($$3.toString()), false);
            return 1;
        } else {
            commandSourceStack0.sendFailure(Component.literal("Found no started raids"));
            return 0;
        }
    }

    @Nullable
    private static Raid getRaid(ServerPlayer serverPlayer0) {
        return serverPlayer0.serverLevel().getRaidAt(serverPlayer0.m_20183_());
    }
}