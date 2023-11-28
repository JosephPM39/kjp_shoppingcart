FROM ubuntu:20.04

RUN apt-get update

RUN apt-get install -y nocache curl git wget gpg socat

RUN mkdir -p /workspace/kjp_shoppingcart

RUN wget -O - https://apt.corretto.aws/corretto.key | gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list

RUN apt-get update && apt-get install -y java-17-amazon-corretto-jdk

WORKDIR /IdeaProjects/kjp_shoppingcart

RUN chmod -R 777 /workspace/kjp_shoppingcart
RUN chmod -R 777 /IdeaProjects/kjp_shoppingcart


VOLUME /workspace/kjp_shoppingcart
VOLUME /IdeaProjects/kjp_shoppingcart

COPY . .

EXPOSE 8080

CMD [ "mvnw" ]
ENTRYPOINT socat TCP-LISTEN:8081,fork,reuseaddr TCP:keycloak:8081 & \
           socat TCP-LISTEN:5432,fork,reuseaddr TCP:shoppingcart_db:5432