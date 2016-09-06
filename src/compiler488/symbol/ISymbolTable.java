package compiler488.symbol;

public interface ISymbolTable {
    public int getCurrentNestLevel();
    public Symbol lookup(String id) throws Exception;
    public void enter(String id, Symbol symbol) throws Exception;
    public boolean declaredLocally(String name);
    public void openScope();
    public void closeScope();
}
