package io.github.lightman314.lightmanscurrency.client.gui.screen.team;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.screen.TeamManagerScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollTextDisplay;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class TeamMemberListTab extends TeamTab {

    ScrollTextDisplay memberDisplay;

    public TeamMemberListTab(TeamManagerScreen screen) {
        super(screen);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.PLAYER_HEAD);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TEAM_MEMBERS.get();
    }

    @Override
    public boolean allowViewing(Player player, Team team) {
        return team != null;
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.memberDisplay = this.addChild(new ScrollTextDisplay(screenArea.pos.offset(10, 10), screenArea.width - 20, screenArea.height - 20, this::getMemberList));
        this.memberDisplay.setColumnCount(2);
    }

    private List<Component> getMemberList() {
        List<Component> list = Lists.newArrayList();
        Team team = this.getActiveTeam();
        if (team != null) {
            list.add(team.getOwner().getNameComponent(true).withStyle(ChatFormatting.GREEN));
            team.getAdmins().forEach(admin -> list.add(admin.getNameComponent(true).withStyle(ChatFormatting.DARK_GREEN)));
            team.getMembers().forEach(member -> list.add(member.getNameComponent(true)));
        }
        return list;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
    }
}