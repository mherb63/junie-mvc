-- Create beer table
CREATE TABLE beer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    beer_name VARCHAR(255),
    beer_style VARCHAR(255),
    upc VARCHAR(255),
    quantity_on_hand INT,
    price DECIMAL(10, 2)
);

-- Create customer table
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255)
);

-- Create beer_order table
CREATE TABLE beer_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    order_status VARCHAR(50) NOT NULL,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Create beer_order_line table
CREATE TABLE beer_order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    beer_order_id INT,
    beer_id INT,
    order_quantity INT DEFAULT 0,
    quantity_allocated INT DEFAULT 0,
    FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
    FOREIGN KEY (beer_id) REFERENCES beer(id)
);

-- Add indexes for performance
CREATE INDEX idx_beer_name ON beer(beer_name);
CREATE INDEX idx_beer_style ON beer(beer_style);
CREATE INDEX idx_customer_name ON customer(name);
CREATE INDEX idx_customer_email ON customer(email);
CREATE INDEX idx_beer_order_customer ON beer_order(customer_id);
CREATE INDEX idx_beer_order_status ON beer_order(order_status);
CREATE INDEX idx_beer_order_line_beer_order ON beer_order_line(beer_order_id);
CREATE INDEX idx_beer_order_line_beer ON beer_order_line(beer_id);