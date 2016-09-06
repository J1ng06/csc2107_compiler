package compiler488.ast.type;

import compiler488.ast.BaseAST;
import compiler488.symbol.SymbolType;

/**
 * A placeholder for types.
 */
public abstract class Type extends BaseAST {

    public SymbolType type = SymbolType.UNKNOWN;

	public Type(int line, int column) {
		super(line, column);
	}

    public void setType(SymbolType type) {
        this.type = type;
    }

    public SymbolType getType() {
        return this.type;
    }
}
