package org.violetmoon.quark.content.building.module;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "building")
public class MorePottedPlantsModule extends ZetaModule {

    private static Map<Block, Block> tintedBlocks = new HashMap();

    @Hint(key = "pottable_stuff")
    List<Block> pottableBlocks = Lists.newArrayList();

    @LoadEvent
    public final void register(ZRegister event) {
        this.add(event, Blocks.BEETROOTS, "beetroot");
        this.add(event, Blocks.SWEET_BERRY_BUSH, "berries");
        this.add(event, Blocks.CARROTS, "carrot");
        this.add(event, Blocks.CHORUS_FLOWER, "chorus");
        this.add(event, Blocks.COCOA, "cocoa_bean");
        Block grass = this.add(event, Blocks.GRASS, "grass");
        this.add(event, Blocks.PEONY, "peony");
        Block largeFern = this.add(event, Blocks.LARGE_FERN, "large_fern");
        this.add(event, Blocks.LILAC, "lilac");
        this.add(event, Blocks.MELON_STEM, "melon");
        this.add(event, Blocks.NETHER_SPROUTS, "nether_sprouts");
        this.add(event, Blocks.NETHER_WART, "nether_wart");
        this.add(event, Blocks.POTATOES, "potato");
        this.add(event, Blocks.PUMPKIN_STEM, "pumpkin");
        this.add(event, Blocks.ROSE_BUSH, "rose");
        event.getVariantRegistry().addFlowerPot(Blocks.SEA_PICKLE, "sea_pickle", p -> p.lightLevel(b -> 3));
        Block sugarCane = this.add(event, Blocks.SUGAR_CANE, "sugar_cane");
        this.add(event, Blocks.SUNFLOWER, "sunflower");
        Block tallGrass = this.add(event, Blocks.TALL_GRASS, "tall_grass");
        this.add(event, Blocks.TWISTING_VINES, "twisting_vines");
        Block vine = this.add(event, Blocks.VINE, "vine");
        this.add(event, Blocks.WEEPING_VINES, "weeping_vines");
        this.add(event, Blocks.WHEAT, "wheat");
        event.getVariantRegistry().addFlowerPot(Blocks.CAVE_VINES, "cave_vines", p -> p.lightLevel(b -> 14));
        this.add(event, Blocks.PITCHER_PLANT, "pitcher_plant");
        tintedBlocks.put(grass, Blocks.GRASS);
        tintedBlocks.put(largeFern, Blocks.LARGE_FERN);
        tintedBlocks.put(sugarCane, Blocks.SUGAR_CANE);
        tintedBlocks.put(tallGrass, Blocks.TALL_GRASS);
        tintedBlocks.put(vine, Blocks.VINE);
    }

    private FlowerPotBlock add(ZRegister event, Block block, String name) {
        this.pottableBlocks.add(block);
        return event.getVariantRegistry().addFlowerPot(block, name, Functions.identity());
    }

    @LoadEvent
    public final void clientSetup(ZClientSetup event) {
        for (Block b : tintedBlocks.keySet()) {
            BlockState tState = ((Block) tintedBlocks.get(b)).defaultBlockState();
            BlockColor color = (state, worldIn, pos, tintIndex) -> Minecraft.getInstance().getBlockColors().getColor(tState, worldIn, pos, tintIndex);
            Minecraft.getInstance().getBlockColors().register(color, b);
        }
    }
}