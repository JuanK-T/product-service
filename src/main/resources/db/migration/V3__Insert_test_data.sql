-- Datos de prueba para productos
INSERT INTO products (id, name, image_url, description, price, currency, rating, category, brand) VALUES
                                                                                                      ('prod001', 'Smartphone Galaxy XZ', 'https://example.com/images/smartphone-xz.jpg', 'Teléfono inteligente de gama alta con pantalla AMOLED y cámara triple.', 899.99, 'USD', 4.7, 'Electrónica', 'TechNova'),
                                                                                                      ('prod002', 'Laptop UltraBook Pro 15', 'https://example.com/images/laptop-ultrabook.jpg', 'Laptop delgada y potente ideal para productividad y edición multimedia.', 1349.50, 'USD', 4.5, 'Computadores', 'NeoTech'),
                                                                                                      ('prod003', 'Auriculares NoiseFree 300', 'https://example.com/images/auriculares-noisefree.jpg', 'Auriculares inalámbricos con cancelación activa de ruido y autonomía extendida.', 249.99, 'USD', 4.8, 'Audio', 'Soundify'),
                                                                                                      ('prod004', 'Smart TV 55" 4K UHD', 'https://example.com/images/smart-tv-55.jpg', 'Televisor inteligente con resolución 4K y sistema de sonido Dolby Atmos', 599.99, 'USD', 4.3, 'Electrodomésticos', 'VisionPlus'),
                                                                                                      ('prod005', 'Zapatillas Running Ultra', 'https://example.com/images/zapatillas-running.jpg', 'Zapatillas deportivas para running con tecnología de amortiguación avanzada', 129.99, 'USD', 4.6, 'Deportes', 'RunMax'),
                                                                                                      ('prod006', 'Cámara Mirrorless Alpha', 'https://example.com/images/camara-mirrorless.jpg', 'Cámara mirrorless profesional con sensor full frame y grabación 4K', 1899.99, 'USD', 4.9, 'Fotografía', 'FotoPro'),
                                                                                                      ('prod007', 'Tablet Pro 12.9"', 'https://example.com/images/tablet-pro.jpg', 'Tablet profesional con pantalla Liquid Retina y chip M2', 1099.00, 'USD', 4.7, 'Tablets', 'iTech'),
                                                                                                      ('prod008', 'Reloj Inteligente FitPro', 'https://example.com/images/reloj-inteligente.jpg', 'Smartwatch con monitorización avanzada de salud y deporte', 199.99, 'USD', 4.4, 'Wearables', 'FitTech'),
                                                                                                      ('prod009', 'Consola de Videojuegos NextGen', 'https://example.com/images/consola-nextgen.jpg', 'Consola de última generación con gráficos 8K y SSD ultrarrápido', 499.99, 'USD', 4.8, 'Videojuegos', 'GameMax'),
                                                                                                      ('prod010', 'Mochila Antirrobo Urbana', 'https://example.com/images/mochila-antirrobo.jpg', 'Mochila segura con compartimentos anti-corte y USB integrado', 79.99, 'USD', 4.2, 'Accesorios', 'UrbanSafe')
    ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
                            image_url = EXCLUDED.image_url,
                            description = EXCLUDED.description,
                            price = EXCLUDED.price,
                            currency = EXCLUDED.currency,
                            rating = EXCLUDED.rating,
                            category = EXCLUDED.category,
                            brand = EXCLUDED.brand,
                            updated_at = CURRENT_TIMESTAMP;

-- Especificaciones para prod001
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod001', 'pantalla', '6.5 pulgadas AMOLED'),
                                                                          ('prod001', 'procesador', 'Snapdragon 8 Gen 2'),
                                                                          ('prod001', 'memoria', '256GB'),
                                                                          ('prod001', 'camara', '108MP + 12MP + 5MP'),
                                                                          ('prod001', 'bateria', '4800mAh')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod002
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod002', 'pantalla', '15.6 pulgadas Full HD'),
                                                                          ('prod002', 'procesador', 'Intel Core i7 13th Gen'),
                                                                          ('prod002', 'ram', '16GB DDR5'),
                                                                          ('prod002', 'almacenamiento', '1TB SSD'),
                                                                          ('prod002', 'grafica', 'NVIDIA RTX 3050')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod003
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod003', 'conectividad', 'Bluetooth 5.3'),
                                                                          ('prod003', 'autonomia', '30 horas'),
                                                                          ('prod003', 'cancelacion_ruido', 'Activa híbrida'),
                                                                          ('prod003', 'resistencia', 'IPX5'),
                                                                          ('prod003', 'peso', '220g')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod004
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod004', 'pantalla', '55 pulgadas'),
                                                                          ('prod004', 'resolucion', '4K UHD'),
                                                                          ('prod004', 'sistema_operativo', 'Android TV'),
                                                                          ('prod004', 'puertos', '4 HDMI, 3 USB'),
                                                                          ('prod004', 'conectividad', 'WiFi, Bluetooth')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod005
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod005', 'material', 'Malla transpirable'),
                                                                          ('prod005', 'suela', 'EVA de alta densidad'),
                                                                          ('prod005', 'tallas_disponibles', '38-46'),
                                                                          ('prod005', 'peso', '280g'),
                                                                          ('prod005', 'color', 'Negro/Rojo')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod006
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod006', 'sensor', 'Full Frame 35mm'),
                                                                          ('prod006', 'resolucion', '45MP'),
                                                                          ('prod006', 'video', '4K 60fps'),
                                                                          ('prod006', 'conectividad', 'WiFi, Bluetooth'),
                                                                          ('prod006', 'lente_incluido', '24-70mm f/2.8')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod007
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod007', 'pantalla', '12.9" Liquid Retina'),
                                                                          ('prod007', 'procesador', 'Chip M2'),
                                                                          ('prod007', 'almacenamiento', '256GB'),
                                                                          ('prod007', 'camara', '12MP + 10MP'),
                                                                          ('prod007', 'bateria', 'Hasta 10 horas')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod008
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod008', 'pantalla', '1.4" AMOLED'),
                                                                          ('prod008', 'resistencia', '5ATM'),
                                                                          ('prod008', 'sensores', 'Cardíaco, SpO2, GPS'),
                                                                          ('prod008', 'bateria', '7 días'),
                                                                          ('prod008', 'compatibilidad', 'Android, iOS')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod009
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod009', 'procesador', 'CPU Zen 2 personalizado'),
                                                                          ('prod009', 'graficos', 'GPU RDNA 2'),
                                                                          ('prod009', 'almacenamiento', '1TB SSD'),
                                                                          ('prod009', 'resolucion', 'Hasta 8K'),
                                                                          ('prod009', 'incluye', '1 control inalámbrico')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;

-- Especificaciones para prod010
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
                                                                          ('prod010', 'capacidad', '25L'),
                                                                          ('prod010', 'material', 'Poliéster resistente'),
                                                                          ('prod010', 'compartimentos', 'Para laptop 15"'),
                                                                          ('prod010', 'caracteristicas', 'Anti-corte, USB integrado'),
                                                                          ('prod010', 'color', 'Negro, Azul, Gris')
    ON CONFLICT (product_id, spec_key) DO UPDATE SET
    spec_value = EXCLUDED.spec_value;