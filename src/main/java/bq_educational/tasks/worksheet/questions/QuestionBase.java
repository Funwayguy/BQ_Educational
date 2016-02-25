package bq_educational.tasks.worksheet.questions;

import betterquesting.client.gui.GuiQuesting;
import betterquesting.client.gui.misc.GuiEmbedded;
import betterquesting.utils.JsonHelper;
import com.google.gson.JsonObject;

public abstract class QuestionBase
{
	public String question = "What's 1 + 1?";
	public String answer = ""; // Set by students
	public String comment = ""; // Set by teachers
	public boolean isCorrect = false; // Set by teachers
	
	/**
	 * Removes and answers and marking data for template use
	 */
	public void clean()
	{
		answer = "";
		comment = "";
		isCorrect = false;
	}
	
	public void writeToJson(JsonObject json)
	{
		json.addProperty("question", question);
		json.addProperty("answer", answer);
		json.addProperty("comment", comment);
		json.addProperty("isCorrect", isCorrect);
	}
	
	public void readFromJson(JsonObject json)
	{
		question = JsonHelper.GetString(json, "question", "What's 1 + 1?");
		answer = JsonHelper.GetString(json, "answer", answer);
		comment = JsonHelper.GetString(json, "comment", comment);
		isCorrect = JsonHelper.GetBoolean(json, "isCorrect", false);
	}
	
	/**
	 * Gets the GUI displaying the question and any necessary images
	 */
	public abstract GuiEmbedded getGuiQuestion(GuiQuesting screen, int posX, int posY, int sizeX, int sizeY);
	
	/**
	 * Gets the GUI elements used to answer the given question. These elements should 
	 */
	public abstract GuiEmbedded getGuiAnswer(GuiQuesting screen, int posX, int posY, int sizeX, int sizeY);
}