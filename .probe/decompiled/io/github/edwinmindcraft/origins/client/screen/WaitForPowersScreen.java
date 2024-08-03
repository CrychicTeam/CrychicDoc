package io.github.edwinmindcraft.origins.client.screen;

import com.google.common.collect.Sets;
import io.github.apace100.origins.screen.OriginDisplayScreen;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.origins.api.origin.IOriginCallbackPower;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.network.C2SFinalizeNowReadyPowers;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

public class WaitForPowersScreen extends OriginDisplayScreen {

    private static final ResourceLocation WAITING_WINDOW = new ResourceLocation("origins", "textures/gui/wait_for_powers.png");

    private final Set<ResourceKey<ConfiguredPower<?, ?>>> waitingFor = Sets.newHashSet();

    private final boolean wasOrb;

    public WaitForPowersScreen(boolean showDirtBackground, Set<ResourceKey<ConfiguredPower<?, ?>>> waitingFor, boolean wasOrb) {
        super(Component.empty(), showDirtBackground);
        this.waitingFor.addAll(waitingFor);
        this.wasOrb = wasOrb;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.time += delta;
        this.m_280273_(graphics);
        graphics.blit(WAITING_WINDOW, this.guiLeft, this.guiTop, 0, 0, 176, 182);
        this.renderWaitingText(graphics);
    }

    @Override
    public void tick() {
        if (this.areAllPowersReadyToGo()) {
            OriginsCommon.CHANNEL.sendToServer(new C2SFinalizeNowReadyPowers(this.waitingFor, this.wasOrb));
            this.m_7379_();
        }
    }

    private boolean areAllPowersReadyToGo() {
        return this.waitingFor.stream().map(key -> (ConfiguredPower) ApoliAPI.getPowers().get(key)).allMatch(p -> p == null || !(p.getFactory() instanceof IOriginCallbackPower callbackPower) || callbackPower.isReady(p, this.f_96541_.player, this.wasOrb));
    }

    private void renderWaitingText(@NotNull GuiGraphics graphics) {
        int textWidth = 128;
        int dotAmount = (int) (this.time / 10.0F % 4.0F);
        MutableComponent component = Component.translatable("origins.gui.waiting_for_powers");
        for (int i = 0; i < dotAmount; i++) {
            component.append(".");
        }
        List<FormattedCharSequence> components = this.f_96547_.split(component, textWidth);
        int x = this.guiLeft + 88;
        int y = this.guiTop + 50;
        for (FormattedCharSequence c : components) {
            graphics.drawCenteredString(this.f_96547_, c, x, y, 16777215);
            y += 12;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}