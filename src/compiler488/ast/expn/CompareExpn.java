package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Place holder for all ordered comparisons expression where both operands must
 * be integer expressions. e.g. &lt; , &gt;  etc. comparisons
 */
public class CompareExpn extends BinaryExpn {
    public final static String OP_LESS 			= "<";
    public final static String OP_LESS_EQUAL 	= "<=";
    public final static String OP_GREATER 		= ">";
    public final static String OP_GREATER_EQUAL	= ">=";

    public CompareExpn(String opSymbol, Expn left, Expn right, int line, int column) {
        super(opSymbol, left, right, line, column);

        assert ((opSymbol == OP_LESS) ||
                (opSymbol == OP_LESS_EQUAL) ||
                (opSymbol == OP_GREATER) ||
                (opSymbol == OP_GREATER_EQUAL));
    }
    
    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public SymbolType getExpnType(SymbolTable symbolTable) {
		if (this.symbolType == SymbolType.UNKNOWN) {
			if (this.left.getExpnType(symbolTable) == SymbolType.INTEGER &&
					this.right.getExpnType(symbolTable) == SymbolType.INTEGER) {
				this.symbolType = SymbolType.BOOLEAN;
			}
			else {
				this.symbolType = SymbolType.UNKNOWN;
			}
		}

		return this.symbolType;
	}
}

