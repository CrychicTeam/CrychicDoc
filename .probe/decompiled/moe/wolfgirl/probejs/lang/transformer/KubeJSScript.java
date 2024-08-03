package moe.wolfgirl.probejs.lang.transformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.ProbeConfig;
import moe.wolfgirl.probejs.utils.NameUtils;

public class KubeJSScript {

    public final Set<String> exportedSymbols;

    public final List<String> lines;

    public KubeJSScript(List<String> lines) {
        this.lines = new ArrayList(lines);
        this.exportedSymbols = new HashSet();
    }

    public void processRequire() {
        for (int i = 0; i < this.lines.size(); i++) {
            String tLine = ((String) this.lines.get(i)).trim();
            List<String> parts = new ArrayList();
            for (String s : tLine.split(";")) {
                if (s.startsWith("import")) {
                    Matcher match = NameUtils.MATCH_IMPORT.matcher(s.trim());
                    if (match.matches()) {
                        String names = match.group(1).trim();
                        String classPath = match.group(2).trim();
                        if (classPath.startsWith("\"packages")) {
                            s = "let {%s} = require(%s)".formatted(names, classPath);
                        } else {
                            s = "";
                        }
                    }
                } else if (s.startsWith("const {") && s.contains("require")) {
                    Matcher matcher = NameUtils.MATCH_CONST_REQUIRE.matcher(s.trim());
                    if (matcher.matches()) {
                        String names = matcher.group(1).trim();
                        String classPath = matcher.group(2).trim();
                        if (classPath.startsWith("\"packages")) {
                            s = "let {%s} = require(%s)".formatted(names, classPath);
                        } else {
                            s = "";
                        }
                    }
                }
                parts.add(s);
            }
            this.lines.set(i, String.join(";", parts));
        }
    }

    public void processExport() {
        for (int i = 0; i < this.lines.size(); i++) {
            String tLine = ((String) this.lines.get(i)).trim();
            if (tLine.startsWith("export")) {
                tLine = tLine.substring(6).trim();
                String[] parts = tLine.split(" ", 2);
                String var5 = parts[0];
                String identifier = switch(var5) {
                    case "function" ->
                        parts[1].split("\\(")[0];
                    case "var", "let", "const" ->
                        parts[1].split(" ")[0];
                    default ->
                        null;
                };
                if (identifier == null) {
                    continue;
                }
                this.exportedSymbols.add(identifier);
            }
            this.lines.set(i, tLine);
        }
    }

    public void wrapScope() {
        String exported = (String) this.exportedSymbols.stream().map(s -> "%s: %s".formatted(s, s)).collect(Collectors.joining(", "));
        String destructed = String.join(", ", this.exportedSymbols);
        this.lines.add(0, "const {%s} = (()=>{".formatted(destructed));
        this.lines.add("return {%s};})()".formatted(exported));
    }

    public String[] transform() {
        this.processRequire();
        this.processExport();
        if (ProbeConfig.INSTANCE.isolatedScopes.get() && !this.exportedSymbols.isEmpty()) {
            this.wrapScope();
        }
        return (String[]) this.lines.toArray(String[]::new);
    }
}