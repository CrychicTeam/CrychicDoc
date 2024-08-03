package snownee.kiwi.shadowed.com.ezylang.evalex.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.ASTNode;

public final class EvaluationValue implements Comparable<EvaluationValue> {

    private static final Boolean NULL_BOOLEAN = null;

    private static final List<EvaluationValue> NULL_ARRAY = null;

    private static final Map<String, EvaluationValue> NULL_STRUCTURE = null;

    private final Object value;

    private final EvaluationValue.DataType dataType;

    @Deprecated(since = "3.1.0", forRemoval = true)
    public EvaluationValue(Object value) {
        this(value, ExpressionConfiguration.defaultConfiguration());
    }

    public EvaluationValue(Object value, ExpressionConfiguration configuration) {
        EvaluationValue converted = configuration.getEvaluationValueConverter().convertObject(value, configuration);
        this.value = converted.getValue();
        this.dataType = converted.getDataType();
    }

    private EvaluationValue(Object value, EvaluationValue.DataType dataType) {
        this.dataType = dataType;
        this.value = value;
    }

    public static EvaluationValue nullValue() {
        return new EvaluationValue(null, EvaluationValue.DataType.NULL);
    }

    public static EvaluationValue numberValue(BigDecimal value) {
        return new EvaluationValue(value, EvaluationValue.DataType.NUMBER);
    }

    public static EvaluationValue stringValue(String value) {
        return new EvaluationValue(value, EvaluationValue.DataType.STRING);
    }

    public static EvaluationValue booleanValue(Boolean value) {
        return new EvaluationValue(value, EvaluationValue.DataType.BOOLEAN);
    }

    public static EvaluationValue dateTimeValue(Instant value) {
        return new EvaluationValue(value, EvaluationValue.DataType.DATE_TIME);
    }

    public static EvaluationValue durationValue(Duration value) {
        return new EvaluationValue(value, EvaluationValue.DataType.DURATION);
    }

    public static EvaluationValue expressionNodeValue(ASTNode value) {
        return new EvaluationValue(value, EvaluationValue.DataType.EXPRESSION_NODE);
    }

    public static EvaluationValue arrayValue(List<?> value) {
        return new EvaluationValue(value, EvaluationValue.DataType.ARRAY);
    }

    public static EvaluationValue structureValue(Map<?, ?> value) {
        return new EvaluationValue(value, EvaluationValue.DataType.STRUCTURE);
    }

    @Deprecated(since = "3.1.0", forRemoval = true)
    public EvaluationValue(double value, MathContext mathContext) {
        this.dataType = EvaluationValue.DataType.NUMBER;
        this.value = new BigDecimal(Double.toString(value), mathContext);
    }

    public boolean isNumberValue() {
        return this.getDataType() == EvaluationValue.DataType.NUMBER;
    }

    public boolean isStringValue() {
        return this.getDataType() == EvaluationValue.DataType.STRING;
    }

    public boolean isBooleanValue() {
        return this.getDataType() == EvaluationValue.DataType.BOOLEAN;
    }

    public boolean isDateTimeValue() {
        return this.getDataType() == EvaluationValue.DataType.DATE_TIME;
    }

    public boolean isDurationValue() {
        return this.getDataType() == EvaluationValue.DataType.DURATION;
    }

    public boolean isArrayValue() {
        return this.getDataType() == EvaluationValue.DataType.ARRAY;
    }

    public boolean isStructureValue() {
        return this.getDataType() == EvaluationValue.DataType.STRUCTURE;
    }

    public boolean isExpressionNode() {
        return this.getDataType() == EvaluationValue.DataType.EXPRESSION_NODE;
    }

    public boolean isNullValue() {
        return this.getDataType() == EvaluationValue.DataType.NULL;
    }

    public static EvaluationValue numberOfString(String value, MathContext mathContext) {
        if (!value.startsWith("0x") && !value.startsWith("0X")) {
            return numberValue(new BigDecimal(value, mathContext));
        } else {
            BigInteger hexToInteger = new BigInteger(value.substring(2), 16);
            return numberValue(new BigDecimal(hexToInteger, mathContext));
        }
    }

