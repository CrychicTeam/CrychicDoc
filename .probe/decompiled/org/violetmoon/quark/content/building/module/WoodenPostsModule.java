package org.violetmoon.quark.content.building.module;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.block.WoodPostBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.VanillaWoods;
import org.violetmoon.zeta.util.handler.ToolInteractionHandler;

@ZetaLoadModule(category = "building")
public class WoodenPostsModule extends ZetaModule {

    @Hint
    TagKey<Item> postsTag;

    @LoadEvent
    public final void register(ZRegister event) {
        for (VanillaWoods.Wood wood : VanillaWoods.ALL) {
            Block b = wood.fence();
            WoodPostBlock post = new WoodPostBlock(this, b, "", wood.soundWood());
            WoodPostBlock stripped = new WoodPostBlock(this, b, "stripped_", wood.soundWood());
            ToolInteractionHandler.registerInteraction(ToolActions.AXE_STRIP, post, stripped);
        }
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        this.postsTag = ItemTags.create(new ResourceLocation("quark", "posts"));
    }

    public static boolean canHangingBlockConnect(BlockState state, LevelReader worldIn, BlockPos pos, boolean prev) {
        return prev || Quark.ZETA.modules.isEnabled(WoodenPostsModule.class) && (!state.m_61138_(LanternBlock.HANGING) || (Boolean) state.m_61143_(LanternBlock.HANGING)) && worldIn.m_8055_(pos.above()).m_60734_() instanceof WoodPostBlock;
    }
}