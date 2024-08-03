package org.violetmoon.zeta.client.config.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.widget.DefaultDiscardDone;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.SectionDefinition;

public abstract class AbstractSectionInputScreen extends ZetaScreen {

    protected final ChangeSet changes;

    protected final SectionDefinition def;

    protected final ClientDefinitionExt<SectionDefinition> ext;

    protected DefaultDiscardDone defaultDiscardDone;

    public AbstractSectionInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def) {
        super(zc, parent);
        this.changes = changes;
        this.def = def;
        this.ext = zc.clientConfigManager.getExt(def);
    }

    protected abstract void forceUpdateWidgets();

    @Override
    protected void init() {
        super.m_7856_();
        this.defaultDiscardDone = new DefaultDiscardDone(this, this.changes, this.def) {

            @Override
            public void resetToDefault(Button b) {
                super.resetToDefault(b);
                AbstractSectionInputScreen.this.forceUpdateWidgets();
            }

            @Override
            public void discard(Button b) {
                super.discard(b);
                AbstractSectionInputScreen.this.forceUpdateWidgets();
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