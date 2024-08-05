package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemThroughputDisplaySource extends AccumulatedItemCountDisplaySource {

    static final int POOL_SIZE = 10;

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        CompoundTag conf = context.sourceConfig();
        if (conf.contains("Inactive")) {
            return ZERO.copy();
        } else {
            double interval = 20.0 * Math.pow(60.0, (double) conf.getInt("Interval"));
            double rate = (double) conf.getFloat("Rate") * interval;
            if (rate > 0.0) {
                long previousTime = conf.getLong("LastReceived");
                long gameTime = context.blockEntity().m_58904_().getGameTime();
                int diff = (int) (gameTime - previousTime);
                if (diff > 0) {
                    int lastAmount = conf.getInt("LastReceivedAmount");
                    double timeBetweenStacks = (double) lastAmount / rate;
                    if ((double) diff > timeBetweenStacks * 2.0) {
                        conf.putBoolean("Inactive", true);
                    }
                }
            }
            return Lang.number(rate).component();
        }
    }

    @Override
    public void itemReceived(DisplayLinkBlockEntity be, int amount) {
        if (!(Boolean) be.m_58900_().m_61145_(DisplayLinkBlock.POWERED).orElse(true)) {
            CompoundTag conf = be.getSourceConfig();
            long gameTime = be.m_58904_().getGameTime();
            if (!conf.contains("LastReceived")) {
                conf.putLong("LastReceived", gameTime);
            } else {
                long previousTime = conf.getLong("LastReceived");
                ListTag rates = conf.getList("PrevRates", 5);
                if (rates.size() != 10) {
                    rates = new ListTag();
                    for (int i = 0; i < 10; i++) {
                        rates.add(FloatTag.valueOf(-1.0F));
                    }
                }
                int poolIndex = conf.getInt("Index") % 10;
                rates.set(poolIndex, (Tag) FloatTag.valueOf((float) ((double) amount / (double) (gameTime - previousTime))));
                float rate = 0.0F;
                int validIntervals = 0;
                for (int i = 0; i < 10; i++) {
                    float pooledRate = rates.getFloat(i);
                    if (pooledRate >= 0.0F) {
                        rate += pooledRate;
                        validIntervals++;
                    }
                }
                conf.remove("Rate");
                if (validIntervals > 0) {
                    rate /= (float) validIntervals;
                    conf.putFloat("Rate", rate);
                }
                conf.remove("Inactive");
                conf.putInt("LastReceivedAmount", amount);
                conf.putLong("LastReceived", gameTime);
                conf.putInt("Index", poolIndex + 1);
                conf.put("PrevRates", rates);
                be.updateGatheredData();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (!isFirstLine) {
            builder.addSelectionScrollInput(0, 80, (si, l) -> si.forOptions(Lang.translatedOptions("display_source.item_throughput.interval", "second", "minute", "hour")).titled(Lang.translateDirect("display_source.item_throughput.interval")), "Interval");
        }
    }

    @Override
    protected String getTranslationKey() {
        return "item_throughput";
    }
}