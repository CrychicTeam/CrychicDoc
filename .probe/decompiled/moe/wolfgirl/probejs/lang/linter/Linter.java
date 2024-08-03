package moe.wolfgirl.probejs.lang.linter;

import dev.latvian.mods.kubejs.KubeJSPaths;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.linter.rules.NoNamespacePollution;
import moe.wolfgirl.probejs.lang.linter.rules.RespectPriority;
import moe.wolfgirl.probejs.lang.linter.rules.Rule;
import net.minecraft.network.chat.Component;

public class Linter {

    public static final Supplier<Linter> SERVER_SCRIPT = () -> new Linter(KubeJSPaths.SERVER_SCRIPTS).defaultRules();

    public static final Supplier<Linter> CLIENT_SCRIPT = () -> new Linter(KubeJSPaths.CLIENT_SCRIPTS).defaultRules();

    public static final Supplier<Linter> STARTUP_SCRIPT = () -> new Linter(KubeJSPaths.STARTUP_SCRIPTS).defaultRules();

    public static final Linter.RuleFactory[] RULES = new Linter.RuleFactory[] { RespectPriority::new, NoNamespacePollution::new };

    public final Path scriptPath;

    public final List<Rule> rules = new ArrayList();

    public Linter(Path scriptPath) {
        this.scriptPath = scriptPath;
    }

    public Linter defaultRules() {
        for (Linter.RuleFactory rule : RULES) {
            this.rules.add(rule.get());
        }
        return this;
    }

    public Linter exclude(Class<?>... rule) {
        for (Class<?> aClass : rule) {
            this.rules.removeIf(aClass::isInstance);
        }
        return this;
    }

    public List<LintingWarning> lint() throws IOException {
        ArrayList<LintingWarning> warnings = new ArrayList();
        Stream<Path> stream = Files.walk(this.scriptPath);
        try {
            for (Path path : stream::iterator) {
                if (Files.isRegularFile(path, new LinkOption[0]) && path.toString().endsWith("js")) {
                    List<String> content = Files.readAllLines(path);
                    for (Rule rule : this.rules) {
                        rule.acceptFile(path, content);
                    }
                }
            }
        } catch (Throwable var9) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
            }
            throw var9;
        }
        if (stream != null) {
            stream.close();
        }
        for (Rule rule : this.rules) {
            warnings.addAll(rule.lint(this.scriptPath));
        }
        return warnings;
    }

    public static void defaultLint(Consumer<Component> report) {
        try {
            List<Component> warnings = new ArrayList();
            Linter startup = (Linter) STARTUP_SCRIPT.get();
            for (LintingWarning lintingWarning : startup.lint()) {
                warnings.add(lintingWarning.defaultFormatting(startup.scriptPath));
            }
            Linter server = (Linter) SERVER_SCRIPT.get();
            for (LintingWarning lintingWarning : server.lint()) {
                warnings.add(lintingWarning.defaultFormatting(server.scriptPath));
            }
            Linter client = (Linter) CLIENT_SCRIPT.get();
            for (LintingWarning lintingWarning : client.lint()) {
                warnings.add(lintingWarning.defaultFormatting(client.scriptPath));
            }
            for (Component warning : warnings) {
                report.accept(warning);
            }
            if (warnings.isEmpty()) {
                report.accept(Component.translatable("probejs.lint_passed").kjs$green());
            }
        } catch (Throwable var7) {
            ProbeJS.LOGGER.error(var7.getMessage());
        }
    }

    @FunctionalInterface
    public interface RuleFactory {

        Rule get();
    }
}