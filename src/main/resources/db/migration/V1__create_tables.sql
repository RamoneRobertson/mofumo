create table users
(
    id                bigint auto_increment
        primary key,
    email             varchar(255)                                         not null,
    password          varchar(255)                                         not null,
    firstName         varchar(50)                                          not null,
    lastName          varchar(50)                                          not null,
    phone             varchar(20)                                          not null,
    lineId            varchar(255)                                         null,
    ward              varchar(20)                                          null,
    userType          enum ('CUSTOMER', 'PROVIDER') default 'CUSTOMER'     not null,
    preferredLanguage varchar(3)                    default 'en'           null,
    emailVerified     boolean                       default FALSE          not null,
    active            boolean                                              not null,
    createdAt         timestamp                     default CURRENT_TIMESTAMP not null,
    updatedAt         timestamp                                            not null
);

create table providers
(
    id                  bigint auto_increment
        primary key,
    userId              bigint                              not null,
    business_name       varchar(100)                        not null,
    description         text                                not null,
    service_types       json                                not null,
    languages_spoken    json                                not null,
    service_areas       json                                not null,
    mobile_service      boolean   default FALSE             null,
    base_price          int                                 null,
    verified            boolean   default FALSE             null,
    accepts_new_clients boolean   default TRUE              null,
    active              boolean   default TRUE              null,
    created_at          timestamp default current_timestamp not null,
    constraint providers_users_id_fk
        foreign key (userId) references users (id)
            on update cascade on delete restrict
);

create table pets_9
(
    id                      bigint auto_increment
        primary key,
    owner_id                bigint                                                                                      not null,
    name                    varchar(25)                                                                                 not null,
    species                 enum ('DOG', 'CAT', 'FISH', 'BIRD', 'HAMSTER', 'RABBIT', 'OTHER') default 'OTHER'           not null,
    breed                   varchar(100)                                                                                null,
    age_yr                  int                                                                                         null,
    weight_kg               decimal(5, 2)                                                                               null,
    medical_conditions      text                                                                                        null,
    special_instruction     text                                                                                        null,
    emergency_contact_name  varchar(100)                                                                                not null,
    emergency_contact_phone varchar(20)                                                                                 not null,
    active                  boolean                                                           default TRUE              not null,
    createdAt               timestamp                                                         default current_timestamp not null,
    constraint pets_9_users_id_fk
        foreign key (owner_id) references users (id)
            on update cascade on delete cascade
);





