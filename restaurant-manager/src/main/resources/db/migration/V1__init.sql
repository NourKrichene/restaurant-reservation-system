CREATE TABLE restaurant (
                            id UUID PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description TEXT,
                            capacity INTEGER NOT NULL
);

CREATE TABLE working_time (
                              id BIGSERIAL PRIMARY KEY,
                              restaurant_id UUID NOT NULL,
                              open_time TIME NOT NULL,
                              close_time TIME NOT NULL,
                              day_of_week VARCHAR(20) NOT NULL,
                              CONSTRAINT fk_working_time_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);

CREATE TABLE exceptional_closing_time (
                                          id BIGSERIAL PRIMARY KEY,
                                          restaurant_id UUID NOT NULL,
                                          start_time TIMESTAMP NOT NULL,
                                          end_time TIMESTAMP NOT NULL,
                                          reason VARCHAR(500),
                                          CONSTRAINT fk_exceptional_closing_time_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_working_time_restaurant_id ON working_time(restaurant_id);

CREATE INDEX IF NOT EXISTS idx_exceptional_closing_time_restaurant_id ON exceptional_closing_time(restaurant_id);

CREATE INDEX IF NOT EXISTS idx_exceptional_closing_time_dates ON exceptional_closing_time(start_time, end_time);

