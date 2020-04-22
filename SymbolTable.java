import java.util.LinkedHashMap;
import java.util.Iterator;

//The SymbolTable.java file contains all the necessary structures for the implementation of our Symbol Table.


//Tuple class is used to store two variables together, it is needed for the methodId table which will be explained later on.
class Tuple<X, Y> { 
  public final X x; 
  public final Y y; 
  public Tuple(X x, Y y) { 
    this.x = x; 
    this.y = y; 
  } 
} 

//The Method Table is used to store the parameters and local variables of a method (if any)
class MethodTable{

  public LinkedHashMap<String, String> param_table ;
  public LinkedHashMap<String, String> local_table ;

  //GetKey function will return the key (of the parameter table) that corresponds to the "index" argument.
  public String getKey(int index) {

    Iterator<String> itr = param_table.keySet().iterator();
    for (int i = 0; i < index; i++) {
        itr.next();
    }
    return itr.next();
  }

  //p_insert adds a variable and its type to the parameter table.
  public void p_insert(String id, String type){
    if (param_table == null){
      param_table = new LinkedHashMap<String, String>();
    }

    param_table.put(id, type);
  }

  //l_insert adds a variable and its type to the local variables table.
  public void l_insert(String id, String type){
    
    if (local_table == null){
      local_table = new LinkedHashMap<String, String>();
    }


    local_table.put(id, type);
  }


}

//The ClassTable will contain all the fields and method names of a class, as well as some additional information.
class ClassTable{

  public String mother; //Mother is the name of the class whom this class extends from. If the class is not a child, this string is null.
  public int ot_sum; //ot_sum contains the sum of the offsets from the fields. Its usage will be explained later on. 
  public int mt_sum; //Same thing as ot_sum but for the methods.
  public LinkedHashMap<String, String> field_table ;
  public LinkedHashMap<String, Tuple<String,MethodTable>> methodId_table ;
  
  //GetKey function will return the key (of the field table) that corresponds to the "index" argument.
  public String getKey(int index) {

    Iterator<String> itr = field_table.keySet().iterator();
    for (int i = 0; i < index; i++) {
        itr.next();
    }
    return itr.next();
  }

  //Same thing as the previous function, but for the methodId table.
  public String getKey_m(int index) {

    Iterator<String> itr = methodId_table.keySet().iterator();
    for (int i = 0; i < index; i++) {
        itr.next();
    }
    return itr.next();
  }

  //Recurse_lookup will search throughout the class hierarchy to see if the "id" argument exists.
  public String recurse_lookup(String id, LinkedHashMap<String, ClassTable> classId_table ){

    String Type = null;
    if( this.field_table != null  ){
      if( this.field_table.containsKey(id) ) {
        Type = this.field_table.get(id);
        return Type;
      }else{
        if (this.mother != null ){
          ClassTable mother_t = classId_table.get(this.mother);
          Type = mother_t.recurse_lookup(id,classId_table);
          if (Type == null){
            return null;
          }else{
            return Type;
          }
        }
        return null;
      }
    }else{
      if (this.mother != null ){
        ClassTable mother_t = classId_table.get(this.mother);
        Type = mother_t.recurse_lookup(id,classId_table);
        if (Type == null){
          return null;   
        }else{
          return Type;
        }
      }
      return null;
    }

  }

  //f_insert adds a variable and its type to the field table.
  public void f_insert(String id, String type){
    if (field_table == null){
      field_table = new LinkedHashMap<String, String>();
    }
    field_table.put(id, type);
  }

  //meth_insert adds a variable and its type to the field table. Note that the value of a methodId corresponds to a tuple.
  //The tuple consists of a string which is the method type, and a MethodTable object, where parameters and local variables will be stored. 
  public void meth_insert(String id, String ret_type  ){
    
    if (methodId_table == null){
      methodId_table = new LinkedHashMap<String, Tuple<String,MethodTable>>();
    }
    
    methodId_table.put(id, new Tuple(ret_type,new MethodTable() ) );
    
  }

  //Getting the last MethodTable of the methodId table.
  public MethodTable get_last_meth(){

    String lKeyLast = null ;
    for(String key : methodId_table.keySet()){
      lKeyLast = key;
    }

    Tuple<String, MethodTable> temp = methodId_table.get(lKeyLast);

    return temp.y;
  }


}

//The Symbol Table consists of a HashMap. Each key is the class id and its key the corresponding Class Table.
public class SymbolTable {


  LinkedHashMap<String, ClassTable> classId_table;

  //Is_child will search throughout the class hierarchy to see if the "child" argument has f_mother as a mother class.
  public boolean is_child(String f_mother, String child){

    ClassTable current = this.get(child);

    if (current.mother == null){
      return false;
    }else{
      if (current.mother == f_mother){
        return true;
      }else{
        return is_child(f_mother, current.mother);
      }
    }

  }

  //Find_field_table will search throughout the class hierarchy to get the field table offset sum of the first mother class that has a field table.
  public int find_field_table(String id){

    ClassTable current = this.get(id);
    ClassTable mom_table = this.get(current.mother);

    if (current.mother == null && current.field_table == null ){
      return 0;
    }

    if (mom_table.field_table == null){
      return find_field_table(current.mother); 
    }else{
      return mom_table.ot_sum;
    }
  
  }

  //Find_methodId_table will search throughout the class hierarchy to get the methodId table offset sum of the first mother class that has a methodId table.
  //In the case of methods, the sum of all previous sums is returned.
  public int find_methodId_table(String id, int full_sum){

    ClassTable current = this.get(id);
    ClassTable mom_table = this.get(current.mother);

    

    if (current.mother == null ){
      return full_sum;
    }

    if (mom_table.methodId_table != null){
      full_sum = full_sum + mom_table.mt_sum;
      return find_methodId_table(current.mother,full_sum); 
    }else{
      return find_methodId_table(current.mother,full_sum); 
    }
  
  }

  //Mother_search will search throughout the class hierarchy to get the first mother that has the "id" argument.
  public String mother_search(String mother, String id){

    ClassTable current = this.get(mother);

    if (current.mother == null){
      return null;
    }else{
      boolean not_up = false;
      ClassTable new_mother = this.get(current.mother);
      if (new_mother.methodId_table != null){
        if( new_mother.methodId_table.containsKey(id) ){
            return current.mother;
        }else{
            not_up = true;
        }
      }

      if( new_mother.methodId_table == null   ||  not_up == true  ){
        return mother_search(current.mother,id);
      }
    }

    return null;


  }

  //Adding a class to the classId table
  public void add_class(String id){
    
    if (classId_table == null){
      classId_table = new LinkedHashMap<String, ClassTable>();
    }
    classId_table.put(id, new ClassTable());
  }

  //Getting the last ClassTable of the classId table
  public ClassTable get_last(){

    String lKeyLast = null ;
    for(String key : classId_table.keySet()){
      lKeyLast = key;
    }

    ClassTable temp = classId_table.get(lKeyLast);

    return temp;

  }

  //Getting the last class id of the classId table
  public String get_last_key(){

    String lKeyLast = null ;
    for(String key : classId_table.keySet()){
      lKeyLast = key;
    }

    return lKeyLast;

  }

  //Getting the ClassTable of the "id" argument.
  public ClassTable get(String id){

    ClassTable temp = classId_table.get(id);
    return temp;
  }

  

}
