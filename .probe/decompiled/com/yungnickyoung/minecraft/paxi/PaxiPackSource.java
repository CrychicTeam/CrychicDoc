package com.yungnickyoung.minecraft.paxi;

import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;

public interface PaxiPackSource extends PackSource {

    PackSource PACK_SOURCE_PAXI = PackSource.create(decorateWithPaxiSource(), true);

    static UnaryOperator<Component> decorateWithPaxiSource() {
        Component paxiText = Component.literal("paxi");
        return component -> Component.translatable("pack.nameAndSource", component, paxiText).withStyle(ChatFormatting.LIGHT_PURPLE);
    }
}