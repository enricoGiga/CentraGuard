--
-- PostgreSQL database dump
--

-- Dumped from database version 13.3 (Debian 13.3-1.pgdg100+1)
-- Dumped by pg_dump version 13.3 (Debian 13.3-1.pgdg100+1)
drop schema if exists public  cascade ;
create schema public;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


--
-- Name: role_type; Type: TYPE; Schema: public; Owner: enrico
--

CREATE TYPE public.role_type AS ENUM (
    'user',
);


ALTER TYPE public.role_type OWNER TO enrico;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: application_user; Type: TABLE; Schema: public; Owner: enrico
--

CREATE TABLE public.application_user (
    email character varying(60) NOT NULL,
    first_name character varying(60) NOT NULL,
    is_account_non_expired boolean NOT NULL,
    is_account_non_locked boolean NOT NULL,
    is_credentials_non_expired boolean NOT NULL,
    is_enabled boolean NOT NULL,
    last_name character varying(60) NOT NULL,
    password character varying(60) NOT NULL,
    CONSTRAINT application_user_email_check CHECK ((length((email)::text) >= 5)),
    CONSTRAINT application_user_first_name_check CHECK ((length((first_name)::text) >= 2)),
    CONSTRAINT application_user_last_name_check CHECK ((length((last_name)::text) >= 2)),
    CONSTRAINT application_user_password_check CHECK ((length((password)::text) >= 6))
);


ALTER TABLE public.application_user OWNER TO enrico;

--
-- Name: application_user_user_role; Type: TABLE; Schema: public; Owner: enrico
--

CREATE TABLE public.application_user_user_role (
    email character varying(60) NOT NULL,
    user_role_id integer NOT NULL,
    CONSTRAINT application_user_user_role_email_check CHECK ((length((email)::text) >= 5))
);


ALTER TABLE public.application_user_user_role OWNER TO enrico;

--
-- Name: granted_authority; Type: TABLE; Schema: public; Owner: enrico
--

CREATE TABLE public.granted_authority (
    granted_authority_id integer NOT NULL,
    authority character varying(20) NOT NULL,
    user_role_id integer
);


ALTER TABLE public.granted_authority OWNER TO enrico;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: enrico
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO enrico;

--
-- Name: reset_password_code; Type: TABLE; Schema: public; Owner: enrico
--

CREATE TABLE public.reset_password_code (
    user_email character varying(255) NOT NULL,
    reset_code integer,
    verified boolean DEFAULT false,
    CONSTRAINT reset_password_code_reset_code_check CHECK ((reset_code <= 99999999))
);


ALTER TABLE public.reset_password_code OWNER TO enrico;

--
-- Name: user_role; Type: TABLE; Schema: public; Owner: enrico
--

CREATE TABLE public.user_role (
    user_role_id integer NOT NULL,
    role character varying(20) NOT NULL
);


ALTER TABLE public.user_role OWNER TO enrico;

--
-- Name: verification_token; Type: TABLE; Schema: public; Owner: enrico
--

CREATE TABLE public.verification_token (
    id bigint NOT NULL,
    expiry_date timestamp without time zone,
    token character varying(255),
    email character varying(60) NOT NULL,
    CONSTRAINT verification_token_email_check CHECK ((length((email)::text) >= 5))
);


ALTER TABLE public.verification_token OWNER TO enrico;

--
-- Data for Name: application_user; Type: TABLE DATA; Schema: public; Owner: enrico
--

COPY public.application_user (email, first_name, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, last_name, password) FROM stdin;
enrico.gigante@gmail.com	enrico	t	t	t	t	gigante	$2a$10$nW.ZUD.4DKMA71Y2SulDfOSC1AstKKOplSel8AbTS36dSXkOc3B5i
\.


--
-- Data for Name: application_user_user_role; Type: TABLE DATA; Schema: public; Owner: enrico
--

