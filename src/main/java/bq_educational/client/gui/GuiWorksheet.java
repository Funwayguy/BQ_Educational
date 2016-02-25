package bq_educational.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiButtonQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.client.themes.ThemeRegistry;
import bq_educational.tasks.worksheet.Worksheet;
import bq_educational.tasks.worksheet.questions.QuestionBase;

public class GuiWorksheet extends GuiQuesting
{
	Worksheet sheet;
	public int page = 0;
	GuiEmbedded uiLeft; // Question
	GuiEmbedded uiRight; // Answer(s)
	
	public GuiWorksheet(Worksheet sheet, GuiScreen parent)
	{
		super(parent, sheet.title);
		this.sheet = sheet;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.add(new GuiButtonQuesting(1, guiLeft + sizeX/2 - 120, guiTop + sizeY - 16, 20, 20, "<"));
		this.buttonList.add(new GuiButtonQuesting(1, guiLeft + sizeX/2 + 100, guiTop + sizeY - 16, 20, 20, ">"));
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTick)
	{
		super.drawScreen(mx, my, partialTick);
		
		drawEmbedded(mx, my, partialTick);
		
		String pgTxt = (page + 1) + "/" + Math.max(1, sheet.questionList.size());
		mc.fontRenderer.drawString(pgTxt, guiLeft + sizeX/2 - mc.fontRenderer.getStringWidth(pgTxt)/2, guiTop + sizeY - 28, ThemeRegistry.curTheme().textColor().getRGB());
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		
		if(button.id == 1)
		{
			page--;
			refresh();
		} else if(button.id == 2)
		{
			page++;
			refresh();
		}
	}
	
	public void refresh()
	{
		embedded.clear(); // Deletes all previous embedded UIs
		
		if(sheet.questionList.size() <= 0)
		{
			page = 0;
			uiLeft = null;
			uiRight = null;
		}
		
		page = MathHelper.clamp_int(page, 0, sheet.questionList.size() - 1);
		QuestionBase question = sheet.questionList.get(page);
		uiLeft = question.getGuiQuestion(this, guiLeft + 24, guiTop + 32, sizeX/2 - 32, sizeY - 64);
		uiRight = question.getGuiAnswer(this, guiLeft + sizeX/2 + 8, guiTop + 32, sizeX/2 - 32, sizeY - 64);
		
		// These could be null!
		embedded.add(uiLeft);
		embedded.add(uiRight);
	}
}
