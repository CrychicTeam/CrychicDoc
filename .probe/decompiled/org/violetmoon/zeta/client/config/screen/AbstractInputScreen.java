package org.violetmoon.zeta.client.config.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.widget.DefaultDiscardDone;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.ValueDefinition;

public abstract class AbstractInputScreen<T> extends ZetaScreen {

    protected final ChangeSet changes;

    protected final ValueDefinition<T> def;

    protected final ClientDefinitionExt<ValueDefinition<T>> ext;

    protected DefaultDiscardDone defaultDiscardDone;

    public AbstractInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<T> def) {
        super(zc, parent);
        this.changes = changes;
        this.def = def;
        this.ext = zc.clientConfigManager.getExt(def);
    }

    protected T get() {
        return this.changes.get(this.def);
    }

    protected void set(T thing) {
        this.changes.set(this.def, thing);
    }

    protected abstract void forceUpdateWidgetsTo(T var1);

    @Override
    protected void init() {
        super.m_7856_();
        this.defaultDiscardDone = new DefaultDiscardDone(this, this.changes, this.def) {

            @Override
            public void resetToDefault(Button b) {
                super.resetToDefault(b);
                AbstractInputScreen.this.forceUpdateWidgetsTo(AbstractInputScreen.this.get());
            }

            @Override
            public void discard(Button b) {
                super.discard(b);
                AbstractInputScreen.this.forceUpdateWidgetsTo(AbstractInputScreen.this.get());
            }
        };
        this.defaultDiscardDone.addWidgets(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
        this.updateButtonStatus(true);
    }

    protected void updateButtonStatus(boolean valid) {
        this.defaultDiscardDone.done.f_93623_ = valid;
        this.defaultDiscardDone.discard.f_93623_ = !valid || this.changes.isDirty(this.def);
    }
}