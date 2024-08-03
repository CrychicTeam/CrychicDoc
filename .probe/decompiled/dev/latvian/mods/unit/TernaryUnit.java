package dev.latvian.mods.unit;

public class TernaryUnit extends Unit {

    public final Unit cond;

    public final Unit left;

    public final Unit right;

    public TernaryUnit(Unit cond, Unit left, Unit right) {
        this.cond = cond;
        this.left = left;
        this.right = right;
    }

    @Override
    public double get(UnitVariables variables) {
        return this.cond.getBoolean(variables) ? this.left.get(variables) : this.right.get(variables);
    }

    @Override
    public int getInt(UnitVariables variables) {
        return this.cond.getBoolean(variables) ? this.left.getInt(variables) : this.right.getInt(variables);
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this.cond.getBoolean(variables) ? this.left.getBoolean(variables) : this.right.getBoolean(variables);
    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append('(');
        this.cond.toString(builder);
        builder.append(" ? ");
        this.left.toString(builder);
        builder.append(" : ");
        this.right.toString(builder);
        builder.append(')');
    }
}