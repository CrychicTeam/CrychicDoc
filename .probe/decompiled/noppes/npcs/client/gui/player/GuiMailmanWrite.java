package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketPlayerMailDelete;
import noppes.npcs.packets.server.SPacketPlayerMailSend;
import noppes.npcs.shared.client.gui.components.GuiButtonNextPage;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiClose;
import noppes.npcs.shared.client.gui.listeners.IGuiError;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

@OnlyIn(Dist.CLIENT)
public class GuiMailmanWrite extends GuiContainerNPCInterface<ContainerMail> implements ITextfieldListener, IGuiError, IGuiClose {

    private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");

    private static final ResourceLocation bookWidgets = new ResourceLocation("textures/gui/widgets.png");

    private static final ResourceLocation bookInventory = new ResourceLocation("textures/gui/container/inventory.png");

    private final TextFieldHelper pageEdit = new TextFieldHelper(this::getText, this::setText, this::getClipboard, this::setClipboard, p_238774_1_ -> p_238774_1_.length() < 1024 && this.f_96547_.wordWrapHeight(p_238774_1_, 114) <= 128);

    private int updateCount;

    private int bookImageWidth = 192;

    private int bookImageHeight = 192;

    private int bookTotalPages = 1;

    private int currPage;

    private ListTag bookPages;

    private GuiButtonNextPage buttonNextPage;

    private GuiButtonNextPage buttonPreviousPage;

    private final boolean canEdit;

    private final boolean canSend;

    private boolean hasSend = false;

    public static Screen parent;

    public static PlayerMail mail = new PlayerMail();

    private Minecraft mc = Minecraft.getInstance();

    private String username = "";

    private GuiLabel error;

    public GuiMailmanWrite(ContainerMail container, Inventory inv, Component titleIn) {
        super(null, container, inv, titleIn);
        this.title = "";
        this.canEdit = container.canEdit;
        this.canSend = container.canSend;
        if (mail.message.contains("pages")) {
            this.bookPages = mail.message.getList("pages", 8);
        }
        if (this.bookPages != null) {
            this.bookPages = this.bookPages.copy();
            this.bookTotalPages = this.bookPages.size();
            if (this.bookTotalPages < 1) {
                this.bookTotalPages = 1;
            }
        } else {
            this.bookPages = new ListTag();
            this.bookPages.add(StringTag.valueOf(""));
            this.bookTotalPages = 1;
        }
        this.f_97726_ = 360;
        this.f_97727_ = 260;
        this.drawDefaultBackground = false;
    }

    @Override
    public void containerTick() {
        this.updateCount++;
    }

