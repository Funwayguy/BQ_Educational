package bq_educational.tasks.worksheet;

import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import betterquesting.utils.JsonHelper;
import bq_educational.tasks.worksheet.questions.QuestionBase;
import bq_educational.tasks.worksheet.questions.QuestionRegistry;

public class Worksheet
{
	public int ID = -1; // Do not edit
	public String title = "New Worksheet";
	public int grade = -1; // Defaults to lowest grade
	public ArrayList<QuestionBase> questionList = new ArrayList<QuestionBase>();
	
	/**
	 * Removes and answers and marking data from all questions for template use
	 */
	public void makeTemplate()
	{
		for(QuestionBase q : questionList)
		{
			q.clean();
		}
		
		grade = -1;
	}
	
	/**
	 * Creates a clean copy for students to use (kind of like handing out photocopies in class)
	 */
	public Worksheet cleanCopy()
	{
		Worksheet clean = new Worksheet();
		JsonObject data = new JsonObject();
		writeToJson(data);
		clean.readFromJson(data);
		clean.makeTemplate();
		return clean;
	}
	
	public void writeToJson(JsonObject json)
	{
		json.addProperty("title", title);
		json.addProperty("grade", grade);
		
		JsonArray qAry = new JsonArray();
		for(QuestionBase q : questionList)
		{
			JsonObject qj = new JsonObject();
			q.writeToJson(qj);
			qAry.add(qj);
		}
		json.add("questions", qAry);
	}
	
	public void readFromJson(JsonObject json)
	{
		title = JsonHelper.GetString(json, "title", "New Worksheet");
		grade = JsonHelper.GetNumber(json, "grade", -1).intValue();
		
		questionList = new ArrayList<QuestionBase>();
		for(JsonElement je : JsonHelper.GetArray(json, "questions"))
		{
			if(je == null || !je.isJsonObject())
			{
				continue;
			}
			
			JsonObject qj = je.getAsJsonObject();
			QuestionBase question = QuestionRegistry.InstatiateTask(JsonHelper.GetString(json, "questionID", ""));
			
			if(question != null)
			{
				question.readFromJson(qj);
				questionList.add(question);
			}
		}
	}
}
