package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.Expn;
import compiler488.visitor.IVisitor;

/**
 * The command to return from a function.
 */
public class ReturnStmt extends Stmt {
	/* The value to be returned by the function (if any.) */
	private Expn value = null;
	private String expectedReturnType = "unknown";

	/**
	 * Construct a function <code>return <em>value</em></code> statement with a
	 * value expression.
	 *
	 * @param value
	 *            AST for the return expression
	 */
	public ReturnStmt(Expn value, int line, int column) {
		super(line, column);

		this.value = value;
	}

	/**
	 * Construct a procedure <code>return</code> statement (with no return
	 * value)
	 */
	public ReturnStmt(int line, int column) {
		this(null, line, column);
	}

	public Expn getValue() {
		return value;
	}

	public void prettyPrint(PrettyPrinter p) {
		p.print("return");

		if (value != null) {
			p.print(" with ");
			value.prettyPrint(p);
		}
	}
	
	public String getReturnType(){
	    return this.expectedReturnType;
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ASTList<ReturnStmt> findReturnStmts(String type) {
        this.expectedReturnType = type;
		ASTList<ReturnStmt> returnStmts = new ASTList<>();
        returnStmts.add(this);
        return returnStmts;
    }
}
