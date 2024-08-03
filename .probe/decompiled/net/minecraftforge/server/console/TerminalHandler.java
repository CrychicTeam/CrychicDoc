package net.minecraftforge.server.console;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.LineReader.Option;
import org.jline.terminal.Terminal;

public final class TerminalHandler {

    private TerminalHandler() {
    }

    public static boolean handleCommands(DedicatedServer server) {
        Terminal terminal = TerminalConsoleAppender.getTerminal();
        if (terminal == null) {
            return false;
        } else {
            LineReader reader = LineReaderBuilder.builder().appName("Forge").terminal(terminal).completer(new ConsoleCommandCompleter(server)).build();
            reader.setOpt(Option.DISABLE_EVENT_EXPANSION);
            reader.unsetOpt(Option.INSERT_TAB);
            TerminalConsoleAppender.setReader(reader);
            try {
                while (!server.m_129918_() && server.m_130010_()) {
                    String line;
                    try {
                        line = reader.readLine("> ");
                    } catch (EndOfFileException var9) {
                        continue;
                    }
                    if (line == null) {
                        break;
                    }
                    line = line.trim();
                    if (!line.isEmpty()) {
                        server.handleConsoleInput(line, server.m_129893_());
                    }
                }
            } catch (UserInterruptException var10) {
                server.m_7570_(true);
            } finally {
                TerminalConsoleAppender.setReader(null);
            }
            return true;
        }
    }
}