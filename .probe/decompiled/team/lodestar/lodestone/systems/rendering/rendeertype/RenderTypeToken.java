package team.lodestar.lodestone.systems.rendering.rendeertype;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;

public class RenderTypeToken implements Supplier<RenderStateShard.EmptyTextureStateShard> {

    private static final HashMap<ResourceLocation, RenderTypeToken> CACHED_TEXTURE_TOKENS = new HashMap();

    private static final HashMap<RenderStateShard.EmptyTextureStateShard, RenderTypeToken> CACHED_STATE_TOKENS = new HashMap();

    private final UUID identifier = UUID.randomUUID();

    private final RenderStateShard.EmptyTextureStateShard texture;

    protected RenderTypeToken(ResourceLocation texture) {
        this(new RenderStateShard.TextureStateShard(texture, false, false));
    }

    protected RenderTypeToken(RenderStateShard.EmptyTextureStateShard texture) {
        this.texture = texture;
    }

    public static RenderTypeToken createToken(ResourceLocation texture) {
        return new RenderTypeToken(texture);
    }

    public static RenderTypeToken createToken(RenderStateShard.EmptyTextureStateShard texture) {
        return new RenderTypeToken(texture);
    }

    public static RenderTypeToken createCachedToken(ResourceLocation texture) {
        return (RenderTypeToken) CACHED_TEXTURE_TOKENS.computeIfAbsent(texture, RenderTypeToken::new);
    }

    public static RenderTypeToken createCachedToken(RenderStateShard.EmptyTextureStateShard texture) {
        return (RenderTypeToken) CACHED_STATE_TOKENS.computeIfAbsent(texture, RenderTypeToken::new);
    }

    public RenderStateShard.EmptyTextureStateShard get() {
        return this.texture;
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.identifier, this.texture });
    }
}