package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.expn.Expn;
import compiler488.visitor.IVisitor;

/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {
	public WhileDoStmt(Expn expn, ASTList<Stmt> body, int line, int column) {
		super(expn, body, line, column);
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print("while ");
		expn.prettyPrint(p);
		p.println(" do");
		body.prettyPrintBlock(p);
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ASTList<ReturnStmt> findReturnStmts(String type) {
        ASTList<ReturnStmt> returnStmts = new ASTList<>();
        for (Stmt child : this.body) {
			returnStmts.addAll(child.findReturnStmts(type));
        }
        return returnStmts;
    }
}
