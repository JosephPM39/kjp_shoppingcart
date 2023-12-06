# Shopping Cart API with Spring Boot

## Datos del estudiante:
* Estudiante: José Josias Padilla Martínez
* Email: josias.jjpm49@gmail.com

## Instalación
Este proyecto hace uso de docker para las dependencias, por lo cual, tendrá que instalar en su maquina local, docker y hacer uso del docker-compose
De igual forma, si no quieres instalar java en tu maquina, puedes usar el Dockerfile para generar un contenedor con el proyecto, de igual forma
puedes usarlo como un devcontainer en VSCode e Intellij IDEA Ultimate.

**Pasos**
* Descargar el repo
* Iniciar las dependencias con docker compose up -d
* Crea la siguiente red global con "docker network create --driver=bridge global" (Solo para dockerizar la api, usando el Dockerfile)
* Abre en tu navegador keycloak web en: http://localhost:8081/
* Importa en keycloak el siguiente realm: [real-export spring-boot-dev.json](https://raw.githubusercontent.com/JosephPM39/kjp_shoppingcart/master/storage/keycloak/realm-export%20spring-boot-dev.json)
* Puedes configurar un par de usuarios, y asignales los roles "admin_client" y "user_client"
* Genera el client-secret en keycloak y pegalo en las application.properties en "app.auth-client.client-secret=YOUR_SECRET" y  "app.auth-client.client-secret=YOUR_SECRET"
* Configura las propiedades en (application.properties) "app.auth-client.user-console=YOUR_USER" y "app.auth-client.password-console=YOUR_PASSWORD"
  para hacer uso de la consola de keycloak
* Descarga la siguiente colección de postman para que puedas probar la api: [Postman API Collection](https://www.postman.com/maintenance-architect-42550734/workspace/josephpm39-public-workspace/collection/18993824-97066f9e-ca9c-4637-93d5-098c811510e7)

Consideraciones:
* En caso que al abrir el devcontainer ya sea con vscode o Intellij, el archivo pom.xml no reconoce
  las dependencias (Dependency not found), recargue el proyecto entero con Maven Reload Project
* Este proyecto hace uso de imágenes docker en una red global, deberá crearla con el siguiente
  comando "docker network create --driver=bridge global"

## Dependencias usadas para el Stack
* Keycloak
* Postgres
* Pgadmin
* Java 17 Amazon Corretto

## Requerimientos que resuelve la API

1. Endpoints de autenticación (registro, inicio de sesión, cierre de sesión) y todos los procesos y flujos de trabajo relacionados a autenticación
   (puedes usar JWT y/o un resource server)
2. Listar los productos de nuestro catalogo de productos. Ten en cuenta que pueden ser muchos productos, cientos o miles, por lo que necesitaremos paginar.
3. Busque productos del catálogo por categoría, nombre o palabras clave
4. Tenemos dos tipos de usuarios: Administrador y Comprador:

a. Como Administrador puedo:
+Crear una nueva categoría del catálogo
+Modificar una categoría del catálogo
+Deshabilitar una categoría del catálogo
+Crear productos
+Actualizar productos
+Eliminar productos
+Deshabilitar productos
+Mostrar pedidos de compradores
+Deshabilitar compradores
+Forzar la cancelación de pedidos

b. Como Comprador puedo:
+Ver productos
+Ver los detalles del producto
+Comprar productos
+Agregar productos al carrito de compras
+Escribir referencias de productos y/o darle like o dislike
+Mostrar mi pedido
+Cancelar un pedido

## Diagrama de la base de datos
![image](https://raw.githubusercontent.com/JosephPM39/kjp_shoppingcart/master/images-readme/KJP_ShoppingCart-DB_Desing.jpg)

## Code Coverage

### Intellij Idea
![image](https://raw.githubusercontent.com/JosephPM39/kjp_shoppingcart/master/images-readme/code-coverage-intellij.png)

### Jacoco
![image](https://raw.githubusercontent.com/JosephPM39/kjp_shoppingcart/master/images-readme/code-coverage-jacoco.png)