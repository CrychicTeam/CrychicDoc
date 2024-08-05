package io.github.lightman314.lightmanscurrency.client.gui.widget.button;

import com.google.common.base.Supplier;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TeamButton extends EasyButton {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/teambutton.png");

    public static final int HEIGHT = 20;

    public static final int TEXT_COLOR = 16777215;

    private final TeamButton.Size size;

    private final Supplier<Team> teamSource;

    private final Supplier<Boolean> selectedSource;

    public Team getTeam() {
        return (Team) this.teamSource.get();
    }

    public TeamButton(ScreenPosition pos, TeamButton.Size size, Consumer<EasyButton> press, @Nonnull Supplier<Team> teamSource, @Nonnull Supplier<Boolean> selectedSource) {
        super(pos, size.width, 20, press);
        this.size = size;
        this.teamSource = teamSource;
        this.selectedSource = selectedSource;
    }

    public TeamButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        if (this.f_93624_ && this.getTeam() != null) {
            gui.resetColor();
            gui.blit(GUI_TEXTURE, 0, 0, 0, (this.selectedSource.get() ? 20 : 0) + this.size.guiPos, this.size.width, 20);
            gui.drawString(TextRenderUtil.fitString(this.getTeam().getName(), this.f_93618_ - 4), 2, 2, 16777215);
            gui.drawString(TextRenderUtil.fitString(LCText.GUI_OWNER_CURRENT.get(this.getTeam().getOwner().getName(true)), this.f_93618_ - 4), 2, 10, 16777215);
        }
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
        if (this.f_93624_ && this.getTeam() != null) {
            super.playDownSound(soundManager);
        }
    }

    public static enum Size {

        WIDE(180, 0), NORMAL(156, 1), NARROW(90, 2);

        public final int width;

        public final int guiPos;

        private Size(int width, int guiPos) {
            this.width = width;
            this.guiPos = guiPos * 20 * 2;
        }
    }
}