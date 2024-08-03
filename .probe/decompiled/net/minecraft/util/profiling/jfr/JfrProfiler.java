package net.minecraft.util.profiling.jfr;

import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import jdk.jfr.Configuration;
import jdk.jfr.Event;
import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;
import net.minecraft.FileUtil;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
import net.minecraft.util.profiling.jfr.event.ChunkGenerationEvent;
import net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;
import net.minecraft.util.profiling.jfr.event.WorldLoadFinishedEvent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class JfrProfiler implements JvmProfiler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String ROOT_CATEGORY = "Minecraft";

    public static final String WORLD_GEN_CATEGORY = "World Generation";

    public static final String TICK_CATEGORY = "Ticking";

    public static final String NETWORK_CATEGORY = "Network";

    private static final List<Class<? extends Event>> CUSTOM_EVENTS = List.of(ChunkGenerationEvent.class, PacketReceivedEvent.class, PacketSentEvent.class, NetworkSummaryEvent.class, ServerTickTimeEvent.class, WorldLoadFinishedEvent.class);

    private static final String FLIGHT_RECORDER_CONFIG = "/flightrecorder-config.jfc";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd-HHmmss").toFormatter().withZone(ZoneId.systemDefault());

    private static final JfrProfiler INSTANCE = new JfrProfiler();

    @Nullable
    Recording recording;

    private float currentAverageTickTime;

    private final Map<String, NetworkSummaryEvent.SumAggregation> networkTrafficByAddress = new ConcurrentHashMap();

    private JfrProfiler() {
        CUSTOM_EVENTS.forEach(FlightRecorder::register);
        FlightRecorder.addPeriodicEvent(ServerTickTimeEvent.class, () -> new ServerTickTimeEvent(this.currentAverageTickTime).commit());
        FlightRecorder.addPeriodicEvent(NetworkSummaryEvent.class, () -> {
            Iterator<NetworkSummaryEvent.SumAggregation> $$0 = this.networkTrafficByAddress.values().iterator();
            while ($$0.hasNext()) {
                ((NetworkSummaryEvent.SumAggregation) $$0.next()).commitEvent();
                $$0.remove();
            }
        });
    }

    public static JfrProfiler getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean start(Environment environment0) {
        URL $$1 = JfrProfiler.class.getResource("/flightrecorder-config.jfc");
        if ($$1 == null) {
            LOGGER.warn("Could not find default flight recorder config at {}", "/flightrecorder-config.jfc");
            return false;
        } else {
            try {
                BufferedReader $$2 = new BufferedReader(new InputStreamReader($$1.openStream()));
                boolean var4;
                try {
                    var4 = this.start($$2, environment0);
                } catch (Throwable var7) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                    throw var7;
                }
                $$2.close();
                return var4;
            } catch (IOException var8) {
                LOGGER.warn("Failed to start flight recorder using configuration at {}", $$1, var8);
                return false;
            }
        }
    }

    @Override
    public Path stop() {
        if (this.recording == null) {
            throw new IllegalStateException("Not currently profiling");
        } else {
            this.networkTrafficByAddress.clear();
            Path $$0 = this.recording.getDestination();
            this.recording.stop();
            return $$0;
        }
    }

    @Override
    public boolean isRunning() {
        return this.recording != null;
    }

    @Override
    public boolean isAvailable() {
        return FlightRecorder.isAvailable();
    }

    private boolean start(Reader reader0, Environment environment1) {
        if (this.isRunning()) {
            LOGGER.warn("Profiling already in progress");
            return false;
        } else {
            try {
                Configuration $$2 = Configuration.create(reader0);
                String $$3 = DATE_TIME_FORMATTER.format(Instant.now());
                this.recording = Util.make(new Recording($$2), p_185311_ -> {
                    CUSTOM_EVENTS.forEach(p_185311_::enable);
                    p_185311_.setDumpOnExit(true);
                    p_185311_.setToDisk(true);
                    p_185311_.setName(String.format(Locale.ROOT, "%s-%s-%s", environment1.getDescription(), SharedConstants.getCurrentVersion().getName(), $$3));
                });
                Path $$4 = Paths.get(String.format(Locale.ROOT, "debug/%s-%s.jfr", environment1.getDescription(), $$3));
                FileUtil.createDirectoriesSafe($$4.getParent());
                this.recording.setDestination($$4);
                this.recording.start();
                this.setupSummaryListener();
            } catch (ParseException | IOException var6) {
                LOGGER.warn("Failed to start jfr profiling", var6);
                return false;
            }
            LOGGER.info("Started flight recorder profiling id({}):name({}) - will dump to {} on exit or stop command", new Object[] { this.recording.getId(), this.recording.getName(), this.recording.getDestination() });
            return true;
        }
    }

    private void setupSummaryListener() {
        FlightRecorder.addListener(new FlightRecorderListener() {

            final SummaryReporter summaryReporter = new SummaryReporter(() -> JfrProfiler.this.recording = null);

            public void recordingStateChanged(Recording p_185339_) {
                if (p_185339_ == JfrProfiler.this.recording && p_185339_.getState() == RecordingState.STOPPED) {
                    this.summaryReporter.recordingStopped(p_185339_.getDestination());
                    FlightRecorder.removeListener(this);
                }
            }
        });
    }

    @Override
    public void onServerTick(float float0) {
        if (ServerTickTimeEvent.TYPE.isEnabled()) {
            this.currentAverageTickTime = float0;
        }
    }

    @Override
    public void onPacketReceived(int int0, int int1, SocketAddress socketAddress2, int int3) {
        if (PacketReceivedEvent.TYPE.isEnabled()) {
            new PacketReceivedEvent(int0, int1, socketAddress2, int3).commit();
        }
        if (NetworkSummaryEvent.TYPE.isEnabled()) {
            this.networkStatFor(socketAddress2).trackReceivedPacket(int3);
        }
    }

    @Override
    public void onPacketSent(int int0, int int1, SocketAddress socketAddress2, int int3) {
        if (PacketSentEvent.TYPE.isEnabled()) {
            new PacketSentEvent(int0, int1, socketAddress2, int3).commit();
        }
        if (NetworkSummaryEvent.TYPE.isEnabled()) {
            this.networkStatFor(socketAddress2).trackSentPacket(int3);
        }
    }

    private NetworkSummaryEvent.SumAggregation networkStatFor(SocketAddress socketAddress0) {
        return (NetworkSummaryEvent.SumAggregation) this.networkTrafficByAddress.computeIfAbsent(socketAddress0.toString(), NetworkSummaryEvent.SumAggregation::new);
    }

    @Nullable
    @Override
    public ProfiledDuration onWorldLoadedStarted() {
        if (!WorldLoadFinishedEvent.TYPE.isEnabled()) {
            return null;
        } else {
            WorldLoadFinishedEvent $$0 = new WorldLoadFinishedEvent();
            $$0.begin();
            return $$0::commit;
        }
    }

    @Nullable
    @Override
    public ProfiledDuration onChunkGenerate(ChunkPos chunkPos0, ResourceKey<Level> resourceKeyLevel1, String string2) {
        if (!ChunkGenerationEvent.TYPE.isEnabled()) {
            return null;
        } else {
            ChunkGenerationEvent $$3 = new ChunkGenerationEvent(chunkPos0, resourceKeyLevel1, string2);
            $$3.begin();
            return $$3::commit;
        }
    }
}