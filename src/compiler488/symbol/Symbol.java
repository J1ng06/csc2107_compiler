package compiler488.symbol;

import compiler488.ast.BaseAST;

public class Symbol implements ISymbol {

    private String name;
    private int depth;
    private int ll;
    private int on;
    private SymbolType symbolType = SymbolType.UNKNOWN;
    private SpecialType specialType = SpecialType.NONE;
    private BaseAST definingNode;

    public Symbol(String name, SymbolType symbolType, BaseAST definingNode) {
        this.name = name;
        this.symbolType = symbolType;
        this.definingNode = definingNode;
    }

    public Symbol(String name, SymbolType symbolType, BaseAST definingNode, SpecialType specialType) {
        this.name = name;
        this.symbolType = symbolType;
        this.definingNode = definingNode;
        this.specialType = specialType;
    }
    
    public Symbol(String name, SymbolType symbolType, BaseAST definingNode, SpecialType specialType, int ll, int on) {
        this.name = name;
        this.symbolType = symbolType;
        this.definingNode = definingNode;
        this.specialType = specialType;
        this.ll = ll;
        this.on = on;
    }

    @Override
    public BaseAST getDefiningNode() {
        return this.definingNode;
    }

    @Override
    public SymbolType getType() {
        return this.symbolType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public SpecialType getSpecialType() {
        return this.specialType;
    }
    
    public int getON() {
        return this.on;
    }
    
    public int getLL() {
        return this.ll;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

}
