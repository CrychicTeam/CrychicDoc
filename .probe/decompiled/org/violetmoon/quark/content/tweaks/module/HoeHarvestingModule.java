package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolActions;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.play.ZBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.MiscUtil;

@ZetaLoadModule(category = "tweaks")
public class HoeHarvestingModule extends ZetaModule {

    @Config
    @Config.Min(1.0)
    @Config.Max(5.0)
    public static int regularHoeRadius = 2;

    @Config
    @Config.Min(1.0)
    @Config.Max(5.0)
    public static int highTierHoeRadius = 3;

    @Hint(key = "hoe_harvesting")
    TagKey<Item> hoes = ItemTags.HOES;

    public static TagKey<Item> bigHarvestingHoesTag;

    public static int getRange(ItemStack hoe) {
        if (!Quark.ZETA.modules.isEnabled(HoeHarvestingModule.class)) {
            return 1;
        } else if (!isHoe(hoe)) {
            return 1;
        } else {
            return hoe.is(bigHarvestingHoesTag) ? highTierHoeRadius : regularHoeRadius;
        }
    }

    public static boolean isHoe(ItemStack itemStack) {
        return !itemStack.isEmpty() && (itemStack.getItem() instanceof HoeItem || itemStack.is(ItemTags.HOES) || itemStack.getItem().canPerformAction(itemStack, ToolActions.HOE_DIG));
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        bigHarvestingHoesTag = ItemTags.create(new ResourceLocation("quark", "big_harvesting_hoes"));
    }

    @PlayEvent
    public void onBlockBroken(ZBlock.Break event) {
        LevelAccessor world = event.getLevel();
        if (world instanceof Level level) {
            Player player = event.getPlayer();
            BlockPos basePos = event.getPos();
            ItemStack stack = player.m_21205_();
            if (isHoe(stack) && this.canHarvest(player, world, basePos, event.getState())) {
                boolean brokeNonInstant = false;
                int range = getRange(stack);
                for (int i = 1 - range; i < range; i++) {
                    for (int k = 1 - range; k < range; k++) {
                        if (i != 0 || k != 0) {
                            BlockPos pos = basePos.offset(i, 0, k);
                            BlockState state = world.m_8055_(pos);
                            if (this.canHarvest(player, world, pos, state)) {
                                Block block = state.m_60734_();
                                if (state.m_60800_(world, pos) != 0.0F) {
                                    brokeNonInstant = true;
                                }
                                block.playerWillDestroy(level, pos, state, player);
                                if (block.canHarvestBlock(state, world, pos, player)) {
                                    block.playerDestroy(level, player, pos, state, world.m_7702_(pos), stack);
                                }
                                world.m_46961_(pos, false);
                                world.levelEvent(2001, pos, Block.getId(state));
                            }
                        }
                    }
                }
                if (brokeNonInstant) {
                    MiscUtil.damageStack(player, InteractionHand.MAIN_HAND, stack, 1);
                }
            }
        }
    }

    private boolean canHarvest(Player player, LevelAccessor world, BlockPos pos, BlockState state) {
        if (state.m_60734_() instanceof IPlantable plant) {
            PlantType type = plant.getPlantType(world, pos);
            return type != PlantType.WATER && type != PlantType.DESERT;
        } else {
            return isHarvestableMaterial(state) && state.m_60629_(new BlockPlaceContext(new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(0.5, 0.5, 0.5), Direction.DOWN, pos, false))));
        }
    }

    public static boolean isHarvestableMaterial(BlockState state) {
        NoteBlockInstrument instrument = state.m_280603_();
        boolean PLANT = state.f_283893_ == MapColor.PLANT && state.m_60811_() == PushReaction.DESTROY;
        boolean WATER_PLANT = state.f_283893_ == MapColor.WATER && instrument == NoteBlockInstrument.BASEDRUM;
        boolean REPLACEABLE_FIREPROOF_PLANT = state.f_283893_ == MapColor.PLANT && state.m_247087_() && state.m_60811_() == PushReaction.DESTROY;
        boolean REPLACEABLE_WATER_PLANT = state.f_283893_ == MapColor.WATER && state.m_247087_() && state.m_60811_() == PushReaction.DESTROY;
        return PLANT || WATER_PLANT || REPLACEABLE_FIREPROOF_PLANT || REPLACEABLE_WATER_PLANT;
    }
}