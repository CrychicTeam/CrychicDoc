package se.mickelus.tetra;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.common.ToolAction;

@ParametersAreNonnullByDefault
public class TetraToolActions {

    public static final ToolAction cut = ToolAction.get("cut");

    public static final ToolAction hammer = ToolAction.get("hammer_dig");

    public static final ToolAction pry = ToolAction.get("pry");

    public static final ToolAction dowse = ToolAction.get("dowse");
}