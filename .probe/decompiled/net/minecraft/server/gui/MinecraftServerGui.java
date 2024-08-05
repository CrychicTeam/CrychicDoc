package net.minecraft.server.gui;

import com.google.common.collect.Lists;
import com.mojang.logging.LogQueues;
import com.mojang.logging.LogUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.server.dedicated.DedicatedServer;
import org.slf4j.Logger;

public class MinecraftServerGui extends JComponent {

    private static final Font MONOSPACED = new Font("Monospaced", 0, 12);

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String TITLE = "Minecraft server";

    private static final String SHUTDOWN_TITLE = "Minecraft server - shutting down!";

    private final DedicatedServer server;

    private Thread logAppenderThread;

    private final Collection<Runnable> finalizers = Lists.newArrayList();

    final AtomicBoolean isClosing = new AtomicBoolean();

    public static MinecraftServerGui showFrameFor(final DedicatedServer dedicatedServer0) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception var3) {
        }
        final JFrame $$1 = new JFrame("Minecraft server");
        final MinecraftServerGui $$2 = new MinecraftServerGui(dedicatedServer0);
        $$1.setDefaultCloseOperation(2);
        $$1.add($$2);
        $$1.pack();
        $$1.setLocationRelativeTo(null);
        $$1.setVisible(true);
        $$1.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent p_139944_) {
                if (!$$2.isClosing.getAndSet(true)) {
                    $$1.setTitle("Minecraft server - shutting down!");
                    dedicatedServer0.m_7570_(true);
                    $$2.runFinalizers();
                }
            }
        });
        $$2.addFinalizer($$1::dispose);
        $$2.start();
        return $$2;
    }

    private MinecraftServerGui(DedicatedServer dedicatedServer0) {
        this.server = dedicatedServer0;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());
        try {
            this.add(this.buildChatPanel(), "Center");
            this.add(this.buildInfoPanel(), "West");
        } catch (Exception var3) {
            LOGGER.error("Couldn't build server GUI", var3);
        }
    }

    public void addFinalizer(Runnable runnable0) {
        this.finalizers.add(runnable0);
    }

    private JComponent buildInfoPanel() {
        JPanel $$0 = new JPanel(new BorderLayout());
        StatsComponent $$1 = new StatsComponent(this.server);
        this.finalizers.add($$1::m_139964_);
        $$0.add($$1, "North");
        $$0.add(this.buildPlayerPanel(), "Center");
        $$0.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return $$0;
    }

    private JComponent buildPlayerPanel() {
        JList<?> $$0 = new PlayerListComponent(this.server);
        JScrollPane $$1 = new JScrollPane($$0, 22, 30);
        $$1.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return $$1;
    }

    private JComponent buildChatPanel() {
        JPanel $$0 = new JPanel(new BorderLayout());
        JTextArea $$1 = new JTextArea();
        JScrollPane $$2 = new JScrollPane($$1, 22, 30);
        $$1.setEditable(false);
        $$1.setFont(MONOSPACED);
        JTextField $$3 = new JTextField();
        $$3.addActionListener(p_276357_ -> {
            String $$2x = $$3.getText().trim();
            if (!$$2x.isEmpty()) {
                this.server.handleConsoleInput($$2x, this.server.m_129893_());
            }
            $$3.setText("");
        });
        $$1.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent p_139949_) {
            }
        });
        $$0.add($$2, "Center");
        $$0.add($$3, "South");
        $$0.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        this.logAppenderThread = new Thread(() -> {
            String $$2x;
            while (($$2x = LogQueues.getNextLogEvent("ServerGuiConsole")) != null) {
                this.print($$1, $$2, $$2x);
            }
        });
        this.logAppenderThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        this.logAppenderThread.setDaemon(true);
        return $$0;
    }

    public void start() {
        this.logAppenderThread.start();
    }

    public void close() {
        if (!this.isClosing.getAndSet(true)) {
            this.runFinalizers();
        }
    }

    void runFinalizers() {
        this.finalizers.forEach(Runnable::run);
    }

    public void print(JTextArea jTextArea0, JScrollPane jScrollPane1, String string2) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> this.print(jTextArea0, jScrollPane1, string2));
        } else {
            Document $$3 = jTextArea0.getDocument();
            JScrollBar $$4 = jScrollPane1.getVerticalScrollBar();
            boolean $$5 = false;
            if (jScrollPane1.getViewport().getView() == jTextArea0) {
                $$5 = (double) $$4.getValue() + $$4.getSize().getHeight() + (double) (MONOSPACED.getSize() * 4) > (double) $$4.getMaximum();
            }
            try {
                $$3.insertString($$3.getLength(), string2, null);
            } catch (BadLocationException var8) {
            }
            if ($$5) {
                $$4.setValue(Integer.MAX_VALUE);
            }
        }
    }
}