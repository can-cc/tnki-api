FROM naartjie/alpine-lein-node
VOLUME /root/.m2
RUN mkdir /src
RUN node -v

ADD . /src/tnki-api

RUN rm -rf /src/tnki-api/node_modules

WORKDIR /src/tnki-api
        
RUN lein npm install
