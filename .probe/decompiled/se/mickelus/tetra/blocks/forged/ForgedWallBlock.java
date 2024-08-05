package se.mickelus.tetra.blocks.forged;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import se.mickelus.tetra.blocks.TetraBlock;

@ParametersAreNonnullByDefault
public class ForgedWallBlock extends TetraBlock {

    public static final String identifier = "forged_wall";

    public ForgedWallBlock() {
        super(ForgedBlockCommon.propertiesSolid);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }
}