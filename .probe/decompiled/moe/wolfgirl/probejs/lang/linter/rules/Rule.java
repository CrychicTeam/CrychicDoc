package moe.wolfgirl.probejs.lang.linter.rules;

import java.nio.file.Path;
import java.util.List;
import moe.wolfgirl.probejs.lang.linter.LintingWarning;

public abstract class Rule {

    public abstract void acceptFile(Path path, List<String> content);

    public abstract List<LintingWarning> lint(Path basePath);
}