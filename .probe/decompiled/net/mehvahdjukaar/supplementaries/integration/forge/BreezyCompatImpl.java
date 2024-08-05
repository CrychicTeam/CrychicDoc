package net.mehvahdjukaar.supplementaries.integration.forge;

import codyhuh.breezy.common.network.BreezyNetworking;
import codyhuh.breezy.common.network.NewWindSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BreezyCompatImpl {

    public static float getWindDirection(BlockPos pos, Level level) {
        NewWindSavedData data = BreezyNetworking.CLIENT_CACHE;
        return data != null ? (float) data.getWindAtHeight(pos.m_123342_(), level) : 90.0F;
    }
}