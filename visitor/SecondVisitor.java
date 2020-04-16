import visitor.*;
import syntaxtree.*;
import java.util.*;



public class SecondVisitor extends GJDepthFirst<String, String>{
      
    public SymbolTable visitor_sym;
    public String curr_class;
    public String curr_meth;

    public SecondVisitor(SymbolTable st){
        this.visitor_sym = st;   
    }


       /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public String visit(ClassDeclaration n, String argu) throws Exception {

    String _ret=null;

    n.f0.accept(this, argu);
    String class_name = n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    this.curr_class = class_name;

    System.out.println("noooo " + class_name);

    return _ret;
 }

  //Yada yada, extends declaration

     /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
    public String visit(MethodDeclaration n, String argu) throws Exception {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String meth_name = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        String stmt = n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);


        curr_meth = meth_name;

        System.out.println("noooo " + meth_name);

        return _ret;
    }



      /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public String visit(Statement n, String argu) {
    return n.f0.accept(this, argu);
 }

      /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
  public String visit(AssignmentStatement n, String argu) {
    
    String _ret=null;
    String left_id = n.f0.accept(this, argu);

    ClassTable temp = visitor_sym.classId_table.get(curr_class);

    System.out.println("noooo " + this.curr_class);
    
    Tuple<String, MethodTable> tupe = temp.methodId_table.get(curr_meth);

    if ( !(tupe.y.local_table.containsKey(left_id) ) ){
        System.out.println("Errpr!42");
    }

    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);

    return _ret;
 }


  public String visit(NodeToken n, String argu) { return n.toString(); }

}