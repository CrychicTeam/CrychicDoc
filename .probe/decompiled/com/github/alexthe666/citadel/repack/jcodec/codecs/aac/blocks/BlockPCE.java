package com.github.alexthe666.citadel.repack.jcodec.codecs.aac.blocks;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.ChannelPosition;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;

public class BlockPCE extends Block {

    private static final int MAX_ELEM_ID = 16;

    @Override
    public void parse(BitReader _in) {
        _in.readNBit(2);
        int samplingIndex = _in.readNBit(4);
        int num_front = _in.readNBit(4);
        int num_side = _in.readNBit(4);
        int num_back = _in.readNBit(4);
        int num_lfe = _in.readNBit(2);
        int num_assoc_data = _in.readNBit(3);
        int num_cc = _in.readNBit(4);
        if (_in.read1Bit() != 0) {
            _in.readNBit(4);
        }
        if (_in.read1Bit() != 0) {
            _in.readNBit(4);
        }
        if (_in.read1Bit() != 0) {
            _in.readNBit(3);
        }
        BlockPCE.ChannelMapping[] layout_map = new BlockPCE.ChannelMapping[64];
        int tags = 0;
        this.decodeChannelMap(layout_map, tags, ChannelPosition.AAC_CHANNEL_FRONT, _in, num_front);
        this.decodeChannelMap(layout_map, num_front, ChannelPosition.AAC_CHANNEL_SIDE, _in, num_side);
        tags = num_front + num_side;
        this.decodeChannelMap(layout_map, tags, ChannelPosition.AAC_CHANNEL_BACK, _in, num_back);
        tags += num_back;
        this.decodeChannelMap(layout_map, tags, ChannelPosition.AAC_CHANNEL_LFE, _in, num_lfe);
        tags += num_lfe;
        _in.skip(4 * num_assoc_data);
        this.decodeChannelMap(layout_map, tags, ChannelPosition.AAC_CHANNEL_CC, _in, num_cc);
        tags += num_cc;
        _in.align();
        int comment_len = _in.readNBit(8) * 8;
        _in.skip(comment_len);
    }

    private void decodeChannelMap(BlockPCE.ChannelMapping[] layout_map, int offset, ChannelPosition type, BitReader _in, int n) {
        while (n-- > 0) {
            RawDataBlockType syn_ele = null;
            switch(type) {
                case AAC_CHANNEL_FRONT:
                case AAC_CHANNEL_BACK:
                case AAC_CHANNEL_SIDE:
                    syn_ele = RawDataBlockType.values()[_in.read1Bit()];
                    break;
                case AAC_CHANNEL_CC:
                    _in.read1Bit();
                    syn_ele = RawDataBlockType.TYPE_CCE;
                    break;
                case AAC_CHANNEL_LFE:
                    syn_ele = RawDataBlockType.TYPE_LFE;
            }
            layout_map[offset].syn_ele = syn_ele;
            layout_map[offset].someInt = _in.readNBit(4);
            layout_map[offset].position = type;
            offset++;
        }
    }

    public static class ChannelMapping {

        RawDataBlockType syn_ele;

        int someInt;

        ChannelPosition position;
    }
}