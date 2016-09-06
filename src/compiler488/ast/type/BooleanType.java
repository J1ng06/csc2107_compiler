package compiler488.ast.type;

import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * The type of things that may be true or false.
 */
public class BooleanType extends Type {
	public BooleanType(int line, int column) {
		super(line, column);
        this.type = SymbolType.BOOLEAN;
	}
	@Override
	public String toString() {
		return "boolean";
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
