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

CREATE TABLE CHECKIN (
	check_in Long,
	business_id VARCHAR(25) PRIMARY KEY,
	FOREIGN KEY(business_id) REFERENCES BUSINESS(business_id)
);


CREATE INDEX MAIN_CATEGORY_NAME_INDEX ON BUSINESS_MAIN_CATEGORIES(main_category);
CREATE INDEX MAIN_CATEGORY_ID_INDEX ON BUSINESS_MAIN_CATEGORIES(business_id);

CREATE INDEX SUB_CATEGORY_NAME_INDEX ON BUSINESS_SUB_CATEGORIES(sub_category);
CREATE INDEX SUB_CATEGORY_ID_INDEX ON BUSINESS_SUB_CATEGORIES(business_id);

CREATE INDEX BUSINESS_ARRTRIBUTES_INDEX ON BUSINESS_ATTRIBUTES(attributes);
CREATE INDEX BUSINESS_ATTRIBUTES_ID_INDEX ON BUSINESS_ATTRIBUTES(business_id);

CREATE INDEX REVIEW_BUSINESS_ID_INDEX ON REVIEW(business_id);
CREATE INDEX REVIEW_USER_ID_INDEX ON REVIEW(user_id);