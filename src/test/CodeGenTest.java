package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import compiler488.ast.stmt.Program;
import compiler488.codegen.CodeGen;
import compiler488.parser.Lexer;
import compiler488.parser.Source488Parser;
import compiler488.parser.SyntaxErrorException;
import compiler488.runtime.ExecutionException;
import compiler488.runtime.Machine;
import compiler488.runtime.MachineExecutor;
import compiler488.semantics.SemanticErrorException;
import compiler488.semantics.Semantics;
import compiler488.visitor.IVisitor;
import java_cup.runtime.Symbol;

public class CodeGenTest {

    public static boolean errorOccurred;
    File testOutput;
    File errorOutput;
    Machine machine;

    public Machine makeMachine(File output, File error) {
        try {
            OutputStream outStream = new FileOutputStream(output);
            OutputStream errorStream = new FileOutputStream(error);
            return new MachineExecutor(new PrintStream(errorStream),
                    new PrintStream(outStream), System.in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void generateCode(Machine machine, Program programAST) {
        // Reset machine before code generation
        machine.reset();

        try {
            IVisitor visitor = new CodeGen(machine);
            programAST.accept(visitor);
        } catch (Exception e) {
            System.err.println("Exception during Code Generation");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            errorOccurred = true;
        }
    }

    private static void semanticAnalysis(Program programAST) {
        try {
            IVisitor visitor = new Semantics();
            programAST.accept(visitor);
            LinkedList<SemanticErrorException> errorMessages = visitor.getErrorMessages();
            if (errorMessages.size() > 0) {
                for (SemanticErrorException e : errorMessages) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Compilation ended due to semantic error(s)");
                return;
            }
        } catch (Exception e) {
            System.err.println("Exception during Semantic Analysis");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            errorOccurred = true;
        }
    }

    public static void compileProgram(Machine machine, String sourceFileName) {
        Object parserResult = null ; // the result of parsing and AST building
        Program programAST = null;

        errorOccurred = false;

        /* Scan and Parse the program */
        try {
            Source488Parser p = new Source488Parser(new Lexer(new FileReader(sourceFileName)));

            if (!errorOccurred) {
                Symbol value = null;

                value = p.parse();

                if (value == null) {
                    System.err.println("WARNING: Parser returned no value for 'start with' nonterminal");
                } else {
                    parserResult = value.value;
                }
            }

            programAST = (Program) parserResult;
        } catch (FileNotFoundException e) {
            System.err.println("Unable to open file: " + sourceFileName);
            errorOccurred = true;
        } catch (SyntaxErrorException e) {
            // Parser has already printed an error message
            errorOccurred = true;
        } catch (Exception e) {
            System.err.println("Exception during Parsing and AST building");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            errorOccurred = true;
        }

        if (errorOccurred) {
            System.out.println("Processing Terminated due to errors during parsing");
            return;
        }

        /* Do semantic analysis on the program */
        semanticAnalysis(programAST);

        if (errorOccurred) {
            System.out.println("Processing Terminated due to errors during semantic analysis");
            return;
        }

        /* do code generation for the program */
        generateCode(machine, programAST);

        if (errorOccurred) {
            System.out.println("Processing Terminated due to errors during code generation");
            return;
        }
    }

    private static void executeProgram(Machine machine) {
        // execute the compiled program
        try {
            machine.run();
        } catch (ExecutionException e) {
            System.err.println("Exception during Machine Execution" + e.getMessage());
            return;
        } catch (Exception e) {
            System.err.println("Unexpected Exception during Machine Execution");
            System.err.println("Please file a Bug Report with the course instructor");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(200);
        }
    }

    public void arrangeTest(String inputFile) {
        compileProgram(machine, inputFile);
        executeProgram(machine);
    }

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        testOutput = tempFolder.newFile("testOutput.txt");
        errorOutput = tempFolder.newFile("errorOutput.txt");
        machine = makeMachine(testOutput, errorOutput);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHelloWorld() throws Exception {
        arrangeTest("./testing/pass/hello.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Hello 488!\n", s);
    }

    @Test
    public void testArith01() throws Exception {
        arrangeTest("./testing/pass/arith01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("4\n", s);
    }
    
    @Test
    public void testArithmeticPrecedenceTest() throws Exception {
        arrangeTest("./testing/pass/arithmeticPrecedenceTest.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("12\n2\n-3\n1\n", s);
    }

    @Test
    public void testArray01() throws Exception {
        arrangeTest("./testing/pass/array01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("100\n", s);
    }

    @Test
    public void testArray02() throws Exception {
        arrangeTest("./testing/pass/array02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("-300\n-200\n-100\n8990\n1000", s);
    }

    @Test
    public void testArray03() throws Exception {
        arrangeTest("./testing/pass/array03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("800\n900\n", s);
    }

    @Test
    public void testArray04() throws Exception {
        arrangeTest("./testing/pass/array04.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("800\n", s);
    }

    @Test
    public void testArray05() throws Exception {
        arrangeTest("./testing/pass/array05.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("800\n1000\n", s);
    }

    @Test
    public void testArray06() throws Exception {
        arrangeTest("./testing/pass/array06.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("800\n", s);
    }

    @Test
    public void testArray07() throws Exception {
        arrangeTest("./testing/pass/array07.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("800\n1000\n", s);
    }

    @Test
    public void testArray08() throws Exception {
        arrangeTest("./testing/pass/array08.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("1", s);
    }

    @Test
    public void testArray09() throws Exception {
        arrangeTest("./testing/pass/array01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("100\n", s);
    }

    @Test
    public void testArray10() throws Exception {
        arrangeTest("./testing/pass/array10.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("pass\n", s);
    }
    
    @Test
    public void testArray11() throws Exception {
        arrangeTest("./testing/pass/array11.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\n", s);
    }

    @Test
    public void testBooleanAnd01() throws Exception {
        arrangeTest("./testing/pass/boolean_and01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testBooleanAnd02() throws Exception {
        arrangeTest("./testing/pass/boolean_and02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testBooleanOr01() throws Exception {
        arrangeTest("./testing/pass/boolean_or01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testBooleanOr02() throws Exception {
        arrangeTest("./testing/pass/boolean_or02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testBooleanOr03() throws Exception {
        arrangeTest("./testing/pass/boolean_or03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\n", s);
    }
    
    @Test
    public void testBooleanPrecedenceTest() throws Exception {
        arrangeTest("./testing/pass/booleanPrecedenceTest.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\npassed\n", s);
    }

    @Test
    public void testCompareEqual01() throws Exception {
        arrangeTest("./testing/pass/compare_equal01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareEqual02() throws Exception {
        arrangeTest("./testing/pass/compare_equal02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareGreater01() throws Exception {
        arrangeTest("./testing/pass/compare_greater01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareGreater02() throws Exception {
        arrangeTest("./testing/pass/compare_greater02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareGreaterEqual01() throws Exception {
        arrangeTest("./testing/pass/compare_greater_equal01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareGreaterEqual02() throws Exception {
        arrangeTest("./testing/pass/compare_greater_equal02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareLess01() throws Exception {
        arrangeTest("./testing/pass/compare_less01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareLess02() throws Exception {
        arrangeTest("./testing/pass/compare_less02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareLessEqual01() throws Exception {
        arrangeTest("./testing/pass/compare_less_equal01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareLessEqual02() throws Exception {
        arrangeTest("./testing/pass/compare_less_equal02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareNotEqual01() throws Exception {
        arrangeTest("./testing/pass/compare_not_equal01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testCompareNotEqual02() throws Exception {
        arrangeTest("./testing/pass/compare_not_equal02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\npassed\n", s);
    }

    @Test
    public void testConditional01() throws Exception {
        arrangeTest("./testing/pass/conditional01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("1\n0\n", s);
    }

    @Test
    public void testConditional02() throws Exception {
        arrangeTest("./testing/pass/conditional02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("5\n10\n", s);
    }

    @Test
    public void testConditional03() throws Exception {
        arrangeTest("./testing/pass/conditional03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("ifStmt\n10\n", s);
    }

    @Test
    public void testExit01() throws Exception {
        arrangeTest("./testing/pass/exit01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 29 But got 29\n", s);
    }

    @Test
    public void testExit02() throws Exception {
        arrangeTest("./testing/pass/exit02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 0 But got 0\n", s);
    }

    @Test
    public void testExit03() throws Exception {
        arrangeTest("./testing/pass/exit03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 10000 But got 10000\n", s);
    }

    @Test
    public void testExit04() throws Exception {
        arrangeTest("./testing/pass/exit04.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 100 But got 100\n", s);
    }

    @Test
    public void testExit05() throws Exception {
        arrangeTest("./testing/pass/exit05.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 80 But got 80\n", s);
    }

    @Test
    public void testExit06() throws Exception {
        arrangeTest("./testing/pass/exit06.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 160 But got 160\n", s);
    }

    @Test
    public void testExit07() throws Exception {
        arrangeTest("./testing/pass/exit07.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 70 But got 70\n", s);
    }
    
    @Test
    public void scalar01() throws Exception {
        arrangeTest("./testing/pass/scalar01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("7\n", s);
    }
    
    @Test
    public void scalarArray01() throws Exception {
        arrangeTest("./testing/pass/scalar_array_01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("1\n2\n70\n", s);
    }

    @Test
    public void testFunction01() throws Exception {
        arrangeTest("./testing/pass/function01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSEP\n", s);
    }

    @Test
    public void testFunction02() throws Exception {
        arrangeTest("./testing/pass/function02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSEP\n", s);
    }

    @Test
    public void testFunction03() throws Exception {
        arrangeTest("./testing/pass/function03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSEP\n", s);
    }

    @Test
    public void testFunction04() throws Exception {
        arrangeTest("./testing/pass/function04.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSEP\n", s);
    }

    @Test
    public void testFunction05() throws Exception {
        arrangeTest("./testing/pass/function05.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSEP\n", s);
    }

    @Test
    public void testFunction06() throws Exception {
        arrangeTest("./testing/pass/function06.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("500\n500\n", s);
    }

    @Test
    public void testFunction07() throws Exception {
        arrangeTest("./testing/pass/function07.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSED\nPASSED\nPASSED\n", s);
    }
    
    @Test
    public void testFunction08() throws Exception {
        arrangeTest("./testing/pass/function08.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Passed\n", s);
    }
    
    @Test
    public void testFunction09() throws Exception {
        arrangeTest("./testing/pass/function09.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("PASSED\n", s);
    }
    
    @Test
    public void testIfStmt01() throws Exception {
        arrangeTest("./testing/pass/if_stmt01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("8\n", s);
    }

    @Test
    public void testIfStmt02() throws Exception {
        arrangeTest("./testing/pass/if_stmt02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("end\n", s);
    }

    @Test
    public void testIfStmt03() throws Exception {
        arrangeTest("./testing/pass/if_stmt03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("6\n99\n", s);
    }

    @Test
    public void testIfStmt04() throws Exception {
        arrangeTest("./testing/pass/if_stmt04.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("6\n99\n", s);
    }

    @Test
    public void testIfStmt() throws Exception {
        arrangeTest("./testing/pass/ifStmt.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("In false branch\n", s);
    }

    @Test
    public void testNotExpn01() throws Exception {
        arrangeTest("./testing/pass/not_expn01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("10\n", s);
    }

    @Test
    public void testNotExpn02() throws Exception {
        arrangeTest("./testing/pass/not_expn02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("5\n", s);
    }

    @Test
    public void testProcedure01() throws Exception {
        arrangeTest("./testing/pass/procedure01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected -3000 but got -3000\n", s);
    }

    @Test
    public void testProcedure02() throws Exception {
        arrangeTest("./testing/pass/procedure02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 5000 but got 5000\n", s);
    }

    @Test
    public void testProcedure03() throws Exception {
        arrangeTest("./testing/pass/procedure03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 6000 but got 6000\n", s);
    }

    @Test
    public void testProcedure04() throws Exception {
        arrangeTest("./testing/pass/procedure04.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("TRUE\n100\nExpected 2000 but got 2000\n", s);
    }

    @Test
    public void testProcedure05() throws Exception {
        arrangeTest("./testing/pass/procedure05.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 500 but got 500\nExpected 100 but got 100\n", s);
    }

    @Test
    public void testProcedure06() throws Exception {
        arrangeTest("./testing/pass/procedure06.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("Expected 6000 but got 6000\nFALSE Expected 200 but got 200\nExpected 100 but got 100\n", s);
    }

    @Test
    public void testProcedure07() throws Exception {
        arrangeTest("./testing/pass/procedure07.488");
        String s = FileUtils.readFileToString(testOutput);
        String expectedString = "11\n10\n9\n8\n7\n6\n5\n4\n3\n2\n1\ndone\n";
        Assert.assertEquals(expectedString, s);
    }

    @Test
    public void testRepeatUntil01() throws Exception {
        arrangeTest("./testing/pass/repeat_until01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("pass\n", s);
    }

    @Test
    public void testRepeatUntil02() throws Exception {
        arrangeTest("./testing/pass/repeat_until02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("pass\n", s);
    }

    @Test
    public void testUnaryMinus01() throws Exception {
        arrangeTest("./testing/pass/unary_minus01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("-89\n", s);
    }

    @Test
    public void testUnaryMinus02() throws Exception {
        arrangeTest("./testing/pass/unary_minus02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("-8\n", s);
    }
    
    @Test
    public void testUnaryMinus03() throws Exception {
        arrangeTest("./testing/pass/unary_minus03.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("8\n", s);
    }

    @Test
    public void testWhileDo01() throws Exception {
        arrangeTest("./testing/pass/while_do01.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("pass\n", s);
    }
    
    @Test
    public void testWhileDo02() throws Exception {
        arrangeTest("./testing/pass/while_do02.488");
        String s = FileUtils.readFileToString(testOutput);
        Assert.assertEquals("passed\n", s);
    }

}