package net.minecraft.client.gui.screens;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import org.apache.commons.compress.utils.Lists;

public class OnlineOptionsScreen extends SimpleOptionsSubScreen {

    @Nullable
    private final OptionInstance<Unit> difficultyDisplay;

    public static OnlineOptionsScreen createOnlineOptionsScreen(Minecraft minecraft0, Screen screen1, Options options2) {
        List<OptionInstance<?>> $$3 = Lists.newArrayList();
        $$3.add(options2.realmsNotifications());
        $$3.add(options2.allowServerListing());
        OptionInstance<Unit> $$4 = Optionull.map(minecraft0.level, p_288244_ -> {
            Difficulty $$1 = p_288244_.m_46791_();
            return new OptionInstance<>("options.difficulty.online", OptionInstance.noTooltip(), (p_261484_, p_262113_) -> $$1.getDisplayName(), new OptionInstance.Enum<>(List.of(Unit.INSTANCE), Codec.EMPTY.codec()), Unit.INSTANCE, p_261717_ -> {
            });
        });
        if ($$4 != null) {
            $$3.add($$4);
        }
        return new OnlineOptionsScreen(screen1, options2, (OptionInstance<?>[]) $$3.toArray(new OptionInstance[0]), $$4);
    }

    private OnlineOptionsScreen(Screen screen0, Options options1, OptionInstance<?>[] optionInstance2, @Nullable OptionInstance<Unit> optionInstanceUnit3) {
        super(screen0, options1, Component.translatable("options.online.title"), optionInstance2);
        this.difficultyDisplay = optionInstanceUnit3;
    }

    @Override
    protected void init() {
        super.init();
        if (this.difficultyDisplay != null) {
            AbstractWidget $$0 = this.f_96668_.findOption(this.difficultyDisplay);
            if ($$0 != null) {
                $$0.active = false;
            }
        }
        AbstractWidget $$1 = this.f_96668_.findOption(this.f_96282_.telemetryOptInExtra());
        if ($$1 != null) {
            $$1.active = this.f_96541_.extraTelemetryAvailable();
        }
    }
}