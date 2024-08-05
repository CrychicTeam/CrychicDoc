package net.minecraft.realms;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class RealmsScreen extends Screen {

    protected static final int TITLE_HEIGHT = 17;

    protected static final int COMPONENT_HEIGHT = 20;

    protected static final int EXPIRATION_NOTIFICATION_DAYS = 7;

    protected static final long SIZE_LIMIT = 5368709120L;

    public static final int COLOR_WHITE = 16777215;

    public static final int COLOR_GRAY = 10526880;

    protected static final int COLOR_DARK_GRAY = 5000268;

    protected static final int COLOR_MEDIUM_GRAY = 7105644;

    protected static final int COLOR_GREEN = 8388479;

    protected static final int COLOR_DARK_GREEN = 6077788;

    protected static final int COLOR_RED = 16711680;

    protected static final int COLOR_RED_FADE = 15553363;

    protected static final int COLOR_BLACK = -1073741824;

    protected static final int COLOR_YELLOW = 13413468;

    protected static final int COLOR_BRIGHT_YELLOW = -256;

    protected static final int COLOR_LINK = 3368635;

    protected static final int COLOR_LINK_HOVER = 7107012;

    protected static final int COLOR_INFO = 8226750;

    protected static final int COLOR_BUTTON_YELLOW = 16777120;

    protected static final String UPDATE_BREAKS_ADVENTURE_URL = "https://www.minecraft.net/realms/adventure-maps-in-1-9";

    protected static final int SKIN_FACE_SIZE = 8;

    private final List<RealmsLabel> labels = Lists.newArrayList();

    public RealmsScreen(Component component0) {
        super(component0);
    }

    protected static int row(int int0) {
        return 40 + int0 * 13;
    }

    protected RealmsLabel addLabel(RealmsLabel realmsLabel0) {
        this.labels.add(realmsLabel0);
        return (RealmsLabel) this.m_169394_(realmsLabel0);
    }

    public Component createLabelNarration() {
        return CommonComponents.joinLines((Collection<? extends Component>) this.labels.stream().map(RealmsLabel::m_175034_).collect(Collectors.toList()));
    }
}