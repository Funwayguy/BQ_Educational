package bq_educational.client.gui.tasks;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiButtonQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.client.themes.ThemeRegistry;
import bq_educational.tasks.EduTaskWorksheet;
import bq_educational.tasks.worksheet.Worksheet;
import bq_educational.tasks.worksheet.WorksheetDatabase;

public class GuiEduTaskWorksheet extends GuiEmbedded
{
	EduTaskWorksheet task;
	Worksheet sheet;
	GuiButtonQuesting btn;
	
	public GuiEduTaskWorksheet(EduTaskWorksheet task, GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		super(screen, posX, posY, sizeX, sizeY);
		this.task = task;
		this.sheet = WorksheetDatabase.instance.getStudentsWorksheet(screen.mc.thePlayer.getUniqueID(), task.sheetID);
		this.btn = new GuiButtonQuesting(0, posX + sizeX/2 - 50, posY + sizeY/2, 100, 20, I18n.format("bq_educational.btn.open"));
		this.btn.enabled = this.sheet != null;
	}

	@Override
	public void drawGui(int mx, int my, float partialTick)
	{
		
		GL11.glPushMatrix();
		GL11.glTranslatef(posX + sizeX/2, posY + sizeY/2, 0F);
		GL11.glScalef(2F, 2F, 1F);
		
		String txt = EnumChatFormatting.BOLD + (sheet != null? sheet.title : "[ ? ]");
		txt = screen.mc.fontRenderer.trimStringToWidth(txt, (sizeX + 16)/2);
		int tw = screen.mc.fontRenderer.getStringWidth(txt);
		screen.mc.fontRenderer.drawString(txt, -tw/2, -12, ThemeRegistry.curTheme().textColor().getRGB(), false);
		
		GL11.glPopMatrix();
		
		btn.drawButton(screen.mc, mx, my);
	}
	
	@Override
	public void mouseClick(int mx, int my, int click)
	{
		if(btn.mousePressed(screen.mc, mx, my))
		{
			// Open worksheet questions...
		}
	}
}
