-- PostgreSQL Schema and Data for Pet Services Platform

-- Drop tables if they exist (in correct order due to foreign key constraints)
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS provider_availability CASCADE;
DROP TABLE IF EXISTS services CASCADE;
DROP TABLE IF EXISTS pets CASCADE;
DROP TABLE IF EXISTS providers CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create ENUM types
CREATE TYPE ward_type AS ENUM (
    'ADACHI', 'ARAKAWA', 'BUNKYO', 'CHIYODA', 'CHUO', 'EDOGAWA',
    'ITABASHI', 'KATSUSHIKA', 'KITA', 'KOTO', 'MEGURO', 'MINATO',
    'NAKANO', 'NERIMA', 'OTA', 'SETAGAYA', 'SHIBUYA', 'SHINAGAWA',
    'SHINJUKU', 'SUGINAMI', 'SUMIDA', 'TAITO', 'TOSHIMA'
);

CREATE TYPE role_type AS ENUM ('ADMIN', 'CUSTOMER', 'PROVIDER');

CREATE TYPE species_type AS ENUM ('BIRD', 'CAT', 'DOG', 'HAMSTER', 'RABBIT', 'OTHER');

CREATE TYPE gender_type AS ENUM ('FEMALE', 'MALE', 'OTHER');

CREATE TYPE service_category_type AS ENUM (
    'BOARDING', 'DAYCARE', 'GROOMING', 'TRAINING', 'TRANSPORT', 'VETERINARY', 'WALKING'
);

CREATE TYPE day_of_week_type AS ENUM (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
);

CREATE TYPE recurrence_type AS ENUM ('ONE_TIME', 'WEEKLY', 'MONTHLY');

CREATE TYPE location_type AS ENUM ('PROVIDER_LOCATION', 'CUSTOMER_LOCATION', 'MOBILE');

CREATE TYPE booking_status_type AS ENUM ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED');

CREATE TYPE payment_method_type AS ENUM ('CREDIT_CARD', 'PAYPAY', 'LINE_PAY', 'CASH');

CREATE TYPE payment_status_type AS ENUM ('PENDING', 'CONFIRMED', 'CANCELLED', 'DECLINED');

-- Create users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       phone VARCHAR(20) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       ward ward_type,
                       line_id VARCHAR(255),
                       role role_type DEFAULT 'CUSTOMER' NOT NULL,
                       preferred_lang VARCHAR(3) DEFAULT 'en',
                       email_verified BOOLEAN DEFAULT FALSE NOT NULL,
                       active BOOLEAN NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create providers table
