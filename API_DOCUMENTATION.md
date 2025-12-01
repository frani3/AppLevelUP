# LevelUp Backend - Documentación de API

**Base URL:** `https://<host>/api/v1`

**Autenticación:** La mayoría de endpoints requieren token JWT en el header:
```
Authorization: Bearer <token>
```

---

## Índice

1. [Autenticación](#autenticación)
2. [Usuarios](#usuarios)
3. [Productos](#productos)
4. [Categorías](#categorías)
5. [Carrito](#carrito)
6. [Pedidos](#pedidos)
7. [Direcciones](#direcciones)
8. [Regiones](#regiones)
9. [LevelUp (Puntos y Referidos)](#levelup-puntos-y-referidos)
10. [Mensajes de Contacto](#mensajes-de-contacto)
11. [Auditoría](#auditoría)

---

## Autenticación

### POST `/auth/login`
Inicia sesión y obtiene token JWT.

**🔓 No requiere autenticación**

**Request:**
```json
{
  "correo": "usuario@email.com",
  "password": "MiPassword123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "user": {
    "run": "12345678-9",
    "nombre": "Juan",
    "apellidos": "Pérez García",
    "correo": "usuario@email.com",
    "perfil": "Cliente",
    "fechaNacimiento": "1990-05-21",
    "region": "Metropolitana de Santiago",
    "comuna": "Santiago Centro",
    "direccion": "Av. Principal 123",
    "descuentoVitalicio": false,
    "systemAccount": false,
    "referralCode": "JUAN1234",
    "levelUpStats": {
      "run": "12345678-9",
      "points": 150,
      "exp": { "current": 150, "nextLevel": 1000, "level": 1 },
      "referralCode": "JUAN1234",
      "referredBy": null,
      "referidos": { "count": 2, "users": [] },
      "updatedAt": "2025-12-01T10:00:00"
    }
  }
}
```

**Errores:**
| HTTP | Mensaje |
|------|---------|
| 401 | Credenciales inválidas |

---

### POST `/auth/register`
Registra un nuevo usuario.

**🔓 No requiere autenticación**

**Request:**
```json
{
  "run": "12345678-9",
  "nombre": "Juan",
  "apellidos": "Pérez García",
  "correo": "nuevo@email.com",
  "fechaNacimiento": "1990-05-21",
  "region": "Metropolitana de Santiago",
  "comuna": "Santiago Centro",
  "direccion": "Av. Principal 123",
  "password": "MiPassword123",
  "referralCode": "CODIGO123"
}
```

| Campo | Tipo | Requerido | Validación |
|-------|------|-----------|------------|
| run | string | ✅ | Formato RUN chileno (7-8 dígitos + dígito verificador) |
| nombre | string | ✅ | |
| apellidos | string | ✅ | |
| correo | string | ✅ | Email válido |
| fechaNacimiento | string | ❌ | Formato `yyyy-MM-dd` |
| region | string | ✅ | |
| comuna | string | ✅ | |
| direccion | string | ✅ | |
| password | string | ✅ | Mínimo 8 caracteres |
| referralCode | string | ❌ | Código de referido |

**Response:** `201 Created`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "user": { ... }
}
```

---

## Usuarios

### GET `/users/me`
Obtiene el perfil del usuario autenticado.

**🔒 Requiere autenticación**

**Response:** `200 OK`
```json
{
  "run": "12345678-9",
  "nombre": "Juan",
  "apellidos": "Pérez García",
  "correo": "usuario@email.com",
  "perfil": "Cliente",
  "fechaNacimiento": "1990-05-21",
  "region": "Metropolitana de Santiago",
  "comuna": "Santiago Centro",
  "direccion": "Av. Principal 123",
  "descuentoVitalicio": false,
  "systemAccount": false,
  "referralCode": "JUAN1234",
  "levelUpStats": { ... }
}
```

---

### GET `/users`
Lista usuarios (solo admin).

**🔒 Requiere rol ADMINISTRADOR o SUPERADMIN**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| activo | boolean | Filtrar por estado activo/inactivo |
| region | string | Filtrar por región |
| search | string | Buscar por nombre, apellidos, correo o RUN |
| sortBy | string | Campo de ordenamiento: `run`, `nombre`, `apellidos`, `correo`, `region`, `comuna`, `createdAt`, `updatedAt` |
| order | string | `asc` o `desc` |
| page | int | Página (desde 1) |
| limit | int | Resultados por página (máx 100) |

**Response:** `200 OK`
```json
{
  "data": [
    {
      "run": "12345678-9",
      "correo": "usuario@email.com",
      "nombre": "Juan",
      "apellidos": "Pérez",
      "telefono": null,
      "direccion": "Av. Principal 123",
      "region": "Metropolitana de Santiago",
      "comuna": "Santiago Centro",
      "nivel": 1,
      "activo": true,
      "fechaRegistro": "2025-11-01T10:00:00",
      "fechaNacimiento": "1990-05-21",
      "roles": ["Cliente"],
      "ultimoIngreso": null,
      "puntosLevelUp": 150,
      "perfil": "Cliente",
      "referidos": { "count": 2, "users": [] }
    }
  ],
  "meta": {
    "page": 1,
    "limit": 25,
    "total": 100
  }
}
```

---

## Productos

### GET `/products`
Lista productos.

**🔓 No requiere autenticación**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| includeDeleted | boolean | Incluir productos eliminados (default: false) |
| category | string | Filtrar por categoría |
| query | string | Buscar por nombre o código |

**Response:** `200 OK`
```json
[
  {
    "codigo": "PROD-001",
    "nombre": "Producto Ejemplo",
    "descripcion": "Descripción del producto",
    "categoria": "Electrónica",
    "fabricante": "Marca XYZ",
    "distribuidor": "Distribuidor ABC",
    "precio": 29990.00,
    "stock": 50,
    "stockCritico": 10,
    "imagenUrl": "https://ejemplo.com/imagen.jpg",
    "eliminado": false,
    "deletedAt": null,
    "createdAt": "2025-11-01T10:00:00",
    "updatedAt": "2025-11-15T14:30:00"
  }
]
```

---

### GET `/products/{codigo}`
Obtiene un producto por código.

**🔓 No requiere autenticación**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| includeDeleted | boolean | Incluir si está eliminado |

**Response:** `200 OK`
```json
{
  "codigo": "PROD-001",
  "nombre": "Producto Ejemplo",
  "descripcion": "Descripción del producto",
  "categoria": "Electrónica",
  "fabricante": "Marca XYZ",
  "distribuidor": "Distribuidor ABC",
  "precio": 29990.00,
  "stock": 50,
  "stockCritico": 10,
  "imagenUrl": "https://ejemplo.com/imagen.jpg",
  "eliminado": false,
  "deletedAt": null,
  "createdAt": "2025-11-01T10:00:00",
  "updatedAt": "2025-11-15T14:30:00"
}
```

**Errores:**
| HTTP | Mensaje |
|------|---------|
| 404 | Producto no encontrado |

---

### POST `/products`
Crea un producto.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "codigo": "PROD-002",
  "nombre": "Nuevo Producto",
  "descripcion": "Descripción opcional",
  "categoria": "Electrónica",
  "fabricante": "Marca XYZ",
  "distribuidor": "Distribuidor ABC",
  "precio": 19990.00,
  "stock": 100,
  "stockCritico": 15,
  "imagenUrl": "https://ejemplo.com/imagen.jpg"
}
```

| Campo | Tipo | Requerido | Validación |
|-------|------|-----------|------------|
| codigo | string | ❌ | Se genera automáticamente si no se envía |
| nombre | string | ✅ | |
| categoria | string | ✅ | |
| precio | decimal | ✅ | ≥ 0 |
| stock | int | ❌ | ≥ 0 |
| stockCritico | int | ❌ | ≥ 0 |

**Response:** `201 Created`
```json
{
  "codigo": "PROD-002",
  "nombre": "Nuevo Producto",
  ...
}
```

---

### PUT `/products/{codigo}`
Actualiza un producto.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "nombre": "Producto Actualizado",
  "descripcion": "Nueva descripción",
  "categoria": "Electrónica",
  "fabricante": "Nueva Marca",
  "distribuidor": "Nuevo Distribuidor",
  "precio": 24990.00,
  "stock": 75,
  "stockCritico": 20,
  "imagenUrl": "https://ejemplo.com/nueva-imagen.jpg"
}
```

**Response:** `200 OK`

---

### DELETE `/products/{codigo}`
Elimina (soft delete) un producto.

**🔒 Requiere autenticación**

**Response:** `200 OK`

---

### POST `/products/{codigo}/restore`
Restaura un producto eliminado.

**🔒 Requiere autenticación**

**Response:** `200 OK`

---

## Categorías

### GET `/categories`
Lista categorías.

**🔓 No requiere autenticación**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| includeDeleted | boolean | Incluir eliminadas (default: false) |

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Electrónica",
    "eliminada": false,
    "createdAt": "2025-11-01T10:00:00",
    "updatedAt": "2025-11-01T10:00:00"
  }
]
```

---

### POST `/categories`
Crea una categoría.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "nombre": "Nueva Categoría"
}
```

**Response:** `201 Created`
```json
{
  "id": 5,
  "nombre": "Nueva Categoría",
  "eliminada": false,
  "createdAt": "2025-12-01T12:00:00",
  "updatedAt": "2025-12-01T12:00:00"
}
```

---

### PUT `/categories`
Sincroniza categorías (reemplaza todas).

**🔒 Requiere autenticación**

**Request:**
```json
{
  "nombres": ["Electrónica", "Ropa", "Hogar"]
}
```

**Response:** `200 OK`
```json
[
  { "id": 1, "nombre": "Electrónica", "eliminada": false, ... },
  { "id": 2, "nombre": "Ropa", "eliminada": false, ... },
  { "id": 3, "nombre": "Hogar", "eliminada": false, ... }
]
```

---

## Carrito

### GET `/carts/me`
Obtiene el carrito del usuario autenticado.

**🔒 Requiere autenticación**

**Response:** `200 OK`
```json
{
  "userRun": "12345678-9",
  "items": [
    { "productCode": "PROD-001", "quantity": 2 },
    { "productCode": "PROD-002", "quantity": 1 }
  ],
  "totalQuantity": 3,
  "updatedAt": "2025-12-01T15:30:00"
}
```

---

### PUT `/carts/me`
Reemplaza el contenido del carrito.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "items": [
    { "productCode": "PROD-001", "quantity": 2 },
    { "productCode": "PROD-002", "quantity": 1 }
  ],
  "forceReplace": false
}
```

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| items | array | ✅ | Lista de productos (máx 50) |
| items[].productCode | string | ✅ | Código del producto |
| items[].quantity | int | ✅ | Cantidad (mín 1) |
| forceReplace | boolean | ❌ | Si `true` y items vacío, limpia el carrito |

**Response:** `200 OK`
```json
{
  "userRun": "12345678-9",
  "items": [
    { "productCode": "PROD-001", "quantity": 2 },
    { "productCode": "PROD-002", "quantity": 1 }
  ],
  "totalQuantity": 3,
  "updatedAt": "2025-12-01T15:30:00"
}
```

**Errores:**
| HTTP | Mensaje |
|------|---------|
| 400 | Producto inválido: PROD-XXX |
| 400 | Stock insuficiente para el producto PROD-XXX |
| 400 | Producto duplicado en el carrito: PROD-XXX |
| 400 | El carrito no puede exceder 50 unidades en total |
| 401 | No autenticado |

---

### DELETE `/carts/me`
Vacía el carrito.

**🔒 Requiere autenticación**

**Response:** `204 No Content`

---

## Pedidos

### GET `/orders`
Lista pedidos.

**🔒 Requiere autenticación**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| userEmail | string | Filtrar por email del usuario |
| status | string | Filtrar por estado: `Pagado`, `Pendiente`, `Cancelado` |
| paymentMethod | string | Filtrar por método: `tarjeta`, `transferencia`, `efectivo` |
| from | datetime | Fecha desde (ISO 8601) |
| to | datetime | Fecha hasta (ISO 8601) |
| includeDeleted | boolean | Incluir eliminados |

**Response:** `200 OK`
```json
[
  {
    "id": "ORD-20251201-001",
    "userEmail": "cliente@email.com",
    "userName": "Juan Pérez",
    "total": 59980.00,
    "paymentMethod": "tarjeta",
    "status": "Pendiente",
    "direccion": "Av. Principal 123",
    "region": "Metropolitana de Santiago",
    "comuna": "Santiago Centro",
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00",
    "deletedAt": null,
    "items": [
      {
        "codigo": "PROD-001",
        "nombre": "Producto Ejemplo",
        "cantidad": 2,
        "precioUnitario": 29990.00
      }
    ]
  }
]
```

---

### POST `/orders`
Crea un pedido.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "items": [
    {
      "codigo": "PROD-001",
      "nombre": "Producto Ejemplo",
      "cantidad": 2,
      "precioUnitario": 29990.00
    }
  ],
  "paymentMethod": "tarjeta",
  "direccion": "Av. Principal 123",
  "region": "Metropolitana de Santiago",
  "comuna": "Santiago Centro"
}
```

| Campo | Tipo | Requerido |
|-------|------|-----------|
| items | array | ✅ |
| items[].codigo | string | ✅ |
| items[].nombre | string | ✅ |
| items[].cantidad | int | ✅ (mín 1) |
| items[].precioUnitario | decimal | ✅ (≥ 0) |
| paymentMethod | string | ❌ |
| direccion | string | ✅ |
| region | string | ❌ |
| comuna | string | ❌ |

**Response:** `201 Created`
```json
{
  "id": "ORD-20251201-002",
  "userEmail": "cliente@email.com",
  "userName": "Juan Pérez",
  "total": 59980.00,
  ...
}
```

---

### PATCH `/orders/{orderId}/status`
Actualiza el estado de un pedido.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "status": "Pagado"
}
```

Valores válidos: `Pagado`, `Pendiente`, `Cancelado`

**Response:** `200 OK`

---

### DELETE `/orders/{orderId}`
Archiva (soft delete) un pedido.

**🔒 Requiere autenticación**

**Response:** `204 No Content`

---

### POST `/orders/{orderId}/restore`
Restaura un pedido archivado.

**🔒 Requiere autenticación**

**Response:** `200 OK`

---

## Direcciones

### GET `/users/{run}/addresses`
Lista direcciones de un usuario.

**🔒 Requiere autenticación**

**Response:** `200 OK`
```json
[
  {
    "id": "addr-001",
    "fullName": "Juan Pérez",
    "line1": "Av. Principal 123",
    "city": "Santiago",
    "region": "Metropolitana",
    "country": "Chile",
    "isPrimary": true,
    "createdAt": "2025-11-01T10:00:00",
    "updatedAt": "2025-11-01T10:00:00"
  }
]
```

---

### POST `/users/{run}/addresses`
Crea una dirección.

**🔒 Requiere autenticación**

**Request:**
```json
{
  "fullName": "Juan Pérez",
  "line1": "Av. Principal 123",
  "city": "Santiago",
  "region": "Metropolitana",
  "country": "Chile",
  "isPrimary": true
}
```

**Response:** `201 Created`

---

### PUT `/users/{run}/addresses/{addressId}`
Actualiza una dirección.

**🔒 Requiere autenticación**

**Response:** `200 OK`

---

### DELETE `/users/{run}/addresses/{addressId}`
Elimina una dirección.

**🔒 Requiere autenticación**

**Response:** `200 OK` (retorna lista actualizada)

---

### POST `/users/{run}/addresses/{addressId}/primary`
Establece una dirección como principal.

**🔒 Requiere autenticación**

**Response:** `200 OK`

---

## Regiones

### GET `/regiones`
Lista regiones y comunas de Chile.

**🔓 No requiere autenticación**

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Arica y Parinacota",
    "comunas": ["Arica", "Camarones", "Putre", "General Lagos"]
  },
  {
    "id": 13,
    "nombre": "Metropolitana de Santiago",
    "comunas": ["Santiago Centro", "Providencia", "Las Condes", ...]
  }
]
```

---

## LevelUp (Puntos y Referidos)

### GET `/levelup/{run}/stats`
Obtiene estadísticas LevelUp de un usuario.

**🔒 Requiere autenticación**

**Response:** `200 OK`
```json
{
  "run": "12345678-9",
  "points": 1500,
  "exp": {
    "current": 500,
    "nextLevel": 1000,
    "level": 2
  },
  "referralCode": "JUAN1234",
  "referredBy": "MARIA5678",
  "referidos": {
    "count": 3,
    "users": [
      { "email": "ref1@email.com", "date": "2025-11-15T10:00:00" },
      { "email": "ref2@email.com", "date": "2025-11-20T14:30:00" }
    ]
  },
  "updatedAt": "2025-12-01T10:00:00"
}
```

---

### POST `/levelup/referrals`
Aplica un código de referido.

**🔓 No requiere autenticación**

**Request:**
```json
{
  "newRun": "98765432-1",
  "referralCode": "JUAN1234",
  "newEmail": "nuevo@email.com"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Referido aplicado correctamente",
  "pointsAwarded": 100
}
```

---

### POST `/levelup/purchases`
Registra puntos por compra.

**🔓 No requiere autenticación**

**Request:**
```json
{
  "run": "12345678-9",
  "totalCLP": 50000.00
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "pointsAwarded": 50,
  "newTotal": 1550
}
```

---

## Mensajes de Contacto

### GET `/messages`
Lista mensajes de contacto.

**🔒 Requiere autenticación**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| status | string | `pendiente` o `respondido` |
| query | string | Buscar en nombre, email, asunto |

**Response:** `200 OK`
```json
[
  {
    "id": "msg-001",
    "nombre": "Juan Pérez",
    "email": "juan@email.com",
    "asunto": "Consulta sobre producto",
    "mensaje": "Quisiera saber si tienen stock...",
    "status": "pendiente",
    "respuesta": null,
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00"
  }
]
```

---

### POST `/messages`
Envía un mensaje de contacto.

**🔓 No requiere autenticación**

**Request:**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@email.com",
  "asunto": "Consulta sobre producto",
  "mensaje": "Quisiera saber si tienen stock del producto PROD-001"
}
```

**Response:** `201 Created`

---

### PATCH `/messages/{id}`
Actualiza un mensaje (responder).

**🔒 Requiere autenticación**

**Request:**
```json
{
  "status": "respondido",
  "respuesta": "Sí, tenemos stock disponible..."
}
```

**Response:** `200 OK`

---

## Auditoría

### GET `/audit`
Lista eventos de auditoría.

**🔒 Requiere rol ADMINISTRADOR o SUPERADMIN**

**Query params:**
| Param | Tipo | Descripción |
|-------|------|-------------|
| action | string | Filtrar por acción |
| entityType | string | Filtrar por tipo de entidad |
| from | datetime | Fecha desde (ISO 8601) |
| to | datetime | Fecha hasta (ISO 8601) |
| limit | int | Máximo resultados (default 100, máx 750) |

**Response:** `200 OK`
```json
[
  {
    "id": 42,
    "actor": "12345678-9 (admin@levelup.com)",
    "action": "USER_UPDATE",
    "entityType": "Usuario",
    "entityId": "17",
    "summary": "Actualización de perfil",
    "severity": "MEDIUM",
    "metadata": "{\"changes\":{\"email\":\"nuevo@dominio.com\"}}",
    "createdAt": "2025-11-23T14:05:12"
  }
]
```

---

### POST `/audit`
Registra un evento de auditoría.

**🔒 Requiere rol ADMINISTRADOR o SUPERADMIN**

**Request:**
```json
{
  "action": "CUSTOM_ACTION",
  "entityType": "Entidad",
  "entityId": "123",
  "summary": "Descripción del evento",
  "severity": "HIGH",
  "metadata": {
    "key": "value"
  }
}
```

| Campo | Tipo | Requerido |
|-------|------|-----------|
| action | string | ✅ |
| severity | string | ✅ (`LOW`, `MEDIUM`, `HIGH`) |
| entityType | string | ❌ |
| entityId | string | ❌ |
| summary | string | ❌ |
| metadata | object | ❌ |

**Response:** `201 Created`

---

### DELETE `/audit`
Purga todos los eventos de auditoría.

**🔒 Requiere rol ADMINISTRADOR o SUPERADMIN**

**Response:** `204 No Content`

---

## Códigos de Error Comunes

| HTTP | Significado |
|------|-------------|
| 400 | Bad Request - Datos inválidos o faltantes |
| 401 | Unauthorized - Token faltante o inválido |
| 403 | Forbidden - Sin permisos para esta acción |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Duplicado o conflicto de datos |
| 500 | Internal Server Error |

---

## Roles de Usuario

| Rol | Descripción |
|-----|-------------|
| `Cliente` | Usuario normal |
| `Vendedor` | Puede gestionar productos |
| `Administrador` | Acceso a panel admin |
| `SUPERADMIN` | Acceso total (systemAccount=true) |

El rol viene en:
- **JWT:** claim `roles` → `["ROLE_CLIENTE"]`
- **Objeto user:** campo `perfil` → `"Cliente"`
- **Objeto user:** campo `systemAccount` → `true` si es superadmin
