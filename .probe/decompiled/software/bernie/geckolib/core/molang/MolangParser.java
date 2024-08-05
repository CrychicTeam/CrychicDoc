package software.bernie.geckolib.core.molang;

import com.eliotlash.mclib.math.Constant;
import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.MathBuilder;
import com.eliotlash.mclib.math.Variable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleSupplier;
import software.bernie.geckolib.core.molang.expressions.MolangCompoundValue;
import software.bernie.geckolib.core.molang.expressions.MolangValue;
import software.bernie.geckolib.core.molang.expressions.MolangVariableHolder;
import software.bernie.geckolib.core.molang.functions.CosDegrees;
import software.bernie.geckolib.core.molang.functions.SinDegrees;

public class MolangParser extends MathBuilder {

    public static final Map<String, LazyVariable> VARIABLES = new Object2ObjectOpenHashMap();

    public static final MolangVariableHolder ZERO = new MolangVariableHolder(null, new Constant(0.0));

    public static final MolangVariableHolder ONE = new MolangVariableHolder(null, new Constant(1.0));

    public static final String RETURN = "return ";

    public static final MolangParser INSTANCE = new MolangParser();

    private MolangParser() {
        this.doCoreRemaps();
        this.registerAdditionalVariables();
    }

    private void doCoreRemaps() {
        this.functions.put("cos", CosDegrees.class);
        this.functions.put("sin", SinDegrees.class);
        this.remap("abs", "math.abs");
        this.remap("acos", "math.acos");
        this.remap("asin", "math.asin");
        this.remap("atan", "math.atan");
        this.remap("atan2", "math.atan2");
        this.remap("ceil", "math.ceil");
        this.remap("clamp", "math.clamp");
        this.remap("cos", "math.cos");
        this.remap("die_roll", "math.die_roll");
        this.remap("die_roll_integer", "math.die_roll_integer");
        this.remap("exp", "math.exp");
        this.remap("floor", "math.floor");
        this.remap("hermite_blend", "math.hermite_blend");
        this.remap("lerp", "math.lerp");
        this.remap("lerprotate", "math.lerprotate");
        this.remap("ln", "math.ln");
        this.remap("max", "math.max");
        this.remap("min", "math.min");
        this.remap("mod", "math.mod");
        this.remap("pi", "math.pi");
        this.remap("pow", "math.pow");
        this.remap("random", "math.random");
        this.remap("random_integer", "math.random_integer");
        this.remap("round", "math.round");
        this.remap("sin", "math.sin");
        this.remap("sqrt", "math.sqrt");
        this.remap("trunc", "math.trunc");
    }

    private void registerAdditionalVariables() {
        this.register(new LazyVariable("query.anim_time", 0.0));
        this.register(new LazyVariable("query.life_time", 0.0));
        this.register(new LazyVariable("query.actor_count", 0.0));
        this.register(new LazyVariable("query.health", 0.0));
        this.register(new LazyVariable("query.max_health", 0.0));
        this.register(new LazyVariable("query.distance_from_camera", 0.0));
        this.register(new LazyVariable("query.yaw_speed", 0.0));
        this.register(new LazyVariable("query.is_in_water_or_rain", 0.0));
        this.register(new LazyVariable("query.is_in_water", 0.0));
        this.register(new LazyVariable("query.is_on_ground", 0.0));
        this.register(new LazyVariable("query.time_of_day", 0.0));
        this.register(new LazyVariable("query.is_on_fire", 0.0));
        this.register(new LazyVariable("query.ground_speed", 0.0));
    }

    public void register(Variable variable) {
        if (!(variable instanceof LazyVariable)) {
            variable = LazyVariable.from(variable);
        }
        VARIABLES.put(variable.getName(), (LazyVariable) variable);
    }

    public void remap(String old, String newName) {
        this.functions.put(newName, (Class) this.functions.remove(old));
    }

    public void setValue(String name, DoubleSupplier value) {
        this.getVariable(name).set(value);
    }

    public void setMemoizedValue(String name, final DoubleSupplier value) {
        this.getVariable(name).set(new DoubleSupplier() {

            private final DoubleSupplier supplier = value;

            private double computedValue = Double.MIN_VALUE;

            public double getAsDouble() {
                if (this.computedValue == Double.MIN_VALUE) {
                    this.computedValue = this.supplier.getAsDouble();
                }
                return this.computedValue;
            }
        });
    }

    public LazyVariable getVariable(String name) {
        return (LazyVariable) VARIABLES.computeIfAbsent(name, key -> new LazyVariable(key, 0.0));
    }

    public LazyVariable getVariable(String name, MolangCompoundValue currentStatement) {
        if (currentStatement != null) {
            LazyVariable variable = (LazyVariable) currentStatement.locals.get(name);
            if (variable != null) {
                return variable;
            }
        }
        return this.getVariable(name);
    }

    public static MolangValue parseJson(JsonElement element) throws MolangException {
        if (!element.isJsonPrimitive()) {
            return ZERO;
        } else {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return new MolangValue(new Constant(primitive.getAsDouble()));
            } else if (primitive.isString()) {
                String string = primitive.getAsString();
                try {
                    return new MolangValue(new Constant(Double.parseDouble(string)));
                } catch (NumberFormatException var4) {
                    return parseExpression(string);
                }
            } else {
                return ZERO;
            }
        }
    }

    public static MolangValue parseExpression(String expression) throws MolangException {
        MolangCompoundValue result = null;
        for (String split : expression.toLowerCase().trim().split(";")) {
            String trimmed = split.trim();
            if (!trimmed.isEmpty()) {
                if (result == null) {
                    result = new MolangCompoundValue(parseOneLine(trimmed, result));
                } else {
                    result.values.add(parseOneLine(trimmed, result));
                }
            }
        }
        if (result == null) {
            throw new MolangException("Molang expression cannot be blank!");
        } else {
            return result;
        }
    }

    protected static MolangValue parseOneLine(String expression, MolangCompoundValue currentStatement) throws MolangException {
        if (expression.startsWith("return ")) {
            try {
                return new MolangValue(INSTANCE.parse(expression.substring("return ".length())), true);
            } catch (Exception var5) {
                throw new MolangException("Couldn't parse return '" + expression + "' expression!");
            }
        } else {
            try {
                List<Object> symbols = INSTANCE.breakdownChars(INSTANCE.breakdown(expression));
                if (symbols.size() >= 3 && symbols.get(0) instanceof String name && INSTANCE.isVariable(symbols.get(0)) && symbols.get(1).equals("=")) {
                    symbols = symbols.subList(2, symbols.size());
                    LazyVariable variable;
                    if (!VARIABLES.containsKey(name) && !currentStatement.locals.containsKey(name)) {
                        currentStatement.locals.put(name, variable = new LazyVariable(name, 0.0));
                    } else {
                        variable = INSTANCE.getVariable(name, currentStatement);
                    }
                    return new MolangVariableHolder(variable, INSTANCE.parseSymbolsMolang(symbols));
                } else {
                    return new MolangValue(INSTANCE.parseSymbolsMolang(symbols));
                }
            } catch (Exception var6) {
                throw new MolangException("Couldn't parse '" + expression + "' expression!");
            }
        }
    }

    private IValue parseSymbolsMolang(List<Object> symbols) throws MolangException {
        try {
            return this.parseSymbols(symbols);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new MolangException("Couldn't parse an expression!");
        }
    }

    protected boolean isOperator(String s) {
        return super.isOperator(s) || s.equals("=");
    }
}