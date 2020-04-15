import java.util.LinkedHashMap;
import java.util.Iterator;

/*
class Person extends Human{
  String name;
}
class Human extends Voter{
  String language;
}
class Voter extends Person{
  String residence;
}*/

class Tuple<X, Y> { 
  public final X x; 
  public final Y y; 
  public Tuple(X x, Y y) { 
    this.x = x; 
    this.y = y; 
  } 
} 

class MethodTable{
  public LinkedHashMap<String, String> param_table ;
  public LinkedHashMap<String, String> local_table ;

  //public MethodTable(){
  //  param_table = new LinkedHashMap<String, String>();
  //  local_table = new LinkedHashMap<String, String>();
  //}

   String getKey(int index) {

    Iterator<String> itr = param_table.keySet().iterator();
    for (int i = 0; i < index; i++) {
        itr.next();
    }
    return itr.next();
}

  public void p_insert(String id, String type){
    if (param_table == null){
      param_table = new LinkedHashMap<String, String>();
    }

    param_table.put(id, type);
  }

  public void l_insert(String id, String type){
    
    if (local_table == null){
      local_table = new LinkedHashMap<String, String>();
    }


    local_table.put(id, type);
  }

  public void getp(){
    if (param_table != null){
      for (String i : param_table.keySet()) {
        System.out.println("param_name: " + i + " type : " + param_table.get(i));
      }  
    }
    
  }

  public void getl(){
    for (String i : local_table.keySet()) {
        System.out.println("local_var: " + i + " type: " + local_table.get(i));
      }
  }


}

class ClassTable{

  public String mother; 
  public LinkedHashMap<String, String> field_table ;
  public LinkedHashMap<String, Tuple<String,MethodTable>> method_table ;
  
  //public ClassTable(){
  //  field_table = new LinkedHashMap<String, String>();
  // method_table = new LinkedHashMap<String, Tuple<String,MethodTable>>();
  //}

  //init functions here for each table

  public void f_insert(String id, String type){
    if (field_table == null){
      field_table = new LinkedHashMap<String, String>();
    }
    field_table.put(id, type);
  }

  public void meth_insert(String id, String ret_type  ){
    
    if (method_table == null){
      method_table = new LinkedHashMap<String, Tuple<String,MethodTable>>();
    }
    
    method_table.put(id, new Tuple(ret_type,new MethodTable() ) );
    
    //Tuple<String, MethodTable> bobs = method_table.get(id);

    //bobs.y.p_insert("p", "int");
    
    //bobs.y.l_insert("a", "int");



    //MethodTable temp = method_table.get(id);
    //temp.p_insert("p", "int");

    //temp.l_insert("a", "int");


  }

  public MethodTable get_last_meth(){

    String lKeyLast = null ;
    for(String key : method_table.keySet()){
      lKeyLast = key;
    }

    Tuple<String, MethodTable> temp = method_table.get(lKeyLast);

    return temp.y;
  }


  public void get(){
    for (String i : field_table.keySet()) {
        System.out.println("field_name: " + i + " type: " + field_table.get(i));
      }
  }

  public void get2(){
    for (String i : method_table.keySet()) {
        System.out.print("method_name: " + i + " return_type: ");
        Tuple<String, MethodTable> bobs =  method_table.get(i);
        
        System.out.println(bobs.x);

        bobs.y.getp();
        bobs.y.getl();
    }
  }


}

public class SymbolTable {


  LinkedHashMap<String, ClassTable> sym_table;

  public void add_class(String id){
    
    if (sym_table == null){
      sym_table = new LinkedHashMap<String, ClassTable>();
    }
    sym_table.put(id, new ClassTable());
  }

  public ClassTable get_last(){

    String lKeyLast = null ;
    for(String key : sym_table.keySet()){
      lKeyLast = key;
    }

    ClassTable temp = sym_table.get(lKeyLast);

    return temp;

  }

  public ClassTable get(String id){

    ClassTable temp = sym_table.get(id);
    return temp;
    //possible return types.

  }

  public void print_all(){
    for (String i : sym_table.keySet()) {
      System.out.println("local_var: " + i + " type: " + sym_table.get(i));
    }
  }


  //public static void main(String[] args) {

    // Create a HashMap object called people
    

    /*
    // Add keys and values (Name, Age)
    program.put("John", new ClassTable());
    program.put("Steve", new ClassTable());
    program.put("Angie", new ClassTable());
    
    ClassTable temp = program.get("John");
    temp.f_insert("x", "int");
    temp.f_insert("g", "bool");
    temp.get();

    if (temp.method_table == null){
      System.out.println("cool ");
    }

    temp.meth_insert("start", "int");
    temp.get2();




    ClassTable temp2 = program.get("Steve");
    temp2.f_insert("f", "C");
    temp2.meth_insert("finish", "bool");
    temp2.get();
    temp2.get2();

    */
    
  //}
}
