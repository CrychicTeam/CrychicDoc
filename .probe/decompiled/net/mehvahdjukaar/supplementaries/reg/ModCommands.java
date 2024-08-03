package net.mehvahdjukaar.supplementaries.reg;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.common.commands.AddCageMobCommand;
import net.mehvahdjukaar.supplementaries.common.commands.ChangeGlobeSeedCommand;
import net.mehvahdjukaar.supplementaries.common.commands.IUsedToRollTheDice;
import net.mehvahdjukaar.supplementaries.common.commands.MapMarkerCommand;
import net.mehvahdjukaar.supplementaries.common.commands.OpenConfiguredCommand;
import net.mehvahdjukaar.supplementaries.common.commands.RecordSongCommand;
import net.mehvahdjukaar.supplementaries.common.commands.ReloadConfigsCommand;
import net.mehvahdjukaar.supplementaries.common.commands.ResetGlobeSeedCommand;
import net.mehvahdjukaar.supplementaries.common.commands.StructureMapCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void init() {
        RegHelper.addCommandRegistration(ModCommands::register);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("supplementaries").then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("globe").requires(p -> p.hasPermission(2))).then(ChangeGlobeSeedCommand.register(dispatcher))).then(ResetGlobeSeedCommand.register(dispatcher)))).then(ReloadConfigsCommand.register(dispatcher))).then(OpenConfiguredCommand.register(dispatcher))).then(IUsedToRollTheDice.register(dispatcher))).then(AddCageMobCommand.register(dispatcher, context))).then(RecordSongCommand.register(dispatcher))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("map").requires(p -> p.hasPermission(2))).then(MapMarkerCommand.register(dispatcher, context))).then(StructureMapCommand.register(dispatcher, context))));
    }
}