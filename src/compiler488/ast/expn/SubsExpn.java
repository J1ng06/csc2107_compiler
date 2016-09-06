package compiler488.ast.expn;

import compiler488.ast.PrettyPrinter;
import compiler488.ast.Readable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * References to an array element variable
 */
public class SubsExpn extends Expn implements Readable {
	/** Name of the array variable. */
	private String variable;

	/** First subscript. */
	private Expn subscript1;

	/** Second subscript (if any.) */
	private Expn subscript2 = null;

	/** Subscript 2 dimensional array */
	public SubsExpn(String variable, Expn subscript1, Expn subscript2, int line, int column) {
		super(line, column);

		this.variable = variable;
		this.subscript1 = subscript1;
		this.subscript2 = subscript2;
	}

	/** Subscript 1 dimensional array */
	public SubsExpn(String variable, Expn subscript1, int line, int column) {
		this(variable, subscript1, null, line, column);
	}

	public String getVariable() {
		return variable;
	}

	public Expn getSubscript1() {
		return subscript1;
	}

	public Expn getSubscript2() {
		return subscript2;
	}

	public int numSubscripts() {
		return 1 + (subscript2 != null ? 1 : 0);
	}
	
	@Override
	public String toString() {
	    return variable;
	}

	public void prettyPrint(PrettyPrinter p) {
		p.print(variable + "[");

		subscript1.prettyPrint(p);

		if (subscript2 != null) {
			p.print(", ");
			subscript2.prettyPrint(p);
		}

		p.print("]");
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SymbolType getExpnType(SymbolTable symbolTableStack) {
        if (this.symbolType == SymbolType.UNKNOWN) {
            this.symbolType = this.getSymbolType(symbolTableStack, this.variable);
        }
        return this.symbolType;
    }
}
