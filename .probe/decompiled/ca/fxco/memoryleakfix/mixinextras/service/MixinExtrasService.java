package ca.fxco.memoryleakfix.mixinextras.service;

import ca.fxco.memoryleakfix.mixinextras.utils.Blackboard;
import ca.fxco.memoryleakfix.mixinextras.utils.ProxyUtils;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public interface MixinExtrasService {

    int getVersion();

    boolean shouldReplace(Object var1);

    void takeControlFrom(Object var1);

    void concedeTo(Object var1, boolean var2);

    void offerPackage(int var1, String var2);

    void offerExtension(int var1, IExtension var2);

    void offerInjector(int var1, Class<? extends InjectionInfo> var2);

    void initialize();

    static void setup() {
        Object latestImpl = Blackboard.get("MixinExtrasServiceInstance");
        if (latestImpl == null) {
            MixinExtrasService newImpl = new MixinExtrasServiceImpl();
            Blackboard.put("MixinExtrasServiceInstance", newImpl);
            newImpl.takeControlFrom(null);
        } else {
            MixinExtrasService ourImpl = new MixinExtrasServiceImpl();
            if (ourImpl.shouldReplace(latestImpl)) {
                getFrom(latestImpl).concedeTo(ourImpl, true);
                Blackboard.put("MixinExtrasServiceInstance", ourImpl);
                ourImpl.takeControlFrom(latestImpl);
            } else {
                ourImpl.concedeTo(latestImpl, false);
            }
        }
    }

    static MixinExtrasService getFrom(Object serviceImpl) {
        return ProxyUtils.getProxy(serviceImpl, MixinExtrasService.class);
    }

    static MixinExtrasServiceImpl getInstance() {
        Object impl = Blackboard.get("MixinExtrasServiceInstance");
        if (impl instanceof MixinExtrasServiceImpl) {
            MixinExtrasServiceImpl ourImpl = (MixinExtrasServiceImpl) impl;
            if (ourImpl.initialized) {
                return ourImpl;
            } else {
                throw new IllegalStateException("Cannot use service because it is not initialized!");
            }
        } else {
            throw new IllegalStateException("Cannot use service because another service is active: " + impl);
        }
    }
}