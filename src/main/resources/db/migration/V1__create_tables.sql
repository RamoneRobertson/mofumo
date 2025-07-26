create table users
(
    id                bigint auto_increment
        primary key,
    email             varchar(255)                                         not null,
    password          varchar(255)                                         not null,
    firs_name         varchar(50)                                          not null,
    last_name         varchar(50)                                          not null,
    phone             varchar(20)                                          not null,
    line_id           varchar(255)                                         null,
    ward              varchar(20)                                          null,
    user_type         enum ('CUSTOMER', 'PROVIDER') default 'CUSTOMER'     not null,
    preferred_lang    varchar(3)                    default 'en'           null,
    email_verified    boolean                       default FALSE          not null,
    active            boolean                                              not null,
    createdAt         timestamp                     default CURRENT_TIMESTAMP not null,
    updatedAt         timestamp                                            not null
);

create table providers
(
    id                  bigint auto_increment
        primary key,
    user_id              bigint                              not null,
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
        foreign key (user_id) references users (id)
            on update cascade on delete restrict
);

create table pets
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

create table services
(
    id               binary(16) default (uuid_to_bin(uuid()))                                                   not null
        primary key,
    provider_id      bigint                                                                                     not null,
    service_name     varchar(50)                                                                                not null,
    service_category enum ('GROOMING', 'VETERINARY', 'BOARDING', 'WALKING', 'TRAINING', 'DAYCARE', 'TRANSPORT') null,
    duration_minutes int                                                                                        not null,
    price            int                                                                                        not null,
    active           boolean    default TRUE                                                                    not null,
    createdAt        timestamp  default current_timestamp                                                       not null,
    constraint services_providers_id_fk
        foreign key (provider_id) references providers (id)
            on update cascade on delete cascade
);

create table provider_availability
(
    id              bigint auto_increment primary key,
    provider_id     bigint                                                           not null,
    day_of_week     enum ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') null,
    available_date  date                                                             null,
    start_time      time                                                             not null,
    end_time        time                                                             not null,
    spans_midnight  boolean                                            default false not null,
    recurrence_type enum ('one_time', 'weekly', 'monthly')                          not null,
    valid_from      date                                                             not null,
    valid_until     date                                                             null,
    is_blocked      boolean                                            default false not null,
    is_exception    boolean                                            default false not null,
    notes           varchar(255)                                                     null,
    active          boolean                                            default true  not null,
    created_at      timestamp                                          default current_timestamp not null,
    updated_at      timestamp                                          default current_timestamp on update current_timestamp,

    constraint provider_availability_service_provider_fk
        foreign key (provider_id) references providers (id)
            on update cascade on delete cascade,

    constraint chk_time_order
        check (
            (spans_midnight = false and start_time < end_time) or
            (spans_midnight = true and start_time >= end_time)
            ),

    constraint chk_date_logic
        check (
            (recurrence_type = 'one_time' and available_date is not null and day_of_week is null) or
            (recurrence_type in ('weekly', 'monthly') and day_of_week is not null and available_date is null)
            )
);

create table bookings
(
    id               binary(16)                                                default (uuid_to_bin(uuid())) not null
        primary key,
    user_id          bigint                                                                                  not null,
    pet_id           bigint                                                                                  not null,
    provider_id      bigint                                                                                  not null,
    service_id       binary(16)                                                                              not null,
    booking_date     date                                                      default (current_date())        not null,
    start_time       time                                                                                    not null,
    end_time         time                                                                                    not null,
    location_type    enum ('provider_location', 'customer_location', 'mobile') default 'provider_location'   not null,
    special_requests text                                                                                    null,
    total_price      int                                                                                     not null,
    status           enum ('pending', 'confirmed', 'completed', 'cancelled')   default 'pending'             not null,
    updatedAt        timestamp default current_timestamp on update current_timestamp not null,
    createdAt        timestamp                                                 default current_timestamp     not null,

    constraint bookings_pets_id_fk
        foreign key (pet_id) references pets (id)
            on update cascade on delete cascade,
    constraint bookings_providers_id_fk
        foreign key (provider_id) references providers (id)
            on update cascade on delete cascade,
    constraint bookings_services_id_fk
        foreign key (service_id) references services (id)
            on update cascade on delete cascade,
    constraint bookings_users_id_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade
);

create table reviews
(
    id         bigint auto_increment
        primary key,
    booking_id binary(16)                          not null,
    user_id    bigint                              not null,
    rating     int                                 not null,
    comment    text                                null,
    createdAt  timestamp default current_timestamp not null,
    constraint reviews_bookings_id_fk
        foreign key (booking_id) references bookings (id),
    constraint reviews_users_id_fk
        foreign key (user_id) references users (id)
);

create table payments
(
    id                  binary                                                 default (uuid_to_bin(uuid())) not null
    primary key,
    booking_id          binary(16)                                                                           not null,
    amount              int                                                                                  not null,
    payment_method      enum ('credit_card', 'paypay', 'line_pay', 'cash')     default 'credit_card'         not null,
    external_payment_id varchar(255)                                                                         not null,
    status              enum ('pending', 'confirmed', 'cancelled', 'declined') default 'pending'             not null,
    payment_date        date                                                   default (current_date())      not null,
    createdAt           timestamp                                              default current_timestamp     not null,
    constraint payments_bookings_id_fk
    foreign key (booking_id) references bookings (id)
);




















