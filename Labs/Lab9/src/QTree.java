import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the Quadtree data structure used to compress raw
 * grayscale images and uncompress back.  Conceptually, the tree is
 * a collection of QTNode's.  A QTNode either holds a grayscale image
 * value (0-255), or QUAD_SPLIT, meaning the node is split into four
 * sub-nodes that are equally sized sub-regions that divide up the
 * current space.
 *
 * To learn more about quadtrees:
 *      https://en.wikipedia.org/wiki/Quadtree
 *
 * @author Sean Strout @ RIT
 * @author Michael Jansen
 */
public class QTree {
    /** the value of a node that indicates it is split into 4 sub-regions */
    public final static int QUAD_SPLIT = -1;

    /** the root node in the tree */
    private QTNode root;

    /** the square dimension of the tree */
    private int DIM;

    /**  the raw image */
    private int image[][];

    /** the size of the raw image */
    private int rawSize;

    /** the size of the compressed image */
    private int compressedSize;

    /**
     * Create an initially empty tree.
     */
    public QTree() {
        this.root = null;
        this.DIM = 0;
        this.image = null;
        this.rawSize = 0;
        this.compressedSize = 0;
    }

    /**
     * Get the images square dimension.
     *
     * @return the square dimension
     */
    public int getDim() { return this.DIM; }

    /** Get the raw image.
     *
     * @return the raw image
     */
    public int[][] getImage(){ return this.image; }

    /**
     * Get the size of the raw image.
     *
     * @return raw image size
     */
    public int getRawSize() { return this.rawSize; }

    /**
     * Get the size of the compressed image.
     *
     * @return compressed image size
     */
    public int getCompressedSize() { return this.compressedSize; }

    /**
     * A private helper routine for parsing the compressed image into
     * a tree of nodes.  When parsing through the values, there are
     * two cases:
     *
     * 1. The value is a grayscale color (0-255).  In this case
     * return a node containing the value.
     *
     * 2. The value is QUAD_SPLIT.  The node must be split into
     * four sub-regions.  Each sub-region is attained by recursively
     * calling this routine.  A node containing these four sub-regions
     * is returned.
     *
     * @param values the values in the compressed image
     * @return a node that encapsulates this portion of the compressed
     * image
     * @throws QTException if there are not enough values in the
     * compressed image
     */
    private QTNode parse(List<Integer> values) throws QTException {
        int x = values.remove(0);
        if(x != QUAD_SPLIT){
            // If the node is not split, return a node of the value
            return new QTNode(x);
        }else{
            // Otherwise, return a node containing its 4 children
            if(values.size() < 4) throw new QTException("Error uncompressing. Not enough data.");
            return new QTNode(x, parse(values), parse(values), parse(values), parse(values));
        }
    }

    /**
     * This is the core routine for uncompressing an image stored in a tree
     * into its raw image (a 2-D array of grayscale values (0-255).
     * It is called by the public uncompress routine.
     * The main idea is that we are working with a tree whose root represents the
     * entire 2^n x 2^n image.  There are two cases:
     *
     * 1. The node is not split.  We can write out the corresponding
     * "block" of values into the raw image array based on the size
     * of the region
     *
     * 2. The node is split.  We must recursively call ourselves with the
     * the four sub-regions.  Take note of the pattern for representing the
     * starting coordinate of the four sub-regions of a 4x4 grid:
     *      - upper left: (0, 0)
     *      - upper right: (0, 1)
     *      - lower left: (1, 0)
     *      - lower right: (1, 1)
     * We can generalize this pattern by computing the offset and adding
     * it to the starting row and column in the appropriate places
     * (there is a 1).
     *
     * @param node the node to uncompress
     * @param size the size of the square region this node represents
     * @param start the starting coordinate this row represents in the image
     */
    private void uncompress(QTNode node, int size, Coordinate start) {
        int dim = (int) Math.sqrt(size);
        if(node.getVal() != QUAD_SPLIT){
            // If the node is not split, set every pixel in the region to one color
            for(int row = 0; row < dim; row++){
                for(int col = 0; col < dim; col++){
                    image[start.getRow() + row][start.getCol() + col] = node.getVal();
                }
            }
        }else{
            // Otherwise, recursively uncompress the 4 children until all pixels are set
            uncompress(node.getUpperLeft(), size/4, start);
            uncompress(node.getUpperRight(), size/4, new Coordinate(start.getRow(), start.getCol() + dim/2));
            uncompress(node.getLowerLeft(), size/4, new Coordinate(start.getRow() + dim/2, start.getCol()));
            uncompress(node.getLowerRight(), size/4, new Coordinate(start.getRow() + dim/2, start.getCol() + dim/2));
        }
    }

    /**
     * Uncompress a RIT compressed file.  This is the public facing routine
     * meant to be used by a client to uncompress an image for displaying.
     *
     * The file is expected to be 2^n x 2^n pixels.  The first line in
     * the file is its size (number of values).  The remaining lines are
     * the values in the compressed image, one per line, of "size" lines.
     *
     * Once this routine completes, the raw image of grayscale values (0-255)
     * is stored internally and can be retrieved by the client using getImage().
     *
     * @param filename the name of the compressed file
     * @throws IOException if there are issues working with the compressed file
     * @throws QTException if there are issues parsing the data in the file
     */
    public void uncompress(String filename) throws IOException, QTException {
        Scanner in = new Scanner(new File(filename));
        int size = in.nextInt();
        this.DIM = (int) Math.sqrt(size);
        List<Integer> values = new ArrayList<>();
        while(in.hasNext()){
            values.add(in.nextInt());
        }

        this.root = parse(values);
        this.image = new int[DIM][DIM];
        uncompress(this.root, size, new Coordinate(0, 0));
    }

