package bq_educational.tasks.worksheet;

import java.util.HashMap;
import java.util.UUID;
import org.apache.logging.log4j.Level;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import betterquesting.utils.JsonHelper;
import betterquesting.utils.NBTConverter;
import bq_educational.core.BQ_Educational;
import bq_educational.network.PacketEdu.EduPacketType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WorksheetDatabase
{
	public static final WorksheetDatabase instance = new WorksheetDatabase();
	
	public HashMap<Integer,Worksheet> worksheets = new HashMap<Integer,Worksheet>();
	public HashMap<UUID,StudentWork> studentWork = new HashMap<UUID,StudentWork>();
	
	private WorksheetDatabase(){}
	
	/**
	 * Registers a new worksheet and returns its ID or -1 in the event of failure<br>
	 * <b>WARNING:</b> This will reset the worksheet to template form
	 */
	public int registerWorksheet(Worksheet sheet)
	{
		if(sheet == null || worksheets.containsValue(sheet))
		{
			return -1;
		}
		
		int id = getUniqueID();
		sheet.makeTemplate();
		worksheets.put(id, sheet);
		return id;
	}
	
	public int getUniqueID()
	{
		int i = 0;
		
		while(worksheets.containsKey(i))
		{
			i++;
		}
		
		return i;
	}
	
	public Worksheet getByID(int id)
	{
		return worksheets.get(id);
	}
	
	/**
	 * Gets a worksheet based on the order it is stored
	 */
	public Worksheet getByOrder(int index)
	{
		if(index < 0 || index >= worksheets.size())
		{
			return null;
		}
		
		return worksheets.values().toArray(new Worksheet[0])[index];
	}
	
	/**
	 * Gets or creates a container for a student's collection of work
	 */
	public StudentWork getStudentsWork(UUID uuid)
	{
		if(uuid == null)
		{
			return new StudentWork(); // Dummy instance to prevent accidental NPE crashes
		}
		
		if(studentWork.containsKey(uuid))
		{
			return studentWork.get(uuid);
		} else
		{
			StudentWork work = new StudentWork();
			work.uuid = uuid;
			studentWork.put(uuid, work);
			return work;
		}
	}
	
	/**
	 * Shortcut method for obtaining a specific student's worksheet.
	 * Makes a blank copy if no existing one is found
	 */
	public Worksheet getStudentsWorksheet(UUID uuid, int sheetID)
	{
		StudentWork work = getStudentsWork(uuid);
		
		if(work == null)
		{
			BQ_Educational.logger.log(Level.ERROR, "Unable to obtain student's worksheet! This should not happen!", new NullPointerException());
			return null;
		} else
		{
			Worksheet sheet = work.sheets.get(sheetID);
			
			if(sheet != null)
			{
				return sheet;
			} else
			{
				Worksheet template = getByID(sheetID);
				
				if(template == null)
				{
					return null; // If you got here you likely did something wrong!
				}
				
				sheet = template.cleanCopy();
				work.sheets.put(sheetID, sheet);
				return sheet;
			}
		}
	}
	
	public void UpdateClients()
	{
		NBTTagCompound tags = new NBTTagCompound();
		JsonObject json = new JsonObject();
		writeToJson(json);
		tags.setTag("Database", NBTConverter.JSONtoNBT_Object(json, new NBTTagCompound()));
		BQ_Educational.instance.network.sendToAll(EduPacketType.EDU_WS_DB.makePacket(tags));
	}
	
	public void UpdatePlayer(EntityPlayerMP player)
	{
		NBTTagCompound tags = new NBTTagCompound();
		JsonObject json = new JsonObject();
		writeToJson(json);
		tags.setTag("Database", NBTConverter.JSONtoNBT_Object(json, new NBTTagCompound()));
		BQ_Educational.instance.network.sendTo(EduPacketType.EDU_WS_DB.makePacket(tags), player);
	}
	
	public void writeToJson(JsonObject json)
	{
		JsonArray db = new JsonArray();
		for(Worksheet sheet : worksheets.values())
		{
			if(sheet == null || sheet.ID < 0)
			{
				continue;
			}
			
			JsonObject js = new JsonObject();
			sheet.writeToJson(js);
			db.add(db);
		}
		json.add("originals", db);
		
		JsonArray sw = new JsonArray();
		for(StudentWork work : studentWork.values())
		{
			if(work == null || work.uuid == null)
			{
				continue;
			}
			
			JsonObject jw = new JsonObject();
			work.writeToJson(jw);
			sw.add(jw);
		}
		json.add("studentWork", sw);
	}
	
	public void readFromJson(JsonObject json)
	{
		worksheets = new HashMap<Integer,Worksheet>();
		for(JsonElement je : JsonHelper.GetArray(json, "originals"))
		{
			if(je == null || !je.isJsonObject())
			{
				continue;
			}
			
			JsonObject js = je.getAsJsonObject();
			Worksheet sheet = new Worksheet();
			sheet.readFromJson(js);
			
			if(sheet.ID < 0) // Invalid ID
			{
				continue;
			}
			
			worksheets.put(sheet.ID, sheet);
		}
		
		studentWork = new HashMap<UUID,StudentWork>();
		for(JsonElement je : JsonHelper.GetArray(json, "studentWork"))
		{
			if(je == null || !je.isJsonObject())
			{
				continue;
			}
			
			JsonObject jw = je.getAsJsonObject();
			StudentWork work = new StudentWork();
			work.readFromJson(jw);
			
			if(work.uuid == null)
			{
				continue;
			}
			
			studentWork.put(work.uuid, work);
		}
	}
	
	public static class StudentWork
	{
		public UUID uuid = null;
		public HashMap<Integer,Worksheet> sheets = new HashMap<Integer,Worksheet>();
		
		public void writeToJson(JsonObject json)
		{
			json.addProperty("uuid", uuid.toString());
			
			JsonArray db = new JsonArray();
			for(Worksheet sheet : sheets.values())
			{
				if(sheet == null || sheet.ID < 0)
				{
					continue;
				}
				
				JsonObject js = new JsonObject();
				sheet.writeToJson(js);
				db.add(db);
			}
			json.add("sheets", db);
		}
		
		public void readFromJson(JsonObject json)
		{
			uuid = UUID.fromString(JsonHelper.GetString(json, "uuid", ""));
			
			sheets.clear();
			for(JsonElement je : JsonHelper.GetArray(json, "sheets"))
			{
				if(je == null || !je.isJsonObject())
				{
					continue;
				}
				
				JsonObject js = je.getAsJsonObject();
				Worksheet sheet = new Worksheet();
				sheet.readFromJson(js);
				
				if(sheet.ID < 0) // Invalid ID
				{
					continue;
				}
				
				sheets.put(sheet.ID, sheet);
			}
		}
	}
}
