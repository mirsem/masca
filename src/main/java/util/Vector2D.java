package util;

public class Vector2D {
	
    public static final long serialVersionUID = 1L;

    public double x;
    public double y;

    public Vector2D(){
        x=y=0;
    }

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D v){
        this.x+=v.x;
        this.y+=v.y;
    }

    public void div(double d){
        this.x/=d;
        this.y/=d;
    }

    public void mul(double d){
        this.x*=d;
        this.y*=d;
    }


    public double mod(){
        return Math.sqrt(x*x+y*y);
    }

    public void normalize(){
        double m = mod();
        
        if(m>0){
            x/=m;
            y/=m;
        }
    }

    public double ang(){
        return Math.atan2(y, x);
    }
    
    public double ang2(){
    	double a = Math.atan2(y, x);
    	if(a>0) return a;
    	else return 2*Math.PI-a;
    }

    public double dist(Vector2D d){
        return Math.sqrt((x-d.x)*(x-d.x)+(y-d.y)*(y-d.y));
    }

    @Override
    public String toString(){
        return "("+x+","+y+")";
    }

}

