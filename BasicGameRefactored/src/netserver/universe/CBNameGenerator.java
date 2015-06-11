package netserver.universe;

public class CBNameGenerator {
	static String ABC = "QWERTZUIOPASDFGHJKLYXCVBNM";
	
	public static String getName(){
		String name = Character.toString(ABC.charAt((int)(Math.random()*26)))+"-";
		name+= (int)(Math.random()*1000);
		return name;
	}
}
