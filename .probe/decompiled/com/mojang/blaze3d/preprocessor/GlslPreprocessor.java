package com.mojang.blaze3d.preprocessor;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.Util;
import net.minecraft.util.StringUtil;

public abstract class GlslPreprocessor {

    private static final String C_COMMENT = "/\\*(?:[^*]|\\*+[^*/])*\\*+/";

    private static final String LINE_COMMENT = "//[^\\v]*";

    private static final Pattern REGEX_MOJ_IMPORT = Pattern.compile("(#(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*moj_import(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*(?:\"(.*)\"|<(.*)>))");

    private static final Pattern REGEX_VERSION = Pattern.compile("(#(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*version(?:/\\*(?:[^*]|\\*+[^*/])*\\*+/|\\h)*(\\d+))\\b");

    private static final Pattern REGEX_ENDS_WITH_WHITESPACE = Pattern.compile("(?:^|\\v)(?:\\s|/\\*(?:[^*]|\\*+[^*/])*\\*+/|(//[^\\v]*))*\\z");

    public List<String> process(String string0) {
        GlslPreprocessor.Context $$1 = new GlslPreprocessor.Context();
        List<String> $$2 = this.processImports(string0, $$1, "");
        $$2.set(0, this.setVersion((String) $$2.get(0), $$1.glslVersion));
        return $$2;
    }

    private List<String> processImports(String string0, GlslPreprocessor.Context glslPreprocessorContext1, String string2) {
        int $$3 = glslPreprocessorContext1.sourceId;
        int $$4 = 0;
        String $$5 = "";
        List<String> $$6 = Lists.newArrayList();
        Matcher $$7 = REGEX_MOJ_IMPORT.matcher(string0);
        while ($$7.find()) {
            if (!isDirectiveDisabled(string0, $$7, $$4)) {
                String $$8 = $$7.group(2);
                boolean $$9 = $$8 != null;
                if (!$$9) {
                    $$8 = $$7.group(3);
                }
                if ($$8 != null) {
                    String $$10 = string0.substring($$4, $$7.start(1));
                    String $$11 = string2 + $$8;
                    String $$12 = this.applyImport($$9, $$11);
                    if (!Strings.isNullOrEmpty($$12)) {
                        if (!StringUtil.endsWithNewLine($$12)) {
                            $$12 = $$12 + System.lineSeparator();
                        }
                        glslPreprocessorContext1.sourceId++;
                        int $$13 = glslPreprocessorContext1.sourceId;
                        List<String> $$14 = this.processImports($$12, glslPreprocessorContext1, $$9 ? FileUtil.getFullResourcePath($$11) : "");
                        $$14.set(0, String.format(Locale.ROOT, "#line %d %d\n%s", 0, $$13, this.processVersions((String) $$14.get(0), glslPreprocessorContext1)));
                        if (!Util.isBlank($$10)) {
                            $$6.add($$10);
                        }
                        $$6.addAll($$14);
                    } else {
                        String $$15 = $$9 ? String.format(Locale.ROOT, "/*#moj_import \"%s\"*/", $$8) : String.format(Locale.ROOT, "/*#moj_import <%s>*/", $$8);
                        $$6.add($$5 + $$10 + $$15);
                    }
                    int $$16 = StringUtil.lineCount(string0.substring(0, $$7.end(1)));
                    $$5 = String.format(Locale.ROOT, "#line %d %d", $$16, $$3);
                    $$4 = $$7.end(1);
                }
            }
        }
        String $$17 = string0.substring($$4);
        if (!Util.isBlank($$17)) {
            $$6.add($$5 + $$17);
        }
        return $$6;
    }

    private String processVersions(String string0, GlslPreprocessor.Context glslPreprocessorContext1) {
        Matcher $$2 = REGEX_VERSION.matcher(string0);
        if ($$2.find() && isDirectiveEnabled(string0, $$2)) {
            glslPreprocessorContext1.glslVersion = Math.max(glslPreprocessorContext1.glslVersion, Integer.parseInt($$2.group(2)));
            return string0.substring(0, $$2.start(1)) + "/*" + string0.substring($$2.start(1), $$2.end(1)) + "*/" + string0.substring($$2.end(1));
        } else {
            return string0;
        }
    }

    private String setVersion(String string0, int int1) {
        Matcher $$2 = REGEX_VERSION.matcher(string0);
        return $$2.find() && isDirectiveEnabled(string0, $$2) ? string0.substring(0, $$2.start(2)) + Math.max(int1, Integer.parseInt($$2.group(2))) + string0.substring($$2.end(2)) : string0;
    }

    private static boolean isDirectiveEnabled(String string0, Matcher matcher1) {
        return !isDirectiveDisabled(string0, matcher1, 0);
    }

    private static boolean isDirectiveDisabled(String string0, Matcher matcher1, int int2) {
        int $$3 = matcher1.start() - int2;
        if ($$3 == 0) {
            return false;
        } else {
            Matcher $$4 = REGEX_ENDS_WITH_WHITESPACE.matcher(string0.substring(int2, matcher1.start()));
            if (!$$4.find()) {
                return true;
            } else {
                int $$5 = $$4.end(1);
                return $$5 == matcher1.start();
            }
        }
    }

    @Nullable
    public abstract String applyImport(boolean var1, String var2);

    static final class Context {

        int glslVersion;

        int sourceId;
    }
}