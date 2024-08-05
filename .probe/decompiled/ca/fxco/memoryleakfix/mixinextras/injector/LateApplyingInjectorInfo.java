package ca.fxco.memoryleakfix.mixinextras.injector;

import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasService;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinExtrasLogger;
import ca.fxco.memoryleakfix.mixinextras.utils.ProxyUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public interface LateApplyingInjectorInfo {

    void lateInject();

    void latePostInject();

    void wrap(LateApplyingInjectorInfo var1);

    @Deprecated
    default void lateApply() {
        this.lateInject();
        MixinExtrasLogger logger = MixinExtrasLogger.get("Sugar");
        logger.warn("Skipping post injection checks for {} since it is from 0.2.0-beta.1 and cannot be saved", this);
    }

    static boolean wrap(Object inner, LateApplyingInjectorInfo outer) {
        Class<?> theirInterface = (Class<?>) Arrays.stream(inner.getClass().getInterfaces()).filter(it -> it.getName().endsWith(".LateApplyingInjectorInfo")).findFirst().orElse(null);
        if (theirInterface != null && MixinExtrasService.getInstance().isClassOwned(theirInterface.getName())) {
            try {
                inner.getClass().getMethod("wrap", theirInterface).invoke(inner, ProxyUtils.getProxy(outer, theirInterface));
                return true;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var4) {
                throw new RuntimeException("Failed to wrap InjectionInfo: ", var4);
            }
        } else {
            return false;
        }
    }
}