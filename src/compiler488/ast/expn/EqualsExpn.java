package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Place holder for all binary expression where both operands could be either
 * integer or boolean expressions. e.g. = and != comparisons
 */
public class EqualsExpn extends BinaryExpn {
    public final static String OP_EQUAL 	= "=";
    public final static String OP_NOT_EQUAL	= "not =";

    public EqualsExpn(String opSymbol, Expn left, Expn right, int line, int column) {
        super(opSymbol, left, right, line, column);

        assert ((opSymbol == OP_EQUAL) ||
                (opSymbol == OP_NOT_EQUAL));
    }
    
    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
	
	@Override
	public SymbolType getExpnType(SymbolTable symbolTable) {
		if (this.symbolType == SymbolType.UNKNOWN) {
			SymbolType leftType = this.left.getExpnType(symbolTable);
			boolean typesMatch = (leftType == this.right.getExpnType(symbolTable));
			boolean eligibleType = (leftType == SymbolType.INTEGER || leftType == SymbolType.BOOLEAN);
			if (typesMatch && eligibleType) {
				this.symbolType = SymbolType.BOOLEAN;
			} else {
				this.symbolType = SymbolType.UNKNOWN;
			}
		}
		return this.symbolType;
	}
}
