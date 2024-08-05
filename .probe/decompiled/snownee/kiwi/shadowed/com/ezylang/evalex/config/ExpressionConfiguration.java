package snownee.kiwi.shadowed.com.ezylang.evalex.config;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Supplier;
import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.DataAccessorIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.MapBasedDataAccessor;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion.DefaultEvaluationValueConverter;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion.EvaluationValueConverterIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.AbsFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.CeilingFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.CoalesceFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.FactFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.FloorFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.IfFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.Log10Function;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.LogFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.MaxFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.MinFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.NotFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.RandomFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.RoundFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.SqrtFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic.SumFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DateTimeFormatFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DateTimeNewFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DateTimeNowFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DateTimeParseFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DateTimeToEpochFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DateTimeTodayFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DurationFromMillisFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DurationNewFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DurationParseFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime.DurationToMillisFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.string.StringContains;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.string.StringEndsWithFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.string.StringLowerFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.string.StringStartsWithFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.string.StringUpperFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AcosFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AcosHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AcosRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AcotFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AcotHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AcotRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AsinFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AsinHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AsinRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.Atan2Function;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.Atan2RFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AtanFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AtanHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.AtanRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CosFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CosHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CosRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CotFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CotHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CotRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CscFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CscHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.CscRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.DegFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.RadFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.SecFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.SecHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.SecRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.SinFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.SinHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.SinRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.TanFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.TanHFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric.TanRFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.OperatorIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.InfixDivisionOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.InfixMinusOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.InfixModuloOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.InfixMultiplicationOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.InfixPlusOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.InfixPowerOfOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.PrefixMinusOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.arithmetic.PrefixPlusOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixAndOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixEqualsOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixGreaterEqualsOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixGreaterOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixLessEqualsOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixLessOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixNotEqualsOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.InfixOrOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.booleans.PrefixNotOperator;

public class ExpressionConfiguration {

    public static final Map<String, EvaluationValue> StandardConstants = Collections.unmodifiableMap(getStandardConstants());

    public static final int DECIMAL_PLACES_ROUNDING_UNLIMITED = -1;

    public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(68, RoundingMode.HALF_EVEN);

    protected static final List<DateTimeFormatter> DEFAULT_DATE_TIME_FORMATTERS = new ArrayList(List.of(DateTimeFormatter.ISO_DATE_TIME, DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_LOCAL_DATE_TIME, DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.RFC_1123_DATE_TIME));

    private final OperatorDictionaryIfc operatorDictionary;

    private final FunctionDictionaryIfc functionDictionary;

    private final MathContext mathContext;

    private final Supplier<DataAccessorIfc> dataAccessorSupplier;

    private final Map<String, EvaluationValue> defaultConstants;

    private final boolean arraysAllowed;

    private final boolean structuresAllowed;

    private final boolean implicitMultiplicationAllowed;

    private final boolean singleQuoteStringLiteralsAllowed;

    private final int powerOfPrecedence;

    private final int decimalPlacesResult;

    private final int decimalPlacesRounding;

    private final boolean stripTrailingZeros;

    private final boolean allowOverwriteConstants;

    private final ZoneId zoneId;

    private final List<DateTimeFormatter> dateTimeFormatters;

    private final EvaluationValueConverterIfc evaluationValueConverter;

    public static ExpressionConfiguration defaultConfiguration() {
        return builder().build();
    }

    @SafeVarargs
    public final ExpressionConfiguration withAdditionalOperators(Entry<String, OperatorIfc>... operators) {
        Arrays.stream(operators).forEach(entry -> this.operatorDictionary.addOperator((String) entry.getKey(), (OperatorIfc) entry.getValue()));
        return this;
    }

    @SafeVarargs
    public final ExpressionConfiguration withAdditionalFunctions(Entry<String, FunctionIfc>... functions) {
        Arrays.stream(functions).forEach(entry -> this.functionDictionary.addFunction((String) entry.getKey(), (FunctionIfc) entry.getValue()));
        return this;
    }

