package net.minecraft.client.gui.screens;

import com.mojang.authlib.minecraft.BanDetails;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.time.Duration;
import java.time.Instant;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.chat.report.BanReason;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.StringUtils;

public class BanNoticeScreen {

    private static final Component TEMPORARY_BAN_TITLE = Component.translatable("gui.banned.title.temporary").withStyle(ChatFormatting.BOLD);

    private static final Component PERMANENT_BAN_TITLE = Component.translatable("gui.banned.title.permanent").withStyle(ChatFormatting.BOLD);

    public static ConfirmLinkScreen create(BooleanConsumer booleanConsumer0, BanDetails banDetails1) {
        return new ConfirmLinkScreen(booleanConsumer0, getBannedTitle(banDetails1), getBannedScreenText(banDetails1), "https://aka.ms/mcjavamoderation", CommonComponents.GUI_ACKNOWLEDGE, true);
    }

    private static Component getBannedTitle(BanDetails banDetails0) {
        return isTemporaryBan(banDetails0) ? TEMPORARY_BAN_TITLE : PERMANENT_BAN_TITLE;
    }

    private static Component getBannedScreenText(BanDetails banDetails0) {
        return Component.translatable("gui.banned.description", getBanReasonText(banDetails0), getBanStatusText(banDetails0), Component.literal("https://aka.ms/mcjavamoderation"));
    }

    private static Component getBanReasonText(BanDetails banDetails0) {
        String $$1 = banDetails0.reason();
        String $$2 = banDetails0.reasonMessage();
        if (StringUtils.isNumeric($$1)) {
            int $$3 = Integer.parseInt($$1);
            BanReason $$4 = BanReason.byId($$3);
            Component $$5;
            if ($$4 != null) {
                $$5 = ComponentUtils.mergeStyles($$4.title().copy(), Style.EMPTY.withBold(true));
            } else if ($$2 != null) {
                $$5 = Component.translatable("gui.banned.description.reason_id_message", $$3, $$2).withStyle(ChatFormatting.BOLD);
            } else {
                $$5 = Component.translatable("gui.banned.description.reason_id", $$3).withStyle(ChatFormatting.BOLD);
            }
            return Component.translatable("gui.banned.description.reason", $$5);
        } else {
            return Component.translatable("gui.banned.description.unknownreason");
        }
    }

    private static Component getBanStatusText(BanDetails banDetails0) {
        if (isTemporaryBan(banDetails0)) {
            Component $$1 = getBanDurationText(banDetails0);
            return Component.translatable("gui.banned.description.temporary", Component.translatable("gui.banned.description.temporary.duration", $$1).withStyle(ChatFormatting.BOLD));
        } else {
            return Component.translatable("gui.banned.description.permanent").withStyle(ChatFormatting.BOLD);
        }
    }

    private static Component getBanDurationText(BanDetails banDetails0) {
        Duration $$1 = Duration.between(Instant.now(), banDetails0.expires());
        long $$2 = $$1.toHours();
        if ($$2 > 72L) {
            return CommonComponents.days($$1.toDays());
        } else {
            return $$2 < 1L ? CommonComponents.minutes($$1.toMinutes()) : CommonComponents.hours($$1.toHours());
        }
    }

    private static boolean isTemporaryBan(BanDetails banDetails0) {
        return banDetails0.expires() != null;
    }
}