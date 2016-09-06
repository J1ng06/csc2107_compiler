package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {
    
    private Expn operand;
    
	public NotExpn(Expn operand, int line, int column) {
		super(UnaryExpn.OP_NOT, operand, line, column);
		
		this.operand = operand;
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SymbolType getExpnType(SymbolTable symbolTable) {
        if (this.symbolType == SymbolType.UNKNOWN) {
            if (this.getOperand().getExpnType(symbolTable) == SymbolType.BOOLEAN) {
                this.symbolType = SymbolType.BOOLEAN;
            } else {
                this.symbolType = SymbolType.UNKNOWN;
            }
        }
        return this.symbolType;
    }
}
