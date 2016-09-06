package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {
    
    private Expn operand;
    
	public UnaryMinusExpn(Expn operand, int line, int column) {
		super(UnaryExpn.OP_MINUS, operand, line, column);
		
		this.operand = operand;
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
	
	public Expn getOperand() {
	    return operand;
	}

    @Override
    public SymbolType getExpnType(SymbolTable symbolTableStack) {
        if (this.symbolType== SymbolType.UNKNOWN) {
            if (this.getOperand().getExpnType(symbolTableStack) == SymbolType.INTEGER) {
                this.symbolType = SymbolType.INTEGER;
            }
            else {
                this.symbolType = SymbolType.UNKNOWN;
            }
        }

        return this.symbolType;
    }
}