COPY public.application_user_user_role (email, user_role_id) FROM stdin;
enrico.gigante@gmail.com	2
\.


--
-- Data for Name: granted_authority; Type: TABLE DATA; Schema: public; Owner: enrico
--

COPY public.granted_authority (granted_authority_id, authority, user_role_id) FROM stdin;
1	user:write	2
2	user:read	2
3	admin:write	1
4	admin:read	1
5	user:write	1
6	user:read	1
7	ROLE_STUDENT	2
8	ROLE_ADMIN	1
\.


--
-- Data for Name: reset_password_code; Type: TABLE DATA; Schema: public; Owner: enrico
--

COPY public.reset_password_code (user_email, reset_code, verified) FROM stdin;
\.


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: enrico
--

COPY public.user_role (user_role_id, role) FROM stdin;
1	ROLE_ADMIN
2	ROLE_USER
\.


--
-- Data for Name: verification_token; Type: TABLE DATA; Schema: public; Owner: enrico
--

COPY public.verification_token (id, expiry_date, token, email) FROM stdin;
1	2021-05-22 09:25:55.551	38278289-d596-46d7-9b75-466d4e22685a	enrico.gigante@gmail.com
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: enrico
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, true);


--
-- Name: application_user application_user_pkey; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.application_user
    ADD CONSTRAINT application_user_pkey PRIMARY KEY (email);


--
-- Name: application_user_user_role application_user_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.application_user_user_role
    ADD CONSTRAINT application_user_user_role_pkey PRIMARY KEY (email, user_role_id);


--
-- Name: granted_authority granted_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.granted_authority
    ADD CONSTRAINT granted_authority_pkey PRIMARY KEY (granted_authority_id);


--
-- Name: reset_password_code reset_password_code_pkey; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.reset_password_code
    ADD CONSTRAINT reset_password_code_pkey PRIMARY KEY (user_email);


--
-- Name: reset_password_code uk_eid1lv7gcudnbekgjcygtwtl2; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.reset_password_code
    ADD CONSTRAINT uk_eid1lv7gcudnbekgjcygtwtl2 UNIQUE (reset_code);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_role_id);


--
-- Name: verification_token verification_token_pkey; Type: CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.verification_token
    ADD CONSTRAINT verification_token_pkey PRIMARY KEY (id);


--
-- Name: application_user_user_role fk4i8nkqdgteb4vxhd9u7ldlcvd; Type: FK CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.application_user_user_role
    ADD CONSTRAINT fk4i8nkqdgteb4vxhd9u7ldlcvd FOREIGN KEY (email) REFERENCES public.application_user(email);


--
-- Name: application_user_user_role fkap4ebb3vi5csv81s1qodrjboq; Type: FK CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.application_user_user_role
    ADD CONSTRAINT fkap4ebb3vi5csv81s1qodrjboq FOREIGN KEY (user_role_id) REFERENCES public.user_role(user_role_id);


--
-- Name: reset_password_code fklup864uaba0hgjlvv2a1xghf9; Type: FK CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.reset_password_code
    ADD CONSTRAINT fklup864uaba0hgjlvv2a1xghf9 FOREIGN KEY (user_email) REFERENCES public.application_user(email);


--
-- Name: granted_authority fklya8mfvga6y26o23ip5wsjc2w; Type: FK CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.granted_authority
    ADD CONSTRAINT fklya8mfvga6y26o23ip5wsjc2w FOREIGN KEY (user_role_id) REFERENCES public.user_role(user_role_id);


--
-- Name: verification_token fkt1dhmmxfp1nn8trnodnqjy3kn; Type: FK CONSTRAINT; Schema: public; Owner: enrico
--

ALTER TABLE ONLY public.verification_token
    ADD CONSTRAINT fkt1dhmmxfp1nn8trnodnqjy3kn FOREIGN KEY (email) REFERENCES public.application_user(email);


--
-- PostgreSQL database dump complete
--

