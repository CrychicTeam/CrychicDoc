package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameters;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameters({ @FunctionParameter(name = "value"), @FunctionParameter(name = "parameters", isVarArg = true) })
public class DateTimeFormatFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        DateTimeFormatter formatter = (DateTimeFormatter) expression.getConfiguration().getDateTimeFormatters().get(0);
        if (parameterValues.length > 1) {
            formatter = DateTimeFormatter.ofPattern(parameterValues[1].getStringValue());
        }
        ZoneId zoneId = expression.getConfiguration().getZoneId();
        if (parameterValues.length == 3) {
            zoneId = ZoneIdConverter.convert(functionToken, parameterValues[2].getStringValue());
        }
        return expression.convertValue(parameterValues[0].getDateTimeValue().atZone(zoneId).format(formatter));
    }

    @Override
    public void validatePreEvaluation(Token token, EvaluationValue... parameterValues) throws EvaluationException {
        super.validatePreEvaluation(token, parameterValues);
        if (parameterValues.length > 3) {
            throw new EvaluationException(token, "Too many parameters");
        } else if (!parameterValues[0].isDateTimeValue()) {
            throw new EvaluationException(token, String.format("Unable to format a '%s' type as a date-time", parameterValues[0].getDataType().name()));
        }
    }
}