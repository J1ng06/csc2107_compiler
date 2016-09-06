package compiler488.ast.type;

import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Used to declare objects that yield integers.
 */
public class IntegerType extends Type {
	public IntegerType(int line, int column) {
		super(line, column);
		this.type = SymbolType.INTEGER;
	}
	@Override
	public String toString() {
		return "integer";
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
