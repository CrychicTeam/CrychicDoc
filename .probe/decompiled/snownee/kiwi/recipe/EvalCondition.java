package snownee.kiwi.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import snownee.kiwi.Kiwi;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.util.KEval;

public class EvalCondition implements ICondition {

    public static final ResourceLocation ID = Kiwi.id("eval");

    private final String expression;

    public EvalCondition(String expression) {
        this.expression = expression;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        try {
            return new Expression(this.expression, KEval.config()).evaluate().getBooleanValue();
        } catch (Throwable var3) {
            throw new JsonSyntaxException(var3);
        }
    }

    public static enum Serializer implements IConditionSerializer<EvalCondition> {

        INSTANCE;

        public void write(JsonObject json, EvalCondition value) {
            json.addProperty("ex", value.expression);
        }

        public EvalCondition read(JsonObject json) {
            return new EvalCondition(GsonHelper.getAsString(json, "ex"));
        }

        @Override
        public ResourceLocation getID() {
            return EvalCondition.ID;
        }
    }
}