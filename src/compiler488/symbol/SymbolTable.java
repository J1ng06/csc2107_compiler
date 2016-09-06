package compiler488.symbol;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class SymbolTable implements ISymbolTable {

    private boolean isMajorScope;
    public int nestingLevel;
    private Hashtable<String, Stack<Symbol>> symbols;

    public SymbolTable() {
        // Created when in the entire program scope.
        this.isMajorScope = true;
        this.nestingLevel = -1;
        this.symbols = new Hashtable<>();

    }

    private void incrNestLevel() {
        this.nestingLevel++;
    }

    private void decrNestLevel() {
        this.nestingLevel--;
    }

    @Override
    public int getCurrentNestLevel() {
        return this.nestingLevel;
    }

    @Override
    public Symbol lookup(String id) {
        if (symbols.get(id) != null) {
            return symbols.get(id).peek();
        } else {
            return null;
        }
    }

    @Override
    public void enter(String id, Symbol symbol) throws Exception {
        Symbol oldSymbol = lookup(id);
        symbol.setDepth(this.nestingLevel);
        // Check for duplicates
        if ((oldSymbol != null) && oldSymbol.getDepth() == symbol.getDepth()) {
            String msg = String.format("Duplicate definition of %s", id);
            throw new Exception(msg);
        }

        //symbol.setDepth(this.nestingLevel);

        if (oldSymbol == null) {
            this.symbols.put(id, new Stack<Symbol>());
            this.symbols.get(id).push(symbol);
        } else {
            this.symbols.get(id).push(symbol);
        }
    }

    public boolean declaredLocally(String id) {
        Stack<Symbol> stack = this.symbols.get(id);
        if (stack == null) {
            return false;
        } else if (stack.peek().getDepth() == nestingLevel) {
            return true;
        }
        return false;
    }

    @Override
    public void openScope() {
        this.nestingLevel++;
    }

    @Override
    public void closeScope() {
        Iterator<Map.Entry<String, Stack<Symbol>>> iter = symbols.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Stack<Symbol>> entry = iter.next();
            // Handles the corner case for closing whole program scope.
            if ((entry.getValue().peek().getDepth() == this.nestingLevel)) {
                if (entry.getValue().size() == 1) {
                    // If it is the last item in the stack, remove the stack from symbols
                    iter.remove();
                } else {
                    entry.getValue().pop();
                }
            }
        }
        this.nestingLevel--;
    }

    public boolean isMajorScope() {
        return isMajorScope;
    }

    public void setMajorScope(boolean majorScope) {
        isMajorScope = majorScope;
    }


    // Original methods provided by starter code.
    public void Initialize() {
    }

    public void Finalize() {
    }
}
