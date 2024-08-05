package org.embeddedt.modernfix.forge.classloading;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import net.minecraftforge.forgespi.language.ModFileScanData.ClassData;
import net.minecraftforge.forgespi.locating.IModFile;
import org.objectweb.asm.Type;

public class ModFileScanDataDeduplicator {

    private final Interner<Type> typeInterner = Interners.newStrongInterner();

    private final Function<Type, Type> internerFn = type -> type != null ? (Type) this.typeInterner.intern(type) : null;

    private static Field classClazzField;

    private static Field parentField;

    private static Field interfacesField;

    private static Field annotationClazzField;

    private static Field annotationTypeField;

    private static final boolean reflectionSuccessful;

    ModFileScanDataDeduplicator() {
    }

    private void runDeduplication() {
        ModList.get().forEachModFile(this::deduplicateFile);
    }

    private void deduplicateFile(IModFile file) {
        ModFileScanData data = file.getScanResult();
        if (data != null) {
            data.getClasses().forEach(this::deduplicateClass);
            data.getAnnotations().forEach(this::deduplicateAnnotation);
        }
    }

    private void deduplicateClass(ClassData data) {
        try {
            Type type = (Type) classClazzField.get(data);
            type = (Type) this.internerFn.apply(type);
            classClazzField.set(data, type);
            type = (Type) parentField.get(data);
            type = (Type) this.internerFn.apply(type);
            parentField.set(data, type);
            Set<Type> types = (Set<Type>) interfacesField.get(data);
            types = (Set<Type>) types.stream().map(this.internerFn).collect(Collectors.toSet());
            interfacesField.set(data, types);
        } catch (ReflectiveOperationException var4) {
        }
    }

    private void deduplicateAnnotation(AnnotationData data) {
        try {
            Type type = (Type) annotationClazzField.get(data);
            type = (Type) this.internerFn.apply(type);
            annotationClazzField.set(data, type);
            type = (Type) annotationTypeField.get(data);
            type = (Type) this.internerFn.apply(type);
            annotationTypeField.set(data, type);
        } catch (ReflectiveOperationException var3) {
        }
    }

    public static void deduplicate() {
        if (reflectionSuccessful) {
            new ModFileScanDataDeduplicator().runDeduplication();
        }
    }

    static {
        boolean success = false;
        try {
            classClazzField = ClassData.class.getDeclaredField("clazz");
            classClazzField.setAccessible(true);
            parentField = ClassData.class.getDeclaredField("parent");
            parentField.setAccessible(true);
            interfacesField = ClassData.class.getDeclaredField("interfaces");
            interfacesField.setAccessible(true);
            annotationClazzField = AnnotationData.class.getDeclaredField("clazz");
            annotationClazzField.setAccessible(true);
            annotationTypeField = AnnotationData.class.getDeclaredField("annotationType");
            annotationTypeField.setAccessible(true);
            success = true;
        } catch (RuntimeException | ReflectiveOperationException var2) {
        }
        reflectionSuccessful = success;
    }
}