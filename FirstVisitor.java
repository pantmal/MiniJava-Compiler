import visitor.*;
import syntaxtree.*;
import java.util.*;

//The First Visitor will create the Symbol Table so the Second Visitor can perform type checks.
public class FirstVisitor extends GJDepthFirst<String, String>{
      
    public SymbolTable visitor_sym;
    public List<String> idList;

    public FirstVisitor(){
          idList = new ArrayList<String>();
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

      //Initialize the symbol table, if null
      if (this.visitor_sym == null){
            this.visitor_sym = new SymbolTable();
      }

      String main_class = n.f1.accept(this, argu);

      //If the class id is already in the class_id table throw an exception.
      if( visitor_sym.classId_table != null ){

            if( visitor_sym.classId_table.containsKey(main_class) ){

                  throw new Exception("Declaration error!");      
            
            }
      }

      //Otherwise add the classId to the table.
      visitor_sym.add_class(main_class);


      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      
      String ret_Type = n.f5.accept(this, argu);
      String id = n.f6.accept(this, argu);

      ClassTable current = visitor_sym.get_last();

      //If the main method is already in the methodId table throw an exception.
      if (current.methodId_table != null){
            if( current.methodId_table.containsKey(id) ){
                
                  throw new Exception("Declaration error!");
            }
      }
      
      //Otherwise add the main method.
      current.meth_insert(id, ret_Type);


      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      
      
      String args = n.f11.accept(this, argu);

      MethodTable curr_meth = current.get_last_meth();

      //If the main method arguments are already in the parameter table of the main method, throw an exception.
      if (curr_meth.param_table != null){
            if( curr_meth.param_table.containsKey(args) ){
                
                  throw new Exception("Declaration error!");
            }
      }

      //Otherwise add them to the parameter table
      curr_meth.p_insert(args, "String[]");

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

      //Initialize the symbol table, if null
      if (this.visitor_sym == null){
            this.visitor_sym = new SymbolTable();
      }



      String _ret=null;
      String class_id;

      n.f0.accept(this, argu);
      class_id = n.f1.accept(this, argu);

      //If the class id is already in the class_id table throw an exception.
      if( visitor_sym.classId_table != null ){

            if( visitor_sym.classId_table.containsKey(class_id) ){

                  throw new Exception("Declaration error!");      
            
            }
      }

      //Otherwise add the classId to the table.
      visitor_sym.add_class(class_id);

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

      //If the symbol table is null, throw an exception.
      if (this.visitor_sym == null){
            throw new Exception("Declaration error!");
      }


      String _ret=null;

      String class_id;

      n.f0.accept(this, argu);
      class_id = n.f1.accept(this, argu);

      //If the class id is already in the class_id table throw an exception.
      if( visitor_sym.classId_table.containsKey(class_id) ){

            throw new Exception("Declaration error!");      
      
      }

      //Otherwise add the classId to the table.
      visitor_sym.add_class(class_id);

      n.f2.accept(this, argu);

      ClassTable current = visitor_sym.get_last();
      String mother = n.f3.accept(this, argu);
      
      //A class cannot extend from itself.
      if(mother == class_id){
            throw new Exception("Declaration error!");
      }

      //If the mother is already in the classId table throw an exception.
      if( !(visitor_sym.classId_table.containsKey(mother)) ){

            throw new Exception("Declaration error!");      
      
      }

      //Otherwise, add the mother's name to the ClassTable.
      current.mother = mother;

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      return _ret;
   }




