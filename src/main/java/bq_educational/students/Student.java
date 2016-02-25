package bq_educational.students;

import java.util.ArrayList;
import java.util.UUID;
import com.google.gson.JsonObject;

public class Student // Still a WIP
{
	public String name = "John Smith";
	public String studentID = "NONE";
	public UUID playerID;
	ArrayList<String> groups = new ArrayList<String>();
	ArrayList<String> noteBook = new ArrayList<String>();
	
	public void writeToJson(JsonObject json)
	{
		
	}
	
	public void readFromJson(JsonObject json)
	{
		
	}
}
