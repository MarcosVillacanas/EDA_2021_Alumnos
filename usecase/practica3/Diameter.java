package usecase.practica3;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.HashMap;
import java.util.Iterator;

public class Diameter<E> {

	private BinaryTree<E> tree;

	public Diameter(BinaryTree<E> tree) { this.tree = tree; }

	/**
	 * Determines the longest path between any two nodes
	 */
	public int diameter() {
		Iterator<Position<E>> ite = tree.iterator();
		int maxDiameter = -1;
		while (ite.hasNext()) {
			Position<E> next = ite.next();
			for (Position<E> next2 : tree) {
				maxDiameter = Math.max(maxDiameter, diameter(next, next2));
			}
		}
		return maxDiameter;
	}

	/**
	 * Determines the longest path between two given nodes
	 */
	public int diameter(Position<E> n1, Position<E> n2) {
		HashMap<Position<E>, Integer> ancestors = new HashMap<>();
		Position<E> parent = n1;

		int level = 0;
		while (!tree.isRoot(parent)) {
			ancestors.put(parent, level++);
			parent = tree.parent(parent);
		}
		ancestors.put(parent, level);

		level = 0;
		parent = n2;
		while (!ancestors.containsKey(parent)) {
			parent = tree.parent(parent);
			level++;
		}

		return level + ancestors.get(parent);
	}
}
