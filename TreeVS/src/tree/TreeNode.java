package tree;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TreeNode {
    int value;
    List<TreeNode> children;
    private Circle circle;
    private List<Line> lines;
    private int height;
    private double x, y;

    public TreeNode(int value) {
        this.value = value;
        this.height = 1;
        this.children = new ArrayList<>();
        this.circle = new Circle();
        this.lines = new ArrayList<>();
    }
    public void setValue(int value) {
    	this.value = value;
    }

    public Circle getCircle() {
        return circle;
    }

    public List<Line> getLines() {
        return lines;
    }

    public int getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public void removeChild(TreeNode child) {
        children.remove(child);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public TreeNode cloneNode() {
        TreeNode clone = new TreeNode(this.value);
        clone.setHeight(this.height);
        for (TreeNode child : this.children) {
            clone.addChild(child.cloneNode());
        }
        return clone;
    }
}
