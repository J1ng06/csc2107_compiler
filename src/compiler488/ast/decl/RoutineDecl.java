package compiler488.ast.decl;

import compiler488.ast.ASTList;
import compiler488.ast.PrettyPrinter;
import compiler488.ast.stmt.Scope;
import compiler488.ast.type.Type;
import compiler488.symbol.SymbolTable;
import compiler488.visitor.IVisitor;

/**
 * Represents the declaration of a function or procedure.
 */
public class RoutineDecl extends Declaration {
	/**
	 * The formal parameters for this routine (if any.)
	 *
	 * <p>
	 * This value must be non-<code>null</code>. If absent, use an empty list
	 * instead.
	 * </p>
	 */
	private ASTList<ScalarDecl> parameters = new ASTList<ScalarDecl>();

	/** The body of this routine (if any.) */
	private Scope body = null;
	private boolean isFunction = false;
	private SymbolTable symbolTable;
	private int paramCount;	

    /**
	 * Construct a function with parameters, and a definition of the body.
	 *
	 * @param name
	 *            Name of the routine
	 * @param type
	 *            Type returned by the function
	 * @param parameters
	 *            List of parameters to the routine
	 * @param body
	 *            Body scope for the routine
	 */
	public RoutineDecl(String name, Type type, ASTList<ScalarDecl> parameters, Scope body, int line, int column) {
		super(name, type, line, column);

		this.parameters = parameters;
		this.isFunction = true;
		this.body = body;
		this.paramCount = parameters.size();
	}

	/**
	 * Construct a function with no parameters, and a definition of the body.
	 *
	 * @param name
	 *            Name of the routine
	 * @param type
	 *            Type returned by the function
	 * @param body
	 *            Body scope for the routine
	 */
	public RoutineDecl(String name, Type type, Scope body, int line, int column) {
		this(name, type, new ASTList<ScalarDecl>(), body, line, column);
		this.paramCount = 0;
		this.isFunction = true;
	}

	/**
	 * Construct a procedure with parameters, and a definition of the body.
	 *
	 * @param name
	 *            Name of the routine
	 * @param parameters
	 *            List of parameters to the routine
	 * @param body
	 *            Body scope for the routine
	 */
	public RoutineDecl(String name, ASTList<ScalarDecl> parameters, Scope body, int line, int column) {
		this(name, null, parameters, body, line, column);

		this.parameters = parameters;
		this.body = body;
		this.isFunction = false;
		this.paramCount = parameters.size();
	}

	/**
	 * Construct a procedure with no parameters, and a definition of the body.
	 *
	 * @param name
	 *            Name of the routine
	 * @param body
	 *            Body scope for the routine
	 */
	public RoutineDecl(String name, Scope body, int line, int column) {
		this(name, null, new ASTList<ScalarDecl>(), body, line, column);
		this.paramCount = 0;
		this.isFunction = false;
	}

	public ASTList<ScalarDecl> getParameters() {
		return parameters;
	}

	public boolean isFunction() {
        return isFunction;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
    
    public void setSymbolTable(SymbolTable st) {
        this.symbolTable = st;
    }

    public Scope getBody() {
		return body;
	}
    
    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }
    
	@Override
	public void prettyPrint(PrettyPrinter p) {
		if (type == null) {
			p.print("procedure ");
		} else {
			p.print(" function ");
		}

		p.print(name);

		if (parameters.size() > 0) {
			p.print("(");
			parameters.prettyPrintCommas(p);
			p.print(")");
		}
		/* print type of function */
		if (type != null) {
			p.print(" : ");
			type.prettyPrint(p);
		}
		if (body != null) {
			p.print(" ");
			body.prettyPrint(p);
		}
	}
	
	@Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
