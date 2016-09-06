package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.visitor.IVisitor;

/**
 * Represents the command to exit from a loop.
 */
public class ExitStmt extends Stmt {
	/** Condition for 'exit when'. */
	private Expn expn = null;

	/** Number of levels to exit. */
	private Integer level = -1;

	public ExitStmt(Expn cond, Integer level, int line, int column){
		super(line, column);
		expn = cond;
		this.level = level;
	}
	/**
	 * Returns the string <b>"exit"</b> or <b>"exit when e"</b>" or
	 * <b>"exit"</b> level or <b>"exit"</b> level when e
	 */
	@Override
	public String toString() {
		String stmt = "exit";

		if (level >= 0) {
			stmt = " " + stmt + level;
		}

		if (expn != null) {
			stmt = " " + stmt + "when " + expn + " ";
		}

		return stmt;
	}

	public Expn getExpn() {
		return expn;
	}

	public void setExpn(Expn expn) {
		this.expn = expn;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
