-- =============================================
-- INICIALIZACIÓN DE DATOS POR DEFECTO
-- =============================================

-- 1. INSERTAR PERMISOS SI NO EXISTEN
INSERT INTO permissions (nombre)
SELECT 'CREATE'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE nombre = 'CREATE');

INSERT INTO permissions (nombre)
SELECT 'READ'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE nombre = 'READ');

INSERT INTO permissions (nombre)
SELECT 'UPDATE'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE nombre = 'UPDATE');

INSERT INTO permissions (nombre)
SELECT 'DELETE'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE nombre = 'DELETE');

INSERT INTO permissions (nombre)
SELECT 'PAYMENT_PROCESS'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE nombre = 'PAYMENT_PROCESS');

INSERT INTO permissions (nombre)
SELECT 'CART_MANAGE'
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE nombre = 'CART_MANAGE');

-- 2. INSERTAR ROLES SI NO EXISTEN
INSERT INTO roles (role_nombre)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_nombre = 'ADMIN');

INSERT INTO roles (role_nombre)
SELECT 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_nombre = 'USER');

INSERT INTO roles (role_nombre)
SELECT 'INVITED'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_nombre = 'INVITED');

-- 3. ASIGNAR PERMISOS A ROL ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.role_nombre = 'ADMIN'
  AND p.nombre IN ('CREATE', 'READ', 'UPDATE', 'DELETE', 'PAYMENT_PROCESS', 'CART_MANAGE')
  AND NOT EXISTS (SELECT 1 FROM role_permissions WHERE role_id = r.id AND permission_id = p.id);

-- 4. ASIGNAR PERMISOS A ROL USER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.role_nombre = 'USER'
  AND p.nombre IN ('READ', 'PAYMENT_PROCESS', 'CART_MANAGE')
  AND NOT EXISTS (SELECT 1 FROM role_permissions WHERE role_id = r.id AND permission_id = p.id);

-- 5. ASIGNAR PERMISOS A ROL INVITED
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.role_nombre = 'INVITED'
  AND p.nombre = 'READ'
  AND NOT EXISTS (SELECT 1 FROM role_permissions WHERE role_id = r.id AND permission_id = p.id);

-- 6. INSERTAR CATEGORÍAS DE PLATOS
INSERT INTO categoria_plato (nombre_categoria)
SELECT 'Entradas'
WHERE NOT EXISTS (SELECT 1 FROM categoria_plato WHERE nombre_categoria = 'Entradas');

INSERT INTO categoria_plato (nombre_categoria)
SELECT 'Comida Rapida'
WHERE NOT EXISTS (SELECT 1 FROM categoria_plato WHERE nombre_categoria = 'Comida Rapida');

INSERT INTO categoria_plato (nombre_categoria)
SELECT 'Platos principales'
WHERE NOT EXISTS (SELECT 1 FROM categoria_plato WHERE nombre_categoria = 'Plato Fuerte');

INSERT INTO categoria_plato (nombre_categoria)
SELECT 'Postres'
WHERE NOT EXISTS (SELECT 1 FROM categoria_plato WHERE nombre_categoria = 'Postres');

INSERT INTO categoria_plato (nombre_categoria)
SELECT 'Bebidas'
WHERE NOT EXISTS (SELECT 1 FROM categoria_plato WHERE nombre_categoria = 'Bebidas');

-- 7. RESETEAR SECUENCIAS
SELECT setval('categoria_plato_id_seq', COALESCE((SELECT MAX(id) FROM categoria_plato), 1), false);
SELECT setval('permissions_id_seq', COALESCE((SELECT MAX(id) FROM permissions), 1), false);
SELECT setval('roles_id_seq', COALESCE((SELECT MAX(id) FROM roles), 1), false);