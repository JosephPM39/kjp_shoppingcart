* En caso que al abrir el devcontainer ya sea con vscode o Intellij, el archivo pom.xml no reconoce 
  las dependencias (Dependency not found), recargue el proyecto entero con Maven Reload Project
* Este proyecto hace uso de imágenes docker en una red global, deberá crearla con el siguiente
  comando "docker network create --driver=bridge global"