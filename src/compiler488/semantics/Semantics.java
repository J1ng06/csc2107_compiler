package compiler488.semantics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler488.ast.ASTList;
import compiler488.ast.BaseAST;
import compiler488.ast.Printable;
import compiler488.ast.ReadableExpn;
import compiler488.ast.decl.ArrayBoundPart;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.expn.ArithExpn;
import compiler488.ast.expn.BoolConstExpn;
import compiler488.ast.expn.BoolExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.ConditionalExpn;
import compiler488.ast.expn.EqualsExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.expn.NotExpn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.expn.UnaryMinusExpn;
import compiler488.ast.stmt.AssignStmt;
import compiler488.ast.stmt.ExitStmt;
import compiler488.ast.stmt.IfStmt;
import compiler488.ast.stmt.ProcedureCallStmt;
import compiler488.ast.stmt.Program;
import compiler488.ast.stmt.ReadStmt;
import compiler488.ast.stmt.RepeatUntilStmt;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.stmt.WriteStmt;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.symbol.SpecialType;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;

// TODO - get rid of method getExpnType and do it the proper way

/**
 * Implement semantic analysis for compiler 488
 *
 * @author <B> Put your names here </B>
 */
public class Semantics implements IVisitor {
    /** Flag for tracing semantic analysis */
    private boolean traceSemantics = false;

    /** File sink for semantic analysis trace */
    private String traceFile = new String();

    public FileWriter Tracer;
    public File f;

    private SymbolTable symbolTable;
    private LinkedList<SemanticErrorException> errorList;
    private int loopLevel = 0;

    /** Regex pattern for matching valid identifiers. Idents start with
     * upper/lower case letters and may contain letters or digits as well
     * as underscore.
     */
    Pattern identPattern = Pattern.compile("\\b([A-Za-z][A-Za-z0-9_]*)\\b");

    public Semantics() {
        // S00
        this.symbolTable = new SymbolTable();
        this.errorList = new LinkedList<SemanticErrorException>();
    }

    public LinkedList<SemanticErrorException> getErrorMessages() {
        return errorList;
    }

    /**
     * Initialize semantic analysis - called once by the parser at the start of
     * compilation
     */
    void Initialize() {
    }

    /**
     * SemanticsFinalize - called by the parser once at the end of compilation
     */
    void Finalize() {
    }

    /**
     * Perform one semantic analysis action
     *
     * @param actionNumber
     *            semantic analysis action number
     */
    void semanticAction(int actionNumber, BaseAST visitable) {
        if (traceSemantics) {
            if (traceFile.length() > 0) {
                // output trace to the file represented by traceFile
                try {
                    // open the file for writing and append to it
                    File f = new File(traceFile);
                    Tracer = new FileWriter(traceFile, true);

                    Tracer.write("Sematics: S" + actionNumber + "\n");
                    // always be sure to close the file
                    Tracer.close();
                } catch (IOException e) {
//                    System.out.println(traceFile + " could be opened/created.  It may be in use.");
                }
            } else {
                // output the trace to standard out.
//                System.out.println("Sematics: S" + actionNumber);
            }

        }

        // System.out.println("Semantic Action: S" + actionNumber);
    }

    /**
     * Determines if an identifier being declared matches the regular expression
     * for valid identifiers in the language.
     * @param ident
     *      The identifer to check.
     * @return
     *      whether ident is valid.
     */
    private boolean isValidIdent(String ident) {
        Matcher m = identPattern.matcher(ident);
        return m.matches();
    }

    // TODO : docstring
    private SymbolType makeSymbolType(Declaration decl) {
        SymbolType declType;
        if (decl.getType() == null) {
            return SymbolType.NONE;
        }
        if (decl.getType().toString() == "integer") {
            declType = SymbolType.INTEGER;
        } else if (decl.getType().toString() == "boolean") {
            declType = SymbolType.BOOLEAN;
        } else {
            declType = SymbolType.NONE;
        }
        return declType;
    }

    // TODO - docstring
    private void duplicateDefinitionError(Declaration decl) {
        String msg = String.format("Duplicate definition of %s in local scope.",
                decl.getName());
                errorList.add(new SemanticErrorException(msg, decl));
    }
    private void duplicateDefinitionError(DeclarationPart decl) {
        String msg = String.format("Duplicate definition of %s in local scope.",
                decl.getName());
        errorList.add(new SemanticErrorException(msg, decl));
    }

