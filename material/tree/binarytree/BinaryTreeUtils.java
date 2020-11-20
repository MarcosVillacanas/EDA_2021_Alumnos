package material.tree.binarytree;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.*;

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

		BinaryTree<E> newTree = new LinkedBinaryTree<>();

		Queue<Position<E>> origins = new LinkedList<>();
		Queue<Position<E>> destinations = new LinkedList<>();

		origins.add(tree.root());
		destinations.add(newTree.addRoot(tree.root().getElement()));

		while (!origins.isEmpty()) {
			Position<E> originCurrent = origins.poll();
			Position<E> destCurrent = destinations.poll();

			if (tree.hasLeft(originCurrent)) {
				destinations.add(newTree.insertRight(destCurrent, tree.left(originCurrent).getElement()));
				origins.add(tree.left(originCurrent));
			}
			if (tree.hasRight(originCurrent)) {
				destinations.add(newTree.insertLeft(destCurrent, tree.right(originCurrent).getElement()));
				origins.add(tree.right(originCurrent));
			}
		}
		return newTree;
	}
	/**
	 * Determines whether the element e is in the tree or not
	 */
	public boolean contains (E e) {
		for (Position<E> p : tree) {
			if (p.getElement().equals(e)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Determines the level of a node in the tree (root is located at level 1)
	 */
	public int level(Position<E> p) {
		int level = 1;
		while (!p.equals(tree.root())) {
			level++;
			p = tree.parent(p);
		}
		return level;
	}

	public Iterable<? extends Integer> levelsIncomplete() {
		TreeSet<Integer> incomplete = new TreeSet<>();
		HashMap<Position<E>, Integer> levels = new HashMap<>();
		Queue<Position<E>> queue = new LinkedList<>();

		levels.put(tree.root(), 1);
		queue.add(tree.root());

		while (!queue.isEmpty()) {
			Position<E> current = queue.poll();

			if (tree.isLeaf(current)) {
				continue;
			}

			if (tree.hasLeft(current)) {
				queue.add(tree.left(current));
				levels.put(tree.left(current), levels.get(current) + 1);
			}
			else {
				incomplete.add(levels.get(current));
			}
			if (tree.hasRight(current)) {
				queue.add(tree.right(current));
				levels.put(tree.right(current), levels.get(current) + 1);
			}
			else {
				incomplete.add(levels.get(current));
			}
		}
		return incomplete;
	}

	public Iterable<? extends Integer> levelsComplete() {
		TreeSet<Integer> levelsFound = new TreeSet<>();
		TreeSet<Integer> incomplete = new TreeSet<>();
		HashMap<Position<E>, Integer> levels = new HashMap<>();
		Queue<Position<E>> queue = new LinkedList<>();

		levels.put(tree.root(), 1);
		queue.add(tree.root());

		while (!queue.isEmpty()) {
			Position<E> current = queue.poll();
			levelsFound.add(levels.get(current));

			if (tree.isLeaf(current)) {
				continue;
			}

			if (tree.hasLeft(current)) {
				queue.add(tree.left(current));
				levels.put(tree.left(current), levels.get(current) + 1);
			}
			else {
				incomplete.add(levels.get(current));
			}
			if (tree.hasRight(current)) {
				queue.add(tree.right(current));
				levels.put(tree.right(current), levels.get(current) + 1);
			}
			else {
				incomplete.add(levels.get(current));
			}
		}
		levelsFound.removeAll(incomplete);
		return levelsFound;
	}

	public int sumBinaryLevels(Integer[] levels) {
		HashSet<Integer> levelsToAdd = new HashSet<>(Arrays.asList(levels));
		HashMap<Position<E>, Integer> positionLevel = new HashMap<>();
		Queue<Position<E>> queue = new LinkedList<>();
		int sum = 0;

		if (!tree.isEmpty()) {
			positionLevel.put(tree.root(), 0);
			queue.add(tree.root());

			while (!queue.isEmpty()) {
				Position<E> current = queue.poll();
				int currentLevel = positionLevel.get(current);
				if (levelsToAdd.contains(currentLevel)) {
					sum += (Integer) current.getElement();
				}
				for (Position<E> child : tree.children(current)) {
					queue.add(child);
					positionLevel.put(child, currentLevel + 1);
				}
			}
		}
		return sum;
	}

	public boolean isOdd() {
		if (this.tree.isEmpty() || this.tree.size() == 1) {
			return true;
		}

		if (this.tree.hasLeft(this.tree.root())) {
			int leftDescendants = 0;
			Queue<Position<E>> queue = new LinkedList<>();
			queue.add(this.tree.left(this.tree.root()));
			while (!queue.isEmpty()) {
				Position<E> current = queue.poll();
				leftDescendants++;
				for (Position<E> child : this.tree.children(current)) {
					queue.add(child);
				}
			}
			return (leftDescendants > ((this.tree.size() - 1) / 2));
		}
		else {
			return false;
		}
	}

}
