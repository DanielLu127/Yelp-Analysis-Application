import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.*;


public class Populate {
    public static void main(String [] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
                System.out.println("Error loading driver: " + e);
        }

        try {
            String oracleURL = "jdbc:oracle:thin:@localhost:1521:XE";
            Connection connection = DriverManager.getConnection(oracleURL, "system", "lxw1997127");

            cleanTables(connection);
            createTables(connection);

            //populate yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json
            populateBusiness(connection, args[0]);
            populateUsers(connection, args[3]);
            populateReviews(connection, args[1]);
            populateCheckin(connection, args[2]);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void cleanTables(Connection connection) {
        try {

            PreparedStatement prepareDeleteStm;

            String delete = "DROP TABLE REVIEW\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

            delete = "DROP TABLE YELP_USERS\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

            delete = "DROP TABLE BUSINESS_ATTRIBUTES\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

            delete = "DROP TABLE BUSINESS_SUB_CATEGORIES\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

            delete = "DROP TABLE BUSINESS_MAIN_CATEGORIES\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

            delete = "DROP TABLE CHECKIN\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

            delete = "DROP TABLE BUSINESS\n ";
            prepareDeleteStm = connection.prepareStatement(delete.toString());
            prepareDeleteStm.executeQuery();
            System.out.println(delete + "\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void createTables(Connection connection) {

        try {
            PreparedStatement prepareCreateStm;

            StringBuilder create = new StringBuilder();
            create.append("CREATE TABLE BUSINESS (\n " +
                    "business_id VARCHAR(25) PRIMARY KEY, \n" +
                    "full_address VARCHAR(200) NOT NULL, \n" +
                    "ifOpen  VARCHAR(5), " +
                    "city VARCHAR(50) NOT NULL, \n" +
                    "state_name VARCHAR(20) NOT NULL, \n" +
                    "review_count NUMBER, \n" +
                    "business_name VARCHAR(100) NOT NULL, \n" +
                    "starts NUMBER ) \n"
            );

            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");


            create = new StringBuilder();
            create.append("CREATE TABLE BUSINESS_MAIN_CATEGORIES( \n" +
                    "business_id VARCHAR(25), \n" +
                    "main_category VARCHAR(50), \n" +
                    "PRIMARY KEY (business_id, main_category), \n" +
                    "FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id) )\n"
            );

            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");


            create = new StringBuilder();
            create.append("CREATE TABLE BUSINESS_SUB_CATEGORIES ( \n" +
                    "business_id VARCHAR(25), \n" +
                    "sub_category VARCHAR(50), \n" +
                    "PRIMARY KEY (business_id, sub_category), \n" +
                    "FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id) )\n"
            );
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");


            create = new StringBuilder();
            create.append("CREATE TABLE BUSINESS_ATTRIBUTES ( \n" +
                    "business_id VARCHAR(25), \n" +
                    "attributes VARCHAR(50), \n" +
                    "PRIMARY KEY (business_id, attributes), \n" +
                    "FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id) )\n"
            );

            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");


            create = new StringBuilder();
            create.append("CREATE TABLE YELP_USERS ( \n" +
                    "yelping_since DATE, \n" +
                    "num_votes NUMBER, \n" +
                    "review_count NUMBER, \n" +
                    "user_name VARCHAR(100), \n" +
                    "user_id VARCHAR(25) PRIMARY KEY, \n" +
                    "average_stars NUMBER, \n" +
                    "numFriend NUMBER )\n"
            );
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");


            create = new StringBuilder();
            create.append("CREATE TABLE REVIEW ( \n" +
                    "num_votes NUMBER, \n" +
                    "user_id VARCHAR(25) NOT NULL, \n" +
                    "review_id VARCHAR(25) PRIMARY KEY, \n" +
                    "stars NUMBER, \n" +
                    "review_date DATE, \n" +
                    "review_text Long, \n" +
                    "business_id VARCHAR(25) NOT NULL, \n" +
                    "FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id), \n" +
                    "FOREIGN KEY(user_id) REFERENCES YELP_USERS(user_id) )\n"
            );
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");


            create = new StringBuilder();
            create.append("CREATE TABLE CHECKIN ( \n" +
                    "check_in Long, \n" +
                    "business_id VARCHAR(25) PRIMARY KEY, \n" +
                    "FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id) )\n"
            );
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX MAIN_CATEGORY_NAME_INDEX ON BUSINESS_MAIN_CATEGORIES(main_category)");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX MAIN_CATEGORY_ID_INDEX ON BUSINESS_MAIN_CATEGORIES(business_id)");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX SUB_CATEGORY_NAME_INDEX ON BUSINESS_SUB_CATEGORIES(sub_category)");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX SUB_CATEGORY_ID_INDEX ON BUSINESS_SUB_CATEGORIES(business_id)");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX BUSINESS_ARRTRIBUTES_INDEX ON BUSINESS_ATTRIBUTES(attributes) \n ");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX BUSINESS_ATTRIBUTES_ID_INDEX ON BUSINESS_ATTRIBUTES(business_id) \n ");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX REVIEW_BUSINESS_ID_INDEX ON REVIEW(business_id) \n ");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");

            create = new StringBuilder();
            create.append("CREATE INDEX REVIEW_USER_ID_INDEX ON REVIEW(user_id) \n");
            prepareCreateStm = connection.prepareStatement(create.toString());
            prepareCreateStm.executeQuery();
            System.out.println(create + "\n");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void populateReviews(Connection connection, String argument)  {
        String filePath = "src/" + argument;
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {

            int count = 0;
            String line = buffer.readLine();
            PreparedStatement prepareReviewStm = connection.prepareStatement("INSERT into REVIEW VALUES(?,?,?,?,?,?,?)");

            while (line !=  null) {
                //transform every line of input to json object
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(line);

                //retrieve json values from the first layer of the json object
                String text = (String) jsonObject.get("text");
                String user_id = (String) jsonObject.get("user_id");
                String review_id = (String) jsonObject.get("review_id");

                //get stars
                long stars = (long)jsonObject.get("stars");

                //get date
                String date_string = (String) jsonObject.get("date");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date javaDate = sdf1.parse(date_string);
                java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());

                //get business id
                String business_id = (String) jsonObject.get("business_id");

                // get votes
                JSONObject votes = (JSONObject) jsonObject.get("votes");
                long funny = (long) votes.get("funny");
                long useful = (long) votes.get("useful");
                long cool = (long) votes.get("cool");
                long numVotes = funny + useful + cool;

                //set sql statement
                prepareReviewStm.setLong(1, numVotes);
                prepareReviewStm.setString(2, user_id);
                prepareReviewStm.setString(3, review_id);
                prepareReviewStm.setLong(4, stars);
                prepareReviewStm.setDate(5, sqlDate);
                prepareReviewStm.setString(6, text);
                prepareReviewStm.setString(7, business_id);
                prepareReviewStm.addBatch();

                //read next line
                line = buffer.readLine();
                System.out.println("REVIEW record prepare SUCCEED " + count++);
            }

            //update sql batch
            prepareReviewStm.executeBatch();
            prepareReviewStm.close();
            System.out.println("REVIEW record insert SUCCEED");

        } catch (IOException | ParseException | SQLException | java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private static void populateUsers(Connection connection, String argument)  {
        String filePath = "src/" + argument;
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
            int count = 0;
            String line = buffer.readLine();
            PreparedStatement prepareUserStm = connection.prepareStatement("INSERT into YELP_USERS VALUES(?,?,?,?,?,?,?)");

            while (line != null) {
                //transform every line of input to json object
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(line);

                //get yelping since
                String yelping_since_string = (String) jsonObject.get("yelping_since");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                java.util.Date javaDate = sdf1.parse(yelping_since_string);
                java.sql.Date yelping_since = new java.sql.Date(javaDate.getTime());

                //review count,
                long reviewCount = (long) jsonObject.get("review_count");

                //name, user_id
                String name = (String) jsonObject.get("name");
                String userId = (String) jsonObject.get("user_id");

                //get num friends
                JSONArray friendList = (JSONArray) jsonObject.get("friends");
                int numFriend = friendList.size();

                //average stars
                double averageStars = (double) jsonObject.get("average_stars");

                //get votes
                JSONObject votes = (JSONObject)jsonObject.get("votes");
                long funny = (long)votes.get("funny");
                long useful = (long)votes.get("useful");
                long cool = (long)votes.get("cool");
                long numVotes = funny + useful + cool;

                //prepare sql statement
                prepareUserStm.setDate(1, yelping_since);
                prepareUserStm.setLong(2, numVotes);
                prepareUserStm.setLong(3, reviewCount);
                prepareUserStm.setString(4, name);
                prepareUserStm.setString(5, userId);
                prepareUserStm.setDouble(6, averageStars);
                prepareUserStm.setInt(7, numFriend);
                prepareUserStm.addBatch();

                //read next line
                line = buffer.readLine();
                System.out.println("USER record prepare SUCCEED " + count++);
            }

            prepareUserStm.executeBatch();
            prepareUserStm.close();
            System.out.println("USER records insert SUCCEED ");

        } catch (IOException | ParseException | java.text.ParseException | SQLException e ) {
            e.printStackTrace();
        }
    }

    private static void populateBusiness(Connection connection, String argument) {
        int count = 0;
        HashSet<String> main_categories_map = new HashSet<>();
        String []  array = new String[]{"Active Life", "Arts & Entertainment", "Automotive", "Car Rental", "Cafes",
                "Beauty & Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores", "Department Stores",
                "Education", "Event Planning & Services", "Flowers & Gifts", "Food", "Health & Medical", "Home Services",
        "Home & Garden", "Hospitals", "Hotels & Travel", "Hardware Stores", "Grocery", " Medical Centers",
                "Nurseries & Gardening", "Nightlife", "Restaurants", "Shopping", "Transportation"};

        for(String s: array) {
            main_categories_map.add(s);
        }

        String filePath = "src/" + argument;
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath ))) {

            PreparedStatement prepareBusinessStm = connection.prepareStatement("INSERT into BUSINESS VALUES(?,?,?,?,?,?,?,?)");
            PreparedStatement preparedMainCatStm =  connection.prepareStatement("INSERT into BUSINESS_MAIN_CATEGORIES(business_id, main_category) VALUES(?,?)");
            PreparedStatement preparedSubCatStm =  connection.prepareStatement("INSERT into BUSINESS_SUB_CATEGORIES VALUES(?,?)");
            PreparedStatement preparedAttriStm =  connection.prepareStatement("INSERT into BUSINESS_ATTRIBUTES VALUES(?,?)");
            String line = buffer.readLine();

            while (line != null) {
                //transform every line of input to json object
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(line);

                // get business
                String business_id = (String)jsonObject.get("business_id");
                //get address
                String full_address = (String) jsonObject.get("full_address");
                //get hours
                JSONObject hours = (JSONObject) jsonObject.get("hours");
                //if open
                String open = "false";
                if ((Boolean)jsonObject.get("open")) {
                    open = "true";
                }
                JSONArray categories = (JSONArray)jsonObject.get("categories");
                String city = (String)jsonObject.get("city");
                String state = (String)jsonObject.get("state");
                double latitude = (double)jsonObject.get("latitude");
                double longitude = (double)jsonObject.get("longitude");
                long review_count = (long)jsonObject.get("review_count");
                String name = (String)jsonObject.get("name");
                double stars = (double)jsonObject.get("stars");
                JSONObject attributes = (JSONObject) jsonObject.get("attributes");

                //insert values to Business table
                prepareBusinessStm.setString (1, business_id);
                prepareBusinessStm.setString (2, full_address);
                prepareBusinessStm.setString (3, open);
                prepareBusinessStm.setString (4, city);
                prepareBusinessStm.setString (5, state);
                prepareBusinessStm.setLong (6, review_count);
                prepareBusinessStm.setString (7, name);
                prepareBusinessStm.setDouble (8, stars);
                prepareBusinessStm.addBatch();

                //insert categories
                for(int i = 0; i < categories.size(); i++) {
                    if (main_categories_map.contains(categories.get(i))) {
                        String mainCat = (String)categories.get(i);
                        preparedMainCatStm.setString(1, business_id);
                        preparedMainCatStm.setString(2, mainCat);
                        preparedMainCatStm.addBatch();
                    }
                    else {
                        String subCat = (String)categories.get(i);
                        preparedSubCatStm.setString(1, business_id);
                        preparedSubCatStm.setString(2, subCat);
                        preparedSubCatStm.addBatch();
                    }
                }

                //insert attributes
                Iterator iterator = attributes.keySet().iterator();
                while (iterator.hasNext()) {
                     String key = (String) iterator.next();
                     Object val = attributes.get(key);
                     if (val instanceof JSONObject) {
                         JSONObject innerAttributes = (JSONObject)val;
                         Iterator iterator2 = innerAttributes.keySet().iterator();
                         while (iterator2.hasNext()) {
                             String key2 = (String) iterator2.next();
                             String innerAttribute = (String) innerAttributes.get(key2).toString();
                             StringBuilder sb = new StringBuilder();
                             sb.append(key);
                             sb.append("_");
                             sb.append(key2);
                             sb.append("_");
                             sb.append(innerAttribute);
                             preparedAttriStm.setString(1, business_id);
                             preparedAttriStm.setString(2, sb.toString());
                             preparedAttriStm.addBatch();
                         }
                     }
                     else {
                         String outerAttribute = val.toString();
                         StringBuilder sb = new StringBuilder();
                         sb.append(key);
                         sb.append("_");
                         sb.append(outerAttribute);
                         preparedAttriStm.setString(1, business_id);
                         preparedAttriStm.setString(2, sb.toString());
                         preparedAttriStm.addBatch();
                     }
                }
                //read next line*/
                System.out.println("BUSINESS record prepare SUCCEED  " + count++);
                line = buffer.readLine();
            }
            prepareBusinessStm.executeBatch();
            preparedMainCatStm.executeBatch();
            preparedSubCatStm.executeBatch();
            preparedAttriStm.executeBatch();

            prepareBusinessStm.close();
            preparedMainCatStm.close();
            preparedSubCatStm.close();
            preparedAttriStm.close();
            System.out.println("BUSINESS records insert SUCCEED  ");

        } catch (IOException | ParseException | SQLException e ) {
            e.printStackTrace();
        }
    }

    private static void populateCheckin(Connection connection, String argument) {
        String filePath = "src/" + argument;
        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
            String line = buffer.readLine();
            while (line != null) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(line);

                //String checkin = (String) jsonObject.get("checkin_info");
                //String businessId = (String) jsonObject.get("business_id");


                line = buffer.readLine();
            }
        } catch (IOException | ParseException e ) {
            e.printStackTrace();
        }
    }
}
