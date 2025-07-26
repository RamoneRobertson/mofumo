create table users
(
    id                bigint auto_increment
        primary key,
    email             varchar(255)                                         not null,
    password          varchar(255)                                         not null,
    firstName         varchar(50)                                          not null,
    lastName          varchar(50)                                          not null,
    phone             varchar(17)                                          not null,
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
            on update cascade on delete cascade
);



