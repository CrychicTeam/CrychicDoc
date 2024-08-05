package journeymap.client.command;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import journeymap.client.Constants;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSource;
import org.apache.logging.log4j.util.Strings;

public class ClientCommandInvoker implements JMCommand {

    Map<String, JMCommand> commandMap = new HashMap();

    public ClientCommandInvoker registerSub(JMCommand command) {
        this.commandMap.put(command.getName().toLowerCase(), command);
        return this;
    }

    @Override
    public String getName() {
        return "jm";
    }

    @Override
    public String getUsage(CommandSource sender) {
        StringBuffer sb = new StringBuffer();
        for (JMCommand command : this.commandMap.values()) {
            String usage = command.getUsage(sender);
            if (!Strings.isEmpty(usage)) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append("/jm ").append(usage);
            }
        }
        return sb.toString();
    }

    @Override
    public int execute(CommandSource sender, String[] args) throws CommandRuntimeException {
        try {
            if (args.length > 0) {
                JMCommand command = this.getSubCommand(args);
                if (command != null) {
                    String[] subArgs = (String[]) Arrays.copyOfRange(args, 1, args.length);
                    command.execute(sender, subArgs);
                }
            } else {
                sender.sendSystemMessage(Constants.getStringTextComponent(this.getUsage(sender)));
            }
            return 0;
        } catch (Throwable var5) {
            Journeymap.getLogger().error(LogFormatter.toPartialString(var5));
            throw new CommandRuntimeException(Constants.getStringTextComponent("Error in /jm: " + var5));
        }
    }

    public JMCommand getSubCommand(String[] args) {
        if (args.length > 0) {
            JMCommand command = (JMCommand) this.commandMap.get(args[0].toLowerCase());
            if (command != null) {
                return command;
            }
        }
        return null;
    }

    public String getPossibleCommands() {
        return Joiner.on(",").join(this.commandMap.keySet());
    }
}