CREATE TABLE providers (
                           id BIGSERIAL PRIMARY KEY,
                           user_id BIGINT NOT NULL,
                           business_name VARCHAR(100) NOT NULL,
                           address VARCHAR(255) NOT NULL,
                           description TEXT NOT NULL,
                           service_types JSONB NOT NULL,
                           languages_spoken JSONB NOT NULL,
                           service_areas JSONB NOT NULL,
                           mobile_service BOOLEAN DEFAULT FALSE,
                           base_price INTEGER,
                           verified BOOLEAN DEFAULT FALSE,
                           accepts_new_clients BOOLEAN DEFAULT TRUE,
                           active BOOLEAN DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           CONSTRAINT providers_users_id_fk
                               FOREIGN KEY (user_id) REFERENCES users (id)
                                   ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Create pets table
CREATE TABLE pets (
                      id BIGSERIAL PRIMARY KEY,
                      owner_id BIGINT NOT NULL,
                      name VARCHAR(25) NOT NULL,
                      species species_type NOT NULL,
                      breed VARCHAR(100),
                      age_yr INTEGER,
                      weight_kg DECIMAL(5, 2),
                      gender gender_type,
                      medical_conditions TEXT,
                      special_instruction TEXT,
                      emergency_contact_name VARCHAR(100) NOT NULL,
                      emergency_contact_phone VARCHAR(20) NOT NULL,
                      active BOOLEAN DEFAULT TRUE NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                      CONSTRAINT pets_users_id_fk
                          FOREIGN KEY (owner_id) REFERENCES users (id)
                              ON UPDATE CASCADE ON DELETE CASCADE
);

-- Create services table
CREATE TABLE services (
                          id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                          provider_id BIGINT NOT NULL,
                          service_name VARCHAR(50) NOT NULL,
                          service_category service_category_type,
                          duration_minutes INTEGER NOT NULL,
                          price INTEGER NOT NULL,
                          active BOOLEAN DEFAULT TRUE NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          CONSTRAINT services_providers_id_fk
                              FOREIGN KEY (provider_id) REFERENCES providers (id)
                                  ON UPDATE CASCADE ON DELETE CASCADE
);

-- Create provider_availability table
CREATE TABLE provider_availability (
                                       id BIGSERIAL PRIMARY KEY,
                                       provider_id BIGINT NOT NULL,
                                       day_of_week day_of_week_type,
                                       available_date DATE,
                                       start_time TIME NOT NULL,
                                       end_time TIME NOT NULL,
                                       spans_midnight BOOLEAN DEFAULT false NOT NULL,
                                       recurrence_type recurrence_type NOT NULL,
                                       valid_from DATE NOT NULL,
                                       valid_until DATE,
                                       is_blocked BOOLEAN DEFAULT false NOT NULL,
                                       is_exception BOOLEAN DEFAULT false NOT NULL,
                                       notes VARCHAR(255),
                                       active BOOLEAN DEFAULT true NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT provider_availability_service_provider_fk
                                           FOREIGN KEY (provider_id) REFERENCES providers (id)
                                               ON UPDATE CASCADE ON DELETE CASCADE,

                                       CONSTRAINT chk_time_order
                                           CHECK (
                                               (spans_midnight = false AND start_time < end_time) OR
                                               (spans_midnight = true AND start_time >= end_time)
                                               ),

                                       CONSTRAINT chk_date_logic
                                           CHECK (
                                               (recurrence_type = 'ONE_TIME' AND available_date IS NOT NULL AND day_of_week IS NULL) OR
                                               (recurrence_type IN ('WEEKLY', 'MONTHLY') AND day_of_week IS NOT NULL AND available_date IS NULL)
                                               )
);

-- Create bookings table
CREATE TABLE bookings (
                          id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          pet_id BIGINT NOT NULL,
                          provider_id BIGINT NOT NULL,
                          service_id UUID NOT NULL,
                          booking_date DATE DEFAULT CURRENT_DATE NOT NULL,
                          start_time TIME NOT NULL,
                          end_time TIME NOT NULL,
                          location_type location_type DEFAULT 'PROVIDER_LOCATION' NOT NULL,
                          special_requests TEXT,
                          total_price INTEGER NOT NULL,
                          status booking_status_type DEFAULT 'PENDING' NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

                          CONSTRAINT bookings_pets_id_fk
                              FOREIGN KEY (pet_id) REFERENCES pets (id)
                                  ON UPDATE CASCADE ON DELETE CASCADE,
                          CONSTRAINT bookings_providers_id_fk
                              FOREIGN KEY (provider_id) REFERENCES providers (id)
                                  ON UPDATE CASCADE ON DELETE CASCADE,
                          CONSTRAINT bookings_services_id_fk
                              FOREIGN KEY (service_id) REFERENCES services (id)
                                  ON UPDATE CASCADE ON DELETE CASCADE,
                          CONSTRAINT bookings_users_id_fk
                              FOREIGN KEY (user_id) REFERENCES users (id)
                                  ON UPDATE CASCADE ON DELETE CASCADE
);

-- Create reviews table
CREATE TABLE reviews (
                         id BIGSERIAL PRIMARY KEY,
                         booking_id UUID NOT NULL,
                         user_id BIGINT NOT NULL,
                         rating INTEGER NOT NULL,
                         comment TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         CONSTRAINT reviews_bookings_id_fk
                             FOREIGN KEY (booking_id) REFERENCES bookings (id),
                         CONSTRAINT reviews_users_id_fk
                             FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Create payments table
CREATE TABLE payments (
                          id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                          booking_id UUID NOT NULL,
                          amount INTEGER NOT NULL,
                          payment_method payment_method_type DEFAULT 'CREDIT_CARD' NOT NULL,
                          external_payment_id VARCHAR(255) NOT NULL,
                          status payment_status_type DEFAULT 'PENDING' NOT NULL,
                          payment_date DATE DEFAULT CURRENT_DATE NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          CONSTRAINT payments_bookings_id_fk
                              FOREIGN KEY (booking_id) REFERENCES bookings (id)
);

-- Insert dummy data for Tokyo

-- Insert admin user
INSERT INTO users (email, password, first_name, last_name, phone, address, ward, role, preferred_lang, email_verified, active, updated_at) VALUES
    ('admin@petcare.jp', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', '03-1234-5678', '1-1-1 Shibuya', 'SHIBUYA', 'ADMIN', 'ja', true, true, CURRENT_TIMESTAMP);

-- Insert customer users
INSERT INTO users (email, password, first_name, last_name, phone, address, ward, role, preferred_lang, email_verified, active, updated_at) VALUES
                                                                                                                                               ('tanaka.hiroshi@gmail.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '田中', '寛', '090-1234-5678', '3-15-7 Shibuya', 'SHIBUYA', 'CUSTOMER', 'ja', true, true, CURRENT_TIMESTAMP),
                                                                                                                                               ('sato.yuki@yahoo.co.jp', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '佐藤', '雪', '080-9876-5432', '2-8-12 Shinjuku', 'SHINJUKU', 'CUSTOMER', 'ja', true, true, CURRENT_TIMESTAMP),
                                                                                                                                               ('john.smith@example.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John', 'Smith', '070-5555-1234', '5-3-1 Minato-ku', 'MINATO', 'CUSTOMER', 'en', true, true, CURRENT_TIMESTAMP),
                                                                                                                                               ('yamada.akiko@docomo.ne.jp', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '山田', '明子', '090-7777-8888', '1-4-6 Setagaya', 'SETAGAYA', 'CUSTOMER', 'ja', true, true, CURRENT_TIMESTAMP),
                                                                                                                                               ('chen.mei@gmail.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Chen', 'Mei', '080-2222-3333', '7-2-9 Chiyoda', 'CHIYODA', 'CUSTOMER', 'en', true, true, CURRENT_TIMESTAMP);

-- Insert provider users
INSERT INTO users (email, password, first_name, last_name, phone, address, ward, role, preferred_lang, email_verified, active, updated_at) VALUES
                                                                                                                                               ('provider1@petvet.jp', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '鈴木', '太郎', '03-5555-1111', '4-12-3 Shibuya', 'SHIBUYA', 'PROVIDER', 'ja', true, true, CURRENT_TIMESTAMP),
                                                                                                                                               ('provider2@pawscare.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Maria', 'Garcia', '03-4444-2222', '6-8-15 Shinjuku', 'SHINJUKU', 'PROVIDER', 'en', true, true, CURRENT_TIMESTAMP),
                                                                                                                                               ('provider3@tokyopets.jp', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '高橋', '花子', '03-3333-4444', '2-5-8 Meguro', 'MEGURO', 'PROVIDER', 'ja', true, true, CURRENT_TIMESTAMP);

-- Insert providers
INSERT INTO providers (user_id, business_name, address, description, service_types, languages_spoken, service_areas, mobile_service, base_price, verified, accepts_new_clients, active) VALUES
                                                                                                                                                                                            (6, 'Tokyo Pet Clinic', '4-12-3 Shibuya, Tokyo', '東京で20年以上の経験を持つ獣医クリニックです。犬、猫、小動物の診療を行っています。',
                                                                                                                                                                                             '["VETERINARY", "GROOMING"]'::jsonb, '["Japanese", "English"]'::jsonb, '["SHIBUYA", "SHINJUKU", "MINATO"]'::jsonb, false, 5000, true, true, true),

                                                                                                                                                                                            (7, 'Paws & Care Mobile Services', '6-8-15 Shinjuku, Tokyo', 'Professional mobile pet care services. We come to your home for grooming, walking, and basic veterinary care.',
                                                                                                                                                                                             '["GROOMING", "WALKING", "TRANSPORT"]'::jsonb, '["English", "Spanish", "Japanese"]'::jsonb, '["SHINJUKU", "SHIBUYA", "SETAGAYA", "MEGURO"]'::jsonb, true, 3000, true, true, true),

                                                                                                                                                                                            (8, 'Meguro Pet Hotel & Spa', '2-5-8 Meguro, Tokyo', 'ペットホテルとスパサービスを提供しています。大切なペットを家族のように扱います。',
                                                                                                                                                                                             '["BOARDING", "DAYCARE", "GROOMING"]'::jsonb, '["Japanese"]'::jsonb, '["MEGURO", "SETAGAYA", "SHIBUYA"]'::jsonb, false, 4000, true, true, true);

-- Insert pets
INSERT INTO pets (owner_id, name, species, breed, age_yr, weight_kg, gender, medical_conditions, special_instruction, emergency_contact_name, emergency_contact_phone, active) VALUES
                                                                                                                                                                                   (2, 'ポチ', 'DOG', 'Shiba Inu', 3, 12.5, 'MALE', NULL, 'とても人懐っこいです', '田中花子', '090-1111-2222', true),
                                                                                                                                                                                   (2, 'ミケ', 'CAT', 'Japanese Bobtail', 2, 4.2, 'FEMALE', 'アレルギー（鶏肉）', '静かな環境を好みます', '田中花子', '090-1111-2222', true),
                                                                                                                                                                                   (3, 'Snow', 'CAT', 'Persian', 5, 5.8, 'FEMALE', 'Kidney disease', 'Needs special diet, very gentle', '佐藤健', '080-9999-7777', true),
                                                                                                                                                                                   (4, 'Max', 'DOG', 'Golden Retriever', 4, 28.3, 'MALE', NULL, 'Very energetic, loves playing fetch', 'Sarah Smith', '070-6666-1111', true),
                                                                                                                                                                                   (5, 'ハナ', 'RABBIT', 'Holland Lop', 1, 1.8, 'FEMALE', NULL, '野菜が大好きです', '山田太郎', '090-8888-9999', true),
                                                                                                                                                                                   (5, 'チップ', 'HAMSTER', 'Syrian Hamster', 1, 0.15, 'MALE', NULL, '夜行性です', '山田太郎', '090-8888-9999', true);

-- Insert services
INSERT INTO services (provider_id, service_name, service_category, duration_minutes, price) VALUES
                                                                                                (1, '一般診療', 'VETERINARY', 60, 8000),
                                                                                                (1, '健康診断', 'VETERINARY', 90, 12000),
                                                                                                (1, 'グルーミング基本', 'GROOMING', 120, 6000),
                                                                                                (2, 'Mobile Grooming', 'GROOMING', 90, 8000),
                                                                                                (2, 'Dog Walking', 'WALKING', 60, 3000),
                                                                                                (2, 'Pet Transport', 'TRANSPORT', 30, 2500),
                                                                                                (3, 'ペットホテル（1日）', 'BOARDING', 1440, 5000),
                                                                                                (3, 'デイケア', 'DAYCARE', 480, 3500),
                                                                                                (3, 'プレミアムグルーミング', 'GROOMING', 150, 10000);

-- Insert provider availability (weekly schedules)
INSERT INTO provider_availability (provider_id, day_of_week, start_time, end_time, recurrence_type, valid_from, valid_until) VALUES
-- Tokyo Pet Clinic (Mon-Fri 9:00-18:00, Sat 9:00-15:00)
(1, 'MONDAY', '09:00', '18:00', 'WEEKLY', '2024-01-01', NULL),
(1, 'TUESDAY', '09:00', '18:00', 'WEEKLY', '2024-01-01', NULL),
(1, 'WEDNESDAY', '09:00', '18:00', 'WEEKLY', '2024-01-01', NULL),
(1, 'THURSDAY', '09:00', '18:00', 'WEEKLY', '2024-01-01', NULL),
(1, 'FRIDAY', '09:00', '18:00', 'WEEKLY', '2024-01-01', NULL),
(1, 'SATURDAY', '09:00', '15:00', 'WEEKLY', '2024-01-01', NULL),

-- Paws & Care Mobile (Tue-Sat 8:00-20:00)
(2, 'TUESDAY', '08:00', '20:00', 'WEEKLY', '2024-01-01', NULL),
(2, 'WEDNESDAY', '08:00', '20:00', 'WEEKLY', '2024-01-01', NULL),
(2, 'THURSDAY', '08:00', '20:00', 'WEEKLY', '2024-01-01', NULL),
(2, 'FRIDAY', '08:00', '20:00', 'WEEKLY', '2024-01-01', NULL),
(2, 'SATURDAY', '08:00', '20:00', 'WEEKLY', '2024-01-01', NULL),

-- Meguro Pet Hotel (Daily 7:00-22:00)
(3, 'MONDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL),
(3, 'TUESDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL),
(3, 'WEDNESDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL),
(3, 'THURSDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL),
(3, 'FRIDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL),
(3, 'SATURDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL),
(3, 'SUNDAY', '07:00', '22:00', 'WEEKLY', '2024-01-01', NULL);

-- Insert sample bookings
INSERT INTO bookings (user_id, pet_id, provider_id, service_id, booking_date, start_time, end_time, location_type, total_price, status) VALUES
                                                                                                                                            (2, 1, 1, (SELECT id FROM services WHERE service_name = '一般診療' LIMIT 1), '2024-12-15', '10:00', '11:00', 'PROVIDER_LOCATION', 8000, 'CONFIRMED'),
                                                                                                                                            (3, 3, 2, (SELECT id FROM services WHERE service_name = 'Mobile Grooming' LIMIT 1), '2024-12-16', '14:00', '15:30', 'CUSTOMER_LOCATION', 8000, 'CONFIRMED'),
                                                                                                                                            (4, 4, 2, (SELECT id FROM services WHERE service_name = 'Dog Walking' LIMIT 1), '2024-12-17', '16:00', '17:00', 'CUSTOMER_LOCATION', 3000, 'PENDING'),
                                                                                                                                            (5, 5, 3, (SELECT id FROM services WHERE service_name = 'デイケア' LIMIT 1), '2024-12-18', '08:00', '16:00', 'PROVIDER_LOCATION', 3500, 'CONFIRMED');

-- Insert sample payments
INSERT INTO payments (booking_id, amount, payment_method, external_payment_id, status) VALUES
                                                                                           ((SELECT id FROM bookings WHERE user_id = 2 AND pet_id = 1 LIMIT 1), 8000, 'CREDIT_CARD', 'cc_1234567890', 'CONFIRMED'),
                                                                                           ((SELECT id FROM bookings WHERE user_id = 3 AND pet_id = 3 LIMIT 1), 8000, 'PAYPAY', 'pp_0987654321', 'CONFIRMED'),
                                                                                           ((SELECT id FROM bookings WHERE user_id = 5 AND pet_id = 5 LIMIT 1), 3500, 'LINE_PAY', 'lp_5555666677', 'CONFIRMED');

-- Insert sample reviews
INSERT INTO reviews (booking_id, user_id, rating, comment) VALUES
                                                               ((SELECT id FROM bookings WHERE user_id = 2 AND pet_id = 1 LIMIT 1), 2, 5, 'とても親切で、ポチも安心していました。また利用します。'),
                                                               ((SELECT id FROM bookings WHERE user_id = 3 AND pet_id = 3 LIMIT 1), 3, 4, 'Professional service and Snow looks beautiful! Will book again.');

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_ward ON users(ward);
CREATE INDEX idx_providers_user_id ON providers(user_id);
CREATE INDEX idx_pets_owner_id ON pets(owner_id);
CREATE INDEX idx_services_provider_id ON services(provider_id);
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_provider_id ON bookings(provider_id);
CREATE INDEX idx_bookings_date ON bookings(booking_date);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_provider_availability_provider_id ON provider_availability(provider_id);
CREATE INDEX idx_reviews_booking_id ON reviews(booking_id);
CREATE INDEX idx_payments_booking_id ON payments(booking_id);