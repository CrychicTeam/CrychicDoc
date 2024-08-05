package icyllis.modernui.mc.forge;

import icyllis.modernui.ModernUI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class ServerHandler {

    public static final ServerHandler INSTANCE = new ServerHandler();

    boolean mStarted = false;

    private long mShutdownTime = 0L;

    private long mNextShutdownNotify = 0L;

    private final long[] mShutdownNotifyTimes = new long[] { 1000L, 2000L, 3000L, 4000L, 5000L, 6000L, 7000L, 8000L, 9000L, 10000L, 60000L, 300000L, 600000L, 1800000L };

    @SubscribeEvent
    void onStart(@Nonnull ServerStartedEvent event) {
        this.mStarted = true;
        this.determineShutdownTime();
    }

    @SubscribeEvent
    void onStop(@Nonnull ServerStoppingEvent event) {
        this.mStarted = false;
    }

    public void determineShutdownTime() {
        if (this.mStarted) {
            if (Config.COMMON.autoShutdown.get()) {
                Calendar calendar = Calendar.getInstance();
                int current = calendar.get(11) * 3600 + calendar.get(12) * 60 + calendar.get(13);
                int target = Integer.MAX_VALUE;
                for (String s : Config.COMMON.shutdownTimes.get()) {
                    try {
                        String[] s1 = s.split(":", 2);
                        int h = Integer.parseInt(s1[0]);
                        int m = Integer.parseInt(s1[1]);
                        if (h >= 0 && h < 24 && m >= 0 && m < 60) {
                            int t = h * 3600 + m * 60;
                            if (t < current) {
                                t += 86400;
                            }
                            target = Math.min(t, target);
                        } else {
                            ModernUI.LOGGER.warn(ModernUI.MARKER, "Wrong time format while setting auto-shutdown time, input: {}", s);
                        }
                    } catch (IndexOutOfBoundsException | NumberFormatException var10) {
                        ModernUI.LOGGER.error(ModernUI.MARKER, "Wrong time format while setting auto-shutdown time, input: {}", s, var10);
                    }
                }
                if (target < Integer.MAX_VALUE && target > current) {
                    this.mShutdownTime = Util.getMillis() + (long) (target - current) * 1000L;
                    ModernUI.LOGGER.debug(ModernUI.MARKER, "Server will shutdown at {}", SimpleDateFormat.getDateTimeInstance().format(new Date(this.mShutdownTime)));
                    this.mNextShutdownNotify = this.mShutdownNotifyTimes[this.mShutdownNotifyTimes.length - 1];
                } else {
                    this.mShutdownTime = 0L;
                }
            } else {
                this.mShutdownTime = 0L;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    void onLastEndTick(@Nonnull TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && this.mShutdownTime > 0L) {
            long countdown = this.mShutdownTime - Util.getMillis();
            this.sendShutdownNotification(countdown);
            if (countdown <= 0L) {
                ServerLifecycleHooks.getCurrentServer().halt(false);
            }
        }
    }

    private void sendShutdownNotification(long countdown) {
        if (countdown < this.mNextShutdownNotify) {
            while (true) {
                int index = Arrays.binarySearch(this.mShutdownNotifyTimes, this.mNextShutdownNotify);
                if (index > 0) {
                    this.mNextShutdownNotify = this.mShutdownNotifyTimes[index - 1];
                    if (countdown < this.mNextShutdownNotify) {
                        continue;
                    }
                } else {
                    this.mNextShutdownNotify = 0L;
                }
                long l = Math.round((double) countdown / 1000.0);
                String key;
                String str;
                if (l > 60L) {
                    l = Math.round((double) l / 60.0);
                    key = "message.modernui.server_shutdown_min";
                    str = "Server will shutdown in %d minutes";
                } else {
                    key = "message.modernui.server_shutdown_sec";
                    str = "Server will shutdown in %d seconds";
                }
                ModernUI.LOGGER.info(ModernUI.MARKER, String.format(str, l));
                Component component = Component.translatable(key, l).withStyle(ChatFormatting.LIGHT_PURPLE);
                ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().forEach(p -> p.displayClientMessage(component, true));
                break;
            }
        }
    }
}