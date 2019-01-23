FROM fwchen/alpine-lein-node

WORKDIR /app

COPY . ./

RUN ./scripts/nbuild

