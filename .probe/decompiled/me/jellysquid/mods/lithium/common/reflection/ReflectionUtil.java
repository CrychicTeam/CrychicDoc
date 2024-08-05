package me.jellysquid.mods.lithium.common.reflection;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReflectionUtil {

    public static boolean hasMethodOverride(Class<?> clazz, Class<?> superclass, boolean fallbackResult, String methodName, Class<?>... methodArgs) {
        while (clazz != null && clazz != superclass && superclass.isAssignableFrom(clazz)) {
            try {
                clazz.getDeclaredMethod(methodName, methodArgs);
                return true;
            } catch (NoSuchMethodException var9) {
                clazz = clazz.getSuperclass();
            } catch (RuntimeException | NoClassDefFoundError var10) {
                Logger logger = LogManager.getLogger("Radium Class Analysis");
                logger.warn("Radium Class Analysis Error: Class " + clazz.getName() + " cannot be analysed, because getting declared methods crashes with " + var10.getClass().getSimpleName() + ": " + var10.getMessage() + ". This is usually caused by modded entities declaring methods that have a return type or parameter type that is annotated with @OnlyIn(Dist.CLIENT). Loading the type is not possible, because it only exists in the CLIENT environment. The recommended fix is to annotate the method with this argument or return type with the same annotation. Lithium handles this error by assuming the class cannot be included in some optimizations.");
                return fallbackResult;
            } catch (Throwable var11) {
                String crashedClass = clazz.getName();
                CrashReport crashReport = CrashReport.forThrowable(var11, "Radium Class Analysis");
                CrashReportCategory crashReportSection = crashReport.addCategory(var11.getClass().toString() + " when getting declared methods.");
                crashReportSection.setDetail("Analyzed class", crashedClass);
                crashReportSection.setDetail("Analyzed method name", methodName);
                crashReportSection.setDetail("Analyzed method args", methodArgs);
                throw new ReportedException(crashReport);
            }
        }
        return false;
    }
}