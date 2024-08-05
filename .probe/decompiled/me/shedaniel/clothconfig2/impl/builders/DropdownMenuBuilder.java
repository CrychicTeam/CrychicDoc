package me.shedaniel.clothconfig2.impl.builders;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DropdownMenuBuilder<T> extends FieldBuilder<T, DropdownBoxEntry<T>, DropdownMenuBuilder<T>> {

    protected DropdownBoxEntry.SelectionTopCellElement<T> topCellElement;

    protected DropdownBoxEntry.SelectionCellCreator<T> cellCreator;

    protected Function<T, Optional<Component[]>> tooltipSupplier = str -> Optional.empty();

    protected Consumer<T> saveConsumer = null;

    protected Iterable<T> selections = Collections.emptyList();

    protected boolean suggestionMode = true;

    public DropdownMenuBuilder(Component resetButtonKey, Component fieldNameKey, DropdownBoxEntry.SelectionTopCellElement<T> topCellElement, DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        super(resetButtonKey, fieldNameKey);
        this.topCellElement = (DropdownBoxEntry.SelectionTopCellElement<T>) Objects.requireNonNull(topCellElement);
        this.cellCreator = (DropdownBoxEntry.SelectionCellCreator<T>) Objects.requireNonNull(cellCreator);
    }

    public DropdownMenuBuilder<T> setSelections(Iterable<T> selections) {
        this.selections = selections;
        return this;
    }

    public DropdownMenuBuilder<T> setDefaultValue(Supplier<T> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public DropdownMenuBuilder<T> setDefaultValue(T defaultValue) {
        this.defaultValue = () -> Objects.requireNonNull(defaultValue);
        return this;
    }

    public DropdownMenuBuilder<T> setSaveConsumer(Consumer<T> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public DropdownMenuBuilder<T> setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = str -> (Optional) tooltipSupplier.get();
        return this;
    }

    public DropdownMenuBuilder<T> setTooltipSupplier(Function<T, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    public DropdownMenuBuilder<T> setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = str -> tooltip;
        return this;
    }

    public DropdownMenuBuilder<T> setTooltip(Component... tooltip) {
        this.tooltipSupplier = str -> Optional.ofNullable(tooltip);
        return this;
    }

    public DropdownMenuBuilder<T> requireRestart() {
        this.requireRestart(true);
        return this;
    }

    public DropdownMenuBuilder<T> setErrorSupplier(Function<T, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }

    public DropdownMenuBuilder<T> setSuggestionMode(boolean suggestionMode) {
        this.suggestionMode = suggestionMode;
        return this;
    }

    public boolean isSuggestionMode() {
        return this.suggestionMode;
    }

    @NotNull
    public DropdownBoxEntry<T> build() {
        DropdownBoxEntry<T> entry = new DropdownBoxEntry<>(this.getFieldNameKey(), this.getResetButtonKey(), null, this.isRequireRestart(), this.defaultValue, this.saveConsumer, this.selections, this.topCellElement, this.cellCreator);
        entry.setTooltipSupplier(() -> (Optional) this.tooltipSupplier.apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        entry.setSuggestionMode(this.suggestionMode);
        return this.finishBuilding(entry);
    }

    public static class CellCreatorBuilder {

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> of() {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<>();
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> of(Function<T, Component> toTextFunction) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<>(toTextFunction);
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> ofWidth(int cellWidth) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>() {

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> ofWidth(int cellWidth, Function<T, Component> toTextFunction) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>(toTextFunction) {

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> ofCellCount(int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>() {

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> ofCellCount(int maxItems, Function<T, Component> toTextFunction) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>(toTextFunction) {

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> of(int cellWidth, int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>() {

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> of(int cellWidth, int maxItems, Function<T, Component> toTextFunction) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>(toTextFunction) {

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> of(int cellHeight, int cellWidth, int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>() {

                @Override
                public int getCellHeight() {
                    return cellHeight;
                }

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static <T> DropdownBoxEntry.SelectionCellCreator<T> of(int cellHeight, int cellWidth, int maxItems, Function<T, Component> toTextFunction) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<T>(toTextFunction) {

                @Override
                public int getCellHeight() {
                    return cellHeight;
                }

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static DropdownBoxEntry.SelectionCellCreator<ResourceLocation> ofItemIdentifier() {
            return ofItemIdentifier(20, 146, 7);
        }

        public static DropdownBoxEntry.SelectionCellCreator<ResourceLocation> ofItemIdentifier(int maxItems) {
            return ofItemIdentifier(20, 146, maxItems);
        }

        public static DropdownBoxEntry.SelectionCellCreator<ResourceLocation> ofItemIdentifier(int cellHeight, int cellWidth, int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<ResourceLocation>() {

                public DropdownBoxEntry.SelectionCellElement<ResourceLocation> create(ResourceLocation selection) {
                    final ItemStack s = new ItemStack(BuiltInRegistries.ITEM.get(selection));
                    return new DropdownBoxEntry.DefaultSelectionCellElement<ResourceLocation>(selection, this.toTextFunction) {

                        @Override
                        public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                            this.rendering = true;
                            this.x = x;
                            this.y = y;
                            this.width = width;
                            this.height = height;
                            boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
                            if (b) {
                                graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
                            }
                            graphics.drawString(Minecraft.getInstance().font, ((Component) this.toTextFunction.apply(this.r)).getVisualOrderText(), x + 6 + 18, y + 6, b ? 16777215 : 8947848);
                            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                            graphics.renderItem(s, x + 4, y + 2);
                        }
                    };
                }

                @Override
                public int getCellHeight() {
                    return cellHeight;
                }

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static DropdownBoxEntry.SelectionCellCreator<ResourceLocation> ofBlockIdentifier() {
            return ofBlockIdentifier(20, 146, 7);
        }

        public static DropdownBoxEntry.SelectionCellCreator<ResourceLocation> ofBlockIdentifier(int maxItems) {
            return ofBlockIdentifier(20, 146, maxItems);
        }

        public static DropdownBoxEntry.SelectionCellCreator<ResourceLocation> ofBlockIdentifier(int cellHeight, int cellWidth, int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<ResourceLocation>() {

                public DropdownBoxEntry.SelectionCellElement<ResourceLocation> create(ResourceLocation selection) {
                    final ItemStack s = new ItemStack(BuiltInRegistries.BLOCK.get(selection));
                    return new DropdownBoxEntry.DefaultSelectionCellElement<ResourceLocation>(selection, this.toTextFunction) {

                        @Override
                        public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                            this.rendering = true;
                            this.x = x;
                            this.y = y;
                            this.width = width;
                            this.height = height;
                            boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
                            if (b) {
                                graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
                            }
                            graphics.drawString(Minecraft.getInstance().font, ((Component) this.toTextFunction.apply(this.r)).getVisualOrderText(), x + 6 + 18, y + 6, b ? 16777215 : 8947848);
                            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                            graphics.renderItem(s, x + 4, y + 2);
                        }
                    };
                }

                @Override
                public int getCellHeight() {
                    return cellHeight;
                }

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static DropdownBoxEntry.SelectionCellCreator<Item> ofItemObject() {
            return ofItemObject(20, 146, 7);
        }

        public static DropdownBoxEntry.SelectionCellCreator<Item> ofItemObject(int maxItems) {
            return ofItemObject(20, 146, maxItems);
        }

        public static DropdownBoxEntry.SelectionCellCreator<Item> ofItemObject(int cellHeight, int cellWidth, int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<Item>(i -> Component.literal(BuiltInRegistries.ITEM.getKey(i).toString())) {

                public DropdownBoxEntry.SelectionCellElement<Item> create(Item selection) {
                    final ItemStack s = new ItemStack(selection);
                    return new DropdownBoxEntry.DefaultSelectionCellElement<Item>(selection, this.toTextFunction) {

                        @Override
                        public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                            this.rendering = true;
                            this.x = x;
                            this.y = y;
                            this.width = width;
                            this.height = height;
                            boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
                            if (b) {
                                graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
                            }
                            graphics.drawString(Minecraft.getInstance().font, ((Component) this.toTextFunction.apply(this.r)).getVisualOrderText(), x + 6 + 18, y + 6, b ? 16777215 : 8947848);
                            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                            graphics.renderItem(s, x + 4, y + 2);
                        }
                    };
                }

                @Override
                public int getCellHeight() {
                    return cellHeight;
                }

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }

        public static DropdownBoxEntry.SelectionCellCreator<Block> ofBlockObject() {
            return ofBlockObject(20, 146, 7);
        }

        public static DropdownBoxEntry.SelectionCellCreator<Block> ofBlockObject(int maxItems) {
            return ofBlockObject(20, 146, maxItems);
        }

        public static DropdownBoxEntry.SelectionCellCreator<Block> ofBlockObject(int cellHeight, int cellWidth, int maxItems) {
            return new DropdownBoxEntry.DefaultSelectionCellCreator<Block>(i -> Component.literal(BuiltInRegistries.BLOCK.getKey(i).toString())) {

                public DropdownBoxEntry.SelectionCellElement<Block> create(Block selection) {
                    final ItemStack s = new ItemStack(selection);
                    return new DropdownBoxEntry.DefaultSelectionCellElement<Block>(selection, this.toTextFunction) {

                        @Override
                        public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                            this.rendering = true;
                            this.x = x;
                            this.y = y;
                            this.width = width;
                            this.height = height;
                            boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
                            if (b) {
                                graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
                            }
                            graphics.drawString(Minecraft.getInstance().font, ((Component) this.toTextFunction.apply(this.r)).getVisualOrderText(), x + 6 + 18, y + 6, b ? 16777215 : 8947848);
                            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                            graphics.renderItem(s, x + 4, y + 2);
                        }
                    };
                }

                @Override
                public int getCellHeight() {
                    return cellHeight;
                }

                @Override
                public int getCellWidth() {
                    return cellWidth;
                }

                @Override
                public int getDropBoxMaxHeight() {
                    return this.getCellHeight() * maxItems;
                }
            };
        }
    }

    public static class TopCellElementBuilder {

        public static final Function<String, ResourceLocation> IDENTIFIER_FUNCTION = str -> {
            try {
                return new ResourceLocation(str);
            } catch (NumberFormatException var2) {
                return null;
            }
        };

        public static final Function<String, ResourceLocation> ITEM_IDENTIFIER_FUNCTION = str -> {
            try {
                ResourceLocation identifier = new ResourceLocation(str);
                if (BuiltInRegistries.ITEM.m_6612_(identifier).isPresent()) {
                    return identifier;
                }
            } catch (Exception var2) {
            }
            return null;
        };

        public static final Function<String, ResourceLocation> BLOCK_IDENTIFIER_FUNCTION = str -> {
            try {
                ResourceLocation identifier = new ResourceLocation(str);
                if (BuiltInRegistries.BLOCK.m_6612_(identifier).isPresent()) {
                    return identifier;
                }
            } catch (Exception var2) {
            }
            return null;
        };

        public static final Function<String, Item> ITEM_FUNCTION = str -> {
            try {
                return (Item) BuiltInRegistries.ITEM.m_6612_(new ResourceLocation(str)).orElse(null);
            } catch (Exception var2) {
                return null;
            }
        };

        public static final Function<String, Block> BLOCK_FUNCTION = str -> {
            try {
                return (Block) BuiltInRegistries.BLOCK.m_6612_(new ResourceLocation(str)).orElse(null);
            } catch (Exception var2) {
                return null;
            }
        };

        private static final ItemStack BARRIER = new ItemStack(Items.BARRIER);

        public static <T> DropdownBoxEntry.SelectionTopCellElement<T> of(T value, Function<String, T> toObjectFunction) {
            return of(value, toObjectFunction, t -> Component.literal(t.toString()));
        }

        public static <T> DropdownBoxEntry.SelectionTopCellElement<T> of(T value, Function<String, T> toObjectFunction, Function<T, Component> toTextFunction) {
            return new DropdownBoxEntry.DefaultSelectionTopCellElement<>(value, toObjectFunction, toTextFunction);
        }

        public static DropdownBoxEntry.SelectionTopCellElement<ResourceLocation> ofItemIdentifier(Item item) {
            return new DropdownBoxEntry.DefaultSelectionTopCellElement<ResourceLocation>(BuiltInRegistries.ITEM.getKey(item), ITEM_IDENTIFIER_FUNCTION, identifier -> Component.literal(identifier.toString())) {

                @Override
                public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                    this.textFieldWidget.m_252865_(x + 4);
                    this.textFieldWidget.m_253211_(y + 6);
                    this.textFieldWidget.m_93674_(width - 4 - 20);
                    this.textFieldWidget.setEditable(this.getParent().isEditable());
                    this.textFieldWidget.setTextColor(this.getPreferredTextColor());
                    this.textFieldWidget.m_88315_(graphics, mouseX, mouseY, delta);
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    ItemStack stack = this.hasConfigError() ? DropdownMenuBuilder.TopCellElementBuilder.BARRIER : new ItemStack(BuiltInRegistries.ITEM.get(this.getValue()));
                    graphics.renderItem(stack, x + width - 18, y + 2);
                }
            };
        }

        public static DropdownBoxEntry.SelectionTopCellElement<ResourceLocation> ofBlockIdentifier(Block block) {
            return new DropdownBoxEntry.DefaultSelectionTopCellElement<ResourceLocation>(BuiltInRegistries.BLOCK.getKey(block), BLOCK_IDENTIFIER_FUNCTION, identifier -> Component.literal(identifier.toString())) {

                @Override
                public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                    this.textFieldWidget.m_252865_(x + 4);
                    this.textFieldWidget.m_253211_(y + 6);
                    this.textFieldWidget.m_93674_(width - 4 - 20);
                    this.textFieldWidget.setEditable(this.getParent().isEditable());
                    this.textFieldWidget.setTextColor(this.getPreferredTextColor());
                    this.textFieldWidget.m_88315_(graphics, mouseX, mouseY, delta);
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    ItemStack stack = this.hasConfigError() ? DropdownMenuBuilder.TopCellElementBuilder.BARRIER : new ItemStack(BuiltInRegistries.BLOCK.get(this.getValue()));
                    graphics.renderItem(stack, x + width - 18, y + 2);
                }
            };
        }

        public static DropdownBoxEntry.SelectionTopCellElement<Item> ofItemObject(Item item) {
            return new DropdownBoxEntry.DefaultSelectionTopCellElement<Item>(item, ITEM_FUNCTION, i -> Component.literal(BuiltInRegistries.ITEM.getKey(i).toString())) {

                @Override
                public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                    this.textFieldWidget.m_252865_(x + 4);
                    this.textFieldWidget.m_253211_(y + 6);
                    this.textFieldWidget.m_93674_(width - 4 - 20);
                    this.textFieldWidget.setEditable(this.getParent().isEditable());
                    this.textFieldWidget.setTextColor(this.getPreferredTextColor());
                    this.textFieldWidget.m_88315_(graphics, mouseX, mouseY, delta);
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    ItemStack stack = this.hasConfigError() ? DropdownMenuBuilder.TopCellElementBuilder.BARRIER : new ItemStack(this.getValue());
                    graphics.renderItem(stack, x + width - 18, y + 2);
                }
            };
        }

        public static DropdownBoxEntry.SelectionTopCellElement<Block> ofBlockObject(Block block) {
            return new DropdownBoxEntry.DefaultSelectionTopCellElement<Block>(block, BLOCK_FUNCTION, i -> Component.literal(BuiltInRegistries.BLOCK.getKey(i).toString())) {

                @Override
                public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                    this.textFieldWidget.m_252865_(x + 4);
                    this.textFieldWidget.m_253211_(y + 6);
                    this.textFieldWidget.m_93674_(width - 4 - 20);
                    this.textFieldWidget.setEditable(this.getParent().isEditable());
                    this.textFieldWidget.setTextColor(this.getPreferredTextColor());
                    this.textFieldWidget.m_88315_(graphics, mouseX, mouseY, delta);
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    ItemStack stack = this.hasConfigError() ? DropdownMenuBuilder.TopCellElementBuilder.BARRIER : new ItemStack(this.getValue());
                    graphics.renderItem(stack, x + width - 18, y + 2);
                }
            };
        }
    }
}