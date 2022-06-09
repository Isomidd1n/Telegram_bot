PGDMP         #                z            nout_uz    14.2    14.2 6    ?           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            @           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            A           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            B           1262    123123    nout_uz    DATABASE     d   CREATE DATABASE nout_uz WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Russian_Russia.1251';
    DROP DATABASE nout_uz;
                postgres    false            �            1255    156055    log_function()    FUNCTION     �  CREATE FUNCTION public.log_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare


begin


    if TG_OP = 'UPDATE' AND NEW.price <> OLD.price then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (OLD.category_id, OLD.id, now(), 'Narxi o''zgartirildi');

    elsif TG_OP = 'UPDATE' AND NEW.description <> OLD.description then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (OLD.category_id, OLD.id, now(), 'Tafsiloti o''zgartirildi');

    elsif TG_OP = 'UPDATE' AND NEW.name <> OLD.name then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (OLD.category_id, OLD.id, now(), 'Nomi o''zgartirildi');


    elsif TG_OP = 'UPDATE' AND NEW.deleted = true then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (OLD.category_id, OLD.id, now(), 'Deleted fildi yoqildi');

    elsif TG_OP = 'UPDATE' AND NEW.deleted = false then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (OLD.category_id, OLD.id, now(), 'Deleted fildi o''chirildi');


    elsif TG_OP = 'INSERT' then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (NEW.category_id, NEW.id, now(), 'Yangi mahsulot qo''shildi');

    elsif TG_OP = 'DELETE' then
        insert into operations (category_id, product_id, updated_at, operation_name)
        values (OLD.category_id, OLD.id, now(), 'Mahsulot o''chirildi');

    end if;
    return new;


end;
$$;
 %   DROP FUNCTION public.log_function();
       public          postgres    false            �            1259    131452    product    TABLE       CREATE TABLE public.product (
    id bigint NOT NULL,
    category_id bigint,
    name character varying(150) NOT NULL,
    price real DEFAULT 0 NOT NULL,
    image character varying(255) NOT NULL,
    deleted boolean DEFAULT false,
    description text,
    quantity integer
);
    DROP TABLE public.product;
       public         heap    postgres    false            �            1259    156061    all_products_view    VIEW     �   CREATE VIEW public.all_products_view AS
 SELECT product.id,
    product.category_id,
    product.name,
    product.price,
    product.image,
    product.deleted,
    product.description,
    product.quantity
   FROM public.product;
 $   DROP VIEW public.all_products_view;
       public          postgres    false    213    213    213    213    213    213    213    213            �            1259    131477    cart_product    TABLE     �   CREATE TABLE public.cart_product (
    id bigint NOT NULL,
    customer_id character varying(100),
    product bigint,
    quantity bigint
);
     DROP TABLE public.cart_product;
       public         heap    postgres    false            �            1259    131476    cart_product_id_seq    SEQUENCE     |   CREATE SEQUENCE public.cart_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.cart_product_id_seq;
       public          postgres    false    215            C           0    0    cart_product_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.cart_product_id_seq OWNED BY public.cart_product.id;
          public          postgres    false    214            �            1259    131428    category    TABLE     �   CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    deleted boolean DEFAULT false,
    parent_category integer
);
    DROP TABLE public.category;
       public         heap    postgres    false            �            1259    131427    category_id_seq    SEQUENCE     x   CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.category_id_seq;
       public          postgres    false    211            D           0    0    category_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;
          public          postgres    false    210            �            1259    123344    customer    TABLE     �   CREATE TABLE public.customer (
    id character varying(100) NOT NULL,
    first_name character varying(70),
    last_name character varying(70),
    phone_number character varying(20) NOT NULL,
    status character varying(100)
);
    DROP TABLE public.customer;
       public         heap    postgres    false            �            1259    156065    deleted_view    VIEW       CREATE VIEW public.deleted_view AS
 SELECT product.id,
    product.category_id,
    product.name,
    product.price,
    product.image,
    product.deleted,
    product.description,
    product.quantity
   FROM public.product
  WHERE (product.deleted = true);
    DROP VIEW public.deleted_view;
       public          postgres    false    213    213    213    213    213    213    213    213            �            1259    156057    not_deleted_view    VIEW     	  CREATE VIEW public.not_deleted_view AS
 SELECT product.id,
    product.category_id,
    product.name,
    product.price,
    product.image,
    product.deleted,
    product.description,
    product.quantity
   FROM public.product
  WHERE (product.deleted = false);
 #   DROP VIEW public.not_deleted_view;
       public          postgres    false    213    213    213    213    213    213    213    213            �            1259    156046 
   operations    TABLE     �   CREATE TABLE public.operations (
    id bigint NOT NULL,
    category_id integer NOT NULL,
    product_id integer NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    operation_name character varying NOT NULL
);
    DROP TABLE public.operations;
       public         heap    postgres    false            �            1259    156045    operations_id_seq    SEQUENCE     z   CREATE SEQUENCE public.operations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.operations_id_seq;
       public          postgres    false    217            E           0    0    operations_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.operations_id_seq OWNED BY public.operations.id;
          public          postgres    false    216            �            1259    156069    operations_view    VIEW     �   CREATE VIEW public.operations_view AS
 SELECT operations.id,
    operations.category_id,
    operations.product_id,
    operations.updated_at,
    operations.operation_name
   FROM public.operations;
 "   DROP VIEW public.operations_view;
       public          postgres    false    217    217    217    217    217            �            1259    172430    orders    TABLE     v  CREATE TABLE public.orders (
    id bigint NOT NULL,
    user_id character varying(100) NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    phone_number character varying(100) NOT NULL,
    user_order character varying(1000) NOT NULL,
    totalprice numeric NOT NULL,
    date_time character varying(100) NOT NULL
);
    DROP TABLE public.orders;
       public         heap    postgres    false            �            1259    172429    orders_id_seq    SEQUENCE     v   CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.orders_id_seq;
       public          postgres    false    223            F           0    0    orders_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;
          public          postgres    false    222            �            1259    131451    product_id_seq    SEQUENCE     w   CREATE SEQUENCE public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.product_id_seq;
       public          postgres    false    213            G           0    0    product_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;
          public          postgres    false    212            �           2604    131480    cart_product id    DEFAULT     r   ALTER TABLE ONLY public.cart_product ALTER COLUMN id SET DEFAULT nextval('public.cart_product_id_seq'::regclass);
 >   ALTER TABLE public.cart_product ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    214    215            �           2604    131431    category id    DEFAULT     j   ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);
 :   ALTER TABLE public.category ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    210    211    211            �           2604    156049    operations id    DEFAULT     n   ALTER TABLE ONLY public.operations ALTER COLUMN id SET DEFAULT nextval('public.operations_id_seq'::regclass);
 <   ALTER TABLE public.operations ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    216    217            �           2604    172433 	   orders id    DEFAULT     f   ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);
 8   ALTER TABLE public.orders ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    222    223            �           2604    131455 
   product id    DEFAULT     h   ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);
 9   ALTER TABLE public.product ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    213    212    213            8          0    131477    cart_product 
   TABLE DATA           J   COPY public.cart_product (id, customer_id, product, quantity) FROM stdin;
    public          postgres    false    215   �F       4          0    131428    category 
   TABLE DATA           F   COPY public.category (id, name, deleted, parent_category) FROM stdin;
    public          postgres    false    211   
