package ca.fxco.memoryleakfix.mixinextras.service;

import ca.fxco.memoryleakfix.mixinextras.injector.ModifyExpressionValueInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyReceiverInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyReturnValueInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.WrapWithConditionInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperationApplicatorExtension;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperationInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.StringUtils;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.SugarPostProcessingExtension;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.SugarWrapperInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.transformer.MixinTransformerExtension;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinExtrasLogger;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import ca.fxco.memoryleakfix.mixinextras.wrapper.factory.FactoryRedirectWrapperInjectionInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class MixinExtrasServiceImpl implements MixinExtrasService {

    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("Service");

    private final List<Versioned<String>> offeredPackages = new ArrayList();

    private final List<Versioned<IExtension>> offeredExtensions = new ArrayList();

    private final List<Versioned<Class<? extends InjectionInfo>>> offeredInjectors = new ArrayList();

    private final String ownPackage = StringUtils.substringBefore(this.getClass().getName(), ".service.");

    private final List<Versioned<String>> allPackages = new ArrayList(Collections.singletonList(new Versioned<>(this.getVersion(), this.ownPackage)));

    private final List<IExtension> ownExtensions = Arrays.asList(new MixinTransformerExtension(), new ServiceInitializationExtension(this), new WrapOperationApplicatorExtension(), new SugarPostProcessingExtension());

    private final List<Class<? extends InjectionInfo>> ownInjectors = Arrays.asList(ModifyExpressionValueInjectionInfo.class, ModifyReceiverInjectionInfo.class, ModifyReturnValueInjectionInfo.class, WrapOperationInjectionInfo.class, WrapWithConditionInjectionInfo.class);

    private final List<Class<? extends InjectionInfo>> internalInjectors = Arrays.asList(SugarWrapperInjectionInfo.class, FactoryRedirectWrapperInjectionInfo.class);

    boolean initialized;

    @Override
    public int getVersion() {
        return MixinExtrasVersion.LATEST.getNumber();
    }

    @Override
    public boolean shouldReplace(Object otherService) {
        return this.getVersion() > MixinExtrasService.getFrom(otherService).getVersion();
    }

    @Override
    public void takeControlFrom(Object olderService) {
        LOGGER.debug("{} is taking over from {}", this, olderService);
        this.ownExtensions.forEach(it -> MixinInternals.registerExtension(it, it instanceof ServiceInitializationExtension || it instanceof MixinTransformerExtension));
        this.ownInjectors.forEach(it -> this.registerInjector(it, this.ownPackage));
    }

    @Override
    public void concedeTo(Object newerService, boolean wasActive) {
        this.requireNotInitialized();
        LOGGER.debug("{} is conceding to {}", this, newerService);
        MixinExtrasService newService = MixinExtrasService.getFrom(newerService);
        if (wasActive) {
            this.deInitialize();
        }
        this.offeredPackages.forEach(packageName -> newService.offerPackage(packageName.version, (String) packageName.value));
        newService.offerPackage(this.getVersion(), this.ownPackage);
        this.offeredExtensions.forEach(extension -> newService.offerExtension(extension.version, (IExtension) extension.value));
        this.ownExtensions.forEach(extension -> newService.offerExtension(this.getVersion(), extension));
        this.offeredInjectors.forEach(injector -> newService.offerInjector(injector.version, (Class<? extends InjectionInfo>) injector.value));
        this.ownInjectors.forEach(injector -> newService.offerInjector(this.getVersion(), injector));
    }

    @Override
    public void offerPackage(int version, String packageName) {
        this.requireNotInitialized();
        this.offeredPackages.add(new Versioned<>(version, packageName));
        this.allPackages.add(new Versioned<>(version, packageName));
        this.ownInjectors.forEach(it -> this.registerInjector(it, packageName));
    }

    @Override
    public void offerExtension(int version, IExtension extension) {
        this.requireNotInitialized();
        this.offeredExtensions.add(new Versioned<>(version, extension));
    }

    @Override
    public void offerInjector(int version, Class<? extends InjectionInfo> injector) {
        this.requireNotInitialized();
        this.offeredInjectors.add(new Versioned<>(version, injector));
    }

    public String toString() {
        return String.format("%s(version=%s)", this.getClass().getName(), MixinExtrasVersion.LATEST);
    }

    @Override
    public void initialize() {
        this.requireNotInitialized();
        LOGGER.info("Initializing MixinExtras via {}.", this);
        this.detectBetaPackages();
        this.internalInjectors.forEach(InjectionInfo::register);
        this.initialized = true;
    }

    private void deInitialize() {
        for (IExtension extension : this.ownExtensions) {
            MixinInternals.unregisterExtension(extension);
        }
        for (Class<? extends InjectionInfo> injector : this.ownInjectors) {
            this.allPackages.forEach(it -> this.unregisterInjector(injector, (String) it.value));
        }
    }

    private void registerInjector(Class<? extends InjectionInfo> injector, String packageName) {
        String name = ((AnnotationType) injector.getAnnotation(AnnotationType.class)).value().getName();
        String suffix = StringUtils.removeStart(name, this.ownPackage);
        MixinInternals.registerInjector(packageName + suffix, injector);
    }

    private void unregisterInjector(Class<? extends InjectionInfo> injector, String packageName) {
        String name = ((AnnotationType) injector.getAnnotation(AnnotationType.class)).value().getName();
        String suffix = StringUtils.removeStart(name, this.ownPackage);
        MixinInternals.unregisterInjector(packageName + suffix);
    }

    public Type changePackage(Class<?> ourType, Type theirReference, Class<?> ourReference) {
        String suffix = StringUtils.substringAfter(ourReference.getName(), this.ownPackage);
        String theirPackage = StringUtils.substringBefore(theirReference.getClassName(), suffix);
        return Type.getObjectType((theirPackage + StringUtils.substringAfter(ourType.getName(), this.ownPackage)).replace('.', '/'));
    }

    public Set<String> getAllClassNames(String ourName) {
        return this.getAllClassNamesAtLeast(ourName, Integer.MIN_VALUE);
    }

    public Set<String> getAllClassNamesAtLeast(String ourName, MixinExtrasVersion minVersion) {
        return this.getAllClassNamesAtLeast(ourName, minVersion.getNumber());
    }

    private Set<String> getAllClassNamesAtLeast(String ourName, int minVersion) {
        String ourBinaryName = ourName.replace('/', '.');
        return (Set<String>) this.allPackages.stream().filter(it -> it.version >= minVersion).map(it -> (String) it.value).map(it -> StringUtils.replaceOnce(ourBinaryName, this.ownPackage, it)).collect(Collectors.toSet());
    }

    public boolean isClassOwned(String name) {
        return this.allPackages.stream().map(it -> (String) it.value).anyMatch(name::startsWith);
    }

    private void requireNotInitialized() {
        if (this.initialized) {
            throw new IllegalStateException("The MixinExtras service has already been selected and is initialized!");
        }
    }

    private void detectBetaPackages() {
        for (IExtension extension : MixinInternals.getExtensions().getActiveExtensions()) {
            String name = extension.getClass().getName();
            String suffix = ".sugar.impl.SugarApplicatorExtension";
            if (name.endsWith(suffix) && !this.isClassOwned(name)) {
                String packageName = StringUtils.removeEnd(name, suffix);
                MixinExtrasVersion version = this.getBetaVersion(packageName);
                this.allPackages.add(new Versioned<>(version.getNumber(), packageName));
                LOGGER.warn("Found problematic active MixinExtras instance at {} (version {})", packageName, version);
                LOGGER.warn("Versions from 0.2.0-beta.1 to 0.2.0-beta.9 have limited support and it is strongly recommended to update.");
            }
        }
    }

    private MixinExtrasVersion getBetaVersion(String packageName) {
        String bootstrapClassName = packageName + ".MixinExtrasBootstrap";
        try {
            Class<?> bootstrapClass = Class.forName(bootstrapClassName);
            Field versionField = bootstrapClass.getDeclaredField("VERSION");
            versionField.setAccessible(true);
            String versionName = (String) versionField.get(null);
            switch(versionName) {
                case "0.2.0-beta.1":
                    return MixinExtrasVersion.V0_2_0_BETA_1;
                case "0.2.0-beta.2":
                    return MixinExtrasVersion.V0_2_0_BETA_2;
                case "0.2.0-beta.3":
                    return MixinExtrasVersion.V0_2_0_BETA_3;
                case "0.2.0-beta.4":
                    return MixinExtrasVersion.V0_2_0_BETA_4;
                case "0.2.0-beta.5":
                    return MixinExtrasVersion.V0_2_0_BETA_5;
                case "0.2.0-beta.6":
                    return MixinExtrasVersion.V0_2_0_BETA_6;
                case "0.2.0-beta.7":
                    return MixinExtrasVersion.V0_2_0_BETA_7;
                case "0.2.0-beta.8":
                    return MixinExtrasVersion.V0_2_0_BETA_8;
                case "0.2.0-beta.9":
                    return MixinExtrasVersion.V0_2_0_BETA_9;
                default:
                    throw new IllegalArgumentException("Unrecognized version " + versionName);
            }
        } catch (Exception var8) {
            LOGGER.error(String.format("Failed to determine version of MixinExtras instance at %s, assuming 0.2.0-beta.1", packageName), var8);
            return MixinExtrasVersion.V0_2_0_BETA_1;
        }
    }
}