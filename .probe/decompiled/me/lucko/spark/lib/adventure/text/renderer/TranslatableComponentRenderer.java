package me.lucko.spark.lib.adventure.text.renderer;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import me.lucko.spark.lib.adventure.text.BlockNBTComponent;
import me.lucko.spark.lib.adventure.text.BuildableComponent;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.ComponentBuilder;
import me.lucko.spark.lib.adventure.text.EntityNBTComponent;
import me.lucko.spark.lib.adventure.text.KeybindComponent;
import me.lucko.spark.lib.adventure.text.NBTComponent;
import me.lucko.spark.lib.adventure.text.NBTComponentBuilder;
import me.lucko.spark.lib.adventure.text.ScoreComponent;
import me.lucko.spark.lib.adventure.text.SelectorComponent;
import me.lucko.spark.lib.adventure.text.StorageNBTComponent;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.TranslatableComponent;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TranslatableComponentRenderer<C> extends AbstractComponentRenderer<C> {

    private static final Set<Style.Merge> MERGES = Style.Merge.merges(Style.Merge.COLOR, Style.Merge.DECORATIONS, Style.Merge.INSERTION, Style.Merge.FONT);

    @NotNull
    public static TranslatableComponentRenderer<Locale> usingTranslationSource(@NotNull final Translator source) {
        Objects.requireNonNull(source, "source");
        return new TranslatableComponentRenderer<Locale>() {

            @Nullable
            protected MessageFormat translate(@NotNull final String key, @NotNull final Locale context) {
                return source.translate(key, context);
            }
        };
    }

    @Nullable
    protected abstract MessageFormat translate(@NotNull final String key, @NotNull final C context);

    @NotNull
    @Override
    protected Component renderBlockNbt(@NotNull final BlockNBTComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.Spliterator.getExactSizeIfKnown(Spliterator.java:414)
        //   at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:526)
        //   at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //   at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //   at java.base/java.util.stream.ReferencePipeline.anyMatch(ReferencePipeline.java:632)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: aload 0
        // 01: aload 2
        // 02: invokestatic me/lucko/spark/lib/adventure/text/Component.blockNBT ()Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder;
        // 05: aload 1
        // 06: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.nbt (Ljava/lang/Object;Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;Lme/lucko/spark/lib/adventure/text/NBTComponent;)Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;
        // 09: checkcast me/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder
        // 0c: aload 1
        // 0d: invokeinterface me/lucko/spark/lib/adventure/text/BlockNBTComponent.pos ()Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Pos; 1
        // 12: invokeinterface me/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder.pos (Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Pos;)Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder; 2
        // 17: astore 3
        // 18: aload 0
        // 19: aload 1
        // 1a: aload 3
        // 1b: aload 2
        // 1c: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 1f: areturn
    }

    @NotNull
    @Override
    protected Component renderEntityNbt(@NotNull final EntityNBTComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:526)
        //   at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //   at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //   at java.base/java.util.stream.ReferencePipeline.anyMatch(ReferencePipeline.java:632)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: aload 0
        // 01: aload 2
        // 02: invokestatic me/lucko/spark/lib/adventure/text/Component.entityNBT ()Lme/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder;
        // 05: aload 1
        // 06: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.nbt (Ljava/lang/Object;Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;Lme/lucko/spark/lib/adventure/text/NBTComponent;)Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;
        // 09: checkcast me/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder
        // 0c: aload 1
        // 0d: invokeinterface me/lucko/spark/lib/adventure/text/EntityNBTComponent.selector ()Ljava/lang/String; 1
        // 12: invokeinterface me/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder.selector (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder; 2
        // 17: astore 3
        // 18: aload 0
        // 19: aload 1
        // 1a: aload 3
        // 1b: aload 2
        // 1c: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 1f: areturn
    }

    @NotNull
    @Override
    protected Component renderStorageNbt(@NotNull final StorageNBTComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.Collections$2.tryAdvance(Collections.java:4853)
        //   at java.base/java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        //   at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        //   at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        //   at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //   at java.base/java.util.stream.ReferencePipeline.anyMatch(ReferencePipeline.java:632)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: aload 0
        // 01: aload 2
        // 02: invokestatic me/lucko/spark/lib/adventure/text/Component.storageNBT ()Lme/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder;
        // 05: aload 1
        // 06: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.nbt (Ljava/lang/Object;Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;Lme/lucko/spark/lib/adventure/text/NBTComponent;)Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;
        // 09: checkcast me/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder
        // 0c: aload 1
        // 0d: invokeinterface me/lucko/spark/lib/adventure/text/StorageNBTComponent.storage ()Lme/lucko/spark/lib/adventure/key/Key; 1
        // 12: invokeinterface me/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder.storage (Lme/lucko/spark/lib/adventure/key/Key;)Lme/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder; 2
        // 17: astore 3
        // 18: aload 0
        // 19: aload 1
        // 1a: aload 3
        // 1b: aload 2
        // 1c: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 1f: areturn
    }

    protected <O extends NBTComponent<O, B>, B extends NBTComponentBuilder<O, B>> B nbt(@NotNull final C context, final B builder, final O oldComponent) {
        builder.nbtPath(oldComponent.nbtPath()).interpret(oldComponent.interpret());
        Component separator = oldComponent.separator();
        if (separator != null) {
            builder.separator(this.render(separator, context));
        }
        return builder;
    }

    @NotNull
    @Override
    protected Component renderKeybind(@NotNull final KeybindComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: invokestatic me/lucko/spark/lib/adventure/text/Component.keybind ()Lme/lucko/spark/lib/adventure/text/KeybindComponent$Builder;
        // 03: aload 1
        // 04: invokeinterface me/lucko/spark/lib/adventure/text/KeybindComponent.keybind ()Ljava/lang/String; 1
        // 09: invokeinterface me/lucko/spark/lib/adventure/text/KeybindComponent$Builder.keybind (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/KeybindComponent$Builder; 2
        // 0e: astore 3
        // 0f: aload 0
        // 10: aload 1
        // 11: aload 3
        // 12: aload 2
        // 13: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 16: areturn
    }

    @NotNull
    @Override
    protected Component renderScore(@NotNull final ScoreComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: invokestatic me/lucko/spark/lib/adventure/text/Component.score ()Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder;
        // 03: aload 1
        // 04: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent.name ()Ljava/lang/String; 1
        // 09: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.name (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 0e: aload 1
        // 0f: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent.objective ()Ljava/lang/String; 1
        // 14: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.objective (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 19: aload 1
        // 1a: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent.value ()Ljava/lang/String; 1
        // 1f: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.value (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 24: astore 3
        // 25: aload 0
        // 26: aload 1
        // 27: aload 3
        // 28: aload 2
        // 29: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 2c: areturn
    }

    @NotNull
    @Override
    protected Component renderSelector(@NotNull final SelectorComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: invokestatic me/lucko/spark/lib/adventure/text/Component.selector ()Lme/lucko/spark/lib/adventure/text/SelectorComponent$Builder;
        // 03: aload 1
        // 04: invokeinterface me/lucko/spark/lib/adventure/text/SelectorComponent.pattern ()Ljava/lang/String; 1
        // 09: invokeinterface me/lucko/spark/lib/adventure/text/SelectorComponent$Builder.pattern (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/SelectorComponent$Builder; 2
        // 0e: astore 3
        // 0f: aload 0
        // 10: aload 1
        // 11: aload 3
        // 12: aload 2
        // 13: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 16: areturn
    }

    @NotNull
    @Override
    protected Component renderText(@NotNull final TextComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getCurrentContext(DecompilerContext.java:67)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.DecompilerContext.getStructContext(DecompilerContext.java:137)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 00: invokestatic me/lucko/spark/lib/adventure/text/Component.text ()Lme/lucko/spark/lib/adventure/text/TextComponent$Builder;
        // 03: aload 1
        // 04: invokeinterface me/lucko/spark/lib/adventure/text/TextComponent.content ()Ljava/lang/String; 1
        // 09: invokeinterface me/lucko/spark/lib/adventure/text/TextComponent$Builder.content (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TextComponent$Builder; 2
        // 0e: astore 3
        // 0f: aload 0
        // 10: aload 1
        // 11: aload 3
        // 12: aload 2
        // 13: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 16: areturn
    }

    @NotNull
    @Override
    protected Component renderTranslatable(@NotNull final TranslatableComponent component, @NotNull final C context) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        //   at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //   at java.base/java.util.stream.ReferencePipeline.anyMatch(ReferencePipeline.java:632)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$20(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 000: aload 0
        // 001: aload 1
        // 002: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.key ()Ljava/lang/String; 1
        // 007: aload 2
        // 008: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.translate (Ljava/lang/String;Ljava/lang/Object;)Ljava/text/MessageFormat;
        // 00b: astore 3
        // 00c: aload 3
        // 00d: ifnonnull 084
        // 010: invokestatic me/lucko/spark/lib/adventure/text/Component.translatable ()Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder;
        // 013: aload 1
        // 014: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.key ()Ljava/lang/String; 1
        // 019: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent$Builder.key (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder; 2
        // 01e: astore 4
        // 020: aload 1
        // 021: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.args ()Ljava/util/List; 1
        // 026: invokeinterface java/util/List.isEmpty ()Z 1
        // 02b: ifne 07b
        // 02e: new java/util/ArrayList
        // 031: dup
        // 032: aload 1
        // 033: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.args ()Ljava/util/List; 1
        // 038: invokespecial java/util/ArrayList.<init> (Ljava/util/Collection;)V
        // 03b: astore 5
        // 03d: bipush 0
        // 03e: istore 6
        // 040: aload 5
        // 042: invokeinterface java/util/List.size ()I 1
        // 047: istore 7
        // 049: iload 6
        // 04b: iload 7
        // 04d: if_icmpge 071
        // 050: aload 5
        // 052: iload 6
        // 054: aload 0
        // 055: aload 5
        // 057: iload 6
        // 059: invokeinterface java/util/List.get (I)Ljava/lang/Object; 2
        // 05e: checkcast me/lucko/spark/lib/adventure/text/Component
        // 061: aload 2
        // 062: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.render (Lme/lucko/spark/lib/adventure/text/Component;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/Component;
        // 065: invokeinterface java/util/List.set (ILjava/lang/Object;)Ljava/lang/Object; 3
        // 06a: pop
        // 06b: iinc 6 1
        // 06e: goto 049
        // 071: aload 4
        // 073: aload 5
        // 075: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent$Builder.args (Ljava/util/List;)Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder; 2
        // 07a: pop
        // 07b: aload 0
        // 07c: aload 1
        // 07d: aload 4
        // 07f: aload 2
        // 080: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyleAndOptionallyDeepRender (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 083: areturn
        // 084: aload 1
        // 085: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.args ()Ljava/util/List; 1
        // 08a: astore 4
        // 08c: invokestatic me/lucko/spark/lib/adventure/text/Component.text ()Lme/lucko/spark/lib/adventure/text/TextComponent$Builder;
        // 08f: astore 5
        // 091: aload 0
        // 092: aload 1
        // 093: aload 5
        // 095: aload 2
        // 096: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.mergeStyle (Lme/lucko/spark/lib/adventure/text/Component;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)V
        // 099: aload 4
        // 09b: invokeinterface java/util/List.isEmpty ()Z 1
        // 0a0: ifeq 0c9
        // 0a3: aload 5
        // 0a5: aload 3
        // 0a6: aconst_null
        // 0a7: new java/lang/StringBuffer
        // 0aa: dup
        // 0ab: invokespecial java/lang/StringBuffer.<init> ()V
        // 0ae: aconst_null
        // 0af: invokevirtual java/text/MessageFormat.format ([Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/text/FieldPosition;)Ljava/lang/StringBuffer;
        // 0b2: invokevirtual java/lang/StringBuffer.toString ()Ljava/lang/String;
        // 0b5: invokeinterface me/lucko/spark/lib/adventure/text/TextComponent$Builder.content (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TextComponent$Builder; 2
        // 0ba: pop
        // 0bb: aload 0
        // 0bc: aload 1
        // 0bd: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.children ()Ljava/util/List; 1
        // 0c2: aload 5
        // 0c4: aload 2
        // 0c5: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.optionallyRenderChildrenAppendAndBuild (Ljava/util/List;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 0c8: areturn
        // 0c9: aload 4
        // 0cb: invokeinterface java/util/List.size ()I 1
        // 0d0: anewarray 295
        // 0d3: astore 6
        // 0d5: aload 3
        // 0d6: aload 6
        // 0d8: new java/lang/StringBuffer
        // 0db: dup
        // 0dc: invokespecial java/lang/StringBuffer.<init> ()V
        // 0df: aconst_null
        // 0e0: invokevirtual java/text/MessageFormat.format ([Ljava/lang/Object;Ljava/lang/StringBuffer;Ljava/text/FieldPosition;)Ljava/lang/StringBuffer;
        // 0e3: astore 7
        // 0e5: aload 3
        // 0e6: aload 6
        // 0e8: invokevirtual java/text/MessageFormat.formatToCharacterIterator (Ljava/lang/Object;)Ljava/text/AttributedCharacterIterator;
        // 0eb: astore 8
        // 0ed: aload 8
        // 0ef: invokeinterface java/text/AttributedCharacterIterator.getIndex ()I 1
        // 0f4: aload 8
        // 0f6: invokeinterface java/text/AttributedCharacterIterator.getEndIndex ()I 1
        // 0fb: if_icmpge 160
        // 0fe: aload 8
        // 100: invokeinterface java/text/AttributedCharacterIterator.getRunLimit ()I 1
        // 105: istore 9
        // 107: aload 8
        // 109: getstatic java/text/MessageFormat$Field.ARGUMENT Ljava/text/MessageFormat$Field;
        // 10c: invokeinterface java/text/AttributedCharacterIterator.getAttribute (Ljava/text/AttributedCharacterIterator$Attribute;)Ljava/lang/Object; 2
        // 111: checkcast java/lang/Integer
        // 114: astore 10
        // 116: aload 10
        // 118: ifnull 13a
        // 11b: aload 5
        // 11d: aload 0
        // 11e: aload 4
        // 120: aload 10
        // 122: invokevirtual java/lang/Integer.intValue ()I
        // 125: invokeinterface java/util/List.get (I)Ljava/lang/Object; 2
        // 12a: checkcast me/lucko/spark/lib/adventure/text/Component
        // 12d: aload 2
        // 12e: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.render (Lme/lucko/spark/lib/adventure/text/Component;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/Component;
        // 131: invokeinterface me/lucko/spark/lib/adventure/text/TextComponent$Builder.append (Lme/lucko/spark/lib/adventure/text/Component;)Lme/lucko/spark/lib/adventure/text/ComponentBuilder; 2
        // 136: pop
        // 137: goto 153
        // 13a: aload 5
        // 13c: aload 7
        // 13e: aload 8
        // 140: invokeinterface java/text/AttributedCharacterIterator.getIndex ()I 1
        // 145: iload 9
        // 147: invokevirtual java/lang/StringBuffer.substring (II)Ljava/lang/String;
        // 14a: invokestatic me/lucko/spark/lib/adventure/text/Component.text (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TextComponent;
        // 14d: invokeinterface me/lucko/spark/lib/adventure/text/TextComponent$Builder.append (Lme/lucko/spark/lib/adventure/text/Component;)Lme/lucko/spark/lib/adventure/text/ComponentBuilder; 2
        // 152: pop
        // 153: aload 8
        // 155: iload 9
        // 157: invokeinterface java/text/AttributedCharacterIterator.setIndex (I)C 2
        // 15c: pop
        // 15d: goto 0ed
        // 160: aload 0
        // 161: aload 1
        // 162: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent.children ()Ljava/util/List; 1
        // 167: aload 5
        // 169: aload 2
        // 16a: invokevirtual me/lucko/spark/lib/adventure/text/renderer/TranslatableComponentRenderer.optionallyRenderChildrenAppendAndBuild (Ljava/util/List;Lme/lucko/spark/lib/adventure/text/ComponentBuilder;Ljava/lang/Object;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 16d: areturn
    }

    protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O mergeStyleAndOptionallyDeepRender(final Component component, final B builder, final C context) {
        this.mergeStyle(component, builder, context);
        return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
    }

    protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O optionallyRenderChildrenAppendAndBuild(final List<Component> children, final B builder, final C context) {
        if (!children.isEmpty()) {
            children.forEach(child -> builder.append(this.render(child, context)));
        }
        return builder.build();
    }

    protected <B extends ComponentBuilder<?, ?>> void mergeStyle(final Component component, final B builder, final C context) {
        builder.mergeStyle(component, MERGES);
        builder.clickEvent(component.clickEvent());
        HoverEvent<?> hoverEvent = component.hoverEvent();
        if (hoverEvent != null) {
            builder.hoverEvent(hoverEvent.withRenderedValue(this, context));
        }
    }
}