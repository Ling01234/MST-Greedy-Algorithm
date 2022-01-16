import java.util.*;

class Assignment implements Comparator<Assignment>{
	int number;
	int weight;
    int deadline;
    int completiontime;
	
	
	protected Assignment() {
	}
	
	protected Assignment(int number, int weight, int deadline, int completiontime) {
		this.number = number;
		this.weight = weight;
        this.deadline = deadline;
        this.completiontime = completiontime;
	}
	
	
	
	/**
	 * This method is used to sort to compare assignment objects for sorting. 
	 * Return -1 if a1 > a2; A2 should appear after A1 in the sorted list
	 * Return 1 if a1 < a2; A1 should appear after A2 in the sorted list
	 * Return 0 if a1 = a2 
	 */
	@Override
	public int compare(Assignment a1, Assignment a2) {
		
		float avg1 = (float) (a1.weight / a1.completiontime) ;
		float avg2 = (float) (a2.weight / a2.completiontime) ;
		
		
		
		if (avg1 == avg2) {//equal avg weight
			
			if (a1.deadline == a2.deadline && a1.weight == a2.weight) {//everything is the same
				return 0;
			}
			
			
			if (a1.deadline <= a2.deadline) { //a1 deadline is sooner
				return -1;
			}
			
			
			return 1;
	
		}
		
		else if (avg1 < avg2) {
			return 1; //avg2 weights more
		}
		else {
			return -1; //avg1 weights more
		}

	}
	
	public static double mark(Assignment a, int time) {//determine grade received for asgn
		
		
		if (time >= a.deadline) { //start past deadline
			return 0;
		}
		
		if (time + a.completiontime <= a.deadline) { //have time to finish
			return (double) a.weight;
		}
		
		else {//we will be submitting late
			double lateTime = (double) a.completiontime + time - a.deadline;
			
			if (lateTime >= 10) { //will get 0 even ifs we finish
				return 0;
			}
			
			//grade received for late submission
			return (double) a.weight * (1 - lateTime * 0.1);
		}
		
		
	}
	
	
}

public class HW_Sched {
	ArrayList<Assignment> Assignments = new ArrayList<Assignment>();
	int m;
    int lastDeadline = 0;
    double grade = 0.0;
	
	protected HW_Sched(int[] weights, int[] deadlines, int[] completiontimes, int size) throws Exception {
        if(size==0){
            throw new Exception("There is no assignment.");
        }
		for (int i=0; i<size; i++) {
			Assignment homework = new Assignment(i, weights[i], deadlines[i], completiontimes[i]);
			this.Assignments.add(homework);
			if (homework.deadline > lastDeadline) {
				lastDeadline = homework.deadline;
			}
		}
		m =size;
	}
	
	
	/**
	 * 
	 * @return Array where output[i] corresponds to the assignment 
	 * that will be done at time i.
	 */
	public ArrayList<Integer> SelectAssignments() {
		
		
		
		//Sort assignments
		//Order will depend on how compare function is implemented
		Collections.sort(Assignments, new Assignment());
        
        //TODO Implement this

		// If homeworkPlan[i] has a value -1, it indicates that the 
		// i'th timeslot in the homeworkPlan is empty
		ArrayList<Integer> homeworkPlan = new ArrayList<>();
		for (int i=0; i < lastDeadline; ++i) {
			homeworkPlan.add(-1);
		}
		
		int time = 0;
		
		
		//loop through each assignment
		for (int j = 0; j<Assignments.size(); j++) {
			
			
			if (time >= this.lastDeadline) {
				break; //past all deadlines
			}
		
			//last element or no more time to do more assignments
			if (j == Assignments.size() - 1 || Assignments.get(j).completiontime + time > this.lastDeadline) {
				//System.out.println("Last element");
				
				//System.out.println(this.lastDeadline);
				for (int k = time; k < time + Assignments.get(j).completiontime; k++) {
					if (k < this.lastDeadline) {
						homeworkPlan.set(k, Assignments.get(j).number);
					}
					else {
						homeworkPlan.add(Assignments.get(j).number);
					}
				}
				this.lastDeadline = homeworkPlan.size() - 1;
				grade += Assignment.mark(Assignments.get(j), time);
				break;
			}
			

			
			
			

			
			if (Assignment.mark(Assignments.get(j), time) == 0) {
				continue; //this assignment doesn't give any marks.
			}
			
			//if we have time to finish assignment before deadline
			if (Assignments.get(j).completiontime + time <= Assignments.get(j).deadline) {
				for (int k = time; k < time + Assignments.get(j).completiontime; k++) {
					
					//System.out.printf("this is iteration %d with hmrk number %d \n", j, Assignments.get(j).number);
					homeworkPlan.set(k, Assignments.get(j).number);
				}
				grade += Assignments.get(j).weight;
				time += Assignments.get(j).completiontime; //update time after finishing asgn
				continue;
			}
			
			
		
			else { //cannot finish assignment on time

				double grade1 = Assignment.mark(Assignments.get(j), time);
				int timeTmp = time + Assignments.get(j).completiontime;
				
				
				//System.out.printf("this is ITERATION %d with hmrk number %d \n", j, Assignments.get(j).number);
				double grade2 = Assignment.mark(Assignments.get(j+1), timeTmp);
				//if we do both assignments
				double grade1and2 = grade1 + grade2; 
				
				//if we skip current assignment and do the next one
				double gradeOnly2 = Assignment.mark(Assignments.get(j+1), time);
				

				
				if (grade1and2 > gradeOnly2) {
					//do grade1and2
					/*
					for (int k = time; k < time + Assignments.get(j).completiontime; k++) {
						if (k < this.lastDeadline) {
							homeworkPlan.set(k, Assignments.get(j).number);
						}
						else {
							homeworkPlan.add(Assignments.get(j).number);
						}
					}
					
					this.lastDeadline = homeworkPlan.size() - 1;
					//System.out.println(this.lastDeadline);
					this.grade += grade1;
					time += Assignments.get(j).completiontime;
					continue;
					*/
					
					for (int k = time; k < time + Assignments.get(j).completiontime; k++) {
						homeworkPlan.set(k, Assignments.get(j).number);
					}
					this.grade += grade1;
					time += Assignments.get(j).completiontime;
					continue;
					
				}
				
				else {//gradeOnly2 is better
					continue;
				}

			}

		}
		

		return homeworkPlan;
	}
	
	
	
	public static void main(String args[]) throws Exception{

		
		int [] i1 = {34,65,23,4}; //weight
		int [] i2 = {7,5,2,1}; //deadline
		int [] i3 = {4,5,2,1}; //completion time
		

		HW_Sched hmrk = new HW_Sched(i1, i2, i3, 4);
		
		//System.out.println(hmrk.SelectAssignments());
		//System.out.printf("Grade %f, last deadline %d \n", hmrk.grade, hmrk.lastDeadline);

	}
	
}
	



