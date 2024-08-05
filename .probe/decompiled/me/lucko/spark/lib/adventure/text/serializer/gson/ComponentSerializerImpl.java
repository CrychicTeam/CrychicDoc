package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import me.lucko.spark.lib.adventure.key.Key;
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
import org.jetbrains.annotations.Nullable;

final class ComponentSerializerImpl extends TypeAdapter<Component> {

    static final String TEXT = "text";

    static final String TRANSLATE = "translate";

    static final String TRANSLATE_WITH = "with";

    static final String SCORE = "score";

    static final String SCORE_NAME = "name";

    static final String SCORE_OBJECTIVE = "objective";

    static final String SCORE_VALUE = "value";

    static final String SELECTOR = "selector";

    static final String KEYBIND = "keybind";

    static final String EXTRA = "extra";

    static final String NBT = "nbt";

    static final String NBT_INTERPRET = "interpret";

    static final String NBT_BLOCK = "block";

    static final String NBT_ENTITY = "entity";

    static final String NBT_STORAGE = "storage";

    static final String SEPARATOR = "separator";

    static final Type COMPONENT_LIST_TYPE = (new TypeToken<List<Component>>() {
    }).getType();

    private final Gson gson;

    static TypeAdapter<Component> create(final Gson gson) {
        return new ComponentSerializerImpl(gson).nullSafe();
    }

    private ComponentSerializerImpl(final Gson gson) {
        this.gson = gson;
    }

