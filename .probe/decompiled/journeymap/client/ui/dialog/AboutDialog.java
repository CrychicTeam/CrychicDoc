package journeymap.client.ui.dialog;

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.client.model.SplashInfo;
import journeymap.client.model.SplashPerson;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.JmUI;
import journeymap.common.Journeymap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class AboutDialog extends JmUI {

    protected Texture discordLogo = TextureCache.getTexture(TextureCache.Discord);

    Button buttonClose;

    Button buttonOptions;

    Button buttonDiscord;

    Button buttonWebsite;

    Button buttonDownload;

    ButtonList peopleButtons;

    ButtonList devButtons;

    ButtonList logoButtons;

    ButtonList linkButtons;

    ButtonList bottomButtons;

    ButtonList infoButtons;

    private long lastPeopleMove;

    private List<SplashPerson> people = Arrays.asList();

    private List<SplashPerson> devs = Arrays.asList(new SplashPerson("79f597fe-2877-4ecb-acdf-8c58cc1854ca", "mysticdrew", "jm.common.splash_developer"), new SplashPerson("a2039b6c-5a3d-407d-b49c-091405062b85", "techbrew", "jm.common.splash_developer"), new SplashPerson("ca5c3fc3-4d99-4f2e-a0d0-9565fe783e1f", "gdude2002", "jm.common.splash_developer"), new SplashPerson("16fd5b0e-a7a7-42a6-b203-52de6493c2b4", "Sandriell", "jm.common.splash_artist"));

    private SplashInfo info;

    public AboutDialog(JmUI returnDisplay) {
        super(Constants.getString("jm.common.splash_title", Journeymap.JM_VERSION), returnDisplay);
    }

    @Override
    public void init() {
        JourneymapClient.getInstance().getCoreProperties().splashViewed.set(Journeymap.JM_VERSION.toString()).save();
        super.setRenderBottomBar(true);
        if (this.info == null) {
            this.info = FileHandler.getMessageModel(SplashInfo.class, "splash");
            if (this.info == null) {
                this.info = new SplashInfo();
            }
            String bday = Constants.birthdayMessage();
            if (bday != null) {
                this.info.lines.add(0, new SplashInfo.Line(bday, "dialog.FullscreenActions#tweet#" + bday));
                this.devs = new ArrayList(this.devs);
                this.devs.add(new SplashPerson.Fake("", "", TextureCache.getTexture(TextureCache.ColorPicker2)));
            }
        } else {
            this.getRenderables().clear();
            Font fr = this.getFontRenderer();
            this.devButtons = new ButtonList();
            for (SplashPerson dev : this.devs) {
                Button button = new Button(dev.name);
                this.devButtons.add(button);
                dev.setButton(button);
            }
            this.devButtons.setWidths(20);
            this.devButtons.setHeights(20);
            this.devButtons.layoutDistributedHorizontal(0, 35, this.f_96543_, true);
            this.peopleButtons = new ButtonList();
            for (SplashPerson peep : this.people) {
                Button button = new Button(peep.name);
                this.peopleButtons.add(button);
                peep.setButton(button);
            }
            this.peopleButtons.setWidths(20);
            this.peopleButtons.setHeights(20);
            this.peopleButtons.layoutDistributedHorizontal(0, this.f_96544_ - 65, this.f_96543_, true);
            this.infoButtons = new ButtonList();
            for (SplashInfo.Line line : this.info.lines) {
                AboutDialog.SplashInfoButton button = new AboutDialog.SplashInfoButton(line, b -> line.invokeAction(this));
                button.setDrawBackground(false);
                button.setDefaultStyle(false);
                button.setDrawFrame(false);
                button.setHeight(9 + 5);
                if (line.hasAction()) {
                    button.setTooltip(new String[] { Constants.getString("jm.common.splash_action") });
                }
                this.infoButtons.add(button);
            }
            this.infoButtons.equalizeWidths(fr);
            this.getRenderables().addAll(this.infoButtons);
            this.buttonClose = (Button) this.m_142416_(new Button(Constants.getString("jm.common.close"), buttonx -> this.closeAndReturn()));
            this.buttonOptions = (Button) this.m_142416_(new Button(Constants.getString("jm.common.options_button"), buttonx -> {
                if (returnDisplayStack != null && returnDisplayStack.peek() != null && returnDisplayStack.peek() instanceof OptionsManager) {
                    this.closeAndReturn();
                } else {
                    UIManager.INSTANCE.openOptionsManager(this);
                }
            }));
            this.buttonOptions.setDefaultStyle(false);
            this.buttonClose.setDefaultStyle(false);
            this.bottomButtons = new ButtonList(this.buttonOptions);
            if (this.f_96541_.level != null) {
                this.bottomButtons.add(this.buttonClose);
            }
            this.bottomButtons.equalizeWidths(fr);
            this.bottomButtons.setWidths(Math.max(100, this.buttonOptions.m_5711_()));
            this.getRenderables().addAll(this.bottomButtons);
            this.buttonWebsite = (Button) this.m_142416_(new Button("http://journeymap.info", buttonx -> FullscreenActions.launchWebsite("")));
            this.buttonWebsite.setTooltip(Constants.getString("jm.common.website"));
            this.buttonDownload = (Button) this.m_142416_(new Button(Constants.getString("jm.common.download"), buttonx -> FullscreenActions.launchDownloadWebsite()));
            this.buttonDownload.setTooltip(Constants.getString("jm.common.download.tooltip"));
            this.buttonWebsite.setDefaultStyle(false);
            this.buttonDownload.setDefaultStyle(false);
            this.linkButtons = new ButtonList(this.buttonWebsite, this.buttonDownload);
            this.linkButtons.equalizeWidths(fr);
            this.getRenderables().addAll(this.linkButtons);
            int commonWidth = Math.max(this.bottomButtons.getWidth(0) / this.bottomButtons.size(), this.linkButtons.getWidth(0) / this.linkButtons.size());
            this.bottomButtons.setWidths(commonWidth);
            this.linkButtons.setWidths(commonWidth);
            this.buttonDiscord = (Button) this.m_142416_(new Button("", buttonx -> FullscreenActions.discord()));
            this.buttonDiscord.setDefaultStyle(false);
            this.buttonDiscord.setDrawBackground(false);
            this.buttonDiscord.setDrawFrame(false);
            this.buttonDiscord.setTooltip(Constants.getString("jm.common.discord"), Constants.getString("jm.common.discord.tooltip"));
            this.buttonDiscord.setWidth((int) ((double) this.discordLogo.getWidth() / this.getScaleFactor()));
            this.buttonDiscord.setHeight((int) ((double) this.discordLogo.getHeight() / this.getScaleFactor()));
            this.logoButtons = new ButtonList(this.buttonDiscord);
            this.logoButtons.setLayout(ButtonList.Layout.Horizontal, ButtonList.Direction.LeftToRight);
            this.logoButtons.setHeights((int) ((double) this.discordLogo.getHeight() / this.getScaleFactor()));
            this.logoButtons.setWidths((int) ((double) this.discordLogo.getWidth() / this.getScaleFactor()));
            this.getRenderables().addAll(this.logoButtons);
            this.infoButtons.forEach(x$0 -> {
                Button var10000 = (Button) this.m_142416_(x$0);
            });
        }
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.getRenderables().isEmpty()) {
            this.init();
        }
        double mx = this.f_96541_.mouseHandler.xpos() * (double) this.f_96543_ / (double) this.f_96541_.getWindow().getScreenWidth();
        double my = (double) this.f_96544_ - this.f_96541_.mouseHandler.ypos() * (double) this.f_96544_ / (double) this.f_96541_.getWindow().getScreenHeight() - 1.0;
        int hgap = 4;
        int vgap = 4;
        Font fr = this.getFontRenderer();
        int estimatedInfoHeight = this.infoButtons.getHeight(4);
        int estimatedButtonsHeight = (this.buttonClose.m_93694_() + 4) * 3 + 4;
        int centerHeight = this.f_96544_ - 35 - estimatedButtonsHeight;
        int lineHeight = (int) (9.0 * 1.4);
        int bx = this.f_96543_ / 2;
        int by = 0;
        boolean movePeople = System.currentTimeMillis() - this.lastPeopleMove > 20L;
        if (movePeople) {
            this.lastPeopleMove = System.currentTimeMillis();
        }
        Double screenBounds = new Double(0.0, 0.0, (double) this.f_96543_, (double) this.f_96544_);
        if (!this.devButtons.isEmpty()) {
            for (SplashPerson dev : this.devs) {
                if (dev.getButton().mouseOver(mx, my)) {
                    dev.randomizeVector();
                }
                this.drawPerson(graphics, by, lineHeight, dev);
                if (movePeople) {
                    dev.avoid(this.devs);
                    dev.adjustVector(screenBounds);
                }
            }
        }
        if (!this.peopleButtons.isEmpty()) {
            for (SplashPerson peep : this.people) {
                if (peep.getButton().mouseOver(mx, my)) {
                    peep.randomizeVector();
                }
                this.drawPerson(graphics, by, lineHeight, peep);
                if (movePeople) {
                    peep.avoid(this.devs);
                    peep.adjustVector(screenBounds);
                }
            }
        }
        if (!this.infoButtons.isEmpty()) {
            by = 35 + (centerHeight - estimatedInfoHeight) / 2;
            int var23 = (int) ((double) by + (double) lineHeight * 1.5);
            this.infoButtons.layoutCenteredVertical(bx - ((Button) this.infoButtons.get(0)).m_5711_() / 2, var23 + this.infoButtons.getHeight(0) / 2, true, 0);
            int listX = this.infoButtons.getLeftX() - 10;
            int listY = by - 5;
            int listWidth = this.infoButtons.getRightX() + 10 - listX;
            int listHeight = this.infoButtons.getBottomY() + 5 - listY;
            DrawUtil.drawGradientRect(graphics.pose(), (double) (listX - 1), (double) (listY - 1), (double) (listWidth + 2), (double) (listHeight + 2), 12632256, 0.8F, 12632256, 0.8F);
            DrawUtil.drawGradientRect(graphics.pose(), (double) listX, (double) listY, (double) listWidth, (double) listHeight, 4210752, 1.0F, 0, 1.0F);
            DrawUtil.drawLabel(graphics, Constants.getString("jm.common.splash_whatisnew"), (double) bx, (double) by, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.0F, 65535, 1.0F, 1.0, true);
        }
        int rowHeight = this.buttonOptions.m_93694_() + 4;
        by = this.f_96544_ - rowHeight - 4;
        this.bottomButtons.layoutCenteredHorizontal(bx, by, true, 4);
        by -= rowHeight;
        this.linkButtons.layoutCenteredHorizontal(bx, by, true, 4);
        by -= 4 + this.logoButtons.getHeight();
        this.logoButtons.layoutCenteredHorizontal(bx, by, true, 6);
        DrawUtil.drawImage(graphics.pose(), this.discordLogo, (double) this.buttonDiscord.m_252754_(), (double) this.buttonDiscord.m_252907_(), false, (float) (1.0 / this.getScaleFactor()), 0.0);
    }

    protected int drawPerson(GuiGraphics graphics, int by, int lineHeight, SplashPerson person) {
        float scale = 1.0F;
        Button button = person.getButton();
        int imgSize = (int) ((float) person.getSkin().getWidth() * scale);
        int imgY = button.m_252907_() - 2;
        int imgX = button.getCenterX() - imgSize / 2;
        if (!(person instanceof SplashPerson.Fake)) {
            DrawUtil.drawGradientRect(graphics.pose(), (double) (imgX - 1), (double) (imgY - 1), (double) (imgSize + 2), (double) (imgSize + 2), 0, 0.4F, 0, 0.8F);
            DrawUtil.drawImage(graphics.pose(), person.getSkin(), 1.0F, (double) imgX, (double) imgY, false, scale, 0.0);
        } else {
            float size = Math.min((float) person.getSkin().getWidth() * scale, 24.0F * scale);
            DrawUtil.drawQuad(graphics.pose(), person.getSkin(), 16777215, 1.0F, (double) imgX, (double) imgY, (double) size, (double) size, false, 0.0);
        }
        by = imgY + imgSize + 4;
        String name = person.name.trim();
        String name2 = null;
        boolean twoLineName = name.contains(" ");
        if (twoLineName) {
            String[] parts = person.name.split(" ");
            name = parts[0];
            name2 = parts[1];
        }
        DrawUtil.drawLabel(graphics, name, (double) button.getCenterX(), (double) by, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.0F, 16777215, 1.0F, (double) scale, true);
        by += lineHeight;
        if (name2 != null) {
            DrawUtil.drawLabel(graphics, name2, (double) button.getCenterX(), (double) by, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.0F, 16777215, 1.0F, (double) scale, true);
            by += lineHeight;
        }
        DrawUtil.drawLabel(graphics, person.title, (double) button.getCenterX(), (double) by, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, 0, 0.0F, 65280, 1.0F, (double) scale, true);
        return by + lineHeight;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        switch(keyCode) {
            case 256:
                this.closeAndReturn();
            default:
                return true;
        }
    }

    class SplashInfoButton extends Button {

        final SplashInfo.Line infoLine;

        public SplashInfoButton(SplashInfo.Line infoLine, net.minecraft.client.gui.components.Button.OnPress pressable) {
            super(infoLine.label, pressable);
            this.infoLine = infoLine;
        }
    }
}