    private static Map<String, EvaluationValue> getStandardConstants() {
        Map<String, EvaluationValue> constants = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        constants.put("TRUE", EvaluationValue.booleanValue(true));
        constants.put("FALSE", EvaluationValue.booleanValue(false));
        constants.put("PI", EvaluationValue.numberValue(new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")));
        constants.put("E", EvaluationValue.numberValue(new BigDecimal("2.71828182845904523536028747135266249775724709369995957496696762772407663")));
        constants.put("NULL", EvaluationValue.nullValue());
        constants.put("DT_FORMAT_ISO_DATE_TIME", EvaluationValue.stringValue("yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]['['VV']']"));
        constants.put("DT_FORMAT_LOCAL_DATE_TIME", EvaluationValue.stringValue("yyyy-MM-dd'T'HH:mm:ss[.SSS]"));
        constants.put("DT_FORMAT_LOCAL_DATE", EvaluationValue.stringValue("yyyy-MM-dd"));
        return constants;
    }

    @Generated
    private static OperatorDictionaryIfc $default$operatorDictionary() {
        return MapBasedOperatorDictionary.ofOperators(Map.entry("+", new PrefixPlusOperator()), Map.entry("-", new PrefixMinusOperator()), Map.entry("+", new InfixPlusOperator()), Map.entry("-", new InfixMinusOperator()), Map.entry("*", new InfixMultiplicationOperator()), Map.entry("/", new InfixDivisionOperator()), Map.entry("^", new InfixPowerOfOperator()), Map.entry("%", new InfixModuloOperator()), Map.entry("=", new InfixEqualsOperator()), Map.entry("==", new InfixEqualsOperator()), Map.entry("!=", new InfixNotEqualsOperator()), Map.entry("<>", new InfixNotEqualsOperator()), Map.entry(">", new InfixGreaterOperator()), Map.entry(">=", new InfixGreaterEqualsOperator()), Map.entry("<", new InfixLessOperator()), Map.entry("<=", new InfixLessEqualsOperator()), Map.entry("&&", new InfixAndOperator()), Map.entry("||", new InfixOrOperator()), Map.entry("!", new PrefixNotOperator()));
    }

    @Generated
    private static FunctionDictionaryIfc $default$functionDictionary() {
        return MapBasedFunctionDictionary.ofFunctions(Map.entry("ABS", new AbsFunction()), Map.entry("CEILING", new CeilingFunction()), Map.entry("COALESCE", new CoalesceFunction()), Map.entry("FACT", new FactFunction()), Map.entry("FLOOR", new FloorFunction()), Map.entry("IF", new IfFunction()), Map.entry("LOG", new LogFunction()), Map.entry("LOG10", new Log10Function()), Map.entry("MAX", new MaxFunction()), Map.entry("MIN", new MinFunction()), Map.entry("NOT", new NotFunction()), Map.entry("RANDOM", new RandomFunction()), Map.entry("ROUND", new RoundFunction()), Map.entry("SUM", new SumFunction()), Map.entry("SQRT", new SqrtFunction()), Map.entry("ACOS", new AcosFunction()), Map.entry("ACOSH", new AcosHFunction()), Map.entry("ACOSR", new AcosRFunction()), Map.entry("ACOT", new AcotFunction()), Map.entry("ACOTH", new AcotHFunction()), Map.entry("ACOTR", new AcotRFunction()), Map.entry("ASIN", new AsinFunction()), Map.entry("ASINH", new AsinHFunction()), Map.entry("ASINR", new AsinRFunction()), Map.entry("ATAN", new AtanFunction()), Map.entry("ATAN2", new Atan2Function()), Map.entry("ATAN2R", new Atan2RFunction()), Map.entry("ATANH", new AtanHFunction()), Map.entry("ATANR", new AtanRFunction()), Map.entry("COS", new CosFunction()), Map.entry("COSH", new CosHFunction()), Map.entry("COSR", new CosRFunction()), Map.entry("COT", new CotFunction()), Map.entry("COTH", new CotHFunction()), Map.entry("COTR", new CotRFunction()), Map.entry("CSC", new CscFunction()), Map.entry("CSCH", new CscHFunction()), Map.entry("CSCR", new CscRFunction()), Map.entry("DEG", new DegFunction()), Map.entry("RAD", new RadFunction()), Map.entry("SIN", new SinFunction()), Map.entry("SINH", new SinHFunction()), Map.entry("SINR", new SinRFunction()), Map.entry("SEC", new SecFunction()), Map.entry("SECH", new SecHFunction()), Map.entry("SECR", new SecRFunction()), Map.entry("TAN", new TanFunction()), Map.entry("TANH", new TanHFunction()), Map.entry("TANR", new TanRFunction()), Map.entry("STR_CONTAINS", new StringContains()), Map.entry("STR_ENDS_WITH", new StringEndsWithFunction()), Map.entry("STR_LOWER", new StringLowerFunction()), Map.entry("STR_STARTS_WITH", new StringStartsWithFunction()), Map.entry("STR_UPPER", new StringUpperFunction()), Map.entry("DT_DATE_NEW", new DateTimeNewFunction()), Map.entry("DT_DATE_PARSE", new DateTimeParseFunction()), Map.entry("DT_DATE_FORMAT", new DateTimeFormatFunction()), Map.entry("DT_DATE_TO_EPOCH", new DateTimeToEpochFunction()), Map.entry("DT_DURATION_NEW", new DurationNewFunction()), Map.entry("DT_DURATION_FROM_MILLIS", new DurationFromMillisFunction()), Map.entry("DT_DURATION_TO_MILLIS", new DurationToMillisFunction()), Map.entry("DT_DURATION_PARSE", new DurationParseFunction()), Map.entry("DT_NOW", new DateTimeNowFunction()), Map.entry("DT_TODAY", new DateTimeTodayFunction()));
    }

    @Generated
    private static MathContext $default$mathContext() {
        return DEFAULT_MATH_CONTEXT;
    }

    @Generated
    private static Supplier<DataAccessorIfc> $default$dataAccessorSupplier() {
        return MapBasedDataAccessor::new;
    }

    @Generated
    private static Map<String, EvaluationValue> $default$defaultConstants() {
        return getStandardConstants();
    }

    @Generated
    private static boolean $default$arraysAllowed() {
        return true;
    }

    @Generated
    private static boolean $default$structuresAllowed() {
        return true;
    }

    @Generated
    private static boolean $default$implicitMultiplicationAllowed() {
        return true;
    }

    @Generated
    private static boolean $default$singleQuoteStringLiteralsAllowed() {
        return false;
    }

    @Generated
    private static int $default$powerOfPrecedence() {
        return 40;
    }

    @Generated
    private static int $default$decimalPlacesResult() {
        return -1;
    }

    @Generated
    private static int $default$decimalPlacesRounding() {
        return -1;
    }

    @Generated
    private static boolean $default$stripTrailingZeros() {
        return true;
    }

    @Generated
    private static boolean $default$allowOverwriteConstants() {
        return true;
    }

    @Generated
    private static ZoneId $default$zoneId() {
        return ZoneId.systemDefault();
    }

    @Generated
    private static List<DateTimeFormatter> $default$dateTimeFormatters() {
        return DEFAULT_DATE_TIME_FORMATTERS;
    }

    @Generated
    private static EvaluationValueConverterIfc $default$evaluationValueConverter() {
        return new DefaultEvaluationValueConverter();
    }

    @Generated
    ExpressionConfiguration(OperatorDictionaryIfc operatorDictionary, FunctionDictionaryIfc functionDictionary, MathContext mathContext, Supplier<DataAccessorIfc> dataAccessorSupplier, Map<String, EvaluationValue> defaultConstants, boolean arraysAllowed, boolean structuresAllowed, boolean implicitMultiplicationAllowed, boolean singleQuoteStringLiteralsAllowed, int powerOfPrecedence, int decimalPlacesResult, int decimalPlacesRounding, boolean stripTrailingZeros, boolean allowOverwriteConstants, ZoneId zoneId, List<DateTimeFormatter> dateTimeFormatters, EvaluationValueConverterIfc evaluationValueConverter) {
        this.operatorDictionary = operatorDictionary;
        this.functionDictionary = functionDictionary;
        this.mathContext = mathContext;
        this.dataAccessorSupplier = dataAccessorSupplier;
        this.defaultConstants = defaultConstants;
        this.arraysAllowed = arraysAllowed;
        this.structuresAllowed = structuresAllowed;
        this.implicitMultiplicationAllowed = implicitMultiplicationAllowed;
        this.singleQuoteStringLiteralsAllowed = singleQuoteStringLiteralsAllowed;
        this.powerOfPrecedence = powerOfPrecedence;
        this.decimalPlacesResult = decimalPlacesResult;
        this.decimalPlacesRounding = decimalPlacesRounding;
        this.stripTrailingZeros = stripTrailingZeros;
        this.allowOverwriteConstants = allowOverwriteConstants;
        this.zoneId = zoneId;
        this.dateTimeFormatters = dateTimeFormatters;
        this.evaluationValueConverter = evaluationValueConverter;
    }

    @Generated
    public static ExpressionConfiguration.ExpressionConfigurationBuilder builder() {
        return new ExpressionConfiguration.ExpressionConfigurationBuilder();
    }

    @Generated
    public ExpressionConfiguration.ExpressionConfigurationBuilder toBuilder() {
        return new ExpressionConfiguration.ExpressionConfigurationBuilder().operatorDictionary(this.operatorDictionary).functionDictionary(this.functionDictionary).mathContext(this.mathContext).dataAccessorSupplier(this.dataAccessorSupplier).defaultConstants(this.defaultConstants).arraysAllowed(this.arraysAllowed).structuresAllowed(this.structuresAllowed).implicitMultiplicationAllowed(this.implicitMultiplicationAllowed).singleQuoteStringLiteralsAllowed(this.singleQuoteStringLiteralsAllowed).powerOfPrecedence(this.powerOfPrecedence).decimalPlacesResult(this.decimalPlacesResult).decimalPlacesRounding(this.decimalPlacesRounding).stripTrailingZeros(this.stripTrailingZeros).allowOverwriteConstants(this.allowOverwriteConstants).zoneId(this.zoneId).dateTimeFormatters(this.dateTimeFormatters).evaluationValueConverter(this.evaluationValueConverter);
    }

    @Generated
    public OperatorDictionaryIfc getOperatorDictionary() {
        return this.operatorDictionary;
    }

    @Generated
    public FunctionDictionaryIfc getFunctionDictionary() {
        return this.functionDictionary;
    }

    @Generated
    public MathContext getMathContext() {
        return this.mathContext;
    }

    @Generated
    public Supplier<DataAccessorIfc> getDataAccessorSupplier() {
        return this.dataAccessorSupplier;
    }

    @Generated
    public Map<String, EvaluationValue> getDefaultConstants() {
        return this.defaultConstants;
    }

    @Generated
    public boolean isArraysAllowed() {
        return this.arraysAllowed;
    }

    @Generated
    public boolean isStructuresAllowed() {
        return this.structuresAllowed;
    }

    @Generated
    public boolean isImplicitMultiplicationAllowed() {
        return this.implicitMultiplicationAllowed;
    }

    @Generated
    public boolean isSingleQuoteStringLiteralsAllowed() {
        return this.singleQuoteStringLiteralsAllowed;
    }

    @Generated
    public int getPowerOfPrecedence() {
        return this.powerOfPrecedence;
    }

    @Generated
    public int getDecimalPlacesResult() {
        return this.decimalPlacesResult;
    }

    @Generated
    public int getDecimalPlacesRounding() {
        return this.decimalPlacesRounding;
    }

    @Generated
    public boolean isStripTrailingZeros() {
        return this.stripTrailingZeros;
    }

    @Generated
    public boolean isAllowOverwriteConstants() {
        return this.allowOverwriteConstants;
    }

    @Generated
    public ZoneId getZoneId() {
        return this.zoneId;
    }

    @Generated
    public List<DateTimeFormatter> getDateTimeFormatters() {
        return this.dateTimeFormatters;
    }

    @Generated
    public EvaluationValueConverterIfc getEvaluationValueConverter() {
        return this.evaluationValueConverter;
    }

    @Generated
    public static class ExpressionConfigurationBuilder {

        @Generated
        private boolean operatorDictionary$set;

        @Generated
        private OperatorDictionaryIfc operatorDictionary$value;

        @Generated
        private boolean functionDictionary$set;

        @Generated
        private FunctionDictionaryIfc functionDictionary$value;

        @Generated
        private boolean mathContext$set;

        @Generated
        private MathContext mathContext$value;

        @Generated
        private boolean dataAccessorSupplier$set;

        @Generated
        private Supplier<DataAccessorIfc> dataAccessorSupplier$value;

        @Generated
        private boolean defaultConstants$set;

        @Generated
        private Map<String, EvaluationValue> defaultConstants$value;

        @Generated
        private boolean arraysAllowed$set;

        @Generated
        private boolean arraysAllowed$value;

        @Generated
        private boolean structuresAllowed$set;

        @Generated
        private boolean structuresAllowed$value;

        @Generated
        private boolean implicitMultiplicationAllowed$set;

        @Generated
        private boolean implicitMultiplicationAllowed$value;

        @Generated
        private boolean singleQuoteStringLiteralsAllowed$set;

        @Generated
        private boolean singleQuoteStringLiteralsAllowed$value;

        @Generated
        private boolean powerOfPrecedence$set;

        @Generated
        private int powerOfPrecedence$value;

        @Generated
        private boolean decimalPlacesResult$set;

        @Generated
        private int decimalPlacesResult$value;

        @Generated
        private boolean decimalPlacesRounding$set;

        @Generated
        private int decimalPlacesRounding$value;

        @Generated
        private boolean stripTrailingZeros$set;

        @Generated
        private boolean stripTrailingZeros$value;

        @Generated
        private boolean allowOverwriteConstants$set;

        @Generated
        private boolean allowOverwriteConstants$value;

        @Generated
        private boolean zoneId$set;

        @Generated
        private ZoneId zoneId$value;

        @Generated
        private boolean dateTimeFormatters$set;

        @Generated
        private List<DateTimeFormatter> dateTimeFormatters$value;

        @Generated
        private boolean evaluationValueConverter$set;

        @Generated
        private EvaluationValueConverterIfc evaluationValueConverter$value;

        @Generated
        ExpressionConfigurationBuilder() {
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder operatorDictionary(OperatorDictionaryIfc operatorDictionary) {
            this.operatorDictionary$value = operatorDictionary;
            this.operatorDictionary$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder functionDictionary(FunctionDictionaryIfc functionDictionary) {
            this.functionDictionary$value = functionDictionary;
            this.functionDictionary$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder mathContext(MathContext mathContext) {
            this.mathContext$value = mathContext;
            this.mathContext$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder dataAccessorSupplier(Supplier<DataAccessorIfc> dataAccessorSupplier) {
            this.dataAccessorSupplier$value = dataAccessorSupplier;
            this.dataAccessorSupplier$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder defaultConstants(Map<String, EvaluationValue> defaultConstants) {
            this.defaultConstants$value = defaultConstants;
            this.defaultConstants$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder arraysAllowed(boolean arraysAllowed) {
            this.arraysAllowed$value = arraysAllowed;
            this.arraysAllowed$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder structuresAllowed(boolean structuresAllowed) {
            this.structuresAllowed$value = structuresAllowed;
            this.structuresAllowed$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder implicitMultiplicationAllowed(boolean implicitMultiplicationAllowed) {
            this.implicitMultiplicationAllowed$value = implicitMultiplicationAllowed;
            this.implicitMultiplicationAllowed$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder singleQuoteStringLiteralsAllowed(boolean singleQuoteStringLiteralsAllowed) {
            this.singleQuoteStringLiteralsAllowed$value = singleQuoteStringLiteralsAllowed;
            this.singleQuoteStringLiteralsAllowed$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder powerOfPrecedence(int powerOfPrecedence) {
            this.powerOfPrecedence$value = powerOfPrecedence;
            this.powerOfPrecedence$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder decimalPlacesResult(int decimalPlacesResult) {
            this.decimalPlacesResult$value = decimalPlacesResult;
            this.decimalPlacesResult$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder decimalPlacesRounding(int decimalPlacesRounding) {
            this.decimalPlacesRounding$value = decimalPlacesRounding;
            this.decimalPlacesRounding$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder stripTrailingZeros(boolean stripTrailingZeros) {
            this.stripTrailingZeros$value = stripTrailingZeros;
            this.stripTrailingZeros$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder allowOverwriteConstants(boolean allowOverwriteConstants) {
            this.allowOverwriteConstants$value = allowOverwriteConstants;
            this.allowOverwriteConstants$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder zoneId(ZoneId zoneId) {
            this.zoneId$value = zoneId;
            this.zoneId$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder dateTimeFormatters(List<DateTimeFormatter> dateTimeFormatters) {
            this.dateTimeFormatters$value = dateTimeFormatters;
            this.dateTimeFormatters$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration.ExpressionConfigurationBuilder evaluationValueConverter(EvaluationValueConverterIfc evaluationValueConverter) {
            this.evaluationValueConverter$value = evaluationValueConverter;
            this.evaluationValueConverter$set = true;
            return this;
        }

        @Generated
        public ExpressionConfiguration build() {
            OperatorDictionaryIfc operatorDictionary$value = this.operatorDictionary$value;
            if (!this.operatorDictionary$set) {
                operatorDictionary$value = ExpressionConfiguration.$default$operatorDictionary();
            }
            FunctionDictionaryIfc functionDictionary$value = this.functionDictionary$value;
            if (!this.functionDictionary$set) {
                functionDictionary$value = ExpressionConfiguration.$default$functionDictionary();
            }
            MathContext mathContext$value = this.mathContext$value;
            if (!this.mathContext$set) {
                mathContext$value = ExpressionConfiguration.$default$mathContext();
            }
            Supplier<DataAccessorIfc> dataAccessorSupplier$value = this.dataAccessorSupplier$value;
            if (!this.dataAccessorSupplier$set) {
                dataAccessorSupplier$value = ExpressionConfiguration.$default$dataAccessorSupplier();
            }
            Map<String, EvaluationValue> defaultConstants$value = this.defaultConstants$value;
            if (!this.defaultConstants$set) {
                defaultConstants$value = ExpressionConfiguration.$default$defaultConstants();
            }
            boolean arraysAllowed$value = this.arraysAllowed$value;
            if (!this.arraysAllowed$set) {
                arraysAllowed$value = ExpressionConfiguration.$default$arraysAllowed();
            }
            boolean structuresAllowed$value = this.structuresAllowed$value;
            if (!this.structuresAllowed$set) {
                structuresAllowed$value = ExpressionConfiguration.$default$structuresAllowed();
            }
            boolean implicitMultiplicationAllowed$value = this.implicitMultiplicationAllowed$value;
            if (!this.implicitMultiplicationAllowed$set) {
                implicitMultiplicationAllowed$value = ExpressionConfiguration.$default$implicitMultiplicationAllowed();
            }
            boolean singleQuoteStringLiteralsAllowed$value = this.singleQuoteStringLiteralsAllowed$value;
            if (!this.singleQuoteStringLiteralsAllowed$set) {
                singleQuoteStringLiteralsAllowed$value = ExpressionConfiguration.$default$singleQuoteStringLiteralsAllowed();
            }
            int powerOfPrecedence$value = this.powerOfPrecedence$value;
            if (!this.powerOfPrecedence$set) {
                powerOfPrecedence$value = ExpressionConfiguration.$default$powerOfPrecedence();
            }
            int decimalPlacesResult$value = this.decimalPlacesResult$value;
            if (!this.decimalPlacesResult$set) {
                decimalPlacesResult$value = ExpressionConfiguration.$default$decimalPlacesResult();
            }
            int decimalPlacesRounding$value = this.decimalPlacesRounding$value;
            if (!this.decimalPlacesRounding$set) {
                decimalPlacesRounding$value = ExpressionConfiguration.$default$decimalPlacesRounding();
            }
            boolean stripTrailingZeros$value = this.stripTrailingZeros$value;
            if (!this.stripTrailingZeros$set) {
                stripTrailingZeros$value = ExpressionConfiguration.$default$stripTrailingZeros();
            }
            boolean allowOverwriteConstants$value = this.allowOverwriteConstants$value;
            if (!this.allowOverwriteConstants$set) {
                allowOverwriteConstants$value = ExpressionConfiguration.$default$allowOverwriteConstants();
            }
            ZoneId zoneId$value = this.zoneId$value;
            if (!this.zoneId$set) {
                zoneId$value = ExpressionConfiguration.$default$zoneId();
            }
            List<DateTimeFormatter> dateTimeFormatters$value = this.dateTimeFormatters$value;
            if (!this.dateTimeFormatters$set) {
                dateTimeFormatters$value = ExpressionConfiguration.$default$dateTimeFormatters();
            }
            EvaluationValueConverterIfc evaluationValueConverter$value = this.evaluationValueConverter$value;
            if (!this.evaluationValueConverter$set) {
                evaluationValueConverter$value = ExpressionConfiguration.$default$evaluationValueConverter();
            }
            return new ExpressionConfiguration(operatorDictionary$value, functionDictionary$value, mathContext$value, dataAccessorSupplier$value, defaultConstants$value, arraysAllowed$value, structuresAllowed$value, implicitMultiplicationAllowed$value, singleQuoteStringLiteralsAllowed$value, powerOfPrecedence$value, decimalPlacesResult$value, decimalPlacesRounding$value, stripTrailingZeros$value, allowOverwriteConstants$value, zoneId$value, dateTimeFormatters$value, evaluationValueConverter$value);
        }

        @Generated
        public String toString() {
            return "ExpressionConfiguration.ExpressionConfigurationBuilder(operatorDictionary$value=" + this.operatorDictionary$value + ", functionDictionary$value=" + this.functionDictionary$value + ", mathContext$value=" + this.mathContext$value + ", dataAccessorSupplier$value=" + this.dataAccessorSupplier$value + ", defaultConstants$value=" + this.defaultConstants$value + ", arraysAllowed$value=" + this.arraysAllowed$value + ", structuresAllowed$value=" + this.structuresAllowed$value + ", implicitMultiplicationAllowed$value=" + this.implicitMultiplicationAllowed$value + ", singleQuoteStringLiteralsAllowed$value=" + this.singleQuoteStringLiteralsAllowed$value + ", powerOfPrecedence$value=" + this.powerOfPrecedence$value + ", decimalPlacesResult$value=" + this.decimalPlacesResult$value + ", decimalPlacesRounding$value=" + this.decimalPlacesRounding$value + ", stripTrailingZeros$value=" + this.stripTrailingZeros$value + ", allowOverwriteConstants$value=" + this.allowOverwriteConstants$value + ", zoneId$value=" + this.zoneId$value + ", dateTimeFormatters$value=" + this.dateTimeFormatters$value + ", evaluationValueConverter$value=" + this.evaluationValueConverter$value + ")";
        }
    }
}