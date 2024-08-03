package moe.wolfgirl.probejs.lang.linter.rules;

import com.mojang.datafixers.util.Pair;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.lang.linter.LintingWarning;
import moe.wolfgirl.probejs.utils.NameUtils;

public class RespectPriority extends Rule {

    private final Map<Path, RespectPriority.ScriptFile> files = new HashMap();

    @Override
    public void acceptFile(Path path, List<String> content) {
        int priority = 0;
        for (String s : content) {
            s = s.trim();
            if (s.startsWith("//") && s.contains("priority")) {
                try {
                    priority = Integer.parseInt(s.split(":", 2)[1].trim());
                    break;
                } catch (Throwable var9) {
                }
            }
        }
        List<Pair<Integer, Path>> depends = new ArrayList();
        for (int i = 0; i < content.size(); i++) {
            String sx = ((String) content.get(i)).trim();
            if (sx.endsWith(";")) {
                sx = sx.substring(0, sx.length() - 1);
            }
            if (sx.startsWith("import")) {
                Matcher matcher = NameUtils.MATCH_IMPORT.matcher(sx);
                if (matcher.matches()) {
                    String dependsOn = (String) ProbeJS.GSON.fromJson(matcher.group(2), String.class);
                    if (!dependsOn.startsWith("package")) {
                        depends.add(new Pair(i, path.getParent().resolve(dependsOn + ".js").toAbsolutePath().normalize()));
                    }
                }
            } else if (sx.contains("require")) {
                Matcher matcher = NameUtils.MATCH_ANY_REQUIRE.matcher(sx);
                if (matcher.matches()) {
                    String dependsOn = (String) ProbeJS.GSON.fromJson(matcher.group(2), String.class);
                    if (!dependsOn.startsWith("package")) {
                        depends.add(new Pair(i, path.getParent().resolve(dependsOn + ".js").toAbsolutePath().normalize()));
                    }
                }
            }
        }
        this.files.put(path, new RespectPriority.ScriptFile(path, priority, content, depends));
    }

    @Override
    public List<LintingWarning> lint(Path basePath) {
        List<LintingWarning> warnings = new ArrayList();
        for (Entry<Path, RespectPriority.ScriptFile> entry : this.files.entrySet()) {
            Path path = (Path) entry.getKey();
            RespectPriority.ScriptFile scriptFile = (RespectPriority.ScriptFile) entry.getValue();
            for (Pair<Integer, Path> pair : scriptFile.dependencies) {
                int line = (Integer) pair.getFirst();
                Path dependency = (Path) pair.getSecond();
                RespectPriority.ScriptFile dependencyFile = (RespectPriority.ScriptFile) this.files.get(dependency);
                if (dependencyFile == null) {
                    ProbeJS.LOGGER.info(path);
                    ProbeJS.LOGGER.info(dependency);
                    ProbeJS.LOGGER.info(this.files);
                    warnings.add(new LintingWarning(path, LintingWarning.Level.WARNING, line, 0, "Unknown dependency: %s".formatted(basePath.relativize(dependency))));
                } else if (scriptFile.compareTo(dependencyFile)) {
                    warnings.add(new LintingWarning(path, LintingWarning.Level.ERROR, line, 0, "Required %s before it loads!".formatted(basePath.relativize(dependency))));
                }
            }
        }
        return warnings;
    }

    static record ScriptFile(Path path, int priority, List<String> content, List<Pair<Integer, Path>> dependencies) {

        public boolean compareTo(RespectPriority.ScriptFile o2) {
            int priority = -Integer.compare(this.priority, o2.priority);
            if (priority == 0) {
                priority = this.path.compareTo(o2.path);
            }
            return priority < 0;
        }
    }
}