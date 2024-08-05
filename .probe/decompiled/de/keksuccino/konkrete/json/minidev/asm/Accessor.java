package de.keksuccino.konkrete.json.minidev.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Accessor {

    protected Field field;

    protected Method setter;

    protected Method getter;

    protected int index;

    protected Class<?> type;

    protected Type genericType;

    protected String fieldName;

    public int getIndex() {
        return this.index;
    }

    public boolean isPublic() {
        return this.setter == null && this.getter == null;
    }

    public boolean isEnum() {
        return this.type.isEnum();
    }

    public String getName() {
        return this.fieldName;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Type getGenericType() {
        return this.genericType;
    }

    public boolean isUsable() {
        return this.field != null || this.getter != null || this.setter != null;
    }

    public boolean isReadable() {
        return this.field != null || this.getter != null;
    }

    public boolean isWritable() {
        return this.field != null || this.getter != null;
    }

    public Accessor(Class<?> c, Field field, FieldFilter filter) {
        this.fieldName = field.getName();
        int m = field.getModifiers();
        if ((m & 136) <= 0) {
            if ((m & 1) > 0) {
                this.field = field;
            }
            String name = ASMUtil.getSetterName(field.getName());
            try {
                this.setter = c.getDeclaredMethod(name, field.getType());
            } catch (Exception var10) {
            }
            boolean isBool = field.getType().equals(boolean.class);
            if (isBool) {
                name = ASMUtil.getIsName(field.getName());
            } else {
                name = ASMUtil.getGetterName(field.getName());
            }
            try {
                this.getter = c.getDeclaredMethod(name);
            } catch (Exception var9) {
            }
            if (this.getter == null && isBool) {
                try {
                    this.getter = c.getDeclaredMethod(ASMUtil.getGetterName(field.getName()));
                } catch (Exception var8) {
                }
            }
            if (this.field != null || this.getter != null || this.setter != null) {
                if (this.getter != null && !filter.canUse(field, this.getter)) {
                    this.getter = null;
                }
                if (this.setter != null && !filter.canUse(field, this.setter)) {
                    this.setter = null;
                }
                if (this.getter != null || this.setter != null || this.field != null) {
                    this.type = field.getType();
                    this.genericType = field.getGenericType();
                }
            }
        }
    }
}