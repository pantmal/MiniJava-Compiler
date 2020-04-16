import syntaxtree.*;
import visitor.*;
import java.io.*;

public class Main {
    public static void main (String [] args) throws Exception {
    if(args.length != 1){
        System.err.println("Usage: java Main <inputFile>");
        System.exit(1);
    }
    FileInputStream fis = null;
    try{
        fis = new FileInputStream(args[0]);
        MiniJavaParser parser = new MiniJavaParser(fis);
        System.err.println("Program parsed successfully.");
	Goal root = parser.Goal();        
	
    FirstVisitor eval = new FirstVisitor();
    //try{
        root.accept(eval, null);
   // }catch(Exception ouch){
   //     System.out.println("We had an error! ");    
   // }
    
    //eval.visitor_sym.print_all();

    for (int counter = 0; counter < eval.idList.size(); counter++) { 		      
        String id_check = eval.idList.get(counter);
        if( !(eval.visitor_sym.classId_table.containsKey(id_check)) ){

            throw new Exception("Semantic Error!");
      
      }
    } 

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
    }

    //ClassTable current = eval.visitor_sym.get_last();

    SecondVisitor eval2 = new SecondVisitor(eval.visitor_sym);
    root.accept(eval2, null);
    

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
