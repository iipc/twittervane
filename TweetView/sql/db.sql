CREATE DATABASE "twittervane" WITH ENCODING='UTF8';
\c twittervane
CREATE ROLE usr_tv LOGIN PASSWORD 'password'
	NOINHERIT
		VALID UNTIL 'infinity';
grant usage on schema public to usr_tv;