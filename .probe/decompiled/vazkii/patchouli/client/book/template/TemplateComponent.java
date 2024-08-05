package vazkii.patchouli.client.book.template;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.IVariablesAvailableCallback;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.base.PatchouliConfig;

public abstract class TemplateComponent implements IVariablesAvailableCallback {

    public String group = "";

    public int x;

    public int y;

    public String flag = "";

    public String advancement = "";

    @SerializedName("negate_advancement")
    boolean negateAdvancement = false;

    public String guard = null;

    transient boolean guardPass = false;

    transient boolean isVisible = true;

    private transient boolean compiled = false;

    public transient JsonObject sourceObject;

    public final void compile(Level level, IVariableProvider variables, IComponentProcessor processor, @Nullable TemplateInclusion encapsulation) {
        if (!this.compiled) {
            if (encapsulation != null) {
                this.x = this.x + encapsulation.x;
                this.y = this.y + encapsulation.y;
            }
            VariableAssigner.assignVariableHolders(level, this, variables, processor, encapsulation);
            this.compiled = true;
        }
    }

    public boolean getVisibleStatus(IComponentProcessor processor) {
        if (processor != null && this.group != null && !this.group.isEmpty() && !processor.allowRender(this.group)) {
            return false;
        } else if (!this.guardPass) {
            return false;
        } else if (!this.flag.isEmpty() && !PatchouliConfig.getConfigFlag(this.flag)) {
            return false;
        } else {
            return !this.advancement.isEmpty() ? ClientAdvancements.hasDone(this.advancement) != this.negateAdvancement : true;
        }
    }

    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
    }

    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
    }

    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
    }

    public boolean mouseClicked(BookPage page, double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        this.group = ((IVariable) lookup.apply(IVariable.wrap(this.group))).asString();
        this.flag = ((IVariable) lookup.apply(IVariable.wrap(this.flag))).asString();
        this.advancement = ((IVariable) lookup.apply(IVariable.wrap(this.advancement))).asString();
        this.guardPass = this.guard == null || ((IVariable) lookup.apply(IVariable.wrap(this.guard))).asBoolean();
    }
}