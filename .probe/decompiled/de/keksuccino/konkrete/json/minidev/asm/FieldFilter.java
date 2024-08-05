package de.keksuccino.konkrete.json.minidev.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface FieldFilter {

    boolean canUse(Field var1);

    boolean canUse(Field var1, Method var2);

    boolean canRead(Field var1);

    boolean canWrite(Field var1);
}