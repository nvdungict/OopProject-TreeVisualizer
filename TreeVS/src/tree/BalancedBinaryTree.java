package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class BalancedBinaryTree implements Tree{
    private TreeNode root;
    public int maxDifference;

    public BalancedBinaryTree(int maxDifference) {
        this.root = null;
        this.maxDifference = maxDifference;
    }
    
    @Override
    public Tree cloneTree() {
        BalancedBinaryTree clonedTree = new BalancedBinaryTree(this.maxDifference);
        if (this.root != null) {
            clonedTree.root = this.root.cloneNode();
        }
        return clonedTree;
    }

    @Override
    public void update(int oldValue, int newValue) {
        TreeNode node = search(oldValue);
        if (node == null) {
            throw new IllegalArgumentException("Value " + oldValue + " not found in the tree.");
        }
        node.setValue(newValue); // Change the node's value
    }
 
    
    public void createRandomTree(int numberOfNodes) {
    	int plus=0;
        if (numberOfNodes <= 0) return;

        List<TreeNode> nodes = new ArrayList<>(); // Maintain a list of nodes already added to the tree

        // Generate a random value for the root node
        int rootValue = (int) (Math.random() * 100); // Random root value generation, adjust range as needed
        root = new TreeNode(rootValue);
        nodes.add(root);

        // Generate the remaining values and insert them into the tree
        for (int i = 1; i < numberOfNodes; i++) {
            // Find a parent node with less than two children and ensures tree remains balanced
            TreeNode parentNode = findParentWithAvailableSlot(nodes);

            if (parentNode == null) {
                //System.out.println("All nodes have maximum children, cannot insert more.");
                break; // No eligible parents left
            }

            int newValue;
            do {
                newValue = (int) (Math.random() * 100);
            } while (containsValue(root, newValue)); // Ensure the value is unique
            TreeNode newNode = new TreeNode(newValue);
            parentNode.addChild(newNode); // Add the new node as a child of the parent
            nodes.add(newNode); // Add the new node to the list of nodes
            
            if (!isBalanced()) {
                // If tree is not balanced after insertion, remove the newly added node
                parentNode.removeChild(newNode);
                nodes.remove(newNode);
                plus=1;
                numberOfNodes+=plus;
                plus=0;
                //System.out.println("Tree became unbalanced after adding node, removing the last added node.");
            }
        }
    }

    private TreeNode findParentWithAvailableSlot(List<TreeNode> nodes) {
        // Shuffle the list to randomize selection
        Collections.shuffle(nodes);

        // Iterate through the shuffled list and find a parent node with less than two children
        for (TreeNode node : nodes) {
            if (node.getChildren().size() < 2) {
                return node;
            }
        }

        return null; // No parent with available slot found
    }


   /* private void insertWithBalance(TreeNode parentNode, int newValue, List<TreeNode> nodes) {
        TreeNode newNode = new TreeNode(newValue);
        if (parentNode.getChildren().size() < 2) {
            parentNode.addChild(newNode);

            // Check if the tree is still balanced after insertion
            if (!isBalanced()) {
                // If unbalanced, remove the newly added node
                System.out.println("Insertion would unbalance the tree, rolling back.");
                parentNode.removeChild(newNode);
            } else {
                nodes.add(newNode);
            }
        } else {
            System.out.println("Parent node already has two children.");
        }
    }   */

    public void delete(int value) {
        if (root == null) return;
        if (root.getValue() == value) {
            root = null;
            return;
        }
        deleteRecursive(root, value);
    }

    private boolean deleteRecursive(TreeNode currentNode, int value) {
        for (TreeNode child : currentNode.getChildren()) {
            if (child.getValue() == value) {
                currentNode.removeChild(child);
                if (!isBalanced()) {
                    System.out.println("Deletion would unbalance the tree, rolling back.");
                    currentNode.addChild(child);
                    return false;
                }
                return true;
            } else {
                boolean deleted = deleteRecursive(child, value);
                if (deleted) {
                    return true;
                }
            }
        }
        return false;
    }

    public TreeNode search(int value) {
        return search(root, value);
    }

    private TreeNode search(TreeNode currentNode, int value) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.getValue() == value) {
            return currentNode;
        }

        for (TreeNode child : currentNode.getChildren()) {
            TreeNode result = search(child, value);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public TreeNode getRoot() {
        return root;
    }

    public boolean isBalanced() {
        if (root == null) return true;

        List<Integer> leafDepths = new ArrayList<>();
        calculateLeafDepths(root, 0, leafDepths);

        int minDepth = Integer.MAX_VALUE;
        int maxDepth = Integer.MIN_VALUE;

        for (int depth : leafDepths) {
            if (depth < minDepth) minDepth = depth;
            if (depth > maxDepth) maxDepth = depth;
        }

        return (maxDepth - minDepth) <= maxDifference;
    }

    private void calculateLeafDepths(TreeNode node, int currentDepth, List<Integer> leafDepths) {
        if (node.getChildren().isEmpty()) {
            leafDepths.add(currentDepth);
            return;
        }

        for (TreeNode child : node.getChildren()) {
            calculateLeafDepths(child, currentDepth + 1, leafDepths);
        }
    }

    public void bfsTraverse() {
        if (root != null) {
            traverseBFS();
        }
    }

    private void traverseBFS() {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode currentNode = queue.poll();
            System.out.print(currentNode.getValue() + " ");

            for (TreeNode child : currentNode.getChildren()) {
                queue.add(child);
            }
        }
        System.out.println();
    }

    public void dfsTraverse() {
        if (root != null) {
            traverseDFS();
        }
    }

    private void traverseDFS() {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode currentNode = stack.pop();
            System.out.print(currentNode.getValue() + " ");

            List<TreeNode> children = currentNode.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                stack.push(children.get(i));
            }
        }
        System.out.println();
    }

    public void setMaximumDifference(int maxDifference) {
        this.maxDifference = maxDifference;
    }

    public void insert(int parentValue, int newValue) {
        if (root == null) {
            root = new TreeNode(newValue);
            return;
        }
        if (containsValue(root, newValue)) { System.out.println("The new value has already");
        
        }
        else {

        TreeNode parentNode = search(root, parentValue);
        if (parentNode != null) {
            if (parentNode.getChildren().size() < 2) {
                TreeNode newNode = new TreeNode(newValue);
                parentNode.addChild(newNode);

                // Check if the tree is still balanced after insertion
                if (!isBalanced()) {
                    // If unbalanced, remove the newly added node
                    System.out.println("Insertion would unbalance the tree, rolling back.");
                    parentNode.removeChild(newNode);
                }
            } else {
                System.out.println("Parent node already has two children.");
            }
        } else {
            System.out.println("Parent value not found in the tree.");
        }
    }
    }

}