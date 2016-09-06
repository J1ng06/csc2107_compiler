package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {
    public final static String OP_OR 	= "or";
    public final static String OP_AND	= "and";


    public BoolExpn(String opSymbol, Expn left, Expn right, int line, int column) {
        super(opSymbol, left, right, line, column);

        assert ((opSymbol == OP_OR) ||
                (opSymbol == OP_AND));
    }


    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public SymbolType getExpnType(SymbolTable symbolTable) {
        if (this.symbolType == SymbolType.UNKNOWN) {
            if (this.left.getExpnType(symbolTable) == SymbolType.BOOLEAN &&
                    this.right.getExpnType(symbolTable) == SymbolType.BOOLEAN) {
                this.symbolType = SymbolType.BOOLEAN;
            } else {
                this.symbolType = SymbolType.UNKNOWN;
            }
        }
        return this.symbolType;
    }
}
