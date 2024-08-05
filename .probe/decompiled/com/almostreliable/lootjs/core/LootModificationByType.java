package com.almostreliable.lootjs.core;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;

public class LootModificationByType extends AbstractLootModification {

    private final List<LootContextType> types;

    public LootModificationByType(String name, List<LootContextType> types, List<ILootHandler> handlers) {
        super(name, handlers);
        this.types = types;
    }

    @Override
    public boolean shouldExecute(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        return data != null && this.matchesType(data);
    }

    private boolean matchesType(ILootContextData pData) {
        for (LootContextType type : this.types) {
            if (pData.getLootContextType() == type) {
                return true;
            }
        }
        return false;
    }
}