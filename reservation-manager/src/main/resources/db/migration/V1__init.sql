CREATE TABLE reservation (
                             id UUID PRIMARY KEY,
                             customer_email VARCHAR(255),
                             customer_phone_number VARCHAR(255),
                             start_time TIMESTAMP,
                             end_time TIMESTAMP,
                             number_of_guests INTEGER NOT NULL,
                             status VARCHAR(50) NOT NULL,
                             restaurant_id UUID
);

CREATE INDEX idx_reservation_customer_email ON reservation(customer_email);
CREATE INDEX idx_reservation_customer_phone_number ON reservation(customer_phone_number);
CREATE INDEX idx_reservation_restaurant_id ON reservation(restaurant_id);