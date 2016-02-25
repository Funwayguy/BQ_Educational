package bq_educational.tasks.worksheet.questions;

import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import bq_educational.client.gui.questions.GuiQuestionBasic;

public class QuestionTest extends QuestionBase
{
	@Override
	public GuiEmbedded getGuiQuestion(GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		GuiQuestionBasic q = new GuiQuestionBasic(this, screen, posX, posY, sizeX, sizeY);
		return q;
	}
	
	@Override
	public GuiEmbedded getGuiAnswer(GuiQuesting screen, int posX, int posY, int sizeX, int sizeY)
	{
		return null;
	}
	
}
