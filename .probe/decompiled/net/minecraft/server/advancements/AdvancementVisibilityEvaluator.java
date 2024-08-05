package net.minecraft.server.advancements;

import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Predicate;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;

public class AdvancementVisibilityEvaluator {

    private static final int VISIBILITY_DEPTH = 2;

    private static AdvancementVisibilityEvaluator.VisibilityRule evaluateVisibilityRule(Advancement advancement0, boolean boolean1) {
        DisplayInfo $$2 = advancement0.getDisplay();
        if ($$2 == null) {
            return AdvancementVisibilityEvaluator.VisibilityRule.HIDE;
        } else if (boolean1) {
            return AdvancementVisibilityEvaluator.VisibilityRule.SHOW;
        } else {
            return $$2.isHidden() ? AdvancementVisibilityEvaluator.VisibilityRule.HIDE : AdvancementVisibilityEvaluator.VisibilityRule.NO_CHANGE;
        }
    }

    private static boolean evaluateVisiblityForUnfinishedNode(Stack<AdvancementVisibilityEvaluator.VisibilityRule> stackAdvancementVisibilityEvaluatorVisibilityRule0) {
        for (int $$1 = 0; $$1 <= 2; $$1++) {
            AdvancementVisibilityEvaluator.VisibilityRule $$2 = (AdvancementVisibilityEvaluator.VisibilityRule) stackAdvancementVisibilityEvaluatorVisibilityRule0.peek($$1);
            if ($$2 == AdvancementVisibilityEvaluator.VisibilityRule.SHOW) {
                return true;
            }
            if ($$2 == AdvancementVisibilityEvaluator.VisibilityRule.HIDE) {
                return false;
            }
        }
        return false;
    }

    private static boolean evaluateVisibility(Advancement advancement0, Stack<AdvancementVisibilityEvaluator.VisibilityRule> stackAdvancementVisibilityEvaluatorVisibilityRule1, Predicate<Advancement> predicateAdvancement2, AdvancementVisibilityEvaluator.Output advancementVisibilityEvaluatorOutput3) {
        boolean $$4 = predicateAdvancement2.test(advancement0);
        AdvancementVisibilityEvaluator.VisibilityRule $$5 = evaluateVisibilityRule(advancement0, $$4);
        boolean $$6 = $$4;
        stackAdvancementVisibilityEvaluatorVisibilityRule1.push($$5);
        for (Advancement $$7 : advancement0.getChildren()) {
            $$6 |= evaluateVisibility($$7, stackAdvancementVisibilityEvaluatorVisibilityRule1, predicateAdvancement2, advancementVisibilityEvaluatorOutput3);
        }
        boolean $$8 = $$6 || evaluateVisiblityForUnfinishedNode(stackAdvancementVisibilityEvaluatorVisibilityRule1);
        stackAdvancementVisibilityEvaluatorVisibilityRule1.pop();
        advancementVisibilityEvaluatorOutput3.accept(advancement0, $$8);
        return $$6;
    }

    public static void evaluateVisibility(Advancement advancement0, Predicate<Advancement> predicateAdvancement1, AdvancementVisibilityEvaluator.Output advancementVisibilityEvaluatorOutput2) {
        Advancement $$3 = advancement0.getRoot();
        Stack<AdvancementVisibilityEvaluator.VisibilityRule> $$4 = new ObjectArrayList();
        for (int $$5 = 0; $$5 <= 2; $$5++) {
            $$4.push(AdvancementVisibilityEvaluator.VisibilityRule.NO_CHANGE);
        }
        evaluateVisibility($$3, $$4, predicateAdvancement1, advancementVisibilityEvaluatorOutput2);
    }

    @FunctionalInterface
    public interface Output {

        void accept(Advancement var1, boolean var2);
    }

    static enum VisibilityRule {

        SHOW, HIDE, NO_CHANGE
    }
}