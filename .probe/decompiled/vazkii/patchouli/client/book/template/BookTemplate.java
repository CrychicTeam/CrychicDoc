package vazkii.patchouli.client.book.template;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.template.component.ComponentCustom;
import vazkii.patchouli.client.book.template.component.ComponentEntity;
import vazkii.patchouli.client.book.template.component.ComponentFrame;
import vazkii.patchouli.client.book.template.component.ComponentHeader;
import vazkii.patchouli.client.book.template.component.ComponentImage;
import vazkii.patchouli.client.book.template.component.ComponentItemStack;
import vazkii.patchouli.client.book.template.component.ComponentSeparator;
import vazkii.patchouli.client.book.template.component.ComponentText;
import vazkii.patchouli.client.book.template.component.ComponentTooltip;
import vazkii.patchouli.common.book.Book;

public class BookTemplate {

    public static final HashMap<ResourceLocation, Class<? extends TemplateComponent>> componentTypes = new HashMap();

    @SerializedName("include")
    List<TemplateInclusion> inclusions = new ArrayList();

    List<TemplateComponent> components = new ArrayList();

    @SerializedName("processor")
    String processorClass;

    transient Book book;

    @Nullable
    transient TemplateInclusion encapsulation;

    transient IComponentProcessor processor;

    transient boolean compiled = false;

    transient boolean attemptedCreatingProcessor = false;

    public static BookTemplate createTemplate(Book book, BookContentsBuilder builder, String type, @Nullable TemplateInclusion inclusion) {
        ResourceLocation key;
        if (type.contains(":")) {
            key = new ResourceLocation(type);
        } else {
            key = new ResourceLocation(book.id.getNamespace(), type);
        }
        Supplier<BookTemplate> supplier = builder.getTemplate(key);
        if (supplier == null) {
            throw new IllegalArgumentException("Template " + key + " does not exist");
        } else {
            BookTemplate template = (BookTemplate) supplier.get();
            template.book = book;
            template.encapsulation = inclusion;
            return template;
        }
    }

    public void compile(Level level, BookContentsBuilder builder, IVariableProvider variables) {
        if (!this.compiled) {
            this.createProcessor();
            this.components.removeIf(Objects::isNull);
            if (this.processor != null) {
                IVariableProvider processorVars = variables;
                if (this.encapsulation != null) {
                    processorVars = this.encapsulation.wrapProvider(variables);
                }
                try {
                    this.processor.setup(level, processorVars);
                } catch (Exception var7) {
                    throw new RuntimeException("Error setting up template processor", var7);
                }
            }
            for (TemplateInclusion include : this.inclusions) {
                if (include.template == null || include.template.isEmpty() || include.as == null || include.as.isEmpty()) {
                    throw new IllegalArgumentException("Template inclusion must define both \"template\" and \"as\" fields.");
                }
                include.upperMerge(this.encapsulation);
                include.process(level, this.processor);
                BookTemplate template = createTemplate(this.book, builder, include.template, include);
                template.compile(level, builder, variables);
                this.components.addAll(template.components);
            }
            for (TemplateComponent c : this.components) {
                c.compile(level, variables, this.processor, this.encapsulation);
            }
            this.compiled = true;
        }
    }

    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        if (this.compiled) {
            this.components.forEach(c -> c.build(builder, page, entry, pageNum));
        }
    }

    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
        if (this.compiled) {
            if (this.processor != null) {
                this.processor.refresh(parent, left, top);
            }
            this.components.forEach(c -> c.isVisible = c.getVisibleStatus(this.processor));
            this.components.forEach(c -> c.onDisplayed(page, parent, left, top));
        }
    }

    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        if (this.compiled) {
            this.components.forEach(c -> {
                if (c.isVisible) {
                    c.render(graphics, page, mouseX, mouseY, pticks);
                }
            });
        }
    }

    public boolean mouseClicked(BookPage page, double mouseX, double mouseY, int mouseButton) {
        if (this.compiled) {
            for (TemplateComponent c : this.components) {
                if (c.isVisible && c.mouseClicked(page, mouseX, mouseY, mouseButton)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void registerComponent(ResourceLocation name, Class<? extends TemplateComponent> clazz) {
        componentTypes.put(name, clazz);
    }

    private void createProcessor() {
        if (!this.attemptedCreatingProcessor) {
            if (this.processorClass != null && !this.processorClass.isEmpty()) {
                try {
                    Class<?> clazz = Class.forName(this.processorClass);
                    this.processor = (IComponentProcessor) clazz.newInstance();
                } catch (Exception var2) {
                    throw new RuntimeException("Failed to create component processor " + this.processorClass, var2);
                }
            }
            this.attemptedCreatingProcessor = true;
        }
    }

    static {
        registerComponent(new ResourceLocation("patchouli", "text"), ComponentText.class);
        registerComponent(new ResourceLocation("patchouli", "item"), ComponentItemStack.class);
        registerComponent(new ResourceLocation("patchouli", "image"), ComponentImage.class);
        registerComponent(new ResourceLocation("patchouli", "header"), ComponentHeader.class);
        registerComponent(new ResourceLocation("patchouli", "separator"), ComponentSeparator.class);
        registerComponent(new ResourceLocation("patchouli", "frame"), ComponentFrame.class);
        registerComponent(new ResourceLocation("patchouli", "entity"), ComponentEntity.class);
        registerComponent(new ResourceLocation("patchouli", "tooltip"), ComponentTooltip.class);
        registerComponent(new ResourceLocation("patchouli", "custom"), ComponentCustom.class);
    }
}