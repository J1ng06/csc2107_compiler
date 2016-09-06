package compiler488.symbol;

import compiler488.ast.BaseAST;

public interface ISymbol {
    public String getName();
    public BaseAST getDefiningNode();
    public SymbolType getType();
}
