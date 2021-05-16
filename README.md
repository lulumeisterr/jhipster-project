# Sobre
    
    Projeto voltado para testar a construção de aplicações web rapidamente utlizando J-hipster , pensando em reduzir
    o retrabalho de configurações de projeto , evitar repetições de cruds etc..

    Link da aplicação : http://jhipsterfiap.ddns.net:8080
# J-Hipster project

  > <p> Recursos em nuvem <br>
    - AWS RDS Mysql
    - AWS GATEWAY API
    - AWS EC2
   </p>
    
 > <p> Backend <br>
    - Spring WebFlux
    - Spring Security (JWT)
   </p>
  
 > <p>Front-end/tests<br>
    - ReactJs
    - Cypress
   </p>
    

  
# Exemplo de utilização da API - GATEWAY URLS:

    # Autentificação
    
     Endpoint :  https://v0mw8khzu3.execute-api.us-east-1.amazonaws.com/prod/api/authenticate
   
    - Utilizar o metodo [POST]
    - Body
    
        {
            "username":"admin",
            "password":"admin"
        }
        
     # Requisição Get para obter os sensores
     
     > Endpoint : https://v0mw8khzu3.execute-api.us-east-1.amazonaws.com/prod/api/sensors
     - Metodo [GET]
     - Headers
        Authorization: Bearer ---TOKENX-----
         
 # Como inicializar o projeto ?
 
 > 1 - Clonar o repositorio <br>
 > 2 - Configure o seu banco de dados utilizando as propriedades da aplicação <br>
 > 3 - Inicialize o KeyClock (Defina o profile de dev na hora de inicializar o maven caso for rodar local)
         
        docker-compose -f /src/main/docker/keyclock.yml up -d
         
 > 3 - Inicialize a aplicação <br>
     
       ./mvn -Dspring.profiles.active={nome do propertie}
 
 # O'que foi configurado na ec2 ?
 
- Configurações de variaveis de ambiente do java <br>  
         sudo apt install openjdk-11-jre-headless -y <br>
         export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64  <br>
    
- Instalação do nodejs (Caso use linux)<br>
         sudo apt install curl
         curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | bash <br>
         source ~/.nvm/nvm.sh <br>
         nvm install 12.18.3 <br>
         sudo apt install npm 
    
- Instalação do JHipster <br>  

    npm install -g generator-jhipster <br>
    jhpister (Configuração do framework) <br>
    jhpister --with-entities (Importando entidades)
   
- Instalação do Docker(Caso use linux) <br>  
     curl -fsSL https://get.docker.com -o get-docker.sh <br>
     sudo sh get-docker.sh <br>
     sudo apt-get install docker-compose -y <br> 
     docker-compose -f src/main/docker/keycloak.yml up <br>
 
 
 - Link para demonstração
 
    https://www.loom.com/share/19ae6a31a2234d8891ac1452e212447d