G       2          0    123344    customer 
   TABLE DATA           S   COPY public.customer (id, first_name, last_name, phone_number, status) FROM stdin;
    public          postgres    false    209   �G       :          0    156046 
   operations 
   TABLE DATA           ]   COPY public.operations (id, category_id, product_id, updated_at, operation_name) FROM stdin;
    public          postgres    false    217   _I       <          0    172430    orders 
   TABLE DATA           u   COPY public.orders (id, user_id, first_name, last_name, phone_number, user_order, totalprice, date_time) FROM stdin;
    public          postgres    false    223   �L       6          0    131452    product 
   TABLE DATA           f   COPY public.product (id, category_id, name, price, image, deleted, description, quantity) FROM stdin;
    public          postgres    false    213   N       H           0    0    cart_product_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.cart_product_id_seq', 129, true);
          public          postgres    false    214            I           0    0    category_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.category_id_seq', 13, true);
          public          postgres    false    210            J           0    0    operations_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.operations_id_seq', 99, true);
          public          postgres    false    216            K           0    0    orders_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.orders_id_seq', 4, true);
          public          postgres    false    222            L           0    0    product_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.product_id_seq', 22, true);
          public          postgres    false    212            �           2606    131482    cart_product cart_product_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.cart_product
    ADD CONSTRAINT cart_product_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.cart_product DROP CONSTRAINT cart_product_pkey;
       public            postgres    false    215            �           2606    131436    category category_name_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_name_key UNIQUE (name);
 D   ALTER TABLE ONLY public.category DROP CONSTRAINT category_name_key;
       public            postgres    false    211            �           2606    131434    category category_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.category DROP CONSTRAINT category_pkey;
       public            postgres    false    211            �           2606    123348    customer customer_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.customer DROP CONSTRAINT customer_pkey;
       public            postgres    false    209            �           2606    156054    operations operations_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.operations
    ADD CONSTRAINT operations_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.operations DROP CONSTRAINT operations_pkey;
       public            postgres    false    217            �           2606    172437    orders orders_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_pkey;
       public            postgres    false    223            �           2606    131459    product product_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public            postgres    false    213            �           2606    147861 !   product unique_name_with_category 
   CONSTRAINT     i   ALTER TABLE ONLY public.product
    ADD CONSTRAINT unique_name_with_category UNIQUE (category_id, name);
 K   ALTER TABLE ONLY public.product DROP CONSTRAINT unique_name_with_category;
       public            postgres    false    213    213            �           2620    156056    product audit_product    TRIGGER     �   CREATE TRIGGER audit_product AFTER INSERT OR DELETE OR UPDATE ON public.product FOR EACH ROW EXECUTE FUNCTION public.log_function();
 .   DROP TRIGGER audit_product ON public.product;
       public          postgres    false    213    235            �           2606    131483 *   cart_product cart_product_customer_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart_product
    ADD CONSTRAINT cart_product_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customer(id);
 T   ALTER TABLE ONLY public.cart_product DROP CONSTRAINT cart_product_customer_id_fkey;
       public          postgres    false    215    209    3215            �           2606    131488 &   cart_product cart_product_product_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart_product
    ADD CONSTRAINT cart_product_product_fkey FOREIGN KEY (product) REFERENCES public.product(id);
 P   ALTER TABLE ONLY public.cart_product DROP CONSTRAINT cart_product_product_fkey;
       public          postgres    false    215    213    3221            �           2606    147853 &   category category_parent_category_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_parent_category_fkey FOREIGN KEY (parent_category) REFERENCES public.category(id);
 P   ALTER TABLE ONLY public.category DROP CONSTRAINT category_parent_category_fkey;
       public          postgres    false    3219    211    211            �           2606    131460     product product_category_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.category(id);
 J   ALTER TABLE ONLY public.product DROP CONSTRAINT product_category_id_fkey;
       public          postgres    false    213    3219    211            8      x������ � �      4   x   x�5�;�0�z�� �~��d{���@�(h(\�O���͌��~ޏZ_�3�
)�ET��B����g�#�%���b������j: ��5��b� �,�Xڡ�����c4�|��<.���n �      2   �  x�eQMkSA]O~��a�̝;s���E4]dBjL߃�ӕ�B�""HqQ,~W\�p��-ӭ����8]��{�=�a���Q���jo[�b��3T��ڹso+��mm�6  5X
A�ߞ��\�Ώ�/�˺�|u��*�|^5]��fC��D�3krF���>�.��]?�k�&cSc�yhH�?�j�B'X ��2F#1��������Y�0v��vyt08|T���4Y�����d"�����a<��e�d9��� Y��ZP��~��W�<����Q�iuY������[uUM�L�Đ0]���!Tyo0\
�IZ�e%/��g�H�$��8>:��&zf�=ȠD �|��Չ�����ڒ��K���1��q1){u�w��W�8�/��L��%L����j�� ��$k��aYdͬ���kiW옓{�F���	�      :   f  x�}��n1�ϳO������Ι+��A��
��?���`��4��J��؟��oX�U~��
4�@������������������͗�����KԆ�\��*0*��Z��Q�̹�P�j�NZ�����������<<>_=�<���̫�T�BKVm3��qK�й��}F��:��J���%�-(�;���D�?O��9
u�
��qRI��YupT��2v!��q�K��!ABY�Ze�P^�6�T$ꜝ��:*G��,cg��u!]d2�,C�S��V�*cb2�R��([Jc5[�,��\���|�-f���0�UF���*g��t�*P�PL)g����k�5FYᬕ����B��t�.���œ�5�V�]
�')c�Zy�r%T�ٞ�Z���ye�ZE�{�(o̴Ԋ�2jU0+_Zj����IbEK����҉��X��+F��*;�B+�
D�ޮ�I)�B+�
�^H�L؅V�h����K}s,�V�R�	�W6馛����8�TLУU��'�c�6�,C|����5Ό�U�:R!��4[�h�]��Z4:��$E|�jg�x�P�3���ˑ^�,�|��Y��ą�.Y1[����>����o2W��o�ǝ��%��1�ol�)B��Pv���z�j+�WkI)�"�;���$����/G�b/Ș�y�_g9:,p�6a�0�GB�:�������Ri� {^�y�l�(�`ٞubc���&���N���-A����^��5�h;׻d<l@ݍ�ń5j�=hb}��~�j���h�1���i�M�������ضb9^�E�?��P�%_!�Ye:���Τ�-y�>|��b��"�����%���\.��P �~      <   !  x�Œ�N�0@g�+<�ݝ���KF�Ș%�Dj$6~����>���� T���v`:˺{��;Tb��x&�ݼ?�R��\��#���m���iƶ��������]6}�}Ց>�X �/��tH	U�ݼ�|����~l�& Z�[@q�&8	9���K���n��wӪW���*����� ��u̮��?�� L�3��"V0��*�C�aA�̿!r9�)ș#�fyvl3{���Չ��^���J�7B�_��k��#A>�!QU
j�b�E���q�U�e�'��M      6   �  x������@���S����F��
#4DT�I&1�t����
O?l�s��*���0\ĩw0HF�$��3B<�Nh�Lݿ�����w.yҸ��r����{��+��ζ��]���Î�	�)	A@#~n�*$�x45�M]�<��`��!���U���.x�F��v��yS��a
�emò�����gZ��L�q���|m����2�F/��{5/άH�a{��ꘂ��?=g���Jl�Ob��F3R����eqJAҜa��WS(*�F���!߃�!j��?9�8z�J���M���T�N�*���si
S��)���gG%_L@��w�"��y_��
H��x2�f�n�|4"����a��xmݼξo���ͱ�k�}�R�ku�9XH���i�V%�f     