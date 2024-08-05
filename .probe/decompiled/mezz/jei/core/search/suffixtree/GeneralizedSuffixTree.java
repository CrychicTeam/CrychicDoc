package mezz.jei.core.search.suffixtree;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.function.Consumer;
import mezz.jei.core.search.ISearchStorage;
import mezz.jei.core.util.Pair;
import mezz.jei.core.util.SubString;
import org.jetbrains.annotations.Nullable;

public class GeneralizedSuffixTree<T> implements ISearchStorage<T> {

    private final RootNode<T> root = new RootNode<>();

    private Node<T> activeLeaf = this.root;

    @Override
    public void getSearchResults(String word, Consumer<Collection<T>> resultsConsumer) {
        Node<T> tmpNode = searchNode(this.root, word);
        if (tmpNode != null) {
            tmpNode.getData(resultsConsumer);
        }
    }

    @Override
    public void getAllElements(Consumer<Collection<T>> resultsConsumer) {
        this.root.getData(resultsConsumer);
    }

    @Nullable
    private static <T> Node<T> searchNode(Node<T> root, String word) {
        Node<T> currentNode = root;
        SubString wordSubString = new SubString(word);
        while (!wordSubString.isEmpty()) {
            Edge<T> currentEdge = currentNode.getEdge(wordSubString);
            if (currentEdge == null) {
                return null;
            }
            int lenToMatch = Math.min(wordSubString.length(), currentEdge.length());
            if (!currentEdge.regionMatches(wordSubString, lenToMatch)) {
                return null;
            }
            if (lenToMatch == wordSubString.length()) {
                return currentEdge.getDest();
            }
            currentNode = currentEdge.getDest();
            wordSubString = wordSubString.substring(lenToMatch);
        }
        return null;
    }

    @Override
    public void put(String key, T value) {
        this.activeLeaf = this.root;
        Node<T> s = this.root;
        SubString text = new SubString(key, 0, 0);
        for (int i = 0; i < key.length(); i++) {
            SubString rest = new SubString(key, i);
            Pair<Node<T>, SubString> active = this.update(s, text, key.charAt(i), rest, value);
            s = active.first();
            text = active.second();
        }
        if (null == this.activeLeaf.getSuffix() && this.activeLeaf != this.root && this.activeLeaf != s) {
            this.activeLeaf.setSuffix(s);
        }
    }

    private static <T> Pair<Boolean, Node<T>> testAndSplit(Node<T> startNode, SubString searchString, char t, SubString remainder, T value) {
        assert !remainder.isEmpty();
        assert remainder.charAt(0) == t;
        Pair<Node<T>, SubString> canonizeResult = canonize(startNode, searchString);
        startNode = canonizeResult.first();
        searchString = canonizeResult.second();
        if (!searchString.isEmpty()) {
            Edge<T> g = startNode.getEdge(searchString);
            assert g != null;
            if (g.length() > searchString.length() && g.charAt(searchString.length()) == t) {
                return new Pair<>(true, startNode);
            } else {
                Node<T> newNode = splitNode(startNode, g, searchString);
                return new Pair<>(false, newNode);
            }
        } else {
            Edge<T> e = startNode.getEdge(remainder);
            if (e == null) {
                return new Pair<>(false, startNode);
            } else if (e.startsWith(remainder)) {
                if (e.length() == remainder.length()) {
                    Node<T> dest = e.getDest();
                    dest.addRef(value);
                    return new Pair<>(true, startNode);
                } else {
                    Node<T> newNode = splitNode(startNode, e, remainder);
                    newNode.addRef(value);
                    return new Pair<>(false, startNode);
                }
            } else {
                return new Pair<>(true, startNode);
            }
        }
    }

    private static <T> Node<T> splitNode(Node<T> s, Edge<T> e, SubString splitFirstPart) {
        assert e == s.getEdge(splitFirstPart);
        assert e.startsWith(splitFirstPart);
        assert e.length() > splitFirstPart.length();
        SubString splitSecondPart = e.substring(splitFirstPart.length());
        Node<T> r = new Node<>();
        s.addEdge(new Edge<>(splitFirstPart, r));
        r.addEdge(new Edge<>(splitSecondPart, e.getDest()));
        return r;
    }

    private static <T> Pair<Node<T>, SubString> canonize(Node<T> s, SubString input) {
        Node<T> currentNode = s;
        SubString remainder = input;
        while (!remainder.isEmpty()) {
            Edge<T> nextEdge = currentNode.getEdge(remainder);
            if (nextEdge == null || !nextEdge.isPrefix(remainder)) {
                break;
            }
            currentNode = nextEdge.getDest();
            remainder = remainder.substring(nextEdge.length());
        }
        return new Pair<>(currentNode, remainder);
    }

    private Pair<Node<T>, SubString> update(Node<T> s, SubString stringPart, char newChar, SubString rest, T value) {
        assert !rest.isEmpty();
        assert rest.charAt(0) == newChar;
        SubString k = stringPart.append(newChar);
        Node<T> oldRoot = this.root;
        Pair<Boolean, Node<T>> ret = testAndSplit(s, stringPart, newChar, rest, value);
        Node<T> r = ret.second();
        boolean endpoint = ret.first();
        while (!endpoint) {
            Edge<T> tempEdge = r.getEdge(newChar);
            Node<T> leaf;
            if (tempEdge != null) {
                leaf = tempEdge.getDest();
            } else {
                leaf = new Node<>();
                leaf.addRef(value);
                r.addEdge(new Edge<>(rest, leaf));
            }
            if (this.activeLeaf != this.root) {
                this.activeLeaf.setSuffix(leaf);
            }
            this.activeLeaf = leaf;
            if (oldRoot != this.root) {
                oldRoot.setSuffix(r);
            }
            oldRoot = r;
            if (null == s.getSuffix()) {
                assert this.root == s;
                k = k.substring(1);
            } else {
                Pair<Node<T>, SubString> canonized = canonize(s.getSuffix(), safeCutLastChar(k));
                char nextChar = k.charAt(k.length() - 1);
                s = canonized.first();
                k = canonized.second().append(nextChar);
            }
            ret = testAndSplit(s, safeCutLastChar(k), newChar, rest, value);
            endpoint = ret.first();
            r = ret.second();
        }
        if (oldRoot != this.root) {
            oldRoot.setSuffix(r);
        }
        return canonize(s, k);
    }

    private static SubString safeCutLastChar(SubString subString) {
        return subString.length() == 0 ? subString : subString.shorten(1);
    }

    @Override
    public String statistics() {
        return "GeneralizedSuffixTree:\nNode size stats: \n" + this.root.nodeSizeStats() + "\nNode edge stats: \n" + this.root.nodeEdgeStats();
    }

    public void printTree(PrintWriter out, boolean includeSuffixLinks) {
        this.root.printTree(out, includeSuffixLinks);
    }
}