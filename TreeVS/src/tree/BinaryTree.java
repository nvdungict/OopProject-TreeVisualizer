package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree implements Tree {
    private TreeNode root;

    public BinaryTree() {
        this.root = null;
    }
    
    @Override
    public Tree cloneTree() {
        BinaryTree clonedTree = new BinaryTree();
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
        if (numberOfNodes <= 0) return;

        List<TreeNode> nodes = new ArrayList<>(); // Maintain a list of nodes already added to the tree

        // Generate a random value for the root node
        int rootValue = (int) (Math.random() * 100); // Random root value generation, adjust range as needed
        root = new TreeNode(rootValue);
        nodes.add(root);

        // Generate the remaining values and insert them into the tree
        for (int i = 1; i < numberOfNodes; i++) {
            // Find a parent node with less than two children
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
                parentNode.addChild(new TreeNode(newValue));
            } else {
            	System.out.println();
                System.out.println("Parent node already has two children.");
            }
        } else {
        	System.out.println();
            System.out.println("Parent value not found in the tree.");
        }
        }
    }

    public void delete(int value) {
        // Logic for deleting from a binary tree
        root = deleteRecursive(root, value);
    }

    private TreeNode deleteRecursive(TreeNode currentNode, int value) {
        if (currentNode == null) {
            return null;
        }

        if (value == currentNode.getValue()) {
            // Node to be deleted found
            return null; // Deleting the current node and all its children
        }

        // Recursively delete in children
        List<TreeNode> children = currentNode.getChildren();
        for (int i = 0; i < children.size(); i++) {
            TreeNode child = children.get(i);
            if (child.getValue() == value) {
                children.remove(i);
                i--; // Adjust index after removal
            } else {
                deleteRecursive(child, value);
            }
        }

        return currentNode;
    }

    public TreeNode search(int value) {
        return search(root, value);
    }

    private TreeNode search(TreeNode currentNode, int value) {
        if (currentNode == null || currentNode.getValue() == value) {
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
    }

    public TreeNode getRoot() {
        return root;
    }

    private void traverseDFS() {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode currentNode = stack.pop();
            System.out.print(currentNode.getValue() + " ");

            // Push children onto the stack in reverse order to achieve DFS
            List<TreeNode> children = currentNode.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                stack.push(children.get(i));
            }
        }
    }

    public void bfsTraverse() {
        if (root != null) {
            traverseBFS();
        }
    }

    public void dfsTraverse() {
        if (root != null) {
            traverseDFS();
        }
    }
}
