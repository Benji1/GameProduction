package netserver.universe;

public class CBNameGenerator {
	static String ABC = "QWERTZUIOPASDFGHJKLYXCVBNM";
	static int ID = 0;
	
	public static String getName(){
		String name = Character.toString(ABC.charAt((int)(Math.random()*26)))+"-";
		name+= (int)(Math.random()*1000);
		return name;
	}
	
	public static int getID(){
		System.out.println(ID);
		return ID++;
	}
}
