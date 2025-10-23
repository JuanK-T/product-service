-- Tabla principal de productos
CREATE TABLE products (
                          id VARCHAR(50) PRIMARY KEY,
                          name VARCHAR(60) NOT NULL,
                          image_url VARCHAR(500),
                          description VARCHAR(500) NOT NULL,
                          price NUMERIC(10,2) NOT NULL,
                          currency VARCHAR(3) NOT NULL DEFAULT 'USD',
                          rating DOUBLE PRECISION NOT NULL,
                          category VARCHAR(50) NOT NULL,
                          brand VARCHAR(50),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de especificaciones
CREATE TABLE product_specifications (
                                        product_id VARCHAR(50) NOT NULL,
                                        spec_key VARCHAR(50) NOT NULL,
                                        spec_value VARCHAR(80) NOT NULL,
                                        PRIMARY KEY (product_id, spec_key),
                                        CONSTRAINT fk_product_specifications_product
                                            FOREIGN KEY (product_id)
                                                REFERENCES products(id)
                                                ON DELETE CASCADE
);

-- Índices para mejor performance
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_brand ON products(brand);
CREATE INDEX idx_products_rating ON products(rating);
CREATE INDEX idx_products_price ON products(price);