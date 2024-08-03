package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.common.commands.forge.RecordSongCommandImpl;
import net.minecraft.commands.CommandSourceStack;

public class RecordSongCommand {

    @ExpectPlatform
    @Transformed
    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return RecordSongCommandImpl.register(dispatcher);
    }
}