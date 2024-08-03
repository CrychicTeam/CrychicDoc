package io.github.lightman314.lightmanscurrency.api.traders.terminal;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import java.util.Comparator;
import javax.annotation.Nonnull;

public class TerminalSorter {

    private TerminalSorter() {
    }

    public static TerminalSorter.SortingOptions options() {
        return new TerminalSorter.SortingOptions();
    }

    @Nonnull
    public static Comparator<TraderData> getDefaultSorter() {
        return getSorter(options().withCreativePriority(true).withAuctionHousePriority(true).withEmptyLowPriority(true).withUnnamedLowPriorityFromConfig());
    }

    @Nonnull
    public static Comparator<TraderData> getSorter(@Nonnull TerminalSorter.SortingOptions options) {
        return new TerminalSorter.TraderSorter(options);
    }

    public static final class SortingOptions {

        private boolean creativeAtTop = false;

        private boolean auctionHouseAtTop = false;

        private boolean emptyAtBottom = false;

        private boolean unnamedAtBottom = false;

        private SortingOptions() {
        }

        public TerminalSorter.SortingOptions withCreativePriority(boolean priority) {
            this.creativeAtTop = priority;
            return this;
        }

        public TerminalSorter.SortingOptions withAuctionHousePriority(boolean priority) {
            this.auctionHouseAtTop = priority;
            return this;
        }

        public TerminalSorter.SortingOptions withEmptyLowPriority(boolean priority) {
            this.emptyAtBottom = priority;
            return this;
        }

        public TerminalSorter.SortingOptions withUnnamedLowPriority(boolean priority) {
            this.unnamedAtBottom = priority;
            return this;
        }

        public TerminalSorter.SortingOptions withUnnamedLowPriorityFromConfig() {
            return this.withUnnamedLowPriority(LCConfig.SERVER.moveUnnamedTradersToBottom.get());
        }
    }

    private static record TraderSorter(@Nonnull TerminalSorter.SortingOptions options) implements Comparator<TraderData> {

        public int compare(TraderData a, TraderData b) {
            try {
                if (this.options.auctionHouseAtTop) {
                    boolean ahA = a instanceof AuctionHouseTrader;
                    boolean ahB = b instanceof AuctionHouseTrader;
                    if (ahA && !ahB) {
                        return -1;
                    }
                    if (ahB && !ahA) {
                        return 1;
                    }
                }
                if (this.options.emptyAtBottom) {
                    boolean emptyA = !a.hasValidTrade();
                    boolean emptyB = !b.hasValidTrade();
                    if (emptyA != emptyB) {
                        return emptyA ? 1 : -1;
                    }
                }
                if (this.options.creativeAtTop) {
                    if (a.isCreative() && !b.isCreative()) {
                        return -1;
                    }
                    if (b.isCreative() && !a.isCreative()) {
                        return 1;
                    }
                }
                if (this.options.unnamedAtBottom) {
                    if (a.hasCustomName() && !b.hasCustomName()) {
                        return -1;
                    }
                    if (b.hasCustomName() && !a.hasCustomName()) {
                        return 1;
                    }
                }
                int sort = a.getName().getString().toLowerCase().compareTo(b.getName().getString().toLowerCase());
                if (sort == 0) {
                    sort = a.getOwner().getName().getString().compareToIgnoreCase(b.getOwner().getName().getString());
                }
                return sort;
            } catch (Throwable var5) {
                return 0;
            }
        }
    }
}