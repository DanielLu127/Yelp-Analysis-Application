import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class hw3 extends JFrame{
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

    ////////////////////////////////////////////////under business panel//////////////////////////
    //category panel
    private JPanel categoriesPanel = new JPanel();
    private JLabel categoryLabel = new JLabel();

    //main categories
    private JPanel mainCategoryPanel = new JPanel();
    private JLabel mainCategoryLabel = new JLabel();
    private JPanel mainCategoryListPanel = new JPanel();
    private JScrollPane mainCategoryScrollPane = new JScrollPane();
    private Button mainSelectButton = new Button("Select");

    //sub categories
    private JPanel subCategoryPanel = new JPanel();
    private JLabel subCategoryLabel = new JLabel();
    private JPanel subCategoryListPanel = new JPanel();
    private JScrollPane subCategoryScrollPane = new JScrollPane();
    private Button subSelectButton = new Button("Select");

    //attribute
    private JPanel attributePanel = new JPanel();;
    private JLabel attributeLabel= new JLabel();
    private JPanel attributeListPanel =  new JPanel();
    private JScrollPane attributeScrollPane = new JScrollPane();
    private Button attributeSelect = new Button("Select");

    //search panel
    private JPanel businessSearchPanel = new JPanel();

    //search text panel and checkbox
    String [] hint ={"Choose AND or OR for the search", "AND", "OR" };
    private JComboBox businessSearchComboBox = new JComboBox(hint);


    //////////////////////////////////////under review panel////////////////////////////////////////
    String symbol[]={"=, >, <", "=", "<", ">"};

    /////extra panel
    private JPanel extraPanelForReview = new JPanel();

    //////date
    private JPanel datePanel = new JPanel();
    private JLabel dateFromLabel = new JLabel("From");
    private JLabel dateToLabel = new JLabel("To");
    private JTextField dateFromTextField = new JTextField("YYYY-MM-DD");
    private JTextField dateToTextField = new JTextField("YYYY-MM-DD");

    ////stars
    private JPanel starsPanel = new JPanel();
    private JLabel starNameLabel = new JLabel("stars equal, smaller or greater ");
    private JLabel starValueLabel = new JLabel("stars value");
    private JComboBox  starsComboBox = new JComboBox(symbol);
    private JTextField starsTextField = new JTextField("");

    /////votes
    private JPanel votesPanel = new JPanel();
    private JLabel votesNameLabel = new JLabel("votes equal, smaller or greater ");
    private JLabel votesValueLabel = new JLabel("votes value");
    private JComboBox  votesComboBox = new JComboBox(symbol);
    private JTextField votesTextField = new JTextField("");

    ///////////////////////////////////under userPanel///////////////////////////
    private JPanel userAttributesPanel = new JPanel();
    private JPanel userSearchPanel = new JPanel();
    private JComboBox userSearchComboBox = new JComboBox(hint);

    /////memberSince
    private JPanel memberSincePanel = new JPanel();
    private JLabel memberSinceLabel = new JLabel("Member since");
    private JTextField memberSinceTextField = new JTextField("YYYY-MM-DD");


    ////reviewCnt
    private JPanel reviewCntPanel = new JPanel();
    private JLabel reviewCntLabel = new JLabel("Review         Count");
    private JComboBox  reviewCntComboBox = new JComboBox(symbol);
    private JTextField reviewCntTextField = new JTextField("");

    ////numFriends
    private JPanel numFriendsPanel = new JPanel();
    private JLabel numFriendsLabel = new JLabel("Number of Friends");
    private JComboBox  numFriendsComboBox = new JComboBox(symbol);
    private JTextField numFriendsTextField = new JTextField("");

    ///avgStars
    private JPanel avgStarsPanel = new JPanel();
    private JLabel avgStarsLabel = new JLabel("Average         stars");
    private JComboBox  avgStarsComboBox = new JComboBox(symbol);
    private JTextField avgStarsTextField = new JTextField("");

    //////numVotes
    private JPanel numVotesPanel = new JPanel();
    private JLabel numVotesLabel = new JLabel("Number   of  votes");
    private JComboBox  numVotesComboBox = new JComboBox(symbol);
    private JTextField numVotesTextField = new JTextField("");


    ////////////////////////////////////under result panel/////////
    private JPanel extraPanelForResult = new JPanel();
    private JPanel executePanel = new JPanel();
    private Button executeBusinessButton = new Button("Execute Business Search");
    private Button executeUserButton = new Button("Execute User Search");
    private JTable resultTableForBusiness = new JTable();
    private JTable resultTableForUser = new JTable();

    private JScrollPane resultScrollPane = new JScrollPane();

    /////////////////////hash sets for storing queried and selected categories and attributes//////////////////////////////

    private HashSet<String> queriedMainCat = new HashSet<>();
    private HashSet<String> selectedMainCat = new HashSet<>();
    private HashSet<String> queriedSubCat = new HashSet<>();
    private HashSet<String> selectedSubCat = new HashSet<>();
    private HashSet<String> queriedAttributes = new HashSet<>();
    private HashSet<String> selectedAttributes = new HashSet<>();

    private boolean ifUserTableListener = false;
    private boolean ifBusinessTableListener = false;



    ////////////////////user search methods///////////////////////////////
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


    ///////////////////business search methods///////////////////////

    private void businessSearch() {
        //System.out.println("selectedSize " + selectedMainCat.size());
        if (selectedMainCat.size() == 0 && selectedSubCat.size() == 0 && selectedAttributes.size() == 0) {
            JOptionPane.showMessageDialog(null, "Please select a Main Category");
            return;
        }
        else if (selectedSubCat.size() == 0 && selectedAttributes.size() == 0) {
            searchWithMainCat("execute");
        }
        else if (selectedAttributes.size() == 0) {
            searchWithSubCat("execute");
        }
        else {
            searchWithAttribute("execute");
        }
    }

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

    private void updateCategoryScreen(JPanel listPanel, HashSet querySet, HashSet selectedSet) {

        ArrayList<String> newList = new ArrayList<String>();
        Iterator setIterator = querySet.iterator();

        while(setIterator.hasNext()) {
            newList.add(setIterator.next().toString());
        }
        Collections.sort(newList);
        Iterator iterator = newList.iterator();

        while (iterator.hasNext()) {
            String attributeName = iterator.next().toString();
            JCheckBox checkBox = new JCheckBox(attributeName);

            checkBox.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    if (checkBox.isSelected()) {
                        selectedSet.add(checkBox.getText());
                        System.out.println("selected:  " + checkBox.getText());
                    }
                    if (!checkBox.isSelected()) {
                        selectedSet.remove(checkBox.getText());
                        System.out.println("removed:  " + checkBox.getText());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            listPanel.add(checkBox);
        }
        listPanel.updateUI();
    }

    //////////////////result display methods/////////////////////////

    private void displayResults(ResultSet results, String [] columnNames, JTable table, JPanel panel, String type)
            throws SQLException {

        results.last();
        int numRow = results.getRow();
        int numColumn = columnNames.length;
        String [][] data = new String [numRow][numColumn+1];

        System.out.println("Results table: Number of rows " + numRow + " " + numColumn);

        results.first();
        for(int i = 0; i < numRow; i++) {
            for (int j = 1; j < numColumn + 1; j++) {
                data[i][j - 1] = results.getString(j);
            }
            results.next();
        }

        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(defaultTableModel);
        table.setRowSelectionAllowed(true);

        //List<EventListener> listener = table.getListeners();
        //table.removeMouseListener(a);
        MouseAdapter a  = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    if (type == "user") {
                        String id = (String)table.getValueAt(table.getSelectedRow(), 4);
                        showDetail(type, id);
                    }
                    else if (type == "business") {
                        String id = (String)table.getValueAt(table.getSelectedRow(), 1);
                        showDetail(type, id);
                    }
                    else if (type == "user_review") {
                        String reviewText = (String)table.getValueAt(table.getSelectedRow(), 5);
                        showReviewText(reviewText);
                    }
                }
            }
        };

        if (table == resultTableForBusiness) {
            resultScrollPane.setViewportView(resultTableForBusiness);
        }
        else if (table == resultTableForUser) {
            resultScrollPane.setViewportView(resultTableForUser);
        }

        if (table == resultTableForBusiness && !ifBusinessTableListener) {
            table.addMouseListener(a);
            ifBusinessTableListener = true;
        }
        if (table == resultTableForUser && !ifUserTableListener) {
            table.addMouseListener(a);
            ifUserTableListener = true;
        }
        if (type == "user_review") {
            table.addMouseListener(a);
        }

        panel.updateUI();
    }

    private void showDetail(String type, String id) {

        JFrame detailFrame = new JFrame();
        JPanel detailPanel = new JPanel();
        JScrollPane detailScrollPane = new JScrollPane();
        JTable detailTable = new JTable();

        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailFrame.setSize(500, 600);
        detailFrame.setLayout(new GridLayout(1, 1));
        detailFrame.setVisible(true);

        detailPanel.setBackground(new Color(255, 255, 255));
        detailPanel.setPreferredSize(new Dimension(1200, 300));
        pack();


        String [] columns = new String[]{};
        StringBuilder sqlQuery = new StringBuilder();

        if (type == "user") {
            columns = new String[]{"Num Votes", "User Id", "Review Id", "Stars", "Review Date", "Review Text", "Business Id"};
            getReviewList(type, id, columns, sqlQuery);
        }
        else if (type == "business") {
            columns = new String[]{"Num Votes", "User Id", "Review Id", "Stars", "Review Date", "Review Text", "Business Id"};
            getReviewList(type, id, columns, sqlQuery);
        }


        try {
            Connection connection = getConnection();
            Statement queryStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = queryStm.executeQuery(sqlQuery.toString());
            displayResults(results, columns, detailTable, detailPanel, "user_review");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        detailScrollPane.setViewportView(detailTable);
        detailPanel.add(detailScrollPane);
        detailFrame.add(detailPanel);
        detailPanel.updateUI();
    }


    /////////////////two execute buttons methods//////////////////

    private void executeUserSearch() {
        executeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userLogicOperator = userSearchComboBox.getSelectedItem().toString();

                if (userLogicOperator == "Choose AND or OR for the search") {
                    JOptionPane.showMessageDialog(null, "Please select an operation: AND, OR");
                    return;
                }
                else {
                    userSearch();
                }
            }
        });
    }

    private void executeBusinessSearch() {
        executeBusinessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                businessSearch();
            }
        });
    }


    ///////////////////////utilities methods////////////////////

    private void showReviewText(String reviewText) {
        JFrame reviewTextFrame = new JFrame();
        JPanel reviewTextPanel = new JPanel();
        JScrollPane reviewTextScrollPane = new JScrollPane();
        JTextArea reviewTextBox = new JTextArea(50, 50);
        reviewTextBox.setText(reviewText);

        reviewTextFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reviewTextFrame.setSize(500, 600);
        reviewTextFrame.setLayout(new GridLayout(1, 1));
        reviewTextFrame.setVisible(true);
        reviewTextPanel.setBackground(new Color(255, 255, 255));
        reviewTextBox.setLineWrap(true);
        reviewTextBox.setWrapStyleWord(true);

        reviewTextScrollPane.setViewportView(reviewTextBox);
        reviewTextPanel.add(reviewTextScrollPane);
        reviewTextFrame.add(reviewTextPanel);
        pack();
    }

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

    private boolean isValidDate(String date) {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); //If you have a pattern and create a date object that strictly matches your pattern, set lenient to false

        try {
            sdf.parse(date);
        } catch (ParseException e) {

            if (date.equals("YYYY-MM-DD")) {
                return false;
            }

            JOptionPane.showMessageDialog(null, "The date is not valid, If you do not want search with member since condition, set date field to \"YYYY-MM-DD\"");
            return false;
        }
        return true;
    }

    private boolean isValidNumber(String number) {
        try {
            Double.parseDouble(number);
        } catch (NumberFormatException e){
            if (number.equals("")) {
                return false;
            }

            JOptionPane.showMessageDialog(null, "The number is not valid");
            return false;
        }

        return true;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName("oracle.jdbc.driver.OracleDriver");
        String oracleURL = "jdbc:oracle:thin:@localhost:1521:XE";
        Connection connection = DriverManager.getConnection(oracleURL, "system", "lxw1997127");

        return connection;
    }

    private void ifFirstCondition(Boolean firstCondition, StringBuilder sqlQuery, String userLogicOperator) {
        if (firstCondition) {
            sqlQuery.append("WHERE ");
        }
        else {
            sqlQuery.append(userLogicOperator);
        }

    }


    ////////////////////// Init UI creations /////////////////////
    private void businessPanel() {
        ////////////////////////business panel and label//////////////////////////////
        businessPanel.setBackground(new Color(119,136,153));
        businessPanel.setLayout(new BorderLayout());
        businessLabel.setText("Business");
        businessLabel.setHorizontalAlignment(SwingConstants.CENTER);
        businessPanel.add(businessLabel, BorderLayout.PAGE_START);
        mainPanel.add(businessPanel);

        //////categoriesPanel
        categoriesPanel.setLayout(new GridLayout(1, 3));
        categoriesPanel.setBackground(new Color(100, 20, 25));
        businessPanel.add(categoriesPanel, BorderLayout.CENTER);

        //////mainCategoryPanel
        mainCategoryPanel.setLayout(new BorderLayout());
        mainCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainCategoryLabel.setText("Main-Category");
        mainCategoryPanel.add(mainCategoryLabel, BorderLayout.PAGE_START);
        categoriesPanel.add(mainCategoryPanel);

        mainCategoryListPanel.setLayout(new GridLayout(0, 1));
        mainCategoryScrollPane.setViewportView(mainCategoryListPanel);
        mainCategoryPanel.add(mainCategoryScrollPane, BorderLayout.CENTER);
        mainCategoryPanel.add(mainSelectButton, BorderLayout.PAGE_END);

        //////subCategoryPanel
        subCategoryPanel.setLayout(new BorderLayout());
        subCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subCategoryLabel.setText("Sub-category");
        subCategoryPanel.add(subCategoryLabel, BorderLayout.PAGE_START);
        categoriesPanel.add(subCategoryPanel);

        subCategoryListPanel.setLayout(new GridLayout(0, 1));
        subCategoryScrollPane.setViewportView(subCategoryListPanel);
        subCategoryPanel.add(subCategoryScrollPane, BorderLayout.CENTER);
        subCategoryPanel.add(subSelectButton, BorderLayout.PAGE_END);

        //////attributePanel
        attributePanel.setLayout(new BorderLayout());
        attributeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        attributeLabel.setText("Attribute");
        attributePanel.add(attributeLabel, BorderLayout.PAGE_START);
        categoriesPanel.add(attributePanel);

        attributeListPanel.setLayout(new GridLayout(0, 1));
        attributeScrollPane.setViewportView(attributeListPanel);
        attributePanel.add(attributeScrollPane, BorderLayout.CENTER);

        /////search panel
        businessSearchPanel.setBackground(new Color(220, 220, 220));
        //searchPanel.setForeground(new Color(0, 102, 0));
        businessSearchComboBox.setBackground(new Color(220, 220, 220));

        GroupLayout searchLayout = new GroupLayout(businessSearchPanel);
        businessSearchPanel.setLayout(searchLayout);
        searchLayout.setAutoCreateGaps(true);
        searchLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = searchLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = searchLayout.createSequentialGroup();

        hGroup.addGroup(searchLayout.createParallelGroup().addComponent(businessSearchComboBox));
        searchLayout.setHorizontalGroup(hGroup);

        vGroup.addGroup(searchLayout.createParallelGroup().addComponent(businessSearchComboBox));
        searchLayout.setVerticalGroup(vGroup);

        businessPanel.add(businessSearchPanel, BorderLayout.PAGE_END);
    }

    private void reviewPanel() {
        ///////////////////////////////review panel//////////////////////////////////
        reviewPanel.setBackground(new Color(119,136,153));
        reviewPanel.setLayout(new BorderLayout());
        reviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        reviewLabel.setText("Review");
        reviewPanel.add(reviewLabel, BorderLayout.PAGE_START);
        mainPanel.add(reviewPanel);

        ///////extra panel for review
        extraPanelForReview.setLayout(new GridLayout(3, 1));
        extraPanelForReview.setBackground(new Color(234,23, 34));
        reviewPanel.add(extraPanelForReview, BorderLayout.CENTER);

        ///////date panel
        datePanel.setBackground(new Color(128,128,128));
        GroupLayout dateLayout = new GroupLayout(datePanel);
        datePanel.setLayout(dateLayout);
        dateLayout.setAutoCreateGaps(true);
        dateLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hhhGroup = dateLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vvvGroup = dateLayout.createSequentialGroup();

        hhhGroup.addGroup(dateLayout.createParallelGroup().
                addComponent(dateFromLabel).addComponent(dateFromTextField));
        hhhGroup.addGroup(dateLayout.createParallelGroup().
                addComponent(dateToLabel).addComponent(dateToTextField));
        dateLayout.setHorizontalGroup(hhhGroup);

        vvvGroup.addGroup(dateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(dateFromLabel).addComponent(dateToLabel));
        vvvGroup.addGroup(dateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(dateFromTextField).addComponent(dateToTextField));
        dateLayout.setVerticalGroup(vvvGroup);
        extraPanelForReview.add(datePanel);

        //////stars panel
        starsPanel.setBackground(new Color(169,169,169));

        GroupLayout startsLayout = new GroupLayout(starsPanel);
        starsPanel.setLayout(startsLayout);
        startsLayout.setAutoCreateGaps(true);
        startsLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = startsLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = startsLayout.createSequentialGroup();

        hGroup.addGroup(startsLayout.createParallelGroup().
                addComponent(starNameLabel).addComponent(starsComboBox));
        hGroup.addGroup(startsLayout.createParallelGroup().
                addComponent(starValueLabel).addComponent(starsTextField));
        startsLayout.setHorizontalGroup(hGroup);

        vGroup.addGroup(startsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(starNameLabel).addComponent(starValueLabel));
        vGroup.addGroup(startsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(starsComboBox).addComponent(starsTextField));
        startsLayout.setVerticalGroup(vGroup);

        extraPanelForReview.add(starsPanel);

        //////votes panel
        votesPanel.setBackground(new Color(128,128,128));
        GroupLayout votesLayout = new GroupLayout(votesPanel);
        votesPanel.setLayout(votesLayout);
        votesLayout.setAutoCreateGaps(true);
        votesLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hhGroup = votesLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vvGroup = votesLayout.createSequentialGroup();

        hhGroup.addGroup(votesLayout.createParallelGroup().
                addComponent(votesNameLabel).addComponent(votesComboBox));
        hhGroup.addGroup(votesLayout.createParallelGroup().
                addComponent(votesValueLabel).addComponent(votesTextField));
        votesLayout.setHorizontalGroup(hhGroup);

        vvGroup.addGroup(votesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(votesNameLabel).addComponent(votesValueLabel));
        vvGroup.addGroup(votesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(votesComboBox).addComponent(votesTextField));
        votesLayout.setVerticalGroup(vvGroup);
        extraPanelForReview.add(votesPanel);
    }

    private void userPanel() {
        //////////////////user panel///////////////////////////////
        userPanel.setBackground(new Color(119,136,153));
        userPanel.setLayout(new BorderLayout());
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setText("User");
        userPanel.add(userLabel, BorderLayout.PAGE_START);
        mainPanel.add(userPanel);

        ////////////////////userAttributesPanel
        userAttributesPanel.setLayout(new GridLayout(5, 1));
        userAttributesPanel.setBackground(new Color(10, 200, 225));
        userPanel.add(userAttributesPanel, BorderLayout.CENTER);

        ///////member since panel
        memberSincePanel.setBackground(new Color(128,128,128));
        GroupLayout memberSinceLayout = new GroupLayout(memberSincePanel);
        memberSincePanel.setLayout(memberSinceLayout);
        memberSinceLayout.setAutoCreateGaps(true);
        memberSinceLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup h_memberSinceGroup = memberSinceLayout.createSequentialGroup();
        GroupLayout.SequentialGroup v_memeberSinceGroup = memberSinceLayout.createSequentialGroup();

        h_memberSinceGroup.addGroup(memberSinceLayout.createParallelGroup().
                addComponent(memberSinceLabel).addComponent(memberSinceTextField));
        memberSinceLayout.setHorizontalGroup(h_memberSinceGroup);

        v_memeberSinceGroup.addGroup(memberSinceLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(memberSinceLabel));
        v_memeberSinceGroup.addGroup(memberSinceLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(memberSinceTextField));
        memberSinceLayout.setVerticalGroup(v_memeberSinceGroup);
        userAttributesPanel.add(memberSincePanel);

        //////review count panel
        reviewCntPanel.setBackground(new Color(169,169,169));
        GroupLayout reviewCntLayout = new GroupLayout(reviewCntPanel);
        reviewCntPanel.setLayout(reviewCntLayout);
        reviewCntLayout.setAutoCreateGaps(true);
        reviewCntLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup h_reviewCntGroup = reviewCntLayout.createSequentialGroup();
        GroupLayout.SequentialGroup v_reviewCntGroup = reviewCntLayout.createSequentialGroup();

        h_reviewCntGroup.addGroup(reviewCntLayout.createParallelGroup().
                addComponent(reviewCntLabel));
        h_reviewCntGroup.addGroup(reviewCntLayout.createParallelGroup().
                addComponent(reviewCntComboBox));
        h_reviewCntGroup.addGroup(reviewCntLayout.createParallelGroup().
                addComponent(reviewCntTextField));
        reviewCntLayout.setHorizontalGroup(h_reviewCntGroup);

        v_reviewCntGroup.addGroup(reviewCntLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(reviewCntLabel).addComponent(reviewCntComboBox).addComponent(reviewCntTextField));

        reviewCntLayout.setVerticalGroup(v_reviewCntGroup);
        userAttributesPanel.add(reviewCntPanel);

        /////number of friends panel
        numFriendsPanel.setBackground(new Color(100,100,140));
        GroupLayout numFriendsLayout = new GroupLayout(numFriendsPanel);
        numFriendsPanel.setLayout(numFriendsLayout);
        numFriendsLayout.setAutoCreateGaps(true);
        numFriendsLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup h_numFriendGroup = numFriendsLayout.createSequentialGroup();
        GroupLayout.SequentialGroup v_numFriendGroup = numFriendsLayout.createSequentialGroup();

        h_numFriendGroup.addGroup(numFriendsLayout.createParallelGroup().
                addComponent(numFriendsLabel));
        h_numFriendGroup.addGroup(numFriendsLayout.createParallelGroup().
                addComponent(numFriendsComboBox));
        h_numFriendGroup.addGroup(numFriendsLayout.createParallelGroup().
                addComponent(numFriendsTextField));
        numFriendsLayout.setHorizontalGroup(h_numFriendGroup);

        v_numFriendGroup.addGroup(numFriendsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(numFriendsLabel).addComponent(numFriendsComboBox).addComponent(numFriendsTextField));

        numFriendsLayout.setVerticalGroup(v_numFriendGroup);
        userAttributesPanel.add(numFriendsPanel);

        /////Average stars panel
        avgStarsPanel.setBackground(new Color(90,140,100));
        GroupLayout avgStarLayout = new GroupLayout(avgStarsPanel);
        avgStarsPanel.setLayout(avgStarLayout);
        avgStarLayout.setAutoCreateGaps(true);
        avgStarLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup h_avgStarGroup = avgStarLayout.createSequentialGroup();
        GroupLayout.SequentialGroup v_navgStarGroup = avgStarLayout.createSequentialGroup();

        h_avgStarGroup.addGroup(avgStarLayout.createParallelGroup().
                addComponent(avgStarsLabel));
        h_avgStarGroup.addGroup(avgStarLayout.createParallelGroup().
                addComponent(avgStarsComboBox));
        h_avgStarGroup.addGroup(avgStarLayout.createParallelGroup().
                addComponent(avgStarsTextField));
        avgStarLayout.setHorizontalGroup(h_avgStarGroup);

        v_navgStarGroup.addGroup(avgStarLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(avgStarsLabel).addComponent(avgStarsComboBox).addComponent(avgStarsTextField));

        avgStarLayout.setVerticalGroup(v_navgStarGroup);
        userAttributesPanel.add(avgStarsPanel);

        /////Number of votes panel
        numVotesPanel.setBackground(new Color(50,100,100));
        GroupLayout numVotesLayout = new GroupLayout(numVotesPanel);
        numVotesPanel.setLayout(numVotesLayout);
        numVotesLayout.setAutoCreateGaps(true);
        numVotesLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup h_numVotesGroup = numVotesLayout.createSequentialGroup();
        GroupLayout.SequentialGroup v_numVotesGroup = numVotesLayout.createSequentialGroup();

        h_numVotesGroup.addGroup(numVotesLayout.createParallelGroup().
                addComponent(numVotesLabel));
        h_numVotesGroup.addGroup(numVotesLayout.createParallelGroup().
                addComponent(numVotesComboBox));
        h_numVotesGroup.addGroup(numVotesLayout.createParallelGroup().
                addComponent(numVotesTextField));
        numVotesLayout.setHorizontalGroup(h_numVotesGroup);

        v_numVotesGroup.addGroup(numVotesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(numVotesLabel).addComponent(numVotesComboBox).addComponent(numVotesTextField));

        numVotesLayout.setVerticalGroup(v_numVotesGroup);
        userAttributesPanel.add(numVotesPanel);


        /////////////////////search panel
        userSearchPanel.setBackground(new Color(220, 220, 220));
        businessSearchComboBox.setBackground(new Color(255, 255, 255));

        GroupLayout searchLayout = new GroupLayout(userSearchPanel);
        userSearchPanel.setLayout(searchLayout);
        searchLayout.setAutoCreateGaps(true);
        searchLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup h_searchGroup = searchLayout.createSequentialGroup();
        GroupLayout.SequentialGroup v_searchGroup = searchLayout.createSequentialGroup();

        h_searchGroup.addGroup(searchLayout.createParallelGroup().addComponent(userSearchComboBox));
        searchLayout.setHorizontalGroup(h_searchGroup);

        v_searchGroup.addGroup(searchLayout.createParallelGroup().addComponent(userSearchComboBox));
        searchLayout.setVerticalGroup(v_searchGroup);

        userPanel.add(userSearchPanel, BorderLayout.PAGE_END);
    }

    private void resultPanel() {
        ////////////////////////////result panel//////////////////////////////////
        resultPanel.setBackground(new Color(119,136,153));
        resultPanel.setLayout(new BorderLayout());
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setText("Result");
        resultPanel.add(resultLabel, BorderLayout.PAGE_START);
        mainPanel.add(resultPanel);

        ///////////////////extraPanelForResult
        extraPanelForResult.setLayout(new BorderLayout());
        extraPanelForResult.setBackground(new Color(10, 200, 225));
        resultPanel.add(extraPanelForResult, BorderLayout.CENTER);
        //resultScrollPane.setViewportView(resultTableForBusiness);
        //resultScrollPane.setViewportView(resultTableForUser);
        resultPanel.add(resultScrollPane, java.awt.BorderLayout.CENTER);


        ////////////////execute panel
        executePanel.setLayout(new BorderLayout());
        executePanel.setBackground(new Color(100, 20, 225));

        GroupLayout executeLayout = new GroupLayout(executePanel);
        executePanel.setLayout(executeLayout);
        executeLayout.setAutoCreateGaps(true);
        executeLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = executeLayout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = executeLayout.createSequentialGroup();

        hGroup.addGroup(executeLayout.createParallelGroup().addComponent(executeBusinessButton));
        hGroup.addGroup(executeLayout.createParallelGroup().addComponent(executeUserButton));
        executeLayout.setHorizontalGroup(hGroup);

        vGroup.addGroup(executeLayout.createParallelGroup().addComponent(executeBusinessButton).addComponent(executeUserButton));
        //vGroup.addGroup(executeLayout.createParallelGroup().addComponent(executeUserButton));
        executeLayout.setVerticalGroup(vGroup);

        resultPanel.add(executePanel, BorderLayout.PAGE_END);
    }

    public hw3() {
        //set the frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        ///////main panel
        mainPanel.setBackground(new Color(0, 255, 255));
        //mainPanel.setToolTipText("Yelp");
        mainPanel.setPreferredSize(new Dimension(1300, 900));
        mainPanel.setLayout(new GridLayout(2, 2)); //divide the window with four equal parts

        businessPanel();
        reviewPanel();
        userPanel();
        resultPanel();

        GroupLayout layout = new GroupLayout(getContentPane());
        //getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();

        initMainCat();
        executeUserSearch();
        executeBusinessSearch();
    }

    public static void main(String args[]) {
        // JOptionPane.showMessageDialog(null, "Welcome to Yelp Search App, click ok to continue");
        new hw3().setVisible(true);
    }
}
