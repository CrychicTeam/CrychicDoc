package net.minecraft.client.gui.screens;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

public class CreateFlatWorldScreen extends Screen {

    private static final int SLOT_TEX_SIZE = 128;

    private static final int SLOT_BG_SIZE = 18;

    private static final int SLOT_STAT_HEIGHT = 20;

    private static final int SLOT_BG_X = 1;

    private static final int SLOT_BG_Y = 1;

    private static final int SLOT_FG_X = 2;

    private static final int SLOT_FG_Y = 2;

    protected final CreateWorldScreen parent;

    private final Consumer<FlatLevelGeneratorSettings> applySettings;

    FlatLevelGeneratorSettings generator;

    private Component columnType;

    private Component columnHeight;

    private CreateFlatWorldScreen.DetailsList list;

    private Button deleteLayerButton;

    public CreateFlatWorldScreen(CreateWorldScreen createWorldScreen0, Consumer<FlatLevelGeneratorSettings> consumerFlatLevelGeneratorSettings1, FlatLevelGeneratorSettings flatLevelGeneratorSettings2) {
        super(Component.translatable("createWorld.customize.flat.title"));
        this.parent = createWorldScreen0;
        this.applySettings = consumerFlatLevelGeneratorSettings1;
        this.generator = flatLevelGeneratorSettings2;
    }

    public FlatLevelGeneratorSettings settings() {
        return this.generator;
    }

    public void setConfig(FlatLevelGeneratorSettings flatLevelGeneratorSettings0) {
        this.generator = flatLevelGeneratorSettings0;
    }

