FROM node:18-alpine

RUN npm install -g @mockoon/cli@6.1.0
COPY ./mock /mockoon

RUN apk --no-cache add curl tzdata

RUN adduser --shell /bin/sh --disabled-password --gecos "" mockoon
RUN chown -R mock /mock
USER mockoon

EXPOSE 8080

ENTRYPOINT ["mockoon-cli","start","--disable-log-to-file","--data","/mock/produto.json","--port","8080"]