    @Override
    public void init() {
        super.m_7856_();
        this.f_169369_.clear();
        if (this.canEdit && !this.canSend) {
            this.addLabel(new GuiLabel(0, "mailbox.sender", this.guiLeft + 170, this.guiTop + 32, 0));
        } else {
            this.addLabel(new GuiLabel(0, "mailbox.username", this.guiLeft + 170, this.guiTop + 32, 0));
        }
        if (this.canEdit && !this.canSend) {
            this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 170, this.guiTop + 42, 114, 20, mail.sender));
        } else if (this.canEdit) {
            this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 170, this.guiTop + 42, 114, 20, this.username));
        } else {
            this.addLabel(new GuiLabel(10, mail.sender, this.guiLeft + 170, this.guiTop + 42, 0));
        }
        this.addLabel(new GuiLabel(1, "mailbox.subject", this.guiLeft + 170, this.guiTop + 72, 0));
        if (this.canEdit) {
            this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 170, this.guiTop + 82, 114, 20, mail.subject));
        } else {
            this.addLabel(new GuiLabel(11, mail.subject, this.guiLeft + 170, this.guiTop + 82, 0));
        }
        this.addLabel(this.error = new GuiLabel(2, "", this.guiLeft + 170, this.guiTop + 114, 16711680));
        if (this.canEdit && !this.canSend) {
            this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 200, this.guiTop + 171, 60, 20, "gui.done"));
        } else if (this.canEdit) {
            this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 200, this.guiTop + 171, 60, 20, "mailbox.send"));
        }
        if (!this.canEdit && !this.canSend) {
            this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 200, this.guiTop + 171, 60, 20, "selectServer.delete"));
        }
        if (!this.canEdit || this.canSend) {
            this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 200, this.guiTop + 194, 60, 20, "gui.cancel"));
        }
        this.addButton(this.buttonNextPage = new GuiButtonNextPage(this, 1, this.guiLeft + 120, this.guiTop + 156, true, b -> {
            if (this.currPage < this.bookTotalPages - 1) {
                this.currPage++;
            } else if (this.canEdit) {
                this.addNewPage();
                if (this.currPage < this.bookTotalPages - 1) {
                    this.currPage++;
                }
            }
            this.updateButtons();
        }));
        this.addButton(this.buttonPreviousPage = new GuiButtonNextPage(this, 2, this.guiLeft + 38, this.guiTop + 156, false, b -> {
            if (this.currPage > 0) {
                this.currPage--;
            }
            this.updateButtons();
        }));
        this.updateButtons();
    }

    @Override
    public void onClose() {
    }

    private void updateButtons() {
        this.buttonNextPage.f_93624_ = this.currPage < this.bookTotalPages - 1 || this.canEdit;
        this.buttonPreviousPage.f_93624_ = this.currPage > 0;
    }

    @Override
    public void buttonEvent(GuiButtonNop par1GuiButton) {
        if (par1GuiButton.f_93623_) {
            int id = par1GuiButton.id;
            if (id == 0) {
                mail.message.put("pages", this.bookPages);
                if (this.canSend) {
                    if (!this.hasSend) {
                        this.hasSend = true;
                        Packets.sendServer(new SPacketPlayerMailSend(this.username, mail.writeNBT()));
                    }
                } else {
                    this.close();
                }
            }
            if (id == 3) {
                this.close();
            }
            if (id == 4) {
                ConfirmScreen guiyesno = new ConfirmScreen(flag -> {
                    if (flag) {
                        Packets.sendServer(new SPacketPlayerMailDelete(mail.time, mail.sender));
                        this.close();
                    } else {
                        NoppesUtil.openGUI(this.player, this);
                    }
                }, Component.literal(""), Component.translatable(I18n.get("gui.deleteMessage")));
                this.setScreen(guiyesno);
            }
            this.updateButtons();
        }
    }

    private void addNewPage() {
        if (this.bookPages != null && this.bookPages.size() < 50) {
            this.bookPages.add(StringTag.valueOf(""));
            this.bookTotalPages++;
        }
    }

    @Override
    public boolean charTyped(char par1, int limbSwingAmount) {
        if (!GuiTextFieldNop.isAnyActive() && this.canEdit) {
            if (SharedConstants.isAllowedChatCharacter(par1)) {
                this.pageEdit.insertText(Character.toString(par1));
                return true;
            }
        } else {
            super.m_5534_(par1, limbSwingAmount);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        return super.m_7933_(p_231046_1_, p_231046_2_, p_231046_3_) ? true : this.bookKeyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    private boolean bookKeyPressed(int p_214230_1_, int p_214230_2_, int p_214230_3_) {
        if (Screen.isSelectAll(p_214230_1_)) {
            this.pageEdit.selectAll();
            return true;
        } else if (Screen.isCopy(p_214230_1_)) {
            this.pageEdit.copy();
            return true;
        } else if (Screen.isPaste(p_214230_1_)) {
            this.pageEdit.paste();
            return true;
        } else if (Screen.isCut(p_214230_1_)) {
            this.pageEdit.cut();
            return true;
        } else {
            switch(p_214230_1_) {
                case 257:
                case 335:
                    this.pageEdit.insertText("\n");
                    return true;
                case 259:
                    this.pageEdit.removeCharsFromCursor(-1);
                    return true;
                case 261:
                    this.pageEdit.removeCharsFromCursor(1);
                    return true;
                case 262:
                    this.pageEdit.moveByChars(1, Screen.hasShiftDown());
                    return true;
                case 263:
                    this.pageEdit.moveByChars(-1, Screen.hasShiftDown());
                    return true;
                case 266:
                    this.buttonPreviousPage.m_5691_();
                    return true;
                case 267:
                    this.buttonNextPage.m_5691_();
                    return true;
                default:
                    return false;
            }
        }
    }

    private String getText() {
        return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.size() ? this.bookPages.getString(this.currPage) : "";
    }

    private void setText(String par1Str) {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.size()) {
            this.bookPages.set(this.currPage, (Tag) StringTag.valueOf(par1Str));
        }
    }

    private void setClipboard(String p_238760_1_) {
        if (this.f_96541_ != null) {
            TextFieldHelper.setClipboardContents(this.f_96541_, p_238760_1_);
        }
    }

    private String getClipboard() {
        return this.f_96541_ != null ? TextFieldHelper.getClipboardContents(this.f_96541_) : "";
    }

    private void append(String par1Str) {
        String s1 = this.getText();
        String s2 = s1 + par1Str;
        int i = this.mc.font.wordWrapHeight(s2 + ChatFormatting.BLACK + "_", 118);
        if (i <= 118 && s2.length() < 256) {
            this.setText(s2);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float par3) {
        super.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, bookGuiTextures);
        graphics.blit(bookGuiTextures, this.guiLeft + 130, this.guiTop + 22, 0, 0, this.bookImageWidth, this.bookImageHeight / 3);
        graphics.blit(bookGuiTextures, this.guiLeft + 130, this.guiTop + 22 + this.bookImageHeight / 3, 0, this.bookImageHeight / 2, this.bookImageWidth, this.bookImageHeight / 2);
        graphics.blit(bookGuiTextures, this.guiLeft, this.guiTop + 2, 0, 0, this.bookImageWidth, this.bookImageHeight);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, bookInventory);
        graphics.blit(bookInventory, this.guiLeft + 20, this.guiTop + 173, 0, 82, 180, 55);
        graphics.blit(bookInventory, this.guiLeft + 20, this.guiTop + 228, 0, 140, 180, 28);
        String s = I18n.get("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
        String s1 = "";
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.size()) {
            s1 = this.bookPages.getString(this.currPage);
        }
        if (this.canEdit) {
            if (this.updateCount / 6 % 2 == 0) {
                s1 = s1 + "_";
            } else if (this.updateCount / 6 % 2 == 0) {
                s1 = s1 + ChatFormatting.BLACK + "_";
            } else {
                s1 = s1 + ChatFormatting.GRAY + "_";
            }
        }
        int l = this.mc.font.width(s);
        graphics.drawString(this.mc.font, s, this.guiLeft - l + this.bookImageWidth - 44, this.guiTop + 18, 0);
        graphics.drawWordWrap(this.mc.font, Component.translatable(s1), this.guiLeft + 36, this.guiTop + 18 + 16, 116, 0);
        graphics.fillGradient(this.guiLeft + 175, this.guiTop + 136, this.guiLeft + 269, this.guiTop + 154, -1072689136, -804253680);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, bookWidgets);
        for (int i = 0; i < 4; i++) {
            graphics.blit(bookWidgets, this.guiLeft + 175 + i * 24, this.guiTop + 134, 0, 22, 24, 24);
        }
        super.m_88315_(graphics, mouseX, mouseY, par3);
    }

    @Override
    public void close() {
        this.mc.setScreen(parent);
        parent = null;
        mail = new PlayerMail();
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            this.username = textfield.m_94155_();
        }
        if (textfield.id == 1) {
            mail.subject = textfield.m_94155_();
        }
        if (textfield.id == 2) {
            mail.sender = textfield.m_94155_();
        }
    }

    @Override
    public void setError(int i, CompoundTag data) {
        if (i == 0) {
            this.error.m_93666_(Component.translatable("mailbox.errorUsername"));
        }
        if (i == 1) {
            this.error.m_93666_(Component.translatable("mailbox.errorSubject"));
        }
        this.hasSend = false;
    }

    @Override
    public void setClose(CompoundTag data) {
        this.player.sendSystemMessage(Component.translatable("mailbox.succes", data.getString("username")));
    }

    @Override
    public void save() {
    }
}