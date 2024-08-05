package io.github.apace100.origins.screen;

import com.google.common.collect.ImmutableList;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class WaitForNextLayerScreen extends Screen {

    private final List<Holder<OriginLayer>> layerList;

    private final int currentLayerIndex;

    private final boolean showDirtBackground;

    private final int maxSelection;

    protected WaitForNextLayerScreen(List<Holder<OriginLayer>> layerList, int currentLayerIndex, boolean showDirtBackground) {
        super(Component.empty());
        this.layerList = ImmutableList.copyOf(layerList);
        this.currentLayerIndex = currentLayerIndex;
        this.showDirtBackground = showDirtBackground;
        Player player = Minecraft.getInstance().player;
        Holder<OriginLayer> currentLayer = (Holder<OriginLayer>) layerList.get(currentLayerIndex);
        this.maxSelection = currentLayer.value().getOriginOptionCount((Player) Objects.requireNonNull(player));
    }

    public void openSelection() {
        int index = this.currentLayerIndex + 1;
        Player player = Minecraft.getInstance().player;
        LazyOptional<IOriginContainer> iOriginContainerLazyOptional = IOriginContainer.get(player);
        if (!iOriginContainerLazyOptional.isPresent()) {
            Minecraft.getInstance().setScreen(null);
        } else {
            for (IOriginContainer component = iOriginContainerLazyOptional.orElseThrow(RuntimeException::new); index < this.layerList.size(); index++) {
                if (!component.hasOrigin((Holder<OriginLayer>) this.layerList.get(index)) && ((OriginLayer) ((Holder) this.layerList.get(index)).value()).origins((Player) Objects.requireNonNull(player)).size() > 0) {
                    Minecraft.getInstance().setScreen(new ChooseOriginScreen(this.layerList, index, this.showDirtBackground));
                    return;
                }
            }
            Minecraft.getInstance().setScreen(null);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.maxSelection == 0) {
            this.openSelection();
        } else {
            this.renderBackground(graphics);
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics) {
        if (this.showDirtBackground) {
            super.renderDirtBackground(graphics);
        } else {
            super.renderBackground(graphics);
        }
    }
}