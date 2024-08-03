package portb.biggerstacks;

import java.nio.file.Path;
import java.text.DecimalFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

public class Constants {

    public static final int ONE_BILLION = 1000000000;

    public static final int ONE_MILLION = 1000000;

    public static final int ONE_THOUSAND = 1000;

    public static final String MOD_ID = "biggerstacks";

    public static final String RULESET_FILE_NAME = "biggerstacks-rules.xml";

    public static final Path RULESET_FILE = FMLPaths.CONFIGDIR.get().resolve("biggerstacks-rules.xml");

    public static final ResourceLocation CONFIG_GUI_BG = new ResourceLocation("biggerstacks", "textures/gui/config_background.png");

    public static final DecimalFormat TOOLTIP_NUMBER_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    public static final int CHANGE_STACK_SIZE_COMMAND_PERMISSION_LEVEL = 4;
}