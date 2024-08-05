package de.keksuccino.konkrete.json.jsonpath;

import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import de.keksuccino.konkrete.json.jsonpath.internal.filter.RelationalExpressionNode;
import de.keksuccino.konkrete.json.jsonpath.internal.filter.RelationalOperator;
import de.keksuccino.konkrete.json.jsonpath.internal.filter.ValueNode;
import de.keksuccino.konkrete.json.jsonpath.internal.filter.ValueNodes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Criteria implements Predicate {

    private final List<Criteria> criteriaChain;

    private ValueNode left;

    private RelationalOperator criteriaType;

    private ValueNode right;

    private Criteria(List<Criteria> criteriaChain, ValueNode left) {
        this.left = left;
        this.criteriaChain = criteriaChain;
        this.criteriaChain.add(this);
    }

    private Criteria(ValueNode left) {
        this(new LinkedList(), left);
    }

    @Override
    public boolean apply(Predicate.PredicateContext ctx) {
        for (RelationalExpressionNode expressionNode : this.toRelationalExpressionNodes()) {
            if (!expressionNode.apply(ctx)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return Utils.join(" && ", this.toRelationalExpressionNodes());
    }

    private Collection<RelationalExpressionNode> toRelationalExpressionNodes() {
        List<RelationalExpressionNode> nodes = new ArrayList(this.criteriaChain.size());
        for (Criteria criteria : this.criteriaChain) {
            nodes.add(new RelationalExpressionNode(criteria.left, criteria.criteriaType, criteria.right));
        }
        return nodes;
    }

    @Deprecated
    public static Criteria where(Path key) {
        return new Criteria(ValueNode.createPathNode(key));
    }

    public static Criteria where(String key) {
        return new Criteria(ValueNode.toValueNode(prefixPath(key)));
    }

    public Criteria and(String key) {
        this.checkComplete();
        return new Criteria(this.criteriaChain, ValueNode.toValueNode(prefixPath(key)));
    }

    public Criteria is(Object o) {
        this.criteriaType = RelationalOperator.EQ;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria eq(Object o) {
        return this.is(o);
    }

    public Criteria ne(Object o) {
        this.criteriaType = RelationalOperator.NE;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria lt(Object o) {
        this.criteriaType = RelationalOperator.LT;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria lte(Object o) {
        this.criteriaType = RelationalOperator.LTE;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria gt(Object o) {
        this.criteriaType = RelationalOperator.GT;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria gte(Object o) {
        this.criteriaType = RelationalOperator.GTE;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria regex(Pattern pattern) {
        Utils.notNull(pattern, "pattern can not be null");
        this.criteriaType = RelationalOperator.REGEX;
        this.right = ValueNode.toValueNode(pattern);
        return this;
    }

    public Criteria in(Object... o) {
        return this.in(Arrays.asList(o));
    }

    public Criteria in(Collection<?> c) {
        Utils.notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.IN;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    public Criteria contains(Object o) {
        this.criteriaType = RelationalOperator.CONTAINS;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    public Criteria nin(Object... o) {
        return this.nin(Arrays.asList(o));
    }

    public Criteria nin(Collection<?> c) {
        Utils.notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.NIN;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    public Criteria subsetof(Object... o) {
        return this.subsetof(Arrays.asList(o));
    }

    public Criteria subsetof(Collection<?> c) {
        Utils.notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.SUBSETOF;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    public Criteria anyof(Object... o) {
        return this.subsetof(Arrays.asList(o));
    }

    public Criteria anyof(Collection<?> c) {
        Utils.notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.ANYOF;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    public Criteria noneof(Object... o) {
        return this.noneof(Arrays.asList(o));
    }

    public Criteria noneof(Collection<?> c) {
        Utils.notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.NONEOF;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    public Criteria all(Object... o) {
        return this.all(Arrays.asList(o));
    }

    public Criteria all(Collection<?> c) {
        Utils.notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.ALL;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    public Criteria size(int size) {
        this.criteriaType = RelationalOperator.SIZE;
        this.right = ValueNode.toValueNode(size);
        return this;
    }

    public Criteria type(Class<?> clazz) {
        this.criteriaType = RelationalOperator.TYPE;
        this.right = ValueNode.createClassNode(clazz);
        return this;
    }

    public Criteria exists(boolean shouldExist) {
        this.criteriaType = RelationalOperator.EXISTS;
        this.right = ValueNode.toValueNode(shouldExist);
        this.left = this.left.asPathNode().asExistsCheck(shouldExist);
        return this;
    }

    @Deprecated
    public Criteria notEmpty() {
        return this.empty(false);
    }

    public Criteria empty(boolean empty) {
        this.criteriaType = RelationalOperator.EMPTY;
        this.right = empty ? ValueNodes.TRUE : ValueNodes.FALSE;
        return this;
    }

    public Criteria matches(Predicate p) {
        this.criteriaType = RelationalOperator.MATCHES;
        this.right = new ValueNodes.PredicateNode(p);
        return this;
    }

    @Deprecated
    public static Criteria parse(String criteria) {
        if (criteria == null) {
            throw new InvalidPathException("Criteria can not be null");
        } else {
            String[] split = criteria.trim().split(" ");
            if (split.length == 3) {
                return create(split[0], split[1], split[2]);
            } else if (split.length == 1) {
                return create(split[0], "EXISTS", "true");
            } else {
                throw new InvalidPathException("Could not parse criteria");
            }
        }
    }

    @Deprecated
    public static Criteria create(String left, String operator, String right) {
        Criteria criteria = new Criteria(ValueNode.toValueNode(left));
        criteria.criteriaType = RelationalOperator.fromString(operator);
        criteria.right = ValueNode.toValueNode(right);
        return criteria;
    }

    private static String prefixPath(String key) {
        if (!key.startsWith("$") && !key.startsWith("@")) {
            key = "@." + key;
        }
        return key;
    }

    private void checkComplete() {
        boolean complete = this.left != null && this.criteriaType != null && this.right != null;
        if (!complete) {
            throw new JsonPathException("Criteria build exception. Complete on criteria before defining next.");
        }
    }
}