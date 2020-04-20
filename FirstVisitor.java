import visitor.*;
import syntaxtree.*;
import java.util.*;


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

      if (this.visitor_sym == null){
            this.visitor_sym = new SymbolTable();
      }

      String main_class = n.f1.accept(this, argu);

      if( visitor_sym.classId_table != null ){

            if( visitor_sym.classId_table.containsKey(main_class) ){

                  throw new Exception("Semactic error!");      
            
            }
      }


      visitor_sym.add_class(main_class);

      //Add some garbage code here so the local variables don't end up in fields

      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      
      String ret_Type = n.f5.accept(this, argu);
      String id = n.f6.accept(this, argu);

      ClassTable current = visitor_sym.get_last();

      if (current.methodId_table != null){
            if( current.methodId_table.containsKey(id) ){
                
                  throw new Exception("Semactic error!");
            }
      }
      
      current.meth_insert(id, ret_Type);


      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      
      
      String args = n.f11.accept(this, argu);

      MethodTable curr_meth = current.get_last_meth();

      if (curr_meth.param_table != null){
            if( curr_meth.param_table.containsKey(args) ){
                
                  throw new Exception("Semactic error!");
            }
      }

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

      if (this.visitor_sym == null){
            this.visitor_sym = new SymbolTable();
      }



      String _ret=null;
      String class_id;

      n.f0.accept(this, argu);
      class_id = n.f1.accept(this, argu);

      if( visitor_sym.classId_table != null ){

            if( visitor_sym.classId_table.containsKey(class_id) ){

                  throw new Exception("Semactic error!");      
            
            }
      }


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

      if (this.visitor_sym == null){
            throw new Exception("Semactic error!");
      }


      String _ret=null;

      String class_id;

      n.f0.accept(this, argu);
      class_id = n.f1.accept(this, argu);

      if( visitor_sym.classId_table.containsKey(class_id) ){

            throw new Exception("Semactic error!");      
      
      }



      visitor_sym.add_class(class_id);

      n.f2.accept(this, argu);

      ClassTable current = visitor_sym.get_last();
      String mother = n.f3.accept(this, argu);
      
      if(mother == class_id){
            throw new Exception("Semactic error!");
      }

      if( !(visitor_sym.classId_table.containsKey(mother)) ){

            throw new Exception("Semactic error!");      
      
      }

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

      ClassTable current = visitor_sym.get_last();

      

      Type = n.f0.accept(this, argu);
      
      Type = Type.toString();


      if(Type != "int" && Type != "boolean" && Type != "int[]" && Type != "boolean[]"){
            idList.add(Type);
      }

      
      

      id = n.f1.accept(this, argu);
	System.out.println(Type + " " + id);
      n.f2.accept(this, argu);

      if (current.field_table == null && current.methodId_table == null ){
            current.f_insert(id, Type);     
      }else if(current.methodId_table == null ){
            if( current.field_table.containsKey(id) ){
                
                throw new Exception("Semactic error!");
            }

            current.f_insert(id, Type);     

      }else if(current.methodId_table != null ){

            MethodTable curr_meth = current.get_last_meth();

            if (curr_meth.param_table != null){
                  if( curr_meth.param_table.containsKey(id) ){
                      
                        throw new Exception("Semactic error!");
                  }
            }

            if (curr_meth.local_table != null){
                  if( curr_meth.local_table.containsKey(id) ){
                      
                        throw new Exception("Semactic error!");
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

      ClassTable current = visitor_sym.get_last();


      n.f0.accept(this, argu);
      
      ret_Type = n.f1.accept(this, argu);
      id = n.f2.accept(this, argu);

      if(ret_Type != "int" && ret_Type != "boolean" && ret_Type != "int[]" && ret_Type != "boolean[]"){
            idList.add(ret_Type);
      }
      
      if(current.mother != null){
            ClassTable mother = visitor_sym.get(current.mother);
            if( mother.methodId_table.containsKey(id) ){

                  Tuple<String, MethodTable> tupe =  mother.methodId_table.get(id);
                  if(tupe.x != ret_Type ){
                        throw new Exception("Semactic error!");      
                  }
            
            }
      }

      if (current.methodId_table != null){
            if( current.methodId_table.containsKey(id) ){
                
                  throw new Exception("Semactic error!");
            }
      }
      
      current.meth_insert(id, ret_Type);

      n.f3.accept(this, argu);
      n.f4.accept(this, argu);

      if(current.mother != null){
            ClassTable mother = visitor_sym.get(current.mother);
            if( mother.methodId_table.containsKey(id) ){


                  Tuple<String, MethodTable> mother_tupe =  mother.methodId_table.get(id);
                  Tuple<String, MethodTable> curr_tupe = current.methodId_table.get(id);
                  if( mother_tupe.y.param_table != null && curr_tupe.y.param_table == null){
                        throw new Exception("Semactic error!");      
                  }
                  if( mother_tupe.y.param_table == null && curr_tupe.y.param_table != null){
                        throw new Exception("Semactic error!");      
                  }
                  if( mother_tupe.y.param_table != null && curr_tupe.y.param_table != null){
                        if (mother_tupe.y.param_table.size() != curr_tupe.y.param_table.size()){
                              System.out.println(mother_tupe.y.param_table.size());
                              System.out.println(curr_tupe.y.param_table.size());
                              throw new Exception("Semactic error!");      
                        }
                        int counter = 0;
                        for (String i : curr_tupe.y.param_table.keySet()) {
                              String curr_check = curr_tupe.y.param_table.get(i);
                              
                              String check = mother_tupe.y.getKey(counter);
                              String mother_type = mother_tupe.y.param_table.get(check);

                              if(curr_check != mother_type){
                                    throw new Exception("Semactic error!");      
                              }
                              
                              counter++;

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

      ClassTable current = visitor_sym.get_last();

      MethodTable curr_meth = current.get_last_meth();

      Type = n.f0.accept(this, argu);
      id = n.f1.accept(this, argu);

      
      if(Type != "int" && Type != "boolean" && Type != "int[]" && Type != "boolean[]"){
            idList.add(Type);
      }

      /*if(current.mother != null){
            ClassTable mother = visitor_sym.get(current.mother);
            if( mother.method_table.containsKey(id) ){

                  Tuple<String, MethodTable> tupe =  mother.method_table.get(id);
                  
            
            }
      }*/

      if (curr_meth.param_table != null){
            if( curr_meth.param_table.containsKey(id) ){
                
                  throw new Exception("Semactic error!");
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


    public String visit(NodeToken n, String argu) throws Exception { return n.toString(); }

    
}
