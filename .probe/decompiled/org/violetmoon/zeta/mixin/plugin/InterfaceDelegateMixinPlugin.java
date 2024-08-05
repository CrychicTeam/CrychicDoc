package org.violetmoon.zeta.mixin.plugin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.util.Annotations;

public class InterfaceDelegateMixinPlugin implements IMixinConfigPlugin {

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.RuntimeException: invalid constant type: Ljava/util/Map; with value V
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ConstExprent.toJava(ConstExprent.java:356)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.SwitchExprent.toJava(SwitchExprent.java:104)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1018)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.AssignmentExprent.toJava(AssignmentExprent.java:154)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:895)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:90)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.IfStatement.toJava(IfStatement.java:241)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.DoStatement.toJava(DoStatement.java:148)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.IfStatement.toJava(IfStatement.java:241)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 000: invokestatic com/google/common/collect/HashMultimap.create ()Lcom/google/common/collect/HashMultimap;
        // 003: astore 5
        // 005: aload 4
        // 007: bipush 0
        // 008: invokeinterface org/spongepowered/asm/mixin/extensibility/IMixinInfo.getClassNode (I)Lorg/objectweb/asm/tree/ClassNode; 2
        // 00d: astore 6
        // 00f: aload 1
        // 010: ldc "."
        // 012: ldc "/"
        // 014: invokevirtual java/lang/String.replace (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        // 017: astore 7
        // 019: aload 6
        // 01b: ldc org/violetmoon/zeta/mixin/plugin/DelegateInterfaceMixin
        // 01d: invokestatic org/spongepowered/asm/util/Annotations.getVisible (Lorg/objectweb/asm/tree/ClassNode;Ljava/lang/Class;)Lorg/objectweb/asm/tree/AnnotationNode;
        // 020: astore 8
        // 022: aload 8
        // 024: ifnull 330
        // 027: aload 6
        // 029: getfield org/objectweb/asm/tree/ClassNode.access I
        // 02c: sipush 512
        // 02f: iand
        // 030: ifeq 330
        // 033: aload 8
        // 035: ldc "delegate"
        // 037: invokestatic org/spongepowered/asm/util/Annotations.getValue (Lorg/objectweb/asm/tree/AnnotationNode;Ljava/lang/String;)Ljava/lang/Object;
        // 03a: checkcast org/objectweb/asm/Type
        // 03d: invokevirtual org/objectweb/asm/Type.getInternalName ()Ljava/lang/String;
        // 040: astore 9
        // 042: aload 8
        // 044: ldc "methods"
        // 046: invokestatic org/spongepowered/asm/util/Annotations.getValue (Lorg/objectweb/asm/tree/AnnotationNode;Ljava/lang/String;)Ljava/lang/Object;
        // 049: checkcast java/util/List
        // 04c: astore 10
        // 04e: aload 10
        // 050: invokeinterface java/util/List.iterator ()Ljava/util/Iterator; 1
        // 055: astore 11
        // 057: aload 11
        // 059: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 05e: ifeq 0cc
        // 061: aload 11
        // 063: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 068: checkcast org/objectweb/asm/tree/AnnotationNode
        // 06b: astore 12
        // 06d: aload 12
        // 06f: ldc "target"
        // 071: bipush 0
        // 072: invokestatic org/spongepowered/asm/util/Annotations.getValue (Lorg/objectweb/asm/tree/AnnotationNode;Ljava/lang/String;Z)Ljava/util/List;
        // 075: astore 13
        // 077: aload 12
        // 079: ldc "delegate"
        // 07b: invokestatic org/spongepowered/asm/util/Annotations.getValue (Lorg/objectweb/asm/tree/AnnotationNode;Ljava/lang/String;)Ljava/lang/Object;
        // 07e: checkcast java/lang/String
        // 081: astore 14
        // 083: aload 12
        // 085: ldc "desc"
        // 087: invokestatic org/spongepowered/asm/util/Annotations.getValue (Lorg/objectweb/asm/tree/AnnotationNode;Ljava/lang/String;)Ljava/lang/Object;
        // 08a: checkcast java/lang/String
        // 08d: astore 15
        // 08f: aload 13
        // 091: invokeinterface java/util/List.iterator ()Ljava/util/Iterator; 1
        // 096: astore 16
        // 098: aload 16
        // 09a: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 09f: ifeq 0c9
        // 0a2: aload 16
        // 0a4: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 0a9: checkcast java/lang/String
        // 0ac: astore 17
        // 0ae: aload 5
        // 0b0: aload 17
        // 0b2: new org/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$MethodReference
        // 0b5: dup
        // 0b6: aload 14
        // 0b8: aload 15
        // 0ba: invokestatic org/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor.tokenize (Ljava/lang/String;)Lorg/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor;
        // 0bd: invokespecial org/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$MethodReference.<init> (Ljava/lang/String;Lorg/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor;)V
        // 0c0: invokeinterface com/google/common/collect/Multimap.put (Ljava/lang/Object;Ljava/lang/Object;)Z 3
        // 0c5: pop
        // 0c6: goto 098
        // 0c9: goto 057
        // 0cc: aload 2
        // 0cd: getfield org/objectweb/asm/tree/ClassNode.methods Ljava/util/List;
        // 0d0: invokeinterface java/util/List.iterator ()Ljava/util/Iterator; 1
        // 0d5: astore 11
        // 0d7: aload 11
        // 0d9: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 0de: ifeq 330
        // 0e1: aload 11
        // 0e3: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 0e8: checkcast org/objectweb/asm/tree/MethodNode
        // 0eb: astore 12
        // 0ed: aload 12
        // 0ef: getfield org/objectweb/asm/tree/MethodNode.name Ljava/lang/String;
        // 0f2: aload 12
        // 0f4: getfield org/objectweb/asm/tree/MethodNode.desc Ljava/lang/String;
        // 0f7: invokedynamic makeConcatWithConstants (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; bsm=java/lang/invoke/StringConcatFactory.makeConcatWithConstants (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ "\u0001\u0001" ]
        // 0fc: astore 13
        // 0fe: aload 5
        // 100: aload 13
        // 102: invokeinterface com/google/common/collect/Multimap.containsKey (Ljava/lang/Object;)Z 2
        // 107: ifne 111
        // 10a: aload 12
        // 10c: getfield org/objectweb/asm/tree/MethodNode.name Ljava/lang/String;
        // 10f: astore 13
        // 111: aload 5
        // 113: aload 13
        // 115: invokeinterface com/google/common/collect/Multimap.containsKey (Ljava/lang/Object;)Z 2
        // 11a: ifeq 32d
        // 11d: aload 12
        // 11f: getfield org/objectweb/asm/tree/MethodNode.desc Ljava/lang/String;
        // 122: invokestatic org/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor.tokenize (Ljava/lang/String;)Lorg/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor;
        // 125: astore 14
        // 127: aload 14
        // 129: getfield org/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor.ret Ljava/lang/String;
        // 12c: astore 16
        // 12e: bipush -1
        // 12f: istore 17
        // 131: aload 16
        // 133: invokevirtual java/lang/String.hashCode ()I
        // 136: tableswitch 258 66 90 178 162 244 258 227 258 258 130 210 258 258 258 258 258 258 258 258 146 258 258 114 258 258 258 194
        // 1a8: aload 16
        // 1aa: ldc "V"
        // 1ac: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1af: ifeq 238
        // 1b2: bipush 0
        // 1b3: istore 17
        // 1b5: goto 238
        // 1b8: aload 16
        // 1ba: ldc "I"
        // 1bc: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1bf: ifeq 238
        // 1c2: bipush 1
        // 1c3: istore 17
        // 1c5: goto 238
        // 1c8: aload 16
        // 1ca: ldc "S"
        // 1cc: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1cf: ifeq 238
        // 1d2: bipush 2
        // 1d3: istore 17
        // 1d5: goto 238
        // 1d8: aload 16
        // 1da: ldc "C"
        // 1dc: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1df: ifeq 238
        // 1e2: bipush 3
        // 1e3: istore 17
        // 1e5: goto 238
        // 1e8: aload 16
        // 1ea: ldc "B"
        // 1ec: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1ef: ifeq 238
        // 1f2: bipush 4
        // 1f3: istore 17
        // 1f5: goto 238
        // 1f8: aload 16
        // 1fa: ldc "Z"
        // 1fc: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1ff: ifeq 238
        // 202: bipush 5
        // 203: istore 17
        // 205: goto 238
        // 208: aload 16
        // 20a: ldc "J"
        // 20c: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 20f: ifeq 238
        // 212: bipush 6
        // 214: istore 17
        // 216: goto 238
        // 219: aload 16
        // 21b: ldc "F"
        // 21d: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 220: ifeq 238
        // 223: bipush 7
        // 225: istore 17
        // 227: goto 238
        // 22a: aload 16
        // 22c: ldc "D"
        // 22e: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 231: ifeq 238
        // 234: bipush 8
        // 236: istore 17
        // 238: iload 17
        // 23a: tableswitch 80 0 8 50 56 56 56 56 56 62 68 74
        // 26c: sipush 177
        // 26f: goto 28d
        // 272: sipush 172
        // 275: goto 28d
        // 278: sipush 173
        // 27b: goto 28d
        // 27e: sipush 174
        // 281: goto 28d
        // 284: sipush 175
        // 287: goto 28d
        // 28a: sipush 176
        // 28d: istore 15
        // 28f: new java/util/HashMap
        // 292: dup
        // 293: invokespecial java/util/HashMap.<init> ()V
        // 296: astore 16
        // 298: aload 5
        // 29a: aload 13
        // 29c: invokeinterface com/google/common/collect/Multimap.get (Ljava/lang/Object;)Ljava/util/Collection; 2
        // 2a1: astore 17
        // 2a3: bipush 0
        // 2a4: istore 18
        // 2a6: iload 18
        // 2a8: aload 12
        // 2aa: getfield org/objectweb/asm/tree/MethodNode.instructions Lorg/objectweb/asm/tree/InsnList;
        // 2ad: invokevirtual org/objectweb/asm/tree/InsnList.size ()I
        // 2b0: if_icmpge 2f0
        // 2b3: aload 12
        // 2b5: getfield org/objectweb/asm/tree/MethodNode.instructions Lorg/objectweb/asm/tree/InsnList;
        // 2b8: iload 18
        // 2ba: invokevirtual org/objectweb/asm/tree/InsnList.get (I)Lorg/objectweb/asm/tree/AbstractInsnNode;
        // 2bd: astore 19
        // 2bf: aload 19
        // 2c1: instanceof org/objectweb/asm/tree/InsnNode
        // 2c4: ifeq 2ea
        // 2c7: aload 19
        // 2c9: checkcast org/objectweb/asm/tree/InsnNode
        // 2cc: astore 20
        // 2ce: aload 19
        // 2d0: invokevirtual org/objectweb/asm/tree/AbstractInsnNode.getOpcode ()I
        // 2d3: iload 15
        // 2d5: if_icmpne 2ea
        // 2d8: aload 0
        // 2d9: aload 9
        // 2db: aload 7
        // 2dd: aload 12
        // 2df: aload 14
        // 2e1: aload 20
        // 2e3: aload 17
        // 2e5: aload 16
        // 2e7: invokevirtual org/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin.buildTransforms (Ljava/lang/String;Ljava/lang/String;Lorg/objectweb/asm/tree/MethodNode;Lorg/violetmoon/zeta/mixin/plugin/InterfaceDelegateMixinPlugin$Descriptor;Lorg/objectweb/asm/tree/InsnNode;Ljava/util/Collection;Ljava/util/Map;)V
        // 2ea: iinc 18 1
        // 2ed: goto 2a6
        // 2f0: aload 16
        // 2f2: invokeinterface java/util/Map.keySet ()Ljava/util/Set; 1
        // 2f7: invokeinterface java/util/Set.iterator ()Ljava/util/Iterator; 1
        // 2fc: astore 18
        // 2fe: aload 18
        // 300: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 305: ifeq 32d
        // 308: aload 18
        // 30a: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 30f: checkcast org/objectweb/asm/tree/InsnNode
        // 312: astore 19
        // 314: aload 12
        // 316: getfield org/objectweb/asm/tree/MethodNode.instructions Lorg/objectweb/asm/tree/InsnList;
        // 319: aload 19
        // 31b: aload 16
        // 31d: aload 19
        // 31f: invokeinterface java/util/Map.get (Ljava/lang/Object;)Ljava/lang/Object; 2
        // 324: checkcast org/objectweb/asm/tree/InsnList
        // 327: invokevirtual org/objectweb/asm/tree/InsnList.insertBefore (Lorg/objectweb/asm/tree/AbstractInsnNode;Lorg/objectweb/asm/tree/InsnList;)V
        // 32a: goto 2fe
        // 32d: goto 0d7
        // 330: return
    }

    private void buildTransforms(String callInvoker, String owner, MethodNode target, InterfaceDelegateMixinPlugin.Descriptor targetDescriptor, InsnNode returnNode, Collection<InterfaceDelegateMixinPlugin.MethodReference> transformers, Map<InsnNode, InsnList> transformations) {
        String ownerType = "L" + owner + ";";
        boolean isStatic = (target.access & 8) != 0;
        for (InterfaceDelegateMixinPlugin.MethodReference transformer : transformers) {
            InterfaceDelegateMixinPlugin.Descriptor transformerDescriptor = transformer.descriptor;
            InsnList transformation = this.foldTransformerParams(ownerType, isStatic, targetDescriptor, transformerDescriptor);
            if (transformation != null) {
                transformation.add(new MethodInsnNode(184, callInvoker, transformer.name, transformer.descriptor.toString()));
                if (transformations.containsKey(returnNode)) {
                    ((InsnList) transformations.get(returnNode)).add(transformation);
                } else {
                    transformations.put(returnNode, transformation);
                }
            }
        }
    }

    private InsnList foldTransformerParams(String ownerType, boolean isStatic, InterfaceDelegateMixinPlugin.Descriptor target, InterfaceDelegateMixinPlugin.Descriptor transformer) {
        if (!target.ret.equals(transformer.ret)) {
            return null;
        } else if (transformer.params.length == 0 || !transformer.params[0].equals(target.ret)) {
            return null;
        } else if (transformer.params.length == 1) {
            return new InsnList();
        } else {
            InsnList localVarsToLoad = new InsnList();
            boolean firstWasThis = false;
            int lvtIndex = isStatic ? 0 : 1;
            for (int i = 0; i < transformer.params.length - 1; i++) {
                String transformerParam = transformer.params[i + 1];
                if (i == 0 && !isStatic && transformerParam.equals(ownerType)) {
                    firstWasThis = true;
                    localVarsToLoad.add(new VarInsnNode(25, 0));
                } else {
                    int targetIndex = firstWasThis ? i - 1 : i;
                    if (targetIndex >= target.params.length) {
                        return null;
                    }
                    if (transformerParam.charAt(0) != target.params[targetIndex].charAt(0)) {
                        return null;
                    }
                    int opcode = switch(transformerParam) {
                        case "I", "S", "C", "B", "Z" ->
                            21;
                        case "J" ->
                            22;
                        case "F" ->
                            23;
                        case "D" ->
                            24;
                        default ->
                            25;
                    };
                    localVarsToLoad.add(new VarInsnNode(opcode, lvtIndex++));
                    if (opcode == 22 || opcode == 24) {
                        lvtIndex++;
                    }
                }
            }
            return localVarsToLoad;
        }
    }

    private static record Descriptor(String[] params, String ret) {

        private static final String END_TOKEN = "ISCBZJFD";

        public String toString() {
            StringBuilder out = new StringBuilder("(");
            for (String param : this.params) {
                out.append(param);
            }
            out.append(')');
            out.append(this.ret);
            return out.toString();
        }

        private static InterfaceDelegateMixinPlugin.Descriptor tokenize(String desc) {
            if (desc.length() >= 3 && desc.charAt(0) == '(') {
                List<String> parameters = new ArrayList();
                String returnType = "V";
                int pointer = 1;
                boolean parsingReturnType = false;
                boolean parsingClassType = false;
                StringBuilder collected = new StringBuilder();
                while (pointer < desc.length()) {
                    char charAt = desc.charAt(pointer++);
                    if (!parsingClassType && "ISCBZJFD".indexOf(charAt) >= 0 || parsingClassType && charAt == ';') {
                        collected.append(charAt);
                        if (parsingReturnType) {
                            returnType = collected.toString();
                        } else {
                            parameters.add(collected.toString());
                        }
                        parsingClassType = false;
                        collected = new StringBuilder();
                    } else if (charAt == ')') {
                        parsingReturnType = true;
                    } else {
                        if (charAt == 'L') {
                            parsingClassType = true;
                        }
                        collected.append(charAt);
                    }
                }
                return new InterfaceDelegateMixinPlugin.Descriptor((String[]) parameters.toArray(new String[0]), returnType);
            } else {
                return new InterfaceDelegateMixinPlugin.Descriptor(new String[0], "V");
            }
        }
    }

    private static record MethodReference(String name, InterfaceDelegateMixinPlugin.Descriptor descriptor) {
    }
}