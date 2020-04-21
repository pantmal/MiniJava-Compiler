import visitor.*;
import syntaxtree.*;
import java.util.*;



public class OffsetTable {

    public SymbolTable visitor_sym;

    public OffsetTable(SymbolTable st){
        this.visitor_sym = st;   
    }


    public void OutputCreator(){

        
        for (String id : visitor_sym.classId_table.keySet()) {
            
            ClassTable temp = visitor_sym.classId_table.get(id);
            if (temp.methodId_table!=null){
                if(temp.methodId_table.containsKey("main")){
                    continue;
                }
            }

            System.out.println("-------Class "+id+"-------" );

            System.out.println("--Variables--" );
            if(temp.field_table!=null){
                int counter = 0;
                int ot_sum = 0;
                String last_type = null;

                int last_count = 0;

                for (String f_id : temp.field_table.keySet()) {
                    
                    String Type = temp.field_table.get(f_id);
                    
                    last_type = Type;
                    
                    if (counter == 0){
                        if(temp.mother == null){
                            System.out.println(id+"."+f_id+" : 0" );
                        }else{
                            int full_sum = 0;
                            last_count = visitor_sym.find_field_table(id);
                            System.out.println(id+"."+f_id+" : "+last_count );
                        }
                    }else{
                        int w_counter = counter;

                        int sum = 0 ;
                        if (last_count != 0){
                            sum = last_count;
                        }else{
                            sum = 0;
                        }
                        

                        w_counter--;
                        while(w_counter >= 0 ){
                            String field = temp.getKey(w_counter);
                            String f_type = temp.field_table.get(field);
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

                if(last_type == "int" ){
                    ot_sum = ot_sum + 4;
                }
                if(last_type == "boolean" ){
                    ot_sum = ot_sum + 1;
                }
                if(last_type == "boolean[]" || last_type == "int[]" || visitor_sym.classId_table.containsKey(last_type) ){
                    ot_sum = ot_sum + 8;
                }

                temp.ot_sum = ot_sum;
                
            }

            System.out.println("--Methods--" );

            if(temp.methodId_table!=null){
                int counter = 0;
                int mt_sum = 0;

                int last_count = 0;

                for (String m_id : temp.methodId_table.keySet()) {

                    String new_mother = visitor_sym.mother_search(id,m_id);
                    if (new_mother != null){
                        continue;
                    }

                    
                    if (counter == 0){
                        
                        if(temp.mother == null){
                            System.out.println(id+"."+m_id+" : 0" );
                        }else{
                            int full_sum = 0;
                            last_count = visitor_sym.find_methodId_table(id,full_sum);
                            System.out.println(id+"."+m_id+" : "+last_count );
                        }
                    }else{
                        int w_counter = counter;
                        w_counter--;

                        int sum;
                        if (last_count != 0){
                            sum = last_count;
                        }else{
                            sum = 0;
                        }
        
                        while(w_counter >= 0 ){

                            sum = sum + 8;

                            w_counter--;

                        }

                        System.out.println(id+"."+m_id+" : "+sum );

                        mt_sum = sum;

                    }
                    counter++;
                }

                mt_sum = mt_sum + 8;
                temp.mt_sum = mt_sum;

            }

            System.out.print("\n");

       }



    }







    
}