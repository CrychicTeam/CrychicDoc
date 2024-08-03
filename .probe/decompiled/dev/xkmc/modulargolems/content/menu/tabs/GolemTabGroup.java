package dev.xkmc.modulargolems.content.menu.tabs;

import java.util.List;

public class GolemTabGroup<G extends GolemTabGroup<G>> {

    private final List<GolemTabToken<G, ?>> list;

    public GolemTabGroup(List<GolemTabToken<G, ?>> list) {
        this.list = list;
    }

    public List<GolemTabToken<G, ?>> getList() {
        return this.list;
    }
}