    public BuildableComponent<?, ?> read(final JsonReader in) throws IOException {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.StackOverflowError
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.getClass(StructContext.java:77)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.StructContext.instanceOf(StructContext.java:282)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$18(InvocationExprent.java:1598)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.lambda$isMappingInBounds$19(InvocationExprent.java:1648)
        //
        // Bytecode:
        // 000: aload 1
        // 001: invokevirtual com/google/gson/stream/JsonReader.peek ()Lcom/google/gson/stream/JsonToken;
        // 004: astore 2
        // 005: aload 2
        // 006: getstatic com/google/gson/stream/JsonToken.STRING Lcom/google/gson/stream/JsonToken;
        // 009: if_acmpeq 01a
        // 00c: aload 2
        // 00d: getstatic com/google/gson/stream/JsonToken.NUMBER Lcom/google/gson/stream/JsonToken;
        // 010: if_acmpeq 01a
        // 013: aload 2
        // 014: getstatic com/google/gson/stream/JsonToken.BOOLEAN Lcom/google/gson/stream/JsonToken;
        // 017: if_acmpne 022
        // 01a: aload 1
        // 01b: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.readString (Lcom/google/gson/stream/JsonReader;)Ljava/lang/String;
        // 01e: invokestatic me/lucko/spark/lib/adventure/text/Component.text (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TextComponent;
        // 021: areturn
        // 022: aload 2
        // 023: getstatic com/google/gson/stream/JsonToken.BEGIN_ARRAY Lcom/google/gson/stream/JsonToken;
        // 026: if_acmpne 06f
        // 029: aconst_null
        // 02a: astore 3
        // 02b: aload 1
        // 02c: invokevirtual com/google/gson/stream/JsonReader.beginArray ()V
        // 02f: aload 1
        // 030: invokevirtual com/google/gson/stream/JsonReader.hasNext ()Z
        // 033: ifeq 058
        // 036: aload 0
        // 037: aload 1
        // 038: invokevirtual me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.read (Lcom/google/gson/stream/JsonReader;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 03b: astore 4
        // 03d: aload 3
        // 03e: ifnonnull 04c
        // 041: aload 4
        // 043: invokeinterface me/lucko/spark/lib/adventure/text/BuildableComponent.toBuilder ()Lme/lucko/spark/lib/adventure/text/ComponentBuilder; 1
        // 048: astore 3
        // 049: goto 055
        // 04c: aload 3
        // 04d: aload 4
        // 04f: invokeinterface me/lucko/spark/lib/adventure/text/ComponentBuilder.append (Lme/lucko/spark/lib/adventure/text/Component;)Lme/lucko/spark/lib/adventure/text/ComponentBuilder; 2
        // 054: pop
        // 055: goto 02f
        // 058: aload 3
        // 059: ifnonnull 064
        // 05c: aload 1
        // 05d: invokevirtual com/google/gson/stream/JsonReader.getPath ()Ljava/lang/String;
        // 060: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.notSureHowToDeserialize (Ljava/lang/Object;)Lcom/google/gson/JsonParseException;
        // 063: athrow
        // 064: aload 1
        // 065: invokevirtual com/google/gson/stream/JsonReader.endArray ()V
        // 068: aload 3
        // 069: invokeinterface me/lucko/spark/lib/adventure/text/ComponentBuilder.build ()Lme/lucko/spark/lib/adventure/text/BuildableComponent; 1
        // 06e: areturn
        // 06f: aload 2
        // 070: getstatic com/google/gson/stream/JsonToken.BEGIN_OBJECT Lcom/google/gson/stream/JsonToken;
        // 073: if_acmpeq 07e
        // 076: aload 1
        // 077: invokevirtual com/google/gson/stream/JsonReader.getPath ()Ljava/lang/String;
        // 07a: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.notSureHowToDeserialize (Ljava/lang/Object;)Lcom/google/gson/JsonParseException;
        // 07d: athrow
        // 07e: new com/google/gson/JsonObject
        // 081: dup
        // 082: invokespecial com/google/gson/JsonObject.<init> ()V
        // 085: astore 3
        // 086: invokestatic java/util/Collections.emptyList ()Ljava/util/List;
        // 089: astore 4
        // 08b: aconst_null
        // 08c: astore 5
        // 08e: aconst_null
        // 08f: astore 6
        // 091: aconst_null
        // 092: astore 7
        // 094: aconst_null
        // 095: astore 8
        // 097: aconst_null
        // 098: astore 9
        // 09a: aconst_null
        // 09b: astore 10
        // 09d: aconst_null
        // 09e: astore 11
        // 0a0: aconst_null
        // 0a1: astore 12
        // 0a3: aconst_null
        // 0a4: astore 13
        // 0a6: bipush 0
        // 0a7: istore 14
        // 0a9: aconst_null
        // 0aa: astore 15
        // 0ac: aconst_null
        // 0ad: astore 16
        // 0af: aconst_null
        // 0b0: astore 17
        // 0b2: aconst_null
        // 0b3: astore 18
        // 0b5: aload 1
        // 0b6: invokevirtual com/google/gson/stream/JsonReader.beginObject ()V
        // 0b9: aload 1
        // 0ba: invokevirtual com/google/gson/stream/JsonReader.hasNext ()Z
        // 0bd: ifeq 260
        // 0c0: aload 1
        // 0c1: invokevirtual com/google/gson/stream/JsonReader.nextName ()Ljava/lang/String;
        // 0c4: astore 19
        // 0c6: aload 19
        // 0c8: ldc "text"
        // 0ca: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 0cd: ifeq 0d9
        // 0d0: aload 1
        // 0d1: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.readString (Lcom/google/gson/stream/JsonReader;)Ljava/lang/String;
        // 0d4: astore 5
        // 0d6: goto 25d
        // 0d9: aload 19
        // 0db: ldc "translate"
        // 0dd: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 0e0: ifeq 0ec
        // 0e3: aload 1
        // 0e4: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 0e7: astore 6
        // 0e9: goto 25d
        // 0ec: aload 19
        // 0ee: ldc "with"
        // 0f0: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 0f3: ifeq 109
        // 0f6: aload 0
        // 0f7: getfield me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.gson Lcom/google/gson/Gson;
        // 0fa: aload 1
        // 0fb: getstatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.COMPONENT_LIST_TYPE Ljava/lang/reflect/Type;
        // 0fe: invokevirtual com/google/gson/Gson.fromJson (Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        // 101: checkcast java/util/List
        // 104: astore 7
        // 106: goto 25d
        // 109: aload 19
        // 10b: ldc "score"
        // 10d: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 110: ifeq 17f
        // 113: aload 1
        // 114: invokevirtual com/google/gson/stream/JsonReader.beginObject ()V
        // 117: aload 1
        // 118: invokevirtual com/google/gson/stream/JsonReader.hasNext ()Z
        // 11b: ifeq 164
        // 11e: aload 1
        // 11f: invokevirtual com/google/gson/stream/JsonReader.nextName ()Ljava/lang/String;
        // 122: astore 20
        // 124: aload 20
        // 126: ldc "name"
        // 128: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 12b: ifeq 137
        // 12e: aload 1
        // 12f: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 132: astore 8
        // 134: goto 161
        // 137: aload 20
        // 139: ldc "objective"
        // 13b: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 13e: ifeq 14a
        // 141: aload 1
        // 142: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 145: astore 9
        // 147: goto 161
        // 14a: aload 20
        // 14c: ldc "value"
        // 14e: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 151: ifeq 15d
        // 154: aload 1
        // 155: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 158: astore 10
        // 15a: goto 161
        // 15d: aload 1
        // 15e: invokevirtual com/google/gson/stream/JsonReader.skipValue ()V
        // 161: goto 117
        // 164: aload 8
        // 166: ifnull 16e
        // 169: aload 9
        // 16b: ifnonnull 178
        // 16e: new com/google/gson/JsonParseException
        // 171: dup
        // 172: ldc "A score component requires a name and objective"
        // 174: invokespecial com/google/gson/JsonParseException.<init> (Ljava/lang/String;)V
        // 177: athrow
        // 178: aload 1
        // 179: invokevirtual com/google/gson/stream/JsonReader.endObject ()V
        // 17c: goto 25d
        // 17f: aload 19
        // 181: ldc "selector"
        // 183: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 186: ifeq 192
        // 189: aload 1
        // 18a: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 18d: astore 11
        // 18f: goto 25d
        // 192: aload 19
        // 194: ldc "keybind"
        // 196: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 199: ifeq 1a5
        // 19c: aload 1
        // 19d: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 1a0: astore 12
        // 1a2: goto 25d
        // 1a5: aload 19
        // 1a7: ldc "nbt"
        // 1a9: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1ac: ifeq 1b8
        // 1af: aload 1
        // 1b0: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 1b3: astore 13
        // 1b5: goto 25d
        // 1b8: aload 19
        // 1ba: ldc "interpret"
        // 1bc: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1bf: ifeq 1cb
        // 1c2: aload 1
        // 1c3: invokevirtual com/google/gson/stream/JsonReader.nextBoolean ()Z
        // 1c6: istore 14
        // 1c8: goto 25d
        // 1cb: aload 19
        // 1cd: ldc "block"
        // 1cf: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1d2: ifeq 1e8
        // 1d5: aload 0
        // 1d6: getfield me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.gson Lcom/google/gson/Gson;
        // 1d9: aload 1
        // 1da: getstatic me/lucko/spark/lib/adventure/text/serializer/gson/SerializerFactory.BLOCK_NBT_POS_TYPE Ljava/lang/Class;
        // 1dd: invokevirtual com/google/gson/Gson.fromJson (Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        // 1e0: checkcast me/lucko/spark/lib/adventure/text/BlockNBTComponent$Pos
        // 1e3: astore 15
        // 1e5: goto 25d
        // 1e8: aload 19
        // 1ea: ldc "entity"
        // 1ec: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 1ef: ifeq 1fb
        // 1f2: aload 1
        // 1f3: invokevirtual com/google/gson/stream/JsonReader.nextString ()Ljava/lang/String;
        // 1f6: astore 16
        // 1f8: goto 25d
        // 1fb: aload 19
        // 1fd: ldc "storage"
        // 1ff: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 202: ifeq 218
        // 205: aload 0
        // 206: getfield me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.gson Lcom/google/gson/Gson;
        // 209: aload 1
        // 20a: getstatic me/lucko/spark/lib/adventure/text/serializer/gson/SerializerFactory.KEY_TYPE Ljava/lang/Class;
        // 20d: invokevirtual com/google/gson/Gson.fromJson (Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        // 210: checkcast me/lucko/spark/lib/adventure/key/Key
        // 213: astore 17
        // 215: goto 25d
        // 218: aload 19
        // 21a: ldc "extra"
        // 21c: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 21f: ifeq 235
        // 222: aload 0
        // 223: getfield me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.gson Lcom/google/gson/Gson;
        // 226: aload 1
        // 227: getstatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.COMPONENT_LIST_TYPE Ljava/lang/reflect/Type;
        // 22a: invokevirtual com/google/gson/Gson.fromJson (Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        // 22d: checkcast java/util/List
        // 230: astore 4
        // 232: goto 25d
        // 235: aload 19
        // 237: ldc "separator"
        // 239: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
        // 23c: ifeq 249
        // 23f: aload 0
        // 240: aload 1
        // 241: invokevirtual me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.read (Lcom/google/gson/stream/JsonReader;)Lme/lucko/spark/lib/adventure/text/BuildableComponent;
        // 244: astore 18
        // 246: goto 25d
        // 249: aload 3
        // 24a: aload 19
        // 24c: aload 0
        // 24d: getfield me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.gson Lcom/google/gson/Gson;
        // 250: aload 1
        // 251: ldc_w com/google/gson/JsonElement
        // 254: invokevirtual com/google/gson/Gson.fromJson (Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        // 257: checkcast com/google/gson/JsonElement
        // 25a: invokevirtual com/google/gson/JsonObject.add (Ljava/lang/String;Lcom/google/gson/JsonElement;)V
        // 25d: goto 0b9
        // 260: aload 5
        // 262: ifnull 274
        // 265: invokestatic me/lucko/spark/lib/adventure/text/Component.text ()Lme/lucko/spark/lib/adventure/text/TextComponent$Builder;
        // 268: aload 5
        // 26a: invokeinterface me/lucko/spark/lib/adventure/text/TextComponent$Builder.content (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TextComponent$Builder; 2
        // 26f: astore 19
        // 271: goto 389
        // 274: aload 6
        // 276: ifnull 2a3
        // 279: aload 7
        // 27b: ifnull 294
        // 27e: invokestatic me/lucko/spark/lib/adventure/text/Component.translatable ()Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder;
        // 281: aload 6
        // 283: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent$Builder.key (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder; 2
        // 288: aload 7
        // 28a: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent$Builder.args (Ljava/util/List;)Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder; 2
        // 28f: astore 19
        // 291: goto 389
        // 294: invokestatic me/lucko/spark/lib/adventure/text/Component.translatable ()Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder;
        // 297: aload 6
        // 299: invokeinterface me/lucko/spark/lib/adventure/text/TranslatableComponent$Builder.key (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/TranslatableComponent$Builder; 2
        // 29e: astore 19
        // 2a0: goto 389
        // 2a3: aload 8
        // 2a5: ifnull 2e5
        // 2a8: aload 9
        // 2aa: ifnull 2e5
        // 2ad: aload 10
        // 2af: ifnonnull 2c8
        // 2b2: invokestatic me/lucko/spark/lib/adventure/text/Component.score ()Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder;
        // 2b5: aload 8
        // 2b7: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.name (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 2bc: aload 9
        // 2be: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.objective (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 2c3: astore 19
        // 2c5: goto 389
        // 2c8: invokestatic me/lucko/spark/lib/adventure/text/Component.score ()Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder;
        // 2cb: aload 8
        // 2cd: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.name (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 2d2: aload 9
        // 2d4: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.objective (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 2d9: aload 10
        // 2db: invokeinterface me/lucko/spark/lib/adventure/text/ScoreComponent$Builder.value (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/ScoreComponent$Builder; 2
        // 2e0: astore 19
        // 2e2: goto 389
        // 2e5: aload 11
        // 2e7: ifnull 300
        // 2ea: invokestatic me/lucko/spark/lib/adventure/text/Component.selector ()Lme/lucko/spark/lib/adventure/text/SelectorComponent$Builder;
        // 2ed: aload 11
        // 2ef: invokeinterface me/lucko/spark/lib/adventure/text/SelectorComponent$Builder.pattern (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/SelectorComponent$Builder; 2
        // 2f4: aload 18
        // 2f6: invokeinterface me/lucko/spark/lib/adventure/text/SelectorComponent$Builder.separator (Lme/lucko/spark/lib/adventure/text/ComponentLike;)Lme/lucko/spark/lib/adventure/text/SelectorComponent$Builder; 2
        // 2fb: astore 19
        // 2fd: goto 389
        // 300: aload 12
        // 302: ifnull 314
        // 305: invokestatic me/lucko/spark/lib/adventure/text/Component.keybind ()Lme/lucko/spark/lib/adventure/text/KeybindComponent$Builder;
        // 308: aload 12
        // 30a: invokeinterface me/lucko/spark/lib/adventure/text/KeybindComponent$Builder.keybind (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/KeybindComponent$Builder; 2
        // 30f: astore 19
        // 311: goto 389
        // 314: aload 13
        // 316: ifnull 381
        // 319: aload 15
        // 31b: ifnull 339
        // 31e: invokestatic me/lucko/spark/lib/adventure/text/Component.blockNBT ()Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder;
        // 321: aload 13
        // 323: iload 14
        // 325: aload 18
        // 327: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.nbt (Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;Ljava/lang/String;ZLme/lucko/spark/lib/adventure/text/Component;)Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;
        // 32a: checkcast me/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder
        // 32d: aload 15
        // 32f: invokeinterface me/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder.pos (Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Pos;)Lme/lucko/spark/lib/adventure/text/BlockNBTComponent$Builder; 2
        // 334: astore 19
        // 336: goto 389
        // 339: aload 16
        // 33b: ifnull 359
        // 33e: invokestatic me/lucko/spark/lib/adventure/text/Component.entityNBT ()Lme/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder;
        // 341: aload 13
        // 343: iload 14
        // 345: aload 18
        // 347: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.nbt (Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;Ljava/lang/String;ZLme/lucko/spark/lib/adventure/text/Component;)Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;
        // 34a: checkcast me/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder
        // 34d: aload 16
        // 34f: invokeinterface me/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder.selector (Ljava/lang/String;)Lme/lucko/spark/lib/adventure/text/EntityNBTComponent$Builder; 2
        // 354: astore 19
        // 356: goto 389
        // 359: aload 17
        // 35b: ifnull 379
        // 35e: invokestatic me/lucko/spark/lib/adventure/text/Component.storageNBT ()Lme/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder;
        // 361: aload 13
        // 363: iload 14
        // 365: aload 18
        // 367: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.nbt (Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;Ljava/lang/String;ZLme/lucko/spark/lib/adventure/text/Component;)Lme/lucko/spark/lib/adventure/text/NBTComponentBuilder;
        // 36a: checkcast me/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder
        // 36d: aload 17
        // 36f: invokeinterface me/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder.storage (Lme/lucko/spark/lib/adventure/key/Key;)Lme/lucko/spark/lib/adventure/text/StorageNBTComponent$Builder; 2
        // 374: astore 19
        // 376: goto 389
        // 379: aload 1
        // 37a: invokevirtual com/google/gson/stream/JsonReader.getPath ()Ljava/lang/String;
        // 37d: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.notSureHowToDeserialize (Ljava/lang/Object;)Lcom/google/gson/JsonParseException;
        // 380: athrow
        // 381: aload 1
        // 382: invokevirtual com/google/gson/stream/JsonReader.getPath ()Ljava/lang/String;
        // 385: invokestatic me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.notSureHowToDeserialize (Ljava/lang/Object;)Lcom/google/gson/JsonParseException;
        // 388: athrow
        // 389: aload 19
        // 38b: aload 0
        // 38c: getfield me/lucko/spark/lib/adventure/text/serializer/gson/ComponentSerializerImpl.gson Lcom/google/gson/Gson;
        // 38f: aload 3
        // 390: getstatic me/lucko/spark/lib/adventure/text/serializer/gson/SerializerFactory.STYLE_TYPE Ljava/lang/Class;
        // 393: invokevirtual com/google/gson/Gson.fromJson (Lcom/google/gson/JsonElement;Ljava/lang/Class;)Ljava/lang/Object;
        // 396: checkcast me/lucko/spark/lib/adventure/text/format/Style
        // 399: invokeinterface me/lucko/spark/lib/adventure/text/ComponentBuilder.style (Lme/lucko/spark/lib/adventure/text/format/Style;)Lme/lucko/spark/lib/adventure/text/ComponentBuilder; 2
        // 39e: aload 4
        // 3a0: invokeinterface me/lucko/spark/lib/adventure/text/ComponentBuilder.append (Ljava/lang/Iterable;)Lme/lucko/spark/lib/adventure/text/ComponentBuilder; 2
        // 3a5: pop
        // 3a6: aload 1
        // 3a7: invokevirtual com/google/gson/stream/JsonReader.endObject ()V
        // 3aa: aload 19
        // 3ac: invokeinterface me/lucko/spark/lib/adventure/text/ComponentBuilder.build ()Lme/lucko/spark/lib/adventure/text/BuildableComponent; 1
        // 3b1: areturn
    }

