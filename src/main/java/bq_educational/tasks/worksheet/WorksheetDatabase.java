package bq_educational.tasks.worksheet;

import java.util.HashMap;
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
		json.add("database", db);
	}
	
	public void readFromJson(JsonObject json)
	{
		worksheets = new HashMap<Integer,Worksheet>();
		for(JsonElement je : JsonHelper.GetArray(json, "database"))
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
	}
}
