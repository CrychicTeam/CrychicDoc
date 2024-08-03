package dev.xkmc.l2complements.content.enchantment.digging;

import dev.xkmc.l2complements.init.data.LCConfig;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DelayedBlockBreaker {

    private final ServerPlayer player;

    private final Level level;

    private final ItemStack stack;

    private final List<BlockPos> list;

    private int count = 0;

    public DelayedBlockBreaker(ServerPlayer player, List<BlockPos> list) {
        this.player = player;
        this.level = player.m_9236_();
        this.stack = player.m_21205_();
        this.list = list;
    }

    private boolean check() {
        return this.player.m_6084_() && this.player.m_9236_() == this.level && this.player.m_21205_() == this.stack;
    }

    public boolean tick() {
        if (!this.check()) {
            return true;
        } else {
            RangeDiggingEnchantment.execute(this.player, () -> {
                int n = LCConfig.COMMON.chainDiggingBlockPerTick.get();
                for (int i = 0; i < n; i++) {
                    this.player.gameMode.destroyBlock((BlockPos) this.list.get(this.count));
                    this.count++;
                    if (this.count >= this.list.size()) {
                        return;
                    }
                }
            });
            return this.count >= this.list.size();
        }
    }
}