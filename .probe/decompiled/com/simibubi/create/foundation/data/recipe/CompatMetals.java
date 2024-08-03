package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.foundation.utility.Lang;

public enum CompatMetals {

    ALUMINUM(Mods.IE),
    LEAD(Mods.MEK, Mods.TH, Mods.IE),
    NICKEL(Mods.TH, Mods.IE),
    OSMIUM(Mods.MEK),
    PLATINUM,
    QUICKSILVER,
    SILVER(Mods.TH, Mods.IE),
    TIN(Mods.TH, Mods.MEK),
    URANIUM(Mods.MEK, Mods.IE);

    private final Mods[] mods;

    private final String name = Lang.asId(this.name());

    private CompatMetals(Mods... mods) {
        this.mods = mods;
    }

    public String getName() {
        return this.name;
    }

    public Mods[] getMods() {
        return this.mods;
    }
}