package compiler488.ast.decl;

import compiler488.visitor.IVisitor;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDeclPart extends DeclarationPart {
	public ScalarDeclPart(String name, int line, int column) {
		super(name, line, column);
		this.isArray = false;
	}

	/**
	 * Returns a string describing the name of the object being declared.
	 */
	@Override
	public String toString() {
		return name;
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