    // TODO
    private void invalidIdentError(Declaration decl) {
        String msg = String.format("%s is not a valid identifier name", decl.getName());
        errorList.add(new SemanticErrorException(msg, decl));
    }
    private void invalidIdentError(DeclarationPart decl) {
        String msg = String.format("%s is not a valid identifier name", decl.getName());
        errorList.add(new SemanticErrorException(msg, decl));
    }

    @Override
    public void visit(Scope scope) {
//        System.out.println("In Scope");
        symbolTable.openScope();
        // scope.getStatements().isEmpty());
        if (scope.getDeclarations() != null) {
            for (Declaration decl : scope.getDeclarations()) {
                decl.accept(this);
            }
        }
        if (scope.getStatements() != null) {
            // System.out.println("In scope if stmt");
            for (Stmt stmt : scope.getStatements()) {
                stmt.accept(this);
            }
        }
        scope.setSymbolTable(symbolTable);
        symbolTable.closeScope();
    }

    @Override
    public void visit(ArrayBoundPart arrayBoundPart) {
        int lb = arrayBoundPart.getLb();
        int ub = arrayBoundPart.getUb();

        if (lb > ub) {
            String msg = "Array upper bound must be larger than lower bound.";
            errorList.add(new SemanticErrorException(msg, arrayBoundPart));
        }
    }

    @Override
    public void visit(ArrayDeclPart arrayDeclPart) {
        /* We do the semantic action of declaring the array within
         * MultiDeclarations because we need access to its type.
         */
        arrayDeclPart.getBound1().accept(this);
        if (arrayDeclPart.isTwoDim()) {
            arrayDeclPart.getBound2().accept(this);
        }

    }

    @Override
    public void visit(MultiDeclarations multiDeclarations) {
        // S47
        // System.out.println("In multiDeclarations");
        for (DeclarationPart declPart : multiDeclarations.getParts()) {
            if (!isValidIdent(declPart.getName())) {
                invalidIdentError(declPart);
            }
            if (declPart.isArray()) {
                Symbol newSymbol = new Symbol(declPart.getName(),
                        makeSymbolType(multiDeclarations),
                        multiDeclarations, SpecialType.ARRAY);
                try {
                    symbolTable.enter(declPart.getName(), newSymbol);
                } catch (Exception e) {
                    duplicateDefinitionError(declPart);
                }
                declPart.accept(this);
            } else {
                Symbol newSymbol = new Symbol(declPart.getName(),
                        makeSymbolType(multiDeclarations),
                        multiDeclarations);
                try {
                    symbolTable.enter(declPart.getName(), newSymbol);
                } catch (Exception e) {
                    duplicateDefinitionError(declPart);
                }
                /* Visiting scalarDeclPart is unnecessary as its semantic
                 * actions are being handled here.
                 */
            }
        }
    }

    @Override
    public void visit(RoutineDecl routineDecl) {
        String msg;

        if (!isValidIdent(routineDecl.getName())) {
            invalidIdentError(routineDecl);
        }
        // S54
        Scope routineScope = routineDecl.getBody();
        // TODO: edit findReturnStmts for reachability
        ASTList<ReturnStmt> returnStatements = routineScope.findReturnStmts(
                (routineDecl.getType() == null) ? "none" : routineDecl.getType().toString());

        if (returnStatements.isEmpty() && routineDecl.isFunction()) {
            msg = String.format("function %s was declared without a reachable return statement.",
                                routineDecl.getName());
            errorList.add(new SemanticErrorException(msg, routineDecl));
        }

        // S11 S12
        Symbol routine = new Symbol(routineDecl.getName(), makeSymbolType(routineDecl), routineDecl,
                routineDecl.isFunction() ? SpecialType.FUNCTION : SpecialType.PROCEDURE);
        try {
            symbolTable.enter(routineDecl.getName(), routine);
        } catch (Exception e) {
            duplicateDefinitionError(routineDecl);
        }

        // S13 - Associate scope with function/procedure.
        symbolTable.openScope();
        // Enter parameter symbols in the new scope
        for (ScalarDecl scalarDecl : routineDecl.getParameters()) {
            scalarDecl.accept(this);
        }
        routineDecl.getBody().accept(this);
        // Associate the state of the symbol table with the function
        routineDecl.setSymbolTable(this.symbolTable);
        symbolTable.closeScope();
    }

