package io.redspace.ironsspellbooks.registries;

import com.mojang.brigadier.CommandDispatcher;
import io.redspace.ironsspellbooks.command.CastCommand;
import io.redspace.ironsspellbooks.command.ClearCooldownCommand;
import io.redspace.ironsspellbooks.command.ClearRecastsCommand;
import io.redspace.ironsspellbooks.command.ClearSpellSelectionCommand;
import io.redspace.ironsspellbooks.command.CreateDebugWizardCommand;
import io.redspace.ironsspellbooks.command.CreateImbuedSwordCommand;
import io.redspace.ironsspellbooks.command.CreateScrollCommand;
import io.redspace.ironsspellbooks.command.CreateSpellBookCommand;
import io.redspace.ironsspellbooks.command.GenerateModList;
import io.redspace.ironsspellbooks.command.IronsDebugCommand;
import io.redspace.ironsspellbooks.command.LearnCommand;
import io.redspace.ironsspellbooks.command.ManaCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLLoader;

@EventBusSubscriber
public class CommandRegistry {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CommandBuildContext commandBuildContext = event.getBuildContext();
        CreateScrollCommand.register(commandDispatcher);
        CreateSpellBookCommand.register(commandDispatcher);
        CreateImbuedSwordCommand.register(commandDispatcher, commandBuildContext);
        CreateDebugWizardCommand.register(commandDispatcher);
        CastCommand.register(commandDispatcher);
        ManaCommand.register(commandDispatcher);
        GenerateModList.register(commandDispatcher);
        LearnCommand.register(commandDispatcher);
        ClearCooldownCommand.register(commandDispatcher);
        ClearRecastsCommand.register(commandDispatcher);
        if (!FMLLoader.isProduction()) {
            ClearSpellSelectionCommand.register(commandDispatcher);
            IronsDebugCommand.register(commandDispatcher);
        }
    }
}