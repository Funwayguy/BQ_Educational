package bq_educational.tasks.worksheet.questions;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Level;
import bq_educational.core.BQ_Educational;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class QuestionRegistry
{
	public static HashMap<String, Class<? extends QuestionBase>> questionTypes = new HashMap<String, Class<? extends QuestionBase>>();
	
	public void registerQuestion(Class<? extends QuestionBase> question, String idName)
	{

		try
		{
			ModContainer mod = Loader.instance().activeModContainer();
			
			if(idName.contains(":"))
			{
				throw new IllegalArgumentException("Illegal character(s) used in question ID name");
			}
			
			if(question == null)
			{
				throw new NullPointerException("Tried to register null question");
			} else if(mod == null)
			{
				throw new IllegalArgumentException("Tried to register a question without an active mod instance");
			}
			
			try
			{
				question.getDeclaredConstructor();
			} catch(NoSuchMethodException e)
			{
				throw new NoSuchMethodException("Question is missing a default constructor with 0 arguemnts");
			}
			
			String fullName = mod.getModId() + ":" + idName;
			
			if(questionTypes.containsKey(fullName) || questionTypes.containsValue(question))
			{
				throw new IllegalStateException("Cannot register dupliate question type '" + fullName + "'");
			}
			
			questionTypes.put(fullName, question);
		} catch(Exception e)
		{
			BQ_Educational.logger.log(Level.ERROR, "An error occured while trying to register question", e);
		}
	}
	
	public static ArrayList<String> GetTypeList()
	{
		return new ArrayList<String>(questionTypes.keySet());
	}
	
	public static String GetID(Class<? extends QuestionBase> reward)
	{
		for(String idName : questionTypes.keySet())
		{
			if(questionTypes.get(idName) == reward)
			{
				return idName;
			}
		}
		
		return null;
	}
	
	public static QuestionBase InstatiateTask(String idName)
	{
		try
		{
			Class<? extends QuestionBase> question = questionTypes.get(idName);
			
			if(question == null)
			{
				BQ_Educational.logger.log(Level.ERROR, "Tried to load missing task question '" + idName + "'! Are you missing an expansion?");
				return null;
			}
			
			return questionTypes.get(idName).newInstance();
		} catch(Exception e)
		{
			BQ_Educational.logger.log(Level.ERROR, "Unable to instatiate question: " + idName, e);
			return null;
		}
	}
}
