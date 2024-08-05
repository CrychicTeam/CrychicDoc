package com.simibubi.create.content.schematics.client.tools;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum ToolType {

    DEPLOY(new DeployTool(), AllIcons.I_TOOL_DEPLOY),
    MOVE(new MoveTool(), AllIcons.I_TOOL_MOVE_XZ),
    MOVE_Y(new MoveVerticalTool(), AllIcons.I_TOOL_MOVE_Y),
    ROTATE(new RotateTool(), AllIcons.I_TOOL_ROTATE),
    FLIP(new FlipTool(), AllIcons.I_TOOL_MIRROR),
    PRINT(new PlaceTool(), AllIcons.I_CONFIRM);

    private ISchematicTool tool;

    private AllIcons icon;

    private ToolType(ISchematicTool tool, AllIcons icon) {
        this.tool = tool;
        this.icon = icon;
    }

    public ISchematicTool getTool() {
        return this.tool;
    }

    public MutableComponent getDisplayName() {
        return Lang.translateDirect("schematic.tool." + Lang.asId(this.name()));
    }

    public AllIcons getIcon() {
        return this.icon;
    }

    public static List<ToolType> getTools(boolean creative) {
        List<ToolType> tools = new ArrayList();
        Collections.addAll(tools, new ToolType[] { MOVE, MOVE_Y, DEPLOY, ROTATE, FLIP });
        if (creative) {
            tools.add(PRINT);
        }
        return tools;
    }

    public List<Component> getDescription() {
        return Lang.translatedOptions("schematic.tool." + Lang.asId(this.name()) + ".description", "0", "1", "2", "3");
    }
}