    private static String readString(final JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return in.nextString();
        } else if (peek == JsonToken.BOOLEAN) {
            return String.valueOf(in.nextBoolean());
        } else {
            throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a string");
        }
    }

    private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final String nbt, final boolean interpret, @Nullable final Component separator) {
        return builder.nbtPath(nbt).interpret(interpret).separator(separator);
    }

    public void write(final JsonWriter out, final Component value) throws IOException {
        out.beginObject();
        if (value.hasStyling()) {
            JsonElement style = this.gson.toJsonTree(value.style(), SerializerFactory.STYLE_TYPE);
            if (style.isJsonObject()) {
                for (Entry<String, JsonElement> entry : style.getAsJsonObject().entrySet()) {
                    out.name((String) entry.getKey());
                    this.gson.toJson((JsonElement) entry.getValue(), out);
                }
            }
        }
        if (!value.children().isEmpty()) {
            out.name("extra");
            this.gson.toJson(value.children(), COMPONENT_LIST_TYPE, out);
        }
        if (value instanceof TextComponent) {
            out.name("text");
            out.value(((TextComponent) value).content());
        } else if (value instanceof TranslatableComponent) {
            TranslatableComponent translatable = (TranslatableComponent) value;
            out.name("translate");
            out.value(translatable.key());
            if (!translatable.args().isEmpty()) {
                out.name("with");
                this.gson.toJson(translatable.args(), COMPONENT_LIST_TYPE, out);
            }
        } else if (value instanceof ScoreComponent) {
            ScoreComponent score = (ScoreComponent) value;
            out.name("score");
            out.beginObject();
            out.name("name");
            out.value(score.name());
            out.name("objective");
            out.value(score.objective());
            if (score.value() != null) {
                out.name("value");
                out.value(score.value());
            }
            out.endObject();
        } else if (value instanceof SelectorComponent) {
            SelectorComponent selector = (SelectorComponent) value;
            out.name("selector");
            out.value(selector.pattern());
            this.serializeSeparator(out, selector.separator());
        } else if (value instanceof KeybindComponent) {
            out.name("keybind");
            out.value(((KeybindComponent) value).keybind());
        } else {
            if (!(value instanceof NBTComponent)) {
                throw notSureHowToSerialize(value);
            }
            NBTComponent<?, ?> nbt = (NBTComponent<?, ?>) value;
            out.name("nbt");
            out.value(nbt.nbtPath());
            out.name("interpret");
            out.value(nbt.interpret());
            this.serializeSeparator(out, nbt.separator());
            if (value instanceof BlockNBTComponent) {
                out.name("block");
                this.gson.toJson(((BlockNBTComponent) value).pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, out);
            } else if (value instanceof EntityNBTComponent) {
                out.name("entity");
                out.value(((EntityNBTComponent) value).selector());
            } else {
                if (!(value instanceof StorageNBTComponent)) {
                    throw notSureHowToSerialize(value);
                }
                out.name("storage");
                this.gson.toJson(((StorageNBTComponent) value).storage(), SerializerFactory.KEY_TYPE, out);
            }
        }
        out.endObject();
    }

    private void serializeSeparator(final JsonWriter out, @Nullable final Component separator) throws IOException {
        if (separator != null) {
            out.name("separator");
            this.write(out, separator);
        }
    }

    static JsonParseException notSureHowToDeserialize(final Object element) {
        return new JsonParseException("Don't know how to turn " + element + " into a Component");
    }

    private static IllegalArgumentException notSureHowToSerialize(final Component component) {
        return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
    }
}