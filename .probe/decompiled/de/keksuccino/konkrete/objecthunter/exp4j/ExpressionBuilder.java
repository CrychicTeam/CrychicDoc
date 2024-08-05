package de.keksuccino.konkrete.objecthunter.exp4j;

import de.keksuccino.konkrete.objecthunter.exp4j.function.Function;
import de.keksuccino.konkrete.objecthunter.exp4j.function.Functions;
import de.keksuccino.konkrete.objecthunter.exp4j.operator.Operator;
import de.keksuccino.konkrete.objecthunter.exp4j.shuntingyard.ShuntingYard;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionBuilder {

    private final String expression;

    private final Map<String, Function> userFunctions;

    private final Map<String, Operator> userOperators;

    private final Set<String> variableNames;

    private boolean implicitMultiplication = true;

    public ExpressionBuilder(String expression) {
        if (expression != null && expression.trim().length() != 0) {
            this.expression = expression;
            this.userOperators = new HashMap(4);
            this.userFunctions = new HashMap(4);
            this.variableNames = new HashSet(4);
        } else {
            throw new IllegalArgumentException("Expression can not be empty");
        }
    }

    public ExpressionBuilder function(Function function) {
        this.userFunctions.put(function.getName(), function);
        return this;
    }

    public ExpressionBuilder functions(Function... functions) {
        for (Function f : functions) {
            this.userFunctions.put(f.getName(), f);
        }
        return this;
    }

    public ExpressionBuilder functions(List<Function> functions) {
        for (Function f : functions) {
            this.userFunctions.put(f.getName(), f);
        }
        return this;
    }

    public ExpressionBuilder variables(Set<String> variableNames) {
        this.variableNames.addAll(variableNames);
        return this;
    }

    public ExpressionBuilder variables(String... variableNames) {
        Collections.addAll(this.variableNames, variableNames);
        return this;
    }

    public ExpressionBuilder variable(String variableName) {
        this.variableNames.add(variableName);
        return this;
    }

    public ExpressionBuilder implicitMultiplication(boolean enabled) {
        this.implicitMultiplication = enabled;
        return this;
    }

    public ExpressionBuilder operator(Operator operator) {
        this.checkOperatorSymbol(operator);
        this.userOperators.put(operator.getSymbol(), operator);
        return this;
    }

    private void checkOperatorSymbol(Operator op) {
        String name = op.getSymbol();
        for (char ch : name.toCharArray()) {
            if (!Operator.isAllowedOperatorChar(ch)) {
                throw new IllegalArgumentException("The operator symbol '" + name + "' is invalid");
            }
        }
    }

    public ExpressionBuilder operator(Operator... operators) {
        for (Operator o : operators) {
            this.operator(o);
        }
        return this;
    }

    public ExpressionBuilder operator(List<Operator> operators) {
        for (Operator o : operators) {
            this.operator(o);
        }
        return this;
    }

    public Expression build() {
        if (this.expression.length() == 0) {
            throw new IllegalArgumentException("The expression can not be empty");
        } else {
            this.variableNames.add("pi");
            this.variableNames.add("π");
            this.variableNames.add("e");
            this.variableNames.add("φ");
            for (String var : this.variableNames) {
                if (Functions.getBuiltinFunction(var) != null || this.userFunctions.containsKey(var)) {
                    throw new IllegalArgumentException("A variable can not have the same name as a function [" + var + "]");
                }
            }
            return new Expression(ShuntingYard.convertToRPN(this.expression, this.userFunctions, this.userOperators, this.variableNames, this.implicitMultiplication), this.userFunctions.keySet());
        }
    }
}