package compiler488.ast.expn;

import compiler488.ast.BaseAST;
import compiler488.ast.Printable;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;

/**
 * A placeholder for all expressions.
 */
public abstract class Expn extends BaseAST implements Printable {
    
    //SymbolType expnType = null;
    public SymbolType symbolType = null;

	public Expn(int line, int column) {
		super(line, column);
        this.symbolType = SymbolType.UNKNOWN;
	}

    public SymbolType getSymbolType() {
        return this.symbolType;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

	public SymbolType getSymbolType(SymbolTable symbolTable, String ident) {
        try {
            Symbol symbol = symbolTable.lookup(ident);
            return symbol.getType();
        } catch (Exception e) {
           return SymbolType.UNKNOWN;
        }
	}
	
	public abstract SymbolType getExpnType(SymbolTable symbolTable);

}
