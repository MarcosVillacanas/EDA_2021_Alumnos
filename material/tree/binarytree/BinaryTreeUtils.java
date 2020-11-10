package material.tree.binarytree;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.Iterator;

public class BinaryTreeUtils<E> {
	
	private BinaryTree<E> tree;
	
	public BinaryTreeUtils(BinaryTree<E> tree) {
		this.tree = tree;
	}

	/**
	 * Given a tree the method returns a new tree where all left children
	 * become right children and vice versa
	 */
	public BinaryTree<E> mirror() {
		LinkedBinaryTree<E> mirrored = new LinkedBinaryTree<>();
		mirrored.addRoot(this.tree.root().getElement());
		mirrorAux(mirrored, mirrored.root(), this.tree.root());
		return mirrored;
	}

	private void mirrorAux(BinaryTree<E> mirrored,
						   Position<E> mirroredPos, Position<E> treePos) {

		if (this.tree.hasRight(treePos)) {
			mirrored.insertLeft(mirroredPos, this.tree.right(treePos).getElement());
			mirrorAux(mirrored, mirrored.left(mirroredPos), this.tree.right(treePos));
		}
		if (this.tree.hasLeft(treePos)) {
			mirrored.insertRight(mirroredPos, this.tree.left(treePos).getElement());
			mirrorAux(mirrored, mirrored.right(mirroredPos), this.tree.left(treePos));
		}
	}
	/**
	 * Determines whether the element e is in the tree or not
	 */
	public boolean contains (E e) {
		Iterator<Position<E>> it = this.tree.iterator();
		boolean found = false;
		while (it.hasNext() && !found) {
			Position<E> next = it.next();
			found = next.getElement().equals(e);
		}
		return found;
	}
	/**
	 * Determines the level of a node in the tree (root is located at level 1)
	 */
	public int level(Position<E> p) {
		Position<E> parent = p;
		int lev = 1;
		while (parent != this.tree.root()) {
			parent = tree.parent(parent);
			lev++;
		}
		return lev;
	}

	/**
	 * Determines the height of a node in the tree
	 */
	private int height(Position<E> p) {
		if (this.tree.isLeaf(p)) {
			return 0;
		}
		else if (this.tree.hasLeft(p) && this.tree.hasRight(p)) {
			return 1 + Math.max(height(this.tree.left(p)), height(this.tree.right(p)));
		}
		else if (this.tree.hasLeft(p)) {
			return height(this.tree.left(p));
		}
		else {
			return height((this.tree.right(p)));
		}
	}

}
