package com.simibubi.create.content.redstone.displayLink.source;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Lang;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.Objective;

public class ScoreboardDisplaySource extends ValueListDisplaySource {

    @Override
    protected Stream<IntAttached<MutableComponent>> provideEntries(DisplayLinkContext context, int maxRows) {
        if (context.blockEntity().m_58904_() instanceof ServerLevel sLevel) {
            String name = context.sourceConfig().getString("Objective");
            return this.showScoreboard(sLevel, name, maxRows);
        } else {
            return Stream.empty();
        }
    }

    protected Stream<IntAttached<MutableComponent>> showScoreboard(ServerLevel sLevel, String objectiveName, int maxRows) {
        Objective objective = sLevel.getScoreboard().m_83477_(objectiveName);
        return objective == null ? this.notFound(objectiveName).stream() : sLevel.getScoreboard().m_83498_(objective).stream().map(score -> IntAttached.with(score.getScore(), Components.literal(score.getOwner()).m_6881_())).sorted(IntAttached.comparator()).limit((long) maxRows);
    }

    private ImmutableList<IntAttached<MutableComponent>> notFound(String objective) {
        return ImmutableList.of(IntAttached.with(404, Lang.translateDirect("display_source.scoreboard.objective_not_found", objective)));
    }

    @Override
    protected String getTranslationKey() {
        return "scoreboard";
    }

    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        if (isFirstLine) {
            builder.addTextInput(0, 137, (e, t) -> {
                e.setValue("");
                t.withTooltip(ImmutableList.of(Lang.translateDirect("display_source.scoreboard.objective").withStyle(s -> s.withColor(5476833)), Lang.translateDirect("gui.schedule.lmb_edit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
            }, "Objective");
        } else {
            this.addFullNumberConfig(builder);
        }
    }

    @Override
    protected boolean valueFirst() {
        return false;
    }
}