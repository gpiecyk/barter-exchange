CREATE TABLE public.addresses (
    add_id bigint NOT NULL,
    add_audit_cd timestamp without time zone,
    add_audit_md timestamp without time zone,
    add_audit_rd timestamp without time zone,
    add_city character varying(45) NOT NULL,
    add_post_code character varying(45) NOT NULL,
    add_street character varying(200) NOT NULL
);


ALTER TABLE public.addresses OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16523)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16530)
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
    ofr_id bigint NOT NULL,
    ofr_audit_cd timestamp without time zone,
    ofr_audit_md timestamp without time zone,
    ofr_audit_rd timestamp without time zone,
    ofr_category character varying(255) NOT NULL,
    ofr_description character varying(500) NOT NULL,
    ofr_end_date timestamp without time zone,
    ofr_title character varying(255) NOT NULL,
    ofr_user_id_fk bigint NOT NULL
);


ALTER TABLE public.offers OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16538)
-- Name: pictures; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pictures (
    pic_id bigint NOT NULL,
    pic_audit_cd timestamp without time zone,
    pic_audit_md timestamp without time zone,
    pic_audit_rd timestamp without time zone,
    pic_ofr_id_fk bigint NOT NULL,
    pic_picture text NOT NULL
);


ALTER TABLE public.pictures OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16546)
-- Name: transaction_pictures; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction_pictures (
    trp_id bigint NOT NULL,
    trp_audit_cd timestamp without time zone,
    trp_audit_md timestamp without time zone,
    trp_audit_rd timestamp without time zone,
    trp_picture text NOT NULL,
    trp_trn_id_fk bigint NOT NULL
);


ALTER TABLE public.transaction_pictures OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16554)
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    trn_id bigint NOT NULL,
    trn_audit_cd timestamp without time zone,
    trn_audit_md timestamp without time zone,
    trn_audit_rd timestamp without time zone,
    trn_items_for_exchange character varying(500) NOT NULL,
    trn_message character varying(1000) NOT NULL,
    trn_ofr_id_fk bigint NOT NULL,
    trn_offering_user_id_fk bigint NOT NULL,
    trn_publisher_user_id_fk bigint NOT NULL,
    trn_status character varying(1) NOT NULL
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16562)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    usr_id bigint NOT NULL,
    usr_audit_cd timestamp without time zone,
    usr_audit_md timestamp without time zone,
    usr_audit_rd timestamp without time zone,
    usr_address_fk_id bigint NOT NULL,
    usr_email character varying(200) NOT NULL,
    usr_first_name character varying(45) NOT NULL,
    usr_last_name character varying(45) NOT NULL,
    usr_password character varying(300) NOT NULL,
    usr_phone_number character varying(45),
    usr_status character varying(1) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16567)
-- Name: verification_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.verification_token (
    vet_id bigint NOT NULL,
    vet_audit_cd timestamp without time zone,
    vet_audit_md timestamp without time zone,
    vet_audit_rd timestamp without time zone,
    vet_expiry_date timestamp without time zone NOT NULL,
    vet_token character varying(255) NOT NULL,
    vet_type character varying(255) NOT NULL,
    vet_user_id_fk bigint NOT NULL
);


ALTER TABLE public.verification_token OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16575)
-- Name: watch_list; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.watch_list (
    wal_id bigint NOT NULL,
    wal_audit_cd timestamp without time zone,
    wal_audit_md timestamp without time zone,
    wal_audit_rd timestamp without time zone,
    wal_ofr_id_fk bigint NOT NULL,
    wal_usr_id_fk bigint NOT NULL
);


ALTER TABLE public.watch_list OWNER TO postgres;

--
-- TOC entry 3022 (class 2606 OID 16529)
-- Name: addresses addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (add_id);


--
-- TOC entry 3024 (class 2606 OID 16537)
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (ofr_id);


--
-- TOC entry 3026 (class 2606 OID 16545)
-- Name: pictures pictures_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pictures
    ADD CONSTRAINT pictures_pkey PRIMARY KEY (pic_id);


