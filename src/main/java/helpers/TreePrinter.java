package helpers;

import gpLibrary.primitives.INodeVisitor;
import gpLibrary.primitives.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class TreePrinter implements INodeVisitor<Double> {


    private final List<Node<Double>> nodes;

    public TreePrinter() {
        this.nodes = new ArrayList<>();
    }

    /**
     * @deprecated Not used in this inheritor
     * @param node The node being visited
     */
    @Override
    @Deprecated public void visit( Node<Double> node) {
        nodes.add(node);
    }

    // The code below has been taken from https://prismoskills.appspot.com/lessons/Binary_Trees/Tree_printing.jsp
    // and adapted to fit my needs.
    // This code is not intended for marking or for any assessment and I do not claim it as my own!
    // The code is for helping me to debug trees by having a visual representation.
    public void drawTree(Node<Double> root) throws Exception {

        System.out.println("\n\nLevel order traversal of tree:");
        int depth = depth(root);
        setLevels (root, 0);

        int[] depthChildCount = new int [depth+1];


        LinkedList<Node<Double>> q = new LinkedList<>();
        q.add(root.getChild(0));
        q.add(root.getChild(1));

        // draw root first
        root._drawPos = (int)Math.pow(2, depth-1)*H_SPREAD;
        currDrawLevel = root._level;
        currSpaceCount = root._drawPos;
        System.out.print(getSpace(root._drawPos) + root.Name);

        while (!q.isEmpty())
        {
            Node<Double> ele = q.pollFirst();
            drawElement (ele, depthChildCount, depth, q);
            if (ele == null)
                continue;
            q.add(ele.getChild(0));
            q.add(ele.getChild(1));
        }
        System.out.println();
    }

    private int currDrawLevel  = -1;
    private int currSpaceCount = -1;
    private final int H_SPREAD = 3;
    private void drawElement(Node<Double> ele, int[] depthChildCount, int depth, LinkedList<Node<Double>> q) throws Exception {
        if (ele == null)
            return;

        if (ele._level != currDrawLevel)
        {
            currDrawLevel = ele._level;
            currSpaceCount = 0;
            System.out.println();
            for (int i=0; i<(depth-ele._level+1); i++)
            {
                int drawn = 0;
                if (ele.Parent.getChild(0) != null)
                {
                    drawn = ele.Parent._drawPos - 2*i - 2;
                    System.out.print(getSpace(drawn) + "/");
                }
                if (ele.Parent.getChild(1) != null)
                {
                    int drawn2 = ele.Parent._drawPos + 2*i + 2;
                    System.out.print(getSpace(drawn2 - drawn) + "\\");
                    drawn = drawn2;
                }

                Node<Double> doneParent = ele.Parent;
                for (Node<Double>  sibling: q)
                {
                    if (sibling == null)
                        continue;
                    if (sibling.Parent == doneParent)
                        continue;
                    doneParent = sibling.Parent;
                    if (sibling.Parent.getChild(0) != null)
                    {
                        int drawn2 = sibling.Parent._drawPos - 2*i - 2;
                        System.out.print(getSpace(drawn2-drawn-1) + "/");
                        drawn = drawn2;
                    }

                    if (sibling.Parent.getChild(1) != null)
                    {
                        int drawn2 = sibling.Parent._drawPos + 2*i + 2;
                        System.out.print(getSpace(drawn2-drawn-1) + "\\");
                        drawn = drawn2;
                    }
                }
                System.out.println();
            }
        }
        int offset=0;
        int numDigits = ele.Name.length();
        if (ele.Parent.getChild(0) == ele)
        {
            ele._drawPos = ele.Parent._drawPos - H_SPREAD*(depth-currDrawLevel+1);
            //offset = 2;
            offset += numDigits/2;
        }
        else
        {
            ele._drawPos = ele.Parent._drawPos + H_SPREAD*(depth-currDrawLevel+1);
            //offset = -2;
            offset -= numDigits;
        }

        System.out.print (getSpace(ele._drawPos - currSpaceCount + offset) + ele.Name);
        currSpaceCount = ele._drawPos + numDigits/2;
    }

    private void setLevels (Node<Double> r, int level)
    {
        if (r == null)
            return;
        r._level = level;

        for (Node<Double> child : r.getChildren()) {
            setLevels (child, level+1);
        }
    }

    private String getSpace (int i)
    {
        StringBuilder s = new StringBuilder();
        while (i-- > 0)
            s.append(" ");
        return s.toString();
    }

    private static int depth (Node<Double> n) throws Exception {
        if (n == null)
            return 0;
        n._depth = 1 + Math.max(depth(n.getChild(0)), depth(n.getChild(1)));
        return n._depth;
    }
}
