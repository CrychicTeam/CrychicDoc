package snownee.kiwi.shadowed.com.ezylang.evalex.data.conversion;

import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;

public class StringConverter implements ConverterIfc {

    @Override
    public EvaluationValue convert(Object object, ExpressionConfiguration configuration) {
        String string;
        if (object instanceof CharSequence) {
            string = ((CharSequence) object).toString();
        } else {
            if (!(object instanceof Character)) {
                throw this.illegalArgument(object);
            }
            string = ((Character) object).toString();
        }
        return EvaluationValue.stringValue(string);
    }

    @Override
    public boolean canConvert(Object object) {
        return object instanceof CharSequence || object instanceof Character;
    }
}