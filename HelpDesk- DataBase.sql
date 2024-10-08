PGDMP  /                    |            CDLTEST    16.2    16.2 W               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                        0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            !           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            "           1262    19255    CDLTEST    DATABASE     �   CREATE DATABASE "CDLTEST" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE "CDLTEST";
                postgres    false            �            1259    19501    address    TABLE     �  CREATE TABLE public.address (
    address_id bigint NOT NULL,
    city character varying(255),
    country character varying(255),
    created_date date,
    createdby character varying(255),
    district character varying(255),
    house_no character varying(255),
    landmark character varying(255),
    pincode integer,
    state character varying(255),
    street character varying(255),
    updated_date date,
    updatedby character varying(255),
    employee_id bigint
);
    DROP TABLE public.address;
       public         heap    postgres    false            �            1259    19500    address_address_id_seq    SEQUENCE        CREATE SEQUENCE public.address_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.address_address_id_seq;
       public          postgres    false    216            #           0    0    address_address_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.address_address_id_seq OWNED BY public.address.address_id;
          public          postgres    false    215            �            1259    19519    employee    TABLE     �  CREATE TABLE public.employee (
    emp_id bigint NOT NULL,
    about_me character varying(255),
    contact_primary character varying(255),
    contact_secondary character varying(255),
    created_date date,
    createdby character varying(255),
    date_of_birth date,
    date_of_joining date,
    designation character varying(255),
    email_id character varying(255),
    emp_first_name character varying(255),
    emp_last_name character varying(255),
    password character varying(255),
    permissions character varying(255),
    reporting_manager character varying(255),
    roles character varying(255),
    sap_entry_id bigint,
    status character varying(255),
    updated_date date,
    updatedby character varying(255),
    user_id bigint,
    location_id bigint,
    description character varying(255),
    employee_organisation character varying(255),
    main_department_id bigint
);
    DROP TABLE public.employee;
       public         heap    postgres    false            �            1259    19518    employee_emp_id_seq    SEQUENCE     |   CREATE SEQUENCE public.employee_emp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.employee_emp_id_seq;
       public          postgres    false    218            $           0    0    employee_emp_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.employee_emp_id_seq OWNED BY public.employee.emp_id;
          public          postgres    false    217            �            1259    19528    location    TABLE     �  CREATE TABLE public.location (
    location_id bigint NOT NULL,
    city character varying(255),
    country character varying(255),
    created_date date,
    createdby character varying(255),
    district character varying(255),
    latitude double precision,
    location_name character varying(255),
    longitude double precision,
    pincode integer,
    state character varying(255),
    updated_date date,
    updatedby character varying(255)
);
    DROP TABLE public.location;
       public         heap    postgres    false            �            1259    19527    location_location_id_seq    SEQUENCE     �   CREATE SEQUENCE public.location_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.location_location_id_seq;
       public          postgres    false    220            %           0    0    location_location_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.location_location_id_seq OWNED BY public.location.location_id;
          public          postgres    false    219            �            1259    19656    main_department    TABLE       CREATE TABLE public.main_department (
    main_department_id bigint NOT NULL,
    created_date date,
    createdby character varying(255),
    description character varying(255),
    main_department character varying(255),
    updated_date date,
    updatedby character varying(255)
);
 #   DROP TABLE public.main_department;
       public         heap    postgres    false            �            1259    19655 &   main_department_main_department_id_seq    SEQUENCE     �   CREATE SEQUENCE public.main_department_main_department_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 =   DROP SEQUENCE public.main_department_main_department_id_seq;
       public          postgres    false    232            &           0    0 &   main_department_main_department_id_seq    SEQUENCE OWNED BY     q   ALTER SEQUENCE public.main_department_main_department_id_seq OWNED BY public.main_department.main_department_id;
          public          postgres    false    231            �            1259    31835    service_level_agreement    TABLE     �   CREATE TABLE public.service_level_agreement (
    service_level_id bigint NOT NULL,
    severity character varying(255),
    time_line character varying(255)
);
 +   DROP TABLE public.service_level_agreement;
       public         heap    postgres    false            �            1259    19537    skills    TABLE     :  CREATE TABLE public.skills (
    skill_id bigint NOT NULL,
    created_date date,
    createdby character varying(255),
    experience character varying(255),
    level character varying(255),
    skill character varying(255),
    updated_date date,
    updatedby character varying(255),
    employee_id bigint
);
    DROP TABLE public.skills;
       public         heap    postgres    false            �            1259    19536    skills_skill_id_seq    SEQUENCE     |   CREATE SEQUENCE public.skills_skill_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.skills_skill_id_seq;
       public          postgres    false    222            '           0    0    skills_skill_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.skills_skill_id_seq OWNED BY public.skills.skill_id;
          public          postgres    false    221            �            1259    19665    sub_department    TABLE     6  CREATE TABLE public.sub_department (
    sub_department_id bigint NOT NULL,
    created_date date,
    createdby character varying(255),
    description character varying(255),
    sub_department character varying(255),
    updated_date date,
    updatedby character varying(255),
    main_deptmt_id bigint
);
 "   DROP TABLE public.sub_department;
       public         heap    postgres    false            �            1259    19664 $   sub_department_sub_department_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sub_department_sub_department_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ;   DROP SEQUENCE public.sub_department_sub_department_id_seq;
       public          postgres    false    234            (           0    0 $   sub_department_sub_department_id_seq    SEQUENCE OWNED BY     m   ALTER SEQUENCE public.sub_department_sub_department_id_seq OWNED BY public.sub_department.sub_department_id;
          public          postgres    false    233            �            1259    19566    ticket_comment    TABLE     U  CREATE TABLE public.ticket_comment (
    comment_id bigint NOT NULL,
    comment character varying(255),
    description character varying(255),
    emp_id bigint NOT NULL,
    ticket_id bigint,
    created_by bigint,
    updated_by bigint,
    updated_date timestamp(6) without time zone,
    created_date timestamp(6) without time zone
);
 "   DROP TABLE public.ticket_comment;
       public         heap    postgres    false            �            1259    19565    ticket_comment_comment_id_seq    SEQUENCE     �   CREATE SEQUENCE public.ticket_comment_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.ticket_comment_comment_id_seq;
       public          postgres    false    224            )           0    0    ticket_comment_comment_id_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.ticket_comment_comment_id_seq OWNED BY public.ticket_comment.comment_id;
          public          postgres    false    223            �            1259    31846    ticket_documents    TABLE     [   CREATE TABLE public.ticket_documents (
    doc_id bigint NOT NULL,
    ticket_id bigint
);
 $   DROP TABLE public.ticket_documents;
       public         heap    postgres    false            �            1259    19575    ticket_help_team    TABLE     �  CREATE TABLE public.ticket_help_team (
    help_team_id bigint NOT NULL,
    city character varying(255),
    country character varying(255),
    created_date timestamp(6) without time zone,
    description character varying(255),
    district character varying(255),
    email_id character varying(255),
    employee_id bigint NOT NULL,
    employee_name character varying(255),
    employee_organisation character varying(255),
    location character varying(255),
    main_department character varying(255),
    state character varying(255),
    sub_department character varying(255),
    team_member_type character varying(255),
    updated_date timestamp(6) without time zone,
    created_by bigint,
    updated_by bigint
);
 $   DROP TABLE public.ticket_help_team;
       public         heap    postgres    false            �            1259    19574 !   ticket_help_team_help_team_id_seq    SEQUENCE     �   CREATE SEQUENCE public.ticket_help_team_help_team_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 8   DROP SEQUENCE public.ticket_help_team_help_team_id_seq;
       public          postgres    false    226            *           0    0 !   ticket_help_team_help_team_id_seq    SEQUENCE OWNED BY     g   ALTER SEQUENCE public.ticket_help_team_help_team_id_seq OWNED BY public.ticket_help_team.help_team_id;
          public          postgres    false    225            �            1259    19584    ticket_history    TABLE       CREATE TABLE public.ticket_history (
    history_id bigint NOT NULL,
    assigned_to_employee_id character varying(255),
    created_date timestamp(6) without time zone,
    description character varying(255),
    history_info character varying(255),
    initial_assigned_to_employee_id bigint,
    main_department character varying(255),
    priority character varying(255),
    severity character varying(255),
    status character varying(255),
    sub_department character varying(255),
    ticket_description character varying(255),
    ticket_docid character varying(255),
    ticket_title character varying(255),
    ticket_id bigint,
    createdby bigint,
    updatedby bigint,
    created_by bigint,
    updated_by bigint,
    updated_date timestamp(6) without time zone
);
 "   DROP TABLE public.ticket_history;
       public         heap    postgres    false            �            1259    19583    ticket_history_history_id_seq    SEQUENCE     �   CREATE SEQUENCE public.ticket_history_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 4   DROP SEQUENCE public.ticket_history_history_id_seq;
       public          postgres    false    228            +           0    0    ticket_history_history_id_seq    SEQUENCE OWNED BY     _   ALTER SEQUENCE public.ticket_history_history_id_seq OWNED BY public.ticket_history.history_id;
          public          postgres    false    227            �            1259    19593    tickets    TABLE     �  CREATE TABLE public.tickets (
    ticket_id bigint NOT NULL,
    assigned_to_admin_id bigint NOT NULL,
    assigned_to_employee_id bigint NOT NULL,
    city character varying(255),
    closed_date timestamp(6) without time zone,
    country character varying(255),
    created_date timestamp(6) without time zone,
    description character varying(255),
    district character varying(255),
    location character varying(255),
    main_department character varying(255),
    priority character varying(255),
    raised_by_employee_id bigint NOT NULL,
    raised_by_employee_org character varying(255),
    raised_by_employee_role character varying(255),
    remarks character varying(255),
    severity character varying(255),
    state character varying(255),
    status character varying(255),
    sub_department character varying(255),
    ticket_classification character varying(255),
    ticket_description character varying(255),
    title character varying(255),
    updated_date timestamp(6) without time zone,
    slacompliance character varying(255),
    slatime_line timestamp(6) without time zone,
    createdby bigint,
    updatedby bigint,
    time_line character varying(255),
    in_progress_date date,
    open_date date,
    resolved_date timestamp(6) without time zone,
    cancelled_date date,
    created_by bigint,
    updated_by bigint,
    time_taken_for_closure_of_ticket character varying(255)
);
    DROP TABLE public.tickets;
       public         heap    postgres    false            �            1259    19592    tickets_ticket_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.tickets_ticket_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.tickets_ticket_id_seq;
       public          postgres    false    230            ,           0    0    tickets_ticket_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.tickets_ticket_id_seq OWNED BY public.tickets.ticket_id;
          public          postgres    false    229            O           2604    19504    address address_id    DEFAULT     x   ALTER TABLE ONLY public.address ALTER COLUMN address_id SET DEFAULT nextval('public.address_address_id_seq'::regclass);
 A   ALTER TABLE public.address ALTER COLUMN address_id DROP DEFAULT;
       public          postgres    false    215    216    216            P           2604    19522    employee emp_id    DEFAULT     r   ALTER TABLE ONLY public.employee ALTER COLUMN emp_id SET DEFAULT nextval('public.employee_emp_id_seq'::regclass);
 >   ALTER TABLE public.employee ALTER COLUMN emp_id DROP DEFAULT;
       public          postgres    false    218    217    218            Q           2604    19531    location location_id    DEFAULT     |   ALTER TABLE ONLY public.location ALTER COLUMN location_id SET DEFAULT nextval('public.location_location_id_seq'::regclass);
 C   ALTER TABLE public.location ALTER COLUMN location_id DROP DEFAULT;
       public          postgres    false    220    219    220            W           2604    19659 "   main_department main_department_id    DEFAULT     �   ALTER TABLE ONLY public.main_department ALTER COLUMN main_department_id SET DEFAULT nextval('public.main_department_main_department_id_seq'::regclass);
 Q   ALTER TABLE public.main_department ALTER COLUMN main_department_id DROP DEFAULT;
       public          postgres    false    231    232    232            R           2604    19540    skills skill_id    DEFAULT     r   ALTER TABLE ONLY public.skills ALTER COLUMN skill_id SET DEFAULT nextval('public.skills_skill_id_seq'::regclass);
 >   ALTER TABLE public.skills ALTER COLUMN skill_id DROP DEFAULT;
       public          postgres    false    222    221    222            X           2604    19668     sub_department sub_department_id    DEFAULT     �   ALTER TABLE ONLY public.sub_department ALTER COLUMN sub_department_id SET DEFAULT nextval('public.sub_department_sub_department_id_seq'::regclass);
 O   ALTER TABLE public.sub_department ALTER COLUMN sub_department_id DROP DEFAULT;
       public          postgres    false    234    233    234            S           2604    19569    ticket_comment comment_id    DEFAULT     �   ALTER TABLE ONLY public.ticket_comment ALTER COLUMN comment_id SET DEFAULT nextval('public.ticket_comment_comment_id_seq'::regclass);
 H   ALTER TABLE public.ticket_comment ALTER COLUMN comment_id DROP DEFAULT;
       public          postgres    false    224    223    224            T           2604    19578    ticket_help_team help_team_id    DEFAULT     �   ALTER TABLE ONLY public.ticket_help_team ALTER COLUMN help_team_id SET DEFAULT nextval('public.ticket_help_team_help_team_id_seq'::regclass);
 L   ALTER TABLE public.ticket_help_team ALTER COLUMN help_team_id DROP DEFAULT;
       public          postgres    false    226    225    226            U           2604    19587    ticket_history history_id    DEFAULT     �   ALTER TABLE ONLY public.ticket_history ALTER COLUMN history_id SET DEFAULT nextval('public.ticket_history_history_id_seq'::regclass);
 H   ALTER TABLE public.ticket_history ALTER COLUMN history_id DROP DEFAULT;
       public          postgres    false    227    228    228            V           2604    19596    tickets ticket_id    DEFAULT     v   ALTER TABLE ONLY public.tickets ALTER COLUMN ticket_id SET DEFAULT nextval('public.tickets_ticket_id_seq'::regclass);
 @   ALTER TABLE public.tickets ALTER COLUMN ticket_id DROP DEFAULT;
       public          postgres    false    230    229    230                      0    19501    address 
   TABLE DATA           �   COPY public.address (address_id, city, country, created_date, createdby, district, house_no, landmark, pincode, state, street, updated_date, updatedby, employee_id) FROM stdin;
    public          postgres    false    216          
          0    19519    employee 
   TABLE DATA           u  COPY public.employee (emp_id, about_me, contact_primary, contact_secondary, created_date, createdby, date_of_birth, date_of_joining, designation, email_id, emp_first_name, emp_last_name, password, permissions, reporting_manager, roles, sap_entry_id, status, updated_date, updatedby, user_id, location_id, description, employee_organisation, main_department_id) FROM stdin;
    public          postgres    false    218   -                 0    19528    location 
   TABLE DATA           �   COPY public.location (location_id, city, country, created_date, createdby, district, latitude, location_name, longitude, pincode, state, updated_date, updatedby) FROM stdin;
    public          postgres    false    220   k�                 0    19656    main_department 
   TABLE DATA           �   COPY public.main_department (main_department_id, created_date, createdby, description, main_department, updated_date, updatedby) FROM stdin;
    public          postgres    false    232   ˄                 0    31835    service_level_agreement 
   TABLE DATA           X   COPY public.service_level_agreement (service_level_id, severity, time_line) FROM stdin;
    public          postgres    false    235   �                 0    19537    skills 
   TABLE DATA           �   COPY public.skills (skill_id, created_date, createdby, experience, level, skill, updated_date, updatedby, employee_id) FROM stdin;
    public          postgres    false    222   Y�                 0    19665    sub_department 
   TABLE DATA           �   COPY public.sub_department (sub_department_id, created_date, createdby, description, sub_department, updated_date, updatedby, main_deptmt_id) FROM stdin;
    public          postgres    false    234   v�                 0    19566    ticket_comment 
   TABLE DATA           �   COPY public.ticket_comment (comment_id, comment, description, emp_id, ticket_id, created_by, updated_by, updated_date, created_date) FROM stdin;
    public          postgres    false    224   ۅ                 0    31846    ticket_documents 
   TABLE DATA           =   COPY public.ticket_documents (doc_id, ticket_id) FROM stdin;
    public          postgres    false    236   ��                 0    19575    ticket_help_team 
   TABLE DATA             COPY public.ticket_help_team (help_team_id, city, country, created_date, description, district, email_id, employee_id, employee_name, employee_organisation, location, main_department, state, sub_department, team_member_type, updated_date, created_by, updated_by) FROM stdin;
    public          postgres    false    226   ��                 0    19584    ticket_history 
   TABLE DATA           K  COPY public.ticket_history (history_id, assigned_to_employee_id, created_date, description, history_info, initial_assigned_to_employee_id, main_department, priority, severity, status, sub_department, ticket_description, ticket_docid, ticket_title, ticket_id, createdby, updatedby, created_by, updated_by, updated_date) FROM stdin;
    public          postgres    false    228   È                 0    19593    tickets 
   TABLE DATA           /  COPY public.tickets (ticket_id, assigned_to_admin_id, assigned_to_employee_id, city, closed_date, country, created_date, description, district, location, main_department, priority, raised_by_employee_id, raised_by_employee_org, raised_by_employee_role, remarks, severity, state, status, sub_department, ticket_classification, ticket_description, title, updated_date, slacompliance, slatime_line, createdby, updatedby, time_line, in_progress_date, open_date, resolved_date, cancelled_date, created_by, updated_by, time_taken_for_closure_of_ticket) FROM stdin;
    public          postgres    false    230   z�       -           0    0    address_address_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.address_address_id_seq', 1, false);
          public          postgres    false    215            .           0    0    employee_emp_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.employee_emp_id_seq', 6, true);
          public          postgres    false    217            /           0    0    location_location_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.location_location_id_seq', 1, false);
          public          postgres    false    219            0           0    0 &   main_department_main_department_id_seq    SEQUENCE SET     U   SELECT pg_catalog.setval('public.main_department_main_department_id_seq', 1, false);
          public          postgres    false    231            1           0    0    skills_skill_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.skills_skill_id_seq', 1, false);
          public          postgres    false    221            2           0    0 $   sub_department_sub_department_id_seq    SEQUENCE SET     S   SELECT pg_catalog.setval('public.sub_department_sub_department_id_seq', 1, false);
          public          postgres    false    233            3           0    0    ticket_comment_comment_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.ticket_comment_comment_id_seq', 10, true);
          public          postgres    false    223            4           0    0 !   ticket_help_team_help_team_id_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.ticket_help_team_help_team_id_seq', 1, false);
          public          postgres    false    225            5           0    0    ticket_history_history_id_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public.ticket_history_history_id_seq', 124, true);
          public          postgres    false    227            6           0    0    tickets_ticket_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.tickets_ticket_id_seq', 89, true);
          public          postgres    false    229            Z           2606    19508    address address_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);
 >   ALTER TABLE ONLY public.address DROP CONSTRAINT address_pkey;
       public            postgres    false    216            \           2606    19526    employee employee_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (emp_id);
 @   ALTER TABLE ONLY public.employee DROP CONSTRAINT employee_pkey;
       public            postgres    false    218            ^           2606    19535    location location_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (location_id);
 @   ALTER TABLE ONLY public.location DROP CONSTRAINT location_pkey;
       public            postgres    false    220            j           2606    19663 $   main_department main_department_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.main_department
    ADD CONSTRAINT main_department_pkey PRIMARY KEY (main_department_id);
 N   ALTER TABLE ONLY public.main_department DROP CONSTRAINT main_department_pkey;
       public            postgres    false    232            n           2606    31839 4   service_level_agreement service_level_agreement_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.service_level_agreement
    ADD CONSTRAINT service_level_agreement_pkey PRIMARY KEY (service_level_id);
 ^   ALTER TABLE ONLY public.service_level_agreement DROP CONSTRAINT service_level_agreement_pkey;
       public            postgres    false    235            `           2606    19544    skills skills_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.skills
    ADD CONSTRAINT skills_pkey PRIMARY KEY (skill_id);
 <   ALTER TABLE ONLY public.skills DROP CONSTRAINT skills_pkey;
       public            postgres    false    222            l           2606    19672 "   sub_department sub_department_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public.sub_department
    ADD CONSTRAINT sub_department_pkey PRIMARY KEY (sub_department_id);
 L   ALTER TABLE ONLY public.sub_department DROP CONSTRAINT sub_department_pkey;
       public            postgres    false    234            b           2606    19573 "   ticket_comment ticket_comment_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.ticket_comment
    ADD CONSTRAINT ticket_comment_pkey PRIMARY KEY (comment_id);
 L   ALTER TABLE ONLY public.ticket_comment DROP CONSTRAINT ticket_comment_pkey;
       public            postgres    false    224            p           2606    31850 &   ticket_documents ticket_documents_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.ticket_documents
    ADD CONSTRAINT ticket_documents_pkey PRIMARY KEY (doc_id);
 P   ALTER TABLE ONLY public.ticket_documents DROP CONSTRAINT ticket_documents_pkey;
       public            postgres    false    236            d           2606    19582 &   ticket_help_team ticket_help_team_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.ticket_help_team
    ADD CONSTRAINT ticket_help_team_pkey PRIMARY KEY (help_team_id);
 P   ALTER TABLE ONLY public.ticket_help_team DROP CONSTRAINT ticket_help_team_pkey;
       public            postgres    false    226            f           2606    19591 "   ticket_history ticket_history_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.ticket_history
    ADD CONSTRAINT ticket_history_pkey PRIMARY KEY (history_id);
 L   ALTER TABLE ONLY public.ticket_history DROP CONSTRAINT ticket_history_pkey;
       public            postgres    false    228            h           2606    19600    tickets tickets_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (ticket_id);
 >   ALTER TABLE ONLY public.tickets DROP CONSTRAINT tickets_pkey;
       public            postgres    false    230            t           2606    19560 "   skills fk2e90v70j9kr11xah3pw71bti3    FK CONSTRAINT     �   ALTER TABLE ONLY public.skills
    ADD CONSTRAINT fk2e90v70j9kr11xah3pw71bti3 FOREIGN KEY (employee_id) REFERENCES public.employee(emp_id);
 L   ALTER TABLE ONLY public.skills DROP CONSTRAINT fk2e90v70j9kr11xah3pw71bti3;
       public          postgres    false    4700    218    222            w           2606    19678 *   sub_department fk5astf6f56ue8ktrhnjn3guyvn    FK CONSTRAINT     �   ALTER TABLE ONLY public.sub_department
    ADD CONSTRAINT fk5astf6f56ue8ktrhnjn3guyvn FOREIGN KEY (main_deptmt_id) REFERENCES public.main_department(main_department_id);
 T   ALTER TABLE ONLY public.sub_department DROP CONSTRAINT fk5astf6f56ue8ktrhnjn3guyvn;
       public          postgres    false    232    234    4714            r           2606    19673 $   employee fk8vpe5ipnf9rptgwlpvm0qojmh    FK CONSTRAINT     �   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT fk8vpe5ipnf9rptgwlpvm0qojmh FOREIGN KEY (main_department_id) REFERENCES public.main_department(main_department_id);
 N   ALTER TABLE ONLY public.employee DROP CONSTRAINT fk8vpe5ipnf9rptgwlpvm0qojmh;
       public          postgres    false    232    218    4714            u           2606    19601 *   ticket_comment fkjm7vupmht8ekpbmfb461qi5e6    FK CONSTRAINT     �   ALTER TABLE ONLY public.ticket_comment
    ADD CONSTRAINT fkjm7vupmht8ekpbmfb461qi5e6 FOREIGN KEY (ticket_id) REFERENCES public.tickets(ticket_id);
 T   ALTER TABLE ONLY public.ticket_comment DROP CONSTRAINT fkjm7vupmht8ekpbmfb461qi5e6;
       public          postgres    false    4712    230    224            v           2606    19606 *   ticket_history fkkkhg6aquudxfcbofalx8rtc6v    FK CONSTRAINT     �   ALTER TABLE ONLY public.ticket_history
    ADD CONSTRAINT fkkkhg6aquudxfcbofalx8rtc6v FOREIGN KEY (ticket_id) REFERENCES public.tickets(ticket_id);
 T   ALTER TABLE ONLY public.ticket_history DROP CONSTRAINT fkkkhg6aquudxfcbofalx8rtc6v;
       public          postgres    false    228    4712    230            s           2606    19555 $   employee fknfotji9xluv8o3y9gogq2hxiw    FK CONSTRAINT     �   ALTER TABLE ONLY public.employee
    ADD CONSTRAINT fknfotji9xluv8o3y9gogq2hxiw FOREIGN KEY (location_id) REFERENCES public.location(location_id);
 N   ALTER TABLE ONLY public.employee DROP CONSTRAINT fknfotji9xluv8o3y9gogq2hxiw;
       public          postgres    false    220    218    4702            q           2606    19545 #   address fkq4m60pqp7shng4u5n9h2346mp    FK CONSTRAINT     �   ALTER TABLE ONLY public.address
    ADD CONSTRAINT fkq4m60pqp7shng4u5n9h2346mp FOREIGN KEY (employee_id) REFERENCES public.employee(emp_id);
 M   ALTER TABLE ONLY public.address DROP CONSTRAINT fkq4m60pqp7shng4u5n9h2346mp;
       public          postgres    false    218    4700    216                  x������ � �      
   .  x���[��H�g�s�3wA����7I'��*�Z\?�j�8#v�g��?	�K�S�bh�}IH�p���(��/�S� �-L �I/K>3��W�2��λ�i�JX��xRm�Ɏ�k/�it������f���Ҵ����B�����+Ig��������KJb2�A������"C�=V��(}�~d"zXsiL�h1?��1�(�;�-�/ٵ�_���h�^ƻp=�v]�he?���ඬb���`?/�������ű��i2�Ĥm��Q�und� 	�V��zk������#5@)H=���}a���k���	T��T�q4U)�.�=D�]�W�����r��[fj���9�9}}a�Z��e{�G���ڏL$-m�HEO�{2F˔m.�F��`��~9�`�É�̘kÓ�����S����z��g�=��z��U�_�jz.�j�m\Y�Cy��-�t8���]g1�0>d���&`��~I�c��
