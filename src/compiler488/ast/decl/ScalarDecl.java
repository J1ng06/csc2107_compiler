package compiler488.ast.decl;

import compiler488.ast.PrettyPrinter;
import compiler488.ast.type.Type;
import compiler488.visitor.IVisitor;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDecl extends Declaration {
	public ScalarDecl(String name, Type type, int line, int column) {
		super(name, type, line, column);
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print(name + " : " + type);
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
