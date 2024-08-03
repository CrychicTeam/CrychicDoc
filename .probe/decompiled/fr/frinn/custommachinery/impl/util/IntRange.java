package fr.frinn.custommachinery.impl.util;

import com.mojang.serialization.DataResult;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class IntRange extends Range<Integer> {

    public static final NamedCodec<IntRange> CODEC = NamedCodec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(createFromString(s));
        } catch (IllegalArgumentException var2) {
            return DataResult.error(var2::getMessage);
        }
    }, Range::toString, "Integer range");

    private static final Map<String, IntRange> CACHE_SPEC = Collections.synchronizedMap(new WeakHashMap());

    private static final Restriction<Integer> EVERYTHING = new Restriction(null, false, null, false);

    public static final IntRange ALL = new IntRange(Collections.singletonList(EVERYTHING));

    private IntRange(List<Restriction<Integer>> restrictions) {
        super(restrictions);
    }

    public static IntRange createFromString(String spec) throws IllegalArgumentException {
        if (spec == null) {
            throw new IllegalArgumentException("Can't parse an integer range fromm a null String");
        } else if (!spec.isEmpty() && !spec.equals("*")) {
            IntRange cached = (IntRange) CACHE_SPEC.get(spec);
            if (cached != null) {
                return cached;
            } else {
                List<Restriction<Integer>> restrictions = new ArrayList();
                String process = spec;
                Integer upperBound = null;
                Integer lowerBound = null;
                while (process.startsWith("[") || process.startsWith("(")) {
                    int index1 = process.indexOf(41);
                    int index2 = process.indexOf(93);
                    int index = index2;
                    if ((index2 < 0 || index1 < index2) && index1 >= 0) {
                        index = index1;
                    }
                    if (index < 0) {
                        throw new IllegalArgumentException("Unbounded range: \"" + spec + "\"");
                    }
                    Restriction<Integer> restriction = parseRestriction(process.substring(0, index + 1));
                    if (lowerBound == null) {
                        lowerBound = (Integer) restriction.lowerBound();
                    }
                    if (upperBound != null && (restriction.lowerBound() == null || ((Integer) restriction.lowerBound()).compareTo(upperBound) < 0)) {
                        throw new IllegalArgumentException("Ranges overlap: \"" + spec + "\"");
                    }
                    restrictions.add(restriction);
                    upperBound = (Integer) restriction.upperBound();
                    process = process.substring(index + 1).trim();
                    if (process.startsWith(",")) {
                        process = process.substring(1).trim();
                    }
                }
                if (process.length() <= 0) {
                    cached = new IntRange(restrictions);
                    CACHE_SPEC.put(spec, cached);
                    return cached;
                } else if (restrictions.size() > 0) {
                    throw new IllegalArgumentException("Only fully-qualified sets allowed in multiple set scenario: \"" + spec + "\"");
                } else {
                    label38: try {
                        int bound = (int) Double.parseDouble(process);
                        restrictions.add(new Restriction(bound, true, bound, true));
                        break label38;
                    } catch (NumberFormatException var10) {
                        throw new IllegalArgumentException("Invalid integer range, \"" + process + "\" is not a number");
                    }
                }
            }
        } else {
            return ALL;
        }
    }

    public static IntRange of(Object o) throws IllegalArgumentException {
        if (o == null) {
            throw new IllegalArgumentException("Cannot build IntRange from null");
        } else if (o instanceof CharSequence string) {
            return createFromString(string.toString());
        } else if (o instanceof Number number) {
            return new IntRange(Collections.singletonList(new Restriction(number.intValue(), true, number.intValue(), true)));
        } else {
            throw new IllegalArgumentException("Cannot build IntRange from " + o);
        }
    }

    private static Restriction<Integer> parseRestriction(String spec) throws IllegalArgumentException {
        boolean lowerBoundInclusive = spec.startsWith("[");
        boolean upperBoundInclusive = spec.endsWith("]");
        String process = spec.substring(1, spec.length() - 1).trim();
        int index = process.indexOf(44);
        Restriction<Integer> restriction;
        if (index < 0) {
            if (!lowerBoundInclusive || !upperBoundInclusive) {
                throw new IllegalArgumentException("Single version must be surrounded by []: " + spec);
            }
            Integer version = Integer.parseInt(process);
            restriction = new Restriction(version, lowerBoundInclusive, version, upperBoundInclusive);
        } else {
            String lowerBound = process.substring(0, index).trim();
            String upperBound = process.substring(index + 1).trim();
            if (lowerBound.equals(upperBound)) {
                throw new IllegalArgumentException("Range cannot have identical boundaries: " + spec);
            }
            Integer lowerVersion = null;
            if (lowerBound.length() > 0) {
                lowerVersion = Integer.parseInt(lowerBound);
            }
            Integer upperVersion = null;
            if (upperBound.length() > 0) {
                upperVersion = Integer.parseInt(upperBound);
            }
            if (upperVersion != null && lowerVersion != null && upperVersion.compareTo(lowerVersion) < 0) {
                throw new IllegalArgumentException("Range defies version ordering: " + spec);
            }
            restriction = new Restriction(lowerVersion, lowerBoundInclusive, upperVersion, upperBoundInclusive);
        }
        return restriction;
    }
}