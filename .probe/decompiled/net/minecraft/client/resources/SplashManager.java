package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;

public class SplashManager extends SimplePreparableReloadListener<List<String>> {

    private static final ResourceLocation SPLASHES_LOCATION = new ResourceLocation("texts/splashes.txt");

    private static final RandomSource RANDOM = RandomSource.create();

    private final List<String> splashes = Lists.newArrayList();

    private final User user;

    public SplashManager(User user0) {
        this.user = user0;
    }

    protected List<String> prepare(ResourceManager resourceManager0, ProfilerFiller profilerFiller1) {
        try {
            BufferedReader $$2 = Minecraft.getInstance().getResourceManager().m_215597_(SPLASHES_LOCATION);
            List var4;
            try {
                var4 = (List) $$2.lines().map(String::trim).filter(p_118876_ -> p_118876_.hashCode() != 125780783).collect(Collectors.toList());
            } catch (Throwable var7) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var4;
        } catch (IOException var8) {
            return Collections.emptyList();
        }
    }

    protected void apply(List<String> listString0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2) {
        this.splashes.clear();
        this.splashes.addAll(listString0);
    }

    @Nullable
    public SplashRenderer getSplash() {
        Calendar $$0 = Calendar.getInstance();
        $$0.setTime(new Date());
        if ($$0.get(2) + 1 == 12 && $$0.get(5) == 24) {
            return SplashRenderer.CHRISTMAS;
        } else if ($$0.get(2) + 1 == 1 && $$0.get(5) == 1) {
            return SplashRenderer.NEW_YEAR;
        } else if ($$0.get(2) + 1 == 10 && $$0.get(5) == 31) {
            return SplashRenderer.HALLOWEEN;
        } else if (this.splashes.isEmpty()) {
            return null;
        } else {
            return this.user != null && RANDOM.nextInt(this.splashes.size()) == 42 ? new SplashRenderer(this.user.getName().toUpperCase(Locale.ROOT) + " IS YOU") : new SplashRenderer((String) this.splashes.get(RANDOM.nextInt(this.splashes.size())));
        }
    }
}