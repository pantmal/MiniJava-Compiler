class Factorial{
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}


class Fac {
    int x;
    public int ComputeFac(int num){
        int num_aux ;
        if (num < 1)
            num_aux = 1 ;
        else
            num_aux = num * (this.ComputeFac(num-1)) ;
        return num_aux ;
    }
}


class Fac2 extends Fac {
    int y;
    public bool ComputeFac(int num, int j){
	Fac f;
	int g;
        int num_aux ;
        if (num < 1){
            num_aux = 1 ;
            g = this;
	}else{
            num_aux = num * (this.ComputeFac(num-1)) ;
	}
        return num_aux ;
    }
}
