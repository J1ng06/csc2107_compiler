package compiler488.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
import compiler488.compiler.Main;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.semantics.SemanticErrorException;
import compiler488.symbol.SpecialType;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.SymbolType;
import compiler488.visitor.IVisitor;
import compiler488.ast.type.Type;

/**
 * CodeGenerator.java
 *
 * <pre>
 *  Code Generation Conventions
 *
 *  To simplify the course project, this code generator is
 *  designed to compile directly to pseudo machine memory
 *  which is available as the private array memory[]
 *
 *  It is assumed that the code generator places instructions
 *  in memory in locations
 *
 *      memory[ 0 .. startMSP - 1 ]
 *
 *  The code generator may also place instructions and/or
 *  constants in high memory at locations (though this may
 *  not be necessary)
 *      memory[ startMLP .. Machine.MEMORY_SIZE - 1 ]
 *
 *  During program exection the memory area
 *      memory[ startMSP .. startMLP - 1 ]
 *  is used as a dynamic stack for storing activation records
 *  and temporaries used during expression evaluation.
 *  A hardware exception (stack overflow) occurs if the pointer
 *  for this stack reaches the memory limit register (mlp).
 *
 *  The code generator is responsible for setting the global
 *  variables:
 *      startPC         initial value for program counter
 *      startMSP        initial value for msp
 *      startMLP        initial value for mlp
 * </pre>
 *
 * @author <B> PUT YOUR NAMES HERE </B>
 */

public class CodeGen implements IVisitor {
    /** initial value for memory stack pointer */
    private short startMSP;
    /** initial value for program counter */
    private short startPC;
    /** initial value for memory limit pointer */
    private short startMLP;

    /** flag for tracing code generation */
    private boolean traceCodeGen = false; //Main.traceCodeGen;

    private Machine machine;

    /** The current address of the target machine. */
    private short currentAddr;
    /** The current lexical level */
    private short lexLevel;
    int exitLevel;
    private short offsetNum;
    private short numParams;
    private short numLocalVar;
    private String funcName;
    private short on;
    private String prevFuncName;
    private Short returnCount = 0;
    private Short funcCount = 0;
    
    private SymbolTable symbolTable = new SymbolTable();
    private ArrayList<Short> loopExitAddr = new ArrayList<Short>();
    private HashMap<String, Short> calleeAddr = 
            new HashMap<String, Short>();
    private HashMap<String, Short> callerAddr = 
            new HashMap<String, Short>();
    private ArrayList<String> funcNames = 
            new ArrayList<String>();
    
    private boolean DEBUG = false;

    /**
     * Constructor to initialize code generation
     */
    public CodeGen(Machine machine) {
        this.machine = machine;
        this.currentAddr = 0;
        this.lexLevel = 0;
        this.offsetNum = 0;
    }

    // Utility procedures used for code generation GO HERE.

    /**
     * Additional intialization for gode generation. Called once at the start of
     * code generation. May be unnecesary if constructor does everything.
     */

    /** Additional initialization for Code Generation (if required) */
    void Initialize() {
        /********************************************************/
        /* Initialization code for the code generator GOES HERE */
        /* This procedure is called once before codeGeneration */
        /*                                                      */
        /********************************************************/
    }

    /**
     * Perform any required cleanup at the end of code generation. Called once
     * at the end of code generation.
     *
     * @throws MemoryAddressException
     *             from Machine.writeMemory
     */
    void Finalize() throws MemoryAddressException {
        /********************************************************/
        /* Finalization code for the code generator GOES HERE. */
        /*                                                      */
        /* This procedure is called once at the end of code */
        /* generation */
        /********************************************************/

        // REPLACE THIS CODE WITH YOUR OWN CODE
        // THIS CODE generates a single HALT instruction
        // as an example.
        machine.setPC((short) 0); /* where code to be executed begins */
        machine.setMSP((short) 1); /* where memory stack begins */
        machine.setMLP((short) (Machine.MEMORY_SIZE - 1));
        /* limit of stack */
        machine.writeMemory((short) 0, Machine.HALT);
    }

    /**
     * Procedure to implement code generation based on code generation action
     * number
     *
     * @param actionNumber
     *            code generation action to perform
     */
    void generateCode(int actionNumber) {
        if (traceCodeGen) {
            // output the standard trace stream
            Main.traceStream.println("CodeGen: C" + actionNumber);
        }

        /****************************************************************/
        /* Code to implement the code generation actions GOES HERE */
        /* This dummy code generator just prints the actionNumber */
        /* In Assignment 5, you'll implement something more interesting */
        /*                                                               */
        /* FEEL FREE TO ignore or replace this procedure */
        /****************************************************************/

        System.out.println("Codegen: C" + actionNumber);
    }

