package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
	/** The value of the constant */
	private boolean value;

	public BoolConstExpn(boolean value, int line, int column) {
		super(line, column);

		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value ? "true" : "false";
	}

	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SymbolType getExpnType(SymbolTable symbolTable) {
        return SymbolType.BOOLEAN;
    }
}
