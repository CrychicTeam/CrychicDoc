package net.minecraft.world.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class RecordItem extends Item {

    private static final Map<SoundEvent, RecordItem> BY_NAME = Maps.newHashMap();

    private final int analogOutput;

    private final SoundEvent sound;

    private final int lengthInTicks;

    protected RecordItem(int int0, SoundEvent soundEvent1, Item.Properties itemProperties2, int int3) {
        super(itemProperties2);
        this.analogOutput = int0;
        this.sound = soundEvent1;
        this.lengthInTicks = int3 * 20;
        BY_NAME.put(this.sound, this);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if ($$3.m_60713_(Blocks.JUKEBOX) && !(Boolean) $$3.m_61143_(JukeboxBlock.HAS_RECORD)) {
            ItemStack $$4 = useOnContext0.getItemInHand();
            if (!$$1.isClientSide) {
                Player $$5 = useOnContext0.getPlayer();
                if ($$1.getBlockEntity($$2) instanceof JukeboxBlockEntity $$6) {
                    $$6.m_272287_($$4.copy());
                    $$1.m_220407_(GameEvent.BLOCK_CHANGE, $$2, GameEvent.Context.of($$5, $$3));
                }
                $$4.shrink(1);
                if ($$5 != null) {
                    $$5.awardStat(Stats.PLAY_RECORD);
                }
            }
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public int getAnalogOutput() {
        return this.analogOutput;
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        listComponent2.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }

    public MutableComponent getDisplayName() {
        return Component.translatable(this.m_5524_() + ".desc");
    }

    @Nullable
    public static RecordItem getBySound(SoundEvent soundEvent0) {
        return (RecordItem) BY_NAME.get(soundEvent0);
    }

    public SoundEvent getSound() {
        return this.sound;
    }

    public int getLengthInTicks() {
        return this.lengthInTicks;
    }
}