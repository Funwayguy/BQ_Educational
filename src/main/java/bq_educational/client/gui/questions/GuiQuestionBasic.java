package bq_educational.client.gui.questions;

import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.client.gui.misc.GuiScrollingText;
import bq_educational.tasks.worksheet.questions.QuestionBase;

public class GuiQuestionBasic extends GuiEmbedded
{
	QuestionBase question;
	ResourceLocation img = null;
	GuiScrollingText txt;
	
	float iScale = 1F;
	float isw = 256;
	float ish = 256;
	
	public GuiQuestionBasic(QuestionBase question, GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		super(screen, posX, posY, sizeX, sizeY);
		this.question = question;
		txt = new GuiScrollingText(screen, sizeY, sizeX, posX, posY, question.question);
	}
	
	public void setDiagram(ResourceLocation image, int imgWidth, int imgHeight)
	{
		img = image;
		
		if(image != null)
		{
			txt = new GuiScrollingText(screen, sizeY/2, sizeX, posX, posY, question.question);
			isw = MathHelper.clamp_int(imgWidth, 1, 256);
			ish = MathHelper.clamp_int(imgHeight, 1, 256);
			iScale = Math.min(sizeX/isw, (sizeY/2)/ish);
		} else
		{
			txt = new GuiScrollingText(screen, sizeY, sizeX, posX, posY, question.question);
		}
	}

	@Override
	public void drawGui(int mx, int my, float partialTick)
	{
		if(img != null)
		{
			float ipx = sizeX/2 - (isw/2*iScale);
			float ipy = sizeY/4 - (ish/2*iScale);
			GL11.glPushMatrix();
			GL11.glTranslatef(ipx, ipy, 0);
			GL11.glScalef(iScale, iScale, 1F);
			this.screen.drawTexturedModalRect(0, 0, 0, 0, (int)isw, (int)ish);
			GL11.glPopMatrix();
		}
		
		txt.drawScreen(mx, my, partialTick);
	}
}
