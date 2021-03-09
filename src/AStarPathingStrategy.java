import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        List<Point> path = new LinkedList<Point>();

        // if start point is at goal, return list
        if (withinReach.test(start, end))
            return path;


//        1.  Initialize the open lists
        // openListSorted sorted by F value
        PriorityQueue<Node> openListSorted = new PriorityQueue<Node>((n1, n2) -> n1.getF() - n2.getF());
        // hashmap to compare G value
        HashMap<Point, Integer> openListG = new HashMap<>();
        // add start point to open lists and set as current point
        Node currentNode = new Node(start, end);
        openListSorted.add(currentNode);
        openListG.put(currentNode.p, currentNode.getG());


//        2.  Initialize the closed list
        HashMap<Point, Integer> closedList = new HashMap<>();

//        3. While open list isn't empty, check all valid adjacent nodes not on closed list
        while (!openListG.isEmpty()) {
            List<Node> neighbors = potentialNeighbors.apply(currentNode.p)
                    .filter(canPassThrough::test)
                    .filter(p -> !closedList.containsKey(p))
                    .map(p -> new Node(p, end))
                    .collect(Collectors.toList());

// REMOVE LATER
//                    System.out.println("NEIGHBORS");
//                    for (Node p: neighbors) {
//                        System.out.println(p);
//                    }
// END OF REMOVE

            // for each point adjacent to the current one:
            for (Node n : neighbors) {

            // A) Goal Check: if point is at goal (within reach), build path (going backwards from get prior nodes)
                if (withinReach.test(n.p, end)) {
                    path.add(0, n.p);
                    n.setPrior(currentNode);
                    while (currentNode.p!=start) {
                        path.add(0, currentNode.p);
                        currentNode = currentNode.getPrior();
                    }
//// REMOVE LATER
//                    System.out.println("PATH");
//                    for (Point p: path) {
//                        System.out.println(p);
//                    }
//// END OF REMOVE
                    return path;
                }

            // B) If node not already on open list, add it to list
                if (!openListG.containsKey(n.p)) {
                    // 1. find and set g value (previous node g + 1) and f value
                    n.setG(currentNode.getG() + 1);
                    n.updateF();
                    // 2. add node to open list
                    openListSorted.add(n);
                }
                // if already on open list, update g value if necessary (and f)
                else {
                    int g = 1 + currentNode.getG();
                    if (n.getG() > g)
                        n.setG(g);
                    n.updateF();
                }
                // add to (other) open list and set the current node as the adjacent's prior
                openListG.put(n.p, n.getG());
                n.setPrior(currentNode);

// REMOVE LATER
//                System.out.println(n);
            }

            // 4.  Move Node to closed List and remove from open lists
            closedList.put(currentNode.p, currentNode.getF());
            openListG.remove(currentNode.p);
            openListSorted.remove(currentNode);
// REMOVE LATER
//            System.out.println("OPEN LIST");
//            for (Node n : openListSorted)
//                System.out.println(n);
//
//            System.out.println("CLOSED LIST");
//            for (Point p : closedList.keySet() )
//                System.out.println(p);
// END OF REMOVE

        // 5.  Set current node as the node w/ lowest F value on open list
            currentNode = openListSorted.peek();
// REMOVE LATER
//            System.out.println("Current Node: " + currentNode);
        }

        return path;
    }


}


class Node {
    public Point p;
    private Node prior;
    private int f;
    private int g;
    private int h;

    public Node(Point p, Point goal) {
        this.p = p;
        g = 0;
        h = Math.abs(p.x - goal.x) + Math.abs(p.y - goal.y);
        f = g+h;
    }

    public boolean equals(Object other)
    {
        if (other ==null || !this.getClass().equals(other.getClass()))
            return false;
        Node n = (Node) other;
        return  n.f == this.f &&
                n.h == this.h &&
                n.g == this.g &&
                p.equals(n.p);
    }

    public int hashCode()
    {
        int result = 17;
        result = result * 31 + g;
        result = result * 31 + h;
        result = result * p.hashCode();
        return result;
    }

    public int getF() {
        return f;
    }

    public void updateF() {
        this.f = g + h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setPrior(Node p) {
        prior = p;
    }

    public Node getPrior() {
        return prior;
    }

    public String toString()
    {
        return "Point" + p.toString() + " g h & f : "
                + getG() + ", " + getH() + ", " + getF();
    }

}