    @Override
    protected void init() {
        this.columnType = Component.translatable("createWorld.customize.flat.tile");
        this.columnHeight = Component.translatable("createWorld.customize.flat.height");
        this.list = new CreateFlatWorldScreen.DetailsList();
        this.m_7787_(this.list);
        this.deleteLayerButton = (Button) this.m_142416_(Button.builder(Component.translatable("createWorld.customize.flat.removeLayer"), p_95845_ -> {
            if (this.hasValidSelection()) {
                List<FlatLayerInfo> $$1 = this.generator.getLayersInfo();
                int $$2 = this.list.m_6702_().indexOf(this.list.m_93511_());
                int $$3 = $$1.size() - $$2 - 1;
                $$1.remove($$3);
                this.list.setSelected($$1.isEmpty() ? null : (CreateFlatWorldScreen.DetailsList.Entry) this.list.m_6702_().get(Math.min($$2, $$1.size() - 1)));
                this.generator.updateLayers();
                this.list.resetRows();
                this.updateButtonValidity();
            }
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 52, 150, 20).build());
        this.m_142416_(Button.builder(Component.translatable("createWorld.customize.presets"), p_280790_ -> {
            this.f_96541_.setScreen(new PresetFlatWorldScreen(this));
            this.generator.updateLayers();
            this.updateButtonValidity();
        }).bounds(this.f_96543_ / 2 + 5, this.f_96544_ - 52, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280791_ -> {
            this.applySettings.accept(this.generator);
            this.f_96541_.setScreen(this.parent);
            this.generator.updateLayers();
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 28, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280792_ -> {
            this.f_96541_.setScreen(this.parent);
            this.generator.updateLayers();
        }).bounds(this.f_96543_ / 2 + 5, this.f_96544_ - 28, 150, 20).build());
        this.generator.updateLayers();
        this.updateButtonValidity();
    }

    void updateButtonValidity() {
        this.deleteLayerButton.f_93623_ = this.hasValidSelection();
    }

    private boolean hasValidSelection() {
        return this.list.m_93511_() != null;
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.list.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        int $$4 = this.f_96543_ / 2 - 92 - 16;
        guiGraphics0.drawString(this.f_96547_, this.columnType, $$4, 32, 16777215);
        guiGraphics0.drawString(this.f_96547_, this.columnHeight, $$4 + 2 + 213 - this.f_96547_.width(this.columnHeight), 32, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    class DetailsList extends ObjectSelectionList<CreateFlatWorldScreen.DetailsList.Entry> {

        static final ResourceLocation STATS_ICON_LOCATION = new ResourceLocation("textures/gui/container/stats_icons.png");

        public DetailsList() {
            super(CreateFlatWorldScreen.this.f_96541_, CreateFlatWorldScreen.this.f_96543_, CreateFlatWorldScreen.this.f_96544_, 43, CreateFlatWorldScreen.this.f_96544_ - 60, 24);
            for (int $$0 = 0; $$0 < CreateFlatWorldScreen.this.generator.getLayersInfo().size(); $$0++) {
                this.m_7085_(new CreateFlatWorldScreen.DetailsList.Entry());
            }
        }

        public void setSelected(@Nullable CreateFlatWorldScreen.DetailsList.Entry createFlatWorldScreenDetailsListEntry0) {
            super.m_6987_(createFlatWorldScreenDetailsListEntry0);
            CreateFlatWorldScreen.this.updateButtonValidity();
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93388_ - 70;
        }

        public void resetRows() {
            int $$0 = this.m_6702_().indexOf(this.m_93511_());
            this.m_93516_();
            for (int $$1 = 0; $$1 < CreateFlatWorldScreen.this.generator.getLayersInfo().size(); $$1++) {
                this.m_7085_(new CreateFlatWorldScreen.DetailsList.Entry());
            }
            List<CreateFlatWorldScreen.DetailsList.Entry> $$2 = this.m_6702_();
            if ($$0 >= 0 && $$0 < $$2.size()) {
                this.setSelected((CreateFlatWorldScreen.DetailsList.Entry) $$2.get($$0));
            }
        }

        class Entry extends ObjectSelectionList.Entry<CreateFlatWorldScreen.DetailsList.Entry> {

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                FlatLayerInfo $$10 = (FlatLayerInfo) CreateFlatWorldScreen.this.generator.getLayersInfo().get(CreateFlatWorldScreen.this.generator.getLayersInfo().size() - int1 - 1);
                BlockState $$11 = $$10.getBlockState();
                ItemStack $$12 = this.getDisplayItem($$11);
                this.blitSlot(guiGraphics0, int3, int2, $$12);
                guiGraphics0.drawString(CreateFlatWorldScreen.this.f_96547_, $$12.getHoverName(), int3 + 18 + 5, int2 + 3, 16777215, false);
                Component $$13;
                if (int1 == 0) {
                    $$13 = Component.translatable("createWorld.customize.flat.layer.top", $$10.getHeight());
                } else if (int1 == CreateFlatWorldScreen.this.generator.getLayersInfo().size() - 1) {
                    $$13 = Component.translatable("createWorld.customize.flat.layer.bottom", $$10.getHeight());
                } else {
                    $$13 = Component.translatable("createWorld.customize.flat.layer", $$10.getHeight());
                }
                guiGraphics0.drawString(CreateFlatWorldScreen.this.f_96547_, $$13, int3 + 2 + 213 - CreateFlatWorldScreen.this.f_96547_.width($$13), int2 + 3, 16777215, false);
            }

            private ItemStack getDisplayItem(BlockState blockState0) {
                Item $$1 = blockState0.m_60734_().asItem();
                if ($$1 == Items.AIR) {
                    if (blockState0.m_60713_(Blocks.WATER)) {
                        $$1 = Items.WATER_BUCKET;
                    } else if (blockState0.m_60713_(Blocks.LAVA)) {
                        $$1 = Items.LAVA_BUCKET;
                    }
                }
                return new ItemStack($$1);
            }

            @Override
            public Component getNarration() {
                FlatLayerInfo $$0 = (FlatLayerInfo) CreateFlatWorldScreen.this.generator.getLayersInfo().get(CreateFlatWorldScreen.this.generator.getLayersInfo().size() - DetailsList.this.m_6702_().indexOf(this) - 1);
                ItemStack $$1 = this.getDisplayItem($$0.getBlockState());
                return (Component) (!$$1.isEmpty() ? Component.translatable("narrator.select", $$1.getHoverName()) : CommonComponents.EMPTY);
            }

            @Override
            public boolean mouseClicked(double double0, double double1, int int2) {
                if (int2 == 0) {
                    DetailsList.this.setSelected(this);
                    return true;
                } else {
                    return false;
                }
            }

            private void blitSlot(GuiGraphics guiGraphics0, int int1, int int2, ItemStack itemStack3) {
                this.blitSlotBg(guiGraphics0, int1 + 1, int2 + 1);
                if (!itemStack3.isEmpty()) {
                    guiGraphics0.renderFakeItem(itemStack3, int1 + 2, int2 + 2);
                }
            }

            private void blitSlotBg(GuiGraphics guiGraphics0, int int1, int int2) {
                guiGraphics0.blit(CreateFlatWorldScreen.DetailsList.STATS_ICON_LOCATION, int1, int2, 0, 0.0F, 0.0F, 18, 18, 128, 128);
            }
        }
    }
}