    @Override
    public void visit(ScalarDecl scalarDecl) {
        Symbol parameter = new Symbol(scalarDecl.getName(), makeSymbolType(scalarDecl), scalarDecl);
        if (!isValidIdent(scalarDecl.getName())) {
            invalidIdentError(scalarDecl);
        }
        try {
            symbolTable.enter(scalarDecl.getName(), parameter);
        } catch (Exception e) {
            duplicateDefinitionError(scalarDecl);
        }
    }

    @Override
    public void visit(ScalarDeclPart scalarDeclPart) {
        /* ScalarDeclPart is handled within the MultiDeclaration visitor as we
         * that is the only way we will have access to the declaration type for
         * symbol entry.
         */
    }

    @Override
    public void visit(AssignStmt assignStmt) {      // TODO - this should probably accept expressions
        Expn lVal = assignStmt.getLval();           // TODO - first rather than use getExpnType.
        Expn rVal = assignStmt.getRval();
        rVal.accept(this);
        lVal.accept(this);

        try {
            symbolTable.lookup(lVal.toString());    // TODO - lookup no longer throws.
        } catch (Exception e) {
            String msg = String.format("%s has not been declared", lVal.toString());
            errorList.add(new SemanticErrorException(msg, lVal));
        }

        SymbolType lType = lVal.getExpnType(symbolTable);
        SymbolType rType = rVal.getExpnType(symbolTable);

        if (lType != rType) {
            String msg = String.format("Can't assign %s type to %s type.",
                                       lType.toString(), rType.toString());
            errorList.add(new SemanticErrorException(msg, assignStmt));
        }
    }
    
    @Override
    public void visit(ExitStmt exitStmt) {
        String msg;
        // S53 S50
        if (exitStmt.getLevel() > this.loopLevel) {
           msg = String.format("trying to exit %d loops, but current loop level is %d.",
                               exitStmt.getLevel(), this.loopLevel);
           errorList.add(new SemanticErrorException(msg, exitStmt));
        }
        // S30
        Expn expn = exitStmt.getExpn();
        expn.accept(this);
        if ((exitStmt.getExpn() != null) && (expn.getSymbolType() != SymbolType.BOOLEAN)) {
            msg = String.format("Expression %s in exit statement must be boolean type.",
                    exitStmt.getExpn().toString());
            errorList.add(new SemanticErrorException(msg, exitStmt.getExpn()));
        }
    }

    @Override
    public void visit(IfStmt ifStmt) {
        Expn cond = ifStmt.getCondition();
        cond.accept(this);

        if (cond.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = "Valid boolean expression expected within if statement";
            errorList.add(new SemanticErrorException(msg, ifStmt));
        }

        ifStmt.getWhenTrue().accept(this);
        if (ifStmt.getWhenFalse() != null) {
            ifStmt.getWhenFalse().accept(this);
        }
    }

    @Override
    public void visit(ProcedureCallStmt procedureCallStmt) {
        Symbol procedureSymbol = symbolTable.lookup(procedureCallStmt.getName());
        if (procedureSymbol == null) {
            String msg = String.format("Procedure %s has not been declared.",
                    procedureCallStmt.getName());
            errorList.add(new SemanticErrorException(msg, procedureCallStmt));
        } else if (procedureSymbol.getSpecialType() == SpecialType.NONE) {
            String msg = String.format("Identifier \"%s\" is not a function!",
                    procedureCallStmt.getName());
            errorList.add(new SemanticErrorException(msg, procedureCallStmt));
        } else {
            RoutineDecl definingNode = (RoutineDecl) procedureSymbol.getDefiningNode();
            if (definingNode.getParamCount() != procedureCallStmt.getArguments().size()) {
                String msg = String.format("Procedure %s takes %d arguments but got %d arguments",
                        procedureCallStmt.getName(),
                        definingNode.getParamCount(),
                        procedureCallStmt.getArguments().size());
                errorList.add(new SemanticErrorException(msg, procedureCallStmt));
            }else {
            	for (int i = 0; i < definingNode.getParamCount(); i++){
            		if (definingNode.getParameters().get(i).getSymbolType() != procedureCallStmt.getArguments().get(i).getExpnType(symbolTable)){
            			String msg1 = String.format("Function %s takes No.%d argument as %s type, but got %s type",
            					procedureCallStmt.getName(),
                                i+1,
                                definingNode.getParameters().get(i).getSymbolType(),
                                procedureCallStmt.getArguments().get(i).getExpnType(symbolTable));
            			errorList.add(new SemanticErrorException(msg1, procedureCallStmt));
            		}
            	}
            }
        }
    }

