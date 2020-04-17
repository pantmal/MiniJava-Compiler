import visitor.*;
import syntaxtree.*;
import java.util.*;



public class SecondVisitor extends GJDepthFirst<String, String>{
      
    public SymbolTable visitor_sym;
    public String curr_class;
    public String curr_meth;
    public boolean in_assign;

    public SecondVisitor(SymbolTable st){
        this.visitor_sym = st;   
    }



       /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
   public String visit(MainClass n, String argu) throws Exception {
    String _ret=null;
    n.f0.accept(this, argu);
    n.f1.accept(this, argu);
    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);
    n.f6.accept(this, argu);
    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);

    String id = n.f11.accept(this, argu);

    n.f12.accept(this, argu);
    n.f13.accept(this, argu);
    n.f14.accept(this, argu);
    n.f15.accept(this, argu);
    n.f16.accept(this, argu);
    n.f17.accept(this, argu);
    return _ret;
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
    this.curr_class = class_name;

    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);




    return _ret;
 }

  /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {
        String _ret=null;
        n.f0.accept(this, argu);
        String class_name = n.f1.accept(this, argu);
        this.curr_class = class_name;

        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        return _ret;
     }

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
        this.curr_meth = meth_name;
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);

        return _ret;
    }



      /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
  public String visit(AssignmentStatement n, String argu) throws Exception {
    
    String _ret=null;
    String left_id = n.f0.accept(this, argu);

    ClassTable temp = visitor_sym.classId_table.get(curr_class);
    
    Tuple<String, MethodTable> tupe = temp.methodId_table.get(curr_meth);

    //System.out.println("ANYTHING " + left_id);

    if ( !(tupe.y.local_table.containsKey(left_id) )  && !(tupe.y.param_table.containsKey(left_id))
          && !(temp.field_table.containsKey(left_id) ) ){  //WATCH OUT. NOT FOR FIELD OF MOTHER CLASS
        throw new Exception("Type error!");

    }

    String l_Type = null;

    if ( (tupe.y.local_table.containsKey(left_id) ) || (tupe.y.param_table.containsKey(left_id) ) ){
      if(  (tupe.y.local_table.containsKey(left_id) ) ){
        l_Type = tupe.y.local_table.get(left_id);
          System.out.println("ANYTHING ass_visit l " + l_Type);
      }
      if(  (tupe.y.param_table.containsKey(left_id) ) ){
        l_Type = tupe.y.param_table.get(left_id);
        System.out.println("ANYTHING ass_visit l " + l_Type);
      }
    }else{
      l_Type = temp.field_table.get(left_id);
    }



    n.f1.accept(this, argu);

    this.in_assign = true;
    String r_Type = n.f2.accept(this, argu);
    
    System.out.println("ANYTHING ass_visit r " + r_Type );

    if (l_Type != r_Type){ //CAREFUL SUBTYPES NEED CHECKING
      throw new Exception("Type error!");
    }
    
    
    n.f3.accept(this, argu);
    this.in_assign = false;

    return _ret;
 }

    /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | Clause()
    */
    public String visit(Expression n, String argu) {
      return n.f0.accept(this, argu);
   }


      /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    public String visit(AndExpression n, String argu) throws Exception {
      String _ret=null;

      String L_type = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String R_type = n.f2.accept(this, argu);

      if(L_type != "boolean" || R_type != "boolean" ){
        throw new Exception("Type error!");
      }

      return "boolean";
   }

      /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, String argu) throws Exception {
      String _ret=null;

      String L_type = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String R_type = n.f2.accept(this, argu);

      if(L_type != "int" || R_type != "int" ){
        throw new Exception("Type error!");
      }

      return "boolean";
   }

      /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, String argu) throws Exception {
      String _ret=null;

      String L_type = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String R_type = n.f2.accept(this, argu);

      if(L_type != "int" || R_type != "int" ){
        throw new Exception("Type error!");
      }

      return "int";
   }

      /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, String argu) throws Exception {
      String _ret=null;

      String L_type = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String R_type = n.f2.accept(this, argu);

      if(L_type != "int" || R_type != "int" ){
        throw new Exception("Type error!");
      }

      return "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n, String argu) throws Exception {
      String _ret=null;

      String L_type = n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      String R_type = n.f2.accept(this, argu);

      if(L_type != "int" || R_type != "int" ){
        throw new Exception("Type error!");
      }

      return "int";
   }


      /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, String argu) throws Exception {
      String _ret=null;

      String item_ret = null;
      String arr_name = n.f0.accept(this, argu);

      if (arr_name == "int[]" || arr_name == "boolean[]"){
        if (arr_name == "int[]"){
          item_ret = "int";
        }
        if (arr_name == "boolean[]"){
          item_ret = "boolean";
        }
      }else{
        throw new Exception("Type error!");
      }

      n.f1.accept(this, argu);
      String index = n.f2.accept(this, argu);

      if( index != "int" ){
        throw new Exception("Type error!");
      }

      n.f3.accept(this, argu);


      return item_ret;
   }

      /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, String argu) throws Exception {
      String _ret=null;
      
      String arr_name = n.f0.accept(this, argu);

      if (arr_name != "int[]" && arr_name != "boolean[]"){
        throw new Exception("Type error!");
      }

      n.f1.accept(this, argu);
      n.f2.accept(this, argu);

      return "int";
   }

      /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    public String visit(MessageSend n, String argu) throws Exception {
      String _ret=null;

      String obj_type = n.f0.accept(this, argu);

      if (!(this.visitor_sym.classId_table.containsKey(obj_type)) ){
        throw new Exception("Type error!");
      }

      n.f1.accept(this, argu);
      this.in_assign = false; //NOT RESET TO TRUE?
      String meth_name = n.f2.accept(this, argu);

      ClassTable temp = visitor_sym.classId_table.get(obj_type);

      if (!(temp.methodId_table.containsKey(meth_name)) ){
        throw new Exception("Type error!");
      }
      
      Tuple<String, MethodTable> tupe = temp.methodId_table.get(meth_name);

      String ret_type = tupe.x;

      n.f3.accept(this, argu);
      String expr_list = n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      
      if ( ( tupe.y.param_table != null && expr_list == null ) || ( tupe.y.param_table == null && expr_list != null )  ){
        throw new Exception("Type error!");
      }
      
      return ret_type;
   }

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
   public String visit(ExpressionList n, String argu) throws Exception {
    String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return "_ret";
   }

   /**
    * f0 -> ( ExpressionTerm() )*
    */
   public String visit(ExpressionTail n, String argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public String visit(ExpressionTerm n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }



   /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
    public String visit(Clause n, String argu) {
      
      String Type = n.f0.accept(this, argu);
      return Type;
      
   }

      /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | BracketExpression()
    */
    public String visit(PrimaryExpression n, String argu) {
      String Type = n.f0.accept(this, argu);
      return Type;
   }

      /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public String visit(AllocationExpression n, String argu) throws Exception{
      String _ret=null;
      n.f0.accept(this, argu);
      this.in_assign = false; //NOT RESET TO TRUE?
      String Type = n.f1.accept(this, argu);
      if (!(this.visitor_sym.classId_table.containsKey(Type)) ){
        throw new Exception("Type error!");
      }

      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return Type;
   }


      /**
    * f0 -> <IDENTIFIER>
    */
    public String visit(Identifier n, String argu) throws Exception {

      if (in_assign == true){
      
      ClassTable temp = visitor_sym.classId_table.get(curr_class);
    
      Tuple<String, MethodTable> tupe = temp.methodId_table.get(curr_meth);

      String id = n.f0.accept(this, argu);

      if ( !(tupe.y.local_table.containsKey(id) ) && !(tupe.y.param_table.containsKey(id)) 
           && !(temp.field_table.containsKey(id) ) ){  //WATCH OUT. NOT FOR FIELD OF MOTHER CLASS
          throw new Exception("Type error!");
      }

      String Type = null;

      if ( (tupe.y.local_table.containsKey(id) ) || (tupe.y.param_table.containsKey(id) ) ){
        if(  (tupe.y.local_table.containsKey(id) ) ){
            Type = tupe.y.local_table.get(id);
            System.out.println("ANYTHING id_visit " + Type);
        }
        if(  (tupe.y.param_table.containsKey(id) ) ){
          Type = tupe.y.param_table.get(id);
          System.out.println("ANYTHING id_visit " + Type);
        }
      }else{
        Type = temp.field_table.get(id);
      }

      return Type;

    }else{
      String id = n.f0.accept(this, argu);
      return id;
    }

    //String id = n.f0.accept(this, argu);
    //return id;

      
   }


   /**
    * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n, String argu) {
      return "int";
   }

   /**
    * f0 -> "this"
    */
    public String visit(ThisExpression n, String argu) {
      return curr_class;
   }

      /**
    * f0 -> "true"
    */
    public String visit(TrueLiteral n, String argu) {
      return "boolean";
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n, String argu) {
      return "boolean";
   }


      /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      String Type = n.f1.accept(this, argu);
      return Type;
   }

  public String visit(NodeToken n, String argu) { return n.toString(); }

}