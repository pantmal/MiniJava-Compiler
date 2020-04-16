class Factorial{
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}



class Fac {
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



}


class Fac2 extends Fac{
    int y;
    boolean[] bool_arr;
    public int ComputeFac(int num, int g){
        int num_loc ;
	num_loc = 1;
	return num_loc ;
    }
}