    @Override
    public void visit(Program program) {
        symbolTable.openScope();
        // scope.getStatements().isEmpty());
        if (program.getDeclarations() != null) {
            for (Declaration decl : program.getDeclarations()) {
                decl.accept(this);
            }
        }
        if (program.getStatements() != null) {
            // System.out.println("In scope if stmt");
            for (Stmt stmt : program.getStatements()) {
                stmt.accept(this);
            }
        }
        symbolTable.closeScope();
    }
    


    @Override
    public void visit(RepeatUntilStmt repeatUntilStmt) {
        Expn cond = repeatUntilStmt.getExpn();
        cond.accept(this);
        if (cond.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = String.format("Expression %s in Repeat statement must be type boolean.",
                    cond.toString());
            errorList.add(new SemanticErrorException(msg, repeatUntilStmt));
        }
    }

    @Override
    public void visit(ReturnStmt returnStmt) {

        // returnType field for returnStmt would have been set in the routine
        // declaration, otherwise if it is outside a routine, it will be null.
        SymbolType retType;
        switch (returnStmt.getReturnType()) {
            case "integer":
                retType = SymbolType.INTEGER;
                break;
            case "boolean":
                retType = SymbolType.BOOLEAN;
                break;
            case "none":
                retType = SymbolType.NONE;
                break;
            default:
                retType = null;
                String msg = "Return statement occurs outside of function/procedure.";
                errorList.add(new SemanticErrorException(msg, returnStmt));
        }

        Expn expn = returnStmt.getValue();
        if (expn != null) {
            expn.accept(this);
            if (retType != null) {
                if (expn.getSymbolType() != retType) {
                    String msg = "Expression is incompatible with routine return type.";
                    errorList.add(new SemanticErrorException(msg, expn));
                }
            }
        }
    }
    
    @Override
    public void visit(WhileDoStmt whileDoStmt) {
        this.loopLevel += 1;
        Expn cond = whileDoStmt.getExpn();
        cond.accept(this);
        if (cond.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = String.format("Expression %s in while-do statement must be boolean",
                                       cond.toString());
            errorList.add(new SemanticErrorException(msg, whileDoStmt));
        }

        for (Stmt stmt : whileDoStmt.getBody()) {
            stmt.accept(this);
        }
        this.loopLevel -= 1;
    }

    @Override
    public void visit(ArithExpn arithExpn) {
        Expn left = arithExpn.getLeft();
        Expn right = arithExpn.getRight();
        left.accept(this);
        right.accept(this);
        if (left.getSymbolType() != SymbolType.INTEGER) {
            String msg = String.format("Expression %s must be integer type.",
                                       left.toString());
            errorList.add(new SemanticErrorException(msg, left));
        }
        if (right.getSymbolType() != SymbolType.INTEGER) {
            String msg = String.format("Expression %s must be integer type.",
                                       right.toString());
            errorList.add(new SemanticErrorException(msg, right));
        }
        arithExpn.setSymbolType(SymbolType.INTEGER);
    }
    
    @Override
    public void visit(BoolConstExpn boolConstExpn) {
        boolConstExpn.setSymbolType(SymbolType.BOOLEAN);
    }

    @Override
    public void visit(BoolExpn boolExpn) {
        Expn left = boolExpn.getLeft();
        Expn right = boolExpn.getRight();
        left.accept(this);
        right.accept(this);
        if (left.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = String.format("Expression %s in boolean expression must be boolean type",
                                       left.toString());
            errorList.add(new SemanticErrorException(msg, left));
        }
        if (right.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = String.format("Expression %s in boolean expression must be boolean type",
                                       right.toString());
            errorList.add(new SemanticErrorException(msg, left));
        }
        boolExpn.setSymbolType(SymbolType.BOOLEAN);
    }

