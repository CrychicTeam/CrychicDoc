package org.embeddedt.modernfix.core;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.embeddedt.modernfix.core.config.ModernFixEarlyConfig;
import org.embeddedt.modernfix.core.config.Option;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.embeddedt.modernfix.world.ThreadDumper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

public class ModernFixMixinPlugin implements IMixinConfigPlugin {

    private static final String MIXIN_PACKAGE_ROOT = "org.embeddedt.modernfix.mixin.";

    public final Logger logger = LogManager.getLogger("ModernFix");

    public ModernFixEarlyConfig config = null;

    public static ModernFixMixinPlugin instance;

    public ModernFixMixinPlugin() {
        ModernFixPlatformHooks.INSTANCE.getCustomModOptions();
        boolean firstConfig = instance == null;
        if (firstConfig) {
            instance = this;
            try {
                this.config = ModernFixEarlyConfig.load(new File("./config/modernfix-mixins.properties"));
            } catch (Exception var5) {
                throw new RuntimeException("Could not load configuration file for ModernFix", var5);
            }
            this.logger.info("Loaded configuration file for ModernFix {}: {} options available, {} override(s) found", ModernFixPlatformHooks.INSTANCE.getVersionString(), this.config.getOptionCount(), this.config.getOptionOverrideCount());
            this.config.getOptionMap().values().forEach(option -> {
                if (option.isOverridden()) {
                    String source = "[unknown]";
                    if (option.isUserDefined()) {
                        source = "user configuration";
                    } else if (!ModernFixPlatformHooks.INSTANCE.isEarlyLoadingNormally()) {
                        source = "load error";
                    } else if (option.isModDefined()) {
                        source = "mods [" + String.join(", ", option.getDefiningMods()) + "]";
                    }
                    this.logger.warn("Option '{}' overriden (by {}) to '{}'", option.getName(), source, option.isEnabled());
                }
            });
            if (ModernFixEarlyConfig.OPTIFINE_PRESENT) {
                this.logger.fatal("OptiFine detected. Use of ModernFix with OptiFine is not supported due to its impact on launch time and breakage of Forge features.");
            }
            try {
                Class.forName("sun.misc.Unsafe").getDeclaredMethod("defineAnonymousClass", Class.class, byte[].class, Object[].class);
            } catch (NullPointerException | ReflectiveOperationException var4) {
                this.logger.info("Applying Nashorn fix");
                Properties properties = System.getProperties();
                properties.setProperty("nashorn.args", properties.getProperty("nashorn.args", "") + " --anonymous-classes=false");
            }
            ModernFixPlatformHooks.INSTANCE.injectPlatformSpecificHacks();
            if (instance.isOptionEnabled("feature.spam_thread_dump.ThreadDumper")) {
                ThreadDumper.obtainThreadDump();
                Thread t = new Thread() {

                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(60000L);
                                ModernFixMixinPlugin.this.logger.error("------ DEBUG THREAD DUMP (occurs every 60 seconds) ------");
                                ModernFixMixinPlugin.this.logger.error(ThreadDumper.obtainThreadDump());
                            } catch (RuntimeException | InterruptedException var2) {
                            }
                        }
                    }
                };
                t.setDaemon(true);
                t.start();
            }
        }
    }

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        mixinClassName = ModernFixEarlyConfig.sanitize(mixinClassName);
        if (!mixinClassName.startsWith("org.embeddedt.modernfix.mixin.")) {
            this.logger.error("Expected mixin '{}' to start with package root '{}', treating as foreign and disabling!", mixinClassName, "org.embeddedt.modernfix.mixin.");
            return false;
        } else {
            String mixin = mixinClassName.substring("org.embeddedt.modernfix.mixin.".length());
            if (!instance.isOptionEnabled(mixin)) {
                return false;
            } else {
                String disabledBecauseMod = (String) instance.config.getPermanentlyDisabledMixins().get(mixin);
                return disabledBecauseMod == null;
            }
        }
    }

    public boolean isOptionEnabled(String mixin) {
        Option option = instance.config.getEffectiveOptionForMixin(mixin);
        if (option == null) {
            String msg = "No rules matched mixin '{}', treating as foreign and disabling!";
            if (ModernFixPlatformHooks.INSTANCE.isDevEnv()) {
                this.logger.error(msg, mixin);
            } else {
                this.logger.debug(msg, mixin);
            }
            return false;
        } else {
            return option.isEnabled();
        }
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (mixinClassName.equals("org.embeddedt.modernfix.common.mixin.perf.reduce_blockstate_cache_rebuilds.BlockStateBaseMixin")) {
            try {
                this.applyBlockStateCacheScan(targetClass);
            } catch (RuntimeException var6) {
                instance.logger.error("Applying blockstate cache ASM patch failed", var6);
            }
        }
        ModernFixPlatformHooks.INSTANCE.applyASMTransformers(mixinClassName, targetClass);
    }

    private void applyBlockStateCacheScan(ClassNode targetClass) {
        Set<String> initCacheMethodNames = ImmutableSet.of("m_60611_", "func_215692_c", "method_26200", "initCache");
        Set<String> whitelistedInjections = ImmutableSet.of("getFluidState", "method_26227", "m_60819_", "func_204520_s");
        Map<String, MethodNode> injectorMethodNames = new HashMap();
        Map<String, MethodNode> allMethods = new HashMap();
        Map<String, String> injectorMixinSource = new HashMap();
        String descriptor = Type.getDescriptor(MixinMerged.class);
        for (MethodNode m : targetClass.methods) {
            if ((m.access & 8) == 0) {
                allMethods.put(m.name, m);
                Set<AnnotationNode> seenNodes = new HashSet();
                if (m.invisibleAnnotations != null) {
                    for (AnnotationNode ann : m.invisibleAnnotations) {
                        if (ann.desc.equals(descriptor)) {
                            seenNodes.add(ann);
                        }
                    }
                }
                if (m.visibleAnnotations != null) {
                    for (AnnotationNode annx : m.visibleAnnotations) {
                        if (annx.desc.equals(descriptor)) {
                            seenNodes.add(annx);
                        }
                    }
                }
                if (seenNodes.size() > 0) {
                    injectorMethodNames.put(m.name, m);
                    for (AnnotationNode node : seenNodes) {
                        for (int i = 0; i < node.values.size(); i += 2) {
                            if (Objects.equals(node.values.get(i), "mixin")) {
                                injectorMixinSource.put(m.name, (String) node.values.get(i + 1));
                                break;
                            }
                        }
                    }
                }
            }
        }
        Set<String> cacheCalledInjectors = new HashSet();
        for (MethodNode mx : targetClass.methods) {
            if ((mx.access & 8) == 0 && initCacheMethodNames.contains(mx.name)) {
                ListIterator var27 = mx.instructions.iterator();
                while (var27.hasNext()) {
                    AbstractInsnNode n = (AbstractInsnNode) var27.next();
                    if (n instanceof MethodInsnNode) {
                        MethodInsnNode invoke = (MethodInsnNode) n;
                        if (((MethodInsnNode) n).owner.equals(targetClass.name) && injectorMethodNames.containsKey(((MethodInsnNode) n).name)) {
                            cacheCalledInjectors.add(invoke.name);
                        }
                    }
                }
                break;
            }
        }
        Set<String> accessedFieldNames = new HashSet();
        Map<String, MethodNode> writingMethods = new HashMap(injectorMethodNames);
        writingMethods.keySet().retainAll(cacheCalledInjectors);
        int previousSize = 0;
        Set<String> checkedCalls = new HashSet();
        while (writingMethods.size() > previousSize) {
            previousSize = writingMethods.size();
            for (String name : new ArrayList(writingMethods.keySet())) {
                if (checkedCalls.add(name)) {
                    ListIterator var16 = ((MethodNode) writingMethods.get(name)).instructions.iterator();
                    while (var16.hasNext()) {
                        AbstractInsnNode n = (AbstractInsnNode) var16.next();
                        if (n instanceof MethodInsnNode) {
                            MethodInsnNode invokeNode = (MethodInsnNode) n;
                            if (invokeNode.owner.equals(targetClass.name)) {
                                MethodNode theMethod = (MethodNode) allMethods.get(invokeNode.name);
                                if (theMethod != null) {
                                    writingMethods.put(invokeNode.name, theMethod);
                                }
                            }
                        }
                    }
                }
            }
        }
        writingMethods.forEach((namex, method) -> {
            ListIterator var4x = method.instructions.iterator();
            while (var4x.hasNext()) {
                AbstractInsnNode nx = (AbstractInsnNode) var4x.next();
                if (nx instanceof FieldInsnNode) {
                    FieldInsnNode fieldAcc = (FieldInsnNode) nx;
                    if (fieldAcc.getOpcode() == 181 && fieldAcc.owner.equals(targetClass.name)) {
                        accessedFieldNames.add(fieldAcc.name);
                    }
                }
            }
        });
        injectorMethodNames.forEach((namex, method) -> {
            if (!whitelistedInjections.contains(namex) && !cacheCalledInjectors.contains(namex)) {
                boolean needInjection = false;
                ListIterator injection = method.instructions.iterator();
                while (injection.hasNext()) {
                    AbstractInsnNode nx = (AbstractInsnNode) injection.next();
                    if (nx instanceof FieldInsnNode fieldAcc && fieldAcc.getOpcode() == 180 && accessedFieldNames.contains(fieldAcc.name)) {
                        needInjection = true;
                        break;
                    }
                }
                if (needInjection) {
                    instance.logger.info("Injecting BlockStateBase cache population hook into {} from {}", namex, injectorMixinSource.getOrDefault(namex, "[unknown mixin]"));
                    InsnList injectionx = new InsnList();
                    injectionx.add(new VarInsnNode(25, 0));
                    injectionx.add(new MethodInsnNode(182, targetClass.name, "mfix$generateCache", "()V"));
                    method.instructions.insert(injectionx);
                }
            }
        });
    }
}