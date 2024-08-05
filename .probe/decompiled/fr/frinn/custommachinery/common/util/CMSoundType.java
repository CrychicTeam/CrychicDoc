package fr.frinn.custommachinery.common.util;

import com.mojang.datafixers.util.Either;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import fr.frinn.custommachinery.impl.codec.NamedRecordCodec;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.apache.commons.lang3.StringUtils;

public class CMSoundType extends SoundType {

    public static final CMSoundType DEFAULT = new CMSoundType(new PartialBlockState(Blocks.IRON_BLOCK));

    public static final NamedCodec<CMSoundType> FROM_STATE = PartialBlockState.CODEC.xmap(CMSoundType::new, type -> type.defaultBlock, "Sound type");

    public static final NamedMapCodec<CMSoundType> FROM_PARTS = NamedCodec.record(cmSoundTypeInstance -> cmSoundTypeInstance.group(NamedCodec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(SoundType::m_56773_), NamedCodec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(SoundType::m_56774_), partCodec("break", SoundType::m_56775_), partCodec("step", SoundType::m_56776_), partCodec("place", SoundType::m_56777_), partCodec("hit", SoundType::m_56778_), partCodec("fall", SoundType::m_56779_)).apply(cmSoundTypeInstance, CMSoundType::new), "Sound type");

    public static final NamedCodec<CMSoundType> CODEC = NamedCodec.either(FROM_STATE, FROM_PARTS, "Interaction sounds").xmap(either -> (CMSoundType) either.map(Function.identity(), Function.identity()), Either::right, "Interaction sounds");

    private final PartialBlockState defaultBlock;

    public CMSoundType(float volume, float pitch, SoundEvent breakSound, SoundEvent stepSound, SoundEvent placeSound, SoundEvent hitSound, SoundEvent fallSound) {
        super(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
        this.defaultBlock = PartialBlockState.AIR;
    }

    public CMSoundType(PartialBlockState state) {
        super(1.0F, 1.0F, state.getBlockState().m_60827_().getBreakSound(), state.getBlockState().m_60827_().getStepSound(), state.getBlockState().m_60827_().getPlaceSound(), state.getBlockState().m_60827_().getHitSound(), state.getBlockState().m_60827_().getFallSound());
        this.defaultBlock = state;
    }

    private static NamedRecordCodec<CMSoundType, SoundEvent> partCodec(String field, Function<SoundType, SoundEvent> typeToSound) {
        return NamedCodec.either(PartialBlockState.CODEC, DefaultCodecs.SOUND_EVENT, StringUtils.capitalize(field) + " Sound").<SoundEvent>xmap(either -> (SoundEvent) either.map(state -> (SoundEvent) typeToSound.apply(state.getBlockState().m_60827_()), Function.identity()), Either::right, StringUtils.capitalize(field) + " Sound").optionalFieldOf(field, (SoundEvent) typeToSound.apply(DEFAULT)).forGetter(typeToSound::apply);
    }
}