CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--create types
DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'role_type') THEN
            CREATE TYPE role_type AS enum
                (
                    'user'
                    );
        END IF;
    END
$$;