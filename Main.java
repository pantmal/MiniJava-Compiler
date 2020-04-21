import syntaxtree.*;
import visitor.*;
import java.io.*;

public class Main {
    public static void main (String [] args) throws Exception {


        if(args.length < 1){
            System.err.println("Usage: java Main <inputFile1> <inputFile2> ... <inputFileN> ");
            System.exit(1);
        }

        FileInputStream fis = null;
        try{


            for (String s: args) {
                System.out.println("Type check on program: "+s);
                System.out.print("\n");    


                fis = new FileInputStream(s);
                MiniJavaParser parser = new MiniJavaParser(fis);
                System.err.println("Program parsed successfully.");
                System.out.print("\n");    
                Goal root = parser.Goal();        
            
                FirstVisitor eval = new FirstVisitor();

                boolean decl_error = false;

                try{
                    root.accept(eval, null);
                }catch(Exception ouch){

                    System.out.println("We encountered at least one declaration error! ");  
                    System.out.print("\n");      
                    decl_error = true;

                }

                if(decl_error == true){
                    continue;
                }
            
            //eval.visitor_sym.print_all();

                for (int counter = 0; counter < eval.idList.size(); counter++) { 		      
                    String id_check = eval.idList.get(counter);
                    if( !(eval.visitor_sym.classId_table.containsKey(id_check)) ){

                        System.out.println("We encountered at least one declaration error! ");    
                        System.out.print("\n");    
                        decl_error = true;
                
                    }
                }
                
                if(decl_error == true){
                    continue;
                }

            /*
            for (String i : eval.visitor_sym.classId_table.keySet()) {
                ClassTable current = eval.visitor_sym.classId_table.get(i);
                
                System.out.print(i);
                if (current.mother!=null){
                    System.out.println(" extends "+ current.mother);    
                }else{
                    System.out.print(" \n");    
                }
                current.get();
                current.get2();
            }*/




            //ClassTable current = eval.visitor_sym.get_last();

                SecondVisitor eval2 = new SecondVisitor(eval.visitor_sym);
                
                boolean type_error = false;

                try{
                    root.accept(eval2, null);
                }catch(Exception ouch){
                    System.out.println("We encountered at least one type error! ");   
                    System.out.print("\n");     
                    type_error = true;//System.out.println("We had an error! ");    
                }

                if (type_error == true){
                    continue;
                }
        

                System.out.println("Our program is free of type errors, moving on to the offset table: ");    
                System.out.print("\n");    


                OffsetTable ot = new OffsetTable(eval.visitor_sym);
                ot.OutputCreator();
                System.out.print("\n");    

        }

        }
        catch(ParseException ex){
            System.out.println(ex.getMessage());
        }
        catch(FileNotFoundException ex){
            System.err.println(ex.getMessage());
        }
        finally{
            try{
            if(fis != null) fis.close();
            }
            catch(IOException ex){
            System.err.println(ex.getMessage());
            }
        }


    }



}
