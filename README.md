# Sobre
    Link da aplicação : http://jhipsterfiap.ddns.net:8080
# J-Hipster project

  > <p> Recursos em nuvem <br>
    - AWS RDS 
    - AWS GATEWAY API
    - AWS EC2
   </p>
    
 > <p> Backend <br>
    - WebFlux
    - JWT e oauth2
   </p>
  
 > <p>Front-end/tests<br>
    - ReactJs
    - Cypress
   </p>
    

  
# Exemplo de utilização da API
    
    # Autentificação

    - Endpoint http://jhipsterfiap.ddns.net:8080/api/authenticate
    - Utilizar o metodo [POST]
    - Body
    
        {
            "username":"user",
            "password":"user"
        }
        
        
     # Requisição Get para obter os sensores
     
     - Endpoint http://jhipsterfiap.ddns.net:8080/api/sensors
     - Metodo [GET]
         
 
 # Como inicializar o projeto ?
 
 > 1 - Clonar o repositorio <br>
 > 2 - Configure o seu banco de dados nas configurações de propriedades da aplicação <br>
 > 3 - Inicialize o maven <br>
    > Se quiser rodar com perfil diferenciado de properties <br> 
        > ./mvn -Dspring.profiles.active={nome do propertie} <br>
 
 # Oque foi configurado na ec2 ?
 
- Configurações de variaveis de ambiente do java
         sudo apt install openjdk-11-jre-headless -y
         export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64     
    
- Instalação do nodejs (Caso use linux)
         sudo apt install curl
         curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | bash
         source ~/.nvm/nvm.sh
         nvm install 12.18.3
         sudo apt install npm
    
- Instalação do JHipster
    npm install -g generator-jhipster
   
- Instalação do Docker(Caso use linux)
     curl -fsSL https://get.docker.com -o get-docker.sh
     sudo sh get-docker.sh
     sudo apt-get install docker-compose -y
     docker-compose -f src/main/docker/keycloak.yml up
 
