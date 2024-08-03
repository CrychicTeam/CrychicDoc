package de.keksuccino.fancymenu.util.rendering.ui.screen.filebrowser;

import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import java.io.File;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChooseFileScreen extends AbstractFileBrowserScreen {

    @NotNull
    public static ChooseFileScreen build(@NotNull File rootDirectory, @NotNull Consumer<File> callback) {
        return new ChooseFileScreen(rootDirectory, rootDirectory, callback);
    }

    public ChooseFileScreen(@Nullable File rootDirectory, @NotNull File startDirectory, @NotNull Consumer<File> callback) {
        super(Component.translatable("fancymenu.ui.filechooser.choose.file"), rootDirectory, startDirectory, callback);
    }

    @NotNull
    @Override
    protected ExtendedButton buildConfirmButton() {
        return new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.ok"), button -> {
            AbstractFileBrowserScreen.AbstractFileScrollAreaEntry selected = this.getSelectedEntry();
            if (selected != null && !selected.resourceUnfriendlyFileName) {
                this.callback.accept(new File(selected.file.getPath().replace("\\", "/")));
            }
        }) {

            @Override
            public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                AbstractFileBrowserScreen.AbstractFileScrollAreaEntry e = ChooseFileScreen.this.getSelectedEntry();
                this.f_93623_ = e != null && !e.resourceUnfriendlyFileName && e.file.isFile();
                super.render(graphics, mouseX, mouseY, partial);
            }
        };
    }

    @Override
    protected AbstractFileBrowserScreen.AbstractFileScrollAreaEntry buildFileEntry(@NotNull File f) {
        return new ChooseFileScreen.FileScrollAreaEntry(this.fileListScrollArea, f);
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        if (keycode == 257) {
            AbstractFileBrowserScreen.AbstractFileScrollAreaEntry selected = this.getSelectedEntry();
            if (selected != null) {
                this.callback.accept(new File(selected.file.getPath().replace("\\", "/")));
                return true;
            }
        }
        return super.m_7933_(keycode, scancode, modifiers);
    }

    public class FileScrollAreaEntry extends AbstractFileBrowserScreen.AbstractFileScrollAreaEntry {

        public FileScrollAreaEntry(@NotNull ScrollArea parent, @NotNull File file) {
            super(parent, file);
        }

        @Override
        public void onClick(ScrollAreaEntry entry) {
            if (!this.resourceUnfriendlyFileName) {
                long now = System.currentTimeMillis();
                if (now - this.lastClick < 400L) {
                    if (this.file.isFile()) {
                        ChooseFileScreen.this.callback.accept(new File(this.file.getPath().replace("\\", "/")));
                    } else if (this.file.isDirectory()) {
                        ChooseFileScreen.this.setDirectory(this.file, true);
                    }
                }
                ChooseFileScreen.this.updatePreview(this.file);
                this.lastClick = now;
            }
        }
    }
}