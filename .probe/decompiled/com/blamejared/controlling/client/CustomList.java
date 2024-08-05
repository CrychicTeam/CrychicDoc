package com.blamejared.controlling.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;

public class CustomList extends KeyBindsList {

    public List<KeyBindsList.Entry> allEntries;

    public CustomList(KeyBindsScreen controls, Minecraft mcIn) {
        super(controls, mcIn);
    }

    public List<KeyBindsList.Entry> getAllEntries() {
        return this.allEntries;
    }

    protected int addEntry(KeyBindsList.Entry ent) {
        if (this.allEntries == null) {
            this.allEntries = new ArrayList();
        }
        this.allEntries.add(ent);
        this.m_6702_().add(ent);
        return this.m_6702_().size() - 1;
    }
}