package se.mickelus.tetra.blocks.workbench.action;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.data.ToolData;

public abstract class ConfigAction implements WorkbenchAction {

    public String key;

    public ItemPredicate requirement;

    public ToolData requiredTools = new ToolData();

    public ResourceLocation lootTable;

    public boolean inWorld = true;
}