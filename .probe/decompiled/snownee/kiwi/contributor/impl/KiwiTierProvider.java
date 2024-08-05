package snownee.kiwi.contributor.impl;

import com.google.common.collect.Sets;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.contributor.client.CosmeticLayer;
import snownee.kiwi.contributor.impl.client.layer.FoxTailLayer;
import snownee.kiwi.contributor.impl.client.layer.PlanetLayer;
import snownee.kiwi.contributor.impl.client.layer.SantaHatLayer;
import snownee.kiwi.contributor.impl.client.layer.SunnyMilkLayer;

public class KiwiTierProvider extends JsonTierProvider {

    private final List<String> renderableTiers = List.of("2020q3", "2020q4", "sunny_milk", "xmas");

    public KiwiTierProvider() {
        super("Snownee", KiwiTierProvider::getURLs);
    }

    private static List<String> getURLs() {
        String cdn = "https://cdn.jsdelivr.net/gh/Snownee/Kiwi@master/contributors.json";
        String github = "https://raw.githubusercontent.com/Snownee/Kiwi/master/contributors.json";
        return List.of(cdn, github);
    }

    private static boolean isInXmas() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(2) == 11 && calendar.get(5) >= 15;
    }

    @Override
    public Set<String> getPlayerTiers(String playerName) {
        Set<String> ret = super.getPlayerTiers(playerName);
        if (isInXmas()) {
            ret = Sets.newHashSet(ret);
            ret.add("xmas");
        }
        return ret;
    }

    @Override
    public Set<String> getTiers() {
        return Set.copyOf(this.getRenderableTiers());
    }

    @Override
    public List<String> getRenderableTiers() {
        return this.renderableTiers;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public CosmeticLayer createRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRenderer, String tier) {
        return (CosmeticLayer) (switch(tier) {
            case "2020q3" ->
                new PlanetLayer(entityRenderer);
            case "2020q4" ->
                new FoxTailLayer(entityRenderer);
            case "xmas" ->
                new SantaHatLayer(entityRenderer);
            case "sunny_milk" ->
                new SunnyMilkLayer(entityRenderer);
            default ->
                null;
        });
    }
}