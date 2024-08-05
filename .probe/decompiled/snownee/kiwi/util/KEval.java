package snownee.kiwi.util;

import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.resources.ResourceLocation;
import snownee.kiwi.KiwiCommonConfig;
import snownee.kiwi.KiwiModules;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.config.ExpressionConfiguration;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.DataAccessorIfc;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.AbstractOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.operators.InfixOperator;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public class KEval {

    private static final ExpressionConfiguration CONFIG = ExpressionConfiguration.builder().defaultConstants(generateConstants()).dataAccessorSupplier(KEval.DataAccessor::new).singleQuoteStringLiteralsAllowed(true).build();

    private static Map<String, EvaluationValue> generateConstants() {
        Map<String, EvaluationValue> map = new TreeMap(ExpressionConfiguration.StandardConstants);
        map.put("MC", EvaluationValue.numberValue(new BigDecimal(Platform.getVersionNumber("minecraft"))));
        map.put("FORGELIKE", EvaluationValue.booleanValue(Platform.getPlatformSeries() == Platform.Type.Forge));
        map.put("FABRICLIKE", EvaluationValue.booleanValue(Platform.getPlatformSeries() == Platform.Type.Fabric));
        map.put("MODLOADER", EvaluationValue.stringValue(Platform.getPlatform().name()));
        return map;
    }

    public static ExpressionConfiguration config() {
        return CONFIG;
    }

    static {
        config().getFunctionDictionary().addFunction("HAS", new KEval.HasFunction());
        config().getFunctionDictionary().addFunction("VER", new KEval.VerFunction());
        config().getOperatorDictionary().addOperator("??", new KEval.NullishCoalescingOperator());
    }

    private static class DataAccessor implements DataAccessorIfc {

        @Override
        public EvaluationValue getData(String variable) {
            Object o = KiwiCommonConfig.vars.get(variable);
            return new EvaluationValue(o, KEval.config());
        }

        @Override
        public void setData(String variable, EvaluationValue value) {
        }
    }

    @FunctionParameter(name = "id")
    private static class HasFunction extends AbstractFunction {

        @Override
        public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
            String string = parameterValues[0].getStringValue();
            return string.startsWith("@") ? EvaluationValue.booleanValue(KiwiModules.isLoaded(new ResourceLocation(string.substring(1)))) : EvaluationValue.booleanValue(Platform.isModLoaded(string));
        }
    }

    @InfixOperator(precedence = 2)
    private static class NullishCoalescingOperator extends AbstractOperator {

        @Override
        public EvaluationValue evaluate(Expression expression, Token operatorToken, EvaluationValue... operands) throws EvaluationException {
            return operands[0].isNullValue() ? operands[1] : operands[0];
        }
    }

    @FunctionParameter(name = "id")
    private static class VerFunction extends AbstractFunction {

        private final Object2IntMap<String> cache = new Object2IntAVLTreeMap();

        @Override
        public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
            String string = parameterValues[0].getStringValue();
            return EvaluationValue.numberValue(new BigDecimal(this.cache.computeIfAbsent(string, Platform::getVersionNumber)));
        }
    }
}