    @Override
    public void visit(CompareExpn compareExpn) {
        Expn left = compareExpn.getLeft();
        Expn right = compareExpn.getRight();
        left.accept(this);
        right.accept(this);
        if (left.getSymbolType() != SymbolType.INTEGER) {
            String msg = String.format("Expression %s in comparison expression must be type integer.",
                                       left.toString());
            errorList.add(new SemanticErrorException(msg, left));
        }
        if (right.getSymbolType() != SymbolType.INTEGER) {
            String msg = String.format("Expression %s in comparison expression must be type integer.",
                    right.toString());
            errorList.add(new SemanticErrorException(msg, right));
        }
        compareExpn.setSymbolType(SymbolType.BOOLEAN);
    }
    
    @Override
    public void visit(ConditionalExpn conditionalExpn) {
        Expn condition = conditionalExpn.getCondition();
        Expn falseVal = conditionalExpn.getFalseValue();
        Expn trueVal = conditionalExpn.getTrueValue();
        condition.accept(this);
        falseVal.accept(this);
        trueVal.accept(this);

        if (condition.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = String.format("Condition %s in conditional expression must be type boolean.",
                                       condition.toString());
            errorList.add(new SemanticErrorException(msg, condition));
        }

        if (!(falseVal.getSymbolType() == trueVal.getSymbolType())) {
            String msg = String.format("Type of expression %s does not match type of expression %s in" +
                                       " conditional expression.",
                                       falseVal.toString(),
                                       trueVal.toString());
            errorList.add(new SemanticErrorException(msg, conditionalExpn));
        }

        if (falseVal.getSymbolType() == trueVal.getSymbolType()) {
            conditionalExpn.setSymbolType(falseVal.getSymbolType());
        }
    }


    @Override
    public void visit(EqualsExpn equalsExpn) {
        Expn left = equalsExpn.getLeft();
        Expn right = equalsExpn.getRight();
        left.accept(this);
        right.accept(this);

        if (left.getSymbolType() != right.getSymbolType()) {
            String msg = String.format("Type of expression %s does not match type of expression %s" +
                                       " in equals expression.", left.toString(), right.toString());
            errorList.add(new SemanticErrorException(msg, equalsExpn));
        }
        equalsExpn.setSymbolType(SymbolType.BOOLEAN);
    }

    @Override
    public void visit(FunctionCallExpn functionCallExpn) {
        Symbol functionSymbol = symbolTable.lookup(functionCallExpn.getIdent());
        if (functionSymbol == null) {
            String msg = String.format("Function %s has not been declared.",
                                       functionCallExpn.getIdent());
            errorList.add(new SemanticErrorException(msg, functionCallExpn));
            functionCallExpn.setSymbolType(SymbolType.UNKNOWN);
        } else if (functionSymbol.getSpecialType() == SpecialType.NONE) {
            String msg = String.format("Identifier \"%s\" is not a function!",
                                       functionCallExpn.getIdent());
            errorList.add(new SemanticErrorException(msg, functionCallExpn));
        } else {
            RoutineDecl definingNode = (RoutineDecl) functionSymbol.getDefiningNode();
            if (definingNode.getParamCount() != functionCallExpn.getArguments().size()) {
                String msg = String.format("Function %s takes %d arguments but got %d arguments",
                                           functionCallExpn.getIdent(),
                                           definingNode.getParamCount(),
                                           functionCallExpn.getArguments().size());
                errorList.add(new SemanticErrorException(msg, functionCallExpn));
            }else {
            	for (int i = 0; i < definingNode.getParamCount(); i++){
            		if (definingNode.getParameters().get(i).getSymbolType() != functionCallExpn.getArguments().get(i).getExpnType(symbolTable)){
            			String msg1 = String.format("Function %s takes No.%d argument as %s type, but got %s type",
                                functionCallExpn.getIdent(),
                                i+1,
                                definingNode.getParameters().get(i).getSymbolType(),
                                functionCallExpn.getArguments().get(i).getExpnType(symbolTable));
            			errorList.add(new SemanticErrorException(msg1, functionCallExpn));
            		}
            	}
            }
            
            functionCallExpn.setSymbolType(functionSymbol.getType());
        }
    }

    @Override
    public void visit(IdentExpn identExpn) {  // TODO - Check if identExpn is an array.
        String id = identExpn.getIdent();
        Symbol symbol = symbolTable.lookup(id);
        if ((symbol.getSpecialType() == SpecialType.ARRAY) ||
            (symbol.getSpecialType() == SpecialType.PROCEDURE)) {
            String msg = String.format("Identifier is declared as %s, but used as scalar variable.",
                                       symbol.getSpecialType().toString());
            errorList.add(new SemanticErrorException(msg, identExpn));
        }
        if (symbol == null) {
            String msg = String.format("Identifier %s does not exist.", identExpn.getIdent());
            errorList.add(new SemanticErrorException(msg, identExpn));
        } else {
            identExpn.setSymbolType(symbol.getType());
        }
    }

