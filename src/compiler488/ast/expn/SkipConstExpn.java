package compiler488.ast.expn;

import compiler488.ast.Printable;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {
	public SkipConstExpn(int line, int column) {
		super(line, column);
	}

	@Override
	public String toString() {
		return "newline";
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SymbolType getExpnType(SymbolTable symbolTableStack) {
        return SymbolType.NEWLINE;
    }
}