    /**
     * Write an instruction with no args into machine memory.
     *
     * @param instruction
     *              the instruction code.
     */
    private void writeInstruction(Short instruction) {

        try {
            machine.writeMemory(currentAddr, instruction);
            currentAddr++;
        } catch (MemoryAddressException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write an instruction with 1 arg into machine memory.
     *
     * @param instruction
     *              the instruction code.
     * @param arg
     *              the argument to the instruction.
     */
    private void writeInstruction(Short instruction, Short arg) {
        writeInstruction(instruction);
        writeInstruction(arg);
    }

    /**
     * Write an instruction with 2 args into machine memory.
     *
     * @param instruction
     *              the instruction code.
     * @param arg1
     *              the first argument.
     * @param arg2
     *              the f argument.
     */
    private void writeInstruction(Short instruction, Short arg1, Short arg2) {
        writeInstruction(instruction);
        writeInstruction(arg1);
        writeInstruction(arg2);
    }

    private void writeToAddr(Short address, Short data) {
        try {
            machine.writeMemory(address, data);
        } catch (MemoryAddressException e) {
            e.printStackTrace();
        }
    }

    private Short readFromMemAddr (Short addr) {
        Short value = Machine.UNDEFINED;
        try {
            value = machine.readMemory(addr);
        } catch (MemoryAddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return value;
    }
    
    private void insertSymbol(String name, SymbolType symbolType, BaseAST definingNode, SpecialType specialType, short ll, short on) {
        Symbol symbol = new Symbol(
                name, 
                symbolType, 
                definingNode, 
                specialType, 
                ll, 
                on);

        try {
            symbolTable.enter(name, symbol);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    @Override
    public void visit(ASTList astList) {

    }

    @Override
    public void visit(ArrayBoundPart arrayBoundPart) {

    }

    @Override
    public void visit(ArrayDeclPart arrayDeclPart) {
        String string = (DEBUG) ? "arrayDeclPart\n" : "";
        System.out.print(string); 
    }

    @Override
    public void visit(MultiDeclarations multiDeclarations) {
        String string = (DEBUG) ? "multiDeclarations\n" : "";
        System.out.print(string);
        
        ASTList<DeclarationPart> parts = multiDeclarations.getParts();

        for(DeclarationPart part : parts) {
            if(part instanceof ScalarDeclPart) {
                part = 
                        new ScalarDeclPart(part.getName(), part.getLine(), part.getColumn());

                Symbol symbol = 
                        new Symbol(
                                part.getName(), 
                                multiDeclarations.getSymbolType(), 
                                multiDeclarations, 
                                SpecialType.NONE, 
                                lexLevel, 
                                offsetNum);

                try {
                    symbolTable.enter(part.getName(), symbol);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }

                offsetNum++;

                writeInstruction(Machine.PUSH, Machine.UNDEFINED);

                part.accept(this);

            } else {// array
                ArrayDeclPart arrayDecl = (ArrayDeclPart) part;

                if (arrayDecl.isTwoDim()) {// two D array
                    Symbol symbol = 
                            new Symbol(
                                    arrayDecl.getName(), 
                                    multiDeclarations.getSymbolType(), 
                                    arrayDecl, 
                                    SpecialType.ARRAY, 
                                    lexLevel, 
                                    offsetNum);

                    try {
                        symbolTable.enter(arrayDecl.getName(), symbol);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    }

                    // push undefined (size of array)
                    writeInstruction(Machine.PUSH, Machine.UNDEFINED);
                    writeInstruction(Machine.PUSH, 
                            (short) (arrayDecl.getBound1Size() * arrayDecl.getBound2Size()));
                    writeInstruction(Machine.DUPN);

                    // update offset number
                    offsetNum += arrayDecl.getBound1Size() * arrayDecl.getBound2Size();

                    arrayDecl.accept(this);

                } else {// one D array
                    Symbol symbol = 
                            new Symbol(
                                    arrayDecl.getName(), 
                                    multiDeclarations.getSymbolType(), 
                                    arrayDecl, 
                                    SpecialType.ARRAY, 
                                    lexLevel, 
                                    offsetNum);

                    try {
                        symbolTable.enter(arrayDecl.getName(), symbol);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    }

                    // push undefined (size of array)
                    writeInstruction(Machine.PUSH, Machine.UNDEFINED);
                    writeInstruction(Machine.PUSH, (short) arrayDecl.getBound1Size());
                    writeInstruction(Machine.DUPN);

                    // update offset number
                    offsetNum += arrayDecl.getBound1Size();

                    arrayDecl.accept(this);
                }
            }
        }
    }

    @Override
    public void visit(RoutineDecl routineDecl) {
        String string = (DEBUG) ? "routineDecl\n" : "";
        System.out.print(string);
        
        Type returnType = routineDecl.getType();

        if (returnType != null) {
            // function declarations
            returnCount = 0;
            funcCount++;
            funcNames.add(routineDecl.getName());
            on = offsetNum;
            
            SymbolType symbolType = 
                    (returnType.toString() == "boolean" ? SymbolType.BOOLEAN : SymbolType.INTEGER);
            // increase one  level
            lexLevel++;
            this.insertSymbol(
                    routineDecl.getName(),
                    symbolType,
                    routineDecl,
                    SpecialType.FUNCTION,
                    lexLevel,
                    offsetNum);
            
            offsetNum++;
            writeInstruction(Machine.PUSH, Machine.UNDEFINED);
            
        } else {
            // procedure declarations
            lexLevel++;
            this.insertSymbol(
                    routineDecl.getName(),
                    SymbolType.NONE,
                    routineDecl,
                    SpecialType.PROCEDURE,
                    lexLevel,
                    offsetNum);
        }
        
        // skip the routine
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short endAddr = (short) (currentAddr - 1);
        writeInstruction(Machine.BR);
        
        // record routine start label (caller jump here)
        calleeAddr.put(routineDecl.getName(), currentAddr);

        symbolTable.openScope();
        short tempOffsetNum = offsetNum;
        short tempNumParams = numParams;
        numParams = (short) routineDecl.getParamCount();
        ASTList<ScalarDecl> parameters = routineDecl.getParameters();
        offsetNum = 0;
        Scope body = routineDecl.getBody();
        
        // set display
        writeInstruction(Machine.PUSHMT);
        writeInstruction(Machine.SETD, lexLevel);
        
        for (int i = 0; i < numParams; i++) {
            ScalarDecl paramDecl = parameters.get(i);
            
            this.insertSymbol(
                    paramDecl.getName(),
                    paramDecl.getSymbolType(),
                    paramDecl,
                    SpecialType.NONE,
                    lexLevel,
                    (short) (-numParams + i));
        }
        symbolTable.nestingLevel--;
        body.accept(this);
        
        // pop off all local variables and parameters
        writeInstruction(Machine.PUSH, (short) (offsetNum + numParams));
        writeInstruction(Machine.POPN);
        
      // restore offsetNum
      offsetNum = tempOffsetNum;
      this.numParams = tempNumParams;
      
//      // reset display
//      lexLevel--;
//      writeInstruction(Machine.PUSHMT);
//      writeInstruction(Machine.SETD, lexLevel);
//      System.out.println("#####lexLevel: " + lexLevel);
      
      lexLevel--;
      // jump back to caller
      writeInstruction(Machine.BR);
      
   // end of routine decl label
      this.writeToAddr(endAddr, currentAddr);
    }

    @Override
    public void visit(ScalarDecl scalarDecl) {

    }

    @Override
    public void visit(ScalarDeclPart scalarDeclPart) {
        String string = (DEBUG) ? "scalarDeclPart\n" : "";
        System.out.print(string);
        //        Symbol(scalarDeclPart.getName(), scalarDeclPart.getType(), SymbolKind.SCALAR, decl, num_var, lexicalLevel);
        //writeInstruction(machine.PUSH, machine.UNDEFINED);
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        String string = (DEBUG) ? "assignStmt\n" : "";
        System.out.print(string); 

        Expn leftExpn = assignStmt.getLval();
        Expn rightExpn = assignStmt.getRval();

        leftExpn.accept(this);
        rightExpn.accept(this);

        if (rightExpn instanceof IdentExpn || rightExpn instanceof SubsExpn) {
            writeInstruction(Machine.LOAD);
        }

        writeInstruction(Machine.STORE);
    }

    @Override
    public void visit(ExitStmt exitStmt) {
        String string = (DEBUG) ? "exitStmt\n" : "";
        System.out.print(string);

        this.exitLevel = exitStmt.getLevel();
        Expn exitExpn = exitStmt.getExpn();
        
        if (exitExpn != null) {
            // exit when true
            exitExpn.accept(this);
            
            /*
             *  negate above boolean, which will force BF to branch 
             *  if above boolean is true
             */
            writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
            writeInstruction(Machine.EQ);
            
        } else {
            // exit integer
            // make it false, which will force the BF to branch 
            writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
        }

        // branch to the end of loop based on the exit integer
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        loopExitAddr.set(loopExitAddr.size() - exitLevel, (short)(currentAddr - 1));
        writeInstruction(Machine.BF);
    }

    @Override
    public void visit(IfStmt ifStmt) {
        String string = (DEBUG) ? "ifStmt\n" : "";
        System.out.print(string); 

        // This one is over-commented cause I think its difficult to follow.

        Expn condition = ifStmt.getCondition();
        Stmt trueBranch = ifStmt.getWhenTrue();
        Stmt falseBranch = ifStmt.getWhenFalse();

        // Go to the condition and push its Machine result onto the stack.
        condition.accept(this);

        if (condition instanceof IdentExpn || condition instanceof SubsExpn) {
            // those two expression types only returns the address
            writeInstruction(Machine.LOAD);
        }

        // Put a placeholder for the false branch address on the stack,
        // then record where this placeholder is so we can update later.
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short falseAddrPlaceholder = (short) (currentAddr - 1);
        writeInstruction(Machine.BF);       // jump to false branch if necessary.
        trueBranch.accept(this);            // generate true branch code...
        // Put a placeholder for the end address on the stack, then
        // record where this placeholder is so we can update later.
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short endAddrPlaceholder = (short) (currentAddr - 1);
        writeInstruction(Machine.BR);
        // Now record where the false branch actually is...
        writeToAddr(falseAddrPlaceholder, currentAddr);
        if (falseBranch != null) {
            falseBranch.accept(this);       // generate false branch code...
        }
        // Now record where the end of the statement actually is
        writeToAddr(endAddrPlaceholder, currentAddr);
    }

    @Override
    public void visit(ProcedureCallStmt procedureCallStmt) {
        String string = (DEBUG) ? "procedureCallStmt\n" : "";
        System.out.print(string);

        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short returnAddr = (short) (currentAddr - 1);
        //        machine.showStack();

        ASTList<Expn> arguments = procedureCallStmt.getArguments();

        for (int i = arguments.size(); i > 0; i--) {
            Expn expn = arguments.get(arguments.size() - i);

            expn.accept(this);

            if (expn instanceof IdentExpn ||
                    expn instanceof SubsExpn) {

                writeInstruction(Machine.LOAD);
            }
        }

        writeInstruction(Machine.PUSH, calleeAddr.get(procedureCallStmt.getName()));
        writeInstruction(Machine.BR);

        writeToAddr(returnAddr, currentAddr);
    }

    @Override
    public void visit(Program program) {
        String string = (DEBUG) ? "CodeGen Program visit method\n" : "";
        System.out.print(string);

        // Start tracing execution if it has been enabled.
        if (traceCodeGen) {
            writeInstruction(Machine.TRON);
        }

        // C00 - Prepare for start of program execution.
        writeInstruction(Machine.PUSHMT);
        writeInstruction(Machine.SETD, lexLevel);       // lexLevel 0 on program start.
        visit((Scope) program);

        // C01 - Prepare to end program execution.
        writeInstruction(Machine.HALT);

        // C02 - Set pc msp and mlp
        machine.setPC((short) 0);
        machine.setMSP(currentAddr);
        machine.setMLP((short) (Machine.MEMORY_SIZE - 1));
    }

    @Override
    public void visit(ReadStmt readStmt) {
        for (ReadableExpn expn : readStmt.getInputs()) {
            IdentExpn ident = (IdentExpn) expn.getVarExpn();
            ident.accept(this);
            writeInstruction(Machine.READI);
            writeInstruction(Machine.STORE);
        }
    }

    @Override
    public void visit(RepeatUntilStmt repeatUntilStmt) {
        String string = (DEBUG) ? "repeatUntilStmt\n" : "";
        System.out.print(string); 

        // new loop, add UNDEFINED to occupy a space
        loopExitAddr.add(Machine.UNDEFINED);

        Expn condition = repeatUntilStmt.getExpn();
        ASTList<Stmt> body = repeatUntilStmt.getBody();

        // record start label
        short startAddr = currentAddr;

        // run body at least once
        for (Stmt stmt : body) {
            stmt.accept(this);
        }

        // check condition
        condition.accept(this);

        if (condition instanceof IdentExpn || condition instanceof SubsExpn) {
            writeInstruction(Machine.LOAD);
        }

        // start label (if true end, else go back to start)
        writeInstruction(Machine.PUSH, startAddr);
        writeInstruction(Machine.BF);

        // pop the top value, update the loop-end address if the branching address holder exists
        if (loopExitAddr.get(loopExitAddr.size() - 1) != Machine.UNDEFINED) {
            this.writeToAddr(loopExitAddr.remove(loopExitAddr.size() - 1), currentAddr);
        } else {
            loopExitAddr.remove(loopExitAddr.size() - 1);
        }
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        String string = (DEBUG) ? "returnStmt\n" : "";
        System.out.print(string); 
        
        Symbol symbol;
        
        returnCount++;
        Expn returnExpn = returnStmt.getValue();

        if (returnCount == 1 || funcCount > 1) {
            prevFuncName = funcNames.get(funcNames.size() - 1);
            symbol = symbolTable.lookup(funcNames.remove(funcNames.size() - 1));
        } else {
            symbol = symbolTable.lookup(prevFuncName);
        }
        
        if (returnExpn != null) {
//            writeInstruction(Machine.ADDR, lexLevel, (short)(-2 - numParams));
            writeInstruction(Machine.ADDR, (short)(symbol.getLL() - 1), (short)(symbol.getON()));
            
            returnExpn.accept(this);
            
            if (returnExpn instanceof IdentExpn || returnExpn instanceof SubsExpn) {
                writeInstruction(Machine.LOAD);
            }
            
            writeInstruction(Machine.STORE);
        }
    }

    @Override
    public void visit(Scope scope) {
        String string = (DEBUG) ? "CodeGen scope visit method\n" : "";
        System.out.print(string); 
        symbolTable.openScope();
        if (scope.getDeclarations() != null) {
            for (Declaration decl : scope.getDeclarations()) {
                decl.accept(this);
            }
        }
        if (scope.getStatements() != null) {
            for (Stmt stmt : scope.getStatements()) {
                stmt.accept(this);
            }
        }
        symbolTable.nestingLevel--;
    }

    @Override
    public void visit(WhileDoStmt whileDoStmt) {
        String string = (DEBUG) ? "whileDoStmt\n" : "";
        System.out.print(string); 

        // new loop, add UNDEFINED to occupy a space
        loopExitAddr.add(Machine.UNDEFINED);

        Expn condition = whileDoStmt.getExpn();
        ASTList<Stmt> body = whileDoStmt.getBody();

        // record start label
        short startAddr = currentAddr;

        // check condition
        condition.accept(this);

        if (condition instanceof IdentExpn || condition instanceof SubsExpn) {
            writeInstruction(Machine.LOAD);
        }

        // if true run body, false go to end
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short endAddr = (short) (currentAddr - 1);
        writeInstruction(Machine.BF);

        // run body
        for (Stmt stmt : body) {
            stmt.accept(this);
        }

        // go to start label
        writeInstruction(Machine.PUSH, startAddr);
        writeInstruction(Machine.BR);

        this.writeToAddr(endAddr, currentAddr);

        // pop the top value, update the loop-end address if the branching address holder exists
        if (loopExitAddr.get(loopExitAddr.size() - 1) != Machine.UNDEFINED) {
            this.writeToAddr(loopExitAddr.remove(loopExitAddr.size() - 1), currentAddr);
        } else {
            loopExitAddr.remove(loopExitAddr.size() - 1);
        }
    }

    @Override
    public void visit(WriteStmt writeStmt) {
        String string = (DEBUG) ? "writeStmt\n" : "";
        System.out.print(string);

        for (Printable output : writeStmt.getOutputs()) {
            Expn expn = (Expn) output;

            Symbol symbol = symbolTable.lookup(expn.toString());

            if (expn instanceof IdentExpn || expn instanceof SubsExpn) {
                // for identifier or array identifier
                expn.accept(this);
                writeInstruction(Machine.LOAD);
                writeInstruction(Machine.PRINTI);

            } else if (expn instanceof IntConstExpn) {
                // for integer constant
                expn.accept(this);
                writeInstruction(Machine.PRINTI);
            } else if (expn instanceof UnaryMinusExpn) {
                expn.accept(this);
                writeInstruction(Machine.PRINTI);
            } else if (expn instanceof  ArithExpn) {
                expn.accept(this);
                writeInstruction(Machine.PRINTI);
            } else {
                // for text or newline
                expn.accept(this);
            }
        }
        
//        writeInstruction(Machine.POP);
    }

    @Override
    public void visit(ArithExpn arithExpn) {
        String string = (DEBUG) ? "arithExpn\n" : "";
        System.out.print(string);

        Expn leftExpn = arithExpn.getLeft();
        Expn rightExpn = arithExpn.getRight();

        leftExpn.accept(this);

        if (leftExpn instanceof IdentExpn || leftExpn instanceof SubsExpn) {
            // need to load the address
            writeInstruction(Machine.LOAD);
        }

        rightExpn.accept(this);

        if (rightExpn instanceof IdentExpn || rightExpn instanceof SubsExpn) {
            // need to load the address
            writeInstruction(Machine.LOAD);
        }

        if (arithExpn.getOpSymbol().equals(ArithExpn.OP_PLUS)){
            writeInstruction(Machine.ADD);
        } else if (arithExpn.getOpSymbol().equals(ArithExpn.OP_MINUS)){
            writeInstruction(Machine.SUB);
        } else if (arithExpn.getOpSymbol().equals(ArithExpn.OP_TIMES)){
            writeInstruction(Machine.MUL);
        } else if (arithExpn.getOpSymbol().equals(ArithExpn.OP_DIVIDE)){
            writeInstruction(Machine.DIV);
        }

    }

    @Override
    public void visit(BoolConstExpn boolConstExpn) {
        String string = (DEBUG) ? "boolConstExpn\n" : "";
        System.out.print(string);

        if (boolConstExpn.getValue()) {
            writeInstruction(Machine.PUSH, Machine.MACHINE_TRUE);
        }
        else {
            writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
        }
    }

    @Override
    public void visit(BoolExpn boolExpn) {
        String string = (DEBUG) ? "boolExpn\n" : "";
        System.out.print(string);

        Expn leftExpn = boolExpn.getLeft();
        Expn rightExpn = boolExpn.getRight();

        leftExpn.accept(this);

        if (leftExpn instanceof IdentExpn) {
            writeInstruction(Machine.LOAD);
        }
        
//        writeInstruction(Machine.PUSH, (short) 2);
//        writeInstruction(Machine.DUPN);

        if (boolExpn.getOpSymbol() == BoolExpn.OP_AND) {
            // if true, check second operand
            // if false, branch to end, push false
            // if second operand true, push true, else push false

            // second operand label
            writeInstruction(Machine.PUSH, Machine.UNDEFINED);
            short endAddr = (short) (currentAddr - 1);
            writeInstruction(Machine.BF);

            // second operand value
            rightExpn.accept(this);

            if (rightExpn instanceof IdentExpn) {
                writeInstruction(Machine.LOAD);
            }

            // record end label
            writeToAddr(endAddr, currentAddr);
        }
        else if (boolExpn.getOpSymbol() == BoolExpn.OP_OR) {
            // if true, branch to end, push true
            // if false, check second operand
            // if second operand true, push true, else push false

            // second operand label
            writeInstruction(Machine.PUSH, Machine.UNDEFINED);
            short secondOpAddr = (short) (currentAddr - 1);
            writeInstruction(Machine.BF);

            // return result TRUE
            writeInstruction(Machine.PUSH, Machine.MACHINE_TRUE);

            // end label
            writeInstruction(Machine.PUSH, Machine.UNDEFINED);
            short endAddr = (short) (currentAddr - 1);
            writeInstruction(Machine.BR);

            // record second operand label
            writeToAddr(secondOpAddr, currentAddr);

            // second operand value
            rightExpn.accept(this);

            if (rightExpn instanceof IdentExpn) {
                writeInstruction(Machine.LOAD);
            }

            // record end label
            writeToAddr(endAddr, currentAddr);
        }
        else {
            // something wrong, operator does not match
        }
    }

    @Override
    public void visit(CompareExpn compareExpn) {
        String string = (DEBUG) ? "compareExpn\n" : "";
        System.out.print(string);

        Expn leftExpn = compareExpn.getLeft();
        Expn rightExpn = compareExpn.getRight();

        leftExpn.accept(this);

        if (leftExpn instanceof IdentExpn || leftExpn instanceof SubsExpn) {
            writeInstruction(Machine.LOAD);
        }

        rightExpn.accept(this);

        if (rightExpn instanceof IdentExpn || rightExpn instanceof SubsExpn) {
            writeInstruction(Machine.LOAD);
        }

        if (compareExpn.getOpSymbol() == CompareExpn.OP_LESS) {
            writeInstruction(Machine.LT);
        }
        else if (compareExpn.getOpSymbol() == CompareExpn.OP_GREATER) {
            writeInstruction(Machine.SWAP);
            writeInstruction(Machine.LT);
        }
        else if (compareExpn.getOpSymbol() == CompareExpn.OP_LESS_EQUAL) {
            writeInstruction(Machine.SWAP);
            writeInstruction(Machine.LT);
            writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
            writeInstruction(Machine.EQ);
        }
        else if (compareExpn.getOpSymbol() == CompareExpn.OP_GREATER_EQUAL) {
            writeInstruction(Machine.LT);
            writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
            writeInstruction(Machine.EQ);
        }
        else {
            // something wrong, none of the operators matched
        }
    }

    @Override
    public void visit(ConditionalExpn conditionalExpn) {
        String string = (DEBUG) ? "conditionalExpn\n" : "";
        System.out.print(string);

        Expn condition = conditionalExpn.getCondition();
        Expn trueExpn = conditionalExpn.getTrueValue();
        Expn falseExpn = conditionalExpn.getFalseValue();

        condition.accept(this);

        if (condition instanceof IdentExpn) {
            writeInstruction(Machine.LOAD);
        }

        // if true get true value
        // if false get false value

        // false label
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short falseAddr = (short) (currentAddr - 1);
        writeInstruction(Machine.BF);

        // get true value
        trueExpn.accept(this);

        if (trueExpn instanceof IdentExpn) {
            writeInstruction(Machine.LOAD);
        }

        // end label
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short endAddr = (short) (currentAddr - 1);
        writeInstruction(Machine.BR);

        // record false label
        writeToAddr(falseAddr, currentAddr);

        // get false value
        falseExpn.accept(this);

        if (falseExpn instanceof IdentExpn) {
            writeInstruction(Machine.LOAD);
        }

        // record end label
        writeToAddr(endAddr, currentAddr);
    }

    @Override
    public void visit(EqualsExpn equalsExpn) {
        String string = (DEBUG) ? "equalsExpn\n" : "";
        System.out.print(string);

        Expn leftExpn = equalsExpn.getLeft();
        Expn rightExpn = equalsExpn.getRight();

        leftExpn.accept(this);

        if (leftExpn instanceof IdentExpn || 
                leftExpn instanceof SubsExpn || 
                leftExpn instanceof FunctionCallExpn) {
            
            writeInstruction(Machine.LOAD);
        }

        rightExpn.accept(this);

        if (rightExpn instanceof IdentExpn || rightExpn instanceof SubsExpn) {
            writeInstruction(Machine.LOAD);
        }

        if (equalsExpn.getOpSymbol().equals(EqualsExpn.OP_EQUAL)) {
            writeInstruction(Machine.EQ);
        }
        else if (equalsExpn.getOpSymbol().equals(EqualsExpn.OP_NOT_EQUAL)) {
            writeInstruction(Machine.EQ);
            writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
            writeInstruction(Machine.EQ);
        }
        else {
            // something wrong, operator does not match
        }
    }

    @Override
    public void visit(FunctionCallExpn functionCallExpn) {
        String string = (DEBUG) ? "functionCallExpn\n" : "";
        System.out.print(string);

        Symbol symbol = symbolTable.lookup(functionCallExpn.getIdent());
        
        // return value
        //writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        
        writeInstruction(Machine.PUSH, Machine.UNDEFINED);
        short returnAddr = (short) (currentAddr - 1);
        //        machine.showStack();

        ASTList<Expn> arguments = functionCallExpn.getArguments();

        for (int i = arguments.size(); i > 0; i--) {
            Expn expn = arguments.get(arguments.size() - i);

            expn.accept(this);

            if (expn instanceof IdentExpn ||
                    expn instanceof SubsExpn) {

                writeInstruction(Machine.LOAD);
            }
        }

        writeInstruction(Machine.PUSH, calleeAddr.get(functionCallExpn.getIdent()));
        writeInstruction(Machine.BR);

        writeToAddr(returnAddr, currentAddr);
        
//        writeInstruction(Machine.ADDR, (short)(lexLevel + 1), (short)(-2 - arguments.size()));
        writeInstruction(Machine.ADDR, (short)(symbol.getLL() - 1), (short)(symbol.getON()));
    }

    @Override
    public void visit(IdentExpn identExpn) {
        String string = (DEBUG) ? "identExpn\n" : "";
        System.out.print(string); 

        if (calleeAddr.get(identExpn.toString()) != null) {
            // this ident is a routine
            Symbol symbol = symbolTable.lookup(identExpn.toString());
            
            if (symbol.getSpecialType() == SpecialType.FUNCTION) {
                FunctionCallExpn fce = 
                        new FunctionCallExpn(
                                identExpn.toString(), 
                                new ASTList<Expn>(), 
                                identExpn.getLine(), 
                                identExpn.getColumn());
                fce.accept(this);
            } else if (symbol.getSpecialType() == SpecialType.PROCEDURE) {
                ProcedureCallStmt pcs = 
                        new ProcedureCallStmt(
                                identExpn.toString(), 
                                new ASTList<Expn>(), 
                                identExpn.getLine(), 
                                identExpn.getColumn());
                pcs.accept(this);
            } else {
                // TODO
            }
            
        } else {
            Symbol symbol = symbolTable.lookup(identExpn.toString());

            writeInstruction(Machine.ADDR, 
                    (short) symbol.getLL(),
                    (short) symbol.getON());
        }
    }

    @Override
    public void visit(IntConstExpn intConstExpn) {
        String string = (DEBUG) ? "intConstExpn" : "";
        System.out.print(string);

        writeInstruction(Machine.PUSH, intConstExpn.getValue().shortValue());
    }

    @Override
    public void visit(NotExpn notExpn) {
        notExpn.getOperand().accept(this);

        if (notExpn.getOperand() instanceof IdentExpn) {
            writeInstruction(Machine.LOAD);
        }

        writeInstruction(Machine.PUSH, Machine.MACHINE_FALSE);
        writeInstruction(Machine.EQ);
    }

    @Override
    public void visit(SkipConstExpn skipConstExpn) {
        String string = (DEBUG) ? "skipConstExpn\n" : "";
        System.out.print(string); 

        writeInstruction(Machine.PUSH, (short) '\n');
        writeInstruction(Machine.PRINTC);
    }

    @Override
    public void visit(SubsExpn subsExpn) {
        String string = (DEBUG) ? "subsExpn\n" : "";
        System.out.print(string);

        Short lb1, lb2;
        Short subscript1, subscript2;
        Short offset, row_offset, col_offset;

        Symbol array = symbolTable.lookup(subsExpn.getVariable());

        if (subsExpn.numSubscripts() > 1) {// two dim
            lb1 = (short) ((ArrayDeclPart) array.getDefiningNode()).getBound1().getLb();
            lb2 = (short) ((ArrayDeclPart) array.getDefiningNode()).getBound2().getLb();

            Expn subscript1Expn = subsExpn.getSubscript1();
            Expn subscript2Expn = subsExpn.getSubscript2();

            subscript1Expn.accept(this);

            // only IdentExpn return address
            // everything else (arightmetic, int const, unaryExpn) returns the actual value
            if (subscript1Expn instanceof IdentExpn || subscript1Expn instanceof SubsExpn) {
                writeInstruction(Machine.LOAD);
            } 

            // get the value from the top of stack
            if (subscript1Expn instanceof UnaryMinusExpn) {
                /* unary expression has some weird behaviro when returning the
                 * negative value, hence the work around is to get the positive
                 * value, then manually convert it to negative value 
                 */
                subscript1 = this.readFromMemAddr((short)(currentAddr - 2));
                subscript1 = (short) (0 - subscript1);

            } else {
                subscript1 = this.readFromMemAddr((short)(currentAddr - 1));
            }

            // pop off the item from stack (very important)
            writeInstruction(Machine.POP);

            subscript2Expn.accept(this);

            // only IdentExpn return address
            // everything else (arightmetic, int const, unaryExpn) returns the actual value
            if (subscript2Expn instanceof IdentExpn || subscript2Expn instanceof SubsExpn) {
                writeInstruction(Machine.LOAD);
            } 

            // get the value from the top of stack
            if (subscript2Expn instanceof UnaryMinusExpn) {
                /* unary expression has some weird behaviro when returning the
                 * negative value, hence the work around is to get the positive
                 * value, then manually convert it to negative value 
                 */
                subscript2 = this.readFromMemAddr((short)(currentAddr - 2));
                subscript2 = (short) (0 - subscript2);

            } else {
                subscript2 = this.readFromMemAddr((short)(currentAddr - 1));
            }

            // pop off the item from stack (very important)
            writeInstruction(Machine.POP);

            /* (row offset = (accessed index (1st D) - lb index (1st D)) * (2nd dimension array length)
                (col offset = (accessed index (2nd D) - lb index (2nd D))*/
            row_offset = (short) ((subscript1 - lb1) * ((ArrayDeclPart) array.getDefiningNode()).getBound2Size());
            col_offset = (short) (subscript2 - lb2);

            // calculate the offset number
            offset = (short) (row_offset + col_offset);

        } else { // one dim
            lb1 = (short) ((ArrayDeclPart) array.getDefiningNode()).getBound1().getLb();
            Expn subscript1Expn = subsExpn.getSubscript1();

            subscript1Expn.accept(this);

            // only IdentExpn return address
            // everything else (arightmetic, int const, unaryExpn) returns the actual value
            if (subscript1Expn instanceof IdentExpn || subscript1Expn instanceof SubsExpn) {
                writeInstruction(Machine.LOAD);
            } 

            // get the value from the top of stack
            if (subscript1Expn instanceof UnaryMinusExpn) {
                /* unary expression has some weird behaviro when returning the
                 * negative value, hence the work around is to get the positive
                 * value, then manually convert it to negative value 
                 */
                subscript1 = this.readFromMemAddr((short)(currentAddr - 2));
                subscript1 = (short) (0 - subscript1);

            } else {
                subscript1 = this.readFromMemAddr((short)(currentAddr - 1));
            }

            // pop off the item from stack (very important)
            writeInstruction(Machine.POP);

            // calculate offset number (row offset = accessed index - lb index)
            offset =  (short) (subscript1 - lb1);
        }

        // address of this subsExpn
        writeInstruction(Machine.ADDR, (short) symbolTable.lookup(subsExpn.toString()).getLL(), 
                (short) (symbolTable.lookup(subsExpn.toString()).getON() + offset));
    }

    @Override
    public void visit(TextConstExpn textConstExpn) {
        String output = textConstExpn.getValue();
        for (byte b : output.getBytes()) {
            writeInstruction(Machine.PUSH, (short) b);
            writeInstruction(Machine.PRINTC);
        }
    }

    @Override
    public void visit(UnaryMinusExpn unaryMinusExpn) {
        String string = (DEBUG) ? "unaryMinusExpn\n" : "";
        System.out.print(string); 

        if (unaryMinusExpn.getOperand() instanceof IdentExpn) {
            unaryMinusExpn.getOperand().accept(this);
            writeInstruction(Machine.LOAD);
            writeInstruction(Machine.NEG);
        }
        else {
            unaryMinusExpn.getOperand().accept(this);
            writeInstruction(Machine.NEG);
        }
    }

    @Override
    public void visit(BooleanType booleanType) {

    }

    @Override
    public void visit(IntegerType integerType) {

    }

    @Override
    public void visit(ReadableExpn readableExpn) {
        // See ReadStmt.
    }

    @Override
    public LinkedList<SemanticErrorException> getErrorMessages() {
        return null;
    }

    // ADDITIONAL FUNCTIONS TO IMPLEMENT CODE GENERATION GO HERE
}
