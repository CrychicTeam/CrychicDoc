package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

public class FilterScreen extends AbstractFilterScreen<FilterMenu> {

    private static final String PREFIX = "gui.filter.";

    private Component allowN = Lang.translateDirect("gui.filter.allow_list");

    private Component allowDESC = Lang.translateDirect("gui.filter.allow_list.description");

    private Component denyN = Lang.translateDirect("gui.filter.deny_list");

    private Component denyDESC = Lang.translateDirect("gui.filter.deny_list.description");

    private Component respectDataN = Lang.translateDirect("gui.filter.respect_data");

    private Component respectDataDESC = Lang.translateDirect("gui.filter.respect_data.description");

    private Component ignoreDataN = Lang.translateDirect("gui.filter.ignore_data");

    private Component ignoreDataDESC = Lang.translateDirect("gui.filter.ignore_data.description");

    private IconButton whitelist;

    private IconButton blacklist;

    private IconButton respectNBT;

    private IconButton ignoreNBT;

    private Indicator whitelistIndicator;

    private Indicator blacklistIndicator;

    private Indicator respectNBTIndicator;

    private Indicator ignoreNBTIndicator;

    public FilterScreen(FilterMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, AllGuiTextures.FILTER);
    }

    @Override
    protected void init() {
        this.setWindowOffset(-11, 5);
        super.init();
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.blacklist = new IconButton(x + 18, y + 75, AllIcons.I_BLACKLIST);
        this.blacklist.withCallback(() -> {
            ((FilterMenu) this.f_97732_).blacklist = true;
            this.sendOptionUpdate(FilterScreenPacket.Option.BLACKLIST);
        });
        this.blacklist.setToolTip(this.denyN);
        this.whitelist = new IconButton(x + 36, y + 75, AllIcons.I_WHITELIST);
        this.whitelist.withCallback(() -> {
            ((FilterMenu) this.f_97732_).blacklist = false;
            this.sendOptionUpdate(FilterScreenPacket.Option.WHITELIST);
        });
        this.whitelist.setToolTip(this.allowN);
        this.blacklistIndicator = new Indicator(x + 18, y + 69, Components.immutableEmpty());
        this.whitelistIndicator = new Indicator(x + 36, y + 69, Components.immutableEmpty());
        this.addRenderableWidgets(new AbstractSimiWidget[] { this.blacklist, this.whitelist, this.blacklistIndicator, this.whitelistIndicator });
        this.respectNBT = new IconButton(x + 60, y + 75, AllIcons.I_RESPECT_NBT);
        this.respectNBT.withCallback(() -> {
            ((FilterMenu) this.f_97732_).respectNBT = true;
            this.sendOptionUpdate(FilterScreenPacket.Option.RESPECT_DATA);
        });
        this.respectNBT.setToolTip(this.respectDataN);
        this.ignoreNBT = new IconButton(x + 78, y + 75, AllIcons.I_IGNORE_NBT);
        this.ignoreNBT.withCallback(() -> {
            ((FilterMenu) this.f_97732_).respectNBT = false;
            this.sendOptionUpdate(FilterScreenPacket.Option.IGNORE_DATA);
        });
        this.ignoreNBT.setToolTip(this.ignoreDataN);
        this.respectNBTIndicator = new Indicator(x + 60, y + 69, Components.immutableEmpty());
        this.ignoreNBTIndicator = new Indicator(x + 78, y + 69, Components.immutableEmpty());
        this.addRenderableWidgets(new AbstractSimiWidget[] { this.respectNBT, this.ignoreNBT, this.respectNBTIndicator, this.ignoreNBTIndicator });
        this.handleIndicators();
    }

    @Override
    protected List<IconButton> getTooltipButtons() {
        return Arrays.asList(this.blacklist, this.whitelist, this.respectNBT, this.ignoreNBT);
    }

    @Override
    protected List<MutableComponent> getTooltipDescriptions() {
        return Arrays.asList(this.denyDESC.plainCopy(), this.allowDESC.plainCopy(), this.respectDataDESC.plainCopy(), this.ignoreDataDESC.plainCopy());
    }

    @Override
    protected List<Indicator> getIndicators() {
        return Arrays.asList(this.blacklistIndicator, this.whitelistIndicator, this.respectNBTIndicator, this.ignoreNBTIndicator);
    }

    @Override
    protected boolean isButtonEnabled(IconButton button) {
        if (button == this.blacklist) {
            return !((FilterMenu) this.f_97732_).blacklist;
        } else if (button == this.whitelist) {
            return ((FilterMenu) this.f_97732_).blacklist;
        } else if (button == this.respectNBT) {
            return !((FilterMenu) this.f_97732_).respectNBT;
        } else {
            return button == this.ignoreNBT ? ((FilterMenu) this.f_97732_).respectNBT : true;
        }
    }

    @Override
    protected boolean isIndicatorOn(Indicator indicator) {
        if (indicator == this.blacklistIndicator) {
            return ((FilterMenu) this.f_97732_).blacklist;
        } else if (indicator == this.whitelistIndicator) {
            return !((FilterMenu) this.f_97732_).blacklist;
        } else if (indicator == this.respectNBTIndicator) {
            return ((FilterMenu) this.f_97732_).respectNBT;
        } else {
            return indicator == this.ignoreNBTIndicator ? !((FilterMenu) this.f_97732_).respectNBT : false;
        }
    }
}