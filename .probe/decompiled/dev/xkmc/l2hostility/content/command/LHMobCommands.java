package dev.xkmc.l2hostility.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiPredicate;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LHMobCommands extends HostilityCommands {

    private static final DynamicCommandExceptionType ERR_INVALID_NAME = new DynamicCommandExceptionType(xva$0 -> rec$.get(xva$0));

    protected static LiteralArgumentBuilder<CommandSourceStack> build() {
        return (LiteralArgumentBuilder<CommandSourceStack>) literal("mobs").then(((RequiredArgumentBuilder) argument("targets", EntityArgument.entities()).then(level())).then(trait()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> trait() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) literal("trait").then(((LiteralArgumentBuilder) literal("clear").requires(e -> e.hasPermission(2))).executes(mobRun(LHMobCommands::commandClearTrait)))).then(((LiteralArgumentBuilder) literal("remove").requires(e -> e.hasPermission(2))).then(argument("trait", ResourceKeyArgument.key(LHTraits.TRAITS.key())).executes(mobTrait(LHMobCommands::commandRemoveTrait))))).then(((LiteralArgumentBuilder) literal("set").requires(e -> e.hasPermission(2))).then(argument("trait", ResourceKeyArgument.key(LHTraits.TRAITS.key())).then(argument("rank", IntegerArgumentType.integer(0)).executes(mobTraitRank(LHMobCommands::commandSetTrait)))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> level() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) literal("level").then(((LiteralArgumentBuilder) literal("rerollTrait").requires(e -> e.hasPermission(2))).executes(mobRun((mob, cap) -> cap.reinit(mob, cap.getLevel(), false))))).then(((LiteralArgumentBuilder) literal("rerollTraitNoSuppression").requires(e -> e.hasPermission(2))).executes(mobRun((mob, cap) -> cap.reinit(mob, cap.getLevel(), true))))).then(((LiteralArgumentBuilder) literal("setAndRerollTrait").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0)).executes(mobLevel((mob, cap, level) -> cap.reinit(mob, level, false)))))).then(((LiteralArgumentBuilder) literal("addAndRerollTrait").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0)).executes(mobLevel((mob, cap, level) -> cap.reinit(mob, cap.getLevel() + level, false)))))).then(((LiteralArgumentBuilder) literal("set").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0)).executes(mobLevel((mob, cap, level) -> commandSetLevel(cap, mob, level)))))).then(((LiteralArgumentBuilder) literal("add").requires(e -> e.hasPermission(2))).then(argument("level", IntegerArgumentType.integer(0)).executes(mobLevel((mob, cap, level) -> commandSetLevel(cap, mob, cap.getLevel() + level)))));
    }

    private static boolean commandSetLevel(MobTraitCap cap, LivingEntity mob, int level) {
        cap.setLevel(mob, level);
        cap.syncToClient(mob);
        return true;
    }

    private static boolean commandClearTrait(LivingEntity le, MobTraitCap cap) {
        if (cap.traits.isEmpty()) {
            return false;
        } else {
            for (MobTrait e : cap.traits.keySet()) {
                cap.setTrait(e, 0);
            }
            return true;
        }
    }

    private static boolean commandRemoveTrait(LivingEntity le, MobTraitCap cap, MobTrait trait) {
        if (!cap.hasTrait(trait)) {
            return false;
        } else {
            cap.removeTrait(trait);
            return true;
        }
    }

    private static boolean commandSetTrait(LivingEntity le, MobTraitCap cap, MobTrait trait, int rank) {
        if (!trait.allow(le)) {
            return false;
        } else if (trait.getConfig().max_rank < rank) {
            return false;
        } else {
            cap.setTrait(trait, rank);
            return true;
        }
    }

    private static Command<CommandSourceStack> mobRun(LHMobCommands.MobCommand cmd) {
        return ctx -> {
            Collection<? extends Entity> list = EntityArgument.getEntities(ctx, "targets");
            int count = iterate(list, cmd::run);
            printCompletion((CommandSourceStack) ctx.getSource(), count);
            return 0;
        };
    }

    private static Command<CommandSourceStack> mobLevel(LHMobCommands.MobLevelCommand cmd) {
        return ctx -> {
            int level = (Integer) ctx.getArgument("level", Integer.class);
            Collection<? extends Entity> list = EntityArgument.getEntities(ctx, "targets");
            int count = iterate(list, (le, cap) -> cmd.run(le, cap, level));
            printCompletion((CommandSourceStack) ctx.getSource(), count);
            return 0;
        };
    }

    private static Command<CommandSourceStack> mobTrait(LHMobCommands.MobTraitCommand cmd) {
        return ctx -> {
            Holder.Reference<MobTrait> trait = resolveKey(ctx, "trait", LHTraits.TRAITS.key(), ERR_INVALID_NAME);
            Collection<? extends Entity> list = EntityArgument.getEntities(ctx, "targets");
            int count = iterate(list, (le, cap) -> cmd.run(le, cap, (MobTrait) trait.get()));
            printCompletion((CommandSourceStack) ctx.getSource(), count);
            return 0;
        };
    }

    private static Command<CommandSourceStack> mobTraitRank(LHMobCommands.MobTraitRankCommand cmd) {
        return ctx -> {
            int rank = (Integer) ctx.getArgument("rank", Integer.class);
            Holder.Reference<MobTrait> trait = resolveKey(ctx, "trait", LHTraits.TRAITS.key(), ERR_INVALID_NAME);
            Collection<? extends Entity> list = EntityArgument.getEntities(ctx, "targets");
            int count = iterate(list, (le, cap) -> cmd.run(le, cap, (MobTrait) trait.get(), rank));
            printCompletion((CommandSourceStack) ctx.getSource(), count);
            return 0;
        };
    }

    private static int iterate(Collection<? extends Entity> list, BiPredicate<LivingEntity, MobTraitCap> task) {
        int count = 0;
        for (Entity e : list) {
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) e;
                if (MobTraitCap.HOLDER.isProper(le)) {
                    MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(le);
                    if (task.test(le, cap)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static void printCompletion(CommandSourceStack ctx, int count) {
        if (count > 0) {
            ctx.sendSystemMessage(LangData.COMMAND_MOB_SUCCEED.get(count));
        } else {
            ctx.sendSystemMessage(LangData.COMMAND_PLAYER_FAIL.get().withStyle(ChatFormatting.RED));
        }
    }

    private static <T> ResourceKey<T> getRegistryKey(CommandContext<CommandSourceStack> ctx, String name, ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err) throws CommandSyntaxException {
        ResourceKey<?> ans = (ResourceKey<?>) ctx.getArgument(name, ResourceKey.class);
        Optional<ResourceKey<T>> optional = ans.cast(reg);
        return (ResourceKey<T>) optional.orElseThrow(() -> err.create(ans));
    }

    private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> ctx, ResourceKey<? extends Registry<T>> reg) {
        return ((CommandSourceStack) ctx.getSource()).getServer().registryAccess().m_175515_(reg);
    }

    private static <T> Holder.Reference<T> resolveKey(CommandContext<CommandSourceStack> ctx, String name, ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err) throws CommandSyntaxException {
        ResourceKey<T> ans = getRegistryKey(ctx, name, reg, err);
        return (Holder.Reference<T>) getRegistry(ctx, reg).getHolder(ans).orElseThrow(() -> err.create(ans.location()));
    }

    private interface MobCommand {

        boolean run(LivingEntity var1, MobTraitCap var2);
    }

    private interface MobLevelCommand {

        boolean run(LivingEntity var1, MobTraitCap var2, int var3);
    }

    private interface MobTraitCommand {

        boolean run(LivingEntity var1, MobTraitCap var2, MobTrait var3);
    }

    private interface MobTraitRankCommand {

        boolean run(LivingEntity var1, MobTraitCap var2, MobTrait var3, int var4);
    }
}