package journeymap.client.ui.component;

import java.util.ArrayList;
import journeymap.client.Constants;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.gui.Font;

public class OnOffButton extends Button {

    protected Boolean toggled = true;

    protected String labelOn;

    protected String labelOff;

    protected ArrayList<OnOffButton.ToggleListener> toggleListeners = new ArrayList(0);

    public OnOffButton(String labelOn, String labelOff, boolean toggled, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(toggled ? labelOn : labelOff, onPress);
        this.labelOn = labelOn;
        this.labelOff = labelOff;
        this.setToggled(toggled);
        this.finishInit();
    }

    public void setLabels(String labelOn, String labelOff) {
        this.labelOn = labelOn;
        this.labelOff = labelOff;
        this.updateLabel();
    }

    @Override
    protected void updateLabel() {
        if (this.labelOn != null && this.labelOff != null) {
            this.m_93666_(Constants.getStringTextComponent(this.getToggled() ? this.labelOn : this.labelOff));
        }
    }

    public void toggle() {
        this.setToggled(!this.getToggled());
    }

    @Override
    public int getFitWidth(Font fr) {
        int max = fr.width(this.m_6035_().getString());
        if (this.labelOn != null) {
            max = Math.max(max, fr.width(this.labelOn));
        }
        if (this.labelOff != null) {
            max = Math.max(max, fr.width(this.labelOff));
        }
        return max + this.WIDTH_PAD;
    }

    @Override
    public boolean isActive() {
        return this.isEnabled() && this.toggled;
    }

    public Boolean getToggled() {
        return this.toggled;
    }

    public void setToggled(Boolean toggled) {
        this.setToggled(toggled, true);
    }

    public void setToggled(Boolean toggled, boolean notifyToggleListener) {
        if (this.toggled != toggled && this.isEnabled() && this.f_93624_) {
            boolean allowChange = true;
            try {
                if (notifyToggleListener && !this.toggleListeners.isEmpty()) {
                    for (OnOffButton.ToggleListener listener : this.toggleListeners) {
                        allowChange = listener.onToggle(this, toggled);
                        if (!allowChange) {
                            break;
                        }
                    }
                }
            } catch (Throwable var6) {
                Journeymap.getLogger().error("Error trying to toggle button '" + this.m_6035_() + "': " + LogFormatter.toString(var6));
                allowChange = false;
            }
            if (allowChange) {
                this.toggled = toggled;
                this.updateLabel();
            }
        }
    }

    public void addToggleListener(OnOffButton.ToggleListener toggleListener) {
        this.toggleListeners.add(toggleListener);
    }

    public interface ToggleListener {

        boolean onToggle(OnOffButton var1, boolean var2);
    }
}