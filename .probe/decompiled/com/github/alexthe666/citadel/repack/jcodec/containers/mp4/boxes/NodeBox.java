package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.IBoxFactory;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class NodeBox extends Box {

    protected List<Box> boxes = new LinkedList();

    protected IBoxFactory factory;

    public NodeBox(Header atom) {
        super(atom);
    }

    public void setFactory(IBoxFactory factory) {
        this.factory = factory;
    }

    @Override
    public void parse(ByteBuffer input) {
        while (input.remaining() >= 8) {
            Box child = parseChildBox(input, this.factory);
            if (child != null) {
                this.boxes.add(child);
            }
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
            Box ret = null;
            Header childAtom = Header.read(input);
            if (childAtom != null && (long) input.remaining() >= childAtom.getBodySize()) {
                ret = Box.parseBox(NIOUtils.read(input, (int) childAtom.getBodySize()), childAtom, factory);
            }
            return ret;
        }
    }

    public List<Box> getBoxes() {
        return this.boxes;
    }

    public void add(Box box) {
        this.boxes.add(box);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        for (Box box : this.boxes) {
            box.write(out);
        }
    }

    @Override
    public int estimateSize() {
        int total = 0;
        for (Box box : this.boxes) {
            total += box.estimateSize();
        }
        return total + Header.estimateHeaderSize(total);
    }

    public void addFirst(MovieHeaderBox box) {
        this.boxes.add(0, box);
    }

    public void replace(String fourcc, Box box) {
        this.removeChildren(new String[] { fourcc });
        this.add(box);
    }

    public void replaceBox(Box box) {
        this.removeChildren(new String[] { box.getFourcc() });
        this.add(box);
    }

    @Override
    protected void dump(StringBuilder sb) {
        sb.append("{\"tag\":\"" + this.header.getFourcc() + "\",");
        sb.append("\"boxes\": [");
        this.dumpBoxes(sb);
        sb.append("]");
        sb.append("}");
    }

    protected void dumpBoxes(StringBuilder sb) {
        for (int i = 0; i < this.boxes.size(); i++) {
            ((Box) this.boxes.get(i)).dump(sb);
            if (i < this.boxes.size() - 1) {
                sb.append(",");
            }
        }
    }

    public void removeChildren(String[] fourcc) {
        Iterator<Box> it = this.boxes.iterator();
        while (it.hasNext()) {
            Box box = (Box) it.next();
            String fcc = box.getFourcc();
            for (int i = 0; i < fourcc.length; i++) {
                String cand = fourcc[i];
                if (cand.equals(fcc)) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public static Box doCloneBox(Box box, int approxSize, IBoxFactory bf) {
        ByteBuffer buf = ByteBuffer.allocate(approxSize);
        box.write(buf);
        buf.flip();
        return parseChildBox(buf, bf);
    }

    public static Box cloneBox(Box box, int approxSize, IBoxFactory bf) {
        return doCloneBox(box, approxSize, bf);
    }

    public static <T extends Box> T[] findDeep(Box box, Class<T> class1, String name) {
        List<T> storage = new ArrayList();
        findDeepInner(box, class1, name, storage);
        return (T[]) storage.toArray((Box[]) Array.newInstance(class1, 0));
    }

    public static <T extends Box> void findDeepInner(Box box, Class<T> class1, String name, List<T> storage) {
        if (box != null) {
            if (name.equals(box.getHeader().getFourcc())) {
                storage.add(box);
            } else {
                if (box instanceof NodeBox) {
                    NodeBox nb = (NodeBox) box;
                    for (Box candidate : nb.getBoxes()) {
                        findDeepInner(candidate, class1, name, storage);
                    }
                }
            }
        }
    }

    public static <T extends Box> T[] findAll(Box box, Class<T> class1, String path) {
        return (T[]) findAllPath(box, class1, new String[] { path });
    }

    public static <T extends Box> T findFirst(NodeBox box, Class<T> clazz, String path) {
        return findFirstPath(box, clazz, new String[] { path });
    }

    public static <T extends Box> T findFirstPath(NodeBox box, Class<T> clazz, String[] path) {
        T[] result = (T[]) findAllPath(box, clazz, path);
        return result.length > 0 ? result[0] : null;
    }

    public static <T extends Box> T[] findAllPath(Box box, Class<T> class1, String[] path) {
        List<Box> result = new LinkedList();
        findBox(box, new ArrayList(Arrays.asList(path)), result);
        ListIterator<Box> it = result.listIterator();
        while (it.hasNext()) {
            Box next = (Box) it.next();
            if (next == null) {
                it.remove();
            } else if (!Platform.isAssignableFrom(class1, next.getClass())) {
                try {
                    it.set(Box.asBox(class1, next));
                } catch (Exception var7) {
                    Logger.warn("Failed to reinterpret box: " + next.getFourcc() + " as: " + class1.getName() + "." + var7.getMessage());
                    it.remove();
                }
            }
        }
        return (T[]) result.toArray((Box[]) Array.newInstance(class1, 0));
    }

    public static void findBox(Box root, List<String> path, Collection<Box> result) {
        if (path.size() > 0) {
            String head = (String) path.remove(0);
            if (root instanceof NodeBox) {
                NodeBox nb = (NodeBox) root;
                for (Box candidate : nb.getBoxes()) {
                    if (head == null || head.equals(candidate.header.getFourcc())) {
                        findBox(candidate, path, result);
                    }
                }
            }
            path.add(0, head);
        } else {
            result.add(root);
        }
    }
}