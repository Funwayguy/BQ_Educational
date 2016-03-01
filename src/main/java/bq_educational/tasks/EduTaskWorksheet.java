package bq_educational.tasks;

import net.minecraft.entity.player.EntityPlayer;
import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.quests.tasks.TaskBase;
import betterquesting.utils.JsonHelper;
import bq_educational.client.gui.tasks.GuiEduTaskWorksheet;
import bq_educational.core.BQ_Educational;
import bq_educational.tasks.worksheet.Worksheet;
import bq_educational.tasks.worksheet.WorksheetDatabase;
import com.google.gson.JsonObject;

public class EduTaskWorksheet extends TaskBase
{
	public int sheetID = -1;
	
	@Override
	public String getUnlocalisedName()
	{
		return BQ_Educational.MODID + ".task.worksheet";
	}
	
	@Override
	public void Update(EntityPlayer player)
	{
		if(player.ticksExisted%1200 == 0 && !isComplete(player.getUniqueID())) // Self update once a minute
		{
			Detect(player);
		}
	}
	
	@Override
	public void Detect(EntityPlayer player)
	{
		Worksheet sheet = WorksheetDatabase.instance.getStudentsWorksheet(player.getUniqueID(), sheetID);
		
		if(sheet == null)
		{
			return;
		}
		
		if(sheet.grade >= 0)
		{
			setCompletion(player.getUniqueID(), true);
		}
	}
	
	@Override
	public void writeToJson(JsonObject json)
	{
		super.writeToJson(json);
		
		json.addProperty("sheetID", sheetID);
	}
	
	@Override
	public void readFromJson(JsonObject json)
	{
		super.readFromJson(json);
		
		sheetID = JsonHelper.GetNumber(json, "sheetID", -1).intValue();
	}
	
	@Override
	public GuiEmbedded getGui(GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		return new GuiEduTaskWorksheet(this, screen, posX, posY, sizeX, sizeY);
	}
	
}
