package dev.xkmc.modulargolems.content.menu.registry;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabGroup;

public class EquipmentGroup extends GolemTabGroup<EquipmentGroup> {

    public AbstractGolemEntity<?, ?> golem;

    public EquipmentGroup(AbstractGolemEntity<?, ?> golem) {
        super(GolemTabRegistry.LIST_EQUIPMENT);
        this.golem = golem;
    }
}