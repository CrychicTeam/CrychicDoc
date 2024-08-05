package dev.latvian.mods.rhino.ast;

public class NumberLiteral extends AstNode {

    private String value;

    private double number;

    public NumberLiteral() {
        this.type = 40;
    }

    public NumberLiteral(int pos) {
        super(pos);
        this.type = 40;
    }

    public NumberLiteral(int pos, int len) {
        super(pos, len);
        this.type = 40;
    }

    public NumberLiteral(int pos, String value) {
        super(pos);
        this.type = 40;
        this.setValue(value);
        this.setLength(value.length());
    }

    public NumberLiteral(int pos, String value, double number) {
        this(pos, value);
        this.setDouble(number);
    }

    public NumberLiteral(double number) {
        this.type = 40;
        this.setDouble(number);
        this.setValue(Double.toString(number));
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.assertNotNull(value);
        this.value = value;
    }

    public double getNumber() {
        return this.number;
    }

    public void setNumber(double value) {
        this.number = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}