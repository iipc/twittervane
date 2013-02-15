--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: app_config; Type: TABLE; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE TABLE app_config (
    id integer NOT NULL,
    access_token character varying(255),
    access_token_secret character varying(255),
    bitly_api_key character varying(255),
    bitly_login character varying(255),
    consumer_key character varying(255),
    consumer_secret character varying(255),
    http_proxy_host character varying(255),
    http_proxy_pass character varying(255),
    http_proxy_port character varying(255),
    http_proxy_user character varying(255)
);


ALTER TABLE public.app_config OWNER TO usr_tv;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: usr_tv
--

CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO usr_tv;

--
-- Name: search_term; Type: TABLE; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE TABLE search_term (
    id bigint NOT NULL,
    term character varying(255),
    web_collection_id bigint
);


ALTER TABLE public.search_term OWNER TO usr_tv;

--
-- Name: tweet; Type: TABLE; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE TABLE tweet (
    id bigint NOT NULL,
    creation_date date,
    name character varying(255),
    processed boolean,
    retweet_count bigint,
    text character varying(255),
    tweetid bigint,
    twitter_date character varying(255)
);


ALTER TABLE public.tweet OWNER TO usr_tv;

--
-- Name: url_entity; Type: TABLE; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE TABLE url_entity (
    id bigint NOT NULL,
    errors character varying(1000),
    expanded boolean,
    popularity bigint,
    total_retweets bigint,
    total_tweets bigint,
    url_domain character varying(4000),
    url_full character varying(4000),
    url_original character varying(4000),
    tweet_id bigint,
    web_collection_id bigint
);


ALTER TABLE public.url_entity OWNER TO usr_tv;

--
-- Name: web_collection; Type: TABLE; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE TABLE web_collection (
    id bigint NOT NULL,
    creation_date timestamp without time zone,
    description character varying(255),
    end_date timestamp without time zone,
    name character varying(255),
    start_date timestamp without time zone
);


ALTER TABLE public.web_collection OWNER TO usr_tv;

--
-- Name: app_config_pkey; Type: CONSTRAINT; Schema: public; Owner: usr_tv; Tablespace: 
--

ALTER TABLE ONLY app_config
    ADD CONSTRAINT app_config_pkey PRIMARY KEY (id);


--
-- Name: search_term_pkey; Type: CONSTRAINT; Schema: public; Owner: usr_tv; Tablespace: 
--

ALTER TABLE ONLY search_term
    ADD CONSTRAINT search_term_pkey PRIMARY KEY (id);


--
-- Name: tweet_pkey; Type: CONSTRAINT; Schema: public; Owner: usr_tv; Tablespace: 
--

ALTER TABLE ONLY tweet
    ADD CONSTRAINT tweet_pkey PRIMARY KEY (id);


--
-- Name: url_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: usr_tv; Tablespace: 
--

ALTER TABLE ONLY url_entity
    ADD CONSTRAINT url_entity_pkey PRIMARY KEY (id);


--
-- Name: web_collection_pkey; Type: CONSTRAINT; Schema: public; Owner: usr_tv; Tablespace: 
--

ALTER TABLE ONLY web_collection
    ADD CONSTRAINT web_collection_pkey PRIMARY KEY (id);


--
-- Name: ix_search_term_wci; Type: INDEX; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE INDEX ix_search_term_wci ON search_term USING btree (web_collection_id);


--
-- Name: ix_tweet_processed; Type: INDEX; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE INDEX ix_tweet_processed ON tweet USING btree (processed);


--
-- Name: ix_url_entity_ti; Type: INDEX; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE INDEX ix_url_entity_ti ON url_entity USING btree (tweet_id);


--
-- Name: ix_url_entity_uf; Type: INDEX; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE INDEX ix_url_entity_uf ON url_entity USING btree (url_full);


--
-- Name: ix_url_entity_uo; Type: INDEX; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE INDEX ix_url_entity_uo ON url_entity USING btree (url_original);


--
-- Name: ix_url_entity_wci; Type: INDEX; Schema: public; Owner: usr_tv; Tablespace: 
--

CREATE INDEX ix_url_entity_wci ON url_entity USING btree (web_collection_id);


--
-- Name: fkd316f0b3886a36e6; Type: FK CONSTRAINT; Schema: public; Owner: usr_tv
--

ALTER TABLE ONLY url_entity
    ADD CONSTRAINT fkd316f0b3886a36e6 FOREIGN KEY (web_collection_id) REFERENCES web_collection(id);


--
-- Name: fkd316f0b3c4e3cc3d; Type: FK CONSTRAINT; Schema: public; Owner: usr_tv
--

ALTER TABLE ONLY url_entity
    ADD CONSTRAINT fkd316f0b3c4e3cc3d FOREIGN KEY (tweet_id) REFERENCES tweet(id);


--
-- Name: fkdfdbe0a3886a36e6; Type: FK CONSTRAINT; Schema: public; Owner: usr_tv
--

ALTER TABLE ONLY search_term
    ADD CONSTRAINT fkdfdbe0a3886a36e6 FOREIGN KEY (web_collection_id) REFERENCES web_collection(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
GRANT USAGE ON SCHEMA public TO usr_tv;


--
-- PostgreSQL database dump complete
--

