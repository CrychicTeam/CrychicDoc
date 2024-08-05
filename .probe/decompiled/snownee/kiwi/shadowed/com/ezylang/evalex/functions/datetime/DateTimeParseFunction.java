package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion.DateTimeConverter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameters;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameters({ @FunctionParameter(name = "value"), @FunctionParameter(name = "parameters", isVarArg = true) })
public class DateTimeParseFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        String value = parameterValues[0].getStringValue();
        ZoneId zoneId = expression.getConfiguration().getZoneId();
        if (parameterValues.length > 1 && !parameterValues[1].isNullValue()) {
            zoneId = ZoneIdConverter.convert(functionToken, parameterValues[1].getStringValue());
        }
        List<DateTimeFormatter> formatters;
        if (parameterValues.length > 2) {
            formatters = new ArrayList();
            for (int i = 2; i < parameterValues.length; i++) {
                try {
                    formatters.add(DateTimeFormatter.ofPattern(parameterValues[i].getStringValue()));
                } catch (IllegalArgumentException var9) {
                    throw new EvaluationException(functionToken, String.format("Illegal date-time format in parameter %d: '%s'", i + 1, parameterValues[i].getStringValue()));
                }
            }
        } else {
            formatters = expression.getConfiguration().getDateTimeFormatters();
        }
        DateTimeConverter converter = new DateTimeConverter();
        Instant instant = converter.parseDateTime(value, zoneId, formatters);
        if (instant == null) {
            throw new EvaluationException(functionToken, String.format("Unable to parse date-time string '%s'", value));
        } else {
            return EvaluationValue.dateTimeValue(instant);
        }
    }
}