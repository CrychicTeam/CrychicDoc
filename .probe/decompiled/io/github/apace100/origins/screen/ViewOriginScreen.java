package io.github.apace100.origins.screen;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ViewOriginScreen extends OriginDisplayScreen {

    private final ArrayList<Tuple<Holder<OriginLayer>, Holder<Origin>>> originLayers;

    private int currentLayer = 0;

    private Button chooseOriginButton;

    public ViewOriginScreen() {
        super(Component.translatable("origins.screen.view_origin"), false);
        Player player = (Player) Objects.requireNonNull(Minecraft.getInstance().player);
        Map<ResourceKey<OriginLayer>, ResourceKey<Origin>> origins = (Map<ResourceKey<OriginLayer>, ResourceKey<Origin>>) IOriginContainer.get(player).map(IOriginContainer::getOrigins).orElseGet(ImmutableMap::of);
        this.originLayers = new ArrayList(origins.size());
        Registry<Origin> originsRegistry = OriginsAPI.getOriginsRegistry();
        Registry<OriginLayer> layersRegistry = OriginsAPI.getLayersRegistry();
        origins.forEach((layer, origin) -> {
            Optional<Holder.Reference<Origin>> origin1 = originsRegistry.getHolder(origin);
            Optional<Holder.Reference<OriginLayer>> layer1 = layersRegistry.getHolder(layer);
            if (!origin1.isEmpty() && ((Holder.Reference) origin1.get()).isBound()) {
                if (!layer1.isEmpty() && ((Holder.Reference) layer1.get()).isBound()) {
                    ItemStack displayItem = ((Origin) ((Holder.Reference) origin1.get()).value()).getIcon();
                    if (displayItem.getItem() == Items.PLAYER_HEAD && (!displayItem.hasTag() || !((CompoundTag) Objects.requireNonNull(displayItem.getTag())).contains("SkullOwner"))) {
                        displayItem.getOrCreateTag().putString("SkullOwner", player.getDisplayName().getString());
                    }
                    if ((!((Holder.Reference) origin1.get()).is(OriginRegisters.EMPTY.getId()) || ((OriginLayer) ((Holder.Reference) layer1.get()).value()).getOriginOptionCount(player) > 0) && !((OriginLayer) ((Holder.Reference) layer1.get()).value()).hidden()) {
                        this.originLayers.add(new Tuple<>((Holder) layer1.get(), (Holder) origin1.get()));
                    }
                }
            }
        });
        this.originLayers.sort(Comparator.comparing(x -> (OriginLayer) ((Holder) x.getA()).value()));
        if (this.originLayers.size() > 0) {
            Tuple<Holder<OriginLayer>, Holder<Origin>> current = (Tuple<Holder<OriginLayer>, Holder<Origin>>) this.originLayers.get(this.currentLayer);
            this.showOrigin(current.getB(), current.getA(), false);
        } else {
            this.showNone();
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.f_96543_ - 176) / 2;
        this.guiTop = (this.f_96544_ - 182) / 2;
        if (this.originLayers.size() > 0) {
            this.m_142416_(this.chooseOriginButton = Button.builder(Component.translatable("origins.gui.choose"), b -> Minecraft.getInstance().setScreen(new ChooseOriginScreen(Lists.newArrayList(new Holder[] { (Holder) ((Tuple) this.originLayers.get(this.currentLayer)).getA() }), 0, false))).bounds(this.guiLeft + 88 - 50, this.guiTop + 182 - 40, 100, 20).build());
            Player player = (Player) Objects.requireNonNull(Minecraft.getInstance().player);
            this.chooseOriginButton.f_93623_ = this.chooseOriginButton.f_93624_ = ((Holder) ((Tuple) this.originLayers.get(this.currentLayer)).getB()).is(OriginRegisters.EMPTY.getId()) && ((OriginLayer) ((Holder) ((Tuple) this.originLayers.get(this.currentLayer)).getA()).value()).getOriginOptionCount(player) > 0;
            if (this.originLayers.size() > 1) {
                this.m_142416_(Button.builder(Component.literal("<"), b -> {
                    this.currentLayer = (this.currentLayer - 1 + this.originLayers.size()) % this.originLayers.size();
                    this.switchLayer(player);
                }).bounds(this.guiLeft - 40, this.f_96544_ / 2 - 10, 20, 20).build());
                this.m_142416_(Button.builder(Component.literal(">"), b -> {
                    this.currentLayer = (this.currentLayer + 1) % this.originLayers.size();
                    this.switchLayer(player);
                }).bounds(this.guiLeft + 176 + 20, this.f_96544_ / 2 - 10, 20, 20).build());
            }
        }
        this.m_142416_(Button.builder(Component.translatable("origins.gui.close"), b -> Minecraft.getInstance().setScreen(null)).bounds(this.guiLeft + 88 - 50, this.guiTop + 182 + 5, 100, 20).build());
    }

    private void switchLayer(Player player) {
        Tuple<Holder<OriginLayer>, Holder<Origin>> current = (Tuple<Holder<OriginLayer>, Holder<Origin>>) this.originLayers.get(this.currentLayer);
        this.showOrigin(current.getB(), current.getA(), false);
        this.chooseOriginButton.f_93623_ = this.chooseOriginButton.f_93624_ = current.getB().is(OriginRegisters.EMPTY.getId()) && current.getA().value().getOriginOptionCount(player) > 0;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        if (this.originLayers.size() == 0) {
            graphics.drawCenteredString(this.f_96547_, Component.translatable("origins.gui.view_origin.empty").getString(), this.f_96543_ / 2, this.guiTop + 48, 16777215);
        }
    }

    @Override
    protected Component getTitleText() {
        Component titleText = ((OriginLayer) this.getCurrentLayer().get()).title().view();
        return (Component) (titleText != null ? titleText : Component.translatable("origins.gui.view_origin.title", ((OriginLayer) this.getCurrentLayer().get()).name()));
    }
}