package bq_educational.students;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EduDatabase
{
	public static HashMap<UUID, Student> students = new HashMap<UUID, Student>();
	public static HashMap<String, EduGroup> groups = new HashMap<String, EduGroup>();
	
	public boolean useScaleE = false;
	private static String[] gradeScaleF = new String[]{"F-","F","F+","D-","D","D+","C-","C","C+","B-","B","B+","A-","A","A+"};
	private static String[] gradeScaleE = new String[]{"E-","E","E+","D-","D","D+","C-","C","C+","B-","B","B+","A-","A","A+"};
	
	public String getGrade(int raw)
	{
		if(raw < 0)
		{
			return "N/A"; // Ungraded
		} else if(!useScaleE)
		{
			return gradeScaleF[MathHelper.clamp_int(raw, 0, gradeScaleF.length)];
		} else
		{
			return gradeScaleE[MathHelper.clamp_int(raw, 0, gradeScaleE.length)];
		}
	}
	
	public static Student getStudent(EntityPlayer player)
	{
		return getOrCreateStudent(player, true);
	}
	
	public static Student getOrCreateStudent(EntityPlayer player, boolean canCreate)
	{
		Student s = getStudent(player.getUniqueID());
		
		if(s != null || canCreate)
		{
			return s;
		}
		
		s = new Student();
		s.playerID = player.getUniqueID();
		s.name = player.getCommandSenderName(); // Defaults to user name. Can be changed to real name for external referencing
		students.put(s.playerID, s);
		
		return s;
	}
	
	public static Student getStudent(UUID uuid)
	{
		return students.get(uuid);
	}
	
	public static Student getStudent(String sid)
	{
		for(Student s : students.values())
		{
			if(s.studentID.equalsIgnoreCase(sid))
			{
				return s;
			}
		}
		
		return null;
	}
	
	public static void writeToJson(JsonObject json)
	{
		JsonArray sAry = new JsonArray();
		for(Student s : students.values())
		{
			JsonObject js = new JsonObject();
			s.writeToJson(js);
			sAry.add(js);
		}
		json.add("students", sAry);
	}
}
