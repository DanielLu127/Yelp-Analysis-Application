# Yelp-Analysis-Application
# Overview
##  Basic Info
* use data from Yelp.com, implement a data analysis application
* tech stack: Java Swing + JDBC + Oracle database 11g
* data: business: 20000, reviews: 800000, users: 210000
##  Functionality 
### Init GUI
![在这里插入图片描述](https://img-blog.csdnimg.cn/779ec79c3f9c40d994939c9eefb47398.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
###  Simple Business Search
**choose main category and query for sub category,  choose sub category and query for attributes**
![在这里插入图片描述](https://img-blog.csdnimg.cn/fa479471b7ac4d10bcaa836521dd69eb.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
**clicking "execute business search" shows businesses that satisfied selected conditions**

**sub-category and attributes are optional**
![在这里插入图片描述](https://img-blog.csdnimg.cn/54ceabeab3ae4b548781153a814e240d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
**clicking a business in result，it will show all the reviews that the business recieved.
cliking a review will show the content of the review**
![在这里插入图片描述](https://img-blog.csdnimg.cn/451863353357441eac8fdc34d8efcd02.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
###  Simple User Search
**filter users based on yelp since, Review Count, friend count, rating，and votes**
![在这里插入图片描述](https://img-blog.csdnimg.cn/b2bf6025d17d498f988c963e19f54f6b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
**clicking "Execute User Search" shows user that satisfied selected conditions
clicking a user will show all the reviews that the user wrote，cliking a review will show the content of the review**
![在这里插入图片描述](https://img-blog.csdnimg.cn/cc4650e00a604542bfe4860004f408ef.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
### 筛选review
add conditions in review to screen reviews，(This user had 6 reviews，now it has one)
It also can screen reviews for business
![在这里插入图片描述](https://img-blog.csdnimg.cn/d06f14113edd49feb0a73bc4c96b56e0.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
This business had many reviews，now it has six
![在这里插入图片描述](https://img-blog.csdnimg.cn/b233881b0cf342faa28a660c83b2fd32.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
### OR AND
* business search and user search has AND or OR attriutes
* business search: main category, sub category, attributes can be AND or OR relation
* user search: the conditions for user search can be AND or OR
![在这里插入图片描述](https://img-blog.csdnimg.cn/275f3cb351344c03b7ce9e210f5abe27.png)
# Implementation
## JSON files --- source data
business.json
![在这里插入图片描述](https://img-blog.csdnimg.cn/1e7a6d310b854211987e1514e4238d60.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
user.json
![在这里插入图片描述](https://img-blog.csdnimg.cn/7c6d6ea4d06d47c88100151c5e7c1bf7.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
review.json
![在这里插入图片描述](https://img-blog.csdnimg.cn/b1b6169748a147cbb6fffd9a95294e8b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
##  create.db --- create tables

```sql
CREATE TABLE BUSINESS (
    business_id VARCHAR(25) PRIMARY KEY,
    full_address VARCHAR(200) NOT NULL,
	ifOpen  VARCHAR(5),
	city VARCHAR(50) NOT NULL,
	state_name VARCHAR(20) NOT NULL,
	review_count NUMBER,
	business_name VARCHAR(100) NOT NULL,
	starts NUMBER
);

CREATE TABLE BUSINESS_MAIN_CATEGORIES(
	business_id VARCHAR(25),
	main_category VARCHAR(50),
	PRIMARY KEY (business_id, main_category),
	FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id)
);

CREATE TABLE BUSINESS_SUB_CATEGORIES (
	business_id VARCHAR(25),
	sub_category VARCHAR(50),
	PRIMARY KEY (business_id, sub_category),
	FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id)
);

CREATE TABLE BUSINESS_ATTRIBUTES (
	business_id VARCHAR(25),
	attributes VARCHAR(50),
	PRIMARY KEY (business_id, attributes),
	FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id)
);

CREATE TABLE YELP_USERS ( 
	yelping_since DATE,
	num_votes NUMBER,
	review_count NUMBER,
	user_name VARCHAR(100),
	user_id VARCHAR(25) PRIMARY KEY,
	average_stars NUMBER,
	numFriend NUMBER
);

CREATE TABLE REVIEW (
	num_votes NUMBER,
	user_id VARCHAR(25) NOT NULL,
	review_id VARCHAR(25) PRIMARY KEY,
	stars NUMBER,
	review_date DATE,
	review_text Long,
	business_id VARCHAR(25) NOT NULL,
	FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id),
	FOREIGN KEY(user_id) REFERENCES YELP_USERS(user_id)
);

```

## Populate.Java --- populate data
![在这里插入图片描述](https://img-blog.csdnimg.cn/bc450a24602b4cc4ab67343d95262d78.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
###  JDBC connection
![在这里插入图片描述](https://img-blog.csdnimg.cn/5935c5178fe1460c977a5a558a626de1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)

### populate data from JSON files
**use simpleJson --- import org.json.simple.**

Example: review.json
```java
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
```

##  hw3.java --- GUI + SQL query
###  implement GUI
####  delcare swing compnents
Example: 
```java
    ///////////////////////////////////////////main panel/////////////////////////////////////
    private JPanel mainPanel = new JPanel();

    ///////////////////////////////////////////under main panel///////////////////////////////
    //////////business panel
    private JPanel businessPanel = new JPanel();
    private JLabel businessLabel = new JLabel();
    //////////review panel
    private JPanel reviewPanel = new JPanel();
    private JLabel reviewLabel = new JLabel();
    /////////user panel
    private JPanel userPanel = new JPanel();
    private JLabel userLabel = new JLabel();
    /////////result panel
    private JPanel resultPanel = new JPanel();
    private JLabel resultLabel = new JLabel();
```

#### init GUI

```java
//set the frame
setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
setResizable(false);
setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
setResizable(false);

///////main panel
mainPanel.setBackground(new Color(0, 255, 255
mainPanel.setPreferredSize(new Dimension(1300, 900));
mainPanel.setLayout(new GridLayout(2, 2)); //divide the window with four equal parts

businessPanel(); 
reviewPanel();
userPanel();
resultPanel();
```

###  Business Query
**use HashSet to store slected main categories, sub categories,attributes and queried main categories, sub categories,attributes**
```java
 private HashSet<String> queriedMainCat = new HashSet<>();
 private HashSet<String> selectedMainCat = new HashSet<>();
 private HashSet<String> queriedSubCat = new HashSet<>();
 private HashSet<String> selectedSubCat = new HashSet<>();
 private HashSet<String> queriedAttributes = new HashSet<>();
 private HashSet<String> selectedAttributes = new HashSet<>();
```

####  display Main Category 
After open the application, all the main category is shown and the query needed for this as shown below:

```sql
SELECT DISTINCT main_category, business_id
FROM BUSINESS_MAIN_CATEGORIES
```


```java
private void initMainCat()  {
        selectedMainCat.clear();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT DISTINCT main_category, business_id").append("\n")
                .append("FROM BUSINESS_MAIN_CATEGORIES\n");

        System.out.println("---------Prepare SQL query for main categories--------");
        System.out.println(sqlQuery);

        try(Connection connection = getConnection()) {
            //get all the main categories
            PreparedStatement queryMainCategoryStm = connection.prepareStatement(sqlQuery.toString());
            ResultSet results = queryMainCategoryStm.executeQuery();
            System.out.println("---------SQL query send--------");

            //add main categories to  queriedMainCat
            while (results.next()) {
                queriedMainCat.add(results.getString("main_category"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //display all the main categories and keep records of selected main categories
        updateCategoryScreen(mainCategoryListPanel, queriedMainCat, selectedMainCat);
        System.out.println("---------Display main categories--------\n");

        //listener for clicking the select button
        mainSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWithMainCat("select");
            }
        });
    }
```

####  search with main category
![在这里插入图片描述](https://img-blog.csdnimg.cn/1a4dd8c2185c45268012c6cf3c488a97.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
after clicking select, a query will be sent as shown below

```sql
SELECT DISTINCT sub_category
FROM BUSINESS_SUB_CATEGORIES b
WHERE b.business_id in (
	SELECT DISTINCT business_id 
	FROM BUSINESS_MAIN_CATEGORIES t
	WHERE t.main_category in ('Convenience Stores', 'Dentists')
	GROUP BY business_id
	HAVING COUNT(business_id) = 2
)
```


after clicking execute business search, a query will be sent as shown below

```sql
SELECT b.business_name, b.business_id, b.state_name, b.city, b.starts
FROM business b
WHERE b.business_id in (
	SELECT DISTINCT business_id 
	FROM BUSINESS_MAIN_CATEGORIES t
	WHERE t.main_category in ('Convenience Stores', 'Dentists')
	GROUP BY business_id
	HAVING COUNT(business_id) = 2
)
```
Implementation

```java
private void searchWithMainCat(String clickType) {
        if (businessSearchComboBox.getSelectedItem() == "Choose AND or OR for the search") {
            JOptionPane.showMessageDialog(null, "Please select an operation: AND, OR");
            return;
        }

        queriedSubCat.clear();
        subCategoryListPanel.removeAll();

        StringBuilder sqlQuery = new StringBuilder();
        searchWithMainCategorySQL(sqlQuery, clickType);

        try(Connection connection = getConnection()) {

            Statement queryUserStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet results = queryUserStm.executeQuery(sqlQuery.toString());

            if (clickType == "execute") {
                String[] columns = new String[]{"Business_name", "Business_id", "State_name", "City", "Stars"};
                displayResults(results, columns, resultTableForBusiness, extraPanelForResult, "business");
            }
            else {
                while (results.next()) {
                    String subCategory = results.getString("sub_category");
                    queriedSubCat.add(subCategory);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //display all the sub categories and keep records of selected sub categories
        updateCategoryScreen(subCategoryListPanel, queriedSubCat, selectedSubCat);
        System.out.println("---------Display sub categories--------");

        //listener for subSelectButton
        subSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchWithSubCat("select");
            }
        });

    }
```

```java
 private void searchWithMainCategorySQL(StringBuilder sqlQuery, String clickType) {
        if (clickType == "execute") {
            sqlQuery.append("SELECT b.business_name, b.business_id, b.state_name, b.city, b.starts\n")
                    .append("FROM business b").append("\n");
        }
        else if (clickType == "select") {
            selectedSubCat.clear();
            selectedAttributes.clear();
            System.out.println("selected sub category cleared");
            System.out.println("selected attributes cleared\n");

            sqlQuery.append("SELECT DISTINCT sub_category\n")
                    .append("FROM BUSINESS_SUB_CATEGORIES b").append("\n");
        }

        sqlQuery.append("WHERE b.business_id in (\n");
        sqlTemplate(sqlQuery, "mainCategory", selectedMainCat);
        sqlQuery.append("\n)");

        System.out.println(sqlQuery);
        System.out.println("---------SQL query send--------" + "\n");
    }
```

####  search with sub category
![在这里插入图片描述](https://img-blog.csdnimg.cn/880d3ada02ad4b629e6768db779b7711.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
after clicking select, a query will be sent as shown below

```sql
SELECT DISTINCT attributes
FROM BUSINESS_ATTRIBUTES b
WHERE b.business_id in (
	SELECT DISTINCT business_id 
	FROM BUSINESS_MAIN_CATEGORIES t
	WHERE t.main_category in ('Convenience Stores', 'Dentists')

 	INTERSECT 
 	
	SELECT DISTINCT business_id 
	FROM BUSINESS_SUB_CATEGORIES t
	WHERE t.sub_category in ('Day Spas', 'Cosmetic Dentists', 'Endodontists')
)
```

after clicking execute business search, a query will be sent as shown below

```sql
SELECT b.business_name, b.business_id, b.state_name, b.city, b.starts
FROM business b
WHERE b.business_id in (
	SELECT DISTINCT business_id 
	FROM BUSINESS_MAIN_CATEGORIES t
	WHERE t.main_category in ('Convenience Stores', 'Dentists')

 	INTERSECT 
 	
	SELECT DISTINCT business_id 
	FROM BUSINESS_SUB_CATEGORIES t
	WHERE t.sub_category in ('Day Spas', 'Cosmetic Dentists', 'Endodontists')
)
```

Implementation

```java
private void searchWithSubCat(String clickType) {
        queriedAttributes.clear();
        attributeListPanel.removeAll();
        StringBuilder sqlQuery = new StringBuilder();

        searchWithSubCategorySQL(sqlQuery, clickType);

        System.out.println(sqlQuery.toString());

        try(Connection connection = getConnection()) {

            Statement queryUserStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet results = queryUserStm.executeQuery(sqlQuery.toString());

            System.out.println("---------SQL query send--------");

            if (clickType == "execute") {
                String[] columns = new String[]{"Business_name", "Business_id", "State_name", "City", "Stars"};
                displayResults(results, columns, resultTableForBusiness, extraPanelForResult, "business");
            }
            else {
                while (results.next()) {
                    String attributes = results.getString("attributes");
                    queriedAttributes.add(attributes);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //display all the attributes and keep records of selected attributes
        updateCategoryScreen(attributeListPanel, queriedAttributes, selectedAttributes);
    }
```

```java
 private void searchWithSubCategorySQL (StringBuilder sqlQuery, String clickType) {
        if (clickType == "execute") {
            sqlQuery.append("SELECT b.business_name, b.business_id, b.state_name, b.city, b.starts\n")
                    .append("FROM business b").append("\n");
        }
        else if (clickType == "select") {
            selectedAttributes.clear();
            System.out.println("selected attributes cleared\n");
            sqlQuery.append("SELECT DISTINCT attributes\n")
                    .append("FROM BUSINESS_ATTRIBUTES b").append("\n");
        }

        sqlQuery.append("WHERE b.business_id in (\n");
        sqlTemplate(sqlQuery, "mainCategory", selectedMainCat);
        sqlQuery.append("\n INTERSECT \n");
        sqlTemplate(sqlQuery, "subCategory", selectedSubCat);
        sqlQuery.append("\n)");

        System.out.println(sqlQuery);
        System.out.println("---------SQL query send--------" + "\n");
    }
```

####  search with attributes
![在这里插入图片描述](https://img-blog.csdnimg.cn/e870108564bd4faaa46c5333773a181b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)

after clicking execute business search, a query will be sent as shown below

```sql
SELECT b.business_name, b.business_id, b.state_name, b.city, b.starts
FROM business b
WHERE b.business_id in (
	SELECT DISTINCT business_id 
	FROM BUSINESS_MAIN_CATEGORIES t
	WHERE t.main_category in ('Convenience Stores', 'Dentists')
	GROUP BY business_id
	HAVING COUNT(business_id) = 2
	
	 INTERSECT 
	 
	SELECT DISTINCT business_id 
	FROM BUSINESS_SUB_CATEGORIES t
	WHERE t.sub_category in ('Day Spas', 'Cosmetic Dentists', 'Endodontists')
	GROUP BY business_id
	HAVING COUNT(business_id) = 3
	
	 INTERSECT 
	 
	SELECT DISTINCT business_id 
	FROM BUSINESS_ATTRIBUTES t
	WHERE t.attributes in ('Parking_lot_false', 'Parking_validated_false', 'Parking_street_false')
	GROUP BY business_id
	HAVING COUNT(business_id) = 3
)
```

Implementation

```java
private void searchWithAttribute(String clickType) {
        StringBuilder sqlQuery = new StringBuilder();
        searchWithAttributesSQL(sqlQuery, clickType);

        try (Connection connection = getConnection()) {
            Statement queryUserStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet results = queryUserStm.executeQuery(sqlQuery.toString());

            String[] columns = new String[]{"Business_name", "Business_id", "State_name", "City", "Stars"};
            displayResults(results, columns, resultTableForBusiness, extraPanelForResult, "business");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
```

```java
private void searchWithAttributesSQL(StringBuilder sqlQuery, String clickType) {
        if (clickType == "execute") {
            sqlQuery.append("SELECT b.business_name, b.business_id, b.state_name, b.city, b.starts\n")
                    .append("FROM business b").append("\n");
        }

        sqlQuery.append("WHERE b.business_id in (\n");
        sqlTemplate(sqlQuery, "mainCategory", selectedMainCat);
        sqlQuery.append("\n INTERSECT \n");
        sqlTemplate(sqlQuery, "subCategory", selectedSubCat);
        sqlQuery.append("\n INTERSECT \n");
        sqlTemplate(sqlQuery, "attributes", selectedAttributes);
        sqlQuery.append("\n)");

        System.out.println(sqlQuery);
        System.out.println("---------SQL query send--------" + "\n");
    }
```

####  common part for building all SQL queries

```java
private void sqlTemplate(StringBuilder sqlQuery, String filterType, HashSet<String> selectedItems) {
        String tableName = "";
        String elementName = "";

        if (filterType == "mainCategory") {
            tableName = "BUSINESS_MAIN_CATEGORIES t";
            elementName = "main_category";
        }
        else if (filterType == "subCategory") {
            tableName = "BUSINESS_SUB_CATEGORIES t";
            elementName = "sub_category";
        }
        else if (filterType == "attributes") {
            tableName = "BUSINESS_ATTRIBUTES t";
            elementName = "attributes";
        }

        Iterator it = selectedItems.iterator();
        StringBuilder selectedItemString = new StringBuilder();
        while (it.hasNext()) {
            String item = it.next().toString();
            item = item.replace("'", "''");
            if (it.hasNext()) {
                selectedItemString.append("'").append(item).append("'").append(",").append(" ");
            }
            else {
                selectedItemString.append("'").append(item).append("'");
            }
        }

        int count = selectedItems.size();

        sqlQuery.append("SELECT DISTINCT business_id \n");
        sqlQuery.append("FROM ").append(tableName).append("\n");
        sqlQuery.append("WHERE ").append("t.").append(elementName).append(" in ")
                .append("(");
        sqlQuery.append(selectedItemString.toString());
        sqlQuery.append(")\n");

        if (businessSearchComboBox.getSelectedItem() == "AND") {
            sqlQuery.append("GROUP BY business_id\n");
            sqlQuery.append("HAVING COUNT(business_id) = ").append(String.valueOf(count));
        }
    }
```

###  User Query
![在这里插入图片描述](https://img-blog.csdnimg.cn/2e583c89e98a4c689b5a328affa5fab6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)
after clicking "Execute User Search" a query will be sent

```sql
SELECT *
FROM YELP_USERS
WHERE  review_count >10
OR numFriend <100
OR average_stars >2
OR num_votes >10
```
Implementation

```java
 private void userSearch() {
        String userLogicOperator = userSearchComboBox.getSelectedItem().toString();

        String memberSince = memberSinceTextField.getText();

        String reviewCntOperator = reviewCntComboBox.getSelectedItem().toString();
        String reviewCntValue = reviewCntTextField.getText();

        String numFriendOpertor = numFriendsComboBox.getSelectedItem().toString();
        String numFriendsValue = numFriendsTextField.getText();

        String avgStarsOperator = avgStarsComboBox.getSelectedItem().toString();
        String avgStarValue = avgStarsTextField.getText();

        String numVotesOperator = numVotesComboBox.getSelectedItem().toString();
        String numVotesValue = numVotesTextField.getText();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT *").append("\n")
                .append("FROM YELP_USERS").append("\n");

        boolean firstCondition = true;

        if (isValidDate(memberSince)) {
            ifFirstCondition(firstCondition, sqlQuery, userLogicOperator);

            sqlQuery.append("yelping_since").append(" >= ").append("TO_DATE('").append(memberSince).append("','")
                    .append("YYYY-MM-DD')")
                    .append("\n");

            firstCondition = false;
        }
        if (reviewCntOperator != "=, >, <" && isValidNumber(reviewCntValue)) {
            ifFirstCondition(firstCondition, sqlQuery, userLogicOperator);

            sqlQuery.append(" review_count ")
                    .append(reviewCntOperator).append(reviewCntValue).append("\n");

            firstCondition = false;
        }
        if (numFriendOpertor != "=, >, <" && isValidNumber(numFriendsValue)) {
            ifFirstCondition(firstCondition, sqlQuery, userLogicOperator);

            sqlQuery.append(" numFriend ")
                    .append(numFriendOpertor).append(numFriendsValue).append("\n");

            firstCondition = false;
        }
        if (avgStarsOperator != "=, >, <" && isValidNumber(avgStarValue)) {
            ifFirstCondition(firstCondition, sqlQuery, userLogicOperator);

            sqlQuery.append(" average_stars ")
                    .append(avgStarsOperator).append(avgStarValue).append("\n");

            firstCondition = false;
        }
        if (numVotesOperator != "=, >, <" && isValidNumber(numVotesValue )) {
            ifFirstCondition(firstCondition, sqlQuery, userLogicOperator);

            sqlQuery.append(" num_votes ")
                    .append(numVotesOperator).append(numVotesValue);

            firstCondition = false;
        }

        System.out.println(sqlQuery.toString());


        //display the results
        try (Connection connection = getConnection()){

            Statement queryUserStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet results = queryUserStm.executeQuery(sqlQuery.toString());

            String [] columns = new String [] {"Joined Date", "Num Votes", "Review Cnt",
                    "User Name", "User Id", "Avg Stars", "Num Friends"};

            displayResults(results, columns, resultTableForUser, extraPanelForResult, "user");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
```

###  Review Query
```sql
SELECT *
FROM REVIEW
WHERE user_id = 'uygg55wWaEP0xTR7dHDg-g'
AND ( stars >2
OR num_votes <100
)
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/01245db2744d4895bd0645968a65bea8.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBARDAxMDAxMTAxMQ==,size_20,color_FFFFFF,t_70,g_se,x_16)

Implementation

```java
private StringBuilder getReviewList(String type, String id, String [] columns, StringBuilder sqlQuery) {

        String idType = "";
        String logicOperator = "";
        String dateFrom = dateFromTextField.getText();
        String dateTo = dateToTextField.getText();
        String reviewStars = starsTextField.getText();
        String reviewStarsOperator = starsComboBox.getSelectedItem().toString();
        String reviewVotes = votesTextField.getText();
        String reviewVotesOperator = votesComboBox.getSelectedItem().toString();

        if (type == "business") {
            logicOperator = businessSearchComboBox.getSelectedItem().toString();
            idType  = "business_id";
        }
        else if (type == "user") {
            logicOperator = userSearchComboBox.getSelectedItem().toString();
            idType  = "user_id";
        }

        sqlQuery.append("SELECT *").append("\n")
                .append("FROM REVIEW").append("\n")
                .append("WHERE ").append(idType).append(" = '").append(id).append("'\n");


        boolean firstCondition = true;

        if (isValidDate(dateFrom)) {
            if (firstCondition) {
                sqlQuery.append("AND (");
            }
            else  {
                sqlQuery.append(logicOperator);
            }

            sqlQuery.append(" review_date").append(" >= ")
                    .append(" TO_DATE('").append(dateFrom).append("','").append("YYYY-MM-DD')")
                    .append("\n");

            firstCondition =  false;
        }
        if (isValidDate(dateTo)) {
            if (firstCondition) {
                sqlQuery.append("AND (");
            }
            else  {
                sqlQuery.append(logicOperator);
            }

            sqlQuery.append(" review_date").append(" <= ")
                    .append(" TO_DATE('").append(dateTo).append("','").append("YYYY-MM-DD')")
                    .append("\n");

            firstCondition =  false;
        }
        if (reviewStarsOperator != "=, >, <" && isValidNumber(reviewStars)) {
            if (firstCondition) {
                sqlQuery.append("AND (");
            }
            else  {
                sqlQuery.append(logicOperator);
            }

            sqlQuery.append(" stars ")
                    .append(reviewStarsOperator).append(reviewStars).append("\n");

            firstCondition =  false;
        }
        if (reviewVotesOperator != "=, >, <" && isValidNumber(reviewVotes)) {
            if (firstCondition) {
                sqlQuery.append("AND (");
            }
            else  {
                sqlQuery.append(logicOperator);
            }

            sqlQuery.append(" num_votes ")
                    .append(reviewVotesOperator).append(reviewVotes).append("\n");

            firstCondition =  false;
        }

        if (!firstCondition) {
            sqlQuery.append(")");
        }
        System.out.println(sqlQuery.toString());

        return sqlQuery;
    }
```

 