�>��0BG��hA����=�C+)�e����M�,>�]�@���ah19><�u�Ԑ�\Z�K��\���o�ո��잉�ANGF��ѯrϪƩu�%gbʙ��,Z�'��V�lu���#~�9����ufn_���o�pL\����\3$+ՐV���X����>0�S�r�|8�D[�o�E�3,-���6����wA���n�E�{@u)w�_��S���q��f���|Co�X����w4r����W�7ߠ���V��3�#Ǉ����E@���Ĝ�%�V�U?S���s��`�Zl�4L�l���S�`:ƾ��M�?��|Nؙ@Zr�k͕S��
��1LK%�-��t޲���4	��;>���'T�N����a4ē��53�u)s�
d�oO��<��J�A��%�U�#>�����1;F�����z�D���.W�¹8O��c��p�����F�
u��rU`��]i 8�J��������zߟ�_e�b �� �v����ۃ1�q?���Ē��:h4<v�U�LK��u�o��3��QsD����.2	^U���f����]���kz���I�f-���l��Vo���1
�Z�J�Ǯf���ub�R�W2��F��;�cf�&��Y�a2����Gސ��:�;-KN�i��0�1[�8���!\P�v��g���=ˠ�n�&�b��:ƾ�ީ��ޭɣ�v�{�q֣_�5HLZ-�>WG,��`nР���H��'*���ڎ.S*�	_|U�T�a��}��Aݲ�uߤ*jS�F	:���pnP�$�VC������m%A��S�tƎn�헠ߏM�3�;����7�[�B         P   x�3�LJ�KO��/J���K�L��!�(�S�X��W������XU��X��X����eD�A��%�i��9)8L����� ��.�         ,   x�3��"� ��!�������
�0FH;��C�c���� Z��         B   x�3�t.�,�LN��4���/-*�2���L��4�MI�,�2��MM�,��4��M8}��9M!�=... ���            x������ � �         U   x�3�� �� (ϐ�!����
�0F��eq� D݃CB=C"�r�9G�0נ�`�q�\fHƅ9{8"���qqq ��$M         �   x���1
�0��:E.�/Y��sO��dk����7i��A�|��2�a������s�;��ВF�jYI�s�ߖ�T�O��J�q~`g�S!�mNb�R��%'
�&��*N�Pz�$GќP�e��J������f�mE��5�=[k��P�\h�D�&�Rb            x�34�ര�24�䴰�����  5�         �  x���݊�0������l�M�ִ^�Ғ��B`�J�X���S��ud/���JӀ�p��w<�{$D�G�jJ��9���t�դ̊0, ��{�\@�J�9$$�0G�t*)1C��/
����5IP+�1A������\?�\���j4�"�{�@#��*��'d�Bm2&%�!yiE�9ǣ3G�K�*�h�B���6��Pqa�&F��坞�͛G{5�^rӇBYv�F���w;���mϵA�1����O��Jx�,�S+��z�܍	� s#������N�!ev�F ��S�bI?c.�-�c��L�=�����V�<']�!�_�>b$8�?ݽO2�X?n�v���BQ8�z	^^W��k`O�ˠ��E��L�=V1��J0q&�Y�:	&��>	��u'��(�?CA3���:�%��S���6�����n�C0s������3\(�{�e���K}�Hp�k��+�+;ȼ�� mW8[b�=�"�za�=n�݇�-�F����o         �  x�ݛQoT�ǟ�O� Fc����T���JP�+�%�l�趀��^��_�	E��'�	���$������g8���	f�ǙcـLH`lZZ������lw���W����7�_7�ｸ�6x�/��p��)z�TJ��qZ	heI+��2eLZj.�8�-i���	򄚀YGh�xI�Z�Sn\��I@�%M<�TآD@�����UO+4!��2�i@�%M=�`���\���ڒ�Vhm"M��h��}��ݾsd����dxt p����;�@������m�)
8I��,����O�,)D�=����j��׫w�u2������ͳ�o?�|�ݼ�������7������?���z�����7o�o��?\ݼ���a�������������_�������<�'�/=�����Q�����G�������_;yZ�g�@�@c}�y>�~����}�n�?۽w~�|0g�s��-7+�����9�PRժ���s�s�y�4SE�2'���o�.�S7�('K�^����0��������o)�{��B�
ЌUM�Hm4 ��J4�
��\IQ*�]�F\���J�#3��
5( d+��,�I�J��3Ĩv�����f��<P�1���j'�
�v7�h�z��ahǽ?H<��&�(�6�����j��O^�>{�uủ/�;;??{d�
^W�fN��!+�8���e�vk]·d��S��k�#�����o���[� n�	q�����w�Wӧ�
{;�\��Q�g��B��7��E@�
�xз�u��]C@j֠�~����A���HE�HIJ�a�V��˂���mi�$���&�@�.)E����U�m7z��%�*��%껋�*T<��E��ƽ� F�pqu�hȽ�.l��@;\�~��%�@
��L�@*QK\\OL�� �HMxd�&*��
;�`��5ŕ���,$��t��undJoR[�<��e!�,l+@�TK��m��V�+@+��Ӱ��6��hJn�К�G�4J|�;n���ٞa�y$J#GO��s�@�	[�Qd�a���)�(�~Z�V����=9��E>�#E!�(,�?�#�`�\���,]�=}sk��e��܇�> ��W{ �*�@q�$� V���|jM��2)��i"�8w�IT���;�}tg���<�T��3���|���`c?N2oK)g�{�/^n^_���Fq��g�.P'�~zFX�c=��c�NF�E������N 0�E�'d�K���3t����odj0�38rR�T��-�E1����Cq|�Vq��&h�׃���t?���D�ԑ�V�Ϝ��������O=6c^0�c�	aN����1�����?��s��m%, �!����+QՃ!���%ȦGI�­�p[�K��N�0�i~�^��n�FǸ'�0�y�soʉ@���Gm
�6E��vOdOr��r�L�?]�%&{��P,L<G֗]d({ ڋ��%��̀�֋KX��7��L��b�mPFE�%��%�fdN��B~�F��rD֫�Z�Yq%QY�mX{�4�R�V�k���e?�~�V�|X{�Z�΅Ő�q�<�oH���Ȕ��7-�+�Cu��
��&�*�&�����V���w -
�����.�S�l����bɀhH�o�����F�N����x�Y�,�h���}����c�$��t�VMP���Ծ�9Y¡��ng�ve-��\h3O����W�fm5��1R�Ѫ����(km��RG�^�s[<��{�&���7��B\�
�x"��T�RGD/rX�dZ!��Z� ɐ�DK�ł�K��)�ueaĶ�h۱�����,H\{�#WW�5�\W������*�#GS�E���h=M.I�)8�����%������r!F5��m���N�����4r�6��5��XjH���Dtd���T�9�4�����\2e �ׅ��Ӝ6�L�k��U�4�z��9��L���i��|�4�z��9�����r�[#�Q/9�$ǀ}]̈́��c�Hrt���Rr���� 6�C�#�Q/9���N�.rZ��1���K��~����P����i$9�%��Ӊ�(K�Ґv 4����Kəw�&���aDV5�������[�n�1��=e��h$9�%˒X����d5��t���W�%�_��2�LG��)�:��)���� H9�         �
  x�͛[s�Hǟ�O��V���7o�5I�'N9��ڪy��������9-#d!�jaoLxH%�O��?�Q�3-g���z��%�/'�Q��D�2~BH��Q6˯k���hy��,G��:�t�7�(]l�|�=�W77�oѧϿ�~�������(��z�%�z���ˋ�h|7�k&�۾����`)�b��!l|��(F�l2LAI�CZ��o���A>����ks���M7�x<��p�����!��p�0�k�SJv�8)�,��|��f�8��⹯���/�#B��a�M���(��N�x�Ah�☍�
��h�	�ƈ�,���-"�ik��A�g��|��qq~��y4�l�����0Y��ŽPt���~��:K-u�.�/�0O�zx��C��?<�/.�[c�x�������o��Uf�<e�xV,�u�@�c����^/�}���Vcz g��8�F�"��&�g����Uz`%jΧ1-P�P�p!�1��LT1����P��
p<��D�vK������%�k������	�;솥@>h�6w��Q�p�x�R���Sf�)''�X�t��"z���Ì#�}Q����2�,��J-�Ax"� �S���J���5��h9F\.���}��uƫ����#.�b?��7P<bb��i�M�)?���S�w�z9�WHv��@�&��3&��h�|���AºHt?A�bm%�읒x�	ġ��$���o|$�l��l1$d6���8$��{�}�FM�=�����$2	� I�5Ht$��]8�,�d�W��$�L��J��OѭM�xY��������h�����a���)jbJ��Wa�%�((%��+���4�P�ℼA��G�l�D�ĵ��ز1 ���T�󗓯W��_���d[ϪՂb4��(-�<�]��Ż��K�I�ΊךW��L��������P���'$�)��LJO��0[Od�s�`�����Γ$l�����64�p�C懔
�'e���{Vq�`@���%�mۋ���&&�cK�Y�P�%T�W�[22�3����.2�ґn���4��V}"�}����9 �.F������¿�֓|u���	u����t�Ax�Y`c}BYByBT̅�X��`;�Q�um3��KeӥB�g|PA�ibK�g,A��QU�Yt?A�7�	�t"b���G�,׊]�a�i3q{!D��vI$�m ��K�~iq2l�5.�C$/K���"AtSx�[
�Xhx �`������~3�����`�LK���ԖG=�J�`�w���g��bހ��nzMm$GAblD�5���°�!��@w��lO��-�~nP~�5o�3��$�Cl���ѫ��6X��ZcV\I�j��30�Z�u��q��o�C���Jd��_���
�?��
��rA���rx�E��*���e)t��w�K�~�kL@����������13�"���������.j��!]^��:B��b�DPSm��G��f�����EZ�:��}-w���Z]0�й���2DI����{�����y�y싚�)��`�8ג(h!�~�_M[oRW�G������b��FL��pӫ�����v?���mc�7/ u����w�Z{�C)�St�Nbk��Y�p�$�ML��^��U��K�n��e�ox��"Hų�gd��y%��G�o{G�p�F�& �Q}%O.�$���f�U	�?W��&��$�H��.��7�{��
�SN������zu?2�/�=5��اIԕ�=�m��>� L1��,~� M��e簁S�>xzZ�Cx<�Σcp})�7�g�^�h'��R��kϴu�������w���W<��f�X��Yp2���h<[m%M����pl���v4���g���� "��j�����XkET1�HA���Q���?���[jR�eb�\[-�c:�|�
 Cc-Ad��Z���x,�K&��8����o[��y��������	Ι)5D�(�7�|���h>��5R_�>��f���v7T�N+���ۄc��5���_4�YO���6T�����itk@�rd��U�$0`����X�?vƗ�Z9�]���0KJ���0K�<��>��&UKGRA�%���.!#�\3�u�)���f���67Ec!���l��Tw���m��Xe��3 �n4cc�(�r�N��Y�pow���ws-�|Y��u�{j�R�BD?�sǉC�4�������T77�
*��/F���$�(�R�:�lg�P8�
"�K����^�����G&��Ŀ�������_�K*7!#PϠ�����^>�ː:*���d��~i�(c��0�E4����T�����	�/ Ɵ�(`!c�%�sD ����6��|qJ��E�d�c��@g�fE:<���ȍY�g�N�^�P���<BZ"Peg��4]g� ��t}��Q���Z[�,�6+\�8&�z��#��0�˝3�$jXY~�ov�P��3��
�LQ>��t�KMXf�Ŋ#!��V��<�1�y힧�~NC&N���p?�!"6�(��+L/�`ݞ28#
Z�w�G6�Opp�G	�~a���1�����/L� 0���QJ�4����n����X J��(k��A�NQ��@����∲�ů	�^��Xb�4��|u��I'�
P:#�j"u��_ O���b�ej��#F+��	�8p�l�Z��L:V�b&������0[=�[�z�;�����q�f     