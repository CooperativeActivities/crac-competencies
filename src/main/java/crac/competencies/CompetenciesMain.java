package crac.competencies;

import java.io.IOException;

public class CompetenciesMain {

	public static void main(String[] args) {
		CompetenciesOntology ont = new CompetenciesOntology();
		ont.initializeCompetencies();
		
		ont.addPerson("Markus");
		ont.addBasicCompetency("JavaProgrammierung");
		
		
		ont.addCompetencyToPerson("Markus", "JavaProgrammierung", 90);
		
		ont.addBasicCompetency("MSOffice");
		ont.addCompetencyToPerson("Markus", "MSOffice", 70);
		//ont.print();
		
		
		/*try {
			
			
			String response = ont.httpGet("http://localhost:9200/megacorp/employee/1");
	
			System.out.println(response);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