	/**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, String argu) throws Exception {
      String _ret=null;
      String Type, id;

      //Getting the last ClassTable of the Symbol Table.
      ClassTable current = visitor_sym.get_last();

      
      Type = n.f0.accept(this, argu);
      Type = Type.toString();

      //If the type is not a primitive, add it to a list. We will use the list in the Main class to check if it has any objects from classes that were not declared. 
      if(Type != "int" && Type != "boolean" && Type != "int[]" && Type != "boolean[]"){
            idList.add(Type);
      }
      

      id = n.f1.accept(this, argu);

      n.f2.accept(this, argu);

      //If both the field and methodId tables are null, add the first field.
      if (current.field_table == null && current.methodId_table == null ){
            current.f_insert(id, Type);     
      }else if(current.methodId_table == null ){ //If the methodId table is null add a new field, if it's not already declared.
      
            if( current.field_table.containsKey(id) ){
                
                throw new Exception("Declaration error!");
            }

            current.f_insert(id, Type);     

      }else if(current.methodId_table != null ){ //If the methodId table has at least one entry, we add the variable to the local variables of the last method.

            MethodTable curr_meth = current.get_last_meth();

            //If the variable is already in the parameter or local variables table of the current method, throw an exception.
            if (curr_meth.param_table != null){
                  if( curr_meth.param_table.containsKey(id) ){
                      
                        throw new Exception("Declaration error!");
                  }
            }

            if (curr_meth.local_table != null){
                  if( curr_meth.local_table.containsKey(id) ){
                      
                        throw new Exception("Declaration error!");
                  }
            }            

            curr_meth.l_insert(id, Type);                        
      }


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
      
      String ret_Type, id;		

      //Getting the last classId and its ClassTable from the Symbol Table.
      ClassTable current = visitor_sym.get_last();
      String last_class = visitor_sym.get_last_key();

      n.f0.accept(this, argu);
      
      ret_Type = n.f1.accept(this, argu);
      id = n.f2.accept(this, argu);

      //If the type is not a primitive, add it to a list. We will use the list in the Main class to check if it has any objects from classes that were not declared. 
      if(ret_Type != "int" && ret_Type != "boolean" && ret_Type != "int[]" && ret_Type != "boolean[]"){
            idList.add(ret_Type);
      }
      
      //If we're in a hierarchy we need to get the first mother class that has the same function, to see if the return types are the same.
      if(current.mother != null){

            String new_mother = visitor_sym.mother_search(last_class,id);

            if (new_mother != null){ //If new_mother is null this means the function is not the class hierarchy.
                  ClassTable mother = visitor_sym.get(new_mother);

                  if (mother.methodId_table != null){
                        if( mother.methodId_table.containsKey(id) ){

                              Tuple<String, MethodTable> tupe =  mother.methodId_table.get(id);
                              if(tupe.x != ret_Type ){ //If the return types are different we have a declaration error.
                                    throw new Exception("Declaration error!");      
                              }
                        }
                  }     
            }

      }

      //Adding the methodId if it's not already in the table.
      if (current.methodId_table != null){
            if( current.methodId_table.containsKey(id) ){
                
                  throw new Exception("Declaration error!");
            }
      }
      
      current.meth_insert(id, ret_Type);

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);

      //Now we need to check the argument types, if the function is in another mother class.
      if(current.mother != null){

            String new_mother = visitor_sym.mother_search(last_class,id);

            if(new_mother != null) { //If new_mother is null this means the function is not the class hierarchy.
            
                  ClassTable mother = visitor_sym.get(new_mother);

                  if (mother.methodId_table != null){
                        if( mother.methodId_table.containsKey(id) ){

                              Tuple<String, MethodTable> mother_tupe =  mother.methodId_table.get(id);
                              Tuple<String, MethodTable> curr_tupe = current.methodId_table.get(id);

                              //Both parameter tables must be of the same size.
                              if( mother_tupe.y.param_table != null && curr_tupe.y.param_table == null){
                                    throw new Exception("Declaration error!");      
                              }
                              if( mother_tupe.y.param_table == null && curr_tupe.y.param_table != null){
                                    throw new Exception("Declaration error!");      
                              }
                              if( mother_tupe.y.param_table != null && curr_tupe.y.param_table != null){
                                    if (mother_tupe.y.param_table.size() != curr_tupe.y.param_table.size()){
                                          throw new Exception("Declaration error!");      
                                    }

                                    //If they both have the same size, we need to check if the argument types are in the right order.
                                    int counter = 0;
                                    for (String i : curr_tupe.y.param_table.keySet()) {

                                          //Getting the type in our current method.
                                          String curr_check = curr_tupe.y.param_table.get(i);
                                          
                                          //Getting the type in the mother using as the index the counter we're on.
                                          String check = mother_tupe.y.getKey(counter);
                                          String mother_type = mother_tupe.y.param_table.get(check);
                                          
                                          //Here, the type in the mother is different than that of the current class, so we throw an exception.
                                          if(curr_check != mother_type){
                                                throw new Exception("Declaration error!");      
                                          }
                                          
                                          counter++;

                                    }  
                              }
                        
                        }

                  }

            }

      }


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
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public String visit(FormalParameter n, String argu) throws Exception {
      String _ret=null;

      String Type, id;		

      //Getting the last ClassTable from the Symbol Table.
      ClassTable current = visitor_sym.get_last();

      //Getting the last methodId from the current ClassTable.
      MethodTable curr_meth = current.get_last_meth();

      Type = n.f0.accept(this, argu);
      id = n.f1.accept(this, argu);

      //If the type is not a primitive, add it to a list. We will use the list in the Main class to check if it has any objects from classes that were not declared.       
      if(Type != "int" && Type != "boolean" && Type != "int[]" && Type != "boolean[]"){
            idList.add(Type);
      }

      //Add the variable to the parameter table, if it's not already in the table.
      if (curr_meth.param_table != null){
            if( curr_meth.param_table.containsKey(id) ){
                
                  throw new Exception("Declaration error!");
            }
      }

      curr_meth.p_insert(id, Type);


      return _ret;
   }


   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
    public String visit(Type n, String argu) throws Exception {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> BooleanArrayType()
    *       | IntegerArrayType()
    */
    public String visit(ArrayType n, String argu) throws Exception {
      return n.f0.accept(this, argu);
   }

    /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(IntegerArrayType n, String argu) throws Exception {
      //R _ret=null;

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);

      

      return "int[]";
   }


   /**
    * f0 -> "boolean"
    * f1 -> "["
    * f2 -> "]"
    */
    public String visit(BooleanArrayType n, String argu) throws Exception {
     //R _ret=null;

     n.f0.accept(this, argu);
     n.f1.accept(this, argu);
     n.f2.accept(this, argu);

     

     return "boolean[]";

   }

    //Returning tokens as strings.
    public String visit(NodeToken n, String argu) throws Exception { return n.toString(); }
   
}
