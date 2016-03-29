package crac.competencies;

import java.io.IOException;

import org.json.simple.JSONObject;

public class CompetenciesMain {

	public static void main(String[] args) {
		//ElasticSearchAdapter adapter = new ElasticSearchAdapter();
		/*CompetenciesOntology ont = new CompetenciesOntology();
		ont.initializeCompetencies();
		
		ont.addPerson("Hannes");
		ont.addBasicCompetency("JavaProgrammierung");
		
		
		ont.addCompetencyToPerson("Hannes", "JavaProgrammierung", 90);
		
		ont.addBasicCompetency("MSOffice");
		ont.addCompetencyToPerson("Hannes", "MSOffice", 70);
		//ont.print();*/
		
		
		try {
			JSONObject searchObj = new JSONObject();
			JSONObject query = new JSONObject();
			JSONObject match = new JSONObject();
			match.put("competencies.competency", "JavaProgrammierung");
			query.put("match", match);
			searchObj.put("query", query);
			
			System.out.println(searchObj);
			
			String response = ElasticSearchAdapter.search(searchObj);
			System.out.println(response);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
