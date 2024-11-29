FROM node:18-alpine

RUN npm install -g @mockoon/cli@9.0.0
COPY ./mock /mockoon

RUN apk --no-cache add curl tzdata

RUN adduser --shell /bin/sh --disabled-password --gecos "" mockoon
RUN chown -R mockoon /mockoon
USER mockoon

EXPOSE 8080

ENTRYPOINT ["mockoon-cli","start","--disable-log-to-file","--data","/mockoon/produto.json","--port","8080"]