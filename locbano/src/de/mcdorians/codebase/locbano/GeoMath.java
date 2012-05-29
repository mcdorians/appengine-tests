package de.mcdorians.codebase.locbano;

public class GeoMath {
	
	final static double PK=  57.2957795; //180/PI
	
	/**
	 * Taken from http://stackoverflow.com/a/6787208
	 * 
	 * @param latUser
	 * @param lngUser
	 * @param latOther
	 * @param lngOther
	 * @return
	 */
    public static double calcDistance(double latUser, double lngUser, double latOther, double lngOther) {
    	
        double a1 = latUser / PK;
        double a2 = lngUser / PK;
        double b1 = latOther / PK;
        double b2 = lngOther / PK;
    	
        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        
        return 6366000*tt; 
    }

}
