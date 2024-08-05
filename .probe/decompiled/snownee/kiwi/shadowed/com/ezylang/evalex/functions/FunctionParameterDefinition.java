package snownee.kiwi.shadowed.com.ezylang.evalex.functions;

import lombok.Generated;

public final class FunctionParameterDefinition {

    private final String name;

    private final boolean isVarArg;

    private final boolean isLazy;

    private final boolean nonZero;

    private final boolean nonNegative;

    @Generated
    FunctionParameterDefinition(String name, boolean isVarArg, boolean isLazy, boolean nonZero, boolean nonNegative) {
        this.name = name;
        this.isVarArg = isVarArg;
        this.isLazy = isLazy;
        this.nonZero = nonZero;
        this.nonNegative = nonNegative;
    }

    @Generated
    public static FunctionParameterDefinition.FunctionParameterDefinitionBuilder builder() {
        return new FunctionParameterDefinition.FunctionParameterDefinitionBuilder();
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public boolean isVarArg() {
        return this.isVarArg;
    }

    @Generated
    public boolean isLazy() {
        return this.isLazy;
    }

    @Generated
    public boolean isNonZero() {
        return this.nonZero;
    }

    @Generated
    public boolean isNonNegative() {
        return this.nonNegative;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof FunctionParameterDefinition)) {
            return false;
        } else {
            FunctionParameterDefinition other = (FunctionParameterDefinition) o;
            if (this.isVarArg() != other.isVarArg()) {
                return false;
            } else if (this.isLazy() != other.isLazy()) {
                return false;
            } else if (this.isNonZero() != other.isNonZero()) {
                return false;
            } else if (this.isNonNegative() != other.isNonNegative()) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                return this$name == null ? other$name == null : this$name.equals(other$name);
            }
        }
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isVarArg() ? 79 : 97);
        result = result * 59 + (this.isLazy() ? 79 : 97);
        result = result * 59 + (this.isNonZero() ? 79 : 97);
        result = result * 59 + (this.isNonNegative() ? 79 : 97);
        Object $name = this.getName();
        return result * 59 + ($name == null ? 43 : $name.hashCode());
    }

    @Generated
    public String toString() {
        return "FunctionParameterDefinition(name=" + this.getName() + ", isVarArg=" + this.isVarArg() + ", isLazy=" + this.isLazy() + ", nonZero=" + this.isNonZero() + ", nonNegative=" + this.isNonNegative() + ")";
    }

    @Generated
    public static class FunctionParameterDefinitionBuilder {

        @Generated
        private String name;

        @Generated
        private boolean isVarArg;

        @Generated
        private boolean isLazy;

        @Generated
        private boolean nonZero;

        @Generated
        private boolean nonNegative;

        @Generated
        FunctionParameterDefinitionBuilder() {
        }

        @Generated
        public FunctionParameterDefinition.FunctionParameterDefinitionBuilder name(String name) {
            this.name = name;
            return this;
        }

        @Generated
        public FunctionParameterDefinition.FunctionParameterDefinitionBuilder isVarArg(boolean isVarArg) {
            this.isVarArg = isVarArg;
            return this;
        }

        @Generated
        public FunctionParameterDefinition.FunctionParameterDefinitionBuilder isLazy(boolean isLazy) {
            this.isLazy = isLazy;
            return this;
        }

        @Generated
        public FunctionParameterDefinition.FunctionParameterDefinitionBuilder nonZero(boolean nonZero) {
            this.nonZero = nonZero;
            return this;
        }

        @Generated
        public FunctionParameterDefinition.FunctionParameterDefinitionBuilder nonNegative(boolean nonNegative) {
            this.nonNegative = nonNegative;
            return this;
        }

        @Generated
        public FunctionParameterDefinition build() {
            return new FunctionParameterDefinition(this.name, this.isVarArg, this.isLazy, this.nonZero, this.nonNegative);
        }

        @Generated
        public String toString() {
            return "FunctionParameterDefinition.FunctionParameterDefinitionBuilder(name=" + this.name + ", isVarArg=" + this.isVarArg + ", isLazy=" + this.isLazy + ", nonZero=" + this.nonZero + ", nonNegative=" + this.nonNegative + ")";
        }
    }
}