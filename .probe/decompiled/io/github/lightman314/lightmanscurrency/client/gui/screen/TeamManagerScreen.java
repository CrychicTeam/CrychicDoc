package io.github.lightman314.lightmanscurrency.client.gui.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamBankAccountTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamMemberEditTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamMemberListTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamNameTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamOwnerTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamSelectionTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamStatsTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.team.TeamTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;

public class TeamManagerScreen extends EasyScreen {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/teammanager.png");

    private long activeTeamID = -1L;

    private final List<TeamTab> tabs = ImmutableList.of(new TeamSelectionTab(this), new TeamMemberListTab(this), new TeamNameTab(this), new TeamMemberEditTab(this), new TeamBankAccountTab(this), new TeamStatsTab(this), new TeamOwnerTab(this));

    List<TabButton> tabButtons = Lists.newArrayList();

    int currentTabIndex = 0;

    public TeamManagerScreen() {
        this.resize(200, 200);
    }

    public Team getActiveTeam() {
        if (this.activeTeamID < 0L) {
            return null;
        } else {
            Team team = TeamSaveData.GetTeam(true, this.activeTeamID);
            return team != null && team.isMember(this.getPlayer()) ? team : null;
        }
    }

    public void setActiveTeam(long teamID) {
        this.activeTeamID = teamID;
    }

    public TeamTab currentTab() {
        return (TeamTab) this.tabs.get(MathUtil.clamp(this.currentTabIndex, 0, this.tabs.size() - 1));
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        for (TeamTab tab : this.tabs) {
            TabButton button = this.addChild(new TabButton(this::clickedOnTab, tab)).withAddons(EasyAddonHelper.activeCheck((NonNullSupplier<Boolean>) (() -> this.tabs.indexOf(tab) != this.currentTabIndex)), EasyAddonHelper.visibleCheck((NonNullSupplier<Boolean>) (() -> tab.allowViewing(this.getPlayer(), this.getActiveTeam()))));
            this.tabButtons.add(button);
        }
        this.positionTabButtons();
        this.currentTab().onOpen();
    }

    private ScreenPosition getTabPos(int index) {
        if (index < 8) {
            return this.getCorner().offset(25 * index, -25);
        } else if (index < 16) {
            return this.getCorner().offset(this.getXSize(), 25 * (index - 8));
        } else {
            return index < 24 ? this.getCorner().offset(this.getXSize() - 25 * (index - 16), this.getYSize()) : this.getCorner().offset(this.getGuiLeft() - 25, this.getYSize() - 25 * (index - 24));
        }
    }

    private int getTabRotation(int index) {
        if (index < 8) {
            return 0;
        } else if (index < 16) {
            return 1;
        } else {
            return index < 24 ? 2 : 3;
        }
    }

    private void positionTabButtons() {
        int index = 0;
        for (TabButton thisButton : this.tabButtons) {
            if (thisButton.f_93624_) {
                thisButton.reposition(this.getTabPos(index), this.getTabRotation(index));
                index++;
            }
        }
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        try {
            this.currentTab().renderBG(gui);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error rendering Team Manager BG", var3);
        }
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        super.renderAfterWidgets(gui);
        try {
            this.currentTab().renderAfterWidgets(gui);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error rendering Team Manager FG", var3);
        }
    }

    @Override
    protected void screenTick() {
        if (this.activeTeamID < 0L && this.currentTabIndex != 0) {
            this.changeTab(0);
        }
        boolean updateTabs = false;
        for (int i = 0; i < this.tabs.size(); i++) {
            boolean visible = ((TeamTab) this.tabs.get(i)).allowViewing(this.getPlayer(), this.getActiveTeam());
            if (visible != ((TabButton) this.tabButtons.get(i)).f_93624_) {
                updateTabs = true;
                ((TabButton) this.tabButtons.get(i)).f_93624_ = visible;
            }
        }
        if (updateTabs) {
            this.positionTabButtons();
        }
        if (!this.currentTab().allowViewing(this.getPlayer(), this.getActiveTeam()) && this.currentTabIndex != 0) {
            this.changeTab(0);
        }
    }

    public void changeTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < this.tabs.size()) {
            if (((TeamTab) this.tabs.get(tabIndex)).allowViewing(this.getPlayer(), this.getActiveTeam())) {
                this.currentTab().onClose();
                ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = true;
                this.currentTabIndex = tabIndex;
                ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = false;
                this.currentTab().onOpen();
            }
        }
    }

    private void clickedOnTab(EasyButton tab) {
        int tabIndex = -1;
        if (tab instanceof TabButton) {
            tabIndex = this.tabButtons.indexOf(tab);
        }
        if (tabIndex >= 0) {
            this.changeTab(tabIndex);
        }
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab().blockInventoryClosing();
    }
}