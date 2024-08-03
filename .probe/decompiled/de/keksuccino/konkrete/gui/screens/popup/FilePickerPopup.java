package de.keksuccino.konkrete.gui.screens.popup;

import com.google.common.io.Files;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.scrollarea.ScrollArea;
import de.keksuccino.konkrete.gui.content.scrollarea.ScrollAreaEntry;
import de.keksuccino.konkrete.input.KeyboardData;
import de.keksuccino.konkrete.input.KeyboardHandler;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.localization.Locals;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class FilePickerPopup extends Popup {

    private static ResourceLocation fileIcon = new ResourceLocation("keksuccino", "filechooser/file_icon.png");

    private static ResourceLocation folderIcon = new ResourceLocation("keksuccino", "filechooser/folder_icon.png");

    private static ResourceLocation backIcon = new ResourceLocation("keksuccino", "filechooser/back_icon.png");

    private static String lastpath;

    private boolean checklastpath;

    public Color overlayColor = new Color(26, 26, 26);

    private ScrollArea scroll;

    public File home;

    public File directory;

    private Popup fallback;

    private List<String> filetypes = new ArrayList();

    private String filetypesString;

    private Consumer<File> callback;

    private int lastWidth = 0;

    private int lastHeight = 0;

    private AdvancedButton chooseButton;

    private AdvancedButton closeButton;

    private FilePickerPopup.FileChooserEntry focused;

    public FilePickerPopup(String directory, @Nullable String home, @Nullable Popup fallback, boolean checkForLastPath, Consumer<File> callback, @Nullable String... filetypes) {
        super(240);
        this.fallback = fallback;
        if (home != null) {
            this.home = new File(home);
        }
        this.directory = new File(directory);
        this.callback = callback;
        this.checklastpath = checkForLastPath;
        if (this.checklastpath && lastpath != null) {
            File f = new File(lastpath);
            if (f.exists() && f.isDirectory()) {
                if (this.home != null) {
                    if (f.getAbsolutePath().replace("\\", "/").startsWith(this.home.getAbsolutePath().replace("\\", "/"))) {
                        this.directory = f;
                    }
                } else {
                    this.directory = f;
                }
            }
        } else {
            lastpath = directory;
        }
        if (filetypes != null) {
            for (String s : filetypes) {
                this.filetypes.add(s.toLowerCase());
                if (this.filetypesString == null) {
                    this.filetypesString = s.toUpperCase();
                } else {
                    this.filetypesString = this.filetypesString + ", " + s.toUpperCase();
                }
            }
        }
        if (this.filetypesString == null) {
            this.filetypesString = "ALL";
        }
        KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
        KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
        this.updateFileList();
        this.chooseButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.choosefile.choose"), true, press -> {
            if (this.focused != null) {
                this.focused.onClick();
            }
        });
        this.addButton(this.chooseButton);
        this.colorizePopupButton(this.chooseButton);
        this.closeButton = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.yesno.cancel"), true, press -> {
            if (this.callback != null) {
                this.callback.accept(null);
            }
            this.setDisplayed(false);
            if (this.fallback != null) {
                PopupHandler.displayPopup(this.fallback);
            }
        });
        this.addButton(this.closeButton);
        this.colorizePopupButton(this.closeButton);
    }

    public FilePickerPopup(String directory, @Nullable String home, @Nullable Popup fallback, boolean checkForLastPath, Consumer<File> callback) {
        this(directory, home, fallback, checkForLastPath, callback, (String[]) null);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, Screen renderIn) {
        super.render(graphics, mouseX, mouseY, renderIn);
        if (this.lastWidth != renderIn.width || this.lastHeight != renderIn.height) {
            this.updateFileList();
            this.focused = null;
        }
        this.lastWidth = renderIn.width;
        this.lastHeight = renderIn.height;
        this.scroll.height = renderIn.height - 100;
        this.scroll.y = 40;
        this.scroll.x = renderIn.width / 2 - this.scroll.width / 2;
        this.scroll.render(graphics);
        graphics.fill(0, 0, renderIn.width, 40, this.overlayColor.getRGB());
        graphics.fill(0, renderIn.height - 60, renderIn.width, renderIn.height, this.overlayColor.getRGB());
        graphics.drawCenteredString(Minecraft.getInstance().font, "§l" + Locals.localize("popup.choosefile.title"), renderIn.width / 2, 17, Color.WHITE.getRGB());
        if (this.filetypesString != null) {
            graphics.drawCenteredString(Minecraft.getInstance().font, Locals.localize("popup.choosefile.supported") + " " + this.filetypesString, renderIn.width / 2, renderIn.height - 50, Color.WHITE.getRGB());
            this.chooseButton.f_93620_ = renderIn.width / 2 - this.chooseButton.getWidth() - 5;
            this.chooseButton.f_93621_ = renderIn.height - 30;
            this.closeButton.f_93620_ = renderIn.width / 2 + 5;
            this.closeButton.f_93621_ = renderIn.height - 30;
        } else {
            this.chooseButton.f_93620_ = renderIn.width / 2 - this.chooseButton.getWidth() - 5;
            this.chooseButton.f_93621_ = renderIn.height - 40;
            this.closeButton.f_93620_ = renderIn.width / 2 + 5;
            this.closeButton.f_93621_ = renderIn.height - 40;
        }
        this.renderButtons(graphics, mouseX, mouseY);
        if (this.focused != null && !this.focused.focused) {
            this.focused = null;
        }
    }

    public void updateFileList() {
        this.scroll = new ScrollArea(0, 0, 200, 0);
        this.scroll.backgroundColor = new Color(255, 255, 255, 20);
        if (this.directory.exists() && this.directory.isDirectory()) {
            File parent = this.directory.getAbsoluteFile().getParentFile();
            if (this.home != null) {
                if (parent != null && parent.getAbsolutePath().replace("\\", "/").startsWith(this.home.getAbsolutePath().replace("\\", "/")) && parent.exists() && parent.isDirectory()) {
                    this.scroll.addEntry(new FilePickerPopup.FileChooserEntry(null, this, FilePickerPopup.FileChooserEntry.Type.BACK));
                }
            } else if (parent != null && parent.exists() && parent.isDirectory()) {
                this.scroll.addEntry(new FilePickerPopup.FileChooserEntry(null, this, FilePickerPopup.FileChooserEntry.Type.BACK));
            }
            List<String> folders = new ArrayList();
            List<String> files = new ArrayList();
            for (File f : this.directory.listFiles()) {
                if (f.isDirectory()) {
                    folders.add(f.getPath());
                } else if (!this.filetypes.isEmpty()) {
                    if (this.filetypes.contains(Files.getFileExtension(f.getName().toLowerCase()))) {
                        files.add(f.getPath());
                    }
                } else {
                    files.add(f.getPath());
                }
            }
            Collections.sort(folders, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(files, String.CASE_INSENSITIVE_ORDER);
            for (String s : folders) {
                File fx = new File(s);
                this.scroll.addEntry(new FilePickerPopup.FileChooserEntry(fx, this, FilePickerPopup.FileChooserEntry.Type.FOLDER));
            }
            for (String s : files) {
                File fx = new File(s);
                this.scroll.addEntry(new FilePickerPopup.FileChooserEntry(fx, this, FilePickerPopup.FileChooserEntry.Type.FILE));
            }
        }
    }

    public void onEnterPressed(KeyboardData d) {
        if (d.keycode == 257 && this.isDisplayed() && this.focused != null) {
            this.focused.onClick();
        }
    }

    public void onEscapePressed(KeyboardData d) {
        if (d.keycode == 256 && this.isDisplayed()) {
            if (this.callback != null) {
                this.callback.accept(null);
            }
            this.setDisplayed(false);
            if (this.fallback != null) {
                PopupHandler.displayPopup(this.fallback);
            }
        }
    }

    public static class FileChooserEntry extends ScrollAreaEntry {

        public File file;

        public FilePickerPopup.FileChooserEntry.Type type;

        public FilePickerPopup filechooser;

        private int clickTick = 0;

        private boolean clickPre = false;

        private boolean click = false;

        private boolean focused = false;

        public FileChooserEntry(File file, FilePickerPopup filechooser, FilePickerPopup.FileChooserEntry.Type type) {
            super(filechooser.scroll);
            this.file = file;
            this.type = type;
            this.filechooser = filechooser;
        }

        @Override
        public void render(GuiGraphics graphics) {
            if (this.isHoveredOrFocused() && this.isVisible() && MouseInput.isLeftMouseDown()) {
                this.focused = true;
                this.filechooser.focused = this;
                if (!this.click) {
                    this.clickPre = true;
                    this.clickTick = 0;
                }
            }
            if (!this.isHoveredOrFocused() && MouseInput.isLeftMouseDown()) {
                this.focused = false;
            }
            super.render(graphics);
        }

        @Override
        public void renderEntry(GuiGraphics graphics) {
            RenderSystem.enableBlend();
            ResourceLocation res = null;
            if (this.type == FilePickerPopup.FileChooserEntry.Type.FILE) {
                res = FilePickerPopup.fileIcon;
            }
            if (this.type == FilePickerPopup.FileChooserEntry.Type.FOLDER) {
                res = FilePickerPopup.folderIcon;
            }
            if (this.type == FilePickerPopup.FileChooserEntry.Type.BACK) {
                res = FilePickerPopup.backIcon;
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (res != null) {
                graphics.blit(res, this.x, this.y, 0.0F, 0.0F, 20, 20, 20, 20);
            }
            if (this.type == FilePickerPopup.FileChooserEntry.Type.BACK) {
                graphics.drawString(Minecraft.getInstance().font, Locals.localize("popup.choosefile.back"), this.x + 30, this.y + 7, Color.WHITE.getRGB());
            } else {
                graphics.drawString(Minecraft.getInstance().font, this.file.getName(), this.x + 30, this.y + 7, Color.WHITE.getRGB());
            }
            if (!MouseInput.isLeftMouseDown() && this.clickPre) {
                this.click = true;
                this.clickPre = false;
                this.clickTick = 0;
            }
            if (this.click) {
                if (this.clickTick < 15) {
                    this.clickTick++;
                } else {
                    this.click = false;
                    this.clickTick = 0;
                }
                if (MouseInput.isLeftMouseDown() && this.isHoveredOrFocused()) {
                    this.onClick();
                    this.click = false;
                    this.clickTick = 0;
                }
            }
            if (this.focused) {
                this.renderBorder(graphics);
            }
        }

        private void renderBorder(GuiGraphics graphics) {
            graphics.fill(this.x, this.y, this.x + 1, this.y + this.getHeight(), Color.WHITE.getRGB());
            graphics.fill(this.x + this.getWidth() - 1, this.y, this.x + this.getWidth(), this.y + this.getHeight(), Color.WHITE.getRGB());
            graphics.fill(this.x, this.y, this.x + this.getWidth(), this.y + 1, Color.WHITE.getRGB());
            graphics.fill(this.x, this.y + this.getHeight() - 1, this.x + this.getWidth(), this.y + this.getHeight(), Color.WHITE.getRGB());
        }

        public void onClick() {
            if (this.type == FilePickerPopup.FileChooserEntry.Type.BACK) {
                File parent = this.filechooser.directory.getParentFile();
                if (parent.exists() && parent.isDirectory()) {
                    String home = null;
                    if (this.filechooser.home != null) {
                        home = this.filechooser.home.getPath();
                    }
                    PopupHandler.displayPopup(new FilePickerPopup(parent.getPath(), home, this.filechooser.fallback, false, this.filechooser.callback, (String[]) this.filechooser.filetypes.toArray(new String[0])));
                }
            }
            if (this.type == FilePickerPopup.FileChooserEntry.Type.FOLDER) {
                String home = null;
                if (this.filechooser.home != null) {
                    home = this.filechooser.home.getPath();
                }
                PopupHandler.displayPopup(new FilePickerPopup(this.file.getPath(), home, this.filechooser.fallback, false, this.filechooser.callback, (String[]) this.filechooser.filetypes.toArray(new String[0])));
            }
            if (this.type == FilePickerPopup.FileChooserEntry.Type.FILE) {
                if (this.filechooser.callback != null) {
                    this.filechooser.callback.accept(new File(this.file.getAbsolutePath().replace("\\", "/")));
                }
                this.filechooser.setDisplayed(false);
                if (this.filechooser.fallback != null) {
                    PopupHandler.displayPopup(this.filechooser.fallback);
                }
            }
        }

        @Override
        public int getHeight() {
            return 20;
        }

        public static enum Type {

            FILE, FOLDER, BACK
        }
    }
}