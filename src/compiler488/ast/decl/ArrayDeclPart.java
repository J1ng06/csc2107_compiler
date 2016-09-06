package compiler488.ast.decl;

import compiler488.ast.PrettyPrinter;
import compiler488.visitor.IVisitor;

/**
 * Holds the declaration part of an array.
 */
public class ArrayDeclPart extends DeclarationPart {
    /**
     * The lower bound of dimension 1.
     */
    private ArrayBoundPart bound1;

    /**
     * The lower bound of dimension 2 (if any.)
     */
    private ArrayBoundPart bound2 = null;

    /**
     * True iff this is an 2D array
     */
    private Boolean isTwoDimensional = false;

    public ArrayDeclPart(String name, ArrayBoundPart bound1, int line, int column) {
        super(name, line, column);

        this.bound1 = bound1;

        this.isTwoDimensional = false;
        this.bound2 = null;
        this.isArray = true;
    }

    public ArrayDeclPart(String name, ArrayBoundPart bound1, ArrayBoundPart bound2, int line, int column) {
        this(name, bound1, line, column);

        this.isTwoDimensional = true;
        this.bound2 = bound2;
    }

    /**
     public ArrayDeclPart(String name, Integer[] dim1, int line, int column) {
     this(name, dim1[0], dim1[1], line, column);
     }

     public ArrayDeclPart(String name, Integer[] dim1, Integer[] dim2) {
     this(name, dim1[0], dim1[1], dim2[0], dim2[1]);
     }
     */

    /**
     * Returns a string that describes the array.
     */
    @Override
    public String toString() {
        return name + "[" + bound1.getLb() + ".." + bound1.getUb() + (isTwoDimensional ? ", " + bound2.getLb() + ".." + bound2.getUb() : "") + "]";
    }

    /**
     * Calculates the number of values held in an array declared in this way.
     * <p/>
     * TODO: Add a correct computation of the size of this array.
     *
     * @return size of the array
     */
    public int getBound1Size() {
        return Math.abs(bound1.getUb() - bound1.getLb()) + 1; 
    }
    
    public int getBound2Size() {
        if (this.isTwoDimensional) {
            return Math.abs(bound2.getUb() - bound2.getLb()) + 1;
        } 
        else {
            throw new UnsupportedOperationException();
        }
    }

    public Boolean isTwoDim() {
        return this.isTwoDimensional;
    }

    public ArrayBoundPart getBound1() {
        return this.bound1;
    }

    public ArrayBoundPart getBound2() {
        return this.bound2;
    }

    @Override
    public void prettyPrint(PrettyPrinter p) {
        p.print(name + "[" + bound1.getLb() + ".." + bound1.getUb());

        if (isTwoDimensional) {
            p.print(", " + bound2.getLb() + ".." + bound2.getUb());
        }

        p.print("]");
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
