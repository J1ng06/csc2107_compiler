package compiler488.visitor;

import java.util.LinkedList;

import compiler488.ast.ASTList;
import compiler488.ast.ReadableExpn;
import compiler488.ast.decl.ArrayBoundPart;
import compiler488.ast.decl.ArrayDeclPart;
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
import compiler488.ast.stmt.WhileDoStmt;
import compiler488.ast.stmt.WriteStmt;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.IntegerType;
import compiler488.semantics.SemanticErrorException;

public interface IVisitor {
    
    public void visit(ASTList astList);
    
    // Declarations
    public void visit(ArrayBoundPart arrayBoundPart);
    public void visit(ArrayDeclPart arrayDeclPart);
    public void visit(MultiDeclarations multiDeclarations);
    public void visit(RoutineDecl routineDecl);
    public void visit(ScalarDecl scalarDecl);
    public void visit(ScalarDeclPart scalarDeclPart);
    
    // Statements
    public void visit(AssignStmt assignStmt);
    public void visit(ExitStmt exitStmt);
    public void visit(IfStmt ifStmt);
    public void visit(ProcedureCallStmt procedureCallStmt);
    public void visit(Program program);
    public void visit(ReadStmt readStmt);
    public void visit(RepeatUntilStmt repeatUntilStmt);
    public void visit(ReturnStmt returnStmt);
    public void visit(Scope scope);
    public void visit(WhileDoStmt whileDoStmt);
    public void visit(WriteStmt writeStmt);
    
    // Expressions
    public void visit(ArithExpn arithExpn);
    public void visit(BoolConstExpn boolConstExpn);
    public void visit(BoolExpn boolExpn);
    public void visit(CompareExpn compareExpn);
    public void visit(ConditionalExpn conditionalExpn);
    public void visit(EqualsExpn equalsExpn);
    public void visit(FunctionCallExpn functionCallExpn);
    public void visit(IdentExpn identExpn);
    public void visit(IntConstExpn intConstExpn);
    public void visit(NotExpn notExpn);
    public void visit(SkipConstExpn skipConstExpn);
    public void visit(SubsExpn subsExpn);
    public void visit(TextConstExpn textConstExpn);
    public void visit(UnaryMinusExpn unaryMinusExpn);
    
    // Types
    public void visit(BooleanType booleanType);
    public void visit(IntegerType integerType);

	public void visit(ReadableExpn readableExpn);

    public LinkedList<SemanticErrorException> getErrorMessages();
    

}
