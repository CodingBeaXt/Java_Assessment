import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface Hierarchy {
    int size();

    int nodeId(int index);

    int depth(int index);

    default String formatString() {
        return IntStream.range(0, size()).mapToObj(i -> "" + nodeId(i) + ":" + depth(i)).collect(Collectors.joining(", ", "[", "]"));
    }
}

class Filter {
    // Filter the hierarchy based on the given nodeIdPredicate
    static Hierarchy filter(Hierarchy hierarchy, IntPredicate nodeIdPredicate) {
        int size = hierarchy.size();

        int[] filteredNodeIds = new int[size];
        int[] filteredDepths = new int[size];
        int filteredSize = 0;

        boolean[] includeNode = new boolean[size];

        for (int i = 0; i < size; i++) {
            int nodeId = hierarchy.nodeId(i);
            int depth = hierarchy.depth(i);

            // Determine whether the node should be included in the filtered hierarchy
            boolean shouldInclude = includeNode(nodeIdPredicate, nodeId, depth, i, hierarchy, includeNode);

            if (shouldInclude) {
                filteredNodeIds[filteredSize] = nodeId;
                filteredDepths[filteredSize] = depth;
                filteredSize++;
            }
        }

        return createFilteredHierarchy(filteredNodeIds, filteredDepths, filteredSize);
    }

    // Helper method to check if a node should be included based on the nodeIdPredicate and ancestor nodes
    private static boolean includeNode(IntPredicate nodeIdPredicate, int nodeId, int depth, int currentIndex, Hierarchy hierarchy, boolean[] includeNode) {
        includeNode[currentIndex] = nodeIdPredicate.test(nodeId);
        int steps = 1;

        if (depth > 0 && includeNode[currentIndex]) {
            for (int j = currentIndex - 1; j >= 0; j--) {
                if (hierarchy.depth(j) + steps == depth) {
                    steps += 1;
                    if (!includeNode[j]) {
                        includeNode[currentIndex] = false;
                        break;
                    }
                }
                if (hierarchy.depth(j) == 0) {
                    break;
                }
            }
        }

        return includeNode[currentIndex];
    }

    // Helper method to create a new hierarchy with the filtered data
    private static Hierarchy createFilteredHierarchy(int[] nodeIds, int[] depths, int size) {
        return new ArrayBasedHierarchy(
                Arrays.copyOf(nodeIds, size),
                Arrays.copyOf(depths, size)
        );
    }
}

class ArrayBasedHierarchy implements Hierarchy {
    private final int[] myNodeIds;
    private final int[] myDepths;

    public ArrayBasedHierarchy(int[] nodeIds, int[] depths) {
        myNodeIds = nodeIds;
        myDepths = depths;
    }

    @Override
    public int size() {
        return myDepths.length;
    }

    @Override
    public int nodeId(int index) {
        return myNodeIds[index];
    }

    @Override
    public int depth(int index) {
        return myDepths[index];
    }
}
