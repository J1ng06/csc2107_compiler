package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn {
	/**
	 * The value of this literal.
	 */
	private Integer value;

	public IntConstExpn(Integer value, int line, int column) {
		super(line, column);

		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public SymbolType getExpnType(SymbolTable symbolTableStack) {
		return SymbolType.INTEGER;
	}
}
