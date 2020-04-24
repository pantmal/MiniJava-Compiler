import visitor.*;
import syntaxtree.*;
import java.util.*;

//The Second Visitor will perform type checks in our program.
public class SecondVisitor extends GJDepthFirst<String, String>{
      
    public SymbolTable visitor_sym; //The SymbolTable of this Visitor.
    public String curr_class; //Curr_class will get the "scope" of the last class we're on.
    public String curr_meth; //Curr_class will get the "scope" of the last method we're on.
    public Stack counterStack; //counterStack will used to store some counters. Its usage will be explained in the MessageSend visit.
    public boolean give_type; //Give_type will be used in order to get the type of the identifier.
    // Will be set to false in some cases because we may need the name of the identifier and not its type. 
    
    
    //The constructor gets the SymbolTable object. 
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

    //Setting the scope of the Main class and main method using the curr_class and curr_meth fields.
    this.give_type = false;
    String main_class = n.f1.accept(this, argu);
    this.curr_class = main_class;

    n.f2.accept(this, argu);
    n.f3.accept(this, argu);
    n.f4.accept(this, argu);
    n.f5.accept(this, argu);

    String main = n.f6.accept(this, argu);
    this.curr_meth = main;

    n.f7.accept(this, argu);
    n.f8.accept(this, argu);
    n.f9.accept(this, argu);
    n.f10.accept(this, argu);

    this.give_type = false;
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

    //Setting the scope of the current class we're on.
    this.give_type = false;
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

        //Setting the scope of the current class we're on.
        this.give_type = false;
        String class_name = n.f1.accept(this, argu);
        this.curr_class = class_name;

        n.f2.accept(this, argu);
        
        this.give_type = false;
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

        this.give_type = false;
        String ret_type = n.f1.accept(this, argu);

