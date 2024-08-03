package net.minecraft.server.dedicated;

import com.google.common.base.MoreObjects;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import org.slf4j.Logger;

public abstract class Settings<T extends Settings<T>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Properties properties;

    public Settings(Properties properties0) {
        this.properties = properties0;
    }

    public static Properties loadFromFile(Path path0) {
        try {
            try {
                InputStream $$1 = Files.newInputStream(path0);
                Properties var13;
                try {
                    CharsetDecoder $$2 = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
                    Properties $$3 = new Properties();
                    $$3.load(new InputStreamReader($$1, $$2));
                    var13 = $$3;
                } catch (Throwable var8) {
                    if ($$1 != null) {
                        try {
                            $$1.close();
                        } catch (Throwable var6) {
                            var8.addSuppressed(var6);
                        }
                    }
                    throw var8;
                }
                if ($$1 != null) {
                    $$1.close();
                }
                return var13;
            } catch (CharacterCodingException var9) {
                LOGGER.info("Failed to load properties as UTF-8 from file {}, trying ISO_8859_1", path0);
                Reader $$5 = Files.newBufferedReader(path0, StandardCharsets.ISO_8859_1);
                Properties var4;
                try {
                    Properties $$6 = new Properties();
                    $$6.load($$5);
                    var4 = $$6;
                } catch (Throwable var7) {
                    if ($$5 != null) {
                        try {
                            $$5.close();
                        } catch (Throwable var5) {
                            var7.addSuppressed(var5);
                        }
                    }
                    throw var7;
                }
                if ($$5 != null) {
                    $$5.close();
                }
                return var4;
            }
        } catch (IOException var10) {
            LOGGER.error("Failed to load properties from file: {}", path0, var10);
            return new Properties();
        }
    }

    public void store(Path path0) {
        try {
            Writer $$1 = Files.newBufferedWriter(path0, StandardCharsets.UTF_8);
            try {
                this.properties.store($$1, "Minecraft server properties");
            } catch (Throwable var6) {
                if ($$1 != null) {
                    try {
                        $$1.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if ($$1 != null) {
                $$1.close();
            }
        } catch (IOException var7) {
            LOGGER.error("Failed to store properties to file: {}", path0);
        }
    }

    private static <V extends Number> Function<String, V> wrapNumberDeserializer(Function<String, V> functionStringV0) {
        return p_139845_ -> {
            try {
                return (Number) functionStringV0.apply(p_139845_);
            } catch (NumberFormatException var3) {
                return null;
            }
        };
    }

    protected static <V> Function<String, V> dispatchNumberOrString(IntFunction<V> intFunctionV0, Function<String, V> functionStringV1) {
        return p_139856_ -> {
            try {
                return intFunctionV0.apply(Integer.parseInt(p_139856_));
            } catch (NumberFormatException var4) {
                return functionStringV1.apply(p_139856_);
            }
        };
    }

    @Nullable
    private String getStringRaw(String string0) {
        return (String) this.properties.get(string0);
    }

    @Nullable
    protected <V> V getLegacy(String string0, Function<String, V> functionStringV1) {
        String $$2 = this.getStringRaw(string0);
        if ($$2 == null) {
            return null;
        } else {
            this.properties.remove(string0);
            return (V) functionStringV1.apply($$2);
        }
    }

    protected <V> V get(String string0, Function<String, V> functionStringV1, Function<V, String> functionVString2, V v3) {
        String $$4 = this.getStringRaw(string0);
        V $$5 = (V) MoreObjects.firstNonNull($$4 != null ? functionStringV1.apply($$4) : null, v3);
        this.properties.put(string0, functionVString2.apply($$5));
        return $$5;
    }

    protected <V> Settings<T>.MutableValue<V> getMutable(String string0, Function<String, V> functionStringV1, Function<V, String> functionVString2, V v3) {
        String $$4 = this.getStringRaw(string0);
        V $$5 = (V) MoreObjects.firstNonNull($$4 != null ? functionStringV1.apply($$4) : null, v3);
        this.properties.put(string0, functionVString2.apply($$5));
        return new Settings.MutableValue<>(string0, $$5, functionVString2);
    }

    protected <V> V get(String string0, Function<String, V> functionStringV1, UnaryOperator<V> unaryOperatorV2, Function<V, String> functionVString3, V v4) {
        return this.get(string0, p_139849_ -> {
            V $$3 = (V) functionStringV1.apply(p_139849_);
            return $$3 != null ? unaryOperatorV2.apply($$3) : null;
        }, functionVString3, v4);
    }

    protected <V> V get(String string0, Function<String, V> functionStringV1, V v2) {
        return this.get(string0, functionStringV1, Objects::toString, v2);
    }

    protected <V> Settings<T>.MutableValue<V> getMutable(String string0, Function<String, V> functionStringV1, V v2) {
        return this.getMutable(string0, functionStringV1, Objects::toString, v2);
    }

    protected String get(String string0, String string1) {
        return this.get(string0, Function.identity(), Function.identity(), string1);
    }

    @Nullable
    protected String getLegacyString(String string0) {
        return this.getLegacy(string0, Function.identity());
    }

    protected int get(String string0, int int1) {
        return this.get(string0, wrapNumberDeserializer(Integer::parseInt), Integer.valueOf(int1));
    }

    protected Settings<T>.MutableValue<Integer> getMutable(String string0, int int1) {
        return this.getMutable(string0, wrapNumberDeserializer(Integer::parseInt), int1);
    }

    protected int get(String string0, UnaryOperator<Integer> unaryOperatorInteger1, int int2) {
        return this.get(string0, wrapNumberDeserializer(Integer::parseInt), unaryOperatorInteger1, Objects::toString, int2);
    }

    protected long get(String string0, long long1) {
        return this.get(string0, wrapNumberDeserializer(Long::parseLong), long1);
    }

    protected boolean get(String string0, boolean boolean1) {
        return this.get(string0, Boolean::valueOf, boolean1);
    }

    protected Settings<T>.MutableValue<Boolean> getMutable(String string0, boolean boolean1) {
        return this.getMutable(string0, Boolean::valueOf, boolean1);
    }

    @Nullable
    protected Boolean getLegacyBoolean(String string0) {
        return this.getLegacy(string0, Boolean::valueOf);
    }

    protected Properties cloneProperties() {
        Properties $$0 = new Properties();
        $$0.putAll(this.properties);
        return $$0;
    }

    protected abstract T reload(RegistryAccess var1, Properties var2);

    public class MutableValue<V> implements Supplier<V> {

        private final String key;

        private final V value;

        private final Function<V, String> serializer;

        MutableValue(String string0, V v1, Function<V, String> functionVString2) {
            this.key = string0;
            this.value = v1;
            this.serializer = functionVString2;
        }

        public V get() {
            return this.value;
        }

        public T update(RegistryAccess registryAccess0, V v1) {
            Properties $$2 = Settings.this.cloneProperties();
            $$2.put(this.key, this.serializer.apply(v1));
            return Settings.this.reload(registryAccess0, $$2);
        }
    }
}