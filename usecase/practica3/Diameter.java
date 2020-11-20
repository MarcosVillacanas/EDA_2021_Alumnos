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
		int maxDiameter = 0;
		for (Position<E> next : tree) {
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
		int hops = 0;
		while (!n1.equals(tree.root())) {
			hops++;
			n1 = tree.parent(n1);
			ancestors.put(n1, hops);
		}

		hops = 0;
		while (! (ancestors.containsKey(n2) || n2.equals(tree.root()))) {
			hops++;
			n2 = tree.parent(n2);
		}
		return hops + ancestors.getOrDefault(n2, 0);
	}
}
