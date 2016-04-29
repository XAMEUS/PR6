import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utils {
	
	public static String uniqueId1() {
		String uniqueID = UUID.randomUUID().toString();
		System.out.println(uniqueID);
		String id = "j8r";
		byte[] b = new byte[6];
		System.out.println(id.getBytes().length);
		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		b[0] = (byte) cal.get(Calendar.MONTH);
		b[1] = (byte) cal.get(Calendar.DAY_OF_MONTH);
		b[2] = (byte) cal.get(Calendar.HOUR_OF_DAY);
		b[3] = (byte) cal.get(Calendar.MINUTE);
		b[4] = (byte) (cal.get(Calendar.MILLISECOND) % 64);
		b[5] = (byte) (Math.random() * 64);
		//System.out.print("bytes : ");
		//for (int i = 0; i < b.length; i++) System.out.print(String.format("%02d", b[i]) + " ");
		//System.out.println();
		//System.out.println(new String(Base64.getEncoder().encode(b)));
		return new String(Base64.getEncoder().encode(b));
	}
	
}
