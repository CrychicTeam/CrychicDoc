package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.util.HashMap;
import java.util.Map;

public class ColorUtil {

    private static Map<ColorSpace, Map<ColorSpace, Transform>> map = new HashMap();

    public static Transform getTransform(ColorSpace from, ColorSpace to) {
        Map<ColorSpace, Transform> map2 = (Map<ColorSpace, Transform>) map.get(from);
        return map2 == null ? null : (Transform) map2.get(to);
    }

    static {
        Map<ColorSpace, Transform> rgb = new HashMap();
        rgb.put(ColorSpace.RGB, new ColorUtil.Idential());
        rgb.put(ColorSpace.YUV420J, new RgbToYuv420j());
        rgb.put(ColorSpace.YUV420, new RgbToYuv420p());
        rgb.put(ColorSpace.YUV422, new RgbToYuv422p());
        map.put(ColorSpace.RGB, rgb);
        Map<ColorSpace, Transform> yuv420 = new HashMap();
        yuv420.put(ColorSpace.YUV420, new ColorUtil.Idential());
        yuv420.put(ColorSpace.YUV422, new Yuv420pToYuv422p());
        yuv420.put(ColorSpace.RGB, new Yuv420pToRgb());
        yuv420.put(ColorSpace.YUV420J, new ColorUtil.Idential());
        map.put(ColorSpace.YUV420, yuv420);
        Map<ColorSpace, Transform> yuv422 = new HashMap();
        yuv422.put(ColorSpace.YUV422, new ColorUtil.Idential());
        yuv422.put(ColorSpace.YUV420, new Yuv422pToYuv420p());
        yuv422.put(ColorSpace.YUV420J, new Yuv422pToYuv420p());
        yuv422.put(ColorSpace.RGB, new Yuv422pToRgb());
        map.put(ColorSpace.YUV422, yuv422);
        Map<ColorSpace, Transform> yuv444 = new HashMap();
        yuv444.put(ColorSpace.YUV444, new ColorUtil.Idential());
        map.put(ColorSpace.YUV444, yuv444);
        Map<ColorSpace, Transform> yuv444j = new HashMap();
        yuv444j.put(ColorSpace.YUV444J, new ColorUtil.Idential());
        yuv444j.put(ColorSpace.YUV420J, new Yuv444jToYuv420j());
        map.put(ColorSpace.YUV444J, yuv444j);
        Map<ColorSpace, Transform> yuv420j = new HashMap();
        yuv420j.put(ColorSpace.YUV420J, new ColorUtil.Idential());
        yuv420j.put(ColorSpace.YUV422, new Yuv420pToYuv422p());
        yuv420j.put(ColorSpace.RGB, new Yuv420jToRgb());
        yuv420j.put(ColorSpace.YUV420, new ColorUtil.Idential());
        map.put(ColorSpace.YUV420J, yuv420j);
    }

    public static class Idential implements Transform {

        @Override
        public void transform(Picture src, Picture dst) {
            for (int i = 0; i < Math.min(src.getData().length, dst.getData().length); i++) {
                System.arraycopy(src.getPlaneData(i), 0, dst.getPlaneData(i), 0, Math.min(src.getPlaneData(i).length, dst.getPlaneData(i).length));
            }
            byte[][] srcLowBits = src.getLowBits();
            byte[][] dstLowBits = dst.getLowBits();
            if (srcLowBits != null && dstLowBits != null) {
                for (int i = 0; i < Math.min(src.getData().length, dst.getData().length); i++) {
                    System.arraycopy(srcLowBits[i], 0, dstLowBits[i], 0, Math.min(src.getPlaneData(i).length, dst.getPlaneData(i).length));
                }
            }
        }
    }
}