package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class EvaluatorFactory {

    private static final Map<RelationalOperator, Evaluator> evaluators = new HashMap();

    public static Evaluator createEvaluator(RelationalOperator operator) {
        return (Evaluator) evaluators.get(operator);
    }

    static {
        evaluators.put(RelationalOperator.EXISTS, new EvaluatorFactory.ExistsEvaluator());
        evaluators.put(RelationalOperator.NE, new EvaluatorFactory.NotEqualsEvaluator());
        evaluators.put(RelationalOperator.TSNE, new EvaluatorFactory.TypeSafeNotEqualsEvaluator());
        evaluators.put(RelationalOperator.EQ, new EvaluatorFactory.EqualsEvaluator());
        evaluators.put(RelationalOperator.TSEQ, new EvaluatorFactory.TypeSafeEqualsEvaluator());
        evaluators.put(RelationalOperator.LT, new EvaluatorFactory.LessThanEvaluator());
        evaluators.put(RelationalOperator.LTE, new EvaluatorFactory.LessThanEqualsEvaluator());
        evaluators.put(RelationalOperator.GT, new EvaluatorFactory.GreaterThanEvaluator());
        evaluators.put(RelationalOperator.GTE, new EvaluatorFactory.GreaterThanEqualsEvaluator());
        evaluators.put(RelationalOperator.REGEX, new EvaluatorFactory.RegexpEvaluator());
        evaluators.put(RelationalOperator.SIZE, new EvaluatorFactory.SizeEvaluator());
        evaluators.put(RelationalOperator.EMPTY, new EvaluatorFactory.EmptyEvaluator());
        evaluators.put(RelationalOperator.IN, new EvaluatorFactory.InEvaluator());
        evaluators.put(RelationalOperator.NIN, new EvaluatorFactory.NotInEvaluator());
        evaluators.put(RelationalOperator.ALL, new EvaluatorFactory.AllEvaluator());
        evaluators.put(RelationalOperator.CONTAINS, new EvaluatorFactory.ContainsEvaluator());
        evaluators.put(RelationalOperator.MATCHES, new EvaluatorFactory.PredicateMatchEvaluator());
        evaluators.put(RelationalOperator.TYPE, new EvaluatorFactory.TypeEvaluator());
        evaluators.put(RelationalOperator.SUBSETOF, new EvaluatorFactory.SubsetOfEvaluator());
        evaluators.put(RelationalOperator.ANYOF, new EvaluatorFactory.AnyOfEvaluator());
        evaluators.put(RelationalOperator.NONEOF, new EvaluatorFactory.NoneOfEvaluator());
    }

    private static class AllEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            ValueNodes.ValueListNode requiredValues = right.asValueListNode();
            if (!left.isJsonNode()) {
                return false;
            } else {
                ValueNode valueNode = left.asJsonNode().asValueListNode(ctx);
                if (valueNode.isValueListNode()) {
                    ValueNodes.ValueListNode shouldContainAll = valueNode.asValueListNode();
                    for (ValueNode required : requiredValues) {
                        if (!shouldContainAll.contains(required)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    private static class AnyOfEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            ValueNodes.ValueListNode rightValueListNode;
            if (right.isJsonNode()) {
                ValueNode vn = right.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                rightValueListNode = vn.asValueListNode();
            } else {
                rightValueListNode = right.asValueListNode();
            }
            ValueNodes.ValueListNode leftValueListNode;
            if (left.isJsonNode()) {
                ValueNode vn = left.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                leftValueListNode = vn.asValueListNode();
            } else {
                leftValueListNode = left.asValueListNode();
            }
            for (ValueNode leftValueNode : leftValueListNode) {
                for (ValueNode rightValueNode : rightValueListNode) {
                    if (leftValueNode.equals(rightValueNode)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class ContainsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (left.isStringNode() && right.isStringNode()) {
                return left.asStringNode().contains(right.asStringNode().getString());
            } else if (left.isJsonNode()) {
                ValueNode valueNode = left.asJsonNode().asValueListNode(ctx);
                return valueNode.isUndefinedNode() ? false : valueNode.asValueListNode().contains(right);
            } else {
                return false;
            }
        }
    }

    private static class EmptyEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (left.isStringNode()) {
                return left.asStringNode().isEmpty() == right.asBooleanNode().getBoolean();
            } else {
                return left.isJsonNode() ? left.asJsonNode().isEmpty(ctx) == right.asBooleanNode().getBoolean() : false;
            }
        }
    }

    private static class EqualsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return left.isJsonNode() && right.isJsonNode() ? left.asJsonNode().equals(right.asJsonNode(), ctx) : left.equals(right);
        }
    }

    private static class ExistsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (!left.isBooleanNode() && !right.isBooleanNode()) {
                throw new JsonPathException("Failed to evaluate exists expression");
            } else {
                return left.asBooleanNode().getBoolean() == right.asBooleanNode().getBoolean();
            }
        }
    }

    private static class GreaterThanEqualsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (left.isNumberNode() && right.isNumberNode()) {
                return left.asNumberNode().getNumber().compareTo(right.asNumberNode().getNumber()) >= 0;
            } else if (left.isStringNode() && right.isStringNode()) {
                return left.asStringNode().getString().compareTo(right.asStringNode().getString()) >= 0;
            } else {
                return left.isOffsetDateTimeNode() && right.isOffsetDateTimeNode() ? left.asOffsetDateTimeNode().getDate().compareTo(right.asOffsetDateTimeNode().getDate()) >= 0 : false;
            }
        }
    }

    private static class GreaterThanEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (left.isNumberNode() && right.isNumberNode()) {
                return left.asNumberNode().getNumber().compareTo(right.asNumberNode().getNumber()) > 0;
            } else if (left.isStringNode() && right.isStringNode()) {
                return left.asStringNode().getString().compareTo(right.asStringNode().getString()) > 0;
            } else {
                return left.isOffsetDateTimeNode() && right.isOffsetDateTimeNode() ? left.asOffsetDateTimeNode().getDate().compareTo(right.asOffsetDateTimeNode().getDate()) > 0 : false;
            }
        }
    }

    private static class InEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            ValueNodes.ValueListNode valueListNode;
            if (right.isJsonNode()) {
                ValueNode vn = right.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                valueListNode = vn.asValueListNode();
            } else {
                valueListNode = right.asValueListNode();
            }
            return valueListNode.contains(left);
        }
    }

    private static class LessThanEqualsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (left.isNumberNode() && right.isNumberNode()) {
                return left.asNumberNode().getNumber().compareTo(right.asNumberNode().getNumber()) <= 0;
            } else if (left.isStringNode() && right.isStringNode()) {
                return left.asStringNode().getString().compareTo(right.asStringNode().getString()) <= 0;
            } else {
                return left.isOffsetDateTimeNode() && right.isOffsetDateTimeNode() ? left.asOffsetDateTimeNode().getDate().compareTo(right.asOffsetDateTimeNode().getDate()) <= 0 : false;
            }
        }
    }

    private static class LessThanEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (left.isNumberNode() && right.isNumberNode()) {
                return left.asNumberNode().getNumber().compareTo(right.asNumberNode().getNumber()) < 0;
            } else if (left.isStringNode() && right.isStringNode()) {
                return left.asStringNode().getString().compareTo(right.asStringNode().getString()) < 0;
            } else {
                return left.isOffsetDateTimeNode() && right.isOffsetDateTimeNode() ? left.asOffsetDateTimeNode().getDate().compareTo(right.asOffsetDateTimeNode().getDate()) < 0 : false;
            }
        }
    }

    private static class NoneOfEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            ValueNodes.ValueListNode rightValueListNode;
            if (right.isJsonNode()) {
                ValueNode vn = right.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                rightValueListNode = vn.asValueListNode();
            } else {
                rightValueListNode = right.asValueListNode();
            }
            ValueNodes.ValueListNode leftValueListNode;
            if (left.isJsonNode()) {
                ValueNode vn = left.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                leftValueListNode = vn.asValueListNode();
            } else {
                leftValueListNode = left.asValueListNode();
            }
            for (ValueNode leftValueNode : leftValueListNode) {
                for (ValueNode rightValueNode : rightValueListNode) {
                    if (leftValueNode.equals(rightValueNode)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static class NotEqualsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return !((Evaluator) EvaluatorFactory.evaluators.get(RelationalOperator.EQ)).evaluate(left, right, ctx);
        }
    }

    private static class NotInEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return !((Evaluator) EvaluatorFactory.evaluators.get(RelationalOperator.IN)).evaluate(left, right, ctx);
        }
    }

    private static class PredicateMatchEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return right.asPredicateNode().getPredicate().apply(ctx);
        }
    }

    private static class RegexpEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (!(left.isPatternNode() ^ right.isPatternNode())) {
                return false;
            } else if (left.isPatternNode()) {
                return !right.isValueListNode() && (!right.isJsonNode() || !right.asJsonNode().isArray(ctx)) ? this.matches(left.asPatternNode(), this.getInput(right)) : this.matchesAny(left.asPatternNode(), right.asJsonNode().asValueListNode(ctx));
            } else {
                return !left.isValueListNode() && (!left.isJsonNode() || !left.asJsonNode().isArray(ctx)) ? this.matches(right.asPatternNode(), this.getInput(left)) : this.matchesAny(right.asPatternNode(), left.asJsonNode().asValueListNode(ctx));
            }
        }

        private boolean matches(ValueNodes.PatternNode patternNode, String inputToMatch) {
            return patternNode.getCompiledPattern().matcher(inputToMatch).matches();
        }

        private boolean matchesAny(ValueNodes.PatternNode patternNode, ValueNode valueNode) {
            if (!valueNode.isValueListNode()) {
                return false;
            } else {
                ValueNodes.ValueListNode listNode = valueNode.asValueListNode();
                Pattern pattern = patternNode.getCompiledPattern();
                Iterator<ValueNode> it = listNode.iterator();
                while (it.hasNext()) {
                    String input = this.getInput((ValueNode) it.next());
                    if (pattern.matcher(input).matches()) {
                        return true;
                    }
                }
                return false;
            }
        }

        private String getInput(ValueNode valueNode) {
            String input = "";
            if (valueNode.isStringNode() || valueNode.isNumberNode()) {
                input = valueNode.asStringNode().getString();
            } else if (valueNode.isBooleanNode()) {
                input = valueNode.asBooleanNode().toString();
            }
            return input;
        }
    }

    private static class SizeEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            if (!right.isNumberNode()) {
                return false;
            } else {
                int expectedSize = right.asNumberNode().getNumber().intValue();
                if (left.isStringNode()) {
                    return left.asStringNode().length() == expectedSize;
                } else {
                    return left.isJsonNode() ? left.asJsonNode().length(ctx) == expectedSize : false;
                }
            }
        }
    }

    private static class SubsetOfEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            ValueNodes.ValueListNode rightValueListNode;
            if (right.isJsonNode()) {
                ValueNode vn = right.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                rightValueListNode = vn.asValueListNode();
            } else {
                rightValueListNode = right.asValueListNode();
            }
            ValueNodes.ValueListNode leftValueListNode;
            if (left.isJsonNode()) {
                ValueNode vn = left.asJsonNode().asValueListNode(ctx);
                if (vn.isUndefinedNode()) {
                    return false;
                }
                leftValueListNode = vn.asValueListNode();
            } else {
                leftValueListNode = left.asValueListNode();
            }
            return leftValueListNode.subsetof(rightValueListNode);
        }
    }

    private static class TypeEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return right.asClassNode().getClazz() == left.type(ctx);
        }
    }

    private static class TypeSafeEqualsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return !left.getClass().equals(right.getClass()) ? false : ((Evaluator) EvaluatorFactory.evaluators.get(RelationalOperator.EQ)).evaluate(left, right, ctx);
        }
    }

    private static class TypeSafeNotEqualsEvaluator implements Evaluator {

        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx) {
            return !((Evaluator) EvaluatorFactory.evaluators.get(RelationalOperator.TSEQ)).evaluate(left, right, ctx);
        }
    }
}