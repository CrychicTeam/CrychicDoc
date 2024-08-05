package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import de.keksuccino.konkrete.json.jsonpath.internal.path.PathCompiler;
import de.keksuccino.konkrete.json.jsonpath.internal.path.PredicateContextImpl;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import de.keksuccino.konkrete.json.minidev.json.parser.JSONParser;
import de.keksuccino.konkrete.json.minidev.json.parser.ParseException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ValueNodes {

    ValueNodes.NullNode NULL_NODE = new ValueNodes.NullNode();

    ValueNodes.BooleanNode TRUE = new ValueNodes.BooleanNode("true");

    ValueNodes.BooleanNode FALSE = new ValueNodes.BooleanNode("false");

    ValueNodes.UndefinedNode UNDEFINED = new ValueNodes.UndefinedNode();

    public static class BooleanNode extends ValueNode {

        private final Boolean value;

        private BooleanNode(CharSequence boolValue) {
            this.value = Boolean.parseBoolean(boolValue.toString());
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Boolean.class;
        }

        @Override
        public boolean isBooleanNode() {
            return true;
        }

        @Override
        public ValueNodes.BooleanNode asBooleanNode() {
            return this;
        }

        public boolean getBoolean() {
            return this.value;
        }

        public String toString() {
            return this.value.toString();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.BooleanNode that)) {
                return false;
            } else {
                return this.value != null ? this.value.equals(that.value) : that.value == null;
            }
        }
    }

    public static class ClassNode extends ValueNode {

        private final Class clazz;

        ClassNode(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Class.class;
        }

        @Override
        public boolean isClassNode() {
            return true;
        }

        @Override
        public ValueNodes.ClassNode asClassNode() {
            return this;
        }

        public Class getClazz() {
            return this.clazz;
        }

        public String toString() {
            return this.clazz.getName();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.ClassNode that)) {
                return false;
            } else {
                return this.clazz != null ? this.clazz.equals(that.clazz) : that.clazz == null;
            }
        }
    }

    public static class JsonNode extends ValueNode {

        private final Object json;

        private final boolean parsed;

        JsonNode(CharSequence charSequence) {
            this.json = charSequence.toString();
            this.parsed = false;
        }

        JsonNode(Object parsedJson) {
            this.json = parsedJson;
            this.parsed = true;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            if (this.isArray(ctx)) {
                return List.class;
            } else if (this.isMap(ctx)) {
                return Map.class;
            } else if (this.parse(ctx) instanceof Number) {
                return Number.class;
            } else if (this.parse(ctx) instanceof String) {
                return String.class;
            } else {
                return this.parse(ctx) instanceof Boolean ? Boolean.class : Void.class;
            }
        }

        @Override
        public boolean isJsonNode() {
            return true;
        }

        @Override
        public ValueNodes.JsonNode asJsonNode() {
            return this;
        }

        public ValueNode asValueListNode(Predicate.PredicateContext ctx) {
            return (ValueNode) (!this.isArray(ctx) ? ValueNodes.UNDEFINED : new ValueNodes.ValueListNode(Collections.unmodifiableList((List) this.parse(ctx))));
        }

        public Object parse(Predicate.PredicateContext ctx) {
            try {
                return this.parsed ? this.json : new JSONParser(-1).parse(this.json.toString());
            } catch (ParseException var3) {
                throw new IllegalArgumentException(var3);
            }
        }

        public boolean isParsed() {
            return this.parsed;
        }

        public Object getJson() {
            return this.json;
        }

        public boolean isArray(Predicate.PredicateContext ctx) {
            return this.parse(ctx) instanceof List;
        }

        public boolean isMap(Predicate.PredicateContext ctx) {
            return this.parse(ctx) instanceof Map;
        }

        public int length(Predicate.PredicateContext ctx) {
            return this.isArray(ctx) ? ((List) this.parse(ctx)).size() : -1;
        }

        public boolean isEmpty(Predicate.PredicateContext ctx) {
            if (this.isArray(ctx) || this.isMap(ctx)) {
                return ((Collection) this.parse(ctx)).size() == 0;
            } else {
                return this.parse(ctx) instanceof String ? ((String) this.parse(ctx)).length() == 0 : true;
            }
        }

        public String toString() {
            return this.json.toString();
        }

        public boolean equals(ValueNodes.JsonNode jsonNode, Predicate.PredicateContext ctx) {
            if (this == jsonNode) {
                return true;
            } else {
                return this.json != null ? this.json.equals(jsonNode.parse(ctx)) : jsonNode.json == null;
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.JsonNode jsonNode)) {
                return false;
            } else {
                return this.json != null ? this.json.equals(jsonNode.json) : jsonNode.json == null;
            }
        }
    }

    public static class NullNode extends ValueNode {

        private NullNode() {
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Void.class;
        }

        @Override
        public boolean isNullNode() {
            return true;
        }

        @Override
        public ValueNodes.NullNode asNullNode() {
            return this;
        }

        public String toString() {
            return "null";
        }

        public boolean equals(Object o) {
            return this == o ? true : o instanceof ValueNodes.NullNode;
        }
    }

    public static class NumberNode extends ValueNode {

        public static ValueNodes.NumberNode NAN = new ValueNodes.NumberNode((BigDecimal) null);

        private final BigDecimal number;

        NumberNode(BigDecimal number) {
            this.number = number;
        }

        NumberNode(CharSequence num) {
            this.number = new BigDecimal(num.toString());
        }

        @Override
        public ValueNodes.StringNode asStringNode() {
            return new ValueNodes.StringNode(this.number.toString(), false);
        }

        public BigDecimal getNumber() {
            return this.number;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Number.class;
        }

        @Override
        public boolean isNumberNode() {
            return true;
        }

        @Override
        public ValueNodes.NumberNode asNumberNode() {
            return this;
        }

        public String toString() {
            return this.number.toString();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.NumberNode) && !(o instanceof ValueNodes.StringNode)) {
                return false;
            } else {
                ValueNodes.NumberNode that = ((ValueNode) o).asNumberNode();
                return that == NAN ? false : this.number.compareTo(that.number) == 0;
            }
        }
    }

    public static class OffsetDateTimeNode extends ValueNode {

        private final OffsetDateTime dateTime;

        OffsetDateTimeNode(OffsetDateTime dateTime) {
            this.dateTime = dateTime;
        }

        OffsetDateTimeNode(CharSequence date) {
            this.dateTime = OffsetDateTime.parse(date);
        }

        @Override
        public ValueNodes.StringNode asStringNode() {
            return new ValueNodes.StringNode(this.dateTime.toString(), false);
        }

        public OffsetDateTime getDate() {
            return this.dateTime;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return ValueNodes.OffsetDateTimeNode.class;
        }

        @Override
        public boolean isOffsetDateTimeNode() {
            return true;
        }

        @Override
        public ValueNodes.OffsetDateTimeNode asOffsetDateTimeNode() {
            return this;
        }

        public String toString() {
            return this.dateTime.toString();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.OffsetDateTimeNode) && !(o instanceof ValueNodes.StringNode)) {
                return false;
            } else {
                ValueNodes.OffsetDateTimeNode that = ((ValueNode) o).asOffsetDateTimeNode();
                return this.dateTime.compareTo(that.dateTime) == 0;
            }
        }
    }

    public static class PathNode extends ValueNode {

        private static final Logger logger = LoggerFactory.getLogger(ValueNodes.PathNode.class);

        private final Path path;

        private final boolean existsCheck;

        private final boolean shouldExist;

        PathNode(Path path) {
            this(path, false, false);
        }

        PathNode(CharSequence charSequence, boolean existsCheck, boolean shouldExist) {
            this(PathCompiler.compile(charSequence.toString()), existsCheck, shouldExist);
        }

        PathNode(Path path, boolean existsCheck, boolean shouldExist) {
            this.path = path;
            this.existsCheck = existsCheck;
            this.shouldExist = shouldExist;
            logger.trace("PathNode {} existsCheck: {}", path, existsCheck);
        }

        public Path getPath() {
            return this.path;
        }

        public boolean isExistsCheck() {
            return this.existsCheck;
        }

        public boolean shouldExists() {
            return this.shouldExist;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Void.class;
        }

        @Override
        public boolean isPathNode() {
            return true;
        }

        @Override
        public ValueNodes.PathNode asPathNode() {
            return this;
        }

        public ValueNodes.PathNode asExistsCheck(boolean shouldExist) {
            return new ValueNodes.PathNode(this.path, true, shouldExist);
        }

        public String toString() {
            return this.existsCheck && !this.shouldExist ? Utils.concat("!", this.path.toString()) : this.path.toString();
        }

        public ValueNode evaluate(Predicate.PredicateContext ctx) {
            if (this.isExistsCheck()) {
                try {
                    Configuration c = Configuration.builder().jsonProvider(ctx.configuration().jsonProvider()).options(Option.REQUIRE_PROPERTIES).build();
                    Object result = this.path.evaluate(ctx.item(), ctx.root(), c).getValue(false);
                    return result == JsonProvider.UNDEFINED ? ValueNodes.FALSE : ValueNodes.TRUE;
                } catch (PathNotFoundException var4) {
                    return ValueNodes.FALSE;
                }
            } else {
                try {
                    Object res;
                    if (ctx instanceof PredicateContextImpl ctxi) {
                        res = ctxi.evaluate(this.path);
                    } else {
                        Object doc = this.path.isRootPath() ? ctx.root() : ctx.item();
                        res = this.path.evaluate(doc, ctx.root(), ctx.configuration()).getValue();
                    }
                    res = ctx.configuration().jsonProvider().unwrap(res);
                    if (res instanceof Number) {
                        return createNumberNode(res.toString());
                    } else if (res instanceof String) {
                        return createStringNode(res.toString(), false);
                    } else if (res instanceof Boolean) {
                        return createBooleanNode(res.toString());
                    } else if (res instanceof OffsetDateTime) {
                        return createOffsetDateTimeNode(res.toString());
                    } else if (res == null) {
                        return ValueNodes.NULL_NODE;
                    } else if (ctx.configuration().jsonProvider().isArray(res)) {
                        return ValueNode.createJsonNode(ctx.configuration().mappingProvider().map(res, List.class, ctx.configuration()));
                    } else if (ctx.configuration().jsonProvider().isMap(res)) {
                        return ValueNode.createJsonNode(ctx.configuration().mappingProvider().map(res, Map.class, ctx.configuration()));
                    } else {
                        throw new JsonPathException("Could not convert " + res.getClass().toString() + ":" + res.toString() + " to a ValueNode");
                    }
                } catch (PathNotFoundException var5) {
                    return ValueNodes.UNDEFINED;
                }
            }
        }
    }

    public static class PatternNode extends ValueNode {

        private final String pattern;

        private final Pattern compiledPattern;

        private final String flags;

        PatternNode(CharSequence charSequence) {
            String tmp = charSequence.toString();
            int begin = tmp.indexOf(47);
            int end = tmp.lastIndexOf(47);
            this.pattern = tmp.substring(begin + 1, end);
            int flagsIndex = end + 1;
            this.flags = tmp.length() > flagsIndex ? tmp.substring(flagsIndex) : "";
            this.compiledPattern = Pattern.compile(this.pattern, PatternFlag.parseFlags(this.flags.toCharArray()));
        }

        PatternNode(Pattern pattern) {
            this.pattern = pattern.pattern();
            this.compiledPattern = pattern;
            this.flags = PatternFlag.parseFlags(pattern.flags());
        }

        Pattern getCompiledPattern() {
            return this.compiledPattern;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return void.class;
        }

        @Override
        public boolean isPatternNode() {
            return true;
        }

        @Override
        public ValueNodes.PatternNode asPatternNode() {
            return this;
        }

        public String toString() {
            return !this.pattern.startsWith("/") ? "/" + this.pattern + "/" + this.flags : this.pattern;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.PatternNode that)) {
                return false;
            } else {
                return this.compiledPattern != null ? this.compiledPattern.equals(that.compiledPattern) : that.compiledPattern == null;
            }
        }
    }

    public static class PredicateNode extends ValueNode {

        private final Predicate predicate;

        public PredicateNode(Predicate predicate) {
            this.predicate = predicate;
        }

        public Predicate getPredicate() {
            return this.predicate;
        }

        @Override
        public ValueNodes.PredicateNode asPredicateNode() {
            return this;
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Void.class;
        }

        @Override
        public boolean isPredicateNode() {
            return true;
        }

        public boolean equals(Object o) {
            return false;
        }

        public String toString() {
            return this.predicate.toString();
        }
    }

    public static class StringNode extends ValueNode {

        private final String string;

        private boolean useSingleQuote = true;

        StringNode(CharSequence charSequence, boolean escape) {
            if (escape && charSequence.length() > 1) {
                char open = charSequence.charAt(0);
                char close = charSequence.charAt(charSequence.length() - 1);
                if (open == '\'' && close == '\'') {
                    charSequence = charSequence.subSequence(1, charSequence.length() - 1);
                } else if (open == '"' && close == '"') {
                    charSequence = charSequence.subSequence(1, charSequence.length() - 1);
                    this.useSingleQuote = false;
                }
                this.string = Utils.unescape(charSequence.toString());
            } else {
                this.string = charSequence.toString();
            }
        }

        @Override
        public ValueNodes.NumberNode asNumberNode() {
            BigDecimal number = null;
            try {
                number = new BigDecimal(this.string);
            } catch (NumberFormatException var3) {
                return ValueNodes.NumberNode.NAN;
            }
            return new ValueNodes.NumberNode(number);
        }

        public String getString() {
            return this.string;
        }

        public int length() {
            return this.getString().length();
        }

        public boolean isEmpty() {
            return this.getString().isEmpty();
        }

        public boolean contains(String str) {
            return this.getString().contains(str);
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return String.class;
        }

        @Override
        public boolean isStringNode() {
            return true;
        }

        @Override
        public ValueNodes.StringNode asStringNode() {
            return this;
        }

        public String toString() {
            String quote = this.useSingleQuote ? "'" : "\"";
            return quote + Utils.escape(this.string, true) + quote;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ValueNodes.StringNode) && !(o instanceof ValueNodes.NumberNode)) {
                return false;
            } else {
                ValueNodes.StringNode that = ((ValueNode) o).asStringNode();
                return this.string != null ? this.string.equals(that.getString()) : that.getString() == null;
            }
        }
    }

    public static class UndefinedNode extends ValueNode {

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return Void.class;
        }

        @Override
        public ValueNodes.UndefinedNode asUndefinedNode() {
            return this;
        }

        @Override
        public boolean isUndefinedNode() {
            return true;
        }

        public boolean equals(Object o) {
            return false;
        }
    }

    public static class ValueListNode extends ValueNode implements Iterable<ValueNode> {

        private List<ValueNode> nodes = new ArrayList();

        public ValueListNode(Collection<?> values) {
            for (Object value : values) {
                this.nodes.add(toValueNode(value));
            }
        }

        public boolean contains(ValueNode node) {
            return this.nodes.contains(node);
        }

        public boolean subsetof(ValueNodes.ValueListNode right) {
            for (ValueNode leftNode : this.nodes) {
                if (!right.nodes.contains(leftNode)) {
                    return false;
                }
            }
            return true;
        }

        public List<ValueNode> getNodes() {
            return Collections.unmodifiableList(this.nodes);
        }

        @Override
        public Class<?> type(Predicate.PredicateContext ctx) {
            return List.class;
        }

        @Override
        public boolean isValueListNode() {
            return true;
        }

        @Override
        public ValueNodes.ValueListNode asValueListNode() {
            return this;
        }

        public String toString() {
            return "[" + Utils.join(",", this.nodes) + "]";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                return !(o instanceof ValueNodes.ValueListNode that) ? false : this.nodes.equals(that.nodes);
            }
        }

        public Iterator<ValueNode> iterator() {
            return this.nodes.iterator();
        }
    }
}