--
-- TOC entry 3028 (class 2606 OID 16553)
-- Name: transaction_pictures transaction_pictures_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_pictures
    ADD CONSTRAINT transaction_pictures_pkey PRIMARY KEY (trp_id);


--
-- TOC entry 3030 (class 2606 OID 16561)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (trn_id);


--
-- TOC entry 3032 (class 2606 OID 16566)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (usr_id);


--
-- TOC entry 3034 (class 2606 OID 16574)
-- Name: verification_token verification_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_token
    ADD CONSTRAINT verification_token_pkey PRIMARY KEY (vet_id);


--
-- TOC entry 3036 (class 2606 OID 16579)
-- Name: watch_list watch_list_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.watch_list
    ADD CONSTRAINT watch_list_pkey PRIMARY KEY (wal_id);


--
-- TOC entry 3038 (class 2606 OID 16585)
-- Name: pictures fk1baivvpc8yafhs3oprf4lc7pj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pictures
    ADD CONSTRAINT fk1baivvpc8yafhs3oprf4lc7pj FOREIGN KEY (pic_ofr_id_fk) REFERENCES public.offers(ofr_id);


--
-- TOC entry 3042 (class 2606 OID 16605)
-- Name: transactions fkdokubjvoaau7v83xq3ir105go; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fkdokubjvoaau7v83xq3ir105go FOREIGN KEY (trn_publisher_user_id_fk) REFERENCES public.users(usr_id);


--
-- TOC entry 3039 (class 2606 OID 16590)
-- Name: transaction_pictures fke4aqa42talstx62w7gqgfnooo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_pictures
    ADD CONSTRAINT fke4aqa42talstx62w7gqgfnooo FOREIGN KEY (trp_trn_id_fk) REFERENCES public.transactions(trn_id);


--
-- TOC entry 3040 (class 2606 OID 16595)
-- Name: transactions fkfntp6do234rx0la045axa1h2y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fkfntp6do234rx0la045axa1h2y FOREIGN KEY (trn_ofr_id_fk) REFERENCES public.offers(ofr_id);


--
-- TOC entry 3044 (class 2606 OID 16615)
-- Name: verification_token fkjyj3w6hk3t0qtxsc9qbvx1ikv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_token
    ADD CONSTRAINT fkjyj3w6hk3t0qtxsc9qbvx1ikv FOREIGN KEY (vet_user_id_fk) REFERENCES public.users(usr_id);


--
-- TOC entry 3043 (class 2606 OID 16610)
-- Name: users fkmvdf3cgdcf30f57cwf2iagn96; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkmvdf3cgdcf30f57cwf2iagn96 FOREIGN KEY (usr_address_fk_id) REFERENCES public.addresses(add_id);


--
-- TOC entry 3041 (class 2606 OID 16600)
-- Name: transactions fkn61apfiemmlroc3ojtjyn3hyl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fkn61apfiemmlroc3ojtjyn3hyl FOREIGN KEY (trn_offering_user_id_fk) REFERENCES public.users(usr_id);


--
-- TOC entry 3045 (class 2606 OID 16620)
-- Name: watch_list fkndx5sjp5l20r08ha32amo17o9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.watch_list
    ADD CONSTRAINT fkndx5sjp5l20r08ha32amo17o9 FOREIGN KEY (wal_ofr_id_fk) REFERENCES public.offers(ofr_id);


--
-- TOC entry 3037 (class 2606 OID 16580)
-- Name: offers fko3illq7ob3bkwf78x32xhg1g7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fko3illq7ob3bkwf78x32xhg1g7 FOREIGN KEY (ofr_user_id_fk) REFERENCES public.users(usr_id);


--
-- TOC entry 3046 (class 2606 OID 16625)
-- Name: watch_list fkof702yfggiace4nlf93eln4hq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.watch_list
    ADD CONSTRAINT fkof702yfggiace4nlf93eln4hq FOREIGN KEY (wal_usr_id_fk) REFERENCES public.users(usr_id);