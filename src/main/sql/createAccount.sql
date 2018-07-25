CREATE SEQUENCE ACCOUNT_SEQ start 1000000 increment 1;
CREATE TABLE ACCOUNT (
	"id" int8 NOT NULL PRIMARY KEY DEFAULT NEXTVAL('ACCOUNT_SEQ'),
	"name" varchar(255) NOT NULL,
	"passwdHash" varchar(255) NOT NULL
);

INSERT INTO ACCOUNT ("name", "passwdHash") VALUES
('kuba', '435d890f3693d48894b5a6ec3abfe4ca'),
('mariolka', '23cdc18507b52418db7740cbb5543e54'),
('michau', '435d890f3693d48894b5a6ec3abfe4ca')
;

SELECT * FROM account ORDER BY id;

ALTER TABLE public.account ADD COLUMN "hasNewHash" boolean DEFAULT false;

ALTER TABLE public.account ADD COLUMN "passwdNewHash" varchar(255);


--read https://www.postgresql.org/docs/9.3/static/pgcrypto.html#PGCRYPTO-HASH-SPEED-TABLE
--run as superuser
CREATE EXTENSION pgcrypto;

UPDATE ACCOUNT SET "passwdNewHash" = crypt("passwdHash", gen_salt('bf', 12));

--after each login
UPDATE ACCOUNT SET "passwdNewHash" = crypt('plainTextPasswd', gen_salt('bf', 12)), "hasNewHash" = true WHERE "id" = 1000000;

ALTER TABLE ACCOUNT RENAME "passwdHash" TO "staleOldPasswdHash";

--when everyone's happy
ALTER TABLE public.account DROP COLUMN "staleOldPasswdHash";

--when all old passwords removed
ALTER TABLE public.account DROP COLUMN "hasNewHash";



