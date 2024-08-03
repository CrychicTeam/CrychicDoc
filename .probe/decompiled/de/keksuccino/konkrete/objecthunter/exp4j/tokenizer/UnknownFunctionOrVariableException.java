package de.keksuccino.konkrete.objecthunter.exp4j.tokenizer;

class UnknownFunctionOrVariableException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    private final String message;

    private final String expression;

    private final String token;

    private final int position;

    public UnknownFunctionOrVariableException(String expression, int position, int length) {
        this.expression = expression;
        this.token = token(expression, position, length);
        this.position = position;
        this.message = "Unknown function or variable '" + this.token + "' at pos " + position + " in expression '" + expression + "'";
    }

    private static String token(String expression, int position, int length) {
        int len = expression.length();
        int end = position + length - 1;
        if (len < end) {
            end = len;
        }
        return expression.substring(position, end);
    }

    public String getMessage() {
        return this.message;
    }

    public String getExpression() {
        return this.expression;
    }

    public String getToken() {
        return this.token;
    }

    public int getPosition() {
        return this.position;
    }
}