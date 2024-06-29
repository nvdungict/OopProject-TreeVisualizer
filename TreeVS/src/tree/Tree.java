package tree;

public interface Tree {
    void createRandomTree(int numberOfValues);
    void insert(int parentValue, int newValue);
    void delete(int value);
    TreeNode search(int value);
    void bfsTraverse();
    void dfsTraverse();
    void update(int oldValue, int newValue);
	TreeNode getRoot();   
	Tree cloneTree();
	default boolean containsValue(TreeNode node, int value) {
        if (node == null) return false;
        if (node.value == value) return true;
        for (TreeNode child : node.children) {
            if (containsValue(child, value)) return true;
        }
        return false;
	}
}