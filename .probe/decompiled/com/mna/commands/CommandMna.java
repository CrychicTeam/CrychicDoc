package com.mna.commands;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.capabilities.WellspringNode;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.commands.AffinityArgument;
import com.mna.api.commands.FactionArgument;
import com.mna.api.commands.SpellPartArgument;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.capabilities.particles.ParticleAuraProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.gui.containers.providers.NamedAuras;
import com.mna.recipes.progression.ProgressionCondition;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class CommandMna {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("mna").then(progressionCommands())).then(auraCommands())).then(raidCommands())).then(wellspringCommands())).then(roteCommands())).then(masteryCommands()));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> progressionCommands() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("progression").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("reset_all").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> resetProgression((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> resetProgression((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("complete").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> completeProgression((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> completeProgression((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) Commands.literal("level").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("level", IntegerArgumentType.integer(0, 75)).executes(context -> setMagicLevel((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException()), IntegerArgumentType.getInteger(context, "level")))).then(Commands.argument("player", EntityArgument.players()).executes(context -> setMagicLevel((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), IntegerArgumentType.getInteger(context, "level"))))))).then(((LiteralArgumentBuilder) Commands.literal("affinity").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("player", EntityArgument.players()).then(Commands.literal("add").then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("amount", FloatArgumentType.floatArg(1.0F, 100.0F)).executes(context -> addAffinity((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity"), FloatArgumentType.getFloat(context, "amount"))))))).then(Commands.literal("remove").then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("amount", FloatArgumentType.floatArg(1.0F, 100.0F)).executes(context -> removeAffinity((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity"), FloatArgumentType.getFloat(context, "amount"))))))).then(Commands.literal("set").then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("amount", FloatArgumentType.floatArg(1.0F, 100.0F)).executes(context -> setAffinity((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity"), FloatArgumentType.getFloat(context, "amount"))))))).then(Commands.literal("reset").then(Commands.argument("affinity", new AffinityArgument()).executes(context -> resetAffinity((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity")))))).then(Commands.literal("reset_all").executes(context -> resetAllAffinity((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"))))))).then(((LiteralArgumentBuilder) Commands.literal("faction").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("faction", new FactionArgument()).executes(context -> setFaction((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException()), FactionArgument.getFaction(context, "faction"), 0))).then(((RequiredArgumentBuilder) Commands.argument("player", EntityArgument.players()).executes(context -> setFaction((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), FactionArgument.getFaction(context, "faction"), 0))).then(Commands.argument("resIdx", IntegerArgumentType.integer()).executes(context -> setFaction((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), FactionArgument.getFaction(context, "faction"), IntegerArgumentType.getInteger(context, "resIdx")))))))).then(((LiteralArgumentBuilder) Commands.literal("tier").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("tier", IntegerArgumentType.integer(1, 5)).executes(context -> setTier((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException()), IntegerArgumentType.getInteger(context, "tier")))).then(Commands.argument("player", EntityArgument.players()).executes(context -> setTier((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), IntegerArgumentType.getInteger(context, "tier"))))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> auraCommands() {
        return ((LiteralArgumentBuilder) Commands.literal("aura").requires(commandSource -> {
            if (ManaAndArtifice.instance.isDebug) {
                return true;
            } else {
                if (commandSource.getEntity() instanceof Player player && player.getGameProfile() != null) {
                    return ManaAndArtifice.instance.enabled_auras.contains(player.getGameProfile().getId());
                }
                return false;
            }
        })).executes(context -> openMenu((CommandSourceStack) context.getSource()));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> raidCommands() {
        return ((LiteralArgumentBuilder) Commands.literal("raid").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("faction", new FactionArgument()).executes(context -> factionRaid((CommandSourceStack) context.getSource(), FactionArgument.getFaction(context, "faction"), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException()), false))).then(((RequiredArgumentBuilder) Commands.argument("player", EntityArgument.players()).executes(context -> factionRaid((CommandSourceStack) context.getSource(), FactionArgument.getFaction(context, "faction"), EntityArgument.getPlayers(context, "player"), false))).then(Commands.argument("force", BoolArgumentType.bool()).executes(context -> factionRaid((CommandSourceStack) context.getSource(), FactionArgument.getFaction(context, "faction"), EntityArgument.getPlayers(context, "player"), BoolArgumentType.getBool(context, "force"))))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> wellspringCommands() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("wellspring").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("create").requires(commandSource -> commandSource.hasPermission(2))).then(Commands.argument("position", BlockPosArgument.blockPos()).executes(context -> makeWellspring((CommandSourceStack) context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"))))).then(Commands.argument("position", BlockPosArgument.blockPos()).then(Commands.argument("affinity", new AffinityArgument()).executes(context -> makeWellspring((CommandSourceStack) context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), AffinityArgument.getAffinity(context, "affinity")))))).then(Commands.argument("position", BlockPosArgument.blockPos()).then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("strength", FloatArgumentType.floatArg(5.0F, 25.0F)).executes(context -> makeWellspring((CommandSourceStack) context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position"), AffinityArgument.getAffinity(context, "affinity"), FloatArgumentType.getFloat(context, "strength")))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("power").then(((LiteralArgumentBuilder) Commands.literal("set").requires(commandSource -> commandSource.hasPermission(2))).then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("level", IntegerArgumentType.integer(0, 100)).executes(context -> setWellspringPower((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity"), IntegerArgumentType.getInteger(context, "level")))))))).then(((LiteralArgumentBuilder) Commands.literal("add").requires(commandSource -> commandSource.hasPermission(2))).then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("level", IntegerArgumentType.integer(0, 1000)).executes(context -> addWellspringPower((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity"), IntegerArgumentType.getInteger(context, "level")))))))).then(((LiteralArgumentBuilder) Commands.literal("remove").requires(commandSource -> commandSource.hasPermission(2))).then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("affinity", new AffinityArgument()).then(Commands.argument("level", IntegerArgumentType.integer(0, 1000)).executes(context -> removeWellspringPower((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), AffinityArgument.getAffinity(context, "affinity"), IntegerArgumentType.getInteger(context, "level"))))))))).then(Commands.literal("delete").then(Commands.argument("position", BlockPosArgument.blockPos()).executes(context -> deleteWellspring((CommandSourceStack) context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "position")))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> roteCommands() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("rote").then(((LiteralArgumentBuilder) Commands.literal("one").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("identifier", new SpellPartArgument()).executes(context -> addRote((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException()), SpellPartArgument.getSpell(context, "identifier")))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addRote((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), SpellPartArgument.getSpell(context, "identifier"))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("shapes").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addRote_Shapes((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addRote_Shapes((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("components").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addRote_SpellEffects((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addRote_SpellEffects((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("modifiers").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addRote_Modifiers((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addRote_Modifiers((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("all").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addRote_All((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addRote_All((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("reset").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> resetRote((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> resetRote((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> masteryCommands() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("mastery").then(((LiteralArgumentBuilder) Commands.literal("one").requires(commandSource -> commandSource.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("identifier", new SpellPartArgument()).executes(context -> addMastery((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException()), SpellPartArgument.getSpell(context, "identifier")))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addMastery((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), SpellPartArgument.getSpell(context, "identifier"))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("shapes").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addMastery_Shapes((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addMastery_Shapes((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("components").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addMastery_SpellEffects((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addMastery_SpellEffects((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("modifiers").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addMastery_Modifiers((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addMastery_Modifiers((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("all").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> addMastery_All((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> addMastery_All((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("reset").requires(commandSource -> commandSource.hasPermission(2))).executes(context -> resetMastery((CommandSourceStack) context.getSource(), Collections.singleton(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.argument("player", EntityArgument.players()).executes(context -> resetMastery((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))));
    }

    private static int openMenu(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.getCapability(ParticleAuraProvider.AURA).ifPresent(a -> NetworkHooks.openScreen(player, new NamedAuras(), a));
        return 1;
    }

    private static int completeProgression(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                IPlayerProgression progression = (IPlayerProgression) spe.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                if (progression == null) {
                    return 0;
                }
                for (ProgressionCondition condition : ProgressionCondition.get(source.getLevel(), progression.getTier(), new ArrayList())) {
                    progression.addTierProgressionComplete(condition.m_6423_());
                }
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.completeProgression.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.completeProgression.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int resetProgression(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                IPlayerProgression progression = (IPlayerProgression) spe.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                IPlayerRoteSpells rote = (IPlayerRoteSpells) spe.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
                IPlayerMagic magic = (IPlayerMagic) spe.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                if (progression == null || rote == null || magic == null) {
                    return 0;
                }
                progression.setTier(0, spe);
                progression.setAlliedFaction(null, spe);
                progression.setAllyCooldown(0);
                progression.setTierProgression(new ArrayList());
                rote.resetMastery();
                rote.resetRote();
                magic.setMagicLevel(spe, 0);
                magic.setMagicXP(0);
                magic.setCastingResourceType(CastingResourceIDs.MANA);
                for (Affinity aff : Affinity.values()) {
                    magic.setAffinityDepth(aff.getShiftAffinity(), 0.0F);
                }
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.fullReset.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.fullReset.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int makeWellspring(CommandSourceStack source, BlockPos position) {
        return makeWellspring(source, position, Affinity.values()[(int) (Math.random() * (double) Affinity.values().length)], (float) (5.0 + Math.random() * 20.0));
    }

    private static int makeWellspring(CommandSourceStack source, BlockPos position, Affinity aff) {
        return makeWellspring(source, position, aff, (float) (5.0 + Math.random() * 20.0));
    }

    private static int makeWellspring(CommandSourceStack source, BlockPos position, Affinity aff, float strength) {
        MutableBoolean success = new MutableBoolean(true);
        source.getLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
            Optional<WellspringNode> existing = m.getWellspringRegistry().getNodeAt(position);
            if (existing.isPresent()) {
                source.sendFailure(Component.translatable("mna.commands.wellspring.create.nodeoverlap"));
                success.setFalse();
            } else if (!m.getWellspringRegistry().addNode(source.getLevel(), position, () -> new WellspringNode(aff, strength), true)) {
                source.sendFailure(Component.translatable("mna.commands.wellspring.create.failed", position.m_123344_()));
                success.setFalse();
            }
        });
        if (success.booleanValue()) {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspring.create.success", aff.name(), position.toString()), true);
        }
        return 0;
    }

    private static int deleteWellspring(CommandSourceStack source, BlockPos position) {
        MutableBoolean success = new MutableBoolean(true);
        source.getLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> success.setValue(m.getWellspringRegistry().deleteNodeAt(position)));
        if (success.booleanValue()) {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspring.delete.success", position.toString()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspring.delete.failed", position.toString()), true);
        }
        return 0;
    }

    private static int addRote(CommandSourceStack source, Collection<ServerPlayer> players, ISpellComponent component) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> r.addRoteXP(null, component, (float) component.requiredXPForRote()));
            }
            MutableComponent comp = Component.translatable(component.getRegistryName().toString());
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.success", comp.getString(), ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.success", comp.getString(), players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addRote_Shapes(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> ((IForgeRegistry) Registries.Shape.get()).getValues().forEach(s -> r.addRoteXP(null, s, (float) s.requiredXPForRote())));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.shapes.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.shapes.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addRote_SpellEffects(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> ((IForgeRegistry) Registries.SpellEffect.get()).getValues().forEach(s -> r.addRoteXP(null, s, (float) s.requiredXPForRote())));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.components.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.components.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addRote_Modifiers(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> ((IForgeRegistry) Registries.Modifier.get()).getValues().forEach(s -> r.addRoteXP(null, s, (float) s.requiredXPForRote())));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.modifiers.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.modifiers.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addRote_All(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                    ((IForgeRegistry) Registries.Shape.get()).getValues().forEach(s -> r.addRoteXP(null, s, (float) s.requiredXPForRote()));
                    ((IForgeRegistry) Registries.SpellEffect.get()).getValues().forEach(s -> r.addRoteXP(null, s, (float) s.requiredXPForRote()));
                    ((IForgeRegistry) Registries.Modifier.get()).getValues().forEach(s -> r.addRoteXP(null, s, (float) s.requiredXPForRote()));
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.all.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.all.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int resetRote(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> r.resetRote());
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.reset.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.rote.reset.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addMastery(CommandSourceStack source, Collection<ServerPlayer> players, ISpellComponent component) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> r.addMastery(null, component, 0.5F));
            }
            MutableComponent comp = Component.translatable(component.getRegistryName().toString());
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.success", comp.getString(), ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.success", comp.getString(), players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addMastery_Shapes(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> ((IForgeRegistry) Registries.Shape.get()).getValues().forEach(s -> r.addMastery(null, s, 0.5F)));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.shapes.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.shapes.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addMastery_SpellEffects(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> ((IForgeRegistry) Registries.SpellEffect.get()).getValues().forEach(s -> r.addMastery(null, s, 0.5F)));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.components.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.components.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addMastery_Modifiers(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> ((IForgeRegistry) Registries.Modifier.get()).getValues().forEach(s -> r.addMastery(null, s, 0.5F)));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.modifiers.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.modifiers.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addMastery_All(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                    ((IForgeRegistry) Registries.Shape.get()).getValues().forEach(s -> r.addMastery(null, s, 0.5F));
                    ((IForgeRegistry) Registries.SpellEffect.get()).getValues().forEach(s -> r.addMastery(null, s, 0.5F));
                    ((IForgeRegistry) Registries.Modifier.get()).getValues().forEach(s -> r.addMastery(null, s, 0.5F));
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.all.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.all.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int resetMastery(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> r.resetMastery());
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.reset.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.mastery.reset.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int factionRaid(CommandSourceStack source, ResourceLocation factionID, Collection<ServerPlayer> players, boolean force) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                IPlayerProgression progression = (IPlayerProgression) spe.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                if (progression == null) {
                    return 0;
                }
                if (!force && progression.getTier() < 3) {
                    return 0;
                }
                IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(factionID);
                if (faction == null) {
                    return 0;
                }
                if (force) {
                    progression.raidImmediate(faction);
                } else {
                    progression.setRaidChance(faction, 1.0);
                }
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.factionRaid.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.factionRaid.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int setWellspringPower(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff, int level) {
        source.getLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> players.forEach(player -> m.getWellspringRegistry().setWellspringPower(player, aff, (float) level)));
        if (players.size() == 1) {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspringpower.set.success", ((ServerPlayer) players.iterator().next()).m_5446_(), aff.name(), level), true);
        } else {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspringpower.set.success", players.size(), aff.name(), level), true);
        }
        return 1;
    }

    private static int addWellspringPower(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff, int amount) {
        source.getLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> players.forEach(player -> {
            if (player.m_36316_() != null && player.m_36316_().getId() != null) {
                m.getWellspringRegistry().insertPower(player.m_36316_().getId(), player.m_9236_(), aff, (float) amount);
            }
        }));
        if (players.size() == 1) {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspringpower.add.success", amount, aff.name(), ((ServerPlayer) players.iterator().next()).m_5446_()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspringpower.add.success", amount, aff.name(), players.size()), true);
        }
        return 1;
    }

    private static int removeWellspringPower(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff, int amount) {
        source.getLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> players.forEach(player -> {
            if (player.m_36316_() != null && player.m_36316_().getId() != null) {
                m.getWellspringRegistry().consumePower(player.m_36316_().getId(), player.m_9236_(), aff, (float) amount);
            }
        }));
        if (players.size() == 1) {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspringpower.add.success", amount, aff.name(), ((ServerPlayer) players.iterator().next()).m_5446_()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("mna.commands.wellspringpower.add.success", amount, aff.name(), players.size()), true);
        }
        return 1;
    }

    private static int setTier(CommandSourceStack source, Collection<ServerPlayer> players, int tier) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(progression -> progression.setTier(tier, spe));
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.tier.success", ((ServerPlayer) players.iterator().next()).m_5446_(), tier), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.tier.success", players.size(), tier), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int setFaction(CommandSourceStack source, Collection<ServerPlayer> players, ResourceLocation factionID, int castingResourceIndex) {
        if (players != null && players.size() != 0) {
            IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(factionID);
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(progression -> progression.setAlliedFaction(faction, spe));
                if (faction != null) {
                    spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                        int loopedIndex = castingResourceIndex % faction.getCastingResources().length;
                        magic.setCastingResourceType(faction.getCastingResources()[loopedIndex]);
                    });
                }
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.faction.success", ((ServerPlayer) players.iterator().next()).m_5446_(), factionID), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.faction.success", players.size(), factionID), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int setMagicLevel(CommandSourceStack source, Collection<ServerPlayer> players, int magicLevel) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    magic.setMagicLevel(spe, magicLevel);
                    magic.setMagicXP(magic.getXPForLevel(magicLevel));
                    magic.getCastingResource().setAmount(magic.getCastingResource().getMaxAmount());
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.magicLevel.success", ((ServerPlayer) players.iterator().next()).m_5446_(), magicLevel), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.magicLevel.success", players.size(), magicLevel), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int setAffinity(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff, float amount) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    magic.setAffinityDepth(aff.getShiftAffinity(), amount);
                    magic.forceSync();
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.set.success", ((ServerPlayer) players.iterator().next()).m_5446_(), aff, amount), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.set.success", players.size(), aff, amount), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int addAffinity(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff, float amount) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    magic.setAffinityDepth(aff.getShiftAffinity(), magic.getAffinityDepth(aff.getShiftAffinity()) + amount);
                    magic.forceSync();
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.add.success", amount, aff, ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.add.success", amount, aff, players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int removeAffinity(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff, float amount) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    magic.setAffinityDepth(aff.getShiftAffinity(), magic.getAffinityDepth(aff.getShiftAffinity()) - amount);
                    magic.forceSync();
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.remove.success", amount, aff, ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.remove.success", amount, aff, players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int resetAffinity(CommandSourceStack source, Collection<ServerPlayer> players, Affinity aff) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    magic.setAffinityDepth(aff.getShiftAffinity(), 0.0F);
                    magic.forceSync();
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.reset.success", aff, ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.reset.success", aff, players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }

    private static int resetAllAffinity(CommandSourceStack source, Collection<ServerPlayer> players) {
        if (players != null && players.size() != 0) {
            for (ServerPlayer spe : players) {
                spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    for (Affinity aff : Affinity.values()) {
                        magic.setAffinityDepth(aff.getShiftAffinity(), 0.0F);
                    }
                    magic.forceSync();
                });
            }
            if (players.size() == 1) {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.reset_all.success", ((ServerPlayer) players.iterator().next()).m_5446_()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("mna.commands.affinity.reset_all.success", players.size()), true);
            }
            return 1;
        } else {
            return 0;
        }
    }
}