package team.lodestar.lodestone.systems.rendering.rendeertype;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.Util;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;

public class RenderTypeProvider {

    private final Function<RenderTypeToken, LodestoneRenderType> function;

    private final Function<RenderTypeToken, LodestoneRenderType> memorizedFunction;

    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> function) {
        this.function = function;
        this.memorizedFunction = Util.memoize(function);
    }

    public LodestoneRenderType apply(RenderTypeToken token) {
        return (LodestoneRenderType) this.function.apply(token);
    }

    public LodestoneRenderType apply(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(this.apply(token), uniformHandler);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token) {
        return (LodestoneRenderType) this.memorizedFunction.apply(token);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(this.applyAndCache(token), uniformHandler);
    }

    public LodestoneRenderType applyWithModifier(RenderTypeToken token, Consumer<LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return this.apply(token);
    }

    public LodestoneRenderType applyWithModifier(RenderTypeToken token, ShaderUniformHandler uniformHandler, Consumer<LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return this.apply(token, uniformHandler);
    }

    public LodestoneRenderType applyWithModifierAndCache(RenderTypeToken token, Consumer<LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return this.applyAndCache(token);
    }

    public LodestoneRenderType applyWithModifierAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler, Consumer<LodestoneRenderTypeRegistry.LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return LodestoneRenderTypeRegistry.applyUniformChanges(this.applyAndCache(token), uniformHandler);
    }
}