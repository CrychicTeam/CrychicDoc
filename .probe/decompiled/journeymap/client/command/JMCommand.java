package journeymap.client.command;

import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSource;

@Deprecated
public interface JMCommand {

    String getName();

    int execute(CommandSource var1, String[] var2) throws CommandRuntimeException;

    String getUsage(CommandSource var1);
}