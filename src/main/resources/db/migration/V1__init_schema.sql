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



