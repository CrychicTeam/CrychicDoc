package ca.fxco.memoryleakfix.config;

import architectury_inject_memoryleakfix_common_76d1734398794a188c70505cad79e2f8_26405a41a578e3db5b702b62f9b9174923b5dda92ea9487a625eace40ffd40fdmemoryleakfixcommon117115devjar.PlatformMethods;
import ca.fxco.memoryleakfix.MemoryLeakFix;
import ca.fxco.memoryleakfix.MemoryLeakFixBootstrap;
import ca.fxco.memoryleakfix.MemoryLeakFixExpectPlatform;
import ca.fxco.memoryleakfix.mixinextras.MixinExtrasBootstrap;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyExpressionValue;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyReceiver;
import ca.fxco.memoryleakfix.mixinextras.injector.ModifyReturnValue;
import ca.fxco.memoryleakfix.mixinextras.injector.WrapWithCondition;
import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperation;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

public class MemoryLeakFixMixinConfigPlugin implements IMixinConfigPlugin {

    private final Set<String> APPLIED_MEMORY_LEAK_FIXES = new HashSet();

    private static boolean shouldMentionFixCount = true;

    private static final boolean VERBOSE = false;

    public void onLoad(String mixinPackage) {
        MemoryLeakFixBootstrap.init();
        if (PlatformMethods.getCurrentTarget().contains("forge")) {
            MixinExtrasBootstrap.init();
        }
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith("ca.fxco.memoryleakfix")) {
            return true;
        } else {
            boolean shouldApply = areRequirementsMet(getMinecraftRequirement(mixinClassName)) && !this.shouldSilentNoClassDefFound(targetClassName, mixinClassName);
            if (shouldApply) {
                String classGroup = mixinClassName.substring(0, mixinClassName.lastIndexOf("."));
                this.APPLIED_MEMORY_LEAK_FIXES.add(classGroup.substring(classGroup.lastIndexOf(".") + 1));
            }
            return shouldApply;
        }
    }

    private boolean shouldSilentNoClassDefFound(String targetClassName, String mixinClassName) {
        try {
            ClassNode mixinClass = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            if (Annotations.getInvisible(mixinClass, SilentClassNotFound.class) != null) {
                try {
                    MixinService.getService().getBytecodeProvider().getClassNode(targetClassName);
                } catch (ClassNotFoundException var5) {
                    return true;
                }
            }
        } catch (Exception var6) {
        }
        return false;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        if (shouldMentionFixCount) {
            shouldMentionFixCount = false;
            int size = this.APPLIED_MEMORY_LEAK_FIXES.size();
            if (size > 0) {
                MemoryLeakFix.LOGGER.info("[MemoryLeakFix] Will be applying " + size + " memory leak fixes!");
                MemoryLeakFix.LOGGER.info("[MemoryLeakFix] Currently enabled memory leak fixes: " + this.APPLIED_MEMORY_LEAK_FIXES);
            }
        }
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public static void runCustomMixinClassNodeAnnotations(String className, ClassNode classNode) {
        if (className.startsWith("ca.fxco.memoryleakfix")) {
            if (areRequirementsMet(Annotations.getInvisible(classNode, MinecraftRequirement.class))) {
                Iterator<MethodNode> methodIterator = classNode.methods.iterator();
                while (methodIterator.hasNext()) {
                    MethodNode node = (MethodNode) methodIterator.next();
                    boolean isValid = true;
                    AnnotationNode requirements = Annotations.getInvisible(node, MinecraftRequirement.class);
                    if (requirements != null) {
                        for (AnnotationNode versionRange : (Iterable) Annotations.getValue(requirements)) {
                            if (!isVersionRangeValid(versionRange)) {
                                methodIterator.remove();
                                isValid = false;
                                break;
                            }
                        }
                    }
                    if (isValid) {
                        AnnotationNode remapTarget = Annotations.getVisible(node, RemapTarget.class);
                        if (remapTarget != null) {
                            executeRemapAnnotation(node, (AnnotationNode) Annotations.getValue(remapTarget, "value"), false);
                            executeRemapAnnotation(node, (AnnotationNode) Annotations.getValue(remapTarget, "target"), true);
                        } else {
                            executeRemapAnnotation(node, Annotations.getVisible(node, Remap.class), false);
                            AnnotationNode remapsAnnotation = Annotations.getInvisible(node, Remaps.class);
                            if (remapsAnnotation != null) {
                                List<AnnotationNode> remaps = (List<AnnotationNode>) Annotations.getValue(remapsAnnotation);
                                if (remaps != null) {
                                    for (AnnotationNode remap : remaps) {
                                        executeRemapAnnotation(node, remap, false);
                                    }
                                }
                            }
                        }
                    }
                }
                Map<FieldNode, String> fieldsRemapping = new HashMap();
                Iterator<FieldNode> fieldIterator = classNode.fields.iterator();
                while (fieldIterator.hasNext()) {
                    FieldNode nodex = (FieldNode) fieldIterator.next();
                    boolean isValidx = true;
                    AnnotationNode requirementsx = Annotations.getInvisible(nodex, MinecraftRequirement.class);
                    if (requirementsx != null) {
                        for (AnnotationNode versionRangex : (Iterable) Annotations.getValue(requirementsx)) {
                            if (!isVersionRangeValid(versionRangex)) {
                                fieldIterator.remove();
                                isValidx = false;
                                break;
                            }
                        }
                    }
                    if (isValidx) {
                        AnnotationNode annotationNode = Annotations.getVisible(nodex, Remap.class);
                        if (annotationNode != null && Annotations.getVisible(nodex, Shadow.class) == null) {
                            List<String> mappings = (List<String>) Annotations.getValue(annotationNode, MemoryLeakFixExpectPlatform.getMappingType());
                            if (mappings != null && mappings.size() >= 1) {
                                if (mappings.size() > 1) {
                                    throw new IllegalStateException("Fields cannot contain more than 1 remap! - " + className + " - " + nodex.name + nodex.desc);
                                }
                                String mapping = (String) mappings.get(0);
                                fieldsRemapping.put(nodex, mapping);
                                nodex.name = mapping;
                            }
                        }
                    }
                }
                if (!fieldsRemapping.isEmpty()) {
                    executeRemapAnnotation(className, classNode, fieldsRemapping);
                }
            }
        }
    }

    private static boolean areRequirementsMet(@Nullable AnnotationNode requirements) {
        if (requirements != null) {
            for (AnnotationNode versionRange : (Iterable) Annotations.getValue(requirements)) {
                if (isVersionRangeValid(versionRange)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    private static AnnotationNode getMinecraftRequirement(String mixinClassName) {
        try {
            return Annotations.getInvisible(MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName), MinecraftRequirement.class);
        } catch (Exception var2) {
            return null;
        }
    }

    private static boolean isVersionRangeValid(AnnotationNode versionRange) {
        String minVersion = (String) Annotations.getValue(versionRange, "minVersion");
        if (minVersion != null && !minVersion.isEmpty() && MemoryLeakFixExpectPlatform.compareMinecraftToVersion(minVersion) < 0) {
            return false;
        } else {
            String maxVersion = (String) Annotations.getValue(versionRange, "maxVersion");
            return maxVersion == null || maxVersion.isEmpty() || MemoryLeakFixExpectPlatform.compareMinecraftToVersion(maxVersion) <= 0;
        }
    }

    private static void executeRemapAnnotation(String className, ClassNode classNode, Map<FieldNode, String> fieldRemapping) {
        className = className.replace(".", "/");
        for (MethodNode methodNode : classNode.methods) {
            ListIterator<AbstractInsnNode> it = methodNode.instructions.iterator();
            while (it.hasNext()) {
                AbstractInsnNode insn = (AbstractInsnNode) it.next();
                if (insn instanceof FieldInsnNode && ((FieldInsnNode) insn).owner.equals(className)) {
                    FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                    for (Entry<FieldNode, String> entry : fieldRemapping.entrySet()) {
                        FieldNode node = (FieldNode) entry.getKey();
                        if (node.name.equals(fieldInsn.name) && node.desc.equals(fieldInsn.desc)) {
                            it.remove();
                            it.add(new FieldInsnNode(fieldInsn.getOpcode(), fieldInsn.owner, (String) entry.getValue(), fieldInsn.desc));
                        }
                    }
                }
            }
        }
    }

    private static void executeRemapAnnotation(MethodNode node, @Nullable AnnotationNode remap, boolean target) {
        if (remap != null) {
            boolean excludeDev = (Boolean) Annotations.getValue(remap, "excludeDev", Boolean.FALSE);
            if (!excludeDev || !MemoryLeakFixExpectPlatform.isDevEnvironment()) {
                if (areRequirementsMet((AnnotationNode) Annotations.getValue(remap, "mcVersions"))) {
                    List<String> mapping = (List<String>) Annotations.getValue(remap, MemoryLeakFixExpectPlatform.getMappingType());
                    if (mapping != null && !mapping.isEmpty()) {
                        if (target) {
                            remapMixinAnnotationTarget(node, mapping);
                        } else {
                            remapMixinAnnotation(node, mapping);
                        }
                    }
                }
            }
        }
    }

    private static void remapMixinAnnotation(MethodNode node, List<String> remapped) {
        AnnotationNode annotationNode = getMixinAnnotation(node);
        if (annotationNode != null) {
            for (int i = 0; i < annotationNode.values.size() - 1; i++) {
                Object obj = annotationNode.values.get(i);
                if (obj instanceof String && obj.equals("method")) {
                    List<String> methodList = (List<String>) annotationNode.values.get(i + 1);
                    annotationNode.values.set(i + 1, remapped);
                }
            }
        }
    }

    private static void remapMixinAnnotationTarget(MethodNode node, List<String> remapped) {
        AnnotationNode annotationNode = getMixinAnnotation(node);
        if (annotationNode != null) {
            int count = 0;
            for (int i = 0; i < annotationNode.values.size(); i++) {
                Object obj = annotationNode.values.get(i);
                if (obj instanceof At[]) {
                    At[] ats = (At[]) obj;
                    for (At at : ats) {
                        annotationNode.values.set(i, alterAtTarget(at, (String) remapped.get(count)));
                        count++;
                    }
                } else if (obj instanceof At) {
                    annotationNode.values.set(i, alterAtTarget((At) obj, (String) remapped.get(count)));
                    count++;
                }
            }
        }
    }

    @Nullable
    private static AnnotationNode getMixinAnnotation(MethodNode node) {
        return Annotations.getSingleVisible(node, new Class[] { Inject.class, Redirect.class, ModifyArg.class, ModifyArgs.class, ModifyConstant.class, ModifyVariable.class, ModifyExpressionValue.class, ModifyReceiver.class, ModifyReturnValue.class, WrapOperation.class, WrapWithCondition.class });
    }

    private static At alterAtTarget(At at, String target) {
        return new At() {

            public Class<? extends Annotation> annotationType() {
                return at.annotationType();
            }

            public String id() {
                return at.id();
            }

            public String value() {
                return at.value();
            }

            public String slice() {
                return at.slice();
            }

            public Shift shift() {
                return at.shift();
            }

            public int by() {
                return at.by();
            }

            public String[] args() {
                return at.args();
            }

            public String target() {
                return target;
            }

            public Desc desc() {
                return at.desc();
            }

            public int ordinal() {
                return at.ordinal();
            }

            public int opcode() {
                return at.opcode();
            }

            public boolean remap() {
                return at.remap();
            }
        };
    }
}