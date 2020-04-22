import visitor.*;
import syntaxtree.*;
import java.util.*;


//Offset Table will create the offset of a semantically correct Minijava program.
public class OffsetTable {

    public SymbolTable visitor_sym;

    //The constructor gets the SymbolTable object. 
    public OffsetTable(SymbolTable st){
        this.visitor_sym = st;   
    }

    //OutputCreator function creates our output.
    public void OutputCreator(){

        //For every class in the classId table
        for (String id : visitor_sym.classId_table.keySet()) {
            
            //We get its Class Table.
            ClassTable temp = visitor_sym.classId_table.get(id);

            //Skipping the Main class since it doesn't have any fields or methods.
            if (temp.methodId_table!=null){
                if(temp.methodId_table.containsKey("main")){
                    continue;
                }
            }

            System.out.println("-------Class "+id+"-------" );

            //Getting the field offsets.
            System.out.println("--Variables--" );
            if(temp.field_table!=null){


                int counter = 0; //Counter of which field we're on.
                int ot_sum = 0; //Ot_sum is the sum of all field offsets.
                String last_type = null; //The last type of the fields.

                int last_count = 0; //Used in case we're in a hierarchy.

                for (String f_id : temp.field_table.keySet()) {
                    
                    //Getting the type of the field.
                    String Type = temp.field_table.get(f_id);
                    last_type = Type;
                    
                    //If it's the first field print 0, except if we're in a hierarchy. If so, get the offset sum of the previous mother class.
                    if (counter == 0){
                        if(temp.mother == null){
                            System.out.println(id+"."+f_id+" : 0" );
                        }else{
                            last_count = visitor_sym.find_field_table(id);
                            System.out.println(id+"."+f_id+" : "+last_count );
                        }
                    }else{ //If it's not the first item we will go to every previous field and create the sum.
                        
                        int w_counter = counter;

                        int sum = 0 ;
                        if (last_count != 0){
                            sum = last_count;
                        }else{
                            sum = 0;
                        }
                        

                        w_counter--;
                        while(w_counter >= 0 ){

                            //Getting the field by index, then its type.
                            String field = temp.getKey(w_counter);
                            String f_type = temp.field_table.get(field);

                            //Adding up the necessary offsets.
                            if(f_type == "int" ){
                                sum = sum + 4;
                            }
                            if(f_type == "boolean" ){
                                sum = sum + 1;
                            }
                            if(f_type == "boolean[]" || f_type == "int[]" || visitor_sym.classId_table.containsKey(f_type) ){
                                sum = sum + 8;
                            }

                            w_counter--;

                        }

                        System.out.println(id+"."+f_id+" : "+sum );

                        ot_sum = sum;

                    }
                    
                    counter++;
                }

                //Adding up the offset of the last type. It may be used if there is a child class that inherits from our current class.
                if(last_type == "int" ){
                    ot_sum = ot_sum + 4;
                }
                if(last_type == "boolean" ){
                    ot_sum = ot_sum + 1;
                }
                if(last_type == "boolean[]" || last_type == "int[]" || visitor_sym.classId_table.containsKey(last_type) ){
                    ot_sum = ot_sum + 8;
                }

                //Storing the field offset sum in the Class Table.
                temp.ot_sum = ot_sum;
                
            }

            //Moving on to the method offsets.
            System.out.println("--Methods--" );
            if(temp.methodId_table!=null){
                
                int counter = 0; //Counter of which methodId we're on.
                int mt_sum = 0; //Mt_sum is the sum of all metthod offsets.

                int last_count = 0; //Used in case we're in a hierarchy.

                for (String m_id : temp.methodId_table.keySet()) {

                    //If we're in a hierarchy we need to skip overriden functions.
                    String new_mother = visitor_sym.mother_search(id,m_id);
                    if (new_mother != null){
                        continue;
                    }

                    //If it's the first method print 0, except if we're in a hierarchy. If so, get the offset sum of the previous mother class.
                    if (counter == 0){
                        
                        if(temp.mother == null){
                            System.out.println(id+"."+m_id+" : 0" );
                        }else{
                            int full_sum = 0;
                            last_count = visitor_sym.find_methodId_table(id,full_sum);
                            System.out.println(id+"."+m_id+" : "+last_count );
                        }
                    }else{ //If it's not the first item we will go to every previous field and create the sum.
                        int w_counter = counter;
                        w_counter--;

                        int sum;
                        if (last_count != 0){
                            sum = last_count;
                        }else{
                            sum = 0;
                        }
                        
                        //We add 8 to the sum for every function.
                        while(w_counter >= 0 ){

                            sum = sum + 8;

                            w_counter--;

                        }

                        System.out.println(id+"."+m_id+" : "+sum );

                        mt_sum = sum;

                    }
                    counter++;
                }

                //Adding up the offset of the last method. It may be used if there is a child class that inherits from our current class.
                mt_sum = mt_sum + 8;

                //Storing the method offset sum in the Class Table.
                temp.mt_sum = mt_sum;

            }

            System.out.print("\n");
       }

    }

    
}