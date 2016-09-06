package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.decl.Declaration;
import compiler488.symbol.SymbolTable;
import compiler488.visitor.IVisitor;

/**
 * Represents the declarations and statements of a scope construct.
 */
public class Scope extends Stmt {
	/** Body of the scope, optional declarations, optional statements */
	protected ASTList<Declaration> declarations;
	protected ASTList<Stmt> statements;
	private SymbolTable symbolTable;

	
	public void setSymbolTable(SymbolTable symbolTable) {
	    this.symbolTable = symbolTable;
	}

	public Scope(int line, int column) {
		super(line, column);
		declarations = new ASTList<Declaration>();
		statements = new ASTList<Stmt>();
	}
	
	public Scope(ASTList<Declaration> decl, ASTList<Stmt> statements, int line, int column){
		super(line, column);
		this.setDeclarations(decl);
		this.setStatements(statements);
	}

	public void setDeclarations(ASTList<Declaration> declarations){
		this.declarations = declarations;
	}

	public ASTList<Declaration> getDeclarations() {
		return declarations;
	}

	public void setStatements(ASTList<Stmt> statements) {
		this.statements = statements;
	}

	public ASTList<Stmt> getStatements() {
		return statements;
	}

	public Scope appendStmt(Stmt s) {
		statements.append(s);
		return this;
	}

	public Scope appendDecl(Declaration decl) {
		declarations.append(decl);
		return this;
	}
	
	@Override
	public ASTList<ReturnStmt> findReturnStmts(String type) {
		ASTList<ReturnStmt> returnStmts = new ASTList<>();
		for (Stmt child : this.statements) {
            returnStmts.addAll(child.findReturnStmts(type));
		}
        if (returnStmts == null) {
            return new ASTList<>();
        }
		return returnStmts;
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.println(" { ");
		if (declarations != null && declarations.size() > 0) {
			declarations.prettyPrintBlock(p);
		}
		if (statements != null && statements.size() > 0) {
			statements.prettyPrintBlock(p);
		}
		p.print(" } ");
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
