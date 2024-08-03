package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.init.data.LangData;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record RankToken(int rank) implements IArtifactFeature.Sprite {

    public static final List<RankToken> ALL_RANKS = Stream.of(1, 2, 3, 4, 5).map(RankToken::new).toList();

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation("l2artifacts", "textures/rank/" + this.rank + ".png");
    }

    @Override
    public MutableComponent getDesc() {
        return LangData.getTranslate("tooltip.rank." + this.rank);
    }
}