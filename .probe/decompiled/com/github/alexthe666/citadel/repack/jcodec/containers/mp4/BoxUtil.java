package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class BoxUtil {

    public static Box parseBox(ByteBuffer input, Header childAtom, IBoxFactory factory) {
        Box box = factory.newBox(childAtom);
        if (childAtom.getBodySize() < 134217728L) {
            box.parse(input);
            return box;
        } else {
            return new Box.LeafBox(Header.createHeader("free", 8L));
        }
    }

    public static Box parseChildBox(ByteBuffer input, IBoxFactory factory) {
        ByteBuffer fork = input.duplicate();
        while (input.remaining() >= 4 && fork.getInt() == 0) {
            input.getInt();
        }
        if (input.remaining() < 4) {
            return null;
        } else {
            Header childAtom = Header.read(input);
            return childAtom != null && (long) input.remaining() >= childAtom.getBodySize() ? parseBox(NIOUtils.read(input, (int) childAtom.getBodySize()), childAtom, factory) : null;
        }
    }

    public static <T extends Box> T as(Class<T> class1, Box.LeafBox box) {
        try {
            T res = (T) Platform.newInstance(class1, new Object[] { box.getHeader() });
            res.parse(box.getData().duplicate());
            return res;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static boolean containsBox(NodeBox box, String path) {
        Box b = NodeBox.findFirstPath(box, Box.class, new String[] { path });
        return b != null;
    }

    public static boolean containsBox2(NodeBox box, String path1, String path2) {
        Box b = NodeBox.findFirstPath(box, Box.class, new String[] { path1, path2 });
        return b != null;
    }
}