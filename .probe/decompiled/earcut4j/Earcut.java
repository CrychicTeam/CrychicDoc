package earcut4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Earcut {

    private Earcut() {
    }

    public static List<Integer> earcut(double[] data) {
        return earcut(data, null, 2);
    }

    public static List<Integer> earcut(double[] data, int[] holeIndices, int dim) {
        boolean hasHoles = holeIndices != null && holeIndices.length > 0;
        int outerLen = hasHoles ? holeIndices[0] * dim : data.length;
        Earcut.Node outerNode = linkedList(data, 0, outerLen, dim, true);
        List<Integer> triangles = new ArrayList();
        if (outerNode != null && outerNode.next != outerNode.prev) {
            double minX = 0.0;
            double minY = 0.0;
            double maxX = 0.0;
            double maxY = 0.0;
            double invSize = Double.MIN_VALUE;
            if (hasHoles) {
                outerNode = eliminateHoles(data, holeIndices, outerNode, dim);
            }
            if (data.length > 80 * dim) {
                minX = maxX = data[0];
                minY = maxY = data[1];
                for (int i = dim; i < outerLen; i += dim) {
                    double x = data[i];
                    double y = data[i + 1];
                    if (x < minX) {
                        minX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
                invSize = Math.max(maxX - minX, maxY - minY);
                invSize = invSize != 0.0 ? 1.0 / invSize : 0.0;
            }
            earcutLinked(outerNode, triangles, dim, minX, minY, invSize, Integer.MIN_VALUE);
            return triangles;
        } else {
            return triangles;
        }
    }

    private static void earcutLinked(Earcut.Node ear, List<Integer> triangles, int dim, double minX, double minY, double invSize, int pass) {
        if (ear != null) {
            if (pass == Integer.MIN_VALUE && invSize != Double.MIN_VALUE) {
                indexCurve(ear, minX, minY, invSize);
            }
            Earcut.Node stop = ear;
            while (ear.prev != ear.next) {
                Earcut.Node prev = ear.prev;
                Earcut.Node next = ear.next;
                if (invSize != Double.MIN_VALUE ? !isEarHashed(ear, minX, minY, invSize) : !isEar(ear)) {
                    ear = next;
                    if (next == stop) {
                        if (pass == Integer.MIN_VALUE) {
                            earcutLinked(filterPoints(next, null), triangles, dim, minX, minY, invSize, 1);
                        } else if (pass == 1) {
                            ear = cureLocalIntersections(filterPoints(next, null), triangles, dim);
                            earcutLinked(ear, triangles, dim, minX, minY, invSize, 2);
                        } else if (pass == 2) {
                            splitEarcut(next, triangles, dim, minX, minY, invSize);
                        }
                        break;
                    }
                } else {
                    triangles.add(prev.i / dim);
                    triangles.add(ear.i / dim);
                    triangles.add(next.i / dim);
                    removeNode(ear);
                    ear = next.next;
                    stop = next.next;
                }
            }
        }
    }

    private static void splitEarcut(Earcut.Node start, List<Integer> triangles, int dim, double minX, double minY, double size) {
        Earcut.Node a = start;
        do {
            for (Earcut.Node b = a.next.next; b != a.prev; b = b.next) {
                if (a.i != b.i && isValidDiagonal(a, b)) {
                    Earcut.Node c = splitPolygon(a, b);
                    a = filterPoints(a, a.next);
                    c = filterPoints(c, c.next);
                    earcutLinked(a, triangles, dim, minX, minY, size, Integer.MIN_VALUE);
                    earcutLinked(c, triangles, dim, minX, minY, size, Integer.MIN_VALUE);
                    return;
                }
            }
            a = a.next;
        } while (a != start);
    }

    private static boolean isValidDiagonal(Earcut.Node a, Earcut.Node b) {
        return a.next.i != b.i && a.prev.i != b.i && !intersectsPolygon(a, b) && (locallyInside(a, b) && locallyInside(b, a) && middleInside(a, b) && (area(a.prev, a, b.prev) != 0.0 || area(a, b.prev, b) != 0.0) || equals(a, b) && area(a.prev, a, a.next) > 0.0 && area(b.prev, b, b.next) > 0.0);
    }

    private static boolean middleInside(Earcut.Node a, Earcut.Node b) {
        Earcut.Node p = a;
        boolean inside = false;
        double px = (a.x + b.x) / 2.0;
        double py = (a.y + b.y) / 2.0;
        do {
            if (p.y > py != p.next.y > py && px < (p.next.x - p.x) * (py - p.y) / (p.next.y - p.y) + p.x) {
                inside = !inside;
            }
            p = p.next;
        } while (p != a);
        return inside;
    }

    private static boolean intersectsPolygon(Earcut.Node a, Earcut.Node b) {
        Earcut.Node p = a;
        while (p.i == a.i || p.next.i == a.i || p.i == b.i || p.next.i == b.i || !intersects(p, p.next, a, b)) {
            p = p.next;
            if (p == a) {
                return false;
            }
        }
        return true;
    }

    private static boolean intersects(Earcut.Node p1, Earcut.Node q1, Earcut.Node p2, Earcut.Node q2) {
        if ((!equals(p1, p2) || !equals(q1, q2)) && (!equals(p1, q2) || !equals(p2, q1))) {
            double o1 = sign(area(p1, q1, p2));
            double o2 = sign(area(p1, q1, q2));
            double o3 = sign(area(p2, q2, p1));
            double o4 = sign(area(p2, q2, q1));
            if (o1 != o2 && o3 != o4) {
                return true;
            } else if (o1 == 0.0 && onSegment(p1, p2, q1)) {
                return true;
            } else if (o2 == 0.0 && onSegment(p1, q2, q1)) {
                return true;
            } else {
                return o3 == 0.0 && onSegment(p2, p1, q2) ? true : o4 == 0.0 && onSegment(p2, q1, q2);
            }
        } else {
            return true;
        }
    }

    private static boolean onSegment(Earcut.Node p, Earcut.Node q, Earcut.Node r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    private static double sign(double num) {
        return num > 0.0 ? 1.0 : (num < 0.0 ? -1.0 : 0.0);
    }

    private static Earcut.Node cureLocalIntersections(Earcut.Node start, List<Integer> triangles, int dim) {
        Earcut.Node p = start;
        do {
            Earcut.Node a = p.prev;
            Earcut.Node b = p.next.next;
            if (!equals(a, b) && intersects(a, p, p.next, b) && locallyInside(a, b) && locallyInside(b, a)) {
                triangles.add(a.i / dim);
                triangles.add(p.i / dim);
                triangles.add(b.i / dim);
                removeNode(p);
                removeNode(p.next);
                start = b;
                p = b;
            }
            p = p.next;
        } while (p != start);
        return filterPoints(p, null);
    }

    private static boolean isEar(Earcut.Node ear) {
        Earcut.Node a = ear.prev;
        Earcut.Node b = ear;
        Earcut.Node c = ear.next;
        if (area(a, ear, c) >= 0.0) {
            return false;
        } else {
            for (Earcut.Node p = ear.next.next; p != ear.prev; p = p.next) {
                if (pointInTriangle(a.x, a.y, b.x, b.y, c.x, c.y, p.x, p.y) && area(p.prev, p, p.next) >= 0.0) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean isEarHashed(Earcut.Node ear, double minX, double minY, double invSize) {
        Earcut.Node a = ear.prev;
        Earcut.Node b = ear;
        Earcut.Node c = ear.next;
        if (area(a, ear, c) >= 0.0) {
            return false;
        } else {
            double minTX = a.x < ear.x ? (a.x < c.x ? a.x : c.x) : (ear.x < c.x ? ear.x : c.x);
            double minTY = a.y < ear.y ? (a.y < c.y ? a.y : c.y) : (ear.y < c.y ? ear.y : c.y);
            double maxTX = a.x > ear.x ? (a.x > c.x ? a.x : c.x) : (ear.x > c.x ? ear.x : c.x);
            double maxTY = a.y > ear.y ? (a.y > c.y ? a.y : c.y) : (ear.y > c.y ? ear.y : c.y);
            double minZ = zOrder(minTX, minTY, minX, minY, invSize);
            double maxZ = zOrder(maxTX, maxTY, minX, minY, invSize);
            Earcut.Node p = ear.prevZ;
            Earcut.Node n;
            for (n = ear.nextZ; p != null && p.z >= minZ && n != null && n.z <= maxZ; n = n.nextZ) {
                if (p != ear.prev && p != ear.next && pointInTriangle(a.x, a.y, b.x, b.y, c.x, c.y, p.x, p.y) && area(p.prev, p, p.next) >= 0.0) {
                    return false;
                }
                p = p.prevZ;
                if (n != ear.prev && n != ear.next && pointInTriangle(a.x, a.y, b.x, b.y, c.x, c.y, n.x, n.y) && area(n.prev, n, n.next) >= 0.0) {
                    return false;
                }
            }
            while (p != null && p.z >= minZ) {
                if (p != ear.prev && p != ear.next && pointInTriangle(a.x, a.y, b.x, b.y, c.x, c.y, p.x, p.y) && area(p.prev, p, p.next) >= 0.0) {
                    return false;
                }
                p = p.prevZ;
            }
            while (n != null && n.z <= maxZ) {
                if (n != ear.prev && n != ear.next && pointInTriangle(a.x, a.y, b.x, b.y, c.x, c.y, n.x, n.y) && area(n.prev, n, n.next) >= 0.0) {
                    return false;
                }
                n = n.nextZ;
            }
            return true;
        }
    }

    private static double zOrder(double x, double y, double minX, double minY, double invSize) {
        int lx = Double.valueOf(32767.0 * (x - minX) * invSize).intValue();
        int ly = Double.valueOf(32767.0 * (y - minY) * invSize).intValue();
        lx = (lx | lx << 8) & 16711935;
        lx = (lx | lx << 4) & 252645135;
        lx = (lx | lx << 2) & 858993459;
        lx = (lx | lx << 1) & 1431655765;
        ly = (ly | ly << 8) & 16711935;
        ly = (ly | ly << 4) & 252645135;
        ly = (ly | ly << 2) & 858993459;
        ly = (ly | ly << 1) & 1431655765;
        return (double) (lx | ly << 1);
    }

    private static void indexCurve(Earcut.Node start, double minX, double minY, double invSize) {
        Earcut.Node p = start;
        do {
            if (p.z == Double.MIN_VALUE) {
                p.z = zOrder(p.x, p.y, minX, minY, invSize);
            }
            p.prevZ = p.prev;
            p.nextZ = p.next;
            p = p.next;
        } while (p != start);
        p.prevZ.nextZ = null;
        p.prevZ = null;
        sortLinked(p);
    }

    private static Earcut.Node sortLinked(Earcut.Node list) {
        int inSize = 1;
        int numMerges;
        do {
            Earcut.Node p = list;
            list = null;
            Earcut.Node tail = null;
            numMerges = 0;
            while (p != null) {
                numMerges++;
                Earcut.Node q = p;
                int pSize = 0;
                for (int i = 0; i < inSize; i++) {
                    pSize++;
                    q = q.nextZ;
                    if (q == null) {
                        break;
                    }
                }
                int qSize = inSize;
                while (pSize > 0 || qSize > 0 && q != null) {
                    Earcut.Node e;
                    if (pSize == 0) {
                        e = q;
                        q = q.nextZ;
                        qSize--;
                    } else if (qSize == 0 || q == null) {
                        e = p;
                        p = p.nextZ;
                        pSize--;
                    } else if (p.z <= q.z) {
                        e = p;
                        p = p.nextZ;
                        pSize--;
                    } else {
                        e = q;
                        q = q.nextZ;
                        qSize--;
                    }
                    if (tail != null) {
                        tail.nextZ = e;
                    } else {
                        list = e;
                    }
                    e.prevZ = tail;
                    tail = e;
                }
                p = q;
            }
            tail.nextZ = null;
            inSize *= 2;
        } while (numMerges > 1);
        return list;
    }

    private static Earcut.Node eliminateHoles(double[] data, int[] holeIndices, Earcut.Node outerNode, int dim) {
        List<Earcut.Node> queue = new ArrayList();
        int len = holeIndices.length;
        for (int i = 0; i < len; i++) {
            int start = holeIndices[i] * dim;
            int end = i < len - 1 ? holeIndices[i + 1] * dim : data.length;
            Earcut.Node list = linkedList(data, start, end, dim, false);
            if (list == list.next) {
                list.steiner = true;
            }
            queue.add(getLeftmost(list));
        }
        queue.sort(new Comparator<Earcut.Node>() {

            public int compare(Earcut.Node o1, Earcut.Node o2) {
                if (o1.x - o2.x > 0.0) {
                    return 1;
                } else {
                    return o1.x - o2.x < 0.0 ? -2 : 0;
                }
            }
        });
        for (Earcut.Node node : queue) {
            eliminateHole(node, outerNode);
            outerNode = filterPoints(outerNode, outerNode.next);
        }
        return outerNode;
    }

    private static Earcut.Node filterPoints(Earcut.Node start, Earcut.Node end) {
        if (start == null) {
            return start;
        } else {
            if (end == null) {
                end = start;
            }
            Earcut.Node p = start;
            boolean again;
            do {
                again = false;
                if ((p.steiner || !equals(p, p.next)) && area(p.prev, p, p.next) != 0.0) {
                    p = p.next;
                } else {
                    removeNode(p);
                    end = p.prev;
                    p = p.prev;
                    if (p == p.next) {
                        break;
                    }
                    again = true;
                }
            } while (again || p != end);
            return end;
        }
    }

    private static boolean equals(Earcut.Node p1, Earcut.Node p2) {
        return p1.x == p2.x && p1.y == p2.y;
    }

    private static double area(Earcut.Node p, Earcut.Node q, Earcut.Node r) {
        return (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
    }

    private static void eliminateHole(Earcut.Node hole, Earcut.Node outerNode) {
        outerNode = findHoleBridge(hole, outerNode);
        if (outerNode != null) {
            Earcut.Node b = splitPolygon(outerNode, hole);
            filterPoints(outerNode, outerNode.next);
            filterPoints(b, b.next);
        }
    }

    private static Earcut.Node splitPolygon(Earcut.Node a, Earcut.Node b) {
        Earcut.Node a2 = new Earcut.Node(a.i, a.x, a.y);
        Earcut.Node b2 = new Earcut.Node(b.i, b.x, b.y);
        Earcut.Node an = a.next;
        Earcut.Node bp = b.prev;
        a.next = b;
        b.prev = a;
        a2.next = an;
        an.prev = a2;
        b2.next = a2;
        a2.prev = b2;
        bp.next = b2;
        b2.prev = bp;
        return b2;
    }

    private static Earcut.Node findHoleBridge(Earcut.Node hole, Earcut.Node outerNode) {
        Earcut.Node p = outerNode;
        double hx = hole.x;
        double hy = hole.y;
        double qx = -Double.MAX_VALUE;
        Earcut.Node m = null;
        do {
            if (hy <= p.y && hy >= p.next.y) {
                double x = p.x + (hy - p.y) * (p.next.x - p.x) / (p.next.y - p.y);
                if (x <= hx && x > qx) {
                    qx = x;
                    if (x == hx) {
                        if (hy == p.y) {
                            return p;
                        }
                        if (hy == p.next.y) {
                            return p.next;
                        }
                    }
                    m = p.x < p.next.x ? p : p.next;
                }
            }
            p = p.next;
        } while (p != outerNode);
        if (m == null) {
            return null;
        } else if (hx == qx) {
            return m;
        } else {
            Earcut.Node stop = m;
            double mx = m.x;
            double my = m.y;
            double tanMin = Double.MAX_VALUE;
            p = m;
            do {
                if (hx >= p.x && p.x >= mx && pointInTriangle(hy < my ? hx : qx, hy, mx, my, hy < my ? qx : hx, hy, p.x, p.y)) {
                    double tan = Math.abs(hy - p.y) / (hx - p.x);
                    if (locallyInside(p, hole) && (tan < tanMin || tan == tanMin && (p.x > m.x || p.x == m.x && sectorContainsSector(m, p)))) {
                        m = p;
                        tanMin = tan;
                    }
                }
                p = p.next;
            } while (p != stop);
            return m;
        }
    }

    private static boolean locallyInside(Earcut.Node a, Earcut.Node b) {
        return area(a.prev, a, a.next) < 0.0 ? area(a, b, a.next) >= 0.0 && area(a, a.prev, b) >= 0.0 : area(a, b, a.prev) < 0.0 || area(a, a.next, b) < 0.0;
    }

    private static boolean sectorContainsSector(Earcut.Node m, Earcut.Node p) {
        return area(m.prev, m, p.prev) < 0.0 && area(p.next, m, m.next) < 0.0;
    }

    private static boolean pointInTriangle(double ax, double ay, double bx, double by, double cx, double cy, double px, double py) {
        return (cx - px) * (ay - py) - (ax - px) * (cy - py) >= 0.0 && (ax - px) * (by - py) - (bx - px) * (ay - py) >= 0.0 && (bx - px) * (cy - py) - (cx - px) * (by - py) >= 0.0;
    }

    private static Earcut.Node getLeftmost(Earcut.Node start) {
        Earcut.Node p = start;
        Earcut.Node leftmost = start;
        do {
            if (p.x < leftmost.x || p.x == leftmost.x && p.y < leftmost.y) {
                leftmost = p;
            }
            p = p.next;
        } while (p != start);
        return leftmost;
    }

    private static Earcut.Node linkedList(double[] data, int start, int end, int dim, boolean clockwise) {
        Earcut.Node last = null;
        if (clockwise == signedArea(data, start, end, dim) > 0.0) {
            for (int i = start; i < end; i += dim) {
                last = insertNode(i, data[i], data[i + 1], last);
            }
        } else {
            for (int i = end - dim; i >= start; i -= dim) {
                last = insertNode(i, data[i], data[i + 1], last);
            }
        }
        if (last != null && equals(last, last.next)) {
            removeNode(last);
            last = last.next;
        }
        return last;
    }

    private static void removeNode(Earcut.Node p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        if (p.prevZ != null) {
            p.prevZ.nextZ = p.nextZ;
        }
        if (p.nextZ != null) {
            p.nextZ.prevZ = p.prevZ;
        }
    }

    private static Earcut.Node insertNode(int i, double x, double y, Earcut.Node last) {
        Earcut.Node p = new Earcut.Node(i, x, y);
        if (last == null) {
            p.prev = p;
            p.next = p;
        } else {
            p.next = last.next;
            p.prev = last;
            last.next.prev = p;
            last.next = p;
        }
        return p;
    }

    private static double signedArea(double[] data, int start, int end, int dim) {
        double sum = 0.0;
        int j = end - dim;
        for (int i = start; i < end; i += dim) {
            sum += (data[j] - data[i]) * (data[i + 1] + data[j + 1]);
            j = i;
        }
        return sum;
    }

    private static class Node {

        int i;

        double x;

        double y;

        double z;

        boolean steiner;

        Earcut.Node prev;

        Earcut.Node next;

        Earcut.Node prevZ;

        Earcut.Node nextZ;

        Node(int i, double x, double y) {
            this.i = i;
            this.x = x;
            this.y = y;
            this.prev = null;
            this.next = null;
            this.z = Double.MIN_VALUE;
            this.prevZ = null;
            this.nextZ = null;
            this.steiner = false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{i: ").append(this.i).append(", x: ").append(this.x).append(", y: ").append(this.y).append(", prev: ").append(this.toString(this.prev)).append(", next: ").append(this.toString(this.next)).append("}");
            return sb.toString();
        }

        public String toString(Earcut.Node node) {
            return node == null ? "null" : "{i: " + node.i + ", x: " + node.x + ", y: " + node.y + "}";
        }
    }
}