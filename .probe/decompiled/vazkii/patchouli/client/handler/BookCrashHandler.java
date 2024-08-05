package vazkii.patchouli.client.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;
import net.minecraft.SystemReport;
import net.minecraft.client.Minecraft;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.gui.GuiBookEntryList;
import vazkii.patchouli.common.book.Book;

public class BookCrashHandler implements Supplier<String> {

    private static final String INDENT = "\n\t\t";

    private static final String LABEL = "Patchouli open book context";

    public static void appendToCrashReport(SystemReport report) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.screen instanceof GuiBook) {
            try {
                report.setDetail("Patchouli open book context", new BookCrashHandler());
            } catch (Exception var3) {
                PatchouliAPI.LOGGER.fatal("Failed to extend crash report system info", var3);
            }
        }
    }

    public String get() {
        if (Minecraft.getInstance().screen instanceof GuiBook gui) {
            Book book = gui.book;
            StringBuilder builder = new StringBuilder("\n\t\t");
            builder.append("Open book: ").append(book.id);
            if (gui instanceof GuiBookEntry entry) {
                builder.append("\n\t\t").append("Current entry: ").append(entry.getEntry().getId());
            } else if (gui instanceof GuiBookEntryList list) {
                builder.append("\n\t\t").append("Search query: ").append(list.getSearchQuery());
            }
            builder.append("\n\t\t").append("Current page spread: ").append(gui.getSpread());
            if (book.getContents().isErrored()) {
                Exception ex = book.getContents().getException();
                builder.append("\n\t\t").append("Book loading error: ");
                try {
                    StringWriter sw = new StringWriter();
                    try {
                        PrintWriter pw = new PrintWriter(sw);
                        try {
                            ex.printStackTrace(pw);
                            builder.append(sw.toString().replaceAll("\n", "\n\t\t"));
                        } catch (Throwable var12) {
                            try {
                                pw.close();
                            } catch (Throwable var11) {
                                var12.addSuppressed(var11);
                            }
                            throw var12;
                        }
                        pw.close();
                    } catch (Throwable var13) {
                        try {
                            sw.close();
                        } catch (Throwable var10) {
                            var13.addSuppressed(var10);
                        }
                        throw var13;
                    }
                    sw.close();
                } catch (IOException var14) {
                }
            }
            return builder.toString();
        } else {
            return "n/a";
        }
    }
}