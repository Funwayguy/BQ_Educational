package bq_educational.students;

import java.util.ArrayList;

/**
 * Recommended interface for querying and collating student data
 */
public class EduJsonApi
{
	/**
	 * Search names and IDs for a list of matching students
	 */
	public ArrayList<Student> searchStudent(String query)
	{
		ArrayList<Student> results = new ArrayList<Student>();
		
		for(Student s : EduDatabase.students.values())
		{
			if(s.name.toLowerCase().startsWith(query))
			{
				results.add(s);
				continue;
			} else if(s.studentID.toLowerCase().startsWith(query))
			{
				results.add(s);
				continue;
			}
		}
		
		return results;
	}
}
