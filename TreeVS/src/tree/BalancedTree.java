package tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class BalancedTree implements Tree {
    private TreeNode root;
    public int maxDifference;

    public BalancedTree(int maxDifference) {
        this.root = null;
        this.maxDifference = maxDifference;
    }
    
    @Override
    public Tree cloneTree() {
        BalancedTree clonedTree = new BalancedTree(this.maxDifference);
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

        List<TreeNode> nodes = new ArrayList<>();
        int rootValue = (int) (Math.random() * 100);
        root = new TreeNode(rootValue);
        nodes.add(root);

        for (int i = 1; i < numberOfNodes; i++) {
            TreeNode parentNode = nodes.get((int) (Math.random() * nodes.size()));
            int newValue;
            do {
                newValue = (int) (Math.random() * 100);
            } while (containsValue(root, newValue)); // Ensure the value is unique
            numberOfNodes+=insertWithBalance(parentNode, newValue, nodes);;
            if (nodes.size() >= numberOfNodes) break; // Exit loop when desired number of nodes is reached
        }
    }

    private int insertWithBalance(TreeNode parentNode, int newValue, List<TreeNode> nodes) {
        TreeNode newNode = new TreeNode(newValue);
        parentNode.addChild(newNode);

        // Check if the tree is still balanced after insertion
        if (!isBalanced()) {
            // If unbalanced, remove the newly added node
            parentNode.removeChild(newNode);
            return 1;
        } else {
            nodes.add(newNode);
            return 0;
        }
    }

    @Override
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

    @Override
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

    @Override
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
            TreeNode newNode = new TreeNode(newValue);
            parentNode.addChild(newNode);

            // Check if the tree is still balanced after insertion
            if (!isBalanced()) {
                // If unbalanced, remove the newly added node
                System.out.println("Insertion would unbalance the tree, rolling back.");
                parentNode.removeChild(newNode);
            }
        } else {
            System.out.println("Parent value not found in the tree.");
        }
        }
    }
}

