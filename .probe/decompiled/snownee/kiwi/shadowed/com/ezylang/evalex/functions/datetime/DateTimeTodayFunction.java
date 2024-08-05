package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "parameters", isVarArg = true)
public class DateTimeTodayFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        ZoneId zoneId = this.parseZoneId(expression, functionToken, parameterValues);
        Instant today = LocalDate.now().atStartOfDay(zoneId).toInstant();
        return expression.convertValue(today);
    }

    private ZoneId parseZoneId(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        return parameterValues.length > 0 && !parameterValues[0].isNullValue() ? ZoneIdConverter.convert(functionToken, parameterValues[0].getStringValue()) : expression.getConfiguration().getZoneId();
    }
}