package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;

public enum PlacementOptions {

    Merged(AllIcons.I_CENTERED), Attached(AllIcons.I_ATTACHED), Inserted(AllIcons.I_INSERTED);

    public String translationKey = Lang.asId(this.name());

    public AllIcons icon;

    private PlacementOptions(AllIcons icon) {
        this.icon = icon;
    }
}