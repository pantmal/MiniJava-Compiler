class Factorial{
    public static void main(String[] a){
        System.out.println(1);
//new Fac().ComputeFac(10)
    }
}


class Fac {
    int x;
    int k;
    boolean y;
    int[] int_arr;
    Fac b;
    public int SomeFunc(int f,int g,int k){
	
    	return 1;
    }	

    public int NewFunc(int f,int g,int k){
	
    	return 1;
    }

    public int ComputeFac(int f){
	int other_num;
        int num_aux ;
	int[] local_arr;
	boolean checker;
	Fac c;      
        b = new Fac();
	num_aux = local_arr[0];
	other_num = new Fac().SomeFunc(num_aux,c.SomeFunc(1,2,3),other_num);
	local_arr[0] = num_aux;
        return num_aux ;
    }



}

/*class Fac {
    int x;
    boolean y;
    int[] int_arr;
    Fac b;
    public int ComputeFac(int num, int f){
        int num_aux ;
        if (num < 1)
            num_aux = 1 ;
        else
            num_aux = num * (this.ComputeFac(num-1)) ;
        return num_aux ;
    }



}*/


class Fac2 extends Fac{
    //int y;
    //boolean[] bool_arr;
    public int ComputeFac(int f){
	int l;
        l = k;
	return k;
    }
}

/*class Fac3 extends Fac2{

	public int SomeFunc(int g,int k, int j){
	x = k;
	
    	return 1;
	    }	
	
	
}*/



class A{
int d; 
 public int foo(int g){
	return 0;
}

}

class B extends A{
int d;

}

class C extends B{
int d; 
int g;

}

class D extends C{
int d;
public int foo(int k){
	return 0;
}

}






