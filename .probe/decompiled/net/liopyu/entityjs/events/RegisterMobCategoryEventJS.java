package net.liopyu.entityjs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "entityjs", bus = Bus.FORGE)
public class RegisterMobCategoryEventJS extends EventJS {

    private RegisterMobCategoryEventJS.RegisterMobCategoryEvent event;

    public RegisterMobCategoryEventJS(RegisterMobCategoryEventJS.RegisterMobCategoryEvent event) {
        this.event = event;
    }

    @SubscribeEvent
    public void registerMobCategories(RegisterMobCategoryEventJS.RegisterMobCategoryEvent event) {
        this.event = event;
    }

    public static class MobCategoryRegistrationHelper {

        private final String name;

        private final String displayName;

        private final int max;

        private final boolean isFriendly;

        private final boolean isPersistent;

        private final int despawnDistance;

        public MobCategoryRegistrationHelper(String name, String displayName, int max, boolean isFriendly, boolean isPersistent, int despawnDistance) {
            this.name = name;
            this.displayName = displayName;
            this.max = max;
            this.isFriendly = isFriendly;
            this.isPersistent = isPersistent;
            this.despawnDistance = despawnDistance;
        }

        public String getName() {
            return this.name;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public int getMax() {
            return this.max;
        }

        public boolean isFriendly() {
            return this.isFriendly;
        }

        public boolean isPersistent() {
            return this.isPersistent;
        }

        public int getDespawnDistance() {
            return this.despawnDistance;
        }
    }

    public static class RegisterMobCategoryEvent extends Event {

        private final Consumer<RegisterMobCategoryEventJS.MobCategoryRegistrationHelper> registrationHelperConsumer;

        public RegisterMobCategoryEvent(Consumer<RegisterMobCategoryEventJS.MobCategoryRegistrationHelper> registrationHelperConsumer) {
            this.registrationHelperConsumer = registrationHelperConsumer;
        }

        public void registerCategories(String name, String displayName, int max, boolean isFriendly, boolean isPersistent, int despawnDistance) {
            this.registrationHelperConsumer.accept(new RegisterMobCategoryEventJS.MobCategoryRegistrationHelper(name, displayName, max, isFriendly, isPersistent, despawnDistance));
        }
    }
}