package compiler488.semantics;

import compiler488.ast.BaseAST;

/**
 * Exception to be thrown when a semantic error is found.
 *
 */
public class SemanticErrorException extends RuntimeException {
    
    private BaseAST node;
    
    public SemanticErrorException(String errorMessage, BaseAST astNode) {
        super(errorMessage);
        this.node = astNode;
    }
    
    public String getMessage() {
        String message = super.getMessage();
        
        if (message == null || message.isEmpty()) {
            message = String.format("\tError: Unspecified semantic error\n\t\t(line %d, column %d)",
                                    node.getLine(),
                                    node.getColumn());
            return message;
        } else {
            return  String.format("\tError: " + message + "\n\t\t(line %d, column %d)",
                                  node.getLine(), 
                                  node.getColumn());
        }
    }
}
