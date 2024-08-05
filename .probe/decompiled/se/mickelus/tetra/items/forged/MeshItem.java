package se.mickelus.tetra.items.forged;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class MeshItem extends TetraItem {

    public static final String identifier = "forged_mesh";

    @ObjectHolder(registryName = "item", value = "tetra:forged_mesh")
    public static MeshItem instance;

    public MeshItem() {
        super(new Item.Properties());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }
}