ALTER TABLE url_entity ADD COLUMN processed BOOLEAN DEFAULT FALSE;
CREATE INDEX ix_ue_processed ON url_entity USING btree (processed);
CREATE INDEX ix_ue_expanded ON url_entity USING btree (expanded);
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_TWEETS BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_URLS_ORIGINAL BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_URLS_EXPANDED BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_URL_ERRORS BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ALTER COLUMN NAME SET NOT NULL;
ALTER TABLE URL_ENTITY ADD COLUMN creation_date date;
CREATE INDEX ix_ue_creation_date ON url_entity USING btree (creation_date);
INSERT INTO WEB_COLLECTION(ID, CREATION_DATE, DESCRIPTION, NAME, TOTAL_TWEETS, TOTAL_URLS_ORIGINAL, TOTAL_URLS_EXPANDED, TOTAL_URL_ERRORS) VALUES (1, NOW(), 'Bucket for URLs that cannot be associated with a Web Collection', 'UNKNOWN', 0, 0, 0, 0);
ALTER TABLE WEB_COLLECTION ADD COLUMN TOP_URL varchar(4000);
ALTER TABLE WEB_COLLECTION ADD COLUMN TOP_URL_TWEETS BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOP_URL_RETWEETS BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_TWEETS_PROCESSED BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_TWEETS_UNPROCESSED BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_URLS_PROCESSED BIGINT DEFAULT 0;
ALTER TABLE WEB_COLLECTION ADD COLUMN TOTAL_URLS_UNPROCESSED BIGINT DEFAULT 0;
update url_entity set processed = true from tweet where tweet.id = tweet_id and tweet.processed = true;
update url_entity set creation_date = tweet.creation_date from tweet where tweet.id = tweet_id;
update url_entity set popularity = retweet_count from tweet where retweet_count > 0 and tweet.id = tweet_id;
update tweet set processed = false from url_entity where tweet.id = tweet_id and web_collection_id is null;
update web_collection set total_tweets= 0, total_urls_original = 0, total_urls_expanded = 0, total_url_errors = 0, top_url = null, top_url_tweets = 0, top_url_retweets = 0, total_tweets_processed = 0, total_tweets_unprocessed = 0, total_urls_processed = 0, total_urls_unprocessed = 0;
-- set the processed fields to false for tweets and urls to ensure that the summary data held in the
-- web_collections table is generated
update tweet set processed = false;
update url_entity set processed = false;
ALTER TABLE URL_ENTITY ADD COLUMN FREQUENCY INTEGER DEFAULT 1;
update url_entity u set frequency = (select count(1) from url_entity where web_collection_id = u.web_collection_id and url_original = u.url_original);
CREATE INDEX ix_ue_frequency ON URL_ENTITY USING BTREE (frequency);

