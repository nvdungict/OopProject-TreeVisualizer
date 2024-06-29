package tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GenericTree implements Tree {
    private TreeNode root;

    public GenericTree() {
        this.root = null;
    }
    
    @Override
    public Tree cloneTree() {
        GenericTree clonedTree = new GenericTree();
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

    @Override
    public void createRandomTree(int numberOfNodes) {
        if (numberOfNodes <= 0) return;

        List<TreeNode> nodes = new ArrayList<>(); // Maintain a list of nodes already added to the tree

        // Generate a random value for the root node
        int rootValue = (int) (Math.random() * 100); // Random root value generation, adjust range as needed
        root = new TreeNode(rootValue);
        nodes.add(root);

        // Generate the remaining values and insert them into the tree
        for (int i = 1; i < numberOfNodes; i++) {
            TreeNode parentNode = nodes.get((int) (Math.random() * nodes.size())); // Randomly select a parent node
            int newValue;
            do {
                newValue = (int) (Math.random() * 100);
            } while (containsValue(root, newValue)); // Ensure the value is unique
            insert(parentNode.getValue(), newValue); // Pass parent node directly
            nodes.add(new TreeNode(newValue)); // Add the new node to the list of nodes
        }
    }



    @Override
    public void insert(int parentValue, int newValue) {
        if (root == null) {
            // Inserting as root node
            root = new TreeNode(newValue);
            return;
        }
        if (containsValue(root, newValue)) { System.out.println("The new value has already");
        
        }
        else {

        TreeNode parentNode = search(root, parentValue);
        if (parentNode != null) {
            // Insert the new value as a child of the parent node
            TreeNode newNode = new TreeNode(newValue);
            parentNode.addChild(newNode);
        } else {
            System.out.println("Parent value not found in the tree.");
        }
    }
    }


    @Override
    public void delete(int value) {
        // Logic for deleting from a generic tree
        if (root == null) {
            return;
        }
        if (root.getValue() == value) {
            root = null; // If the root is to be deleted, set the tree to empty
            return;
        }
        deleteRecursive(root, value);
    }

    private boolean deleteRecursive(TreeNode currentNode, int value) {
        for (TreeNode child : currentNode.getChildren()) {
            if (child.getValue() == value) {
                currentNode.removeChild(child);
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

    @Override
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

    public void bfsTraverse() {
        if (root != null) {
            traverseBFS();
        }
    }
    
    public void dfsTraverse() {
        if (root != null) {
            traverseBFS();
        }
    }

}
