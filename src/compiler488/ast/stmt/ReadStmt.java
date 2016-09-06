package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.ReadableExpn;
import compiler488.visitor.IVisitor;

/**
 * The command to read data into one or more variables.
 */
public class ReadStmt extends Stmt {
	/** A list of locations to put the values read. */
	private ASTList<ReadableExpn> inputs;

	public ReadStmt(ASTList<ReadableExpn> inputs, int line, int column) {
		super(line, column);
		this.inputs = inputs;
	}

	@Override
	public void prettyPrint(PrettyPrinter p) {
		p.print("read ");
		inputs.prettyPrintCommas(p);
	}

	public ASTList<ReadableExpn> getInputs() {
		return inputs;
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
