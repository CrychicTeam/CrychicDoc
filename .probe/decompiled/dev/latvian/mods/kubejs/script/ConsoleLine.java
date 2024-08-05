package dev.latvian.mods.kubejs.script;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.LogType;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;

public class ConsoleLine {

    public static final ConsoleLine[] EMPTY_ARRAY = new ConsoleLine[0];

    public final ConsoleJS console;

    public final long timestamp;

    public String message;

    public LogType type = LogType.INFO;

    public String group = "";

    public Collection<ConsoleLine.SourceLine> sourceLines = Set.of();

    public Path externalFile = null;

    public List<String> stackTrace = List.of();

    private String cachedText;

    public ConsoleLine(ConsoleJS console, long timestamp, String message) {
        this.console = console;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ConsoleLine(FriendlyByteBuf buf) {
        this.console = ScriptType.VALUES[buf.readByte()].console;
        this.timestamp = buf.readLong();
        this.message = buf.readUtf();
        this.type = LogType.VALUES[buf.readByte()];
        this.group = "";
        this.sourceLines = buf.<ConsoleLine.SourceLine>readList(ConsoleLine.SourceLine::new);
        this.stackTrace = buf.readList(FriendlyByteBuf::m_130277_);
    }

    public static void writeToNet(FriendlyByteBuf buf, ConsoleLine line) {
        buf.writeByte(line.console.scriptType.ordinal());
        buf.writeLong(line.timestamp);
        buf.writeUtf(line.message);
        buf.writeByte(line.type.ordinal());
        buf.writeCollection(line.sourceLines, ConsoleLine.SourceLine::write);
        buf.writeCollection(line.stackTrace, FriendlyByteBuf::m_130070_);
    }

    public String getText() {
        if (this.cachedText == null) {
            StringBuilder builder = new StringBuilder();
            if (!this.sourceLines.isEmpty()) {
                for (ConsoleLine.SourceLine line : this.sourceLines) {
                    if (line.line != 0 && !line.source.isEmpty()) {
                        builder.append(line.source).append('#').append(line.line).append(':').append(' ');
                        break;
                    }
                }
            }
            if (!this.group.isEmpty()) {
                builder.append(this.group);
            }
            builder.append(this.message);
            this.cachedText = builder.toString();
        }
        return this.cachedText;
    }

    public ConsoleLine withSourceLine(String source, int line) {
        if (source == null) {
            source = "";
        }
        if (!source.isEmpty() && source.startsWith(this.console.scriptType.nameStrip)) {
            source = source.substring(this.console.scriptType.nameStrip.length());
        }
        if (line < 0) {
            line = 0;
        }
        if (this.sourceLines.isEmpty()) {
            this.sourceLines = Set.of(new ConsoleLine.SourceLine(source, line));
            return this;
        } else {
            if (this.sourceLines.size() == 1) {
                ConsoleLine.SourceLine line0 = (ConsoleLine.SourceLine) this.sourceLines.iterator().next();
                this.sourceLines = new LinkedHashSet();
                this.sourceLines.add(line0);
            }
            this.sourceLines.add(new ConsoleLine.SourceLine(source, line));
            return this;
        }
    }

    public ConsoleLine withExternalFile(Path path) {
        this.externalFile = path;
        this.sourceLines = Set.of(new ConsoleLine.SourceLine(path.getFileName().toString(), 0));
        return this;
    }

    public String toString() {
        return this.getText();
    }

    public static record SourceLine(String source, int line) {

        public SourceLine(FriendlyByteBuf buf) {
            this(buf.readUtf(), buf.readVarInt());
        }

        public String toString() {
            if (this.source.isEmpty() && this.line == 0) {
                return "";
            } else if (this.source.isEmpty()) {
                return "<unknown source>#" + this.line;
            } else {
                return this.line == 0 ? this.source : this.source + "#" + this.line;
            }
        }

        public static void write(FriendlyByteBuf buf, ConsoleLine.SourceLine sourceLine) {
            buf.writeUtf(sourceLine.source);
            buf.writeVarInt(sourceLine.line);
        }
    }
}