    /**
     * The private writer is a recursive helper routine that writes out the
     * compressed image.  It goes through the tree in preorder fashion
     * writing out the values of each node as they are encountered.
     *
     * @param node the current node in the tree
     * @param writer the writer to write the node data out to
     * @throws IOException if there are issues with the writer
     */
    private void write(QTNode node, BufferedWriter writer) throws IOException {
        // I just used the preorder method and replaced spaces with a new line
        // because it would be basically writing the same code twice if I didn't
        String values = preorder(node);
        writer.write(values.replaceAll(" ", "\n"));
    }

    /**
     * Write the compressed image to the output file.  This routine is meant to be
     * called from a client after it has been compressed
     *
     * @rit.pre client has called compress() to compress the input file
     * @param outFile the name of the file to write the compressed image to
     * @throws IOException any errors involved with writing the file out
     * @throws QTException if the file has not been compressed yet
     */
    public void write(String outFile) throws IOException, QTException {
        if(this.root == null) throw new QTException("Error writing to file. Image has not yet been compressed.");
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        out.write(this.rawSize + "\n");
        write(this.root, out);
        out.flush();
        out.close();
    }

    /**
     * Check to see whether a region in the raw image contains the same value.
     * This routine is used by the private compress routine so that it can
     * construct the nodes in the tree.
     *
     * @param start the starting coordinate in the region
     * @param size the size of the region
     * @return whether the region can be compressed or not
     */
    private boolean canCompressBlock(Coordinate start, int size) {
        int dim = (int) Math.sqrt(size);
        for(int row = start.getRow(); row < start.getRow() + dim; row++){
            for(int col = start.getCol(); col < start.getCol() + dim; col++){
                if(image[row][col] != image[start.getRow()][start.getCol()]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This is the core compression routine.  Its job is to work over a region
     * of the image and compress it.  It is a recursive routine with two cases:
     *
     * 1. The entire region represented by this image has the same value, or
     * we are down to one pixel.  In either case, we can now create a node
     * that represents this.
     *
     * 2. If we can't compress at this level, we need to divide into 4
     * equally sized sub-regions and call ourselves again.  Just like with
     * uncompressing, we can compute the starting point of the four sub-regions
     * by using the starting point and size of the full region.
     *
     * @param start the start coordinate for this region
     * @param size the size this region represents
     * @return a node containing the compression information for the region
     */
    private QTNode compress(Coordinate start, int size) {
        this.compressedSize++;
        if(canCompressBlock(start, size)){
            // If a block contains only one color, return a node of that value
            return new QTNode(image[start.getRow()][start.getCol()]);
        }else{
            // Otherwise, return a node with its 4 children
            int dim = (int) Math.sqrt(size);
            return new QTNode(QUAD_SPLIT, compress(start, size/4),
                    compress(new Coordinate(start.getRow(), start.getCol() + dim/2), size/4),
                    compress(new Coordinate(start.getRow() + dim/2, start.getCol()), size/4),
                    compress(new Coordinate(start.getRow() + dim/2, start.getCol() + dim/2), size/4));
        }
    }

    /**
     * Compress a raw image into the RIT format.  This routine is meant to be
     * called by a client.  It is expected to be passed a file which represents
     * the raw image.  It is ASCII formatted and contains a series of grayscale
     * values (0-255).  There is one value per line, and 2^n x 2^n total lines.
     *
     * @param inputFile the raw image file name
     * @throws IOException if there are issues working with the file
     */
    public void compress(String inputFile) throws IOException {
        Scanner in = new Scanner(new File(inputFile));
        List<Integer> values = new ArrayList<>();
        while (in.hasNext()){
            values.add(in.nextInt());
        }
        this.rawSize = values.size();
        this.DIM = (int) Math.sqrt(this.rawSize);
        this.image = new int[DIM][DIM];
        for(int row = 0; row < DIM; row++){
            for(int col = 0; col < DIM; col++){
                image[row][col] = values.remove(0);
            }
        }
        this.compressedSize = 1;
        this.root = compress(new Coordinate(0, 0), this.rawSize);
    }

    /**
     * A preorder (parent, left, right) traversal of a node.  It returns
     * a string which is empty if the node is null.  Otherwise
     * it returns a string that concatenates the current node's value
     * with the values of the 4 sub-regions (with spaces between).
     *
     * @param node the node being traversed on
     * @return the string of the node
     */
    private String preorder(QTNode node) {
        String str = node.getVal() + " ";
        if(node.getUpperLeft() != null && node.getLowerLeft() != null &&
                node.getUpperRight() != null && node.getLowerRight() != null){
            str += preorder(node.getUpperLeft());
            str += preorder(node.getUpperRight());
            str += preorder(node.getLowerLeft());
            str += preorder(node.getLowerRight());
        }
        return str;
    }

    /**
     * Returns a string which is a preorder traversal of the tree.
     *
     * @return the qtree string representation
     */
    @Override
    public String toString() {
        return "QTree: " + preorder(this.root);
    }
}