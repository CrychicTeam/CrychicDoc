package io.github.lightman314.lightmanscurrency.client.gui.widget;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.TeamButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import org.jetbrains.annotations.NotNull;

public class TeamSelectWidget extends EasyWidgetWithChildren {

    private final int rows;

    private final TeamButton.Size size;

    private final Supplier<List<Team>> teamSource;

    private final Supplier<Team> selectedTeam;

    private final Consumer<Integer> onPress;

    private final List<TeamButton> teamButtons = new ArrayList();

    private int scroll = 0;

    public TeamSelectWidget(ScreenPosition pos, int rows, Supplier<List<Team>> teamSource, Supplier<Team> selectedTeam, Consumer<Integer> onPress) {
        this(pos.x, pos.y, rows, teamSource, selectedTeam, onPress);
    }

    public TeamSelectWidget(int x, int y, int rows, Supplier<List<Team>> teamSource, Supplier<Team> selectedTeam, Consumer<Integer> onPress) {
        this(x, y, rows, TeamButton.Size.WIDE, teamSource, selectedTeam, onPress);
    }

    public TeamSelectWidget(ScreenPosition pos, int rows, TeamButton.Size size, Supplier<List<Team>> teamSource, Supplier<Team> selectedTeam, Consumer<Integer> onPress) {
        this(pos.x, pos.y, rows, size, teamSource, selectedTeam, onPress);
    }

    public TeamSelectWidget(int x, int y, int rows, TeamButton.Size size, Supplier<List<Team>> teamSource, Supplier<Team> selectedTeam, Consumer<Integer> onPress) {
        super(x, y, size.width, 20 * rows);
        this.rows = rows;
        this.size = size;
        this.teamSource = teamSource;
        this.selectedTeam = selectedTeam;
        this.onPress = onPress;
    }

    public TeamSelectWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void addChildren() {
        this.teamButtons.clear();
        for (int i = 0; i < this.rows; i++) {
            int index = i;
            TeamButton button = this.addChild(new TeamButton(this.getPosition().offset(0, i * 20), this.size, this::onTeamSelect, () -> this.getTeam(index), () -> this.isSelected(index)));
            this.teamButtons.add(button);
        }
    }

    @Override
    protected void renderTick() {
        for (TeamButton b : this.teamButtons) {
            b.setVisible(this.f_93624_);
        }
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        this.teamButtons.forEach(b -> b.f_93624_ = this.f_93624_);
        if (this.f_93624_) {
            gui.fill(this.getArea().atPosition(ScreenPosition.ZERO), -16777216);
        }
    }

    private Team getTeam(int index) {
        List<Team> teamList = (List<Team>) this.teamSource.get();
        this.validateScroll(teamList.size());
        index += this.scroll;
        return index >= 0 && index < teamList.size() ? (Team) teamList.get(index) : null;
    }

    private boolean isSelected(int index) {
        Team team = this.getTeam(index);
        return team == null ? false : team == this.selectedTeam.get();
    }

    private void validateScroll(int teamListSize) {
        this.scroll = MathUtil.clamp(this.scroll, 0, this.maxScroll(teamListSize));
    }

    private int maxScroll(int teamListSize) {
        return MathUtil.clamp(teamListSize - this.rows, 0, Integer.MAX_VALUE);
    }

    private boolean canScrollDown() {
        return this.scroll < this.maxScroll(((List) this.teamSource.get()).size());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.f_93624_) {
            return false;
        } else {
            if (delta < 0.0) {
                if (!this.canScrollDown()) {
                    return false;
                }
                this.scroll++;
            } else if (delta > 0.0) {
                if (this.scroll <= 0) {
                    return false;
                }
                this.scroll--;
            }
            return true;
        }
    }

    private void onTeamSelect(EasyButton button) {
        int index = -1;
        if (button instanceof TeamButton) {
            index = this.teamButtons.indexOf(button);
        }
        if (index >= 0) {
            this.onPress.accept(this.scroll + index);
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrator) {
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return false;
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
    }
}