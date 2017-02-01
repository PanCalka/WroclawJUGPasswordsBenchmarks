create extension pgcrypto;
select gen_salt('bf', 12);
select crypt('P@ssw0rd!', gen_salt('bf', 12));
select crypt('P@ssw0rd!', '$2a$12$OvLMY.L6vQecUscA5GnWZeS7MhbdTHns2oHtz/UGZFP3WRVlVYCoK');

-- but don't trust https://www.postgresql.org/docs/9.3/static/pgcrypto.html#PGCRYPTO-HASH-SPEED-TABLE
