package de.keksuccino.konkrete.json.minidev.asm;

import de.keksuccino.konkrete.json.minidev.asm.ex.ConvertException;

public class DefaultConverter {

    public static int convertToint(Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        } else if (obj instanceof String) {
            return Integer.parseInt((String) obj);
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
        }
    }

    public static Integer convertToInt(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Integer.class) {
                return (Integer) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).intValue();
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Integer");
            }
        }
    }

    public static short convertToshort(Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        } else if (obj instanceof String) {
            return Short.parseShort((String) obj);
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to short");
        }
    }

    public static Short convertToShort(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Short.class) {
                return (Short) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).shortValue();
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Short");
            }
        }
    }

    public static long convertTolong(Object obj) {
        if (obj == null) {
            return 0L;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else if (obj instanceof String) {
            return Long.parseLong((String) obj);
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to long");
        }
    }

    public static Long convertToLong(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Long.class) {
                return (Long) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).longValue();
            } else {
                throw new ConvertException("Primitive: Can not convert value '" + obj + "' As " + obj.getClass().getName() + " to Long");
            }
        }
    }

    public static byte convertTobyte(Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        } else if (obj instanceof String) {
            return Byte.parseByte((String) obj);
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to byte");
        }
    }

    public static Byte convertToByte(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Byte.class) {
                return (Byte) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).byteValue();
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Byte");
            }
        }
    }

    public static float convertTofloat(Object obj) {
        if (obj == null) {
            return 0.0F;
        } else if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        } else if (obj instanceof String) {
            return Float.parseFloat((String) obj);
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
        }
    }

    public static Float convertToFloat(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Float.class) {
                return (Float) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).floatValue();
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
            }
        }
    }

    public static double convertTodouble(Object obj) {
        if (obj == null) {
            return 0.0;
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        } else if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to float");
        }
    }

    public static Double convertToDouble(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Double.class) {
                return (Double) obj;
            } else if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Float");
            }
        }
    }

    public static char convertTochar(Object obj) {
        if (obj == null) {
            return ' ';
        } else if (obj instanceof String) {
            return ((String) obj).length() > 0 ? ((String) obj).charAt(0) : ' ';
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to char");
        }
    }

    public static Character convertToChar(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Character.class) {
                return (Character) obj;
            } else if (obj instanceof String) {
                return ((String) obj).length() > 0 ? ((String) obj).charAt(0) : ' ';
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Character");
            }
        }
    }

    public static boolean convertTobool(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj.getClass() == Boolean.class) {
            return (Boolean) obj;
        } else if (obj instanceof String) {
            return Boolean.parseBoolean((String) obj);
        } else if (obj instanceof Number) {
            return !obj.toString().equals("0");
        } else {
            throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to boolean");
        }
    }

    public static Boolean convertToBool(Object obj) {
        if (obj == null) {
            return null;
        } else {
            Class<?> c = obj.getClass();
            if (c == Boolean.class) {
                return (Boolean) obj;
            } else if (obj instanceof String) {
                return Boolean.parseBoolean((String) obj);
            } else {
                throw new ConvertException("Primitive: Can not convert " + obj.getClass().getName() + " to Boolean");
            }
        }
    }
}