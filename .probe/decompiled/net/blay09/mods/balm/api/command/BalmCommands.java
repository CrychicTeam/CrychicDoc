package net.blay09.mods.balm.api.command;

import com.mojang.brigadier.CommandDispatcher;
import java.util.function.Consumer;
import net.minecraft.commands.CommandSourceStack;

public interface BalmCommands {

    void register(Consumer<CommandDispatcher<CommandSourceStack>> var1);
}