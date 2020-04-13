import visitor.GJDepthFirst;
import syntaxtree.*;

public class FirstVisitor extends GJDepthFirst<String, String>{
	
	/**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, String argu) {
      String _ret=null;
      String Type, id;		
	Type =      n.f0.accept(this, argu);
      id = n.f1.accept(this, argu);
	System.out.println(Type + " " + id);
      n.f2.accept(this, argu);
      return _ret;
   }

    public String visit(NodeToken n, String argu) { return n.toString(); }

    
}
