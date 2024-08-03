package snownee.loquat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.spawner.LycheeCompat;
import snownee.loquat.spawner.Spawner;

public class SpawnCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("spawn").then(Commands.argument("area", AreaArgument.area()).then(((RequiredArgumentBuilder) Commands.argument("spawner", ResourceLocationArgument.id()).suggests(LycheeCompat.SPAWNERS.suggestionProvider).executes(ctx -> spawn(ctx, null))).then(Commands.argument("difficulty", ResourceLocationArgument.id()).suggests(LycheeCompat.DIFFICULTIES.suggestionProvider).executes(ctx -> spawn(ctx, ResourceLocationArgument.getId(ctx, "difficulty"))))));
    }

    private static int spawn(CommandContext<CommandSourceStack> ctx, @Nullable ResourceLocation difficultyId) throws CommandSyntaxException {
        ResourceLocation spawnerId = ResourceLocationArgument.getId(ctx, "spawner");
        CommandSourceStack source = (CommandSourceStack) ctx.getSource();
        try {
            Spawner.spawn(spawnerId, difficultyId, source.getLevel(), AreaArgument.getArea(ctx, "area"));
        } catch (NullPointerException var5) {
            source.sendFailure(Component.translatable("loquat.command.spawnerNotExists"));
            return 0;
        }
        source.sendSuccess(() -> Component.translatable("loquat.command.spawn.success", spawnerId.toString()), true);
        return 1;
    }
}