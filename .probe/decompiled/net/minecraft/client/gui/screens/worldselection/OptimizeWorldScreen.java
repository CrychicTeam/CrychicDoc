package net.minecraft.client.gui.screens.worldselection;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.WorldStem;
import net.minecraft.util.Mth;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.slf4j.Logger;

public class OptimizeWorldScreen extends Screen {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Object2IntMap<ResourceKey<Level>> DIMENSION_COLORS = Util.make(new Object2IntOpenCustomHashMap(Util.identityStrategy()), p_101324_ -> {
        p_101324_.put(Level.OVERWORLD, -13408734);
        p_101324_.put(Level.NETHER, -10075085);
        p_101324_.put(Level.END, -8943531);
        p_101324_.defaultReturnValue(-2236963);
    });

    private final BooleanConsumer callback;

    private final WorldUpgrader upgrader;

    @Nullable
    public static OptimizeWorldScreen create(Minecraft minecraft0, BooleanConsumer booleanConsumer1, DataFixer dataFixer2, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess3, boolean boolean4) {
        try {
            OptimizeWorldScreen var8;
            try (WorldStem $$5 = minecraft0.createWorldOpenFlows().loadWorldStem(levelStorageSourceLevelStorageAccess3, false)) {
                WorldData $$6 = $$5.worldData();
                RegistryAccess.Frozen $$7 = $$5.registries().compositeAccess();
                levelStorageSourceLevelStorageAccess3.saveDataTag($$7, $$6);
                var8 = new OptimizeWorldScreen(booleanConsumer1, dataFixer2, levelStorageSourceLevelStorageAccess3, $$6.getLevelSettings(), boolean4, $$7.m_175515_(Registries.LEVEL_STEM));
            }
            return var8;
        } catch (Exception var11) {
            LOGGER.warn("Failed to load datapacks, can't optimize world", var11);
            return null;
        }
    }

    private OptimizeWorldScreen(BooleanConsumer booleanConsumer0, DataFixer dataFixer1, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess2, LevelSettings levelSettings3, boolean boolean4, Registry<LevelStem> registryLevelStem5) {
        super(Component.translatable("optimizeWorld.title", levelSettings3.levelName()));
        this.callback = booleanConsumer0;
        this.upgrader = new WorldUpgrader(levelStorageSourceLevelStorageAccess2, dataFixer1, registryLevelStem5, boolean4);
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_101322_ -> {
            this.upgrader.cancel();
            this.callback.accept(false);
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 150, 200, 20).build());
    }

    @Override
    public void tick() {
        if (this.upgrader.isFinished()) {
            this.callback.accept(true);
        }
    }

    @Override
    public void onClose() {
        this.callback.accept(false);
    }

    @Override
    public void removed() {
        this.upgrader.cancel();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        int $$4 = this.f_96543_ / 2 - 150;
        int $$5 = this.f_96543_ / 2 + 150;
        int $$6 = this.f_96544_ / 4 + 100;
        int $$7 = $$6 + 10;
        guiGraphics0.drawCenteredString(this.f_96547_, this.upgrader.getStatus(), this.f_96543_ / 2, $$6 - 9 - 2, 10526880);
        if (this.upgrader.getTotalChunks() > 0) {
            guiGraphics0.fill($$4 - 1, $$6 - 1, $$5 + 1, $$7 + 1, -16777216);
            guiGraphics0.drawString(this.f_96547_, Component.translatable("optimizeWorld.info.converted", this.upgrader.getConverted()), $$4, 40, 10526880);
            guiGraphics0.drawString(this.f_96547_, Component.translatable("optimizeWorld.info.skipped", this.upgrader.getSkipped()), $$4, 40 + 9 + 3, 10526880);
            guiGraphics0.drawString(this.f_96547_, Component.translatable("optimizeWorld.info.total", this.upgrader.getTotalChunks()), $$4, 40 + (9 + 3) * 2, 10526880);
            int $$8 = 0;
            for (ResourceKey<Level> $$9 : this.upgrader.levels()) {
                int $$10 = Mth.floor(this.upgrader.dimensionProgress($$9) * (float) ($$5 - $$4));
                guiGraphics0.fill($$4 + $$8, $$6, $$4 + $$8 + $$10, $$7, DIMENSION_COLORS.getInt($$9));
                $$8 += $$10;
            }
            int $$11 = this.upgrader.getConverted() + this.upgrader.getSkipped();
            guiGraphics0.drawCenteredString(this.f_96547_, $$11 + " / " + this.upgrader.getTotalChunks(), this.f_96543_ / 2, $$6 + 2 * 9 + 2, 10526880);
            guiGraphics0.drawCenteredString(this.f_96547_, Mth.floor(this.upgrader.getProgress() * 100.0F) + "%", this.f_96543_ / 2, $$6 + ($$7 - $$6) / 2 - 9 / 2, 10526880);
        }
        super.render(guiGraphics0, int1, int2, float3);
    }
}