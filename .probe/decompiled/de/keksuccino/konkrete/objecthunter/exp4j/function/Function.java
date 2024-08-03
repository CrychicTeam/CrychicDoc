package de.keksuccino.konkrete.objecthunter.exp4j.function;

public abstract class Function {

    private final String name;

    protected final int numArguments;

    public Function(String name, int numArguments) {
        if (numArguments < 0) {
            throw new IllegalArgumentException("The number of function arguments can not be less than 0 for '" + name + "'");
        } else if (!isValidFunctionName(name)) {
            throw new IllegalArgumentException("The function name '" + name + "' is invalid");
        } else {
            this.name = name;
            this.numArguments = numArguments;
        }
    }

    public Function(String name) {
        this(name, 1);
    }

    public String getName() {
        return this.name;
    }

    public int getNumArguments() {
        return this.numArguments;
    }

    public abstract double apply(double... var1);

    /**
     * @deprecated
     */
    public static char[] getAllowedFunctionCharacters() {
        char[] chars = new char[53];
        int count = 0;
        for (int i = 65; i < 91; i++) {
            chars[count++] = (char) i;
        }
        for (int i = 97; i < 123; i++) {
            chars[count++] = (char) i;
        }
        chars[count] = '_';
        return chars;
    }

    public static boolean isValidFunctionName(String name) {
        if (name == null) {
            return false;
        } else {
            int size = name.length();
            if (size == 0) {
                return false;
            } else {
                for (int i = 0; i < size; i++) {
                    char c = name.charAt(i);
                    if (!Character.isLetter(c) && c != '_' && (!Character.isDigit(c) || i <= 0)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
}