package net.minecraft.client.gui.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class LanguageSelectScreen extends OptionsSubScreen {

    private static final Component WARNING_LABEL = Component.literal("(").append(Component.translatable("options.languageWarning")).append(")").withStyle(ChatFormatting.GRAY);

    private LanguageSelectScreen.LanguageSelectionList packSelectionList;

    final LanguageManager languageManager;

    public LanguageSelectScreen(Screen screen0, Options options1, LanguageManager languageManager2) {
        super(screen0, options1, Component.translatable("options.language"));
        this.languageManager = languageManager2;
    }

    @Override
    protected void init() {
        this.packSelectionList = new LanguageSelectScreen.LanguageSelectionList(this.f_96541_);
        this.m_7787_(this.packSelectionList);
        this.m_142416_(this.f_96282_.forceUnicodeFont().createButton(this.f_96282_, this.f_96543_ / 2 - 155, this.f_96544_ - 38, 150));
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_288243_ -> this.onDone()).bounds(this.f_96543_ / 2 - 155 + 160, this.f_96544_ - 38, 150, 20).build());
        super.m_7856_();
    }

    void onDone() {
        LanguageSelectScreen.LanguageSelectionList.Entry $$0 = (LanguageSelectScreen.LanguageSelectionList.Entry) this.packSelectionList.m_93511_();
        if ($$0 != null && !$$0.code.equals(this.languageManager.getSelected())) {
            this.languageManager.setSelected($$0.code);
            this.f_96282_.languageCode = $$0.code;
            this.f_96541_.reloadResourcePacks();
            this.f_96282_.save();
        }
        this.f_96541_.setScreen(this.f_96281_);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (CommonInputs.selected(int0)) {
            LanguageSelectScreen.LanguageSelectionList.Entry $$3 = (LanguageSelectScreen.LanguageSelectionList.Entry) this.packSelectionList.m_93511_();
            if ($$3 != null) {
                $$3.select();
                this.onDone();
                return true;
            }
        }
        return super.m_7933_(int0, int1, int2);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.packSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 16, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, WARNING_LABEL, this.f_96543_ / 2, this.f_96544_ - 56, 8421504);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    class LanguageSelectionList extends ObjectSelectionList<LanguageSelectScreen.LanguageSelectionList.Entry> {

        public LanguageSelectionList(Minecraft minecraft0) {
            super(minecraft0, LanguageSelectScreen.this.f_96543_, LanguageSelectScreen.this.f_96544_, 32, LanguageSelectScreen.this.f_96544_ - 65 + 4, 18);
            String $$1 = LanguageSelectScreen.this.languageManager.getSelected();
            LanguageSelectScreen.this.languageManager.getLanguages().forEach((p_265492_, p_265377_) -> {
                LanguageSelectScreen.LanguageSelectionList.Entry $$3 = new LanguageSelectScreen.LanguageSelectionList.Entry(p_265492_, p_265377_);
                this.m_7085_($$3);
                if ($$1.equals(p_265492_)) {
                    this.m_6987_($$3);
                }
            });
            if (this.m_93511_() != null) {
                this.m_93494_((LanguageSelectScreen.LanguageSelectionList.Entry) this.m_93511_());
            }
        }

        @Override
        protected int getScrollbarPosition() {
            return super.m_5756_() + 20;
        }

        @Override
        public int getRowWidth() {
            return super.m_5759_() + 50;
        }

        @Override
        protected void renderBackground(GuiGraphics guiGraphics0) {
            LanguageSelectScreen.this.m_280273_(guiGraphics0);
        }

        public class Entry extends ObjectSelectionList.Entry<LanguageSelectScreen.LanguageSelectionList.Entry> {

            final String code;

            private final Component language;

            private long lastClickTime;

            public Entry(String string0, LanguageInfo languageInfo1) {
                this.code = string0;
                this.language = languageInfo1.toComponent();
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                guiGraphics0.drawCenteredString(LanguageSelectScreen.this.f_96547_, this.language, LanguageSelectionList.this.f_93388_ / 2, int2 + 1, 16777215);
            }

            @Override
            public boolean mouseClicked(double double0, double double1, int int2) {
                if (int2 == 0) {
                    this.select();
                    if (Util.getMillis() - this.lastClickTime < 250L) {
                        LanguageSelectScreen.this.onDone();
                    }
                    this.lastClickTime = Util.getMillis();
                    return true;
                } else {
                    this.lastClickTime = Util.getMillis();
                    return false;
                }
            }

            void select() {
                LanguageSelectionList.this.m_6987_(this);
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.language);
            }
        }
    }
}