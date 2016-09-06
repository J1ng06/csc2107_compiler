package compiler488.ast.decl;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.type.Type;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Holds the declaration of multiple elements.
 */
public class MultiDeclarations extends Declaration {
	/** The parts being declared */
	private ASTList<DeclarationPart> elements;
	private Type type;

	public MultiDeclarations(Type type, ASTList<DeclarationPart> elements, int line, int column) {
		super(null, type, line, column);

		this.elements = elements;
		this.type = type;
	}

	public ASTList<DeclarationPart> getParts() {
		return elements;
	}
	
	public Type getType() {
	    return type;
	}
	
	public SymbolType getSymbolType() {
	    return type.getType();
	}

	public void prettyPrint(PrettyPrinter p) {
		p.print("var ");
		elements.prettyPrintCommas(p);
		p.print(" : " + type);
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
