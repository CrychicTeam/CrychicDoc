package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.TimeZone;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "values", isVarArg = true, nonNegative = true)
public class DateTimeNewFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        int parameterLength = parameterValues.length;
        if (parameterLength == 1) {
            BigDecimal millis = parameterValues[0].getNumberValue();
            return expression.convertValue(Instant.ofEpochMilli(millis.longValue()));
        } else {
            ZoneId zoneId = expression.getConfiguration().getZoneId();
            if (parameterValues[parameterLength - 1].isStringValue()) {
                zoneId = ZoneIdConverter.convert(functionToken, parameterValues[parameterLength - 1].getStringValue());
                parameterLength--;
            }
            int year = parameterValues[0].getNumberValue().intValue();
            int month = parameterValues[1].getNumberValue().intValue();
            int day = parameterValues[2].getNumberValue().intValue();
            int hour = parameterLength >= 4 ? parameterValues[3].getNumberValue().intValue() : 0;
            int minute = parameterLength >= 5 ? parameterValues[4].getNumberValue().intValue() : 0;
            int second = parameterLength >= 6 ? parameterValues[5].getNumberValue().intValue() : 0;
            int nanoOfs = parameterLength == 7 ? parameterValues[6].getNumberValue().intValue() : 0;
            return expression.convertValue(LocalDateTime.of(year, month, day, hour, minute, second, nanoOfs).atZone(zoneId).toInstant());
        }
    }

    @Override
    public void validatePreEvaluation(Token token, EvaluationValue... parameterValues) throws EvaluationException {
        super.validatePreEvaluation(token, parameterValues);
        int parameterLength = parameterValues.length;
        if (parameterLength == 1) {
            if (!parameterValues[0].isNumberValue()) {
                throw new EvaluationException(token, "Expected a number value for the time in milliseconds since the epoch");
            }
        } else {
            if (parameterValues[parameterLength - 1].isStringValue()) {
                if (!Set.of(TimeZone.getAvailableIDs()).contains(parameterValues[parameterLength - 1].getStringValue())) {
                    throw new EvaluationException(token, "Time zone with id '" + parameterValues[parameterLength - 1].getStringValue() + "' not found");
                }
                parameterLength--;
            }
            if (parameterLength < 3) {
                throw new EvaluationException(token, "A minimum of 3 parameters (year, month, day) is required");
            } else if (parameterLength > 7) {
                throw new EvaluationException(token, "Too many parameters to function");
            }
        }
    }
}