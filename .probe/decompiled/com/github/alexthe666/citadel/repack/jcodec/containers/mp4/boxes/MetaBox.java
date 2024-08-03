package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MetaBox extends NodeBox {

    private static final String FOURCC = "meta";

    public MetaBox(Header atom) {
        super(atom);
    }

    public static MetaBox createMetaBox() {
        return new MetaBox(Header.createHeader(fourcc(), 0L));
    }

    public Map<String, MetaValue> getKeyedMeta() {
        Map<String, MetaValue> result = new LinkedHashMap();
        IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());
        MdtaBox[] keys = NodeBox.findAllPath(this, MdtaBox.class, new String[] { KeysBox.fourcc(), MdtaBox.fourcc() });
        if (ilst != null && keys.length != 0) {
            for (Entry<Integer, List<Box>> entry : ilst.getValues().entrySet()) {
                Integer index = (Integer) entry.getKey();
                if (index != null) {
                    DataBox db = this.getDataBox((List<Box>) entry.getValue());
                    if (db != null) {
                        MetaValue value = MetaValue.createOtherWithLocale(db.getType(), db.getLocale(), db.getData());
                        if (index > 0 && index <= keys.length) {
                            result.put(keys[index - 1].getKey(), value);
                        }
                    }
                }
            }
            return result;
        } else {
            return result;
        }
    }

    private DataBox getDataBox(List<Box> value) {
        for (Box box : value) {
            if (box instanceof DataBox) {
                return (DataBox) box;
            }
        }
        return null;
    }

    public Map<Integer, MetaValue> getItunesMeta() {
        Map<Integer, MetaValue> result = new LinkedHashMap();
        IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());
        if (ilst == null) {
            return result;
        } else {
            for (Entry<Integer, List<Box>> entry : ilst.getValues().entrySet()) {
                Integer index = (Integer) entry.getKey();
                if (index != null) {
                    DataBox db = this.getDataBox((List<Box>) entry.getValue());
                    if (db != null) {
                        MetaValue value = MetaValue.createOtherWithLocale(db.getType(), db.getLocale(), db.getData());
                        result.put(index, value);
                    }
                }
            }
            return result;
        }
    }

    public void setKeyedMeta(Map<String, MetaValue> map) {
        if (!map.isEmpty()) {
            KeysBox keys = KeysBox.createKeysBox();
            Map<Integer, List<Box>> data = new LinkedHashMap();
            int i = 1;
            for (Entry<String, MetaValue> entry : map.entrySet()) {
                keys.add(MdtaBox.createMdtaBox((String) entry.getKey()));
                MetaValue v = (MetaValue) entry.getValue();
                List<Box> children = new ArrayList();
                children.add(DataBox.createDataBox(v.getType(), v.getLocale(), v.getData()));
                data.put(i, children);
                i++;
            }
            IListBox ilst = IListBox.createIListBox(data);
            this.replaceBox(keys);
            this.replaceBox(ilst);
        }
    }

    public void setItunesMeta(Map<Integer, MetaValue> map) {
        if (!map.isEmpty()) {
            Map<Integer, MetaValue> copy = new LinkedHashMap();
            copy.putAll(map);
            IListBox ilst = NodeBox.findFirst(this, IListBox.class, IListBox.fourcc());
            Map<Integer, List<Box>> data;
            if (ilst == null) {
                data = new LinkedHashMap();
            } else {
                data = ilst.getValues();
                for (Entry<Integer, List<Box>> entry : data.entrySet()) {
                    int index = (Integer) entry.getKey();
                    MetaValue v = (MetaValue) copy.get(index);
                    if (v != null) {
                        DataBox dataBox = DataBox.createDataBox(v.getType(), v.getLocale(), v.getData());
                        this.dropChildBox((List<Box>) entry.getValue(), DataBox.fourcc());
                        ((List) entry.getValue()).add(dataBox);
                        copy.remove(index);
                    }
                }
            }
            for (Entry<Integer, MetaValue> entryx : copy.entrySet()) {
                int index = (Integer) entryx.getKey();
                MetaValue v = (MetaValue) entryx.getValue();
                DataBox dataBox = DataBox.createDataBox(v.getType(), v.getLocale(), v.getData());
                List<Box> children = new ArrayList();
                data.put(index, children);
                children.add(dataBox);
            }
            Set<Integer> keySet = new HashSet(data.keySet());
            keySet.removeAll(map.keySet());
            for (Integer dropped : keySet) {
                data.remove(dropped);
            }
            this.replaceBox(IListBox.createIListBox(data));
        }
    }

    private void dropChildBox(List<Box> children, String fourcc2) {
        ListIterator<Box> listIterator = children.listIterator();
        while (listIterator.hasNext()) {
            Box next = (Box) listIterator.next();
            if (fourcc2.equals(next.getFourcc())) {
                listIterator.remove();
            }
        }
    }

    public static String fourcc() {
        return "meta";
    }
}