        //Setting the scope of the current method we're on.
        this.give_type = false;
        String meth_name = n.f2.accept(this, argu);
        this.curr_meth = meth_name;

        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);

        this.give_type = true;
        String expr_type = n.f10.accept(this, argu);


        if (meth_name != "main"){
          ClassTable right_table = null;

          //Getting the Class Table of the type returned from the expression (if it's a class)
          if(expr_type != "int" && expr_type != "boolean" && expr_type != "int[]" && expr_type != "boolean[]"){
            right_table = visitor_sym.classId_table.get(expr_type);
          }
      
          if(right_table!=null){
            
            //If the expression type has no mother, we check if the return type is different that the one returned from the expression.
            if(right_table.mother==null){
              if (ret_type != expr_type){ 
                throw new Exception("Type error!");
              }  
            }else{ //If the expression type has a mother, we need to check if the return type is in the hierarchy.
              boolean child = visitor_sym.is_child(ret_type,expr_type);
              if( ret_type != expr_type  && child == false ){ //If child is false, this means expr_type does not have ret_type as its mother.
                throw new Exception("Type error!");  
              }
            }
      
          }else{
            
            //If the return type is different than the one returned from the expression, we have a type error.
            if (ret_type != expr_type){ 
              throw new Exception("Type error!");
            }
          }
        }

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

    //Getting the left type.
    this.give_type = true;
    String l_Type = n.f0.accept(this, argu);

    //Getting the ClassTable of our current class.
    ClassTable temp = visitor_sym.classId_table.get(curr_class);
    
    //Same thing for the method.
    Tuple<String, MethodTable> tupe = temp.methodId_table.get(curr_meth);

  
    n.f1.accept(this, argu);

    //Getting the right type.
    this.give_type = true;
    String r_Type = n.f2.accept(this, argu);
    
    //Getting the Class Table of the type returned from the expression (if it's a class).
    ClassTable right_table = null;
    if(r_Type != "int" && r_Type != "boolean" && r_Type != "int[]" && r_Type != "boolean[]"){
      right_table = visitor_sym.classId_table.get(r_Type);
    }

    if(right_table!=null){

      //If the expression type has no mother, we check if the left type is different that the one returned from the expression.
      if(right_table.mother==null){
        if (l_Type != r_Type){ 
          throw new Exception("Type error!");
        }  
      }else{ //If the expression type has a mother, we need to check if the left type is in the hierarchy.
        boolean child = visitor_sym.is_child(l_Type,r_Type);
        if( l_Type != r_Type  && child == false ){ //If child is false, this means r_Type does not have l_Type as its mother.
          throw new Exception("Type error!");  
        }
      }

    }else{

      //If the left type is different than the one returned from the expression, we have a type error.
      if (l_Type != r_Type){ 
        throw new Exception("Type error!");
      }
    }
    
    
    n.f3.accept(this, argu);
    this.give_type = false;

    return _ret;
 }


    /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
    public String visit(ArrayAssignmentStatement n, String argu) throws Exception {
      String _ret=null;

      this.give_type = true;
      String left_Type = n.f0.accept(this, argu);

      if( (left_Type != "int[]") && (left_Type != "boolean[]") ){ //Left type must be an array.
        throw new Exception("Type error!");
      }

      String arr_type = null; //Setting the appropiate array type.
      if (left_Type == "int[]" ){
        arr_type = "int[]";
      }else if (left_Type == "boolean[]" ){
        arr_type = "boolean[]";
      }


      n.f1.accept(this, argu);
      String index = n.f2.accept(this, argu);

      if (index != "int"){ //Index must be of type int.
        throw new Exception("Type error!");
      }

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);

      String right_Type = n.f5.accept(this, argu);

      if( (right_Type != "int") && (right_Type != "boolean")  ){ //Right type must be either int or boolean.
        throw new Exception("Type error!");
      }

      if (right_Type == "int" ){ //Ints have to be stored in an int[] type.
        if(arr_type != "int[]"){
          throw new Exception("Type error!");
        }
      }else if (right_Type == "boolean" ){//Same thing for booleans.
        if( arr_type != "boolean[]"){
          throw new Exception("Type error!");
        }
      }


      n.f6.accept(this, argu);
      return _ret;
   }

      /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
    public String visit(IfStatement n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);

      this.give_type = true;
      String cond = n.f2.accept(this, argu);

      if (cond != "boolean"){ //The expression inside the condition must be of boolean type.
        throw new Exception("Type error!");
      }

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public String visit(WhileStatement n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);

      this.give_type = true;
      String cond = n.f2.accept(this, argu);

      if (cond != "boolean"){ //The expression inside the condition must be of boolean type.
        throw new Exception("Type error!");
      }

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }


   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public String visit(PrintStatement n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);

      this.give_type = true;
      String print_arg = n.f2.accept(this, argu);

      if (print_arg != "int"){ //The expression inside the println() argument must be of int type.
        throw new Exception("Type error!");
      }

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
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
    public String visit(Expression n, String argu) throws Exception {
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

      if(L_type != "boolean" || R_type != "boolean" ){ //Both clauses in an AndExpression must of boolean type.
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

      this.give_type = true;
      String L_type = n.f0.accept(this, argu);
      
      n.f1.accept(this, argu);

      this.give_type = true;
      String R_type = n.f2.accept(this, argu);
      

      if(L_type != "int" || R_type != "int" ){ //Both primary expressions in a CompareExpression must of int type.
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

      if(L_type != "int" || R_type != "int" ){ //Both primary expressions in a PlusExpression must of int type.
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

      if(L_type != "int" || R_type != "int" ){ //Both primary expressions in a MinusExpression must of int type.
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

      if(L_type != "int" || R_type != "int" ){ //Both primary expressions in a TimesExpression must of int type.
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
      String arr_type = n.f0.accept(this, argu);

      if (arr_type == "int[]" || arr_type == "boolean[]"){ //Setting the appropiate return type based on the array type.
        if (arr_type == "int[]"){
          item_ret = "int";
        }
        if (arr_type == "boolean[]"){
          item_ret = "boolean";
        }
      }else{
        throw new Exception("Type error!");
      }

      n.f1.accept(this, argu);

      String index = n.f2.accept(this, argu);

      if( index != "int" ){ //The index must be of type int.
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
      
      String arr_type = n.f0.accept(this, argu);

      if (arr_type != "int[]" && arr_type != "boolean[]"){ //Making sure we have an array type.
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

      this.give_type = true;
      String obj_type = n.f0.accept(this, argu);

    
      if (!(this.visitor_sym.classId_table.containsKey(obj_type)) ){ //Making sure the type of the object appears in the classId table.
        throw new Exception("Type error!");
      }

      n.f1.accept(this, argu);
      this.give_type = false; 
      String meth_name = n.f2.accept(this, argu);

      //Getting its Class Table.
      ClassTable temp = visitor_sym.classId_table.get(obj_type);
      
      Tuple<String, MethodTable> tupe;

      if (temp.mother == null){ //If the object's class doesn't have a mother 
        if (temp.methodId_table == null){ //Throw an exception if there aren't any methods.
          throw new Exception("Type error!");
        }
        if (!(temp.methodId_table.containsKey(meth_name)) ){//Or if the method isn't in the methodId table.
          throw new Exception("Type error!");
        }
        
        tupe = temp.methodId_table.get(meth_name); //Otherwise get the tuple containing the return type and MethodTable of the method.

      }else{ //If the object's class has at least one mother 

        //Check if the method Id is in the table of our current class.
        boolean not_local = false;
        if (temp.methodId_table == null){
          not_local = true;
        }else if (!(temp.methodId_table.containsKey(meth_name)) ){
          not_local = true;
        }
        
        //If the method Id is in the table of our current class, get the tuple containing the return type and MethodTable.
        if(not_local == false){
          tupe = temp.methodId_table.get(meth_name);
        }else{ //If it's not in the current table, we will check the table of the first mother we find.
          
          String new_mother = visitor_sym.mother_search(obj_type,meth_name);
          if (new_mother != null){ //If new_mother is not null this means the function is the class hierarchy. 
            
            //So we"ll get the tuple from the ClassTable of the mother we found.
            ClassTable mother_table = visitor_sym.get(new_mother);
            tupe = mother_table.methodId_table.get(meth_name);
          }else{ //If new_mother is null this means the function is not the class hierarchy.
            throw new Exception("Type error!");
          }
        }
        

      }

      //The return type is the left part of the tuple.
      String ret_type = tupe.x;

      n.f3.accept(this, argu);
      this.give_type = true;

      //We create a counterStack here. This Stack will keep the counters of every argument list.
      if ( counterStack == null ){
        counterStack = new Stack();  
      }

      int counter = 0;
      counterStack.push(counter);

      String comb = obj_type + "-" + meth_name; //Passing the combination of the object type and method name for the next accept function.

      String expr_list = n.f4.accept(this, comb);
      
      //Here the calling function parameter table does not have the same size as the one it was declared with.       
      if ( ( tupe.y.param_table != null && expr_list == null ) || ( tupe.y.param_table == null && expr_list != null )  ){
        throw new Exception("Type error!");
      }

      //Same check here, with the difference that both parameter tables have at least one item.
      if (tupe.y.param_table != null && expr_list != null){
        
        int arg_counter = (int) counterStack.peek();
        if (tupe.y.param_table.size() != (arg_counter+1) ){
          throw new Exception("Type error!");
        }

      }

      //We finished checking the parameter tables, so the counter can be removed from the stack.
      counterStack.pop();

      n.f5.accept(this, argu);
      
      return ret_type;
   }

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
   public String visit(ExpressionList n, String argu) throws Exception {
    String _ret=null;

      String Type = n.f0.accept(this, argu); //Getting the Type of the calling expression.

      int curr_counter = (int) counterStack.peek(); //Getting the last counter.


      String string = argu; //Splitting the combined string.
      String[] parts = string.split("-");
      String obj_type = parts[0]; 
      String meth_name = parts[1];

      //Getting the tables we need.
      ClassTable temp = visitor_sym.classId_table.get(obj_type);
     
      Tuple<String, MethodTable> tupe = temp.methodId_table.get(meth_name);

      //Getting the type we want from the parameter table using as the index the counter we're on.
      String check = tupe.y.getKey(curr_counter);
      String type_check = tupe.y.param_table.get(check);

      //Getting the Class Table of the type from the calling expression (if it's a class).
      ClassTable right_table = null;
      if(Type != "int" && Type != "boolean" && Type != "int[]" && Type != "boolean[]"){
        right_table = visitor_sym.classId_table.get(Type);
      }
  
      if(right_table!=null){

        //If the calling expression type has no mother, we check if the type in the declaration is different from the one of the calling expression.
        if(right_table.mother==null){
          if (type_check != Type){ 
            throw new Exception("Type error!");
          }  
        }else{ //If the calling expression type has a mother, we need to check if the declaration type is in the hierarchy.
          boolean child = visitor_sym.is_child(type_check,Type);
          if( type_check != Type  && child == false ){ //If child is false, this means calling expression type does not have the declaration type as its mother.
            throw new Exception("Type error!");  
          }
        }
  
      }else{
  
        //If the calling expression type is different than the type in the declaration, we have a type error.
        if (type_check != Type){ 
          throw new Exception("Type error!");
        }
      }

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

      //We have a new expression here, so we increment the last counter in the Stack.
      int inc_counter = (int) counterStack.peek();
      inc_counter++;

      //Changing its value in the Stack
      counterStack.set((counterStack.size()-1), inc_counter); 

      int curr_counter = (int) counterStack.peek(); //Getting the last counter.
    
      String Type = n.f1.accept(this, argu); //Getting the Type of the calling expression.

      String string = argu;//Splitting the combined string.
      String[] parts = string.split("-");
      String obj_type = parts[0]; 
      String meth_name = parts[1]; 

      //Getting the tables we need.
      ClassTable temp = visitor_sym.classId_table.get(obj_type);
     
      Tuple<String, MethodTable> tupe = temp.methodId_table.get(meth_name);

      //If the counter exceeded the size of the parameter table we have more arguments in the calling expression.
      if ( tupe.y.param_table.size() < (curr_counter+1) ){ 
        throw new Exception("Type error!");
      }

      //Getting the type we want from the parameter table using as the index the counter we're on.
      String check = tupe.y.getKey(curr_counter);
      String type_check = tupe.y.param_table.get(check);

      //Getting the Class Table of the type from the calling expression (if it's a class).
      ClassTable right_table = null;
      if(Type != "int" && Type != "boolean" && Type != "int[]" && Type != "boolean[]"){
        right_table = visitor_sym.classId_table.get(Type);
      }
  
      if(right_table!=null){

        //If the calling expression type has no mother, we check if the type in the declaration is different from the one of the calling expression.
        if(right_table.mother==null){
          if (type_check != Type){ 
            throw new Exception("Type error!");
          }  
        }else{ //If the calling expression type has a mother, we need to check if the declaration type is in the hierarchy.
          boolean child = visitor_sym.is_child(type_check,Type);
          if( type_check != Type  && child == false ){ //If child is false, this means calling expression type does not have the declaration type as its mother.
            throw new Exception("Type error!");  
          }
        }
  
      }else{
  
        //If the calling expression type is different than the type in the declaration, we have a type error.
        if (type_check != Type){ 
          throw new Exception("Type error!");
        }
      }


      
      return _ret;
   }



   /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
    public String visit(Clause n, String argu) throws Exception {
      
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
    public String visit(PrimaryExpression n, String argu) throws Exception {
      String Type = n.f0.accept(this, argu);
      return Type;
   }


      /**
    * f0 -> <IDENTIFIER>
    */
    public String visit(Identifier n, String argu) throws Exception {

      if (give_type == true){ //Returning the Type if the identifier.
      
        //Getting the tables we need.
        ClassTable temp = visitor_sym.classId_table.get(curr_class);
        Tuple<String, MethodTable> tupe = temp.methodId_table.get(curr_meth);

        String id = n.f0.accept(this, argu);

        String Type = null;

        //Taking in account the many cases we have.

        //The general idea for every if statement is as follows: 
        //First we check if the variable is in the local or parameter table of the method we're in. 
        //If not we check the field table of the class we're in. 
        //If not we also check the field table of the first mother we find (if the class is in hierarchy).

        //Both parameter and local variable tables are null
        if ( tupe.y.param_table == null && tupe.y.local_table == null  ){
          if ( temp.field_table != null  ){
            if( temp.field_table.containsKey(id) ) {
              Type = temp.field_table.get(id);
              return Type;
            }else{
              if (temp.mother != null ){
                ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                if (Type == null){
                  throw new Exception("Type error!");    
                }else{
                  return Type;
                }
              }
              throw new Exception("Type error!");
            }
          }else{
            if (temp.mother != null ){
              ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
              Type = mother_t.recurse_lookup(id, visitor_sym.classId_table);
              if (Type == null){
                throw new Exception("Type error!");    
              }else{
                return Type;
              }
            }
            throw new Exception("Type error!");
          }
        }

        //Only parameter table is null. 
        if ( tupe.y.param_table == null && tupe.y.local_table != null  ){
          if ( tupe.y.local_table.containsKey(id)  ){
            Type = tupe.y.local_table.get(id);
            return Type;
          }else{
            if ( temp.field_table != null  ){
              if( temp.field_table.containsKey(id) ) {
                Type = temp.field_table.get(id);
                return Type;
              }else{
                if (temp.mother != null ){
                  ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                  Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                  if (Type == null){
                    throw new Exception("Type error!");    
                  }else{
                    return Type;
                  }
                }
                throw new Exception("Type error!");
              }
            }else{
              if (temp.mother != null ){
                ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                if (Type == null){
                  throw new Exception("Type error!");    
                }else{
                  return Type;
                }
              }
              throw new Exception("Type error!");
            }
          }
        }

        //Only local table is null. 
        if ( tupe.y.param_table != null && tupe.y.local_table == null  ){
          if ( tupe.y.param_table.containsKey(id)  ){
            Type = tupe.y.param_table.get(id);
            return Type;
          }else{
            if ( temp.field_table != null  ){
              if( temp.field_table.containsKey(id) ) {
                Type = temp.field_table.get(id);
                return Type;
              }else{
                if (temp.mother != null ){
                  ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                  Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                  if (Type == null){
                    throw new Exception("Type error!");    
                  }else{
                    return Type;
                  }
                }
                throw new Exception("Type error!");
              }
            }else{
              if (temp.mother != null ){
                ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                if (Type == null){
                  throw new Exception("Type error!");    
                }else{
                  return Type;
                }
              }
              throw new Exception("Type error!");
            }
          }
        }

        //Both parameter and local tables have at least one variable.
        if ( tupe.y.param_table != null && tupe.y.local_table != null  ){
          if ( tupe.y.param_table.containsKey(id)  ){
            Type = tupe.y.param_table.get(id);
            return Type;
          }else if ( tupe.y.local_table.containsKey(id) ){
            Type = tupe.y.local_table.get(id);
            return Type;
          }else{
            if ( temp.field_table != null  ){
              if( temp.field_table.containsKey(id) ) {
                Type = temp.field_table.get(id);
                return Type;
              }else{
                if (temp.mother != null ){
                  ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                  Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                  if (Type == null){
                    throw new Exception("Type error!");    
                  }else{
                    return Type;
                  }
                }
                throw new Exception("Type error!");
              }
            }else{
              if (temp.mother != null ){
                ClassTable mother_t = visitor_sym.classId_table.get(temp.mother);
                Type = mother_t.recurse_lookup(id, visitor_sym.classId_table );
                if (Type == null){
                  throw new Exception("Type error!");    
                }else{
                  return Type;
                }
              }
              throw new Exception("Type error!");
            }
          }
        }

        return Type;

    }else{ //In this case we return the name of the identifier.
      String id = n.f0.accept(this, argu);
      return id;
    }

      
  }


   /**
    * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n, String argu) throws Exception {
      return "int";
    }

   /**
    * f0 -> "this"
    */
    public String visit(ThisExpression n, String argu) throws Exception {
      return curr_class; //In a ThisExpression we return the current class we're on.
   }

      /**
    * f0 -> "true"
    */
    public String visit(TrueLiteral n, String argu) throws Exception {
      return "boolean";
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n, String argu) throws Exception {
      return "boolean";
   }

      /**
    * f0 -> BooleanArrayAllocationExpression()
    *       | IntegerArrayAllocationExpression()
    */
    public String visit(ArrayAllocationExpression n, String argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "new"
    * f1 -> "boolean"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(BooleanArrayAllocationExpression n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);

      String index = n.f3.accept(this, argu);

      if (index != "int"){ //The index in an array must be of int type.
        throw new Exception("Type error!");
      }

      n.f4.accept(this, argu);
      return "boolean[]";
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(IntegerArrayAllocationExpression n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      String index = n.f3.accept(this, argu);

      if (index != "int"){ //The index in an array must be of int type.
        throw new Exception("Type error!");
      }

      n.f4.accept(this, argu);
      return "int[]";
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
      
      this.give_type = false; 

      String Type = n.f1.accept(this, argu);
      if (!(this.visitor_sym.classId_table.containsKey(Type)) ){ //Check if the Type in the classId table.
        throw new Exception("Type error!");
      }
      this.give_type = true; 

      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return Type;
   }


      /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      String Type = n.f1.accept(this, argu);
      return Type;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, String argu) throws Exception {
      String _ret=null;
      n.f0.accept(this, argu);
      String Type = n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      
      return Type;
   }

   
  public String visit(NodeToken n, String argu) throws Exception { return n.toString(); }

}