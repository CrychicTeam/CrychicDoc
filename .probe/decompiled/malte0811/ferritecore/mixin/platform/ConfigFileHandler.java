package malte0811.ferritecore.mixin.platform;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.nio.file.Path;
import java.util.List;
import malte0811.ferritecore.mixin.config.FerriteConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigFileHandler {

    public static void finish(List<FerriteConfig.Option> options) {
        ConfigSpec spec = new ConfigSpec();
        for (FerriteConfig.Option o : options) {
            spec.define(o.getName(), o.getDefaultValue());
        }
        CommentedFileConfig configData = read(FMLPaths.CONFIGDIR.get().resolve("ferritecore-mixin.toml"));
        for (FerriteConfig.Option o : options) {
            configData.setComment(o.getName(), o.getComment());
        }
        spec.correct(configData);
        configData.save();
        for (FerriteConfig.Option o : options) {
            o.set(configData::get);
        }
    }

    private static CommentedFileConfig read(Path configPath) {
        CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(configPath).sync().preserveInsertionOrder().build();
        configData.load();
        return configData;
    }
}