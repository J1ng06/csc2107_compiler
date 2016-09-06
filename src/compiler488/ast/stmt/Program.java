package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.visitor.IVisitor;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {
	public Program (Scope scope, int left, int right){
		super(scope.getDeclarations(), scope.getStatements(),
				left, right);
	}

	@Override
	public ASTList<ReturnStmt> findReturnStmts(String type) {
		// Don't need to bother with this. A program can never be within a
        // function decl
		return null;
	}

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