    public BigDecimal getNumberValue() {
        switch(this.getDataType()) {
            case NUMBER:
                return (BigDecimal) this.value;
            case BOOLEAN:
                return Boolean.TRUE.equals(this.value) ? BigDecimal.ONE : BigDecimal.ZERO;
            case STRING:
                return Boolean.parseBoolean((String) this.value) ? BigDecimal.ONE : BigDecimal.ZERO;
            case NULL:
                return null;
            default:
                return BigDecimal.ZERO;
        }
    }

    public String getStringValue() {
        switch(this.getDataType()) {
            case NUMBER:
                return ((BigDecimal) this.value).toPlainString();
            case NULL:
                return null;
            default:
                return this.value.toString();
        }
    }

    public Boolean getBooleanValue() {
        switch(this.getDataType()) {
            case NUMBER:
                return this.getNumberValue().compareTo(BigDecimal.ZERO) != 0;
            case BOOLEAN:
                return (Boolean) this.value;
            case STRING:
                return Boolean.parseBoolean((String) this.value);
            case NULL:
                return NULL_BOOLEAN;
            default:
                return false;
        }
    }

    public Instant getDateTimeValue() {
        try {
            switch(this.getDataType()) {
                case NUMBER:
                    return Instant.ofEpochMilli(((BigDecimal) this.value).longValue());
                case BOOLEAN:
                case NULL:
                default:
                    return Instant.EPOCH;
                case STRING:
                    return Instant.parse((String) this.value);
                case DATE_TIME:
                    return (Instant) this.value;
            }
        } catch (DateTimeException var2) {
            return Instant.EPOCH;
        }
    }

    public Duration getDurationValue() {
        try {
            switch(this.getDataType()) {
                case NUMBER:
                    return Duration.ofMillis(((BigDecimal) this.value).longValue());
                case STRING:
                    return Duration.parse((String) this.value);
                case DURATION:
                    return (Duration) this.value;
                default:
                    return Duration.ZERO;
            }
        } catch (DateTimeException var2) {
            return Duration.ZERO;
        }
    }

    public List<EvaluationValue> getArrayValue() {
        if (this.isArrayValue()) {
            return (List<EvaluationValue>) this.value;
        } else {
            return this.isNullValue() ? NULL_ARRAY : Collections.emptyList();
        }
    }

    public Map<String, EvaluationValue> getStructureValue() {
        if (this.isStructureValue()) {
            return (Map<String, EvaluationValue>) this.value;
        } else {
            return this.isNullValue() ? NULL_STRUCTURE : Collections.emptyMap();
        }
    }

    public ASTNode getExpressionNode() {
        return this.isExpressionNode() ? (ASTNode) this.getValue() : null;
    }

    public int compareTo(EvaluationValue toCompare) {
        switch(this.getDataType()) {
            case NUMBER:
                return this.getNumberValue().compareTo(toCompare.getNumberValue());
            case BOOLEAN:
                return this.getBooleanValue().compareTo(toCompare.getBooleanValue());
            case STRING:
            default:
                return this.getStringValue().compareTo(toCompare.getStringValue());
            case NULL:
                throw new NullPointerException("Can not compare a null value");
            case DATE_TIME:
                return this.getDateTimeValue().compareTo(toCompare.getDateTimeValue());
            case DURATION:
                return this.getDurationValue().compareTo(toCompare.getDurationValue());
        }
    }

    @Generated
    public Object getValue() {
        return this.value;
    }

    @Generated
    public EvaluationValue.DataType getDataType() {
        return this.dataType;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof EvaluationValue)) {
            return false;
        } else {
            EvaluationValue other = (EvaluationValue) o;
            Object this$value = this.getValue();
            Object other$value = other.getValue();
            if (this$value == null ? other$value == null : this$value.equals(other$value)) {
                Object this$dataType = this.getDataType();
                Object other$dataType = other.getDataType();
                return this$dataType == null ? other$dataType == null : this$dataType.equals(other$dataType);
            } else {
                return false;
            }
        }
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        Object $dataType = this.getDataType();
        return result * 59 + ($dataType == null ? 43 : $dataType.hashCode());
    }

    @Generated
    public String toString() {
        return "EvaluationValue(value=" + this.getValue() + ", dataType=" + this.getDataType() + ")";
    }

    public static enum DataType {

        STRING,
        NUMBER,
        BOOLEAN,
        DATE_TIME,
        DURATION,
        ARRAY,
        STRUCTURE,
        EXPRESSION_NODE,
        NULL
    }
}