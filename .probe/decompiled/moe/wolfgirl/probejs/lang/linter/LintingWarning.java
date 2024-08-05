package moe.wolfgirl.probejs.lang.linter;

import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import java.nio.file.Path;
import net.minecraft.network.chat.Component;

public record LintingWarning(Path file, LintingWarning.Level level, int line, int column, String message) {

    public Component defaultFormatting(Path relativeBase) {
        Path stripped = relativeBase.getParent().relativize(this.file);
        return Component.literal("[").append(Component.literal(this.level().name()).kjs$color(this.level().color)).append(Component.literal("] ")).append(Component.literal(stripped.toString())).append(Component.literal(":%d:%d: %s".formatted(this.line, this.column, this.message)));
    }

    public static enum Level {

        INFO(ColorWrapper.BLUE), WARNING(ColorWrapper.GOLD), ERROR(ColorWrapper.RED);

        public final Color color;

        private Level(Color color) {
            this.color = color;
        }
    }
}