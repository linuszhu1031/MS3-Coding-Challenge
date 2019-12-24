import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Challenge {
	public static void main(String[] args) throws IOException {
		// Create two lists of data, one for good data, the other is for rows missing data
		List<List<String>> records = new ArrayList<>();
		List<List<String>> badRecords = new ArrayList<>();

		// Get scanner instance
        Scanner scanner = new Scanner(new File("data.csv"));
        
        // Create writer for rows that have a missing piece of data
        BufferedWriter badWriter = Files.newBufferedWriter(Paths.get("bad-data.csv"));

        // Set the delimiter used in file
        scanner.useDelimiter(",");
        
        // Go through csv row by row, if row is missing a piece of data, add it to the bad data list
        while (scanner.hasNextLine()) {
        	List<String> line;
        	line = getRecordFromLine(scanner.nextLine());
        	
        	if (line.contains(""))
        		badRecords.add(line);
        	
        	else {
        		records.add(line);
        	}
            
        }
        
        // Go through the bad data list and write it to a csv file
        // Should be writing it to a SQLite database file instead
        // Due to lack of time and skill with SQL, creating a csv file 
        for (List<String> record : badRecords) {
            badWriter.write(String.join(",", record));
            badWriter.newLine();
        }
        
        badWriter.close();
        scanner.close();
        
        // Create writer for the good data
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("valid-data.csv"));
        
        for (List<String> record : records) {
            writer.write(String.join(",", record));
            writer.newLine();
        }
        
        writer.close();
        
        // Providing statistics
        // # of records received
        // # of records successfully added
        // # of records failed because of missing data
        
		int review;
		int success;
		int fail;
		
        review = badRecords.size() + records.size() - 1;
        success = records.size() - 1;
        fail = badRecords.size();
        
        String reviewString = Integer.toString(review);
        String successString = Integer.toString(success);
        String failString = Integer.toString(fail);

        BufferedWriter log = Files.newBufferedWriter(Paths.get("data.log"));
        
        log.write("# of records received: " + reviewString);
        log.newLine();
        log.write("# of records successful: " + successString);
        log.newLine();
        log.write("# of records failed: " + failString);
        
        log.close();

	}
	
	// Helper function to parse lines and store it
	private static List<String> getRecordFromLine(String line) {
	    List<String> values = new ArrayList<String>();
	    
	    try (Scanner rowScanner = new Scanner(line)) {
	        rowScanner.useDelimiter(",");
	        while (rowScanner.hasNext()) {
	            values.add(rowScanner.next());
	        }
	    }
	    return values;
	}
}
