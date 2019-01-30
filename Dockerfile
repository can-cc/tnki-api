FROM fwchen/alpine-lein-node

WORKDIR /app

COPY . ./

RUN lein npm install

RUN ./scripts/nbuild

CMD [ "./tnki-server" ]