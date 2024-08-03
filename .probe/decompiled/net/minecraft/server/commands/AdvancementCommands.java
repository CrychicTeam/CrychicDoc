package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementCommands {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_ADVANCEMENTS = (p_136344_, p_136345_) -> {
        Collection<Advancement> $$2 = ((CommandSourceStack) p_136344_.getSource()).getServer().getAdvancements().getAllAdvancements();
        return SharedSuggestionProvider.suggestResource($$2.stream().map(Advancement::m_138327_), p_136345_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("advancement").requires(p_136318_ -> p_136318_.hasPermission(2))).then(Commands.literal("grant").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).then(Commands.literal("only").then(((RequiredArgumentBuilder) Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136363_ -> perform((CommandSourceStack) p_136363_.getSource(), EntityArgument.getPlayers(p_136363_, "targets"), AdvancementCommands.Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(p_136363_, "advancement"), AdvancementCommands.Mode.ONLY)))).then(Commands.argument("criterion", StringArgumentType.greedyString()).suggests((p_136339_, p_136340_) -> SharedSuggestionProvider.suggest(ResourceLocationArgument.getAdvancement(p_136339_, "advancement").getCriteria().keySet(), p_136340_)).executes(p_136361_ -> performCriterion((CommandSourceStack) p_136361_.getSource(), EntityArgument.getPlayers(p_136361_, "targets"), AdvancementCommands.Action.GRANT, ResourceLocationArgument.getAdvancement(p_136361_, "advancement"), StringArgumentType.getString(p_136361_, "criterion"))))))).then(Commands.literal("from").then(Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136359_ -> perform((CommandSourceStack) p_136359_.getSource(), EntityArgument.getPlayers(p_136359_, "targets"), AdvancementCommands.Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(p_136359_, "advancement"), AdvancementCommands.Mode.FROM)))))).then(Commands.literal("until").then(Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136357_ -> perform((CommandSourceStack) p_136357_.getSource(), EntityArgument.getPlayers(p_136357_, "targets"), AdvancementCommands.Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(p_136357_, "advancement"), AdvancementCommands.Mode.UNTIL)))))).then(Commands.literal("through").then(Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136355_ -> perform((CommandSourceStack) p_136355_.getSource(), EntityArgument.getPlayers(p_136355_, "targets"), AdvancementCommands.Action.GRANT, getAdvancements(ResourceLocationArgument.getAdvancement(p_136355_, "advancement"), AdvancementCommands.Mode.THROUGH)))))).then(Commands.literal("everything").executes(p_136353_ -> perform((CommandSourceStack) p_136353_.getSource(), EntityArgument.getPlayers(p_136353_, "targets"), AdvancementCommands.Action.GRANT, ((CommandSourceStack) p_136353_.getSource()).getServer().getAdvancements().getAllAdvancements())))))).then(Commands.literal("revoke").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).then(Commands.literal("only").then(((RequiredArgumentBuilder) Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136351_ -> perform((CommandSourceStack) p_136351_.getSource(), EntityArgument.getPlayers(p_136351_, "targets"), AdvancementCommands.Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(p_136351_, "advancement"), AdvancementCommands.Mode.ONLY)))).then(Commands.argument("criterion", StringArgumentType.greedyString()).suggests((p_136315_, p_136316_) -> SharedSuggestionProvider.suggest(ResourceLocationArgument.getAdvancement(p_136315_, "advancement").getCriteria().keySet(), p_136316_)).executes(p_136349_ -> performCriterion((CommandSourceStack) p_136349_.getSource(), EntityArgument.getPlayers(p_136349_, "targets"), AdvancementCommands.Action.REVOKE, ResourceLocationArgument.getAdvancement(p_136349_, "advancement"), StringArgumentType.getString(p_136349_, "criterion"))))))).then(Commands.literal("from").then(Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136347_ -> perform((CommandSourceStack) p_136347_.getSource(), EntityArgument.getPlayers(p_136347_, "targets"), AdvancementCommands.Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(p_136347_, "advancement"), AdvancementCommands.Mode.FROM)))))).then(Commands.literal("until").then(Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136342_ -> perform((CommandSourceStack) p_136342_.getSource(), EntityArgument.getPlayers(p_136342_, "targets"), AdvancementCommands.Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(p_136342_, "advancement"), AdvancementCommands.Mode.UNTIL)))))).then(Commands.literal("through").then(Commands.argument("advancement", ResourceLocationArgument.id()).suggests(SUGGEST_ADVANCEMENTS).executes(p_136337_ -> perform((CommandSourceStack) p_136337_.getSource(), EntityArgument.getPlayers(p_136337_, "targets"), AdvancementCommands.Action.REVOKE, getAdvancements(ResourceLocationArgument.getAdvancement(p_136337_, "advancement"), AdvancementCommands.Mode.THROUGH)))))).then(Commands.literal("everything").executes(p_136313_ -> perform((CommandSourceStack) p_136313_.getSource(), EntityArgument.getPlayers(p_136313_, "targets"), AdvancementCommands.Action.REVOKE, ((CommandSourceStack) p_136313_.getSource()).getServer().getAdvancements().getAllAdvancements()))))));
    }

    private static int perform(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, AdvancementCommands.Action advancementCommandsAction2, Collection<Advancement> collectionAdvancement3) {
        int $$4 = 0;
        for (ServerPlayer $$5 : collectionServerPlayer1) {
            $$4 += advancementCommandsAction2.perform($$5, collectionAdvancement3);
        }
        if ($$4 == 0) {
            if (collectionAdvancement3.size() == 1) {
                if (collectionServerPlayer1.size() == 1) {
                    throw new CommandRuntimeException(Component.translatable(advancementCommandsAction2.getKey() + ".one.to.one.failure", ((Advancement) collectionAdvancement3.iterator().next()).getChatComponent(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()));
                } else {
                    throw new CommandRuntimeException(Component.translatable(advancementCommandsAction2.getKey() + ".one.to.many.failure", ((Advancement) collectionAdvancement3.iterator().next()).getChatComponent(), collectionServerPlayer1.size()));
                }
            } else if (collectionServerPlayer1.size() == 1) {
                throw new CommandRuntimeException(Component.translatable(advancementCommandsAction2.getKey() + ".many.to.one.failure", collectionAdvancement3.size(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()));
            } else {
                throw new CommandRuntimeException(Component.translatable(advancementCommandsAction2.getKey() + ".many.to.many.failure", collectionAdvancement3.size(), collectionServerPlayer1.size()));
            }
        } else {
            if (collectionAdvancement3.size() == 1) {
                if (collectionServerPlayer1.size() == 1) {
                    commandSourceStack0.sendSuccess(() -> Component.translatable(advancementCommandsAction2.getKey() + ".one.to.one.success", ((Advancement) collectionAdvancement3.iterator().next()).getChatComponent(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
                } else {
                    commandSourceStack0.sendSuccess(() -> Component.translatable(advancementCommandsAction2.getKey() + ".one.to.many.success", ((Advancement) collectionAdvancement3.iterator().next()).getChatComponent(), collectionServerPlayer1.size()), true);
                }
            } else if (collectionServerPlayer1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable(advancementCommandsAction2.getKey() + ".many.to.one.success", collectionAdvancement3.size(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable(advancementCommandsAction2.getKey() + ".many.to.many.success", collectionAdvancement3.size(), collectionServerPlayer1.size()), true);
            }
            return $$4;
        }
    }

    private static int performCriterion(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, AdvancementCommands.Action advancementCommandsAction2, Advancement advancement3, String string4) {
        int $$5 = 0;
        if (!advancement3.getCriteria().containsKey(string4)) {
            throw new CommandRuntimeException(Component.translatable("commands.advancement.criterionNotFound", advancement3.getChatComponent(), string4));
        } else {
            for (ServerPlayer $$6 : collectionServerPlayer1) {
                if (advancementCommandsAction2.performCriterion($$6, advancement3, string4)) {
                    $$5++;
                }
            }
            if ($$5 == 0) {
                if (collectionServerPlayer1.size() == 1) {
                    throw new CommandRuntimeException(Component.translatable(advancementCommandsAction2.getKey() + ".criterion.to.one.failure", string4, advancement3.getChatComponent(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()));
                } else {
                    throw new CommandRuntimeException(Component.translatable(advancementCommandsAction2.getKey() + ".criterion.to.many.failure", string4, advancement3.getChatComponent(), collectionServerPlayer1.size()));
                }
            } else {
                if (collectionServerPlayer1.size() == 1) {
                    commandSourceStack0.sendSuccess(() -> Component.translatable(advancementCommandsAction2.getKey() + ".criterion.to.one.success", string4, advancement3.getChatComponent(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
                } else {
                    commandSourceStack0.sendSuccess(() -> Component.translatable(advancementCommandsAction2.getKey() + ".criterion.to.many.success", string4, advancement3.getChatComponent(), collectionServerPlayer1.size()), true);
                }
                return $$5;
            }
        }
    }

    private static List<Advancement> getAdvancements(Advancement advancement0, AdvancementCommands.Mode advancementCommandsMode1) {
        List<Advancement> $$2 = Lists.newArrayList();
        if (advancementCommandsMode1.parents) {
            for (Advancement $$3 = advancement0.getParent(); $$3 != null; $$3 = $$3.getParent()) {
                $$2.add($$3);
            }
        }
        $$2.add(advancement0);
        if (advancementCommandsMode1.children) {
            addChildren(advancement0, $$2);
        }
        return $$2;
    }

    private static void addChildren(Advancement advancement0, List<Advancement> listAdvancement1) {
        for (Advancement $$2 : advancement0.getChildren()) {
            listAdvancement1.add($$2);
            addChildren($$2, listAdvancement1);
        }
    }

    static enum Action {

        GRANT("grant") {

            @Override
            protected boolean perform(ServerPlayer p_136395_, Advancement p_136396_) {
                AdvancementProgress $$2 = p_136395_.getAdvancements().getOrStartProgress(p_136396_);
                if ($$2.isDone()) {
                    return false;
                } else {
                    for (String $$3 : $$2.getRemainingCriteria()) {
                        p_136395_.getAdvancements().award(p_136396_, $$3);
                    }
                    return true;
                }
            }

            @Override
            protected boolean performCriterion(ServerPlayer p_136398_, Advancement p_136399_, String p_136400_) {
                return p_136398_.getAdvancements().award(p_136399_, p_136400_);
            }
        }
        , REVOKE("revoke") {

            @Override
            protected boolean perform(ServerPlayer p_136406_, Advancement p_136407_) {
                AdvancementProgress $$2 = p_136406_.getAdvancements().getOrStartProgress(p_136407_);
                if (!$$2.hasProgress()) {
                    return false;
                } else {
                    for (String $$3 : $$2.getCompletedCriteria()) {
                        p_136406_.getAdvancements().revoke(p_136407_, $$3);
                    }
                    return true;
                }
            }

            @Override
            protected boolean performCriterion(ServerPlayer p_136409_, Advancement p_136410_, String p_136411_) {
                return p_136409_.getAdvancements().revoke(p_136410_, p_136411_);
            }
        }
        ;

        private final String key;

        Action(String p_136372_) {
            this.key = "commands.advancement." + p_136372_;
        }

        public int perform(ServerPlayer p_136380_, Iterable<Advancement> p_136381_) {
            int $$2 = 0;
            for (Advancement $$3 : p_136381_) {
                if (this.perform(p_136380_, $$3)) {
                    $$2++;
                }
            }
            return $$2;
        }

        protected abstract boolean perform(ServerPlayer var1, Advancement var2);

        protected abstract boolean performCriterion(ServerPlayer var1, Advancement var2, String var3);

        protected String getKey() {
            return this.key;
        }
    }

    static enum Mode {

        ONLY(false, false), THROUGH(true, true), FROM(false, true), UNTIL(true, false), EVERYTHING(true, true);

        final boolean parents;

        final boolean children;

        private Mode(boolean p_136424_, boolean p_136425_) {
            this.parents = p_136424_;
            this.children = p_136425_;
        }
    }
}