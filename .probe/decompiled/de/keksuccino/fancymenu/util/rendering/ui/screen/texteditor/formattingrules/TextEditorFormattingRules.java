package de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules;

import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules.brackets.HighlightAngleBracketsFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules.brackets.HighlightCurlyBracketsFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules.brackets.HighlightRoundBracketsFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules.brackets.HighlightSquareBracketsFormattingRule;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextEditorFormattingRules {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final List<Class<? extends TextEditorFormattingRule>> RULE_CLASSES = new ArrayList();

    public static void addRuleAtTop(Class<? extends TextEditorFormattingRule> rule) {
        if (!RULE_CLASSES.contains(rule)) {
            RULE_CLASSES.add(0, rule);
        }
    }

    public static void addRuleAtBottom(Class<? extends TextEditorFormattingRule> rule) {
        if (!RULE_CLASSES.contains(rule)) {
            RULE_CLASSES.add(rule);
        }
    }

    public static List<TextEditorFormattingRule> getRules() {
        List<TextEditorFormattingRule> r = new ArrayList();
        for (Class<? extends TextEditorFormattingRule> rule : RULE_CLASSES) {
            try {
                r.add((TextEditorFormattingRule) rule.getDeclaredConstructor().newInstance());
            } catch (Exception var4) {
                LOGGER.error("[FANCYMENU] Unable to construct new instance of rule (" + (rule != null ? rule.getName() : "NULL") + ")!");
                LOGGER.error("[FANCYMENU] Rules need an empty public constructor!");
                var4.printStackTrace();
            }
        }
        return r;
    }

    static {
        addRuleAtTop(HighlightPlaceholdersFormattingRule.class);
        addRuleAtBottom(HighlightAngleBracketsFormattingRule.class);
        addRuleAtBottom(HighlightCurlyBracketsFormattingRule.class);
        addRuleAtBottom(HighlightRoundBracketsFormattingRule.class);
        addRuleAtBottom(HighlightSquareBracketsFormattingRule.class);
    }
}