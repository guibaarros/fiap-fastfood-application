-- CREATE SEQUENCES

CREATE SEQUENCE IF NOT EXISTS public.tb_client_client_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.tb_order_order_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.tb_product_image_product_image_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.tb_product_product_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- CREATE TABLES

CREATE TABLE IF NOT EXISTS public.tb_client
(
    client_id bigint NOT NULL DEFAULT nextval('tb_client_client_id_seq'::regclass),
    cpf bigint,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    last_visit date NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT tb_client_pkey PRIMARY KEY (client_id)
);

CREATE TABLE IF NOT EXISTS public.tb_order
(
    order_id bigint NOT NULL DEFAULT nextval('tb_order_order_id_seq'::regclass),
    created_at timestamp(6) without time zone NOT NULL,
    finished_at timestamp(6) without time zone,
    "number" integer,
    payment_status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    total_amount numeric(38,2),
    updated_at timestamp(6) without time zone,
    payment_status_updated_at timestamp(6) without time zone,
    payment_qr_code_data character varying(255) COLLATE pg_catalog."default",
    external_id bigint,
    client_id bigint,
    CONSTRAINT tb_order_pkey PRIMARY KEY (order_id),
    CONSTRAINT tk_tb_order_client FOREIGN KEY (client_id)
        REFERENCES public.tb_client (client_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT tb_order_payment_status_check CHECK (payment_status::text = ANY (ARRAY['AWAITING_PAYMENT'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying]::text[])),
    CONSTRAINT tb_order_status_check CHECK (status::text = ANY (ARRAY['AWAITING_PAYMENT'::character varying, 'AWAITING_PREPARATION'::character varying, 'PREPARING'::character varying, 'READY'::character varying, 'RECEIVED'::character varying::text, 'FINISHED'::character varying, 'CANCELLED'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS public.tb_product
(
    product_id bigint NOT NULL DEFAULT nextval('tb_product_product_id_seq'::regclass),
    category character varying(255) COLLATE pg_catalog."default",
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    price numeric(38,2),
    CONSTRAINT tb_product_pkey PRIMARY KEY (product_id),
    CONSTRAINT tb_product_category_check CHECK (category::text = ANY (ARRAY['SNACK'::character varying, 'SIDE'::character varying, 'DRINK'::character varying, 'DESSERT'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS public.tb_order_products
(
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    CONSTRAINT fk_tb_order_products_order FOREIGN KEY (order_id)
        REFERENCES public.tb_order (order_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_tb_order_products_product FOREIGN KEY (product_id)
        REFERENCES public.tb_product (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.tb_product_image
(
    product_image_id bigint NOT NULL DEFAULT nextval('tb_product_image_product_image_id_seq'::regclass),
    content_type character varying(255) COLLATE pg_catalog."default",
    image oid,
    name character varying(255) COLLATE pg_catalog."default",
    size bigint,
    product_id bigint,
    CONSTRAINT tb_product_image_pkey PRIMARY KEY (product_image_id),
    CONSTRAINT fk_tb_product_image_product_id FOREIGN KEY (product_id)
        REFERENCES public.tb_product (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- INSERT INITIAL DATA

-- INSERT CLIENT
INSERT INTO public.tb_client (cpf, created_at, email, last_visit, name) VALUES (28781099029, now(), 'guilherme@gmail.com', now(), 'Guilherme');
INSERT INTO public.tb_client (cpf, created_at, email, last_visit, name) VALUES (00935439056, now(), 'camila@gmail.com', now(), 'Camila');
INSERT INTO public.tb_client (cpf, created_at, email, last_visit, name) VALUES (74044290059, now(), 'lisa@gmail.com', now(), 'Lisa');
INSERT INTO public.tb_client (cpf, created_at, email, last_visit, name) VALUES (53309306003, now(), 'cora@gmail.com', now(), 'Cora');
INSERT INTO public.tb_client (cpf, created_at, last_visit) VALUES (61657364089, now(), now());

-- INSERT PRODUCT
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('SNACK', now(), 'X-Salada Clássico', 'X-Salada', 15);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('SNACK', now(), 'Pão, carne e queijo', 'X-Burger', 12);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('SNACK', now(), 'Hot-Dog completo', 'Hot-Dog', 10);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('SIDE', now(), 'Porção de Fritas individual', 'Fritas', 8);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('SIDE', now(), 'Anéis de cebola empanados', 'Onion Rings', 8);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('SIDE', now(), 'Porção de Nuggets', 'Nuggets', 12);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('DRINK', now(), 'Coca-Cola 200ml', 'Coca-Cola', 4);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('DRINK', now(), 'Água Mineral sem gás', 'Água Mineral', 3);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('DRINK', now(), 'Água Mineral com gás', 'Água com gás', 3);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('DESSERT', now(), 'Pudim de Leite', 'Pudim', 6);
INSERT INTO public.tb_product(category, created_at, description, name, price) VALUES ('DESSERT', now(), 'Mousse cremoso de chocolate', 'Mousse de Chocolate', 6);

-- INSERT 
INSERT INTO public.tb_order(created_at, "number", payment_status, status, total_amount, finished_at, updated_at, payment_status_updated_at, external_id, payment_qr_code_data) VALUES ('2023-10-18 14:15:32.595585', 1, 'PAID', 'FINISHED', 32, '2023-10-18 14:28:58.125883', '2023-10-18 14:28:58.125883', '2023-10-18 14:17:21.7458', 1, '"00020101021243650016COM.MERCADOLIBRE02013063638f1192a-5fd1-4180-a180-8bcae3556bc35204000053039865802BR5925IZABEL AAAA DE MELO6007BARUERI62070503***63040B6D"');
INSERT INTO public.tb_order(created_at, "number", payment_status, status, total_amount, client_id, payment_status_updated_at, external_id, payment_qr_code_data) VALUES (now(), 1, 'PAID', 'AWAITING_PREPARATION', 33, 1, now(), 2, '"00020101021243650016COM.MERCADOLIBRE02013063638f1192a-5fd1-4180-a180-8bcae3556bc35204000053039865802BR5925IZABEL AAAA DE MELO6007BARUERI62070503***63040B6D"');
INSERT INTO public.tb_order(created_at, "number", payment_status, status, total_amount, client_id, external_id, payment_qr_code_data) VALUES (now(), 2, 'AWAITING_PAYMENT', 'AWAITING_PAYMENT', 23, 2, 3, '"00020101021243650016COM.MERCADOLIBRE02013063638f1192a-5fd1-4180-a180-8bcae3556bc35204000053039865802BR5925IZABEL AAAA DE MELO6007BARUERI62070503***63040B6D"');

-- INSERT ORDER PRODUCTS
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (1, 1);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (1, 5);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (1, 8);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (1, 11);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (2, 1);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (2, 4);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (2, 7);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (2, 10);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (3, 2);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (3, 4);
INSERT INTO public.tb_order_products(order_id, product_id) VALUES (3, 8);