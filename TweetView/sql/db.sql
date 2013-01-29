CREATE DATABASE "twittervane" WITH ENCODING='UTF8';
\c twittervane
CREATE ROLE usr_tv LOGIN PASSWORD 'password' NOINHERIT VALID UNTIL 'infinity';
grant all PRIVILEGES on DATABASE twittervane to usr_tv;