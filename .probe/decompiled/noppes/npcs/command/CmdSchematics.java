package noppes.npcs.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.schematics.SchematicWrapper;

public class CmdSchematics {

    public static final List<String> names = new ArrayList();

    public static final SuggestionProvider<CommandSourceStack> SCHEMAS = SuggestionProviders.register(new ResourceLocation("schemas"), (context, builder) -> SharedSuggestionProvider.suggest(names.stream(), builder));

    public static final SuggestionProvider<CommandSourceStack> ROTATION = SuggestionProviders.register(new ResourceLocation("rotation"), (context, builder) -> SharedSuggestionProvider.suggest(new String[] { "0", "90", "180", "270" }, builder));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("schema").requires(source -> source.hasPermission(4))).then(Commands.literal("build").then(Commands.argument("name", StringArgumentType.word()).suggests(SCHEMAS).then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("rotation", StringArgumentType.word()).suggests(ROTATION).executes(context -> {
            String name = StringArgumentType.getString(context, "name");
            BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
            int rotation = Integer.parseInt(StringArgumentType.getString(context, "rotation"));
            SchematicWrapper schem = SchematicController.Instance.load(name);
            schem.init(pos, ((CommandSourceStack) context.getSource()).getLevel(), rotation);
            SchematicController.Instance.build(schem, (CommandSourceStack) context.getSource());
            return 1;
        })))))).then(Commands.literal("stop").executes(context -> {
            SchematicController.Instance.stop((CommandSourceStack) context.getSource());
            return 1;
        }))).then(Commands.literal("info").executes(context -> {
            SchematicController.Instance.info((CommandSourceStack) context.getSource());
            return 1;
        }))).then(Commands.literal("list").executes(context -> {
            List<String> list = SchematicController.Instance.list();
            if (list.isEmpty()) {
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("No schemas available"), false);
                return 1;
            } else {
                String s = "";
                for (String file : list) {
                    s = s + file + ", ";
                }
                String finalS = s;
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable(finalS), false);
                return 1;
            }
        }));
    }
}