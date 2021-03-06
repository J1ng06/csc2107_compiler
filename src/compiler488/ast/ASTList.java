package compiler488.ast;

import java.util.LinkedList;

import compiler488.visitor.IVisitable;
import compiler488.visitor.IVisitor;

/**
 * A list of AST nodes.
 *
 * <p>
 * This is implemented by extending {@link java.util.LinkedList}. Thanks to
 * this, any method that expects to take an instance of
 * {@link java.util.Collection} or {@link java.util.List} can take an
 * <code>ASTList</code> as well. You can also directly iterate over it using an
 * enhanced Java <code>for</code>.
 *
 * <p>
 * This list is itself an AST node as well, since it implements the AST
 * interface.
 * </p>
 */
public class ASTList<E extends AST> extends LinkedList<E> implements AST, IVisitable {
	private static final long serialVersionUID = 1L;
	private static final int UNDEFINED = -1;

	/**
	 * Create an empty AST list
	 */
	public ASTList() {
		super();
	}

	/**
	 * Create an AST list and add one element to begin with.
	 *
	 * @param elem
	 *            the first element to add
	 */
	public ASTList(E elem) {
		this();

		append(elem);
	}

	/**
	 * Append an element to the list, while return the list itself.
	 *
	 * <p>
	 * This is a pure-side-effect method, so it doesn't need to return anything.
	 * However, we like the conciseness gained when such methods return the
	 * target object, as it allows you to chain calls together, such as
	 * {@code new ASTList<Expn>(first).append(second).append(third)}.
	 *
	 * @param elem
	 *            the element to add at the end of the list
	 * @return the list itself
	 */
	public ASTList<E> append(E elem) {
		addLast(elem);

		return this;
	}
    public int getLine() {
    	return isEmpty() ? UNDEFINED : getFirst().getLine();
    }
    
    public int getColumn() {
    	return isEmpty() ? UNDEFINED : getFirst().getColumn();
    }

	/**
	 * By default, pretty-print the list with one element per line.
	 *
	 * @param p
	 *            the printer to use
	 */
	public void prettyPrint(PrettyPrinter p) {
		prettyPrintNewlines(p);
	}

	/**
	 * Pretty-print the list elements, separated by commas.
	 *
	 * @param p
	 *            the printer to use
	 */
	public void prettyPrintCommas(PrettyPrinter p) {
		boolean first = true;

		for (PrettyPrintable node : this) {
			if (!first) {
				p.print(", ");
			}

			node.prettyPrint(p);

			first = false;
		}
	}

	/**
	 * Pretty-print the list with one element per line.
	 *
	 * @param p
	 *            the printer to use
	 */
	public void prettyPrintNewlines(PrettyPrinter p) {
		for (PrettyPrintable node : this) {
			node.prettyPrint(p);
			p.newline();
		}
	}

	/**
	 * Pretty-print the list in a new block, with one element per line.
	 *
	 * @param p
	 *            the printer to use
	 */
	public void prettyPrintBlock(PrettyPrinter p) {
		p.enterBlock();
		prettyPrintNewlines(p);
		p.exitBlock();
	}

	/**
	 * Return a comma separated list of the stringified list elements.
	 *
	 * @return a string of comma-separated list elements
	 */
	public String toString() {
		if (size() == 0) {
			return "";
		}

		boolean first = true;
		StringBuffer buf = new StringBuffer();

		for (PrettyPrintable node : this) {
			if (!first) {
				buf.append(", ");
			}

			buf.append(node.toString());

			first = false;
		}

		return buf.toString();
	}
	
public boolean containsInstance(Class<? extends AST> clazz) {
	    for (E e : this) {
	        if (clazz.isInstance(e)) {
	            return true;
	        }
	    }
	    return false;
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
