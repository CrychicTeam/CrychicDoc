package moe.wolfgirl.probejs.lang.typescript;

import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;

public record Reference(ClassPath classPath, String original, String input) {

    public String getImport() {
        String importOriginal = this.original.equals(this.classPath.getName()) ? this.original : "%s as %s".formatted(this.classPath.getName(), this.original);
        String exportedInput = "%s$Type".formatted(this.classPath.getName());
        String importInput = this.input.equals(exportedInput) ? this.input : "%s as %s".formatted(exportedInput, this.input);
        return "import {%s, %s} from \"packages/%s\"".formatted(importOriginal, importInput, this.classPath.getTypeScriptPath());
    }
}