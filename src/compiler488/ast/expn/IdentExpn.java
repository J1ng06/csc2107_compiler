package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * References to a scalar variable or function call without parameters.
 */
public class IdentExpn extends Expn implements Readable {
	/** Name of the identifier. */
	private String ident;

	public IdentExpn(String ident, int line, int column) {
		super(line, column);

		this.ident = ident;
	}

	public String getIdent() {
		return ident;
	}

	/**
	 * Returns the name of the variable or function.
	 */
	@Override
	public String toString() {
		return ident;
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public SymbolType getExpnType(SymbolTable symbolTable) {
		if (this.symbolType == SymbolType.UNKNOWN) {
			this.symbolType = this.getSymbolType(symbolTable, this.ident);
		}
		return this.symbolType;
	}
}
