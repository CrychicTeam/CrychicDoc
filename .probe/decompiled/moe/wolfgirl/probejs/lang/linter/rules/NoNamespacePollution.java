package moe.wolfgirl.probejs.lang.linter.rules;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import moe.wolfgirl.probejs.ProbeConfig;
import moe.wolfgirl.probejs.lang.linter.LintingWarning;

public class NoNamespacePollution extends Rule {

    final Multimap<String, Pair<Integer, Path>> identifiers = ArrayListMultimap.create();

    @Override
    public void acceptFile(Path path, List<String> content) {
        if (ProbeConfig.INSTANCE.isolatedScopes.get()) {
            for (int i = 0; i < content.size(); i++) {
                String s = ((String) content.get(i)).trim();
                if (s.startsWith("export")) {
                    s = s.substring(6).trim();
                    String[] parts = s.split(" ", 2);
                    String var7 = parts[0];
                    String identifier = switch(var7) {
                        case "function" ->
                            parts[1].split("\\(", 2)[0];
                        case "var", "let", "const" ->
                            parts[1].split(" ")[0];
                        default ->
                            null;
                    };
                    if (identifier != null) {
                        this.identifiers.put(identifier, new Pair(i, path));
                    }
                }
            }
        } else {
            for (int ix = 0; ix < content.size(); ix++) {
                String s = ((String) content.get(ix)).trim();
                if (s.startsWith("var") || s.startsWith("let") || s.startsWith("const") || s.startsWith("function")) {
                    String[] parts = s.split(" ", 2);
                    String var14 = parts[0];
                    String identifier = switch(var14) {
                        case "function" ->
                            parts[1].split("\\(", 2)[0];
                        case "var", "let", "const" ->
                            parts[1].split(" ")[0];
                        default ->
                            null;
                    };
                    if (identifier != null) {
                        this.identifiers.put(identifier, new Pair(ix, path));
                    }
                }
            }
        }
    }

    @Override
    public List<LintingWarning> lint(Path basePath) {
        ArrayList<LintingWarning> warnings = new ArrayList();
        for (Entry<String, Collection<Pair<Integer, Path>>> entry : this.identifiers.asMap().entrySet()) {
            String identifier = (String) entry.getKey();
            Collection<Pair<Integer, Path>> paths = (Collection<Pair<Integer, Path>>) entry.getValue();
            if (paths.size() > 1) {
                for (Pair<Integer, Path> path : paths) {
                    warnings.add(new LintingWarning((Path) path.getSecond(), LintingWarning.Level.ERROR, (Integer) path.getFirst(), 0, "Duplicate declaration of %s".formatted(identifier)));
                }
            }
        }
        return warnings;
    }
}