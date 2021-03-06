import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MemAlloc 
{

	public static void main(String[] args) 
	{
		checkArgumentForHelp(args);
		checkForArgumentsLength(args);
		ArrayList<Integer> holes = checkFirstCommandArgumentForFileInput(args);
		ArrayList<Integer> processes = checkSecondCommandArgumentForFileInput(args);
		String chosenAlgorithm = checkThirdArgumentForAlgo(args); 
		printFilesBeingUsed(args); 
		printInitalConditionsForHoles(holes); 
		printInitialConditionsForProcesses(processes); 
		executeChosenAlgorithm(chosenAlgorithm, holes, processes);
	}
	
	
	public static void checkArgumentForHelp(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equalsIgnoreCase("-H") || args[i].equalsIgnoreCase("H"))
			{
				System.out.printf("\nmemAlloc holeFile procFile first|best|worst\n");
				System.out.printf("* indicates change was made to memory hole\n");
				System.exit(1);
			}
		}
	}
	
	
	public static void checkForArgumentsLength(String[] args)
	{
		if(args.length > 3)
		{
			System.err.printf("\n** ERROR: Too many command line args\n");
			System.exit(2);
		}

		if(args.length < 3)
		{
			System.err.printf("\n** ERROR: Too few command line args\n");
			System.exit(3);
		}
	}
	
	
	public static ArrayList<Integer> checkFirstCommandArgumentForFileInput(String[] args)
	{
		Scanner scanner;
		ArrayList<Integer> holes = new ArrayList<>(); 
		
		try
		{
			File file = new File(args[0]);
			scanner = new Scanner(file);
			
			while(scanner.hasNext())
			{
				int currentLine; 
				try
				{
					currentLine = Integer.parseInt(scanner.nextLine());
					holes.add(currentLine);
				}
				catch(Exception exc)
				{
					System.err.printf("\n** ERROR: Contents of %s must be numerical.\n", args[0]);
					System.exit(5);
				}
				
			}
			scanner.close();
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("\n** ERROR: %s could not be found.\n", args[0]);
			System.exit(4);
		}
	
		return holes;
	}
	
	
	public static ArrayList<Integer> checkSecondCommandArgumentForFileInput(String[] args)
	{
		ArrayList<Integer> processes = new ArrayList<>();
		Scanner scanner;
		
		try
		{
			File file = new File(args[1]);
			scanner = new Scanner(file);
			
			while(scanner.hasNextLine())
			{
				String[] currentLine = scanner.nextLine().split(" +");
				String processesSize = currentLine[1]; 
				int numericalProcessSize;
				
				try
				{
					numericalProcessSize = Integer.parseInt(processesSize); 
					processes.add(numericalProcessSize); 
				}
				catch(Exception exce)
				{
					System.err.printf("\n** ERROR: Contents of %s must be numerical.\n", args[1]);
					System.exit(7);
				}
			}
			scanner.close();
		}
		catch(FileNotFoundException e)
		{
			System.err.printf("\n** ERROR: %s could not be found.\n", args[1]);
			System.exit(6);
		}
		
		return processes;
	}
	
	
	public static String checkThirdArgumentForAlgo(String[] args)
	{
		if(!(args[2].equalsIgnoreCase("first") || (args[2].equalsIgnoreCase("best")
				|| (args[2].equalsIgnoreCase("worst")))))
		{
			System.err.printf("\n** ERROR: Third argument must be memory algorithm\n");
			System.exit(8);
		}
		
		return args[2];
	}
	
	
	public static void printFilesBeingUsed(String[] args)
	{
		System.out.printf("\nUsing files \"%s\" and \"%s\".\n", args[0], args[1]);
	}
	
	
	public static void printInitalConditionsForHoles(ArrayList <Integer> holes)
	{
		System.out.printf("\nInitial Conditions\n\n");
		System.out.printf("Holes\n");
		
		for(int i = 0; i < holes.size(); i++)
		{
			System.out.printf(" H%d: %5d K\n", i, holes.get(i));
		}
	}
	
	
	public static void printInitialConditionsForProcesses(ArrayList<Integer> processes)
	{
		System.out.printf("\nProcs\n");
		
		for(int i = 0; i < processes.size(); i++)
		{
			System.out.printf(" P%d: %5d K\n", i + 1, processes.get(i));
		}
	}
	
	
	public static void executeChosenAlgorithm(String chosenAlgorithm, ArrayList<Integer> holes, 
			ArrayList<Integer> processes)
	{
		if(chosenAlgorithm.equalsIgnoreCase("first"))
		{
			System.out.printf("\nFirstFit\n");
			System.out.printf("\nAfter allocations have been made\n");
			executeFirstFitAlgorithm(holes, processes); 
		}
		else if(chosenAlgorithm.equalsIgnoreCase("best"))
		{
			System.out.printf("\nBestFit\n");
			System.out.printf("\nAfter allocations have been made\n");
			executeBestFitAlgorithm(holes, processes);
		}
		else
		{
			System.out.printf("\nWorstFit\n");
			System.out.printf("\nAfter allocations have been made\n");
			executeWorstFitAlgorithm(holes, processes);
		}
	}
	
	public static void executeFirstFitAlgorithm(ArrayList<Integer> holes, ArrayList<Integer> processes)
	{
		int newValue = 0; 
		ArrayList <Integer> holesIndexes = new ArrayList<>(); 
		ArrayList <Integer> allocationResults = new ArrayList<>(); 
		
		for(int i = 0; i < processes.size(); i++)
		{
			for(int j = 0; j < holes.size(); j++)
			{
				if(holes.get(j) >= processes.get(i))
				{
					newValue = holes.get(j) - processes.get(i); 
					allocationResults.add(newValue); 
					holes.set(j, newValue); 
					holesIndexes.add(j); 
					break; 
				}
			}
		}
		printAlgorithmAllocationResults(holes, holesIndexes, allocationResults); 
	}
	
	
	public static void executeBestFitAlgorithm(ArrayList<Integer> holes, ArrayList<Integer> processes)
	{
		int newValue = 0;
		int bestFitIndex = 0; 
		ArrayList <Integer> holesIndexes = new ArrayList<>();
		ArrayList <Integer> allocationResults = new ArrayList<>();
		
		for(int i = 0; i < processes.size(); i++)
		{
			bestFitIndex = -1;
			for(int j = 0; j < holes.size(); j++)
			{
				if(holes.get(j) >= processes.get(i))
				{
					if(bestFitIndex == -1)
					{
						bestFitIndex = j;
					}
					else if(holes.get(bestFitIndex) > holes.get(j))
					{
						bestFitIndex = j;
					}
				}
			}
			
			if(bestFitIndex != -1)
			{
			 newValue = holes.get(bestFitIndex) - processes.get(i);
			 allocationResults.add(newValue);
			 holes.set(bestFitIndex, newValue);
			 holesIndexes.add(bestFitIndex);
			}
		}
		printAlgorithmAllocationResults(holes, holesIndexes, allocationResults);
	}
	

	public static void executeWorstFitAlgorithm(ArrayList<Integer> holes, ArrayList<Integer> processes)
	{
		int newValue = 0;
		int worstFitIndex = 0;
		ArrayList <Integer> holesIndexes = new ArrayList<>();
		ArrayList <Integer> allocationResults = new ArrayList<>();
		
		for(int i = 0; i < processes.size(); i++)
		{
			worstFitIndex = -1;
			for(int j = 0; j < holes.size(); j++)
			{
				if(holes.get(j) >= processes.get(i))
				{
					if(worstFitIndex == -1)
					{
						worstFitIndex = j;
					}
					else if(holes.get(worstFitIndex) < holes.get(j))
					{
						worstFitIndex = j;
					}
				}
			}
			
			if(worstFitIndex != -1)
			{
			 newValue = holes.get(worstFitIndex) - processes.get(i);
			 allocationResults.add(newValue);
			 holes.set(worstFitIndex, newValue);
			 holesIndexes.add(worstFitIndex);
			}
		}
		printAlgorithmAllocationResults(holes, holesIndexes, allocationResults);
	}
	
	public static void printAlgorithmAllocationResults(ArrayList<Integer> holes, ArrayList<Integer>holesIndexes, 
			ArrayList<Integer>allocationResults)
	{
		System.out.printf("\nHoles\n");
		for(int i = 0; i < holes.size(); i++)
		{
			System.out.printf(" H%d: %5d K\n", i, holes.get(i));
		}
		
		System.out.printf("\nResults\n");
		for(int j = 0; j < holes.size() - 1; j++)
		{
			System.out.printf(" P%d:   @ H%d: %5d k\n", j + 1, holesIndexes.get(j), allocationResults.get(j));
		}
		
		System.exit(0);
	}
}
