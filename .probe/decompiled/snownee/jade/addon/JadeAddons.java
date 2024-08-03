package snownee.jade.addon;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import snownee.jade.api.theme.IThemeHelper;

@Mod("jadeaddons")
public class JadeAddons {

    public static final String ID = "jadeaddons";

    public static final String NAME = "Jade Addons";

    public static final Logger LOGGER = LogUtils.getLogger();

    public JadeAddons() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
        if (FMLEnvironment.dist.isClient()) {
            JadeAddonsClient.init();
        }
    }

    public static MutableComponent seconds(int sec) {
        return IThemeHelper.get().info(StringUtil.formatTickDuration(sec * 20)).withStyle(ChatFormatting.WHITE);
    }
}