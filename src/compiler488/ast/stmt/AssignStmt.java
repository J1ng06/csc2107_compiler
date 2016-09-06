package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.Expn;
import compiler488.visitor.IVisitor;

/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
	/** The location being assigned to. */
	private Expn lval;

	/** The value being assigned. */
	private Expn rval;

	public AssignStmt(Expn lval, Expn rval, int line, int column) {
		super(line, column);

		this.lval = lval;
		this.rval = rval;
	}

	public Expn getLval() {
		return lval;
	}

	public Expn getRval() {
		return rval;
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		lval.prettyPrint(p);
		p.print(" : = ");
		rval.prettyPrint(p);
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ASTList<ReturnStmt> findReturnStmts(String type) {
        return new ASTList<>();
    }
}
