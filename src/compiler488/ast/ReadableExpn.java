package compiler488.ast;
import compiler488.ast.expn.Expn;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitable;
import compiler488.visitor.IVisitor;

public class ReadableExpn implements Readable, IVisitable {

	private Expn varExpn;
    private SymbolType type = SymbolType.UNKNOWN;
    
	public ReadableExpn(Expn variable) {
		setVarExpn(variable);
	}
	@Override
	public void prettyPrint(PrettyPrinter p) {
	    varExpn.prettyPrint(p);
	}
	public Expn getVarExpn() {
		return varExpn;
	}
	public void setVarExpn(Expn varExpn) {
		this.varExpn = varExpn;
	}
	@Override
	public int getLine() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getColumn() {
		// TODO Auto-generated method stub
		return 0;
	}
    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    /*@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }*/
}
