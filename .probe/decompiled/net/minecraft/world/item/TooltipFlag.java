package net.minecraft.world.item;

public interface TooltipFlag {

    TooltipFlag.Default NORMAL = new TooltipFlag.Default(false, false);

    TooltipFlag.Default ADVANCED = new TooltipFlag.Default(true, false);

    boolean isAdvanced();

    boolean isCreative();

    public static record Default(boolean f_43368_, boolean f_257043_) implements TooltipFlag {

        private final boolean advanced;

        private final boolean creative;

        public Default(boolean f_43368_, boolean f_257043_) {
            this.advanced = f_43368_;
            this.creative = f_257043_;
        }

        @Override
        public boolean isAdvanced() {
            return this.advanced;
        }

        @Override
        public boolean isCreative() {
            return this.creative;
        }

        public TooltipFlag.Default asCreative() {
            return new TooltipFlag.Default(this.advanced, true);
        }
    }
}