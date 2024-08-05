package snownee.loquat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import snownee.kiwi.loader.Platform;
import snownee.loquat.Loquat;
import snownee.loquat.client.LoquatClient;
import snownee.loquat.core.select.PosSelection;
import snownee.loquat.core.select.SelectionManager;

public class LoquatCommand {

    public static final SimpleCommandExceptionType EMPTY_SELECTION = new SimpleCommandExceptionType(Component.translatable("loquat.command.emptySelection"));

    public static final SimpleCommandExceptionType TOO_MANY_SELECTIONS = new SimpleCommandExceptionType(Component.translatable("loquat.command.tooManySelections"));

    public static final SimpleCommandExceptionType AREA_MUST_BE_BOX = new SimpleCommandExceptionType(Component.translatable("loquat.command.areaMustBeBox"));

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_ADD_ZONE = SuggestionProviders.register(Loquat.id("add_zone"), (ctx, builder) -> {
        if (Platform.isPhysicalClient()) {
            LoquatClient.get().suggestAddZone(builder::suggest);
        }
        return builder.buildFuture();
    });

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_REMOVE_ZONE = SuggestionProviders.register(Loquat.id("remove_zone"), (ctx, builder) -> {
        if (Platform.isPhysicalClient()) {
            LoquatClient.get().suggestRemoveZone(builder::suggest);
        }
        return builder.buildFuture();
    });

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("loquat").requires(cs -> cs.hasPermission(2))).then(CreateCommand.register())).then(DeleteCommand.register())).then(ListCommand.register())).then(OutlineCommand.register())).then(SelectCommand.register())).then(UnselectCommand.register())).then(ZoneCommand.register())).then(ReplaceCommand.register())).then(TagCommand.register())).then(EmptyCommand.register())).then(PlaceCommand.register())).then(RestrictCommand.register())).then(ClearCommand.register());
        if (Loquat.hasLychee) {
            builder.then(SpawnCommand.register());
        } else {
            builder.then(((LiteralArgumentBuilder) Commands.literal("spawn").requires(cs -> cs.hasPermission(2))).executes(ctx -> {
                ((CommandSourceStack) ctx.getSource()).sendFailure(Component.translatable("loquat.command.lycheeNotInstalled"));
                return 0;
            }));
        }
        dispatcher.register(builder);
    }

    public static int countedSuccess(CommandContext<CommandSourceStack> ctx, String key, int count) {
        if (count == 0) {
            ((CommandSourceStack) ctx.getSource()).sendFailure(Component.translatable("loquat.command.nothingChanged"));
        } else {
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.translatable("loquat.command." + key + ".success", count), true);
        }
        return count;
    }

    public static PosSelection getSingleSelectionAndClear(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        List<PosSelection> selections = SelectionManager.of(((CommandSourceStack) ctx.getSource()).getPlayerOrException()).getSelections();
        if (selections.isEmpty()) {
            throw EMPTY_SELECTION.create();
        } else if (selections.size() > 1) {
            throw TOO_MANY_SELECTIONS.create();
        } else {
            PosSelection selection = (PosSelection) selections.get(0);
            selections.clear();
            return selection;
        }
    }
}