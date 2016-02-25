package bq_educational.tasks;

import java.util.HashMap;
import java.util.UUID;
import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.quests.tasks.TaskBase;
import bq_educational.tasks.worksheet.Worksheet;

public class EduTaskBase extends TaskBase
{
	public HashMap<UUID, Worksheet> userSubmissions = new HashMap<UUID, Worksheet>();
	
	@Override
	public String getUnlocalisedName()
	{
		return null;
	}

	@Override
	public GuiEmbedded getGui(GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		return null;
	}
	
}
