package compiler488.ast.expn;

import compiler488.ast.PrettyPrinter;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/** Represents a conditional expression (i.e., x>0?3:4). */
public class ConditionalExpn extends Expn {
	private Expn condition; // Evaluate this to decide which value to yield.

	private Expn trueValue; // The value is this when the condition is true.

	private Expn falseValue; // Otherwise, the value is this.

	public ConditionalExpn (Expn cond, Expn ifTrue, Expn ifFalse, int line, int column) {
		super(line, column);
		condition = cond;
		trueValue = ifTrue;
		falseValue = ifFalse;
	}
	/** Returns a string that describes the conditional expression. */
	@Override
	public String toString() {
		return "(" + condition + " ? " + trueValue + " : " + falseValue + ")";
	}

	public Expn getCondition() {
		return condition;
	}

	public void setCondition(Expn condition) {
		this.condition = condition;
	}

	public Expn getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(Expn falseValue) {
		this.falseValue = falseValue;
	}

	public Expn getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(Expn trueValue) {
		this.trueValue = trueValue;
	}
	
	@Override
    public void prettyPrint(PrettyPrinter p) {
        p.print("(");
        condition.prettyPrint(p);
        p.print(")");
        p.print(" ? " + trueValue + " : " + falseValue + ")");
    }
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

	@Override
	public SymbolType getExpnType(SymbolTable symbolTable) {
		if (this.symbolType == SymbolType.UNKNOWN){
			if (this.trueValue.getExpnType(symbolTable) == this.falseValue.getExpnType(symbolTable)){
				this.symbolType = this.trueValue.getExpnType(symbolTable);
			}else {
				this.symbolType = SymbolType.UNKNOWN;
			}
		}
		return this.symbolType;
	}
}
