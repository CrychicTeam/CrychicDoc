package de.keksuccino.konkrete.math;

import java.util.Random;

public class MathUtils {

    public static boolean isIntegerOrDouble(String value) {
        try {
            if (value.contains(".")) {
                Double.parseDouble(value);
            } else {
                Integer.parseInt(value);
            }
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            return min;
        } else {
            Random r = new Random();
            return r.nextInt(max - min + 1) + min;
        }
    }

    public static double calculateFromString(final String in) {
        if (isDouble(in)) {
            return Double.parseDouble(in);
        } else {
            try {
                return (new Object() {

                    int pos = -1;

                    int ch;

                    void nextChar() {
                        this.ch = ++this.pos < in.length() ? in.charAt(this.pos) : -1;
                    }

                    boolean eat(int charToEat) {
                        while (this.ch == 32) {
                            this.nextChar();
                        }
                        if (this.ch == charToEat) {
                            this.nextChar();
                            return true;
                        } else {
                            return false;
                        }
                    }

                    double parse() {
                        this.nextChar();
                        double x = this.parseExpression();
                        if (this.pos < in.length()) {
                            throw new RuntimeException("[KONKRETE] Unexpected: " + (char) this.ch);
                        } else {
                            return x;
                        }
                    }

                    double parseExpression() {
                        double x = this.parseTerm();
                        while (true) {
                            while (!this.eat(43)) {
                                if (!this.eat(45)) {
                                    return x;
                                }
                                x -= this.parseTerm();
                            }
                            x += this.parseTerm();
                        }
                    }

                    double parseTerm() {
                        double x = this.parseFactor();
                        while (true) {
                            while (!this.eat(42)) {
                                if (!this.eat(47)) {
                                    return x;
                                }
                                x /= this.parseFactor();
                            }
                            x *= this.parseFactor();
                        }
                    }

                    double parseFactor() {
                        if (this.eat(43)) {
                            return this.parseFactor();
                        } else if (this.eat(45)) {
                            return -this.parseFactor();
                        } else {
                            int startPos = this.pos;
                            double x;
                            if (this.eat(40)) {
                                x = this.parseExpression();
                                this.eat(41);
                            } else if ((this.ch < 48 || this.ch > 57) && this.ch != 46) {
                                if (this.ch < 97 || this.ch > 122) {
                                    throw new RuntimeException("[KONKRETE] Unexpected: " + (char) this.ch);
                                }
                                while (this.ch >= 97 && this.ch <= 122) {
                                    this.nextChar();
                                }
                                String func = in.substring(startPos, this.pos);
                                x = this.parseFactor();
                                if (func.equals("sqrt")) {
                                    x = Math.sqrt(x);
                                } else if (func.equals("sin")) {
                                    x = Math.sin(Math.toRadians(x));
                                } else if (func.equals("cos")) {
                                    x = Math.cos(Math.toRadians(x));
                                } else {
                                    if (!func.equals("tan")) {
                                        throw new RuntimeException("[KONKRETE] Unknown function: " + func);
                                    }
                                    x = Math.tan(Math.toRadians(x));
                                }
                            } else {
                                while (this.ch >= 48 && this.ch <= 57 || this.ch == 46) {
                                    this.nextChar();
                                }
                                x = Double.parseDouble(in.substring(startPos, this.pos));
                            }
                            if (this.eat(94)) {
                                x = Math.pow(x, this.parseFactor());
                            }
                            return x;
                        }
                    }
                }).parse();
            } catch (Exception var2) {
                var2.printStackTrace();
                return 0.0;
            }
        }
    }

    @Deprecated
    public static boolean isCalculateableString(String in) {
        return false;
    }
}