    @Override
    public void visit(IntConstExpn intConstExpn) {
        intConstExpn.setSymbolType(SymbolType.INTEGER);
    }

    @Override
    public void visit(NotExpn notExpn) {
        Expn operand = notExpn.getOperand();
        operand.accept(this);
        if (operand.getSymbolType() != SymbolType.BOOLEAN) {
            String msg = String.format("Not expression is incompatible with type %s",
                                       operand.getSymbolType());
            errorList.add(new SemanticErrorException(msg, notExpn));
        }
        notExpn.setSymbolType(SymbolType.BOOLEAN);
    }
    
    @Override
    public void visit(SkipConstExpn skipConstExpn) {
        skipConstExpn.setSymbolType(SymbolType.NEWLINE);
    }

    @Override
    public void visit(SubsExpn subsExpn) {
        Symbol array = symbolTable.lookup(subsExpn.getVariable());
        if (array == null) {
            String msg = String.format("Array %s has not been declared.", subsExpn.getVariable());
            errorList.add(new SemanticErrorException(msg, subsExpn));
        } else {
            subsExpn.setSymbolType(array.getType());
        }

        Expn subscript1 = subsExpn.getSubscript1();
        subscript1.accept(this);
        if (subscript1.getSymbolType() != SymbolType.INTEGER) {
            String msg = String.format("Array subscript must be type integer. Currently type %s",
                                       subscript1.getSymbolType());
            errorList.add(new SemanticErrorException(msg, subscript1));
        }
        Expn subscript2 = subsExpn.getSubscript2();
        if (subscript2 != null){
            subscript2.accept(this);
            if (subscript2.getSymbolType() != SymbolType.INTEGER) {
                String msg = String.format("Array subscript must be type integer. Currently type %s",
                        subscript2.getSymbolType());
                errorList.add(new SemanticErrorException(msg, subscript2));
            }
        }
    }

    //TODO - get rid of isNotSameType

    @Override
    public void visit(TextConstExpn textConstExpn) {
        textConstExpn.setSymbolType(SymbolType.TEXT);
    }

    @Override
    public void visit(UnaryMinusExpn unaryMinusExpn) {
        Expn operand = unaryMinusExpn.getOperand();
        operand.accept(this);
        if (operand.getSymbolType() != SymbolType.INTEGER) {
            String msg = String.format("Unary minus cannot be applied to type %s",
                                       operand.getSymbolType());
            errorList.add(new SemanticErrorException(msg, unaryMinusExpn));
        }
        unaryMinusExpn.setSymbolType(SymbolType.INTEGER);
    }

    @Override
    public void visit(BooleanType booleanType) {
        booleanType.setType(SymbolType.BOOLEAN);
    }

    @Override
    public void visit(IntegerType integerType) {
        integerType.setType(SymbolType.INTEGER);
    }

    @Override
    public void visit(WriteStmt writeStmt) {
        for (Printable printable : writeStmt.getOutputs()) {
            Expn output = (Expn) printable;
            output.accept(this);
            SymbolType type = output.getSymbolType();
            if (type != SymbolType.INTEGER && type != SymbolType.TEXT && type != SymbolType.NEWLINE) {
                String msg = String.format("%s is not a compatible type for 'write'",
                                           output.getSymbolType().toString());
                errorList.add(new SemanticErrorException(msg, writeStmt));
            }
        }
    }

    @Override
    public void visit(ReadStmt readStmt) {
        // Visit each readable
        for (ReadableExpn input : readStmt.getInputs()) {
            input.accept(this);
        }
    }

	@Override
	public void visit(ReadableExpn readableExpn) {
	    Expn expn = readableExpn.getVarExpn();
        expn.accept(this);
	    // S31 - check it is int
	    if (expn.getSymbolType() != SymbolType.INTEGER) {
            String msg = "Input is only compatible with type integer.";
            errorList.add(new SemanticErrorException(msg, readableExpn.getVarExpn()));
        } else {
            expn.setSymbolType(SymbolType.INTEGER);
        }
	}

	@Override
	public void visit(ASTList astList) {
	    // We implement this elsewhere.
	}
}
