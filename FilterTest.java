import org.junit.Assert;
import org.junit.Test;


public class FilterTest {
    @Test
    public void testFilter() {
        Hierarchy unfiltered = new ArrayBasedHierarchy(
                //         x  x     x  x     x  x     x   x
                new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
                new int[] {0, 1, 2, 3, 1, 0, 1, 0, 1, 1, 2}
        );
        Hierarchy filteredActual = Filter.filter(unfiltered, nodeId -> nodeId % 3 != 0);
        Hierarchy filteredExpected = new ArrayBasedHierarchy(
                new int[] {1, 2, 5, 8, 10, 11},
                new int[] {0, 1, 1, 0, 1, 2}
        );

        Assert.assertEquals(filteredExpected.formatString(), filteredActual.formatString());
    }


    @Test
    public void testFilterIncludeSpecificNodes() {
        Hierarchy unfiltered = new ArrayBasedHierarchy(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                new int[]{0, 1, 2, 3, 1, 0, 1, 0, 1}
        );
        Hierarchy filtered = Filter.filter(unfiltered, nodeId -> nodeId % 2 == 0);
        Hierarchy expected = new ArrayBasedHierarchy(new int[]{ 6, 8}, new int[]{ 0, 0});
        Assert.assertEquals(expected.formatString(), filtered.formatString());
    }
    @Test
    public void testFilterIncludeNoneInNestedHierarchy() {
        Hierarchy unfiltered = new ArrayBasedHierarchy(
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                new int[]{0, 1, 2, 1, 0, 0, 0, 0, 1}
        );
        Hierarchy filtered = Filter.filter(unfiltered, nodeId -> nodeId % 2 == 0);
        Hierarchy expected = new ArrayBasedHierarchy(new int[]{ 6, 8}, new int[]{0, 0});
        Assert.assertEquals(expected.formatString(), filtered.formatString());
    }


}


