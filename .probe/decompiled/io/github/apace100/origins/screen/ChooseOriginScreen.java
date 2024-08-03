package io.github.apace100.origins.screen;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Impact;
import io.github.apace100.origins.registry.ModItems;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.data.PartialOrigin;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.network.C2SChooseOrigin;
import io.github.edwinmindcraft.origins.common.network.C2SChooseRandomOrigin;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ChooseOriginScreen extends OriginDisplayScreen {

    private static final Comparator<Holder<Origin>> COMPARATOR = Comparator.comparingInt(a -> ((Origin) a.value()).getImpact().getImpactValue()).thenComparingInt(a -> ((Origin) a.value()).getOrder());

    private final List<Holder<OriginLayer>> layerList;

    private final int currentLayerIndex;

    private int currentOrigin = 0;

    private final List<Holder<Origin>> originSelection;

    private int maxSelection;

    private Origin randomOrigin;

    public ChooseOriginScreen(List<Holder<OriginLayer>> layerList, int currentLayerIndex, boolean showDirtBackground) {
        super(Component.translatable("origins.screen.choose_origin"), showDirtBackground);
        this.layerList = layerList;
        this.currentLayerIndex = currentLayerIndex;
        this.originSelection = new ArrayList(10);
        Player player = Minecraft.getInstance().player;
        Holder<OriginLayer> currentLayer = (Holder<OriginLayer>) layerList.get(currentLayerIndex);
        currentLayer.value().origins((Player) Objects.requireNonNull(player)).forEach(origin -> {
            if (origin.isBound() && ((Origin) origin.value()).isChoosable()) {
                ItemStack displayItem = ((Origin) origin.value()).getIcon();
                if (displayItem.getItem() == Items.PLAYER_HEAD && (!displayItem.hasTag() || !((CompoundTag) Objects.requireNonNull(displayItem.getTag())).contains("SkullOwner"))) {
                    displayItem.getOrCreateTag().putString("SkullOwner", player.getDisplayName().getString());
                }
                this.originSelection.add(origin);
            }
        });
        this.originSelection.sort(COMPARATOR);
        this.maxSelection = this.originSelection.size();
        if (currentLayer.value().allowRandom() && currentLayer.value().randomOrigins(player).size() > 0) {
            this.maxSelection++;
        }
        if (this.maxSelection == 0) {
            this.openNextLayerScreen();
        }
        Holder<Origin> newOrigin = this.getCurrentOriginInternal();
        this.showOrigin(newOrigin, (Holder<OriginLayer>) layerList.get(currentLayerIndex), newOrigin.value() == this.randomOrigin);
    }

    private void openNextLayerScreen() {
        Minecraft.getInstance().setScreen(new WaitForNextLayerScreen(this.layerList, this.currentLayerIndex, this.showDirtBackground));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.f_96543_ - 176) / 2;
        this.guiTop = (this.f_96544_ - 182) / 2;
        if (this.maxSelection > 1) {
            this.m_142416_(Button.builder(Component.literal("<"), b -> {
                this.currentOrigin = (this.currentOrigin - 1 + this.maxSelection) % this.maxSelection;
                Holder<Origin> newOrigin = this.getCurrentOriginInternal();
                this.showOrigin(newOrigin, (Holder<OriginLayer>) this.layerList.get(this.currentLayerIndex), newOrigin.value() == this.randomOrigin);
            }).bounds(this.guiLeft - 40, this.f_96544_ / 2 - 10, 20, 20).build());
            this.m_142416_(Button.builder(Component.literal(">"), b -> {
                this.currentOrigin = (this.currentOrigin + 1) % this.maxSelection;
                Holder<Origin> newOrigin = this.getCurrentOriginInternal();
                this.showOrigin(newOrigin, (Holder<OriginLayer>) this.layerList.get(this.currentLayerIndex), newOrigin.value() == this.randomOrigin);
            }).bounds(this.guiLeft + 176 + 20, this.f_96544_ / 2 - 10, 20, 20).build());
        }
        this.m_142416_(Button.builder(Component.translatable("origins.gui.select"), b -> {
            ResourceLocation layer = (ResourceLocation) ((Optional) ((Holder) this.layerList.get(this.currentLayerIndex)).unwrap().map(Optional::of, OriginsAPI.getLayersRegistry(null)::m_7854_)).map(ResourceKey::m_135782_).orElseThrow();
            if (this.currentOrigin == this.originSelection.size()) {
                OriginsCommon.CHANNEL.send(PacketDistributor.SERVER.noArg(), new C2SChooseRandomOrigin(layer));
            } else {
                Optional<ResourceKey<Origin>> key = (Optional<ResourceKey<Origin>>) this.getCurrentOrigin().unwrap().map(Optional::of, OriginsAPI.getOriginsRegistry(null)::m_7854_);
                if (key.isPresent()) {
                    OriginsCommon.CHANNEL.send(PacketDistributor.SERVER.noArg(), new C2SChooseOrigin(layer, ((ResourceKey) key.get()).location()));
                } else {
                    Origins.LOGGER.error("Unregistered origin found for layer {}: {}", layer, this.getCurrentOrigin());
                }
            }
            this.openNextLayerScreen();
        }).bounds(this.guiLeft + 88 - 50, this.guiTop + 182 + 5, 100, 20).build());
    }

    @Override
    protected Component getTitleText() {
        Component titleText = ((OriginLayer) this.getCurrentLayer().get()).title().choose();
        return (Component) (titleText != null ? titleText : Component.translatable("origins.gui.choose_origin.title", ((OriginLayer) this.getCurrentLayer().get()).name()));
    }

    private Holder<Origin> getCurrentOriginInternal() {
        if (this.currentOrigin == this.originSelection.size()) {
            if (this.randomOrigin == null) {
                this.initRandomOrigin();
            }
            return Holder.direct(this.randomOrigin);
        } else {
            return (Holder<Origin>) this.originSelection.get(this.currentOrigin);
        }
    }

    private void initRandomOrigin() {
        this.randomOrigin = PartialOrigin.builder().icon(new ItemStack(ModItems.ORB_OF_ORIGIN.get())).impact(Impact.NONE).order(Integer.MAX_VALUE).loadingOrder(Integer.MAX_VALUE).build().create(Origins.identifier("random"));
        MutableComponent text = Component.literal("");
        List<Holder<Origin>> randoms = ((OriginLayer) ((Holder) this.layerList.get(this.currentLayerIndex)).value()).randomOrigins((Player) Objects.requireNonNull(Minecraft.getInstance().player)).stream().filter(Objects::nonNull).sorted(COMPARATOR).toList();
        randoms.forEach(x -> text.append(((Origin) x.value()).getName()).append("\n"));
        this.setRandomOriginText(text);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics) {
        if (this.showDirtBackground) {
            super.m_280039_(graphics);
        } else {
            super.renderBackground(graphics);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.maxSelection == 0) {
            this.openNextLayerScreen();
        } else {
            super.render(graphics, mouseX, mouseY, delta);
        }
    }
}