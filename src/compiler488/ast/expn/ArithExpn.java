package compiler488.ast.expn;

import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {
    public final static String OP_PLUS 		= "+";
    public final static String OP_MINUS 	= "-";
    public final static String OP_TIMES 	= "*";
    public final static String OP_DIVIDE 	= "/";

    public ArithExpn(String opSymbol, Expn left, Expn right, int line, int column) {
        super(opSymbol, left, right, line, column);

        assert ((opSymbol == OP_PLUS) ||
                (opSymbol == OP_MINUS) ||
                (opSymbol == OP_TIMES) ||
                (opSymbol == OP_DIVIDE));
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SymbolType getExpnType(SymbolTable symbolTable) {
        if (this.symbolType == SymbolType.UNKNOWN) {
            if (this.left.getExpnType(symbolTable) == SymbolType.INTEGER &&
                    this.right.getExpnType(symbolTable) == SymbolType.INTEGER) {
                this.symbolType = SymbolType.INTEGER;
            } else {
                this.symbolType = SymbolType.UNKNOWN;
            }
          }

          return this.symbolType;
    }

}
