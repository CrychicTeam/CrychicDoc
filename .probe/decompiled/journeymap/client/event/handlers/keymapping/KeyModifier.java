package journeymap.client.event.handlers.keymapping;

public enum KeyModifier {

    NONE, CONTROL, SHIFT, ALT;

    public boolean isActive(KeyConflictContext context) {
        return this.getForge().isActive(context.getForge());
    }

    public net.minecraftforge.client.settings.KeyModifier getForge() {
        return switch(this) {
            case SHIFT ->
                net.minecraftforge.client.settings.KeyModifier.SHIFT;
            case CONTROL ->
                net.minecraftforge.client.settings.KeyModifier.CONTROL;
            case ALT ->
                net.minecraftforge.client.settings.KeyModifier.ALT;
            default ->
                net.minecraftforge.client.settings.KeyModifier.NONE;
        };
    }

    public static KeyModifier fromForge(net.minecraftforge.client.settings.KeyModifier forge) {
        if (net.minecraftforge.client.settings.KeyModifier.SHIFT.equals(forge)) {
            return SHIFT;
        } else if (net.minecraftforge.client.settings.KeyModifier.CONTROL.equals(forge)) {
            return CONTROL;
        } else {
            return net.minecraftforge.client.settings.KeyModifier.ALT.equals(forge) ? ALT : NONE;
        }
    }
}