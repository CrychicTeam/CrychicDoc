package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;

public class BossBarCommands {

    private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(p_136636_ -> Component.translatable("commands.bossbar.create.failed", p_136636_));

    private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(p_136623_ -> Component.translatable("commands.bossbar.unknown", p_136623_));

    private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.players.unchanged"));

    private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.name.unchanged"));

    private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.color.unchanged"));

    private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.style.unchanged"));

    private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.value.unchanged"));

    private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.max.unchanged"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.visibility.unchanged.hidden"));

    private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType(Component.translatable("commands.bossbar.set.visibility.unchanged.visible"));

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR = (p_136587_, p_136588_) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack) p_136587_.getSource()).getServer().getCustomBossEvents().getIds(), p_136588_);

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("bossbar").requires(p_136627_ -> p_136627_.hasPermission(2))).then(Commands.literal("add").then(Commands.argument("id", ResourceLocationArgument.id()).then(Commands.argument("name", ComponentArgument.textComponent()).executes(p_136693_ -> createBar((CommandSourceStack) p_136693_.getSource(), ResourceLocationArgument.getId(p_136693_, "id"), ComponentArgument.getComponent(p_136693_, "name"))))))).then(Commands.literal("remove").then(Commands.argument("id", ResourceLocationArgument.id()).suggests(SUGGEST_BOSS_BAR).executes(p_136691_ -> removeBar((CommandSourceStack) p_136691_.getSource(), getBossBar(p_136691_)))))).then(Commands.literal("list").executes(p_136689_ -> listBars((CommandSourceStack) p_136689_.getSource())))).then(Commands.literal("set").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("id", ResourceLocationArgument.id()).suggests(SUGGEST_BOSS_BAR).then(Commands.literal("name").then(Commands.argument("name", ComponentArgument.textComponent()).executes(p_136687_ -> setName((CommandSourceStack) p_136687_.getSource(), getBossBar(p_136687_), ComponentArgument.getComponent(p_136687_, "name")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("color").then(Commands.literal("pink").executes(p_136685_ -> setColor((CommandSourceStack) p_136685_.getSource(), getBossBar(p_136685_), BossEvent.BossBarColor.PINK)))).then(Commands.literal("blue").executes(p_136683_ -> setColor((CommandSourceStack) p_136683_.getSource(), getBossBar(p_136683_), BossEvent.BossBarColor.BLUE)))).then(Commands.literal("red").executes(p_136681_ -> setColor((CommandSourceStack) p_136681_.getSource(), getBossBar(p_136681_), BossEvent.BossBarColor.RED)))).then(Commands.literal("green").executes(p_136679_ -> setColor((CommandSourceStack) p_136679_.getSource(), getBossBar(p_136679_), BossEvent.BossBarColor.GREEN)))).then(Commands.literal("yellow").executes(p_136677_ -> setColor((CommandSourceStack) p_136677_.getSource(), getBossBar(p_136677_), BossEvent.BossBarColor.YELLOW)))).then(Commands.literal("purple").executes(p_136675_ -> setColor((CommandSourceStack) p_136675_.getSource(), getBossBar(p_136675_), BossEvent.BossBarColor.PURPLE)))).then(Commands.literal("white").executes(p_136673_ -> setColor((CommandSourceStack) p_136673_.getSource(), getBossBar(p_136673_), BossEvent.BossBarColor.WHITE))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("style").then(Commands.literal("progress").executes(p_136671_ -> setStyle((CommandSourceStack) p_136671_.getSource(), getBossBar(p_136671_), BossEvent.BossBarOverlay.PROGRESS)))).then(Commands.literal("notched_6").executes(p_136669_ -> setStyle((CommandSourceStack) p_136669_.getSource(), getBossBar(p_136669_), BossEvent.BossBarOverlay.NOTCHED_6)))).then(Commands.literal("notched_10").executes(p_136667_ -> setStyle((CommandSourceStack) p_136667_.getSource(), getBossBar(p_136667_), BossEvent.BossBarOverlay.NOTCHED_10)))).then(Commands.literal("notched_12").executes(p_136665_ -> setStyle((CommandSourceStack) p_136665_.getSource(), getBossBar(p_136665_), BossEvent.BossBarOverlay.NOTCHED_12)))).then(Commands.literal("notched_20").executes(p_136663_ -> setStyle((CommandSourceStack) p_136663_.getSource(), getBossBar(p_136663_), BossEvent.BossBarOverlay.NOTCHED_20))))).then(Commands.literal("value").then(Commands.argument("value", IntegerArgumentType.integer(0)).executes(p_136661_ -> setValue((CommandSourceStack) p_136661_.getSource(), getBossBar(p_136661_), IntegerArgumentType.getInteger(p_136661_, "value")))))).then(Commands.literal("max").then(Commands.argument("max", IntegerArgumentType.integer(1)).executes(p_136659_ -> setMax((CommandSourceStack) p_136659_.getSource(), getBossBar(p_136659_), IntegerArgumentType.getInteger(p_136659_, "max")))))).then(Commands.literal("visible").then(Commands.argument("visible", BoolArgumentType.bool()).executes(p_136657_ -> setVisible((CommandSourceStack) p_136657_.getSource(), getBossBar(p_136657_), BoolArgumentType.getBool(p_136657_, "visible")))))).then(((LiteralArgumentBuilder) Commands.literal("players").executes(p_136655_ -> setPlayers((CommandSourceStack) p_136655_.getSource(), getBossBar(p_136655_), Collections.emptyList()))).then(Commands.argument("targets", EntityArgument.players()).executes(p_136653_ -> setPlayers((CommandSourceStack) p_136653_.getSource(), getBossBar(p_136653_), EntityArgument.getOptionalPlayers(p_136653_, "targets")))))))).then(Commands.literal("get").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("id", ResourceLocationArgument.id()).suggests(SUGGEST_BOSS_BAR).then(Commands.literal("value").executes(p_136648_ -> getValue((CommandSourceStack) p_136648_.getSource(), getBossBar(p_136648_))))).then(Commands.literal("max").executes(p_136643_ -> getMax((CommandSourceStack) p_136643_.getSource(), getBossBar(p_136643_))))).then(Commands.literal("visible").executes(p_136638_ -> getVisible((CommandSourceStack) p_136638_.getSource(), getBossBar(p_136638_))))).then(Commands.literal("players").executes(p_136625_ -> getPlayers((CommandSourceStack) p_136625_.getSource(), getBossBar(p_136625_)))))));
    }

    private static int getValue(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1) {
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.get.value", customBossEvent1.getDisplayName(), customBossEvent1.getValue()), true);
        return customBossEvent1.getValue();
    }

    private static int getMax(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1) {
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.get.max", customBossEvent1.getDisplayName(), customBossEvent1.getMax()), true);
        return customBossEvent1.getMax();
    }

    private static int getVisible(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1) {
        if (customBossEvent1.m_8323_()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.visible", customBossEvent1.getDisplayName()), true);
            return 1;
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.get.visible.hidden", customBossEvent1.getDisplayName()), true);
            return 0;
        }
    }

    private static int getPlayers(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1) {
        if (customBossEvent1.m_8324_().isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.none", customBossEvent1.getDisplayName()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.get.players.some", customBossEvent1.getDisplayName(), customBossEvent1.m_8324_().size(), ComponentUtils.formatList(customBossEvent1.m_8324_(), Player::m_5446_)), true);
        }
        return customBossEvent1.m_8324_().size();
    }

    private static int setVisible(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, boolean boolean2) throws CommandSyntaxException {
        if (customBossEvent1.m_8323_() == boolean2) {
            if (boolean2) {
                throw ERROR_ALREADY_VISIBLE.create();
            } else {
                throw ERROR_ALREADY_HIDDEN.create();
            }
        } else {
            customBossEvent1.m_8321_(boolean2);
            if (boolean2) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.visible", customBossEvent1.getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.visible.success.hidden", customBossEvent1.getDisplayName()), true);
            }
            return 0;
        }
    }

    private static int setValue(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, int int2) throws CommandSyntaxException {
        if (customBossEvent1.getValue() == int2) {
            throw ERROR_NO_VALUE_CHANGE.create();
        } else {
            customBossEvent1.setValue(int2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.value.success", customBossEvent1.getDisplayName(), int2), true);
            return int2;
        }
    }

    private static int setMax(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, int int2) throws CommandSyntaxException {
        if (customBossEvent1.getMax() == int2) {
            throw ERROR_NO_MAX_CHANGE.create();
        } else {
            customBossEvent1.setMax(int2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.max.success", customBossEvent1.getDisplayName(), int2), true);
            return int2;
        }
    }

    private static int setColor(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, BossEvent.BossBarColor bossEventBossBarColor2) throws CommandSyntaxException {
        if (customBossEvent1.m_18862_().equals(bossEventBossBarColor2)) {
            throw ERROR_NO_COLOR_CHANGE.create();
        } else {
            customBossEvent1.m_6451_(bossEventBossBarColor2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.color.success", customBossEvent1.getDisplayName()), true);
            return 0;
        }
    }

    private static int setStyle(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, BossEvent.BossBarOverlay bossEventBossBarOverlay2) throws CommandSyntaxException {
        if (customBossEvent1.m_18863_().equals(bossEventBossBarOverlay2)) {
            throw ERROR_NO_STYLE_CHANGE.create();
        } else {
            customBossEvent1.m_5648_(bossEventBossBarOverlay2);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.style.success", customBossEvent1.getDisplayName()), true);
            return 0;
        }
    }

    private static int setName(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, Component component2) throws CommandSyntaxException {
        Component $$3 = ComponentUtils.updateForEntity(commandSourceStack0, component2, null, 0);
        if (customBossEvent1.m_18861_().equals($$3)) {
            throw ERROR_NO_NAME_CHANGE.create();
        } else {
            customBossEvent1.m_6456_($$3);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.name.success", customBossEvent1.getDisplayName()), true);
            return 0;
        }
    }

    private static int setPlayers(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1, Collection<ServerPlayer> collectionServerPlayer2) throws CommandSyntaxException {
        boolean $$3 = customBossEvent1.setPlayers(collectionServerPlayer2);
        if (!$$3) {
            throw ERROR_NO_PLAYER_CHANGE.create();
        } else {
            if (customBossEvent1.m_8324_().isEmpty()) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.none", customBossEvent1.getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.set.players.success.some", customBossEvent1.getDisplayName(), collectionServerPlayer2.size(), ComponentUtils.formatList(collectionServerPlayer2, Player::m_5446_)), true);
            }
            return customBossEvent1.m_8324_().size();
        }
    }

    private static int listBars(CommandSourceStack commandSourceStack0) {
        Collection<CustomBossEvent> $$1 = commandSourceStack0.getServer().getCustomBossEvents().getEvents();
        if ($$1.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.none"), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.list.bars.some", $$1.size(), ComponentUtils.formatList($$1, CustomBossEvent::m_136288_)), false);
        }
        return $$1.size();
    }

    private static int createBar(CommandSourceStack commandSourceStack0, ResourceLocation resourceLocation1, Component component2) throws CommandSyntaxException {
        CustomBossEvents $$3 = commandSourceStack0.getServer().getCustomBossEvents();
        if ($$3.get(resourceLocation1) != null) {
            throw ERROR_ALREADY_EXISTS.create(resourceLocation1.toString());
        } else {
            CustomBossEvent $$4 = $$3.create(resourceLocation1, ComponentUtils.updateForEntity(commandSourceStack0, component2, null, 0));
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.create.success", $$4.getDisplayName()), true);
            return $$3.getEvents().size();
        }
    }

    private static int removeBar(CommandSourceStack commandSourceStack0, CustomBossEvent customBossEvent1) {
        CustomBossEvents $$2 = commandSourceStack0.getServer().getCustomBossEvents();
        customBossEvent1.removeAllPlayers();
        $$2.remove(customBossEvent1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.bossbar.remove.success", customBossEvent1.getDisplayName()), true);
        return $$2.getEvents().size();
    }

    public static CustomBossEvent getBossBar(CommandContext<CommandSourceStack> commandContextCommandSourceStack0) throws CommandSyntaxException {
        ResourceLocation $$1 = ResourceLocationArgument.getId(commandContextCommandSourceStack0, "id");
        CustomBossEvent $$2 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getCustomBossEvents().get($$1);
        if ($$2 == null) {
            throw ERROR_DOESNT_EXIST.create($$1.toString());
        } else {
            return $$2;
        }
    }
}