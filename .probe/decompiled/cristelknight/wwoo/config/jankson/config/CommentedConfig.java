package cristelknight.wwoo.config.jankson.config;

import com.mojang.serialization.Codec;
import cristelknight.wwoo.config.jankson.ConfigUtil;
import cristelknight.wwoo.config.jankson.JanksonOps;
import java.nio.file.Path;
import java.util.HashMap;
import net.cristellib.CristelLibExpectPlatform;
import org.jetbrains.annotations.Nullable;

public interface CommentedConfig<T extends Record> {

    String getSubPath();

    T getInstance();

    T getDefault();

    Codec<T> getCodec();

    @Nullable
    HashMap<String, String> getComments();

    @Nullable
    String getHeader();

    boolean isSorted();

    void setInstance(T var1);

    default T getConfig() {
        return this.getConfig(false, false);
    }

    default Path getConfigPath() {
        return CristelLibExpectPlatform.getConfigDirectory().resolve(this.getSubPath() + ".json5");
    }

    default T getConfig(boolean fromFile, boolean save) {
        if (this.getInstance() == null || fromFile || save) {
            this.setInstance(this.readConfig(save));
        }
        return this.getInstance();
    }

    default T readConfig(boolean recreate) {
        if (!this.getConfigPath().toFile().exists() || recreate) {
            this.createConfig();
        }
        return ConfigUtil.readConfig(this.getConfigPath(), this.getCodec(), JanksonOps.INSTANCE);
    }

    default void createConfig() {
        T create = this.getInstance();
        if (create == null) {
            create = this.getDefault();
        }
        ConfigUtil.createConfig(this.getConfigPath(), this.getCodec(), this.getMap(this.getComments()), JanksonOps.INSTANCE, create, this.isSorted(), this.getComment(this.getHeader()));
    }

    private String getComment(String header) {
        return header != null ? "/*\n" + header + "\n*/" : null;
    }

    private HashMap<String, String> getMap(HashMap<String, String> comments) {
        if (comments == null) {
            comments = new HashMap();
        }
        return comments;
    }
}