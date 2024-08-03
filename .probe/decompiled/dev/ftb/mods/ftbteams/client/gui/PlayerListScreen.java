package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftblibrary.icon.FaceIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;

public class PlayerListScreen extends BaseScreen {

    private final Component title;

    private final List<GameProfile> profiles;

    private final Set<GameProfile> selected;

    private final Consumer<GameProfile> callback;

    public PlayerListScreen(Component t, List<GameProfile> p, Consumer<GameProfile> c) {
        this.title = t;
        this.profiles = p;
        this.selected = new HashSet();
        this.callback = c;
    }

    @Override
    public void addWidgets() {
        this.add((new Panel(this) {

            @Override
            public void addWidgets() {
                for (final GameProfile profile : PlayerListScreen.this.profiles) {
                    this.add(new NordButton(this, Component.literal(profile.getName()), FaceIcon.getFace(profile)) {

                        @Override
                        public void onClicked(MouseButton mouseButton) {
                            if (PlayerListScreen.this.selected.contains(profile)) {
                                PlayerListScreen.this.selected.remove(profile);
                            } else {
                                PlayerListScreen.this.selected.add(profile);
                            }
                            refreshWidgets();
                        }
                    });
                }
            }

            @Override
            public void alignWidgets() {
                this.align(new WidgetLayout.Vertical(1, 2, 1));
            }
        }).setPosAndSize(4, 12, this.width - 8, this.height - 16));
    }
}