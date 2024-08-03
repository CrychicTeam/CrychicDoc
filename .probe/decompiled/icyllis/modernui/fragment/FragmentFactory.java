package icyllis.modernui.fragment;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public class FragmentFactory {

    @Nonnull
    public static Class<? extends Fragment> loadFragmentClass(@Nonnull ClassLoader classLoader, @Nonnull String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException("Unable to instantiate fragment " + className + ": make sure class name exists", var3);
        } catch (ClassCastException var4) {
            throw new RuntimeException("Unable to instantiate fragment " + className + ": make sure class is a valid subclass of Fragment", var4);
        }
    }

    @Nonnull
    public Fragment instantiate(@Nonnull ClassLoader classLoader, @Nonnull String className) {
        return this.instantiate(loadFragmentClass(classLoader, className));
    }

    @Nonnull
    public Fragment instantiate(@Nonnull Class<? extends Fragment> clazz) {
        try {
            return (Fragment) clazz.getConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException var3) {
            throw new RuntimeException("Unable to instantiate fragment " + clazz + ": make sure class name exists, is public, and has an empty constructor that is public", var3);
        } catch (NoSuchMethodException var4) {
            throw new RuntimeException("Unable to instantiate fragment " + clazz + ": could not find Fragment constructor", var4);
        } catch (InvocationTargetException var5) {
            throw new RuntimeException("Unable to instantiate fragment " + clazz + ": calling Fragment constructor caused an exception", var5);
        }
    }
}