package mezz.jei.forge.startup;

import java.util.HashSet;
import java.util.Set;
import mezz.jei.forge.events.PermanentEventSubscriptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartEventObserver implements ResourceManagerReloadListener {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Set<Class<? extends Event>> requiredEvents = Set.of(TagsUpdatedEvent.class, RecipesUpdatedEvent.class);

    private final Set<Class<? extends Event>> observedEvents = new HashSet();

    private final Runnable startRunnable;

    private final Runnable stopRunnable;

    private StartEventObserver.State state = StartEventObserver.State.DISABLED;

    public StartEventObserver(Runnable startRunnable, Runnable stopRunnable) {
        this.startRunnable = startRunnable;
        this.stopRunnable = stopRunnable;
    }

    public void register(PermanentEventSubscriptions subscriptions) {
        requiredEvents.forEach(eventClass -> subscriptions.register(eventClass, this::onEvent));
        subscriptions.register(ClientPlayerNetworkEvent.LoggingIn.class, event -> {
            if (event.getPlayer() != null) {
                LOGGER.info("JEI StartEventObserver received {}", event.getClass());
                if (this.state == StartEventObserver.State.DISABLED) {
                    this.transitionState(StartEventObserver.State.ENABLED);
                }
            }
        });
        subscriptions.register(ClientPlayerNetworkEvent.LoggingOut.class, event -> {
            if (event.getPlayer() != null) {
                LOGGER.info("JEI StartEventObserver received {}", event.getClass());
                this.transitionState(StartEventObserver.State.DISABLED);
            }
        });
        subscriptions.register(ScreenEvent.Init.Pre.class, event -> {
            if (this.state != StartEventObserver.State.JEI_STARTED) {
                Screen screen = event.getScreen();
                Minecraft minecraft = screen.getMinecraft();
                if (screen instanceof AbstractContainerScreen && minecraft != null && minecraft.player != null) {
                    LOGGER.error("A Screen is opening but JEI hasn't started yet.\nNormally, JEI is started after ClientPlayerNetworkEvent.LoggedInEvent, TagsUpdatedEvent, and RecipesUpdatedEvent.\nSomething has caused one or more of these events to fail, so JEI is starting very late.");
                    this.transitionState(StartEventObserver.State.DISABLED);
                    this.transitionState(StartEventObserver.State.ENABLED);
                    this.transitionState(StartEventObserver.State.JEI_STARTED);
                }
            }
        });
    }

    private <T extends Event> void onEvent(T event) {
        if (this.state != StartEventObserver.State.DISABLED) {
            LOGGER.info("JEI StartEventObserver received {}", event.getClass());
            Class<? extends Event> eventClass = event.getClass();
            if (requiredEvents.contains(eventClass) && this.observedEvents.add(eventClass) && this.observedEvents.containsAll(requiredEvents)) {
                if (this.state == StartEventObserver.State.JEI_STARTED) {
                    this.restart();
                } else {
                    this.transitionState(StartEventObserver.State.JEI_STARTED);
                }
            }
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.restart();
    }

    private void restart() {
        if (this.state == StartEventObserver.State.JEI_STARTED) {
            this.transitionState(StartEventObserver.State.DISABLED);
            this.transitionState(StartEventObserver.State.ENABLED);
            this.transitionState(StartEventObserver.State.JEI_STARTED);
        }
    }

    private void transitionState(StartEventObserver.State newState) {
        LOGGER.info("JEI StartEventObserver transitioning state from " + this.state + " to " + newState);
        switch(newState) {
            case DISABLED:
                if (this.state == StartEventObserver.State.JEI_STARTED) {
                    this.stopRunnable.run();
                }
                break;
            case ENABLED:
                if (this.state != StartEventObserver.State.DISABLED) {
                    throw new IllegalStateException("Attempted Illegal state transition from " + this.state + " to " + newState);
                }
                break;
            case JEI_STARTED:
                if (this.state != StartEventObserver.State.ENABLED) {
                    throw new IllegalStateException("Attempted Illegal state transition from " + this.state + " to " + newState);
                }
                this.startRunnable.run();
        }
        this.state = newState;
        this.observedEvents.clear();
    }

    private static enum State {

        DISABLED, ENABLED, JEI_STARTED
    }
}