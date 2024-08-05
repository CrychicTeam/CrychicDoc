package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.entities.utility.PresentItem;
import com.mna.items.ItemInit;
import com.mna.tools.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class RitualEffectHole extends RitualEffect {

    protected static final int radius = 1;

    public RitualEffectHole(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        int delay = 10;
        for (BlockPos pos = context.getCenter().below(); pos.m_123342_() > context.getLevel().m_141937_() && context.getLevel().getBlockState(pos).m_60734_() != Blocks.BEDROCK; pos = pos.below()) {
            DelayedEventQueue.pushEvent(context.getLevel(), new TimedDelayedEvent<>("hole", delay, new RitualEffectHole.DelayData(context.getCaster(), pos, context.getLevel()), this::breakBlocks));
            delay += 10;
        }
        MutableBoolean drop_book = new MutableBoolean(true);
        context.getAllPositions().forEach(c -> {
            if (c.isPresent() && c.getReagent() != null && c.getReagent().getResourceLocation().equals(ForgeRegistries.ITEMS.getKey(ItemInit.FLAT_LANDS_BOOK.get())) && c.getReagent().shouldConsumeReagent()) {
                drop_book.setFalse();
            }
        });
        if (drop_book.booleanValue()) {
            PresentItem epi = new PresentItem(context.getLevel(), (double) context.getCenter().m_123341_(), (double) (context.getCenter().m_123342_() + 1), (double) context.getCenter().m_123343_());
            epi.m_32045_(new ItemStack(ItemInit.FLAT_LANDS_BOOK.get()));
            epi.m_20334_(0.0, 0.0, 0.0);
            context.getLevel().m_7967_(epi);
        }
        return true;
    }

    private void breakBlocks(String identifier, RitualEffectHole.DelayData data) {
        if (data.caster != null && data.world != null && data.world.isLoaded(data.center)) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    BlockPos target = data.center.offset(i, 0, j);
                    if (data.world.isLoaded(target) && data.world.getBlockEntity(target) == null) {
                        BlockUtils.destroyBlock(data.caster, data.world, target, false, Tiers.IRON);
                    }
                }
            }
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }

    protected class DelayData {

        public Player caster;

        public BlockPos center;

        public Level world;

        public DelayData(Player caster, BlockPos pos, Level world) {
            this.caster = caster;
            this.center = pos;
            this.world = world;
        }
    }
}