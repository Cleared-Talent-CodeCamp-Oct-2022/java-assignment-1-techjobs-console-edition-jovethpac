import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        // Bonus mission: sort the results
        Collections.sort(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        // Bonus mission; normal version returns allJobs
        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();
//creates a new ArrayList call jobs where we store any jobs that match our search term
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            //String aValue = row.get(column);  //original in case I mess up
            String aValue = row.get(column).toLowerCase();  //makes columns like name, position, location caseinsensitive
           // if (aValue.contains(value)) { //original in case I mess up
            if (aValue.contains(value.toLowerCase())) { //makes the specific entries under each column case insensitive
                jobs.add(row);
            }
        }

        return jobs;

    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        // TODO - implement this method  //acts like a list of results, my new searches will get fed into jobQuery
        ArrayList<HashMap<String, String>> jobQuery = new ArrayList<>();

        for (HashMap<String, String> allRows : allJobs) {  //singular: allRows  collection: allJobs

            for (String oneValue : allRows.keySet()) {  //oneValue one singular key within this entire key sets  holds only oneValue  ; don't copy the first for loop, we don't want a HashMap
                String allResults = allRows.get(oneValue); //allResults stores all of the values (LaunchCode, Developer etc) from the oneValue key
               // if (allResults.contains(value)) { //modify a little based on the second for loops...chanage based on what I use for second for loop
                if (allResults.contains(value.toLowerCase())) { //makes all values caseInsensitive
                    jobQuery.add(allRows);  //adds individual key value pair to this whoel ArrayList of jobQuery
                    break; //this is correct. stops at the first instance
                }
            }
        }
        return jobQuery;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
