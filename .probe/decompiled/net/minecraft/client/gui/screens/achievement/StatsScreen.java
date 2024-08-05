package net.minecraft.client.gui.screens.achievement;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class StatsScreen extends Screen implements StatsUpdateListener {

    private static final Component PENDING_TEXT = Component.translatable("multiplayer.downloadingStats");

    private static final ResourceLocation STATS_ICON_LOCATION = new ResourceLocation("textures/gui/container/stats_icons.png");

    protected final Screen lastScreen;

    private StatsScreen.GeneralStatisticsList statsList;

    StatsScreen.ItemStatisticsList itemStatsList;

    private StatsScreen.MobsStatisticsList mobsStatsList;

    final StatsCounter stats;

    @Nullable
    private ObjectSelectionList<?> activeList;

    private boolean isLoading = true;

    private static final int SLOT_TEX_SIZE = 128;

    private static final int SLOT_BG_SIZE = 18;

    private static final int SLOT_STAT_HEIGHT = 20;

    private static final int SLOT_BG_X = 1;

    private static final int SLOT_BG_Y = 1;

    private static final int SLOT_FG_X = 2;

    private static final int SLOT_FG_Y = 2;

    private static final int SLOT_LEFT_INSERT = 40;

    private static final int SLOT_TEXT_OFFSET = 5;

    private static final int SORT_NONE = 0;

    private static final int SORT_DOWN = -1;

    private static final int SORT_UP = 1;

    public StatsScreen(Screen screen0, StatsCounter statsCounter1) {
        super(Component.translatable("gui.stats"));
        this.lastScreen = screen0;
        this.stats = statsCounter1;
    }

    @Override
    protected void init() {
        this.isLoading = true;
        this.f_96541_.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
    }

    public void initLists() {
        this.statsList = new StatsScreen.GeneralStatisticsList(this.f_96541_);
        this.itemStatsList = new StatsScreen.ItemStatisticsList(this.f_96541_);
        this.mobsStatsList = new StatsScreen.MobsStatisticsList(this.f_96541_);
    }

    public void initButtons() {
        this.m_142416_(Button.builder(Component.translatable("stat.generalButton"), p_96963_ -> this.setActiveList(this.statsList)).bounds(this.f_96543_ / 2 - 120, this.f_96544_ - 52, 80, 20).build());
        Button $$0 = (Button) this.m_142416_(Button.builder(Component.translatable("stat.itemsButton"), p_96959_ -> this.setActiveList(this.itemStatsList)).bounds(this.f_96543_ / 2 - 40, this.f_96544_ - 52, 80, 20).build());
        Button $$1 = (Button) this.m_142416_(Button.builder(Component.translatable("stat.mobsButton"), p_96949_ -> this.setActiveList(this.mobsStatsList)).bounds(this.f_96543_ / 2 + 40, this.f_96544_ - 52, 80, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280843_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 28, 200, 20).build());
        if (this.itemStatsList.m_6702_().isEmpty()) {
            $$0.f_93623_ = false;
        }
        if (this.mobsStatsList.m_6702_().isEmpty()) {
            $$1.f_93623_ = false;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.isLoading) {
            this.m_280273_(guiGraphics0);
            guiGraphics0.drawCenteredString(this.f_96547_, PENDING_TEXT, this.f_96543_ / 2, this.f_96544_ / 2, 16777215);
            guiGraphics0.drawCenteredString(this.f_96547_, f_97124_[(int) (Util.getMillis() / 150L % (long) f_97124_.length)], this.f_96543_ / 2, this.f_96544_ / 2 + 9 * 2, 16777215);
        } else {
            this.getActiveList().m_88315_(guiGraphics0, int1, int2, float3);
            guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
            super.render(guiGraphics0, int1, int2, float3);
        }
    }

    @Override
    public void onStatsUpdated() {
        if (this.isLoading) {
            this.initLists();
            this.initButtons();
            this.setActiveList(this.statsList);
            this.isLoading = false;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return !this.isLoading;
    }

    @Nullable
    public ObjectSelectionList<?> getActiveList() {
        return this.activeList;
    }

    public void setActiveList(@Nullable ObjectSelectionList<?> objectSelectionList0) {
        if (this.activeList != null) {
            this.m_169411_(this.activeList);
        }
        if (objectSelectionList0 != null) {
            this.m_7787_(objectSelectionList0);
            this.activeList = objectSelectionList0;
        }
    }

    static String getTranslationKey(Stat<ResourceLocation> statResourceLocation0) {
        return "stat." + statResourceLocation0.getValue().toString().replace(':', '.');
    }

    int getColumnX(int int0) {
        return 115 + 40 * int0;
    }

    void blitSlot(GuiGraphics guiGraphics0, int int1, int int2, Item item3) {
        this.blitSlotIcon(guiGraphics0, int1 + 1, int2 + 1, 0, 0);
        guiGraphics0.renderFakeItem(item3.getDefaultInstance(), int1 + 2, int2 + 2);
    }

    void blitSlotIcon(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        guiGraphics0.blit(STATS_ICON_LOCATION, int1, int2, 0, (float) int3, (float) int4, 18, 18, 128, 128);
    }

    class GeneralStatisticsList extends ObjectSelectionList<StatsScreen.GeneralStatisticsList.Entry> {

        public GeneralStatisticsList(Minecraft minecraft0) {
            super(minecraft0, StatsScreen.this.f_96543_, StatsScreen.this.f_96544_, 32, StatsScreen.this.f_96544_ - 64, 10);
            ObjectArrayList<Stat<ResourceLocation>> $$1 = new ObjectArrayList(Stats.CUSTOM.iterator());
            $$1.sort(Comparator.comparing(p_96997_ -> I18n.get(StatsScreen.getTranslationKey(p_96997_))));
            ObjectListIterator var4 = $$1.iterator();
            while (var4.hasNext()) {
                Stat<ResourceLocation> $$2 = (Stat<ResourceLocation>) var4.next();
                this.m_7085_(new StatsScreen.GeneralStatisticsList.Entry($$2));
            }
        }

        @Override
        protected void renderBackground(GuiGraphics guiGraphics0) {
            StatsScreen.this.m_280273_(guiGraphics0);
        }

        class Entry extends ObjectSelectionList.Entry<StatsScreen.GeneralStatisticsList.Entry> {

            private final Stat<ResourceLocation> stat;

            private final Component statDisplay;

            Entry(Stat<ResourceLocation> statResourceLocation0) {
                this.stat = statResourceLocation0;
                this.statDisplay = Component.translatable(StatsScreen.getTranslationKey(statResourceLocation0));
            }

            private String getValueText() {
                return this.stat.format(StatsScreen.this.stats.getValue(this.stat));
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                guiGraphics0.drawString(StatsScreen.this.f_96547_, this.statDisplay, int3 + 2, int2 + 1, int1 % 2 == 0 ? 16777215 : 9474192);
                String $$10 = this.getValueText();
                guiGraphics0.drawString(StatsScreen.this.f_96547_, $$10, int3 + 2 + 213 - StatsScreen.this.f_96547_.width($$10), int2 + 1, int1 % 2 == 0 ? 16777215 : 9474192);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(this.getValueText()));
            }
        }
    }

    class ItemStatisticsList extends ObjectSelectionList<StatsScreen.ItemStatisticsList.ItemRow> {

        protected final List<StatType<Block>> blockColumns;

        protected final List<StatType<Item>> itemColumns;

        private final int[] iconOffsets = new int[] { 3, 4, 1, 2, 5, 6 };

        protected int headerPressed = -1;

        protected final Comparator<StatsScreen.ItemStatisticsList.ItemRow> itemStatSorter = new StatsScreen.ItemStatisticsList.ItemRowComparator();

        @Nullable
        protected StatType<?> sortColumn;

        protected int sortOrder;

        public ItemStatisticsList(Minecraft minecraft0) {
            super(minecraft0, StatsScreen.this.f_96543_, StatsScreen.this.f_96544_, 32, StatsScreen.this.f_96544_ - 64, 20);
            this.blockColumns = Lists.newArrayList();
            this.blockColumns.add(Stats.BLOCK_MINED);
            this.itemColumns = Lists.newArrayList(new StatType[] { Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED });
            this.m_93473_(true, 20);
            Set<Item> $$1 = Sets.newIdentityHashSet();
            for (Item $$2 : BuiltInRegistries.ITEM) {
                boolean $$3 = false;
                for (StatType<Item> $$4 : this.itemColumns) {
                    if ($$4.contains($$2) && StatsScreen.this.stats.getValue($$4.get($$2)) > 0) {
                        $$3 = true;
                    }
                }
                if ($$3) {
                    $$1.add($$2);
                }
            }
            for (Block $$5 : BuiltInRegistries.BLOCK) {
                boolean $$6 = false;
                for (StatType<Block> $$7 : this.blockColumns) {
                    if ($$7.contains($$5) && StatsScreen.this.stats.getValue($$7.get($$5)) > 0) {
                        $$6 = true;
                    }
                }
                if ($$6) {
                    $$1.add($$5.asItem());
                }
            }
            $$1.remove(Items.AIR);
            for (Item $$8 : $$1) {
                this.m_7085_(new StatsScreen.ItemStatisticsList.ItemRow($$8));
            }
        }

        @Override
        protected void renderHeader(GuiGraphics guiGraphics0, int int1, int int2) {
            if (!this.f_93386_.mouseHandler.isLeftPressed()) {
                this.headerPressed = -1;
            }
            for (int $$3 = 0; $$3 < this.iconOffsets.length; $$3++) {
                StatsScreen.this.blitSlotIcon(guiGraphics0, int1 + StatsScreen.this.getColumnX($$3) - 18, int2 + 1, 0, this.headerPressed == $$3 ? 0 : 18);
            }
            if (this.sortColumn != null) {
                int $$4 = StatsScreen.this.getColumnX(this.getColumnIndex(this.sortColumn)) - 36;
                int $$5 = this.sortOrder == 1 ? 2 : 1;
                StatsScreen.this.blitSlotIcon(guiGraphics0, int1 + $$4, int2 + 1, 18 * $$5, 0);
            }
            for (int $$6 = 0; $$6 < this.iconOffsets.length; $$6++) {
                int $$7 = this.headerPressed == $$6 ? 1 : 0;
                StatsScreen.this.blitSlotIcon(guiGraphics0, int1 + StatsScreen.this.getColumnX($$6) - 18 + $$7, int2 + 1 + $$7, 18 * this.iconOffsets[$$6], 18);
            }
        }

        @Override
        public int getRowWidth() {
            return 375;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93388_ / 2 + 140;
        }

        @Override
        protected void renderBackground(GuiGraphics guiGraphics0) {
            StatsScreen.this.m_280273_(guiGraphics0);
        }

        @Override
        protected void clickedHeader(int int0, int int1) {
            this.headerPressed = -1;
            for (int $$2 = 0; $$2 < this.iconOffsets.length; $$2++) {
                int $$3 = int0 - StatsScreen.this.getColumnX($$2);
                if ($$3 >= -36 && $$3 <= 0) {
                    this.headerPressed = $$2;
                    break;
                }
            }
            if (this.headerPressed >= 0) {
                this.sortByColumn(this.getColumn(this.headerPressed));
                this.f_93386_.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }

        private StatType<?> getColumn(int int0) {
            return int0 < this.blockColumns.size() ? (StatType) this.blockColumns.get(int0) : (StatType) this.itemColumns.get(int0 - this.blockColumns.size());
        }

        private int getColumnIndex(StatType<?> statType0) {
            int $$1 = this.blockColumns.indexOf(statType0);
            if ($$1 >= 0) {
                return $$1;
            } else {
                int $$2 = this.itemColumns.indexOf(statType0);
                return $$2 >= 0 ? $$2 + this.blockColumns.size() : -1;
            }
        }

        @Override
        protected void renderDecorations(GuiGraphics guiGraphics0, int int1, int int2) {
            if (int2 >= this.f_93390_ && int2 <= this.f_93391_) {
                StatsScreen.ItemStatisticsList.ItemRow $$3 = (StatsScreen.ItemStatisticsList.ItemRow) this.m_168795_();
                int $$4 = (this.f_93388_ - this.getRowWidth()) / 2;
                if ($$3 != null) {
                    if (int1 < $$4 + 40 || int1 > $$4 + 40 + 20) {
                        return;
                    }
                    Item $$5 = $$3.getItem();
                    this.renderMousehoverTooltip(guiGraphics0, this.getString($$5), int1, int2);
                } else {
                    Component $$6 = null;
                    int $$7 = int1 - $$4;
                    for (int $$8 = 0; $$8 < this.iconOffsets.length; $$8++) {
                        int $$9 = StatsScreen.this.getColumnX($$8);
                        if ($$7 >= $$9 - 18 && $$7 <= $$9) {
                            $$6 = this.getColumn($$8).getDisplayName();
                            break;
                        }
                    }
                    this.renderMousehoverTooltip(guiGraphics0, $$6, int1, int2);
                }
            }
        }

        protected void renderMousehoverTooltip(GuiGraphics guiGraphics0, @Nullable Component component1, int int2, int int3) {
            if (component1 != null) {
                int $$4 = int2 + 12;
                int $$5 = int3 - 12;
                int $$6 = StatsScreen.this.f_96547_.width(component1);
                guiGraphics0.fillGradient($$4 - 3, $$5 - 3, $$4 + $$6 + 3, $$5 + 8 + 3, -1073741824, -1073741824);
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().translate(0.0F, 0.0F, 400.0F);
                guiGraphics0.drawString(StatsScreen.this.f_96547_, component1, $$4, $$5, -1);
                guiGraphics0.pose().popPose();
            }
        }

        protected Component getString(Item item0) {
            return item0.getDescription();
        }

        protected void sortByColumn(StatType<?> statType0) {
            if (statType0 != this.sortColumn) {
                this.sortColumn = statType0;
                this.sortOrder = -1;
            } else if (this.sortOrder == -1) {
                this.sortOrder = 1;
            } else {
                this.sortColumn = null;
                this.sortOrder = 0;
            }
            this.m_6702_().sort(this.itemStatSorter);
        }

        class ItemRow extends ObjectSelectionList.Entry<StatsScreen.ItemStatisticsList.ItemRow> {

            private final Item item;

            ItemRow(Item item0) {
                this.item = item0;
            }

            public Item getItem() {
                return this.item;
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                StatsScreen.this.blitSlot(guiGraphics0, int3 + 40, int2, this.item);
                for (int $$10 = 0; $$10 < StatsScreen.this.itemStatsList.blockColumns.size(); $$10++) {
                    Stat<Block> $$11;
                    if (this.item instanceof BlockItem) {
                        $$11 = ((StatType) StatsScreen.this.itemStatsList.blockColumns.get($$10)).get(((BlockItem) this.item).getBlock());
                    } else {
                        $$11 = null;
                    }
                    this.renderStat(guiGraphics0, $$11, int3 + StatsScreen.this.getColumnX($$10), int2, int1 % 2 == 0);
                }
                for (int $$13 = 0; $$13 < StatsScreen.this.itemStatsList.itemColumns.size(); $$13++) {
                    this.renderStat(guiGraphics0, ((StatType) StatsScreen.this.itemStatsList.itemColumns.get($$13)).get(this.item), int3 + StatsScreen.this.getColumnX($$13 + StatsScreen.this.itemStatsList.blockColumns.size()), int2, int1 % 2 == 0);
                }
            }

            protected void renderStat(GuiGraphics guiGraphics0, @Nullable Stat<?> stat1, int int2, int int3, boolean boolean4) {
                String $$5 = stat1 == null ? "-" : stat1.format(StatsScreen.this.stats.getValue(stat1));
                guiGraphics0.drawString(StatsScreen.this.f_96547_, $$5, int2 - StatsScreen.this.f_96547_.width($$5), int3 + 5, boolean4 ? 16777215 : 9474192);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.item.getDescription());
            }
        }

        class ItemRowComparator implements Comparator<StatsScreen.ItemStatisticsList.ItemRow> {

            public int compare(StatsScreen.ItemStatisticsList.ItemRow statsScreenItemStatisticsListItemRow0, StatsScreen.ItemStatisticsList.ItemRow statsScreenItemStatisticsListItemRow1) {
                Item $$2 = statsScreenItemStatisticsListItemRow0.getItem();
                Item $$3 = statsScreenItemStatisticsListItemRow1.getItem();
                int $$4;
                int $$5;
                if (ItemStatisticsList.this.sortColumn == null) {
                    $$4 = 0;
                    $$5 = 0;
                } else if (ItemStatisticsList.this.blockColumns.contains(ItemStatisticsList.this.sortColumn)) {
                    StatType<Block> $$6 = (StatType<Block>) ItemStatisticsList.this.sortColumn;
                    $$4 = $$2 instanceof BlockItem ? StatsScreen.this.stats.getValue($$6, ((BlockItem) $$2).getBlock()) : -1;
                    $$5 = $$3 instanceof BlockItem ? StatsScreen.this.stats.getValue($$6, ((BlockItem) $$3).getBlock()) : -1;
                } else {
                    StatType<Item> $$9 = (StatType<Item>) ItemStatisticsList.this.sortColumn;
                    $$4 = StatsScreen.this.stats.getValue($$9, $$2);
                    $$5 = StatsScreen.this.stats.getValue($$9, $$3);
                }
                return $$4 == $$5 ? ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId($$2), Item.getId($$3)) : ItemStatisticsList.this.sortOrder * Integer.compare($$4, $$5);
            }
        }
    }

    class MobsStatisticsList extends ObjectSelectionList<StatsScreen.MobsStatisticsList.MobRow> {

        public MobsStatisticsList(Minecraft minecraft0) {
            super(minecraft0, StatsScreen.this.f_96543_, StatsScreen.this.f_96544_, 32, StatsScreen.this.f_96544_ - 64, 9 * 4);
            for (EntityType<?> $$1 : BuiltInRegistries.ENTITY_TYPE) {
                if (StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get($$1)) > 0 || StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get($$1)) > 0) {
                    this.m_7085_(new StatsScreen.MobsStatisticsList.MobRow($$1));
                }
            }
        }

        @Override
        protected void renderBackground(GuiGraphics guiGraphics0) {
            StatsScreen.this.m_280273_(guiGraphics0);
        }

        class MobRow extends ObjectSelectionList.Entry<StatsScreen.MobsStatisticsList.MobRow> {

            private final Component mobName;

            private final Component kills;

            private final boolean hasKills;

            private final Component killedBy;

            private final boolean wasKilledBy;

            public MobRow(EntityType<?> entityType0) {
                this.mobName = entityType0.getDescription();
                int $$1 = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(entityType0));
                if ($$1 == 0) {
                    this.kills = Component.translatable("stat_type.minecraft.killed.none", this.mobName);
                    this.hasKills = false;
                } else {
                    this.kills = Component.translatable("stat_type.minecraft.killed", $$1, this.mobName);
                    this.hasKills = true;
                }
                int $$2 = StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(entityType0));
                if ($$2 == 0) {
                    this.killedBy = Component.translatable("stat_type.minecraft.killed_by.none", this.mobName);
                    this.wasKilledBy = false;
                } else {
                    this.killedBy = Component.translatable("stat_type.minecraft.killed_by", this.mobName, $$2);
                    this.wasKilledBy = true;
                }
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                guiGraphics0.drawString(StatsScreen.this.f_96547_, this.mobName, int3 + 2, int2 + 1, 16777215);
                guiGraphics0.drawString(StatsScreen.this.f_96547_, this.kills, int3 + 2 + 10, int2 + 1 + 9, this.hasKills ? 9474192 : 6316128);
                guiGraphics0.drawString(StatsScreen.this.f_96547_, this.killedBy, int3 + 2 + 10, int2 + 1 + 9 * 2, this.wasKilledBy ? 9474192 : 6316128);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", CommonComponents.joinForNarration(this.kills, this.killedBy));
            }
        }
    }
}