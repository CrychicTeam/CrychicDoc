package net.minecraft.client.gui.navigation;

public interface FocusNavigationEvent {

    ScreenDirection getVerticalDirectionForInitialFocus();

    public static record ArrowNavigation(ScreenDirection f_263812_) implements FocusNavigationEvent {

        private final ScreenDirection direction;

        public ArrowNavigation(ScreenDirection f_263812_) {
            this.direction = f_263812_;
        }

        @Override
        public ScreenDirection getVerticalDirectionForInitialFocus() {
            return this.direction.getAxis() == ScreenAxis.VERTICAL ? this.direction : ScreenDirection.DOWN;
        }
    }

    public static class InitialFocus implements FocusNavigationEvent {

        @Override
        public ScreenDirection getVerticalDirectionForInitialFocus() {
            return ScreenDirection.DOWN;
        }
    }

    public static record TabNavigation(boolean f_263782_) implements FocusNavigationEvent {

        private final boolean forward;

        public TabNavigation(boolean f_263782_) {
            this.forward = f_263782_;
        }

        @Override
        public ScreenDirection getVerticalDirectionForInitialFocus() {
            return this.forward ? ScreenDirection.DOWN : ScreenDirection.UP;
        }
    }
}